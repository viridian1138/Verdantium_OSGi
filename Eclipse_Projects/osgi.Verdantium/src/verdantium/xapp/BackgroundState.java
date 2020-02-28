package verdantium.xapp;

import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.UIManager;

import jundo.runtime.ExtMilieuRef;
import jundo.runtime.IExtPair;
import meta.DataFormatException;
import meta.VersionBuffer;
import verdantium.EtherEvent;
import verdantium.EtherEventHandler;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumUtils;
import verdantium.core.BackgroundPropertyEditor;
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
* A undoable data reference for a component's background color and opaqueness.
* 
* @author Thorn Green
*/
public class BackgroundState
	implements EtherEventHandler, PropertyChangeListener {

	/**
	 * The undoable data model for the background state.
	 */
	protected pdx_BackgroundModel_pdx_ObjectRef backgroundModel = null;

	/**
	 * Property change supports through which to send events.
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
	protected transient Object target = null;

	/**
	 * UndoManager through which to support undo.
	 */
	protected transient UndoManager undoMgr = null;

	/**
	 * The current background color of the component.
	 */
	protected transient Color backgroundColor = null;

	/**
	 * Whether the background is currently opaque.
	 */
	protected transient boolean opaqueFlag = false;

	/**
	 * Constructs the Border for a UndoManager.
	 * @param _undoMgr The input UndoManager.
	 */
	public BackgroundState(UndoManager _undoMgr) {
		undoMgr = _undoMgr;
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		IExtPair pair =
			pdx_BackgroundModel_pdx_ObjectRef.pdxm_new_BackgroundModel(
				mil,
				UIManager.getColor("Panel.background"),
				false);
		mil = pair.getMilieu();
		backgroundModel =
			(pdx_BackgroundModel_pdx_ObjectRef) (pair.getObject());
		undoMgr.handleCommitTempChange(mil);
		undoMgr.addPropertyChangeListener(this);
	}
        
	/**
	 * Gets the UndoManager of the BackgroundState.
	 * @return The UndoManager of the BackgroundState.
	 */
        public UndoManager getUndoMgr()
        {
            return( undoMgr );
        }

    /**
     * Handles the destruction of the BackgroundState.    
     */
	public void handleDestroy() {
		undoMgr.removePropertyChangeListener(this);
	}

	/**
	 * Gets the current background color of the component.
	 * @return The current background color of the component.
	 */
	public Color getBackgroundColor() {
		return (backgroundColor);
	}

	/**
	 * Gets whether the background is currently opaque.
	 * @return Whether the background is currently opaque.
	 */
	public boolean isOpaqueFlag() {
		return (opaqueFlag);
	}

	/**
	 * Fires property change events on the BackgroundState.
	 */
	protected void firePropertyChangeEvents() {

		ExtMilieuRef mil = undoMgr.getCurrentMil();
		boolean initialized = false;
		try
		{
	 		opaqueFlag = backgroundModel.pdxm_isOpaqueFlag(mil);
			backgroundColor =
				(Color) (backgroundModel.pdxm_getBackgroundColor(mil));
			initialized = true;
		}
		catch( NullPointerException ex )
		{
			// Do Nothing.
		}
		if( initialized && ( target instanceof BackgroundStateHandler ) ) {
			((BackgroundStateHandler) target).handleBackgroundState(
				backgroundColor,
				opaqueFlag);
		}
		if (target instanceof VerdantiumComponent) {
			((VerdantiumComponent) target).getGUI().repaint();
		} else if (target instanceof Component) {
			((Component) target).repaint();
		}

		int count;
		int len = PropLa.length;
		for (count = 0; count < len; count++) {
			PropertyChangeSupport p = PropLa[count];
			p.firePropertyChange(
				BackgroundPropertyEditor.AppBackground,
				null,
				null);
		}
		len = PropLb.length;
		for (count = 0; count < len; count++) {
			PropertyChangeFire p = PropLb[count];
			p.firePropertyChg(
				BackgroundPropertyEditor.AppBackground,
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
	* Sets the background state.
	* @param InC The current background color of the component.
	* @param opaque Whether the background is currently opaque.
	*/
	public void setBackgroundState(Color inC, boolean opaque) {
		ExtMilieuRef mil =
			backgroundModel.pdxm_setBackgroundState(
				undoMgr.getCurrentMil(),
				inC,
				opaque);

		backgroundColor = inC;
		opaqueFlag = opaque;
		undoMgr.handleCommitTempChange(mil);
		firePropertyChangeEvents();
	}

	/**
	 * Configures the BackgroundState object so that it can handle Ether Events.
	 * @param _target EtherEventHandler of the target component.
	 * @param _PropLa Property change supports through which to send events.
	 * @param _PropLb Property change fires through which to send events.
	 */
	public void configureForEtherEvents(
		Object _target,
		PropertyChangeSupport[] _PropLa,
		PropertyChangeFire[] _PropLb) {
		target = _target;
		PropLa = _PropLa;
		PropLb = _PropLb;
	}

	/**
	 * Configures the BackgroundState object so that it can handle Ether Events.
	 * @param _target EtherEventHandler of the target component.
	 * @param _p Property change support through which to send events.
	 */
	public void configureForEtherEvents(
		Object _target,
		PropertyChangeSupport _p) {
		PropertyChangeSupport[] _PropLa = { _p };
		if (_p == null) {
			_PropLa = new PropertyChangeSupport[0];
		}
		target = _target;
		PropLa = _PropLa;
		PropLb = new PropertyChangeFire[0];
	}

	/**
	 * Handles Ether Events to alter the properties of the BackgroundState.
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
				.equals(PropertyEditEtherEvent.isBackgroundSupported))
				return (PropertyEditEtherEvent.isBackgroundSupported);

			if (in
				.getEtherID()
				.equals(PropertyEditEtherEvent.setBackgroundState)) {
				Object[] myo = (Object[]) (in.getParameter());
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				setBackgroundState(
					(Color) (myo[0]),
					((Boolean) (myo[1])).booleanValue());
				undoMgr.commitUndoableOp(utag, "Background Change");
				return (null);
			}
		}

		return (ret);
	}

	/**
		* Writes the object to persistent storage.
		*/
	public void writeData(VersionBuffer MyF) {
		MyF.setBoolean("Opaque", isOpaqueFlag());
		MyF.setProperty(
			"Background",
			VerdantiumUtils.cloneColorRGB(getBackgroundColor()));
	}

	/**
	* Reads the object from persistent storage.
	*/
	public void readData(VersionBuffer MyF) throws DataFormatException {
		boolean op = MyF.getBoolean("Opaque");
		if (target instanceof IScrollingComponent) {
			if (((IScrollingComponent) target).isScrolling())
				op = true;
		}
		Color bk = (Color) (MyF.getProperty("Background"));
		VersionBuffer.chkNul(bk);
		setBackgroundState(bk, op);

	}

};
