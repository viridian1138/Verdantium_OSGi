package verdantium.demo.poseidon;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.EtherEventPropertySource;
import verdantium.ProgramDirector;
import verdantium.core.ColorCell;
import verdantium.core.DefaultPropertyEditor;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.utils.IllegalInputException;
import verdantium.utils.VerticalLayout;

//$$strtCprt
/*
     Poseidon ripple-tank simulator by Thorn Green
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
*    | 12/06/2001            | Thorn Green (viridian_1138@yahoo.com)           | Need more specific error messages.                                   | Added more specific error messages.
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
* This is the property editor class for {@link Poseidon}.
* <P>
* @author Thorn Green
*/
public class PoseidonPropertyEditor extends DefaultPropertyEditor implements PropertyChangeListener {
	
	/**
	* ColorCell used to edit the peak color of the displayed wave.
	*/
	private ColorCell TopColorCell;
	
	/**
	* ColorCell used to edit the middle color of the displayed wave.
	*/
	private ColorCell MidColorCell;
	
	/**
	* ColorCell used to edit the bottom color of the dislayed wave.
	*/
	private ColorCell BottomColorCell;
	
	/**
	* TextField used to set the pixel resolution of the wave display.
	*/
	private JTextField ResField;
	
	/**
	* TextField used to enter the magnitude of the wave.
	*/
	private JTextField MagField;
	
	/**
	* TextField used to enter the period of the wave.
	*/
	private JTextField PeriodField;
	
	/**
	* TextField used to enter the phase of the wave.
	*/
	private JTextField PhaseField;
	
	/**
	* The point where the user clicked to create the property editor.  Used to set wave position.
	*/
	private Point ClickPoint;

	/**
	* Flag used to indicate that properties are being updated, and property change events should be ignored.
	*/
	private boolean updating = false;

	/**
	* Handles the destruction of the component by calling handleDestroy() on all embedded components.
	*/
	public void handleDestroy() {
		super.handleDestroy();
		TopColorCell.handleDestroy();
		MidColorCell.handleDestroy();
		BottomColorCell.handleDestroy();
	}

	/**
	* Handles a color property change event by updating the embedded color cells.
	* @param e The input event.
	*/
	public void propertyChange(PropertyChangeEvent e) {
		super.propertyChange(e);

		if (e.getPropertyName() == Poseidon.PoseidonColors)
			handleTargetColorChange(e);
	}

	/**
	* Handles a color change requested by some other property editor.
	* @param e The input event.
	*/
	protected void handleTargetColorChange(PropertyChangeEvent e) {
		if (!updating) {
			updating = true;
			TopColorCell.setColor(target.getTopColor());
			MidColorCell.setColor(target.getMidColor());
			BottomColorCell.setColor(target.getBottomColor());
			MyPanel.repaint();
			updating = false;
		}

	}

