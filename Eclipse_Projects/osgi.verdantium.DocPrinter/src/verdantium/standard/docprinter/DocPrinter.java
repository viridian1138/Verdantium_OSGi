package verdantium.standard.docprinter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;

import meta.DataFormatException;
import meta.VersionBuffer;
import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.EtherEventPropertySource;
import verdantium.ProgramDirector;
import verdantium.ProgramDirectorEvent;
import verdantium.ProgramDirectorPropertyEditor;
import verdantium.StandardEtherEvent;
import verdantium.TransVersionBuffer;
import verdantium.TransVersionBufferFlavor;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.VerdantiumUtils;
import verdantium.standard.ScrollingInternalDesktop;
import verdantium.standard.docprinter.help.DocPrinterHelp;
import verdantium.utils.VerdantiumDragUtils;
import verdantium.utils.VerdantiumDropUtils;

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
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/29/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for multiple PLAFs.                                          | Made code changes to support multiple PLAFs.
*    | 10/05/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for command-line arguments.                                  | Added support for command-line arguments.
*    | 12/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Third-Cut at Error Handling.                                         | Third-Cut at Error Handling.
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
* This is a component that prints other documents or components under program
* control.  This demonstrates practical functionality for a number of potential
* applications.  For instance, it would not be hard to modify this component to
* print a predefined series of documents in an automated fashion.  Each printed
* document is temporarily embedded in the desktop pane at the top of the component.
* After printing ends, the save dialog for the component (if needed) is displayed
* in the desktop pane.
* <P>
* @author Thorn Green
*/
public class DocPrinter extends Object implements VerdantiumComponent, EtherEventPropertySource {
	
	/**
	* Support for property change events on the class.
	*/
	private static PropertyChangeSupport propL = null;
	
	/**
	* The top-level GUI panel.
	*/
	private JPanel formPanel = new JPanel();
	
	/**
	* The horizontal panel in which the buttons reside.
	*/
	private JPanel buttonPanel = new JPanel();
	
	/**
	* The "print" button of the component.
	*/
	private JButton print = new JButton("print...");
	
	/**
	* The "help" button of the component.
	*/
	private JButton help = new JButton("help");
	
	/**
	* A scrolling internal desktop used to support the printing.
	*/
	private ScrollingInternalDesktop myDesk = new ScrollingInternalDesktop();

	/**
	 * Returns the GUI of the component.
	 * @return The GUI of the component.
	 */
	public JComponent getGUI() {
		return (formPanel);
	}

	/**
	* Constructs the component.
	*/
	public DocPrinter() {
		propL = new PropertyChangeSupport(this);
		print.setToolTipText("Prints A Document");
		help.setToolTipText("Displays help");

		Color BkgndColor = UIManager.getColor("Panel.background");
		formPanel.setBorder(new MatteBorder(5, 0, 0, 0, BkgndColor));

		formPanel.setLayout(new BorderLayout(0, 0));
		formPanel.add("Center", myDesk.getGUI());
		formPanel.add("South", buttonPanel);
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(print);
		buttonPanel.add(help);

		formPanel.setPreferredSize(new Dimension(300, 200));
		VerdantiumDragUtils.setDragUtil(buttonPanel, this);
		VerdantiumDropUtils.setDropUtil(buttonPanel, this, this);

		ActionListener MyL = Adapters.createGActionListener(this, "handlePrint");
		print.addActionListener(MyL);
		MyL = Adapters.createGActionListener(this, "handleHelp");
		help.addActionListener(MyL);
	}

	/**
	* Handles a button press event to invoke printing.
	* @param e The input event.
	*/
	public void handlePrint(ActionEvent e) {
		try {
			EtherEvent send = new StandardEtherEvent(this, StandardEtherEvent.showPropertiesEditor, null, this);
			ProgramDirector.fireEtherEvent(send, null);
		}
		catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles a button press event to display help for the component.
	* @param e The input event.
	*/
	public void handleHelp(ActionEvent e) {
		displayVerdantiumHelp(this);
	}

	/**
	* Handles the destruction of the component.  Currently does nothing.
	*/
	public void handleDestroy() {
		propL.firePropertyChange(ProgramDirector.propertyDestruction, null, null);
	}

	/**
	* Handles Ether Events on the component.  Handles requests for document printing.
	* @param in The event to handle.
	* @param refcon A reference to context data for the event.
	* @return The result of handling the event, or null if there is no result.
	*/
	public Object processObjEtherEvent(EtherEvent in, Object refcon) throws Throwable {
		if (in instanceof StandardEtherEvent) {
			if (in.getEtherID().equals(StandardEtherEvent.makePropertiesEditor))
				return (makePropertiesEditor());

			if (in.getEtherID().equals(StandardEtherEvent.showPropertiesEditor))
				showPropertiesEditor();
		}

		if (in instanceof ProgramDirectorEvent) {
			if (in.getEtherID().equals(ProgramDirectorEvent.isProgramDirectorEventSupported)) {
				return (new Boolean(true));
			}
			else {
				return (handleProgramDirectorEvent((ProgramDirectorEvent) in, refcon));
			}

		}

		return (null);
	}

	/**
	* Prints a component in response to a ProgramDirectorEvent.
	* @param e The input event.
	* @return null.
	*/
	protected Object handleProgramDirectorEvent(ProgramDirectorEvent e, Object refcon) throws Throwable {
		Object reply2 = myDesk.processObjEtherEvent(e, refcon);
		VerdantiumComponent target = (VerdantiumComponent) (reply2);
		EtherEvent send = new StandardEtherEvent(this, StandardEtherEvent.objPrintEvent, null, target);
		ProgramDirector.fireEtherEvent(send, null);
		send = new StandardEtherEvent(this, StandardEtherEvent.objQuitEvent, null, target);
		ProgramDirector.fireEtherEvent(send, null);
		return (null);
	}

	/**
	 * Creates the properties editor for the component.
	 * @return The created property editor.
	 */
	public VerdantiumPropertiesEditor makePropertiesEditor() {
		ProgramDirectorPropertyEditor MyEdit = new ProgramDirectorPropertyEditor(this, true, false, null);
		return (MyEdit);
	}

	/**
	 * Shows the properties editor for the component.
	 * @param e The event for showing the editor.
	 */
	public void showPropertiesEditor() {
		VerdantiumPropertiesEditor MyEdit = makePropertiesEditor();
		ProgramDirector.showPropertyEditor(MyEdit, getGUI(), "Doc Printer print Editor");
	}

	/**
	* Adds a property change listener.
	* @param e The listener to add.
	*/
	public void addPropertyChangeListener(PropertyChangeListener e) {
		propL.addPropertyChangeListener(e);
	}

	/**
	* Removes a property change listener.
	* @param e The listener to remove.
	*/
	public void removePropertyChangeListener(PropertyChangeListener e) {
		propL.removePropertyChangeListener(e);
	}

	/**
	* Returns the data flavors that the component can load from persistent storage.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF = { new TransVersionBufferFlavor("Doc Printer", "Doc Printer")};
		return (MyF);
	}

	/**
	* Returns the data flavors that the component can save to persistent storage.
    * @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF = { new TransVersionBufferFlavor("Doc Printer", "Doc Printer")};
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
		TransVersionBuffer MyF = new TransVersionBuffer("Doc Printer", "Doc Printer");
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
		DocPrinterHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		DocPrinter MyComp = new DocPrinter();
		ProgramDirector.showComponent(MyComp, "Doc Printer", argv, true);
	}
	
}

