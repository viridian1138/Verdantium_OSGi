package verdantium.core;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import meta.WrapRuntimeException;
import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.EtherEventPropertySource;
import verdantium.MessageEditor;
import verdantium.ProgramDirector;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumUtils;
import verdantium.utils.VerticalLayout;
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
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/10/2001            | Thorn Green (viridian_1138@yahoo.com)           | Second-Cut at Error Handling.                                        | Second-Cut at Error Handling.
*    | 12/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Third-Cut at Error Handling.                                         | Third-Cut at Error Handling.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
*    | 04/21/2002            | Thorn Green (viridian_1138@yahoo.com)           | Find/Replace Support.                                                | Created FindPropertyEditor using pieces from several other classes.
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
* A property editor for searching a component for all instances of a particular text string.
* <P>
* @author Thorn Green
*/
public class FindPropertyEditor extends PropertyEditAdapter {
	
	/**
	* The panel in which the property editor lies.
	*/
	protected JPanel myPan = new JPanel();

	/**
	* The check box for match case.
	*/
	protected JCheckBox matchCaseCheck = new JCheckBox("Match Case", false);

	/**
	* TextField for entering the search string.
	*/
	protected JTextField searchStringField = new JTextField(30);

	/**
	* The current find/replace iterator.
	*/
	protected FindReplaceIterator findIterator = null;

	/**
	* The data model of the component being searched.
	*/
	protected EtherEventPropertySource target = null;

	/**
	* The last search string used.
	*/
	protected String lastSrchString = "";

	/**
	* Constructs the property editor for a given property change source.
	* @param in The data model of the component being searched.
	*/
	public FindPropertyEditor(EtherEventPropertySource in) {
		target = in;
		target.addPropertyChangeListener(this);
		initUI();
	}

	/**
	* Initializes the user interface.
	*/
	protected void initUI() {
		JPanel center = new JPanel();
		JPanel east = new JPanel();
		myPan.setLayout(new BorderLayout(2, 2));
		myPan.add(BorderLayout.CENTER, center);
		myPan.add(BorderLayout.EAST, east);

		center.setLayout(new VerticalLayout(2));
		JPanel p1 = new JPanel();
		center.add("any", p1);
		center.add("any", matchCaseCheck);
		p1.setLayout(new BorderLayout(0, 0));
		p1.add(BorderLayout.WEST, new JLabel("Find What : "));
		p1.add(BorderLayout.CENTER, searchStringField);

		east.setLayout(new VerticalLayout(2));
		JButton findNext = new JButton("Find Next");
		JButton cancel = new JButton("Cancel");
		east.add("any", findNext);
		east.add("any", cancel);

		ActionListener FndL =
			Adapters.createGActionListener(this, "handleFind");
		findNext.addActionListener(FndL);
		ActionListener CanL =
			Adapters.createGActionListener(this, "handleCancel");
		cancel.addActionListener(CanL);
	}

	/**
	* Gets the GUI of the property editor.
	* @return The GUI of the property editor.
	*/
	public JComponent getGUI() {
		return (myPan);
	}

	/**
	* Handles the destruction of the component by removing appropriate change listeners.
	*/
	public void handleDestroy() {
		setFindIterator(null);
		target.removePropertyChangeListener(this);
	}

	/**
	* Handles a find event.
	* @param e The input event.
	*/
	public void handleFind(ActionEvent e) {
		String srch = searchStringField.getText();
		if (!(srch.equals(lastSrchString)))
			setFindIterator(null);
		lastSrchString = srch;

		try {
			if (findIterator == null) {
				Object[] parameter =
					{
						searchStringField.getText(),
						new Boolean(matchCaseCheck.isSelected())};
				EtherEvent send =
					new PropertyEditEtherEvent(
						this,
						PropertyEditEtherEvent.createFindReplaceIterator,
						null,
						null);
				send.setParameter(parameter);

				try {
					setFindIterator(
						(FindReplaceIterator) (target
							.processObjEtherEvent(send, null)));
				} catch (Throwable ex) {
					throw (new WrapRuntimeException("Iterator Failed", ex));
				}
			}

			if (findIterator == null)
				throw (
					new RuntimeException("findIterator shouldn't be null here."));
			else {
				if (findIterator.hasNext()) {
					Object nxt = findIterator.next();
				} else {
					Toolkit.getDefaultToolkit().beep();
					VerdantiumComponent comp = (VerdantiumComponent) target;
					JComponent jcomp = null;
					if (comp != null)
						jcomp = comp.getGUI();
					MessageEditor edit =
						new MessageEditor(
							target,
							"Finished Searching",
							"Finished searching the document.",
							ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					ProgramDirector.showPropertyEditor(
						edit,
						jcomp,
						"Finished Searching");
					setFindIterator(null);
				}
			}
		} catch (Exception ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles a cancel event.
	* @param e The input event.
	*/
	public void handleCancel(ActionEvent e) {
		VerdantiumUtils.disposeContainer(this);
	}

	/**
	* Sets the findIterator.
	* @param in The findIterator to set.
	*/
	protected void setFindIterator(FindReplaceIterator in) {
		if ((findIterator != null) && (findIterator != in))
			findIterator.handleDestroy();
		findIterator = in;
	}

	/**
	* Handles the throwing of an error or exception.
	* @param in The input error or exception.
	*/
	public void handleThrow(Throwable in) {
		VerdantiumUtils.handleThrow(in, this, target);
	}

	
}

