package verdantium.core;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.Beans;
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
import verdantium.core.help.DesignerControlHelp;
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
* Designer control controls the designer state which is equivalent to the JavaBeans
* "Design Time" state.  This is used to control whether one can edit certain parts
* of a document.  This allows documents that are mutable for the "designer" but
* immutable for the "user".  For instance, a user should not be able to edit an
* online help page just because it's a document.
* 
* @author Thorn Green
*/
public class DesignerControl
	extends Object
	implements VerdantiumComponent, PropertyChangeListener {
	
	/**
	* The designer control fires a property change event of this type when
	* the designer mode is altered.
	*/
	public static final String DesignerCntlChange = "DesignerCntlChange";

	/**
	 * Whether the component is updating.
	 */
	private boolean updating = false;

	/**
	* Returns true iff. in "designer mode".
	* @return True iff. in "designer mode".
	*/
	public static boolean isDesignTime() {
		return (Beans.isDesignTime());
	}

	/**
	* Handles class Ether Events.
	*/
	public static Object processClassEtherEvent(EtherEvent in, Object refcon) {
		if (in instanceof StandardEtherEvent) {
			if (in.getEtherID().equals(StandardEtherEvent.setDesignTime)) {
				setDesignTime(((Boolean) (in.getParameter())).booleanValue());
			}
		}

		return (null);
	}

	/**
	* Sets whether the system is in "designer mode".
	* @param in Whether the system is in "designer mode".
	*/
	public static void setDesignTime(boolean in) {
		boolean tmp = Beans.isDesignTime();
		Beans.setDesignTime(in);
		propL.firePropertyChange(
			DesignerCntlChange,
			new Boolean(tmp),
			new Boolean(in));
		EditorControl.updateListeners();
	}

	/**
	 * Radio button indicating that the system is in "designer mode"
	 */
	private JRadioButton designButton =
		new JRadioButton("Designer Mode", Beans.isDesignTime());
	
	/**
	 * Radio button indicating that the system is in "user mode"
	 */
	private JRadioButton userButton =
		new JRadioButton("User Mode", !(Beans.isDesignTime()));

	/**
	* The property change support used to fire property change events.
	*/
	private static PropertyChangeSupport propL =
		new PropertyChangeSupport("Designer Control");

	/**
	 * Constructor.
	 */
	public DesignerControl() {
		myPanel.setLayout(new VerticalLayout(1));
		myPanel.add("any", designButton);
		myPanel.add("any", userButton);
		ButtonGroup MyGrp = new ButtonGroup();
		MyGrp.add(designButton);
		MyGrp.add(userButton);

		ItemListener item = Adapters.createGItemListener(this, "handleButton");
		designButton.addItemListener(item);
		userButton.addItemListener(item);

		DesignerControl.addPropertyChangeListener(this);
		VerdantiumDragUtils.setDragUtil(myPanel, this);
		VerdantiumDropUtils.setDropUtil(myPanel, this, null);
		VerdantiumDragUtils.setDragUtil(designButton, this);
		VerdantiumDropUtils.setDropUtil(designButton, this, null);
		VerdantiumDragUtils.setDragUtil(userButton, this);
		VerdantiumDropUtils.setDropUtil(userButton, this, null);
	}

	/**
	* Handles a change to the button state on the designer control GUI.
	* @param evt The input event.
	*/
	public void handleButton(ItemEvent evt) {
		if (!updating) {
			try {
				if (((JRadioButton) (evt.getSource()))
					.getModel()
					.isSelected()) {
					boolean mode = Beans.isDesignTime();
					updating = true;
					if (designButton.getModel().isSelected())
						mode = true;
					if (userButton.getModel().isSelected())
						mode = false;

					EtherEvent send =
						new StandardEtherEvent(
							this,
							StandardEtherEvent.setDesignTime,
							new Boolean(mode),
							"Designer Control");
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
			if (Beans.isDesignTime()) {
				designButton.getModel().setSelected(true);
			} else {
				userButton.getModel().setSelected(true);
			}

			updating = false;
			myPanel.repaint();
		}
	}

	/**
	* Adds a property change listener to the class.
	* @param in The listener to add.
	*/
	public static void addPropertyChangeListener(PropertyChangeListener in) {
		propL.addPropertyChangeListener(in);
	}

	/**
	* Removes a property change listener from the class.
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
		DesignerControl.removePropertyChangeListener(this);
	}

	/**
	* Returns the data flavors that the component can load from persistent storage.
	* DesignerControl supports its own proprietary format only.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{
				 new TransVersionBufferFlavor(
					"Designer Control",
					"Designer Control")};
		return (MyF);
	}

	/**
	* Returns the data flavors that the component can save to persistent storage.
	* DesignerControl supports its own proprietary format only.
    * @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{
				 new TransVersionBufferFlavor(
					"Designer Control",
					"Designer Control")};
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
			new TransVersionBuffer("Designer Control", "Designer Control");
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
		DesignerControlHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		DesignerControl myComp = new DesignerControl();
		ProgramDirector.showComponent(myComp, "Designer Control", argv, false);
	}
	
}

