package verdantium.pagewelder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import meta.WrapRuntimeException;
import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.EtherEventPropertySource;
import verdantium.ProgramDirector;
import verdantium.VerdantiumApplet;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.core.DefaultPropertyEditor;
import verdantium.utils.JGMenuItem;

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
*    | 01/28/2001            | Thorn Green (viridian_1138@yahoo.com)           | Multiple bugs in calling of handleDestroy()                          | Implemented a set of bug-fixes.
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Macro support.                                                       | Added macro support.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/29/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for multiple PLAFs.                                          | Made code changes to support multiple PLAFs.
*    | 10/05/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for command-line arguments.                                  | Added support for command-line arguments.
*    | 12/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Third-Cut at Error Handling.                                         | Third-Cut at Error Handling.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
*    | 04/21/2002            | Thorn Green (viridian_1138@yahoo.com)           | Find/Replace support.                                                | Added find/replace support.
*    | 05/10/2002            | Thorn Green (viridian_1138@yahoo.com)           | Redundant information in persistent storage.                         | Made numerous persistence and packaging changes.
*    | 01/12/2003            | Thorn Green (viridian_1138@yahoo.com)           | Application development too complex.                                 | Simplified application development.
*    | 03/09/2003            | Thorn Green (viridian_1138@yahoo.com)           | PageWelder.                                                          | Implemented PageWelder using code from other classes.
*    | 08/07/2004            | Thorn Green (viridian_1138@yahoo.com)           | Establish baseline for all changes in the last year.                 | Establish baseline for all changes in the last year.
*    | 08/12/2004            | Thorn Green (viridian_1138@yahoo.com)           | First cut at template editing for PageWelder.                        | Added initial template functionality.
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
* PageWelder shows how to build a simple container application.  Copy this source code,
* rather than verdantium.core.ContainerApp, when building your own container.
* 
* @author Thorn Green
*/
public class PageWelderPropertyEditor extends DefaultPropertyEditor {

	/**
	* Constructs the property editor.
	* @param in The component to be edited.
	* @param inp Properties defining what content to display.
	*/
	public PageWelderPropertyEditor(PageWelder in, Properties inp) {
		super(in, inp);

		try {
			addPageWelderMenus(in, inp);
		} catch (Throwable ex) {
			throw (new WrapRuntimeException("Prop. Init. Failed", ex));
		}
	}

	/**
	* De-functionalizes superclass method to add menus to the property editor.
	* @param in The model of the component being edited.
	* @param inp Properties defining menu creation.
	*/
	protected void addMenus(EtherEventPropertySource in, Properties inp) {
	}

	/**
	* Adds menus to the property editor.
	* @param in The model of the component being edited.
	* @param inp Properties defining menu creation.
	*/
	protected void addPageWelderMenus(
		EtherEventPropertySource in,
		Properties inp)
		throws Throwable 
	{
		addFileMenu(in, inp);
		addEditMenu(in, inp);
		addGoMenu(in, inp);
		addMacroMenu(in, inp);
		addHelpMenu(in, inp);
	}

