package verdantium.demo.poseidon;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.image.ImageProducer;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.Timer;

import jundo.runtime.ExtMilieuRef;
import jundo.util.array.pdx_JobjArray_pdx_ObjectRef;
import jundo.util.array.pdx_JobjArray_pdx_PairRef;
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
import verdantium.demo.poseidon.help.PoseidonHelp;
import verdantium.undo.UTag;
import verdantium.undo.UndoManager;
import verdantium.utils.IllegalInputException;
import verdantium.utils.VerdantiumDragUtils;
import verdantium.utils.VerdantiumDropUtils;
import verdantium.xapp.DocPageFormat;
import verdantium.xapp.JcApplicationAdapter;
import verdantium.xapp.MacroTreeMap;
import verdantium.xapp.OnlyDesignerEdits;

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
* This is a demonstration of a very simple ripple tank application that can be embedded
* in other components.
* <P>
* @author Thorn Green
*/
public class Poseidon
	extends JcApplicationAdapter
	implements
		GuiShowNotify {
	
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
	* Indicates whether the component has been painted in order to get around some
	* current implementation problems (i.e. it's a kluge).
	*/
	transient private boolean painted = false;

	/**
	* Property name indivating that the wave colors have been changed.
	*/
	public static final String PoseidonColors = "PoseidonColors";
	
	/**
	* Ether Event name for setting the resolution of the wave.
	*/
	public static final String setPoseidonResolution = "setPoseidonResolution";
	
	/**
	* Ether Event name for setting the colors of the wave display.
	*/
	public static final String setPoseidonColors = "setPoseidonColors";
	
	/**
	* Ether Event name for setting whether the component is animated.
	*/
	public static final String setPoseidonAnimated = "setPoseidonAnimated";
	
	/**
	* Ether Event name for adding a wave to the simulation.
	*/
	public static final String addPoseidonWave = "addPoseidonWave";
	
	/**
	* Ether Event name for removing the last wave from the simulation.
	*/
	public static final String removeLastWave = "removeLastWave";

	/**
	* The maximum number of frames in the animation.
	*/
	transient protected int max_idex = 6;
	
	/**
	* The animated set of images.
	*/
	transient protected Image[] images = null;
	
	/**
	* The current "frame" in the animation.
	*/
	transient protected int cur_idex = 0;
	
	/**
	* The pixel size of the displayed images.
	*/
	transient protected int img_size = 100;
	
	/**
	* The set of pixels displayed using different color models.
	*/
	transient protected byte[] pixels = null;
	
	/**
	* The set of color tables used to display the animation.
	*/
	transient protected IndexColorModel[] color_tables = null;
	
	/**
	* The color of the trough of the wave in the simulation.
	*/
	transient protected Color StartColor = new Color(0, 0, 255);
	
	/**
	* The color of the middle of the wave in the simulation.
	*/
	transient protected Color MidColor = new Color(64, 64, 255);
	
	/**
	* The color of the wave peak in the simulation.
	*/
	transient protected Color EndColor = new Color(127, 127, 255);

	/**
	* The number of waves to be animated.
	*/
	transient protected int num_waves = 0;
	
	/**
	* An array containing all of the waves.
	*/
	transient protected WaveNode[] waves = null;
	
	/**
	* The value of a wave calculated by the {@link #calculateWave} method.  This value is
	* then read by other methods which call calculateWave().
	*/
	transient protected double wave_value;
	
	/**
	* The derivative of a wave calculated by the {@link #calculateWave} method.  This value is
	* then read by other methods which call calculateWave().
	*/
	transient protected double derivative_value;
	
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
	protected pdx_PoseidonModel_pdx_ObjectRef model = null;

	/**
	* Constructs the Poseidon component.
	*/
	public Poseidon() {
		initializeUndoMgr();
		macroMap = new MacroTreeMap(undoMgr);
		docPageFormat = new DocPageFormat(undoMgr);
		onlyDesignerEdits = new OnlyDesignerEdits(undoMgr);
		undoMgr.addPropertyChangeListener(this);
		PropL = new PropertyChangeSupport(this);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		setOpaque(true);
		setMinimumSize(new Dimension(2, 2));
		setPreferredSize(new Dimension(400, 400));
		setDoubleBuffered(false);
		ActionListener MyL =
			Adapters.createGActionListener(this, "handleTimerEvent");
		MyTime = new Timer(100, MyL);
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
		ExtMilieuRef mil = jundo.runtime.Runtime.getInitialMilieu();
		pdx_JobjArray_pdx_PairRef aref =
			pdx_JobjArray_pdx_ObjectRef.pdxm_allocate_JobjArray(mil, 0);
		mil = aref.getMilieu();
		pdx_JobjArray_pdx_ObjectRef arr =
			(pdx_JobjArray_pdx_ObjectRef) (aref.getObject());
		pdx_PoseidonModel_pdx_PairRef ref =
			pdx_PoseidonModel_pdx_ObjectRef.pdxm_new_PoseidonModel(
				mil,
				img_size,
				arr,
				EndColor,
				MidColor,
				StartColor);
		model = (pdx_PoseidonModel_pdx_ObjectRef) (ref.getObject());
		undoMgr = UndoManager.createInstanceUndoManager(ref.getMilieu());
	}

	@Override
	public void processMouseEvent(MouseEvent e) {
		switch (e.getID()) {
			case MouseEvent.MOUSE_PRESSED :
				try {
					Boolean param = new Boolean(!animated);
					EtherEvent send =
						new PropertyEditEtherEvent(
							this,
							Poseidon.setPoseidonAnimated,
							null,
							this);
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
	* @param Whether the component is animated.
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
				EtherEvent send =
					new StandardEtherEvent(
						this,
						StandardEtherEvent.showPropertiesEditor,
						null,
						this);
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
	public void handleTimerEvent(ActionEvent e) {
		if (!(isShowing()) && !aniTurnedOff) {
			MyTime.stop();
			aniTurnedOff = true;
		} else {
			cur_idex++;
			if (cur_idex >= max_idex)
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

		Rectangle MyRect = getBounds();
		g.setColor(getBackground());
		g.fillRect(MyRect.x, MyRect.y, MyRect.width, MyRect.height);
		Image Im = images[cur_idex];
		int wid = Math.min(MyRect.width, MyRect.height);

		if (MyRect.width > wid) {
			g.fillRect(wid + 1, 0, MyRect.width - wid, MyRect.height);
		} else {
			g.fillRect(0, wid + 1, MyRect.width, MyRect.height - wid);
		}

		g.drawImage(
			Im,
			0,
			0,
			wid - 1,
			wid - 1,
			0,
			0,
			img_size - 1,
			img_size - 1,
			this);
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	/**
	* Creates the pixel source for an image.
	* @param idex The index to the frame of animation.
	* @return The generated pixel source.
	*/
	protected ImageProducer createImageSource(int idex) {
		return (
			new MemoryImageSource(
				img_size,
				img_size,
				color_tables[idex],
				pixels,
				0,
				img_size));
	}

	/**
	* Creates one image for a particular animation index.
	* @param idex The index to the frame of animation.
	* @return The generated image.
	*/
	protected Image createImage(int idex) {
		ImageProducer MyS = createImageSource(idex);
		Image Im = createImage(MyS);
		return (Im);
	}

	/**
	* Creates the images to be displayed by the simulation.
	*/
	protected void createImages() {
		int count;
		images = new Image[max_idex];
		for (count = 0; count < max_idex; ++count) {
			images[count] = createImage(count);
		}
	}

	/**
	* Gets the red value for a particular index and color table offset.
	* @param idex The index to the frame of animation.
	* @param offset The offset into the color table.
	* @return The red value.
	*/
	protected byte getIndexRed(int idex, int offset) {
		int st_red = StartColor.getRed();
		int mid_red = MidColor.getRed();
		int end_red = EndColor.getRed();
		byte ret = 0;

		int old_idex = idex;
		idex = idex + offset;
		idex += idex / 128 - old_idex / 128;
		idex = idex % 256;
		if (idex > 127)
			idex = 127 - (idex - 128);

		if (idex > 63) {
			double u = (idex - 64) / (double) (127 - 64);
			ret = (byte) ((1 - u) * mid_red + u * end_red);
		} else {
			double u = (idex) / (double) (63);
			ret = (byte) ((1 - u) * st_red + u * mid_red);
		}

		return (ret);
	}

	/**
	* Gets the green value for a particular index and color table offset.
	* @param idex The index to the frame of animation.
	* @param offset The offset into the color table.
	* @return The green value.
	*/
	protected byte getIndexGreen(int idex, int offset) {
		int st_green = StartColor.getGreen();
		int mid_green = MidColor.getGreen();
		int end_green = EndColor.getGreen();
		byte ret = 0;

		int old_idex = idex;
		idex = idex + offset;
		idex += idex / 128 - old_idex / 128;
		idex = idex % 256;
		if (idex > 127)
			idex = 127 - (idex - 128);

		if (idex > 63) {
			double u = (idex - 64) / (double) (127 - 64);
			ret = (byte) ((1 - u) * mid_green + u * end_green);
		} else {
			double u = (idex) / (double) (63);
			ret = (byte) ((1 - u) * st_green + u * mid_green);
		}

		return (ret);
	}

	/**
	* Gets the blue value for a particular index and color table offset.
	* @param idex The index to the frame of animation.
	* @param offset The offset into the color table.
	* @return The blue value.
	*/
	protected byte getIndexBlue(int idex, int offset) {
		int st_blue = StartColor.getBlue();
		int mid_blue = MidColor.getBlue();
		int end_blue = EndColor.getBlue();
		byte ret = 0;

		int old_idex = idex;
		idex = idex + offset;
		idex += idex / 128 - old_idex / 128;
		idex = idex % 256;
		if (idex > 127)
			idex = 127 - (idex - 128);

		if (idex > 63) {
			double u = (idex - 64) / (double) (127 - 64);
			ret = (byte) ((1 - u) * mid_blue + u * end_blue);
		} else {
			double u = (idex) / (double) (63);
			ret = (byte) ((1 - u) * st_blue + u * mid_blue);
		}

		return (ret);
	}

	/**
	* Creates a particular indexed color table for the component.
	* @param idex The index to the frame of animation.
	* @return The geberated index color table.
	*/
	protected IndexColorModel createColorTable(int idex) {
		byte[] red = new byte[256];
		byte[] green = new byte[256];
		byte[] blue = new byte[256];
		int count;
		double offset = 2.0 * Math.PI * idex / max_idex;
		for (count = 0; count < 256; ++count) {
			int kval = count % 128;
			double val = 2.0 * (kval / 127.0 - 0.5);
			double theta = Math.asin(val);
			boolean cond1 = Math.cos(theta) >= 0.0;
			boolean cond2 = count >= 128;
			if ((cond1 && !cond2) || (!cond1 && cond2))
				theta -= offset;
			else
				theta += offset;

			val = Math.sin(theta);
			kval = (int) (((val / 2.0) + 0.5) * 128.0);
			if (kval > 127)
				kval = 127;

			red[count] = getIndexRed(kval, 0);
			green[count] = getIndexGreen(kval, 0);
			blue[count] = getIndexBlue(kval, 0);
		}

		IndexColorModel Im = new IndexColorModel(8, 256, red, green, blue);
		return (Im);
	}

	/**
	* Creates the color tables for the component.
	*/
	protected void createColorTables() {
		color_tables = new IndexColorModel[max_idex];
		int count;
		for (count = 0; count < max_idex; count++) {
			color_tables[count] = createColorTable(count);
		}
	}

	/**
	* Calculates the value of a particular wave at a position (x, y).  This method sets the
	* {@link #wave_value} and {@link #derivative_value} members, and the calling methods read these
	* members in order to determine the wave's characteristics.
	* @param in The wave node for which to calculate the wave value.
	* @param x The x-coordinate at which to calculate the wave value.
	* @param y The y-coordinate at which to calculate the wave value.
	*/
	protected void calculateWave(WaveNode in, double x, double y) {
		double delx = x - in.getX();
		double dely = y - in.getY();
		double dist = Math.sqrt(delx * delx + dely * dely);

		double val = 2.0 * Math.PI / in.getPeriod();
		double val2 = val * dist + in.getPhase();
		wave_value += in.getMagnitude() * (Math.sin(val2) + 1.0);
		derivative_value += val * in.getMagnitude() * Math.cos(val2);
	}

	/**
	* Superpositions all waves for a particular (x, y) point, and puts the result 
	* in an array.
	* @param x The x-coordinate at which to calculate the wave values.
	* @param y The y-coordinate at which to calculate the wave values.
	*/
	protected void calculateWaves(double x, double y) {
		int count;
		wave_value = 0.0;
		derivative_value = 0.0;
		for (count = 0; count < num_waves; count++) {
			WaveNode MyN = waves[count];
			calculateWave(MyN, x, y);
		}
	}

	/**
	* Calculates the displayed pixels for a set of animation.
	*/
	protected void calculatePixels() {
		int sx;
		int sy;
		double max_mag = 0.0;
		int count;

		for (count = 0; count < num_waves; ++count)
			max_mag += waves[count].getMagnitude();

		max_mag = max_mag * 2.0;

		for (sy = 0; sy < img_size; ++sy) {
			for (sx = 0; sx < img_size; ++sx) {
				double x = sx / (double) img_size;
				double y = sy / (double) img_size;
				calculateWaves(x, y);
				double dval = wave_value / max_mag;
				byte val = (byte) (128 * dval);
				if (val > 127)
					val = 127;
				if (derivative_value < 0.0)
					val = (byte) (val + 128);
				pixels[img_size * sy + sx] = val;
			}
		}
	}

	/**
	* Handles the showing of the component by performing actions (like creating images) that
	* can only happen after the component's GUI has a peer.
	*/
	public void guiShowNotify() {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		pixels = new byte[img_size * img_size];
		num_waves = 2;
		waves = new WaveNode[2];
		pdx_JobjArray_pdx_PairRef aref =
			pdx_JobjArray_pdx_ObjectRef.pdxm_allocate_JobjArray(mil, 2);
		mil = aref.getMilieu();
		pdx_JobjArray_pdx_ObjectRef uNewWave =
			(pdx_JobjArray_pdx_ObjectRef) (aref.getObject());
		mil = model.pdxm_setWaves(mil, uNewWave);
		WaveNode wv1 = new WaveNode(1, 0.25, 0.25, 0, 0.125);
		WaveNode wv2 = new WaveNode(1, 0.75, 0.6, 0, 0.125);
		waves[0] = wv1;
		mil = uNewWave.pdxm_s(mil, 0, wv1);
		waves[1] = wv2;
		mil = uNewWave.pdxm_s(mil, 1, wv2);
		undoMgr.handleCommitTempChange(mil);
		calculatePixels();
		createColorTables();
		createImages();
	}

	/**
	* Removes the last wave from the list of waves for the component.
	*/
	protected void removeLastWave() {
		if (num_waves > 0) {
			ExtMilieuRef mil = undoMgr.getCurrentMil();
			WaveNode[] NewWave = new WaveNode[num_waves - 1];
			pdx_JobjArray_pdx_PairRef aref =
				pdx_JobjArray_pdx_ObjectRef.pdxm_allocate_JobjArray(
					mil,
					num_waves - 1);
			mil = aref.getMilieu();
			pdx_JobjArray_pdx_ObjectRef uNewWave =
				(pdx_JobjArray_pdx_ObjectRef) (aref.getObject());
			int count;
			for (count = 0; count < (num_waves - 1); ++count) {
				NewWave[count] = waves[count];
				mil = uNewWave.pdxm_s(mil, count, waves[count]);
			}

			waves = NewWave;
			mil = model.pdxm_setWaves(mil, uNewWave);
			undoMgr.handleCommitTempChange(mil);
			num_waves--;
			calculatePixels();
			createImages();
			repaint();
		}
	}

	/**
	 * Adds a wave to the component.
	 * @param sx The X coordinate of a particular wave in normalized device coordinates.
	 * @param sy The Y coordinate of a particular wave in normalized device coordinates.
	 * @param magnitude The magnitude of a particular wave.
	 * @param period The period of the wave (represented as the wavelength in normalized device coordinates).
	 * @param phase The phase of the wave in radians.
	 * @throws IllegalInputException
	 */
	protected void addWave(
		int sx,
		int sy,
		double magnitude,
		double period,
		double phase)
		throws IllegalInputException {
		if (magnitude < 0.0)
			throw (
				new IllegalInputException("The magnitude must be positive."));

		if (period <= 0.0)
			throw (new IllegalInputException("The period must be positive."));

		Rectangle bounds = getBounds();
		WaveNode MyN = new WaveNode();
		MyN =
			new WaveNode(
				magnitude,
				sx / (double) bounds.width,
				sy / (double) bounds.height,
				Math.PI * phase / 180.0,
				period);

		ExtMilieuRef mil = undoMgr.getCurrentMil();
		WaveNode[] NewWave = new WaveNode[num_waves + 1];
		pdx_JobjArray_pdx_PairRef aref =
			pdx_JobjArray_pdx_ObjectRef.pdxm_allocate_JobjArray(
				mil,
				num_waves + 1);
		mil = aref.getMilieu();
		pdx_JobjArray_pdx_ObjectRef uNewWave =
			(pdx_JobjArray_pdx_ObjectRef) (aref.getObject());
		int count;
		for (count = 0; count < num_waves; ++count) {
			NewWave[count] = waves[count];
			mil = uNewWave.pdxm_s(mil, count, waves[count]);
		}

		NewWave[num_waves] = MyN;
		mil = uNewWave.pdxm_s(mil, num_waves, MyN);

		waves = NewWave;
		mil = model.pdxm_setWaves(mil, uNewWave);
		undoMgr.handleCommitTempChange(mil);
		num_waves++;
		calculatePixels();
		createImages();
		repaint();
	}

	/**
	* Gets the color of the trough of the displayed wave.
	* @return The color of the trough of the displayed wave.
	*/
	public Color getBottomColor() {
		return (StartColor);
	}

	/**
	* Gets the color of the middle of the displayed wave.
	* @return The color of the middle of the displayed wave.
	*/
	public Color getMidColor() {
		return (MidColor);
	}

	/**
	* Gets the color of the peak of the displayed wave.
	* @return The color of the peak of the displayed wave.
	*/
	public Color getTopColor() {
		return (EndColor);
	}

	/**
	* Sets the resolution of the component, and updates the undo state.
	* @param in The input resolution.
	*/
	protected void setResolution(int in) {
		ExtMilieuRef mil =
			model.pdxm_setResolution(undoMgr.getCurrentMil(), in);
		undoMgr.handleCommitTempChange(mil);
		setResolutionComp(in);
	}

	/**
	* Sets the resolution of the component.
	* @param in The input resolution.
	*/
	protected void setResolutionComp(int in) {
		img_size = in;
		pixels = new byte[img_size * img_size];
		calculatePixels();
		createImages();
		repaint();
	}

	/**
	 * Sets the component's wave colors, and updates the undo state.
	 * @param Start The color of the trough of the wave in the simulation.
	 * @param Mid The color of the middle of the wave in the simulation.
	 * @param End The color of the peak of the wave in the simulation.
	 */
	protected void setColors(Color Start, Color Mid, Color End) {
		ExtMilieuRef mil =
			model.pdxm_setStartColor(undoMgr.getCurrentMil(), Start);
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
		StartColor = Start;
		MidColor = Mid;
		EndColor = End;
		createColorTables();
		createImages();
		repaint();
		PropL.firePropertyChange(PoseidonColors, null, null);
	}

	/**
	* Handles the destruction of the component by stopping the timer that drives the animation.
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

			if (in.getEtherID().equals(removeLastWave)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				removeLastWave();
				undoMgr.commitUndoableOp(utag, "Remove Last Wave");
			}

			if (in.getEtherID().equals(setPoseidonResolution)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				Integer param = (Integer) (in.getParameter());
				setResolution(param.intValue());
				undoMgr.commitUndoableOp(utag, "Resolution Change");
			}

			if (in.getEtherID().equals(addPoseidonWave)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				Object[] param = (Object[]) (in.getParameter());
				int x = ((Integer) (param[0])).intValue();
				int y = ((Integer) (param[1])).intValue();
				double magnitude = ((Double) (param[2])).doubleValue();
				double period = ((Double) (param[3])).doubleValue();
				double phase = ((Double) (param[4])).doubleValue();
				addWave(x, y, magnitude, period, phase);
				undoMgr.commitUndoableOp(utag, "Add Wave");
			}

			if (in.getEtherID().equals(setPoseidonColors)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				Object[] param = (Object[]) (in.getParameter());
				Color c1 = (Color) (param[0]);
				Color c2 = (Color) (param[1]);
				Color c3 = (Color) (param[2]);
				setColors(c1, c2, c3);
				undoMgr.commitUndoableOp(utag, "Color Change");
			}

			if (in.getEtherID().equals(setPoseidonAnimated)) {
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
		PoseidonPropertyEditor MyEdit = new PoseidonPropertyEditor(this, MyP);
		MyEdit.setClickPoint(new Point(10, 10));
		return (MyEdit);
	}

	/**
	 * Shows the properties editor for the component.
	 * @param e The event for showing the editor.
	 */
	public void showPropertiesEditor(EtherEvent e) {
		VerdantiumPropertiesEditor MyEdit = makePropertiesEditor();
		((DefaultPropertyEditor) MyEdit).setClickPoint(
			(Point) (e.getParameter()));
		ProgramDirector.showPropertyEditor(
			MyEdit,
			getGUI(),
			"Poseidon Property Editor");
	}

	/**
	* Returns the data flavors that the component can load from persistent storage.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{ new TransVersionBufferFlavor("Poseidon", "Poseidon")};
		return (MyF);
	}

	/**
	* Returns the data flavors that the component can save to persistent storage.
    * @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{ new TransVersionBufferFlavor("Poseidon", "Poseidon")};
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
			docPageFormat.setDocPageFormat(null);
			num_waves = 0;
			waves = new WaveNode[0];
			ExtMilieuRef mil = undoMgr.getCurrentMil();
			pdx_JobjArray_pdx_PairRef aref =
				pdx_JobjArray_pdx_ObjectRef.pdxm_allocate_JobjArray(mil, 0);
			mil = aref.getMilieu();
			pdx_JobjArray_pdx_ObjectRef uNewWave =
				(pdx_JobjArray_pdx_ObjectRef) (aref.getObject());
			mil = model.pdxm_setWaves(mil, uNewWave);
			undoMgr.handleCommitTempChange(mil);
			setResolution(100);
			Color StartColor = new Color(0, 0, 255);
			Color MidColor = new Color(64, 64, 255);
			Color EndColor = new Color(127, 127, 255);
			setColors(StartColor, MidColor, EndColor);
			createColorTables();
			createImages();
			repaint();
		} else {
			try {
				TransVersionBuffer MyF = (TransVersionBuffer) trans;
				VersionBuffer.chkNul(MyF);
				onlyDesignerEdits.setOnlyDesignerEdits(
					MyF.getBoolean("OnlyDesignerEdits"));
				macroMap.readData(MyF);
				setResolution(MyF.getInt("Resolution"));
				num_waves = MyF.getInt("NumWaves");
				waves = (WaveNode[]) (MyF.getProperty("Waves"));
				VersionBuffer.chkNul(waves);
				ExtMilieuRef mil = undoMgr.getCurrentMil();
				pdx_JobjArray_pdx_PairRef aref =
					pdx_JobjArray_pdx_ObjectRef.pdxm_allocate_JobjArray(
						mil,
						num_waves);
				mil = aref.getMilieu();
				pdx_JobjArray_pdx_ObjectRef uNewWave =
					(pdx_JobjArray_pdx_ObjectRef) (aref.getObject());
				int count;
				for (count = 0; count < num_waves; count++) {
					mil = uNewWave.pdxm_s(mil, count, waves[count]);
				}
				mil = model.pdxm_setWaves(mil, uNewWave);
				undoMgr.handleCommitTempChange(mil);
				Color EndColor = (Color) (MyF.getProperty("TopColor"));
				VersionBuffer.chkNul(EndColor);
				Color MidColor = (Color) (MyF.getProperty("MidColor"));
				VersionBuffer.chkNul(MidColor);
				Color StartColor = (Color) (MyF.getProperty("BottomColor"));
				VersionBuffer.chkNul(StartColor);
				setColors(StartColor, MidColor, EndColor);
			} catch (IOException ex) {
				throw (ex);
			} catch (ClassCastException ex) {
				throw (new DataFormatException(ex));
			}

			pixels = new byte[img_size * img_size];
			calculatePixels();
			createColorTables();
			createImages();
			repaint();
		}

	}

	/**
	* Saves the component to persistent storage.
    * @param The flavor in which to save the data.
    * @return Transferable to which the data is saved.
	*/
	public Transferable savePersistentData(DataFlavor flavor) {
		TransVersionBuffer MyF = new TransVersionBuffer("Poseidon", "Poseidon");
		MyF.setInt("Resolution", img_size);
		MyF.setInt("NumWaves", num_waves);
		MyF.setProperty("Waves", waves);
		MyF.setProperty("TopColor", EndColor);
		MyF.setProperty("MidColor", MidColor);
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
		int img_size = model.pdxm_getResolution(mil);
		pdx_JobjArray_pdx_ObjectRef arr = model.pdxm_getWaves(mil);
		Color EndColor = (Color) (model.pdxm_getEndColor(mil));
		Color MidColor = (Color) (model.pdxm_getMidColor(mil));
		Color StartColor = (Color) (model.pdxm_getStartColor(mil));
		setResolutionComp(img_size);
		num_waves = arr.pdxm_length(mil);
		waves = new WaveNode[num_waves];
		int count;
		for (count = 0; count < num_waves; count++) {
			waves[count] = (WaveNode) (arr.pdxm_g(mil, count));
		}
		calculatePixels();
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
		PoseidonHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		Poseidon MyComp = new Poseidon();
		ProgramDirector.showComponent(MyComp, "Poseidon", argv, false);
	}

	
}

