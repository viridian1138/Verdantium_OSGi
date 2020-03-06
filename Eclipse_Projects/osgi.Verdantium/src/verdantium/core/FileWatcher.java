package verdantium.core;

import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jundo.runtime.ExtMilieuRef;
import jundo.runtime.IExtPair;
import jundo.util.pdx_JobjRef_pdx_ObjectRef;
import meta.DataFormatException;
import meta.VersionBuffer;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.ProgramDirectorEvent;
import verdantium.StandardEtherEvent;
import verdantium.TransVersionBuffer;
import verdantium.TransVersionBufferFlavor;
import verdantium.UrlHolder;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.core.help.FileWatcherHelp;
import verdantium.undo.UTag;
import verdantium.undo.UndoManager;
import verdantium.utils.ComponentNotFoundException;
import verdantium.utils.ResourceNotFoundException;
import verdantium.utils.VerdantiumDragUtils;
import verdantium.utils.VerdantiumDropUtils;
import verdantium.xapp.ApplicationAdapter;

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
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Macro support.                                                       | Started adding macro support, then thought better of it.  Removed previous changes.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/29/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for multiple PLAFs.                                          | Made code changes to support multiple PLAFs.
*    | 10/05/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for command-line arguments.                                  | Added support for command-line arguments.
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
* FileWatcher loads a component from a file and embeds it.  When the FileWatcher is saved to
* persistent storage, it stores a reference to the file (most other components store the contents
* of the file).  This can be used for several purposes.  First, this can be used to link to a URL
* on the network (e.g. a web page) that may change from time to time.  Every time the FileWatcher is
* loaded from serial storage, it will track down the contents of the URL by loading it from the
* network.  Second, FileWatcher can be used to implement a very simple "Publish and Subscribe" 
* capability. A change in the referenced file will be loaded each time the FileWatcher is.  
* One unique feature of this is that FileWatcher will allow "Publish and Subscribe" to work over the network.
* Third, a file can be shared by all other files that load it through a FileWatcher instance.
* Since a FileWatcher file reference may take much less disk space than the original file,
* the file link can allow more information to be put on a disk, and reduce redundancy.  Note that
* the current version of Verdantium only supports absolute paths when referencing files.  
* This can be a problem for certain kinds of applications, and support for relative paths will
* be included in a future version of Verdantium.
* 
* @author Thorn Green
*/
public class FileWatcher extends ApplicationAdapter implements MouseListener {
	
	/**
	 * The panel containing the GUI for the component.
	 */
	private JPanel myPanel = new JPanel();
	
	/**
	 * The last URL that was successfully applied.
	 */
	private URL cacheU = null;
	
	/**
	 * Undoable reference to the URL being watched.
	 */
	private pdx_JobjRef_pdx_ObjectRef myU = null;
	
	/**
	 * The component that was loaded to represent the URL being watched.
	 */
	private VerdantiumComponent myPart = null;

	/**
	 * Gets the URL being watched.
	 * @return The URL being watched.
	 */
	protected URL getMyU() {
		return ((URL) (myU.pdxm_getVal(undoMgr.getCurrentMil())));
	}

	/**
	 * Sets the URL being watched.
	 * @param in The input URL.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws ResourceNotFoundException
	 * @throws ComponentNotFoundException
	 */
	protected void setMyUComp(URL in)
		throws
			IOException,
			ClassNotFoundException,
			ResourceNotFoundException,
			ComponentNotFoundException {
		if (cacheU != in) {
			if (myPart != null) {
				myPart.handleDestroy();
				myPanel.remove(myPart.getGUI());
				myPanel.invalidate();
				myPanel.validate();
				myPanel.repaint();
				myPart = null;
			}
			if (in != null) {
				myPart = ProgramDirector.loadNewPersistentFile(in, myPanel);
				myPanel.invalidate();
				myPart.getGUI().invalidate();
				myPanel.validate();
				myPart.getGUI().validate();
				myPanel.repaint();
			}
		}
		cacheU = in;
	}

