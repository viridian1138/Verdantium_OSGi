package verdantium.standard;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import jundo.runtime.ExtMilieuRef;
import meta.DataFormatException;
import meta.HighLevelList;
import meta.StdLowLevelList;
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
import verdantium.VerdantiumFlavorMap;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.core.BackgroundListener;
import verdantium.core.EditorControl;
import verdantium.core.MacroParameter;
import verdantium.core.MacroRecorder;
import verdantium.core.MacroRecorderNode;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.standard.data.pdx_HyperButtonModel_pdx_ObjectRef;
import verdantium.standard.data.pdx_HyperButtonModel_pdx_PairRef;
import verdantium.standard.help.HyperButtonHelp;
import verdantium.undo.UTag;
import verdantium.undo.UndoManager;
import verdantium.utils.ComponentNotFoundException;
import verdantium.utils.ResourceNotFoundException;
import verdantium.xapp.BackgroundState;
import verdantium.xapp.EditorUndoBridge;
import verdantium.xapp.JbApplicationAdapter;

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
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Macro support.                                                       | Added code for macro support.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/29/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for multiple PLAFs.                                          | Made code changes to support multiple PLAFs.
*    | 10/05/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for command-line arguments.                                  | Added support for command-line arguments.
*    | 11/17/2001            | Thorn Green (viridian_1138@yahoo.com)           | Documentation fixes.                                                 | Documentation fixes.
*    | 12/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Third-Cut at Error Handling.                                         | Third-Cut at Error Handling.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
*    | 03/09/2003            | Thorn Green (viridian_1138@yahoo.com)           | Needed more accessors to support PageWelder.                         | Added accessors.
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
* This is a button that executes a script
* when pressed.  For instance, one could have a button with label "Show ABC"
* that shows component "ABC" when pressed.  Or maybe it shows a TextApp
* file with a tutorial that contains component "ABC".  The default options in the property
* editor allow for the creation of a script that loads a particular document.  In order to
* have the button execute more complex scripts, use the "Open" menu item in the property
* editor to open a script saved by the macro recorder into the button.  One can also add an
* image to the button by using the "Open" menu item to open the desired image.  At some
* point in the future, a friendlier interface might be added to this component to make it
* easier to use arbitrary scripts and/or images.
* <P>
* @author Thorn Green
*/
public class HyperButton
	extends JbApplicationAdapter
	implements BackgroundListener {
	
	/**
	* The image being displayed by the component.
	*/
	transient protected Image mImage = null;

	/**
	* EtherEvent name for setting the button's label.
	*/
	public static final String setLabelText = "setLabelText";
	
	/**
	* EtherEvent name for firing the button.
	*/
	public static final String hyperButtonFire = "hyperButtonFire";
	
	/**
	* EtherEvent name for having the button create and display a new component.
	*/
	public static final String hyperButtonCreateNew = "hyperButtonCreateNew";
	
	/**
	* EtherEvent name for having the button open a file.
	*/
	public static final String hyperButtonOpenFile = "hyperButtonOpenFile";
	

	/**
	* Used in {@link #ImageMode} to indicate that no image is being displayed.
	*/
	protected static final int NO_MODE = 0;
	
	/**
	* Used in {@link #ImageMode} to indicate that a GIF is being displayed.
	*/
	protected static final int GIF_MODE = 1;
	
	/**
	* Used in {@link #ImageMode} to indicate that a JPEG is being displayed.
	*/
	protected static final int JPEG_MODE = 2;
	
	/**
	 * The data model for multi-level undo.
	 */
	protected pdx_HyperButtonModel_pdx_ObjectRef model = null;

	/**
	* Returns the GUI for the component.
	* @return The GUI for the component.
	*/
	public JComponent getGUI() {
		return (this);
	}

	/**
	* Constructs the HyperButton.
	*/
	public HyperButton() {
		super("Button");
		initializeUndoManager();
		initialize(undoMgr);
		undoMgr.addPropertyChangeListener(this);
		bkgnd = new BackgroundState(undoMgr);
		editorBridge = new EditorUndoBridge(undoMgr);
		DefaultBkgnd = super.getBackground();
		configureForEtherEvents();
		bkgnd.setBackgroundState(DefaultBkgnd, true);
		setParamVal(0);
		setResultTable(new Vector<Object>());
		setMacroList(new HighLevelList<MacroRecorderNode,StdLowLevelList<MacroRecorderNode>>());
		setImageMode(NO_MODE);
		setText(super.getText());
		Class<?>[] types = {
		};
		Object[] params = {
		};
		try {
			borderObject.setBorderObject(
				"javax.swing.border.EtchedBorder",
				types,
				params);
		} catch (Exception ex) {
			throw (new WrapRuntimeException("Standard Border Failed", ex));
		}
	}

	/**
	 * Initializes the undo manager of the component.
	 */
	protected void initializeUndoManager() {
		ExtMilieuRef mil = jundo.runtime.Runtime.getInitialMilieu();
		pdx_HyperButtonModel_pdx_PairRef ref =
			pdx_HyperButtonModel_pdx_ObjectRef.pdxm_new_HyperButtonModel(mil);
		model = (pdx_HyperButtonModel_pdx_ObjectRef) (ref.getObject());
		undoMgr = UndoManager.createInstanceUndoManager(mil);
	}

	/**
	 * Handles a change to the global multi-level undo state.
	 */
	protected void handleUndoStateChange() {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		Image im = (Image) (model.pdxm_getImage(mil));
		setImageComp(im);
		String txt = (String) (model.pdxm_getLabelText(mil));
		setTextComp(txt);
	}

	/**
	 * Handles property change events.
	 * @param evt The event to be handled.
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == UndoManager.MODEL_STATE_CHANGED) {
			handleUndoStateChange();
		}

	}

	/**
	* Sets the background color of the component, and whether it is transparent.
	* Note: transparency is ignored because typical JButton PLAFs don't support it.
	* @param inC The color to which to set the background.
	* @param opaque Whether to make the background opaque.
	*/
	public void handleBackgroundState(Color inC, boolean opaque) {
		setBackground(inC);
	}

	/**
	* Gets whether the component is opaque for Swing.
	* @return Whether the component is opaque.
	*/
	public boolean isOpaque() {
		boolean ret = true;
		if (bkgnd != null) {
			ret = bkgnd.isOpaqueFlag();
		}
		return (ret);
	}

	/**
	 * Gets the next incrementing macro parameter index to be allocated.
	 * @return The next incrementing macro parameter index to be allocated.
	 */
	public int getParamVal() {
		int ret = model.pdxm_getParamVal(undoMgr.getCurrentMil());
		return (ret);
	}

	/**
	 * Sets the next incrementing macro parameter index to be allocated.
	 * @param in The next incrementing macro parameter index to be allocated.
	 */
	public void setParamVal(int in) {
		ExtMilieuRef mil = model.pdxm_setParamVal(undoMgr.getCurrentMil(), in);
		undoMgr.handleCommitTempChange(mil);
	}

	/**
	 * Gets the table of macro parameter results to use when playing the macro upon a button press.
	 * @return The table of macro parameter results to use when playing the macro upon a button press.
	 */
	public Vector<Object> getResultTable() {
		Object ret = model.pdxm_getResultTable(undoMgr.getCurrentMil());
		return ( (Vector<Object>) ret );
	}

	/**
	 * Sets the table of macro parameter results to use when playing the macro upon a button press.
	 * @param in The table of macro parameter results to use when playing the macro upon a button press.
	 */
	public void setResultTable( Vector<Object> in ) {
		ExtMilieuRef mil =
			model.pdxm_setResultTable(undoMgr.getCurrentMil(), in);
		undoMgr.handleCommitTempChange(mil);
	}

	/**
	 * Gets the list of macro instructions to be executed upon a button press.
	 * @return The list of macro instructions to be executed upon a button press.
	 */
	public HighLevelList<MacroRecorderNode,StdLowLevelList<MacroRecorderNode>> getMacroList() {
		Object ret = model.pdxm_getMacroList(undoMgr.getCurrentMil());
		return ((HighLevelList<MacroRecorderNode,StdLowLevelList<MacroRecorderNode>>) ret);
	}

	/**
	 * Sets the list of macro instructions to be executed upon a button press.
	 * @param in The list of macro instructions to be executed upon a button press.
	 */
	public void setMacroList( HighLevelList<MacroRecorderNode,StdLowLevelList<MacroRecorderNode>> in ) {
		ExtMilieuRef mil = model.pdxm_setMacroList(undoMgr.getCurrentMil(), in);
		undoMgr.handleCommitTempChange(mil);
	}

	/**
	* Gets the image mode of the component.  Can have a value of either
	* {@link #NO_MODE}, {@link #GIF_MODE}, or {@link #JPEG_MODE}.
	* @return The image mode of the component.
	*/
	public int getImageMode() {
		int ret = model.pdxm_getImageMode(undoMgr.getCurrentMil());
		return (ret);
	}

	/**
	 * Sets the image mode of the component.  Can have a value of either
	 * {@link #NO_MODE}, {@link #GIF_MODE}, or {@link #JPEG_MODE}.
	 * @param in The image mode of the component.
	 */
	public void setImageMode(int in) {
		ExtMilieuRef mil = model.pdxm_setImageMode(undoMgr.getCurrentMil(), in);
		undoMgr.handleCommitTempChange(mil);
	}

	/**
	 * Gets the underlying byte representation of the displayed image.
	 * @return The underlying byte representation of the displayed image.
	 */
	public byte[] getImageBytes() {
		Object ret = model.pdxm_getImageBytes(undoMgr.getCurrentMil());
		return ((byte[]) ret);
	}

	/**
	 * Sets the underlying byte representation of the displayed image.
	 * @param in The underlying byte representation of the displayed image.
	 */
	public void setImageBytes(byte[] in) {
		ExtMilieuRef mil =
			model.pdxm_setImageBytes(undoMgr.getCurrentMil(), in);
		undoMgr.handleCommitTempChange(mil);
	}

	/**
	 * Sets the text displayed by the component.
	 * @param in The text displayed by the component.
	 */
	protected void setTextComp(String in) {
		super.setText(in);
		repaint();
	}

	/**
	 * Sets the text displayed by the component, including the associated multi-level undo state.
	 * @param in The text displayed by the component.
	 */
	public void setText(String in) {
		if (model != null) {
			ExtMilieuRef mil =
				model.pdxm_setLabelText(undoMgr.getCurrentMil(), in);
			undoMgr.handleCommitTempChange(mil);
		}
		setTextComp(in);
	}

	/**
	 * Gets the image being displayed by the component.
	 * @return The image being displayed by the component.
	 */
	protected Image getImage() {
		return (mImage);
	}

	/**
	 * Sets the image displayed by the component.
	 * @param in The image displayed by the component.
	 */
	protected void setImageComp(Image in) {
		mImage = in;
		if (in != null) {
			setIcon(new ImageIcon(in));
		} else {
			setIcon(null);
		}
		revalidate();
		repaint();
	}

	/**
	 * Sets the image displayed by the component, including the associated multi-level undo state.
	 * @param in The image displayed by the component.
	 */
	protected void setImage(Image in) {
		ExtMilieuRef mil = model.pdxm_setImage(undoMgr.getCurrentMil(), in);
		undoMgr.handleCommitTempChange(mil);
		setImageComp(in);
	}

	/**
	* Handles a program Director event to generate part of the result of the button press.
	* @param e The input event.
	* @return The component that was added.
	*/
	protected Object handleProgramDirectorEvent(ProgramDirectorEvent e) {
		setMacroList(new HighLevelList<MacroRecorderNode,StdLowLevelList<MacroRecorderNode>>());
		setResultTable(new Vector<Object>());
		setParamVal(1);
		EtherEvent eCopy = null;
		getResultTable().setSize(getParamVal());
		MacroParameter param0 = new MacroParameter(0);

		EtherEvent start =
			new StandardEtherEvent(
				this,
				StandardEtherEvent.getEventSource,
				null,
				"Hyper Button");

		Object startReply = param0;
		Object replyCopy = null;

		if (e.getEtherID().equals(ProgramDirectorEvent.createApp)) {
			eCopy =
				new PropertyEditEtherEvent(
					this,
					HyperButton.hyperButtonCreateNew,
					e.getParameter(),
					param0);
		} else {
			Object[] param = { e.getProgramURL(), param0 };
			eCopy =
				new PropertyEditEtherEvent(
					this,
					HyperButton.hyperButtonOpenFile,
					e.getParameter(),
					param0);
		}

		MacroRecorderNode MyA = new MacroRecorderNode();
		MyA.setOrigEvent(start);
		MyA.setCopyEvent(start);
		MyA.setOrigReply(startReply);
		MyA.setCopyReply(startReply);
		getMacroList().insertRight(MyA);
		MacroRecorderNode MyB = new MacroRecorderNode();
		MyB.setOrigEvent(eCopy);
		MyB.setCopyEvent(eCopy);
		MyB.setOrigReply(replyCopy);
		MyB.setCopyReply(replyCopy);
		getMacroList().insertRight(MyB);
		return (null);
	}

	/**
	* Handles the firing of the button.
	* @param e The input event.
	* @return The result of executing the event.
	*/
	protected Object hyperButtonFire(EtherEvent e) throws Throwable {
		MacroRecorder.playMacro(getMacroList(), this, getResultTable());
		return (null);
	}

	/**
	* Handles an EtherEvent to create a new component.
	* @param e The input event.
	* @return The result of executing the event.
	*/
	protected Object hyperButtonCreateNew(EtherEvent e) throws Throwable {
		Component tmp = ProgramDirector.getEnclosingContainer(getGUI());
		ProgramDirectorEvent ev =
			new ProgramDirectorEvent(
				this,
				ProgramDirectorEvent.createApp,
				e.getParameter(),
				this);
		VerdantiumComponent MyC =
			ProgramDirector.showComponent(ev, (Container) tmp);
		return (MyC);
	}

	/**
	* Handles an EtherEvent to open a file.
	* @param e The input event.
	* @return The result of executing the event.
	*/
	protected Object hyperButtonOpenFile(EtherEvent e) throws Throwable {
		Component tmp = ProgramDirector.getEnclosingContainer(getGUI());
		ProgramDirectorEvent ev =
			new ProgramDirectorEvent(
				this,
				ProgramDirectorEvent.loadURL,
				e.getParameter(),
				this);
		VerdantiumComponent MyC =
			ProgramDirector.showComponent(ev, (Container) tmp);
		return (MyC);
	}

	/**
	* Handles the pressing of the button.
	* @param in The input button-press event.
	*/
	public void fireActionPerformed(ActionEvent e) {
		try {
			super.fireActionPerformed(e);

			if (EditorControl.getEditorMode(isOnlyDesignerEdits())
				== EditorControl.EditMode) {
				if (!(getMacroList().empty())) {
					EtherEvent send =
						new PropertyEditEtherEvent(
							this,
							hyperButtonFire,
							null,
							this);
					ProgramDirector.fireEtherEvent(send, null);
				}
			} else {
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
	 * Handles Ether Events to alter the properties of the component.
	 * @param in The event to handle.
	 * @param refcon A reference to context data for the event.
	 * @return The result of handling the event, or null if there is no result.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon)
		throws Throwable {

		Object ret = super.processObjEtherEvent(in, refcon);

		if (ret == EtherEvent.EVENT_NOT_HANDLED) {
			ret = null;
		}

		if (in instanceof PropertyEditEtherEvent) {

			if (in.getEtherID().equals(setLabelText)) {
				String text = (String) (in.getParameter());
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				setText(text);
				undoMgr.commitUndoableOp(utag, "Change Label Text");
			}

			if (in.getEtherID().equals(hyperButtonFire)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				Object reta = hyperButtonFire(in);
				undoMgr.commitUndoableOp(utag, "Button Press Action");
				return (reta);
			}

			if (in.getEtherID().equals(hyperButtonCreateNew)) {
				return (hyperButtonCreateNew(in));
			}

			if (in.getEtherID().equals(hyperButtonOpenFile)) {
				return (hyperButtonOpenFile(in));
			}

		}

		if (in instanceof ProgramDirectorEvent) {
			if (in
				.getEtherID()
				.equals(ProgramDirectorEvent.isProgramDirectorEventSupported)) {
				return (new Boolean(true));
			} else {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				Object reta =
					handleProgramDirectorEvent((ProgramDirectorEvent) in);
				undoMgr.commitUndoableOp(utag, "Set Link");
				return (reta);
			}

		}

		return (ret);
	}

	/**
	 * Creates the properties editor for the component.
	 * @return The created property editor.
	 */
	public VerdantiumPropertiesEditor makePropertiesEditor() {
		Properties MyP = new Properties();
		MyP.put("ProgramDirectorEditorName", "Link");
		HyperButtonPropertyEditor MyEdit =
			new HyperButtonPropertyEditor(this, MyP);
		return (MyEdit);
	}

	/**
	 * Shows the properties editor for the component.
	 * @param e The event for showing the editor.
	 */
	public void showPropertiesEditor(EtherEvent e) {
		VerdantiumPropertiesEditor MyEdit = makePropertiesEditor();
		ProgramDirector.showPropertyEditor(
			MyEdit,
			getGUI(),
			"Hyper Button Property Editor");
	}

	/**
	* Returns the data flavors that the component can load from persistent storage.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{
				new TransVersionBufferFlavor("Hyper Button", "Hyper Button"),
				new TransVersionBufferFlavor(
					"Macro Recorder",
					"Macro Recorder"),
				VerdantiumFlavorMap.createInputStreamFlavor("image", "gif"),
				VerdantiumFlavorMap.createInputStreamFlavor("image", "jpeg")};
		return (MyF);
	}

	/**
	* Returns the data flavors that the component can save to persistent storage.
    * @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{ new TransVersionBufferFlavor("Hyper Button", "Hyper Button")};
		return (MyF);
	}

	/**
	* Handles the setting of the component to its initial state.
	*/
	protected void handleClear() {
		setImage(null);
		setImageBytes(null);
		setImageMode(NO_MODE);
		setMacroList(new HighLevelList<MacroRecorderNode,StdLowLevelList<MacroRecorderNode>>());
		setResultTable(new Vector<Object>());
		setParamVal(0);
		setText("Button");
		bkgnd.setBackgroundState(DefaultBkgnd, true);
		Class<?>[] types = {
		};
		Object[] params = {
		};
		try {
			borderObject.setBorderObject(
				"javax.swing.border.EtchedBorder",
				types,
				params);
		} catch (Exception ex) {
			throw (new WrapRuntimeException("Default Border Failed", ex));
		}
		onlyDesignerEdits.setOnlyDesignerEdits(false);
		macroMap.clear();
		docPageFormat.setDocPageFormat(null);
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
			handleClear();
		} else {
			if (flavor instanceof TransVersionBufferFlavor) {
				try {
					TransVersionBuffer MyF = (TransVersionBuffer) trans;
					VersionBuffer.chkNul(MyF);
					TransVersionBufferFlavor T_flavor =
						(TransVersionBufferFlavor) flavor;
					VersionBuffer.chkNul(T_flavor);

					if (T_flavor.getComponentName().equals("Hyper Button")) {
						setMacroList(new HighLevelList<MacroRecorderNode,StdLowLevelList<MacroRecorderNode>>());
						setResultTable(new Vector<Object>());
						setParamVal(0);
						setParamVal(MyF.getInt("ParamVal"));
						setMacroList(
							(HighLevelList<MacroRecorderNode,StdLowLevelList<MacroRecorderNode>>) (MyF.getProperty("MacroList")));
						VersionBuffer.chkNul(getMacroList());
						macroMap.readData(MyF);

						String tmpstr = (String) (MyF.getProperty("Label"));
						VersionBuffer.chkNul(tmpstr);
						setText(tmpstr);

						Object myo = MyF.getProperty("ImageBytes");
						if (myo != null) {
							setImageBytes((byte[]) myo);
							VersionBuffer.chkNul(getImageBytes());
							setImage(
								(Toolkit.getDefaultToolkit()).createImage(
									getImageBytes()));
						}
						setImageMode(MyF.getInt("ImageMode"));

						bkgnd.readData(MyF);

						borderObject.readData(MyF);

						onlyDesignerEdits.setOnlyDesignerEdits(
							MyF.getBoolean("OnlyDesignerEdits"));
						getResultTable().setSize(getParamVal());
					}

					if (T_flavor.getComponentName().equals("Macro Recorder")) {
						setMacroList(new HighLevelList<MacroRecorderNode,StdLowLevelList<MacroRecorderNode>>());
						setResultTable(new Vector<Object>());
						setParamVal(0);
						setParamVal(MyF.getInt("ParamVal"));
						setMacroList(
							(HighLevelList<MacroRecorderNode,StdLowLevelList<MacroRecorderNode>>) (MyF.getProperty("MacroList")));
						VersionBuffer.chkNul(getMacroList());
						getResultTable().setSize(getParamVal());
					}
				} catch (ClassCastException ex) {
					throw (new DataFormatException(ex));
				}
			}

			if (flavor
				.getMimeType()
				.equals("image/gif; class=java.io.InputStream")) {
				try {
					loadImagePersistentData(flavor, trans);
				} catch (IOException ex) {
					throw (ex);
				}
				if (getImage() != null)
					setImageMode(GIF_MODE);
			}

			if (flavor
				.getMimeType()
				.equals("image/jpeg; class=java.io.InputStream")) {
				try {
					loadImagePersistentData(flavor, trans);
				} catch (IOException ex) {
					throw (ex);
				}
				if (getImage() != null)
					setImageMode(JPEG_MODE);
			}

		}
	}

	/**
	* Loads image data for the component from persistent storage.
    * @param flavor The flavor in which to load the data.
    * @param trans The transferable from which to load the data.
	*/
	protected void loadImagePersistentData(
		DataFlavor flavor,
		Transferable trans)
		throws IOException {
		byte buffer[] = new byte[1024];

		try {
			InputStream InStream =
				((InputStream) trans.getTransferData(flavor));

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			while (true) {
				int nRead = InStream.read(buffer, 0, buffer.length);
				if (nRead <= 0)
					break;
				baos.write(buffer, 0, nRead);
			}

			setImage(
				(Toolkit.getDefaultToolkit()).createImage(baos.toByteArray()));
			if (getImage() != null) {
				setImageBytes(baos.toByteArray());
			}

		} catch (IOException e) {
			throw (e);
		} catch (UnsupportedFlavorException e) {
			throw (
				new WrapRuntimeException(
					"Something Inconsistent in Flavors",
					e));
		}

	}

	/**
	* Saves the component to persistent storage.
    * @param The flavor in which to save the data.
    * @return Transferable to which the data is saved.
	*/
	public Transferable savePersistentData(DataFlavor flavor) {
		TransVersionBuffer MyF =
			new TransVersionBuffer("Hyper Button", "Hyper Button");

		MyF.setInt("ParamVal", getParamVal());
		MyF.setProperty("MacroList", getMacroList());

		MyF.setProperty("Label", getText());

		if (getImageBytes() != null)
			MyF.setProperty("ImageBytes", getImageBytes());
		MyF.setInt("ImageMode", getImageMode());

		bkgnd.writeData(MyF);
		MyF.setBoolean("OnlyDesignerEdits", isOnlyDesignerEdits());
		macroMap.writeData(MyF);

		borderObject.writeData(MyF);

		return (MyF);
	}

	/**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		HyperButtonHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		HyperButton MyComp = new HyperButton();
		ProgramDirector.showComponent(MyComp, "Hyper Button", argv, true);
	}

	
}

