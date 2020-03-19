package verdantium.standard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;
import verdantium.kluges.SerPoint2D;

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
* This is a data type used by {@link DrawApp} for drawing curves and polygons.
* <P>
* @author Thorn Green
*/
public class DrawingPolygonalObject extends DrawingObject {
	
	/**
	 * Constant indicating that there was no match to any of the controls.
	 */
	public static final int NO_MATCH = 0;
	
	/**
	 * Constant indicating that there was a match to one of the controls.
	 */
	public static final int MATCH = 1;

	/**
	* True if the curve or polygon is filled.  False otherwise.
	*/
	private boolean filledRender = false;
	
	/**
	* True if curves are to be rendered.  False if polygons are to be rendered.
	*/
	private boolean curveRender = true;
	
	/**
	* The GeneralPath containing the shape to be rendered.
	*/
	private GeneralPath myPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
	
	/**
	* The line color in which the object is to be rendered.
	*/
	private Color lineColor = Color.black;
	
	/**
	* The fill color in which the object is to be rendered.
	*/
	private Color fillColor = Color.red;
	
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
	
	/**
	* The interpolation points of the curve or polygon.
	*/
	private Vector<SerPoint2D> prevInt = new Vector<SerPoint2D>();
	
	/**
	* The Bezier points of the curve.
	*/
	private Vector<Point2D.Double> prevBez = new Vector<Point2D.Double>();
	
	/**
	* The Bezier mode of the curve.  A value of 1 indicates FMILL tangents, a value
	* of 2 indicates Bessel tangents.
	*/
	private int bezMode = 0;
	
	/**
	* True for chord parameters in curve interpolation, false for uniform parameters.
	*/
	private boolean chordP = false;
        
        /**
         * The rendering mode of the curve.
         */
        private int renderingMode = 0;

        
	@Override
	public void draw(DrawApp thePort, Graphics2D g, int toolMode) {
		if (filledRender) {
			g.setStroke(myStroke);
			g.setColor(fillColor);
			g.fill(myPath);
			g.setColor(lineColor);
			g.draw(myPath);
		} else {
			g.setStroke(myStroke);
			g.setColor(lineColor);
			g.draw(myPath);
		}

	}

