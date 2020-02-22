package verdantium.utils;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import meta.WrapRuntimeException;
import verdantium.EtherEvent;

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
*    | 08/14/2002            | Thorn Green (viridian_1138@yahoo.com)           | GeoSlate.                                                            | Created EtherEventTrans to support Drag and Drop.
*    | 08/07/2004            | Thorn Green (viridian_1138@yahoo.com)           | Establish baseline for all changes in the last year.                 | Establish baseline for all changes in the last year.
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
* A Transferable containing a single {@link verdantium.EtherEvent} instance.
* 
* @author Thorn Green
*/
public class EtherEventTrans extends Object implements Transferable {
	
	/**
	* Stores the transfer event.
	*/
	transient protected EtherEvent event = null;

	/**
	* Temporary storage for the supported data flavor.
	*/
	transient protected DataFlavor dat = null;

	/**
	* Constructs the object given the input EtherEvent.
	* @param in The input EtherEvent.
	*/
	public EtherEventTrans(EtherEvent in) {
		this(in, "application/x-verdantium-ether-event; class=verdantium.EtherEvent");
	}

	/**
	* Constructs the object given the input EtherEvent.
	* @param in The input EtherEvent.
	* @param evt The MIME-type string for the associated data flavor.
	*/
	public EtherEventTrans(EtherEvent in, String evt) {
		event = in;
		try {
			dat = new DataFlavor(evt);
		}
		catch (Exception ex) {
			throw (new WrapRuntimeException("DataFlavor failed", ex));
		}
	}

	/**
	* Gets the EtherEvent instance.
	* @param flavor The requested flavor.  Ignored.
	*/
	public Object getTransferData(DataFlavor flavor) {
		return (event);
	}

	/**
	* Returns the data flavor.
	* @return The data flavor.
	*/
	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] flavor = { dat };
		return (flavor);
	}

	/**
	* Returns whether the data flavor is supported.
	* @return Whether the data flavor is supported.
	*/
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return (dat.equals(flavor));
	}

}

