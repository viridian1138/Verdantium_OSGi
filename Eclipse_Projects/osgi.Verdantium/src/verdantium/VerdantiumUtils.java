package verdantium;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterAbortException;
import java.awt.print.PrinterException;
import java.awt.print.PrinterIOException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.RepaintManager;

import meta.PrimitiveThrowHandler;
import verdantium.core.CustomPageSetupPropertyEditor;
import verdantium.core.PrintPreviewPropertyEditor;
import verdantium.undo.UTag;
import verdantium.undo.UndoEtherEvent;
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
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/10/2001            | Thorn Green (viridian_1138@yahoo.com)           | Second-Cut at Error Handling.                                        | Second-Cut at Error Handling.
*    | 10/19/2001            | Thorn Green (viridian_1138@yahoo.com)           | Expanded window menus for GeoFrame/GeoPad.                           | Added functionality relating to window menus.
*    | 11/17/2001            | Thorn Green (viridian_1138@yahoo.com)           | Verdantium Exceptions not modular or extensible.                     | Made the exception handling more extensible.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
*    | 05/10/2002            | Thorn Green (viridian_1138@yahoo.com)           | Redundant information in persistent storage.                         | Made numerous persistence and packaging changes.
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
* Performs a series of utility functions.
* 
* @author Thorn Green
*/
public class VerdantiumUtils extends Object implements Printable {
	
	/**
	 * Whether the previous call was implicitly handled.  Only use this member after
	 * calling handleImplicitObjEtherEvent() or handleImplicitClassEtherEvent().
	 */
	static boolean handledImplicit = false;
	
	/**
	 * The target component to be printed when the printing constructor is used.
	 */
	VerdantiumComponent target;

	/**
	* This constructor is used for VerdantiumUtils's implementation of
	* printing support only.  Do not make direct use of it.
	* @param tar The target component to be printed.
	*/
	public VerdantiumUtils(VerdantiumComponent tar) {
		target = tar;
	}

	/**
	 * Prints a page of the target component.  For internal use of VerdantiumUtils only.
	 * Do not make direct use of it.
	 * @param gi The graphics context to which to render the target component.
	 * @param pf The format of the current page.
	 * @param pageIndex The index of the current page.
	 * @return Returns whether the resulting page exists.
	 */
	public int print(Graphics gi, PageFormat pf, int pageIndex)
		throws PrinterException {
		JComponent myC = (target).getGUI();
		int ret = 0;
		try {
			ret = VerdantiumUtils.printJComponent(gi, pf, pageIndex, myC);
		} catch (PrinterException e) {
			throw (e);
		}
		return (ret);
	}

	/**
	* Gets the enclosing container frame for a component.
	* @param in The component for which to get the frame.
	* @return The enclosing container frame.
	*/
	protected static Component getContainer(VerdantiumComponent in) {
		Component myP = in.getGUI();

		while (!(myP instanceof JInternalFrame)
			&& !(myP instanceof Window)
			&& !(myP == null)
			&& !(myP instanceof Applet))
			myP = myP.getParent();

		return (myP);
	}
        
   /**
    * Quits a component's container frame.
    * @param in The component for which to quit the frame.
    */
   public static void quitContainer(VerdantiumComponent in) {
		Component myP = getContainer(in);

		if (myP instanceof VerdantiumUndoableInternalFrame ) {
	           ( (VerdantiumUndoableInternalFrame) myP ).setClosed( true );
		}
                else
                {
                    disposeContainer( in );
                }

	}
        

	/**
	* Disposes a component's container frame.
	* @param in The component for which to dispose the frame.
	*/
	public static void disposeContainer(VerdantiumComponent in) {
		Component myP = getContainer(in);

		if (myP instanceof Window) {
			((Window) myP).dispose();
		}

		if (myP instanceof JInternalFrame) {
			((JInternalFrame) myP).dispose();
		}

	}

