package verdantium.core;

import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

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
*    | 03/09/2003            | Thorn Green (viridian_1138@yahoo.com)           | Needed more accessors for PageWelder.                                | Added accessors.
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
* This class suppiles the desktop pane in which the {@link ContainerApp} embeds
* component frames.  Those writing applications which make use of either
* transparency or complex JComponent layouts may need to modify this class
* via the subclassing mechanism,
* but most will not need to.  Most application developers can simply
* treat this as an internal class to {@link ContainerApp}.  Those that do
* need to subclass can find examples in the source code for {@link verdantium.standard.DrawApp}
* and {@link verdantium.standard.TextApp}.
* 
* @author Thorn Green
*/
public class ContainerAppDesktopPane extends PageSizeHandlerPane {
	transient private OnlyDesignerEditListener MyEdit = null;

	/**
	 * Constructs the desktop pane.
	 * @param mgr The manager for multi-level undo.
	 */
	public ContainerAppDesktopPane(UndoManager mgr) {
		super(mgr);
	}

	/**
	 * Constructs the desktop pane.
	 * @param mgr The manager for multi-level undo.
	 * @param in The Designer Edit listener for the class.
	 */
	public ContainerAppDesktopPane(
		UndoManager mgr,
		OnlyDesignerEditListener in) {
		super(mgr);
		MyEdit = in;
	}

	/**
	* Returns the Designer Edit listener for the class.
	* @return The Designer Edit listener for the class.
	*/
	public OnlyDesignerEditListener getMyEdit() {
		return (MyEdit);
	}

	/**
	* Updates the user interface.
	*/
	public void updateUI() {
		super.updateUI();

		if (getBackground() == null || getBackground() instanceof UIResource) {
			setBackground(UIManager.getColor("Panel.background"));
		}
	}

	/**
	* Adds an internal frame at a particular layer.
	* @param myF The internal frame to add.
	* @param layer The desired layer.
	*/
	public void add(JInternalFrame myF, Object layer) {
		super.add(myF, layer);
		if (myF instanceof ContainerAppInternalFrame) {
			((ContainerAppInternalFrame) myF).setOnlyDesignerEditListener(
				MyEdit);
		}
	}

}

