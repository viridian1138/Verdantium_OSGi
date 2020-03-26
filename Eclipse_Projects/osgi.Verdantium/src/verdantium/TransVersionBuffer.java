package verdantium;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.net.URL;

import meta.VersionBuffer;

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
*    | 9/24/2000             | Thorn Green (viridian_1138@yahoo.com)           | Needed to provide a standard way to document source file changes.    | Added a souce modification list to the documentation so that changes to the souce could be recorded. 
*    | 10/22/2000            | Thorn Green (viridian_1138@yahoo.com)           | Methods did not have names that followed standard Java conventions.  | Performed a global modification to bring the names within spec.
*    | 05/10/2002            | Thorn Green (viridian_1138@yahoo.com)           | Redundant information in persistent storage.                         | Made numerous persistence and packaging changes.
*    | 07/23/2002            | Thorn Green (viridian_1138@yahoo.com)           | TransVersionBuffer doesn't have a specified UID.                     | Added a defined UID to the class.
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
* Implements a {@link VersionBuffer} that supports the Transferable interface.
* This allows Verdantium components to store data in a manner that supports
* several types of versioning.  For more information on the versioning support,
* see {@link VersionBuffer}.
* 
* @author Thorn Green
*/
public class TransVersionBuffer extends VersionBuffer implements Transferable, UrlHolder {
	private transient URL MyU;

	/**
	* Creates a versioning storage object for the Verdantium component named
	* <code>cName</code> with human-readable name <code>mName</code>.
	* @param hName The human-readable name of the component.
	* @param cName The internal name of the component.
	*/
	public TransVersionBuffer(String hName, String cName) {
		super(true);
		setProperty("VerdantiumHumanPresentableName", hName);
		setProperty("VerdantiumComponentName", cName);
	}

	/**
	* This constructor is for serial storage support.  Don't use it directly.
	*/
	public TransVersionBuffer() {
		super();
	}

	/**
	* Gets the data associated with this object.
	* @return The data associated with this object.
	*/
	public Object getTransferData(DataFlavor flavor) {
		return (this);
	}

	/**
	* Gets the data flavors for this object.
	* @return The data flavors for this object.
	*/
	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor MyFlavor = new TransVersionBufferFlavor(readHumanPresentableName(), readComponentName());
		DataFlavor[] FlavorList = { MyFlavor };
		return (FlavorList);
	}

	/**
	* Returns true iff. <code>flavor</code> is supported by this Transferable.
	* @return True iff. <code>flavor</code> is supported by this Transferable.
	*/
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		boolean ret = false;

		if (flavor instanceof TransVersionBufferFlavor) {
			TransVersionBufferFlavor MyF = (TransVersionBufferFlavor) (flavor);
			if (readComponentName().equals(MyF.getComponentName()));
			ret = true;
		}

		return (ret);
	}

	/**
	* Gets the human-readable name for the component that stored this data.
	* @return The human-readable name for the component that stored this data.
	*/
	public String readHumanPresentableName() {
		Object myo = getProperty("VerdantiumHumanPresentableName");
		return ((String) myo);
	}

	/**
	* Gets the name of the component that stored this data.
	* @return The name of the component that stored this data.
	*/
	public String readComponentName() {
		Object myo = getProperty("VerdantiumComponentName");
		return ((String) myo);
	}

	/**
	* Gets the URL, or null if none.
	* @return The URL, or null if none.
	*/
	public URL getUrl() {
		return (MyU);
	}

	/**
	* Sets the URL, or null if none.
	* @param in The URL, or null if none.
	*/
	public void setUrl(URL in) {
		MyU = in;
	}

	
}

