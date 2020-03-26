package verdantium;

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
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
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
* This is an EtherEvent suite that contains standard Ether Events for Verdantium.
* All Verdantium components support objNewEvent, objOpenEvent, objSaveAsEvent,
* objPrintEvent, and objQuitEvent without the need for explicit handlers.  Verdantium
* automatically handles these events on behalf of the Verdantium component and then
* routes calls to the appropriate methods.  All components with a single kind of
* property editor should support makePropertiesEditor and showPropertiesEditor.  The
* other events are used by partucular components.  For instance, setEditorMode is
* handled by {@link verdantium.core.EditorControl}.  Support for objUndoEvent is
* optional.  Components that may be used in applets should implement appletStartEvent
* and appletStopEvent (which are fired when the applet starts or stops).  The code that handles 
* appletStartEvent and appletStopEvent should fire appletStartEvent and appletStopEvent to its 
* sub-components, but for property editors in separate windows or internal frames it should fire 
* compStartEvent and compStopEvent.
* 
* @author Thorn Green
*/
public class StandardEtherEvent extends EtherEvent {
	
	/**
	* Ether Event name to create a property editor and return it.
	*/
	public static final String makePropertiesEditor = "makePropertiesEditor";
	
	/**
	* Ether Event name to create and show a property editor.
	*/
	public static final String showPropertiesEditor = "showPropertiesEditor";
	
	/**
	* Ether Event name to ask a component to create a new document.
	*/
	public static final String objNewEvent = "objNewEvent";
	
	/**
	* Ether Event name to open a new document from a URL.
	*/
	public static final String objOpenEvent = "objOpenEvent";
	
	/**
	* Ether Event name to execute "Save As" on the current document.
	*/
	public static final String objSaveAsEvent = "objSaveAsEvent";
	
	/**
	* Ether Event name to execute "Save A Copy As" on the current document.
	*/
	public static final String objSaveACopyAsEvent = "objSaveACopyAsEvent";
	
	/**
	* Ether Event name to display the Page Setup editor on the current document.
	*/
	public static final String objPageSetupEvent = "objPageSetupEvent";
	
	/**
	* Ether Event name to display the Custom Page Setup editor on the current document.
	*/
	public static final String objCustomPageSetupEvent = "objCustomPageSetupEvent";
	
	/**
	* Ether Event name to display the Print Preview editor on the current document.
	*/
	public static final String objPrintPreviewEvent = "objPrintPreviewEvent";
	
	/**
	* Ether Event name to execute "Print" on the current document.
	*/
	public static final String objPrintEvent = "objPrintEvent";
	
	/**
	* Ether Event name to execute the "Quit" command.
	*/
	public static final String objQuitEvent = "objQuitEvent";
	
	/**
	 * Ether Event name to ask the class of a component to create a new document.
	 */
	public static final String classNewEvent = "classNewEvent";
	
	/**
	 * Ether Event name to ask the Program Director to load a new persistent file.
	 */
	public static final String loadNewPersistentFile = "loadNewPersistentFile";
	
	/**
	 * Ether Event name requesting that the EditorControl component set the editing mode.
	 */
	public static final String setEditorMode = "setEditorMode";
	
	/**
	 * Ether Event name for requesting that the DesignerControl component set its "User Mode" vs. "Designer Mode" state.
	 */
	public static final String setDesignTime = "setDesignTime";
	
	/**
	 * Ether Event name for a component to perform an undo operation.
	 */
	public static final String objUndoEvent = "objUndoEvent";
	
	/**
	 * Ether Event name to perform an undoable close of the current document.
	 */
	public static final String objUndoableClose = "objUndoableClose";
	
	/**
	 * Ether Event name to ask the class of a component to get the source of the event.
	 */
	public static final String getEventSource = "getEventSource";
	
	/**
	* Ether Event name to get the URL of the current document.
	*/
	public static final String getUrlLocn = "getUrlLocn";
	
	/**
	* Ether Event name to set the URL of the current document.
	*/
	public static final String setUrlLocn = "setUrlLocn";
	
	/**
	* Ether Event name to get the page format of the current document.
	*/
	public static final String getDocPageFormat = "getDocPageFormat";
	
	/**
	* Ether Event name to set the page format of the current document.
	*/
	public static final String setDocPageFormat = "setDocPageFormat";
	
	/**
	* Ether Event name to execute the "drop" part of a drag-and-drop operation
	* with the item being dragged in as an input parameter.
	*/
	public static final String dropComponent = "dropComponent";

	
	/**
	* Constructor for the StandardEtherEvent.
	* @param source The source of the event.
	* @param etherID The ID of the event.
	* @param param The parameter(s) of the event.
	* @param tar The target to which the event is to be delivered.
	*/
	public StandardEtherEvent(Object source, String etherID, Object param, Object tar) {
		super(source, etherID, param, tar);
	}

	/**
	* Constructor for the StandardEtherEvent.
	* @param source The source of the event.
	* @param etherID The ID of the event.
	* @param old_param The older previous set of parameters.
	* @param param The parameter(s) of the event.
	* @param tar The target to which the event is to be delivered.
	*/
	public StandardEtherEvent(Object source, String etherID, Object old_param, Object param, Object tar) {
		super(source, etherID, old_param, param, tar);
	}

	
}


