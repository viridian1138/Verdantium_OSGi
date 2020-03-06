package verdantium.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.PropertyChangeSource;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumUtils;
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
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Needed a class for recording client macros.                          | Initial creating, starting with code from another class.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/10/2001            | Thorn Green (viridian_1138@yahoo.com)           | Second-Cut at Error Handling.                                        | Second-Cut at Error Handling.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
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
* A property editor for recording client (per-component) macros by capturing and storing relevant events for later playback.
*
* @author Thorn Green
*/
public class ClientMacroRecordingEditor extends PropertyEditAdapter {

	/**
	* Macro-recording object class.
	* 
	* @author Thorn Green
	*/
	protected class MrMacroRecord extends MacroRecordingObject {
		
		/**
		 * Constructor.
		 */
		public MrMacroRecord() {
			super();
		}

		/**
		* Subclass this method to handle the adding of an EtherEvent to a list.
		* @param in The input event.
    	* @param reply The reply generated by executing the event.
	    * @param refcon A reference to context data that the generating code can associate with the event.  See various references to "refcon" in MacOS programming.
		*/
		protected void handleEtherEventFinish(
			EtherEvent in,
			Object reply,
			Object refcon) {
			ClientMacroRecordingEditor.this.printEtherEventToList(
				in,
				reply,
				refcon);
		}

		/**
		* Subclass this method to handle the abnormal termination of an EtherEvent with an error.
		* @param in The input event.
    	* @param reply The reply generated by executing the event.
    	* @param refcon A reference to context data that the generating code can associate with the event.  See various references to "refcon" in MacOS programming.
    	* @param ex Error or exception generated by the attempt to execute the event.
		*/
		protected void handleEtherEventErr(
			EtherEvent in,
			Object reply,
			Object refcon,
			Throwable ex) {
			ClientMacroRecordingEditor.this.printErrToList();
		}

	}

	/**
	* The component on which to edit the macros.
	*/
	transient protected VerdantiumComponent[] target = null;

	/**
	* The list of macros to edit.
	*/
	transient protected JList<String> opList = null;

	/**
	* A pane for scrolling the macro list.
	*/
	transient protected JScrollPane scp = null;

	/**
	* The panel used to display the GUI.
	*/
	transient protected JPanel master = new JPanel();

	/**
	* Button used to run a macro.
	*/
	transient protected JButton runButton = new JButton("Run");

	/**
	* Button used to delete a macro.
	*/
	transient protected JButton deleteButton = new JButton("Delete");

	/**
	* The "OK" button.
	*/
	transient protected JButton okButton = new JButton("OK");

	/**
	* The "Apply" button.
	*/
	transient protected JButton applyButton = new JButton("Apply");

	/**
	* The "Cancel" button.
	*/
	transient protected JButton cancelButton = new JButton("Cancel");

	/**
	* The "Record" button.
	*/
	transient protected JButton recordButton = new JButton("Record");

	/**
	* The "Stop" button.
	*/
	transient protected JButton stopButton = new JButton("Stop");

	/**
	* The "Clear" button.
	*/
	transient protected JButton clearButton = new JButton("Clear");

	/**
	* The text field for the macro name.
	*/
	transient protected JTextField macroNameField = new JTextField();

	/**
	* The Macro Recording Object
	*/
	transient protected MrMacroRecord macroRecorder = new MrMacroRecord();

	/**
	* The default button background color.
	*/
	private Color defaultBkgnd = null;

	/**
	* Gets the GUI for the property editor.
	* @return The GUI of the property editor.
	*/
	public JComponent getGUI() {
		return (master);
	}

	/**
	* Constructs a client macro property editor for a component.  Assumes the component(s)
	* implement PropertyChangeSource.
	* @param in The components on which to edit the macro list.
	*/
	public ClientMacroRecordingEditor(VerdantiumComponent[] in) {
		target = in;
		recordInit();
	}

	/**
	* Constructs a client macro property editor for a component.  Assumes the component
	* implements PropertyChangeSource.
	* @param in The component on which to edit the macro list.
	*/
	public ClientMacroRecordingEditor(VerdantiumComponent in) {
		VerdantiumComponent[] tar = { in };
		target = tar;
		recordInit();
	}

