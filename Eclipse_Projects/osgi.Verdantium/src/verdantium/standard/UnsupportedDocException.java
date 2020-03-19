package verdantium.standard;

import verdantium.PropertyChangeSource;
import verdantium.ThrowHandler;
import verdantium.VerdantiumComponent;
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
*    | 11/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Needed support for paragraph formatting.                             | Created this class to to be thrown upon invalid requests.
*    | 11/17/2001            | Thorn Green (viridian_1138@yahoo.com)           | UnsupportedDocException handling was not script-compatible.          | Changed the exception to implement ThrowHandler.
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
* An exception class for when a TextApp request is not supported
* by a certain document type.  For instance, asking for a font change
* in a particular region of a non-Rich-Text ASCII-only document.
*
* @author Thorn Green
*/
public class UnsupportedDocException
	extends Exception
	implements ThrowHandler {

	/**
	 * Constructor.
	 */
	public UnsupportedDocException() {
	}

	/**
	* Handles an exception for a Verdantium app. by displaying a message window.
	* @param in The exception to handle.
	* @param The component generating the exception.
	* @param src The data model of the component generating the exception.
	*/
	public VerdantiumPropertiesEditor handleThrow(
		Throwable in,
		VerdantiumComponent comp,
		PropertyChangeSource src) {
		return (
			VerdantiumUtils.produceMessageWindow(
				in,
				"Operation Not Supported",
				"Operation Not Supported : ",
				"The operation you requested is not supported on this kind of document.",
				comp,
				src));
	}

	
}

