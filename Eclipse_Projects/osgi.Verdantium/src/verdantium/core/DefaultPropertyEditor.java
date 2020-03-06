package verdantium.core;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;

import meta.WrapRuntimeException;
import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.EtherEventPropertySource;
import verdantium.ProgramDirector;
import verdantium.ProgramDirectorEvent;
import verdantium.ProgramDirectorPropertyEditor;
import verdantium.ProgramDirectorSaveEditor;
import verdantium.PropertyChangeSource;
import verdantium.StandardEtherEvent;
import verdantium.VerdantiumApplet;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.VerdantiumUtils;
import verdantium.undo.UndoEtherEvent;
import verdantium.undo.UndoManager;
import verdantium.undo.UndoPropertyEditor;
import verdantium.utils.JGMenuItem;
import verdantium.xapp.PropertyEditAdapter;

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
*    | 10/07/2000            | Thorn Green (viridian_1138@yahoo.com)           | Problems with JDK 1.3 applets.                                       | Made code compatible with JDK 1.3 applets.
*    | 10/22/2000            | Thorn Green (viridian_1138@yahoo.com)           | Methods did not have names that followed standard Java conventions.  | Performed a global modification to bring the names within spec.
*    | 02/04/2001            | Thorn Green (viridian_1138@yahoo.com)           | Not flexible enough to handle insertion of pull-down menus.          | Made menu insertion more modular.
*    | 03/11/2001            | Thorn Green (viridian_1138@yahoo.com)           | Property editor needed retargeting to work with CAGD Kit.            | Added retargeting ability.
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Multiple updates to editor, including macro support.                 | Made a number of overall changes.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/10/2001            | Thorn Green (viridian_1138@yahoo.com)           | Second-Cut at Error Handling.                                        | Second-Cut at Error Handling.
*    | 10/19/2001            | Thorn Green (viridian_1138@yahoo.com)           | Expanded window menus for GeoFrame/GeoPad.                           | Added functionality relating to window menus.
*    | 12/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Third-Cut at Error Handling.                                         | Third-Cut at Error Handling.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
*    | 04/21/2002            | Thorn Green (viridian_1138@yahoo.com)           | Find/Replace support.                                                | Added find/replace support.
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
*
*
*/

