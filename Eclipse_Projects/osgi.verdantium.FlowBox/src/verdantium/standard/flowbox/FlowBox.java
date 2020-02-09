package verdantium.standard.flowbox;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.Timer;

import jundo.runtime.ExtMilieuRef;
import meta.DataFormatException;
import meta.VersionBuffer;
import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.GuiShowNotify;
import verdantium.ProgramDirector;
import verdantium.StandardEtherEvent;
import verdantium.TransVersionBuffer;
import verdantium.TransVersionBufferFlavor;
import verdantium.UrlHolder;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.core.DefaultPropertyEditor;
import verdantium.core.DesignerControl;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.standard.flowbox.help.FlowBoxHelp;
import verdantium.undo.UTag;
import verdantium.undo.UndoManager;
import verdantium.utils.VerdantiumDragUtils;
import verdantium.utils.VerdantiumDropUtils;
import verdantium.xapp.DocPageFormat;
import verdantium.xapp.JcApplicationAdapter;
import verdantium.xapp.MacroTreeMap;
import verdantium.xapp.OnlyDesignerEdits;

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
 *    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Macro support.                                                       | Added macro support.
 *    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
 *    | 09/29/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for multiple PLAFs.                                          | Made code changes to support multiple PLAFs.
 *    | 10/05/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for command-line arguments.                                  | Added support for command-line arguments.
 *    | 11/17/2001            | Thorn Green (viridian_1138@yahoo.com)           | Documentation fixes.                                                 | Documentation fixes.
 *    | 12/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Third-Cut at Error Handling.                                         | Third-Cut at Error Handling.
 *    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
 *    | 05/10/2002            | Thorn Green (viridian_1138@yahoo.com)           | Redundant information in persistent storage.                         | Made numerous persistence and packaging changes.
 *    | 03/09/2003            | Thorn Green (viridian_1138@yahoo.com)           | Change timer usage (coalesce).                                       | Changed timer usage.
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
 * Provides animation for flow diagrams used in presentation graphics.
 * 
 * @author Thorn Green
 */
