package verdantium.standard.flowbox;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
 FlowBox diagram animation by Thorn Green
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
 * This is the property editor class for {@link FlowBox}.
 * <P>
 * 
 * @author Thorn Green
 */
public class FlowBoxPropertyEditor extends DefaultPropertyEditor implements
		PropertyChangeListener {
	/**
	 * Radio button to set the up mode.
	 */
	private JRadioButton UpModeButton;

	/**
	 * Radio button to set the down mode.
	 */
	private JRadioButton DownModeButton;

	/**
	 * Radio button to set the left mode.
	 */
	private JRadioButton LeftModeButton;

	/**
	 * Radio button to set the right mode.
	 */
	private JRadioButton RightModeButton;

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
	 * TextField used to set the size of the color table.
	 */
	private JTextField TableSizeField;

	/**
	 * TextField used to set the delay used in rendering.
	 */
	private JTextField DelayField;

	/**
	 * TextField used to set the number of frames used in rendering.
	 */
	private JTextField NumFramesField;

	/**
	 * The point where the user clicked to create the property editor. Used to
	 * set wave position.
	 */
	private Point ClickPoint;

	/**
	 * Flag used to indicate that properties are being updated, and property
	 * change events should be ignored.
	 */
	private boolean updating = false;

	/**
	 * Handles the destruction of the component by calling handleDestroy() on
	 * all embedded components.
	 */
	public void handleDestroy() {
		super.handleDestroy();
		TopColorCell.handleDestroy();
		MidColorCell.handleDestroy();
		BottomColorCell.handleDestroy();
	}

	/**
	 * Handles a color property change event by updating the embedded color
	 * cells.
	 * @param e The input event.
	 */
	public void propertyChange(PropertyChangeEvent e) {
		super.propertyChange(e);

		if (e.getPropertyName() == FlowBox.FlowBoxColors)
			handleTargetColorChange(e);

		if (e.getPropertyName() == FlowBox.FlowBoxMode)
			handleModeChange(e);
	}

	/**
	 * Handles a change in the drawing mode.
	 * @param e The input event.
	 */
	protected void handleModeChange(PropertyChangeEvent e) {
		if (!updating) {
			updating = true;
			switch (target.getEditMode()) {
			case FlowBox.UpMode:
				UpModeButton.getModel().setSelected(true);
				break;

			case FlowBox.DownMode:
				DownModeButton.getModel().setSelected(true);
				break;

			case FlowBox.LeftMode:
				LeftModeButton.getModel().setSelected(true);
				break;

			case FlowBox.RightMode:
				RightModeButton.getModel().setSelected(true);
				break;

			}
			updating = false;
			MyPanel.repaint();
		}

	}

	/**
	 * Handles one of the buttons for a mode change.
	 * @param evt The input event.
	 */
	public void handleButton(ItemEvent evt) {
		if (!updating) {
			try {
				if (((JRadioButton) (evt.getSource())).getModel().isSelected()) {
					updating = true;
					if (UpModeButton.getModel().isSelected())
						setEditMode(FlowBox.UpMode);
					if (DownModeButton.getModel().isSelected())
						setEditMode(FlowBox.DownMode);
					if (LeftModeButton.getModel().isSelected())
						setEditMode(FlowBox.LeftMode);
					if (RightModeButton.getModel().isSelected())
						setEditMode(FlowBox.RightMode);
				}
			} catch (Throwable ex) {
				handleThrow(ex);
			} finally {
				updating = false;
			}
		}
	}

	/**
	 * Handles a change in the editing mode of the drawing app.
	 * @param in The input editing mode.
	 */
	protected void setEditMode(int in) {
		try {
			EtherEvent send = new PropertyEditEtherEvent(this,
					FlowBox.setFlowBoxMode, null, target);
			send.setParameter(new Integer(in));
			ProgramDirector.fireEtherEvent(send, null);
		} catch (Throwable ex) {
			handleThrow(ex);
		}
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
				EtherEvent send = new PropertyEditEtherEvent(this,
						FlowBox.setFlowBoxColors, null, target);
				Object[] param = { target.getBottomColor(),
						target.getMidColor(), TopColorCell.getColor() };
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);
			} catch (Throwable ex) {
				handleThrow(ex);
			} finally {
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
				EtherEvent send = new PropertyEditEtherEvent(this,
						FlowBox.setFlowBoxColors, null, target);
				Object[] param = { target.getBottomColor(),
						MidColorCell.getColor(), target.getTopColor() };
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);
			} catch (Throwable ex) {
				handleThrow(ex);
			} finally {
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
				EtherEvent send = new PropertyEditEtherEvent(this,
						FlowBox.setFlowBoxColors, null, target);
				Object[] param = { BottomColorCell.getColor(),
						target.getMidColor(), target.getTopColor() };
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);
			} catch (Throwable ex) {
				handleThrow(ex);
			} finally {
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

				UndoManager undoMgr = getUndoManager();
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);

				EtherEvent send = new PropertyEditEtherEvent(this,
						FlowBox.setFlowBoxResolution, null, target);
				Integer param = new Integer(ResField.getText());
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);

				send = new PropertyEditEtherEvent(this,
						FlowBox.setFlowBoxColorTableSize, null, target);
				param = new Integer(TableSizeField.getText());
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);

				send = new PropertyEditEtherEvent(this,
						FlowBox.setFlowBoxDelay, null, target);
				param = new Integer(DelayField.getText());
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);

				send = new PropertyEditEtherEvent(this,
						FlowBox.setFlowBoxNumFrames, null, target);
				param = new Integer(NumFramesField.getText());
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);

				undoMgr.commitUndoableOp(utag, "Resolution Properties Change");
			} catch (NumberFormatException ex) {
				handleThrow(new IllegalInputException(
						"The resolution must be a number.", ex));
			} catch (Throwable ex) {
				handleThrow(ex);
			} finally {
				updating = false;
			}
		}
	}

	/**
	* Adds the tabs to the property editor needed to display FlowBox-specific properties.
	* @param in The property source from which to define the tabs.
	* @param inp Properties defining which tabs to display.
	*/
	protected void addTabs(EtherEventPropertySource in, Properties inp) {
		target = (FlowBox) in;
		TopColorCell = new ColorCell();
		MidColorCell = new ColorCell();
		BottomColorCell = new ColorCell();
		TopColorCell.setOutlineBorder();
		MidColorCell.setOutlineBorder();
		BottomColorCell.setOutlineBorder();

		if (!lacksProperty("Scrolling", inp))
			inp.put("NoTransparent", in);

		inp.put("NoEditControl", in);

		UpModeButton = new JRadioButton("Up");
		DownModeButton = new JRadioButton("Down");
		LeftModeButton = new JRadioButton("Left");
		RightModeButton = new JRadioButton("Right");

		JPanel p2 = new JPanel();
		TabPane.add("Flow Direction", p2);
		p2.setLayout(new VerticalLayout(1));
		p2.add("any", UpModeButton);
		p2.add("any", DownModeButton);
		p2.add("any", LeftModeButton);
		p2.add("any", RightModeButton);

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
		JPanel p5 = new JPanel();
		p5.setLayout(new VerticalLayout(0));
		ResField = new JTextField("25");
		p4.add("North", p5);
		p5.add("any", new JLabel("Resolution"));
		p5.add("any", ResField);
		TableSizeField = new JTextField("16");
		p5.add("any", new JLabel("Table Size"));
		p5.add("any", TableSizeField);
		DelayField = new JTextField("100");
		p5.add("any", new JLabel("Animation Delay (Milliseconds)"));
		p5.add("any", DelayField);
		NumFramesField = new JTextField("6");
		p5.add("any", new JLabel("Number Of Frames"));
		p5.add("any", NumFramesField);
		JButton ResApplyButton = new JButton("Apply");
		p4.add("South", ResApplyButton);

		super.addTabs(in, inp);

		TopColorCell.setColor(target.getTopColor());
		MidColorCell.setColor(target.getMidColor());
		BottomColorCell.setColor(target.getBottomColor());

		UpModeButton.getModel().setSelected(
				target.getEditMode() == FlowBox.UpMode);
		DownModeButton.getModel().setSelected(
				target.getEditMode() == FlowBox.DownMode);
		LeftModeButton.getModel().setSelected(
				target.getEditMode() == FlowBox.LeftMode);
		RightModeButton.getModel().setSelected(
				target.getEditMode() == FlowBox.RightMode);

		ButtonGroup MyGrp = new ButtonGroup();
		MyGrp.add(UpModeButton);
		MyGrp.add(DownModeButton);
		MyGrp.add(LeftModeButton);
		MyGrp.add(RightModeButton);

		ItemListener item = Adapters.createGItemListener(this, "handleButton");
		UpModeButton.addItemListener(item);
		DownModeButton.addItemListener(item);
		LeftModeButton.addItemListener(item);
		RightModeButton.addItemListener(item);

		ActionListener ButtonL = Adapters.createGActionListener(this,
				"handleResApplyButton");
		ResApplyButton.addActionListener(ButtonL);

		ActionListener CellL = Adapters.createGActionListener(this,
				"handleTopCellColorChange");
		TopColorCell.addColorActionListener(CellL);
		CellL = Adapters
				.createGActionListener(this, "handleMidCellColorChange");
		MidColorCell.addColorActionListener(CellL);
		CellL = Adapters.createGActionListener(this,
				"handleBottomCellColorChange");
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
	public FlowBoxPropertyEditor(FlowBox in, Properties inp) {
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
	private FlowBox target;
	
}