	/**
	 * Sets the URL being watched and the associated multi-level undo state.
	 * @param in The input URL.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws ResourceNotFoundException
	 * @throws ComponentNotFoundException
	 */
	protected void setMyU(URL in)
		throws
			IOException,
			ClassNotFoundException,
			ResourceNotFoundException,
			ComponentNotFoundException {
		ExtMilieuRef mil = myU.pdxm_setVal(undoMgr.getCurrentMil(), in);
		undoMgr.handleCommitTempChange(mil);
		setMyUComp(in);
	}

	/**
	 * Handles a change to the current undo state.
	 */
	protected void handleUndoStateChange() {
		URL u = getMyU();
		try {
			setMyUComp(u);
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles property change events.
	* @param e The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == UndoManager.MODEL_STATE_CHANGED) {
			handleUndoStateChange();
		}

	}

	/**
	 * Returns the GUI of the component.
	 * @return The GUI of the component.
	 */
	public JComponent getGUI() {
		return (myPanel);
	}

	/**
	 * Constructor.
	 */
	public FileWatcher() {
		initializeUndoManager();
		initialize(undoMgr);
		myPanel.setOpaque(false);
		myPanel.setMinimumSize(new Dimension(2, 2));
		myPanel.setPreferredSize(new Dimension(100, 100));
		myPanel.addMouseListener(this);
		myPanel.setToolTipText("Right-Click to edit properties");
		VerdantiumDragUtils.setDragUtil(myPanel, this);
		VerdantiumDropUtils.setDropUtil(myPanel, this, this);
		configureForEtherEvents();
	}

	/**
	 * Initializes the undo manager of the component.
	 */
	protected void initializeUndoManager() {
		ExtMilieuRef mil = jundo.runtime.Runtime.getInitialMilieu();
		IExtPair pair = pdx_JobjRef_pdx_ObjectRef.pdxm_allocate_JobjRef(mil);
		mil = pair.getMilieu();
		myU = (pdx_JobjRef_pdx_ObjectRef) (pair.getObject());
		undoMgr = UndoManager.createInstanceUndoManager(mil);
		undoMgr.addPropertyChangeListener(this);
	}

	/**
	 * Handles the destruction of the component.
	 */
	public void handleDestroy() {
		if (myPart != null)
			myPart.handleDestroy();
		super.handleDestroy();
	}

	/**
	 * Handles Ether Events to alter the properties of the component.
	 * @param in The event to handle.
	 * @param refcon A reference to context data for the event.
	 * @return The result of handling the event, or null if there is no result.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon)
		throws Throwable {

		if (in instanceof ProgramDirectorEvent) {
			if (in
				.getEtherID()
				.equals(ProgramDirectorEvent.isProgramDirectorEventSupported)) {
				return (new Boolean(true));
			} else {
				return (handleProgramDirectorEvent((ProgramDirectorEvent) in));
			}

		}

		Object ret = super.processObjEtherEvent(in, refcon);
		if (ret == EtherEvent.EVENT_NOT_HANDLED)
			ret = null;
		return (ret);
	}

	/**
	* Creates a properties editor for the FileWatcher.
	* @return The created property editor.
	*/
	public VerdantiumPropertiesEditor makePropertiesEditor() {
		Properties MyP = new Properties();
		MyP.put("NoNewTab", this);
		MyP.put("NoEditControl", this);
		MyP.put("ProgramDirectorEditorName", "Link");
		DefaultPropertyEditor MyEdit = new DefaultPropertyEditor(this, MyP);
		return (MyEdit);
	}

	/**
	* Shows the properties editor for the FileWatcher.
	* @param e The event for showing the property editor.
	*/
	public void showPropertiesEditor(EtherEvent e) {
		VerdantiumPropertiesEditor MyEdit = makePropertiesEditor();
		ProgramDirector.showPropertyEditor(
			MyEdit,
			getGUI(),
			"File Watcher Property Editor");
	}

	/**
	* Handles a mouse-clicked event.
	* @param e The input event.
	*/
	public void mouseClicked(MouseEvent e) {
	}

	/**
	* Handles a mouse-entered event.
	* @param e The input event.
	*/
	public void mouseEntered(MouseEvent e) {
	}

	/**
	* Handles a mouse-exited event.
	*/
	public void mouseExited(MouseEvent e) {
	}

	/**
	* Handles a mouse-released event.
	* @param e The input event.
	*/
	public void mouseReleased(MouseEvent e) {
	}

