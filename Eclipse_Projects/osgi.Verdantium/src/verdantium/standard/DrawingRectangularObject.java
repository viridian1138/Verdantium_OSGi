package verdantium.standard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;


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
*    | 10/29/2000            | Thorn Green (viridian_1138@yahoo.com)           | Classes did not have names that followed standard Java conventions.  | Performed a global modification to bring the names within spec.
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Needed to support drag-manipulations on DrawingObject.               | Added code using the ClickRec/APPRec pattern.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 11/17/2001            | Thorn Green (viridian_1138@yahoo.com)           | Documentation fixes.                                                 | Documentation fixes.
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
* Class used by {@link DrawApp} for drawing lines, rectangles, round rectangles, and ovals.
* <P>
* @author Thorn Green
*/
public class DrawingRectangularObject
	extends DrawingObject {
	
	/**
	 * Constant indicating that there was no match to any of the controls.
	 */
	public static final int NO_MATCH = 0;
	
	/**
	 * Constant indicating that there was a match to one of the controls.
	 */
	public static final int MATCH = 1;

	/**
	 * Mode for manually dragging the first of four points on the bounding rectangle.
	 */
	public static final int MANUAL_DRAG_P1 = 0;
	
	/**
	 * Mode for manually dragging the second of four points on the bounding rectangle.
	 */
	public static final int MANUAL_DRAG_P2 = 1;
	
	/**
	 * Mode for manually dragging the third of four points on the bounding rectangle.
	 */
	public static final int MANUAL_DRAG_P3 = 2;
	
	/**
	 * Mode for manually dragging the fourth of four points on the bounding rectangle.
	 */
	public static final int MANUAL_DRAG_P4 = 3;

	/**
	* The rendering mode of the object.  This mode can take on the values of either {@link DrawApp#LINE_MODE},
	* {@link DrawApp#RECT_MODE}, {@link DrawApp#FILLED_RECT_MODE}, {@link DrawApp#ROUND_RECT_MODE},
	* {@link DrawApp#FILLED_ROUND_RECT_MODE}, {@link DrawApp#OVAL_MODE}, or {@link DrawApp#FILLED_OVAL_MODE}.
	*/
	private int renderingMode = DrawApp.LINE_MODE;
	
	/**
	* The line color of the rendered object.
	*/
	private Color lineColor = Color.black;
	
	/**
	* The fill color of the rendered object.
	*/
	private Color fillColor = Color.red;
	
	/**
	* The rectangle that describes where the object is to be rendered.
	*/
	private Rectangle myRect = new Rectangle(50, 50, 50, 50);
	
	/**
	* The width of the stroke used to render the object.  Holds -1 if the stroke
	* has not created by the {@link #setBasicStroke} method.
	*/
	private double basicStrokeWidth = 1;
	
	/**
	* The stroke used to render the object.
	*/
	private Stroke myStroke =
		new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER);

	@Override
	public void draw(DrawApp thePort, Graphics2D g, int toolMode) {
		switch (renderingMode) {
			case DrawApp.LINE_MODE :
				g.setStroke(myStroke);
				g.setColor(lineColor);
				g.draw(myLine);
				break;

			case DrawApp.RECT_MODE :
				g.setStroke(myStroke);
				g.setColor(lineColor);
				g.draw(myRct);
				break;

			case DrawApp.FILLED_RECT_MODE :
				g.setStroke(myStroke);
				g.setColor(fillColor);
				g.fill(myRct);
				g.setColor(lineColor);
				g.draw(myRct);
				break;

			case DrawApp.ROUND_RECT_MODE :
				g.setStroke(myStroke);
				g.setColor(lineColor);
				g.draw(myRound);
				break;

			case DrawApp.FILLED_ROUND_RECT_MODE :
				g.setStroke(myStroke);
				g.setColor(fillColor);
				g.fill(myRound);
				g.setColor(lineColor);
				g.draw(myRound);
				break;

			case DrawApp.OVAL_MODE :
				g.setStroke(myStroke);
				g.setColor(lineColor);
				g.draw(myEll);
				break;

			case DrawApp.FILLED_OVAL_MODE :
				g.setStroke(myStroke);
				g.setColor(fillColor);
				g.fill(myEll);
				g.setColor(lineColor);
				g.draw(myEll);
				break;

		}

	}

	@Override
	public void drawTools(DrawApp thePort, Graphics2D g, int toolMode) {
		if (renderingMode == DrawApp.LINE_MODE) {
			Rectangle rect = myRect;
			Rectangle2D.Double r1 =
				thePort.instanceRect(rect.getX(), rect.getY());
			Rectangle2D.Double r2 =
				thePort.instanceRect(
					rect.getX() + rect.getWidth(),
					rect.getY() + rect.getHeight());
			if( toolMode != DrawApp.ErASE_MODE )
                                g.setColor(Color.cyan);
                            else g.setColor(Color.orange);
			g.fill(r1);
			g.fill(r2);
		} else {
			Rectangle rect = myRect;
			Rectangle2D.Double r1 =
				thePort.instanceRect(rect.getX(), rect.getY());
			Rectangle2D.Double r2 =
				thePort.instanceRect(
					rect.getX() + rect.getWidth(),
					rect.getY() + rect.getHeight());
			Rectangle2D.Double r3 =
				thePort.instanceRect(
					rect.getX() + rect.getWidth(),
					rect.getY());
			Rectangle2D.Double r4 =
				thePort.instanceRect(
					rect.getX(),
					rect.getY() + rect.getHeight());
			if( toolMode != DrawApp.ErASE_MODE )
                                g.setColor(Color.cyan);
                            else g.setColor(Color.orange);
			g.fill(r1);
			g.fill(r2);
			g.fill(r3);
			g.fill(r4);
		}
	}

	/**
	 * Determines if a mouse-click was in one of the control regions of the object.
	 * @param thePort The DrawApp providing the context in which to loom for a match.
	 * @param inPt The mouse-click location to check for a match.
	 * @param toolMode The application mode in which to look for a match.
	 * @return An object describing the match if a match was found, otherwise returns null.
	 */
	public ClickRec clickedInRegion(
		DrawApp thePort,
		Point2D.Double inPt,
		int toolMode) {
		APPRec newRec = new APPRec();
		APPRec ret = null;
		double priority = ClickRec.MIN_PRIORITY + 1;
		int lastClick = NO_MATCH;
		Rectangle rect = myRect;

		if (renderingMode == DrawApp.LINE_MODE) {
			priority =
				thePort.defaultGravityField(inPt, rect.getX(), rect.getY());
			if ((priority <= ClickRec.MIN_PRIORITY) && (lastClick == NO_MATCH)) {
				newRec.setValue(MANUAL_DRAG_P1);
				newRec.setXCoord(rect.getX() + rect.getWidth());
				newRec.setYCoord(rect.getY() + rect.getHeight());
				newRec.clickPriority = priority;
				ret = newRec;
				lastClick = MATCH;
			}

			priority =
				thePort.defaultGravityField(
					inPt,
					rect.getX() + rect.getWidth(),
					rect.getY() + rect.getHeight());
			if ((priority <= ClickRec.MIN_PRIORITY) && (lastClick == NO_MATCH)) {
				newRec.setValue(MANUAL_DRAG_P2);
				newRec.setXCoord(rect.getX());
				newRec.setYCoord(rect.getY());
				newRec.clickPriority = priority;
				ret = newRec;
				lastClick = MATCH;
			}

		} else {
			priority =
				thePort.defaultGravityField(inPt, rect.getX(), rect.getY());
			if ((priority <= ClickRec.MIN_PRIORITY) && (lastClick == NO_MATCH)) {
				newRec.setValue(MANUAL_DRAG_P1);
				newRec.setXCoord(rect.getX() + rect.getWidth());
				newRec.setYCoord(rect.getY() + rect.getHeight());
				newRec.clickPriority = priority;
				ret = newRec;
				lastClick = MATCH;
			}

			priority =
				thePort.defaultGravityField(
					inPt,
					rect.getX() + rect.getWidth(),
					rect.getY() + rect.getHeight());
			if ((priority <= ClickRec.MIN_PRIORITY) && (lastClick == NO_MATCH)) {
				newRec.setValue(MANUAL_DRAG_P2);
				newRec.setXCoord(rect.getX());
				newRec.setYCoord(rect.getY());
				newRec.clickPriority = priority;
				ret = newRec;
				lastClick = MATCH;
			}

			priority =
				thePort.defaultGravityField(
					inPt,
					rect.getX() + rect.getWidth(),
					rect.getY());
			if ((priority <= ClickRec.MIN_PRIORITY) && (lastClick == NO_MATCH)) {
				newRec.setValue(MANUAL_DRAG_P3);
				newRec.setXCoord(rect.getX());
				newRec.setYCoord(rect.getY() + rect.getHeight());
				newRec.clickPriority = priority;
				ret = newRec;
				lastClick = MATCH;
			}

			priority =
				thePort.defaultGravityField(
					inPt,
					rect.getX(),
					rect.getY() + rect.getHeight());
			if ((priority <= ClickRec.MIN_PRIORITY) && (lastClick == NO_MATCH)) {
				newRec.setValue(MANUAL_DRAG_P4);
				newRec.setXCoord(rect.getX() + rect.getWidth());
				newRec.setYCoord(rect.getY());
				newRec.clickPriority = priority;
				ret = newRec;
				lastClick = MATCH;
			}

		}

		return ret;
	}

	@Override
	public void dragDisplayControl(
		DrawApp thePort,
		ClickRec in,
		int toolMode,
		Point2D.Double inPt) {
		APPRec ino = (APPRec) (in);
		int val = ino.getValue();
		double xv = ino.getXCoord();
		double yv = ino.getYCoord();

		if (val == MANUAL_DRAG_P1) {
			myRect =
				new Rectangle(
					(int) xv,
					(int) yv,
					(int) (inPt.getX() - xv),
					(int) (inPt.getY() - yv));
		}

		if (val == MANUAL_DRAG_P2) {
			myRect =
				new Rectangle(
					(int) (inPt.getX()),
					(int) (inPt.getY()),
					(int) (xv - inPt.getX()),
					(int) (yv - inPt.getY()));
		}

		if (val == MANUAL_DRAG_P3) {
			myRect =
				new Rectangle(
					(int) (inPt.getX()),
					(int) yv,
					(int) (xv - inPt.getX()),
					(int) (inPt.getY() - yv));
		}

		if (val == MANUAL_DRAG_P4) {
			myRect =
				new Rectangle(
					(int) xv,
					(int) (inPt.getY()),
					(int) (inPt.getX() - xv),
					(int) (yv - inPt.getY()));
		}

		setRenderingMode(renderingMode);
	}

	/**
	* Sets the line color of the rendering.
	* @param in The line color to set.
	*/
	public void setLineColor(Color in) {
		lineColor = in;
	}
	
	/**
	* Sets the fill color of the rendering.
	* @param in The fill color to set.
	*/
	public void setFillColor(Color in) {
		fillColor = in;
	}
	
	/**
	* Gets the rectangle in which the object is to be drawn.
	* @return The rectangle in which the object is to be drawn.
	*/
	public Rectangle getRectangle() {
		return (myRect);
	}
	
	/**
	* Sets the rectangle in which the object is to be drawn.
	* @param in The rectangle to set.
	*/
	public void setRectangle(Rectangle in) {
		myRect = in;
		setRenderingMode(renderingMode);
	}
	
	/**
	* Gets the stroke used for rendering.
	* @return The stroke used for rendering.
	*/
	public Stroke getStroke() {
		return (myStroke);
	}
	
	/**
	* Sets the stroke used by the rendering object to the one passed in the parameter.
	* @param The stroke to set.
	*/
	public void setStroke(Stroke in) {
		myStroke = in;
		basicStrokeWidth = -1;
	}
	
	/**
	* Sets the stroke used when rendering the object to a basic stroke with a certain width.
	* @param in The width to set.
	*/
	public void setBasicStroke(double in) {
		basicStrokeWidth = in;
		myStroke =
			new BasicStroke(
				(float) in,
				BasicStroke.CAP_SQUARE,
				BasicStroke.JOIN_MITER);
	}

	/**
	* Sets the rendering mode indicating what kind of object is to be rendered.
	* This mode can take on the values of either {@link DrawApp#LINE_MODE},
	* {@link DrawApp#RECT_MODE}, {@link DrawApp#FILLED_RECT_MODE}, {@link DrawApp#ROUND_RECT_MODE},
	* {@link DrawApp#FILLED_ROUND_RECT_MODE}, {@link DrawApp#OVAL_MODE}, or {@link DrawApp#FILLED_OVAL_MODE}.
	* @param in The rendering mode to set.
	*/
	public void setRenderingMode(int in) {
		renderingMode = in;

		switch (renderingMode) {
			case DrawApp.LINE_MODE :
				if (myLine == null)
					myLine = new Line2D.Float();
				myLine.setLine(
					myRect.x,
					myRect.y,
					myRect.x + myRect.width,
					myRect.y + myRect.height);
				break;

			case DrawApp.RECT_MODE :
			case DrawApp.FILLED_RECT_MODE :
				if (myRct == null)
					myRct = new Rectangle2D.Float();
				int x = Math.min(myRect.x, myRect.x + myRect.width);
				int y = Math.min(myRect.y, myRect.y + myRect.height);
				myRct.setRect(
					x,
					y,
					Math.abs(myRect.width),
					Math.abs(myRect.height));
				break;

			case DrawApp.ROUND_RECT_MODE :
			case DrawApp.FILLED_ROUND_RECT_MODE :
				if (myRound == null)
					myRound = new RoundRectangle2D.Float();
				x = Math.min(myRect.x, myRect.x + myRect.width);
				y = Math.min(myRect.y, myRect.y + myRect.height);
				myRound.setRoundRect(
					x,
					y,
					Math.abs(myRect.width),
					Math.abs(myRect.height),
					20,
					20);
				break;

			case DrawApp.OVAL_MODE :
			case DrawApp.FILLED_OVAL_MODE :
				if (myEll == null)
					myEll = new Ellipse2D.Float();
				x = Math.min(myRect.x, myRect.x + myRect.width);
				y = Math.min(myRect.y, myRect.y + myRect.height);
				myEll.setFrame(
					x,
					y,
					Math.abs(myRect.width),
					Math.abs(myRect.height));
				break;

		}

	}
        
	    @Override
        public jundo.runtime.ExtMilieuRef upBuild( jundo.runtime.ExtMilieuRef _mil ,
                pdx_DrawingObject_pdx_ObjectRef _out )
        {
            jundo.runtime.ExtMilieuRef mil = _mil;
            pdx_DrawingRectangularObject_pdx_ObjectRef out =
                    (pdx_DrawingRectangularObject_pdx_ObjectRef) _out;
            mil = out.pdxm_setBasicStrokeWidth(mil, basicStrokeWidth);
            mil = out.pdxm_setFillColor(mil, fillColor);
            mil = out.pdxm_setLineColor(mil, lineColor);
            mil = out.pdxm_setMyEll(mil,myEll);
            mil = out.pdxm_setMyLine(mil,myLine);
            mil = out.pdxm_setMyRct(mil, myRct);
            mil = out.pdxm_setMyRect(mil, myRect);
            mil = out.pdxm_setMyRound(mil, myRound);
            mil = out.pdxm_setMyStroke(mil, myStroke);
            mil = out.pdxm_setRenderingMode(mil, renderingMode);
            
            return( mil );
        }
    
        @Override
        public pdx_DrawingObject_pdx_PairRef upCreate( jundo.runtime.ExtMilieuRef mil )
        {
            return( pdx_DrawingRectangularObject_pdx_ObjectRef.pdxm_new_DrawingRectangularObject( mil ) );
        }
        

	/**
	* Ellipse that is rendered if the class is in an ellipse mode.
	*/
	private Ellipse2D.Float myEll = null;
	
	/**
	* Round rectangle that is rendered if the class is in a round rectangle mode.
	*/
	private RoundRectangle2D.Float myRound = null;
	
	/**
	* Rectangle that is rendered if the class is in a rectangle mode.
	*/
	private Rectangle2D.Float myRct = null;
	
	/**
	* Line that is rendered if the class is in LINE_MODE.
	*/
	private Line2D.Float myLine = null;
	
}

