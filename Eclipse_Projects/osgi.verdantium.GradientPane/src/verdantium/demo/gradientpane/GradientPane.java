package verdantium.demo.gradientpane;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JComponent;

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
import verdantium.core.DesignerControl;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.demo.gradientpane.help.GradientPaneHelp;
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
 *    | 01/28/2001            | Thorn Green (viridian_1138@yahoo.com)           | Multiple bugs in calling of handleDestroy()                          | Implemented a set of bug-fixes.
 *    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Macro support.                                                       | Added macro support.
 *    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
 *    | 09/29/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for multiple PLAFs.                                          | Made code changes to support multiple PLAFs.
 *    | 10/05/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for command-line arguments.                                  | Added support for command-line arguments.
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
 * This is a demonstration of a "Gradient Pane" component. It displays a pane
 * with a PowerPoint-like horizontal or vertical gradient (useful for
 * presentations and other types of graphics). This component can be embedded in
 * a container application (e.g. DrawApp) and used as a background for other
 * transparent content.
 * <P>
 * 
 * @author Thorn Green
 */
public class GradientPane extends JcApplicationAdapter {

	/**
	 * EtherEvent name for setting the colors of the gradient.
	 */
	public static String setGradientPaneColors = "setGradientPaneColors";

	/**
	 * EtherEvent name for setting the orientation of the gradient.
	 */
	public static String setGradientPaneVertical = "setGradientPaneVertical";

	/**
	 * Property name indicating that the colors of the gradient have changed.
	 */
	public static String GradientPaneColors = "GradientPaneColors";

	/**
	 * Property name indicating that the orientation of the gradient has
	 * changed.
	 */
	public static String GradientPaneVertical = "GradientPaneVertical";

	/**
	 * The data model for multi-level undo.
	 */
	protected pdx_GradientPaneModel_pdx_ObjectRef model = null;

	/**
	 * The gradient object used to draw the gradient.
	 */
	transient private GradientPaint myPaint = new GradientPaint((float) 0.0,
			(float) 0.0, Color.blue, (float) 0.0, (float) 100.0, Color.white);

	/**
	 * The previous bounds of the rectangle in which the gradient is drawn.
	 */
	transient Rectangle prevBounds = new Rectangle(0, 0, 100, 100);

	/**
	 * The first color of the gradient.
	 */
	protected Color coa = Color.blue;

	/**
	 * The second color of the gradient.
	 */
	protected Color cob = Color.white;

	/**
	 * Indicates whether the gradient is oriented vertical, as opposed to
	 * horizontal.
	 */
	protected boolean vertical = true;

	/**
	 * Gets the GUI used to draw "Gradient Pane".
	 */
	public JComponent getGUI() {
		return (this);
	}

	/**
	 * Constructs the GradientPane component.
	 */
	public GradientPane() {
		initializeUndoMgr();
		macroMap = new MacroTreeMap(undoMgr);
		docPageFormat = new DocPageFormat(undoMgr);
		onlyDesignerEdits = new OnlyDesignerEdits(undoMgr);
		undoMgr.addPropertyChangeListener(this);
		PropL = new PropertyChangeSupport(this);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		setOpaque(true);
		setMinimumSize(new Dimension(2, 2));
		setPreferredSize(new Dimension(200, 100));
		VerdantiumDragUtils.setDragUtil(this, this);
		VerdantiumDropUtils.setDropUtil(this, this, this);
		configureForEtherEvents();
	}

	/**
	 * Initializes the undo manager of the component.
	 */
	protected void initializeUndoMgr() {
		pdx_GradientPaneModel_pdx_PairRef ref = pdx_GradientPaneModel_pdx_ObjectRef
				.pdxm_new_GradientPaneModel(jundo.runtime.Runtime
						.getInitialMilieu(), coa, cob, vertical, false);
		model = (pdx_GradientPaneModel_pdx_ObjectRef) (ref.getObject());
		undoMgr = UndoManager.createInstanceUndoManager(ref.getMilieu());
	}