	/**
	* Handles a mouse-pressed event.
	* @param e The input event.
	*/
	public void mousePressed(MouseEvent e) {
		try {
			if (((DesignerControl.isDesignTime()) || (!isOnlyDesignerEdits()))
				&& !(e.isAltDown())) {
				EtherEvent send =
					new StandardEtherEvent(
						this,
						StandardEtherEvent.showPropertiesEditor,
						null,
						this);
				ProgramDirector.fireEtherEvent(send, null);
			}
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles a ProgramDirectorEvent to embed a component.
	* @param e The input event.
	*/
	protected Object handleProgramDirectorEvent(ProgramDirectorEvent e)
		throws
			IOException,
			ClassNotFoundException,
			ResourceNotFoundException,
			ComponentNotFoundException {
		UTag utag = new UTag();
		undoMgr.prepareForTempCommit(utag);
		try {
			setMyU(e.getProgramURL());
			undoMgr.commitUndoableOp(utag, "Embed File");
			return (myPart);
		} catch (IOException ex) {
			undoMgr.commitHandleCriticalFailure(utag);
			throw (ex);
		} catch (ClassNotFoundException ex) {
			undoMgr.commitHandleCriticalFailure(utag);
			throw (ex);
		} catch (ResourceNotFoundException ex) {
			undoMgr.commitHandleCriticalFailure(utag);
			throw (ex);
		} catch (ComponentNotFoundException ex) {
			undoMgr.commitHandleCriticalFailure(utag);
			throw (ex);
		}
	}

	/**
	* Returns the data flavors that the component can load from persistent storage.
	* FileWatcher supports its own proprietary format only.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{ new TransVersionBufferFlavor("File Watcher", "File Watcher")};
		return (MyF);
	}

	/**
	* Returns the data flavors that the component can save to persistent storage.
	* FileWatcher supports its own proprietary format only.
    * @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{ new TransVersionBufferFlavor("File Watcher", "File Watcher")};
		return (MyF);
	}

	/**
	* Loads data for the component from persistent storage.
    * @param flavor The flavor in which to load the data.
    * @param trans The transferable from which to load the data.
	*/
	public void loadPersistentData(DataFlavor flavor, Transferable trans)
		throws
			IOException,
			ClassNotFoundException,
			ResourceNotFoundException,
			ComponentNotFoundException {
		try {
			if (trans instanceof UrlHolder) {
				fileSaveURL = ((UrlHolder) trans).getUrl();
				fileSaveFlavor = flavor;
			}

			if (trans == null) {
				setMyU(null);
				borderObject.setBorderObject(null, null, null);
				onlyDesignerEdits.setOnlyDesignerEdits(false);
				docPageFormat.setDocPageFormat(null);
			} else {
				TransVersionBuffer MyF = (TransVersionBuffer) trans;
				VersionBuffer.chkNul(MyF);
				URL u = (URL) (MyF.getProperty("URL"));
				setMyU(u);

				borderObject.readData(MyF);

				onlyDesignerEdits.setOnlyDesignerEdits(
					MyF.getBoolean("OnlyDesignerEdits"));
			}
		} catch (IOException e) {
			throw (e);
		} catch (ClassNotFoundException e) {
			throw (e);
		} catch (ResourceNotFoundException e) {
			throw (e);
		} catch (ComponentNotFoundException e) {
			throw (e);
		} catch (ClassCastException e) {
			throw (new DataFormatException(e));
		}

	}

	/**
	* Saves the component to persistent storage.
    * @param The flavor in which to save the data.
    * @return Transferable to which the data is saved.
	*/
	public Transferable savePersistentData(DataFlavor flavor) {
		TransVersionBuffer MyF =
			new TransVersionBuffer("File Watcher", "File Watcher");
		if (getMyU() != null)
			MyF.setProperty("URL", getMyU());
		MyF.setBoolean("OnlyDesignerEdits", isOnlyDesignerEdits());

		borderObject.writeData(MyF);

		return (MyF);
	}

	/**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		FileWatcherHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		FileWatcher MyComp = new FileWatcher();
		ProgramDirector.showComponent(MyComp, "File Watcher", argv, true);
	}

	
}