	/**
	* Sets whether a component's container frame is visible.
	* @param vis Whether the frame is to be visible.
	* @param in The component for which to get the frame visibility.
	*/
	public static void setContainerVisible(
		boolean vis,
		VerdantiumComponent in) {
		Component myP = getContainer(in);

		if (myP instanceof Window) {
			((Window) myP).setVisible(vis);
		}

		if (myP instanceof JInternalFrame) {
			((JInternalFrame) myP).setVisible(vis);
		}
	}

	/**
	* Gets the title of a component's container frame.
	* @param in The component for which to get the frame title.
	* @return The frame title.
	*/
	public static String getContainerTitle(VerdantiumComponent in) {
		String ret = "Name Not Yet Resolved";
		Component myP = getContainer(in);

		if (myP instanceof Frame) {
			ret = ((Frame) myP).getTitle();
		}

		if (myP instanceof JInternalFrame) {
			ret = ((JInternalFrame) myP).getTitle();
		}

		return (ret);
	}

	/**
	* Moves a component's container frame to the front.
	* @param in The component for which to move the frame to the front.
	*/
	public static void toFrontContainer(VerdantiumComponent in) {
		Component myP = getContainer(in);

		if (myP instanceof Window) {
			((Window) myP).toFront();
		}

		if (myP instanceof JInternalFrame) {
			((JInternalFrame) myP).toFront();
		}

	}

	/**
	* Causes the frame containing a component to request the focus.
	* @param in The component for which to request frame focus.
	*/
	public static void requestFocusContainer(VerdantiumComponent in) {
		Component myP = getContainer(in);

		if (myP instanceof Window) {
			((Window) myP).requestFocus();
		}

		if (myP instanceof JInternalFrame) {
			((JInternalFrame) myP).requestFocus();
		}

	}

	/**
	* Adds a key listener to the frame containing a component.
	* @param in The component for which to add the key listener.
	* @param key The key listener to add.
	*/
	public static void containerAddKeyListener(
		VerdantiumComponent in,
		KeyListener key) {
		Component myP = getContainer(in);

		if (myP instanceof Window) {
			((Window) myP).addKeyListener(key);
		}

		if (myP instanceof JInternalFrame) {
			((JInternalFrame) myP).addKeyListener(key);
		}

	}

	/**
	* Causes the frame containing a component to request the focus.
	* @param in The component for which to request frame focus.
	*/
	public static void containerRequestFocus(VerdantiumComponent in) {
		Component myP = getContainer(in);

		if (myP instanceof Window) {
			((Window) myP).requestFocus();
		}

		if (myP instanceof JInternalFrame) {
			((JInternalFrame) myP).requestFocus();
		}

	}

	/**
	* Adds an action listener that is fired whenever the component's container frame
	* is activated.
	* @param in The component for which to add the activation listener.
	* @param al The activation listener to add.
	*/
	public static void addActivationActionListener(
		VerdantiumComponent in,
		ActionListener al) {
		Component myP = getContainer(in);

		if (myP instanceof VerdantiumFrame) {
			((VerdantiumFrame) myP).addActivationActionListener(al);
		}

		if (myP instanceof VerdantiumInternalFrame) {
			((VerdantiumInternalFrame) myP).addActivationActionListener(al);
		}

	}

	/**
	* Removes an activation listener from the component's container frame.
	* @param in The component for which to remove the activation listener.
	* @param al The activation listener to remove.
	*/
	public static void removeActivationActionListener(
		VerdantiumComponent in,
		ActionListener al) {
		Component myP = getContainer(in);

		if (myP instanceof VerdantiumFrame) {
			((VerdantiumFrame) myP).removeActivationActionListener(al);
		}

		if (myP instanceof VerdantiumInternalFrame) {
			((VerdantiumInternalFrame) myP).removeActivationActionListener(al);
		}

	}

	/**
	* Adds an action listener that is fired whenever the component's container frame
	* is closed.
	* @param in The component for which to add the closure listener.
	* @param al The closure listener to add.
	*/
	public static void addClosureActionListener(
		VerdantiumComponent in,
		ActionListener al) {
		Component myP = getContainer(in);

		if (myP instanceof VerdantiumFrame) {
			((VerdantiumFrame) myP).addClosureActionListener(al);
		}

		if (myP instanceof VerdantiumInternalFrame) {
			((VerdantiumInternalFrame) myP).addClosureActionListener(al);
		}

	}

