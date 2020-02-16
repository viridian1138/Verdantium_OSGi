package verdantium.pagewelder;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import verdantium.Adapters;
import verdantium.EtherEventPropertySource;
import verdantium.ProgramDirector;
import verdantium.standard.HyperButtonPropertyEditor;
import verdantium.utils.VerticalLayout;
//import verdantium.demo.help.MyContainerAppHelp;

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
*    | 01/28/2001            | Thorn Green (viridian_1138@yahoo.com)           | Multiple bugs in calling of handleDestroy()                          | Implemented a set of bug-fixes.
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Macro support.                                                       | Added macro support.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/29/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for multiple PLAFs.                                          | Made code changes to support multiple PLAFs.
*    | 10/05/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for command-line arguments.                                  | Added support for command-line arguments.
*    | 12/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Third-Cut at Error Handling.                                         | Third-Cut at Error Handling.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
*    | 04/21/2002            | Thorn Green (viridian_1138@yahoo.com)           | Find/Replace support.                                                | Added find/replace support.
*    | 05/10/2002            | Thorn Green (viridian_1138@yahoo.com)           | Redundant information in persistent storage.                         | Made numerous persistence and packaging changes.
*    | 01/12/2003            | Thorn Green (viridian_1138@yahoo.com)           | Application development too complex.                                 | Simplified application development.
*    | 03/09/2003            | Thorn Green (viridian_1138@yahoo.com)           | PageWelder.                                                          | Implemented PageWelder using code from other classes.
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
* PageWelder shows how to build a simple container application.  Copy this source code,
* rather than verdantium.core.ContainerApp, when building your own container.
* 
* @author Thorn Green
*/
public class PageWelderHyperButtonPropertyEditor
	extends HyperButtonPropertyEditor {
	
	/**
	 * Button to set the HyperButton to go to the next card upon being pressed.
	 */
	private JRadioButton NextButton = new JRadioButton("Next Card", true);
	
	/**
	 * Button to set the HyperButton to go to the previous card upon being pressed.
	 */
	private JRadioButton PrevButton = new JRadioButton("Previous Card", false);

	/**
	 * Button to set the HyperButton to go to the first card upon being pressed.
	 */
	private JRadioButton FirstButton = new JRadioButton("First Card", false);

	/**
	 * Button to set the HyperButton to go to the last card upon being pressed.
	 */
	private JRadioButton LastButton = new JRadioButton("Last Card", false);

	/**
	 * Button to set the HyperButton to add a card upon being pressed.
	 */
	private JRadioButton AddButton = new JRadioButton("Add Card", false);

	/**
	 * Button to set the HyperButton to delete a card upon being pressed.
	 */
	private JRadioButton DeleteButton = new JRadioButton("Delete Card", false);

	
	/**
	* Constructs the property editor.
	* @param in The component being edited.
	* @param inp Properties defining which tabs to display.
	*/
	public PageWelderHyperButtonPropertyEditor(
		PageWelderHyperButton in,
		Properties inp) {
		super(in, inp);
		addTabsSubSub(in, inp);
	}

	/**
	* Shunts the superclass method for adding the tabs.
	* @param in The property source from which to define the tabs.
	* @param inp Properties defining which tabs to display.
	*/
	protected void addTabsSub(EtherEventPropertySource in, Properties inp) {
	}

	/**
	* Adds the tabs for editing the button to the superclass tabs.
	* @param in The property source from which to define the tabs.
	* @param inp Properties defining which tabs to display.
	*/
	protected void addTabsSubSub(EtherEventPropertySource in, Properties inp) {
		super.addTabsSub(in, inp);

		JPanel pan = new JPanel();
		TabPane.add("PageWelder Link", pan);
		pan.setLayout(new BorderLayout(0, 0));
		JButton applyButton = new JButton("Apply");
		pan.add(BorderLayout.SOUTH, applyButton);
		ButtonGroup MyGrp = new ButtonGroup();
		JPanel pan2 = new JPanel();
		pan.add(BorderLayout.CENTER, pan2);
		pan2.setLayout(new VerticalLayout(1));

		MyGrp.add(NextButton);
		MyGrp.add(PrevButton);
		MyGrp.add(FirstButton);
		MyGrp.add(LastButton);
		MyGrp.add(AddButton);
		MyGrp.add(DeleteButton);

		pan2.add("any", NextButton);
		pan2.add("any", PrevButton);
		pan2.add("any", FirstButton);
		pan2.add("any", LastButton);
		pan2.add("any", AddButton);
		pan2.add("any", DeleteButton);

		ActionListener MyL =
			Adapters.createGActionListener(this, "handlePwApply");
		applyButton.addActionListener(MyL);
	}

	/**
	* Handles a button press event fopr the apply button for PageWelder events.
	* @param e The input event.
	*/
	public void handlePwApply(ActionEvent e) {
		Object[] args = null;

		if (NextButton.isSelected()) {
			Object[] ar = { PageWelderEtherEvent.nextCard, null };
			args = ar;
		}

		if (PrevButton.isSelected()) {
			Object[] ar = { PageWelderEtherEvent.prevCard, null };
			args = ar;
		}

		if (FirstButton.isSelected()) {
			Object[] ar = { PageWelderEtherEvent.firstCard, null };
			args = ar;
		}

		if (LastButton.isSelected()) {
			Object[] ar = { PageWelderEtherEvent.lastCard, null };
			args = ar;
		}

		if (AddButton.isSelected()) {
			Object[] ar = { PageWelderEtherEvent.addCard, null };
			args = ar;
		}

		if (DeleteButton.isSelected()) {
			Object[] ar = { PageWelderEtherEvent.deleteCard, null };
			args = ar;
		}

		try {
			PageWelderEtherEvent evt =
				new PageWelderEtherEvent(
					this,
					PageWelderEtherEvent.pwButtonSetLink,
					args,
					target);
			ProgramDirector.fireEtherEvent(evt, null);
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	
}

