package verdantium.vhelp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.StandardEtherEvent;
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
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
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
* Property editor that prints a Verdantium help page.
* <P>
* @author Thorn Green
*/
public class VerdantiumHelpPropertyEditor extends PropertyEditAdapter {
	
	/**
	* The "Print" button of the component.
	*/
	private JButton Print = new JButton("Print The File...");

	/**
	* Returns the GUI for the component.
	* @return The GUI for the component.
	*/
	public JComponent getGUI() {
		return (Print);
	}

	/**
	* The Help component being edited.
	*/
	protected VerdantiumHelp MyHelp = null;

	/**
	* Constructs the property editor.
	* @param in The component page being edited.
	*/
	public VerdantiumHelpPropertyEditor(VerdantiumHelp in) {
		MyHelp = in;
		MyHelp.addPropertyChangeListener(this);
		ActionListener MyL =
			Adapters.createGActionListener(this, "handleButton");
		Print.addActionListener(MyL);
	}

	/**
	* Handles all button press events for the component.
	* @param e The input event.
	*/
	public void handleButton(ActionEvent e) {
		try {
			EtherEvent send =
				new StandardEtherEvent(
					this,
					StandardEtherEvent.objPrintEvent,
					null,
					MyHelp);
			ProgramDirector.fireEtherEvent(send, null);
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles the destruction of the component.
	*/
	public void handleDestroy() {
		MyHelp.removePropertyChangeListener(this);
	}

	/**
	* Handles the throwing of an error or exception.
	* @param in The error or exception to handle.
	*/
	public void handleThrow(Throwable in) {
		VerdantiumUtils.handleThrow(in, MyHelp, MyHelp);
	}

}

