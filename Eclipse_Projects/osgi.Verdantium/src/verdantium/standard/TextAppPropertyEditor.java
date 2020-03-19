package verdantium.standard;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import meta.WrapRuntimeException;
import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.EtherEventPropertySource;
import verdantium.ProgramDirector;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.core.ColorCell;
import verdantium.core.DefaultPropertyEditor;
import verdantium.core.EmbeddingPropertyEditor;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.utils.IllegalInputException;
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
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/10/2001            | Thorn Green (viridian_1138@yahoo.com)           | Second-Cut at Error Handling.                                        | Second-Cut at Error Handling.
*    | 11/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Needed support for paragraph formatting.                             | Added support for paragraph formatting.
*    | 11/17/2001            | Thorn Green (viridian_1138@yahoo.com)           | Verdantium Exceptions not modular or extensible.                     | Made the exception handling more extensible.
*    | 12/06/2001            | Thorn Green (viridian_1138@yahoo.com)           | Need more specific error messages.                                   | Added more specific error messages.
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
*    |                       |                                                 |                                                                      |
*
*
*/

/**
* This is the property editor class for {@link TextApp}.
* <P>
* @author Thorn Green
*/
public class TextAppPropertyEditor
	extends DefaultPropertyEditor
	implements PropertyChangeListener {
	
	/**
	* Color cell component used to edit the foreground color of the text app.
	*/
	private ColorCell fgColorCell;
	
	/**
	* Color cell component used to edit the background color of the text app.
	*/
	private ColorCell bkColorCell;
	
	/**
	* Used to select the text size of the text app.
	*/
	private JComboBox<String> txtChoice;
	
	/**
	* Used to select the font of the text app.
	*/
	private JComboBox<String> fontChoice;
	
	/**
	* Button used to turn boldface on or off.
	*/
	private JButton boldButton;
	
	/**
	* Button used to turn italics on or off.
	*/
	private JButton italicButton;
	
	/**
	* Button used to turn underlining on or off.
	*/
	private JButton underlineButton;
	
	/**
	* Editor for the width of the document page.
	*/
	private PageWidthPropertyEditor widthEdit;

	/**
	* Editor for the insertion of components into the text.
	*/
	private EmbeddingPropertyEditor insertEditor;

	/**
	* Sets a flag when a property change is being asserted to avoid recursion.
	*/
	private boolean updating = false;

	/**
	* Handles the destruction of the component by sending handleDestroy() to all embedded components.
	*/
	public void handleDestroy() {
		super.handleDestroy();
		fgColorCell.handleDestroy();
		bkColorCell.handleDestroy();
		if (widthEdit != null)
			widthEdit.handleDestroy();
		insertEditor.handleDestroy();
	}

	/**
	* Handles a property change event on the component being edited.
	* @param e The input event.
	*/
	public void propertyChange(PropertyChangeEvent e) {
		super.propertyChange(e);

		if (e.getPropertyName() == TextApp.TextAppColors)
			handleTargetColorChange(e);

		if (e.getPropertyName() == TextApp.TextAppFont)
			handleTargetFontChange(e);

		if (e.getPropertyName() == TextApp.TextAppFontSize)
			handleTargetFontSizeChange(e);
	}

	/**
	* Handles a color change in the component being edited.
	* @param e The input event.
	*/
	protected void handleTargetColorChange(PropertyChangeEvent e) {
		if (!updating) {
			updating = true;
			fgColorCell.setColor(target.getFgColor());
			bkColorCell.setColor(target.getBkColor());
			updating = false;
			MyPanel.repaint();
		}

	}

	/**
	* Handles a font size change from the component being edited.
	* @param e The input event.
	*/
	public void handleTargetFontSizeChange(PropertyChangeEvent e) {
		if (!updating) {
			updating = true;
			txtChoice.setSelectedItem(
				(new Integer(target.getFontSize())).toString());
			updating = false;
		}

	}

	/**
	* Handles a font change from the component being edited.
	* @param e The input event.
	*/
	protected void handleTargetFontChange(PropertyChangeEvent e) {
		if (!updating) {
			updating = true;
			fontChoice.setSelectedItem(target.getFontName());
			updating = false;
		}

	}

	/**
	* Handles a foreground color change in the property editor.
	* @param e The input event.
	*/
	public void handleFgCellColorChange(ActionEvent e) {
		if (!updating) {
			try {
				updating = true;
				EtherEvent send =
					new PropertyEditEtherEvent(
						this,
						TextApp.setTextAppColors,
						null,
						target);
				Object[] param = { fgColorCell.getColor(), target.getBkColor()};
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);
			} catch (Throwable ex) {
				handleThrow(ex);
			} finally {
				updating = false;
			}
		}
	}

	/**
	* Handles a background color change in the property editor.
	* @param e The input event.
	*/
	public void handleBkCellColorChange(ActionEvent e) {
		if (!updating) {
			try {
				updating = true;
				EtherEvent send =
					new PropertyEditEtherEvent(
						this,
						TextApp.setTextAppColors,
						null,
						target);
				Object[] param = { target.getFgColor(), bkColorCell.getColor()};
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);
			} catch (Throwable ex) {
				handleThrow(ex);
			} finally {
				updating = false;
			}
		}
	}

	/**
	* Handles a font size change in the property editor.
	* @param e The input event.
	*/
	public void handleFontSizeChange(ItemEvent e) {
		if (!updating) {
			try {
				updating = true;
				EtherEvent send =
					new PropertyEditEtherEvent(
						this,
						TextApp.setTextAppFontSize,
						null,
						target);
				Integer param =
					new Integer((String) (txtChoice.getSelectedItem()));
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);
			} catch (NumberFormatException ex) {
				handleThrow(
					new IllegalInputException(
						"The font size must be a number.",
						ex));
			} catch (Throwable ex) {
				handleThrow(ex);
			} finally {
				updating = false;
			}
		}
	}

	/**
	* Handles a font change in the property editor.
	* @param e The input event.
	*/
	public void handleFontChange(ItemEvent e) {
		if (!updating) {
			try {
				updating = true;
				EtherEvent send =
					new PropertyEditEtherEvent(
						this,
						TextApp.setTextAppFontName,
						null,
						target);
				send.setParameter(fontChoice.getSelectedItem());
				ProgramDirector.fireEtherEvent(send, null);
			} catch (Throwable ex) {
				handleThrow(ex);
			} finally {
				updating = false;
			}
		}
	}

	/**
	* Handles the pressing of the bold button in the property editor.
	* @param e The input event.
	*/
	public void handleBoldButton(ActionEvent e) {
		try {
			EtherEvent send =
				new PropertyEditEtherEvent(
					this,
					TextApp.toggleBoldAction,
					null,
					target);
			ProgramDirector.fireEtherEvent(send, null);
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles the pressing of the italic button in the property editor.
	* @param e The input event.
	*/
	public void handleItalicButton(ActionEvent e) {
		try {
			EtherEvent send =
				new PropertyEditEtherEvent(
					this,
					TextApp.toggleItalicAction,
					null,
					target);
			ProgramDirector.fireEtherEvent(send, null);
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles selections from the Format menu.
	* @param e The input event.
	*/
	public void handleFormat(ActionEvent e) {
		try {
			int tag = ((JGMenuItem) (e.getSource())).getTag();

			switch (tag) {
				case 1 :
					{
						VerdantiumPropertiesEditor prop =
							((TextApp) target).createAlignmentPropertyEditor();
						ProgramDirector.showPropertyEditor(
							prop,
							((TextApp) target).getGUI(),
							"Alignment Properties");
					}
					break;

				case 2 :
					{
						VerdantiumPropertiesEditor prop =
							((TextApp) target).createParagraphPropertyEditor();
						ProgramDirector.showPropertyEditor(
							prop,
							((TextApp) target).getGUI(),
							"Paragraph Properties");
					}
					break;
			}
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles the pressing of the underline button in the property editor.
	* @param e The input event.
	*/
	public void handleUnderlineButton(ActionEvent e) {
		try {
			EtherEvent send =
				new PropertyEditEtherEvent(
					this,
					TextApp.toggleUnderlineAction,
					null,
					target);
			ProgramDirector.fireEtherEvent(send, null);
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles Ether Events on the property editor.  Currently just calls the superclass.
	* @param in The event to handle.
	* @param refcon A reference to context data for the event.
	* @return The result of handling the event, or null if there is no result.
	*/
	public Object processObjEtherEvent(EtherEvent in, Object refcon)
		throws Throwable {
		return (super.processObjEtherEvent(in, refcon));
	}

	/**
	* Shunts the superclass method for adding the tabs.
	* @param in The property source from which to define the tabs.
	* @param inp Properties defining which tabs to display.
	*/
	protected void addTabs(EtherEventPropertySource in, Properties inp) {
	}

	/**
	* Adds the tabs for editing the text app to the superclass tabs.
	* @param in The property source from which to define the tabs.
	* @param inp Properties defining which tabs to display.
	*/
	protected void addTabsSub(EtherEventPropertySource in, Properties inp) {
		target = (TextApp) in;

		txtChoice = new JComboBox<String>();
		fontChoice = new JComboBox<String>();
		boldButton = new JButton("B");
		italicButton = new JButton("I");
		underlineButton = new JButton("u");
		fgColorCell = new ColorCell();
		bkColorCell = new ColorCell();
		fgColorCell.setOutlineBorder();
		bkColorCell.setOutlineBorder();

		txtChoice.setToolTipText("Font Size");
		fontChoice.setToolTipText("Font");
		boldButton.setToolTipText("Bold");
		italicButton.setToolTipText("Italic");
		underlineButton.setToolTipText("Underline");

		if (!lacksProperty("Scrolling", inp))
			inp.put("NoTransparent", in);

		JPanel p2 = new JPanel();
		TabPane.addTab("Document", p2);
		p2.setLayout(new FlowLayout());
		p2.add(fontChoice);
		p2.add(txtChoice);
		p2.add(boldButton);
		p2.add(italicButton);
		p2.add(underlineButton);
		p2.add(fgColorCell.getGUI());
		p2.add(bkColorCell.getGUI());

		super.addTabs(in, inp);

		fgColorCell.getGUI().setMinimumSize(new Dimension(25, 25));
		fgColorCell.getGUI().setPreferredSize(new Dimension(25, 25));
		bkColorCell.getGUI().setMinimumSize(new Dimension(25, 25));
		bkColorCell.getGUI().setPreferredSize(new Dimension(25, 25));

		fgColorCell.setColor(target.getFgColor());
		bkColorCell.setColor(target.getBkColor());

		GraphicsEnvironment ge =
			GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] font_names = ge.getAvailableFontFamilyNames();
		int cnt;
		for (cnt = 0; cnt < font_names.length; cnt++)
			fontChoice.addItem(font_names[cnt]);
		fontChoice.setSelectedItem(target.getFontName());

		txtChoice.addItem("8");
		txtChoice.addItem("10");
		txtChoice.addItem("12");
		txtChoice.addItem("14");
		txtChoice.addItem("16");
		txtChoice.addItem("18");
		txtChoice.addItem("24");
		txtChoice.addItem("30");
		txtChoice.addItem("36");
		txtChoice.setSelectedItem(
			(new Integer(target.getFontSize())).toString());

		boldButton.setFont(new Font("Dialog", Font.BOLD, 12));
		italicButton.setFont(new Font("Monospaced", Font.ITALIC, 12));
		underlineButton.setFont(new Font("Monospaced", Font.PLAIN, 12));

		ActionListener cellL =
			Adapters.createGActionListener(this, "handleFgCellColorChange");
		fgColorCell.addColorActionListener(cellL);
		cellL = Adapters.createGActionListener(this, "handleBkCellColorChange");
		bkColorCell.addColorActionListener(cellL);

		ItemListener fontSzL =
			Adapters.createGItemListener(this, "handleFontSizeChange");
		txtChoice.addItemListener(fontSzL);

		ItemListener fontL =
			Adapters.createGItemListener(this, "handleFontChange");
		fontChoice.addItemListener(fontL);

		ActionListener boldL =
			Adapters.createGActionListener(this, "handleBoldButton");
		boldButton.addActionListener(boldL);

		ActionListener italicL =
			Adapters.createGActionListener(this, "handleItalicButton");
		italicButton.addActionListener(italicL);

		ActionListener underlineL =
			Adapters.createGActionListener(this, "handleUnderlineButton");
		underlineButton.addActionListener(underlineL);

		JPanel p3 = new JPanel();
		TabPane.addTab("Insert", p3);
		insertEditor =
			new EmbeddingPropertyEditor((EtherEventPropertySource) in, null);
		insertEditor.addPropertyChangeListener((PropertyChangeListener) in);
		ProgramDirector.showComponent(insertEditor, p3, "Insert Component");

		if (((TextApp) in).isScrolling()) {
			widthEdit = new PageWidthPropertyEditor((TextApp) in);
			TabPane.add("Page Width", widthEdit.getGUI());
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
	protected void addTextAppMenus(EtherEventPropertySource in, Properties inp)
		throws Throwable {
		addFileMenu(in, inp);
		addEditMenu(in, inp);
		addFormatMenu(in, inp);
		addMacroMenu(in, inp);
		addHelpMenu(in, inp);
	}

	/**
	* Adds the format menu to the menu bar.
	* @param in The model of the component being edited.
	* @param inp Properties defining menu creation.
	*/
	protected void addFormatMenu(EtherEventPropertySource in, Properties inp) {
		JMenu m;
		JMenuItem item;

		ActionListener FormatL =
			Adapters.createGActionListener(this, "handleFormat");

		m = new JMenu("Format", true);
		item = new JGMenuItem("Alignment...", 1, FormatL);
		m.add(item);
		item = new JGMenuItem("Paragraph...", 2, FormatL);
		m.add(item);
		MenuBar.add(m);
	}

	/**
	* Constructs the property editor.
	* @param in The component to be edited.
	* @param inp Properties defining what content to display.
	*/
	public TextAppPropertyEditor(TextApp in, Properties inp) {
		super(in, inp);

		try {
			addTabsSub(in, inp);
			addTextAppMenus(in, inp);
		} catch (Throwable ex) {
			throw (new WrapRuntimeException("Prop. Init. Failed", ex));
		}
	}

	/**
	* Returns the property editor for inserting components.
	* @return The property editor for inserting components.
	*/
	public EmbeddingPropertyEditor getInsertEditor() {
		return (insertEditor);
	}

	/**
	* Returns the color cell for the foreground text color.
	* @return The color cell for the foreground text color.
	*/
	public ColorCell getFgColorCell() {
		return (fgColorCell);
	}

	/**
	* Returns the color cell for the background text color.
	* @return The color cell for the background text color.
	*/
	public ColorCell getBkColorCell() {
		return (bkColorCell);
	}

	/**
	* Gets the persistent data flavors that the property editor can load, which are none.
	* @return The persistent data flavors that the property editor can load, which are none.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		return (null);
	}

	/**
	* The TextApp being edited by the property editor.
	*/
	private TextApp target;

}
