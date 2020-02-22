package verdantium.utils;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.Iterator;

import javax.swing.JTextField;

import meta.WrapRuntimeException;
import verdantium.PropertyChangeSource;
import verdantium.VerdantiumComponent;
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
public class VerdantiumFileDndTextField extends JTextField {

	/**
	 * Constructor.
	 * @param tar The Verdantium component owning the field receiving the mouse-drop.
	 * @param src The property change source of the Verdantium component.
	 */
	public VerdantiumFileDndTextField(VerdantiumComponent tar, PropertyChangeSource src) {
		super();
		TextFieldDropUtil.setDropUtil(this, tar, src);
	}

	/**
	 * Constructor.
	 * @param in The initial string of the text field.
	 * @param tar The Verdantium component owning the field receiving the mouse-drop.
	 * @param src The property change source of the Verdantium component.
	 */
	public VerdantiumFileDndTextField(String in, VerdantiumComponent tar, PropertyChangeSource src) {
		super(in);
		TextFieldDropUtil.setDropUtil(this, tar, src);
	}

	protected static class TextFieldDropUtil implements DropTargetListener {
		
		/**
		 * The text field to receive the mouse drop.
		 */
		JTextField comp = null;
		
		/**
		 * The Verdantium component owning the field receiving the mouse-drop.
		 */
		VerdantiumComponent tar = null;
		
		/**
		 * The property change source of the Verdantium component.
		 */
		PropertyChangeSource src = null;
		
		/**
		 * Whether the alt-key was pressed during the mouse drop.
		 */
		boolean altPressed = false;

		/**
		 * Constructor.
		 * @param icomp The text field to receive the mouse drop.
		 * @param itar The Verdantium component owning the field receiving the mouse-drop.
		 * @param isrc The property change source of the Verdantium component.
		 */
		public TextFieldDropUtil(JTextField icomp, VerdantiumComponent itar, PropertyChangeSource isrc) {
			comp = icomp;
			tar = itar;
			src = isrc;
		}

		/**
		 * Sets a text field as receiving mouse-drop events for a component.
		 * @param in The text field to receive the mouse drop.
		 * @param tar The Verdantium component owning the field receiving the mouse-drop.
		 * @param src The property change source of the Verdantium component.
		 */
		public static void setDropUtil(JTextField in, VerdantiumComponent tar, PropertyChangeSource src) {
			TextFieldDropUtil dropUtil = new TextFieldDropUtil(in, tar, src);

			in.setDropTarget(new DropTarget());

			try {
				in.getDropTarget().addDropTargetListener(dropUtil);
			}
			catch (Exception ex) {
				throw (new WrapRuntimeException("Listener Addition Failed", ex));
			}
		}

		/**
		 * Stub to handle JDK 1.3 DND event.  Not used.
		 * @param dtde The input event.
		 */
		public void dragEnter(DropTargetDragEvent dtde) {
			altPressed = false;
		}

		/**
		 * Stub to handle JDK 1.3 DND event.  Not used.
		 * @param dtde The input event.
		 */
		public void dragExit(DropTargetEvent dte) {}

		/**
		 * Stub to handle JDK 1.3 DND event.  Not used.
		 * @param dtde The input event.
		 */
		public void dragOver(DropTargetDragEvent dtde) {}

		/**
		 * Handles a mouse-drop event from drag and drop.
		 * @param dtde The input event.
		 */
		public void drop(DropTargetDropEvent dtde) {
			Transferable trans = dtde.getTransferable();
			int accept_type = -1;
			File rFile = null;

			DataFlavor[] flavors = trans.getTransferDataFlavors();
			int cnt;
			for (cnt = 0; cnt < flavors.length; cnt++) {
				System.out.println(flavors[cnt]);
			}

			try {
				DataFlavor dat = new DataFlavor("application/x-java-file-list;class=java.util.List");
				if (trans.isDataFlavorSupported(dat)) {
					dtde.acceptDrop(DnDConstants.ACTION_MOVE);
					java.util.List lst = (java.util.List) (trans.getTransferData(dat));
					Iterator it = lst.iterator();
					Object ob = it.next();
					System.out.println(ob);
					rFile = (File) (ob);
					accept_type = 0;
				}

				if (accept_type < 0) {
					dtde.rejectDrop();
				}
			}
			catch (Exception ex) {
				handleThrow(new WrapRuntimeException("Flavor Failed", ex));
			}

			try {
				switch (accept_type) {

					case 0 :
						{
							dtde.dropComplete(true);
							comp.setText(rFile.getAbsolutePath());
							comp.repaint();
						}
						break;

					default :
						{
							dtde.dropComplete(false);
						}
						break;
				}
			}
			catch (Exception ex) {
				handleThrow(new WrapRuntimeException("Drop Failed", ex));
			}

		}

		/**
		 * Stub to handle JDK 1.3 DND event.  Not used.
		 * @param dtde The input event.
		 */
		public void dropActionChanged(DropTargetDragEvent dtde) {}

		/**
		 * Handles the throwing of an error or exception.
		 * @param ex The error or exception.
		 */
		public void handleThrow(Throwable ex) {
			VerdantiumUtils.handleThrow(ex, tar, src);
		}

		
	}

	
}

