package verdantium.utils;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import meta.WrapRuntimeException;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.ProgramDirectorEvent;
import verdantium.PropertyChangeSource;
import verdantium.StandardEtherEvent;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumUtils;
import verdantium.core.DesignerControl;
import verdantium.core.OnlyDesignerEditListener;

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
public class VerdantiumDropUtils implements DropTargetListener {
	
	/**
	 * The JComponent of the drop target.
	 */
	JComponent comp = null;
	
	/**
	 * The Verdantium component to receive the drop event.
	 */
	VerdantiumComponent tar = null;
	
	/**
	 * Listener used to determine whether only the designer can edit the component,
	 * and rejects requests to edit via drag and drop if the component is not
	 * allowed to be edited.
	 */
	OnlyDesignerEditListener otar = null;
	
	/**
	 * The property change source of the Verdantium component.
	 */
	PropertyChangeSource src = null;
	
	/**
	 * Whether the current drag-and-drop operation uses component loading (as opposed to component embedding).
	 */
	boolean usingLoad = false;
	
	/**
	 * Whether the drop target supports component loading.
	 */
	boolean loadSupported = true;
	
	/**
	 * Whether the drop target supports component embedding.
	 */
	boolean embedSupported = false;

	
	/**
	 * Constructor.
	 * @param icomp The JComponent of the drop target.
	 * @param itar The Verdantium component to receive the drop event.
	 * @param isrc The property change source of the Verdantium component.
	 * @param iEmbedSupported Whether the drop target supports component embedding.
	 */
	public VerdantiumDropUtils(
		JComponent icomp,
		VerdantiumComponent itar,
		PropertyChangeSource isrc,
		boolean iEmbedSupported) {
		comp = icomp;
		tar = itar;
		src = isrc;
		if (tar instanceof OnlyDesignerEditListener)
			otar = (OnlyDesignerEditListener) tar;

		try {
			if (iEmbedSupported) {
				ProgramDirectorEvent pdee =
					new ProgramDirectorEvent(
						"Program Director",
						ProgramDirectorEvent.isProgramDirectorEventSupported,
						null,
						itar);
				Object ret = itar.processObjEtherEvent(pdee, null);
				if (ret instanceof Boolean) {
					embedSupported = ((Boolean) ret).booleanValue();
				}
			}
		}
		catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	 * Sets a JComponent as being a drop target for a Verdantium component.  To be implemented.
	 * @param in The JComponent of the drop target.
	 * @param tar The Verdantium component to receive the drop event.
	 * @param src The property change source of the Verdantium component.
	 */
	public static void setDropUtil(JComponent in, VerdantiumComponent tar, PropertyChangeSource src) {
		setDropUtil(in, tar, src, true);
	}

	/**
	 * Sets a JComponent as being a drop target for a Verdantium component.  To be implemented.
	 * @param in The JComponent of the drop target.
	 * @param tar The Verdantium component to receive the drop event.
	 * @param src The property change source of the Verdantium component.
	 * @param iEmbedSupported Whether the drop target supports component embedding.
	 */
	public static void setDropUtil(
		JComponent in,
		VerdantiumComponent tar,
		PropertyChangeSource src,
		boolean iEmbedSupported) {
		VerdantiumDropUtils dropUtil = new VerdantiumDropUtils(in, tar, src, iEmbedSupported);

		//in.setDropTarget( new DropTarget() );

		//try{ in.getDropTarget().addDropTargetListener( dropUtil ); }
		//	catch( Exception ex ) { throw( new WrapRuntimeException( "Listener Addition Failed" , ex ) ); }
	}

	/**
	 * Handles an entry into the drop target by a drag and drop operation.
	 * @param dsde The input event.
	 */
	public void dragEnter(DropTargetDragEvent dtde) {
		usingLoad = false;
	}

	/**
	 * Stub to handle JDK 1.3 DND event.  Not used.
	 * @param dsde The input event.
	 */
	public void dragExit(DropTargetEvent dte) {}

	/**
	 * Stub to handle JDK 1.3 DND event.  Not used.
	 * @param dsde The input event.
	 */
	public void dragOver(DropTargetDragEvent dtde) {}

	/**
	* Handles a mouse-drop event in the drop target.
	* @param dtde The input event.
	*/
	public void drop(DropTargetDropEvent dtde) {
		Transferable trans = dtde.getTransferable();
		int accept_type = -1;
		EtherEvent event = null;
		File rFile = null;
		java.util.List rList = null;
		Point locn = dtde.getLocation();

		boolean designerAccept = DesignerControl.isDesignTime();
		if (!designerAccept && (otar != null))
			designerAccept = !(otar.isOnlyDesignerEdits());

		if (!designerAccept) {
			handleThrow(
				new IllegalInputException(
					"Can't accept drop because the drop target isn't editable in this mode.  "
						+ "Please change either the DesignerControl component's mode or an appropriate OnlyDesignerEdits setting "
						+ "(if available)."));
			return;
		}

		DataFlavor[] flavors = trans.getTransferDataFlavors();
		int cnt;
		for (cnt = 0; cnt < flavors.length; cnt++) {
			System.out.println(flavors[cnt]);
		}

		try {
			DataFlavor dat = new DataFlavor("application/x-verdantium-ether-event; class=verdantium.EtherEvent");
			if (trans.isDataFlavorSupported(dat)) {
				dtde.acceptDrop(DnDConstants.ACTION_MOVE);
				event = (EtherEvent) (trans.getTransferData(dat));
				if (event instanceof StandardEtherEvent) {
					Object[] params = (Object[]) (event.getParameter());
					if ((params[0]) instanceof VerdantiumComponent)
						accept_type = 0;
					if ((params[0]) instanceof String)
						accept_type = 2;
				}
			}

			if (accept_type < 0) {
				dat = new DataFlavor("application/x-java-file-list;class=java.util.List");
				if (trans.isDataFlavorSupported(dat)) {
					dtde.acceptDrop(DnDConstants.ACTION_MOVE);
					java.util.List lst = (java.util.List) (trans.getTransferData(dat));
					rList = lst;
					Iterator it = lst.iterator();
					Object ob = it.next();
					System.out.println(ob);
					rFile = (File) (ob);
					accept_type = 1;
				}
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
						String[] options = { "Load", "Embed", "Cancel" };
						Component comp = null;
						int option = 0;
						if (loadSupported && embedSupported) {
							option =
								JOptionPane.showOptionDialog(
									comp,
									"Load or Embed?",
									"Load or Embed?",
									JOptionPane.DEFAULT_OPTION,
									JOptionPane.QUESTION_MESSAGE,
									null,
									options,
									options[2]);
							if (option == 2)
								return;
						}
						usingLoad = option == 0;
						if (usingLoad) {
							File tempFile = File.createTempFile("sav", "xfile");
							Object[] params = (Object[]) (event.getParameter());
							VerdantiumComponent cmp = (VerdantiumComponent) (params[0]);
							DataFlavor flavor = (cmp.getPersistentOutputDataFlavorsSupported())[0];
							URL MyU = tempFile.toURL();
							ProgramDirector.saveNewPersistentFile(MyU, cmp, flavor);
							URLConnection MyCon = MyU.openConnection();
							trans = ProgramDirector.getTransferable(MyCon);
							tar.loadPersistentData(trans.getTransferDataFlavors()[0], trans);
							tar.getGUI().repaint();
							tempFile.delete();
						}
						else {
							try {
								((Object[]) (event.getParameter()))[1] = locn;
								event.setTarget(tar);
								ProgramDirector.fireEtherEvent(event, null);
							}
							catch (Throwable ex) {
								handleThrow(ex);
							}
						}
					}
					break;

				case 1 :
					{
						dtde.dropComplete(true);
						String[] options = { "Load", "Embed", "Cancel" };
						Component comp = null;
						int option = 0;
						if (loadSupported && embedSupported) {
							option =
								JOptionPane.showOptionDialog(
									comp,
									"Load or Embed?",
									"Load or Embed?",
									JOptionPane.DEFAULT_OPTION,
									JOptionPane.QUESTION_MESSAGE,
									null,
									options,
									options[2]);
							if (option == 2)
								return;
						}
						usingLoad = option == 0;
						if (usingLoad) {
							URL MyU = rFile.toURL();
							URLConnection MyCon = MyU.openConnection();
							trans = ProgramDirector.getTransferable(MyCon);
							tar.loadPersistentData(trans.getTransferDataFlavors()[0], trans);
							tar.getGUI().repaint();
						}
						else {
							try {
								Object[] params = { rList, locn };
								EtherEvent send =
									new StandardEtherEvent(
										"Program Director",
										StandardEtherEvent.dropComponent,
										params,
										tar);
								ProgramDirector.fireEtherEvent(send, null);
							}
							catch (Throwable ex) {
								handleThrow(ex);
							}
						}
					}
					break;

				case 2 : /* Can only embed for this case. */ {
						System.out.println("Trying Embed");
						if (embedSupported) {
							try {
								((Object[]) (event.getParameter()))[1] = locn;
								event.setTarget(tar);
								ProgramDirector.fireEtherEvent(event, null);
							}
							catch (Throwable ex) {
								handleThrow(ex);
							}
						}
						else {
							handleThrow(
								new IllegalInputException("Unable to embed this; drop target doesn't support embedding."));
						}
					}
					break;

				default :
					{
						dtde.dropComplete(false);
					}
					break;
			}
		}
		catch (RuntimeException ex) {
			handleThrow(new WrapRuntimeException("Drop Failed", ex));
		}
		catch (Exception ex) {
			handleThrow(ex);
		}

	}

	/**
	 * Stub to handle JDK 1.3 DND event.  Not used.
	 * @param dtde The input event.
	 */
	public void dropActionChanged(DropTargetDragEvent dtde) {}

	/**
	 * Handles the throwing of an error or exception.
	 * @param ex The input error or exception.
	 */
	public void handleThrow(Throwable ex) {
		VerdantiumUtils.handleThrow(ex, tar, src);
	}

	
}