	/**
	* Removes a closure listener from the component's container frame.
	* @param in The component for which to remove the closure listener.
	* @param al The closure listener to remove.
	*/
	public static void removeClosureActionListener(
		VerdantiumComponent in,
		ActionListener al) {
		Component myP = getContainer(in);

		if (myP instanceof VerdantiumFrame) {
			((VerdantiumFrame) myP).removeClosureActionListener(al);
		}

		if (myP instanceof VerdantiumInternalFrame) {
			((VerdantiumInternalFrame) myP).removeClosureActionListener(al);
		}

	}

	/**
	 * Prints a Swing JComponent to a Printable.
	 * @param gi The graphics context to which to render the JComponent.
	 * @param pf The format of the current page.
	 * @param pageIndex The index of the current page.
	 * @param myC The Swing JComponent to be printed.
	 * @return Returns whether the resulting page exists.
	 * @throws PrinterException
	 */
	public static int printJComponent(
		Graphics gi,
		PageFormat pf,
		int pageIndex,
		JComponent myC)
		throws PrinterException {
		AffineTransform coordTrans = new AffineTransform();
		Graphics2D g = (Graphics2D) gi;
		coordTrans.setToTranslation(pf.getImageableX(), pf.getImageableY());
		AffineTransform trans = g.getTransform();
		trans.concatenate(coordTrans);
		g.setTransform(trans);
		RepaintManager.currentManager(myC).setDoubleBufferingEnabled(false);
		myC.print(g);
		RepaintManager.currentManager(myC).setDoubleBufferingEnabled(true);
		return (Printable.PAGE_EXISTS);
	}

