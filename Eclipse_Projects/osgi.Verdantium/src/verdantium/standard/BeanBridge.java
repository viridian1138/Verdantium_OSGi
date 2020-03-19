package verdantium.standard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.ref.WeakReference;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jundo.runtime.ExtMilieuRef;
import jundo.runtime.IExtPair;
import jundo.util.pdx_JobjRef_pdx_ObjectRef;
import meta.DataFormatException;
import meta.VersionBuffer;
import meta.WrapRuntimeException;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.ProgramDirectorEvent;
import verdantium.StandardEtherEvent;
import verdantium.TransVersionBuffer;
import verdantium.TransVersionBufferFlavor;
import verdantium.UrlHolder;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.core.DefaultPropertyEditor;
import verdantium.core.DesignerControl;
import verdantium.standard.help.BeanBridgeHelp;
import verdantium.undo.UTag;
import verdantium.undo.UndoManager;
import verdantium.utils.ComponentNotFoundException;
import verdantium.utils.IOComponentNotFoundException;
import verdantium.utils.IOResourceNotFoundException;
import verdantium.utils.ResourceNotFoundException;
import verdantium.utils.VerdantiumDragUtils;
import verdantium.utils.VerdantiumDropUtils;
import verdantium.xapp.BorderObject;
import verdantium.xapp.ComponentRef;
import verdantium.xapp.DocPageFormat;
import verdantium.xapp.EditorUndoBridge;
import verdantium.xapp.JpApplicationAdapter;
import verdantium.xapp.OnlyDesignerEdits;

//$$strtCprt
/*
     Verdantium compound-document framework by Thorn Green
	Copyright (C) 2005 Thorn Green

	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
//$$endCprt

/**
*
* --- SOURCE MODIFICATION LIST ---
*
* Please document all changes to this source file here.
* Feel free to add rows if needed.
*
*
*    |-----------------------|-------------------------------------------------|----------------------------------------------------------------------|---------------------------------------------------------------...
*    | Date of Modification  |    Author of Modification                       |    Reason for Modification                                           |    Description of Modification (use multiple rows if needed)  ... 
*    |-----------------------|-------------------------------------------------|----------------------------------------------------------------------|---------------------------------------------------------------...
*    |                       |                                                 |                                                                      |
*    | 9/24/2000             | Thorn Green (viridian_1138@yahoo.com)           | Needed to provide a standard way to document source file changes.    | Added a souce modification list to the documentation so that changes to the souce could be recorded. 
*    | 10/22/2000            | Thorn Green (viridian_1138@yahoo.com)           | Methods did not have names that followed standard Java conventions.  | Performed a global modification to bring the names within spec.
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Macro support.                                                       | Started to add macro support, then thought better of it.  Reversed the changes.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/29/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for multiple PLAFs.                                          | Made code changes to support multiple PLAFs.
*    | 10/05/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for command-line arguments.                                  | Added support for command-line arguments.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
*    | 05/10/2002            | Thorn Green (viridian_1138@yahoo.com)           | Redundant information in persistent storage.                         | Made numerous persistence and packaging changes.
*    | 08/07/2004            | Thorn Green (viridian_1138@yahoo.com)           | Establish baseline for all changes in the last year.                 | Establish baseline for all changes in the last year.
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*
*
*/

