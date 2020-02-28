package verdantium.xapp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.Properties;

import javax.swing.JComponent;

import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.ProgramDirectorEvent;
import verdantium.StandardEtherEvent;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.VerdantiumUtils;
import verdantium.core.DefaultPropertyEditor;
import verdantium.core.EditorControl;
import verdantium.undo.UndoManager;
import verdantium.utils.IllegalInputException;
import verdantium.utils.UndoableScrollPane;

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
*    | 11/05/2002            | Thorn Green (viridian_1138@yahoo.com)           | Application development too complex.                                 | Simplified application development.
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
* ApplicationAdapter provides a default base class for a component with the capabilities of a simple application.
* 
* @author Thorn Green
*/
public abstract class JcApplicationAdapter
	extends JComponent
	implements IApplicationAdapter {
	
	/**
	* The current border.
	*/
	protected BorderObject borderObject = null;

	/**
	* Stores whether only the designer can edit the component.
	*/
	protected OnlyDesignerEdits onlyDesignerEdits = null;

	/**
	* Holds the default background color of the desktop pane.
	*/
	protected static Color DefaultBkgnd = null;

	/**
	* The URL used when the user selects "Save" as opposed to "Save As..."
	*/
	transient protected URL fileSaveURL = null;

	/**
	* The data flavor used when the user selects "Save" as opposed to "Save As..."
	*/
	transient protected DataFlavor fileSaveFlavor = null;

	/**
	* The information stored by the "Page Setup" dialog.
	*/
	transient protected DocPageFormat docPageFormat = null;

	/**
	 * The component's background color and opaqueness.
	 */
	transient protected BackgroundState bkgnd = null;

	/**
	 * Data reference for the only designer edits flag.
	 */
	transient protected EditorUndoBridge editorBridge = null;

	/**
	 * Data reference for PageFormat objects.
	 */
	transient protected PageSizeState pageSizeState = null;

	/**
	 * Extension of JScrollPane supporting multi-level undo and scripting.
	 */
	transient protected UndoableScrollPane scp = null;

	/**
	* The tree map containing the client macros.
	*/
	protected MacroTreeMap macroMap = null;

	/**
	 * UndoManager through which to support undo.
	 */
	protected UndoManager undoMgr = null;

	/**
	 * Initializes the component.
	 * @param mgr The multi-level undo manager for the component.
	 */
	protected void initialize(UndoManager mgr) {
		macroMap = new MacroTreeMap(mgr);
		docPageFormat = new DocPageFormat(mgr);
		onlyDesignerEdits = new OnlyDesignerEdits(mgr);
		borderObject = new BorderObject(mgr);
	}

	/**
	* Handles property change events.
	* @param evt The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
	}

	/**
	* Handles the destruction of the component.
	*/
	public void handleDestroy() {
		EditorControl.removePropertyChangeListener(this);
		if (macroMap != null)
			macroMap.handleDestroy();
		if (docPageFormat != null)
			docPageFormat.handleDestroy();
		if (onlyDesignerEdits != null)
			onlyDesignerEdits.handleDestroy();
		if (borderObject != null)
			borderObject.handleDestroy();
		if (bkgnd != null)
			bkgnd.handleDestroy();
		if (editorBridge != null)
			editorBridge.handleDestroy();
		if (pageSizeState != null)
			pageSizeState.handleDestroy();
		if (scp != null)
			scp.handleDestroy();
		if (undoMgr != null)
			undoMgr.removePropertyChangeListener(this);
		PropL.firePropertyChange(ProgramDirector.propertyHide, null, null);
		PropL.firePropertyChange(
			ProgramDirector.propertyDestruction,
			null,
			null);
	}

	/**
	* Constructs the component.
	*/
	public JcApplicationAdapter() {
		if (PropL == null) {
			PropL = new PropertyChangeSupport(this);
		}
		EditorControl.addPropertyChangeListener(this);
	}

	/**
	 * Configures the application adapter so that it can handle Ether Events.
	 */
	protected void configureForEtherEvents() {
		if (macroMap != null)
			macroMap.configureForEtherEvents(this, PropL);
		if (docPageFormat != null)
			docPageFormat.configureForEtherEvents(this, PropL);
		if (onlyDesignerEdits != null)
			onlyDesignerEdits.configureForEtherEvents(this, PropL);
		if (borderObject != null)
			borderObject.configureForEtherEvents(this, PropL);
		if (bkgnd != null)
			bkgnd.configureForEtherEvents(this, PropL);
	}

	/**
	 * Handles Ether Events to alter the properties of the BorderObject.
	 * @param in The event to handle.
	 * @param refcon A reference to context data for the event.
	 * @return The result of handling the event, or null if there is no result.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon)
		throws Throwable {
		Object ret = null;

		if (undoMgr != null) {
			ret = undoMgr.processObjEtherEvent(in, refcon);
			if (ret != EtherEvent.EVENT_NOT_HANDLED) {
				return (ret);
			}
		}

		if (macroMap != null) {
			ret = macroMap.processObjEtherEvent(in, refcon);
			if (ret != EtherEvent.EVENT_NOT_HANDLED) {
				return (ret);
			}
		}

		if (docPageFormat != null) {
			ret = docPageFormat.processObjEtherEvent(in, refcon);
			if (ret != EtherEvent.EVENT_NOT_HANDLED) {
				return (ret);
			}
		}

		if (onlyDesignerEdits != null) {
			ret = onlyDesignerEdits.processObjEtherEvent(in, refcon);
			if (ret != EtherEvent.EVENT_NOT_HANDLED) {
				return (ret);
			}
		}

		if (borderObject != null) {
			ret = borderObject.processObjEtherEvent(in, refcon);
			if (ret != EtherEvent.EVENT_NOT_HANDLED) {
				return (ret);
			}
		}

		if (bkgnd != null) {
			ret = bkgnd.processObjEtherEvent(in, refcon);
			if (ret != EtherEvent.EVENT_NOT_HANDLED) {
				return (ret);
			}
		}

		if (pageSizeState != null) {
			ret = pageSizeState.processObjEtherEvent(in, refcon);
			if (ret != EtherEvent.EVENT_NOT_HANDLED) {
				return (ret);
			}
		}

		if (scp != null) {
			ret = scp.processObjEtherEvent(in, refcon);
			if (ret != EtherEvent.EVENT_NOT_HANDLED) {
				return (ret);
			}
		}

		if (in instanceof StandardEtherEvent) {
			if (in
				.getEtherID()
				.equals(StandardEtherEvent.makePropertiesEditor))
				return (makePropertiesEditor());

			if (in
				.getEtherID()
				.equals(StandardEtherEvent.showPropertiesEditor))
				showPropertiesEditor(in);

			if (in.getEtherID().equals(StandardEtherEvent.getUrlLocn)) {
				Object[] param = { fileSaveURL, fileSaveFlavor };
				return (param);
			}

			if (in.getEtherID().equals(StandardEtherEvent.setUrlLocn)) {
				Object[] param = (Object[]) (in.getParameter());
				fileSaveURL = (URL) (param[0]);
				fileSaveFlavor = (DataFlavor) (param[1]);
			}

			if (in.getEtherID().equals(StandardEtherEvent.objUndoableClose)) {
				PropL.firePropertyChange(
					ProgramDirector.propertyHide,
					null,
					null);
				return(null);
			}

		}

		return (EtherEvent.EVENT_NOT_HANDLED);
	}

	/**
	* Creates a property editor for the component.
	* @return The generated property editor.
	*/
	public VerdantiumPropertiesEditor makePropertiesEditor() {
		Properties MyP = new Properties();
		if (isScrolling())
			MyP.put("Scrolling", this);
		DefaultPropertyEditor MyEdit = new DefaultPropertyEditor(this, null);
		MyEdit.setClickPoint(new Point(10, 10));
		return (MyEdit);
	}

	/**
	* Displays a property editor for the component.
	* @param e The event requesting the property editor.
	*/
	public void showPropertiesEditor(EtherEvent e) {
	}

	/**
	* Handles a program Director event to embed a component frame.
	* @param e The input event.
	*/
	protected Object handleProgramDirectorEvent(ProgramDirectorEvent e)
		throws Throwable {
		return (null);
	}

	/**
	* Gets the background color.
	* @return The background color.
	*/
	public Color getBackgroundColor() {
		return (bkgnd.getBackgroundColor());
	}

	/**
	 * Gets whether the background is opaque.
	 * @return Whether the background is opaque.
	 */
	public boolean getOpaqueFlag() {
		return (bkgnd.isOpaqueFlag());
	}

	/**
	* Adds a property change listener.
	* @param e The property change listener to add.
	*/
	public void addPropertyChangeListener(PropertyChangeListener e) {
		if (PropL == null) {
			PropL = new PropertyChangeSupport(this);
		}
		PropL.addPropertyChangeListener(e);
	}
	
	/**
	* Removes a property change listener.
	* @param e The property change listener to remove.
	*/
	public void removePropertyChangeListener(PropertyChangeListener e) {
		if (PropL == null) {
			PropL = new PropertyChangeSupport(this);
		}
		PropL.removePropertyChangeListener(e);
	}

	/**
	 * Handles a change to whether only the designer can edit the component.
	 */
	public void handleOnlyDesignerEditsChange() {
	}

	/**
	* Returns whether only the designer can edit the component.
	* @return Whether only the designer can edit the component.
	*/
	public boolean isOnlyDesignerEdits() {
		return (onlyDesignerEdits.isOnlyDesignerEdits());
	}

	/**
	* Provides an interface so that the document size can be altered by a scrolling subclass.
	* @param in The desired page size.
	*/
	public void alterPageSize(Dimension in) throws IllegalInputException {
		if (pageSizeState != null) {
			pageSizeState.alterPageSize(in);
		}
	}

	/**
	 * Handles a change to the document page size.
	 */
	public void handlePageSizeChange() {
	}

	/**
	* Gets the component's document page size.
	* @return The component's document page size.
	*/
	public Dimension getPageSize() {
		return (pageSizeState.getPageSize());
	}

	/**
	* Returns that the component is non-scrolling.
	* @return That the component is non-scrolling.
	*/
	public boolean isScrolling() {
		return (false);
	}

	/**
	* Sets the background color of the component, and whether the component is opaque.
	* @param InC The color of the background.
	* @param Opaque Whether the background is opaque.
	*/
	public void handleBackgroundState(Color InC, boolean Opaque) {
	}

	/**
	* Handles the throwing of an error or exception.
	* @param in The input error or exception.
	*/
	public void handleThrow(Throwable in) {
		VerdantiumUtils.handleThrow(in, this, this);
	}

	/**
	* The property change support used to fire property change events.
	*/
	protected PropertyChangeSupport PropL = null;
	
}

