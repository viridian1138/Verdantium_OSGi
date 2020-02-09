package verdantium.demo.gradientpane;

import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Properties;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.EtherEventPropertySource;
import verdantium.ProgramDirector;
import verdantium.core.ColorCell;
import verdantium.core.DefaultPropertyEditor;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.utils.VerticalLayout;

//$$strtCprt
/*
     Gradient Pane rendering component by Thorn Green
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
* This is the property editor class for {@link GradientPane}.
* <P>
* @author Thorn Green
*/
public class GradientPanePropertyEditor extends DefaultPropertyEditor implements PropertyChangeListener {
	/**
	* Color editor for the first color.
	*/
	private ColorCell coACell;
	/**
	* Color editor for the second color.
	*/
	private ColorCell coBCell;
	/**
	* Check box for setting the orientation of the gradient.
	*/
	private JCheckBox vertical;

	/**
	* Set to true when updating a property to avoid recursion.
	*/
	private boolean updating = false;

	/**
	* Handles the destruction of the component by calling handleDestroy() on all
	* embedded components.
	*/
	public void handleDestroy() {
		super.handleDestroy();
		coACell.handleDestroy();
		coBCell.handleDestroy();
	}

	/**
	* Handles property change events by updating the display of the
	* appropriate properties.
	* @param e The input event.
	*/
	public void propertyChange(PropertyChangeEvent e) {
		super.propertyChange(e);

		if (e.getPropertyName() == GradientPane.GradientPaneVertical)
			handleVerticalChange(e);

		if (e.getPropertyName() == GradientPane.GradientPaneColors)
			handleTargetColorChange(e);
	}

	/**
	* Handles a change in the gradient component's first or second color.
	* @param e The input event.
	*/
	protected void handleTargetColorChange(PropertyChangeEvent e) {
		if (!updating) {
			updating = true;
			coACell.setColor(target.getCoA());
			coBCell.setColor(target.getCoB());
			updating = false;
			MyPanel.repaint();
		}

	}

	/**
	* Handles a change in the drawing mode.
	* @param e The input event.
	*/
	protected void handleVerticalChange(PropertyChangeEvent e) {
		if (!updating) {
			updating = true;

			vertical.setSelected(target.getVertical());

			updating = false;
			MyPanel.repaint();
		}

	}

	/**
	* Handles a color change in the first color cell.
	* @param e The input event.
	*/
	public void handleCoACellColorChange(ActionEvent e) {
		if (!updating) {
			try {
				updating = true;
				EtherEvent send = new PropertyEditEtherEvent(this, GradientPane.setGradientPaneColors, null, target);
				Object[] param = { coACell.getColor(), target.getCoB()};
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
	* Handles a color change in the second color cell.
	* @param e The input event.
	*/
	public void handleCoBCellColorChange(ActionEvent e) {
		if (!updating) {
			try {
				updating = true;
				EtherEvent send = new PropertyEditEtherEvent(this, GradientPane.setGradientPaneColors, null, target);
				Object[] param = { target.getCoA(), coBCell.getColor()};
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);

				updating = false;
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
	* Handles the changing of the vertical/horizontal mode in the property editor.
	* @param in The input event.
	*/
	public void handleVerticalButton(ActionEvent in) {
		try {
			Boolean MyB = new Boolean(vertical.getModel().isSelected());

			EtherEvent send = new PropertyEditEtherEvent(this, GradientPane.setGradientPaneVertical, null, target);
			send.setParameter(MyB);
			ProgramDirector.fireEtherEvent(send, null);
		}
		catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Shunts the superclass method for adding the tabs.
	* @param in The property source from which to define the tabs.
	* @param inp Properties defining which tabs to display.
	*/
	protected void addTabs(EtherEventPropertySource in, Properties inp) {}

	/**
	* Adds the tabs to the property editor needed to display GradientPane-specific properties.
	* @param in The property source from which to define the tabs.
	* @param inp Properties defining which tabs to display.
	*/
	protected void addTabsSub(EtherEventPropertySource in, Properties inp) {
		target = (GradientPane) in;
		coACell = new ColorCell();
		coBCell = new ColorCell();
		coACell.setOutlineBorder();
		coBCell.setOutlineBorder();

		if (!lacksProperty("Scrolling", inp))
			inp.put("NoTransparent", in);

		JPanel p3 = new JPanel();
		TabPane.add("Colors", p3);
		p3.setLayout(new VerticalLayout(1));
		p3.add("any", new JLabel("Gradient Color #1 : "));
		p3.add("any", coACell.getGUI());
		p3.add("any", new JLabel("Gradient Color #2 : "));
		p3.add("any", coBCell.getGUI());
		vertical = new JCheckBox("Vertical Gradient", true);
		p3.add("any", vertical);

		super.addTabs(in, inp);

		coACell.setColor(target.getCoA());
		coBCell.setColor(target.getCoB());
		vertical.setSelected(target.getVertical());

		ActionListener CellL = Adapters.createGActionListener(this, "handleCoACellColorChange");
		coACell.addColorActionListener(CellL);
		CellL = Adapters.createGActionListener(this, "handleCoBCellColorChange");
		coBCell.addColorActionListener(CellL);

		ActionListener PenL = Adapters.createGActionListener(this, "handleVerticalButton");
		vertical.addActionListener(PenL);
	}

	/**
	* Constructs the property editor.
	* @param in The component to be edited.
	* @param inp Properties defining which tabs to display.
	*/
	public GradientPanePropertyEditor(GradientPane in, Properties inp) {
		super(in, inp);
		addTabsSub(in, inp);
	}

	/**
	* Returns the color cell for the first color.
	* @return The first cell color.
	*/
	public ColorCell getCoACell() {
		return (coACell);
	}

	/**
	* Returns the color cell for the second color.
	* @return The second cell color.
	*/
	public ColorCell getCoBCell() {
		return (coBCell);
	}

	/**
	* Returns the data flavors supported by the property editor for persistence, which are none.
	* @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		return (null);
	}

	/**
	* The GradientPane being edited by the property editor.
	*/
	private GradientPane target;
}
