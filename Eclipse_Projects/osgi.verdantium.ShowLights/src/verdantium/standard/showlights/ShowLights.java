package verdantium.standard.showlights;

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
import verdantium.EtherEvent;
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
import verdantium.standard.showlights.help.ShowLightsHelp;
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
 * Provides animation simulating a lighted display for use in presentation
 * graphics.
 * 
 * @author Thorn Green
 */
public class ShowLights extends JcApplicationAdapter implements ActionListener {
	
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
	 * Property name indicating that the colors have been changed.
	 */
	public static final String ShowLightsColors = "ShowLightsColors";

	/**
	 * Ether Event name for setting the numper of pixels in each grid.
	 */
	public static final String setShowLightsResolution = "setShowLightsResolution";

	/**
	 * Ether Event name for setting the inset of the lighted oval.
	 */
	public static final String setShowLightsInset = "setShowLightsInset";

	/**
	 * Ether Event name for setting the flow box delay.
	 */
	public static final String setShowLightsDelay = "setShowLightsDelay";

	/**
	 * Ether Event name for setting the flow box number of animation frames (per
	 * sequence).
	 */
	public static final String setShowLightsNumFrames = "setShowLightsNumFrames";

	/**
	 * Ether Event name for setting the colors of the display.
	 */
	public static final String setShowLightsColors = "setShowLightsColors";

	/**
	 * Ether Event name for setting whether the component is animated.
	 */
	public static final String setShowLightsAnimated = "setShowLightsAnimated";

	/**
	 * The maximum number of frames in the animation.
	 */
	transient protected int num_frames = 6;

	/**
	 * The current "frame" in the animation.
	 */
	transient protected int cur_idex = 0;

	/**
	 * Table delay time in mills.
	 */
	transient protected int tbl_delay = 100;

	/**
	 * The "off" color.
	 */
	transient protected Color StartColor = Color.yellow;

	/**
	 * The "on" color.
	 */
	transient protected Color EndColor = Color.white;

	/**
	 * The size of the circle swatch used to draw a show light.
	 */
	transient protected int swatchSize = 50;

	/**
	 * The inset of the filled circle inside the swatch.
	 */
	transient protected int swatchInset = 5;

	/**
	 * Indicates whether the component is being animated.
	 */
	transient protected boolean animated = true;

	/**
	 * Indicates whether animation was turned off by visibility.
	 */
	transient protected boolean aniTurnedOff = false;

	protected pdx_ShowLightsModel_pdx_ObjectRef model = null;

	/**
	 * Constructs the ShowLights component.
	 */
	public ShowLights() {
		initializeUndoMgr();
		macroMap = new MacroTreeMap(undoMgr);
		docPageFormat = new DocPageFormat(undoMgr);
		onlyDesignerEdits = new OnlyDesignerEdits(undoMgr);
		undoMgr.addPropertyChangeListener(this);
		PropL = new PropertyChangeSupport(this);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		setOpaque(false);
		setMinimumSize(new Dimension(2, 2));
		setPreferredSize(new Dimension(300, 300));
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
		pdx_ShowLightsModel_pdx_PairRef ref = pdx_ShowLightsModel_pdx_ObjectRef
				.pdxm_new_ShowLightsModel(jundo.runtime.Runtime
						.getInitialMilieu(), false, swatchSize, swatchInset,
						tbl_delay, num_frames, EndColor, StartColor);
		model = (pdx_ShowLightsModel_pdx_ObjectRef) (ref.getObject());
		undoMgr = UndoManager.createInstanceUndoManager(ref.getMilieu());
	}

