package verdantium.core;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.beans.PropertyChangeEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import verdantium.EtherEvent;
import verdantium.EtherEventPropertySource;
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
* A property editor provides custom page settings independent of the normal page setup
* dialogs of a particular OS / printer driver.  This is particularly useful for defining pages sizes
* that are not captured by typical page setup dialogs.  For instance, this dialog can be used to
* define poster-sized (such as 96 inch by 48 inch) logical pages.
* <P>
* @author Thorn Green
*/
public class CustomPageSetupPropertyEditor
	extends PropertyEditAdapter
	implements ActionListener, ItemListener {
	
	/**
	* Property change event name indicating a change in the default page size.
	*/
	public static final String defaultPageSizeChanged =
		"defaultPageSizeChanged";
	
	/**
	* The panel in which the property editor lies.
	*/
	private JPanel myPan = new JPanel();
	
	/**
	* The model of the component being edited.
	*/
	private EtherEventPropertySource myPage = null;
	
	/**
	* TextField for changing the user page width of the document page.
	*/
	private JTextField UserPageWidthField = new JTextField();
	
	/**
	* TextField for changing the user page height of the document page.
	*/
	private JTextField UserPageHeightField = new JTextField();
	
	/**
	* TextField for changing the imageble x of the document page.
	*/
	private JTextField ImageableXField = new JTextField();
	
	/**
	* TextField for changing the imagable y of the document page.
	*/
	private JTextField ImageableYField = new JTextField();
	
	/**
	* TextField for changing the imageble width of the document page.
	*/
	private JTextField ImageableWidthField = new JTextField();
	
	/**
	* TextField for changing the imageable height of the document page.
	*/
	private JTextField ImageableHeightField = new JTextField();
	
	/**
	* Label indicating the width of the printer page.
	*/
	private JLabel PageWidthLabel = new JLabel();
	
	/**
	* Label indicating the height of the printer page.
	*/
	private JLabel PageHeightLabel = new JLabel();
	
	/**
	* Combo box for changing the measurement unit.
	*/
	private JComboBox<String> UnitBox = new JComboBox<String>();
	
	/**
	* The initial page format.
	*/
	private PageFormat initial = null;

	/**
	* Constructs the property editor for a given component and page format.
	* @param pg The initial page format.
	* @param in The model of the component being edited.
	*/
	public CustomPageSetupPropertyEditor(
		PageFormat pg,
		EtherEventPropertySource in) {
		myPage = in;
		initial = pg;
		myPage.addPropertyChangeListener(this);
		myPan.setLayout(new BorderLayout(0, 0));
		JButton ApplyButton = new JButton("Apply");
		myPan.add(BorderLayout.SOUTH, ApplyButton);

		JPanel pan2 = new JPanel();
		myPan.add(BorderLayout.CENTER, pan2);
		pan2.setLayout(new VerticalLayout(1));
		pan2.add("any", new JLabel("Measurement Unit : "));
		pan2.add("any", UnitBox);
		pan2.add("any", new JLabel("Page Width : "));
		pan2.add("any", UserPageWidthField);
		pan2.add("any", new JLabel("Page Height : "));
		pan2.add("any", UserPageHeightField);
		pan2.add("any", new JLabel("Imageable X : "));
		pan2.add("any", ImageableXField);
		pan2.add("any", new JLabel("Imageable Y : "));
		pan2.add("any", ImageableYField);
		pan2.add("any", new JLabel("Imageable Width : "));
		pan2.add("any", ImageableWidthField);
		pan2.add("any", new JLabel("Imageable Height : "));
		pan2.add("any", ImageableHeightField);
		pan2.add("any", new JLabel("Initial Printer Imageable Width : "));
		pan2.add("any", PageWidthLabel);
		pan2.add("any", new JLabel("Initial Printer Imageable Height : "));
		pan2.add("any", PageHeightLabel);

		UnitBox.setEditable(false);
		UnitBox.addItem("Inches");
		UnitBox.addItem("Feet");
		UnitBox.addItem("Centimeters");
		UnitBox.addItem("Points");
		UnitBox.addItem("Pixels");
		UnitBox.addItem("Picas");
		UnitBox.addItem("Millimeters");

		UserPageWidthField.setText(
			(new Double(pg.getWidth() / 72.0)).toString());
		UserPageHeightField.setText(
			(new Double(pg.getHeight() / 72.0)).toString());
		ImageableXField.setText(
			(new Double(pg.getImageableX() / 72.0)).toString());
		ImageableYField.setText(
			(new Double(pg.getImageableY() / 72.0)).toString());
		ImageableWidthField.setText(
			(new Double(pg.getImageableWidth() / 72.0)).toString());
		ImageableHeightField.setText(
			(new Double(pg.getImageableHeight() / 72.0)).toString());

		updateDefaultPageLabels();

		ApplyButton.addActionListener(this);
		UnitBox.addItemListener(this);
	}

	/**
	* Given the measurement unit in the JComboBox, alters the printer page
	* size labels to reflect the unit.
	*/
	protected void updateDefaultPageLabels() {
		PageFormat pf1 = initial;

		double wid = pf1.getImageableWidth();
		double hei = pf1.getImageableHeight();
		String str = (String) (UnitBox.getSelectedItem());

		if (str.equals("Inches")) {
			wid = wid / 72.0;
			hei = hei / 72.0;
		}

		if (str.equals("Feet")) {
			wid = wid / (72.0 * 12.0);
			hei = hei / (72.0 * 12.0);
		}

		if (str.equals("Centimeters")) {
			wid = wid / (72.0 / 2.54);
			hei = hei / (72.0 / 2.54);
		}

		if (str.equals("Points")) {
			wid = wid / 1.0;
			hei = hei / 1.0;
		}

		if (str.equals("Pixels")) {
			wid = wid / 1.0;
			hei = hei / 1.0;
		}

		if (str.equals("Picas")) {
			wid = wid / 12.0;
			hei = hei / 12.0;
		}

		if (str.equals("Millimeters")) {
			wid = wid / (72.0 / 25.4);
			hei = hei / (72.0 / 25.4);
		}

		PageWidthLabel.setText(wid + " " + str);
		PageHeightLabel.setText(hei + " " + str);
		PageWidthLabel.repaint();
		PageHeightLabel.repaint();
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
		if (e.getPropertyName() == defaultPageSizeChanged) {
			updateDefaultPageLabels();
		}

		if (e.getPropertyName() == ProgramDirector.propertyHide) {
			VerdantiumUtils.disposeContainer(this);
		}

	}

	/**
	* Handles a button-press event from the Apply button by changing
	* the size of the logical page the component supports.
	* @param e The input event.
	*/
	public void actionPerformed(ActionEvent e) {
		try {
			double uwid = Double.parseDouble(UserPageWidthField.getText());
			double uhei = Double.parseDouble(UserPageHeightField.getText());
			double imx = Double.parseDouble(ImageableXField.getText());
			double imy = Double.parseDouble(ImageableYField.getText());
			double iwid = Double.parseDouble(ImageableWidthField.getText());
			double ihei = Double.parseDouble(ImageableHeightField.getText());
			String str = (String) (UnitBox.getSelectedItem());

			if (uwid < 0.0)
				throw (
					new IllegalInputException("Page width must be a positive number."));

			if (uhei < 0.0)
				throw (
					new IllegalInputException("Page height must be a positive number."));

			if (imx < 0.0)
				throw (
					new IllegalInputException("Imageable X must be a positive number."));

			if (imy < 0.0)
				throw (
					new IllegalInputException("Imageable Y must be a positive number."));

			if (iwid < 0.0)
				throw (
					new IllegalInputException("Imageable width must be a positive number."));

			if (ihei < 0.0)
				throw (
					new IllegalInputException("Imageable height must be a positive number."));

			if (imx + iwid > uwid)
				throw (
					new IllegalInputException("Imageable X plus imageable width must be <BR>less than or equal to page width."));

			if (imy + ihei > uhei)
				throw (
					new IllegalInputException("Imageable Y plus imageable height must be <BR>less than or equal to page height."));

			if (str.equals("Inches")) {
				uwid = uwid * 72.0;
				uhei = uhei * 72.0;
				imx = imx * 72.0;
				imy = imy * 72.0;
				iwid = iwid * 72.0;
				ihei = ihei * 72.0;
			}

			if (str.equals("Feet")) {
				uwid = uwid * (72.0 * 12.0);
				uhei = uhei * (72.0 * 12.0);
				imx = imx * (72.0 * 12.0);
				imy = imy * (72.0 * 12.0);
				iwid = iwid * (72.0 * 12.0);
				ihei = ihei * (72.0 * 12.0);
			}

			if (str.equals("Centimeters")) {
				uwid = uwid * (72.0 / 2.54);
				uhei = uhei * (72.0 / 2.54);
				imx = imx * (72.0 / 2.54);
				imy = imy * (72.0 / 2.54);
				iwid = iwid * (72.0 / 2.54);
				ihei = ihei * (72.0 / 2.54);
			}

			if (str.equals("Points")) {
				uwid = uwid * 1.0;
				uhei = uhei * 1.0;
				imx = imx * 1.0;
				imy = imy * 1.0;
				iwid = iwid * 1.0;
				ihei = ihei * 1.0;
			}

			if (str.equals("Pixels")) {
				uwid = uwid * 1.0;
				uhei = uhei * 1.0;
				imx = imx * 1.0;
				imy = imy * 1.0;
				iwid = iwid * 1.0;
				ihei = ihei * 1.0;
			}

			if (str.equals("Picas")) {
				uwid = uwid * 12.0;
				uhei = uhei * 12.0;
				imx = imx * 12.0;
				imy = imy * 12.0;
				iwid = iwid * 12.0;
				ihei = ihei * 12.0;
			}

			if (str.equals("Millimeters")) {
				uwid = uwid * (72.0 / 25.4);
				uhei = uhei * (72.0 / 25.4);
				imx = imx * (72.0 / 25.4);
				imy = imy * (72.0 / 25.4);
				iwid = iwid * (72.0 / 25.4);
				ihei = ihei * (72.0 / 25.4);
			}

			Paper paper = new Paper();
			paper.setSize(uwid, uhei);
			paper.setImageableArea(imx, imy, iwid, ihei);
			PageFormat pf = new PageFormat();
			pf.setPaper(paper);

			System.out.println(pf.getImageableWidth());

			EtherEvent s3 =
				new StandardEtherEvent(
					"Program Director",
					StandardEtherEvent.setDocPageFormat,
					pf,
					myPage);
			ProgramDirector.fireEtherEvent(s3, null);
		} catch (NumberFormatException ex) {
			handleThrow(
				new IllegalInputException(
					"Please put a number in all numeric fields.",
					ex));
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	
	/**
	* Handles the throwing of an error or exception.
	* @param in The input error or exception.
	*/
	public void handleThrow(Throwable in) {
		VerdantiumUtils.handleThrow(in, this, myPage);
	}

	
}