public class FlowBox extends JcApplicationAdapter implements GuiShowNotify,
		ActionListener {
	
	/**
	 * Returns the GUI of the component.
	 * @return The GUI of the component.
	 */
	public JComponent getGUI() {
		return (this);
	}

	/**
	 * The timer that drives the animation.
	 */
	transient private Timer MyTime = null;

	/**
	 * Indicates whether the component has been painted in order to get around
	 * some current implementation problems (i.e. it's a kluge).
	 */
	transient private boolean painted = false;

	/**
	 * Mode for going up.
	 */
	public static final int UpMode = 1;

	/**
	 * Mode for going down.
	 */
	public static final int DownMode = 2;

	/**
	 * Mode for going left.
	 */
	public static final int LeftMode = 3;

	/**
	 * Mode for going right.
	 */
	public static final int RightMode = 4;

	/**
	 * The current operating mode of the component.
	 */
	transient protected int currentMode = DownMode;

	/**
	 * Property change name for a change in the mode of the component..
	 */
	public static final String FlowBoxMode = "FlowBoxMode";

	/**
	 * Property name indivating that the wave colors have been changed.
	 */
	public static final String FlowBoxColors = "FlowBoxColors";

	/**
	 * EtherEvent name for setting the flow box mode.
	 */
	public static String setFlowBoxMode = "setFlowBoxMode";

	/**
	 * Ether Event name for setting the resolution of the wave.
	 */
	public static final String setFlowBoxResolution = "setFlowBoxResolution";

	/**
	 * Ether Event name for setting the size of the color table.
	 */
	public static final String setFlowBoxColorTableSize = "setFlowBoxColorTableSize";

	/**
	 * Ether Event name for setting the flow box delay.
	 */
	public static final String setFlowBoxDelay = "setFlowBoxDelay";

	/**
	 * Ether Event name for setting the flow box number of animation frames (per
	 * sequence).
	 */
	public static final String setFlowBoxNumFrames = "setFlowBoxNumFrames";

	/**
	 * Ether Event name for setting the colors of the wave display.
	 */
	public static final String setFlowBoxColors = "setFlowBoxColors";

	/**
	 * Ether Event name for setting whether the component is animated.
	 */
	public static final String setFlowBoxAnimated = "setFlowBoxAnimated";

	/**
	 * The maximum number of frames in the animation.
	 */
	transient protected int num_frames = 6;

	/**
	 * The current "frame" in the animation.
	 */
	transient protected int cur_idex = 0;

	/**
	 * The pixel size of the displayed images.
	 */
	transient protected int img_size = 25;

	/**
	 * The color table size used for rendering.
	 */
	transient protected int tbl_size = 16;

	/**
	 * Table delay time between animation frames in mills.
	 */
	transient protected int tbl_delay = 100;

	/**
	 * The set of color tables used to display the animation.
	 */
	transient protected Object[] color_tables = null;

	/**
	 * The color of the trough of the wave in the simulation.
	 */
	transient protected Color startColor = new Color(0, 0, 255);

	/**
	 * The color of the middle of the wave in the simulation.
	 */
	transient protected Color midColor = new Color(64, 64, 255);

	/**
	 * The color of the wave peak in the simulation.
	 */
	transient protected Color endColor = new Color(127, 127, 255);

	/**
	 * Indicates whether the component is being animated.
	 */
	transient protected boolean animated = true;

	/**
	 * Indicates whether animation was turned off by visibility.
	 */
	transient protected boolean aniTurnedOff = false;

	/**
	 * The data model for multi-level undo.
	 */
	protected pdx_FlowBoxModel_pdx_ObjectRef model = null;

	/**
	 * Gets the current editing mode of the component.
	 * @return The editing mode of the component.
	 */
	public int getEditMode() {
		return (currentMode);
	}

	/**
	 * Sets the current editing mode of the component, and updates the undo state.
	 * @param in The editing mode of the component.
	 */
	protected void setEditMode(int in) {
		ExtMilieuRef mil = model.pdxm_setEditMode(undoMgr.getCurrentMil(), in);
		undoMgr.handleCommitTempChange(mil);
		setEditModeComp(in);
	}

	/**
	 * Sets the current editing mode of the component.
	 * @param in The editing mode of the component.
	 */
	protected void setEditModeComp(int in) {
		int tmp = currentMode;
		currentMode = in;
		createColorTables();
		repaint();
		PropL.firePropertyChange(FlowBoxMode, new Integer(tmp), new Integer(
				currentMode));
	}

	/**
	 * Constructs the FlowBox component.
	 */
	public FlowBox() {
		initializeUndoMgr();
		macroMap = new MacroTreeMap(undoMgr);
		docPageFormat = new DocPageFormat(undoMgr);
		onlyDesignerEdits = new OnlyDesignerEdits(undoMgr);
		undoMgr.addPropertyChangeListener(this);
		PropL = new PropertyChangeSupport(this);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		setOpaque(true);
		setMinimumSize(new Dimension(2, 2));
		setPreferredSize(new Dimension(15, 75));
		setDoubleBuffered(false);
		MyTime = new Timer(tbl_delay, this);
		MyTime.setCoalesce(true);
		setToolTipText("Right-Click to change color");
		VerdantiumDragUtils.setDragUtil(this, this);
		VerdantiumDropUtils.setDropUtil(this, this, this);
		configureForEtherEvents();
		/* MyTime.start(); */
	}

	/**
	 * Initializes the undo manager of the component.
	 */
	protected void initializeUndoMgr() {
		pdx_FlowBoxModel_pdx_PairRef ref = pdx_FlowBoxModel_pdx_ObjectRef
				.pdxm_new_FlowBoxModel(
						jundo.runtime.Runtime.getInitialMilieu(), false,
						img_size, tbl_size, tbl_delay, num_frames, currentMode,
						endColor, midColor, startColor);
		model = (pdx_FlowBoxModel_pdx_ObjectRef) (ref.getObject());
		undoMgr = UndoManager.createInstanceUndoManager(ref.getMilieu());
	}

	@Override
	public void processMouseEvent(MouseEvent e) {
		switch (e.getID()) {
		case MouseEvent.MOUSE_PRESSED:
			try {
				Boolean param = new Boolean(!animated);
				EtherEvent send = new PropertyEditEtherEvent(this,
						FlowBox.setFlowBoxAnimated, null, this);
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);
				if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
					handlePopupTrigger(e);
			} catch (Throwable ex) {
				handleThrow(ex);
			}
			break;

		}

		super.processMouseEvent(e);
	}

	/**
	 * Sets whether the component is animated.
	 * @param in Whether the component is animated.
	 */
	protected void setAnimated(boolean in) {
		animated = in;
		aniTurnedOff = false;
		if (!animated)
			MyTime.stop();
		else
			MyTime.start();
	}

	/**
	 * Handles a popup trigger by displaying the component's property editor.
	 * @param e The mouse event triggering the popup.
	 */
	public void handlePopupTrigger(MouseEvent e) {
		try {
			if (((DesignerControl.isDesignTime()) || (!isOnlyDesignerEdits()))
					&& !(e.isAltDown())) {
				EtherEvent send = new StandardEtherEvent(this,
						StandardEtherEvent.showPropertiesEditor, null, this);
				send.setParameter(e.getPoint());
				ProgramDirector.fireEtherEvent(send, null);
			}
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	 * Handles a timer event by repainting the animation.
	 * @param e The input event.
	 */
	public void actionPerformed(ActionEvent e) {
		if (!(isShowing()) && !aniTurnedOff) {
			MyTime.stop();
			aniTurnedOff = true;
		} else {
			cur_idex++;
			if (cur_idex >= num_frames)
				cur_idex = 0;
			repaint();
		}
	}

	@Override
	public void paint(Graphics g) {
		if (!painted && animated)
			MyTime.start();
		painted = true;
		if (aniTurnedOff)
			MyTime.start();
		aniTurnedOff = false;

		boolean vert = (currentMode == UpMode) || (currentMode == DownMode);

		Rectangle MyRect = getBounds();
		Color[] cols = (Color[]) (color_tables[cur_idex]);

		if (vert) {
			int sz = MyRect.height / img_size + 1;
			double mult = img_size / (double) (tbl_size);
			int count;
			int count1;
			for (count = 0; count < sz; count++) {
				int ht = count * img_size;
				for (count1 = 0; count1 < tbl_size; count1++) {
					int hta = ht + (int) (count1 * mult);
					int htb = ht + (int) ((count1 + 1) * mult);
					g.setColor(cols[count1]);
					g.fillRect(MyRect.x, hta, MyRect.width, htb - hta);
				}
			}
		} else {
			int sz = MyRect.width / img_size + 1;
			double mult = img_size / (double) (tbl_size);
			int count;
			int count1;
			for (count = 0; count < sz; count++) {
				int ht = count * img_size;
				for (count1 = 0; count1 < tbl_size; count1++) {
					int hta = ht + (int) (count1 * mult);
					int htb = ht + (int) ((count1 + 1) * mult);
					g.setColor(cols[count1]);
					g.fillRect(hta, MyRect.y, htb - hta, MyRect.height);
				}
			}
		}

	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	/**
	 * Creates a particular indexed color table for the component.
	 * @param idex The index of the table in the animation cycle.
	 */
	protected Color[] createColorTable(int idex) {
		boolean pos = (currentMode == UpMode) || (currentMode == LeftMode);
		Color[] cols = new Color[tbl_size];
		int count;
		double offset = 2.0 * Math.PI * idex / num_frames;
		for (count = 0; count < tbl_size; ++count) {
			double offset2 = 2.0 * Math.PI * count / tbl_size;
			double u = 0.0;
			if (pos)
				u = Math.sin(offset + offset2);
			else
				u = Math.sin(offset - offset2);
			int red = 0;
			int green = 0;
			int blue = 0;

			if (u >= 0.0) {
				double v = 1.0 - u;
				red = (int) (v * (midColor.getRed()) + u * (endColor.getRed()));
				green = (int) (v * (midColor.getGreen()) + u
						* (endColor.getGreen()));
				blue = (int) (v * (midColor.getBlue()) + u
						* (endColor.getBlue()));
			} else {
				u = u + 1.0;
				double v = 1.0 - u;
				red = (int) (v * (startColor.getRed()) + u
						* (midColor.getRed()));
				green = (int) (v * (startColor.getGreen()) + u
						* (midColor.getGreen()));
				blue = (int) (v * (startColor.getBlue()) + u
						* (midColor.getBlue()));
			}

			cols[count] = new Color(red, green, blue);
		}

		return (cols);
	}

	/**
	 * Creates the color tables for the component.
	 */
	protected void createColorTables() {
		color_tables = new Object[num_frames];
		int count;
		for (count = 0; count < num_frames; count++) {
			color_tables[count] = createColorTable(count);
		}
	}

	/**
	 * Handles the showing of the component by performing actions that can only
	 * happen after the component's GUI has a peer.
	 */
	public void guiShowNotify() {
		createColorTables();
	}

	/**
	 * Gets the color of the trough of the displayed wave.
	 * @return The color of the trough of the displayed wave.
	 */
	public Color getBottomColor() {
		return (startColor);
	}

	/**
	 * Gets the color of the middle of the displayed wave.
	 * @return The color of the middle of the displayed wave.
	 */
	public Color getMidColor() {
		return (midColor);
	}

	/**
	 * Gets the color of the peak of the displayed wave.
	 * @return The color of the peak of the displayed wave.
	 */
	public Color getTopColor() {
		return (endColor);
	}

	/**
	 * Sets the resolution of the component, and updates the undo state.
	 * @param in The resolution of the component.
	 */
	protected void setResolution(int in) {
		ExtMilieuRef mil = model
				.pdxm_setResolution(undoMgr.getCurrentMil(), in);
		undoMgr.handleCommitTempChange(mil);
		setResolutionComp(in);
	}

	/**
	 * Sets the resolution of the component.
	 * @param in The resolution of the component.
	 */
	protected void setResolutionComp(int in) {
		img_size = in;
		repaint();
	}

	/**
	 * Sets the color table size of the component, and updates the undo state.
	 * @param in The color table size of the component.
	 */
	protected void setColorTableSize(int in) {
		ExtMilieuRef mil = model.pdxm_setColorTableSize(
				undoMgr.getCurrentMil(), in);
		undoMgr.handleCommitTempChange(mil);
		setColorTableSizeComp(in);
	}

	/**
	 * Sets the color table size of the component.
	 * @param in The color table size of the component.
	 */
	protected void setColorTableSizeComp(int in) {
		tbl_size = in;
		createColorTables();
		repaint();
	}

	/**
	 * Sets the delay between animation frames of the component, and updates the undo state.
	 * @param in The delay time in mills.
	 */
	protected void setDelay(int in) {
		ExtMilieuRef mil = model.pdxm_setDelay(undoMgr.getCurrentMil(), in);
		undoMgr.handleCommitTempChange(mil);
		setDelayComp(in);
	}

	/**
	 * Sets the delay between animation frames of the component.
	 * @param in The delay time in mills.
	 */
	protected void setDelayComp(int in) {
		tbl_delay = in;
		if (MyTime != null)
			MyTime.stop();
		MyTime = new Timer(tbl_delay, this);
		MyTime.start();
		repaint();
	}

	/**
	 * Sets the color table size of the component, and updates the undo state.
	 * @param in The color table size, aka the number of frames.
	 */
	protected void setNumFrames(int in) {
		ExtMilieuRef mil = model.pdxm_setNumFrames(undoMgr.getCurrentMil(), in);
		undoMgr.handleCommitTempChange(mil);
		setNumFramesComp(in);
	}

	/**
	 * Sets the color table size of the component.
	 * @param in The color table size, aka the number of frames.
	 */
	protected void setNumFramesComp(int in) {
		num_frames = in;
		createColorTables();
		repaint();
	}

	/**
	 * Sets the component's wave colors, and updates the undo state.
	 * @param Start The color of the trough of the wave in the simulation.
	 * @param Mid The color of the middle of the wave in the simulation.
	 * @param End The color of the peak of the wave in the simulation.
	 */
	protected void setColors(Color Start, Color Mid, Color End) {
		ExtMilieuRef mil = model.pdxm_setStartColor(undoMgr.getCurrentMil(),
				Start);
		mil = model.pdxm_setMidColor(mil, Mid);
		mil = model.pdxm_setEndColor(mil, End);
		undoMgr.handleCommitTempChange(mil);
		setColorsComp(Start, Mid, End);
	}

	/**
	 * Sets the component's wave colors.
	 * @param Start The color of the trough of the wave in the simulation.
	 * @param Mid The color of the middle of the wave in the simulation.
	 * @param End The color of the peak of the wave in the simulation.
	 */
	protected void setColorsComp(Color Start, Color Mid, Color End) {
		startColor = Start;
		midColor = Mid;
		endColor = End;
		createColorTables();
		repaint();
		PropL.firePropertyChange(FlowBoxColors, null, null);
	}

	/**
	 * Handles the destruction of the component by stopping the timer that
	 * drives the animation.
	 */
	public void handleDestroy() {
		MyTime.stop();
		super.handleDestroy();
	}

	/**
	 * Handles Ether Events to alter the properties of the component.
	 * @param in The event to handle.
	 * @param refcon A reference to context data for the event.
	 * @return The result of handling the event, or null if there is no result.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon)
			throws Throwable {

		Object ret = super.processObjEtherEvent(in, refcon);
		if (ret != EtherEvent.EVENT_NOT_HANDLED) {
			return (ret);
		}

		if (in instanceof PropertyEditEtherEvent) {
			if (in.getEtherID().equals(setFlowBoxMode)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				setEditMode(((Integer) (in.getParameter())).intValue());
				undoMgr.commitUndoableOp(utag, "Mode Change");
			}

			if (in.getEtherID().equals(setFlowBoxResolution)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				Integer param = (Integer) (in.getParameter());
				setResolution(param.intValue());
				undoMgr.commitUndoableOp(utag, "Resolution Change");
			}

			if (in.getEtherID().equals(setFlowBoxColorTableSize)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				Integer param = (Integer) (in.getParameter());
				setColorTableSize(param.intValue());
				undoMgr.commitUndoableOp(utag, "Color Table Size Change");
			}

			if (in.getEtherID().equals(setFlowBoxDelay)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				Integer param = (Integer) (in.getParameter());
				setDelay(param.intValue());
				undoMgr.commitUndoableOp(utag, "Delay Change");
			}

			if (in.getEtherID().equals(setFlowBoxNumFrames)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				Integer param = (Integer) (in.getParameter());
				setNumFrames(param.intValue());
				undoMgr.commitUndoableOp(utag, "Num Frames Change");
			}

			if (in.getEtherID().equals(setFlowBoxColors)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				Object[] param = (Object[]) (in.getParameter());
				Color c1 = (Color) (param[0]);
				Color c2 = (Color) (param[1]);
				Color c3 = (Color) (param[2]);
				setColors(c1, c2, c3);
				undoMgr.commitUndoableOp(utag, "Color Change");
			}

			if (in.getEtherID().equals(setFlowBoxAnimated)) {
				Boolean param = (Boolean) (in.getParameter());
				setAnimated(param.booleanValue());
			}

		}

		return (null);
	}

	/**
	 * Creates the properties editor for the component.
	 * @return The created property editor.
	 */
	public VerdantiumPropertiesEditor makePropertiesEditor() {
		Properties MyP = new Properties();
		FlowBoxPropertyEditor MyEdit = new FlowBoxPropertyEditor(this, MyP);
		MyEdit.setClickPoint(new Point(10, 10));
		return (MyEdit);
	}

	/**
	 * Shows the properties editor for the component.
	 * @param e The event for showing the editor.
	 */
	public void showPropertiesEditor(EtherEvent e) {
		VerdantiumPropertiesEditor MyEdit = makePropertiesEditor();
		((DefaultPropertyEditor) MyEdit).setClickPoint((Point) (e
				.getParameter()));
		ProgramDirector.showPropertyEditor(MyEdit, getGUI(),
				"FlowBox Property Editor");
	}

	/**
	* Returns the data flavors that the component can load from persistent storage.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF = { new TransVersionBufferFlavor("FlowBox", "FlowBox") };
		return (MyF);
	}

	/**
	* Returns the data flavors that the component can save to persistent storage.
    * @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF = { new TransVersionBufferFlavor("FlowBox", "FlowBox") };
		return (MyF);
	}

	/**
	* Loads data for the component from persistent storage.
    * @param flavor The flavor in which to load the data.
    * @param trans The transferable from which to load the data.
	*/
	public void loadPersistentData(DataFlavor flavor, Transferable trans)
			throws IOException {
		if (trans instanceof UrlHolder) {
			fileSaveURL = ((UrlHolder) trans).getUrl();
			fileSaveFlavor = flavor;
		}

		if (trans == null) {
			onlyDesignerEdits.setOnlyDesignerEdits(false);
			macroMap.clear();
			setResolution(25);
			setColorTableSize(16);
			setDelay(100);
			setNumFrames(6);
			setEditMode(DownMode);
			setColors(new Color(0, 0, 255), new Color(64, 64, 255), new Color(
					127, 127, 255));
			repaint();
		} else {
			try {
				TransVersionBuffer MyF = (TransVersionBuffer) trans;
				VersionBuffer.chkNul(MyF);
				onlyDesignerEdits.setOnlyDesignerEdits(MyF
						.getBoolean("OnlyDesignerEdits"));
				macroMap.readData(MyF);
				setResolution(MyF.getInt("Resolution"));
				setColorTableSize(MyF.getInt("TableSize"));
				setDelay(MyF.getInt("TableDelay"));
				setNumFrames(MyF.getInt("NumFrames"));
				Object mode = MyF.getProperty("currentMode");
				if (mode instanceof Integer)
					setEditMode(((Integer) mode).intValue());
				Color EndColor = (Color) (MyF.getProperty("TopColor"));
				VersionBuffer.chkNul(EndColor);
				Color MidColor = (Color) (MyF.getProperty("midColor"));
				VersionBuffer.chkNul(MidColor);
				Color StartColor = (Color) (MyF.getProperty("BottomColor"));
				VersionBuffer.chkNul(StartColor);
				setColors(StartColor, MidColor, EndColor);
			} catch (IOException ex) {
				throw (ex);
			} catch (ClassCastException ex) {
				throw (new DataFormatException(ex));
			}

			createColorTables();
			repaint();
		}

	}

	/**
	* Saves the component to persistent storage.
    * @param The flavor in which to save the data.
    * @return Transferable to which the data is saved.
	*/
	public Transferable savePersistentData(DataFlavor flavor) {
		TransVersionBuffer MyF = new TransVersionBuffer("FlowBox", "FlowBox");
		MyF.setInt("Resolution", img_size);
		MyF.setInt("TableSize", tbl_size);
		MyF.setInt("TableDelay", tbl_delay);
		MyF.setInt("NumFrames", num_frames);
		MyF.setInt("currentMode", currentMode);
		MyF.setProperty("TopColor", endColor);
		MyF.setProperty("midColor", midColor);
		MyF.setProperty("BottomColor", startColor);
		MyF.setBoolean("OnlyDesignerEdits", isOnlyDesignerEdits());
		macroMap.writeData(MyF);
		return (MyF);
	}

	/**
	 * Handles a change to the current undo state.
	 */
	protected void handleUndoStateChange() {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		int img_size = model.pdxm_getResolution(mil);
		int tbl_size = model.pdxm_getColorTableSize(mil);
		int tbl_delay = model.pdxm_getDelay(mil);
		int num_frames = model.pdxm_getNumFrames(mil);
		int CurrentMode = model.pdxm_getEditMode(mil);
		Color EndColor = (Color) (model.pdxm_getEndColor(mil));
		Color MidColor = (Color) (model.pdxm_getMidColor(mil));
		Color StartColor = (Color) (model.pdxm_getStartColor(mil));
		setResolutionComp(img_size);
		setColorTableSizeComp(tbl_size);
		setDelayComp(tbl_delay);
		setNumFramesComp(num_frames);
		setEditModeComp(CurrentMode);
		setColorsComp(StartColor, MidColor, EndColor);
	}

	/**
	 * Handles property change events.
	 * @param evt The event to be handled.
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == UndoManager.MODEL_STATE_CHANGED) {
			handleUndoStateChange();
		}

	}

	/**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		FlowBoxHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		FlowBox MyComp = new FlowBox();
		ProgramDirector.showComponent(MyComp, "FlowBox", argv, false);
	}

}