	/**
	* Handles a series of standard Ether Events on behalf of a compoennt.
	* @param in The event to be handled.
	* @param refcon A reference to context data that the generating code can associate with the rectangle.  See various references to "refcon" in MacOS programming.
	* @return The result of executing the event.
	*/
	public static Object handleImplicitObjEtherEvent(
		EtherEvent in,
		Object refcon)
		throws Throwable {
		handledImplicit = false;
		Object target = in.getTarget();
		Object ob = null;

		if (in instanceof StandardEtherEvent) {

			if (in.getEtherID().equals(StandardEtherEvent.objPrintEvent)) {
				if (target instanceof BookPrintable) {
					((BookPrintable) target).handleBookPrinting();
				} else {
					PrinterJob job = PrinterJob.getPrinterJob();
					EtherEvent s3 =
						new StandardEtherEvent(
							"Program Director",
							StandardEtherEvent.getDocPageFormat,
							null,
							target);
					ob = ProgramDirector.fireEtherEvent(s3, null);
					PageFormat pf1 = (PageFormat) (ob);
					if (pf1 == null) {
						pf1 = job.defaultPage();
					}
					/* pf1 = job.validatePage( pf1 ); */
					Book bk = new Book();
					if (target instanceof Printable)
						bk.append((Printable) target, pf1);
					else
						bk.append(
							new VerdantiumUtils((VerdantiumComponent) target),
							pf1);
					job.setPageable(bk);
					if (job.printDialog()) {
						job.print();
					}

					UTag utag = new UTag();
					UndoManager mgr = getUndoManager(target);
					if (mgr != null) {
						mgr.prepareForTempCommit(utag);
					}
					EtherEvent s4 =
						new StandardEtherEvent(
							"Program Director",
							StandardEtherEvent.setDocPageFormat,
							pf1,
							target);
					ProgramDirector.fireEtherEvent(s4, null);
					if (mgr != null) {
						mgr.commitUndoableOp(
							utag,
							"Page Setup Change From \"Print...\" Command");
					}
				}

				handledImplicit = true;
			}

			if (in
				.getEtherID()
				.equals(StandardEtherEvent.objPrintPreviewEvent)) {
				if (target instanceof BookPrintable) {
					((BookPrintable) target).handlePrintPreview();
				} else {
					PrinterJob job = PrinterJob.getPrinterJob();
					EtherEvent s3 =
						new StandardEtherEvent(
							"Program Director",
							StandardEtherEvent.getDocPageFormat,
							null,
							target);
					ob = ProgramDirector.fireEtherEvent(s3, null);
					PageFormat pf1 = (PageFormat) (ob);
					if (pf1 == null) {
						pf1 = job.defaultPage();
					}
					/* pf1 = job.validatePage( pf1 ); */
					Book bk = new Book();
					if (target instanceof Printable)
						bk.append((Printable) target, pf1);
					else
						bk.append(
							new VerdantiumUtils((VerdantiumComponent) target),
							pf1);

					if (target instanceof PropertyChangeSource) {
						PrintPreviewPropertyEditor prev =
							new PrintPreviewPropertyEditor(
								bk,
								(PropertyChangeSource) target);
						ProgramDirector.showPropertyEditor(
							prev,
							null,
							"Print Preview Property Editor");

						EtherEvent s4 =
							new StandardEtherEvent(
								"Program Director",
								StandardEtherEvent.setDocPageFormat,
								pf1,
								target);
						ProgramDirector.fireEtherEvent(s4, null);
					}
				}

				handledImplicit = true;
			}

		}

		if ((target instanceof VerdantiumComponent)
			&& !(target instanceof VerdantiumPropertiesEditor)
			&& (in instanceof StandardEtherEvent)) {
			if (in.getEtherID().equals(StandardEtherEvent.dropComponent
				/* Embed */
				)) {
				Object[] params = (Object[]) (in.getParameter());
				if ((params[0]) instanceof VerdantiumComponent) {
					File tempFile = File.createTempFile("sav", "xfile");
					VerdantiumComponent cmp = (VerdantiumComponent) (params[0]);
					Point clickPoint = (Point) (params[1]);
					DataFlavor flavor =
						(cmp.getPersistentOutputDataFlavorsSupported())[0];
					URL myU = tempFile.toURL();
					ProgramDirector.saveNewPersistentFile(myU, cmp, flavor);
					Object[] oparams = { myU, clickPoint, new Boolean(false)};
					ProgramDirectorEvent pdee =
						new ProgramDirectorEvent(
							"Program Director",
							ProgramDirectorEvent.loadURL,
							oparams,
							target);
					((VerdantiumComponent) target).processObjEtherEvent(
						pdee,
						null);
					tempFile.delete();
				}
				if ((params[0]) instanceof java.util.List) {
					Point clickPoint = (Point) (params[1]);
					java.util.List<File> lst = (java.util.List<File>) (params[0]);
					for( final File rFile : lst ) {
						URL myU = rFile.toURL();
						Object[] oparams =
							{ myU, clickPoint, new Boolean(false)};
						ProgramDirectorEvent pdee =
							new ProgramDirectorEvent(
								"Program Director",
								ProgramDirectorEvent.loadURL,
								oparams,
								target);
						System.out.println("target " + target);
						System.out.println("pt " + pdee.getClickPoint());
						((VerdantiumComponent) target).processObjEtherEvent(
							pdee,
							null);
					}
				}
				if ((params[0]) instanceof String) {
					String str = (String) (params[0]);
					Point locn = (Point) (params[1]);
					Object[] oparams = { str, locn };
					ProgramDirectorEvent pdee =
						new ProgramDirectorEvent(
							"Program Director",
							ProgramDirectorEvent.createApp,
							oparams,
							target);
					((VerdantiumComponent) target).processObjEtherEvent(
						pdee,
						null);
				}
			}

			if (in.getEtherID().equals(StandardEtherEvent.objSaveAsEvent)) {
				Object[] param = (Object[]) (in.getParameter());
				URL myU = (URL) (param[0]);
				VerdantiumComponent myPart = (VerdantiumComponent) target;
				DataFlavor myFlavor = (DataFlavor) (param[1]);
				ProgramDirector.saveNewPersistentFile(myU, myPart, myFlavor);
				Object[] param2 = { myU, myFlavor };
				EtherEvent s2 =
					new StandardEtherEvent(
						"Program Director",
						StandardEtherEvent.setUrlLocn,
						param2,
						target);
				ProgramDirector.fireEtherEvent(s2, null);
				UndoManager mgr = getUndoManager(myPart);
				if (mgr != null)
					mgr.noteFileSave();
				handledImplicit = true;
			}

			if (in
				.getEtherID()
				.equals(StandardEtherEvent.objSaveACopyAsEvent)) {
				Object[] param = (Object[]) (in.getParameter());
				URL myU = (URL) (param[0]);
				VerdantiumComponent myPart = (VerdantiumComponent) target;
				DataFlavor myFlavor = (DataFlavor) (param[1]);
				ProgramDirector.saveNewPersistentFile(myU, myPart, myFlavor);
				UndoManager mgr = getUndoManager(myPart);
				if (mgr != null)
					mgr.noteFileSave();
				handledImplicit = true;
			}

			if (in.getEtherID().equals(StandardEtherEvent.objQuitEvent)) {
				quitContainer((VerdantiumComponent) target);
				handledImplicit = true;
			}

			if (in.getEtherID().equals(StandardEtherEvent.objNewEvent)) {
				VerdantiumComponent tar = (VerdantiumComponent) target;
				UndoManager undoMgr = getUndoManager(target);
				UTag utag = new UTag();
				if (undoMgr != null) {
					undoMgr.prepareForTempCommit(utag);
				}
				tar.loadPersistentData(null, null);
				tar.getGUI().repaint();
				Object[] param2 = { null, null };
				EtherEvent s2 =
					new StandardEtherEvent(
						"Program Director",
						StandardEtherEvent.setUrlLocn,
						param2,
						target);
				ProgramDirector.fireEtherEvent(s2, null);
				if (undoMgr != null) {
					undoMgr.commitUndoableOp(utag, "File -> New");
				}
				handledImplicit = true;
			}

			if (in.getEtherID().equals(StandardEtherEvent.objOpenEvent)) {
				VerdantiumComponent tar = (VerdantiumComponent) target;
				UndoManager undoMgr = getUndoManager(target);
				UTag utag = new UTag();
				if (undoMgr != null) {
					undoMgr.prepareForTempCommit(utag);
				}
				URL u = (URL) (in.getParameter());
				URLConnection myCon = u.openConnection();
				Transferable trans = ProgramDirector.getTransferable(myCon);
				try {
					((VerdantiumComponent) tar).loadPersistentData(
						trans.getTransferDataFlavors()[0],
						trans);
					tar.getGUI().repaint();
					if (undoMgr != null) {
						undoMgr.commitUndoableOp(utag, "File Load");
					}
				} catch (Throwable ex) {
					if (undoMgr != null)
						undoMgr.commitHandleCriticalFailure(utag);
					throw (ex);
				}
				handledImplicit = true;
			}

			if (in.getEtherID().equals(StandardEtherEvent.objPageSetupEvent)) {
				PrinterJob job = PrinterJob.getPrinterJob();
				ob =
					((VerdantiumComponent) target).processObjEtherEvent(
						in,
						refcon);

				if (ob == null) {
					EtherEvent s2 =
						new StandardEtherEvent(
							"Program Director",
							StandardEtherEvent.getDocPageFormat,
							null,
							target);
					Object ob2 = ProgramDirector.fireEtherEvent(s2, null);
					PageFormat pg = (PageFormat) (ob2);
					if (pg == null) {
						pg = job.defaultPage();
					}
					/* pg = job.validatePage( pg ); */
					PageFormat po = job.pageDialog(pg);
					ob = po;

					EtherEvent s3 =
						new StandardEtherEvent(
							"Program Director",
							StandardEtherEvent.setDocPageFormat,
							po,
							target);
					ProgramDirector.fireEtherEvent(s3, null);
				}

				handledImplicit = true;
				return (ob);
			}

			if (in
				.getEtherID()
				.equals(StandardEtherEvent.objCustomPageSetupEvent)) {
				PrinterJob job = PrinterJob.getPrinterJob();
				ob =
					((VerdantiumComponent) target).processObjEtherEvent(
						in,
						refcon);

				if (ob == null) {
					EtherEvent s2 =
						new StandardEtherEvent(
							"Program Director",
							StandardEtherEvent.getDocPageFormat,
							null,
							target);
					Object ob2 = ProgramDirector.fireEtherEvent(s2, null);
					PageFormat pg = (PageFormat) (ob2);
					if (pg == null) {
						pg = job.defaultPage();
					}
					/* pg = job.validatePage( pg ); */
					ob = pg;
					CustomPageSetupPropertyEditor pr =
						new CustomPageSetupPropertyEditor(
							pg,
							(EtherEventPropertySource) target);
					ProgramDirector.showPropertyEditor(
						pr,
						((VerdantiumComponent) target).getGUI(),
						"Custom Page Setup Editor");
				}

				handledImplicit = true;
				return (ob);
			}

		}

		return (null);
	}

