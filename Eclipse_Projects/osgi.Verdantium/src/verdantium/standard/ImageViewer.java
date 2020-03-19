package verdantium.standard;

import java.awt.AWTEvent;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;

import jundo.runtime.ExtMilieuRef;
import meta.DataFormatException;
import meta.VersionBuffer;
import meta.WrapRuntimeException;
import verdantium.EtherEvent;
import verdantium.GenericStreamOutputTrans;
import verdantium.ProgramDirector;
import verdantium.StandardEtherEvent;
import verdantium.TransVersionBuffer;
import verdantium.TransVersionBufferFlavor;
import verdantium.UrlHolder;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumFlavorMap;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.core.BackgroundListener;
import verdantium.core.DesignerControl;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.standard.data.pdx_ImageViewerModel_pdx_ObjectRef;
import verdantium.standard.data.pdx_ImageViewerModel_pdx_PairRef;
import verdantium.standard.help.ImageViewerHelp;
import verdantium.undo.UTag;
import verdantium.undo.UndoManager;
import verdantium.utils.IllegalInputException;
import verdantium.utils.ResourceNotFoundException;
import verdantium.utils.VerdantiumDragUtils;
import verdantium.utils.VerdantiumDropUtils;
import verdantium.xapp.BackgroundState;
import verdantium.xapp.JcApplicationAdapter;

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
*    | 03/10/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for stretched and tiled images.                              | Added support for stretched and tiled images.
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Macro support.                                                       | Added code for macro support.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/29/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for multiple PLAFs.                                          | Made code changes to support multiple PLAFs.
*    | 10/05/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for command-line arguments.                                  | Added support for command-line arguments.
*    | 11/17/2001            | Thorn Green (viridian_1138@yahoo.com)           | Documentation fixes.                                                 | Documentation fixes.
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
* Simple component for loading and displaying images.
* <P>
* @author Thorn Green
*/
public class ImageViewer
	extends JcApplicationAdapter
	implements BackgroundListener {
	
	/**
	* Property change name for a change in the display mode of the component.
	*/
	public static final String ImageViewerDisplayModeChanged =
		"ImageViewerDisplayModeChanged";
	
	/**
	* EtherEvent name for setting the image viewer display mode.
	*/
	public static final String setImageViewerDisplayMode =
		"setImageViewerDisplayMode";

	/**
	* Panel in which the drawing app. is embedded.
	*/
	transient protected JPanel myPan = new JPanel();

	/**
	 * The data model for multi-level undo.
	 */
	pdx_ImageViewerModel_pdx_ObjectRef model = null;

	/**
	* Used in {@link #ImageMode} to indicate that no image is being displayed.
	*/
	protected static final int NO_MODE = 0;
	
	/**
	* Used in {@link #ImageMode} to indicate that a GIF is being displayed.
	*/
	protected static final int GIF_MODE = 1;
	
	/**
	* Used in {@link #ImageMode} to indicate that a JPEG is being displayed.
	*/
	protected static final int JPEG_MODE = 2;

	/**
	* Display mode for centered image.
	*/
	public static final int CENTER_MODE = 1;
	
	/**
	* Display mode for tiled images.
	*/
	public static final int TILE_MODE = 2;
	
	/**
	* Display mode for stretched image.
	*/
	public static final int STRETCH_MODE = 3;
	
	/**
	* Display mode for docked image.
	*/
	public static final int DOCK_MODE = 4;
	
    /**
     * The current display mode of the component.  Can have a value of either
	 * {@link #DOCK_MODE}, {@link #STRETCH_MODE}, {@link TILE_MODE} or {@link #CENTER_MODE}.
     */
	protected int displayMode = DOCK_MODE;

	/**
	 * The image to be displayed by the component.
	 */
	protected Image image = null;

	/**
	 * Image object identical to the original image, but at the color depth
	 * and settings of the host graphics context.  Utilized when drawing because
	 * it renders more efficiently.
	 */
	protected Image drawImage = null;

	
	/**
	* Gets the current display mode of the component.  Can have a value of either
	* {@link #DOCK_MODE}, {@link #STRETCH_MODE}, {@link TILE_MODE} or {@link #CENTER_MODE}.
	* @return The display mode of the component.
	*/
	public int getDisplayMode() {
		return (displayMode);
	}

	/**
	* Sets the current display mode of the component.  Can have a value of either
	* {@link #DOCK_MODE}, {@link #STRETCH_MODE}, {@link TILE_MODE} or {@link #CENTER_MODE}.
	* @param in The display mode of the component.
	*/
	protected void setDisplayModeComp(int in) {
		displayMode = in;
		getGUI().repaint();
		PropL.firePropertyChange(ImageViewerDisplayModeChanged, null, null);
	}

	/**
	 * Sets the image to be displayed by the component.
	 * @param in The image to be displayed by the component.
	 */
	protected void setImageComp(Image in) {
		image = in;
		drawImage = null;
		getGUI().repaint();
	}

	/**
	* Sets the current display mode of the component, including the associated multi-level undo state.  Can have a value of either
	* {@link #DOCK_MODE}, {@link #STRETCH_MODE}, {@link TILE_MODE} or {@link #CENTER_MODE}.
	* @param in The display mode of the component.
	*/
	protected void setDisplayMode(int in) {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		mil = model.pdxm_setDisplayMode(mil, in);
		undoMgr.handleCommitTempChange(mil);
		setDisplayModeComp(in);
	}

	/**
	 * Sets the image to be displayed by the component, including the associated multi-level undo state.
	 * @param in The image to be displayed by the component.
	 */
	protected void setImage(Image in) {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		mil = model.pdxm_setImage(mil, in);
		undoMgr.handleCommitTempChange(mil);
		setImageComp(in);
	}

	/**
	* Gets the image mode of the component.  Can have a value of either
	* {@link #NO_MODE}, {@link #GIF_MODE}, or {@link #JPEG_MODE}.
	* @return The image mode of the component.
	*/
	protected int getImageMode() {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		int ret = model.pdxm_getImageMode(mil);
		return (ret);
	}

	/**
	 * Gets the underlying byte representation of the displayed image.
	 * @return The underlying byte representation of the displayed image.
	 */
	protected byte[] getImageBytes() {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		byte[] ret = (byte[]) (model.pdxm_getImageBytes(mil));
		return (ret);
	}

	/**
	 * Sets the data state for the image to be displayed.
	 * @param imageMode The image mode of the component.
	 * @param image The image to be displayed.
	 * @param imageBytes The underlying byte representation of the displayed image.
	 */
	protected void setImageData(
		int imageMode,
		Image image,
		byte[] imageBytes) {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		mil = model.pdxm_setImageData(mil, imageMode, image, imageBytes);
		undoMgr.handleCommitTempChange(mil);
	}

	/**
	 * Sets the underlying byte representation of the displayed image.
	 * @param in The underlying byte representation of the displayed image.
	 */
	protected void setImageBytes(byte[] imageBytes) {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		mil = model.pdxm_setImageBytes(mil, imageBytes);
		undoMgr.handleCommitTempChange(mil);
	}

	/**
	 * Sets the image mode of the component.  Can have a value of either
	 * {@link #NO_MODE}, {@link #GIF_MODE}, or {@link #JPEG_MODE}.
	 * @param in The image mode of the component.
	 */
	protected void setImageMode(int imageMode) {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		mil = model.pdxm_setImageMode(mil, imageMode);
		undoMgr.handleCommitTempChange(mil);
	}

	/**
	* Gets the GUI for the component.
	* @return The GUI for the component.
	*/
	public JComponent getGUI() {
		return (myPan);
	}

	/**
	* Constructs the component.
	*/
	public ImageViewer() {
		initializeUndoMgr();
		initialize(undoMgr);
		bkgnd = new BackgroundState(undoMgr);
		DefaultBkgnd = UIManager.getColor("Panel.background");
		arrangeLayout();
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		setToolTipText("Right-Click to edit properties");
		VerdantiumDragUtils.setDragUtil(this, this);
		VerdantiumDropUtils.setDropUtil(this, this, this);
		configureForEtherEvents();
		undoMgr.addPropertyChangeListener(this);
		bkgnd.setBackgroundState(DefaultBkgnd, true);
	}

	/**
	 * Initializes the undo manager of the component.
	 */
	protected void initializeUndoMgr() {
		ExtMilieuRef mil = jundo.runtime.Runtime.getInitialMilieu();
		pdx_ImageViewerModel_pdx_PairRef ref =
			pdx_ImageViewerModel_pdx_ObjectRef.pdxm_new_ImageViewerModel(
				mil,
				DOCK_MODE,
				NO_MODE,
				null,
				null);
		model = (pdx_ImageViewerModel_pdx_ObjectRef) (ref.getObject());
		undoMgr = UndoManager.createInstanceUndoManager(ref.getMilieu());
	}

	/**
	* Sets up the layout hierarchy for the component.
	*/
	protected void arrangeLayout() {
		myPan.setLayout(new BorderLayout(0, 0));
		myPan.add("Center", this);
		myPan.setMinimumSize(new Dimension(2, 2));
		myPan.setPreferredSize(new Dimension(200, 200));
		myPan.setOpaque(false);
	}

	@Override
	public void paint(Graphics g) {
		Rectangle r = null;

		if (bkgnd.isOpaqueFlag()) {
			r = getBounds();
			g.setColor(getBackground());
			g.fillRect(/* r.x , r.y */
			0, 0, r.width, r.height);
		}

		if (image != null) {

			if (drawImage == null) {
				int wid = image.getWidth(this);
				int hei = image.getHeight(this);
				if ((wid == -1) || (hei == -1)) {
					repaint();
					return;
				}

				drawImage = createImage(wid, hei);
				Graphics2D gr = (Graphics2D) (drawImage.getGraphics());
				gr.setComposite(AlphaComposite.Src);
				gr.drawImage(image, 0, 0, wid, hei, this);
				gr.dispose();
			}

			switch (displayMode) {
				case DOCK_MODE :
					g.drawImage(drawImage, 0, 0, this);
					break;

				case CENTER_MODE :
					{
						if (r == null)
							r = getBounds();
						int wid = image.getWidth(this);
						if (wid == -1)
							repaint();
						int hei = image.getHeight(this);
						if (hei == -1)
							repaint();
						int offx = (r.width - wid) / 2;
						int offy = (r.height - hei) / 2;
						g.drawImage(drawImage, offx, offy, this);
					}
					break;

				case TILE_MODE :
					if (r == null)
						r = getBounds();
					if (r == null)
						r = getBounds();
					int wid = image.getWidth(this);
					if (wid == -1)
						repaint();
					int hei = image.getHeight(this);
					if (hei == -1)
						repaint();
					if ((wid > 0) && (hei > 0)) {
						int maxx = r.width / wid + 1;
						int maxy = r.height / hei + 1;
						int x;
						int y;
						for (y = 0; y < maxy; y++) {
							for (x = 0; x < maxx; x++) {
								g.drawImage(drawImage, x * wid, y * hei, this);
							}
						}
					}
					break;

				case STRETCH_MODE :
					{
						if (r == null)
							r = getBounds();
						g.drawImage(drawImage, 0, 0, r.width, r.height, this);
					}
					break;
			}
		}
	}

	@Override
	public void update(Graphics g) {
		paint(g);
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

			if (in.getEtherID().equals(setImageViewerDisplayMode)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				setDisplayMode(((Integer) (in.getParameter())).intValue());
				undoMgr.commitUndoableOp(utag, "Display Mode Change");
			}

		}

		if (ret == EtherEvent.EVENT_NOT_HANDLED)
			ret = null;

		return (ret);
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
		MyP.put("NoEditControl", this);
		if (isScrolling())
			MyP.put("Scrolling", this);
		ImageViewerPropertyEditor MyEdit =
			new ImageViewerPropertyEditor(this, MyP);
		return (MyEdit);
	}

	/**
	 * Shows the properties editor for the component.
	 * @param e The event for showing the editor.
	 */
	public void showPropertiesEditor(EtherEvent e) {
		VerdantiumPropertiesEditor MyEdit = makePropertiesEditor();
		ProgramDirector.showPropertyEditor(
			MyEdit,
			getGUI(),
			"Image Viewer Property Editor");
	}

	/**
	* Handles a mouse event on the component by showing the component's property editor
	* if the component is in a mode that allows the property editor's display.
	* @param e The input event.
	*/
	public void processMouseEvent(MouseEvent e) {
		switch (e.getID()) {
			case MouseEvent.MOUSE_PRESSED :
				try {
					if (((DesignerControl.isDesignTime())
						|| (!isOnlyDesignerEdits()))
						&& !(e.isAltDown())) {
						EtherEvent send =
							new StandardEtherEvent(
								this,
								StandardEtherEvent.showPropertiesEditor,
								null,
								this);
						ProgramDirector.fireEtherEvent(send, null);
					}
				} catch (Throwable ex) {
					handleThrow(ex);
				}
				break;

			default :
				break;

		}

		super.processMouseEvent(e);
	}

	/**
	* Sets the background color of the component, and whether it is transparent.
	* Note: transparency is ignored because there is no attempt to put transparency in the displayed image.
	* @param inC The color to which to set the background.
	* @param opaque Whether to make the background opaque.
	*/
	public void handleBackgroundState(Color inC, boolean opaque) {
		setBackground(inC);
	}

	/**
	* Gets whether the component is opaque for Swing.
	* @return Whether the component is opaque.
	*/
	public boolean isOpaque() {
		return (bkgnd.isOpaqueFlag());
	}

	/**
	 * Handles a change to the global multi-level undo state.
	 */
	protected void handleUndoStateChange() {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		int mode = model.pdxm_getDisplayMode(mil);
		setDisplayModeComp(mode);
		Image img = (Image) (model.pdxm_getImage(mil));
		setImageComp(img);
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
	* Returns the data flavors that the component can load from persistent storage.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{
				new TransVersionBufferFlavor("Image Viewer", "Image Viewer"),
				VerdantiumFlavorMap.createInputStreamFlavor("image", "gif"),
				VerdantiumFlavorMap.createInputStreamFlavor("image", "jpeg")};
		return (MyF);
	}

	/**
	* Returns the data flavors that the component can save to persistent storage.
    * @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF = null;

		switch (getImageMode()) {
			case NO_MODE :
				{
					DataFlavor[] fl =
						{
							 new TransVersionBufferFlavor(
								"Image Viewer",
								"Image Viewer")};
					MyF = fl;
				}
				break;

			case GIF_MODE :
				{
					DataFlavor[] fl =
						{
							new TransVersionBufferFlavor(
								"Image Viewer",
								"Image Viewer"),
							VerdantiumFlavorMap.createOutputStreamFlavor(
								"image",
								"gif")};
					MyF = fl;
				}
				break;

			case JPEG_MODE :
				{
					DataFlavor[] fl =
						{
							new TransVersionBufferFlavor(
								"Image Viewer",
								"Image Viewer"),
							VerdantiumFlavorMap.createOutputStreamFlavor(
								"image",
								"jpeg")};
					MyF = fl;
				}
				break;
		}

		return (MyF);
	}

	/**
	* Loads data for the component from persistent storage.
    * @param flavor The flavor in which to load the data.
    * @param trans The transferable from which to load the data.
	*/
	public void loadPersistentData(DataFlavor flavor, Transferable trans)
		throws IOException, ResourceNotFoundException {
		if (trans instanceof UrlHolder) {
			fileSaveURL = ((UrlHolder) trans).getUrl();
			fileSaveFlavor = flavor;
		}

		if (trans == null) {
			setImageData(NO_MODE, null, null);
			bkgnd.setBackgroundState(DefaultBkgnd, false);
			try {
				borderObject.setBorderObject(null, null, null);
			} catch (Exception ex) {
				throw (new WrapRuntimeException("Default Border Failed", ex));
			}
			onlyDesignerEdits.setOnlyDesignerEdits(false);
			macroMap.clear();
			docPageFormat.setDocPageFormat(null);
		}

		if (flavor instanceof TransVersionBufferFlavor) {
			try {
				TransVersionBuffer MyF = (TransVersionBuffer) trans;
				VersionBuffer.chkNul(MyF);
				Object myo = MyF.getProperty("ImageBytes");
				if (myo != null) {
					setImageBytes((byte[]) myo);
					VersionBuffer.chkNul(getImageBytes());
					setImage(
						(Toolkit.getDefaultToolkit()).createImage(
							getImageBytes()));
				}

				setImageMode(MyF.getInt("ImageMode"));

				if (MyF.getProperty("DisplayMode") != null)
					setDisplayMode(MyF.getInt("DisplayMode"));

				bkgnd.readData(MyF);

				borderObject.readData(MyF);

				Dimension dim = (Dimension) (MyF.getProperty("PageSize"));
				VersionBuffer.chkNul(dim);
				alterPageSize(dim);

				macroMap.readData(MyF);

				onlyDesignerEdits.setOnlyDesignerEdits(
					MyF.getBoolean("OnlyDesignerEdits"));
			} catch (ResourceNotFoundException ex) {
				throw (ex);
			} catch (ClassCastException ex) {
				throw (new DataFormatException(ex));
			} catch (IllegalInputException ex) {
				throw (new DataFormatException(ex));
			}
		}

		if (flavor
			.getMimeType()
			.equals("image/gif; class=java.io.InputStream")) {
			loadStreamPersistentData(flavor, trans);
			if (image != null)
				setImageMode(GIF_MODE);
		}

		if (flavor
			.getMimeType()
			.equals("image/jpeg; class=java.io.InputStream")) {
			loadStreamPersistentData(flavor, trans);
			if (image != null)
				setImageMode(JPEG_MODE);
		}

	}

	/**
	* Loads persistent data in either GIF or JPEG format.
	* @param flavor The flavor in which to load the data.
    * @param trans The transferable from which to load the data.
	*/
	private void loadStreamPersistentData(
		DataFlavor flavor,
		Transferable trans)
		throws IOException {
		byte buffer[] = new byte[1024];

		try {
			InputStream InStream =
				((InputStream) trans.getTransferData(flavor));

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			while (true) {
				int nRead = InStream.read(buffer, 0, buffer.length);
				if (nRead <= 0)
					break;
				baos.write(buffer, 0, nRead);
			}

			setImage(
				(Toolkit.getDefaultToolkit()).createImage(baos.toByteArray()));
			if (image != null) {
				setImageBytes(baos.toByteArray());
				/* int wid = image.getWidth( this );
				int hei = image.getHeight( this );
				MediaTracker mt = new MediaTracker( this );
				mt.addImage( image , 1 );
				mt.waitForID( 1 );
				if( ( wid != -1 ) && ( hei != -1 ) )
					alterPageSize( new Dimension( wid , hei ) );
					else System.out.println( "Image Not Synchronized!" ); 
						Save This Code for Future Expansion */
			}
		} catch (IOException ex) {
			throw (ex);
		} catch (UnsupportedFlavorException ex) {
			throw (
				new WrapRuntimeException(
					"Something Inconsistent in Flavor Handling",
					ex));
		}

	}

	/**
	* Saves the component to persistent storage.
    * @param The flavor in which to save the data.
    * @return Transferable to which the data is saved.
	*/
	public Transferable savePersistentData(DataFlavor flavor) {
		if (flavor instanceof TransVersionBufferFlavor) {
			TransVersionBuffer MyF =
				new TransVersionBuffer("Image Viewer", "Image Viewer");
			if (getImageBytes() != null)
				MyF.setProperty("ImageBytes", getImageBytes());
			MyF.setInt("ImageMode", getImageMode());
			MyF.setInt("DisplayMode", displayMode);
			bkgnd.writeData(MyF);
			MyF.setBoolean("OnlyDesignerEdits", isOnlyDesignerEdits());
			macroMap.writeData(MyF);
			MyF.setProperty(
				"PageSize",
				new Dimension(
					myPan.getBounds().width,
					myPan.getBounds().height));

			borderObject.writeData(MyF);

			return (MyF);
		} else {
			return (saveStreamPersistentData(flavor));
		}
	}

	/**
	* Saves the component's persistent data in either GIF or JPEG format.
	* @param The flavor in which to save the data.
    * @return Transferable to which the data is saved.
	*/
	protected Transferable saveStreamPersistentData(DataFlavor flavor) {
		DataFlavor[] flavors = { flavor };
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		stream.write(getImageBytes(), 0, getImageBytes().length);
		GenericStreamOutputTrans trans =
			new GenericStreamOutputTrans(flavors, stream);
		return (trans);
	}

	/**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		ImageViewerHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		ImageViewer MyComp = new ImageViewer();
		ProgramDirector.showComponent(MyComp, "Image Viewer", argv, false);
	}
	
	
}