	/**
	* Adds a file menu to the property editor.
	* @param in The model of the component being edited.
	* @param inp Properties defining menu creation.
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
		JMenuItem PrintPreviewCardItem =
			new JGMenuItem("Print Preview Card...", 8, MyL);
		JMenuItem PrintPreviewStackItem =
			new JGMenuItem("Print Preview Stack...", 100, MyL);
		JMenuItem PrintCardItem = new JGMenuItem("Print Card...", 9, MyL);
		JMenuItem PrintStackItem = new JGMenuItem("Print Stack...", 101, MyL);
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
		FileMenu.add(PrintPreviewCardItem);
		/* if( !( VerdantiumApplet.isAppletActivated() ) ) */
		FileMenu.add(PrintPreviewStackItem);
		/* if( !( VerdantiumApplet.isAppletActivated() ) ) */
		FileMenu.addSeparator();
		/* if( !( VerdantiumApplet.isAppletActivated() ) ) */
		FileMenu.add(PrintCardItem);
		/* if( !( VerdantiumApplet.isAppletActivated() ) ) */
		FileMenu.add(PrintStackItem);
		FileMenu.addSeparator();
		FileMenu.add(ExitItem);
		MenuBar.add(FileMenu);
	}

	/**
	* Adds a macro menu to the property editor if needed.
	* @param in The model of the component being edited.
	* @param inp Properties defining menu creation.
	*/
	protected void addEditMenu(EtherEventPropertySource in, Properties inp)
		throws Throwable {
		ActionListener MyL =
			Adapters.createGActionListener(this, "handleEditMenu");
		EditMenu = new JMenu("Edit");
		JGMenuItem UndoItem = new JGMenuItem("Undo...", 1, MyL);
		EditMenu.add(UndoItem);
		EditMenu.addSeparator();
		JGMenuItem NewItem = new JGMenuItem("New Card", 2, MyL);
		EditMenu.add(NewItem);
		JGMenuItem DeleteItem = new JGMenuItem("Delete Card", 3, MyL);
		EditMenu.add(DeleteItem);
		EditMenu.addSeparator();
		JGMenuItem BackItem = new JGMenuItem("Background...", 4, MyL);
		EditMenu.add(BackItem);
		EditMenu.addSeparator();
		JGMenuItem FindItem = new JGMenuItem("Find...", 5, MyL);
		EditMenu.add(FindItem);
		JGMenuItem ReplaceItem = new JGMenuItem("Replace...", 6, MyL);
		EditMenu.add(ReplaceItem);
		MenuBar.add(EditMenu);
	}

	/**
	* Adds the Go menu to the menu bar.
	* @param in The model of the component being edited.
	* @param inp Properties defining menu creation.
	*/
	protected void addGoMenu(EtherEventPropertySource in, Properties inp) {
		JMenu m;
		JMenuItem item;

		ActionListener GoL =
			Adapters.createGActionListener(this, "handleGoMenu");

		m = new JMenu("Go", true);

		item = new JGMenuItem("Back", 1, GoL);
		m.add(item);

		m.addSeparator();

		item = new JGMenuItem("First", 2, GoL);
		m.add(item);

		item = new JGMenuItem("Last", 5, GoL);
		m.add(item);

		m.addSeparator();

		item = new JGMenuItem("Prev", 3, GoL);
		m.add(item);

		item = new JGMenuItem("Next", 4, GoL);
		m.add(item);

		MenuBar.add(m);
	}

	/**
	* Handles file menu events.
	* @param e The input event.
	*/
	public void handleFileMenu(ActionEvent e) {
		try {
			EtherEvent send = null;
			int tag = ((JGMenuItem) (e.getSource())).getTag();

			switch (tag) {
				case 100 :
					send =
						new PageWelderEtherEvent(
							this,
							PageWelderEtherEvent.printPreviewStack,
							null,
							target);
					ProgramDirector.fireEtherEvent(send, null);
					break;

				case 101 :
					send =
						new PageWelderEtherEvent(
							this,
							PageWelderEtherEvent.printStack,
							null,
							target);
					ProgramDirector.fireEtherEvent(send, null);
					break;

				default :
					super.handleFileMenu(e);
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
		EtherEvent send = null;
		int tag = ((JGMenuItem) (e.getSource())).getTag();

		try {
			switch (tag) {
				case 1 :
					VerdantiumComponent tarf = (VerdantiumComponent) target;
					VerdantiumPropertiesEditor undoEditor =
						createUndoPropertyEditor();
					ProgramDirector.showPropertyEditor(
						undoEditor,
						tarf.getGUI(),
						"Undo");
					break;
				case 2 :
					send =
						new PageWelderEtherEvent(
							this,
							PageWelderEtherEvent.addCard,
							null,
							target);
					ProgramDirector.fireEtherEvent(send, null);
					break;

				case 3 :
					send =
						new PageWelderEtherEvent(
							this,
							PageWelderEtherEvent.deleteCard,
							null,
							target);
					ProgramDirector.fireEtherEvent(send, null);
					break;

				case 4 :
					send =
						new PageWelderEtherEvent(
							this,
							PageWelderEtherEvent.editBkgnd,
							null,
							target);
					ProgramDirector.fireEtherEvent(send, null);
					break;

				case 5 :
					VerdantiumComponent tar = (VerdantiumComponent) target;
					VerdantiumPropertiesEditor findEditor =
						createFindPropertyEditor();
					ProgramDirector.showPropertyEditor(
						findEditor,
						tar.getGUI(),
						"Find");
					break;

				case 6 :
					VerdantiumComponent targ = (VerdantiumComponent) target;
					VerdantiumPropertiesEditor replEditor =
						createReplacePropertyEditor();
					ProgramDirector.showPropertyEditor(
						replEditor,
						targ.getGUI(),
						"Replace");
					break;
			}

		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles go menu events.
	* @param e The input event.
	*/
	public void handleGoMenu(ActionEvent e) {
		EtherEvent send = null;
		int tag = ((JGMenuItem) (e.getSource())).getTag();

		try {
			switch (tag) {
				case 1 :
					send =
						new PageWelderEtherEvent(
							this,
							PageWelderEtherEvent.backCard,
							null,
							target);
					ProgramDirector.fireEtherEvent(send, null);
					break;

				case 2 :
					send =
						new PageWelderEtherEvent(
							this,
							PageWelderEtherEvent.firstCard,
							null,
							target);
					ProgramDirector.fireEtherEvent(send, null);
					break;

				case 3 :
					send =
						new PageWelderEtherEvent(
							this,
							PageWelderEtherEvent.prevCard,
							null,
							target);
					ProgramDirector.fireEtherEvent(send, null);
					break;

				case 4 :
					send =
						new PageWelderEtherEvent(
							this,
							PageWelderEtherEvent.nextCard,
							null,
							target);
					ProgramDirector.fireEtherEvent(send, null);
					break;

				case 5 :
					send =
						new PageWelderEtherEvent(
							this,
							PageWelderEtherEvent.lastCard,
							null,
							target);
					ProgramDirector.fireEtherEvent(send, null);
					break;
			}

		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	
}