	@Override
	public void drawTools(DrawApp thePort, Graphics2D g, int toolMode) {
		int sz = prevInt.size();
		int count;
		if( toolMode != DrawApp.ErASE_MODE )
                        g.setColor(Color.cyan);
                    else g.setColor(Color.orange);

		for (count = 0; count < sz; count++) {
			SerPoint2D ser = prevInt.elementAt(count);
			Rectangle2D.Double r1 =
				thePort.instanceRect(ser.getX(), ser.getY());
			g.fill(r1);
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

		int sz = prevInt.size();
		int count;

		for (count = 0; count < sz; count++) {
			SerPoint2D ser = prevInt.elementAt(count);

			priority =
				thePort.defaultGravityField(inPt, ser.getX(), ser.getY());
			if ((priority <= ClickRec.MIN_PRIORITY) && (lastClick == NO_MATCH)) {
				newRec.setValue(count);
				newRec.setXCoord(ser.getX());
				newRec.setYCoord(ser.getY());
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

		SerPoint2D pt = new SerPoint2D(inPt.getX(), inPt.getY());

		if (curveRender) {
			CurveEdit.processChangePoint(
				prevInt,
				prevBez,
				val,
				pt,
				bezMode,
				chordP);
		} else {
			prevInt.setElementAt(pt, val);
		}

		createGeneralPathFromVector(prevInt, prevBez);
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
	* Gets the stroke used for rendering.
	* @return The stroke used for rendering.
	*/
	public Stroke getStroke() {
		return (myStroke);
	}
	
	/**
	* Sets the stroke used by the rendering object to the one passed in the parameter.
	* @param in The stroke used for rendering.
	*/
	public void setStroke(Stroke in) {
		myStroke = in;
		basicStrokeWidth = -1;
	}
	
	/**
	* Sets the stroke used when rendering the object to a basic stroke with a certain width.
	* @param in The width of the stroke to set.
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
	* Handles a mouse-drag event by moving the last interpolation point.
	* @param x The X-Coordinate of the mouse-drag.
	* @param y The Y-Coordinate of the mouse-drag.
	*/
	public void handleMouseDrag(int x, int y) {

		int index = prevInt.size() - 1;

		if (curveRender) {
			CurveEdit.processChangePoint(
				prevInt,
				prevBez,
				index,
				new SerPoint2D(x, y),
				bezMode,
				chordP);
		} else {
			prevInt.setElementAt(new SerPoint2D(x, y), index);
		}

		createGeneralPathFromVector(prevInt, prevBez);
	}

	/**
	* Handles a mouse-down event by adding an interpolation point to the shape.
	* @param x The X-Coordinate of the mouse-down.
	* @param y The Y-Coordinate of the mouse-down.
	*/
	public void handleMouseDown(int x, int y) {

		if (prevInt.size() == 0) {
			if (curveRender) {
				CurveEdit.processAddPoint(
					prevInt,
					prevBez,
					new SerPoint2D(x, y),
					bezMode,
					chordP);
			} else {
				prevInt.addElement(new SerPoint2D(x, y));
			}
		}

		if (curveRender) {
			CurveEdit.processAddPoint(
				prevInt,
				prevBez,
				new SerPoint2D(x, y),
				bezMode,
				chordP);
		} else {
			prevInt.addElement(new SerPoint2D(x, y));
		}

		createGeneralPathFromVector(prevInt, prevBez);
	}

	/**
	* Creates a GeneralPath object that represents the shape to be rendered, where prevInt
	* contains the interpolation points, and prevBez contains the Bezier points (if needed).
	* The GeneralPath is stored in myPath.
	* @param prevInt The vector of interpolation points.
	* @param prevBez The vector of Bezier points.
	*/
	protected void createGeneralPathFromVector(
		Vector<SerPoint2D> prevInt,
		Vector<Point2D.Double> prevBez) {
		if (curveRender) {
			myPath = CurveEdit.createGeneralPathFromVector(prevBez);
		} else {
			int elem = prevInt.size();
			GeneralPath temp = new GeneralPath(GeneralPath.WIND_EVEN_ODD, elem);

			if (elem > 0) {
				Point2D.Double first = prevInt.elementAt(0);
				temp.moveTo((float) (first.getX()), (float) (first.getY()));
				int cnt = 1;
				for (cnt = 1; cnt < elem; cnt = cnt + 1) {
					Point2D.Double b =
						prevInt.elementAt(cnt);
					temp.lineTo((float) (b.getX()), (float) (b.getY()));
				}
			}

			myPath = temp;
		}

	}
        
	/**
	 * Sets the interpolation and Bezier vectors.
	 * @param _prevInt The vector of interpolation points.
	 * @param _prevBez The vector of Bezier points.
	 */
        public void setVectors( Vector<SerPoint2D> _prevInt , Vector<Point2D.Double> _prevBez )
        {
            prevInt = _prevInt;
            prevBez = _prevBez;
            createGeneralPathFromVector( prevInt , prevBez );
        }

        
	/**
	* Sets the rendering mode indicating what shape is to be rendered.  The first parameter indicates
	* what kind of shape is to be rendered.  It can take on the values of {@link DrawApp#POLYGON_MODE},
	* {@link DrawApp#FILLED_POLYGON_MODE}, {@link DrawApp#CURVE_MODE}, or {@link DrawApp#FILLED_CURVE_MODE}.
	* curveMode indicates the kind of curve interpolation to be used (if curves are drawn).  A value 
	* of 1 indicates FMILL tangents, a value of 2 indicates Bessel tangents.  inChordP indicates what 
	* kind of interpolation parameters are to be used.  inChordP is true for chord length parameters, 
	* and false for uniform parameters.
	* @param in The kind of shape is to be rendered.
	* @param curveMode The kind of curve interpolation to be used (if curves are drawn).
	* @param inChordP What kind of interpolation parameters are to be used.
	*/
	public void setRenderingMode(int in, int curveMode, boolean inChordP) {
		bezMode = curveMode;
		chordP = inChordP;
                renderingMode = in;

		switch (in) {
			case DrawApp.POLYGON_MODE :
				curveRender = false;
				filledRender = false;
				break;

			case DrawApp.FILLED_POLYGON_MODE :
				curveRender = false;
				filledRender = true;
				break;

			case DrawApp.CURVE_MODE :
				curveRender = true;
				filledRender = false;
				break;

			case DrawApp.FILLED_CURVE_MODE :
				curveRender = true;
				filledRender = true;
				break;
		}
	}
        
		@Override
        public jundo.runtime.ExtMilieuRef upBuild( jundo.runtime.ExtMilieuRef _mil ,
                pdx_DrawingObject_pdx_ObjectRef _out )
        {
            jundo.runtime.ExtMilieuRef mil = _mil;
            pdx_DrawingPolygonalObject_pdx_ObjectRef out =
                    (pdx_DrawingPolygonalObject_pdx_ObjectRef) _out;
            mil = out.pdxm_setFilledRender(mil, filledRender);
            mil = out.pdxm_setCurveRender(mil, curveRender);
            mil = out.pdxm_setMyPath(mil, myPath);
            mil = out.pdxm_setLineColor(mil,lineColor);
            mil = out.pdxm_setFillColor(mil,fillColor);
            mil = out.pdxm_setBasicStrokeWidth(mil, basicStrokeWidth);
            mil = out.pdxm_setMyStroke(mil, myStroke);
            mil = out.pdxm_setPrevInt(mil, prevInt);
            mil = out.pdxm_setPrevBez(mil, prevBez);
            mil = out.pdxm_setBezMode(mil, bezMode);
            mil = out.pdxm_setChordP(mil, chordP);
            mil = out.pdxm_setRenderingMode(mil, renderingMode);
            
            return( mil );
        }

        @Override
        public pdx_DrawingObject_pdx_PairRef upCreate( jundo.runtime.ExtMilieuRef mil )
        {
            return( pdx_DrawingPolygonalObject_pdx_ObjectRef.pdxm_new_DrawingPolygonalObject( mil ) );
        }
        

}