	@Override
	public void paint(Graphics g) {
		Rectangle MyRect = getBounds();

		if (!(MyRect.equals(prevBounds))) {
			prevBounds = MyRect;
			updateGradient();
		}

		((Graphics2D) g).setPaint(myPaint);
		g.fillRect(MyRect.x, MyRect.y, MyRect.width, MyRect.height);
	}

	@Override
	public void print(Graphics g) {
		if (true) {
			int count;
			Rectangle MyRect = getBounds();
			Graphics2D gr = (Graphics2D) g;
			Rectangle2D.Double drect = new Rectangle2D.Double();
			for (count = 0; count < 512; count++) {
				double u0 = count / (double) 512;
				double u1 = (count + 1) / (double) 512;
				double u3 = count / (double) 511;
				double red = (1 - u3) * (coa.getRed()) + u3 * (cob.getRed());
				double green = (1 - u3) * (coa.getGreen()) + u3
						* (cob.getGreen());
				double blue = (1 - u3) * (coa.getBlue()) + u3 * (cob.getBlue());
				Color c = new Color((int) red, (int) green, (int) blue);
				gr.setColor(c);
				if (vertical) {
					drect.setRect(0, u0 * (MyRect.height), MyRect.width,
							(u1 - u0) * (MyRect.height));
				} else {
					drect.setRect(u0 * (MyRect.width), 0, (u1 - u0)
							* (MyRect.width), MyRect.height);
				}
				gr.fill(drect);
			}
		} else {
			paint(g);
		}
	}

