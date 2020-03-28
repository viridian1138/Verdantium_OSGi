package verdantium;

//import java.applet.Applet;
import java.awt.Point;
import java.net.URL;

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
*    | 9/24/2000             | Thorn Green (viridian_1138@yahoo.com)           | Needed to provide a standard way to document source file changes.    | Added a souce modification list to the documentation so that changes to the souce could be recorded. 
*    | 10/10/2000            | Thorn Green (viridian_1138@yahoo.com)           | Needed a standard way to load docs. from applets.                    | Created VerdantiumAutoLoader as a variant on geomdir.applied.VectorAutoLoader
*    | 10/22/2000            | Thorn Green (viridian_1138@yahoo.com)           | Methods did not have names that followed standard Java conventions.  | Performed a global modification to bring the names within spec.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
*    | 05/10/2002            | Thorn Green (viridian_1138@yahoo.com)           | Redundant information in persistent storage.                         | Made numerous persistence and packaging changes.
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
*
*
*/

/**
* An applet that loads and displays a Verdantium-compatible document.  No longer used.
* 
* @author Thorn Green, David Halliday
*/
public class VerdantiumAutoLoader_15 extends VerdantiumApplet_15 {
	
	/**
	 * String representation of URL for the host component to load as its state, or null if there is to be no such loading.
	 */
	transient protected String filename;
	
	/**
	 * String representation of URL for the host component to load and embed, or null if there is to be no such loading.
	 */
	transient protected String fembed;

	@Override
	public void init() {
		super.init();

		filename = getParameter("filename");
		fembed = getParameter("fembed");

		XKit myX = new XKit(this);

		try {
			if (filename != null) {
				URL u = myX.getBaseURL(filename, true, VerdantiumApplet.class);
				EtherEvent send =
					new StandardEtherEvent(
						this,
						StandardEtherEvent.objOpenEvent,
						u,
						myApp);
				ProgramDirector.fireEtherEvent(send, null);
			}

			if (fembed != null) {
				Point OutPt = new Point(20, 20);
				URL u = myX.getBaseURL(fembed, true, VerdantiumApplet.class);
				EtherEvent send =
					new ProgramDirectorEvent(
						this,
						ProgramDirectorEvent.loadURL,
						null,
						myApp);
				Object[] param = { null, OutPt, new Boolean(false)};
				param[0] = u;
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);
			}
		} catch (Throwable e) {
			throw (new WrapRuntimeException("File Load Failed", e));
		}

		if ((filename == null) && (fembed == null)) {
			throw (new RuntimeException("File Not Specified"));
		}
	}

};

