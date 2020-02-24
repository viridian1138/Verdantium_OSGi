package verdantium.undo;

import verdantium.EtherEvent;

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
* This is an EtherEvent suite that contains standard Ether Events for Undo.
* 
* @author Thorn Green
*/
public class UndoEtherEvent extends EtherEvent {
	
	/**
	 * Standard EtherEvent ID to ask a component whether multi-level undo is supported.
	 */
	public static final String isUndoSupported = "isUndoSupported";
	
	/**
	 * Standard EtherEvent ID to ask a component for its undo manager.
	 */
	public static final String getUndoManager = "getUndoManager";
	
	/**
	 * Standard EtherEvent ID to ask a component to clear its undo memory.
	 */
	public static final String clearUndoMemory = "clearUndoMemory";

	/**
	 * Constructor.
	 * @param source The source of the event.
	 * @param EtherID The ID of the event.
	 * @param param The parameters(s) of the event, or null if there are no parameters.
	 * @param tar The target of the event.
	 */
	public UndoEtherEvent(
		Object source,
		String EtherID,
		Object param,
		Object tar) {
		super(source, EtherID, param, tar);
	}

	/**
	 * Constructor.
	 * @param source The source of the event.
	 * @param EtherID The ID of the event.
	 * @param old_param The previous set of parameters that was used for the event.
	 * @param param The parameters(s) of the event, or null if there are no parameters.
	 * @param tar The target of the event.
	 */
	public UndoEtherEvent(
		Object source,
		String EtherID,
		Object old_param,
		Object param,
		Object tar) {
		super(source, EtherID, old_param, param, tar);
	}

	
}

