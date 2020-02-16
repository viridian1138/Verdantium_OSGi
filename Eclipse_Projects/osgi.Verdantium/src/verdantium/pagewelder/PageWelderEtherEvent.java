package verdantium.pagewelder;

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
*    | 03/09/2003            | Thorn Green (viridian_1138@yahoo.com)           | PageWelder.                                                          | Implemented PageWelder using code from other classes.
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
public class PageWelderEtherEvent extends EtherEvent {
	
	/**
	* Ether Event name for going back by one card.
	*/
	public static final String backCard = "backCard";
	
	/**
	* Ether Event name for going to the first card.
	*/
	public static final String firstCard = "firstCard";
	
	/**
	* Ether Event name for going to the previous card.
	*/
	public static final String prevCard = "prevCard";
	
	/**
	* Ether Event name for going to the next card.
	*/
	public static final String nextCard = "nextCard";
	
	/**
	* Ether Event name for going to the last card.
	*/
	
	public static final String lastCard = "lastCard";

	/**
	* Ether Event name for adding a card.
	*/
	public static final String addCard = "addCard";
	
	/**
	* Ether Event name for deleting a card.
	*/
	public static final String deleteCard = "deleteCard";

	/**
	* Ether Event name for running PrintPreview on the stack.
	*/
	public static final String printPreviewStack = "printPreviewStack";
	
	/**
	* Ether Event name for printing a stack.
	*/
	public static final String printStack = "printStack";

	/**
	* EtherEvent name for setting PageWelder button link.
	*/
	public static final String pwButtonSetLink = "pwButtonSetLink";

	/**
	* Ether Event name for editing the bkgnd.
	*/
	public static final String editBkgnd = "editBkgnd";

	/**
	* Constructor for the PageWelderEtherEvent.
	* @param source The source of the event.
	* @param EtherID The ID of the event.
	* @param param The parameter(s) of the event.
	* @param tar The target to which the event is to be delivered.
	*/
	public PageWelderEtherEvent(
		Object source,
		String EtherID,
		Object param,
		Object tar) {
		super(source, EtherID, param, tar);
	}

	/**
	* Constructor for the PageWelderEtherEvent.
	* @param source The source of the event.
	* @param EtherID The ID of the event.
	* @param old_param The older previous set of parameters.
	* @param param The parameter(s) of the event.
	* @param tar The target to which the event is to be delivered.
	*/
	public PageWelderEtherEvent(
		Object source,
		String EtherID,
		Object old_param,
		Object param,
		Object tar) {
		super(source, EtherID, old_param, param, tar);
	}

	
}

