package verdantium.core;

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
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Client macro support.                                                | Initial creation of client macro event class by copying code from another class.
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
* Provides an EtherEvent suite for property editors.
* 
* @author Thorn Green
*/
public class ClientMacroEtherEvent extends EtherEvent {
	
	/**
	* Ether Event name for determining if client macros are supported.
	*/
	public static final String isClientMacroSupported =
		"isClientMacroSupported";
	
	/**
	* Ether Event name for setting a client macro.
	*/
	public static final String setClientMacro = "setClientMacro";
	
	/**
	* Ether Event name for running a client macro. (MacroRecorder.playClientMacro).
	*/
	public static final String runClientMacro = "runClientMacro";
	
	/**
	* Ether Event name for deleting a client macro.
	*/
	public static final String deleteClientMacro = "deleteClientMacro";
	
	/**
	* Ether Event name for getting a macro name iterator.
	*/
	public static final String getClientMacroNameIterator =
		"getClientMacroNameIterator";

	/**
	* Property change event name indicating a change to a macro map.
	*/
	public static final String MACRO_MAP_CHANGED = "MACRO_MAP_CHANGED";

	/**
	* Constructor for the ClientMacroEtherEvent.
	* @param source The source of the event.
	* @param EtherID The ID of the event.
	* @param param The parameter(s) of the event.
	* @param tar The target to which the event is to be delivered.
	*/
	public ClientMacroEtherEvent(
		Object source,
		String EtherID,
		Object param,
		Object tar) {
		super(source, EtherID, param, tar);
	}

	/**
	* Constructor for the ClientMacroEtherEvent.
	* @param source The source of the event.
	* @param EtherID The ID of the event.
	* @param old_param The older previous set of parameters.
	* @param param The parameter(s) of the event.
	* @param tar The target to which the event is to be delivered.
	*/
	public ClientMacroEtherEvent(
		Object source,
		String EtherID,
		Object old_param,
		Object param,
		Object tar) {
		super(source, EtherID, old_param, param, tar);
	}

	
}

