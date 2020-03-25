package verdantium;

import java.awt.Point;
import java.net.URL;

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
* EtherEvent suite for the creation of components and the loading
* of files from URLs (using components).  The event class contains several methods for reading
* parameter objects from the event.
* 
* @author Thorn Green
*/
public class ProgramDirectorEvent extends EtherEvent {
	
	/**
	 * Ether Event name used to ask a component whether it supports program director events.
	 */
	public static final String isProgramDirectorEventSupported =
		"isProgramDirectorEventSupported";
	
	/**
	 * Ether Event name used to request the creation and embedding of a component.
	 */
	public static final String createApp = "createApp";
	
	/**
	 * Ether Event name used to request the loading and embedding of a URL's content.
	 */
	public static final String loadURL = "loadURL";

	/**
	* Constructor for the ProgramDirectorEvent.
	* @param source The source of the event.
	* @param EtherID The ID of the event.
	* @param param The parameter(s) of the event.
	* @param tar The target to which the event is to be delivered.
	*/
	public ProgramDirectorEvent(
		Object source,
		String EtherID,
		Object param,
		Object tar) {
		super(source, EtherID, param, tar);
	}

	/**
	* Constructor for the ProgramDirectorEvent.
	* @param source The source of the event.
	* @param EtherID The ID of the event.
	* @param old_param The older previous set of parameters.
	* @param param The parameter(s) of the event.
	* @param tar The target to which the event is to be delivered.
	*/
	public ProgramDirectorEvent(
		Object source,
		String EtherID,
		Object old_param,
		Object param,
		Object tar) {
		super(source, EtherID, old_param, param, tar);
	}

	/**
	* Gets the program name, if applicable.  Otherwise returns null.
	* @return The program name, if applicable.  Otherwise returns null.
	*/
	public String getProgramName() {
		String ret = null;
		if (getEtherID().equals(createApp)) {
			Object[] param = (Object[]) (getParameter());
			ret = (String) (param[0]);
		}

		return (ret);
	}

	/**
	* Gets the program URL, if applicable.  Otherwise returns null.
	* @return The program URL, if applicable.  Otherwise returns null.
	*/
	public URL getProgramURL() {
		URL ret = null;
		if (getEtherID().equals(loadURL)) {
			Object[] param = (Object[]) (getParameter());
			ret = (URL) (param[0]);
		}

		return (ret);
	}

	/**
	* Gets whether the operation should use {@link verdantium.core.FileWatcher}, if applicable.  Otherwise returns null.
	* @return Whether the operation should use {@link verdantium.core.FileWatcher}, if applicable.  Otherwise returns null.
	*/
	public boolean getUseFileWatcher() {
		boolean ret = false;
		if (getEtherID().equals(loadURL)) {
			Object[] param = (Object[]) (getParameter());
			ret = ((Boolean) (param[2])).booleanValue();
		}

		return (ret);
	}

	/**
	* Gets the embedding location, if applicable.  Otherwise returns null.
	* @return The embedding location, if applicable.  Otherwise returns null.
	*/
	public Point getClickPoint() {
		Point ret = null;
		if (!(getEtherID().equals(isProgramDirectorEventSupported))) {
			Object[] param = (Object[]) (getParameter());
			ret = (Point) (param[1]);
		}

		return (ret);
	}

	
}

