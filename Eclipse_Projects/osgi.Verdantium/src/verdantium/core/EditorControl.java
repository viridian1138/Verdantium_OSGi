package verdantium.core;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import meta.DataFormatException;
import meta.VersionBuffer;
import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.StandardEtherEvent;
import verdantium.TransVersionBuffer;
import verdantium.TransVersionBufferFlavor;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumUtils;
import verdantium.core.help.EditorControlHelp;
import verdantium.utils.VerdantiumDragUtils;
import verdantium.utils.VerdantiumDropUtils;
import verdantium.utils.VerticalLayout;

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
* EditorControl contains storage for the editor state and a component that edits it.
* The editor mode controls how embedded frames are displayed in all documents.  In EditMode,
* no border is displayed around the embedded component, and the conponent can not be moved
* or resized.  This is the mode one would use when printing or "presenting" the compound
* document.  ResizeMode gives the component a thin border.  A component in ResizeMode can be
* resized, but not moved.  MoveMode gives the component a full window border.  A component in
* MoveMode can be either moved or resized.
*
* EditorControl may rely on {@link DesignerControl} and the "OnlyDesignerEdits" property to
* deteremine what editing mode a component should be in.  A component in UserMode that only
* the designer can edit should not be moved or resized.  Further, it should not have a resize
* border since it's information that the designer is "presenting" to the user.
* EditorControl takes outside information into account to enforce this.
* 
* @author Thorn Green
*/
public class EditorControl
	extends Object
	implements VerdantiumComponent, PropertyChangeListener {
	
	/**
	 * Constant indicating that the editor mode is in resize mode.
	 */
	public static final int ResizeMode = 1;
	
	/**
	 * Constant indicating that the editor mode is in move mode.
	 */
	public static final int MoveMode = 2;
	
	/**
	 * Constant indicating that the editor mode is in edit mode.
	 */
	public static final int EditMode = 3;

	/**
	* The editor control fires a property change event of this type when
	* the editor mode is altered.
	*/
	public static final String EditCntlChange = "EditCntlChange";

	/**
	 * The editor mode.  Contains one of ResizeMode, MoveMode, or EditMode.
	 */
	private static int editorMode = ResizeMode;
	
	/**
	 * Indicates whether the component is updating.
	 */
	private boolean updating = false;

	/**
	* Gets the editor mode.
	* @return The editor mode.  Contains one of ResizeMode, MoveMode, or EditMode.
	*/
	public static int getEditorMode() {
		return (editorMode);
	}

	/**
	* Handles an EtherEvent on the EditorControl class.
	* @param in The event to handle.
	* @param refcon A reference to context data for the event.
	* @return The result of handling the event, or null if there is no result.
	*/
	public static Object processClassEtherEvent(EtherEvent in, Object refcon) {
		if (in instanceof StandardEtherEvent) {
			if (in.getEtherID().equals(StandardEtherEvent.setEditorMode)) {
				setEditorMode(((Integer) (in.getParameter())).intValue());
			}
		}

		return (null);
	}

	/**
	* Returns the current EditorMode given whether the designer edits.
	* @return The current EditorMode.  Contains one of ResizeMode, MoveMode, or EditMode.
	*/
	public static int getEditorMode(boolean OnlyDesignerEdits) {
		if (OnlyDesignerEdits && !(DesignerControl.isDesignTime()))
			return (EditMode);
		else
			return (editorMode);
	}

	/**
	* Updates objects listening for a property change.
	*/
	public static void updateListeners() {
		propL.firePropertyChange(EditCntlChange, null, new Integer(editorMode));
	}

	/**
	* Sets the editor mode.
	* @param in The editor mode.  Contains one of ResizeMode, MoveMode, or EditMode.
	*/
	public static void setEditorMode(int in) {
		int tmp = editorMode;
		editorMode = in;
		propL.firePropertyChange(
			EditCntlChange,
			new Integer(tmp),
			new Integer(in));
	}

	/**
	 * Radio button indicating that the system is in "resize mode"
	 */
	private JRadioButton resizeButton =
		new JRadioButton("Resize Mode", editorMode == ResizeMode);
	
	/**
	 * Radio button indicating that the system is in "move mode"
	 */
	private JRadioButton moveButton =
		new JRadioButton("Move Mode", editorMode == MoveMode);
	
	/**
	 * Radio button indicating that the system is in "edit mode"
	 */
	private JRadioButton editButton =
		new JRadioButton("Edit Mode", editorMode == EditMode);

	/**
	* The property change support used to fire property change events.
	*/
	private static PropertyChangeSupport propL =
		new PropertyChangeSupport("Editor Control");

	/**
	 * Constructor.
	 */
	public EditorControl() {
		myPanel.setLayout(new VerticalLayout(1));
		myPanel.add("any", editButton);
		myPanel.add("any", moveButton);
		myPanel.add("any", resizeButton);
		ButtonGroup MyGrp = new ButtonGroup();
		MyGrp.add(editButton);
		MyGrp.add(moveButton);
		MyGrp.add(resizeButton);

		ItemListener item = Adapters.createGItemListener(this, "handleButton");
		resizeButton.addItemListener(item);
		moveButton.addItemListener(item);
		editButton.addItemListener(item);

		EditorControl.addPropertyChangeListener(this);
		VerdantiumDragUtils.setDragUtil(myPanel, this);
		VerdantiumDropUtils.setDropUtil(myPanel, this, null);
		VerdantiumDragUtils.setDragUtil(editButton, this);
		VerdantiumDropUtils.setDropUtil(editButton, this, null);
		VerdantiumDragUtils.setDragUtil(moveButton, this);
		VerdantiumDropUtils.setDropUtil(moveButton, this, null);
		VerdantiumDragUtils.setDragUtil(resizeButton, this);
		VerdantiumDropUtils.setDropUtil(resizeButton, this, null);
	}

	/**
	* Handles a button press on the editor control component.
	* @param evt The input event.
	*/
	public void handleButton(ItemEvent evt) {
		if (!updating) {
			try {
				if (((JRadioButton) (evt.getSource()))
					.getModel()
					.isSelected()) {
					int mode = editorMode;
					updating = true;
					if (resizeButton.getModel().isSelected())
						mode = ResizeMode;
					if (moveButton.getModel().isSelected())
						mode = MoveMode;
					if (editButton.getModel().isSelected())
						mode = EditMode;

					EtherEvent send =
						new StandardEtherEvent(
							this,
							StandardEtherEvent.setEditorMode,
							new Integer(mode),
							"Editor Control");
					ProgramDirector.fireEtherEvent(send, null);
				}
			} catch (Throwable ex) {
				handleThrow(ex);
			} finally {
				updating = false;
			}
		}
	}

	/**
	* Handles a property change event.
	* @param evt The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		if (!updating) {
			updating = true;
			switch (editorMode) {
				case ResizeMode :
					resizeButton.getModel().setSelected(true);
					break;

				case MoveMode :
					moveButton.getModel().setSelected(true);
					break;

				case EditMode :
					editButton.getModel().setSelected(true);
					break;

			}

			updating = false;
			myPanel.repaint();
		}
	}

	/**
	* Adds a property change listener to the EditorControl class.
	* @param in The listener to add.
	*/
	public static void addPropertyChangeListener(PropertyChangeListener in) {
		propL.addPropertyChangeListener(in);
	}

	/**
	* Removes a property change listener from the EditorControl class.
	* @param in The listener to remove.
	*/
	public static void removePropertyChangeListener(PropertyChangeListener in) {
		propL.removePropertyChangeListener(in);
	}

	/**
	 * Panel containing the GUI for the component.
	 */
	private JPanel myPanel = new JPanel();

	/**
	* Gets the GUI for the component.
	* @return The GUI for the component.
	*/
	public JComponent getGUI() {
		return (myPanel);
	}

	/**
	 * Handles Ether Events to alter the properties of the component.
	 * @param in The event to handle.
	 * @param refcon A reference to context data for the event.
	 * @return The result of handling the event, or null if there is no result.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon) {
		return (null);
	}

	/**
	* Handles the destruction of the component.
	*/
	public void handleDestroy() {
		EditorControl.removePropertyChangeListener(this);
	}

	/**
	* Returns the data flavors that the component can load from persistent storage.
	* EditorControl supports its own proprietary format only.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{ new TransVersionBufferFlavor("Editor Control", "Editor Control")};
		return (MyF);
	}

	/**
	* Returns the data flavors that the component can save to persistent storage.
	* EditorControl supports its own proprietary format only.
    * @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{ new TransVersionBufferFlavor("Editor Control", "Editor Control")};
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
			new TransVersionBuffer("Editor Control", "Editor Control");
		return (MyF);
	}

	/**
	* Handles the throwing of an error or exception.
	* Note: this one should only get called if there is some kind of bug.
	* @param in The input error or exception.
	*/
	public void handleThrow(Throwable in) {
		VerdantiumUtils.handleThrow(in, this, null);
	}

	/**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		EditorControlHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		EditorControl MyComp = new EditorControl();
		ProgramDirector.showComponent(MyComp, "Editor Control", argv, false);
	}
	
}

