package verdantium.xapp;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import jundo.runtime.ExtMilieuRef;
import jundo.runtime.IExtPair;
import jundo.util.pdx_JobjRef_pdx_ObjectRef;
import verdantium.core.EditorControl;
import verdantium.undo.UTag;
import verdantium.undo.UndoManager;

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
*    | 09/25/2005            | Thorn Green (viridian_1138@yahoo.com)           | Improved undo support.                                               | Created class.
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
* An undoable data reference for the only designer edits flag.
* 
* @author Thorn Green
*/
public class EditorUndoBridge implements PropertyChangeListener {

	/**
	 * Only designer edits reference.
	 */
	protected pdx_JobjRef_pdx_ObjectRef editorBridge = null;

	/**
	* UndoManager through which to support undo.
	*/
	protected transient UndoManager undoMgr = null;

	/**
	 * Whether the state of the EditorUndoBridge is updating.
	 */
	protected transient boolean updating = false;

	/**
	* Constructs the only designer edits reference for a UndoManager.
	* @param _undoMgr The input UndoManager.
	*/
	public EditorUndoBridge(UndoManager _undoMgr) {
		undoMgr = _undoMgr;
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		IExtPair pair = pdx_JobjRef_pdx_ObjectRef.pdxm_allocate_JobjRef(mil);
		mil = pair.getMilieu();
		editorBridge = (pdx_JobjRef_pdx_ObjectRef) (pair.getObject());
		mil =
			editorBridge.pdxm_setVal(
				mil,
				new Integer(EditorControl.getEditorMode()));
		undoMgr.handleCommitTempChange(mil);
		undoMgr.addPropertyChangeListener(this);
		EditorControl.addPropertyChangeListener(this);
	}

	/**
	 * Handles the destruction of the EditorUndoBridge.
	 */
	public void handleDestroy() {
		undoMgr.removePropertyChangeListener(this);
		EditorControl.removePropertyChangeListener(this);
	}

	/**
	 * Handles a change to the multi-level undo state.
	 */
	protected void handleUndoChange() {
		if (!updating) {
			updating = true;
			Object ob = editorBridge.pdxm_getVal(undoMgr.getCurrentMil());
			Integer intv = (Integer) ob;
			int imode = intv.intValue();
			if (imode != EditorControl.getEditorMode())
				EditorControl.setEditorMode(imode);
			updating = false;
		}
	}

	/**
	 * Handles a change to whether only the designer can edit the target component.
	 */
	protected void handleEditorChange() {
		if (!updating) {
			updating = true;
			UTag utag = new UTag();

			Integer emode = new Integer(EditorControl.getEditorMode());
			undoMgr.prepareForTempCommit(utag);
			ExtMilieuRef mil =
				editorBridge.pdxm_setVal(undoMgr.getCurrentMil(), emode);
			undoMgr.handleCommitTempChange(mil);
			undoMgr.commitUndoableOp(utag, "Editor Change");
			updating = false;
		}
	}

	/**
	* Handles property change events.
	* @param e The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == UndoManager.MODEL_STATE_CHANGED) {
			handleUndoChange();
		}

		if (evt.getPropertyName() == EditorControl.EditCntlChange) {
			handleEditorChange();
		}
	}

	
};

