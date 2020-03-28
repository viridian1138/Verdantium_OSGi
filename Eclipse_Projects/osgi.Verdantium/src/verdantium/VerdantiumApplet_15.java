package verdantium;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JApplet;

import meta.WrapRuntimeException;

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
*    | 10/07/2000            | Thorn Green (viridian_1138@yahoo.com)           | Needed to provide a buffer against future applet security problems.  | Created this class to buffer the actual applet class from VerdantiumApplet. 
*    | 10/22/2000            | Thorn Green (viridian_1138@yahoo.com)           | Methods did not have names that followed standard Java conventions.  | Performed a global modification to bring the names within spec.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 05/10/2002            | Thorn Green (viridian_1138@yahoo.com)           | Redundant information in persistent storage.                         | Made numerous persistence and packaging changes.
*    | 10/05/2002            | Thorn Green (viridian_1138@yahoo.com)           | Run GeoCard from web.                                                | Debugged and made changes to run GeoCard from web.
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
* An applet that displays a Verdantium component in a browser.  No longer used.
* 
* @author Thorn Green
*/
public class VerdantiumApplet_15 extends JApplet {
	
	/**
	* Property change event label indicating the start of an applet.
	*/
	public static final String appletStart = "appletStart";
	
	/**
	* Property change event label indicating the stopping of an applet.
	*/
	public static final String appletStop = "appletStop";

	/**
	* The XKit for the applet.
	*/
	transient protected XKit myX;
	
	/**
	* The component for the applet.
	*/
	transient protected VerdantiumComponent myApp;
	
	/**
	 * Whether the VM is running an applet.
	 */
	private static boolean apppletActivated = false;
	
	/**
	 * Support for class-level property changes.
	 */
	private static PropertyChangeSupport propSL =
		new PropertyChangeSupport("Verdantium Applet");

	@Override
	public void init() {
		try {
			apppletActivated = true;
			Class<? extends VerdantiumComponent> myClass = (Class<? extends VerdantiumComponent>)( Class.forName(getParameter("class")) );
			myX = new XKit(this);
			myApp = myClass.newInstance();
			ProgramDirector.showComponent(myApp, /* getContentPane() */
			this, "Embedded Applet");
			Component myComp = myApp.getGUI();
			invalidate();
			myComp.invalidate();
			validate();
			myComp.validate();
		} catch (Exception e) {
			throw (new WrapRuntimeException("Applet Init Failed", e));
		}
	}

	@Override
	public void start() {
		propSL.firePropertyChange(appletStart, null, null);
	}

	@Override
	public void stop() {
		propSL.firePropertyChange(appletStop, null, null);
	}

	/**
	 * Fires an applet property change to all listeners.
	 * @param propertyName The name of the property that changed.
	 * @param oldValue The old value of the property.
	 * @param newValue The new value of the property.
	 */
	public static void fireAppletPropertyChange(
		String propertyName,
		Object oldValue,
		Object newValue) {
		propSL.firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	* Adds a property change listener to the class.
	* @param in The listener to be added.
	*/
	public static void addClassPropertyChangeListener(PropertyChangeListener in) {
		propSL.addPropertyChangeListener(in);
	}

	/**
	* Removes a property change listener from the class.
	* @param in The listener to be removed.
	*/
	public static void removeClassPropertyChangeListener(PropertyChangeListener in) {
		propSL.removePropertyChangeListener(in);
	}

	/**
	* Returns whether the VM is running an applet.
	* @return Whether the VM is running an applet.
	*/
	public static boolean isAppletActivated() {
		return (apppletActivated);
	}

	/**
	* Sets whether the VM is running an applet.
	* @param in Whether the VM is running an applet.
	*/
	public static void setAppletActivated(boolean in) {
		apppletActivated = in;
	}

	
}