	/**
	 * Initializes the recording GUI.
	 */
	protected void recordInit() {
		((PropertyChangeSource) target[0]).addPropertyChangeListener(this);

		opList = new JList<String>();
		opList.setModel(new DefaultListModel<String>());
		scp =
			new JScrollPane(
				opList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		master.setLayout(new BorderLayout(0, 0));
		JPanel ptop = new JPanel();
		master.add("North", ptop);
		ptop.setLayout(new BorderLayout(0, 0));
		ptop.add("North", new JLabel("Macro Name : "));
		ptop.add("Center", macroNameField);
		ptop.add("South", new JLabel("Macro : "));
		master.add("Center", scp);
		JPanel p2 = new JPanel();
		master.add("South", p2);
		p2.setLayout(new BorderLayout(0, 0));
		JPanel p3 = new JPanel();
		JPanel p4 = new JPanel();
		p2.add("North", p3);
		p2.add("South", p4);
		p3.setLayout(new FlowLayout());
		p4.setLayout(new FlowLayout());
		p3.add(recordButton);
		p3.add(stopButton);
		p3.add(clearButton);
		p4.add(okButton);
		p4.add(applyButton);
		p4.add(cancelButton);

		defaultBkgnd = cancelButton.getBackground();
		stopButton.setBackground(Color.green);

		ActionListener MyL = Adapters.createGActionListener(this, "handleOK");
		okButton.addActionListener(MyL);
		MyL = Adapters.createGActionListener(this, "handleApply");
		applyButton.addActionListener(MyL);
		MyL = Adapters.createGActionListener(this, "handleCancel");
		cancelButton.addActionListener(MyL);
		MyL = Adapters.createGActionListener(this, "handleRecord");
		recordButton.addActionListener(MyL);
		MyL = Adapters.createGActionListener(this, "handleStop");
		stopButton.addActionListener(MyL);
		MyL = Adapters.createGActionListener(this, "handleClear");
		clearButton.addActionListener(MyL);
	}

	/**
	* Handles the pressing of the "OK" button.
	* @param e The input event.
	*/
	public void handleOK(ActionEvent e) {
		handleApply(e);
		VerdantiumUtils.disposeContainer(this);
	}

	/**
	* Handles the pressing of the "Apply" button.
	* @param e The input event.
	*/
	public void handleApply(ActionEvent e) {
		try {
			handleStop(e);

			String macroName = macroNameField.getText();
			MacroObject macro = macroRecorder.getMacro();
			Object[] val = { macroName, macro };

			EtherEvent send =
				new ClientMacroEtherEvent(
					this,
					ClientMacroEtherEvent.setClientMacro,
					null,
					target[0]);
			send.setParameter(val);
			ProgramDirector.fireEtherEvent(send, null);

			handleClear(e);
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles the pressing of the "Cancel" button by quitting.
	* @param e The input event.
	*/
	public void handleCancel(ActionEvent e) {
		VerdantiumUtils.disposeContainer(this);
	}

	/**
	* Handles the pressing of the "Record" button.
	* @param e The input event.
	*/
	public void handleRecord(ActionEvent e) {
		macroRecorder.handleClientRecord(target);
		recordButton.setBackground(Color.green);
		stopButton.setBackground(defaultBkgnd);
		recordButton.repaint();
		stopButton.repaint();
	}

	/**
	* Handles the pressing of the "Stop" button.
	* @param e The input event.
	*/
	public void handleStop(ActionEvent e) {
		macroRecorder.handleStop();
		recordButton.setBackground(defaultBkgnd);
		stopButton.setBackground(Color.green);
		recordButton.repaint();
		stopButton.repaint();
	}

	/**
	* Handles the pressing of the "Clear" button.
	* @param e The input event.
	*/
	public void handleClear(ActionEvent e) {
		macroRecorder.handleClear();
		((DefaultListModel<String>) opList.getModel()).removeAllElements();
	}

	/**
	* Prints the parameters of an Ether Event.
	* @param in The parameters of the event.
	*/
	protected String printParameter(Object in) {
		String out = "";
		Object param = in;
		if (param instanceof Object[]) {
			Object[] parm = (Object[]) param;
			int count;
			out = out + "{ ";
			for (count = 0; count < parm.length; count++) {
				out = out + parm[count];
				if ((count + 1) < parm.length)
					out = out + " , ";
			}
			out = out + " }";
		} else {
			out = out + param;
		}

		return (out);
	}

	/**
	* Prints the parameters of an Ether Event.
	* @param in The event for which to print the parameters.
	*/
	protected String printParameter(EtherEvent in) {
		return (printParameter(in.getParameter()));
	}

	/**
	* Adds an Ether Event to the current recording list displayed on the screen.
	* @param in The input event.
	* @param reply The reply generated by executing the event.
	* @param refcon A reference to context data that the generating code can associate with the event.  See various references to "refcon" in MacOS programming.
	*/
	protected void printEtherEventToList(
		EtherEvent in,
		Object reply,
		Object refcon) {
		String out =
			in.getTarget()
				+ ".["
				+ in.getClass().getName()
				+ "/"
				+ in.getEtherID()
				+ "]( "
				+ printParameter(in)
				+ " ) --> "
				+ printParameter(reply);
		((DefaultListModel<String>) opList.getModel()).addElement(out);
	}

	/**
	* Adds an Ether Event to the current recording list displayed on the screen.
	*/
	protected void printErrToList() {
		String out = "An event was not recorded because it produced an error.";
		((DefaultListModel<String>) opList.getModel()).addElement(out);
	}

	/**
	* Handles the destruction of the property editor.
	*/
	public void handleDestroy() {
		macroRecorder.handleDestroy();
		((PropertyChangeSource) (target[0])).removePropertyChangeListener(this);
	}

	/**
	* Handles the throwing of an error or exception.
	* @param in The input error or exception.
	*/
	public void handleThrow(Throwable in) {
		VerdantiumUtils.handleThrow(
			in,
			this,
			(PropertyChangeSource) (target[0]));
	}

	
}

