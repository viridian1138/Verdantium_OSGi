package verdantium.standard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import jundo.runtime.ExtMilieuRef;
import jundo.util.pdx_HashMapSh_pdx_ObjectRef;
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
import verdantium.VerdantiumUndoableInternalFrame;
import verdantium.core.ContainerApp;
import verdantium.core.ContainerFindIterator;
import verdantium.core.DefaultPropertyEditor;
import verdantium.core.DesignerControl;
import verdantium.core.PageSizeHandlerPane;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.standard.help.InternalDesktopHelp;
import verdantium.undo.UTag;
import verdantium.undo.UndoManager;
import verdantium.utils.ComponentNotFoundException;
import verdantium.utils.IllegalInputException;
import verdantium.utils.ResourceNotFoundException;
import verdantium.utils.VerdantiumDragUtils;
import verdantium.utils.VerdantiumDropUtils;
import verdantium.xapp.ApplicationAdapter;
import verdantium.xapp.DocPageFormat;
import verdantium.xapp.EditorUndoBridge;
import verdantium.xapp.MacroTreeMap;
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
*    | 01/28/2001            | Thorn Green (viridian_1138@yahoo.com)           | Multiple bugs in calling of handleDestroy()                          | Implemented a set of bug-fixes.
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Macro support.                                                       | Added code for macro support.
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
* This is a component that embeds other components in MDI windows.  Printing this component
* is better than having a "screen shot" utility make a bitmap image of the display screen,
* and then printing the bitmap.  Hence, this component can be used for making both printed
* and online documentation where a "screen shot" of a window is needed.
* <P>
* This component can also be useful during those times when one needs more than one "desktop".
* <P>
* @author Thorn Green
*/
public class InternalDesktop
	extends ApplicationAdapter
	implements MouseListener {
	
	/**
	* The panel that contains the desktop.
	*/
	transient protected JPanel myPan = new JPanel();
	
	/**
	* The desktop pane that contains the internal desktop.
	*/
	protected PageSizeHandlerPane myDesk = null;

	/**
	* Gets the GUI for the component.
	* @return The GUI for the component.
	*/
	public JComponent getGUI() {
		return (myPan);
	}

	/**
	* Handles the desruction of the internal desktop by firing a property change event
	* to indicate the destruction.
	*/
	public void handleDestroy() {
		super.handleDestroy();
		myDesk.handleDestroy();
	}

	/**
	* Constructs the internal desktop.
	*/
	public InternalDesktop() {
		super();
		UndoManager mgr =
			UndoManager.createInstanceUndoManager(
				jundo.runtime.Runtime.getInitialMilieu());
		undoMgr = mgr;
		macroMap = new MacroTreeMap(mgr);
		onlyDesignerEdits = new OnlyDesignerEdits(mgr);
		docPageFormat = new DocPageFormat(mgr);
		editorBridge = new EditorUndoBridge(mgr);
		myDesk = new PageSizeHandlerPane(mgr);
		myDesk.setMinimumSize(new Dimension(2, 2));
		myDesk.setPreferredSize(new Dimension(100, 100));
		myDesk.addMouseListener(this);
		arrangeLayout();
		/* myDesk.putClientProperty( "JDesktopPane.dragMode" , "outline" ); */
		/* myDesk.setToolTipText( "Right-Click to edit properties" ); -- Still Buggy */
		VerdantiumDragUtils.setDragUtil(myDesk, this);
		VerdantiumDropUtils.setDropUtil(myDesk, this, this);
		configureForEtherEvents();
	}

	/**
	* Sets up the layout hierarchy on the component.
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

		if (in instanceof ProgramDirectorEvent) {
			if (in
				.getEtherID()
				.equals(ProgramDirectorEvent.isProgramDirectorEventSupported)) {
				return (new Boolean(true));
			} else {
				return (handleProgramDirectorEvent((ProgramDirectorEvent) in));
			}

		}

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

		if (!handled) {
			Object ret = super.processObjEtherEvent(in, refcon);
			if (ret == EtherEvent.EVENT_NOT_HANDLED)
				ret = null;
			return (ret);
		}
		return (null);
	}

	/**
	 * Handles a change to whether only the designer edits.
	 */
	public void handleOnlyDesignerEditsChange() {
		if (isOnlyDesignerEdits())
			myDesk.setToolTipText(null);
		else
			myDesk.setToolTipText("Right-Click to edit properties");
	}

	/**
	* Creates a property editor for the component.
	* @return The created property editor.
	*/
	public VerdantiumPropertiesEditor makePropertiesEditor() {
		Properties MyP = new Properties();
		if (isScrolling())
			MyP.put("Scrolling", this);
		MyP.put("ProgramDirectorEditorName", "Windows");
		DefaultPropertyEditor MyEdit = new DefaultPropertyEditor(this, MyP);
		MyEdit.setClickPoint(new Point(10, 10));
		return (MyEdit);
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
			"Internal Desktop Property Editor");
	}

	/**
	* Handles a mouse-clicked event.  Currently does nothing.
	* @param e The input event.
	*/
	public void mouseClicked(MouseEvent e) {
	}

	/**
	* Handles a mouse-entered event.  Currently does nothing.
	* @param e The input event.
	*/
	public void mouseEntered(MouseEvent e) {
	}

	/**
	* Handles a mouse-exited event.  Currently does nothing.
	* @param e The input event.
	*/
	public void mouseExited(MouseEvent e) {
	}

	/**
	* Handles a mouse-released event.  Currently does nothing.
	* @param e The input event.
	*/
	public void mouseReleased(MouseEvent e) {
	}

	/**
	* Handles a mouse-pressed event by showing the component's property editor
	* if the component is in a mode where it is allowed to be edited.
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
		UTag utag = new UTag();
		undoMgr.prepareForTempCommit(utag);
                
	//	pdx_HashMapSh_pdx_ObjectRef map = myDesk.getMap();
	//	VerdantiumUndoableInternalFrame MyFr =
	//		new VerdantiumUndoableInternalFrame(undoMgr, map);
	//	if (e.getProgramName() != null)
	//		MyFr.setTitle(e.getProgramName());
	//	else
	//		MyFr.setTitle("Embedded Component");
	//	myDesk.add(MyFr, JLayeredPane.PALETTE_LAYER);
	//	try {
	//		VerdantiumComponent in =
	//			ProgramDirector.showComponent(e, MyFr.getContentPane());
//
	//		MyFr.setComponent(in);
        //                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	//		MyFr.packToPreferred();
	//		MyFr.show();
	//		myDesk.repaint();
	//		ProgramDirector.setPointLocation(in, e.getClickPoint());
                    
                        VerdantiumComponent in = ProgramDirector.showComponent( e ,
                                myDesk );
                        ProgramDirector.setPointLocation(in, e.getClickPoint());
                        
			ExtMilieuRef mil = undoMgr.getCurrentMil();
			undoMgr.handleCommitTempChange(mil);
			undoMgr.commitUndoableOp(utag, "MDI Window Creation");
			return (in);
	//	} catch (IOException ex) {
	//		try {
	//			MyFr.setVisible(false);
	//			MyFr.dispose();
	//			myDesk.repaint();
	//		} catch (Exception exx) { /* No Handle */
	//		}
