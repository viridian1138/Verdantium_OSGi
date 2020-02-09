package verdantium.demo.simpleform;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;

import meta.DataFormatException;
import meta.VersionBuffer;
import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.TransVersionBuffer;
import verdantium.TransVersionBufferFlavor;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumUtils;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.demo.simpleform.help.SimpleFormHelp;
import verdantium.utils.VerdantiumDragUtils;
import verdantium.utils.VerdantiumDropUtils;

//$$strtCprt
/*
 Simple Form demo program by Thorn Green
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
* Demonstrates how to use Verdantium to make a simple forms-based component with
* buttons and pull-down menus.  Note: this class is sometimes hindered
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
*   Also, please observe that the button and menu event handlers in SimpleForm
* fire {@link EtherEvent} requests instead of changing the textfield directly.  This
* allows the textfield changes to be scripted by tools such as the {@link MacroRecorder}
* component.
* <P>
* @author Thorn Green
*/
public class SimpleForm extends Object implements VerdantiumComponent {
	/**
	* The menu pane for the component.
	*/
	private JRootPane topPane = new JRootPane();
	/**
	* The panel underneath TopPane that contains all non-menu widgets.
	*/
	private JPanel formPanel = new JPanel();
	/**
	* The horizontal panel in which the buttons reside.
	*/
	private JPanel buttonPanel = new JPanel();
	/**
	* The "Hello" button of the component.
	*/
	private JButton hello = new JButton("Hello");
	/**
	* The "Goodbye" button of the component.
	*/
	private JButton goodbye = new JButton("Goodbye");
	/**
	* The "Whatever" button of the component.
	*/
	private JButton whatever = new JButton("Whatever");
	/**
	* The component's text field.
	*/
	private JTextField textF = new JTextField();
	/**
	* Ether Event name for changing the textfield.
	*/
	public final String changeTextField = "changeTextField";

	/**
	* Returns the GUI for the component.
    * @return The GUI.
	*/
	public JComponent getGUI() {
		return (topPane);
	}

	/* Note to Programmers about this example code: If you don't need
		pull-dowm menus, you can dispense with topPane and have
		getGUI() return formPanel. */

	/**
	* Constructs the component.
	*/
	public SimpleForm() {
		JMenuBar mBar = new JMenuBar();
		JMenu menu = new JMenu("Choice");
		JMenuItem yes = new JMenuItem("Yes");
		JMenuItem no = new JMenuItem("No");
		JMenuItem maybe = new JMenuItem("Maybe");
		menu.add(yes);
		menu.add(no);
		menu.add(maybe);
		mBar.add(menu);
		JMenu menu2 = new JMenu("Mode");
		JMenuItem mode1 = new JMenuItem("Mode 1");
		JMenuItem mode2 = new JMenuItem("Mode 2");
		JMenuItem mode3 = new JMenuItem("Mode 3");
		menu2.add(mode1);
		menu2.add(mode2);
		menu2.add(mode3);
		mBar.add(menu2);
		topPane.setJMenuBar(mBar);

		hello.setToolTipText("Prints Hello");
		goodbye.setToolTipText("Prints Goodbye");
		whatever.setToolTipText("Prints Whatever");

		topPane.getContentPane().setLayout(new BorderLayout(0, 0));
		topPane.getContentPane().add("Center", formPanel);

		Color bkgndColor = UIManager.getColor("Panel.background");
		formPanel.setBorder(new MatteBorder(5, 0, 0, 0, bkgndColor));

		formPanel.setLayout(new BorderLayout(0, 0));
		formPanel.add("North", textF);
		formPanel.add("South", buttonPanel);
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(hello);
		buttonPanel.add(goodbye);
		buttonPanel.add(whatever);

		VerdantiumDragUtils.setDragUtil(topPane, this);
		VerdantiumDropUtils.setDropUtil(topPane, this, null);
		VerdantiumDragUtils.setDragUtil(formPanel, this);
		VerdantiumDropUtils.setDropUtil(formPanel, this, null);
		VerdantiumDragUtils.setDragUtil(buttonPanel, this);
		VerdantiumDropUtils.setDropUtil(buttonPanel, this, null);

		ActionListener myL = Adapters.createGActionListener(this, "handleButton");
		hello.addActionListener(myL);
		goodbye.addActionListener(myL);
		whatever.addActionListener(myL);

		myL = Adapters.createGActionListener(this, "handleMenu");
		yes.addActionListener(myL);
		no.addActionListener(myL);
		maybe.addActionListener(myL);
		mode1.addActionListener(myL);
		mode2.addActionListener(myL);
		mode3.addActionListener(myL);
	}

	/**
	* Handles all button presses for the component.
    * @param e The event to handle.
	*/
	public void handleButton(ActionEvent e) {
		try {
			JButton myB = (JButton) (e.getSource());
			EtherEvent send = new PropertyEditEtherEvent(this, changeTextField, null, this);
			send.setParameter(myB.getText());
			ProgramDirector.fireEtherEvent(send, null);
		}
		catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles all menu item selections for the component.
    * @param e The event to handle.
	*/
	public void handleMenu(ActionEvent e) {
		try {
			JMenuItem myI = (JMenuItem) (e.getSource());
			EtherEvent send = new PropertyEditEtherEvent(this, changeTextField, null, this);
			send.setParameter(myI.getText());
			ProgramDirector.fireEtherEvent(send, null);
		}
		catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles the destruction of the component.  Currently does nothing.
	*/
	public void handleDestroy() {}

	/**
	* Handles Ether Events on the component.  Handles requests to change the textfield.
    * @param in The event to handle.
    * @param refcon A reference to context data for the event.
    * @return The result of handling the event, or null if there is no result.
	*/
	public Object processObjEtherEvent(EtherEvent in, Object refcon) {
		if (in instanceof PropertyEditEtherEvent) {
			if (in.getEtherID().equals(changeTextField)) {
				String str = (String) (in.getParameter());
				textF.setText(str);
				textF.repaint();
			}
		}

		return (null);
	}

	/**
	* Returns the data flavors that the component can load from persistent storage.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] myF = { new TransVersionBufferFlavor("Simple Form", "Simple Form")};
		return (myF);
	}

	/**
	* Returns the data flavors that the component can save to persistent storage.
    * @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] myF = { new TransVersionBufferFlavor("Simple Form", "Simple Form")};
		return (myF);
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
				TransVersionBuffer myF = (TransVersionBuffer) trans;
				VersionBuffer.chkNul(myF);
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
		TransVersionBuffer myF = new TransVersionBuffer("Simple Form", "Simple Form");
		return (myF);
	}

	/**
	* Handles the throwing of an error or exception.
	* Note: this should only be called if there is a bug.
    * @param in The exception to handle.
	*/
	public void handleThrow(Throwable in) {
		VerdantiumUtils.handleThrow(in, this, null);
	}

	/**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		SimpleFormHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		SimpleForm myComp = new SimpleForm();
		ProgramDirector.showComponent(myComp, "Simple Form", argv, false);
	}
}
