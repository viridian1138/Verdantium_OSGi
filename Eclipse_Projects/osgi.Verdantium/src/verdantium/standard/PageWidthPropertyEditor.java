package verdantium.standard;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import meta.WrapRuntimeException;
import verdantium.EtherEvent;
import verdantium.EtherEventHandler;
import verdantium.ProgramDirector;
import verdantium.StandardEtherEvent;
import verdantium.VerdantiumUtils;
import verdantium.utils.IllegalInputException;
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
* A property editor that allows the size of the document page width to be changed for
* a client scrolling version of {@link TextApp}.
* <P>
* @author Thorn Green
*/
public class PageWidthPropertyEditor
	extends PropertyEditAdapter
	implements ActionListener, ItemListener {
	
	/**
	* Property change event name indicating a change in the default page width.
	*/
	public static final String defaultPageWidthChanged =
		"defaultPageWidthChanged";
	
	/**
	* The panel in which the property editor lies.
	*/
	private JPanel myPan = new JPanel();
	
	/**
	* The TextApp being edited.
	*/
	private TextApp myPage = null;
	
	/**
	* TextField for changing the width of the document page.
	*/
	private JTextField widthField = new JTextField();
	
	/**
	* Label indicating the width of the printer page.
	*/
	private JLabel pageWidthLabel = new JLabel();
	
	/**
	* Combo box for changing the measurement unit.
	*/
	private JComboBox<String> unitBox = new JComboBox<String>();

	/**
	* Constructs the property editor for a given TextApp.
	* @param in The TextApp being edited.
	*/
	public PageWidthPropertyEditor(TextApp in) {
		myPage = in;
		myPan.setLayout(new BorderLayout(0, 0));
		JButton applyButton = new JButton("Apply");
		myPan.add(BorderLayout.SOUTH, applyButton);

		JPanel pan2 = new JPanel();
		myPan.add(BorderLayout.CENTER, pan2);
		pan2.setLayout(new VerticalLayout(1));
		pan2.add("any", new JLabel("Measurement Unit : "));
		pan2.add("any", unitBox);
		pan2.add("any", new JLabel("Width : "));
		pan2.add("any", widthField);
		pan2.add("any", new JLabel("Default Printer Page Width : "));
		pan2.add("any", pageWidthLabel);

		unitBox.setEditable(false);
		unitBox.addItem("Inches");
		unitBox.addItem("Centimeters");
		unitBox.addItem("Points");
		unitBox.addItem("Pixels");
		unitBox.addItem("Picas");
		unitBox.addItem("Millimeters");

		updateDefaultPageLabels();

		in.addPropertyChangeListener(this);
		applyButton.addActionListener(this);
		unitBox.addItemListener(this);
	}

	/**
	* Given the measurement unit in the JComboBox, alters the printer page
	* size labels to reflect the unit.
	*/
	protected void updateDefaultPageLabels() {
		try {
			PrinterJob job = PrinterJob.getPrinterJob();
			PageFormat pf1 = null;
			if (myPage instanceof EtherEventHandler) {
				EtherEvent s3 =
					new StandardEtherEvent(
						"Program Director",
						StandardEtherEvent.getDocPageFormat,
						null,
						(EtherEventHandler) myPage);
				Object r3 = ProgramDirector.fireEtherEvent(s3, null);
				pf1 = (PageFormat) (r3);
			}
			if (pf1 == null) {
				pf1 = job.defaultPage();
			}
			pf1 = job.validatePage(pf1);

			double wid = pf1.getImageableWidth();
			String str = (String) (unitBox.getSelectedItem());

			if (str.equals("Inches")) {
				wid = wid / 72.0;
			}

			if (str.equals("Centimeters")) {
				wid = wid / (72.0 / 2.54);
			}

			if (str.equals("Points")) {
				wid = wid / 1.0;
			}

			if (str.equals("Pixels")) {
				wid = wid / 1.0;
			}

			if (str.equals("Picas")) {
				wid = wid / 12.0;
			}

			if (str.equals("Millimeters")) {
				wid = wid / (72.0 / 25.4);
			}

			pageWidthLabel.setText((new Double(wid)).toString() + " " + str);
			pageWidthLabel.repaint();
		} catch (Throwable ex) {
			throw (new WrapRuntimeException("Page Wid. Failed", ex));
		}
	}

	/**
	* Gets the GUI of the property editor.
	* @return The GUI of the property editor.
	*/
	public JComponent getGUI() {
		return (myPan);
	}

	/**
	* Handles the destruction of the component bu removing appropriate property change listeners.
	*/
	public void handleDestroy() {
		myPage.removePropertyChangeListener(this);
	}

	/**
	* Updates all necessary labels to reflect a change in the measurement unit
	* in the property editor's combo box.
	* @param e The input event.
	*/
	public void itemStateChanged(ItemEvent e) {
		updateDefaultPageLabels();
	}

	/**
	* Handles property change events by updating the display of the
	* appropriate properties.
	* @param e The input event.
	*/
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getPropertyName() == defaultPageWidthChanged) {
			updateDefaultPageLabels();
		}

	}

	/**
	* Handles a button-press event from the Apply button by changing
	* the size of the client page size handler.
	* @param e The input event.
	*/
	public void actionPerformed(ActionEvent e) {
		try {
			double wid = Double.parseDouble(widthField.getText());
			String str = (String) (unitBox.getSelectedItem());

			if (str.equals("Inches")) {
				wid = wid * 72.0;
			}

			if (str.equals("Centimeters")) {
				wid = wid * (72.0 / 2.54);
			}

			if (str.equals("Points")) {
				wid = wid * 1.0;
			}

			if (str.equals("Pixels")) {
				wid = wid * 1.0;
			}

			if (str.equals("Picas")) {
				wid = wid * 12.0;
			}

			if (str.equals("Millimeters")) {
				wid = wid * (72.0 / 25.4);
			}

			myPage.alterPageWidth((int) wid);
		} catch (NumberFormatException ex) {
			handleThrow(
				new IllegalInputException(
					"The page width must be a number.",
					ex));
		} catch (IllegalInputException ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles the throwing of an error or exception.
	* @param in The error or exception to handle.
	*/
	public void handleThrow(Throwable in) {
		VerdantiumUtils.handleThrow(in, myPage, myPage);
	}

	
}

