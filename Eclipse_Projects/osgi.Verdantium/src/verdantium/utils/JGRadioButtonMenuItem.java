package verdantium.utils;

import java.awt.event.ActionListener;

import javax.swing.JRadioButtonMenuItem;

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
* An implementation of a radio button menu item with an item number and an object associated with it.
* This makes it easier to track the event coming from the item.
*
*@author Thorn Green
*/
public class JGRadioButtonMenuItem extends JRadioButtonMenuItem {
	
	/**
	* Constructs a radio button menu item with a label, a state, a boolean value,
	* an integer item number, and an action listener.
	* @param Label The label for the menu item.
	* @param InTag The item number associated with the menu item.
	* @param InL Action listener to receive button events.
	*/
	public JGRadioButtonMenuItem(String Label, int InTag, ActionListener InL) {
		super(Label);
		Tag = InTag;
		addActionListener(InL);
	}

	/**
	* Constructs a radio button menu item with a label, a state, a boolean value,
	* an integer item number, an action listener, and an identifying object.
	* @param Label The label for the menu item.
	* @param InTag The item number associated with the menu item.
	* @param InL Action listener to receive button events.
	* @param InR A reference to context data that the generating code can associate with the menu item.
	*/
	public JGRadioButtonMenuItem(String Label, int InTag, ActionListener InL, Object InR) {
		super(Label);
		Tag = InTag;
		addActionListener(InL);
		RefCon = InR;
	}

	/**
	* Gets the item number associated with the menu item.
	* @return The item number associated with the menu item.
	*/
	public int getTag() {
		return (Tag);
	}

	/**
	* Gets a reference to context data that the generating code can associate with the menu item.
	* @return A reference to context data that the generating code can associate with the menu item.
	*/
	public Object getRefCon() {
		return (RefCon);
	}

	/**
	 * A reference to context data that the generating code can associate with the menu item.  See various references to "refcon" in MacOS programming.
	 */
	transient private Object RefCon = null;
	
	/**
	 * The item number associated with the menu item.
	 */
	transient private int Tag;
	
}