	@Override
	public void processMouseEvent(MouseEvent e) {
		switch (e.getID()) {
		case MouseEvent.MOUSE_PRESSED:
			try {
				Boolean param = new Boolean(!animated);
				EtherEvent send = new PropertyEditEtherEvent(this,
						ShowLights.setShowLightsAnimated, null, this);
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
	 * Handles a timer event by repainting the animation. Some explanation is in
	 * order for why paintChanges() is being used instead of a set of standard
	 * repaints. Swing's handling of repaint is not as advanced as the repaint
	 * handlers on other platforms. For instance, on the Mac the union of two
	 * repaint (e.g. invalidated) rectangles produces a Region containing a set
	 * of disjoint rectangles. In Swing RepaintManager, the union of two
	 * rectangles produces a single result rectangle spatially containing both
	 * of the input rectangles. This means that in Swing, as opposed to Mac OS,
	 * sending a repaint request for each light that changed will result, by
	 * union of change rectangles, in essentially a repaint of the entire
	 * component. This has unacceptable performance consequences, particularly
	 * if the ShowLights is sitting on top of a GradientPane. As a result, this
	 * method directly controls the rendering of the animation instead of using
	 * the standard Swing repaint process. Sun Microsystems: If you're
	 * listening, the way rectangle unions are performed in RepaintManager is
	 * problematic from a performance standpoint and you should change it.
	 */
	public void actionPerformed(ActionEvent e) {
		if (!(isShowing()) && !aniTurnedOff) {
			MyTime.stop();
			aniTurnedOff = true;
		} else {
			Graphics g = getGraphics();
			paintChanges(g, StartColor);
			cur_idex++;
			if (cur_idex >= num_frames)
				cur_idex = 0;
			paintChanges(g, EndColor);
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

		Rectangle MyRect = getBounds();

		int numX = MyRect.width / swatchSize;

		int numY = MyRect.height / swatchSize;

		int offset = -cur_idex;

		int count;

		int dWidth = swatchSize - 2 * swatchInset;

		int tmpX = 0;
		int tmpY = swatchInset;

		for (count = 0; count < (numX - 1); count++) {
			if ((offset % num_frames) == 0)
				g.setColor(EndColor);
			else
				g.setColor(StartColor);
			g.fillOval(swatchSize * count + swatchInset, tmpY, dWidth, dWidth);
			offset++;
		}

		tmpX = swatchSize * (numX - 1) + swatchInset;

		for (count = 0; count < (numY - 1); count++) {
			if ((offset % num_frames) == 0)
				g.setColor(EndColor);
			else
				g.setColor(StartColor);
			g.fillOval(tmpX, swatchSize * count + swatchInset, dWidth, dWidth);
			offset++;
		}

		tmpY = swatchSize * (numY - 1) + swatchInset;

		for (count = numX - 1; count > 0; count--) {
			if ((offset % num_frames) == 0)
				g.setColor(EndColor);
			else
				g.setColor(StartColor);
			g.fillOval(swatchSize * count + swatchInset, tmpY, dWidth, dWidth);
			offset++;
		}

		tmpX = swatchInset;

		for (count = numY - 1; count > 0; count--) {
			if ((offset % num_frames) == 0)
				g.setColor(EndColor);
			else
				g.setColor(StartColor);
			g.fillOval(tmpX, swatchSize * count + swatchInset, dWidth, dWidth);
			offset++;
		}

	}

	/**
	 * Paints changes to the component.
	 * @param g The graphics context in which to paint.
	 * @param col The paint color.
	 */
	public void paintChanges(Graphics g, Color col) {
		if (!painted && animated)
			MyTime.start();
		painted = true;
		if (aniTurnedOff)
			MyTime.start();
		aniTurnedOff = false;

		g.setColor(col);

		Rectangle MyRect = getBounds();

		int numX = MyRect.width / swatchSize;

		int numY = MyRect.height / swatchSize;

		int offset = -cur_idex;

		int count;

		int dWidth = swatchSize - 2 * swatchInset;

		int tmpX = 0;
		int tmpY = swatchInset;

		for (count = 0; count < (numX - 1); count++) {
			if ((offset % num_frames) == 0)
				g.fillOval(swatchSize * count + swatchInset, tmpY, dWidth,
						dWidth);
			offset++;
		}

		tmpX = swatchSize * (numX - 1) + swatchInset;

		for (count = 0; count < (numY - 1); count++) {
			if ((offset % num_frames) == 0)
				g.fillOval(tmpX, swatchSize * count + swatchInset, dWidth,
						dWidth);
			offset++;
		}

		tmpY = swatchSize * (numY - 1) + swatchInset;

		for (count = numX - 1; count > 0; count--) {
			if ((offset % num_frames) == 0)
				g.fillOval(swatchSize * count + swatchInset, tmpY, dWidth,
						dWidth);
			offset++;
		}

		tmpX = swatchInset;

		for (count = numY - 1; count > 0; count--) {
			if ((offset % num_frames) == 0)
				g.fillOval(tmpX, swatchSize * count + swatchInset, dWidth,
						dWidth);
			offset++;
		}

	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	/**
	 * Gets the "off" color of the animation.
	 * @return The "off" color.
	 */
	public Color getBottomColor() {
		return (StartColor);
	}

	/**
	 * Gets the "on" color of the animation.
	 * @return The "on" color.
	 */
	public Color getTopColor() {
		return (EndColor);
	}

	/**
	 * Sets the resolution of the component, and sets the undo state.
	 * @param in The resolution in pixels.
	 */
	protected void setResolution(int in) {
		ExtMilieuRef mil = model
				.pdxm_setResolution(undoMgr.getCurrentMil(), in);
		undoMgr.handleCommitTempChange(mil);
		setResolutionComp(in);
	}

	/**
	 * Sets the resolution of the component.
	 * @param in The resolution in pixels.
	 */
	protected void setResolutionComp(int in) {
		swatchSize = in;
		repaint();
	}

	/**
	 * Sets the inset of the component, and sets the undo state.
	 * @param in The number of pixels for the inset.
	 */
	protected void setInset(int in) {
		ExtMilieuRef mil = model.pdxm_setInset(undoMgr.getCurrentMil(), in);
		undoMgr.handleCommitTempChange(mil);
		setInsetComp(in);
	}

	/**
	 * Sets the inset of the component.
	 * @param in The number of pixels for the inset.
	 */
	protected void setInsetComp(int in) {
		swatchInset = in;
		repaint();
	}

	/**
	 * Sets the delay of the component, and sets the undo state.
	 * @param in The delay.
	 */
	protected void setDelay(int in) {
		ExtMilieuRef mil = model.pdxm_setDelay(undoMgr.getCurrentMil(), in);
		undoMgr.handleCommitTempChange(mil);
		setDelayComp(in);
	}

	/**
	 * Sets the delay of the component.
	 * @param in The delay.
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
	 * Sets the number of frames in a complete animation sequence, and updates the undo state.
	 * @param in The number of frames.
	 */
	protected void setNumFrames(int in) {
		ExtMilieuRef mil = model.pdxm_setNumFrames(undoMgr.getCurrentMil(), in);
		undoMgr.handleCommitTempChange(mil);
		setNumFramesComp(in);
	}

	/**
	 * Sets the number of frames in a complete animation sequence.
	 * @param in The number of frames.
	 */
	protected void setNumFramesComp(int in) {
		num_frames = in;
		repaint();
	}

	/**
	 * Sets the component's rendering colors, and updates the undo state.
	 * @param Start The Start color.
	 * @param End The End color.
	 */
	protected void setColors(Color Start, Color End) {
		ExtMilieuRef mil = model.pdxm_setStartColor(undoMgr.getCurrentMil(),
				Start);
		mil = model.pdxm_setEndColor(mil, End);
		undoMgr.handleCommitTempChange(mil);
		setColorsComp(Start, End);
	}

	/**
	 * Sets the component's rendering colors.
	 * @param Start The Start color.
	 * @param End The End color.
	 */
	protected void setColorsComp(Color Start, Color End) {
		StartColor = Start;
		EndColor = End;
		repaint();
		PropL.firePropertyChange(ShowLightsColors, null, null);
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
	 * @param in The event to be handled.
	 * @param refcon A reference to context data for the event.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon)
			throws Throwable {

		Object ret = super.processObjEtherEvent(in, refcon);
		if (ret != EtherEvent.EVENT_NOT_HANDLED) {
			return (ret);
		}

		if (in instanceof PropertyEditEtherEvent) {

			if (in.getEtherID().equals(setShowLightsResolution)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				Integer param = (Integer) (in.getParameter());
				setResolution(param.intValue());
				undoMgr.commitUndoableOp(utag, "Resolution Change");
			}

			if (in.getEtherID().equals(setShowLightsInset)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				Integer param = (Integer) (in.getParameter());
				setInset(param.intValue());
				undoMgr.commitUndoableOp(utag, "Inset Change");
			}

			if (in.getEtherID().equals(setShowLightsDelay)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				Integer param = (Integer) (in.getParameter());
				setDelay(param.intValue());
				undoMgr.commitUndoableOp(utag, "Delay Change");
			}

			if (in.getEtherID().equals(setShowLightsNumFrames)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				Integer param = (Integer) (in.getParameter());
				setNumFrames(param.intValue());
				undoMgr.commitUndoableOp(utag, "Num Frames Change");
			}

			if (in.getEtherID().equals(setShowLightsColors)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				Object[] param = (Object[]) (in.getParameter());
				Color c1 = (Color) (param[0]);
				Color c2 = (Color) (param[1]);
				setColors(c1, c2);
				undoMgr.commitUndoableOp(utag, "Color Change");
			}

			if (in.getEtherID().equals(setShowLightsAnimated)) {
				Boolean param = (Boolean) (in.getParameter());
				setAnimated(param.booleanValue());
			}

		}

		return (null);
	}

	/**
	 * Handles a change to the state defining whether only the designer edits.
	 */
	public void handleOnlyDesignerEditsChange() {
		if (isOnlyDesignerEdits())
			setToolTipText(null);
		else
			setToolTipText("Right-Click to edit properties");
	}

	/**
	 * Creates the properties editor for the component.
	 * @return The created property editor.
	 */
	public VerdantiumPropertiesEditor makePropertiesEditor() {
		Properties MyP = new Properties();
		ShowLightsPropertyEditor MyEdit = new ShowLightsPropertyEditor(this,
				MyP);
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
				"ShowLights Property Editor");
	}

	/**
	* Returns the data flavors that the component can load from persistent storage.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF = { new TransVersionBufferFlavor("ShowLights",
				"ShowLights") };
		return (MyF);
	}

	/**
	* Returns the data flavors that the component can save to persistent storage.
    * @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF = { new TransVersionBufferFlavor("ShowLights",
				"ShowLights") };
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
			setResolution(50);
			setInset(5);
			setDelay(100);
			setNumFrames(6);
			setColors(Color.yellow, Color.white);
			repaint();
		} else {
			try {
				TransVersionBuffer MyF = (TransVersionBuffer) trans;
				VersionBuffer.chkNul(MyF);
				onlyDesignerEdits.setOnlyDesignerEdits(MyF
						.getBoolean("OnlyDesignerEdits"));
				macroMap.readData(MyF);
				setResolution(MyF.getInt("Resolution"));
				setInset(MyF.getInt("Inset"));
				setDelay(MyF.getInt("TableDelay"));
				setNumFrames(MyF.getInt("NumFrames"));
				Color EndColor = (Color) (MyF.getProperty("TopColor"));
				VersionBuffer.chkNul(EndColor);
				Color StartColor = (Color) (MyF.getProperty("BottomColor"));
				VersionBuffer.chkNul(StartColor);
				setColors(StartColor, EndColor);
			} catch (IOException ex) {
				throw (ex);
			} catch (ClassCastException ex) {
				throw (new DataFormatException(ex));
			}

			repaint();
		}

	}

	/**
	* Saves the component to persistent storage.
    * @param The flavor in which to save the data.
    * @return Transferable to which the data is saved.
	*/
	public Transferable savePersistentData(DataFlavor flavor) {
		TransVersionBuffer MyF = new TransVersionBuffer("ShowLights",
				"ShowLights");
		MyF.setInt("Resolution", swatchSize);
		MyF.setInt("Inset", swatchInset);
		MyF.setInt("TableDelay", tbl_delay);
		MyF.setInt("NumFrames", num_frames);
		MyF.setProperty("TopColor", EndColor);
		MyF.setProperty("BottomColor", StartColor);
		MyF.setBoolean("OnlyDesignerEdits", isOnlyDesignerEdits());
		macroMap.writeData(MyF);
		return (MyF);
	}

	/**
	 * Handles a change to the current undo state.
	 */
	protected void handleUndoStateChange() {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		int swatchSize = model.pdxm_getResolution(mil);
		int swatchInset = model.pdxm_getInset(mil);
		int tbl_delay = model.pdxm_getDelay(mil);
		int num_frames = model.pdxm_getNumFrames(mil);
		Color EndColor = (Color) (model.pdxm_getEndColor(mil));
		Color StartColor = (Color) (model.pdxm_getStartColor(mil));
		setResolutionComp(swatchSize);
		setInsetComp(swatchInset);
		setDelayComp(tbl_delay);
		setNumFramesComp(num_frames);
		setColorsComp(StartColor, EndColor);
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
		ShowLightsHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		ShowLights MyComp = new ShowLights();
		ProgramDirector.showComponent(MyComp, "ShowLights", argv, false);
	}

}
