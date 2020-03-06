package verdantium.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.NotSerializableException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;

import jundo.runtime.ExtMilieuRef;
import jundo.util.pdx_HashMapSh_pdx_ObjectRef;
import meta.DataFormatException;
import meta.VersionBuffer;
import meta.WrapRuntimeException;
import verdantium.EtherEvent;
import verdantium.GuiShowNotify;
import verdantium.ProgramDirector;
import verdantium.ProgramDirectorEvent;
import verdantium.StandardEtherEvent;
import verdantium.TransVersionBuffer;
import verdantium.TransVersionBufferFlavor;
import verdantium.UrlHolder;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumDesktopPane;
import verdantium.VerdantiumInternalFrame;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.core.help.ContainerAppHelp;
import verdantium.undo.UTag;
import verdantium.undo.UndoManager;
import verdantium.utils.ComponentNotFoundException;
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
*    | 08/07/2004            | Thorn Green (viridian_1138@yahoo.com)           | Establish baseline for all changes in the last year.                 | Establish baseline for all changes in the last year.
*    | 08/11/2004            | Thorn Green (viridian_1138@yahoo.com)           | Support templates in PageWelder.                                     | Made dsktop pane accessible by subclasses.
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
* ContainerApp provides both a simple container application, and a set of APIs
* (as public static methods) to assist others in building their own container
* applications.  DO NOT copy this source file when building your own container
* application.  Instead, copy verdantium.demo.MyContainerApp.
* 
* @author Thorn Green
*/
public class ContainerApp
	extends ApplicationAdapter
	implements MouseListener, BackgroundListener {
	protected ContainerAppDesktopPane MyDesk = null;

	/**
	 * Returns the GUI of the component.
	 * @return The GUI of the component.
	 */
	public JComponent getGUI() {
		return (MyDesk);
	}

	/**
	* Handles property change events.
	* @param evt The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);

		if (evt.getPropertyName() == EditorControl.EditCntlChange) {
			int count;
			JInternalFrame[] AllFrames = MyDesk.getAllFrames();

			for (count = 0; count < AllFrames.length; count++) {
				AllFrames[count].updateUI();
				AllFrames[count].repaint();
			}
		}

	}

	@Override
	public void handleDestroy() {
		MyDesk.handleDestroy();
		super.handleDestroy();
	}

	/**
	 * Constructor.
	 */
	public ContainerApp() {
		super();
		UndoManager mgr =
			UndoManager.createInstanceUndoManager(
				jundo.runtime.Runtime.getInitialMilieu());
		undoMgr = mgr;
		initialize(mgr);
		editorBridge = new EditorUndoBridge(undoMgr);
		MyDesk = new ContainerAppDesktopPane(mgr, this);
		MyDesk.setMinimumSize(new Dimension(2, 2));
		MyDesk.setPreferredSize(new Dimension(100, 100));
		MyDesk.addMouseListener(this);
		MyDesk.setOpaqueFlag(true);
		DefaultBkgnd = MyDesk.getBackground();
		MyDesk.setToolTipText("Right-Click to edit properties");
		VerdantiumDragUtils.setDragUtil(MyDesk, this);
		VerdantiumDropUtils.setDropUtil(MyDesk, this, this);
		configureForEtherEvents();
		bkgnd = MyDesk.getBkgnd();
		bkgnd.configureForEtherEvents(MyDesk,PropL);
		bkgnd.setBackgroundState(DefaultBkgnd, true);
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
				return (new ContainerFindIterator(param, this, MyDesk));
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
		JInternalFrame[] AllFrames = MyDesk.getAllFrames();

		for (count = 0; count < AllFrames.length; count++) {
			AllFrames[count].updateUI();
			AllFrames[count].repaint();
		}

		if (isOnlyDesignerEdits())
			MyDesk.setToolTipText(null);
		else
			MyDesk.setToolTipText("Right-Click to edit properties");
	}

	/**
	* Displays a property editor for the container app.
	* @param e The input event.
	*/
	public void showPropertiesEditor(EtherEvent e) {
		VerdantiumPropertiesEditor MyEdit = makePropertiesEditor();
		((DefaultPropertyEditor) MyEdit).setClickPoint(
			(Point) (e.getParameter()));
		ProgramDirector.showPropertyEditor(
			MyEdit,
			getGUI(),
			"Container App Property Editor");
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
	*/
	protected Object handleProgramDirectorEvent(ProgramDirectorEvent e)
		throws Throwable {
		return (ContainerApp.addComponentToPane(e, undoMgr, MyDesk));
	}

	/**
	* Closes all frames in a desktop pane in an undoable manner.
	* @param in The desktop pane in which to close the frames.
	*/
	public static void closeAllFrames(JDesktopPane in) {
		JInternalFrame[] AllFrames = in.getAllFrames();
		int max = AllFrames.length;
		int count;
		for (count = 0; count < max; ++count) {
			try {
				AllFrames[count].setClosed(true);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
	}

	/**
	* Loads embedded frames from persistent storage into a desktop pane.
	* @param in The Transferable containing the frames to be loaded.
	* @param frameClass The class of the frames to be inserted.
	* @param mgr Manager for multi-level undo.
	* @param out The desktop pane into which to insert the loaded frames.
	*/
	public static void loadInternalDesktopFrames(
		Transferable in,
		Class<? extends VerdantiumInternalFrame> frameClass,
		UndoManager mgr,
		VerdantiumDesktopPane out)
		throws
			IOException,
			ClassNotFoundException,
			ResourceNotFoundException,
			ComponentNotFoundException {
		try {
			closeAllFrames(out);
			TransVersionBuffer MyF = (TransVersionBuffer) (in);
			VersionBuffer.chkNul(MyF);
			Rectangle[] BoundsList =
				(Rectangle[]) (MyF.getProperty("BoundsList"));
			VersionBuffer.chkNul(BoundsList);
			Transferable[] PartList =
				(Transferable[]) (MyF.getProperty("PartList"));
			VersionBuffer.chkNul(PartList);
			if (BoundsList.length != PartList.length)
				throw (new DataFormatException());
			int len = PartList.length;
			int count;
			Class<?>[] constructClss =
				{ UndoManager.class, pdx_HashMapSh_pdx_ObjectRef.class };
			Object[] constructObj = { mgr, out.getMap() };
			Constructor<? extends VerdantiumInternalFrame> cnst = frameClass.getConstructor(constructClss);

			for (count = 0; count < len; count++) {
				VerdantiumInternalFrame MyFr =
					cnst.newInstance(constructObj);
				MyFr.setBounds(BoundsList[count]);
				DataFlavor[] myFlavors = { null };
				Class<? extends VerdantiumComponent> myClass =
					ProgramDirector.getComponentClass(
						PartList[count],
						myFlavors);
				String MyName = ProgramDirector.getComponentName();
				VerdantiumComponent myPart =
					myClass.newInstance();
				MyFr.setTitle(MyName);
				MyFr.setComponent(myPart);
				MyFr.getContentPane().setLayout(new BorderLayout(0, 0));
				MyFr.getContentPane().add("Center", myPart.getGUI());
				out.add(MyFr, JLayeredPane.PALETTE_LAYER);
				if (myPart instanceof GuiShowNotify)
					 ((GuiShowNotify) myPart).guiShowNotify();
				DataFlavor flavor = myFlavors[0];
				myPart.loadPersistentData(flavor, PartList[count]);
				MyFr.pack();
				MyFr.setBounds(BoundsList[count]);
				MyFr.revalidate();
				MyFr.show();
				MyFr.repaint();
			}
		} catch (ResourceNotFoundException ex) {
			closeAllFrames(out);
			throw (ex);
		} catch (ClassNotFoundException ex) {
			closeAllFrames(out);
			throw (ex);
		} catch (IOException ex) {
			closeAllFrames(out);
			throw (ex);
		} catch (ComponentNotFoundException ex) {
			closeAllFrames(out);
			throw (ex);
		} catch (InstantiationException ex) {
			throw (
				new WrapRuntimeException("Component Construction Failed", ex));
		} catch (IllegalAccessException ex) {
			throw (
				new WrapRuntimeException("Component Construction Failed", ex));
		} catch (NoSuchMethodException ex) {
			throw (
				new WrapRuntimeException("Component Construction Failed", ex));
		} catch (InvocationTargetException ex) {
			throw (
				new WrapRuntimeException("Component Construction Failed", ex));
		} catch (ClassCastException ex) {
			throw (new DataFormatException(ex));
		}
	}

	/**
	* Saves embedded frames from a desktop pane to persistent storage.
	* @param in The desktop pane from which to save the embedded frames.
	* @return The persistence object containing the saved frames.
	*/
	public static Transferable saveInternalDesktopFrames(JDesktopPane in)
		throws IOException, NotSerializableException {
		TransVersionBuffer MyF =
			new TransVersionBuffer("Container Proxy", "Container Proxy");

		try {
			JInternalFrame[] Frames = in.getAllFrames();
			int len1 = Frames.length;
			int idex = 0;
			int len = 0;
			int count;

			for (count = 0; count < len1; count++)
				if ((!(((VerdantiumInternalFrame) (Frames[count]))
					.getComponent()
					instanceof VerdantiumPropertiesEditor))
					&& (!((Frames[count]).isClosed())))
					len++;

			Rectangle[] BoundsList = new Rectangle[len];
			Transferable[] PartList = new Transferable[len];

			for (count = 0; count < len1; ++count) {
				if ((!(((VerdantiumInternalFrame) (Frames[count]))
					.getComponent()
					instanceof VerdantiumPropertiesEditor))
					&& (!((Frames[count]).isClosed()))) {
					BoundsList[(len - 1) - idex] = (Frames[count]).getBounds();
					PartList[(len - 1) - idex] =
						ProgramDirector.getSerializableState(
							((VerdantiumInternalFrame) (Frames[count]))
								.getComponent());
					idex++;
				}
			}

			MyF.setProperty("BoundsList", BoundsList);
			MyF.setProperty("PartList", PartList);
		} catch (NotSerializableException ex) {
			throw (ex);
		} catch (IOException ex) {
			throw (ex);
		}

		return (MyF);
	}

	/**
	* Adds a component into a desktop pane.
	* @param e The event requesting the add of the component.
	* @param mgr Manager for multi-level undo.
	* @param myDesk Desktop pane to which to add the component.
	*/
	public static Object addComponentToPane(
		ProgramDirectorEvent e,
		UndoManager mgr,
		VerdantiumDesktopPane myDesk)
		throws
			IOException,
			ClassNotFoundException,
			ResourceNotFoundException,
			ComponentNotFoundException {
		UTag utag = new UTag();
		mgr.prepareForTempCommit(utag);
		pdx_HashMapSh_pdx_ObjectRef map = myDesk.getMap();
		ContainerAppInternalFrame MyFr =
			new ContainerAppInternalFrame(mgr, map);
		if (e.getProgramName() != null)
			MyFr.setTitle(e.getProgramName());
		else
			MyFr.setTitle("Embedded Component");
		myDesk.add(MyFr, JLayeredPane.PALETTE_LAYER);
		try {
			VerdantiumComponent in =
				ProgramDirector.showComponent(e, MyFr.getContentPane());

			MyFr.setComponent(in);
			MyFr.packToPreferred();
			MyFr.show();
			if (EditorControl.getEditorMode() == EditorControl.EditMode)
				EditorControl.setEditorMode(EditorControl.ResizeMode);
			myDesk.repaint();
			ProgramDirector.setPointLocation(in, e.getClickPoint());
			ExtMilieuRef mil = mgr.getCurrentMil();
			mgr.handleCommitTempChange(mil);
			mgr.commitUndoableOp(utag, "MDI Window Creation");
			return (in);
		} catch (IOException ex) {
			try {
				MyFr.setVisible(false);
				MyFr.dispose();
				myDesk.repaint();
			} catch (Exception exx) { /* No Handle */
			}

			throw (ex);
		} catch (ClassNotFoundException ex) {
			try {
				MyFr.setVisible(false);
				MyFr.dispose();
				myDesk.repaint();
			} catch (Exception exx) { /* No Handle */
			}

			throw (ex);
		} catch (ResourceNotFoundException ex) {
			try {
				MyFr.setVisible(false);
				MyFr.dispose();
				myDesk.repaint();
			} catch (Exception exx) { /* No Handle */
			}

			throw (ex);
		} catch (ComponentNotFoundException ex) {
			try {
				MyFr.setVisible(false);
				MyFr.dispose();
				myDesk.repaint();
			} catch (Exception exx) { /* No Handle */
			}

			throw (ex);
		}
	}

	/**
	* Gets the input data flavors supported.  ContainerApp supports its own
	* proprietary format only.
	* @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{ new TransVersionBufferFlavor("Container App", "Container App")};
		return (MyF);
	}

	/**
	* Gets the output data flavors supported.  ContainerApp supports its own
	* proprietary format only.
	* @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{ new TransVersionBufferFlavor("Container App", "Container App")};
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
		try {
			if (trans instanceof UrlHolder) {
				fileSaveURL = ((UrlHolder) trans).getUrl();
				fileSaveFlavor = flavor;
			}

			if (trans == null) {
				bkgnd.setBackgroundState(DefaultBkgnd, true);
				borderObject.setBorderObject(null, null, null);
				onlyDesignerEdits.setOnlyDesignerEdits(false);
				ContainerApp.closeAllFrames(MyDesk);
				macroMap.clear();
				docPageFormat.setDocPageFormat(null);
			} else {
				TransVersionBuffer MyF = (TransVersionBuffer) trans;
				VersionBuffer.chkNul(MyF);
				Transferable MyT = (Transferable) (MyF.getProperty("Frames"));
				VersionBuffer.chkNul(MyT);

				bkgnd.readData(MyF);

				borderObject.readData(MyF);

				onlyDesignerEdits.setOnlyDesignerEdits(
					MyF.getBoolean("OnlyDesignerEdits"));

				macroMap.readData(MyF);

				ContainerApp.loadInternalDesktopFrames(
					MyT,
					ContainerAppInternalFrame.class,
					undoMgr,
					MyDesk);
			}
		} catch (IOException e) {
			throw (e);
		} catch (ClassNotFoundException e) {
			throw (e);
		} catch (ResourceNotFoundException e) {
			throw (e);
		} catch (ComponentNotFoundException e) {
			throw (e);
		} catch (ClassCastException e) {
			throw (new DataFormatException(e));
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
			new TransVersionBuffer("Container App", "Container App");

		try {
			MyF.setProperty(
				"Frames",
				ContainerApp.saveInternalDesktopFrames(MyDesk));
			MyF.setBoolean("OnlyDesignerEdits", isOnlyDesignerEdits());
			bkgnd.writeData(MyF);
			macroMap.writeData(MyF);

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
		ContainerAppHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		ContainerApp MyComp = new ContainerApp();
		ProgramDirector.showComponent(MyComp, "Container App", argv, true);
	}

	
}

