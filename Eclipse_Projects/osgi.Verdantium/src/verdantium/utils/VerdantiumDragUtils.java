package verdantium.utils;

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

import javax.swing.JComponent;

import verdantium.EtherEvent;
import verdantium.StandardEtherEvent;
import verdantium.VerdantiumComponent;


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
*    | 08/04/2004            | Thorn Green (viridian_1138@yahoo.com)           | Support drag-and-drop                                                | Created class.
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
 * 
 * @author thorngreen
 *
 * Experimental class to support drag-and-drop.  Currently not supported.
 */
public class VerdantiumDragUtils implements DragGestureListener, DragSourceListener {
	
	/**
	 * The component to receive drag and drop events.
	 */
	VerdantiumComponent tar = null;

	/**
	 * Constructor.
	 * @param in The component to receive drag and drop events.
	 */
	public VerdantiumDragUtils(VerdantiumComponent in) {
		tar = in;
	}

	/**
	 * Sets a Verdantium component as receiving drag and drop events on a JComponent.  To be implemented.
	 * @param in The JComponent.
	 * @param tar The Verdantium component.
	 */
	public static void setDragUtil(JComponent in, VerdantiumComponent tar) {
		VerdantiumDragUtils utils = new VerdantiumDragUtils(tar);
		//DragSource ds = new DragSource();
		//ds.createDefaultDragGestureRecognizer( in , DnDConstants.ACTION_MOVE , utils );
	}

	/**
	 * Handles the recognition of a drag gesture.
	 * @param dge The input event.
	 */
	public void dragGestureRecognized(DragGestureEvent dge) {
		if (dge.getTriggerEvent().isAltDown()) {
			Object[] params = { tar, null };
			EtherEvent send = new StandardEtherEvent(this, StandardEtherEvent.dropComponent, params, tar);
			Transferable trans = new EtherEventTrans(send);
			dge.startDrag(null, trans, this);
		}
	}

	/**
	 * Stub to handle JDK 1.3 DND event.  Not used.
	 * @param dsde The input event.
	 */
	public void dragDropEnd(DragSourceDropEvent dsde) {}
	
	/**
	 * Stub to handle JDK 1.3 DND event.  Not used.
	 * @param dsde The input event.
	 */
	public void dragEnter(DragSourceDragEvent dsde) {}
	
	/**
	 * Stub to handle JDK 1.3 DND event.  Not used.
	 * @param dsde The input event.
	 */
	public void dragExit(DragSourceEvent dse) {}
	
	/**
	 * Stub to handle JDK 1.3 DND event.  Not used.
	 * @param dsde The input event.
	 */
	public void dragOver(DragSourceDragEvent dsde) {}
	
	/**
	 * Stub to handle JDK 1.3 DND event.  Not used.
	 * @param dsde The input event.
	 */
	public void dropActionChanged(DragSourceDragEvent dsde) {}

}

