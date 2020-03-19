package verdantium.standard;

import java.awt.BorderLayout;
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
import javax.swing.JCheckBox;
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
import verdantium.utils.IllegalInputException;
import verdantium.utils.VerticalLayout;

//$$strtCprt
/*
     Verdantium compound-document framework by Thorn Green
	Copyright (C) 2007 Thorn Green

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
* This is the property editor class for {@link DrawApp}.
* <P>
* @author Thorn Green
*/
public class DrawAppPropertyEditor
	extends DefaultPropertyEditor
	implements PropertyChangeListener {
	
	/**
	* Color editor for the line color.
	*/
	private ColorCell lineColorCell;
	
	/**
	* Color editor for the fill color.
	*/
	private ColorCell fillColorCell;

	/**
	* Radio button to set the editing mode.
	*/
	private JRadioButton editModeButton;
	
	/**
	* Radio button to set the text insert mode.
	*/
	private JRadioButton textModeButton;
	
	/**
	* Radio button to set the line insert mode.
	*/
	private JRadioButton lineModeButton;
	
	/**
	* Radio button to set the rectangle insert mode.
	*/
	private JRadioButton rectModeButton;
	
	/**
	* Radio button to set the filled rectangle insert mode.
	*/
	private JRadioButton filledRectModeButton;
	
	/**
	* Radio button to set the round rectangle insert mode.
	*/
	private JRadioButton roundRectModeButton;
	
	/**
	* Radio button to set the filled round rectangle insert mode.
	*/
	private JRadioButton filledRoundRectModeButton;
	
	/**
	* Radio button to set the oval insert mode.
	*/
	private JRadioButton ovalModeButton;
	
	/**
	* Radio button to set the filled oval insert mode.
	*/
	private JRadioButton filledOvalModeButton;
	
	/**
	* Radio button to set the polygon insert mode.
	*/
	private JRadioButton polygonModeButton;
	
	/**
	* Radio button to set the filled polygon insert mode.
	*/
	private JRadioButton filledPolygonModeButton;
	
	/**
	* Radio button to set the curve insert mode.
	*/
	private JRadioButton curveModeButton;
	
	/**
	* Radio button to set the filled curve insert mode.
	*/
	private JRadioButton filledCurveModeButton;
	
    /**
	* Radio button to set the mode for erasing individual primitives.
	*/
	private JRadioButton eraseModeButton;
	
    /**
	* Radio button to set the mode for moving a primitive to the front.
	*/
	private JRadioButton toFrontModeButton;
	
    /**
	* Radio button to set the mode for moving a primitive to the back.
	*/
	private JRadioButton toBackModeButton;

	/**
	* Text field for setting the width of the pen.
	*/
	private JTextField penField;

	/**
	* Radio button for setting the FMILL curve mode.
	*/
	private JRadioButton fMILLButton;
	
	/**
	* Radio button for setting the Bessel curve mode.
	*/
	private JRadioButton besselButton;
	
	/**
	* Check box for setting chord as opposed to uniform parameters.
	*/
	private JCheckBox chordParam;

	/**
	* Set to true when updating a property to avoid recursion.
	*/
	protected boolean updating = false;

	/**
	* Handles the destruction of the component by calling handleDestroy() on all
	* embedded components.
	*/
	public void handleDestroy() {
		super.handleDestroy();
		lineColorCell.handleDestroy();
		fillColorCell.handleDestroy();
	}

	/**
	* Handles property change events by updating the display of the
	* appropriate properties.
	* @param e The input event.
	*/
	public void propertyChange(PropertyChangeEvent e) {
		super.propertyChange(e);

		if (e.getPropertyName() == DrawApp.DRAW_APP_MODE)
			handleModeChange(e);

		if (e.getPropertyName() == DrawApp.DRAW_APP_COLORS)
			handleTargetColorChange(e);
	}

	/**
	* Handles a change in the drawing mode.
	* @param e The input event.
	*/
	protected void handleModeChange(PropertyChangeEvent e) {
		if (!updating) {
			updating = true;
			switch (target.getEditMode()) {
				case DrawApp.EDIT_MODE :
					editModeButton.getModel().setSelected(true);
					break;

				case DrawApp.LINE_MODE :
					lineModeButton.getModel().setSelected(true);
					break;

				case DrawApp.TEXT_MODE :
					textModeButton.getModel().setSelected(true);
					break;

				case DrawApp.RECT_MODE :
					rectModeButton.getModel().setSelected(true);
					break;

				case DrawApp.FILLED_RECT_MODE :
					filledRectModeButton.getModel().setSelected(true);
					break;

				case DrawApp.ROUND_RECT_MODE :
					roundRectModeButton.getModel().setSelected(true);
					break;

				case DrawApp.FILLED_ROUND_RECT_MODE :
					filledRoundRectModeButton.getModel().setSelected(true);
					break;

				case DrawApp.OVAL_MODE :
					ovalModeButton.getModel().setSelected(true);
					break;

				case DrawApp.FILLED_OVAL_MODE :
					filledOvalModeButton.getModel().setSelected(true);
					break;

				case DrawApp.POLYGON_MODE :
					polygonModeButton.getModel().setSelected(true);
					break;

				case DrawApp.FILLED_POLYGON_MODE :
					filledPolygonModeButton.getModel().setSelected(true);
					break;

				case DrawApp.CURVE_MODE :
					curveModeButton.getModel().setSelected(true);
					break;

				case DrawApp.FILLED_CURVE_MODE :
					filledCurveModeButton.getModel().setSelected(true);
					break;
                                        
                                case DrawApp.ErASE_MODE :
					eraseModeButton.getModel().setSelected(true);
					break;
                                        
                               case DrawApp.TO_FRONT_MODE :
					toFrontModeButton.getModel().setSelected(true);
					break;
                                        
                               case DrawApp.TO_BACK_MODE :
					toBackModeButton.getModel().setSelected(true);
					break;

			}
			updating = false;
			MyPanel.repaint();
		}

	}

	/**
	* Handles a change in the drawing component's line or fill color.
	* @param e The input event.
	*/
	protected void handleTargetColorChange(PropertyChangeEvent e) {
		if (!updating) {
			updating = true;
			lineColorCell.setColor(target.getLineColor());
			fillColorCell.setColor(target.getFillColor());
			updating = false;
			MyPanel.repaint();
		}

	}

	/**
	* Handles a color change in the foreground color cell.
	* @param e The input event.
	*/
	public void handleFgCellColorChange(ActionEvent e) {
		if (!updating) {
			try {
				updating = true;
				EtherEvent send =
					new PropertyEditEtherEvent(
						this,
						DrawApp.setDrawAppColors,
						null,
						target);
				Object[] param =
					{ lineColorCell.getColor(), target.getFillColor()};
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
	* Handles a color change in the background color cell.
	* @param e The input event.
	*/
	public void handleBkCellColorChange(ActionEvent e) {
		if (!updating) {
			try {
				updating = true;
				EtherEvent send =
					new PropertyEditEtherEvent(
						this,
						DrawApp.setDrawAppColors,
						null,
						target);
				Object[] param =
					{ target.getLineColor(), fillColorCell.getColor()};
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
	* Handles a request to erase rendering primitives from the app.
	* @param e The input event.
	*/
	public void handlePrimitiveEraseRequest(ActionEvent e) {
		try {
			EtherEvent send =
				new PropertyEditEtherEvent(
					this,
					DrawApp.eraseAllPrimitives,
					null,
					target);
			ProgramDirector.fireEtherEvent(send, null);
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles one of the buttons for a mode change.
	* @param evt The input event.
	*/
	public void handleButton(ItemEvent evt) {
		if (!updating) {
			try {
				if (((JRadioButton) (evt.getSource()))
					.getModel()
					.isSelected()) {
					updating = true;
					if (editModeButton.getModel().isSelected())
						setEditMode(DrawApp.EDIT_MODE);
					if (textModeButton.getModel().isSelected())
						setEditMode(DrawApp.TEXT_MODE);
					if (lineModeButton.getModel().isSelected())
						setEditMode(DrawApp.LINE_MODE);
					if (rectModeButton.getModel().isSelected())
						setEditMode(DrawApp.RECT_MODE);
					if (filledRectModeButton.getModel().isSelected())
						setEditMode(DrawApp.FILLED_RECT_MODE);
					if (roundRectModeButton.getModel().isSelected())
						setEditMode(DrawApp.ROUND_RECT_MODE);
					if (filledRoundRectModeButton.getModel().isSelected())
						setEditMode(DrawApp.FILLED_ROUND_RECT_MODE);
					if (ovalModeButton.getModel().isSelected())
						setEditMode(DrawApp.OVAL_MODE);
					if (filledOvalModeButton.getModel().isSelected())
						setEditMode(DrawApp.FILLED_OVAL_MODE);
					if (polygonModeButton.getModel().isSelected())
						setEditMode(DrawApp.POLYGON_MODE);
					if (filledPolygonModeButton.getModel().isSelected())
						setEditMode(DrawApp.FILLED_POLYGON_MODE);
					if (curveModeButton.getModel().isSelected())
						setEditMode(DrawApp.CURVE_MODE);
					if (filledCurveModeButton.getModel().isSelected())
						setEditMode(DrawApp.FILLED_CURVE_MODE);
                                        if (eraseModeButton.getModel().isSelected())
						setEditMode(DrawApp.ErASE_MODE);
                                        if (toFrontModeButton.getModel().isSelected())
						setEditMode(DrawApp.TO_FRONT_MODE);
                                        if (toBackModeButton.getModel().isSelected())
						setEditMode(DrawApp.TO_BACK_MODE);
				}
			} catch (Throwable ex) {
				handleThrow(ex);
			} finally {
				updating = false;
			}
		}
	}

	/**
	* Sets the editing mode of the drawing app.
	* @param in The editing mode to set.
	*/
	protected void setEditMode(int in) {
		try {
			EtherEvent send =
				new PropertyEditEtherEvent(
					this,
					DrawApp.setDrawAppMode,
					null,
					target);
			send.setParameter(new Integer(in));
			ProgramDirector.fireEtherEvent(send, null);
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles the setting of the pen width in the property editor.
	* @param in The input event.
	*/
	public void handlePenApply(ActionEvent in) {
		try {
			Double MyD = new Double(penField.getText());

			EtherEvent send =
				new PropertyEditEtherEvent(
					this,
					DrawApp.setBasicStroke,
					null,
					target);
			send.setParameter(MyD);
			ProgramDirector.fireEtherEvent(send, null);
		} catch (NumberFormatException ex) {
			handleThrow(
				new IllegalInputException(
					"The pen width must be a number.",
					ex));
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles the changing of the curve modes in the property editor.
	* @param in The input event.
	*/
	public void handleCurveApply(ActionEvent in) {
		try {
			int mode = 1;
			if (besselButton.getModel().isSelected())
				mode = 2;
			Boolean MyB = new Boolean(chordParam.getModel().isSelected());
			Integer MyI = new Integer(mode);
			Object[] myo = { MyI, MyB };

			EtherEvent send =
				new PropertyEditEtherEvent(
					this,
					DrawApp.setCurveParam,
					null,
					target);
			send.setParameter(myo);
			ProgramDirector.fireEtherEvent(send, null);
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Shunts the superclass method for adding the tabs.
	* @param in The property source from which to define the tabs.
	* @param inp Properties defining which tabs to display.
	*/
	protected void addTabs(EtherEventPropertySource in, Properties inp) {
	}

	/**
	* Adds the tabs for editing the draw app to the superclass tabs.
	* @param in The property source from which to define the tabs.
	* @param inp Properties defining which tabs to display.
	*/
	protected void addTabsSub(EtherEventPropertySource in, Properties inp) {
		target = (DrawApp) in;
		lineColorCell = new ColorCell();
		fillColorCell = new ColorCell();
		lineColorCell.setOutlineBorder();
		fillColorCell.setOutlineBorder();

		if (!lacksProperty("Scrolling", inp))
			inp.put("NoTransparent", in);

		editModeButton = new JRadioButton("Edit Mode");
		textModeButton = new JRadioButton("Text Mode");
		lineModeButton = new JRadioButton("Line Mode");
		rectModeButton = new JRadioButton("Rect Mode");
		filledRectModeButton = new JRadioButton("Filled Rect Mode");
		roundRectModeButton = new JRadioButton("Round Rect Mode");
		filledRoundRectModeButton = new JRadioButton("Filled Round Rect Mode");
		ovalModeButton = new JRadioButton("Oval Mode");
		filledOvalModeButton = new JRadioButton("Filled Oval Mode");
		polygonModeButton = new JRadioButton("Polygon Mode");
		filledPolygonModeButton = new JRadioButton("Filled Polygon Mode");
		curveModeButton = new JRadioButton("Curve Mode");
		filledCurveModeButton = new JRadioButton("Filled Curve Mode");
                eraseModeButton = new JRadioButton("Erase Mode");
                toFrontModeButton = new JRadioButton("To-Front Mode");
                toBackModeButton = new JRadioButton("To-Back Mode");
		JButton erasePrimitivesButton =
			new JButton("Erase Rendering Primitives");

		JPanel p2 = new JPanel();
		TabPane.add("Document", p2);
		p2.setLayout(new VerticalLayout(1));
		p2.add("any", editModeButton);
		p2.add("any", textModeButton);
		p2.add("any", lineModeButton);
		p2.add("any", rectModeButton);
		p2.add("any", filledRectModeButton);
		p2.add("any", roundRectModeButton);
		p2.add("any", filledRoundRectModeButton);
		p2.add("any", ovalModeButton);
		p2.add("any", filledOvalModeButton);
		p2.add("any", polygonModeButton);
		p2.add("any", filledPolygonModeButton);
		p2.add("any", curveModeButton);
		p2.add("any", filledCurveModeButton);
                p2.add("any", eraseModeButton);
                p2.add("any", toFrontModeButton);
                p2.add("any", toBackModeButton);
		p2.add("any", erasePrimitivesButton);

		JPanel p3 = new JPanel();
		TabPane.add("Colors", p3);
		p3.setLayout(new VerticalLayout(1));
		p3.add("any", new JLabel("Line Color: "));
		p3.add("any", lineColorCell.getGUI());
		p3.add("any", new JLabel("Fill Color: "));
		p3.add("any", fillColorCell.getGUI());

		JPanel p4 = new JPanel();
		TabPane.add("Pen", p4);
		p4.setLayout(new BorderLayout(0, 0));
		JButton penApply = new JButton("Apply");
		p4.add("South", penApply);
		JPanel tmp = new JPanel();
		tmp.setLayout(new BorderLayout(0, 0));
		tmp.add("North", new JLabel("Pen Width:"));
		penField = new JTextField("2");
		tmp.add("Center", penField);
		p4.add("North", tmp);

		JPanel p5 = new JPanel();
		TabPane.add("Curve", p5);
		p5.setLayout(new BorderLayout(0, 0));
		JButton curveApply = new JButton("Apply");
		p5.add("South", curveApply);
		JPanel tmp2 = new JPanel();
		tmp2.setLayout(new VerticalLayout(1));
		fMILLButton = new JRadioButton("FMILL Curve", false);
		besselButton = new JRadioButton("Bessel Curve", true);
		chordParam = new JCheckBox("Chord Param", true);
		tmp2.add("any", fMILLButton);
		tmp2.add("any", besselButton);
		tmp2.add("any", chordParam);
		ButtonGroup grp5 = new ButtonGroup();
		grp5.add(fMILLButton);
		grp5.add(besselButton);
		p5.add("Center", tmp2);

		super.addTabs(in, inp);

		editModeButton.getModel().setSelected(
			target.getEditMode() == DrawApp.EDIT_MODE);
		textModeButton.getModel().setSelected(
			target.getEditMode() == DrawApp.TEXT_MODE);
		lineModeButton.getModel().setSelected(
			target.getEditMode() == DrawApp.LINE_MODE);
		rectModeButton.getModel().setSelected(
			target.getEditMode() == DrawApp.RECT_MODE);
		filledRectModeButton.getModel().setSelected(
			target.getEditMode() == DrawApp.FILLED_RECT_MODE);
		roundRectModeButton.getModel().setSelected(
			target.getEditMode() == DrawApp.ROUND_RECT_MODE);
		filledRoundRectModeButton.getModel().setSelected(
			target.getEditMode() == DrawApp.FILLED_ROUND_RECT_MODE);
		ovalModeButton.getModel().setSelected(
			target.getEditMode() == DrawApp.OVAL_MODE);
		filledOvalModeButton.getModel().setSelected(
			target.getEditMode() == DrawApp.FILLED_OVAL_MODE);
		polygonModeButton.getModel().setSelected(
			target.getEditMode() == DrawApp.POLYGON_MODE);
		filledPolygonModeButton.getModel().setSelected(
			target.getEditMode() == DrawApp.FILLED_POLYGON_MODE);
		curveModeButton.getModel().setSelected(
			target.getEditMode() == DrawApp.CURVE_MODE);
		filledCurveModeButton.getModel().setSelected(
			target.getEditMode() == DrawApp.FILLED_CURVE_MODE);
                eraseModeButton.getModel().setSelected(
			target.getEditMode() == DrawApp.ErASE_MODE);
                toFrontModeButton.getModel().setSelected(
			target.getEditMode() == DrawApp.TO_FRONT_MODE);
                toBackModeButton.getModel().setSelected(
			target.getEditMode() == DrawApp.TO_BACK_MODE);

		ButtonGroup myGrp = new ButtonGroup();
		myGrp.add(editModeButton);
		myGrp.add(textModeButton);
		myGrp.add(lineModeButton);
		myGrp.add(rectModeButton);
		myGrp.add(filledRectModeButton);
		myGrp.add(roundRectModeButton);
		myGrp.add(filledRoundRectModeButton);
		myGrp.add(ovalModeButton);
		myGrp.add(filledOvalModeButton);
		myGrp.add(polygonModeButton);
		myGrp.add(filledPolygonModeButton);
		myGrp.add(curveModeButton);
		myGrp.add(filledCurveModeButton);
                myGrp.add(eraseModeButton);
                myGrp.add(toFrontModeButton);
                myGrp.add(toBackModeButton);

		lineColorCell.setColor(target.getLineColor());
		fillColorCell.setColor(target.getFillColor());

		ItemListener item = Adapters.createGItemListener(this, "handleButton");
		editModeButton.addItemListener(item);
		textModeButton.addItemListener(item);
		lineModeButton.addItemListener(item);
		rectModeButton.addItemListener(item);
		filledRectModeButton.addItemListener(item);
		roundRectModeButton.addItemListener(item);
		filledRoundRectModeButton.addItemListener(item);
		ovalModeButton.addItemListener(item);
		filledOvalModeButton.addItemListener(item);
		polygonModeButton.addItemListener(item);
		filledPolygonModeButton.addItemListener(item);
		curveModeButton.addItemListener(item);
		filledCurveModeButton.addItemListener(item);
                eraseModeButton.addItemListener(item);
                toFrontModeButton.addItemListener(item);
                toBackModeButton.addItemListener(item);

		ActionListener cellL =
			Adapters.createGActionListener(this, "handleFgCellColorChange");
		lineColorCell.addColorActionListener(cellL);
		cellL = Adapters.createGActionListener(this, "handleBkCellColorChange");
		fillColorCell.addColorActionListener(cellL);

		ActionListener penL =
			Adapters.createGActionListener(this, "handlePenApply");
		penApply.addActionListener(penL);

		ActionListener curveL =
			Adapters.createGActionListener(this, "handleCurveApply");
		curveApply.addActionListener(curveL);

		ActionListener primL =
			Adapters.createGActionListener(this, "handlePrimitiveEraseRequest");
		erasePrimitivesButton.addActionListener(primL);
	}

	/**
	* Constructs the property editor.
	* @param in The component to be edited.
	* @param inp Properties defining what content to display.
	*/
	public DrawAppPropertyEditor(DrawApp in, Properties inp) {
		super(in, inp);
		addTabsSub(in, inp);
	}

	/**
	* Returns the line color cell.
	* @return The line color cell.
	*/
	public ColorCell getLineColorCell() {
		return (lineColorCell);
	}

	/**
	* Returns the fill color cell.
	* @return The fill color cell.
	*/
	public ColorCell getFillColorCell() {
		return (fillColorCell);
	}

	/**
	* Gets the persistent data flavors that the property editor can load, which are none.
	* @return The persistent data flavors that the property editor can load, which are none.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		return (null);
	}

	/**
	* The DrawApp being edited by the property editor.
	*/
	protected DrawApp target;
	
}

