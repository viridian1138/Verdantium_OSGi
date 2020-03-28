package verdantium;

import java.awt.Color;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import jundo.runtime.ExtMilieuRef;
import jundo.runtime.IExtPair;
import jundo.util.pdx_HashMapSh_pdx_ObjectRef;
import verdantium.undo.UndoManager;
import verdantium.xapp.BackgroundState;
import verdantium.xapp.BackgroundStateHandler;

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
*    | 05/12/2002            | Thorn Green (viridian_1138@yahoo.com)           | Rendering bug.                                                       | Re-arranged desktop manager functionality to fix the bug.
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
* A desktop pane that fixes certain rendering issues in JDesktopPane.
* 
* @author Thorn Green
*/
public class VerdantiumDesktopPane
	extends JDesktopPane
	implements BackgroundStateHandler {

	/**
	 * Undoable data reference for the pane's background color and opaqueness.
	 */
	transient protected BackgroundState bkgnd = null;
	
	/**
	 * Whether the pane is currently opaque.
	 */
	transient protected boolean opaqueFlag = true;
	
	/**
	 * Undoable map describing which internal frames are in the pane.
	 */
	transient protected pdx_HashMapSh_pdx_ObjectRef map = null;

	/**
	 * Constructor.
	 * @param _undoMgr The undo manager for the component associated with the desktop pane.
	 */
	public VerdantiumDesktopPane(UndoManager _undoMgr) {
		super();
		setDesktopManager(new VerdantiumDesktopManager());
		bkgnd = new BackgroundState(_undoMgr);
		bkgnd.setBackgroundState(getBackground(), true);
		bkgnd.configureForEtherEvents(this, null);
		ExtMilieuRef mil = _undoMgr.getCurrentMil();
		IExtPair pair =
			pdx_HashMapSh_pdx_ObjectRef.pdxm_allocate_HashMapSh(mil);
		map = (pdx_HashMapSh_pdx_ObjectRef) (pair.getObject());
		mil = pair.getMilieu();
		_undoMgr.handleCommitTempChange(mil);
	}

	/**
	 * Gets the undo manager for the component associated with the desktop pane.
	 * @return The undo manager for the component associated with the desktop pane.
	 */
    public UndoManager getUndoMgr()
    {
         return( bkgnd.getUndoMgr() );
    }

	/**
	* Returns whether the pane is opaque.
	* Whether the pane is opaque.
	*/
	public boolean getOpaqueFlag() {
		return (opaqueFlag);
	}
	
	/**
	* Sets whether the pane is opaque.
	* @param in Whether the pane is opaque.
	*/
	public void setOpaqueFlag(boolean in) {
		bkgnd.setBackgroundState(getBackground(), in);
	}

	/**
	* Returns whether the pane is opaque.
	* @return Whether the pane is opaque.
	*/
	public boolean isOpaque() {
		return (opaqueFlag);
	}

	/**
	 * Handles a change to the background color and/or opacity.
	 * @param inC The background color to buse for the pane.
	 * @param opaque Whether the pane is to be opaque.
	 */
	public void handleBackgroundState(Color inC, boolean opaque) {
		super.setBackground(inC);
		opaqueFlag = opaque;
		repaint();
	}

	/**
	 * Sets the background color of the pane.
	 * @param in The background color of the pane.
	 */
	public void setBackground(Color in) {
		if (bkgnd != null) {
			bkgnd.setBackgroundState(in, getOpaqueFlag());
		} else {
			super.setBackground(in);
		}

	}

	/**
	 * Gets the undoable data reference for the pane's background color and opaqueness.
	 * @return The undoable data reference for the pane's background color and opaqueness.
	 */
	public BackgroundState getBkgnd() {
		return (bkgnd);
	}

	/**
	 * Gets the undoable map describing which internal frames are in the pane.
	 * @return The undoable map describing which internal frames are in the pane.
	 */
	public pdx_HashMapSh_pdx_ObjectRef getMap() {
		return (map);
	}

	/**
	 * Handles the destruction of the desktop pane.
	 */
	public void handleDestroy() {
		JInternalFrame[] allFrames = getAllFrames();
		int max = allFrames.length;
		int count;
		for (count = 0; count < max; ++count) {
			try {
				allFrames[count].dispose();
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
		bkgnd.handleDestroy();
	}
	

}

