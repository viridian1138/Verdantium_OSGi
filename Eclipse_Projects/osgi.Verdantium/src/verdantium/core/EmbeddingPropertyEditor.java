package verdantium.core;

import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import verdantium.EtherEvent;
import verdantium.EtherEventPropertySource;
import verdantium.ProgramDirectorPropertyEditor;

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
* A class that broadcasts a a property change event every time it receives an
* embedding request from the user.
* 
* @author Thorn Green
*/
public class EmbeddingPropertyEditor extends ProgramDirectorPropertyEditor {

	/**
	* Creates an embedding property editor.
	* @param in The data model of the component being edited.
	* @param inPt The location at which the next frame should be embedded, or null.
	*/
	public EmbeddingPropertyEditor(EtherEventPropertySource in, Point inPt) {
		super(in, inPt);
		propL = new PropertyChangeSupport(this);
	}

	/**
	* Adds a property change listener to the class.
	* @param in The listener to add.
	*/
	public void addPropertyChangeListener(PropertyChangeListener in) {
		propL.addPropertyChangeListener(in);
	}

	/**
	* Removes a property change listener from the class.
	* @param in The listener to remove.
	*/
	public void removePropertyChangeListener(PropertyChangeListener in) {
		propL.removePropertyChangeListener(in);
	}

	/**
	* Fires the program director ether event.  Override this method to have the ether event go someplace else.
	* @param send The event to send.
	* @param refcon A reference to context data for the event.
	* @return The result of handling the event, or null if there is no result.
	*/
	protected Object fireProgramDirectorEtherEvent(
		EtherEvent send,
		Object refcon) {
		final Object[] chgEventArray = { send, refcon, null };
		propL.firePropertyChange(propertyChgName, null, chgEventArray);
		return (chgEventArray[2]);
	}

	/**
	* Sets the property change name used when events are fired.
	* @param in The property change name used when events are fired.
	*/
	public void setPropertyChgName(String in) {
		propertyChgName = in;
	}

	/**
	* Gets the property change name used when events are fired.
	* @return The property change name used when events are fired.
	*/
	public String getPropertyChgName() {
		return (propertyChgName);
	}

	/**
	* Holds the default name of the property change event fired by this class.
	*/
	public static final String DefaultPropertyChgName =
		"EmbeddingPropertyEditorEvent";

	/**
	* Holds the name of the property change event fired by the class.
	*/
	protected String propertyChgName = DefaultPropertyChgName;

	/**
	* Property change support object that allows the class to broadcast requests made to it.
	*/
	private PropertyChangeSupport propL = null;
	
}

