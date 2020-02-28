package verdantium.xapp;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import jundo.runtime.ExtMilieuRef;
import jundo.runtime.IExtPair;
import jundo.util.pdx_JobjRef_pdx_ObjectRef;
import verdantium.EtherEvent;
import verdantium.EtherEventHandler;
import verdantium.core.DesignerPropertyEditor;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.undo.UTag;
import verdantium.undo.UndoManager;
import verdantium.utils.PropertyChangeFire;

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
public class OnlyDesignerEdits
	implements EtherEventHandler, PropertyChangeListener {

	/**
	 * Only designer edits reference.
	 */
	protected pdx_JobjRef_pdx_ObjectRef onlyDesignerEdits = null;

	/**
	* Property change supports through which to send events
	*/
	protected transient PropertyChangeSupport[] PropLa =
		new PropertyChangeSupport[0];

	/**
	* Property change fires through which to send events.
	*/
	protected transient PropertyChangeFire[] PropLb = new PropertyChangeFire[0];

	/**
	 * EtherEventHandler of the target component.
	 */
	protected transient EtherEventHandler target = null;

	/**
	* UndoManager through which to support undo.
	*/
	protected transient UndoManager undoMgr = null;

	/**
	* Constructs the only designer edits reference for a UndoManager.
	* @param _undoMgr The input UndoManager.
	*/
	public OnlyDesignerEdits(UndoManager _undoMgr) {
		undoMgr = _undoMgr;
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		IExtPair pair = pdx_JobjRef_pdx_ObjectRef.pdxm_allocate_JobjRef(mil);
		mil = pair.getMilieu();
		onlyDesignerEdits = (pdx_JobjRef_pdx_ObjectRef) (pair.getObject());
		mil = onlyDesignerEdits.pdxm_setVal(mil, new Boolean(false));
		undoMgr.handleCommitTempChange(mil);
		undoMgr.addPropertyChangeListener(this);
	}

	/**
     * Handles the destruction of the OnlyDesignerEdits. 
     */
	public void handleDestroy() {
		undoMgr.removePropertyChangeListener(this);
	}

	/**
	 * Fires property change events on the OnlyDesignerEdits.
	 */
	protected void firePropertyChangeEvents() {
		int count;
		int len = PropLa.length;
		for (count = 0; count < len; count++) {
			PropertyChangeSupport p = PropLa[count];
			p.firePropertyChange(
				DesignerPropertyEditor.AppOnlyDesignerEdits,
				null,
				null);
		}
		len = PropLb.length;
		for (count = 0; count < len; count++) {
			PropertyChangeFire p = PropLb[count];
			p.firePropertyChg(
				DesignerPropertyEditor.AppOnlyDesignerEdits,
				null,
				null);
		}

		if (target instanceof OnlyDesignerEditsChangeHandler) {
			((OnlyDesignerEditsChangeHandler) target)
				.handleOnlyDesignerEditsChange();
		}
	}

	/**
	* Handles property change events.
	* @param evt The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == UndoManager.MODEL_STATE_CHANGED) {
			firePropertyChangeEvents();
		}
	}

	/**
	* Gets the whether only the designer can edit.
	* @return Whether only the designer cn edit.
	*/
	public boolean isOnlyDesignerEdits() {
		return (
			((Boolean) (onlyDesignerEdits
				.pdxm_getVal(undoMgr.getCurrentMil())))
				.booleanValue());
	}

	/**
	 * Sets whether only the designer can edit.
	 * @param in Whether only the designer can edit.
	 */
	public void setOnlyDesignerEdits(boolean in) {
		ExtMilieuRef mil =
			onlyDesignerEdits.pdxm_setVal(
				undoMgr.getCurrentMil(),
				new Boolean(in));
		undoMgr.handleCommitTempChange(mil);
		firePropertyChangeEvents();
	}

	/**
	 * Configures the OnlyDesignerEdits object so that it can handle Ether Events.
	 * @param _target EtherEventHandler of the target component.
	 * @param _PropLa Property change supports through which to send events.
	 * @param _PropLb Property change fires through which to send events.
	 */
	public void configureForEtherEvents(
		EtherEventHandler _target,
		PropertyChangeSupport[] _PropLa,
		PropertyChangeFire[] _PropLb) {
		target = _target;
		PropLa = _PropLa;
		PropLb = _PropLb;
	}

	/**
	 * Configures the OnlyDesignerEdits object so that it can handle Ether Events.
	 * @param _target EtherEventHandler of the target component.
	 * @param _p Property change support through which to send events.
	 */
	public void configureForEtherEvents(
		EtherEventHandler _target,
		PropertyChangeSupport _p) {
		PropertyChangeSupport[] _PropLa = { _p };
		target = _target;
		PropLa = _PropLa;
		PropLb = new PropertyChangeFire[0];
	}

	/**
	 * Handles Ether Events to alter the properties of the OnlyDesignerEdits.
	 * @param in The event to handle.
	 * @param refcon A reference to context data for the event.
	 * @return The result of handling the event, or null if there is no result.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon)
		throws Throwable {

		Object ret = EtherEvent.EVENT_NOT_HANDLED;

		if (in instanceof PropertyEditEtherEvent) {
			if (in
				.getEtherID()
				.equals(PropertyEditEtherEvent.setOnlyDesignerEdit)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				setOnlyDesignerEdits(
					((Boolean) (in.getParameter())).booleanValue());
				undoMgr.commitUndoableOp(utag, "Only Designer Edits Change");
				return (null);
			}
		}

		return (ret);
	}

	
};


