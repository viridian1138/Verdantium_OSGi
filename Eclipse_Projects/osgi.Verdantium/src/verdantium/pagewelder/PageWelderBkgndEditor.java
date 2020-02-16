package verdantium.pagewelder;

import java.awt.BorderLayout;
import java.awt.Color;
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

import meta.VersionBuffer;
import meta.WrapRuntimeException;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.ProgramDirectorEvent;
import verdantium.StandardEtherEvent;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.VerdantiumUtils;
import verdantium.core.BackgroundListener;
import verdantium.core.ContainerApp;
import verdantium.core.ContainerAppDesktopPane;
import verdantium.core.ContainerFindIterator;
import verdantium.core.DefaultPropertyEditor;
import verdantium.core.DesignerControl;
import verdantium.core.EditorControl;
import verdantium.core.PropertyEditEtherEvent;
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
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
*    | 05/10/2002            | Thorn Green (viridian_1138@yahoo.com)           | Redundant information in persistent storage.                         | Made numerous persistence and packaging changes.
*    | 03/09/2003            | Thorn Green (viridian_1138@yahoo.com)           | PageWelder.                                                          | Implemented PageWelder using code from other classes.
*    | 08/07/2004            | Thorn Green (viridian_1138@yahoo.com)           | Establish baseline for all changes in the last year.                 | Establish baseline for all changes in the last year.
*    | 08/12/2004            | Thorn Green (viridian_1138@yahoo.com)           | First cut at template editing for PageWelder.                        | Added initial template functionality.
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
* Property editor for altering the background content.
* 
* @author Thorn Green
*/
public class PageWelderBkgndEditor
	extends ApplicationAdapter
	implements
		VerdantiumPropertiesEditor,
		BackgroundListener,
		MouseListener {
	
	/**
	* Returns the panel that encloses the GUI.
	*/
	transient protected JPanel MyPan = new JPanel();
	
	/**
	* The desktop pane used to display embedded components.
	*/
	protected ContainerAppDesktopPane MyDesk = null;

	/**
	* The PageWelder component that made the editing request.
	*/
	private PageWelder target = null;

	/**
	 * Returns the GUI of the property editor.
	 * @return The GUI of the property editor.
	 */
	public JComponent getGUI() {
		return (MyPan);
	}

	/**
	* Handles property change events.
	* @param evt The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == EditorControl.EditCntlChange) {
			int count;
			JInternalFrame[] AllFrames = MyDesk.getAllFrames();

			for (count = 0; count < AllFrames.length; count++) {
				AllFrames[count].updateUI();
				AllFrames[count].repaint();
			}
		}

		if (evt.getPropertyName() == ProgramDirector.propertyHide) {
			VerdantiumUtils.disposeContainer(this);
		}

	}

	/**
	* Handles the destruction of the component.
	*/
	public void handleDestroy() {
		try {
			VersionBuffer buff = new VersionBuffer(true);

			buff.setProperty(
				"Frames",
				ContainerApp.saveInternalDesktopFrames(MyDesk));
			buff.setBoolean("Opaque", getOpaqueFlag());
			buff.setProperty(
				"Background",
				VerdantiumUtils.cloneColorRGB(getBackgroundColor()));

			target.setBkgndTemplate(buff);
			target.getGUI().repaint();
		} catch (Exception ex) {
			throw (
				new WrapRuntimeException(
					"Handle Destroy Failed In Bkgnd Edit",
					ex));
		}

		MyDesk.handleDestroy();
		super.handleDestroy();
	}

	/**
	* Constructs a property editor for altering backgrounds given the PageWelder
	* making the request.
	* @param in The component being edited.
	*/
	public PageWelderBkgndEditor(PageWelder in) {
		super();
		target = in;
		UndoManager mgr =
			UndoManager.createInstanceUndoManager(
				jundo.runtime.Runtime.getInitialMilieu());
		undoMgr = mgr;
		initialize(mgr);
		editorBridge = new EditorUndoBridge(undoMgr);
		MyDesk = new ContainerAppDesktopPane(mgr, this);
		MyDesk.setMinimumSize(new Dimension(2, 2));
		MyDesk.setPreferredSize(new Dimension(100, 100));
		arrangeLayout();
		MyDesk.addMouseListener(this);
		MyDesk.setOpaqueFlag(true);
		DefaultBkgnd = MyDesk.getBackground();
		in.addPropertyChangeListener(this);
		MyDesk.setToolTipText("Right-Click to edit properties");
		VerdantiumDragUtils.setDragUtil(MyDesk, this);
		VerdantiumDropUtils.setDropUtil(MyDesk, this, this);
		configureForEtherEvents();
		bkgnd = MyDesk.getBkgnd();
		bkgnd.configureForEtherEvents(MyDesk, PropL);
		bkgnd.setBackgroundState(DefaultBkgnd, true);
	}

	/**
	* Sets up the layout managers for the component.
	*/
	protected void arrangeLayout() {
		MyPan.setLayout(new BorderLayout(0, 0));
		MyPan.add("Center", MyDesk);
		MyPan.setMinimumSize(new Dimension(2, 2));
		MyPan.setPreferredSize(new Dimension(50, 50));
		MyPan.setOpaque(false);
	}

	/**
	 * Handles Ether Events to alter the properties of the editor.
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
	 * Handles a change to whether only the designer can edit the component.
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
	* Displays a property editor for the PageWelder component.
	* @param e The input event requesting the editor.
	*/
	public void showPropertiesEditor(EtherEvent e) {
		VerdantiumPropertiesEditor MyEdit = makePropertiesEditor();
		((DefaultPropertyEditor) MyEdit).setClickPoint(
			(Point) (e.getParameter()));
		ProgramDirector.showPropertyEditor(
			MyEdit,
			getGUI(),
			"PageWelder Background Property Editor");
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
	*/
	protected Object handleProgramDirectorEvent(ProgramDirectorEvent e)
		throws Throwable {
		return (ContainerApp.addComponentToPane(e, undoMgr, MyDesk));
	}

	/**
	* Sets the background.
	* @param InC The color to which to set the background.
	* @param Opaque Whether to make the background opaque.
	*/
	public void handleBackgroundState(Color InC, boolean Opaque) {
		MyDesk.setBackground(InC);
		MyDesk.setOpaqueFlag(Opaque);
		MyDesk.repaint();
	}

	/**
	* Loads the current background from the transferable object "tar" into the TextApp.
	*/
	public void loadBkgnd()
		throws
			IOException,
			ClassNotFoundException,
			ResourceNotFoundException,
			ComponentNotFoundException {
		target.loadTemplate(undoMgr,MyDesk);
	}

	/**
	* Gets the input data flavors supported.  None are supported in this class.
	* @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		return (null);
	}

	/**
	* Gets the output data flavors supported.  None are supported in this class.
	* @return The supported flavors.
	* 
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		return (null);
	}

	/**
	* Interface to load persistent data.  Does nothing.
	* @param flavor The flavor in which to load the data.
    * @param trans The transferable from which to load the data.
	*/
	public void loadPersistentData(DataFlavor flavor, Transferable trans) {
	}

	/**
	* Interface to save persistent data.  Does nothing.
	* @param The flavor in which to save the data.
    * @return Transferable to which the data is saved.
	*/
	public Transferable savePersistentData(DataFlavor flavor) {
		return (null);
	}

	
}

