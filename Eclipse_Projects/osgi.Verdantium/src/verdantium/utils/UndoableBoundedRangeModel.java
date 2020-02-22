/*
 * Created on Nov 12, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package verdantium.utils;

import javax.swing.DefaultBoundedRangeModel;

import jundo.runtime.ExtMilieuRef;
import jundo.runtime.IExtPair;
import jundo.util.pdx_JobjRef_pdx_ObjectRef;
import meta.WrapRuntimeException;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.core.PropertyEditEtherEvent;
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
 * An extension of DefaultBoundedRangeModel supproting multi-level undo and scripting.
 * 
 * @author thorngreen
 */
public class UndoableBoundedRangeModel extends DefaultBoundedRangeModel {

	/**
	 * The target that receives scriptable events when the value changes.
	 */
	protected Object scriptTarget;

	/**
	 * The multi-level undo manager.
	 */
	protected UndoManager undoMgr;

	/**
	 * The scriptable event name to send at the target when the value changes.
	 */
	protected String etherEventName;

	/**
	 * Reference to the undo state of the range model.
	 */
	protected pdx_JobjRef_pdx_ObjectRef ref = null;

	/**
	 * Constructor.
	 * @param _scriptTarget The target that receives scriptable events when the value changes.
	 * @param _undoMgr The multi-level undo manager.
	 * @param _etherEventName  The scriptable event name to send at the target when the value changes.
	 */
	public UndoableBoundedRangeModel(
		Object _scriptTarget,
		UndoManager _undoMgr,
		String _etherEventName) {
		super();
		scriptTarget = _scriptTarget;
		undoMgr = _undoMgr;
		etherEventName = _etherEventName;
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		IExtPair pair = pdx_JobjRef_pdx_ObjectRef.pdxm_allocate_JobjRef(mil);
		mil = pair.getMilieu();
		ref = (pdx_JobjRef_pdx_ObjectRef) (pair.getObject());
		mil = ref.pdxm_setVal(mil, new Integer(getValue()));
		undoMgr.handleCommitTempChange(mil);
	}

	@Override
	public void setValueIsAdjusting(boolean in) {
		boolean goEvt =
			!in && getValueIsAdjusting() && !(undoMgr.isUndoUpdating());

		if (goEvt) {
			UTag utag = new UTag();
			undoMgr.prepareForTempCommit(utag);
			ExtMilieuRef mil = undoMgr.getCurrentMil();
			mil = ref.pdxm_setVal(mil, new Integer(getValue()));
			undoMgr.handleCommitTempChange(mil);
			undoMgr.commitUndoableOp(utag, "Scroll Change");
			try {
				EtherEvent send =
					new PropertyEditEtherEvent(
						scriptTarget,
						etherEventName,
						null,
						scriptTarget);
				send.setParameter(new Integer(getValue()));
				ProgramDirector.fireEtherEvent(send, null);
			} catch (Throwable ex) {
				throw (new WrapRuntimeException(ex));
			}
		}

		super.setValueIsAdjusting(in);
	}

	@Override
	public void setValue(int in) {
		boolean goEvt =
			!(getValueIsAdjusting())
				&& (in != getValue())
				&& !(undoMgr.isUndoUpdating());

		if (goEvt) {
			UTag utag = new UTag();
			undoMgr.prepareForTempCommit(utag);
			super.setValue(in);
			ExtMilieuRef mil = undoMgr.getCurrentMil();
			mil = ref.pdxm_setVal(mil, new Integer(in));
			undoMgr.handleCommitTempChange(mil);
			undoMgr.commitUndoableOp(utag, "Scroll Change");
			try {
				EtherEvent send =
					new PropertyEditEtherEvent(
						scriptTarget,
						etherEventName,
						null,
						scriptTarget);
				send.setParameter(new Integer(in));
				ProgramDirector.fireEtherEvent(send, null);
			} catch (Throwable ex) {
				throw (new WrapRuntimeException(ex));
			}
		} else {
			super.setValue(in);
		}
	}

	/**
	 * Handles the reversion to a previous multi-level undo state.
	 */
	public void handleScrollUndoImpl() {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		Integer intv = (Integer) (ref.pdxm_getVal(mil));
		super.setValue(intv.intValue());

	}

	
}

