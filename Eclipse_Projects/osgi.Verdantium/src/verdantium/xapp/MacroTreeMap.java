package verdantium.xapp;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Iterator;
import java.util.Vector;

import jundo.runtime.ExtBooleanPair;
import jundo.runtime.ExtMilieuRef;
import jundo.runtime.ExtPair;
import jundo.runtime.IExtObjectRef;
import jundo.runtime.IExtPair;
import jundo.util.pdx_Iterator_pdx_ObjectRef;
import jundo.util.pdx_JkeyRef_pdx_ObjectRef;
import jundo.util.pdx_JobjRef_pdx_ObjectRef;
import jundo.util.pdx_Set_pdx_ObjectRef;
import jundo.util.pdx_TreeMapSh_pdx_ObjectRef;
import meta.DataFormatException;
import meta.VersionBuffer;
import verdantium.EtherEvent;
import verdantium.EtherEventHandler;
import verdantium.core.ClientMacroEtherEvent;
import verdantium.core.MacroObject;
import verdantium.core.MacroRecorder;
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
* A undoable tree map for {@link MacroObject} objects.
* 
* @author Thorn Green
*/
public class MacroTreeMap
	implements EtherEventHandler, PropertyChangeListener {

	/**
	 * Macro tree map reference.
	 */
	protected pdx_TreeMapSh_pdx_ObjectRef map = null;

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
	* Constructs the MacroTreeMap for a UndoManager.
	* @param _undoMgr The input UndoManager.
	*/
	public MacroTreeMap(UndoManager _undoMgr) {
		undoMgr = _undoMgr;
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		IExtPair pair =
			pdx_TreeMapSh_pdx_ObjectRef.pdxm_allocate_TreeMapSh(mil);
		mil = pair.getMilieu();
		map = (pdx_TreeMapSh_pdx_ObjectRef) (pair.getObject());
		undoMgr.handleCommitTempChange(mil);
		undoMgr.addPropertyChangeListener(this);
	}

	/**
	 * Handles the destruction of the MacroTreeMap.
	 */
	public void handleDestroy() {
		undoMgr.removePropertyChangeListener(this);
	}

	/**
	 * Fires property change events on the MacroTreeMap.
	 */
	protected void firePropertyChangeEvents() {
		int count;
		int len = PropLa.length;
		for (count = 0; count < len; count++) {
			PropertyChangeSupport p = PropLa[count];
			p.firePropertyChange(
				ClientMacroEtherEvent.MACRO_MAP_CHANGED,
				null,
				null);
		}
		len = PropLb.length;
		for (count = 0; count < len; count++) {
			PropertyChangeFire p = PropLb[count];
			p.firePropertyChg(
				ClientMacroEtherEvent.MACRO_MAP_CHANGED,
				null,
				null);
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
	* Gets the {@link MacroObject} with human-readable name <code>in</code>.
	* @param in The human-readable name of the macro.
	* @return The MacroObject.
	*/
	public MacroObject getMacro(String in) {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		IExtPair pair = createKey(mil, in);
		mil = pair.getMilieu();
		IExtObjectRef ref = map.pdxm_get(mil, pair.getObject());
		pdx_JobjRef_pdx_ObjectRef jref = (pdx_JobjRef_pdx_ObjectRef) (ref);
		Object obj = jref.pdxm_getVal(mil);
		return ((MacroObject) (obj));
	};

	/**
	* Puts a macro into the TreeMap.
	* @param Human-readable name The name of the macro.
	* @param in Object describing the macro operations.
	*/
	public void putMacro(String name, MacroObject in) {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		IExtPair pkey = createKey(mil, name);
		mil = pkey.getMilieu();
		IExtPair pval = createValue(mil, in);
		mil = pval.getMilieu();
		IExtPair pair = map.pdxm_put(mil, pkey.getObject(), pval.getObject());
		undoMgr.handleCommitTempChange(pair.getMilieu());
	}

	protected IExtPair createKey(ExtMilieuRef _mil, String in) {
		ExtMilieuRef mil = _mil;
		IExtPair p1 = pdx_JkeyRef_pdx_ObjectRef.pdxm_allocate_JkeyRef(mil, in);
		return (p1);
	}

	protected IExtPair createValue(ExtMilieuRef _mil, MacroObject in) {
		ExtMilieuRef mil = _mil;
		IExtPair p1 = pdx_JobjRef_pdx_ObjectRef.pdxm_allocate_JobjRef(mil);
		IExtObjectRef oref = p1.getObject();
		mil = p1.getMilieu();
		pdx_JobjRef_pdx_ObjectRef jref = (pdx_JobjRef_pdx_ObjectRef) (oref);
		mil = jref.pdxm_setVal(mil, in);
		ExtPair pair = new ExtPair(jref, mil);
		return (pair);
	}

	/**
	 * Clears the data state of the map.
	 */
	public void clear() {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		mil = map.pdxm_clear(mil);
		undoMgr.handleCommitTempChange(mil);
		firePropertyChangeEvents();
	}

	/**
	 * Configures the MacroTreeMap object so that it can handle Ether Events.
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
	 * Configures the MacroTreeMap object so that it can handle Ether Events.
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
	 * Handles Ether Events to alter the properties of the MacroTreeMap.
	 * @param in The event to handle.
	 * @param refcon A reference to context data for the event.
	 * @return The result of handling the event, or null if there is no result.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon)
		throws Throwable {

		Object ret = EtherEvent.EVENT_NOT_HANDLED;

		if (in instanceof ClientMacroEtherEvent) {
			if (in
				.getEtherID()
				.equals(ClientMacroEtherEvent.isClientMacroSupported)) {
				return (new Boolean(true));
			}

			if (in.getEtherID().equals(ClientMacroEtherEvent.setClientMacro)) {
				Object[] param = (Object[]) (in.getParameter());
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				MacroRecorder.storeMacroInMap(param[0], param[1], this);
				undoMgr.commitUndoableOp(utag, "Apply Macro");
				firePropertyChangeEvents();
				return (null);
			}

			if (in.getEtherID().equals(ClientMacroEtherEvent.runClientMacro)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);

				try {
					MacroRecorder.playClientMacro(
						this,
						(String) (in.getParameter()),
						target);
					undoMgr.commitUndoableOp(utag, "Macro Playback");
					return (null);
				} catch (Throwable ex) {
					undoMgr.commitHandleCriticalFailure(utag);
					throw (ex);
				}

			}

			if (in
				.getEtherID()
				.equals(ClientMacroEtherEvent.deleteClientMacro)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				ExtMilieuRef mil = undoMgr.getCurrentMil();
				IExtPair pkey = createKey(mil, (String) (in.getParameter()));
				mil = pkey.getMilieu();
				IExtPair pair = map.pdxm_remove(mil, pkey.getObject());
				mil = pair.getMilieu();
				undoMgr.handleCommitTempChange(mil);
				undoMgr.commitUndoableOp(utag, "Delete Macro");
				firePropertyChangeEvents();
				return (null);
			}

			if (in
				.getEtherID()
				.equals(ClientMacroEtherEvent.getClientMacroNameIterator)) {
				ExtMilieuRef mil = undoMgr.getCurrentMil();
				IExtPair pkey = map.pdxm_keySet(mil);
				pdx_Set_pdx_ObjectRef keySet =
					(pdx_Set_pdx_ObjectRef) (pkey.getObject());
				mil = pkey.getMilieu();
				IExtPair piter = keySet.pdxm_iterator(mil);
				mil = piter.getMilieu();
				pdx_Iterator_pdx_ObjectRef pit =
					(pdx_Iterator_pdx_ObjectRef) (piter.getObject());
				Iterator it = new MacroTreeMapIterator(mil, pit);
				return (it);
			}

		}

		return (ret);
	}

	/**
	* Writes the object to persistent storage.
	*/
	public void writeData(VersionBuffer out) {
		Vector names = new Vector();
		Vector values = new Vector();
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		IExtPair pkey = map.pdxm_keySet(mil);
		pdx_Set_pdx_ObjectRef keySet =
			(pdx_Set_pdx_ObjectRef) (pkey.getObject());
		mil = pkey.getMilieu();
		IExtPair piter = keySet.pdxm_iterator(mil);
		mil = piter.getMilieu();
		pdx_Iterator_pdx_ObjectRef it =
			(pdx_Iterator_pdx_ObjectRef) (piter.getObject());

		ExtBooleanPair bpair = it.pdxm_hasNext(mil);
		mil = bpair.getMilieu();
		while (bpair.getObject()) {
			IExtPair pnxt = it.pdxm_next(mil);
			IExtObjectRef okey = pnxt.getObject();
			pdx_JkeyRef_pdx_ObjectRef jokref =
				(pdx_JkeyRef_pdx_ObjectRef) (okey);
			Object sval = jokref.getKey();
			String key = (String) (sval);
			mil = pnxt.getMilieu();
			IExtObjectRef vref = map.pdxm_get(mil, okey);
			pdx_JobjRef_pdx_ObjectRef jovref =
				(pdx_JobjRef_pdx_ObjectRef) (vref);
			Object skey = jovref.pdxm_getVal(mil);
			MacroObject exp = (MacroObject) (skey);
			names.setSize(names.size() + 1);
			values.setSize(values.size() + 1);
			names.setElementAt(key, names.size() - 1);
			values.setElementAt(exp, values.size() - 1);
			bpair = it.pdxm_hasNext(mil);
			mil = bpair.getMilieu();
		}

		out.setProperty("MacroNames", names.toArray());
		out.setProperty("MacroValues", values.toArray());
	}

	/**
	* Reads the object from persistent storage.
	*/
	public void readData(VersionBuffer in) throws DataFormatException {
		Object[] names = (Object[]) (in.getProperty("MacroNames"));
		VersionBuffer.chkNul(names);
		Object[] values = (Object[]) (in.getProperty("MacroValues"));
		VersionBuffer.chkNul(values);
		if (names.length != values.length)
			throw (new DataFormatException());
		int sz = names.length;
		int count;
		for (count = 0; count < sz; ++count) {
			String key = (String) (names[count]);
			VersionBuffer.chkNul(key);
			MacroObject exp = (MacroObject) (values[count]);
			VersionBuffer.chkNul(exp);
			putMacro(key, exp);
		}

		firePropertyChangeEvents();

	}

	
};