	/**
	 * Handles a series of standard Ether Events on behalf of a component.
	 * @param in The event to be handled.
	 * @param refcon A reference to context data that the generating code can associate with the rectangle.  See various references to "refcon" in MacOS programming.
	 * @return The result of executing the event.
	 * @throws Throwable
	 */
	public static Object handleImplicitClassEtherEvent(
		EtherEvent in,
		Object refcon)
		throws Throwable {
		handledImplicit = false;
		Object target = in.getTarget();

		if ((target instanceof String) && (in instanceof StandardEtherEvent)) {
			String programName = (String) target;
			Class<? extends VerdantiumComponent> programClass = ProgramDirector.getComponentClass(programName);

			if (in.getEtherID().equals(StandardEtherEvent.classNewEvent)) {
				Container parent = (Container) (in.getParameter());
				VerdantiumComponent myC =
					ProgramDirector.showComponent(
						programClass,
						parent,
						programName);
				handledImplicit = true;
				return (myC);
			}

		}

		if (in instanceof StandardEtherEvent) {
			if (in.getEtherID().equals(StandardEtherEvent.getEventSource)) {
				handledImplicit = true;
				return (in.getSource());
			}
		}

		return (null);
	}

	/**
	* Returns whether the previous call was implicitly handled.  Only call this method after
	* calling handleImplicitObjEtherEvent() or handleImplicitClassEtherEvent().
	* @return Whether the call was implicitly handled.
	*/
	public static final boolean implicitEventHandled() {
		return (handledImplicit);
	}