	@Override
	public void processMouseEvent(MouseEvent e) {
		switch (e.getID()) {
		case MouseEvent.MOUSE_PRESSED:
			if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
				handlePopupTrigger(e);
			break;

		}

		super.processMouseEvent(e);
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
	 * Updates the objects that display the gradient.
	 */
	protected void updateGradient() {
		if (vertical) {
			myPaint = new GradientPaint((float) 0.0, (float) 0.0, coa,
					(float) 0.0, (float) (prevBounds.height), cob);
		} else {
			myPaint = new GradientPaint((float) 0.0, (float) 0.0, coa,
					(float) (prevBounds.width), (float) 0.0, cob);
		}
	}

	/**
	 * Sets the colors of the gradient.
	 * @param ina The first color of the gradient.
	 * @param inb The second color of the gradient.
	 */
	protected void setColorsComp(Color ina, Color inb) {
		coa = ina;
		cob = inb;
		updateGradient();
		repaint();
		PropL.firePropertyChange(GradientPaneColors, null, null);
	}

	/**
	 * Sets the colors of the gradient, and updates the undo state.
	 * @param ina The first color of the gradient.
	 * @param inb The second color of the gradient.
	 */
	protected void setColors(Color ina, Color inb) {
		ExtMilieuRef mil = model.pdxm_setColorA(undoMgr.getCurrentMil(), ina);
		mil = model.pdxm_setColorB(mil, inb);
		undoMgr.handleCommitTempChange(mil);
		setColorsComp(ina, inb);
	}

	/**
	 * Sets whether the gradient is oriented vertical, as opposed to horizontal.
	 * @param in Whether the gradient is oriented vertical, as opposed to horizontal.
	 */
	public void setVerticalComp(boolean in) {
		vertical = in;
		updateGradient();
		repaint();
		PropL.firePropertyChange(GradientPaneVertical, null, null);
	}

	/**
	 * Sets whether the gradient is oriented vertical, as opposed to horizontal, and updates the undo state.
	 * @param in Whether the gradient is oriented vertical, as opposed to horizontal.
	 */
	public void setVertical(boolean in) {
		ExtMilieuRef mil = model.pdxm_setVertical(undoMgr.getCurrentMil(), in);
		undoMgr.handleCommitTempChange(mil);
		setVerticalComp(in);
	}

	/**
	 * Gets the first color of the gradient.
	 * @return The first color of the gradient.
	 */
	public Color getCoA() {
		return (coa);
	}

	/**
	 * Gets the second color of the gradient.
	 * @return The second color of the gradient.
	 */
	public Color getCoB() {
		return (cob);
	}

	/**
	 * Gets whether the gradient is oriented vertical, as opposed to horizontal.
	 * @return Whether the gradient is oriented vertical, as opposed to horizontal.
	 */
	public boolean getVertical() {
		return (vertical);
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
			if (in.getEtherID().equals(setGradientPaneVertical)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				setVertical(((Boolean) (in.getParameter())).booleanValue());
				undoMgr.commitUndoableOp(utag, "Vertical Gradient Change");
			}

			if (in.getEtherID().equals(setGradientPaneColors)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				Object[] myo = (Object[]) (in.getParameter());
				setColors((Color) (myo[0]), (Color) (myo[1]));
				undoMgr.commitUndoableOp(utag, "Color Change");
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
		MyP.put("NoEditControl", this);
		GradientPanePropertyEditor MyEdit = new GradientPanePropertyEditor(
				this, MyP);
		return (MyEdit);
	}

	/**
	 * Shows the properties editor for the component.
	 * @param e The event for showing the editor.
	 */
	public void showPropertiesEditor(EtherEvent e) {
		VerdantiumPropertiesEditor MyEdit = makePropertiesEditor();
		ProgramDirector.showPropertyEditor(MyEdit, getGUI(),
				"Gradient Pane Property Editor");
	}

	/**
	* Returns the data flavors that the component can load from persistent storage.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF = { new TransVersionBufferFlavor("Gradient Pane",
				"Gradient Pane") };
		return (MyF);
	}

	/**
	* Returns the data flavors that the component can save to persistent storage.
    * @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF = { new TransVersionBufferFlavor("Gradient Pane",
				"Gradient Pane") };
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
			setColors(Color.blue, Color.white);
			setVertical(true);
			onlyDesignerEdits.setOnlyDesignerEdits(false);
			macroMap.clear();
		} else {
			try {
				TransVersionBuffer MyF = (TransVersionBuffer) trans;
				VersionBuffer.chkNul(MyF);
				coa = (Color) (MyF.getProperty("ColorA"));
				VersionBuffer.chkNul(coa);
				cob = (Color) (MyF.getProperty("ColorB"));
				VersionBuffer.chkNul(cob);
				setColors(coa, cob);

				setVertical(MyF.getBoolean("Vertical"));
				onlyDesignerEdits.setOnlyDesignerEdits(MyF
						.getBoolean("OnlyDesignerEdits"));
				macroMap.readData(MyF);
			} catch (ClassCastException e) {
				throw (new DataFormatException(e));
			}
		}
	}

	/**
	* Saves the component to persistent storage.
    * @param The flavor in which to save the data.
    * @return Transferable to which the data is saved.
	*/
	public Transferable savePersistentData(DataFlavor flavor) {
		TransVersionBuffer MyF = new TransVersionBuffer("Gradient Pane",
				"Gradient Pane");

		MyF.setBoolean("OnlyDesignerEdits", isOnlyDesignerEdits());
		macroMap.writeData(MyF);
		MyF.setBoolean("Vertical", vertical);
		MyF.setProperty("ColorA", coa);
		MyF.setProperty("ColorB", cob);
		return (MyF);
	}

	/**
	 * Handles a change to the current undo state.
	 */
	protected void handleUndoStateChange() {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		Color colorA = (Color) (model.pdxm_getColorA(mil));
		Color colorB = (Color) (model.pdxm_getColorB(mil));
		boolean vertical = model.pdxm_isVertical(mil);
		setColorsComp(colorA, colorB);
		setVerticalComp(vertical);
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
		GradientPaneHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		GradientPane MyComp = new GradientPane();
		ProgramDirector.showComponent(MyComp, "Gradient Pane", argv, false);
	}
	
}