/**
* BeanBridge is a bridge that should allow a verdantium component to be embedded in any app.
* that supports both JavaBeans and Java 2.  Further, through the Java Beans Bridge for Active X,
* BeanBridge should be embeddable in any app. that supports Active X embedding (e.g. MS-Word).
* There are bugs in the current version of Java 2 that prevents Java apps from working properly
* in Active X containers.  However, the code for this looks like it should work after the Java 2
* bugs are fixed.
* 
* @author Thorn Green
*/
public class BeanBridge
	extends JpApplicationAdapter
	implements MouseListener, Externalizable {

	/**
	 * Undoable reference to embedded component index.
	 */
	private pdx_JobjRef_pdx_ObjectRef myPart = null;
	
	/**
	 * The last allocated incrementing card index.
	 */
	protected int curIndex = 0;
	
	/**
	 * Layout used to switch between components by card index.
	 */
	protected final CardLayout cardL = new CardLayout();
	
	/**
	 * Inner panel on which card switching is performed.
	 */
	protected final JPanel innerPanel = new JPanel();
	
	/**
	 * Panel used when no component is embedded.
	 */
	protected final JPanel blankPanel = new JPanel();
	
	/**
	 * Weak reference used when building component references.
	 */
	protected final WeakReference<JComponent> comp = new WeakReference<JComponent>(innerPanel);
	
	/**
	 * The index to use when no comp[onent is embedded.
	 */
	protected static final String NULL_IDEX = "NULL_IDEX";
	
	/**
	 * Component reference used when no component is embedded.
	 */
	protected final ComponentRef nullRef =
		new ComponentRef(null, comp, NULL_IDEX);

	/**
	 * Gets the current embedded component.
	 * @return The current embedded component.
	 */
	protected VerdantiumComponent getMyPart() {
		return (
			((ComponentRef) (myPart.pdxm_getVal(undoMgr.getCurrentMil())))
				.get());
	}

	/**
	 * Gets the card index of the current embedded component.
	 * @return The card index of the current embedded component.
	 */
	protected String getPartIndex() {
		return (
			((ComponentRef) (myPart.pdxm_getVal(undoMgr.getCurrentMil())))
				.getIdex());
	}

	/**
	 * Sets the current component including the multi-level undo state.
	 * @param e The input event requesting the component.
	 * @throws Throwable
	 */
	protected void setMyPart(ProgramDirectorEvent e) throws Throwable {
		if (e == null) {
			ExtMilieuRef mil =
				myPart.pdxm_setVal(undoMgr.getCurrentMil(), nullRef);
			undoMgr.handleCommitTempChange(mil);
		} else {
			String idex = "a_" + curIndex;
			curIndex++;
			JPanel pan = new JPanel();
			innerPanel.add(pan, idex);
			VerdantiumComponent in = ProgramDirector.showComponent(e, pan);
			ExtMilieuRef mil =
				myPart.pdxm_setVal(
					undoMgr.getCurrentMil(),
					new ComponentRef(in, comp, idex));
			undoMgr.handleCommitTempChange(mil);
		}
		setMyPartComp();
	}

	/**
	 * Sets the current component including the multi-level undo state.
	 * @param in The component to be set.
	 * @param title The human-readable title of the component.
	 */
	protected void setMyPart(VerdantiumComponent in, String title) {
		if (in == null) {
			ExtMilieuRef mil =
				myPart.pdxm_setVal(undoMgr.getCurrentMil(), nullRef);
			undoMgr.handleCommitTempChange(mil);
		} else {
			String idex = "a_" + curIndex;
			curIndex++;
			JPanel pan = new JPanel();
			innerPanel.add(pan, idex);
			ProgramDirector.showComponent(in, pan, title);
			ExtMilieuRef mil =
				myPart.pdxm_setVal(
					undoMgr.getCurrentMil(),
					new ComponentRef(in, comp, idex));
			undoMgr.handleCommitTempChange(mil);
		}
		setMyPartComp();
	}

	/**
	 * Switches to the component at the current card index.
	 */
	protected void setMyPartComp() {
		String idex = getPartIndex();
		cardL.show(innerPanel, idex);
	}

	/**
	* Handles a program Director event to embed a component.
	* @param e The input event.
	* @return The component that was added.
	*/
	protected Object handleProgramDirectorEvent(ProgramDirectorEvent e)
		throws Throwable {
		UTag utag = new UTag();
		undoMgr.prepareForTempCommit(utag);
		setMyPart(e);
		undoMgr.commitUndoableOp(utag, "Embed Component");
		return (getMyPart());
	}

	/**
	 * Handles a change to the current undo state.
	 */
	protected void handleUndoStateChange() {
		setMyPartComp();
	}

	/**
	* Handles property change events.
	* @param evt The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == UndoManager.MODEL_STATE_CHANGED) {
			handleUndoStateChange();
		}

	}

	/**
	 * Returns the GUI of the component.
	 * @return The GUI of the component.
	 */
	public JComponent getGUI() {
		return (this);
	}

	/**
	 * Constructor.
	 */
	public BeanBridge() {
		initializeUndoManager();
		innerPanel.setLayout(cardL);
		innerPanel.add(blankPanel, NULL_IDEX);
		setLayout(new BorderLayout(0, 0));
		add(BorderLayout.CENTER, innerPanel);
		docPageFormat = new DocPageFormat(undoMgr);
		onlyDesignerEdits = new OnlyDesignerEdits(undoMgr);
		borderObject = new BorderObject(undoMgr);
		editorBridge = new EditorUndoBridge(undoMgr);
		setOpaque(false);
		setMinimumSize(new Dimension(2, 2));
		setPreferredSize(new Dimension(100, 100));
		blankPanel.addMouseListener(this);
		innerPanel.addMouseListener(this);
		addMouseListener(this);
		blankPanel.setToolTipText("Right-Click to edit properties");
		VerdantiumDragUtils.setDragUtil(blankPanel, this);
		VerdantiumDropUtils.setDropUtil(blankPanel, this, this);
		docPageFormat.configureForEtherEvents(this, PropL);
		onlyDesignerEdits.configureForEtherEvents(this, PropL);
		borderObject.configureForEtherEvents(this, PropL);
		setMyPartComp();
	}

	/**
	 * Initializes the undo manager of the component.
	 */
	protected void initializeUndoManager() {
		ExtMilieuRef mil = jundo.runtime.Runtime.getInitialMilieu();
		IExtPair pair = pdx_JobjRef_pdx_ObjectRef.pdxm_allocate_JobjRef(mil);
		mil = pair.getMilieu();
		myPart = (pdx_JobjRef_pdx_ObjectRef) (pair.getObject());
		mil = myPart.pdxm_setVal(mil, nullRef);
		undoMgr = UndoManager.createInstanceUndoManager(mil);
		undoMgr.addPropertyChangeListener(this);
	}

	/**
	 * Handles Ether Events to alter the properties of the component.
	 * @param in The event to handle.
	 * @param refcon A reference to context data for the event.
	 * @return The result of handling the event, or null if there is no result.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon)
		throws Throwable {

		Object ret = super.processObjEtherEvent(in, refcon);
		if (ret != EtherEvent.EVENT_NOT_HANDLED) {
			return (ret);
		} else {
			ret = null;
		}

		if (in instanceof ProgramDirectorEvent) {
			if (in
				.getEtherID()
				.equals(ProgramDirectorEvent.isProgramDirectorEventSupported)) {
				return (new Boolean(true));
			} else {
				return (handleProgramDirectorEvent((ProgramDirectorEvent) in));
			}

		}

		return (ret);
	}

	/**
	 * Handles a change to whether only the designer edits.
	 */
	public void handleOnlyDesignerEditsChange() {
		if (isOnlyDesignerEdits())
			blankPanel.setToolTipText(null);
		else
			blankPanel.setToolTipText("Right-Click to edit properties");
	}

	/**
	* Creates a properties editor for the BeanBridge.
	* @return The generated property editor.
	*/
	public VerdantiumPropertiesEditor makePropertiesEditor() {
		Properties MyP = new Properties();
		MyP.put("ProgramDirectorEditorName", "Embed");
		DefaultPropertyEditor MyEdit = new DefaultPropertyEditor(this, MyP);
		return (MyEdit);
	}

	/**
	* Shows the properties editor for the BeanBridge.
	* @param The input event for showing the properties editor.
	*/
	public void showPropertiesEditor(EtherEvent e) {
		VerdantiumPropertiesEditor MyEdit = makePropertiesEditor();
		ProgramDirector.showPropertyEditor(
			MyEdit,
			getGUI(),
			"Bean Bridge Property Editor");
	}

	/**
	* Handles a mouse-clicked event.
	* @param e The input event.
	*/
	public void mouseClicked(MouseEvent e) {
	}

	/**
	* Handles a mouse-entered event.
	* @param e The input event.
	*/
	public void mouseEntered(MouseEvent e) {
	}

	/**
	* Handles a mouse-exited event.
	* @param e The input event.
	*/
	public void mouseExited(MouseEvent e) {
	}

	/**
	* Handles a mouse-released event.
	* @param e The input event.
	*/
	public void mouseReleased(MouseEvent e) {
	}

	/**
	* Handles a mouse-pressed event.
	* @param e The input event.
	*/
	public void mousePressed(MouseEvent e) {
		try {
			if (((DesignerControl.isDesignTime()) || (!isOnlyDesignerEdits()))
				&& !(e.isAltDown())) {
				EtherEvent send =
					new StandardEtherEvent(
						this,
						StandardEtherEvent.showPropertiesEditor,
						null,
						this);
				ProgramDirector.fireEtherEvent(send, null);
			}
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Gets the input data flavors supported.  ContainerApp supports its own
	* proprietary format only.
	* @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{ new TransVersionBufferFlavor("Bean Bridge", "Bean Bridge")};
		return (MyF);
	}

	/**
	* Gets the output data flavors supported.  ContainerApp supports its own
	* proprietary format only.
	* @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{ new TransVersionBufferFlavor("Bean Bridge", "Bean Bridge")};
		return (MyF);
	}

	/**
	* Loads data for the component from persistent storage.
    * @param flavor The flavor in which to load the data.
    * @param trans The transferable from which to load the data.
	*/
	public void loadPersistentData(DataFlavor flavor, Transferable trans)
		throws
			IOException,
			ClassNotFoundException,
			ResourceNotFoundException,
			ComponentNotFoundException {
		if (trans instanceof UrlHolder) {
			fileSaveURL = ((UrlHolder) trans).getUrl();
			fileSaveFlavor = flavor;
		}

		if (trans == null) {
			setMyPart(null, "Embed");
			borderObject.setBorderObject(null, null, null);
			onlyDesignerEdits.setOnlyDesignerEdits(false);
			docPageFormat.setDocPageFormat(null);
			repaint();
		} else {
			try {
				TransVersionBuffer MyF = (TransVersionBuffer) trans;
				VersionBuffer.chkNul(MyF);
				Transferable part = (Transferable) (MyF.getProperty("MyPart"));

				setMyPart(null, "Embed");
				DataFlavor[] myFlavors = { null };
				if (part != null) {
					Class<? extends VerdantiumComponent> myClass =
						ProgramDirector.getComponentClass(part, myFlavors);
					String MyName = ProgramDirector.getComponentName();
					try {
						setMyPart(
							myClass.newInstance(),
							"Embed");
					} catch (Exception ex) {
						throw (
							new WrapRuntimeException(
								"Component Creation Failed",
								ex));
					}
					DataFlavor fla = myFlavors[0];
					getMyPart().loadPersistentData(fla, part);
				}
				repaint();

				borderObject.readData(MyF);

				onlyDesignerEdits.setOnlyDesignerEdits(
					MyF.getBoolean("OnlyDesignerEdits"));
			} catch (IOException ex) {
				throw (ex);
			} catch (ClassNotFoundException ex) {
				throw (ex);
			} catch (ResourceNotFoundException ex) {
				throw (ex);
			} catch (ComponentNotFoundException ex) {
				throw (ex);
			} catch (ClassCastException ex) {
				throw (new DataFormatException(ex));
			}
		}

	}

	/**
	* Saves the component to persistent storage.
    * @param The flavor in which to save the data.
    * @return Transferable to which the data is saved.
	*/
	public Transferable savePersistentData(DataFlavor flavor)
		throws IOException {
		TransVersionBuffer MyF =
			new TransVersionBuffer("Bean Bridge", "Bean Bridge");

		try {
			if (getMyPart() != null)
				MyF.setProperty(
					"MyPart",
					ProgramDirector.getSerializableState(getMyPart()));

			MyF.setBoolean("OnlyDesignerEdits", isOnlyDesignerEdits());

			borderObject.writeData(MyF);

		} catch (IOException ex) {
			throw (ex);
		}

		return (MyF);
	}

	/**
	* Writes persistent data.
	* @serialData TBD.
	*/
	public void writeExternal(ObjectOutput out) throws IOException {
		TransVersionBufferFlavor BridgeFlavor =
			new TransVersionBufferFlavor("Bean Bridge", "Bean Bridge");
		Transferable MyT = savePersistentData(BridgeFlavor);
		out.writeObject(MyT);
	}

	/**
	* Reads persistent data.
	*/
	public void readExternal(ObjectInput in)
		throws IOException, ClassNotFoundException {
		try {
			TransVersionBufferFlavor BridgeFlavor =
				new TransVersionBufferFlavor("Bean Bridge", "Bean Bridge");
			Transferable MyT = (Transferable) (in.readObject());
			VersionBuffer.chkNul(MyT);
			try {
				loadPersistentData(BridgeFlavor, MyT);
			} catch (ClassNotFoundException ex) {
				throw (ex);
			} catch (IOException ex) {
				throw (ex);
			} catch (ResourceNotFoundException ex) {
				throw (new IOResourceNotFoundException(ex));
			} catch (ComponentNotFoundException ex) {
				throw (new IOComponentNotFoundException(ex));
			}
		} catch (ClassCastException ex) {
			throw (new DataFormatException(ex));
		}
	}

	/**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		BeanBridgeHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		BeanBridge MyComp = new BeanBridge();
		ProgramDirector.showComponent(MyComp, "Bean Bridge", argv, true);
	}

	
}

