package verdantium.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.MatteBorder;

import meta.DataFormatException;
import meta.HighLevelList;
import meta.StdLowLevelList;
import meta.VersionBuffer;
import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.StandardEtherEvent;
import verdantium.TransVersionBuffer;
import verdantium.TransVersionBufferFlavor;
import verdantium.UrlHolder;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.core.help.MacroRecorderHelp;
import verdantium.undo.UndoManager;
import verdantium.utils.IllegalInputException;
import verdantium.utils.VerdantiumDragUtils;
import verdantium.utils.VerdantiumDropUtils;
import verdantium.utils.VerticalLayout;
import verdantium.xapp.ApplicationAdapter;
import verdantium.xapp.DocPageFormat;
import verdantium.xapp.MacroTreeMap;

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
*    | 10/29/2000            | Thorn Green (viridian_1138@yahoo.com)           | Classes did not have names that followed standard Java conventions.  | Performed a global modification to bring the names within spec.
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Needed to separate code macro functionality from MacroRecorder.      | Factored out the functionality.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/10/2001            | Thorn Green (viridian_1138@yahoo.com)           | Second-Cut at Error Handling.                                        | Second-Cut at Error Handling.
*    | 09/29/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for multiple PLAFs.                                          | Made code changes to support multiple PLAFs.
*    | 10/05/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for command-line arguments.                                  | Added support for command-line arguments.
*    | 12/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Third-Cut at Error Handling.                                         | Third-Cut at Error Handling.
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
* This is a simple component that records and then plays
* macros.  This could be useful if one wanted to control a component
* by scripting.
* <P>
* @author Thorn Green
*/
public class MacroRecorder extends ApplicationAdapter {

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
			MacroRecorder.this.printEtherEventToList(in, reply, refcon);
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
			MacroRecorder.this.printErrToList();
		}

	}

	/**
	* The enclosing panel that holds the macro recorder form.
	*/
	private JPanel formPanel = new JPanel();
	
	/**
	* The enclosing panel for the row of buttons near the bottom of the component.
	*/
	private JPanel buttonPanel = new JPanel();
	
	/**
	* The enclosing panel with the row of "play/record/stop" buttons at the top,
	* and the "properties" button at the bottom.
	*/
	private JPanel outerButtonPanel = new JPanel();
	
	/**
	* The button to play a script.
	*/
	private JButton play = new JButton("play");
	
	/**
	* The button to record a script.
	*/
	private JButton record = new JButton("record");
	
	/**
	* The button to stop recording.
	*/
	private JButton stop = new JButton("stop");
	
	/**
	* The button to clear all previous recordings.
	*/
	private JButton clear = new JButton("clear");
	
	/**
	* The button to get the properties of the component.
	*/
	private JButton properties = new JButton("properties...");
	
	/**
	* The scrolling pane containing the list of scripting commands.
	*/
	private JScrollPane opScroll = null;
	
	/**
	* The list component containing the list of scripting commands.
	*/
	private JList<String> opList = new JList<String>();

	/**
	* The Macro Recording Object
	*/
	transient protected MrMacroRecord macroRecorder = new MrMacroRecord();

	/**
	* Gets the GUI of the macro recorder component.
	* @return The GUI of the macro recorder component.
	*/
	public JComponent getGUI() {
		return (formPanel);
	}

	/**
	* Constructs the macro recorder.
	*/
	public MacroRecorder() {
		docPageFormat =
			new DocPageFormat(
				UndoManager.createInstanceUndoManager(
					jundo.runtime.Runtime.getInitialMilieu()));
		formPanel.setBorder(new MatteBorder(1, 1, 1, 1, Color.black));
		opList.setModel(new DefaultListModel<String>());

		opScroll =
			new JScrollPane(
				opList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		formPanel.setLayout(new BorderLayout(0, 0));
		formPanel.add("Center", opScroll);
		formPanel.add("South", outerButtonPanel);
		outerButtonPanel.setLayout(new VerticalLayout(1));
		outerButtonPanel.add("any", buttonPanel);
		outerButtonPanel.add("any", properties);

		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(play);
		buttonPanel.add(record);
		buttonPanel.add(stop);
		buttonPanel.add(clear);

		DefaultBkgnd = play.getBackground();
		stop.setBackground(Color.green);

		ActionListener myL = Adapters.createGActionListener(this, "handlePlay");
		play.addActionListener(myL);
		myL = Adapters.createGActionListener(this, "handleRecord");
		record.addActionListener(myL);
		myL = Adapters.createGActionListener(this, "handleStop");
		stop.addActionListener(myL);
		myL = Adapters.createGActionListener(this, "handleClear");
		clear.addActionListener(myL);
		myL = Adapters.createGActionListener(this, "handleProperties");
		properties.addActionListener(myL);

		if (!(this instanceof VerdantiumPropertiesEditor)) {
			VerdantiumDragUtils.setDragUtil(opList, this);
			VerdantiumDropUtils.setDropUtil(opList, this, this);
		}
		configureForEtherEvents();
	}

	/**
	 * Handles Ether Events to alter the properties of the component.
	 * @param in The event to handle.
	 * @param refcon A reference to context data for the event.
	 * @return The result of handling the event, or null if there is no result.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon)
		throws Throwable {
		Object ret = super.processObjEtherEvent(in, refcon);
		if (ret == EtherEvent.EVENT_NOT_HANDLED)
			ret = null;
		return (ret);
	}

	/**
	* Handles the pressing of the "play" button by stopping all
	* recording, and then playing the recorded macro.
	* @param e The input event.
	*/
	public void handlePlay(ActionEvent e) {
		try {
			handleStop(e);

			MacroRecordingObject.playMacro(
				macroRecorder.getMacro().getMacroList(),
				this,
				macroRecorder.getParamTable());
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles the pressing of the "record" button by turning macro
	* recording on.
	* @param e The input event.
	*/
	public void handleRecord(ActionEvent e) {
		macroRecorder.handleRecord();
		record.setBackground(Color.green);
		stop.setBackground(DefaultBkgnd);
		record.repaint();
		stop.repaint();
	}

	/**
	* Handles the pressing of the "stop" button by stopping all
	* macro recording.
	* @param e The input event.
	*/
	public void handleStop(ActionEvent e) {
		macroRecorder.handleStop();
		record.setBackground(DefaultBkgnd);
		stop.setBackground(Color.green);
		record.repaint();
		stop.repaint();
	}

	/**
	* Handles the pressing of the "clear" button by
	* clearing the macro recorder to its initial state.
	* @param e The input event.
	*/
	public void handleClear(ActionEvent e) {
		macroRecorder.handleClear();
		((DefaultListModel<String>) opList.getModel()).removeAllElements();
		docPageFormat.setDocPageFormat(null);
	}

	/**
	* Fires an Ether Event to show the properties editor for the component.
	* @param e The input event.
	*/
	public void handleProperties(ActionEvent e) {
		try {
			EtherEvent send =
				new StandardEtherEvent(
					this,
					StandardEtherEvent.showPropertiesEditor,
					null,
					this);
			ProgramDirector.fireEtherEvent(send, null);
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Prints the parameters of an Ether Event.
	* @param in The parameters of an Ether Event.
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
	* @param in The event to handle.
	* @param reply The result of handling the event, or null if there is no result.
	* @param refcon A reference to context data for the event.
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
	* Creates a property editor for the component.
	* @return The created property editor.
	*/
	public VerdantiumPropertiesEditor makePropertiesEditor() {
		Properties MyP = new Properties();
		MyP.put("NoEditControl", this);
		MyP.put("NoDesignControl", this);
		DefaultPropertyEditor MyEdit = new DefaultPropertyEditor(this, MyP);
		return (MyEdit);
	}

	/**
	* Shows the property editor for the component.
	* @param e The input event for showing the editor.
	*/
	public void showPropertiesEditor(EtherEvent e) {
		VerdantiumPropertiesEditor MyEdit = makePropertiesEditor();
		ProgramDirector.showPropertyEditor(
			MyEdit,
			getGUI(),
			"Macro Recorder Property Editor");
	}

	/**
	* Handles the destruction of the component by un-hooking its property change support.
	*/
	public void handleDestroy() {
		macroRecorder.handleDestroy();
		super.handleDestroy();
	}

	/**
	* Returns the data flavors that the component can load from persistent storage.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{ new TransVersionBufferFlavor("Macro Recorder", "Macro Recorder")};
		return (MyF);
	}

	/**
	* Returns the data flavors that the component can save to persistent storage.
    * @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{ new TransVersionBufferFlavor("Macro Recorder", "Macro Recorder")};
		return (MyF);
	}

	/**
	* Loads data for the component from persistent storage.
    * @param flavor The flavor in which to load the data.
    * @param trans The transferable from which to load the data.
	*/
	public void loadPersistentData(DataFlavor flavor, Transferable trans)
		throws IOException {
		if (trans instanceof UrlHolder) {
			fileSaveURL = ((UrlHolder) trans).getUrl();
			fileSaveFlavor = flavor;
		}

		if (trans == null) {
			handleClear(null);
		} else {
			try {
				TransVersionBuffer MyF = (TransVersionBuffer) trans;
				VersionBuffer.chkNul(MyF);

				handleClear(null);
				MacroObject tmpm = (MacroObject) (MyF.getProperty("myMacro"));
				VersionBuffer.chkNul(tmpm);
				macroRecorder.setMacro(tmpm);

				if (!(macroRecorder.getMacro().getMacroList().empty())) {
					macroRecorder.getMacro().getMacroList().searchHead();
					boolean Done = false;

					while (!Done) {
						MacroRecorderNode MyNode =
							(MacroRecorderNode) (macroRecorder
								.getMacro()
								.getMacroList()
								.getNode());
						printEtherEventToList(
							MyNode.getCopyEvent(),
							MyNode.getCopyReply(),
							null);

						macroRecorder.getMacro().getMacroList().right();
						Done =
							macroRecorder.getMacro().getMacroList().getHead();
					}
				}
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
			new TransVersionBuffer("Macro Recorder", "Macro Recorder");

		MyF.setProperty("myMacro", macroRecorder.getMacro());

		return (MyF);
	}

	/**
	* Plays a previously recorded client macro stored in a map.
	* @param map The map from which to retrieve the macro.
	* @param macroName The name of the macro in the map.
	* @param transmitter The transmitting source for the event.
	*/
	public static void playClientMacro(
		MacroTreeMap map,
		String macroName,
		Object transmitter)
		throws Throwable {
		MacroRecordingObject.playClientMacro(map, macroName, transmitter);
	}

	/**
	* Plays a previously recorded client macro.
	* @param macroObj The previously recorded macro.
	* @param transmitter The transmitting source for the event.
	*/
	public static void playClientMacro(
		MacroObject macroObj,
		Object transmitter)
		throws Throwable {
		MacroRecordingObject.playClientMacro(macroObj, transmitter);
	}

	/**
	* Plays a previously recorded macro.
	* @param macroList The list of macro instructions.
	* @param transmitter The transmitting source for the event.
	* @param resultTbl The table of macro parameter results.
	*/
	public static void playMacro(
		HighLevelList<MacroRecorderNode,StdLowLevelList<MacroRecorderNode>> macroList,
		Object transmitter,
		Vector<Object> resultTbl)
		throws Throwable {
		MacroRecordingObject.playMacro(macroList, transmitter, resultTbl);
	}

	/**
	* Stores a macro in the macro map.  Throws an exception if the tag is bad.
	* @param tag The tag under which to store the macro.
	* @param macro The macro to store under the tag name.
	* @param macroMap The macro map in which to store the macro.
	*/
	public static void storeMacroInMap(
		Object tag,
		Object macro,
		MacroTreeMap macroMap)
		throws IllegalInputException {
		if (tag instanceof String) {
			if (tag.equals(""))
				throw (
					new IllegalInputException("You must specify a macro name."));

			macroMap.putMacro((String) tag, (MacroObject) macro);
		} else {
			throw (new IllegalInputException("You must specify a macro name."));
		}
	}

	/**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		MacroRecorderHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		MacroRecorder MyComp = new MacroRecorder();
		ProgramDirector.showComponent(MyComp, "Macro Recorder", argv, false);
	}

	
}