	/**
	* Handles a user request to change the top wave color.
	* @param e The input event.
	*/
	public void handleTopCellColorChange(ActionEvent e) {
		if (!updating) {
			try {
				updating = true;
				EtherEvent send = new PropertyEditEtherEvent(this, Poseidon.setPoseidonColors, null, target);
				Object[] param = { target.getBottomColor(), target.getMidColor(), TopColorCell.getColor()};
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);
			}
			catch (Throwable ex) {
				handleThrow(ex);
			}
			finally {
				updating = false;
			}
		}
	}

	/**
	* Handles a user request to change the middle wave color.
	* @param e The input event.
	*/
	public void handleMidCellColorChange(ActionEvent e) {
		if (!updating) {
			try {
				updating = true;
				EtherEvent send = new PropertyEditEtherEvent(this, Poseidon.setPoseidonColors, null, target);
				Object[] param = { target.getBottomColor(), MidColorCell.getColor(), target.getTopColor()};
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);
			}
			catch (Throwable ex) {
				handleThrow(ex);
			}
			finally {
				updating = false;
			}
		}
	}

	/**
	* Handles a user request to change the bottom wave color.
	* @param e The input event.
	*/
	public void handleBottomCellColorChange(ActionEvent e) {
		if (!updating) {
			try {
				updating = true;
				EtherEvent send = new PropertyEditEtherEvent(this, Poseidon.setPoseidonColors, null, target);
				Object[] param = { BottomColorCell.getColor(), target.getMidColor(), target.getTopColor()};
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);
			}
			catch (Throwable ex) {
				handleThrow(ex);
			}
			finally {
				updating = false;
			}
		}
	}

	/**
	* Handles a user request to change the resolution of the wave display.
	* @param evt The input event.
	*/
	public void handleResApplyButton(ActionEvent evt) {
		if (!updating) {
			try {
				updating = true;

				EtherEvent send = new PropertyEditEtherEvent(this, Poseidon.setPoseidonResolution, null, target);
				Integer param = new Integer(ResField.getText());
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);
			}
			catch (NumberFormatException ex) {
				handleThrow(new IllegalInputException("The resolution must be a number.", ex));
			}
			catch (Throwable ex) {
				handleThrow(ex);
			}
			finally {
				updating = false;
			}
		}
	}

	/**
	* Handles a user request to add a wave to the display.
	* @param e The input event.
	*/
	public void handleAddApplyButton(ActionEvent evt) {
		try {
			Integer x = new Integer(ClickPoint.x);
			Integer y = new Integer(ClickPoint.y);
			Double magnitude = new Double(MagField.getText());
			Double period = new Double(PeriodField.getText());
			Double phase = new Double(PhaseField.getText());
			EtherEvent send = new PropertyEditEtherEvent(this, Poseidon.addPoseidonWave, null, target);
			Object[] param = { x, y, magnitude, period, phase };
			send.setParameter(param);
			ProgramDirector.fireEtherEvent(send, null);
		}
		catch (NumberFormatException ex) {
			handleThrow(new IllegalInputException("Something input was not a number.", ex));
		}
		catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles a user request to remove a wave from the display.
	* @param evt The input event.
	*/
	public void handleRemoveApplyButton(ActionEvent evt) {
		try {
			EtherEvent send = new PropertyEditEtherEvent(this, Poseidon.removeLastWave, null, target);
			ProgramDirector.fireEtherEvent(send, null);
		}
		catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Adds the tabs to the property editor needed to display Poseidon-specific properties.
	* @param in The property source from which to define the tabs.
	* @param inp Properties defining which tabs to display.
	*/
	protected void addTabs(EtherEventPropertySource in, Properties inp) {
		target = (Poseidon) in;
		TopColorCell = new ColorCell();
		MidColorCell = new ColorCell();
		BottomColorCell = new ColorCell();
		TopColorCell.setOutlineBorder();
		MidColorCell.setOutlineBorder();
		BottomColorCell.setOutlineBorder();

		if (!lacksProperty("Scrolling", inp))
			inp.put("NoTransparent", in);

		inp.put("NoEditControl", in);

		JPanel p3 = new JPanel();
		TabPane.add("Colors", p3);
		p3.setLayout(new VerticalLayout(1));
		p3.add("any", new JLabel("Top Color: "));
		p3.add("any", TopColorCell.getGUI());
		p3.add("any", new JLabel("Mid Color: "));
		p3.add("any", MidColorCell.getGUI());
		p3.add("any", new JLabel("Bottom Color: "));
		p3.add("any", BottomColorCell.getGUI());

		JPanel p4 = new JPanel();
		TabPane.add("Resolution", p4);
		p4.setLayout(new BorderLayout(0, 0));
		ResField = new JTextField("100");
		p4.add("North", ResField);
		JButton ResApplyButton = new JButton("Apply");
		p4.add("South", ResApplyButton);

		JPanel p5 = new JPanel();
		TabPane.add("Remove Wave", p5);
		p5.setLayout(new BorderLayout(0, 0));
		JButton RemoveApplyButton = new JButton("Apply");
		p5.add("South", RemoveApplyButton);

		JPanel p6 = new JPanel();
		TabPane.add("Add Wave", p6);
		p6.setLayout(new BorderLayout(0, 0));
		JButton AddApplyButton = new JButton("Apply");
		p6.add("South", AddApplyButton);
		JPanel temp = new JPanel();
		p6.add("North", temp);
		temp.setLayout(new VerticalLayout(1));
		temp.add("any", new JLabel("Magnitude"));
		MagField = new JTextField("1.0");
		temp.add("any", MagField);
		temp.add("any", new JLabel("Period"));
		PeriodField = new JTextField("0.125");
		temp.add("any", PeriodField);
		temp.add("any", new JLabel("Phase"));
		PhaseField = new JTextField("0.0");
		temp.add("any", PhaseField);

		super.addTabs(in, inp);

		TopColorCell.setColor(target.getTopColor());
		MidColorCell.setColor(target.getMidColor());
		BottomColorCell.setColor(target.getBottomColor());

		ActionListener ButtonL = Adapters.createGActionListener(this, "handleResApplyButton");
		ResApplyButton.addActionListener(ButtonL);

		ButtonL = Adapters.createGActionListener(this, "handleRemoveApplyButton");
		RemoveApplyButton.addActionListener(ButtonL);

		ButtonL = Adapters.createGActionListener(this, "handleAddApplyButton");
		AddApplyButton.addActionListener(ButtonL);

		ActionListener CellL = Adapters.createGActionListener(this, "handleTopCellColorChange");
		TopColorCell.addColorActionListener(CellL);
		CellL = Adapters.createGActionListener(this, "handleMidCellColorChange");
		MidColorCell.addColorActionListener(CellL);
		CellL = Adapters.createGActionListener(this, "handleBottomCellColorChange");
		BottomColorCell.addColorActionListener(CellL);
	}

	/**
	 * Sets the place where the user clicked to create a property editor.
	 * @param The place where the user clicked to create a property editor.
	 */
	public void setClickPoint(Point InPt) {
		super.setClickPoint(InPt);
		ClickPoint = InPt;
	}

	/**
	* Constructs the property editor.
	* @param in The component to be edited.
	* @param inp Properties defining which tabs to display.
	*/
	public PoseidonPropertyEditor(Poseidon in, Properties inp) {
		super(in, inp);
	}

	/**
	* Returns the top color cell of the property editor.
	* @return The top color cell of the property editor.
	*/
	public ColorCell getTopColorCell() {
		return (TopColorCell);
	}

	/**
	* Returns the mid color cell of the property editor.
	* @return The mid color cell of the property editor.
	*/
	public ColorCell getMidColorCell() {
		return (MidColorCell);
	}

	/**
	* Returns the bottom color cell of the property editor.
	* @return The bottom color cell of the property editor.
	*/
	public ColorCell getBottomColorCell() {
		return (BottomColorCell);
	}

	/**
	* Returns the data flavors supported by the property editor for persistence, which are none.
	* @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		return (null);
	}

	/**
	* Holds the instance of the component that this editor edits.
	*/
	private Poseidon target;
	
}