/**
* This class provides a standard suite of property editor capabilities that can be
* used or subclassed by a variety of components.  One does not have to use this
* property editor, but it's helpful to not have to "reinvent the wheel" every time
* a new app. needs to be thrown together.  Note: this class is sometimes hindered
* by a bug in Java 2 that causes pull-down menus to be clipped to the internal
* frame boundaries.  The current status of the bug is given in the E-Mail below:
* <P>
* Date: Tue, 26 Jan 1999 09:00:30 -0800 (PST)<BR>
* From: Georges Saab <Georges.Saab@Eng.Sun.COM><BR>
* To: "Thorn G. Green" <green@enuxsa.eas.asu.edu><BR>
* Subject: Re: New Swing Bug?<BR>
*<BR>
* > I've found what I think is a new bug in Swing.  When I run the program below<BR>
* > and then activate the menu "Items", the menu is clipped to the inner<BR>
* > JInternalFrame.  Is this bug new, or has somebody already reported it to Sun?<BR>
*<BR>
* Hi Thorn,<BR>
*<BR>
*    That was reported and fixed long ago, unfortunately after the code freeze<BR>
* for Java 2.  You should see the fix in an upcoming version of Swing.<BR>
*<BR>
*   Regards,<BR>
*<BR>
*        Georges Saab<BR>
*        Swing! Team<BR>
* <P>
* Note: at some point this class will need a serious rewrite.
* 
* @author Thorn Green
*/
public class DefaultPropertyEditor
	extends PropertyEditAdapter
	implements EtherEventPropertySource {
	
	/**
	 * The root pane for the editor's menu bar.
	 */
	protected JRootPane RootPane = new JRootPane();
	
	/**
	 * The editor's menu bar.
	 */
	protected JMenuBar MenuBar = new JMenuBar();
	
	/**
	 * The File menu.
	 */
	protected JMenu FileMenu = null;
	
	/**
	 * The Edit menu.
	 */
	protected JMenu EditMenu = null;
	
	/**
	 * The Macro menu.
	 */
	protected JMenu MacroMenu = null;
	
	/**
	 * The Help menu.
	 */
	protected JMenu HelpMenu = null;
	
	/**
	 * The panel containing the GUI.
	 */
	protected JPanel MyPanel = new JPanel();
	
	/**
	 * The tabbed pane in the GUI.
	 */
	protected JTabbedPane TabPane = null;

	/**
	 * The data model of component being edited.
	 */
	protected EtherEventPropertySource target = null;
	
	/**
	 * Provides an interface for creating an instance of a component, etc.
	 */
	protected ProgramDirectorPropertyEditor MyEdit = null;
	
	/**
	 * Implements a client-independent editor for setting the background color and transparency.
	 */
	protected BackgroundPropertyEditor BackEdit = null;
	
	/**
	 * Implements a client-independent editor for setting the "OnlyDesignerEdits" property.
	 */
	protected DesignerPropertyEditor DesignerEdit = null;
	
	/**
	 * Implements a client-independent editor for setting the border.
	 */
	protected BorderPropertyEditor BorderC = null;
	
	/**
	 * Component for controlling the editor state.
	 */
	protected EditorControl EditC = null;
	
	/**
	 * A property editor that allows the size of the document page to be changed.
	 */
	protected PageSizePropertyEditor MyPage = null;

	/**
	 * Property change support for component changes.
	 */
	private PropertyChangeSupport PropL = null;

	/**
	* Gets the GUI for the property editor.
	* @return The GUI for the property editor.
	*/
	public JComponent getGUI() {
		return (RootPane);
	}

	/**
	* Sets the selected index of the tabbed pane.
	* @param in The selected index of the tabbed pane.
	*/
	public void setSelectedIndex(int in) {
		TabPane.setSelectedIndex(in);
	}

	/**
	* Handles the destruction of the component.
	*/
	public void handleDestroy() {
		PropL.firePropertyChange(ProgramDirector.propertyHide, null, null);
		PropL.firePropertyChange(
			ProgramDirector.propertyDestruction,
			null,
			null);
		if (BorderC != null)
			BorderC.handleDestroy();
		if (EditC != null)
			EditC.handleDestroy();
		if (MyEdit != null)
			MyEdit.handleDestroy();
		if (BackEdit != null)
			BackEdit.handleDestroy();
		if (DesignerEdit != null)
			DesignerEdit.handleDestroy();
		if (MyPage != null)
			MyPage.handleDestroy();
		target.removePropertyChangeListener(this);
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

		return (null);
	}

	/**
	* Determines whether a property <code>chk</code> does not exist in 
	* properties list <code>in</code>.
	* @param chk The property to check for.
	* @param in The properties list.
	*/
	protected boolean lacksProperty(String chk, Properties in) {
		boolean tmp = true;
		if (in != null) {
			tmp = ((in.get(chk)) == null);
		}

		return (tmp);
	}

	/**
	 * Constructor.
   	 * @param in The data model of component being edited.
	 * @param inp Properties defining which tabs to display.
	 */
	public DefaultPropertyEditor(EtherEventPropertySource in, Properties inp) {
		try {
			target = in;
			PropL = new PropertyChangeSupport(this);
			RootPane.getContentPane().setLayout(new BorderLayout(0, 0));
			RootPane.getContentPane().add("Center", MyPanel);
			RootPane.setJMenuBar(MenuBar);
			if (target instanceof VerdantiumComponent)
				addMenus(in, inp);
			TabPane = new JTabbedPane();
			MyPanel.setLayout(new BorderLayout(0, 0));
			MyPanel.add("Center", TabPane);
			addTabs(in, inp);
			in.addPropertyChangeListener(this);
		} catch (Throwable ex) {
			throw (new WrapRuntimeException("Prop. Init. Failed", ex));
		}
	}

	/**
	* Adds menus to the property editor.
	* @param in The property source from which to define the menus.
	* @param inp Properties defining which menus to display.
	*/
	protected void addMenus(EtherEventPropertySource in, Properties inp)
		throws Throwable {
		addFileMenu(in, inp);
		addEditMenu(in, inp);
		addMacroMenu(in, inp);
		addHelpMenu(in, inp);
	}

	/**
	* Adds a file menu to the property editor.
	* @param in The property source from which to define the menus.
	* @param inp Properties defining which menus to display.
	*/
	protected void addFileMenu(EtherEventPropertySource in, Properties inp) {
		FileMenu = new JMenu("File");
		ActionListener MyL =
			Adapters.createGActionListener(this, "handleFileMenu");
		JMenu FileMenu = new JMenu("File");
		JMenuItem NewItem = new JGMenuItem("New", 1, MyL);
		JMenuItem OpenItem = new JGMenuItem("Open...", 2, MyL);
		JMenuItem SaveItem = new JGMenuItem("Save", 3, MyL);
		JMenuItem SaveAsItem = new JGMenuItem("Save As...", 4, MyL);
		JMenuItem SaveACopyAsItem = new JGMenuItem("Save A Copy As...", 5, MyL);
		JMenuItem PageSetupItem = new JGMenuItem("Page Setup...", 6, MyL);
		JMenuItem CustomPageSetupItem =
			new JGMenuItem("Custom Page Setup...", 7, MyL);
		JMenuItem PrintPreviewItem = new JGMenuItem("Print Preview...", 8, MyL);
		JMenuItem PrintItem = new JGMenuItem("Print...", 9, MyL);
		JMenuItem ExitItem = new JGMenuItem("Exit", 10, MyL);
		FileMenu.add(NewItem);
		FileMenu.add(OpenItem);
		if (VerdantiumApplet.isAppletActivated())
			OpenItem.setEnabled(false);
		FileMenu.addSeparator();
		FileMenu.add(SaveItem);
		if (VerdantiumApplet.isAppletActivated())
			SaveItem.setEnabled(false);
		FileMenu.add(SaveAsItem);
		if (VerdantiumApplet.isAppletActivated())
			SaveAsItem.setEnabled(false);
		FileMenu.add(SaveACopyAsItem);
		if (VerdantiumApplet.isAppletActivated())
			SaveACopyAsItem.setEnabled(false);
		FileMenu.addSeparator();
		/* if( !( VerdantiumApplet.isAppletActivated() ) ) */
		FileMenu.add(PageSetupItem);
		/* if( !( VerdantiumApplet.isAppletActivated() ) ) */
		FileMenu.add(CustomPageSetupItem);
		/* if( !( VerdantiumApplet.isAppletActivated() ) ) */
		FileMenu.add(PrintPreviewItem);
		/* if( !( VerdantiumApplet.isAppletActivated() ) ) */
		FileMenu.add(PrintItem);
		FileMenu.addSeparator();
		FileMenu.add(ExitItem);
		MenuBar.add(FileMenu);
	}

	/**
	* Adds a help menu to the property editor.
	* @param in The property source from which to define the menus.
	* @param inp Properties defining which menus to display.
	*/
	protected void addHelpMenu(EtherEventPropertySource in, Properties inp) {
		ActionListener MyL =
			Adapters.createGActionListener(this, "handleFileMenu");
		HelpMenu = new JMenu("Help");
		JGMenuItem HelpItem = new JGMenuItem("Help...", 11, MyL);
		HelpMenu.add(HelpItem);
		MenuBar.add(HelpMenu);
	}

	/**
	* Adds a macro menu to the property editor if needed.
	* @param in The property source from which to define the menus.
	* @param inp Properties defining which menus to display.
	*/
	protected void addEditMenu(EtherEventPropertySource in, Properties inp)
		throws Throwable {

		boolean useUndo = false;
		boolean useF = false;

		EtherEvent send =
			new UndoEtherEvent(
				this,
				UndoEtherEvent.isUndoSupported,
				null,
				null);
		Object ob = target.processObjEtherEvent(send, null);
		Boolean param = (Boolean) (ob);
		if (param != null) {
			useUndo = param.booleanValue();
		}

		send =
			new PropertyEditEtherEvent(
				this,
				PropertyEditEtherEvent.isFindReplaceIteratorSupported,
				null,
				null);
		ob = target.processObjEtherEvent(send, null);
		param = (Boolean) (ob);
		if (param != null) {
			useF = param.booleanValue();
		}

		ActionListener MyL = null;

		if (useUndo || useF) {
			MyL = Adapters.createGActionListener(this, "handleEditMenu");
			EditMenu = new JMenu("Edit");
			MenuBar.add(EditMenu);
		}

		if (useUndo) {
			JGMenuItem UndoItem = new JGMenuItem("Undo...", 3, MyL);
			EditMenu.add(UndoItem);
		}

		if (useUndo && useF) {
			EditMenu.addSeparator();
		}

		if (useF) {
			JGMenuItem FindItem = new JGMenuItem("Find...", 1, MyL);
			EditMenu.add(FindItem);
			JGMenuItem ReplaceItem = new JGMenuItem("Replace...", 2, MyL);
			EditMenu.add(ReplaceItem);
		}
	}

	/**
	* Adds a macro menu to the property editor if needed.
	* @param in The property source from which to define the menus.
	* @param inp Properties defining which menus to display.
	*/
	protected void addMacroMenu(EtherEventPropertySource in, Properties inp)
		throws Throwable {
		EtherEvent send =
			new ClientMacroEtherEvent(
				this,
				ClientMacroEtherEvent.isClientMacroSupported,
				null,
				null);
		Object ob = target.processObjEtherEvent(send, null);
		Boolean param = (Boolean) (ob);
		boolean useF = false;
		if (param != null) {
			useF = param.booleanValue();
		}

		if (useF) {
			ActionListener MyL =
				Adapters.createGActionListener(this, "handleMacroMenu");
			MacroMenu = new JMenu("Macro");
			JGMenuItem MacroItem = new JGMenuItem("Macros...", 1, MyL);
			MacroMenu.add(MacroItem);
			JGMenuItem RecordItem =
				new JGMenuItem("Record New Macro...", 2, MyL);
			MacroMenu.add(RecordItem);
			MenuBar.add(MacroMenu);
		}
	}

	/**
	* Handles file menu events.
	* @param e The input event.
	*/
	public void handleFileMenu(ActionEvent e) {
		try {
			int tag = ((JGMenuItem) (e.getSource())).getTag();

			switch (tag) {
				case 1 :
					EtherEvent send =
						new StandardEtherEvent(
							this,
							StandardEtherEvent.objNewEvent,
							null,
							target);
					ProgramDirector.fireEtherEvent(send, null);
					break;

				case 2 :
					ProgramDirectorPropertyEditor MyLoad =
						new ProgramDirectorPropertyEditor(
							this,
							false,
							false,
							null);
					Component MyC =
						ProgramDirector.getEnclosingContainer(getGUI());
					ProgramDirector.showComponent(
						MyLoad,
						(Container) MyC,
						"Load Dialog");
					break;

				case 3 :
					handleSave();
					break;

				case 4 :
					handleSaveAs();
					break;

				case 5 :
					handleSaveACopyAs();
					break;

				case 6 :
					send =
						new StandardEtherEvent(
							this,
							StandardEtherEvent.objPageSetupEvent,
							null,
							target);
					ProgramDirector.fireEtherEvent(send, null);
					break;

				case 7 :
					send =
						new StandardEtherEvent(
							this,
							StandardEtherEvent.objCustomPageSetupEvent,
							null,
							target);
					ProgramDirector.fireEtherEvent(send, null);
					break;

				case 8 :
					send =
						new StandardEtherEvent(
							this,
							StandardEtherEvent.objPrintPreviewEvent,
							null,
							target);
					ProgramDirector.fireEtherEvent(send, null);
					break;

				case 9 :
					send =
						new StandardEtherEvent(
							this,
							StandardEtherEvent.objPrintEvent,
							null,
							target);
					ProgramDirector.fireEtherEvent(send, null);
					break;

				case 10 :
					send =
						new StandardEtherEvent(
							this,
							StandardEtherEvent.objQuitEvent,
							null,
							target);
					ProgramDirector.fireEtherEvent(send, null);
					break;

				case 11 :
					displayHelpOnComponent();
					break;
			}

		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles edit menu events.
	* @param e The input event.
	*/
	public void handleEditMenu(ActionEvent e) {
		int tag = ((JGMenuItem) (e.getSource())).getTag();

		switch (tag) {
			case 1 :
				VerdantiumComponent tar = (VerdantiumComponent) target;
				VerdantiumPropertiesEditor findEditor =
					createFindPropertyEditor();
				ProgramDirector.showPropertyEditor(
					findEditor,
					tar.getGUI(),
					"Find");
				break;

			case 2 :
				VerdantiumComponent targ = (VerdantiumComponent) target;
				VerdantiumPropertiesEditor replEditor =
					createReplacePropertyEditor();
				ProgramDirector.showPropertyEditor(
					replEditor,
					targ.getGUI(),
					"Replace");
				break;

			case 3 :
				VerdantiumComponent tarf = (VerdantiumComponent) target;
				VerdantiumPropertiesEditor undoEditor =
					createUndoPropertyEditor();
				ProgramDirector.showPropertyEditor(
					undoEditor,
					tarf.getGUI(),
					"Undo");
				break;

		}
	}

	/**
	* Handles macro menu events.
	* @param e The input event.
	*/
	public void handleMacroMenu(ActionEvent e) {
		int tag = ((JGMenuItem) (e.getSource())).getTag();

		switch (tag) {
			case 1 :
				VerdantiumComponent tar = (VerdantiumComponent) target;
				VerdantiumPropertiesEditor macroEditor =
					createClientMacroPropertyEditor();
				ProgramDirector.showPropertyEditor(
					macroEditor,
					tar.getGUI(),
					"Macros");
				break;

			case 2 :
				VerdantiumComponent targ = (VerdantiumComponent) target;
				VerdantiumPropertiesEditor macroRecorder =
					createClientMacroRecordingEditor();
				ProgramDirector.showPropertyEditor(
					macroRecorder,
					targ.getGUI(),
					"Record Macro");
				break;

		}
	}

	/**
	 * Creates a multi-level undo property editor.
	 * @return The multi-level undo property editor.
	 */
	public VerdantiumPropertiesEditor createUndoPropertyEditor() {
		VerdantiumPropertiesEditor undoEditor = null;
		try {
			VerdantiumComponent tarf = (VerdantiumComponent) target;
			UndoManager undoMgr = getUndoManager();
			undoEditor =
				new UndoPropertyEditor((PropertyChangeSource) tarf, undoMgr);
		} catch (Throwable ex) {
			handleThrow(ex);
		}
		return (undoEditor);
	}

	/**
	 * Gets the undo manager of the component being edited.
	 * @return The undo manager of the component being edited.
	 * @throws Throwable
	 */
	protected UndoManager getUndoManager() throws Throwable {
		VerdantiumComponent tarf = (VerdantiumComponent) target;
		UndoEtherEvent evt =
			new UndoEtherEvent(
				this,
				UndoEtherEvent.getUndoManager,
				null,
				target);
		UndoManager undoMgr =
			(UndoManager) (tarf.processObjEtherEvent(evt, null));
		return (undoMgr);
	}

	/**
	* Creates a property editor for finding text strings.
	* @return The property editor for finding text strings.
	*/
	public VerdantiumPropertiesEditor createFindPropertyEditor() {
		return (new FindPropertyEditor(target));
	}

	/**
	* Creates a property editor for replacing text strings.
	* @return T property editor for replacing text strings.
	*/
	public VerdantiumPropertiesEditor createReplacePropertyEditor() {
		return (new ReplacePropertyEditor(target));
	}

	/**
	 * Creates a client macro property editor.
	 * @return @return The created client macro property editor.
	 */
	public VerdantiumPropertiesEditor createClientMacroPropertyEditor() {
		return (new ClientMacroPropertyEditor(target));
	}

	/**
	 * Creates a client macro recording editor.
	 * @return @return The created client macro recording editor.
	 */
	public VerdantiumPropertiesEditor createClientMacroRecordingEditor() {
		VerdantiumComponent tar = (VerdantiumComponent) target;
		return (new ClientMacroRecordingEditor(tar));
	}

	/**
	* Handles a program Director event from the "file open" dialog.
	* @param e The input event.
	*/
	protected Object handleProgramDirectorEvent(ProgramDirectorEvent e)
		throws Throwable {
		EtherEvent send =
			new StandardEtherEvent(
				this,
				StandardEtherEvent.objOpenEvent,
				e.getProgramURL(),
				target);
		Object ob = ProgramDirector.fireEtherEvent(send, null);
		return (ob);
	}

	/**
	* Sets the location at which the next frame should be embedded.
	* @param inPt The location of the input mouse event.
	*/
	public void setClickPoint(Point inPt) {
		if (MyEdit != null)
			MyEdit.setClickPoint(inPt);
	}

	/**
	* Adds property edit tabs to the GUI.  Note: a lot of classes are subclassing this method
	* in ways that are necessary, but bad.  Keep this implementation for now, and wait for
	* a better version of JTabbedPane.
	* @param in The property source from which to define the tabs.
	* @param inp Properties defining which tabs to display.
	*/
	protected void addTabs(EtherEventPropertySource in, Properties inp) {

		try {
			EtherEvent send =
				new ProgramDirectorEvent(
					this,
					ProgramDirectorEvent.isProgramDirectorEventSupported,
					null,
					in);

			Object ob = ProgramDirector.fireEtherEvent(send, null);
			if (ob != null) {
				Object tmp = null;
				String Name = "Frames";
				if (inp != null)
					tmp = inp.get("ProgramDirectorEditorName");
				if (tmp != null)
					Name = (String) tmp;
				MyEdit =
					new ProgramDirectorPropertyEditor(
						in,
						lacksProperty("NoNewTab", inp),
						null);
				TabPane.add(Name, MyEdit.getGUI());
			}

			if (lacksProperty("NoEditControl", inp)) {
				EditC = new EditorControl();
				TabPane.add("Editor", EditC.getGUI());
			}

			send =
				new PropertyEditEtherEvent(
					this,
					PropertyEditEtherEvent.isBorderSupported,
					null,
					target);

			ob = ProgramDirector.fireEtherEvent(send, null);
			if (ob != null) {
				BorderC = new BorderPropertyEditor(in);
				TabPane.add("Border", BorderC.getGUI());
			}

			if (in instanceof BackgroundListener) {
				BackEdit =
					new BackgroundPropertyEditor(
						(BackgroundListener) in,
						lacksProperty("NoColor", inp),
						lacksProperty("NoTransparent", inp));
				TabPane.add("Background", BackEdit.getGUI());
			}

			if (in instanceof OnlyDesignerEditListener) {
				if (lacksProperty("NoDesignControl", inp)) {
					DesignerEdit =
						new DesignerPropertyEditor(
							(OnlyDesignerEditListener) in);
					TabPane.add("Authoring", DesignerEdit.getGUI());
				}
			}

			if (in instanceof PageSizeHandler) {
				MyPage = new PageSizePropertyEditor((PageSizeHandler) in);
				TabPane.add("Page Size", MyPage.getGUI());
			}
		} catch (Throwable ex) {
			throw (new WrapRuntimeException("Prop. Init Failed", ex));
		}

	}

	/**
	* Handles Save requests on a file.
	*/
	protected void handleSave() throws Throwable {
		EtherEvent s2 =
			new StandardEtherEvent(
				this,
				StandardEtherEvent.getUrlLocn,
				null,
				target);
		Object ob = ProgramDirector.fireEtherEvent(s2, null);
		Object[] param = (Object[]) (ob);

		boolean chk = param != null;
		if (chk)
			chk = param[0] != null;

		if (chk) {
			DataFlavor inf = (DataFlavor) (param[1]);
			param[1] = ProgramDirector.mapDataFlavorToOutput(inf);
			EtherEvent s3 =
				new StandardEtherEvent(
					this,
					StandardEtherEvent.objSaveAsEvent,
					null,
					target);
			s3.setParameter(param);
			ProgramDirector.fireEtherEvent(s3, null);
		} else {
			handleSaveAs();
		}
	}

	/**
	* Handles Save As requests on a file.
	*/
	protected void handleSaveAs() {
		ProgramDirectorSaveEditor MySave =
			new ProgramDirectorSaveEditor(
				(VerdantiumComponent) (target),
				null,
				true);
		Component MyC = ProgramDirector.getEnclosingContainer(getGUI());
		ProgramDirector.showComponent(MySave, (Container) MyC, "Save Dialog");
	}

	/**
	* Handles Save a Copy As requests on a file.
	*/
	protected void handleSaveACopyAs() {
		EtherEvent send =
			new StandardEtherEvent(
				this,
				StandardEtherEvent.objSaveACopyAsEvent,
				null,
				target);
		ProgramDirectorSaveEditor MySave =
			new ProgramDirectorSaveEditor(
				(VerdantiumComponent) (target),
				null,
				true,
				send);
		Component MyC = ProgramDirector.getEnclosingContainer(getGUI());
		ProgramDirector.showComponent(MySave, (Container) MyC, "Save Dialog");
	}

	/**
	* Displays help on a component in a particular frame.
	*/
	public void displayHelpOnComponent() {
		Class<? extends EtherEventPropertySource> ProgramClass = target.getClass();
		Class<?>[] types = { VerdantiumComponent.class };
		Object[] param = { this };
		try {
			Method ClassMeth =
				(ProgramClass).getMethod("displayVerdantiumHelp", types);
			ClassMeth.invoke(null, param);
		} catch (Exception e) {
			VerdantiumUtils.produceMessageWindow(
				e,
				"Help Not Available",
				"Help Not Available",
				"Help is not Available for this component.",
				(VerdantiumComponent) target,
				target);
		}
	}

	/**
	* Adds a property change listener.
	* @param e The listener to add.
	*/
	public void addPropertyChangeListener(PropertyChangeListener e) {
		PropL.addPropertyChangeListener(e);
	}

	/**
	* Removes a property change listener.
	* @param e The listener to remove.
	*/
	public void removePropertyChangeListener(PropertyChangeListener e) {
		PropL.removePropertyChangeListener(e);
	}

	/**
	* Handles the throwing of an error or exception.
	* @param in The input error or exception.
	*/
	public VerdantiumPropertiesEditor handleThrow(Throwable in) {
		return (
			VerdantiumUtils.handleThrow(
				in,
				(VerdantiumComponent) target,
				target));
	}

	/**
	* Gets the background property editor for the component.
	* @return The background property editor for the component.
	*/
	public BackgroundPropertyEditor getBackgroundEditor() {
		return (BackEdit);
	}

	/**
	* Gets the program director property editor for the component.
	* @return The program director property editor for the component.
	*/
	public ProgramDirectorPropertyEditor getProgramDirectorEditor() {
		return (MyEdit);
	}

	/**
	* Sets the selected menu.
	* @param in The index of the menu to select.
	*/
	public void setMenuSelectedIndex(int in) {
		MenuBar.getSelectionModel().setSelectedIndex(in);
	}

	
}

