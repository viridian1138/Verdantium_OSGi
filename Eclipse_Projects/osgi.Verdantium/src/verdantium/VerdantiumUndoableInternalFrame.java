package verdantium;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.event.InternalFrameEvent;

import jundo.runtime.ExtMilieuRef;
import jundo.runtime.IExtObjectRef;
import jundo.runtime.IExtPair;
import jundo.util.pdx_HashMapSh_pdx_ObjectRef;
import jundo.util.pdx_JkeyRef_pdx_ObjectRef;
import jundo.util.pdx_JobjRef_pdx_ObjectRef;
import verdantium.data.InternalFrameDisposeRef;
import verdantium.data.pdx_InternalFrameModel_pdx_ObjectRef;
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
 * An undoable version of class VerdantiumInternalFrame.
 *
 * @author thorngreen
 */
public class VerdantiumUndoableInternalFrame
	extends VerdantiumInternalFrame
	implements PropertyChangeListener {

	/**
	 * The undo manager for the internal frame.
	 */
	protected UndoManager mgr = null;
	
	/**
	 * Reference to the undoable data model for the frame.
	 */
	protected pdx_InternalFrameModel_pdx_ObjectRef model = null;
	
	/**
	 * Undoable map describing which internal frames are in the containing desktop pane.
	 */
	protected pdx_HashMapSh_pdx_ObjectRef map = null;
	
	/**
	 * The key into the map for the internal frame.
	 */
	protected pdx_JkeyRef_pdx_ObjectRef key = null;
	
	/**
	 * Incrementing value for the last key given to an undoable internal frame for
	 * use as a key into the map.
	 */
	protected static int lastKeyVal = 1;

	/**
	 * Returns whether the undo process is currently updating.
	 * @return Whether the undo process is currently updating.
	 */
	protected boolean isUpdatingUndo() {
		return ((mgr != null) && (mgr.isUndoUpdating()));
	}

	@Override
	public void reshape(int x, int y, int width, int height) {
		super.reshape(x, y, width, height);

		if (!(VerdantiumDesktopManager.isUpdating())) {
			UTag dragUtag = new UTag();
			mgr.prepareForTempCommit(dragUtag);
			ExtMilieuRef mil = mgr.getCurrentMil();
			mil = model.pdxm_setX(mil, x);
			mil = model.pdxm_setY(mil, y);
			mil = model.pdxm_setWidth(mil, width);
			mil = model.pdxm_setHeight(mil, height);
			mgr.handleCommitTempChange(mil);
			mgr.commitUndoableOp(dragUtag, "Change MDI Window Shape / Size");
		}

	}

	/**
	 * This should only be called by {@see verdantium.VerdantiumDesktopManager}.
	 * Handles configuring the undo manager at the end of a mouse-drag operation.
	 */
	public void handlePostDragOp() {
		Rectangle bnd = bounds();
		UTag dragUtag = new UTag();
		mgr.prepareForTempCommit(dragUtag);
		ExtMilieuRef mil = mgr.getCurrentMil();
		int nx = bnd.x;
		int ny = bnd.y;
		int nwid = bnd.width;
		int nhei = bnd.height;

		int px = model.pdxm_getX(mil);
		int py = model.pdxm_getY(mil);
		int pwid = model.pdxm_getWidth(mil);
		int phei = model.pdxm_getHeight(mil);

		if ((nx != px) || (ny != py) || (nwid != pwid) || (nhei != phei)) {
			mil = model.pdxm_setX(mil, nx);
			mil = model.pdxm_setY(mil, ny);
			mil = model.pdxm_setWidth(mil, nwid);
			mil = model.pdxm_setHeight(mil, nhei);
			mgr.handleCommitTempChange(mil);
			mgr.commitUndoableOp(dragUtag, "Change MDI Window Shape / Size");
		}
	}

	/**
	 * Handles a change to the multi-level undo state.
	 */
	protected void handleUndoStateChange() {
		ExtMilieuRef mil = mgr.getCurrentMil();
		boolean visible = false;
		try {
			if (!isClosed()) {
				visible = model.pdxm_getVisible(mil);
			}
		} catch (Exception ex) {
			// Do Nothing.
		}

		try {
			super.setVisible(visible);
                        if(!visible)
                        {
                            fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_CLOSING);
                        }
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		if (visible) {
			int x = model.pdxm_getX(mil);
			int y = model.pdxm_getY(mil);
			int width = model.pdxm_getWidth(mil);
			int height = model.pdxm_getHeight(mil);
			boolean selected = model.pdxm_getSelected(mil);
			super.reshape(x, y, width, height);
			try {
				super.setSelected(selected);
			} catch (Exception e) {
			}
		}

	}

	@Override
	public void setSelected(boolean in)
		throws java.beans.PropertyVetoException {
		if (!isUpdatingUndo()) {
			UTag utag = new UTag();
			mgr.prepareForTempCommit(utag);
			super.setSelected(in);
			ExtMilieuRef mil = mgr.getCurrentMil();
			mil = model.pdxm_setSelected(mil, in);
			mgr.handleCommitTempChange(mil);
			mgr.commitUndoableOp(utag, "Change Selected");
		} else {
			super.setSelected(in);
		}
	}

	@Override
	public void setVisible(boolean in) {
		if (!isUpdatingUndo() && (mgr != null)) {
			UTag utag = new UTag();
			mgr.prepareForTempCommit(utag);
			super.setVisible(in);
			ExtMilieuRef mil = mgr.getCurrentMil();
			mil = model.pdxm_setVisible(mil, in);
			mgr.handleCommitTempChange(mil);
			mgr.commitUndoableOp(utag, "Change Visibility");
		} else {
			super.setVisible(in);
		}
	}

	@Override
	public void setClosed(boolean in) {
		if (in) {
			UTag utag = new UTag();
			mgr.prepareForTempCommit(utag);
			setVisible(!in);
			ExtMilieuRef mil = mgr.getCurrentMil();
			IExtPair pair = map.pdxm_remove(mil, key);
                        fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_CLOSING);
			mil = pair.getMilieu();
			mgr.handleCommitTempChange(mil);
			mgr.commitUndoableOp(utag, "MDI Window Close");
		} else {
			throw (new RuntimeException("Invalid Input."));
		}
	}

	@Override
	public boolean isClosed() {
		boolean ret = false;
		if (mgr != null) {
			ExtMilieuRef mil = mgr.getCurrentMil();
			IExtObjectRef ref = map.pdxm_get(mil, key);
			ret = (ref == null);
		} else {
			ret = super.isClosed();
		}
		return (ret);
	}

	@Override
	public void show() {
		if (!isUpdatingUndo()) {
			UTag utag = new UTag();
			mgr.prepareForTempCommit(utag);
			super.show();
			setVisible(true);
			ExtMilieuRef mil = mgr.getCurrentMil();
			InternalFrameDisposeRef dref =
				new InternalFrameDisposeRef(this, getComponent());
			IExtPair pair =
				pdx_JobjRef_pdx_ObjectRef.pdxm_allocate_JobjRef(mil);
			pdx_JobjRef_pdx_ObjectRef val =
				(pdx_JobjRef_pdx_ObjectRef) (pair.getObject());
			mil = pair.getMilieu();
			mil = val.pdx_ObjectMemberAccess_pdx_asgval(mil, dref);
			pair = map.pdxm_put(mil, key, val);
			mil = pair.getMilieu();
			mgr.handleCommitTempChange(mil);
			mgr.commitUndoableOp(utag, "MDI Window Show");
		} else {
			super.show();
		}
	}

	/**
	 * Performs the default undoable close action for the internal frame.
	 */
	public void doDefaultCloseActionComp() {
		UTag utag = new UTag();
		mgr.prepareForTempCommit(utag);
		setClosed(true);
		try {
			setSelected(false);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_CLOSING);
		ExtMilieuRef mil = mgr.getCurrentMil();
		mgr.handleCommitTempChange(mil);
		mgr.commitUndoableOp(utag, "MDI Window Close");
	}

	@Override
	public void doDefaultCloseAction() {
		doDefaultCloseActionComp();
	}

	/**
	 * Handles property change events.
	 * @param evt The input event.
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == UndoManager.MODEL_STATE_CHANGED) {
			handleUndoStateChange();
		}
	}

	@Override
	public void dispose() {
                if( !disposed )
                {
                    if( getComponent() != null )
                    {
		        getComponent().handleDestroy();
                    }
		    mgr.removePropertyChangeListener(this);
		    super.dispose();
                }
	}

	/**
	 * Gets the eeference to the undoable data model for the frame.
	 * @return The reference to the undoable data model for the frame.
	 */
	public pdx_InternalFrameModel_pdx_ObjectRef getModel() {
		return (model);
	}

	/**
	 * Sets the reference to the undoable data model for the frame.
	 * @param in The reference to the undoable data model for the frame.
	 */
	public void setModel(pdx_InternalFrameModel_pdx_ObjectRef in) {
		model = in;
	}

	/**
	 * Initializes the internal frame.
	 */
	protected void initialize() {
		lastKeyVal++;
		mgr.addPropertyChangeListener(this);
		UTag utag = new UTag();
		mgr.prepareForTempCommit(utag);
		ExtMilieuRef mil = mgr.getCurrentMil();
		IExtPair pair =
			pdx_InternalFrameModel_pdx_ObjectRef.pdxm_new_InternalFrameModel(
				mil);
		mil = pair.getMilieu();
		model = (pdx_InternalFrameModel_pdx_ObjectRef) (pair.getObject());
		mil = model.pdxm_setVisible(mil, isVisible());
		pair =
			pdx_JkeyRef_pdx_ObjectRef.pdxm_allocate_JkeyRef(
				mil,
				new Integer(lastKeyVal));
		mil = pair.getMilieu();
		key = (pdx_JkeyRef_pdx_ObjectRef) (pair.getObject());
		mgr.handleCommitTempChange(mil);
		mgr.commitUndoableOp(utag, "MDI Window Creation");
	}

	/**
	 * Constructor.
	 * @param _mgr The undo manager for the internal frame.
	 * @param _map Undoable map describing which internal frames are in the containing desktop pane.
	 * @param comp The component to be embedded in the frame.
	 */
	public VerdantiumUndoableInternalFrame(
		UndoManager _mgr,
		pdx_HashMapSh_pdx_ObjectRef _map,
		VerdantiumComponent comp) {
		super(comp);
		mgr = _mgr;
		map = _map;
		initialize();
	}

	/**
	 * Constructor.
	 * @param title The internal frame title.
	 * @param resizable Whether the internal frame is to be resizable.
	 * @param closable Whether the internal frame is to be closable.
	 * @param maximizable Whether the internal frame is to be maximizable.
	 * @param iconifiable Whether the internal frame is to be iconifiable.
	 * @param _mgr The undo manager for the internal frame.
	 * @param _map Undoable map describing which internal frames are in the containing desktop pane.
	 * @param comp The component to be embedded in the frame.
	 */
	public VerdantiumUndoableInternalFrame(
		String title,
		boolean resizable,
		boolean closable,
		boolean maximizable,
		boolean iconifiable,
		UndoManager _mgr,
		pdx_HashMapSh_pdx_ObjectRef _map,
		VerdantiumComponent comp) {
		super(title, resizable, closable, maximizable, iconifiable, comp);
		mgr = _mgr;
		map = _map;
		initialize();
	}

	/**
	 * Constructor.
	 * @param title The internal frame title.
	 * @param resizable Whether the internal frame is to be resizable.
	 * @param closable Whether the internal frame is to be closable.
	 * @param maximizable Whether the internal frame is to be maximizable.
	 * @param _mgr The undo manager for the internal frame.
	 * @param _map Undoable map describing which internal frames are in the containing desktop pane.
	 * @param comp The component to be embedded in the frame.
	 */
	public VerdantiumUndoableInternalFrame(
		String title,
		boolean resizable,
		boolean closable,
		boolean maximizable,
		UndoManager _mgr,
		pdx_HashMapSh_pdx_ObjectRef _map,
		VerdantiumComponent comp) {
		super(title, resizable, closable, maximizable, comp);
		mgr = _mgr;
		map = _map;
		initialize();
	}

	/**
	 * Constructor.
	 * @param title The internal frame title.
	 * @param resizable Whether the internal frame is to be resizable.
	 * @param closable Whether the internal frame is to be closable.
	 * @param _mgr The undo manager for the internal frame.
	 * @param _map Undoable map describing which internal frames are in the containing desktop pane.
	 * @param comp The component to be embedded in the frame.
	 */
	public VerdantiumUndoableInternalFrame(
		String title,
		boolean resizable,
		boolean closable,
		UndoManager _mgr,
		pdx_HashMapSh_pdx_ObjectRef _map,
		VerdantiumComponent comp) {
		super(title, resizable, closable, comp);
		mgr = _mgr;
		map = _map;
		initialize();
	}

	/**
	 * Constructor.
	 * @param title The internal frame title.
	 * @param resizable Whether the internal frame is to be resizable.
	 * @param _mgr The undo manager for the internal frame.
	 * @param _map Undoable map describing which internal frames are in the containing desktop pane.
	 * @param comp The component to be embedded in the frame.
	 */
	public VerdantiumUndoableInternalFrame(
		String title,
		boolean resizable,
		UndoManager _mgr,
		pdx_HashMapSh_pdx_ObjectRef _map,
		VerdantiumComponent comp) {
		super(title, resizable, comp);
		mgr = _mgr;
		map = _map;
		initialize();
	}

	/**
	 * Constructor.
	 * @param title The internal frame title.
	 * @param _mgr The undo manager for the internal frame.
	 * @param _map Undoable map describing which internal frames are in the containing desktop pane.
	 * @param comp The component to be embedded in the frame.
	 */
	public VerdantiumUndoableInternalFrame(
		String title,
		UndoManager _mgr,
		pdx_HashMapSh_pdx_ObjectRef _map,
		VerdantiumComponent comp) {
		super(title, comp);
		mgr = _mgr;
		map = _map;
		initialize();
	}

	/**
	 * Constructor.
	 * @param _mgr The undo manager for the internal frame.
	 * @param _map Undoable map describing which internal frames are in the containing desktop pane.
	 */
	public VerdantiumUndoableInternalFrame(
		UndoManager _mgr,
		pdx_HashMapSh_pdx_ObjectRef _map) {
		super();
		mgr = _mgr;
		map = _map;
		initialize();
	}

	
}

