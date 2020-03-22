package verdantium;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;

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
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | Needed a dialog for simple error messages.                           | Initial creation.
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
 * Property editor for displaying exceptions and/or errors that have happened.
 * 
 * @author tgreen
 *
 */
public class ErrorMessageEditor extends MessageEditor {
	
	/**
	 * The original error or exception that triggered the generation of the ErrorMessagaeEditor.
	 */
	public Throwable ex = null;

	/**
	 * Constructor.
	 * @param in_src The data model of the source component.
	 * @param lab The brief label text to be displayed along with the message.
	 * @param message The message explaining the error or exception.
	 * @param in_ex The original error or exception that triggered the generation of the ErrorMessagaeEditor.
	 */
	public ErrorMessageEditor(
		PropertyChangeSource in_src,
		String lab,
		String message,
		Throwable in_ex) {
		super(
			in_src,
			lab,
			message,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		ex = in_ex;
		JButton debugButton = new JButton("Show Debug Info");

		buttonPane.add(debugButton);

		ActionListener evt =
			Adapters.createGActionListener(this, "handleDebug");
		debugButton.addActionListener(evt);

		contentPane.setMinimumSize(new Dimension(350, 150));
		contentPane.setPreferredSize(new Dimension(350, 150));
	}

	/**
	* Handles the pressing of the "Debug Info" button.
	* @param in The input event.
	*/
	public void handleDebug(ActionEvent e) {
		VerdantiumUtils.produceDebugWindow(ex, this, src);
	}

	
}

