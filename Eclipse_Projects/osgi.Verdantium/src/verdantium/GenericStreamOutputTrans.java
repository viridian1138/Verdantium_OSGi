package verdantium;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.ByteArrayOutputStream;

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
* Defines a Transferable that contains an OutputStream.
* 
* @author Thorn Green
*/
public class GenericStreamOutputTrans extends Object implements Transferable {

	/**
	 * Constructor.  Creates a Transferable for an OutputStream with a set of data flavors.
	 * @param s The data flavors associated with the stream.
	 * @param stream The stream.
	 */
	public GenericStreamOutputTrans(
		DataFlavor[] s,
		ByteArrayOutputStream stream) {
		this.flavors = s;
		this.stream = stream;
	}

	/**
	* Gets the stream.
	* @return The stream.
	*/
	public Object getTransferData(DataFlavor flavor) {
		return (stream);
	}

	/**
	* Gets the data flavors associated with the stream.
	* @return The data flavors associated with the stream.
	*/
	public DataFlavor[] getTransferDataFlavors() {
		return (flavors);
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
	 * The data flavors associated with the stream.
	 */
	private DataFlavor[] flavors;
	
	/**
	 * The stream.
	 */
	private ByteArrayOutputStream stream;
	
}

