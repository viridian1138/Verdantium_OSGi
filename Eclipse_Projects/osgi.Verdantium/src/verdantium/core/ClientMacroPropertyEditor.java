package verdantium.core;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import meta.WrapRuntimeException;
import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.EtherEventPropertySource;
import verdantium.ProgramDirector;
import verdantium.PropertyChangeSource;
import verdantium.VerdantiumUtils;
import verdantium.utils.IllegalInputException;
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
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Needed a class for editing client macros.                            | Initial creation, copying code from another class.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
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
* A property editor for client (per-component) macros.
*
* @author Thorn Green
*/
public class ClientMacroPropertyEditor extends PropertyEditAdapter {
	
	/**
	* The model of the component on which to edit the macros.
	*/
	transient protected EtherEventPropertySource target = null;

	/**
	* The list of macros to edit.
	*/
	transient protected JList<String> list = null;

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
	* Button used to indicate that editing is completed.
	*/
	transient protected JButton doneButton = new JButton("Done");

	/**
	* Gets the GUI for the property editor.
	* @return The GUI for the property editor.
	*/
	public JComponent getGUI() {
		return (master);
	}

	/**
	* Constructs a client macro property editor for a component.  Assumes the component
	* implements PropertyChangeSource.
	* @param in The model of the component on which to edit the macros.
	*/
	public ClientMacroPropertyEditor(EtherEventPropertySource in) {
		target = in;
		list = new JList<String>();
		scp =
			new JScrollPane(
				list,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		initMacroList();

		master.setLayout(new BorderLayout(0, 0));
		master.add("North", new JLabel("Macro Name : "));
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
		runButton = new JButton("Run");
		deleteButton = new JButton("Delete");
		p3.add(runButton);
		p3.add(deleteButton);
		doneButton = new JButton("Done");
		p4.add(doneButton);

		ActionListener myL = Adapters.createGActionListener(this, "handleDone");
		doneButton.addActionListener(myL);
		myL = Adapters.createGActionListener(this, "handleRun");
		runButton.addActionListener(myL);
		myL = Adapters.createGActionListener(this, "handleDelete");
		deleteButton.addActionListener(myL);

		((PropertyChangeSource) target).addPropertyChangeListener(this);
	}

	/**
	* Initializes the list of macros.
	*/
	protected void initMacroList() {
		try {
			Vector<String> v = new Vector<String>();
			int cnt = 0;

			EtherEvent send =
				new ClientMacroEtherEvent(
					this,
					ClientMacroEtherEvent.getClientMacroNameIterator,
					null,
					target);
			Object ob = target.processObjEtherEvent(send, null);

			Iterator<String> it = (Iterator<String>) (ob);
			while (it.hasNext()) {
				String obj = it.next();
				v.setSize(cnt + 1);
				v.setElementAt(obj, cnt);
				cnt++;
			}

			list.setListData(v);
		} catch (Throwable ex) {
			throw (new WrapRuntimeException("Init Macro Failed", ex));
		}
	}

	/**
	* Handles the pressing of the "Done" button by quitting.
	* @param e The input event.
	*/
	public void handleDone(ActionEvent e) {
		VerdantiumUtils.disposeContainer(this);
	}

	/**
	* Handles the pressing of the "Run" button by attempting to run a macro.
	* @param e The input event.
	*/
	public void handleRun(ActionEvent e) {
		try {
			String val = list.getSelectedValue();
			if (val != null) {
				EtherEvent send =
					new ClientMacroEtherEvent(
						this,
						ClientMacroEtherEvent.runClientMacro,
						null,
						target);
				send.setParameter(val);
				ProgramDirector.fireEtherEvent(send, null);
			} else
				throw (new IllegalInputException("You must select a macro."));
		} catch (Throwable ex) {
			handleThrow(ex);
		}

	}

	/**
	* Handles the pressing of the "Delete" button by attempting to delete a macro.
	* @param e The input event.
	*/
	public void handleDelete(ActionEvent e) {
		try {
			String val = list.getSelectedValue();
			if (val != null) {
				EtherEvent send =
					new ClientMacroEtherEvent(
						this,
						ClientMacroEtherEvent.deleteClientMacro,
						null,
						target);
				send.setParameter(val);
				ProgramDirector.fireEtherEvent(send, null);
			} else
				throw (new IllegalInputException("You must select a macro."));
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles the destruction of the component.
	*/
	public void handleDestroy() {
		((PropertyChangeSource) target).removePropertyChangeListener(this);
	}

	/**
	* Handles property change events.
	* @param evt The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == ClientMacroEtherEvent.MACRO_MAP_CHANGED) {
			initMacroList();
		}

		if (evt.getPropertyName() == ProgramDirector.propertyHide) {
			VerdantiumUtils.disposeContainer(this);
		}
	}

	/**
	* Handles the throwing of an error or exception.
	* @param in The input error or exception.
	*/
	public void handleThrow(Throwable in) {
		VerdantiumUtils.handleThrow(in, this, target);
	}

	
}

