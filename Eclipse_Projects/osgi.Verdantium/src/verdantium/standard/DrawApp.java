package verdantium.standard;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Externalizable;
import java.io.Serializable;
import java.util.HashMap;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import meta.WrapRuntimeException;
import verdantium.ProgramDirectorEvent;
import verdantium.core.BackgroundListener;
import verdantium.core.ContainerApp;
import verdantium.core.ContainerAppInternalFrame;
import verdantium.core.ContainerFindIterator;
import verdantium.core.EditorControl;
import verdantium.standard.help.DrawAppHelp;
import verdantium.utils.ComponentNotFoundException;
import verdantium.utils.IllegalInputException;
import verdantium.utils.ResourceNotFoundException;
import verdantium.xapp.BackgroundState;
import verdantium.xapp.BorderObject;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
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
import verdantium.core.DefaultPropertyEditor;
import verdantium.core.DesignerControl;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.undo.UTag;
import verdantium.undo.UndoManager;
import verdantium.utils.VerdantiumDragUtils;
import verdantium.utils.VerdantiumDropUtils;
import verdantium.xapp.DocPageFormat;
import verdantium.xapp.JcApplicationAdapter;
import verdantium.xapp.MacroTreeMap;
import verdantium.xapp.OnlyDesignerEdits;

import umeta.*;


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
 *    | 01/28/2001            | Thorn Green (viridian_1138@yahoo.com)           | Multiple bugs in calling of handleDestroy()                          | Implemented a set of bug-fixes.
 *    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Improve DrawApp user interface.                                      | Made multiple changes to improve interface.
 *    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
 *    | 09/29/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for multiple PLAFs.                                          | Made code changes to support multiple PLAFs.
 *    | 10/05/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for command-line arguments.                                  | Added support for command-line arguments.
 *    | 12/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Third-Cut at Error Handling.                                         | Third-Cut at Error Handling.
 *    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
 *    | 04/21/2002            | Thorn Green (viridian_1138@yahoo.com)           | Find/Replace support.                                                | Added find/replace support.
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
 * DrawApp is a simple drawing application that supports embedding.
 * Without such a demonstration component it would be difficult to verify that Verdantium works
 * as advertised.  It also shows how the current implementation of components
 * can be embedded in a variety of other types of software.
 * And it provides a practical working source code example to help individuals
 * to implement other components as needed.
 * <P>
 * @author Thorn Green
 */
