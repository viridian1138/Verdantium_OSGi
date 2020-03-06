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
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Macro support.                                                       | Started adding events for macro support, then wound up moving them to their own event class.
*    | 04/21/2002            | Thorn Green (viridian_1138@yahoo.com)           | Find/Replace support.                                                | Added find/replace support.
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
* Provides an EtherEvent suite for property editors.
* 
* @author Thorn Green
*/
public class PropertyEditEtherEvent extends EtherEvent {
	
	/**
	* Ether Event name for determining if a component supports border alterations.
	*/
	public static final String isBorderSupported = "isBorderSupported";
	
	/**
	* Ether Event name for setting the border of a component.
	*/
	public static final String setBorderObject = "setBorderObject";
	
	/**
	* Ether Event name for determining if the component supports alterations to its background.
	*/
	public static final String isBackgroundSupported = "isBackgroundSupported";
	
	/**
	* Ether Event name for setting the background color (and possibly transparency) of a component.
	*/
	public static final String setBackgroundState = "setBackgroundState";
	
	/**
	* Ether Event name for setting whether only the designer can edit the component.
	*/
	public static final String setOnlyDesignerEdit = "setOnlyDesignerEdit";
	
	/**
	* Ether Event name for setting the document page size of a component.
	*/
	public static final String setPageSize = "setPageSize";

	/**
	* Ether Event name for determining if find/replace iterators are supported.
	*/
	public static final String isFindReplaceIteratorSupported =
		"isFindReplaceIteratorSupported";
	
	/**
	* Ether Event name for an optional event that gets a find/replace iterator.
	*/
	public static final String createFindReplaceIterator =
		"createFindReplaceIterator";

	/**
	 * Ether Event name for an optional event to set the horizontal scroll value.
	 */
	public static final String setHorizScroll = "setHorizScroll";
	
	/**
	 * Ether event name for an optional event to set the vertical scroll value.
	 */
	public static final String setVertScroll = "setVertScroll";


	/**
	* Constructor for the PropertyEditEtherEvent.
	* @param source The source of the event.
	* @param EtherID The ID of the event.
	* @param param The parameter(s) of the event.
	* @param tar The target to which the event is to be delivered.
	*/
	public PropertyEditEtherEvent(
		Object source,
		String EtherID,
		Object param,
		Object tar) {
		super(source, EtherID, param, tar);
	}

	/**
	* Constructor for the PropertyEditEtherEvent.
	* @param source The source of the event.
	* @param EtherID The ID of the event.
	* @param old_param The older previous set of parameters.
	* @param param The parameter(s) of the event.
	* @param tar The target to which the event is to be delivered.
	*/
	public PropertyEditEtherEvent(
		Object source,
		String EtherID,
		Object old_param,
		Object param,
		Object tar) {
		super(source, EtherID, old_param, param, tar);
	}

	
}


