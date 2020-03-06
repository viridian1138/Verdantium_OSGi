package verdantium.core;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import meta.WrapRuntimeException;
import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.EtherEventPropertySource;
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
*    | 04/21/2002            | Thorn Green (viridian_1138@yahoo.com)           | Find/Replace support.                                                | Created ReplacePropertyEditor.
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
* An extension of {@link FindPropertyEditor} for handling both find and replace.
* <P>
* @author Thorn Green
*/
public class ReplacePropertyEditor extends FindPropertyEditor {

	/**
	* TextField for entering the replace string.
	*/
	protected JTextField replaceStringField = new JTextField(30);

	/**
	* Constructs the property editor for a given property source.
	* @param in The data model for the component on which find/replace is being performed.
	*/
	public ReplacePropertyEditor(EtherEventPropertySource in) {
		super(in);
		initUIrepl();
	}

	/**
	* Initializes the user interface.
	*/
	protected void initUIrepl() {
		JPanel center = new JPanel();
		JPanel east = new JPanel();
		myPan.setLayout(new BorderLayout(2, 2));
		myPan.add(BorderLayout.CENTER, center);
		myPan.add(BorderLayout.EAST, east);

		center.setLayout(new VerticalLayout(2));
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		center.add("any", p1);
		center.add("any", p2);
		center.add("any", matchCaseCheck);
		p1.setLayout(new BorderLayout(0, 0));
		p1.add(BorderLayout.WEST, new JLabel("Find What : "));
		p1.add(BorderLayout.CENTER, searchStringField);
		p2.setLayout(new BorderLayout(0, 0));
		p2.add(BorderLayout.WEST, new JLabel("Replace With : "));
		p2.add(BorderLayout.CENTER, replaceStringField);

		east.setLayout(new VerticalLayout(2));
		JButton findNext = new JButton("Find Next");
		JButton repl = new JButton("Replace");
		JButton replAll = new JButton("Replace All");
		JButton cancel = new JButton("Cancel");
		east.add("any", findNext);
		east.add("any", repl);
		east.add("any", replAll);
		east.add("any", cancel);

		ActionListener FndL =
			Adapters.createGActionListener(this, "handleFind");
		findNext.addActionListener(FndL);
		ActionListener ReplL =
			Adapters.createGActionListener(this, "handleReplace");
		repl.addActionListener(ReplL);
		ActionListener ReplaL =
			Adapters.createGActionListener(this, "handleReplaceAll");
		replAll.addActionListener(ReplaL);
		ActionListener CanL =
			Adapters.createGActionListener(this, "handleCancel");
		cancel.addActionListener(CanL);
	}

	/**
	* De-functionalizes the superclass method to initialize the user interface.
	*/
	protected void initUI() {
	}

	/**
	* Handles a replace event.
	* @param e The input event.
	*/
	public void handleReplace(ActionEvent e) {
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
				findIterator.replace(replaceStringField.getText());
			}
		} catch (Exception ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles a replace all event.
	* @param e The input event.
	*/
	public void handleReplaceAll(ActionEvent e) {
		setFindIterator(null);

		try {
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

			if (findIterator == null)
				throw (
					new RuntimeException("findIterator shouldn't be null here."));
			else {
				while (findIterator.hasNext()) {
					findIterator.next();
					findIterator.replace(replaceStringField.getText());
				}
			}
		} catch (Exception ex) {
			handleThrow(ex);
		}
	}

	
}

