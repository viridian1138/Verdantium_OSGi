package verdantium;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.beans.XMLDecoder;
import java.net.URL;

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
* Defines a Transferable that contains an XMLDecoder built from an input stream.
* 
* @author Thorn Green
*/
class GenericOStreamInputTrans
	extends Object
	implements Transferable, UrlHolder {

	/**
	 * Constructor.  Creates a Transferable for an XMLDecoder with a set of data flavors.
	 * @param s The data flavors associated with the decoder.
	 * @param stream The XMLDecoder.
	 * @param u The URL being decoded, or null if there is none.
	 */
	public GenericOStreamInputTrans(
		DataFlavor[] s,
		XMLDecoder stream,
		URL u) {
		flavors = s;
		decoder = stream;
		url = u;
	}

	/**
	* Gets the XMLDecoder.
	* @return The XMLDecoder.
	*/
	public Object getTransferData(DataFlavor flavor) {
		return (decoder);
	}

	/**
	* Gets the data flavors associated with the decoder.
	* @return The data flavors associated with the decoder.
	*/
	public DataFlavor[] getTransferDataFlavors() {
		return (flavors);
	}

	/**
	* Gets the URL being decoded, or null if there is none.
	* @return The URL being decoded, or null if there is none.
	*/
	public URL getUrl() {
		return (url);
	}

	/**
	* Sets the URL being decoded, or null if there is none.
	* @param in The URL being decoded, or null if there is none.
	*/
	public void setUrl(URL in) {
		url = in;
	}

	/**
	* Returns true iff. <code>flavor</code> is supported.
	* @param flavor The flavor to be checked.
	* @return True iff. the flavor is supported.
	*/
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		int count;
		boolean ret = false;

		for (count = 0; count < flavors.length; ++count)
			ret = ret || (flavors[count].equals(flavor));

		return (ret);
	}

	/**
	 * The URL being decoded, or null if there is none.
	 */
	private URL url;
	
	/**
	 * The data flavors associated with the decoder.
	 */
	private DataFlavor[] flavors;
	
	/**
	 * The XMLDecoder.
	 */
	private XMLDecoder decoder;
	
}

