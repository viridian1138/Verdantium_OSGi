package verdantium.xapp;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import jundo.runtime.ExtMilieuRef;
import jundo.runtime.IExtPair;
import meta.DataFormatException;
import meta.VersionBuffer;
import meta.WrapRuntimeException;
import verdantium.EtherEvent;
import verdantium.EtherEventHandler;
import verdantium.VerdantiumComponent;
import verdantium.core.BorderPropertyEditor;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.undo.UTag;
import verdantium.undo.UndoManager;
import verdantium.utils.PropertyChangeFire;
import verdantium.utils.ResourceNotFoundException;

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
* An undoable data reference for the border around a component.
* 
* @author Thorn Green
*/
public class BorderObject
	implements EtherEventHandler, PropertyChangeListener {

	/**
	 * The undoable data model for the border.
	 */
	protected pdx_BorderModel_pdx_ObjectRef borderModel = null;

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
	 * The component to receive the border.
	 */
	protected transient VerdantiumComponent target = null;

	/**
	 * UndoManager through which to support undo.
	 */
	protected transient UndoManager undoMgr = null;

	/**
	 * Property name for firing of property change events.
	 */
	protected transient String propertyName = null;

	/**
	 * Constructs the Border for a UndoManager.
	 * @param _undoMgr The input UndoManager.
	 */
	public BorderObject(UndoManager _undoMgr) {
		undoMgr = _undoMgr;
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		IExtPair pair =
			pdx_BorderModel_pdx_ObjectRef.pdxm_new_BorderModel(
				mil,
				null,
				null,
				null);
		mil = pair.getMilieu();
		borderModel = (pdx_BorderModel_pdx_ObjectRef) (pair.getObject());
		undoMgr.handleCommitTempChange(mil);
		undoMgr.addPropertyChangeListener(this);
	}

	/**
	 * Handles the destruction of the BorderObject.
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
	 * Gets the name of the border class.
	 * @return The name of the border class.
	 */
	public String getClassName() {
		Object obj = borderModel.pdxm_getBorderClass(undoMgr.getCurrentMil());
		return ((String) obj);
	}

	/**
	 * Gets the parameter types of the constructor for the border.
	 * @return The parameter types of the constructor for the border.
	 */
	public Class<?>[] getBorderTypes() {
		Object obj = borderModel.pdxm_getBorderTypes(undoMgr.getCurrentMil());
		return ((Class<?>[]) obj);
	}

	/**
	 * Gets the parameters of the constructor for the border.
	 * @return The parameters of the constructor for the border.
	 */
	public Object[] getBorderParam() {
		Object obj = borderModel.pdxm_getBorderParam(undoMgr.getCurrentMil());
		return ((Object[]) obj);
	}

	/**
	 * Fires property change events on the BorderObject.
	 */
	protected void firePropertyChangeEvents()
		throws ResourceNotFoundException {

		javax.swing.border.Border MyBorder =
			BorderPropertyEditor.createBorder(
				getClassName(),
				getBorderTypes(),
				getBorderParam());

		target.getGUI().setBorder(MyBorder);
		target.getGUI().revalidate();
		target.getGUI().repaint();

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
			try {
				firePropertyChangeEvents();
			} catch (ResourceNotFoundException ex) {
				throw (new WrapRuntimeException("Undo Border Failed", ex));
			}
		}
	}

	/**
	 * Sets the border.
	 * @param CName The name of the border class.
	 * @param types The parameter types of the constructor for the border. 
	 * @param params The parameters of the constructor for the border.
	 * @throws ResourceNotFoundException
	 */
	public void setBorderObject(String CName, Class<?>[] types, Object[] params)
		throws ResourceNotFoundException {
		ExtMilieuRef mil =
			borderModel.pdxm_setBorderObject(
				undoMgr.getCurrentMil(),
				CName,
				types,
				params);

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
		VerdantiumComponent _target,
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
		VerdantiumComponent _target,
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

		if (in instanceof PropertyEditEtherEvent) {
			if (in
				.getEtherID()
				.equals(PropertyEditEtherEvent.isBorderSupported))
				return (PropertyEditEtherEvent.isBorderSupported);

			if (in
				.getEtherID()
				.equals(PropertyEditEtherEvent.setBorderObject)) {
				Object[] myo = (Object[]) (in.getParameter());
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				setBorderObject(
					(String) (myo[0]),
					(Class[]) (myo[1]),
					(Object[]) (myo[2]));
				undoMgr.commitUndoableOp(utag, "Border Change");
				return (null);
			}
		}

		return (ret);
	}

	/**
		* Writes the object to persistent storage.
		*/
	public void writeData(VersionBuffer MyF) {
		if (getClassName() != null) {
			MyF.setProperty("borderClass", getClassName());
			MyF.setProperty(
				"borderTypes",
				BorderPropertyEditor.getTypeStrArray(getBorderTypes()));
			MyF.setProperty("borderParam", getBorderParam());
		}
	}

	/**
	* Reads the object from persistent storage.
	*/
	public void readData(VersionBuffer MyF)
		throws DataFormatException, ResourceNotFoundException {
		Object myo = MyF.getProperty("borderClass");
		if (myo != null) {
			String BorderClass = (String) myo;
			VersionBuffer.chkNul(BorderClass);
			Class<?>[] BorderTypes =
				(Class<?>[]) (BorderPropertyEditor
					.getTypeObjArray(MyF.getProperty("borderTypes")));
			VersionBuffer.chkNul(BorderTypes);
			Object[] BorderParam = (Object[]) (MyF.getProperty("borderParam"));
			VersionBuffer.chkNul(BorderParam);
			setBorderObject(BorderClass, BorderTypes, BorderParam);
		}

	}

	
};

