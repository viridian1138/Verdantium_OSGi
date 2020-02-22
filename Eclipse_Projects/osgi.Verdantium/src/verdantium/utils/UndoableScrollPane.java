/*
 * Created on Nov 12, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package verdantium.utils;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;

import verdantium.EtherEvent;
import verdantium.EtherEventHandler;
import verdantium.core.PropertyEditEtherEvent;
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
 * An extension of JScrollPane supporting multi-level undo and scripting.
 * 
 * @author thorngreen
 *
 */
public class UndoableScrollPane
	extends JScrollPaneAlt
	implements EtherEventHandler, PropertyChangeListener {

	/**
	 * The multi-level undo manager.
	 */
	UndoManager undoMgr = null;

	/**
	 * Constructor.
	 * @param view The view to be scrolled.
	 * @param vsbPolicy The vertical scroll bar policy.
	 * @param hsbPolicy The horizontal scroll bar policy.
	 * @param _scriptTarget The target that receives scriptable events when the value changes.
	 * @param _undoMgr The multi-level undo manager.
	 */
	public UndoableScrollPane(
		Component view,
		int vsbPolicy,
		int hsbPolicy,
		Object _scriptTarget,
		UndoManager _undoMgr) {
		super(view, vsbPolicy, hsbPolicy);
		initializeUndo(_scriptTarget, _undoMgr);
	}

	/**
	 * Constructor.
	 * @param view The view to be scrolled.
	 * @param _scriptTarget The target that receives scriptable events when the value changes.
	 * @param _undoMgr The multi-level undo manager.
	 */
	public UndoableScrollPane(
		Component view,
		Object _scriptTarget,
		UndoManager _undoMgr) {
		super(view);
		initializeUndo(_scriptTarget, _undoMgr);
	}

	/**
	 * Constructor.
	 * @param vsbPolicy The vertical scroll bar policy.
	 * @param hsbPolicy The horizontal scroll bar policy.
	 * @param _scriptTarget The target that receives scriptable events when the value changes.
	 * @param _undoMgr The multi-level undo manager.
	 */
	public UndoableScrollPane(
		int vsbPolicy,
		int hsbPolicy,
		Object _scriptTarget,
		UndoManager _undoMgr) {
		super(vsbPolicy, hsbPolicy);
		initializeUndo(_scriptTarget, _undoMgr);
	}

	/**
	 * Constructor.
	 * @param _scriptTarget The target that receives scriptable events when the value changes.
	 * @param _undoMgr The multi-level undo manager.
	 */
	public UndoableScrollPane(Object _scriptTarget, UndoManager _undoMgr) {
		super();
		initializeUndo(_scriptTarget, _undoMgr);
	}

	/**
	 * Initializes support for multi-level undo.
	 * @param _scriptTarget The target that receives scriptable events when the value changes.
	 * @param _undoMgr The multi-level undo manager.
	 */
	protected void initializeUndo(Object _scriptTarget, UndoManager _undoMgr) {
		undoMgr = _undoMgr;
		undoMgr.addPropertyChangeListener(this);
		JScrollBar horizScroll = getHorizontalScrollBar();
		JScrollBar vertScroll = getVerticalScrollBar();
		UndoableBoundedRangeModel horizModel =
			new UndoableBoundedRangeModel(
				_scriptTarget,
				_undoMgr,
				PropertyEditEtherEvent.setHorizScroll);
		UndoableBoundedRangeModel vertModel =
			new UndoableBoundedRangeModel(
				_scriptTarget,
				_undoMgr,
				PropertyEditEtherEvent.setVertScroll);
		horizScroll.setModel(horizModel);
		vertScroll.setModel(vertModel);
	}

	/**
	* Handles property change events.
	* @param evt The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == UndoManager.MODEL_STATE_CHANGED) {
			handleScrollUndo();
		}
	}

	/**
	 * Implements the undo of the scroll-pane state.
	 */
	protected void handleScrollUndoImpl() {
		((UndoableBoundedRangeModel) (getHorizontalScrollBar().getModel()))
			.handleScrollUndoImpl();
		((UndoableBoundedRangeModel) (getVerticalScrollBar().getModel()))
			.handleScrollUndoImpl();
	}

	/**
	 * Handles the undo of the scroll-pane state.
	 */
	protected void handleScrollUndo() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				handleScrollUndoImpl();
			}
		});
	}

	/**
	 * Handles the destruction of the scroll pane.
	 */
	public void handleDestroy() {
		undoMgr.removePropertyChangeListener(this);
	}

	/**
	* Handles Ether Events related to the data.
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
				.equals(PropertyEditEtherEvent.setHorizScroll)) {
				Integer param = (Integer) (in.getParameter());
				getHorizontalScrollBar().getModel().setValue(param.intValue());
				return (null);
			}

			if (in.getEtherID().equals(PropertyEditEtherEvent.setVertScroll)) {
				Integer param = (Integer) (in.getParameter());
				getVerticalScrollBar().getModel().setValue(param.intValue());
				return (null);
			}

		}

		return (ret);
	}

	
}

