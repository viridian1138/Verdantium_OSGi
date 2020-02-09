package verdantium.demo.mycontainerapp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import meta.DataFormatException;
import meta.VersionBuffer;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.ProgramDirectorEvent;
import verdantium.StandardEtherEvent;
import verdantium.TransVersionBuffer;
import verdantium.TransVersionBufferFlavor;
import verdantium.UrlHolder;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.core.BackgroundListener;
import verdantium.core.ContainerApp;
import verdantium.core.ContainerAppDesktopPane;
import verdantium.core.ContainerAppInternalFrame;
import verdantium.core.ContainerFindIterator;
import verdantium.core.DefaultPropertyEditor;
import verdantium.core.DesignerControl;
import verdantium.core.EditorControl;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.demo.mycontainerapp.help.MyContainerAppHelp;
import verdantium.undo.UndoManager;
import verdantium.utils.ComponentNotFoundException;
import verdantium.utils.IllegalInputException;
import verdantium.utils.ResourceNotFoundException;
import verdantium.utils.VerdantiumDragUtils;
import verdantium.utils.VerdantiumDropUtils;
import verdantium.xapp.ApplicationAdapter;
import verdantium.xapp.EditorUndoBridge;


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
*    | 01/28/2001            | Thorn Green (viridian_1138@yahoo.com)           | Multiple bugs in calling of handleDestroy()                          | Implemented a set of bug-fixes.
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Macro support.                                                       | Added macro support.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/29/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for multiple PLAFs.                                          | Made code changes to support multiple PLAFs.
*    | 10/05/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for command-line arguments.                                  | Added support for command-line arguments.
*    | 12/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Third-Cut at Error Handling.                                         | Third-Cut at Error Handling.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
*    | 04/21/2002            | Thorn Green (viridian_1138@yahoo.com)           | Find/Replace support.                                                | Added find/replace support.
*    | 05/10/2002            | Thorn Green (viridian_1138@yahoo.com)           | Redundant information in persistent storage.                         | Made numerous persistence and packaging changes.
*    | 01/12/2003            | Thorn Green (viridian_1138@yahoo.com)           | Application development too complex.                                 | Simplified application development.
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
* MyContainerApp shows how to build a simple container application.  Copy this source code,
* rather than verdantium.core.ContainerApp, when building your own container.
* @author Thorn Green
*/
public class MyContainerApp
	extends ApplicationAdapter
	implements BackgroundListener, MouseListener {
	
	/**
	* Returns the panel that encloses the GUI.
	*/
	transient protected JPanel myPan = new JPanel();
	
	/**
	* The desktop pane used to display embedded components.
	*/
	protected ContainerAppDesktopPane myDesk = null;
	
	/**
	 * Returns the GUI of the component.
	 * @return The GUI of the component.
	 */
	public JComponent getGUI() {
		return (myPan);
	}

	/**
	* Handles property change events.
	* @param evt The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == EditorControl.EditCntlChange) {
			int count;
			JInternalFrame[] AllFrames = myDesk.getAllFrames();

			for (count = 0; count < AllFrames.length; count++) {
				AllFrames[count].updateUI();
				AllFrames[count].repaint();
			}
		}

	}

	@Override
	public void handleDestroy() {
		super.handleDestroy();
		myDesk.handleDestroy();
	}

	/**
	* Constructs the container app.
	*/
	public MyContainerApp() {
		super();
		UndoManager mgr =
			UndoManager.createInstanceUndoManager(
				jundo.runtime.Runtime.getInitialMilieu());
		undoMgr = mgr;
		initialize(mgr);
		editorBridge = new EditorUndoBridge(undoMgr);
		myDesk = new ContainerAppDesktopPane(mgr, this);
		myDesk.setMinimumSize(new Dimension(2, 2));
		myDesk.setPreferredSize(new Dimension(100, 100));
		arrangeLayout();
		myDesk.addMouseListener(this);
		myDesk.setOpaqueFlag(true);
		DefaultBkgnd = myDesk.getBackground();
		myDesk.setToolTipText("Right-Click to edit properties");
		VerdantiumDragUtils.setDragUtil(myDesk, this);
		VerdantiumDropUtils.setDropUtil(myDesk, this, this);
		configureForEtherEvents();
		bkgnd = myDesk.getBkgnd();
		bkgnd.configureForEtherEvents(myDesk,PropL);
		bkgnd.setBackgroundState(DefaultBkgnd, true);
	}

	/**
	* Sets up the layout managers for the component.
	*/
	protected void arrangeLayout() {
		myPan.setLayout(new BorderLayout(0, 0));
		myPan.add("Center", myDesk);
		myPan.setMinimumSize(new Dimension(2, 2));
		myPan.setPreferredSize(new Dimension(50, 50));
		myPan.setOpaque(false);
	}

	/**
	 * Handles Ether Events to alter the properties of the component.
	 * @param in The event to handle.
	 * @param refcon A reference to context data for the event.
	 * @return The result of handling the event, or null if there is no result.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon)
		throws Throwable {
		boolean handled = false;

		if (in instanceof PropertyEditEtherEvent) {
			if (in
				.getEtherID()
				.equals(
					PropertyEditEtherEvent.isFindReplaceIteratorSupported)) {
				return (new Boolean(true));
			}

			if (in
				.getEtherID()
				.equals(PropertyEditEtherEvent.createFindReplaceIterator)) {
				Object[] param = (Object[]) (in.getParameter());
				return (new ContainerFindIterator(param, this, myDesk));
			}

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

		if (!handled) {
			Object ret = super.processObjEtherEvent(in, refcon);
			if (ret == EtherEvent.EVENT_NOT_HANDLED)
				ret = null;
			return (ret);
		} else
			return (null);
	}

	/**
	 * Handles a change to whether only the designer edits.
	 */
	public void handleOnlyDesignerEditsChange() {
		int count;
		JInternalFrame[] AllFrames = myDesk.getAllFrames();

		for (count = 0; count < AllFrames.length; count++) {
			AllFrames[count].updateUI();
			AllFrames[count].repaint();
		}

		if (isOnlyDesignerEdits())
			myDesk.setToolTipText(null);
		else
			myDesk.setToolTipText("Right-Click to edit properties");
	}

	/**
	 * Shows the properties editor for the component.
	 * @param e The event for showing the editor.
	 */
	public void showPropertiesEditor(EtherEvent e) {
		VerdantiumPropertiesEditor MyEdit = makePropertiesEditor();
		((DefaultPropertyEditor) MyEdit).setClickPoint(
			(Point) (e.getParameter()));
		ProgramDirector.showPropertyEditor(
			MyEdit,
			getGUI(),
			"My Container App Property Editor");
	}

	/**
	* Handles mouse-clicked events.
	* @param e The input event.
	*/
	public void mouseClicked(MouseEvent e) {
	}
	
	/**
	* Handles mouse-entered events.
	* @param e The input event.
	*/
	public void mouseEntered(MouseEvent e) {
	}
	
	/**
	* Handles mouse-exit events.
	* @param e The input event.
	*/
	public void mouseExited(MouseEvent e) {
	}
	
	/**
	* Handles mouse-released events.
	* @param e The input event.
	*/
	public void mouseReleased(MouseEvent e) {
	}
	
	/**
	* Handles mouse-pressed events.
	* @param e The input event.
	*/
	public void mousePressed(MouseEvent e) {
		try {
			if ((DesignerControl.isDesignTime()) || (!isOnlyDesignerEdits())) {
				EtherEvent send =
					new StandardEtherEvent(
						this,
						StandardEtherEvent.showPropertiesEditor,
						null,
						this);
				send.setParameter(e.getPoint());
				ProgramDirector.fireEtherEvent(send, null);
			}
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles a program Director event to embed a component frame.
	* @param e The input event.
	* @return The component that was added.
	*/
	protected Object handleProgramDirectorEvent(ProgramDirectorEvent e)
		throws Throwable {
		return (ContainerApp.addComponentToPane(e, undoMgr, myDesk));
	}

	/**
	* Gets the input data flavors supported.  ContainerApp supports its own
	* proprietary format only.
	* @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{
				 new TransVersionBufferFlavor(
					"My Container App",
					"My Container App")};
		return (MyF);
	}

	/**
	* Gets the output data flavors supported.  ContainerApp supports its own
	* proprietary format only.
	* @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{
				 new TransVersionBufferFlavor(
					"My Container App",
					"My Container App")};
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
			bkgnd.setBackgroundState(DefaultBkgnd, true);
			borderObject.setBorderObject(null, null, null);
			onlyDesignerEdits.setOnlyDesignerEdits(false);
			ContainerApp.closeAllFrames(myDesk);
			macroMap.clear();
			docPageFormat.setDocPageFormat(null);
		} else {
			try {
				TransVersionBuffer MyF = (TransVersionBuffer) trans;
				VersionBuffer.chkNul(MyF);
				Transferable MyT = (Transferable) (MyF.getProperty("Frames"));
				VersionBuffer.chkNul(MyT);

				bkgnd.readData(MyF);

				borderObject.readData(MyF);

				Dimension dim = (Dimension) (MyF.getProperty("PageSize"));
				VersionBuffer.chkNul(dim);
				alterPageSize(dim);

				onlyDesignerEdits.setOnlyDesignerEdits(
					MyF.getBoolean("OnlyDesignerEdits"));
				macroMap.readData(MyF);

				ContainerApp.loadInternalDesktopFrames(
					MyT,
					ContainerAppInternalFrame.class,
                    undoMgr, myDesk);
			} catch (ClassNotFoundException ex) {
				throw (ex);
			} catch (IOException ex) {
				throw (ex);
			} catch (ResourceNotFoundException ex) {
				throw (ex);
			} catch (ComponentNotFoundException ex) {
				throw (ex);
			} catch (ClassCastException ex) {
				throw (new DataFormatException(ex));
			} catch (IllegalInputException ex) {
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
			new TransVersionBuffer("My Container App", "My Container App");

		try {
			MyF.setProperty(
				"Frames",
				ContainerApp.saveInternalDesktopFrames(myDesk));
			MyF.setBoolean("OnlyDesignerEdits", isOnlyDesignerEdits());
			bkgnd.writeData(MyF);
			macroMap.writeData(MyF);
			MyF.setProperty(
				"PageSize",
				new Dimension(
					myPan.getBounds().width,
					myPan.getBounds().height));

			borderObject.writeData(MyF);
		} catch (IOException ex) {
			throw (ex);
		}

		return (MyF);
	}

	/**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		MyContainerAppHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		MyContainerApp MyComp = new MyContainerApp();
		ProgramDirector.showComponent(MyComp, "My Container App", argv, true);
	}

}
