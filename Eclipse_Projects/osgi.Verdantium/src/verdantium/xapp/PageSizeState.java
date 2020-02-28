package verdantium.xapp;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import jundo.runtime.ExtMilieuRef;
import jundo.runtime.IExtPair;
import jundo.util.pdx_JobjRef_pdx_ObjectRef;
import verdantium.EtherEvent;
import verdantium.EtherEventHandler;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.undo.UTag;
import verdantium.undo.UndoManager;
import verdantium.utils.IllegalInputException;

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
* A undoable data reference for PageFormat objects.
* 
* @author Thorn Green
*/
public class PageSizeState
	implements EtherEventHandler, PropertyChangeListener {

	/**
	 * Page size reference.
	 */
	protected pdx_JobjRef_pdx_ObjectRef pageModel = null;

	/**
	 * Declaring EtherEventHandler.
	 */
	protected transient PageHandler target = null;

	/**
	 * UndoManager through which to support undo.
	 */
	protected transient UndoManager undoMgr = null;

	/**
	* The size of the document page.
	*/
	transient protected Dimension PageSize = new Dimension(2000, 2000);

	/**
	 * Constructs the Border for a UndoManager.
	 * @param _undoMgr The input UndoManager.
	 */
	public PageSizeState(UndoManager _undoMgr) {
		undoMgr = _undoMgr;
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		IExtPair pair = pdx_JobjRef_pdx_ObjectRef.pdxm_allocate_JobjRef(mil);
		mil = pair.getMilieu();
		pageModel = (pdx_JobjRef_pdx_ObjectRef) (pair.getObject());
		undoMgr.handleCommitTempChange(mil);
		undoMgr.addPropertyChangeListener(this);
	}

	/**
	 * Handles the destruction of the object.
	 */
	public void handleDestroy() {
		undoMgr.removePropertyChangeListener(this);
	}

	/**
	* Gets the size of the document page.
	* @return The size of the document page.
	*/
	public Dimension getPageSize() {
		return (PageSize);
	}

	/**
	* Handles property change events.
	* @param evt The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == UndoManager.MODEL_STATE_CHANGED) {
			ExtMilieuRef mil = undoMgr.getCurrentMil();
			PageSize = (Dimension) (pageModel.pdxm_getVal(mil));
			target.handlePageSizeChange();
		}
	}

	/**
	* Sets the document page size.
	* @param in The document page size.
	*/
	protected void setPageSize(Dimension in) {
		ExtMilieuRef mil = pageModel.pdxm_setVal(undoMgr.getCurrentMil(), in);

		undoMgr.handleCommitTempChange(mil);
		PageSize = in;
		target.handlePageSizeChange();
	}

	/**
	* Alters the document's page size including the page layout.
	* @param in The page size.
	*/
	public void alterPageSize(Dimension in) throws IllegalInputException {
		if (in.width <= 0)
			throw (
				new IllegalInputException("The width must be at least 1/72 inch."));

		if (in.height <= 0)
			throw (
				new IllegalInputException("The height must be at least 1/72 inch."));

		setPageSize(in);
		target.handlePageSizeChange();
	}

	/**
	 * Configures the PageHandler event target so that the object can handle Ether Events.
	 * @param _target The PageHandler event target.
	 */
	public void configureForEtherEvents(PageHandler _target) {
		target = _target;
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
			if (in.getEtherID().equals(PropertyEditEtherEvent.setPageSize)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				Dimension d = (Dimension) (in.getParameter());
				alterPageSize(d);
				undoMgr.commitUndoableOp(utag, "Change Page Size");
				return (null);
			}
		}

		return (ret);
	}

	
};

