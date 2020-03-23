package verdantium.vhelp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Position;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.rtf.RTFEditorKit;

import meta.WrapRuntimeException;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.ProgramDirectorEvent;
import verdantium.PropertyChangeSource;
import verdantium.StandardEtherEvent;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.VerdantiumUtils;
import verdantium.core.BackgroundListener;
import verdantium.core.BackgroundPropertyEditor;
import verdantium.core.BorderPropertyEditor;
import verdantium.core.ContainerAppDesktopPane;
import verdantium.core.ContainerAppInternalFrame;
import verdantium.core.DesignerControl;
import verdantium.core.EditorControl;
import verdantium.core.EmbeddingPropertyEditor;
import verdantium.core.OnlyDesignerEditListener;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.undo.UndoManager;
import verdantium.utils.ResourceNotFoundException;
import verdantium.xapp.OnlyDesignerEdits;
import verdantium.xapp.OnlyDesignerEditsChangeHandler;
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
*    | 10/22/2000            | Thorn Green (viridian_1138@yahoo.com)           | Methods did not have names that followed standard Java conventions.  | Performed a global modification to bring the names within spec.
*    | 03/10/2001            | Thorn Green (viridian_1138@yahoo.com)           | Help screens had poor execution performance at startup.              | Implemented a performance enhancement.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
*    | 04/28/2002            | Thorn Green (viridian_1138@yahoo.com)           | Help would not load properly on Linux.                               | Fixed the problem by changing the stream loading.
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
* VerdantiumHelp provides a simple help system for Verdantium components.
* <P>
* @author Thorn Green
*/
public class VerdantiumHelp
	extends PropertyEditAdapter
	implements
		MouseListener,
		BackgroundListener,
		OnlyDesignerEditListener,
		PropertyChangeSource,
		OnlyDesignerEditsChangeHandler {
	
	/**
	* The desktop pane of the component.
	*/
	protected VerdantiumHelpDesktopPane myDesk = null;
	
	/**
	* The text editing pane of the component.
	*/
	protected JEditorPane myEdit = new JEditorPane();
	
	/**
	* The panel containing the GUI for the component.
	*/
	protected JPanel myPan = new JPanel();

	/**
	* The name of the border class.
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
	 * Undo manager for help.
	 */
	protected UndoManager mgr = null;

	/**
	* Sets whether the component can only be edited in designer mode.
	*/
	private OnlyDesignerEdits onlyDesignerEdits = null;

	/**
	* Property change event indicating a change in the foreground and background colors of the text.
	*/
	public static String VerdantiumHelpColors = "VerdantiumHelpColors";
	
	/**
	* Property change event indicating a font name change.
	*/
	public static String VerdantiumHelpFont = "VerdantiumHelpFont";
	
	/**
	* Property change event indicating a change in font size.
	*/
	public static String VerdantiumHelpFontSize = "VerdantiumHelpFontSize";
	
	/**
	* EtherEvent name to set the foreground and background colors of the text.
	*/
	public static String setVerdantiumHelpColors = "setVerdantiumHelpColors";
	
	/**
	* EtherEvent name to set the font of the text.
	*/
	public static String setVerdantiumHelpFontName =
		"setVerdantiumHelpFontName";
	
	/**
	* EtherEvent name to set the size of the font.
	*/
	public static String setVerdantiumHelpFontSize =
		"setVerdantiumHelpFontSize";
	
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
	* @param fg The foreground color of the text.
	* @param bk The background color of the text.
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
		propL.firePropertyChange(VerdantiumHelpColors, null, null);
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
	* @param in The font size of the text.
	*/
	protected void setFontSize(int in) {
		int tmp = fontSize;
		fontSize = in;
		ActionEvent e =
			new ActionEvent(myEdit, ActionEvent.ACTION_PERFORMED, "go");
		(new StyledEditorKit.FontSizeAction("font-size", in)).actionPerformed(
			e);
		propL.firePropertyChange(
			VerdantiumHelpFontSize,
			new Integer(tmp),
			new Integer(in));
	}

	/**
	* Gets the font name of the text.
	* @return The font name of the text.
	*/
	public String getFontName() {
		return (fontName);
	}

	/**
	* Sets the font name of the text.
	* @param in The font name of the text.
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
		propL.firePropertyChange(VerdantiumHelpFont, null, null);
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
	* @param e The event to handle.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		try {
			if (evt.getPropertyName() == EditorControl.EditCntlChange) {
				int count;
				JInternalFrame[] AllFrames = myDesk.getAllFrames();
				myEdit.setEditable(
					(DesignerControl.isDesignTime())
						|| (!isOnlyDesignerEdits()));

				for (count = 0; count < AllFrames.length; count++) {
					AllFrames[count].updateUI();
					AllFrames[count].repaint();
				}
			}

			if (evt.getPropertyName()
				== EmbeddingPropertyEditor.DefaultPropertyChgName) {
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
				arr[1] = ProgramDirector.fireEtherEvent(send, obj);
			}
		} catch (Throwable ex) {
			handleThrow(ex);
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
		onlyDesignerEdits.handleDestroy();
	}

	/**
	* Constructs the component.
	* @param in The location of the help page text to be displayed.
	*/
	public VerdantiumHelp(URL in) {
		propL = new PropertyChangeSupport(this);
		onlyDesignerEdits =
			new OnlyDesignerEdits(
				UndoManager.createInstanceUndoManager(
					jundo.runtime.Runtime.getInitialMilieu()));
		RTFEditorKit MyKit = new RTFEditorKit();
		myEdit.setEditorKit(MyKit);
		myDesk =
			new VerdantiumHelpDesktopPane(
				UndoManager.createInstanceUndoManager(
					jundo.runtime.Runtime.getInitialMilieu()),
				this,
				myEdit);
		arrangeLayout();

		try {
			MyKit.read(in.openStream(), myEdit.getDocument(), 0);
		} catch (Exception e) {
			throw (new WrapRuntimeException("Help Failed", e));
		}

		myEdit.addMouseListener(this);
		myEdit.setEditable(true);
		EditorControl.addPropertyChangeListener(this);
		myEdit.setOpaque(true);
		onlyDesignerEdits.configureForEtherEvents(this, propL);
	}

	/**
	 * Parses the elements of the input text for component embedding,
	 * and applies the embedding.
	 */
	protected void parseElements() {
		Vector<Position> strt_pos = new Vector<Position>();
		Vector<Position> end_pos = new Vector<Position>();

		parseElements(strt_pos, end_pos);

		handleParse(strt_pos, end_pos);
	}

	/**
	 * Parses the elements of the input text.
	 * @param strt_pos The output start positions of the recognized tags.
	 * @param end_pos The output end positions of the recognized tags.
	 */
	protected void parseElements(Vector<Position> strt_pos, Vector<Position> end_pos) {
		String Txt = null;
		try {
			Txt = myEdit.getText(0, myEdit.getDocument().getLength() - 1);
		} catch (Exception e) {
			throw (new WrapRuntimeException("Help Failed", e));
		}

		int count;
		int len = Txt.length();
		for (count = 0; count < (len - 2); count++) {
			if ((Txt.charAt(count) == '{') && (Txt.charAt(count + 1) == '%')) {
				try {
					int endp = getParseEnd(Txt, count);
					Position st = myEdit.getDocument().createPosition(count);
					Position ed = myEdit.getDocument().createPosition(endp);
					strt_pos.setSize(strt_pos.size() + 1);
					end_pos.setSize(end_pos.size() + 1);
					strt_pos.setElementAt(st, strt_pos.size() - 1);
					end_pos.setElementAt(ed, end_pos.size() - 1);
				} catch (Exception e) {
					throw (new WrapRuntimeException("Help Failed", e));
				}
			}
		}
	}

	/**
	 * Handles the parsing of the input text by applying component embedding.
	 * @param strt_pos The input start positions of the recognized tags.
	 * @param end_pos The input end positions of the recognized tags.
	 */
	public void handleParse(Vector<Position> strt_pos, Vector<Position> end_pos) {
		int count;

		for (count = 0; count < strt_pos.size(); count++) {
			int strt = strt_pos.elementAt(count).getOffset();
			int end = end_pos.elementAt(count).getOffset();
			strt_pos.setElementAt(null, count);
			end_pos.setElementAt(null, count);
			if (end > strt) {
				try {
					String ParseStr =
						myEdit.getText(strt + 2, (end - 1) - (strt + 2));
					myEdit.select(strt, end + 1);
					myEdit.replaceSelection("");
					myEdit.setCaretPosition(strt);
					dispatchAction(ParseStr);
				} catch (Exception e) {
					throw (new WrapRuntimeException("Help Failed", e));
				}
			} else {
				myEdit.select(strt, strt + 2);
				myEdit.replaceSelection("");
			}
		}

		myEdit.select(1, 0);
	}

	/**
	 * Dispatches the parsing action.  To be overridden by the subclassing component.
	 * @param txt The text of the recognized tag.
	 */
	protected void dispatchAction(String txt) {
	}

	/**
	 * Gets the ending index of the tag.
	 * @param Txt The text to be parsed.
	 * @param idx The starting index of the tag.
	 * @return The ending index of the tag.
	 */
	protected int getParseEnd(String Txt, int idx) {
		int count;
		int len = Txt.length();
		for (count = idx; count < (len - 2); count++) {
			if ((Txt.charAt(count) == '%') && (Txt.charAt(count + 1) == '}')) {
				return (count + 1);
			}
		}

		return (idx);
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

		Object ret = onlyDesignerEdits.processObjEtherEvent(in, refcon);
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

			if (in.getEtherID().equals(toggleBoldAction))
				toggleBoldAction();

			if (in.getEtherID().equals(toggleItalicAction))
				toggleItalicAction();

			if (in.getEtherID().equals(toggleUnderlineAction))
				toggleUnderlineAction();

			if (in.getEtherID().equals(setVerdantiumHelpColors)) {
				Object[] myo = (Object[]) (in.getParameter());
				setColors((Color) (myo[0]), (Color) (myo[1]));
			}

			if (in.getEtherID().equals(setVerdantiumHelpFontName)) {
				String name = (String) (in.getParameter());
				setFontName(name);
			}

			if (in.getEtherID().equals(setVerdantiumHelpFontSize)) {
				Integer val = (Integer) (in.getParameter());
				setFontSize(val.intValue());
			}

			if (in.getEtherID().equals(insertVerdantiumComponent)) {
				ProgramDirectorEvent e =
					(ProgramDirectorEvent) (in.getParameter());
				return (handleProgramDirectorInsertEvent(e));
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
	 * Handles a change to whether only the designer can edit the component.
	 */
	public void handleOnlyDesignerEditsChange() {
		myEdit.setEditable(
			(DesignerControl.isDesignTime()) || (!isOnlyDesignerEdits()));
		int count;
		JInternalFrame[] AllFrames = myDesk.getAllFrames();

		for (count = 0; count < AllFrames.length; count++) {
			AllFrames[count].updateUI();
			AllFrames[count].repaint();
		}

		if (isOnlyDesignerEdits())
			myEdit.setToolTipText(null);
		else
			myEdit.setToolTipText("Right-Click to edit properties");
	}

	/**
	 * Creates the properties editor for the component.
	 * @return The created property editor.
	 */
	protected VerdantiumPropertiesEditor makePropertiesEditor() {
		VerdantiumHelpPropertyEditor MyEdit =
			new VerdantiumHelpPropertyEditor(this);
		return (MyEdit);
	}

	/**
	 * Shows the properties editor for the component.
	 * @param e The event for showing the editor.
	 */
	protected void showPropertiesEditor(EtherEvent e) {
		VerdantiumPropertiesEditor MyEdit = makePropertiesEditor();
		ProgramDirector.showPropertyEditor(
			MyEdit,
			getGUI(),
			"Verdantium Help Property Editor");
	}

	/**
	* Sets the background color of the component and whether it is opaque.
	* @param inC The background color for the component.
	* @param opaque whether the component is opaque.
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
	* Gets whether the component can only be edited in designer mode.
	* @return Whether the component can only be edited in designer mode.
	*/
	public boolean isOnlyDesignerEdits() {
		return (onlyDesignerEdits.isOnlyDesignerEdits());
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
	* @param e The listener to be added.
	*/
	public void addPropertyChangeListener(PropertyChangeListener e) {
		propL.addPropertyChangeListener(e);
	}

	/**
	* Removes a property change listener from the component.
	* @param e The listener to be removed.
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
	* Handles a mouse-press event by showing the property editor of the component.
	* @param e The input event.
	*/
	public void mousePressed(MouseEvent e) {
		try {
			if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
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
	* Handles a program director event on the component.
	* @param e The input event.
	* @return The generated component.
	*/
	protected Object handleProgramDirectorEvent(ProgramDirectorEvent e)
		throws Throwable {
		return (handleProgramDirectorFloatEvent(e));
	}

	/**
	* Handles a program director event to float a component over the text as an embedded frame.
	* @param e The input event.
	* @return The generated component.
	*/
	protected Object handleProgramDirectorFloatEvent(ProgramDirectorEvent e)
		throws Throwable {
		UndoManager mgr =
			UndoManager.createInstanceUndoManager(
				jundo.runtime.Runtime.getInitialMilieu());
		ContainerAppInternalFrame MyFr =
			new ContainerAppInternalFrame(mgr, myDesk.getMap());
		if (e.getProgramName() != null)
			MyFr.setTitle(e.getProgramName());
		else
			MyFr.setTitle("Embedded Component");
		myDesk.add(MyFr, JLayeredPane.PALETTE_LAYER);
		VerdantiumComponent in =
			ProgramDirector.showComponent(e, MyFr.getContentPane());

		MyFr.setComponent(in);
		MyFr.packToPreferred();
		MyFr.show();
		if (EditorControl.getEditorMode() == EditorControl.EditMode)
			EditorControl.setEditorMode(EditorControl.ResizeMode);
		myDesk.repaint();
		ProgramDirector.setPointLocation(in, e.getClickPoint());
		return (in);
	}

	/**
	* Handles a program director event to insert a component directly into the text.
	* @param e The input event.
	* @return The generated component.
	*/
	protected Object handleProgramDirectorInsertEvent(ProgramDirectorEvent e)
		throws Throwable {
		VerdantiumHelpInsertPanel MyFr = new VerdantiumHelpInsertPanel();
		MyFr.setMinimumSize(new Dimension(20, 20));

		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setComponent(attr, MyFr);
		int p = myEdit.getCaretPosition();

		try {
			(myEdit.getDocument()).insertString(p, " ", attr);
		} catch (Exception ex) {
			throw (new WrapRuntimeException("Help Failed", ex));
		}

		VerdantiumComponent in = ProgramDirector.showComponent(e, MyFr);
		MyFr.setSize(MyFr.getPreferredSize());

		Component tmp = MyFr;
		while (!(tmp instanceof Window)) {
			tmp.invalidate();
			if (tmp instanceof JComponent)
				 ((JComponent) tmp).revalidate();
			else
				tmp.validate();
			tmp = tmp.getParent();
		}

		MyFr.setComponent(in);
		MyFr.invalidate();
		in.getGUI().invalidate();
		myEdit.invalidate();
		MyFr.revalidate();
		in.getGUI().revalidate();
		myEdit.revalidate();
		myEdit.repaint();

		return (in);
	}

	/**
	* Inserts a VerdantiumComponent into the text.
	* @param MyC The component to insert.
	*/
	protected void insertComponent(VerdantiumComponent MyC) {
		VerdantiumHelpInsertPanel MyFr = new VerdantiumHelpInsertPanel();
		MyFr.setMinimumSize(new Dimension(20, 20));

		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setComponent(attr, MyFr);
		int p = myEdit.getCaretPosition();

		try {
			(myEdit.getDocument()).insertString(p, " ", attr);
		} catch (Exception e) {
			throw (new WrapRuntimeException("Help Failed", e));
		}

		ProgramDirector.showComponent(MyC, MyFr, "Embedded Component");
		MyFr.setSize(MyFr.getPreferredSize());

		Component tmp = MyFr;
		while (!(tmp instanceof Window)) {
			tmp.invalidate();
			if (tmp instanceof JComponent)
				 ((JComponent) tmp).revalidate();
			else
				tmp.validate();
			tmp = tmp.getParent();
		}

		MyFr.setComponent(MyC);
		MyFr.invalidate();
		MyC.getGUI().invalidate();
		myEdit.invalidate();
		MyFr.revalidate();
		MyC.getGUI().revalidate();
		myEdit.revalidate();
		myEdit.repaint();
	}

	/**
	* Sets the border of the component using the reflection API.  CName is the class name of
	* the border, types contains the parameter types for the constructor of the border, and
	* params contains the parameters for the constructor of the border.
	* @param CName The name of the border class.
	* @param types The parameter types of the constructor for the border.
	* @param params The parameters of the constructor for the border.
	*/
	protected void setBorderObject(
		String CName,
		Class<?>[] types,
		Object[] params)
		throws ResourceNotFoundException {
		try {
			Border myBorder =
				BorderPropertyEditor.createBorder(CName, types, params);

			borderClass = CName;
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
	* Handles the throwing of an error or exception.  Note: this should only get
	* called if there is a bug in the code.
	* @param in The error or exception to handle.
	*/
	public void handleThrow(Throwable in) {
		VerdantiumUtils.handleThrow(in, this, null);
	}

	/**
	* Provides an interface to alter the width of the document page.
	* @param in The width of the document page.
	*/
	public void alterPageWidth(int in) {
	}

	/**
	* Returns whether the component is a scrolling component.
	*/
	protected boolean isScrolling() {
		return (false);
	}

	/**
	* Provides support for firing property change events.
	*/
	private PropertyChangeSupport propL = null;
	
}


/**
* Desktop pane for the {@link VerdantiumHelp} that contains the embedded component frames.
* 
* @author Thorn Green
*/
class VerdantiumHelpDesktopPane extends ContainerAppDesktopPane {
	
	/**
	* The text pane of the VerdantiumHelp.
	*/
	private JComponent backPane = null;

	/**
	* Constructor for the desktop pane.
	*/
	public VerdantiumHelpDesktopPane(
		UndoManager _mgr,
		VerdantiumHelp inp,
		JComponent in) {
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


