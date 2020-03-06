package verdantium.core;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.VerdantiumApplet;
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
*    | 10/07/2000            | Thorn Green (viridian_1138@yahoo.com)           | Problems with JDK 1.3 applets.                                       | Made code compatible with JDK 1.3 applets.
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
*
*
*/


/**
* Property editor for a {@link ColorCell}.
* 
* @author Thorn Green
*/
public class ColorCellPropertyEditor extends PropertyEditAdapter {
	
	/**
	 * Whether the property editor is updating.
	 */
	private boolean updating = false;
	
	/**
	 * Panel containing the GUI for the property editor.
	 */
	private JPanel myPanel = new JPanel();
	
	/**
	 * Color chooser used to edit the color.
	 */
	private JColorChooser myChooser;
	
	/**
	 * Button to apply a change to the ColorCell.
	 */
	private JButton myButton = new JButton("Apply");
	
	/**
	 * Button displaying help on the ColorCell.
	 */
	private JButton helpButton = new JButton("Help");
	
	/**
	 * Gets the GUI of the property editor.
	 * @return The GUI of the property editor.
	 */
	public JComponent getGUI() {
		return (myPanel);
	}

	/**
	* Handles the destruction of the component.
	*/
	public void handleDestroy() {
		target.removeColorActionListener(colorL);
		target.removePropertyChangeListener(this);
	}

	/**
	* Handles a color change in the property editor.
	* @param evt The input event.
	*/
	public void handleColorChange(ActionEvent evt) {
		if (!updating) {
			try {
				updating = true;

				EtherEvent send =
					new PropertyEditEtherEvent(
						this,
						ColorCell.setCellColor,
						null,
						target);
				if (myChooser != null)
					send.setParameter(myChooser.getColor());
				ProgramDirector.fireEtherEvent(send, null);
			} catch (Throwable ex) {
				handleThrow(ex);
			} finally {
				updating = false;
			}
		}
	}

	/**
	* Handles the displaying of the help screen.
	* @param evt The input event.
	*/
	public void handleDisplayHelp(ActionEvent evt) {
		ColorCell.displayVerdantiumHelp(this);
	}

	/**
	* Handles a color change in the client color cell.
	* @param e The input event.
	*/
	public void handleTargetColorChange(ActionEvent e) {
		if (!updating) {
			updating = true;
			if (myChooser != null)
				myChooser.setColor(target.getColor());
			updating = false;
		}
	}

	/**
	* Constructs the ColorCellPropertyEditor for a particular ColorCell.
	* @param in The ColorCell being edited.
	*/
	public ColorCellPropertyEditor(ColorCell in) {
		target = in;
		if (!VerdantiumApplet.isAppletActivated())
			myChooser = new JColorChooser();
		myPanel.setLayout(new BorderLayout(0, 0));
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout());
		if (myChooser != null)
			myPanel.add("Center", myChooser);
		else
			myPanel.add(
				"Center",
				new JTextArea("Sorry, Color Chooser can not run on an applet due to Security Restrictions.\nIt modifies a thread group.\nContact Sun Microsystems for more information."));
		myPanel.add("South", southPanel);
		southPanel.add(myButton);
		if (VerdantiumApplet.isAppletActivated())
			myButton.setEnabled(false);
		southPanel.add(helpButton);
		if (myChooser != null)
			myChooser.setColor(target.getColor());

		colorL =
			Adapters.createGActionListener(this, "handleTargetColorChange");
		target.addColorActionListener(colorL);
		target.addPropertyChangeListener(this);

		ActionListener PanelL =
			Adapters.createGActionListener(this, "handleColorChange");
		myButton.addActionListener(PanelL);
		PanelL = Adapters.createGActionListener(this, "handleDisplayHelp");
		helpButton.addActionListener(PanelL);
	}

	/**
	* Handles the throwing of an error or exception.
	* @param in The input error or exception.
	*/
	public void handleThrow(Throwable in) {
		VerdantiumUtils.handleThrow(in, target, target);
	}

	/**
	 * Action listener called at the point the target ColorCell changes colors.
	 */
	private ActionListener colorL = null;

	/**
	 * The ColorCell being edited.
	 */
	private ColorCell target = null;
	
}

