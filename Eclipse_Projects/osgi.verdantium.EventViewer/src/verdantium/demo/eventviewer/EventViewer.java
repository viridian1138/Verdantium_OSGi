package verdantium.demo.eventviewer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;

import meta.DataFormatException;
import meta.FlexString;
import meta.VersionBuffer;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.TransVersionBuffer;
import verdantium.TransVersionBufferFlavor;
import verdantium.VerdantiumComponent;
import verdantium.demo.eventviewer.help.EventViewerHelp;
import verdantium.utils.VerdantiumDragUtils;
import verdantium.utils.VerdantiumDropUtils;
import verdantium.utils.VerticalLayout;

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
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | User interface needed to be updated to support online help.          | Made numerous user interface changes.
*    | 10/19/2001            | Thorn Green (viridian_1138@yahoo.com)           | Expanded window menus for GeoFrame/GeoPad.                           | Added functionality relating to window menus.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
*    | 04/06/2002            | Thorn Green (viridian_1138@yahoo.com)           | Second cut at status window changes.                                 | Created EventViewer component from former StatusWindow class.
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
* This is a simple component that displays the status of all scripting
* events that have been fired.
* <P>
* @author Thorn Green
*/
public class EventViewer extends Object implements VerdantiumComponent, PropertyChangeListener {

	/**
	* The "Display Last" check box.
	*/
	JCheckBox dispLastCheck = null;

	/**
	* The multiple status-line list.
	*/
	JList<String> myList = new JList<String>();

	/**
	* The label for a single status-line.
	*/
	JLabel myLab = new JLabel(" ");

	/**
	* The scrolling pane for the multiple status-line list.
	*/
	JScrollPane scp = null;

	/**
	* The panel containing the status elements.
	*/
	JPanel chgPanel = null;

	/**
	* The content panel of the component.
	*/
	JPanel contentPane = new JPanel();

	/**
	* Constructs the Event Viewer component.
	*/
	public EventViewer() {
		contentPane.setLayout(new BorderLayout(5, 5));
		JPanel pz = new JPanel();
		pz.setLayout(new VerticalLayout());
		contentPane.add("Center", pz);

		JPanel pa = new JPanel();
		contentPane.add("North", pa);
		pa.setLayout(new FlowLayout());
		dispLastCheck = new JCheckBox("Display Last", true);
		pa.add(dispLastCheck);

		myList.setModel(new DefaultListModel<String>());
		scp =
			new JScrollPane(
				myList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		chgPanel = new JPanel();
		pz.add("any", chgPanel);

		chgPanel.setLayout(new BorderLayout(0, 0));
		JTabbedPane MyPane = new JTabbedPane();
		chgPanel.add("Center", MyPane);
		MyPane.addTab("Single Line", myLab);
		MyPane.addTab("Multiple Lines", scp);

		VerdantiumDragUtils.setDragUtil(myLab, this);
		VerdantiumDropUtils.setDropUtil(myLab, this, null);
		VerdantiumDragUtils.setDragUtil(myList, this);
		VerdantiumDropUtils.setDropUtil(myList, this, null);

		ProgramDirector.addClassPropertyChangeListener(this);

	}

	/**
	 * Returns the GUI of the component.
	 * @return The GUI of the component.
	 */
	public JComponent getGUI() {
		return (contentPane);
	}

	/**
	* Handles a request to change to a single status line.
	* @param e The input event.
	*/
	public void handleSingleLineEvent(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			chgPanel.add("this", myLab);
		}
	}

	/**
	* Handles a request to change to a list of multiple status lines.
	* @param e The input event.
	*/
	public void handleMultipleLineEvent(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			chgPanel.add("this", scp);

			if (dispLastCheck.getModel().isSelected()) {
				myList.ensureIndexIsVisible(((DefaultListModel<String>) (myList.getModel())).getSize() - 1);
			}
		}
	}

	/**
	* Handles a property change event indicating the addition of a status line.
	* @param e The input event.
	*/
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getPropertyName() == ProgramDirector.EtherEventStart) {
			Object[] parm = (Object[]) (e.getNewValue());
			EtherEvent in = (EtherEvent) (parm[0]);
			Object reply = parm[1];
			addedStatusLine(
				new FlexString("Started Sending Event " + (in.getEtherID()) + " To Target " + (in.getTarget())));
		}

		if (e.getPropertyName() == ProgramDirector.EtherEventEnd) {
			Object[] parm = (Object[]) (e.getNewValue());
			EtherEvent in = (EtherEvent) (parm[0]);
			Object reply = parm[1];
			if (reply == null) {
				addedStatusLine(
					new FlexString(
						"Event "
							+ (in.getEtherID())
							+ " Sent To Target "
							+ (in.getTarget())
							+ " Terminated Normally."));
			}
			else {
				addedStatusLine(
					new FlexString(
						"Event "
							+ (in.getEtherID())
							+ " Sent To Target "
							+ (in.getTarget())
							+ " Terminated Normally With Reply : "
							+ reply));
			}
		}

		if (e.getPropertyName() == ProgramDirector.EtherEventErr) {
			Object[] parm = (Object[]) (e.getNewValue());
			EtherEvent in = (EtherEvent) (parm[0]);
			Throwable ex = (Throwable) (parm[3]);
			addedStatusLine(new FlexString("Event " + (in.getEtherID()) + " Terminated With Error : " + ex));
		}

	}

	/**
	* Handles the addition of a new status line.
	* @param newLine The new line to be added.
	*/
	protected void addedStatusLine(FlexString newLine) {
		String MyStr = newLine.exportString();
		myLab.setText(MyStr);
		((DefaultListModel<String>) (myList.getModel())).addElement(MyStr);

		if (dispLastCheck.getModel().isSelected()) {
			myList.ensureIndexIsVisible(((DefaultListModel<String>) (myList.getModel())).getSize() - 1);
		}
	}

	/**
	* Handles the destruction of the component.
	*/
	public void handleDestroy() {
		ProgramDirector.removeClassPropertyChangeListener(this);
	}

	/**
	 * Handles Ether Events to alter the properties of the component.  Currently does nothing.
	 * @param in The event to handle.
	 * @param refcon A reference to context data for the event.
	 * @return The result of handling the event, or null if there is no result.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon) {
		return (null);
	}

	/**
	* Returns the data flavors that the component can load from persistent storage.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF = { new TransVersionBufferFlavor("Event Viewer", "Event Viewer")};
		return (MyF);
	}

	/**
	* Returns the data flavors that the component can save to persistent storage.
    * @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF = { new TransVersionBufferFlavor("Event Viewer", "Event Viewer")};
		return (MyF);
	}

	/**
	* Loads data for the component from persistent storage.
    * @param flavor The flavor in which to load the data.
    * @param trans The transferable from which to load the data.
	*/
	public void loadPersistentData(DataFlavor flavor, Transferable trans) throws IOException {
		if (trans == null) {}
		else {
			try {
				TransVersionBuffer MyF = (TransVersionBuffer) trans;
				VersionBuffer.chkNul(MyF);
			}
			catch (ClassCastException e) {
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
		TransVersionBuffer MyF = new TransVersionBuffer("Event Viewer", "Event Viewer");
		return (MyF);
	}

	/**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		EventViewerHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		EventViewer MyComp = new EventViewer();
		ProgramDirector.showComponent(MyComp, "Event Viewer", argv, false);
	}

	
}