	/**
	 * Handles the throwing of an error or exception by displaying a property edit window..
	 * @param in The error or exception to be displayed in the window.
	 * @param comp The component generating the error or exception.
	 * @param src The data model of the component generating the error or exception.
	 * @return The generated property editor displaying the error or exception.
	 */
	public static VerdantiumPropertiesEditor handleThrow(
		Throwable in,
		VerdantiumComponent comp,
		PropertyChangeSource src) {
		Throwable ex = in;

		while (ex instanceof InvocationTargetException) {
			ex = ((InvocationTargetException) ex).getTargetException();
		}

		if (ex instanceof ThrowHandler)
			return (((ThrowHandler) ex).handleThrow(in, comp, src));

		if (ex instanceof PrimitiveThrowHandler) {
			try {
				Object[] ob = ((PrimitiveThrowHandler) ex).handleThrow(in);
				String s1 = (String) (ob[1]);
				String s2 = (String) (ob[2]);
				String s3 = (String) (ob[3]);
				return (produceMessageWindow(in, s1, s2, s3, comp, src));
			} catch (Exception ex2) { /* Do Nothing. */
			}
		}

		if (ex instanceof IOException) {
			if (ex instanceof FileNotFoundException) {
				return (
					produceMessageWindow(
						in,
						"File Not Found",
						"A File Was Not Found : ",
						ex.getMessage(),
						comp,
						src));
			}

			if (ex instanceof InterruptedIOException) {
				return (
					produceMessageWindow(
						in,
						"I/O Interrupted",
						"I/O Interrupted : ",
						"I/O was unexpectedly interrupted.",
						comp,
						src));
			}

			if (ex instanceof UnknownHostException) {
				return (
					produceMessageWindow(
						in,
						"Host Not Found",
						"Host Not Found : ",
						ex.getMessage(),
						comp,
						src));
			}

			if (ex instanceof MalformedURLException) {
				return (
					produceMessageWindow(
						in,
						"Bad URL",
						"The operation could not be completed because a URL is bad : ",
						ex.getMessage(),
						comp,
						src));
			}

			return (
				produceMessageWindow(
					in,
					"I/O or Data Format Problem",
					"I/O or Data Format Problem : ",
					ex.getMessage(),
					comp,
					src));
		}

		if (ex instanceof ClassNotFoundException) {
			return (
				produceMessageWindow(
					in,
					"Class Not Found",
					"Class Not Found : ",
					"A Required Class Was Not Found.",
					comp,
					src));
		}

		if (ex instanceof PrinterException) {
			if (ex instanceof PrinterAbortException) {
				return (
					produceMessageWindow(
						in,
						"Printing Aborted",
						"Printing Aborted : ",
						ex.getMessage(),
						comp,
						src));
			}

			if (ex instanceof PrinterIOException) {
				return (
					produceMessageWindow(
						in,
						"Printer I/O Problem",
						"Printer I/O Problem : ",
						ex.getMessage(),
						comp,
						src));
			}

			return (
				produceMessageWindow(
					in,
					"Printer Problem",
					"Printer Problem : ",
					ex.getMessage(),
					comp,
					src));
		}

		return (
			produceMessageWindow(
				in,
				"Unknown Problem",
				"Unknown Problem : ",
				"An unknown problem appeared.",
				comp,
				src));
	}

