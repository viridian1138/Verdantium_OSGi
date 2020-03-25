package verdantium;

import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

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
*    | 01/28/2001            | Thorn Green (viridian_1138@yahoo.com)           | Multiple bugs in calling of handleDestroy()                          | Implemented a set of bug-fixes.
*    | 02/04/2001            | Thorn Green (viridian_1138@yahoo.com)           | Recursion problems.                                                  | Tightened recursion checks.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
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
* Internal Verdantium adapter that handles window close.
* 
* @author Thorn Green
*/
class MgrInternalFrameAdapter extends InternalFrameAdapter {
	
	/**
	 * Whether an event to close the frame has been posted.
	 */
	protected boolean posted = false;

	/**
	 * Constructor.
	 * @param in The component embedded within the internal frame.
	 */
	public MgrInternalFrameAdapter(VerdantiumComponent in) {
		myComp = in;
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
		handleCloseEvent(e);
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
		handleCloseEvent(e);
	}

	/**
	 * Handles a request to close the internal frame.
	 * @param e The input event.
	 */
	protected void handleCloseEvent(InternalFrameEvent e) {
		try {
			if (e.getSource() instanceof VerdantiumUndoableInternalFrame) {
				StandardEtherEvent evt =
					new StandardEtherEvent(
						"Program Director",
						StandardEtherEvent.objUndoableClose,
						null,
						myComp);
				myComp.processObjEtherEvent(evt, null);
			} else {
				boolean tmp = posted;
				posted = true;
				if (!tmp) {
					myComp.handleDestroy();
				}
			}
		} catch (Throwable ex) {
			ex.printStackTrace(System.out);
		}
	}

	/**
	 * The component embedded within the internal frame.
	 */
	private VerdantiumComponent myComp = null;
	
}

