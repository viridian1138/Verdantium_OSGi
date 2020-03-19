package verdantium.standard;

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.EtherEventPropertySource;
import verdantium.ProgramDirector;
import verdantium.core.DefaultPropertyEditor;
import verdantium.core.PropertyEditEtherEvent;

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
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
*    | 03/09/2003            | Thorn Green (viridian_1138@yahoo.com)           | Needed more accessors to support PageWelder.                         | Added accessors.
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
* This is a property editor class for {@link HyperButton}.
* <P>
* @author Thorn Green
*/
public class HyperButtonPropertyEditor extends DefaultPropertyEditor {
	
	/**
	* The text area used to edit the label for the button.
	*/
	private JTextField labelField;
	
	/**
	* The apply button for the editing of the label.
	*/
	private JButton applyButton;

	/**
	* Handles the destruction of the component.
	*/
	public void handleDestroy() {
		super.handleDestroy();
	}

	/**
	* Handles the pressing of the "apply" button by firing the EtherEvent to change
	* the button label.
	* @param evt The input event.
	*/
	public void handleButton(ActionEvent evt) {
		try {
			EtherEvent send =
				new PropertyEditEtherEvent(
					this,
					HyperButton.setLabelText,
					null,
					target);
			send.setParameter(labelField.getText());
			ProgramDirector.fireEtherEvent(send, null);
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Shunts the superclass method for adding the tabs.
	* @param in The property source from which to define the tabs.
	* @param inp Properties defining which tabs to display.
	*/
	protected void addTabs(EtherEventPropertySource in, Properties inp) {
	}

	/**
	* Adds the tabs for editing the button to the superclass tabs.
	* @param in The property source from which to define the tabs.
	* @param inp Properties defining which tabs to display.
	*/
	protected void addTabsSub(EtherEventPropertySource in, Properties inp) {
		target = (HyperButton) in;
		labelField = new JTextField();
		applyButton = new JButton("Apply");

		JPanel p2 = new JPanel();
		TabPane.add("Document", p2);
		p2.setLayout(new BorderLayout(0, 0));
		JPanel p2a = new JPanel();
		p2.add("Center", p2a);
		p2a.setLayout(new BorderLayout(0, 0));
		p2a.add("North", new JLabel("Button Label:"));
		JPanel p2b = new JPanel();
		p2a.add("Center", p2b);
		p2b.setLayout(new BorderLayout(0, 0));
		p2b.add("North", labelField);
		p2.add("South", applyButton);

		super.addTabs(in, inp);

		ActionListener ButtonL =
			Adapters.createGActionListener(this, "handleButton");
		applyButton.addActionListener(ButtonL);
	}

	/**
	* Constructs the property editor.
	* @param in The component being edited.
	* @param inp Properties defining which tabs to display.
	*/
	public HyperButtonPropertyEditor(HyperButton in, Properties inp) {
		super(in, inp);
		addTabsSub(in, inp);
	}

	/**
	* Returns the data flavors supported by the property editor, which are none.
	* @return The data flavors supported by the property editor, which are none.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		return (null);
	}

	/**
	* The HyperButton being edited by the property editor.
	*/
	protected HyperButton target;
	
}

