package verdantium;

import java.awt.Component;

import javax.swing.DefaultDesktopManager;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;

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
* Desktop manager that handles frame dragging with transparent
* desktop pane, fixes rendering bugs, and implements part of
* the support for multi-level undo.
* <P>
* @author Thorn Green
*/
public class VerdantiumDesktopManager extends DefaultDesktopManager {

	/**
	 * Whether the desktop manager's state is currently updating.
	 */
	protected static boolean isUpdating = false;

	/**
	 * Gets whether the desktop manager's state is currently updating.
	 * @return Whether the desktop manager's state is currently updating.
	 */
	public static boolean isUpdating() {
		return (isUpdating);
	}

	@Override
	public void dragFrame(JComponent f, int newX, int newY) {
		isUpdating = true;
		try {
			if (f.isOpaque())
				super.dragFrame(f, newX, newY);
			else
				setBoundsForFrame(f, newX, newY, f.getWidth(), f.getHeight());
		} catch (Throwable e) {
			e.printStackTrace(System.out);
		}
		isUpdating = false;
	}

	@Override
	public void resizeFrame(
		JComponent f,
		int newX,
		int newY,
		int newWidth,
		int newHeight) {
		isUpdating = true;
		try {
			super.resizeFrame(f, newX, newY, newWidth, newHeight);
		} catch (Throwable e) {
			e.printStackTrace(System.out);
		}
		isUpdating = false;
	}

	@Override
	public void endDraggingFrame(JComponent f) {
		super.endDraggingFrame(f);
		Component c = f;
		while (!(c instanceof JDesktopPane))
			c = c.getParent();
		c.repaint();
		if (f instanceof VerdantiumUndoableInternalFrame) {
			((VerdantiumUndoableInternalFrame) f).handlePostDragOp();
		}
	}

	@Override
	public void endResizingFrame(JComponent f) {
		super.endResizingFrame(f);
		Component c = f;
		while (!(c instanceof JDesktopPane))
			c = c.getParent();
		c.repaint();
		if (f instanceof VerdantiumUndoableInternalFrame) {
			((VerdantiumUndoableInternalFrame) f).handlePostDragOp();
		}
	}

	
}

