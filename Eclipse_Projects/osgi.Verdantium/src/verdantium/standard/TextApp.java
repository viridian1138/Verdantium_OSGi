package verdantium.standard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.rtf.RTFEditorKit;

import meta.DataFormatException;
import meta.VersionBuffer;
import meta.WrapRuntimeException;
import verdantium.EtherEvent;
import verdantium.EtherEventPropertySource;
import verdantium.GenericStreamOutputTrans;
import verdantium.ProgramDirector;
import verdantium.ProgramDirectorEvent;
import verdantium.StandardEtherEvent;
import verdantium.TransVersionBuffer;
import verdantium.TransVersionBufferFlavor;
import verdantium.UrlHolder;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumFlavorMap;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.VerdantiumUtils;
import verdantium.core.BackgroundListener;
import verdantium.core.BackgroundPropertyEditor;
import verdantium.core.BorderPropertyEditor;
import verdantium.core.CompoundFindIterator;
import verdantium.core.ContainerApp;
import verdantium.core.ContainerAppDesktopPane;
import verdantium.core.ContainerAppInternalFrame;
import verdantium.core.ContainerFindIterator;
import verdantium.core.DefaultPropertyEditor;
import verdantium.core.DesignerControl;
import verdantium.core.EditorControl;
import verdantium.core.EmbeddingPropertyEditor;
import verdantium.core.FindReplaceIterator;
import verdantium.core.OnlyDesignerEditListener;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.standard.help.TextAppHelp;
import verdantium.undo.UndoManager;
import verdantium.utils.ComponentNotFoundException;
import verdantium.utils.IllegalInputException;
import verdantium.utils.ResourceNotFoundException;
import verdantium.utils.VerdantiumDragUtils;
import verdantium.utils.VerdantiumDropUtils;
import verdantium.xapp.DocPageFormat;
import verdantium.xapp.MacroTreeMap;
import verdantium.xapp.OnlyDesignerEdits;
import verdantium.xapp.OnlyDesignerEditsChangeHandler;

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
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Needed to make multiple improvements to TextApp.                     | Improved printing and macro handling among other things.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/29/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for multiple PLAFs.                                          | Made code changes to support multiple PLAFs.
*    | 10/05/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for command-line arguments.                                  | Added support for command-line arguments.
*    | 11/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Needed support for paragraph formatting.                             | Added support for paragraph formatting.
*    | 11/17/2001            | Thorn Green (viridian_1138@yahoo.com)           | Verdantium Exceptions not modular or extensible.                     | Made the exception handling more extensible.
*    | 12/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Third-Cut at Error Handling.                                         | Third-Cut at Error Handling.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
*    | 04/21/2002            | Thorn Green (viridian_1138@yahoo.com)           | Find/Replace support.                                                | Added find/replace support.
*    | 04/25/2002            | Thorn Green (viridian_1138@yahoo.com)           | Find/Replace support for inserted components.                        | Added find/replace support for inserted components.
*    | 04/28/2002            | Thorn Green (viridian_1138@yahoo.com)           | Started making a modification, then thought better of it.            | Should be no actual code change here.
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
* TextApp demonstrates
* how a simple text-based application can embed objects of type {@link VerdantiumComponent}.
* Without such a demonstration component it would be difficult to verify that Verdantium works
* as advertised.  Third, it shows how the current implementation of components
* can be embedded in a variety of other types of software.
* Fourth, it provides a practical working source code example to help individuals 
* to implement other components as needed.
* <P>
* @author Thorn Green
*/
public class TextApp
	extends Object
	implements
		VerdantiumComponent,
		MouseListener,
		BackgroundListener,
		PropertyChangeListener,
		OnlyDesignerEditListener,
		EtherEventPropertySource,
		OnlyDesignerEditsChangeHandler {
	
	/**
	* The desktop pane of the component.
	*/
	protected TextAppDesktopPane myDesk = null;
	
	/**
	* The text editing pane of the component.
	*/
	protected JEditorPane myEdit = new TextAppEditorPane();
	
	/**
	* The panel containing the GUI for the component.
	*/
	protected JPanel myPan = new JPanel();
	

	/**
	* The border class.
	*/
	protected String borderClass = null;
	
	/**
	* The parameter types of the constructor for the border.
	*/
	protected Class<?>[] borderTypes = null;
	
	/**
	* The parameters of the constructor for the border.
	*/
	protected Object[] borderParam = null;

	/**
	* Sets whether the component can only be edited in designer mode.
	*/
	private OnlyDesignerEdits onlyDesignerEdits = null;

	/**
	* Property change event indicating a change in the foreground and background colors of the text.
	*/
	public static String TextAppColors = "TextAppColors";
	
	/**
	* Property change event indicating a font name change.
	*/
	public static String TextAppFont = "TextAppFont";
	
	/**
	* Property change event indicating a change in font size.
	*/
	public static String TextAppFontSize = "TextAppFontSize";
	
	/**
	* EtherEvent name to set the foreground and background colors of the text.
	*/
	public static String setTextAppColors = "setTextAppColors";
	
	/**
	* EtherEvent name to set the font of the text.
	*/
	public static String setTextAppFontName = "setTextAppFontName";
	
	/**
	* EtherEvent name to set the size of the font.
	*/
	public static String setTextAppFontSize = "setTextAppFontSize";
	
	/**
	* EtherEvent name to toggle the use of boldface.
	*/
	public static String toggleBoldAction = "toggleBoldAction";
	
	/**
	* EtherEvent name to toggle the use of italics.
	*/
	public static String toggleItalicAction = "toggleItalicAction";
	
	/**
	* EtherEvent name to toggle the use of underline.
	*/
	public static String toggleUnderlineAction = "toggleUnderlineAction";
	
	/**
	* EtherEvent name to insert a component directly into the text.
	*/
	public static String insertVerdantiumComponent =
		"insertVerdantiumComponent";
	
	/**
	* EtherEvent name to set the paragraph formatting.
	*/
	public static String setParagraphFormatting = "setParagraphFormatting";
	
	/**
	* EtherEvent name to set the paragraph alignment.
	*/
	public static String setParagraphAlignment = "setParagraphAlignment";

	/**
	* The font size of the text.
	*/
	private int fontSize = 12;
	
	/**
	* The font name of the text.
	*/
	private String fontName = "DialogInput";
	
	/**
	* Foreground color of the text.
	*/
	private Color fgColor = Color.black;
	
	/**
	* Background color of the text.
	*/
	private Color bkColor = Color.white;

	/**
	* The URL used when the user selects "Save" as opposed to "Save As..."
	*/
	transient protected URL fileSaveURL = null;

	/**
	* The data flavor used when the user selects "Save" as opposed to "Save As..."
	*/
	transient protected DataFlavor fileSaveFlavor = null;

	/**
	* The information stored by the "Page Setup" dialog.
	*/
	transient protected DocPageFormat docPageFormat = null;

	/**
	* The tree map containing the client macros.
	*/
	protected MacroTreeMap macroMap = null;

	/**
	 * The multi-level undo manager of the component.
	 */
	protected UndoManager tmmgr = null;

	/**
	* Returns whether only the designer can edit the text app.
	* @return Whether only the designer can edit the text app.
	*/
	public boolean isOnlyDesignerEdits() {
		return (onlyDesignerEdits.isOnlyDesignerEdits());
	}

	/**
	* Gets the foreground color of the text.
	* @return The foreground color of the text.
	*/
	public Color getFgColor() {
		return (fgColor);
	}
	
	/**
	* Gets the background color of the text.
	* @return The background color of the text.
	*/
	public Color getBkColor() {
		return (bkColor);
	}

	/**
	* Sets the foreground and background colors of the text.
	* @param fg The foreground color to set.
	* @param bk The background color to set.
	*/
	protected void setColors(Color fg, Color bk) {
		fgColor = fg;
		bkColor = bk;
		ActionEvent e =
			new ActionEvent(myEdit, ActionEvent.ACTION_PERFORMED, "go");
		(
			new StyledEditorKit.ForegroundAction(
				"foreground",
				fgColor)).actionPerformed(
			e);
		/* myEdit.setForeground( Fg ); */
		myEdit.repaint();
		propL.firePropertyChange(TextAppColors, null, null);
	}

	/**
	* Gets the font size of the text.
	* @return The font size of the text.
	*/
	public int getFontSize() {
		return (fontSize);
	}

	/**
	* Sets the font size of the text.
	* @param in The font size to set.
	*/
	protected void setFontSize(int in) {
		int tmp = fontSize;
		fontSize = in;
		ActionEvent e =
			new ActionEvent(myEdit, ActionEvent.ACTION_PERFORMED, "go");
		(new StyledEditorKit.FontSizeAction("font-size", in)).actionPerformed(
			e);
		propL.firePropertyChange(
			TextAppFontSize,
			new Integer(tmp),
			new Integer(in));
	}

	/**
	* Gets the font of the text.
	* @return The name of the font.
	*/
	public String getFontName() {
		return (fontName);
	}

	/**
	* Sets the font name of the text.
	* @param in The name of the font to set.
	*/
	protected void setFontName(String in) {
		fontName = in;
		ActionEvent e =
			new ActionEvent(myEdit, ActionEvent.ACTION_PERFORMED, in);
		(
			new StyledEditorKit.FontFamilyAction(
				"font-family",
				in)).actionPerformed(
			e);
		propL.firePropertyChange(TextAppFont, null, null);
	}

	/**
	* Toggles whether text is boldface.
	*/
	protected void toggleBoldAction() {
		ActionEvent e =
			new ActionEvent(myEdit, ActionEvent.ACTION_PERFORMED, "go");
		(new StyledEditorKit.BoldAction()).actionPerformed(e);
	}
	
	/**
	* Toggles whether text is in italics.
	*/
	protected void toggleItalicAction() {
		ActionEvent e =
			new ActionEvent(myEdit, ActionEvent.ACTION_PERFORMED, "go");
		(new StyledEditorKit.ItalicAction()).actionPerformed(e);
	}

	/**
	* Toggles whether text is underlined.
	*/
	protected void toggleUnderlineAction() {
		ActionEvent e =
			new ActionEvent(myEdit, ActionEvent.ACTION_PERFORMED, "go");
		(new StyledEditorKit.UnderlineAction()).actionPerformed(e);
	}

	/**
	* Returns the GUI for the component.
	* @return The GUI for the component.
	*/
	public JComponent getGUI() {
		return (myPan);
	}

	/**
	* Handles a property change events.
	* @param evt The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == EditorControl.EditCntlChange) {
			int count;
			JInternalFrame[] AllFrames = myDesk.getAllFrames();
			myEdit.setEditable(
				(DesignerControl.isDesignTime()) || (!isOnlyDesignerEdits()));

			for (count = 0; count < AllFrames.length; count++) {
				AllFrames[count].updateUI();
				AllFrames[count].repaint();
			}
		}

		if (evt.getPropertyName()
			== EmbeddingPropertyEditor.DefaultPropertyChgName) {
			try {
				Object param = evt.getNewValue();
				Object[] arr = (Object[]) (param);
				ProgramDirectorEvent ei = (ProgramDirectorEvent) (arr[0]);
				Object obj = arr[2];
				EtherEvent send =
					new PropertyEditEtherEvent(
						this,
						insertVerdantiumComponent,
						ei,
						this);
				ProgramDirector.fireEtherEvent(send, obj);
			} catch (Throwable ex) {
				handleThrow(ex);
			}
		}

	}

	/**
	* Handles the destruction of the component by removing property change listeners, and then
	* firing a property change event indicating the destruction of the component.
	*/
	public void handleDestroy() {
		EditorControl.removePropertyChangeListener(this);
		propL.firePropertyChange(ProgramDirector.propertyHide, null, null);
		propL.firePropertyChange(
			ProgramDirector.propertyDestruction,
			null,
			null);
		myDesk.handleDestroy();
		macroMap.handleDestroy();
		docPageFormat.handleDestroy();
		onlyDesignerEdits.handleDestroy();
	}

	/**
	* Constructs the component.
	*/
	public TextApp() {
		tmmgr =
			UndoManager.createInstanceUndoManager(
				jundo.runtime.Runtime.getInitialMilieu());
		macroMap = new MacroTreeMap(tmmgr);
		docPageFormat = new DocPageFormat(tmmgr);
		onlyDesignerEdits = new OnlyDesignerEdits(tmmgr);
		propL = new PropertyChangeSupport(this);
		myEdit.setEditorKit(new StyledEditorKit());
		myDesk = new TextAppDesktopPane(tmmgr, this, myEdit);
		arrangeLayout();
		/* myEdit.setText( "Text" ); */
		myEdit.setEditable(true);
		EditorControl.addPropertyChangeListener(this);
		myEdit.addMouseListener(this);
		myEdit.setOpaque(true);
		myEdit.setToolTipText("Right-Click to edit properties");
		VerdantiumDragUtils.setDragUtil(myEdit, this);
		VerdantiumDropUtils.setDropUtil(myEdit, this, this);
		macroMap.configureForEtherEvents(this, propL);
		docPageFormat.configureForEtherEvents(this, propL);
		onlyDesignerEdits.configureForEtherEvents(this, propL);
	}

	/**
	* Sets up the layout managers for the component.
	*/
	protected void arrangeLayout() {
		myPan.setOpaque(false);
		myPan.setLayout(new BorderLayout(0, 0));
		myPan.add("Center", myDesk);
		myPan.setMinimumSize(new Dimension(2, 2));
		myPan.setPreferredSize(new Dimension(50, 50));
	}

	/**
	 * Handles Ether Events to alter the properties of the component.
	 * @param in The event to handle.
	 * @param refcon A reference to context data for the event.
	 * @return The result of handling the event, or null if there is no result.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon)
		throws Throwable {

		Object ret = macroMap.processObjEtherEvent(in, refcon);
		if (ret != EtherEvent.EVENT_NOT_HANDLED) {
			return (ret);
		}

		ret = docPageFormat.processObjEtherEvent(in, refcon);
		if (ret != EtherEvent.EVENT_NOT_HANDLED) {
			return (ret);
		}

		ret = onlyDesignerEdits.processObjEtherEvent(in, refcon);
		if (ret != EtherEvent.EVENT_NOT_HANDLED) {
			return (ret);
		}

		if (in instanceof StandardEtherEvent) {
			if (in
				.getEtherID()
				.equals(StandardEtherEvent.makePropertiesEditor))
				return (makePropertiesEditor());

			if (in
				.getEtherID()
				.equals(StandardEtherEvent.showPropertiesEditor))
				showPropertiesEditor(in);

			if (in.getEtherID().equals(StandardEtherEvent.getUrlLocn)) {
				Object[] param = { fileSaveURL, fileSaveFlavor };
				return (param);
			}

			if (in.getEtherID().equals(StandardEtherEvent.setUrlLocn)) {
				Object[] param = (Object[]) (in.getParameter());
				fileSaveURL = (URL) (param[0]);
				fileSaveFlavor = (DataFlavor) (param[1]);
			}

			if (in.getEtherID().equals(StandardEtherEvent.objUndoableClose)) {
				propL.firePropertyChange(
					ProgramDirector.propertyHide,
					null,
					null);
			}

		}

		if (in instanceof PropertyEditEtherEvent) {
			if (in
				.getEtherID()
				.equals(PropertyEditEtherEvent.isBorderSupported))
				return (PropertyEditEtherEvent.isBorderSupported);

			if (in
				.getEtherID()
				.equals(PropertyEditEtherEvent.setBorderObject)) {
				Object[] myo = (Object[]) (in.getParameter());
				setBorderObject(
					(String) (myo[0]),
					(Class[]) (myo[1]),
					(Object[]) (myo[2]));
			}

			if (in
				.getEtherID()
				.equals(PropertyEditEtherEvent.isBackgroundSupported))
				return (PropertyEditEtherEvent.isBackgroundSupported);

			if (in
				.getEtherID()
				.equals(PropertyEditEtherEvent.setBackgroundState)) {
				Object[] myo = (Object[]) (in.getParameter());
				setBackgroundState(
					(Color) (myo[0]),
					((Boolean) (myo[1])).booleanValue());
			}

			if (in
				.getEtherID()
				.equals(
					PropertyEditEtherEvent.isFindReplaceIteratorSupported)) {
				return (new Boolean(true));
			}

			if (in
				.getEtherID()
				.equals(PropertyEditEtherEvent.createFindReplaceIterator)) {
				Object[] param = (Object[]) (in.getParameter());
				FindReplaceIterator ca =
					new ContainerFindIterator(param, this, myDesk);
				FindReplaceIterator cb =
					new TextAppFindIterator(
						param,
						this,
						myEdit,
						getGUI(),
						isScrolling());
				FindReplaceIterator cc =
					new EmbedFindIterator(param, this, myEdit);
				FindReplaceIterator cd =
					new CompoundFindIterator(param, this, cb, cc);
				FindReplaceIterator ce =
					new CompoundFindIterator(param, this, cd, ca);
				return (ce);
			}

			if (in.getEtherID().equals(toggleBoldAction))
				toggleBoldAction();

			if (in.getEtherID().equals(toggleItalicAction))
				toggleItalicAction();

			if (in.getEtherID().equals(toggleUnderlineAction))
				toggleUnderlineAction();

			if (in.getEtherID().equals(setTextAppColors)) {
				Object[] myo = (Object[]) (in.getParameter());
				setColors((Color) (myo[0]), (Color) (myo[1]));
			}

			if (in.getEtherID().equals(setTextAppFontName)) {
				String name = (String) (in.getParameter());
				setFontName(name);
			}

			if (in.getEtherID().equals(setTextAppFontSize)) {
				Integer val = (Integer) (in.getParameter());
				setFontSize(val.intValue());
			}

			if (in.getEtherID().equals(insertVerdantiumComponent)) {
				ProgramDirectorEvent e =
					(ProgramDirectorEvent) (in.getParameter());
				return (handleProgramDirectorInsertEvent(e));
			}

			if (in.getEtherID().equals(setParagraphFormatting)) {
				Object[] params = (Object[]) (in.getParameter());
				double left = ((Double) (params[0])).doubleValue();
				double right = ((Double) (params[1])).doubleValue();
				double first = ((Double) (params[2])).doubleValue();
				double space = ((Double) (params[3])).doubleValue();

				if (left < 0.0)
					throw (
						new IllegalInputException("The left indent must be positive."));

				if (right < 0.0)
					throw (
						new IllegalInputException("The right indent must be positive."));

				if (first < 0.0)
					throw (
						new IllegalInputException("The first indent must be positive."));

				Document doc = myEdit.getDocument();
				if (!(doc instanceof StyledDocument)) {
					throw (new UnsupportedDocException());
				}

				StyledDocument styl = (StyledDocument) (doc);
				AttributeSet at =
					styl
						.getCharacterElement(myEdit.getCaretPosition())
						.getAttributes();
				SimpleAttributeSet simp = new SimpleAttributeSet(at);

				StyleConstants.setLeftIndent(simp, (float) left);
				StyleConstants.setRightIndent(simp, (float) right);
				StyleConstants.setFirstLineIndent(simp, (float) first);
				StyleConstants.setLineSpacing(simp, (float) space);

				setAttributeSet(simp, true);
			}

			if (in.getEtherID().equals(setParagraphAlignment)) {
				Integer align = (Integer) (in.getParameter());
				int alignment = align.intValue();

				Document doc = myEdit.getDocument();
				if (!(doc instanceof StyledDocument)) {
					throw (new UnsupportedDocException());
				}

				StyledDocument styl = (StyledDocument) (doc);
				AttributeSet at =
					styl
						.getCharacterElement(myEdit.getCaretPosition())
						.getAttributes();
				SimpleAttributeSet simp = new SimpleAttributeSet(at);

				StyleConstants.setAlignment(simp, alignment);

				setAttributeSet(simp, true);
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
	* Sets the attributes for the document.
	* @param attr The attributes to set.
	*/
	protected void setAttributeSet(AttributeSet attr)
		throws UnsupportedDocException {
		setAttributeSet(attr, false);
	}

	/**
	* Sets the attributes for a document.
	* @param attr The attributes to set.
	* @param setParagraphAttributes Whether to set the paragraph as opposed to the character attributes.
	*/
	protected void setAttributeSet(
		AttributeSet attr,
		boolean setParagraphAttributes)
		throws UnsupportedDocException {
		Document doc = myEdit.getDocument();
		if (!(doc instanceof StyledDocument)) {
			throw (new UnsupportedDocException());
		}

		StyledDocument styl = (StyledDocument) (doc);

		int xStart = myEdit.getSelectionStart();
		int xFinish = myEdit.getSelectionEnd();
		if (setParagraphAttributes)
			styl.setParagraphAttributes(xStart, xFinish - xStart, attr, false);
		else if (xStart != xFinish)
			styl.setCharacterAttributes(xStart, xFinish - xStart, attr, false);
		else {
			if (!(myEdit.getEditorKit() instanceof StyledEditorKit)) {
				throw (new UnsupportedDocException());
			}

			((StyledEditorKit) (myEdit.getEditorKit()))
				.getInputAttributes()
				.addAttributes(
				attr);
		}
	}

	/**
	 * Handles a change to whether only the designer can edit the component.
	 */
	public void handleOnlyDesignerEditsChange() {
		myEdit.setEditable(
			(DesignerControl.isDesignTime()) || (!isOnlyDesignerEdits()));
		int count;
		JInternalFrame[] allFrames = myDesk.getAllFrames();

		for (count = 0; count < allFrames.length; count++) {
			allFrames[count].updateUI();
			allFrames[count].repaint();
		}

		if (isOnlyDesignerEdits())
			myEdit.setToolTipText(null);
		else
			myEdit.setToolTipText("Right-Click to edit properties");
	}

	/**
	* Creates a property editor for the component.
	* @return The created property editor.
	*/
	public VerdantiumPropertiesEditor makePropertiesEditor() {
		Properties myP = new Properties();
		if (isScrolling())
			myP.put("Scrolling", this);
		TextAppPropertyEditor MyEdit = new TextAppPropertyEditor(this, myP);
		MyEdit.setClickPoint(new Point(10, 10));
		return (MyEdit);
	}

	/**
	* Displays a property editor for the component in response to an event.
	* @param e The input event.
	*/
	public void showPropertiesEditor(EtherEvent e) {
		VerdantiumPropertiesEditor myEdit = makePropertiesEditor();
		((DefaultPropertyEditor) myEdit).setClickPoint(
			(Point) (e.getParameter()));
		ProgramDirector.showPropertyEditor(
			myEdit,
			getGUI(),
			"Text App Property Editor");
	}

	/**
	* Sets the background color of the component and whether it is opaque.  The opaque status is ignored because it is not supported.
	* @param inC The background color of the component.
	* @param opaque Whether the component is opaque.
	*/
	protected void setBackgroundState(Color inC, boolean opaque) {
		myEdit.setBackground(inC);
		myEdit.setOpaque(opaque);
		propL.firePropertyChange(
			BackgroundPropertyEditor.AppBackground,
			null,
			null);
		myEdit.repaint();
	}

	/**
	* Gets the background color of the component.
	* @return The background color of the component.
	*/
	public Color getBackgroundColor() {
		return (myEdit.getBackground());
	}

	/**
	* Gets whether the component is opaque.
	* @return Whether the component is opaque.
	*/
	public boolean getOpaqueFlag() {
		return (myEdit.isOpaque());
	}

	/**
	* Adds a property change listener to the component.
	* @param e The input event.
	*/
	public void addPropertyChangeListener(PropertyChangeListener e) {
		propL.addPropertyChangeListener(e);
	}

	/**
	* Removes a property change listener from the component.
	* @param e The input event.
	*/
	public void removePropertyChangeListener(PropertyChangeListener e) {
		propL.removePropertyChangeListener(e);
	}

	/**
	* Handles a mouse-clicked event.  Currently does nothing.
	* @param e The input event.
	*/
	public void mouseClicked(MouseEvent e) {
	}

	/**
	* Handles a mouse-entered event.  Currently does nothing.
	* @param e The input event.
	*/
	public void mouseEntered(MouseEvent e) {
	}

	/**
	* Handles a mouse-exited event.  Currently does nothing.
	* @param e The input event.
	*/
	public void mouseExited(MouseEvent e) {
	}

	/**
	* Handles a mouse-released event.  Currently does nothing.
	* @param e The input event.
	*/
	public void mouseReleased(MouseEvent e) {
	}

	/**
	* Handles a mouse-press event by showing the property editor of the component
	* if the component is in a mode where the property editor can be shown.
	* @param e The input event.
	*/
	public void mousePressed(MouseEvent e) {
		try {
			if ((DesignerControl.isDesignTime()) || (!isOnlyDesignerEdits())) {
				if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
					EtherEvent send =
						new StandardEtherEvent(
							this,
							StandardEtherEvent.showPropertiesEditor,
							null,
							this);
					send.setParameter(e.getPoint());
					ProgramDirector.fireEtherEvent(send, null);
				}
			}
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles a program director event on the component.
	* @param e The input event.
	* @return The result of executing the event.
	*/
	protected Object handleProgramDirectorEvent(ProgramDirectorEvent e)
		throws Throwable {
		return (handleProgramDirectorFloatEvent(e));
	}

	/**
	* Handles a program director event to float a component over the text as an embedded frame.
	* @param e The input event.
	* @return The result of executing the event.
	*/
	protected Object handleProgramDirectorFloatEvent(ProgramDirectorEvent e)
		throws Throwable {
		return (ContainerApp.addComponentToPane(e, tmmgr, myDesk));
	}

	/**
	* Handles a program director event to insert a component directly into the text.
	* @param e The input event.
	* @return The result of executing the event.
	*/
	protected Object handleProgramDirectorInsertEvent(ProgramDirectorEvent e)
		throws Throwable {
		TextAppInsertPanel myFr = new TextAppInsertPanel();
		myFr.setMinimumSize(new Dimension(20, 20));

		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setComponent(attr, myFr);
		int p = myEdit.getCaretPosition();
		VerdantiumComponent in = null;

		try {
			(myEdit.getDocument()).insertString(p, " ", attr);
		} catch (BadLocationException ex) {
			throw (new WrapRuntimeException("Bad Insert", ex));
		}

		try {
			in = ProgramDirector.showComponent(e, myFr);
			myFr.setSize(myFr.getPreferredSize());

			Component tmp = myFr;
			while (!(tmp instanceof Window)) {
				tmp.invalidate();
				if (tmp instanceof JComponent)
					 ((JComponent) tmp).revalidate();
				else
					tmp.validate();
				tmp = tmp.getParent();
			}

			myFr.setComponent(in);
			myFr.invalidate();
			in.getGUI().invalidate();
			myEdit.invalidate();
			myFr.revalidate();
			in.getGUI().revalidate();
			myEdit.revalidate();
			myEdit.repaint();
		} catch (Throwable ex) {
			try {
				(myEdit.getDocument()).remove(p, 1);
			} catch (BadLocationException exx) { /* No Handle. */
			}

			myEdit.repaint();
			throw (ex);
		}

		return (in);
	}

	/**
	* Sets the border of the component using the reflection API.  CName is the class name of
	* the border, types contains the parameter types for the constructor of the border, and
	* params contains the parameters for the constructor of the border.
	* @param cName The class name of the border.
	* @param types The parameter types for the constructor of the border.
	* @param params The parameters for the constructor of the border.
	*/
	protected void setBorderObject(
		String cName,
		Class<?>[] types,
		Object[] params)
		throws ResourceNotFoundException {
		try {
			Border myBorder =
				BorderPropertyEditor.createBorder(cName, types, params);

			borderClass = cName;
			borderTypes = types;
			borderParam = params;

			getGUI().setBorder(myBorder);
			getGUI().revalidate();
			getGUI().repaint();
		} catch (ResourceNotFoundException ex) {
			throw (ex);
		}
	}

	/**
	* Returns the data flavors which the component can load from persistent storage.
	* @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] myF =
			{
				new TransVersionBufferFlavor("Text App", "Text App"),
				DataFlavor.stringFlavor,
				VerdantiumFlavorMap.createInputStreamFlavor(
					"application",
					"rtf"),
				VerdantiumFlavorMap.createInputStreamFlavor("text", "plain"),
				VerdantiumFlavorMap.createInputStreamFlavor("text", "html")};
		return (myF);
	}

	/**
	* Returns the data flavors to which the component can save its persistent state.
	* @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] myF = null;

		if (myEdit.getEditorKit() instanceof HTMLEditorKit) {
			DataFlavor[] fl =
				{
					new TransVersionBufferFlavor("Text App", "Text App"),
					VerdantiumFlavorMap.createOutputStreamFlavor(
						"text",
						"html"),
					VerdantiumFlavorMap.createOutputStreamFlavor(
						"text",
						"plain")};
			myF = fl;
		} else {
			if (myEdit.getEditorKit() instanceof RTFEditorKit) {
				DataFlavor[] fl =
					{
						new TransVersionBufferFlavor("Text App", "Text App"),
						VerdantiumFlavorMap.createOutputStreamFlavor(
							"application",
							"rtf"),
						VerdantiumFlavorMap.createOutputStreamFlavor(
							"text",
							"plain")};
				myF = fl;
			} else {
				DataFlavor[] fl =
					{
						new TransVersionBufferFlavor("Text App", "Text App"),
						VerdantiumFlavorMap.createOutputStreamFlavor(
							"text",
							"plain")};
				myF = fl;
			}
		}

		return (myF);
	}

	/**
	* Provides an interface to alter the width of the document page.
	* @param in The width of the document page.
	*/
	public void alterPageWidth(int in) throws IllegalInputException {
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
		System.out.println("Text App Loading Persistent Data");
		if (flavor != null)
			System.out.println(flavor.getMimeType());

		if (trans instanceof UrlHolder) {
			fileSaveURL = ((UrlHolder) trans).getUrl();
			fileSaveFlavor = flavor;
		}

		if (trans == null) {
			RTFEditorKit myKit = new RTFEditorKit();
			myEdit.setEditorKit(myKit);
			ContainerApp.closeAllFrames(myDesk);
			setBackgroundState(Color.white, true);
			setBorderObject(null, null, null);
			onlyDesignerEdits.setOnlyDesignerEdits(false);
			macroMap.clear();
			docPageFormat.setDocPageFormat(null);
		}

		if (flavor instanceof TransVersionBufferFlavor) {
			try {
				TransVersionBuffer myF = (TransVersionBuffer) trans;
				VersionBuffer.chkNul(myF);
				Transferable myT = (Transferable) (myF.getProperty("Frames"));
				VersionBuffer.chkNul(myT);
				String MyText = (String) (myF.getPropertyEx("Text"));
				myEdit.setText(MyText);
				(new TextAppPersistence()).readDocument(myF, myEdit);
				myEdit.setDocument(myEdit.getDocument());

				boolean op = myF.getBoolean("Opaque");
				Color bk = (Color) (myF.getProperty("Background"));
				VersionBuffer.chkNul(bk);
				if (isScrolling())
					op = true;
				setBackgroundState(bk, op);

				Object myo = myF.getProperty("borderClass");
				if (myo != null) {
					borderClass = (String) myo;
					VersionBuffer.chkNul(borderClass);
					borderTypes =
						(Class[]) (BorderPropertyEditor
							.getTypeObjArray(myF.getProperty("borderTypes")));
					VersionBuffer.chkNul(borderTypes);
					borderParam = (Object[]) (myF.getProperty("borderParam"));
					VersionBuffer.chkNul(borderParam);
					setBorderObject(borderClass, borderTypes, borderParam);
				}

				alterPageWidth(myF.getInt("pageWidth"));

				onlyDesignerEdits.setOnlyDesignerEdits(
					myF.getBoolean("OnlyDesignerEdits"));

				macroMap.readData(myF);

				ContainerApp.loadInternalDesktopFrames(
					myT,
					ContainerAppInternalFrame.class,
					tmmgr,
					myDesk);
			} catch (IOException ex) {
				throw (ex);
			} catch (ClassNotFoundException ex) {
				throw (ex);
			} catch (ResourceNotFoundException ex) {
				throw (ex);
			} catch (ComponentNotFoundException ex) {
				throw (ex);
			} catch (ClassCastException ex) {
				throw (new DataFormatException(ex));
			} catch (IllegalInputException ex) {
				throw (new DataFormatException(ex));
			}
			return;
		}

		if (flavor.equals(DataFlavor.stringFlavor)) {
			RTFEditorKit myKit = new RTFEditorKit();
			myEdit.setEditorKit(myKit);
			ContainerApp.closeAllFrames(myDesk);
			setBackgroundState(Color.white, true);
			setBorderObject(null, null, null);
			try {
				String s = ((String) trans.getTransferData(flavor));
				myEdit.setText(s);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Bad String Load.", e));
			}
			return;
		}

		if (flavor
			.getMimeType()
			.equals("text/html; class=java.io.InputStream")) {
			try {
				HTMLEditorKit myKit = new HTMLEditorKit();
				myEdit.setEditorKit(myKit);
				ContainerApp.closeAllFrames(myDesk);
				setBackgroundState(Color.white, true);
				setBorderObject(null, null, null);
				myKit.read(
					((InputStream) trans.getTransferData(flavor)),
					myEdit.getDocument(),
					0);
			} catch (IOException ex) {
				throw (ex);
			} catch (BadLocationException ex) {
				throw (new WrapRuntimeException("Location Failed", ex));
			} catch (UnsupportedFlavorException ex) {
				throw (
					new WrapRuntimeException(
						"Something Inconsistent In Flavor Handling",
						ex));
			}
			return;
		}

		if (flavor
			.getMimeType()
			.equals("application/rtf; class=java.io.InputStream")) {
			try {
				RTFEditorKit myKit = new RTFEditorKit();
				myEdit.setEditorKit(myKit);
				ContainerApp.closeAllFrames(myDesk);
				setBackgroundState(Color.white, true);
				setBorderObject(null, null, null);
				myKit.read(
					((InputStream) trans.getTransferData(flavor)),
					myEdit.getDocument(),
					0);
			} catch (IOException ex) {
				throw (ex);
			} catch (BadLocationException ex) {
				throw (new WrapRuntimeException("Location Failed", ex));
			} catch (UnsupportedFlavorException ex) {
				throw (
					new WrapRuntimeException(
						"Something Inconsistent In Flavor Handling",
						ex));
			}
			return;
		}

		/*
		* Most text editors / word processors, when they don't understand what the input is,
		* attempt to load the stream as plain text.  This block covers that case as well as the case for
		* the text/plain MIME type.
		*/
		if ((flavor != null)
			&& (!(flavor instanceof TransVersionBufferFlavor))) {
			InputStream s = null;
			RTFEditorKit myKit = new RTFEditorKit();
			myEdit.setEditorKit(myKit);
			ContainerApp.closeAllFrames(myDesk);
			setBackgroundState(Color.white, true);
			setBorderObject(null, null, null);
			try {
				s = ((InputStream) trans.getTransferData(flavor));
				Document doc = myEdit.getDocument();
				byte[] ibuff = new byte[4096];
				char[] buff = new char[4096];
				int nch;
				while ((nch = s.read(ibuff, 0, buff.length)) != -1) {
					int count;
					for (count = 0; count < nch; count++)
						buff[count] = (char) (ibuff[count]);

					String myStr = new String(buff, 0, nch);
					doc.insertString(doc.getLength(), myStr, null);
				}
			} catch (IOException e) {
				try {
					s.close();
				} catch (Exception ex) {
				}

				throw (e);
			} catch (BadLocationException ex) {
				throw (new WrapRuntimeException("Location Failed", ex));
			} catch (UnsupportedFlavorException ex) {
				throw (
					new WrapRuntimeException(
						"Something Inconsistent In Flavor Handling",
						ex));
			}
			return;
		}

	}

	/**
	* Saves the component to persistent storage.
    * @param The flavor in which to save the data.
    * @return Transferable to which the data is saved.
	*/
	public Transferable savePersistentData(DataFlavor flavor)
		throws IOException {
		Transferable trans = null;

		if (flavor instanceof TransVersionBufferFlavor) {
			try {
				TransVersionBuffer myF =
					new TransVersionBuffer("Text App", "Text App");
				myF.setProperty(
					"Frames",
					ContainerApp.saveInternalDesktopFrames(myDesk));
				(new TextAppPersistence()).writeDocument(
					myEdit.getDocument(),
					myF);
				myF.setProperty("Text", myEdit.getText());
				myF.setBoolean("Opaque", getOpaqueFlag());
				myF.setProperty(
					"Background",
					VerdantiumUtils.cloneColorRGB(getBackgroundColor()));
				myF.setBoolean("OnlyDesignerEdits", isOnlyDesignerEdits());
				myF.setInt("pageWidth", myPan.getBounds().width);
				macroMap.writeData(myF);

				if (borderClass != null) {
					myF.setProperty("borderClass", borderClass);
					myF.setProperty(
						"borderTypes",
						BorderPropertyEditor.getTypeStrArray(borderTypes));
					myF.setProperty("borderParam", borderParam);
				}

				trans = myF;
			} catch (IOException ex) {
				throw (ex);
			}
		}

		if (flavor
			.getMimeType()
			.equals("text/html; class=java.io.ByteArrayOutputStream")) {
			ByteArrayOutputStream stream = null;

			try {
				DataFlavor[] flavors = { flavor };
				stream = new ByteArrayOutputStream();
				myEdit.getEditorKit().write(
					stream,
					myEdit.getDocument(),
					myEdit.getDocument().getStartPosition().getOffset(),
					myEdit.getDocument().getLength());
				stream.close();
				trans = new GenericStreamOutputTrans(flavors, stream);
			} catch (IOException ex) {
				if (stream != null)
					try {
						stream.close();
					} catch (Exception exx) {
					}

				throw (ex);
			} catch (BadLocationException ex) {
				throw (new WrapRuntimeException("Location Failed", ex));
			}
		}

		if (flavor
			.getMimeType()
			.equals("application/rtf; class=java.io.ByteArrayOutputStream")) {
			ByteArrayOutputStream stream = null;

			try {
				DataFlavor[] flavors = { flavor };
				stream = new ByteArrayOutputStream();
				myEdit.getEditorKit().write(
					stream,
					myEdit.getDocument(),
					myEdit.getDocument().getStartPosition().getOffset(),
					myEdit.getDocument().getLength());
				stream.close();
				trans = new GenericStreamOutputTrans(flavors, stream);
			} catch (IOException ex) {
				if (stream != null)
					try {
						stream.close();
					} catch (Exception exx) {
					}

				throw (ex);
			} catch (BadLocationException ex) {
				throw (new WrapRuntimeException("Location Failed", ex));
			}

		}

		if (flavor
			.getMimeType()
			.equals("text/plain; class=java.io.ByteArrayOutputStream")) {
			ByteArrayOutputStream stream = null;

			try {
				int count;
				DataFlavor[] flavors = { flavor };
				String myText = myEdit.getText();
				byte[] myBytes = new byte[myText.length()];
				stream = new ByteArrayOutputStream();

				for (count = 0; count < myBytes.length; ++count)
					myBytes[count] = (byte) (myText.charAt(count));

				stream.write(myBytes, 0, myBytes.length);
				stream.close();
				trans = new GenericStreamOutputTrans(flavors, stream);
			} catch (IOException ex) {
				if (stream != null)
					try {
						stream.close();
					} catch (Exception exx) {
					}

				throw (ex);
			}
		}

		return (trans);
	}

	/**
	* Creates the property editor for paragraph properties.
	* @return The property editor for paragraph properties.
	*/
	public VerdantiumPropertiesEditor createParagraphPropertyEditor()
		throws UnsupportedDocException {
		Document doc = myEdit.getDocument();
		if (!(doc instanceof StyledDocument)) {
			throw (new UnsupportedDocException());
		}

		return (new ParagraphPropertyEditor(this, myEdit));
	}

	/**
	* Creates the property editor for alignment properties.
	* @return The property editor for alignment properties.
	*/
	public VerdantiumPropertiesEditor createAlignmentPropertyEditor()
		throws UnsupportedDocException {
		Document doc = myEdit.getDocument();
		if (!(doc instanceof StyledDocument)) {
			throw (new UnsupportedDocException());
		}

		return (new AlignmentPropertyEditor(this, myEdit));
	}

	/**
	* Returns whether the component is a scrolling component.
	* @return WWhether the component is a scrolling component.
	*/
	protected boolean isScrolling() {
		return (false);
	}

	/**
	* Handles the throwing of an error or exception.
	* @param in The input error or exception.
	*/
	public void handleThrow(Throwable in) {
		VerdantiumUtils.handleThrow(in, this, this);
	}

	/**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		TextAppHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		TextApp MyComp = new TextApp();
		ProgramDirector.showComponent(MyComp, "Text App", argv, true);
	}

	/**
	* Provides support for firing property change events.
	*/
	protected PropertyChangeSupport propL = null;
	
}


/**
* Desktop pane for the {@link TextApp} that contains the embedded component frames.
* 
* @author Thorn Green
*/
class TextAppDesktopPane extends ContainerAppDesktopPane {
	
	/**
	* The text pane of the TextApp.
	*/
	private JComponent backPane = null;

	/**
	* Constructor for the desktop pane.
	* @param mgr The multi-level undo manager of the PageWelder.
	* @param in The TextApp for the desktop pane.
	* @param in The text pane of the TextApp.
	*/
	public TextAppDesktopPane(UndoManager _mgr, TextApp inp, JComponent in) {
		super(_mgr, inp);
		setOpaqueFlag(false);
		backPane = in;
		setBackground(Color.blue);
		add(backPane, 0);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		backPane.setBounds(0, 0, width, height);
	}

	@Override
	public void setBounds(Rectangle r) {
		super.setBounds(r);
		Rectangle r2 = new Rectangle(0, 0, r.width, r.height);
		backPane.setBounds(r2);
	}

	@Override
	public void setSize(Dimension d) {
		super.setSize(d);
		backPane.setSize(d);
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		backPane.setSize(width, height);
	}

	@Override
	public Dimension getMinimumSize() {
		return (backPane.getMinimumSize());
	}

	@Override
	public Dimension getPreferredSize() {
		return (backPane.getPreferredSize());
	}

}
