package verdantium;

import java.beans.PropertyChangeEvent;

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
* EtherEvent provides a class for inter-component communication, for organizing event flow,
* reducing the number of interfaces that a component class has to implement, and
* for making events scriptable.
* 
* @author Thorn Green
*/
public class EtherEvent extends PropertyChangeEvent {

	/**
	 * Event result indicating that the event wasn't handled.
	 */
	public static final Object EVENT_NOT_HANDLED = new Object() {
	};

	/**
	 * Creates an EtherEvent from object <code>source</code> with type <code>EtherID</code>,
	 * and parameter <code>param</code> to be fired at object <code>tar</code>.
	 * @param source The source of the event.
	 * @param etherID The ID of the event.
	 * @param param The parameter(s) of the event.
	 * @param tar The target to which the event is to be delivered.
	 */
	public EtherEvent(
		Object source,
		String etherID,
		Object param,
		Object tar) {
		super(source, etherID, null, param);
		paramObj = param;
		target = tar;
	}

	/**
	 * Creates an EtherEvent from object <code>source</code> with type <code>EtherID</code>,
	 * "old parameter" <code>old_param</code>, and parameter <code>param</code> to be fired at 
	 * object <code>tar</code>.  The "old parameter" is not used in this version of Verdantium.
	 * @param source The source of the event.
	 * @param etherID The ID of the event.
	 * @param old_param The older previous set of parameters.
	 * @param param The parameter(s) of the event.
	 * @param tar The target to which the event is to be delivered.
	 */
	public EtherEvent(
		Object source,
		String etherID,
		Object old_param,
		Object param,
		Object tar) {
		super(source, etherID, old_param, param);
		paramObj = param;
		target = tar;
	}

	/**
	* Gets the parameter(s) of the event.
	* @return The parameter(s) of the event.
	*/
	public Object getParameter() {
		return (paramObj);
	}

	/**
	* Sets the parameter(s) of the event.
	* @param in The parameter(s) of the event.
	*/
	public void setParameter(Object in) {
		paramObj = in;
	}

	/**
	* Gets the new value.
	* @return The new value.
	*/
	public Object getNewValue() {
		return (getParameter());
	}

	/**
	* Gets the ID of the event.
	* @return The ID of the event.
	*/
	public String getEtherID() {
		return (getPropertyName());
	}

	/**
	* Gets the target to which the event is to be delivered.
	* @return The target to which the event is to be delivered.
	*/
	public Object getTarget() {
		return (target);
	}

	/**
	* Sets the target to which the event is to be delivered.
	* @param in The target to which the event is to be delivered.
	*/
	public void setTarget(Object in) {
		target = in;
	}

	/**
	 * The parameter(s) of the event.
	 */
	transient private Object paramObj = null;
	
	/**
	 * The target to which the event is to be delivered.
	 */
	transient private Object target = null;
	
}

