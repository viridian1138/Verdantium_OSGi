package verdantium.core;

import java.awt.Color;
import java.awt.Graphics;

import verdantium.VerdantiumDesktopPane;
import verdantium.undo.UndoManager;

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
*    | 05/12/2002            | Thorn Green (viridian_1138@yahoo.com)           | Rendering bug.                                                       | Re-arranged desktop manager functionality to fix the bug.
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
* A desktop pane in which a rectangle representing the size of the document page
* can be optionally displayed.
* 
* @author Thorn Green
*/
public class PageSizeHandlerPane extends VerdantiumDesktopPane {

	/**
	 * Constructor.
	 * @param mgr The multi-level undo manager for the pane.
	 */
	public PageSizeHandlerPane(UndoManager mgr) {
		super(mgr);
	}

	/**
	* The page size handler that contains the size of the document to be displayed.
	*/
	private PageSizeHandler MyHndl = null;

	/**
	* Gets the pane's page size handler.
	* @return The pane's page size handler.
	*/
	public PageSizeHandler getPageSizeHandler() {
		return (MyHndl);
	}

	/**
	* Sets the pane's page size handler.
	* @param in The pane's page size handler.
	*/
	public void setPageSizeHandler(PageSizeHandler in) {
		MyHndl = in;
	}

	@Override
	public void paintChildren(Graphics g) {
		if (MyHndl != null) {
			g.setColor(Color.orange);
			g.drawRect(
				-2,
				-2,
				MyHndl.getPageSize().width + 4,
				MyHndl.getPageSize().height + 4);
		}

		super.paintChildren(g);
	}

	
}