//
	//		throw (ex);
	//	} catch (ClassNotFoundException ex) {
	//		try {
	//			MyFr.setVisible(false);
	//			MyFr.dispose();
	//			myDesk.repaint();
	//		} catch (Exception exx) { /* No Handle */
	//		}
//
	//		throw (ex);
	//	} catch (ResourceNotFoundException ex) {
	//		try {
	//			MyFr.setVisible(false);
	//			MyFr.dispose();
	//			myDesk.repaint();
	//		} catch (Exception exx) { /* No Handle */
	//		}
//
	//		throw (ex);
	//	} catch (ComponentNotFoundException ex) {
	//		try {
	//			MyFr.setVisible(false);
	//			MyFr.dispose();
	//			myDesk.repaint();
	//		} catch (Exception exx) { /* No Handle */
	//		}
//
	//		throw (ex);
	//	}
	}

	/**
	* Loads embedded frames from persistent storage into a desktop pane.
	* @param in The Transferable containing the frames to be loaded.
	* @param out The desktop pane into which to insert the loaded frames.
	*/
	public static void loadInternalDesktopFrames(
		Transferable in,
		JDesktopPane out)
		throws
			IOException,
			ClassNotFoundException,
			ResourceNotFoundException,
			ComponentNotFoundException {
		try {
			TransVersionBuffer MyF = (TransVersionBuffer) (in);
			VersionBuffer.chkNul(MyF);
			Rectangle[] BoundsList =
				(Rectangle[]) (MyF.getProperty("BoundsList"));
			VersionBuffer.chkNul(BoundsList);
			Transferable[] PartList =
				(Transferable[]) (MyF.getProperty("PartList"));
			VersionBuffer.chkNul(PartList);
			if (BoundsList.length != PartList.length) {
				throw (new DataFormatException());
			}
			int len = PartList.length;
			int count;

			for (count = 0; count < len; count++) {
				DataFlavor[] MyFlavors = { null };
				Class MyClass =
					ProgramDirector.getComponentClass(
						PartList[count],
						MyFlavors);
				VerdantiumComponent MyPart =
					ProgramDirector.showComponent(
						MyClass,
						BoundsList[count],
						out,
						ProgramDirector.getComponentName());
				DataFlavor flavor = MyFlavors[0];
				MyPart.loadPersistentData(flavor, PartList[count]);
			}
		} catch (IOException ex) {
			ContainerApp.closeAllFrames(out);
			throw (ex);
		} catch (ClassNotFoundException ex) {
			ContainerApp.closeAllFrames(out);
			throw (ex);
		} catch (ComponentNotFoundException ex) {
			ContainerApp.closeAllFrames(out);
			throw (ex);
		} catch (ResourceNotFoundException ex) {
			ContainerApp.closeAllFrames(out);
			throw (ex);
		} catch (ClassCastException ex) {
			ContainerApp.closeAllFrames(out);
			throw (new DataFormatException(ex));
		}
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
					"Internal Desktop",
					"Internal Desktop")};
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
					"Internal Desktop",
					"Internal Desktop")};
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
			onlyDesignerEdits.setOnlyDesignerEdits(false);
			ContainerApp.closeAllFrames(myDesk);
			macroMap.clear();
		} else {
			try {
				TransVersionBuffer MyF = (TransVersionBuffer) trans;
				VersionBuffer.chkNul(MyF);
				onlyDesignerEdits.setOnlyDesignerEdits(
					MyF.getBoolean("OnlyDesignerEdits"));
				Transferable MyT = (Transferable) (MyF.getProperty("Frames"));
				VersionBuffer.chkNul(MyT);
				InternalDesktop.loadInternalDesktopFrames(MyT, myDesk);
				Dimension dim = (Dimension) (MyF.getProperty("PageSize"));
				VersionBuffer.chkNul(dim);
				alterPageSize(dim);
				macroMap.readData(MyF);
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
			new TransVersionBuffer("Internal Desktop", "Internal Desktop");
		MyF.setBoolean("OnlyDesignerEdits", isOnlyDesignerEdits());
		MyF.setProperty(
			"Frames",
			ContainerApp.saveInternalDesktopFrames(myDesk));
		macroMap.writeData(MyF);
		MyF.setProperty(
			"PageSize",
			new Dimension(myPan.getBounds().width, myPan.getBounds().height));
		return (MyF);
	}

	/**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		InternalDesktopHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		InternalDesktop MyComp = new InternalDesktop();
		ProgramDirector.showComponent(MyComp, "Internal Desktop", argv, true);
	}

	
}