	/**
	 * Produces a message window for a particular error or exception.
	 * @param in The error or exception to be displayed in the window.
	 * @param title The window/frame title for the property editor.
	 * @param label The brief label text to be displayed along with the message.
	 * @param message The message explaining the error or exception.
	 * @param comp The component generating the error or exception.
	 * @param src The data model of the component generating the error or exception.
	 * @return The generated property editor.
	 */
	public static VerdantiumPropertiesEditor produceMessageWindow(
		Throwable in,
		String title,
		String label,
		String message,
		VerdantiumComponent comp,
		PropertyChangeSource src) {
		JComponent jcomp = null;
		if (comp != null)
			jcomp = comp.getGUI();
		MessageEditor edit = new ErrorMessageEditor(src, label, message, in);
		ProgramDirector.showPropertyEditor(edit, jcomp, title);
		return (edit);
	}

	/**
	 * Produces a debug-level window for a particular error or exception.
	 * @param in The error or exception to be displayed in the window.
	 * @param comp The component generating the error or exception.
	 * @param src The data model of the component generating the error or exception.
	 * @return The generated property editor.
	 */
	public static VerdantiumPropertiesEditor produceDebugWindow(
		Throwable in,
		VerdantiumComponent comp,
		PropertyChangeSource src) {
		JComponent jcomp = null;
		if (comp != null)
			jcomp = comp.getGUI();
		MessageEditor edit =
			new MessageEditor(src, "Exception Debug Info : ", in);
		ProgramDirector.showPropertyEditor(edit, jcomp, "Exception Debug Info");
		return (edit);
	}

	/**
	 * Returns a clone of an RGB color.
	 * @param in The input RGB color.
	 * @return The created clone of the color.
	 */
	public static Color cloneColorRGB(Color in) {
		Color col = new Color(in.getRed(), in.getGreen(), in.getBlue());
		return (col);
	}

	/**
	 * Gets the undo manager for a particular object.
	 * @param target The object for which to get the undo manager.
	 * @return The retrieved undo manager, or null if no manager found.
	 * @throws Throwable
	 */
	protected static UndoManager getUndoManager(Object target)
		throws Throwable {
		UndoManager ret = null;
		EtherEventHandler tarf = (EtherEventHandler) target;
		UndoEtherEvent evt =
			new UndoEtherEvent(
				"Program Director",
				UndoEtherEvent.getUndoManager,
				null,
				target);
		ret = (UndoManager) (tarf.processObjEtherEvent(evt, null));
		return (ret);
	}

	
}

