package verdantium;

import java.awt.datatransfer.DataFlavor;

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
* The data flavor for a {@link TransVersionBuffer}.
* 
* @author Thorn Green
*/
public class TransVersionBufferFlavor extends DataFlavor {
	
	/**
	 * Constructor.
	 * @param humanPresentableName The human-readable name of the component.
	 * @param inComponentName The internal name of the component.
	 */
	public TransVersionBufferFlavor(String humanPresentableName, String inComponentName) {
		super(TransVersionBuffer.class, humanPresentableName);
		componentName = inComponentName;
	}

	/**
	 * Gets the internal name of the component.
	 * @return The internal name of the component.
	 */
	public String getComponentName() {
		return (componentName);
	}

	/**
	* Returns true iff. the stored flavor is equal to <code>dataFlavor</code>.
	* @param dataFlavor The data flavor to compare.
	* @return True iff. the stored flavor is equal to <code>dataFlavor</code>.
	*/
	public boolean equals(TransVersionBufferFlavor dataFlavor) {
		boolean tmp = false;

		if (dataFlavor != null) {
			tmp = componentName.equals(dataFlavor.getComponentName());
		}

		return (tmp);
	}

	@Override
	public boolean equals(DataFlavor dataFlavor) {
		if (dataFlavor instanceof TransVersionBufferFlavor) {
			return (equals((TransVersionBufferFlavor) dataFlavor));
		}
		else {
			return (false);
		}
	}

	/**
	 * The internal name of the component.
	 */
	private String componentName = null;
	
}

