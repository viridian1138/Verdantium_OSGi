package verdantium.standard.showlights;

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
import verdantium.undo.UTag;
import verdantium.undo.UndoManager;
import verdantium.utils.IllegalInputException;
import verdantium.utils.VerticalLayout;

//$$strtCprt
/*
 ShowLights animated marquee by Thorn Green
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
* This is the property editor class for {@link ShowLights}.
* <P>
* @author Thorn Green
*/
public class ShowLightsPropertyEditor extends DefaultPropertyEditor implements PropertyChangeListener {

	/**
	* ColorCell used to edit the "on" color.
	*/
	private ColorCell topColorCell;
	/**
	* ColorCell used to edit the "off" color.
	*/
	private ColorCell bottomColorCell;
	/**
	* TextField used to set the pixel size of each grid.
	*/
	private JTextField resField;
	/**
	* TextField used to set the pixel inset of each grid cell.
	*/
	private JTextField insetField;
	/**
	* TextField used to set the delay used in rendering.
	*/
	private JTextField delayField;
	/**
	* TextField used to set the number of frames used in rendering.
	*/
	private JTextField numFramesField;
	/**
	* The point where the user clicked to create the property editor.
	*/
	private Point clickPoint;

	/**
	* Flag used to indicate that properties are being updated, and property change events should be ignored.
	*/
	private boolean updating = false;

	/**
	* Handles the destruction of the component by calling handleDestroy() on all embedded components.
	*/
	public void handleDestroy() {
		super.handleDestroy();
		topColorCell.handleDestroy();
		bottomColorCell.handleDestroy();
	}

	/**
	* Handles a color property change event by updating the embedded color cells.
	* @param e The input property change event.
	*/
	public void propertyChange(PropertyChangeEvent e) {
		super.propertyChange(e);

		if (e.getPropertyName() == ShowLights.ShowLightsColors)
			handleTargetColorChange(e);
	}

	/**
	* Handles a color change requested by some other property editor.
	* @param e The input property change event.
	*/
	protected void handleTargetColorChange(PropertyChangeEvent e) {
		if (!updating) {
			updating = true;
			topColorCell.setColor(target.getTopColor());
			bottomColorCell.setColor(target.getBottomColor());
			MyPanel.repaint();
			updating = false;
		}

	}

	/**
	* Handles a user request to change the "on" color.
	* @param e The input event.
	*/
	public void handleTopCellColorChange(ActionEvent e) {
		if (!updating) {
			try {
				updating = true;
				EtherEvent send = new PropertyEditEtherEvent(this, ShowLights.setShowLightsColors, null, target);
				Object[] param = { target.getBottomColor(), topColorCell.getColor()};
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
	* Handles a user request to change the "off" color.
	* @param e The input event.
	*/
	public void handleBottomCellColorChange(ActionEvent e) {
		if (!updating) {
			try {
				updating = true;
				EtherEvent send = new PropertyEditEtherEvent(this, ShowLights.setShowLightsColors, null, target);
				Object[] param = { bottomColorCell.getColor(), target.getTopColor()};
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
	* Handles a user request to change the properties of the ShowLights display.
	* @param evt The input event.
	*/
	public void handleResApplyButton(ActionEvent evt) {
		if (!updating) {
			try {
				updating = true;
				
				UndoManager undoMgr = getUndoManager();
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);

				EtherEvent send = new PropertyEditEtherEvent(this, ShowLights.setShowLightsResolution, null, target);
				Integer param = new Integer(resField.getText());
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);

				send = new PropertyEditEtherEvent(this, ShowLights.setShowLightsInset, null, target);
				param = new Integer(insetField.getText());
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);

				send = new PropertyEditEtherEvent(this, ShowLights.setShowLightsDelay, null, target);
				param = new Integer(delayField.getText());
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);

				send = new PropertyEditEtherEvent(this, ShowLights.setShowLightsNumFrames, null, target);
				param = new Integer(numFramesField.getText());
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);
				
				undoMgr.commitUndoableOp( utag,"Resolution Properties Change" );
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
	* Adds the tabs to the property editor needed to display ShowLights-specific properties.
	* @param in The property source from which to define the tabs.
	* @param inp Properties defining which tabs to display.
	*/
	protected void addTabs(EtherEventPropertySource in, Properties inp) {
		target = (ShowLights) in;
		topColorCell = new ColorCell();
		bottomColorCell = new ColorCell();
		topColorCell.setOutlineBorder();
		bottomColorCell.setOutlineBorder();

		if (!lacksProperty("Scrolling", inp))
			inp.put("NoTransparent", in);

		inp.put("NoEditControl", in);

		JPanel p3 = new JPanel();
		TabPane.add("Colors", p3);
		p3.setLayout(new VerticalLayout(1));
		p3.add("any", new JLabel("Top Color: "));
		p3.add("any", topColorCell.getGUI());
		p3.add("any", new JLabel("Bottom Color: "));
		p3.add("any", bottomColorCell.getGUI());

		JPanel p4 = new JPanel();
		TabPane.add("Resolution", p4);
		p4.setLayout(new BorderLayout(0, 0));
		JPanel p5 = new JPanel();
		p5.setLayout(new VerticalLayout(0));
		resField = new JTextField("50");
		p4.add("North", p5);
		p5.add("any", new JLabel("Resolution"));
		p5.add("any", resField);
		insetField = new JTextField("5");
		p5.add("any", new JLabel("Inset (Pixels)"));
		p5.add("any", insetField);
		delayField = new JTextField("100");
		p5.add("any", new JLabel("Animation Delay (Milliseconds)"));
		p5.add("any", delayField);
		numFramesField = new JTextField("6");
		p5.add("any", new JLabel("Number Of Frames"));
		p5.add("any", numFramesField);
		JButton ResApplyButton = new JButton("Apply");
		p4.add("South", ResApplyButton);

		super.addTabs(in, inp);

		topColorCell.setColor(target.getTopColor());
		bottomColorCell.setColor(target.getBottomColor());

		ActionListener ButtonL = Adapters.createGActionListener(this, "handleResApplyButton");
		ResApplyButton.addActionListener(ButtonL);

		ActionListener CellL = Adapters.createGActionListener(this, "handleTopCellColorChange");
		topColorCell.addColorActionListener(CellL);
		CellL = Adapters.createGActionListener(this, "handleBottomCellColorChange");
		bottomColorCell.addColorActionListener(CellL);
	}

	/**
	* Sets the place where the user clicked to create a property editor.
	* @param The place where the user clicked to create a property editor.
	*/
	public void setClickPoint(Point InPt) {
		super.setClickPoint(InPt);
		clickPoint = InPt;
	}

	/**
	* Constructs the property editor.
	* @param in The component to be edited.
	* @param inp Properties defining which tabs to display.
	*/
	public ShowLightsPropertyEditor(ShowLights in, Properties inp) {
		super(in, inp);
	}

	/**
	* Returns the top color cell of the property editor.
	* @return The top cell color.
	*/
	public ColorCell getTopColorCell() {
		return (topColorCell);
	}

	/**
	* Returns the bottom color cell of the property editor.
	* @return The bottom cell color.
	*/
	public ColorCell getBottomColorCell() {
		return (bottomColorCell);
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
	private ShowLights target;
}

