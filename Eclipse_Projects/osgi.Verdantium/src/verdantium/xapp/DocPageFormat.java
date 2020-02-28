package verdantium.xapp;

import java.awt.print.PageFormat;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import jundo.runtime.ExtMilieuRef;
import jundo.runtime.IExtPair;
import jundo.util.pdx_JobjRef_pdx_ObjectRef;
import verdantium.EtherEvent;
import verdantium.EtherEventHandler;
import verdantium.StandardEtherEvent;
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
* An undoable data reference for PageFormat objects.
* 
* @author Thorn Green
*/
public class DocPageFormat
	implements EtherEventHandler, PropertyChangeListener {

	/**
	 * Page format reference.
	 */
	protected pdx_JobjRef_pdx_ObjectRef pageFormat = null;

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
	 * Property name for firing of property change events.
	 */
	protected transient String propertyName = null;

	/**
	 * Constructs the DocPageFormat for a UndoManager.
	 * @param _undoMgr The input UndoManager.
	 */
	public DocPageFormat(UndoManager _undoMgr) {
		undoMgr = _undoMgr;
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		IExtPair pair = pdx_JobjRef_pdx_ObjectRef.pdxm_allocate_JobjRef(mil);
		mil = pair.getMilieu();
		pageFormat = (pdx_JobjRef_pdx_ObjectRef) (pair.getObject());
		undoMgr.handleCommitTempChange(mil);
		undoMgr.addPropertyChangeListener(this);
	}

	/**
	 * Handles the destruction of the DocPageFormat.
	 */
	public void handleDestroy() {
		undoMgr.removePropertyChangeListener(this);
	}

	/**
	 * Sets the property name for firing of property change events.
	 * @param in The property name for firing of property change events.
	 */
	public void setPropertyName(String in) {
		propertyName = in;
	}

	/**
	 * Fires property change events.
	 */
	protected void firePropertyChangeEvents() {
		if (propertyName != null) {
			int count;
			int len = PropLa.length;
			for (count = 0; count < len; count++) {
				PropertyChangeSupport p = PropLa[count];
				p.firePropertyChange(propertyName, null, null);
			}
			len = PropLb.length;
			for (count = 0; count < len; count++) {
				PropertyChangeFire p = PropLb[count];
				p.firePropertyChg(propertyName, null, null);
			}
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
	* Gets the current document page format for printing.
	* @return The current document page format for printing.
	*/
	public PageFormat getDocPageFormat() {
		return ((PageFormat) (pageFormat.pdxm_getVal(undoMgr.getCurrentMil())));
	}

	/**
	 * Sets the document page format for printing and the related multi-level undo state.
	 * @param in The document page format.
	 */
	public void setDocPageFormat(PageFormat in) {
		ExtMilieuRef mil = pageFormat.pdxm_setVal(undoMgr.getCurrentMil(), in);
		undoMgr.handleCommitTempChange(mil);
		firePropertyChangeEvents();
	}

	/**
	 * Configures the BorderObject object so that it can handle Ether Events.
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
	 * Configures the BorderObject object so that it can handle Ether Events.
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
	 * Handles Ether Events to alter the properties of the BorderObject.
	 * @param in The event to handle.
	 * @param refcon A reference to context data for the event.
	 * @return The result of handling the event, or null if there is no result.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon)
		throws Throwable {

		Object ret = EtherEvent.EVENT_NOT_HANDLED;

		if (in instanceof StandardEtherEvent) {
			if (in.getEtherID().equals(StandardEtherEvent.getDocPageFormat)) {
				return (getDocPageFormat());
			}

			if (in.getEtherID().equals(StandardEtherEvent.setDocPageFormat)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				setDocPageFormat((PageFormat) (in.getParameter()));
				undoMgr.commitUndoableOp(utag, "Page Setup Change");
				return (null);
			}
		}

		return (ret);
	}

	
};

