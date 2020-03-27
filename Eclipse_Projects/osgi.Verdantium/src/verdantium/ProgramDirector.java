package verdantium;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.ExceptionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.NotSerializableException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import meta.DataFormatException;
import meta.ExFac;
import meta.Meta;
import meta.VersionBuffer;
import meta.WrapRuntimeException;
import verdantium.clmgr.ComponentManager;
import verdantium.core.ContainerAppDesktopPane;
import verdantium.help.ProgramDirectorHelp;
import verdantium.undo.UndoManager;
import verdantium.utils.ComponentNotFoundException;
import verdantium.utils.ResourceNotFoundException;
import verdantium.utils.VTextProperties;
import jundo.util.pdx_HashMapSh_pdx_ObjectRef;
import jundo.runtime.ExtMilieuRef;

//$$strtCprt
/*
     Verdantium compound-document framework by Thorn Green
	Copyright (C) 2007 Thorn Green

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
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Debugging other systems.                                             | Added some debug statements, and then removed them.  Should be essentially no functionality changes.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/10/2001            | Thorn Green (viridian_1138@yahoo.com)           | Second-Cut at Error Handling.                                        | Second-Cut at Error Handling.
*    | 09/29/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for multiple PLAFs.                                          | Made code changes to support multiple PLAFs.
*    | 10/05/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for command-line arguments.                                  | Added support for command-line arguments.
*    | 10/22/2001            | Thorn Green (viridian_1138@yahoo.com)           | Display help from a command line arg. (like other systems).          | Added a standard "-help" option.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
*    | 05/10/2002            | Thorn Green (viridian_1138@yahoo.com)           | Redundant information in persistent storage.                         | Made numerous persistence and packaging changes.
*    | 10/05/2002            | Thorn Green (viridian_1138@yahoo.com)           | Run GeoCard from web.                                                | Debugged and made changes to run GeoCard from web.
*    | 10/12/2002            | Thorn Green (viridian_1138@yahoo.com)           | Support for Discovery.                                               | Added support for Discovery.
*    | 03/09/2003            | Thorn Green (viridian_1138@yahoo.com)           | Persistence bug in discovered components.                            | Changed input streaming to fix bug.
*    | 03/16/2003            | Thorn Green (viridian_1138@yahoo.com)           | ObjectPC did not use defaultClassLoader.                             | Moved defaultClassLoader into Meta to fix this.
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
* ProgramDirector is the central class for executing Verdantium components.
* It is also a component that provides an interface for executing other
* components.  It also provides an API in the form of a set of public
* static methods for component creation, display, and Ether Event handling.
* 
* @author Thorn Green
*/
public class ProgramDirector
	extends Object
	implements VerdantiumComponent, EtherEventPropertySource {
	
	/**
	* PropertyChangeEvent name that components can use to indicate to their property
	* editors that they are being hidden.
	*/
	public static final String propertyHide = "propertyHide";

	/**
	* PropertyChangeEvent name that components can use to indicate to their property
	* editors that they are being destroyed.
	*/
	public static final String propertyDestruction = "propertyDestruction";

	/**
	 * The button that is the GUI for the component.
	 */
	private JButton runButton = new JButton("Run...");
	
	/**
	* Gets the GUI for the component.
	* @return The GUI for the component.
	*/
	public JComponent getGUI() {
		return (runButton);
	}

	/**
	* Property name indicating that an Ether Event is about to be sent to its target.
	*/
	public static final String EtherEventStart = "EtherEventStart";
	
	/**
	* Property name indicating that an Ether Event has finished normally.
	*/
	public static final String EtherEventEnd = "EtherEventEnd";
	
	/**
	* Property name indicating that an Ether Event has finished abnormally with an error.
	*/
	public static final String EtherEventErr = "EtherEventErr";

	/**
	 * Utilities used to acquire resource files.
	 */
	private static XKit myTarget = new XKit();
	
	/**
	 * The property change support at the class-level for ProgramDirector.
	 */
	private static PropertyChangeSupport propSL =
		new PropertyChangeSupport("Program Director");
	
	/**
	 * The properties for the Pluggable Look And Feel (PLAF).
	 */
	private static VTextProperties plafProperties = null;

	/**
	* Loads MIME assignments from a configuration file.
	* @param filename The name of the configuration file.
	* @return The properties loaded from the file.
	*/
	public static VTextProperties loadConfigFile(String filename) {
		VTextProperties myProp = new VTextProperties();

		try {

			InputStream myStream =
				myTarget.getBaseStream(
					"temp/" + filename,
					ProgramDirector.class);
			myProp.load(myStream);
			myStream.close();

		} catch (Exception e) {
			throw (new WrapRuntimeException("Bad Config Load", e));
		}

		return (myProp);
	}

	/**
	* Gets the ProgramDirector's utilities used to acquire resource files.
	* @return Utilities used to acquire resource files.
	*/
	public static XKit getXKit() {
		return (myTarget);
	}

	/**
	 * Handles Ether Events to alter the properties of the component.
	 * @param in The event to handle.
	 * @param refcon A reference to context data for the event.
	 * @return The result of handling the event, or null if there is no result.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon)
		throws Throwable {
		if (in instanceof StandardEtherEvent) {
			if (in
				.getEtherID()
				.equals(StandardEtherEvent.makePropertiesEditor))
				return (makePropertiesEditor());

			if (in
				.getEtherID()
				.equals(StandardEtherEvent.showPropertiesEditor))
				showPropertiesEditor();

			if (in.getEtherID().equals(StandardEtherEvent.objUndoableClose)) {
				propL.firePropertyChange(
					ProgramDirector.propertyHide,
					null,
					null);
			}
		}

		if (in instanceof ProgramDirectorEvent) {
			if (in
				.getEtherID()
				.equals(ProgramDirectorEvent.isProgramDirectorEventSupported)) {
				return (new Boolean(true));
			} else {
				return (handleProgramDirectorEvent((ProgramDirectorEvent) in));
			}

		}

		return (null);
	}

	/**
	* Handles an EtherEvent on the ProgramDirector class.
	* @param in The event to handle.
	* @param refcon A reference to context data for the event.
	* @return The result of handling the event, or null if there is no result.
	*/
	public static Object processClassEtherEvent(EtherEvent in, Object refcon)
		throws Throwable {
		if (in instanceof StandardEtherEvent) {
			if (in
				.getEtherID()
				.equals(StandardEtherEvent.loadNewPersistentFile)) {
				Object[] param = (Object[]) (in.getParameter());
				URL MyURL = (URL) (param[0]);
				Container parent = (Container) (param[1]);
				VerdantiumComponent MyC = null;
				MyC = loadNewPersistentFile(MyURL, parent);
				return (MyC);
			}
		}

		return (null);
	}

	/**
	* Creates the property editor for ProgramDirector.
	* @return The property editor for ProgramDirector.
	*/
	public VerdantiumPropertiesEditor makePropertiesEditor() {
		ProgramDirectorPropertyEditor MyEdit =
			new ProgramDirectorPropertyEditor(this, null);
		return (MyEdit);
	}

	/**
	* Shows the property editor for ProgramDirector.
	*/
	public void showPropertiesEditor() {
		VerdantiumPropertiesEditor MyEdit = makePropertiesEditor();
		ProgramDirector.showPropertyEditor(
			MyEdit,
			getGUI(),
			"Program Director Property Editor");
	}

	/**
	* Handles the destruction of the component.
	*/
	public void handleDestroy() {
		propL.firePropertyChange(ProgramDirector.propertyHide, null, null);
		propL.firePropertyChange(
			ProgramDirector.propertyDestruction,
			null,
			null);
	}

	/**
	 * Constructor.
	 */
	public ProgramDirector() {
		propL = new PropertyChangeSupport(this);
		ActionListener MyL =
			Adapters.createGActionListener(this, "handleRunButton");
		runButton.addActionListener(MyL);
		runButton.setToolTipText("Click here to Run");
	}

	/**
	* Adds listeners to inform a component when any of its enclosing frames closes.
	* @param in The component to be informed.
	* @param parent The AWT component that is the parent of the VerdantiumComponent.
	*/
	public static void addDestructionListeners(
		VerdantiumComponent in,
		Component parent) {
		Component tmp = parent;
		boolean done = false;

		while (!done) {
			if ((tmp instanceof Window)
				|| (chkInternalFrame(tmp))
				|| (tmp == null)) {
				done = (tmp instanceof Window) || (tmp == null);

				if (tmp instanceof Window) {
					((Window) tmp).addWindowListener(new MgrWindowAdapter(in));
				}

				if (tmp instanceof JInternalFrame) {
					((JInternalFrame) tmp).addInternalFrameListener(
						new MgrInternalFrameAdapter(in));
				}

				if (!done)
					tmp = tmp.getParent();
			} else
				tmp = tmp.getParent();
		}
	}

	/**
	* A variant on showComponent intended to support command-lines from the main() method,
	* with command line args. for optionally loading components.  The args. are passed in
	* cmdln.  The supportsEmbed boolean indicates whether the component can support embedding.
	* If so, the "-l" command option can load an arbitrary file as an embedded file.
	* @param in The component to be shown.
	* @param title The window/frame title for the component.
	* @param cmdIn The command arguments to the main() method.
	* @param supportsEmbed Whether the component can support embedding.
	*/
	public static void showComponent(
		VerdantiumComponent in,
		String title,
		String[] cmdln,
		boolean supportsEmbed) {
		showComponent(in, null, title, cmdln, supportsEmbed);
	}

	/**
	* A variant on showComponent intended to support command-lines from the main() method,
	* with command line args. for optionally loading components.  The args. are passed in
	* cmdln.  The supportsEmbed boolean indicates whether the component can support embedding.
	* If so, the "-l" command option can load an arbitrary file as an embedded file.
	* @param in The component to be shown.
	* @param bounds The bounds in which to show the component.
	* @param title The window/frame title for the component.
	* @param cmdIn The command arguments to the main() method.
	* @param supportsEmbed Whether the component can support embedding.
	*/
	public static void showComponent(
		final VerdantiumComponent in,
		final Rectangle bounds,
		final String title,
		final String[] cmdln,
		final boolean supportsEmbed) {
		if (SwingUtilities.isEventDispatchThread()) {
			try {
				int len = cmdln.length;
				showComponent(in, bounds, null, title);
				if (len > 0) {
					if (len == 1) {
						if ((cmdln[0]).equals("-help")) {
							ProgramDirector.displayHelpOnComponent(
								title,
								in,
								null);
							return;
						}

						URL url = handleParseGenericName(cmdln[0]);
						EtherEvent send =
							new StandardEtherEvent(
								"Program Director",
								StandardEtherEvent.objOpenEvent,
								url,
								in);
						ProgramDirector.fireEtherEvent(send, null);
					}

					if (len == 2) {
						if (supportsEmbed && (cmdln[0]).equals("-l")) {
							EtherEvent send =
								new ProgramDirectorEvent(
									"Program Director",
									ProgramDirectorEvent.loadURL,
									null,
									in);
							Object[] param =
								{ null, new Point(20, 20), new Boolean(false)};
							param[0] = handleParseGenericName(cmdln[1]);
							send.setParameter(param);
							ProgramDirector.fireEtherEvent(send, null);
						}
					}

				}
			} catch (Throwable ex) {
				VerdantiumUtils.handleThrow(ex, null, null);
			}
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				/**
				 * Shows the component.
				 */
				public void run() {
					showComponent(in, bounds, title, cmdln, supportsEmbed);

				}
			});
		}
	}

	/**
	* Embeds a component inside <code>parent</code> with frame title <code>title</code>.
	* If <code>parent</code> is null, the component is embedded in a top-level frame.
	* @param in The component to be informed.
	* @param parent The AWT component that is the parent of the VerdantiumComponent.
	* @param title The window/frame title for the component.
	*/
	public static void showComponent(
		VerdantiumComponent in,
		Container parent,
		String title) {
		showComponent(in, null, parent, title);
	}

	/**
	* Embeds a component inside <code>parent</code> with frame title <code>title</code> and
	* with bounds <code>bounds</code>.
	* If <code>parent</code> is null, the component is embedded in a top-level frame.
	* @param in The component to be informed.
	* @param bounds The bounds in which to show the component.
	* @param parent The AWT component that is the parent of the VerdantiumComponent.
	* @param title The window/frame title for the component.
	*/
	public static void showComponent(
		VerdantiumComponent in,
		Rectangle bounds,
		Container parent,
		String title ) {
		JComponent myComp = in.getGUI();

		if (myComp != null) {
			if (parent == null) {
				VerdantiumFrame myFr = new VerdantiumFrame(in);
				if (title != null)
					myFr.setTitle(title);
				myFr.addCloseButtonEvent();
				myFr.getContentPane().setLayout(new BorderLayout(0, 0));
				myFr.getContentPane().add("Center", myComp);
				myFr.addWindowListener(new MgrWindowAdapter(in));
				if (in instanceof GuiShowNotify)
					 ((GuiShowNotify) in).guiShowNotify();
				if (bounds == null) {
					myFr.packToPreferred();
				} else
					myFr.setBounds(bounds);
				myFr.show();
				if (!(in instanceof VerdantiumPropertiesEditor))
					incrementComponentFrameCount();
				if (in instanceof ProgramDirectorSaveEditor)
					incrementSaveEditorCount();
			} else {
				addDestructionListeners(in, parent);

                                if( parent instanceof VerdantiumDesktopPane )
                                {
                                        UndoManager undoMgr = ( (VerdantiumDesktopPane) parent ).getUndoMgr();
                                        pdx_HashMapSh_pdx_ObjectRef map = ( (VerdantiumDesktopPane) parent ).getMap();
                                        VerdantiumUndoableInternalFrame myFr =
                                            new VerdantiumUndoableInternalFrame(undoMgr, map);
					myFr.addCloseButtonEvent();
					myFr.getContentPane().setLayout(new BorderLayout(0, 0));
					myFr.getContentPane().add("Center", myComp);
					parent.add(myFr, JLayeredPane.PALETTE_LAYER);
					myFr.addInternalFrameListener(
						new MgrInternalFrameAdapter(in));
					if (in instanceof GuiShowNotify)
						 ((GuiShowNotify) in).guiShowNotify();
					if (bounds == null) {
						myFr.packToPreferred();
					} else
						myFr.setBounds(bounds);
                                        ExtMilieuRef mil = undoMgr.getCurrentMil();
                                        undoMgr.handleCommitTempChange(mil);
					myFr.show();
					parent.repaint();
                                }
                                else
                                {
				if (parent instanceof JDesktopPane) {
					VerdantiumInternalFrame myFr =
						new VerdantiumInternalFrame(title, true, true, in);
					myFr.addCloseButtonEvent();
					myFr.getContentPane().setLayout(new BorderLayout(0, 0));
					myFr.getContentPane().add("Center", myComp);
					parent.add(myFr, JLayeredPane.PALETTE_LAYER);
					myFr.addInternalFrameListener(
						new MgrInternalFrameAdapter(in));
					if (in instanceof GuiShowNotify)
						 ((GuiShowNotify) in).guiShowNotify();
					if (bounds == null) {
						myFr.packToPreferred();
					} else
						myFr.setBounds(bounds);
					myFr.show();
					parent.repaint();
				} else {
					if (parent instanceof JApplet) {
						JApplet par = (JApplet) parent;

						par.getContentPane().setLayout(new BorderLayout(0, 0));
						par.getContentPane().add("Center", myComp);
						if (in instanceof GuiShowNotify)
							 ((GuiShowNotify) in).guiShowNotify();
						if (parent instanceof JComponent) {
							myComp.invalidate();
							parent.invalidate();
							par.invalidate();
							((JComponent) parent).revalidate();
						}

						parent.repaint();
					} else {
						parent.setLayout(new BorderLayout(0, 0));
						parent.add("Center", myComp);
						if (in instanceof GuiShowNotify)
							 ((GuiShowNotify) in).guiShowNotify();
						if (parent instanceof JComponent) {
							myComp.invalidate();
							parent.invalidate();
							((JComponent) parent).revalidate();
						}

						parent.repaint();
					}
				}
			}
		} }
	}

	/**
	* Embeds a component inside <code>parent</code> by handling a ProgramDirectorEvent.
	* If <code>parent</code> is null, the component is embedded in a top-level frame.
	* @param e The event to show the component.
	* @param parent The container in which to embed the component, or a top-level frame if null.
	*/
	public static VerdantiumComponent showComponent(
		ProgramDirectorEvent e,
		Container parent)
		throws
			IOException,
			ClassNotFoundException,
			ResourceNotFoundException,
			ComponentNotFoundException {
		VerdantiumComponent myC = null;

		try {

			if (e.getEtherID().equals(ProgramDirectorEvent.createApp)) {
				Object[] param = (Object[]) (e.getParameter());
				String programName = (String) (param[0]);
				EtherEvent send =
					new StandardEtherEvent(
						"Program Director",
						StandardEtherEvent.classNewEvent,
						parent,
						programName);
				Object ob = ProgramDirector.fireEtherEvent(send, null);
				myC = (VerdantiumComponent) (ob);
			}

			if (e.getEtherID().equals(ProgramDirectorEvent.loadURL)) {
				if (e.getUseFileWatcher()) {
					EtherEvent send =
						new StandardEtherEvent(
							"Program Director",
							StandardEtherEvent.classNewEvent,
							parent,
							"File Watcher");
					Object ob = ProgramDirector.fireEtherEvent(send, null);
					myC = (VerdantiumComponent) (ob);
					Object[] pi = (Object[]) (e.getParameter());
					Object[] param = { pi[0], pi[1], new Boolean(false)};
					send =
						new ProgramDirectorEvent(
							"Program Director",
							ProgramDirectorEvent.loadURL,
							param,
							myC);
					ProgramDirector.fireEtherEvent(send, null);
				} else {
					Object[] param = { e.getProgramURL(), parent };
					EtherEvent send =
						new StandardEtherEvent(
							"Program Director",
							StandardEtherEvent.loadNewPersistentFile,
							param,
							"Program Director");
					Object ob = ProgramDirector.fireEtherEvent(send, null);
					myC = (VerdantiumComponent) (ob);
				}
			}
		} catch (Throwable ex) {
			classifyThrowable(ex);
		}

		return (myC);
	}

	/**
	 * Strips off an an invocation target wrapper, and determines the general category of the errors/exception.
	 * Exceptions falling within particular categories are directly thrown.  Everything else
	 * is put within a WrapRuntimeException.
	 * @param in The error or exception to be handled.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws ResourceNotFoundException
	 * @throws ComponentNotFoundException
	 */
	protected static void classifyThrowable(Throwable in)
		throws
			IOException,
			ClassNotFoundException,
			ResourceNotFoundException,
			ComponentNotFoundException {
		Throwable ex = in;

		if (ex instanceof InvocationTargetException) {
			ex = ((InvocationTargetException) ex).getTargetException();
		}

		if (ex instanceof ClassNotFoundException) {
			throw ((ClassNotFoundException) ex);
		}

		if (ex instanceof ComponentNotFoundException) {
			throw ((ComponentNotFoundException) ex);
		}

		if (ex instanceof IOException) {
			throw ((IOException) ex);
		}

		if (ex instanceof ResourceNotFoundException) {
			throw ((ResourceNotFoundException) ex);
		}

		throw (new WrapRuntimeException("showComponent failed", in));
	}
        
   /**
	* Embeds a component of a given class inside <code>parent</code> with frame title <code>title</code>.
	* If <code>parent</code> is null, the component is embedded in a top-level frame.
	* @param in The class of the component to be shown.
	* @param parent The container in which to embed the component, or a top-level frame if null.
	* @param title The window/frame title for the component.
	*/
	public static VerdantiumComponent showComponent(
		Class<? extends VerdantiumComponent> in,
		Container parent,
		String title) {
		return (showComponent(in, null, parent, title));
	}

	/**
	* Embeds a component of a given class inside <code>parent</code> with frame title <code>title</code> and
	* with bounds <code>bounds</code>.
	* If <code>parent</code> is null, the component is embedded in a top-level frame.
	* @param in The class of the component to be shown.
	* @param bounds The bounds in which to show the component.
	* @param parent The container in which to embed the component, or a top-level frame if null.
	* @param title The window/frame title for the component.
	*/
	public static VerdantiumComponent showComponent(
		Class<? extends VerdantiumComponent> in,
		Rectangle bounds,
		Container parent,
		String title) {
		VerdantiumComponent myComp = null;

		try {
			myComp = in.newInstance();
			showComponent(myComp, bounds, parent, title);
		} catch (Exception e) {
			throw (new WrapRuntimeException("Show Component Failed", e));
		}

		return (myComp);
	}

	/**
	* Given a component embedded in a JInternalFrame, moves the location of the
	* JInternalFrame to <code>inPt</code>.
	* @param myC The component in the internal frame.
	* @param inPt The location to which to move the component.
	*/
	public static void setPointLocation(VerdantiumComponent myC, Point inPt) {
		if (inPt != null) {
			Component myP = myC.getGUI();

			while (!(myP instanceof JInternalFrame))
				myP = myP.getParent();

			((JInternalFrame) myP).setLocation(inPt);
		}
	}

	/**
	 * Returns whether the AWT component is a desktop pane that is non-ContainerApp.
	 * @param tmp The component to be checked.
	 * @return Whether the AWT component is a desktop pane that is non-ContainerApp.
	 */
	private static boolean chkDesktopPane(Component tmp) {
		return (
			(tmp instanceof JDesktopPane)
				&& !(tmp instanceof ContainerAppDesktopPane));
	}

	/**
	 * Returns whether the AWT component is an internal frame.
	 * @param tmp The component to be checked.
	 * @return Whether the AWT component is an internal frame.
	 */
	private static boolean chkInternalFrame(Component tmp) {
		return (tmp instanceof JInternalFrame);
	}

	/**
	* Returns the container enclosing a particular AWT component.
	* @param in The AWT component for which to find enclosure.
	* @return The container enclosing the AWT component.
	*/
	public static Component getEnclosingContainer(Component in) {
		Component tmp = in;
		boolean done = false;

		if (tmp != null)
			tmp = tmp.getParent();

		while (!done) {
			if ((tmp instanceof Window)
				|| (chkDesktopPane(tmp))
				|| (tmp == null)) {
				done = true;

				if (tmp instanceof Window)
					tmp = null;

			} else
				tmp = tmp.getParent();
		}

		return (tmp);
	}

	/**
	* Initializes the UI for the startup of a client component as an application.
	* This should only be called on the first line of a component's 
	* static main() method.
	*/
	public static void initUI() {
		if (SwingUtilities.isEventDispatchThread()) {
			String versionKey = "PlafConfig09/01A";

			try {
				UIManager.setLookAndFeel(
					getPlafProperties().getPropertyNonNull(
						versionKey + ".InitPlaf"));
			} catch (Exception e) {
				throw (new WrapRuntimeException("Init Plaf Failed.", e));
			}

			myTarget.initLoadFrameUI();
		} else {
			synchronized (myTarget) {
				SwingUtilities.invokeLater(new Runnable() {

					/**
					 * Initializes the UI.
					 */
					public void run() {
						try {
							initUI();
							synchronized (myTarget) {
								myTarget.notify();
							}
						} catch (Exception ex) {
							ex.printStackTrace(System.out);
							System.out.println(
								"PLAF Won't Initialize; Attempting Shutdown");
							System.exit(1);
						}

					}
				});
				try {
					myTarget.wait();
				} catch (InterruptedException ex) {
					throw (new WrapRuntimeException("Init Plaf. Failed", ex));
				}
			}
		}
	}

	/**
	* Handles an update to the UI.
	*/
	public static void handleUpdateUI() {
		myTarget.initLoadFrameUI();
	}

	/**
	* Gets the default Pluggable Look And Feel (PLAF) properties for Verdantium.
	* @return The default Pluggable Look And Feel (PLAF) properties for Verdantium.
	*/
	public static VTextProperties getPlafProperties() {
		if (plafProperties == null) {
			VTextProperties MyProp = loadConfigFile("plaf.cfg");
			plafProperties = MyProp;
		}

		return (plafProperties);
	}

	/**
	* Adds a property change listener to the class.
	* @param in The listener to be added.
	*/
	public static void addClassPropertyChangeListener(PropertyChangeListener in) {
		propSL.addPropertyChangeListener(in);
	}

	/**
	* Removes a property change listener from the class.
	* @param in The listener to be removed.
	*/
	public static void removeClassPropertyChangeListener(PropertyChangeListener in) {
		propSL.removePropertyChangeListener(in);
	}

	/**
	* Informs ProgramDirector that a save dialog has been created.
	*/
	public static void incrementSaveEditorCount() {
		saveEditorCount++;
	}

	/**
	* Informs ProgramDirector that a save dialog has been closed.
	*/
	public static void decrementSaveEditorCount() {
		saveEditorCount--;
		if (((componentFrameCount + saveEditorCount) <= 0)
			&& (ProgramDirector.getXKit().getApplet() == null))
			System.exit(0);
	}

	/**
	* Informs ProgramDirector that a non-property edit frame has been created.
	*/
	public static void incrementComponentFrameCount() {
		componentFrameCount++;
	}

	/**
	* Informs ProgramDirector that a non-property edit frame has been closed.
	*/
	public static void decrementComponentFrameCount() {
		componentFrameCount--;
		if (((componentFrameCount + saveEditorCount) <= 0)
			&& (ProgramDirector.getXKit().getApplet() == null))
			System.exit(0);
	}

	/**
	 * The number of save dialogs.
	 */
	protected static int saveEditorCount = 0;
	
	/**
	 * The number of non-property edit frames.
	 */
	protected static int componentFrameCount = 0;

	/**
	* Creates an XMLDecoder for loading components.  Uses URLs from the union
	* of all component class loaders utilized.
	* @param in The input stream from which to read.
	* @return The XML Decoder.
	*/
	public static XMLDecoder createComponentDecoder(InputStream in)
		throws IOException {
		// System.out.println("Loader " + Meta.getDefaultClassLoader() );
		XMLDecoder ret = ExFac.createXMLDecoder(in,Meta.getDefaultClassLoader());
		// System.out.println( "ld2 " + ret.getClass().getClassLoader() );
		return (ret);
	}

	/**
	 * Creates an XMLEncoder for saving components.  Uses URLs from the union
	 * of all component class loaders utilized.
	 * @param out The output stream to which to write.
	 * @return The XML Encoder.
	 */
	public static XMLEncoder createComponentEncoder(OutputStream out) {
		// System.out.println("Loader " + Meta.getDefaultClassLoader() );
		XMLEncoder ret = ExFac.createXMLEncoder(out,Meta.getDefaultClassLoader());
		// System.out.println( "ld2 " + ret.getClass().getClassLoader() );
		return (ret);
	}

	/**
	* Gets the file extension at the end of the URL.
	* @return The file extension of the URL.
	*/
	public static String getURLExtension(URLConnection u) {
		String fname = u.getURL().getFile();
		int len = fname.length();
		int idex = len - 1;
		while ((idex >= 0) && ((fname.charAt(idex)) != '.'))
			idex--;

		String ret = "";

		if (idex > 0) {
			int count;
			for (count = idex; count < len; count++)
				ret = ret + fname.charAt(count);
		}

		return (ret);
	}

	/**
	* Gets the data flavors for the data at a particular URL connection.
	* @param u The URL connection for which to get the flavors.
	* @return the data flavors for the data at the URL connection.
	*/
	public static DataFlavor[] getInputDataFlavors(URLConnection u) {
		DataFlavor[] myTypes = null;
		String contentType = u.getContentType();

		if (contentType != null) {
			if (contentType.equals("content/unknown"))
				contentType = null;
		}

		if (contentType != null) {
			if (contentType.equals("application/x-java-serialized-object"))
				contentType = "application/x-java-object-stream";

			String primaryType = "";
			String subType = "";

			int idex = 0;
			while ((contentType.charAt(idex) != '/')
				&& (idex < contentType.length())) {
				primaryType =
					primaryType
						+ (new Character(contentType.charAt(idex))).toString();
				idex++;
			}

			idex++;
			while (idex < contentType.length()) {
				subType =
					subType
						+ (new Character(contentType.charAt(idex))).toString();
				idex++;
			}

			DataFlavor myF = null;
			if (contentType.equals("application/x-java-object-stream")) {
				myF = VerdantiumFlavorMap.serialInputFlavor;
			} else {
				myF =
					ComponentManager.createInputStreamFlavor(
						primaryType,
						subType);
			}

			DataFlavor[] tmp = { myF };
			myTypes = tmp;
		} else {
			String ext = getURLExtension(u);
			myTypes = ComponentManager.getInputFlavorForNative(ext);
			if (myTypes == null) {
				DataFlavor myF =
					ComponentManager.createInputStreamFlavor(
						"content",
						"unknown");
				DataFlavor[] tmp = { myF };
				myTypes = tmp;
			}
		}

		if (myTypes == null)
			throw (
				new RuntimeException("Something Inconsistent In Flavor Grab"));
		return (myTypes);
	}

	/**
	* Reads the URL Connection into a Transferable object with a set of data flavors.
	* @param myCon The URL Connection to read.
	* @return The generated Transferable.
	*/
	public static Transferable getTransferable(URLConnection myCon)
		throws IOException, ClassNotFoundException, ResourceNotFoundException {
		Transferable trans = null;
		InputStream myStream = null;
		XMLDecoder ostream = null;
		BufferedInputStream bstream = null;
		
		ClassLoader threadCL = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(Meta.getDefaultClassLoader()); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

		try {
			DataFlavor[] myFlavors = getInputDataFlavors(myCon);
			myStream = myCon.getInputStream();
			System.out.println("Got Input Stream");
			System.out.println(myFlavors[0].getMimeType());

			if (myFlavors[0].equals(VerdantiumFlavorMap.serialInputFlavor)) {
				ostream = createComponentDecoder(myStream);
				Object myo = ostream.readObject();

				if (myo instanceof Transferable) {
					System.out.println("Entered Transferable");
					Transferable inTrans = (Transferable) (myo);
					Object myo2 = null;

					try {
						final Vector<Exception> except = new Vector<Exception>();
						ostream.setExceptionListener(new ExceptionListener() {
							public void exceptionThrown(Exception ex) {
								except.add(ex);
							}
						});
						myo2 = ostream.readObject();
						if (except.size() > 0) {
							myo2 = null;
						}
					} catch (Exception e) {
						myo2 = null;
					}

					if (myo2 == null) {
						System.out.println("Entered myo == null");
						if (inTrans instanceof UrlHolder)
							 ((UrlHolder) inTrans).setUrl(myCon.getURL());
						myFlavors = inTrans.getTransferDataFlavors();
						trans = inTrans;
					} else {
						System.out.println("Entered myo not null");
						ostream.close();
						ostream = createComponentDecoder(myStream);
						trans =
							new GenericOStreamInputTrans(
								myFlavors,
								ostream,
								myCon.getURL());
					}
				} else {
					ostream.close();
					ostream = createComponentDecoder(myStream);
					trans =
						new GenericOStreamInputTrans(
							myFlavors,
							ostream,
							myCon.getURL());
				}
			} else {
				bstream = new BufferedInputStream(myStream);
				trans =
					new GenericStreamInputTrans(
						myFlavors,
						bstream,
						myCon.getURL());
			}
		} catch (IOException e) {
			if (ostream != null) {
				try {
					ostream.close();
				} catch (Exception e2) { /* No Handle */
				}
			}

			if (bstream != null) {
				try {
					bstream.close();
				} catch (Exception e2) { /* No Handle */
				}
			}

			if (myStream != null) {
				try {
					myStream.close();
				} catch (Exception e2) { /* No Handle */
				}
			}

			throw (e);
		} /* catch (ClassNotFoundException e) {
													if (ostream != null) {
														try {
															ostream.close();
														} catch (Exception e2) { /* No Handle */
		//			}
		//		}
		//
		//			if (bstream != null) {
		//				try {
		//					bstream.close();
		//				} catch (Exception e2) { /* No Handle */
		//				}
		//			}
		//
		//			if (MyStream != null) {
		//				try {
		//					MyStream.close();
		//				} catch (Exception e2) { /* No Handle */
		//				}
		//			}
		//
		//			throw (e);
		//		} 
		//
		
		Thread.currentThread().setContextClassLoader(threadCL); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		return (trans);
	}

	
	/**
	* Associates a URL with a component, creates the component and embeds it in <code>parent</code>, 
	* and loads the data into the component.
	* If <code>parent</code> is null, the component is embedded in a top-level frame.
	* @param u The URL from which to load the file.
	* @param parent The container in which to embed the component, or a top-level frame if null.
	*/
	public static VerdantiumComponent loadNewPersistentFile(
		URL u,
		Container parent)
		throws
			IOException,
			ClassNotFoundException,
			ResourceNotFoundException,
			ComponentNotFoundException {
		System.out.println("Entered Load Routine");
		VerdantiumComponent myPart = null;
		InputStream myStream = null;
		Rectangle bounds = null;

		try {
			URLConnection myCon = u.openConnection();
			myStream = myCon.getInputStream();
			Transferable trans = getTransferable(myCon);
			DataFlavor[] myFlavors = trans.getTransferDataFlavors();

			if (trans instanceof TransVersionBuffer) {
				TransVersionBuffer buf = (TransVersionBuffer) trans;
				if (((parent == null) || (parent instanceof JDesktopPane))
					&& !(parent instanceof ContainerAppDesktopPane)) {
					bounds =
						(Rectangle) (buf
							.getProperty("VerdantiumEncloserBounds"));
				}
			}

			DataFlavor[] outFlavors = { null };
			System.out.println("Looking for Component Class");
			System.out.println(myFlavors[0]);
			Class<? extends VerdantiumComponent> myClass = getComponentClass(myFlavors, outFlavors);
			System.out.println("Got Component Class");

			myPart = showComponent(myClass, bounds, parent, foundString);

			myPart.loadPersistentData(outFlavors[0], trans);
			myPart.getGUI().revalidate();
			myPart.getGUI().repaint();
		} catch (IOException e) {
			if ((myPart != null)
				&& ((parent == null) || (parent instanceof JDesktopPane)))
				try {
					VerdantiumUtils.disposeContainer(myPart);
				} catch (Exception e2) { /* No Handle */
				}

			if (myStream != null)
				try {
					myStream.close();
				} catch (Exception e2) { /* No Handle */
				}

			throw (e);
		} catch (ClassNotFoundException e) {
			if ((myPart != null)
				&& ((parent == null) || (parent instanceof JDesktopPane)))
				try {
					VerdantiumUtils.disposeContainer(myPart);
				} catch (Exception e2) { /* No Handle */
				}

			if (myStream != null)
				try {
					myStream.close();
				} catch (Exception e2) { /* No Handle */
				}

			throw (e);
		} catch (ResourceNotFoundException e) {
			if ((myPart != null)
				&& ((parent == null) || (parent instanceof JDesktopPane)))
				try {
					VerdantiumUtils.disposeContainer(myPart);
				} catch (Exception e2) { /* No Handle */
				}

			if (myStream != null)
				try {
					myStream.close();
				} catch (Exception e2) { /* No Handle */
				}

			throw (e);
		} catch (ComponentNotFoundException e) {
			if ((myPart != null)
				&& ((parent == null) || (parent instanceof JDesktopPane)))
				try {
					VerdantiumUtils.disposeContainer(myPart);
				} catch (Exception e2) { /* No Handle */
				}

			if (myStream != null)
				try {
					myStream.close();
				} catch (Exception e2) { /* No Handle */
				}

			throw (e);
		}

		System.out.println("Left Load Routine");
		return (myPart);
	}

	/**
	* Stores the size of the enclosing frame in the VersionBuffer for a
	* component.
	* @param in The component from which to get the frame size.
	* @param buf The buffer in which to save the size.
	*/
	private static void saveContainerSizeInfo(
		VerdantiumComponent in,
		TransVersionBuffer buf) {
		Component myP = in.getGUI();

		while (!(myP instanceof JInternalFrame) && !(myP instanceof Window))
			myP = myP.getParent();

		buf.setProperty("VerdantiumEncloserBounds", myP.getBounds());
	}

	/**
	* Saves the persistent state of a component to a URL using the data format specified by
	* <code>flavor</code>.  It is a precondition that the flavor specified must be in the list of
	* flavors that the component returns in its getPersistentOutputFlavors() method.
	* @param u The URL to which to save the component.
	* @param in The component to be saved.
	* @param flavor The data flavor in which to perform the save.
	*/
	public static void saveNewPersistentFile(
		URL u,
		VerdantiumComponent in,
		DataFlavor flavor)
		throws IOException {
		OutputStream myStream = null;
		XMLEncoder ostream = null;
		BufferedOutputStream bstream = null;

		try {
			String fileName = u.getFile();
			System.out.println(fileName);

			if (!(u.getProtocol().equals("file")))
				return;

			myStream = new FileOutputStream(fileName);

			/* System.out.println( u );
			System.out.println( u.getProtocol() );
			URLConnection MyCon = u.openConnection();
			System.out.println( MyCon );
			MyCon.setDoOutput( true );
			System.out.println( MyCon.getPermission() );
			MyStream = MyCon.getOutputStream(); */

			Transferable outTrans = in.savePersistentData(flavor);

			if (outTrans instanceof TransVersionBuffer)
				saveContainerSizeInfo(in, (TransVersionBuffer) outTrans);

			if ((outTrans instanceof Serializable)
				|| (outTrans instanceof Externalizable)) {
				final Vector<Exception> except = new Vector<Exception>();
				
				ClassLoader threadCL = Thread.currentThread().getContextClassLoader();
				Thread.currentThread().setContextClassLoader(Meta.getDefaultClassLoader()); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

				ostream = createComponentEncoder(myStream);

				ostream.setExceptionListener(new ExceptionListener() {
					public void exceptionThrown(Exception exception) {
						except.add(exception);
					}
				});

				ostream.writeObject(outTrans);
				ostream.flush();
				ostream.close();
				
				Thread.currentThread().setContextClassLoader(threadCL); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

				if (except.size() > 0) {
					Exception ex = (Exception) (except.elementAt(0));
					if (ex instanceof IOException) {
						throw ((IOException) ex);
					} else {
						throw (new DataFormatException(ex));
					}
				}
			} else {
				bstream = new BufferedOutputStream(myStream);

				ByteArrayOutputStream baos = null;

				try {
					baos =
						(ByteArrayOutputStream) (outTrans
							.getTransferData(flavor));
				} catch (UnsupportedFlavorException ex) {
					throw (
						new WrapRuntimeException(
							"Something is inconsistent in flavor handling.",
							ex));
				}

				bstream.write(baos.toByteArray(), 0, baos.toByteArray().length);
				bstream.flush();
				bstream.close();
			}

		} catch (IOException e) {
			if (ostream != null) {
				try {
					ostream.close();
				} catch (Exception e2) { /* No Handle */
				}
			}

			if (bstream != null) {
				try {
					bstream.close();
				} catch (Exception e2) { /* No Handle */
				}
			}

			if (myStream != null) {
				try {
					myStream.close();
				} catch (Exception e2) { /* No Handle */
				}
			}

			throw (e);
		}

		System.out.println("Save Done");
	}

	/**
	* Attempts to get a serializable Transferable from the input component.
	* @param in The input component.
	* @return The generated serializable Transferable.
	*/
	public static Transferable getSerializableState(VerdantiumComponent in)
		throws IOException, NotSerializableException {
		Transferable outTrans = null;

		try {
			int count = 0;
			DataFlavor flavor = null;
			DataFlavor[] flavors = in.getPersistentOutputDataFlavorsSupported();
			while ((count < flavors.length) && (flavor == null))
				if (flavors[count] instanceof TransVersionBufferFlavor)
					flavor = flavors[count];

			if (flavor == null)
				flavor = flavors[0];

			Transferable inTrans = in.savePersistentData(flavor);

			if ((inTrans instanceof Serializable)
				|| (inTrans instanceof Externalizable)) {
				outTrans = inTrans;
			} else {
				throw (
					new NotSerializableException(inTrans.getClass().getName()));
			}
		} catch (IOException e) {
			throw (e);
		}

		return (outTrans);
	}

	/**
	* Adds a property change listener.
	* @param e The listener to add.
	*/
	public void addPropertyChangeListener(PropertyChangeListener e) {
		propL.addPropertyChangeListener(e);
	}

	/**
	* Removes a property change listener.
	* @param e The listener to remove.
	*/
	public void removePropertyChangeListener(PropertyChangeListener e) {
		propL.removePropertyChangeListener(e);
	}

	/**
	* Embeds a property editor in the first pane outside <code>parent</code> 
	* with frame title <code>title</code>.
	* If <code>parent</code> is null, the component is embedded in a top-level frame.
	* @param in The component to be shown.
	* @param parent The container in which to embed the component, or a top-level frame if null.
	* @param title The window/frame title for the component.
	*/
	public static void showPropertyEditor(
		VerdantiumComponent in,
		Component parent,
		String title) {
		showPropertyEditor(in, null, parent, title);
	}

	/**
	* Embeds a property editor in the first pane outside <code>parent</code> 
	* with frame title <code>title</code> and
	* with bounds <code>bounds</code>.
	* If <code>parent</code> is null, the component is embedded in a top-level frame.
	* @param in The property editor to be shown.
	* @param bounds The bounds in which to show the component.
	* @param parent The container in which to embed the component, or a top-level frame if null.
	* @param title The window/frame title for the component.
	*/
	public static void showPropertyEditor(
		VerdantiumComponent in,
		Rectangle bounds,
		Component parent,
		String title) {
		Component tmp = getEnclosingContainer(parent);
		showComponent(in, bounds, (Container) tmp, title);
	}

	/**
	* Creates a component in response to a ProgramDirectorEvent.
	* @param e The input event.
	*/
	protected Object handleProgramDirectorEvent(ProgramDirectorEvent e)
		throws
			IOException,
			ClassNotFoundException,
			ResourceNotFoundException,
			ComponentNotFoundException {
		try {
			return (
				ProgramDirector.showComponent(
					e,
					(Container) (getEnclosingContainer(getGUI()))));
		} catch (ClassNotFoundException ex) {
			throw (ex);
		} catch (IOException ex) {
			throw (ex);
		} catch (ResourceNotFoundException ex) {
			throw (ex);
		} catch (ComponentNotFoundException ex) {
			throw (ex);
		}
	}

	/**
	* Handles the pressing of the Run button.
	* @param in The input event.
	*/
	public void handleRunButton(ActionEvent e) {
		try {
			EtherEvent send =
				new StandardEtherEvent(
					this,
					StandardEtherEvent.showPropertiesEditor,
					null,
					this);
			ProgramDirector.fireEtherEvent(send, null);
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Gets the component class associated with a Transferable, and places a data flavor
	* compatible with both the the component class and the Transferable in index zero
	* of <code>OutFlavor</code>.
	* @param in The Transferable for which to get the component class.
	* @param outFlavor The array to receive the associated data flavor.
	* @return The determined component class.
	*/
	public static Class<? extends VerdantiumComponent> getComponentClass(
		Transferable in,
		DataFlavor[] outFlavor)
		throws ComponentNotFoundException {
		try {
			DataFlavor[] inFlavor = in.getTransferDataFlavors();
			return (getComponentClass(inFlavor, outFlavor));
		} catch (ComponentNotFoundException ex) {
			throw (ex);
		}
	}

	/**
	* Returns the component class associated with a human-readable component name.
	* @param componentName The human-readable component name.
	* @return The associated component class.
	*/
	public static Class<? extends VerdantiumComponent> getComponentClass(String componentName)
		throws ComponentNotFoundException {
		Class<? extends VerdantiumComponent> ret = ComponentManager.getMap().get( componentName );

		if (ret == null) {
			throw (new ComponentNotFoundException());
		}

		return (ret);
	}

	/**
	* Returns the component name of the class returned by the last call to 
	* {@link #getComponentClass( DataFlavor[] InFlavor , DataFlavor[] OutFlavor )}.
	* @return The component name of the class.
	*/
	public static String getComponentName() {
		return (foundString);
	}

	/**
	 * The component name of the class returned by the last call to 
	 * {@link #getComponentClass( DataFlavor[] InFlavor , DataFlavor[] OutFlavor )}.
	 */
	private static String foundString = null;

	/**
	* Finds a component class that matches a data flavor in <code>inFlavor</code>.  Returns the class,
	* and puts the matching flavor in index zero of <code>outFlavor</code>.
	* @param inFlavor The flavors for which to find the match.
	* @param outFlavor The array to receive the matching flavor.
	* @return The associated component class.
	*/
	public static Class<? extends VerdantiumComponent> getComponentClass(
		DataFlavor[] inFlavor,
		DataFlavor[] outFlavor)
		throws ComponentNotFoundException {
		Class<? extends VerdantiumComponent> foundClass = null;
		foundString = null;

		if ((inFlavor[0]) instanceof TransVersionBufferFlavor) {
			String name = null;
			String componentName =
				((TransVersionBufferFlavor) (inFlavor[0])).getComponentName();
			String[] myS = ComponentManager.getReadingComponentNameForInputName(componentName);
			if (myS != null)
				name = myS[0];

			if (name != null) {
				Class<? extends VerdantiumComponent> clss = ComponentManager.getComponentClassForComponentName( name );
				if (tryFlavorsOnClass(clss,
					inFlavor,
					outFlavor)) {
					foundClass = clss;
					foundString = name;
				}
			}

		}

		if (foundClass == null) {
			String cname = null;
			cname = getComponentNameForInputFlavors(inFlavor, outFlavor);

			if (cname != null) {
				foundClass = ComponentManager.getComponentClassForComponentName( cname );
				foundString = cname;
			}

			/* while( ( count < getComponentClasses().length ) && ( FoundClass == null ) )
				{
				if( tryFlavorsOnClass( getComponentClasses()[ count ] , InFlavor , OutFlavor ) )
					{
					FoundClass = getComponentClasses()[ count ];
					foundString = getComponentNames()[ count ];
					}
				count++;
				} */
		}

		if (foundClass == null) {
			throw (
				new ComponentNotFoundException(
					"No Component Matching: "
						+ ((inFlavor[0]).getHumanPresentableName())));
		}

		return (foundClass);
	}

	/**
	* Finds a component name that matches a data flavor in <code>inFlavor</code>.  Returns the name,
	* and puts the matching flavor in index zero of <code>outFlavor</code>.
	* @param inFlavor The flavors for which to find the match.
	* @param outFlavor The array to receive the matching flavor.
	* @return name The name of the matching component.
	*/
	public static String getComponentNameForInputFlavors(
		DataFlavor[] inFlavor,
		DataFlavor[] outFlavor) {
		String name = null;
		int count = 0;

		while ((count < inFlavor.length) && (name == null)) {
			String[] names =
				ComponentManager.getComponentNameForInputFlavor(inFlavor[count]);

			if (names != null) {
				name = names[0];
				outFlavor[0] = inFlavor[count];
			}

			count++;
		}

		return (name);
	}

	/**
	* Determines whether any of the input flavors work on a particular component class.  Returns true iff.
	* one is found, and places that flavor in index zero of <code>outFlavor</code>.
	* @param in The class on which to test the flavors.
	* @param inFlavor The flavors to test against the class.
	* @param outFlavor The array to receive the matching flavor.
	* @return Whether a match is found.
	*/
	public static boolean tryFlavorsOnClass(
		Class<? extends VerdantiumComponent> in,
		DataFlavor[] inFlavor,
		DataFlavor[] outFlavor) {
		boolean ret = false;
		int count = 0;

		while ((count < inFlavor.length) && (!ret)) {
			Class<?>[] paramTypes = {
			};
			Method objMeth = null;

			try {
				objMeth =
					(in).getMethod(
						"getPersistentInputDataFlavorsSupported",
						paramTypes);

				Object[] params = {
				};
				Object myo = objMeth.invoke(null, params);

				DataFlavor[] test = (DataFlavor[]) myo;
				if (test != null) {
					int count2 = 0;
					while ((count2 < test.length) && (!ret)) {
						DataFlavor f1 = inFlavor[count];
						DataFlavor f2 = test[count2];

						ret = f1.equals(f2);
						count2++;
					}
				}

			} catch (Exception e) {
				throw (new WrapRuntimeException("Dynamic Query Failed", e));
			}

			if (ret)
				outFlavor[0] = inFlavor[count];

			count++;
		}

		return (ret);
	}

	/**
	* Broadcasts an EtherEvent in a scriptable fashion.
	* @param send The event to be broadcast.
	* @param refcon A reference to context data that the generating code can associate with the event.  See various references to "refcon" in MacOS programming.
	* @return The result of executing the event.
	*/
	public static Object fireEtherEvent(EtherEvent in, Object refcon)
		throws Throwable {
		Object ret = null;
		Object[] parm = { in, null, refcon };
		propSL.firePropertyChange(EtherEventStart, null, parm);

		try {
			Object target = in.getTarget();

			if (target instanceof EtherEventHandler) {
				ret = VerdantiumUtils.handleImplicitObjEtherEvent(in, refcon);
				boolean found = VerdantiumUtils.implicitEventHandled();
				if (!found) {
					EtherEventHandler hndl = (EtherEventHandler) target;
					ret = hndl.processObjEtherEvent(in, refcon);
				}
			}

			if (target instanceof String) {
				ret = VerdantiumUtils.handleImplicitClassEtherEvent(in, refcon);
				boolean found = VerdantiumUtils.implicitEventHandled();
				if (!found) {
					String programName = (String) target;
					Class<? extends VerdantiumComponent> programClass =
						ProgramDirector.getComponentClass(programName);
					Class<?>[] paramTypes = { EtherEvent.class, Object.class };
					Object[] params = { in, refcon };
					Method objMeth =
						(programClass).getMethod(
							"processClassEtherEvent",
							paramTypes);
					ret = objMeth.invoke(null, params);
				}
			}
		} catch (Throwable ex) {
			Object[] parm2 = { in, null, refcon, ex };
			try {
				propSL.firePropertyChange(EtherEventErr, null, parm2);
			} catch (Throwable ex2) { /* Do Nothing */
			}
			throw (ex);
		}

		parm[1] = ret;
		propSL.firePropertyChange(EtherEventEnd, null, parm);
		return (ret);
	}

	/**
	* Displays help in a component in a particular frame.
	* @param partName The human-readable name of the component in which to display help.
	* @param fr The component requesting the help display.
	* @param src The data model of the component requesting the help display.
	*/
	public static void displayHelpOnComponent(
		String partName,
		VerdantiumComponent fr,
		PropertyChangeSource src) {
		try {
			Class<? extends VerdantiumComponent> programClass = ProgramDirector.getComponentClass(partName);
			Class<?>[] types = { VerdantiumComponent.class };
			Object[] param = { fr };
			Method classMeth =
				(programClass).getMethod("displayVerdantiumHelp", types);
			classMeth.invoke(null, param);
		} catch (Exception e) {
			if ((e instanceof NoSuchMethodException)
				|| (e instanceof ComponentNotFoundException)) {
				VerdantiumUtils.produceMessageWindow(
					e,
					"Help Not Available",
					"Help Not Available",
					"Help is not Available for this component.",
					fr,
					src);
			} else {
				throw (new WrapRuntimeException("Help Failed", e));
			}
		}
	}

	/**
	* Maps a data flavor to an output data flavor.
	* @param The input data flavor.
	* @return The matching output data flavor.
	*/
	public static DataFlavor mapDataFlavorToOutput(DataFlavor in) {
		DataFlavor ret = null;

		if (in instanceof TransVersionBufferFlavor) {
			ret = in;
		} else {
			ret =
				ComponentManager.createOutputStreamFlavor(
					in.getPrimaryType(),
					in.getSubType());
		}

		return (ret);
	}

	/**
	* Attempts to handle a generic name, first as a URL, and then as a file name.
	* @return The URL matching the generic name.
	*/
	public static URL handleParseGenericName(String name)
		throws MalformedURLException {
		URL url = null;
		try {
			url = new URL(name);
		} catch (Exception ex1) {
			try {
				File fi = new File(name);
				url = fi.toURL();
			} catch (Exception ex) {
				throw (new MalformedURLException("File name : " + name));
			}
		}

		return (url);
	}

	/**
	* Gets the input data flavors supported.  ProgramDirector supports its own
	* proprietary format only.
	* @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{
				 new TransVersionBufferFlavor(
					"Program Director",
					"Program Director")};
		return (MyF);
	}

	/**
	* Gets the output data flavors supported.  ProgramDirector supports its own
	* proprietary format only.
	* @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{
				 new TransVersionBufferFlavor(
					"Program Director",
					"Program Director")};
		return (MyF);
	}

	/**
	* Loads data for the component from persistent storage.
    * @param flavor The flavor in which to load the data.
    * @param trans The transferable from which to load the data.
	*/
	public void loadPersistentData(DataFlavor flavor, Transferable trans)
		throws IOException {
		if (trans == null) {
		} else {
			try {
				TransVersionBuffer MyF = (TransVersionBuffer) trans;
				VersionBuffer.chkNul(MyF);
			} catch (ClassCastException e) {
				throw (new DataFormatException(e));
			}
		}
	}

	/**
	* Saves the component to persistent storage.
    * @param The flavor in which to save the data.
    * @return Transferable to which the data is saved.
	*/
	public Transferable savePersistentData(DataFlavor flavor) {
		TransVersionBuffer MyF =
			new TransVersionBuffer("Program Director", "Program Director");
		return (MyF);
	}

	/**
	* Handles the throwing of an error or exception.
	* @param in The error or exception.
	*/
	public void handleThrow(Throwable in) {
		VerdantiumUtils.handleThrow(in, this, this);
	}

	/**
	* Optional method to display help in a component.
	* @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		ProgramDirectorHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
	* @param argv Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();

		try {
			if (argv.length == 2) {
				if ((argv[0]).equals("-x")) {
					ProgramDirectorEvent send =
						new ProgramDirectorEvent(
							"Program Director",
							ProgramDirectorEvent.loadURL,
							null,
							"Program Director");
					Object[] param =
						{ null, new Point(20, 20), new Boolean(false)};
					param[0] = handleParseGenericName(argv[1]);
					send.setParameter(param);
					ProgramDirector.showComponent(send, null);
					return;
				}
			}

			ProgramDirector MyComp = new ProgramDirector();
			ProgramDirector.showComponent(
				MyComp,
				"Program Director",
				argv,
				true);
		} catch (Throwable ex) {
			VerdantiumUtils.handleThrow(ex, null, null);
		}
	}

	/**
	 * The PropertyChangeSupport for ProgramDirector instances.
	 */
	private PropertyChangeSupport propL = null;
	
	
}

