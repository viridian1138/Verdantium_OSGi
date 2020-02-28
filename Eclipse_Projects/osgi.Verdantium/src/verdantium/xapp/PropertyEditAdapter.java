/*
 * Created on Nov 30, 2005
 *
 */
package verdantium.xapp;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.VerdantiumUtils;

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
 * @author thorngreen
 *
 * Provides code that is common to most Verdantium property editors.
 * Typical verdantium property editors do not try to persist themselves
 * (it's overkill for most components to do this).  The presistence
 * interfaces are present, but this default property edit adapter simply
 * returns null at all the interface points.  Note: there are other compound
 * document frameworks (notably OpenDoc) that do typically persist these classes.
 * Verdantium doesn't in attempt to make it smaller, lighter, and better suited
 * to typical file formats (e.g. most spreadsheets don't persist all of their dialog
 * windows; instead they save to some data-oriented format such as CSV).  If
 * property editor presistence is desired, one is free to make full use of the
 * presistence methods in the VerdantiumPropertiesEditor interface.
 */
public abstract class PropertyEditAdapter
	implements VerdantiumPropertiesEditor, PropertyChangeListener {

	/**
	* Handles property change events.
	* @param evt The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == ProgramDirector.propertyHide) {
			VerdantiumUtils.disposeContainer(this);
		}

	}

	/**
	 * Handles Ether Events.
	 * @param in The event to handle.
	 * @param refcon A reference to context data for the event.
	 * @return The result of handling the event, or null if there is no result.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon)
		throws Throwable {
		return (null);
	}

	/**
	* Gets the input data flavors supported.  None are supported in this class.
	* @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		return (null);
	}

	/**
	* Gets the output data flavors supported.  None are supported in this class.
	* @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		return (null);
	}

	/**
	* Interface to load persistent data.  Does nothing.
	*/
	public void loadPersistentData(DataFlavor flavor, Transferable trans) {
	}

	/**
	* Interface to save persistent data.  Does nothing.
	*/
	public Transferable savePersistentData(DataFlavor flavor) {
		return (null);
	}

	/**
	* Handles the destruction of the component.
	*/
	public void handleDestroy() {
	}

}
