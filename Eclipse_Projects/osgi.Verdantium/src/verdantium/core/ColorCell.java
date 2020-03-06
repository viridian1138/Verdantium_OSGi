package verdantium.core;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import meta.DataFormatException;
import meta.VersionBuffer;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.PropertyChangeSource;
import verdantium.StandardEtherEvent;
import verdantium.TransVersionBuffer;
import verdantium.TransVersionBufferFlavor;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.VerdantiumUtils;
import verdantium.core.help.ColorCellHelp;
import verdantium.undo.UndoManager;
import verdantium.utils.VerdantiumDragUtils;
import verdantium.utils.VerdantiumDropUtils;
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
* A color-editor component.  Often used as a sub-component of some other component or property
* editor.  For instance, the "current" color in the palette of a paint program would be a good
* candidate for a ColorCell.  ColorCell can also be used as an independent component.
* 
* @author Thorn Green
*/
public class ColorCell
	extends Object
	implements
		VerdantiumComponent,
		PropertyChangeSource,
		MouseListener,
		OnlyDesignerEditListener,
		OnlyDesignerEditsChangeHandler {
	
	/**
	 * The panel that encloses the GUI of the component.
	 */
	private JPanel myPanel = new JPanel();

	/**
	 * Returns the GUI of the component.
	 * @return The GUI of the component.
	 */
	public JComponent getGUI() {
		return (myPanel);
	}

	/**
	 * Undoable reference to whether only the designer can edit the state of the ColorCell.
	 */
	private OnlyDesignerEdits onlyDesignerEdits = null;

	/**
	* Ether Event type used to set the cell color.
	*/
	public final static String setCellColor = "setCellColor";

	/**
	* Handles the destruction of the component.
	*/
	public void handleDestroy() {
		propL.firePropertyChange(ProgramDirector.propertyHide, null, null);
		propL.firePropertyChange(
			ProgramDirector.propertyDestruction,
			null,
			null);
		onlyDesignerEdits.handleDestroy();
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
				showPropertiesEditor();

			if (in.getEtherID().equals(StandardEtherEvent.objUndoableClose)) {
				propL.firePropertyChange(
					ProgramDirector.propertyHide,
					null,
					null);
			}
		}

		if (in instanceof PropertyEditEtherEvent) {
			if (in.getEtherID().equals(setCellColor)) {
				Color InCol = (Color) (in.getParameter());
				setColor(InCol);
			}

		}

		return (null);
	}

	/**
	* Handles a change in whether the component can be edited in user mode.
	*/
	public void handleOnlyDesignerEditsChange() {
		if (isOnlyDesignerEdits())
			myPanel.setToolTipText(null);
		else
			myPanel.setToolTipText("Right-Click to edit properties");
	}

	/**
	* Creates a property editor for the ColorCell.
	* @return The created property editor.
	*/
	public VerdantiumPropertiesEditor makePropertiesEditor() {
		ColorCellPropertyEditor MyEdit = new ColorCellPropertyEditor(this);
		return (MyEdit);
	}

	/**
	* Shows a property editor for the ColorCell.
	*/
	public void showPropertiesEditor() {
		VerdantiumPropertiesEditor MyEdit = makePropertiesEditor();
		ProgramDirector.showPropertyEditor(
			MyEdit,
			getGUI(),
			"Color Cell Property Editor");
	}

	/**
	 * Constructor.
	 */
	public ColorCell() {
		propL = new PropertyChangeSupport(this);
		onlyDesignerEdits =
			new OnlyDesignerEdits(
				UndoManager.createInstanceUndoManager(
					jundo.runtime.Runtime.getInitialMilieu()));
		myPanel.setMinimumSize(new Dimension(2, 2));
		myPanel.setPreferredSize(new Dimension(100, 100));
		myPanel.setBackground(myColor);
		myPanel.addMouseListener(this);
		myPanel.setToolTipText("Right-Click to change color");
		VerdantiumDragUtils.setDragUtil(myPanel, this);
		VerdantiumDropUtils.setDropUtil(myPanel, this, this);
		onlyDesignerEdits.configureForEtherEvents(this, propL);
	}

	/**
	* Sets an outline border for the color cell.  Most palette color cells in
	* paint programs have some kind of outline around their borders.
	*/
	public void setOutlineBorder() {
		getGUI().setBorder(new EtchedBorder());
		getGUI().revalidate();
		getGUI().repaint();
	}

	/**
	* Handles a mouse-clicked event in the cell.
	* @param e The input event.
	*/
	public void mouseClicked(MouseEvent e) {
	}

	/**
	* Handles a mouse-entered event in the cell.
	* @param e The input event.
	*/
	public void mouseEntered(MouseEvent e) {
	}

	/**
	* Handles a mouse-exited event in the cell.
	* @param e The input event.
	*/
	public void mouseExited(MouseEvent e) {
	}

	/**
	* Handles a mouse-released event in the cell.
	* @param e The input event.
	*/
	public void mouseReleased(MouseEvent e) {
	}

	/**
	* Handles a mouse-pressed event in the cell.
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
	* Gets the current cell color.
	* @return The current cell color.
	*/
	public Color getColor() {
		return (myColor);
	}

	/**
	* Sets the current cell color.
	* @param in The current cell color.
	*/
	public void setColor(Color in) {
		myColor = in;
		myPanel.setBackground(in);
		if (colorL != null) {
			colorL.actionPerformed(null);
		}
		myPanel.repaint();
	}

	/**
	* Returns whether only the designer can edit the cell color.
	* @return Whether only the designer can edit the cell color.
	*/
	public boolean isOnlyDesignerEdits() {
		return (onlyDesignerEdits.isOnlyDesignerEdits());
	}

	/**
	* Adds a property change listener to the cell.
	* @param e The listener to be added.
	*/
	public void addPropertyChangeListener(PropertyChangeListener e) {
		propL.addPropertyChangeListener(e);
	}

	/**
	* Removes a property change listener from the cell.
	* @param e The listener to be removed.
	*/
	public void removePropertyChangeListener(PropertyChangeListener e) {
		propL.removePropertyChangeListener(e);
	}

	/**
	* Adds an action listener that fires on each color change.
	* @param in The listener to be added.
	*/
	public void addColorActionListener(ActionListener in) {
		colorL = AWTEventMulticaster.add(colorL, in);
	}

	/**
	* Removes an action listener.
	* @param in The listener to be removed.
	*/
	public void removeColorActionListener(ActionListener in) {
		colorL = AWTEventMulticaster.remove(colorL, in);
	}

	/**
	* Returns the data flavors that the component can load from persistent storage.  ColorCell supports its own
	* proprietary format only.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{ new TransVersionBufferFlavor("Color Cell", "Color Cell")};
		return (MyF);
	}

	/**
	* Returns the data flavors that the component can save to persistent storage.  ColorCell supports its own
	* proprietary format only.
    * @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{ new TransVersionBufferFlavor("Color Cell", "Color Cell")};
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
			setColor(Color.orange);
			onlyDesignerEdits.setOnlyDesignerEdits(false);
		} else {
			try {
				TransVersionBuffer MyF = (TransVersionBuffer) trans;
				VersionBuffer.chkNul(MyF);
				Color tmpc = (Color) (MyF.getProperty("Color"));
				VersionBuffer.chkNul(tmpc);
				setColor(tmpc);
				onlyDesignerEdits.setOnlyDesignerEdits(
					MyF.getBoolean("OnlyDesignerEdits"));
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
			new TransVersionBuffer("Color Cell", "Color Cell");
		MyF.setProperty("Color", myColor);
		MyF.setBoolean("OnlyDesignerEdits", isOnlyDesignerEdits());
		return (MyF);
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
		ColorCellHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param argv Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		ColorCell MyComp = new ColorCell();
		ProgramDirector.showComponent(MyComp, "Color Cell", argv, false);
	}

	/**
	 * Property change support for component changes.
	 */
	private PropertyChangeSupport propL = null;
	
	/**
	 * Listener that is called upon a color change.
	 */
	private ActionListener colorL = null;

	/**
	 * The color being edited.
	 */
	private Color myColor = Color.orange;
	
}