public class DrawApp
        extends JcApplicationAdapter
        implements BackgroundListener {
	
    /**
     * Panel in which the drawing app. is embedded.
     */
    transient protected JPanel myPan = new JPanel();
    
    /**
     * The desktop pane taht holds the embedded components for the drawing app.
     */
    transient protected DrawAppDesktopPane myDesk = null;
    
    /**
     * Property change name for a change in the mode of the component..
     */
    public static String DRAW_APP_MODE = "DRAW_APP_MODE";
    
    /**
     * Property change name for a change in the line and fill colors.
     */
    public static String DRAW_APP_COLORS = "DRAW_APP_COLORS";
    
    /**
     * EtherEvent name for setting the drawing app. mode.
     */
    public static String setDrawAppMode = "setDrawAppMode";
    
    /**
     * EtherEvent name for setting the line and fill colors.
     */
    public static String setDrawAppColors = "setDrawAppColors";
    
    /**
     * EtherEvent name for setting the stroke to a basic stroke.
     */
    public static String setBasicStroke = "setBasicStroke";
    
    /**
     * EtherEvent name for setting the stroke for drawing shapes.
     */
    public static String setStroke = "setStroke";
    
    /**
     * EtherEvent name for setting the parameters for curve drawing.
     */
    public static String setCurveParam = "setCurveParam";
    
    /**
     * EtherEvent name for erasing all primitives from the drawing, but
     * leaving embedded components.
     */
    public static String eraseAllPrimitives = "eraseAllPrimitives";
    
    /**
     * Mode for editing shapes.
     */
    public static final int EDIT_MODE = 1;
    
    /**
     * Mode for inserting text fields.
     */
    public static final int TEXT_MODE = 2;
    
    /**
     * Mode for inserting lines.
     */
    public static final int LINE_MODE = 3;
    
    /**
     * Mode for inserting rectangles.
     */
    public static final int RECT_MODE = 4;
    
    /**
     * Mode for inserting filled rectangles.
     */
    public static final int FILLED_RECT_MODE = 5;
    
    /**
     * Mode for inserting round rectangles.
     */
    public static final int ROUND_RECT_MODE = 6;
    
    /**
     * Mode for inserting filled round rectangles.
     */
    public static final int FILLED_ROUND_RECT_MODE = 7;
    
    /**
     * Mode for inserting ovals.
     */
    public static final int OVAL_MODE = 8;
    
    /**
     * Mode for inserting filled ovals.
     */
    public static final int FILLED_OVAL_MODE = 9;
    
    /**
     * Mode for inserting polygons.
     */
    public static final int POLYGON_MODE = 10;
    
    /**
     * Mode for inserting filled polygons.
     */
    public static final int FILLED_POLYGON_MODE = 11;
    
    /**
     * Mode for inserting curves.
     */
    public static final int CURVE_MODE = 12;
    
    /**
     * Mode for inserting filled curves.
     */
    public static final int FILLED_CURVE_MODE = 13;
    
    /**
     * Mode for erasing individual primitives.
     */
    public static final int ErASE_MODE = 14;
    
    /**
     * Mode for bringing primitives to the front.
     */
    public static final int TO_FRONT_MODE = 15;
    
    /**
     * Mode for bringing primitives to the back.
     */
    public static final int TO_BACK_MODE = 16;
    
    /**
     * Flag indicating whether the user has moused-down.
     */
    transient protected boolean mousedDown = false;
    
    /**
     * Flag indicating if the current rendering state is opaque.
     */
    transient protected boolean opaqueFlag = false;
    
    /**
     * Indicates whether a shape is being inserted by rubber-banding.
     */
    transient protected boolean createdEditor = false;
    
    /**
     * Object being inserted by rubber-banding, if there is one.
     */
    transient protected DrawingObject rubberBandingObject = null;
    
    /**
     * The drawing object for dragging.
     */
    transient protected pdx_DrawingObject_pdx_ObjectRef dragDrw = null;
    
    /**
     * The drawing object for dragging.
     */
    transient protected DrawingObject dragDrwDown = null;
    
    /**
     * The click rec for dragging.
     */
    transient protected ClickRec dragRec = null;
    
    /**
     * Indicates whether the last click was a match.
     */
    transient protected boolean lastClickMatch = false;
    
    /**
     * The multi-level undo manager of the component.
     */
    transient protected pdx_DrawAppModel_pdx_ObjectRef model = null;
    
    
    /**
     * Gets the current editing mode of the component.
     * @return The current editing mode of the component.
     */
    public int getEditMode() {
        ExtMilieuRef mil = undoMgr.getCurrentMil();
        return( model.pdxm_getCurrentMode( mil ) );
    }
    
    /**
     * Sets the current editing mode of the component, including the associated multi-level undo state.
     * @param in The current editing mode of the component.
     */
    protected void setEditMode(int in) {
        ExtMilieuRef mil =
                model.pdxm_setCurrentMode(undoMgr.getCurrentMil(), in);
        undoMgr.handleCommitTempChange(mil);
        setEditModeComp(in);
    }
    
    /**
     * Sets the current editing mode of the component.
     * @param in The current editing mode of the component.
     */
    protected void setEditModeComp(int in) {
        dragDrw = null;
        dragDrwDown = null;
        dragRec = null;
        lastClickMatch = false;
        if (in == TEXT_MODE)
            setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        else
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        repaint();
        PropL.firePropertyChange(
                DRAW_APP_MODE,
                null,
                new Integer(in));
    }
    
    /**
     * Gets the line color of the component.
     * @return The line color of the component.
     */
    public Color getLineColor() {
        ExtMilieuRef mil = undoMgr.getCurrentMil();
        return( (Color)( model.pdxm_getLineColor( mil ) ) );
    }
    /**
     * Gets the fill color of the component.
     * @return The fill color of the component.
     */
    public Color getFillColor() {
        ExtMilieuRef mil = undoMgr.getCurrentMil();
        return( (Color)( model.pdxm_getFillColor( mil ) ) );
    }
    
    /**
     * Sets the line and fill colors of the component, including the associated multi-level undo state.
     * @param fg The line color of the component.
     * @param bk The fill color of the component.
     */
    protected void setColors(Color fg, Color bk) {
        ExtMilieuRef mil =
                model.pdxm_setLineColor(undoMgr.getCurrentMil(), fg);
        mil = model.pdxm_setFillColor(mil, bk);
        undoMgr.handleCommitTempChange(mil);
        setColorsComp(fg, bk);
    }
    
    /**
     * Sets the line and fill colors of the component.
     * @param fg The line color of the component.
     * @param bk The fill color of the component.
     */
    protected void setColorsComp(Color fg, Color bk) {
        PropL.firePropertyChange(DRAW_APP_COLORS, null, null);
    }
    
    /**
     * Gets the Bezier mode of the component.
     * @return The Bezier mode of the component.
     */
    public int getBezMode() {
        ExtMilieuRef mil = undoMgr.getCurrentMil();
        return( model.pdxm_getBezMode( mil ) );
    }
    
    /**
     * Gets whether the component is set for chord-length parameterization.
     * @return Whether the component is set for chord-length parameterization.
     */
    public boolean getChordParam() {
        ExtMilieuRef mil = undoMgr.getCurrentMil();
        return( model.pdxm_getChordParam( mil ) );
    }
    
    /**
	* Gets whether the component is opaque for Swing.
	* @return Whether the component is opaque.
	*/
    public boolean isOpaque() {
        return (opaqueFlag);
    }
    
    /**
     * Sets the background color of the component, and whether the component is opaque.
     * @param inC The background color to set.
     * @param opaque Whether the component is to be opaque.
     */
    public void handleBackgroundState(Color inC, boolean opaque) {
        setBackground(inC);
        opaqueFlag = opaque;
        repaint();
    }
    
    @Override
    public void paint(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        
        if (opaqueFlag) {
            Rectangle r = getBounds();
            g.setColor(getBackground());
            g.fillRect(r.x, r.y, r.width, r.height);
        }
        
        paintDrawApp(g);
    }
    
    /**
     * Paints the drawing component's shapes.
     * @param g The graphics context into which to render the shapes.
     */
    protected void paintDrawApp(Graphics2D g) {
        int currentMode = getEditMode();
        
        jundo.runtime.ExtMilieuRef mil = undoMgr.getCurrentMil();
        pdx_HighLevelList_pdx_ObjectRef drawingList =
                model.pdxm_getDrawingList( mil );
        if (!(drawingList.pdxm_empty( mil ))) {
            pdx_LowLevelList_pdx_ObjectRef lowl =
                    drawingList.pdxm_exportNode(mil);
            lowl = lowl.pdxm_searchHead( mil );
            boolean Done = false;
            
            while (!Done) {
                pdx_DrawingObject_pdx_ObjectRef myo = (pdx_DrawingObject_pdx_ObjectRef) (lowl.pdxm_getNode(mil));
                if( myo != dragDrw ) {
                    myo.pdxm_draw(mil, this, g, currentMode);
                    if ( (currentMode == EDIT_MODE) || (currentMode == ErASE_MODE) ||
                            (currentMode == TO_FRONT_MODE) || (currentMode == TO_BACK_MODE) )
                        myo.pdxm_drawTools(mil, this, g, currentMode);
                } else {
                    dragDrwDown.draw(this, g, currentMode);
                    if ((currentMode == EDIT_MODE)||(currentMode==ErASE_MODE)||
                            (currentMode==TO_FRONT_MODE) || (currentMode==TO_BACK_MODE) )
                        dragDrwDown.drawTools(this, g, currentMode);
                }
                
                lowl = lowl.pdxm_right( mil );
                Done = lowl.pdxm_getHead( mil );
            }
        }
        
        if (rubberBandingObject != null)
            rubberBandingObject.draw(this, g, currentMode);
        
    }
    
    @Override
    public void update(Graphics g) {
        paint(g);
    }
    
    /**
     * Gets the GUI of the component.
     * @return The GUI of the component.
     */
    public JComponent getGUI() {
        return (myPan);
    }
    
    /**
     * Handles property change events.
     * @param e The input event.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        if (evt.getPropertyName() == EditorControl.EditCntlChange) {
            int count;
            JInternalFrame[] allFrames = myDesk.getAllFrames();
            
            for (count = 0; count < allFrames.length; count++) {
                allFrames[count].updateUI();
                allFrames[count].repaint();
            }
        }
        if (evt.getPropertyName() == UndoManager.MODEL_STATE_CHANGED) {
            handleUndoStateChange();
        }
        
    }
    
    /**
	 * Handles a change to the current undo state.
	 */
    protected void handleUndoStateChange() {
        ExtMilieuRef mil = undoMgr.getCurrentMil();
        int currentMode = model.pdxm_getCurrentMode( mil );
        setEditModeComp( currentMode );
        Color lineColor = (Color)( model.pdxm_getLineColor(mil) );
        Color fillColor = (Color)( model.pdxm_getFillColor(mil) );
        setColorsComp( lineColor , fillColor );
        repaint();
    }
    
    /**
     * Handles the destruction of the component by removing it as a property change listener,
     * and then firing a property change event indicating the destruction of the component.
     */
    public void handleDestroy() {
        EditorControl.removePropertyChangeListener(this);
        myDesk.handleDestroy();
        super.handleDestroy();
    }
    
    /**
     * Constructs the component.
     */
    public DrawApp() {
        initializeUndoMgr();
        macroMap = new MacroTreeMap(undoMgr);
        docPageFormat = new DocPageFormat(undoMgr);
        onlyDesignerEdits = new OnlyDesignerEdits(undoMgr);
        bkgnd = new BackgroundState(undoMgr);
        borderObject = new BorderObject(undoMgr);
        undoMgr.addPropertyChangeListener(this);
        PropL = new PropertyChangeSupport(this);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
        enableEvents(AWTEvent.KEY_EVENT_MASK);
        setDoubleBuffered(true);
        setOpaque(false);
        setMinimumSize(new Dimension(2, 2));
        setPreferredSize(new Dimension(300, 300));
        setDoubleBuffered(false);
        VerdantiumDragUtils.setDragUtil(this, this);
        VerdantiumDropUtils.setDropUtil(this, this, this);
        configureForEtherEvents();
        bkgnd.setBackgroundState(Color.white, true);
        /* MyTime.start(); */
        myDesk = createDesktopPane();
        arrangeLayout();
        EditorControl.addPropertyChangeListener(this);
    }
    
    /**
	 * Initializes the undo manager of the component.
	 */
    protected void initializeUndoMgr() {
        pdx_DrawAppModel_pdx_PairRef ref = pdx_DrawAppModel_pdx_ObjectRef
                .pdxm_new_DrawAppModel(jundo.runtime.Runtime
                .getInitialMilieu(), LINE_MODE, Color.blue, Color.green,
                2.0, null, 2, true);
        model = (pdx_DrawAppModel_pdx_ObjectRef) (ref.getObject());
        undoMgr = UndoManager.createInstanceUndoManager(ref.getMilieu());
    }
    
    /**
     * Creates the desktop pane for the component.
     * @return The desktop pane for the component.
     */
    protected DrawAppDesktopPane createDesktopPane() {
        return (
                new DrawAppDesktopPane(
                undoMgr,
                this,
                this));
    }
    
    /**
     * Arranges the layout of the component.
     */
    protected void arrangeLayout() {
        myPan.setLayout(new BorderLayout(0, 0));
        myPan.add("Center", myDesk);
        myPan.setMinimumSize(new Dimension(2, 2));
        myPan.setPreferredSize(new Dimension(50, 50));
        myPan.setOpaque(false);
    }
    
    /**
     * Creates a rectangle enclosing the size of a knob at (x,y).
     * @param x The X-axis coordinate of the knob.
     * @param y The Y-axis coordinate of the knob.
     * @return The enclosing rectangle.
     */
    public Rectangle2D.Double instanceRect(double x, double y) {
        Rectangle2D.Double TRect = new Rectangle2D.Double(x - 3, y - 3, 6, 6);
        return (TRect);
    }
    
    /**
     * Returns the default gravity field of a mouse-point <code>p</code> for a knob
     * at (x,y).
     * @param p The location of the mouse.
     * @param x The X-axis coordinate of the knob.
     * @param y The Y-axis coordinate of the knob.
     * @return The gravity field value.
     */
    public double defaultGravityField(Point2D.Double p, double x, double y) {
        double delx = Math.abs(p.getX() - x);
        double dely = Math.abs(p.getY() - y);
        double dist = delx + dely;
        if ((delx <= 3) && (dely <= 3))
            dist = ClickRec.MAX_PRIORITY;
        if (dist > 6.0)
            dist = ClickRec.MIN_PRIORITY + 1;
        return (dist);
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
            if (in
                    .getEtherID()
                    .equals(
                    PropertyEditEtherEvent.isFindReplaceIteratorSupported)) {
                return (new Boolean(true));
            }
            
            if (in
                    .getEtherID()
                    .equals(PropertyEditEtherEvent.createFindReplaceIterator)) {
                Object[] param = (Object[]) (in.getParameter());
                return (new ContainerFindIterator(param, this, myDesk));
            }
            
            if (in.getEtherID().equals(setDrawAppColors)) {
                Object[] myo = (Object[]) (in.getParameter());
                UTag utag = new UTag();
                undoMgr.prepareForTempCommit(utag);
                setColors((Color) (myo[0]), (Color) (myo[1]));
                undoMgr.commitUndoableOp(utag, "Set Colors");
            }
            
            if (in.getEtherID().equals(setDrawAppMode)) {
                UTag utag = new UTag();
                undoMgr.prepareForTempCommit(utag);
                setEditMode(((Integer) (in.getParameter())).intValue());
                undoMgr.commitUndoableOp(utag, "Set Current Mode");
            }
            
            if (in.getEtherID().equals(setStroke)) {
                UTag utag = new UTag();
                undoMgr.prepareForTempCommit(utag);
                setStroke((Stroke) (in.getParameter()));
                undoMgr.commitUndoableOp(utag, "Set Stroke");
            }
            
            if (in.getEtherID().equals(setBasicStroke)) {
                UTag utag = new UTag();
                undoMgr.prepareForTempCommit(utag);
                setBasicStroke(((Double) (in.getParameter())).doubleValue());
                undoMgr.commitUndoableOp(utag, "Set Basic Stroke");
            }
            
            if (in.getEtherID().equals(setCurveParam)) {
                Object[] myo = (Object[]) (in.getParameter());
                setCurveParam(
                        ((Integer) (myo[0])).intValue(),
                        ((Boolean) (myo[1])).booleanValue());
            }
            
            if (in.getEtherID().equals(eraseAllPrimitives)) {
                UTag utag = new UTag();
                undoMgr.prepareForTempCommit(utag);
                ExtMilieuRef mil = undoMgr.getCurrentMil();
                pdx_HighLevelList_pdx_ObjectRef list = model.pdxm_getDrawingList( mil );
                mil = list.pdxm_eraseAllInfo( mil );
                undoMgr.handleCommitTempChange( mil );
                undoMgr.commitUndoableOp(utag, "Erase All Primitives");
                repaint();
            }
            
        }
        
        if (in instanceof ProgramDirectorEvent) {
            if (in
                    .getEtherID()
                    .equals(ProgramDirectorEvent.isProgramDirectorEventSupported)) {
                return (new Boolean(true));
            } else {
                return (handleProgramDirectorEvent((ProgramDirectorEvent) in));
            }
            
        }
        
        return (null);
    }
    
    /**
	 * Handles a change to whether only the designer can edit the component.
	 */
    public void handleOnlyDesignerEditsChange() {
        int count;
        JInternalFrame[] allFrames = myDesk.getAllFrames();
        
        for (count = 0; count < allFrames.length; count++) {
            allFrames[count].updateUI();
            allFrames[count].repaint();
        }
    }
    
    /**
     * Creates a property editor for the component.
     * @return The created property editor.
     */
    public VerdantiumPropertiesEditor makePropertiesEditor() {
        Properties myP = new Properties();
        if (isScrolling())
            myP.put("Scrolling", this);
        DrawAppPropertyEditor myEdit = new DrawAppPropertyEditor(this, myP);
        myEdit.setClickPoint(new Point(10, 10));
        return (myEdit);
    }
    
    /**
     * Shows the property editor for the component.
     * @param e The input event for showing the property editor.
     */
    public void showPropertiesEditor(EtherEvent e) {
        VerdantiumPropertiesEditor myEdit = makePropertiesEditor();
        ((DefaultPropertyEditor) myEdit).setClickPoint(
                (Point) (e.getParameter()));
        ProgramDirector.showPropertyEditor(
                myEdit,
                getGUI(),
                "Draw App Property Editor");
    }
    
    /**
     * Handles mouse events on the drawing pane of the component.
     * @param e The input event.
     */
    public void processMouseEvent(MouseEvent e) {
        switch (e.getID()) {
            case MouseEvent.MOUSE_PRESSED :
                if (((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
                && (rubberBandingObject == null))
                    handlePopupTrigger(e);
                else
                    mouserDown(e, e.getX(), e.getY());
                break;
                
            case MouseEvent.MOUSE_RELEASED :
                mouserUp(e, e.getX(), e.getY());
                break;
                
            case MouseEvent.MOUSE_EXITED :
            {
                if (mousedDown)
                    mouserUp(e, e.getX(), e.getY());
                createdEditor = false;
            }
            break;
            
            case MouseEvent.MOUSE_CLICKED :
                mouserClick(e, e.getX(), e.getY());
                break;
                
        }
        
        super.processMouseEvent(e);
    }
    
    /**
     * Handles mouse motion events on the drawing pane of the component.
     * @param e The input event.
     */
    public void processMouseMotionEvent(MouseEvent e) {
        switch (e.getID()) {
            case MouseEvent.MOUSE_DRAGGED :
                mouserDrag(e, e.getX(), e.getY());
                break;
                
            case MouseEvent.MOUSE_MOVED :
                mouserMove(e, e.getX(), e.getY());
                break;
                
        }
        
        super.processMouseMotionEvent(e);
    }
    
    /**
     * Handles key events on the drawing pane of the component.
     * @param e The input event.
     */
    public void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
    }
    
    /**
     * Handles mouse-down events.
     * @param e The input event.
     * @param x The X-Axis coordinate of the input event.
     * @param y The Y-Axis coordinate of the input event.
     */
    public void mouserDown(MouseEvent e, int x, int y) {
        int currentMode = getEditMode();
        if ((DesignerControl.isDesignTime()) || (!isOnlyDesignerEdits())) {
            if (currentMode == EDIT_MODE) {
                boolean done = false;
                double priority = ClickRec.MIN_PRIORITY + 1;
                Point2D.Double inPt = new Point2D.Double(x, y);
                
                jundo.runtime.ExtMilieuRef mil = undoMgr.getCurrentMil();
                
                pdx_HighLevelList_pdx_ObjectRef drawingList =
                        model.pdxm_getDrawingList(mil);
                
                if (!(drawingList.pdxm_empty(mil))) {
                    pdx_LowLevelList_pdx_ObjectRef lowl =
                            drawingList.pdxm_exportNode(mil);
                    lowl = lowl.pdxm_searchHead( mil );
                    lowl = lowl.pdxm_left( mil );
                    dragRec = null;
                    dragDrw = null;
                    dragDrwDown = null;
                    
                    while (!done) {
                        done = lowl.pdxm_getHead( mil );
                        
                        pdx_DrawingObject_pdx_ObjectRef myDrw =
                                (pdx_DrawingObject_pdx_ObjectRef) lowl.pdxm_getNode( mil );
                        
                        ClickRec mRec =
                                (ClickRec)( myDrw.pdxm_clickedInRegion(mil, this, currentMode, inPt) );
                        
                        if (mRec != null) {
                            if (mRec.clickPriority < priority) {
                                dragRec = mRec;
                                dragDrw = myDrw;
                                dragDrwDown = (DrawingObject)( myDrw.pdxm_downBuild( mil ) );
                                priority = mRec.clickPriority;
                            }
                        }
                        
                        lowl = lowl.pdxm_left( mil );
                    }
                    
                    if ((dragRec != null) && (!lastClickMatch)) {
                        lastClickMatch = true;
                    } else
                        lastClickMatch = false;
                    
                }
                
            }
            
            if ((currentMode > TEXT_MODE)
            && (currentMode < POLYGON_MODE)
            && !createdEditor) {
                DrawingRectangularObject myo = new DrawingRectangularObject();
                rubberBandingObject = myo;
                setDrawingRectangularObjectStroke(myo);
                myo.setRectangle(new Rectangle(x, y, 0, 0));
                myo.setLineColor(getLineColor());
                myo.setFillColor(getFillColor());
                myo.setRenderingMode(currentMode);
                mousedDown = true;
                repaint();
            }
            
            if ((currentMode == TEXT_MODE) && !createdEditor) {
                try {
                    EtherEvent send =
                            new ProgramDirectorEvent(
                            this,
                            ProgramDirectorEvent.createApp,
                            null,
                            this);
                    Object[] param = { "Text App", new Point(x, y)};
                    send.setParameter(param);
                    ProgramDirector.fireEtherEvent(send, null);
                } catch (Throwable ex) {
                    handleThrow(ex);
                }
            }
            
            if ( (currentMode >= POLYGON_MODE) &&
                    ( currentMode < ErASE_MODE ) &&
                    !createdEditor) {
                DrawingPolygonalObject myo =
                        (DrawingPolygonalObject) rubberBandingObject;
                
                if (((e.getModifiers() & InputEvent.BUTTON3_MASK) == 0)
                || (myo == null)) {
                    if (myo == null) {
                        myo = new DrawingPolygonalObject();
                        rubberBandingObject = myo;
                        setDrawingPolygonalObjectStroke(myo);
                        myo.setLineColor(getLineColor());
                        myo.setFillColor(getFillColor());
                        myo.setRenderingMode(currentMode, getBezMode(), getChordParam());
                    }
                    
                    myo.handleMouseDown(x, y);
                    mousedDown = true;
                    repaint();
                } else {
                    myo.handleMouseDown(x, y);
                    jundo.runtime.ExtMilieuRef mil = undoMgr.getCurrentMil();
                    
                    
                    UTag utag = new UTag();
                    undoMgr.prepareForTempCommit(utag);
                    
                    
                    pdx_HighLevelList_pdx_ObjectRef drawingList =
                            model.pdxm_getDrawingList(mil);
                    
                    pdx_DrawingObject_pdx_PairRef pair =
                            rubberBandingObject.upCreate(mil);
                    
                    mil = pair.getMilieu();
                    
                    pdx_DrawingObject_pdx_ObjectRef drawObj =
                            (pdx_DrawingObject_pdx_ObjectRef)( pair.getObject() );
                    
                    
                    mil = rubberBandingObject.upBuild(mil, drawObj);
                    
                    if ( drawingList.pdxm_empty(mil)) {
                        mil = drawingList.pdxm_insertRight( mil, drawObj );
                    } else {
                        mil = drawingList.pdxm_searchHead(mil);
                        mil = drawingList.pdxm_left(mil);
                        mil = drawingList.pdxm_insertRight( mil, drawObj );
                        mil = drawingList.pdxm_searchHead(mil);
                    }
                    
                    undoMgr.handleCommitTempChange(mil);
                    undoMgr.commitUndoableOp(utag, "Add Polygonal Drawing Element");
                    
                    mousedDown = false;
                    rubberBandingObject = null;
                    repaint();
                }
                
            }
            
            if( ( currentMode == ErASE_MODE ) && !createdEditor ) {
                jundo.runtime.ExtMilieuRef mil = undoMgr.getCurrentMil();
                
                
                UTag utag = new UTag();
                undoMgr.prepareForTempCommit(utag);
                
                
                pdx_HighLevelList_pdx_ObjectRef drawingList =
                        model.pdxm_getDrawingList(mil);
                
                
                if ( !( drawingList.pdxm_empty(mil) ) ) {
                    
                    mil = drawingList.pdxm_searchHead(mil);
                    Point2D.Double inPt = new Point2D.Double(x, y);
                    
                    mil = handleEraseClick( mil , drawingList , inPt );
                    
                    if ( !( drawingList.pdxm_empty(mil) ) )
                        mil = drawingList.pdxm_searchHead( mil );
                    
                }
                
                undoMgr.handleCommitTempChange(mil);
                undoMgr.commitUndoableOp(utag, "Delete Drawing Element");
                
                mousedDown = false;
                rubberBandingObject = null;
                repaint();
            }
            
            
            if( ( currentMode == TO_FRONT_MODE ) && !createdEditor ) {
                jundo.runtime.ExtMilieuRef mil = undoMgr.getCurrentMil();
                
                
                UTag utag = new UTag();
                undoMgr.prepareForTempCommit(utag);
                
                
                pdx_HighLevelList_pdx_ObjectRef drawingList =
                        model.pdxm_getDrawingList(mil);
                
                
                if ( !( drawingList.pdxm_empty(mil) ) ) {
                    
                    mil = drawingList.pdxm_searchHead(mil);
                    Point2D.Double inPt = new Point2D.Double(x, y);
                    
                    mil = handleToFrontClick( mil , drawingList , inPt );
                    
                    if ( !( drawingList.pdxm_empty(mil) ) )
                        mil = drawingList.pdxm_searchHead( mil );
                    
                }
                
                undoMgr.handleCommitTempChange(mil);
                undoMgr.commitUndoableOp(utag, "Send Drawing Element To-Front");
                
                mousedDown = false;
                rubberBandingObject = null;
                repaint();
            }
            
            
        if( ( currentMode == TO_BACK_MODE ) && !createdEditor ) {
                jundo.runtime.ExtMilieuRef mil = undoMgr.getCurrentMil();
                
                
                UTag utag = new UTag();
                undoMgr.prepareForTempCommit(utag);
                
                
                pdx_HighLevelList_pdx_ObjectRef drawingList =
                        model.pdxm_getDrawingList(mil);
                
                
                if ( !( drawingList.pdxm_empty(mil) ) ) {
                    
                    mil = drawingList.pdxm_searchHead(mil);
                    Point2D.Double inPt = new Point2D.Double(x, y);
                    
                    mil = handleToBackClick( mil , drawingList , inPt );
                    
                    if ( !( drawingList.pdxm_empty(mil) ) )
                        mil = drawingList.pdxm_searchHead( mil );
                    
                }
                
                undoMgr.handleCommitTempChange(mil);
                undoMgr.commitUndoableOp(utag, "Send Drawing Element To-Back");
                
                mousedDown = false;
                rubberBandingObject = null;
                repaint();
            }
            
        }
    }
    
    /**
     * Handles a mouse click in erase mode.
     * @param _mil The current milieu.
     * @param drawingList The current list of drawing objects.
     * @param inPt The position of the event.
     * @return Mulieu resulting from executing the required operations.
     */
    protected ExtMilieuRef handleEraseClick( ExtMilieuRef _mil , pdx_HighLevelList_pdx_ObjectRef drawingList ,
            Point2D.Double InPt ) {
        ExtMilieuRef mil = _mil;
        boolean done = false;
        boolean found = false;
        double priority = ClickRec.MIN_PRIORITY + 1;
        if (!(drawingList.pdxm_empty(mil))) {
            mil = drawingList.pdxm_searchHead( mil );
            mil = drawingList.pdxm_left( mil );
            
            pdx_HighLevelList_pdx_PairRef pair = pdx_HighLevelList_pdx_ObjectRef.pdxm_new_HighLevelList(mil);
            mil = pair.getMilieu();
            pdx_HighLevelList_pdx_ObjectRef selectl = (pdx_HighLevelList_pdx_ObjectRef)( pair.getObject() );
            
            while (!done) {
                done = drawingList.pdxm_getHead( mil );
                
                pdx_DrawingObject_pdx_ObjectRef myDrw =
                        (pdx_DrawingObject_pdx_ObjectRef) drawingList.pdxm_getNode( mil );
                
                ClickRec mRec =
                        (ClickRec)( myDrw.pdxm_clickedInRegion(mil, this, ErASE_MODE, InPt) );
                
                if (mRec != null) {
                    if (mRec.clickPriority < priority) {
                        found = true;
                        mil = drawingList.pdxm_copyDataPlusPtrInfo( mil , selectl );
                        priority = mRec.clickPriority;
                    }
                }
                
                mil = drawingList.pdxm_left( mil );
            }
            
            if ( found ) {
                mil = selectl.pdxm_copyDataPlusPtrInfo( mil , drawingList );
                mil = drawingList.pdxm_eraseNodeInfo( mil );
            }
            
        }
        
        return(mil);
    }
    
    /**
     * Handles a mouse click in the move-to-front mode.
     * @param _mil The current milieu.
     * @param drawingList The current list of drawing objects.
     * @param inPt The position of the event.
     * @return Mulieu resulting from executing the required operations.
     */
    protected ExtMilieuRef handleToFrontClick( ExtMilieuRef _mil , pdx_HighLevelList_pdx_ObjectRef drawingList ,
            Point2D.Double InPt ) {
        ExtMilieuRef mil = _mil;
        boolean done = false;
        boolean found = false;
        double priority = ClickRec.MIN_PRIORITY + 1;
        if (!(drawingList.pdxm_empty(mil))) {
            mil = drawingList.pdxm_searchHead( mil );
            mil = drawingList.pdxm_left( mil );
            
            pdx_HighLevelList_pdx_PairRef pair = pdx_HighLevelList_pdx_ObjectRef.pdxm_new_HighLevelList(mil);
            mil = pair.getMilieu();
            pdx_HighLevelList_pdx_ObjectRef selectl = (pdx_HighLevelList_pdx_ObjectRef)( pair.getObject() );
            
            while (!done) {
                done = drawingList.pdxm_getHead( mil );
                
                pdx_DrawingObject_pdx_ObjectRef myDrw =
                        (pdx_DrawingObject_pdx_ObjectRef) drawingList.pdxm_getNode( mil );
                
                ClickRec mRec =
                        (ClickRec)( myDrw.pdxm_clickedInRegion(mil, this, TO_FRONT_MODE, InPt) );
                
                if (mRec != null) {
                    if (mRec.clickPriority < priority) {
                        found = true;
                        mil = drawingList.pdxm_copyDataPlusPtrInfo( mil , selectl );
                        priority = mRec.clickPriority;
                    }
                }
                
                mil = drawingList.pdxm_left( mil );
            }
            
            if ( found && !( drawingList.pdxm_isSingleNode( mil ) ) ) {
                mil = drawingList.pdxm_searchHead( mil );
                mil = drawingList.pdxm_left( mil );
                pdx_Meta_pdx_ObjectRef nd = selectl.pdxm_getNode( mil );
                if( nd != ( drawingList.pdxm_getNode( mil ) ) )
                {
                    mil = selectl.pdxm_eraseNodeInfo( mil );
                    mil = drawingList.pdxm_insertRight(mil, nd);
                }
            }
            
        }
        
        return(mil);
    }
    
    /**
     * Handles a mouse click in the move-to-back mode.
     * @param _mil The current milieu.
     * @param drawingList The current list of drawing objects.
     * @param inPt The position of the event.
     * @return Mulieu resulting from executing the required operations.
     */
    protected ExtMilieuRef handleToBackClick( ExtMilieuRef _mil , pdx_HighLevelList_pdx_ObjectRef drawingList ,
            Point2D.Double inPt ) {
        ExtMilieuRef mil = _mil;
        boolean done = false;
        boolean found = false;
        double priority = ClickRec.MIN_PRIORITY + 1;
        if (!(drawingList.pdxm_empty(mil))) {
            mil = drawingList.pdxm_searchHead( mil );
            mil = drawingList.pdxm_left( mil );
            
            pdx_HighLevelList_pdx_PairRef pair = pdx_HighLevelList_pdx_ObjectRef.pdxm_new_HighLevelList(mil);
            mil = pair.getMilieu();
            pdx_HighLevelList_pdx_ObjectRef selectl = (pdx_HighLevelList_pdx_ObjectRef)( pair.getObject() );
            
            while (!done) {
                done = drawingList.pdxm_getHead( mil );
                
                pdx_DrawingObject_pdx_ObjectRef myDrw =
                        (pdx_DrawingObject_pdx_ObjectRef) drawingList.pdxm_getNode( mil );
                
                ClickRec mRec =
                        (ClickRec)( myDrw.pdxm_clickedInRegion(mil, this, TO_BACK_MODE, inPt) );
                
                if (mRec != null) {
                    if (mRec.clickPriority < priority) {
                        found = true;
                        mil = drawingList.pdxm_copyDataPlusPtrInfo( mil , selectl );
                        priority = mRec.clickPriority;
                    }
                }
                
                mil = drawingList.pdxm_left( mil );
            }
            
            if ( found && !( drawingList.pdxm_isSingleNode( mil ) && !( selectl.pdxm_getHead( mil ) ) ) ) {
                mil = drawingList.pdxm_searchHead( mil );
                pdx_Meta_pdx_ObjectRef nd = selectl.pdxm_getNode( mil );
                mil = selectl.pdxm_eraseNodeInfo( mil );
                mil = drawingList.pdxm_insertLeft(mil, nd);
            }
            
        }
        
        return(mil);
    }
    
    /**
     * Handles mouse-up events.
     * @param e The input event.
     */
    public void mouserUp(MouseEvent e, int x, int y) {
        int currentMode = getEditMode();
        if ((currentMode > TEXT_MODE)
        && (currentMode < POLYGON_MODE)
        && mousedDown) {
            
            UTag utag = new UTag();
            undoMgr.prepareForTempCommit(utag);
            
            jundo.runtime.ExtMilieuRef mil = undoMgr.getCurrentMil();
            
            pdx_HighLevelList_pdx_ObjectRef drawingList =
                    model.pdxm_getDrawingList(mil);
            
            pdx_DrawingObject_pdx_PairRef pair =
                    rubberBandingObject.upCreate(mil);
            
            mil = pair.getMilieu();
            
            pdx_DrawingObject_pdx_ObjectRef drawObj =
                    (pdx_DrawingObject_pdx_ObjectRef)( pair.getObject() );
            
            
            mil = rubberBandingObject.upBuild(mil, drawObj);
            
            if ( drawingList.pdxm_empty(mil)) {
                mil = drawingList.pdxm_insertRight( mil, drawObj );
            } else {
                mil = drawingList.pdxm_searchHead(mil);
                mil = drawingList.pdxm_left(mil);
                mil = drawingList.pdxm_insertRight( mil, drawObj );
                mil = drawingList.pdxm_searchHead(mil);
            }
            
            undoMgr.handleCommitTempChange(mil);
            undoMgr.commitUndoableOp(utag, "Add Drawing Element");
            
            mousedDown = false;
            rubberBandingObject = null;
            repaint();
        }
        
        if( ( currentMode == EDIT_MODE ) && ( dragDrw != null ) ) {
            UTag utag = new UTag();
            undoMgr.prepareForTempCommit(utag);
            jundo.runtime.ExtMilieuRef mil = undoMgr.getCurrentMil();
            mil = dragDrwDown.upBuild(mil, dragDrw);
            undoMgr.handleCommitTempChange(mil);
            undoMgr.commitUndoableOp(utag, "Edit Drawing Element");
        }
        
        createdEditor = false;
        lastClickMatch = false;
        dragDrw = null;
        dragRec = null;
        dragDrwDown = null;
    }
    
    /**
     * Handles mouse-drag events.
     * @param e The input event.
     */
    public void mouserDrag(MouseEvent e, int x, int y) {
        int currentMode = getEditMode();
        if ((currentMode == EDIT_MODE) && (lastClickMatch)) {
            Point2D.Double inPt = new Point2D.Double(x, y);
            dragDrwDown.dragDisplayControl(this, dragRec, currentMode, inPt);
            repaint();
        }
        
        if ((currentMode > TEXT_MODE)
        && (currentMode < POLYGON_MODE)
        && mousedDown) {
            DrawingRectangularObject myo =
                    (DrawingRectangularObject) (rubberBandingObject);
            Rectangle myRect = myo.getRectangle();
            myRect.width = x - myRect.x;
            myRect.height = y - myRect.y;
            myo.setRectangle(myRect);
            repaint();
        }
        
        if ( ( currentMode >= POLYGON_MODE ) &&
                ( currentMode < ErASE_MODE ) &&
                mousedDown ) {
            DrawingPolygonalObject myo =
                    (DrawingPolygonalObject) (rubberBandingObject);
            myo.handleMouseDrag(x, y);
            repaint();
        }
    }
    
    /**
     * Gets the width of the current basic stroke.
     * @return The width of the current basic stroke.
     */
    protected double getBasicStrokeWidth() {
        return( model.pdxm_getBasicStrokeWidth( undoMgr.getCurrentMil() ) );
    }
    
    /**
     * Gets the current stroke.
     * @return The current stroke.
     */
    protected Stroke getMyStroke() {
        return( (Stroke)( model.pdxm_getMyStroke( undoMgr.getCurrentMil() ) ) );
    }
    
    /**
     * Sets the line stroke for rectangular objects.
     * @param in The rectangular object on which to set the stroke.
     */
    protected void setDrawingRectangularObjectStroke(DrawingRectangularObject in) {
        if (getBasicStrokeWidth() > 0.0) {
            in.setBasicStroke(getBasicStrokeWidth());
        } else {
            in.setStroke(getMyStroke());
        }
    }
    
    /**
     * Sets the line stroke for polygonal objects.
     * @param in The polygonal object on which to set the stroke.
     */
    protected void setDrawingPolygonalObjectStroke(DrawingPolygonalObject in) {
        if (getBasicStrokeWidth() > 0.0) {
            in.setBasicStroke(getBasicStrokeWidth());
        } else {
            in.setStroke(getMyStroke());
        }
    }
    
    /**
     * Sets the component's stroke.
     * @param in The stroke to set.
     */
    protected void setStroke(Stroke in) {
        ExtMilieuRef mil = model.pdxm_setMyStroke(undoMgr.getCurrentMil(),in);
        mil = model.pdxm_setBasicStrokeWidth(mil, -1);
        undoMgr.handleCommitTempChange(mil);
    }
    
    /**
     * Sets the component's basic stroke.
     * @param in The width of the stroke to set.
     */
    protected void setBasicStroke(double in) throws IllegalInputException {
        if (in <= 0.0)
            throw (
                    new IllegalInputException("The pen width must be positive."));
        
        ExtMilieuRef mil = model.pdxm_setBasicStrokeWidth(undoMgr.getCurrentMil(),in);
        mil = model.pdxm_setMyStroke(mil,
                new BasicStroke(
                (float) in,
                BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_MITER) );
        undoMgr.handleCommitTempChange(mil);
    }
    
    /**
     * Sets the parameters for curve rendering.  The mode parameter sets the curve mode, 1 for FMILL
     * and 2 for Bessel.  Chord sets the parameters of the curve, with true for chord length parameters
     * and false for uniform parameters.
     * @param mode The Bezier mode.
     * @param chord Whether chord-length parameterization is used.
     */
    protected void setCurveParam(int mode, boolean chord) {
        ExtMilieuRef mil = model.pdxm_setBezMode(undoMgr.getCurrentMil(),mode);
        mil = model.pdxm_setChordParam(mil, chord);
        undoMgr.handleCommitTempChange(mil);
    }
    
    /**
     * Handles mouse-move events.
     * @param e The input event.
     * @param x The X-Axis location of the event.
     * @param y The Y-Axis location of the event.
     */
    public void mouserMove(MouseEvent e, int x, int y) {
        int currentMode = getEditMode();
        if (mousedDown && (currentMode >= POLYGON_MODE ) &&
                ( currentMode < ErASE_MODE ) ) {
            DrawingPolygonalObject myo =
                    (DrawingPolygonalObject) (rubberBandingObject);
            myo.handleMouseDrag(x, y);
            repaint();
        }
    }
    
    /**
     * Handles mouse-click events.  Currently does nothing.
     * @param e The input event.
     * @param x The X-Axis location of the event.
     * @param y The Y-Axis location of the event.
     */
    public void mouserClick(MouseEvent e, int x, int y) {
    }
    
    /**
     * Handles a popup trigger by showing the property editor for the component, if
     * the component is in a mode that allows editing.
     * @param e The input event.
     */
    public void handlePopupTrigger(MouseEvent e) {
        try {
            if ((DesignerControl.isDesignTime()) || (!isOnlyDesignerEdits())) {
                EtherEvent send =
                        new StandardEtherEvent(
                        this,
                        StandardEtherEvent.showPropertiesEditor,
                        null,
                        this);
                send.setParameter(e.getPoint());
                ProgramDirector.fireEtherEvent(send, null);
                createdEditor = true;
            }
        } catch (Throwable ex) {
            handleThrow(ex);
        }
    }
    
    /**
     * Handles a program director event by embedding a component in the drawing app.
     * @param e The input event.
     * @return The result of executing the event.
     */
    protected Object handleProgramDirectorEvent(ProgramDirectorEvent e)
    throws Throwable {
        return (ContainerApp.addComponentToPane(e, undoMgr, myDesk));
    }
    
    /**
	* Returns the data flavors that the component can load from persistent storage.
    * @return The supported flavors.
	*/
    public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
        DataFlavor[] myF =
        { new TransVersionBufferFlavor("Draw App", "Draw App")};
        return (myF);
    }
    
    /**
	* Returns the data flavors that the component can save to persistent storage.
    * @return The supported flavors.
	*/
    public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
        DataFlavor[] myF =
        { new TransVersionBufferFlavor("Draw App", "Draw App")};
        return (myF);
    }
    
    /**
	* Loads data for the component from persistent storage.
    * @param flavor The flavor in which to load the data.
    * @param trans The transferable from which to load the data.
	*/
    public void loadPersistentData(DataFlavor flavor, Transferable trans)
    throws
            IOException,
            ClassNotFoundException,
            ResourceNotFoundException,
            ComponentNotFoundException {
        if (trans instanceof UrlHolder) {
            fileSaveURL = ((UrlHolder) trans).getUrl();
            fileSaveFlavor = flavor;
        }
        
        if (trans == null) {
            try {
                setBasicStroke(2);
            } catch (Exception ex) {
                throw (new WrapRuntimeException(ex));
            }
            setColors(Color.blue, Color.green);
            
            jundo.runtime.ExtMilieuRef mil = undoMgr.getCurrentMil();
            pdx_HighLevelList_pdx_ObjectRef drawingList =
                    model.pdxm_getDrawingList(mil);
            mil = drawingList.pdxm_eraseAllInfo(mil);
            undoMgr.handleCommitTempChange(mil);
            
            setEditMode(LINE_MODE);
            bkgnd.setBackgroundState(Color.white, true);
            try {
                borderObject.setBorderObject(null, null, null);
            } catch (Exception ex) {
                throw (new WrapRuntimeException("New Border Failed", ex));
            }
            onlyDesignerEdits.setOnlyDesignerEdits(false);
            ContainerApp.closeAllFrames(myDesk);
            macroMap.clear();
            docPageFormat.setDocPageFormat(null);
        } else {
            try {
                TransVersionBuffer myF = (TransVersionBuffer) trans;
                VersionBuffer.chkNul(myF);
                Color coll = (Color) (myF.getProperty("LineColor"));
                VersionBuffer.chkNul(coll);
                Color colf = (Color) (myF.getProperty("FillColor"));
                VersionBuffer.chkNul(colf);
                setColors( coll , colf );
                
                VersionBuffer drawVb = (VersionBuffer)(myF.getProperty("DrawingList"));
                VersionBuffer.chkNul(drawVb);
                HashMap persistenceContext = new HashMap();
                pdx_HighLevelList_pdx_ObjectRef list = (pdx_HighLevelList_pdx_ObjectRef)( undoMgr.generateJUndoFromPersistence( drawVb , persistenceContext ) );
                VersionBuffer.chkNul(list);
                jundo.runtime.ExtMilieuRef mil = undoMgr.getCurrentMil();
                mil = model.pdxm_setDrawingList(mil, list);
                undoMgr.handleCommitTempChange(mil);
                
                setEditMode( myF.getInt("currentMode") );
                int bezMode = myF.getInt("BezMode");
                boolean chordP = myF.getBoolean("ChordP");
                setCurveParam(bezMode, chordP);
                macroMap.readData(myF);
                
                bkgnd.readData(myF);
                
                borderObject.readData(myF);
                
                Dimension dim = (Dimension) (myF.getProperty("PageSize"));
                VersionBuffer.chkNul(dim);
                alterPageSize(dim);
                
                Object myo = myF.getProperty("BasicStrokeWidth");
                if (myo != null) {
                    double wid = myF.getDouble("BasicStrokeWidth");
                    setBasicStroke(wid);
                }
                
                Stroke strk = (Stroke) (myF.getProperty("MyStroke"));
                if (strk != null) {
                    setStroke( strk );
                }
                
                onlyDesignerEdits.setOnlyDesignerEdits(
                        myF.getBoolean("OnlyDesignerEdits"));
                
                Transferable myT = (Transferable) (myF.getProperty("Frames"));
                VersionBuffer.chkNul(myT);
                ContainerApp.loadInternalDesktopFrames(
                        myT,
                        ContainerAppInternalFrame.class,
                        undoMgr,
                        myDesk);
            } catch (ClassNotFoundException ex) {
                throw (ex);
            } catch (IOException ex) {
                throw (ex);
            } catch (ResourceNotFoundException ex) {
                throw (ex);
            } catch (ComponentNotFoundException ex) {
                throw (ex);
            } catch (ClassCastException ex) {
                throw (new DataFormatException(ex));
            } catch (IllegalInputException ex) {
                throw (new DataFormatException(ex));
            }
        }
    }
    
    /**
	* Saves the component to persistent storage.
    * @param The flavor in which to save the data.
    * @return Transferable to which the data is saved.
	*/
    public Transferable savePersistentData(DataFlavor flavor)
    throws IOException {
        TransVersionBuffer MyF = new TransVersionBuffer("Draw App", "Draw App");
        
        try {
            MyF.setProperty(
                    "Frames",
                    ContainerApp.saveInternalDesktopFrames(myDesk));
            MyF.setProperty("LineColor", getLineColor());
            MyF.setProperty("FillColor", getFillColor());
            
            HashMap persistenceContext = new HashMap();
            MyF.setProperty("DrawingList",
                    undoMgr.generatePersistenceFromJUndo(
                    model.pdxm_getDrawingList( undoMgr.getCurrentMil() ) , persistenceContext ) );
            
            MyF.setInt("currentMode", getEditMode());
            bkgnd.writeData(MyF);
            MyF.setBoolean("OnlyDesignerEdits", isOnlyDesignerEdits());
            MyF.setInt("BezMode", getBezMode() );
            MyF.setBoolean("ChordP", getChordParam() );
            macroMap.writeData(MyF);
            MyF.setProperty(
                    "PageSize",
                    new Dimension(
                    myPan.getBounds().width,
                    myPan.getBounds().height));
            
            borderObject.writeData(MyF);
            
            if (getBasicStrokeWidth() > 0.0)
                MyF.setDouble("BasicStrokeWidth", getBasicStrokeWidth());
            
            if ((getMyStroke() instanceof Serializable)
            || (getMyStroke() instanceof Externalizable))
                MyF.setProperty("MyStroke", getMyStroke());
        } catch (IOException ex) {
            throw (ex);
        }
        
        return (MyF);
    }
    
    /**
     * Gets the desktop pane used for display.
     * @return The desktop pane used for display.
     */
    public JComponent getDesk() {
        return (myDesk);
    }
    
    /**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
    public static void displayVerdantiumHelp(VerdantiumComponent in) {
        DrawAppHelp.run(in);
    }
    
    /**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
    public static void main(String argv[]) {
        ProgramDirector.initUI();
        DrawApp myComp = new DrawApp();
        ProgramDirector.showComponent(myComp, "Draw App", argv, true);
    }
    
    
}

