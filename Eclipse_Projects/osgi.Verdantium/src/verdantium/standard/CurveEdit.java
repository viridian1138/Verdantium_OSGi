/*
 * Created on Nov 20, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package verdantium.standard;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
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
 * @author thorngreen
 *
 * Class for generalized cubic curve editing in Java-2D.
 * For a reference, see "Curves and Surfaces for CAGD" by Gerald Farin.
 */
public class CurveEdit {

	/**
	* X coordinates for three adjacent Bezier points.
	*/
	protected static double xVals[] = { 0, 0, 0 };
	
	/**
	* Y coordinates for three adjacent Bezier points.
	*/
	protected static double yVals[] = { 0, 0, 0 };
	
	/**
	* The parameter deltas between the interpolation points in {@link xVals} and {@link yVals}.
	* Note: deltaU[ 2 ] is not used.
	*/
	protected static double deltaU[] = { 0, 0, 0 };
	

	/**
	* X ordinate of the Bessel tangeant vector calculated by {@link CalcBessel}, and
	* used by other routines.
	*/
	protected static double mix;
	
	/**
	* Y ordinate of the Bessel tangeant vector calculated by {@link CalcBessel}, and
	* used by other routines.
	*/
	protected static double miy;
	
	/**
	* X ordinate of the FMILL tangeant vector calculated by {@link CalcFMILL}, and
	* used by other routines.
	*/
	protected static double lix;
	
	/**
	* Y ordinate of the FMILL tangeant vector calculated by {@link CalcFMILL}, and
	* used by other routines.
	*/
	protected static double liy;

	/**
	* Given interpolation points at ( {@link xVals}[ 0 ] , {@link yVals}[ 0 ] ) and
	* ( {@link xVals}[ 2 ] , {@link yVals}[ 2 ] ) calculates the vector ( {@link lix} , {@link liy} )
	* as an FMILL tangeant.
	*/
	protected static void calcFMILL() {
		double dx = xVals[2] - xVals[0];
		double dy = yVals[2] - yVals[0];
		/* double len = Math.sqrt( dx * dx + dy * dy );
		if( len < 0.0001 ) len = 0.0001; */
		lix = dx;
		liy = dy;
	}

	/**
	* Given interpolation points at ( {@link xVals}[ 0 ] , {@link yVals}[ 0 ] ),
	* ( {@link xVals}[ 1 ] , {@link yVals}[ 1 ] ) and ( {@link xVals}[ 2 ] , {@link yVals}[ 2 ] ) calculates the 
	* vector ( {@link mix} , {@link miy} ) as a Bessel tangeant.
	* @param u The u parameter at which to calculate the tangeant.
	*/
	protected static void calcBessel(double u) {
		double u0;
		double u1;
		double u2;
		double t0;
		double t1;
		double t2;

		u0 = 0;
		u1 = (double) deltaU[0];
		u2 = u1 + (double) deltaU[1];

		t0 = ((u - u1) + (u - u2)) / ((u0 - u1) * (u0 - u2));
		t1 = ((u - u0) + (u - u2)) / ((u1 - u0) * (u1 - u2));
		t2 = ((u - u0) + (u - u1)) / ((u2 - u0) * (u2 - u1));

		mix = xVals[0] * t0 + xVals[1] * t1 + xVals[2] * t2;
		miy = yVals[0] * t0 + yVals[1] * t1 + yVals[2] * t2;
	}

	/**
	 * Refreshes the deltaU values associated with the curve points
	 * in xVals and yVals.  There is some redundant calculation of square
	 * roots in this version of the code, but it doesn't seem to be hurting
	 * performance.
	 * @param chordParam Whether to use chord parametrization as opposed to
	 * uniform parametrization.
	 */
	protected static void refreshDeltaU(boolean chordParam) {
		if (chordParam) {
			deltaU[0] =
				Math.sqrt(
					(xVals[1] - xVals[0]) * (xVals[1] - xVals[0])
						+ (yVals[1] - yVals[0]) * (yVals[1] - yVals[0]));

			deltaU[1] =
				Math.sqrt(
					(xVals[2] - xVals[1]) * (xVals[2] - xVals[1])
						+ (yVals[2] - yVals[1]) * (yVals[2] - yVals[1]));
		} else {
			deltaU[0] = 10;
			deltaU[1] = 10;
		}
		
		if( deltaU[ 0 ] < 0.0001 )
			deltaU[ 0 ] = 0.0001;
			
		if( deltaU[ 1 ] < 0.0001 )
			deltaU[ 1 ] = 0.0001;
	}

	/**
	 * Gets the interpolation point associated with a particular index.  If
	 * no such point exists, returns a guess.
	 * @param pts The vector from which to grab the points.
	 * @param vectIndex The retrieval index.
	 * @return The point at the specified index.
	 */
	protected static Point2D.Double getVirtualPoint(
		Vector<SerPoint2D> pts,
		int vectIndex) {
		int sz = pts.size();

		if (sz < 2) {
			return ( pts.elementAt(0) );
		}

		Point2D.Double ret = null;

		if (vectIndex < 0) {
			Point2D.Double pta = pts.elementAt(0);
			Point2D.Double ptb = pts.elementAt(1);
			double xv = pta.getX() - (ptb.getX() - pta.getX());
			double yv = pta.getY() - (ptb.getY() - pta.getY());
			ret = new Point2D.Double(xv, yv);
		} else {
			if (vectIndex >= sz) {
				Point2D.Double pta = pts.elementAt(sz - 2);
				Point2D.Double ptb = pts.elementAt(sz - 1);
				double xv = pta.getX() + (ptb.getX() - pta.getX());
				double yv = pta.getY() + (ptb.getY() - pta.getY());
				ret = new Point2D.Double(xv, yv);
			} else {
				ret = pts.elementAt(vectIndex);
			}
		}

		return (ret);
	}

	/**
	 * Refresh the xVals and yVals arrays for a particular index.
	 * @param pts The vector from which to get the values.
	 * @param vectIndex The index for retrieval from the vector.
	 * @param valIndex The destination index in xVals / yVals.
	 */
	protected static void refreshXY(Vector<SerPoint2D> pts, int vectIndex, int valIndex) {
		Point2D.Double pt = getVirtualPoint(pts, vectIndex);
		xVals[valIndex] = pt.getX();
		yVals[valIndex] = pt.getY();
	}

	/**
	 * Refreshes xVals, yVals, and deltaU.
	 * @param pts The vector from which to retrieve the values.
	 * @param vectIndex The index at which to retrieve from the vector.
	 * @param chordP Whether to use chord parametrization as opposed to uniform
	 * parametrization.
	 */
	protected static void refreshVals(
		Vector<SerPoint2D> pts,
		int vectIndex,
		boolean chordP) {
		refreshXY(pts, vectIndex - 1, 0);
		refreshXY(pts, vectIndex, 1);
		refreshXY(pts, vectIndex + 1, 2);
		refreshDeltaU(chordP);
	}

	/**
	 * Sets a particular Bezier point if it exists.
	 * @param bezVect The vector of Bezier points.
	 * @param bezIndex The Bezier index to set.
	 * @param pt The point to set at the index.
	 * @param maxBez The maximum number of Bezier points.
	 */
	protected static void setBezierPoint(
		Vector<Point2D.Double> bezVect,
		int bezIndex,
		Point2D.Double pt,
		int maxBez) {
		if ((bezIndex >= 0) && (bezIndex < maxBez)) {
			bezVect.setElementAt(pt, bezIndex);
		}
	}

	/**
	 * Sets a particular Bezier point if it exists.
	 * @param bezVect The vector of Bezier points.
	 * @param bezIndex The Bezier index to set.
	 * @param x The X-value to set at the index.
	 * @param y The Y-Value to set at the index.
	 * @param maxBez The maximum number of Bezier points.
	 */
	protected static void setBezierPoint(
		Vector<Point2D.Double> bezVect,
		int bezIndex,
		double x,
		double y,
		int maxBez) {
		Point2D.Double pt = new SerPoint2D(x, y);
		setBezierPoint(bezVect, bezIndex, pt, maxBez);
	}

	/**
	 * Calculates the maximum number of Bezier points.
	 * @param maxPt The maximum number of interpolation points.
	 * @return The maximum number of Bezier points.
	 */
	protected static int calcMaxBez(int maxPt) {
		return (3 * (maxPt - 1) + 1);
	}

	/**
	 * Calculates the necessary Bezier points given an interpolation point change.
	 * @param prevBez The Bezier points.
	 * @param bezIndex The Bezier index of the interpolation point that has changed.
	 * @param bezMode Bezier mode.  BezMode = 1 means FMILL tangeants.  BezMode = 2 means 
	 * 		Bessel tangeants.
	 * @param chordP Whether to use chord parametrization.  False means uniform parametrization.
	 * @param maxBez The maximum number of Bezier points.
	 */
	protected static void calcBez(
		Vector<Point2D.Double> prevBez,
		int bezIndex,
		int bezMode,
		boolean chordP,
		int maxBez) {
		double px = xVals[1];
		double py = yVals[1];

		if (bezMode == 2) {
			calcBessel((double) (deltaU[0]));
			setBezierPoint(
				prevBez,
				bezIndex - 1,
				px - mix * deltaU[0] / 3.0,
				py - miy * deltaU[0] / 3.0,
				maxBez);
			setBezierPoint(prevBez, bezIndex, px, py, maxBez);
			setBezierPoint(
				prevBez,
				bezIndex + 1,
				px + mix * deltaU[1] / 3.0,
				py + miy * deltaU[1] / 3.0,
				maxBez);
		} else {
			double denom = 3.0 * (deltaU[0] + deltaU[1]);
			calcFMILL();
			setBezierPoint(
				prevBez,
				bezIndex - 1,
				px - deltaU[0] / denom * lix,
				py - deltaU[0] / denom * liy,
				maxBez);
			setBezierPoint(prevBez, bezIndex, px, py, maxBez);
			setBezierPoint(
				prevBez,
				bezIndex + 1,
				px + deltaU[1] / denom * lix,
				py + deltaU[1] / denom * liy,
				maxBez);
		}
	}

	/**
	 * Applies end condition slipe to the last point.
	 * @param bez The Bezier points.
	 * @param maxBez The maximum number of Bezier points.
	 */
	protected static void applyFinalEndConditionSlope(Vector<Point2D.Double> bez, int maxBez) {
		int index = maxBez - 2;
		calcBessel((double) (deltaU[0] + deltaU[1]));
		double nx = xVals[2];
		double ny = yVals[2];
		setBezierPoint(
			bez,
			index,
			nx - mix * deltaU[1] / 3.0,
			ny - miy * deltaU[1] / 3.0,
			maxBez);
	}

	/**
	 * Applies end condition slope to the initial point.
	 * @param bez The Bezier points.
	 * @param maxBez The maximum number of Bezier points.
	 */
	protected static void applyInitialEndConditionSlope(
		Vector<Point2D.Double> bez,
		int maxBez) {
		int index = 1;
		calcBessel(0.0);
		double nx = xVals[0];
		double ny = yVals[0];
		setBezierPoint(
			bez,
			index,
			nx + mix * deltaU[0] / 3.0,
			ny + miy * deltaU[0] / 3.0,
			maxBez);
	}

	/**
	 * Updates Bezier points to handle an interpolation point change.
	 * @param pts The interpolation points.
	 * @param index The index of the changed interpolation point.
	 * @param bez The Bezier points.
	 * @param bezMode The Bezier mode.
	 * @param chordP Whether to use chord parametrization.
	 */
	protected static void processPointUpdate(
		Vector<SerPoint2D> pts,
		int index,
		Vector<Point2D.Double> bez,
		int bezMode,
		boolean chordP) {
		int sz = pts.size();
		int maxBez = calcMaxBez(pts.size());
		if (bez.size() != maxBez) {
			bez.setSize(maxBez);
		}
		refreshVals(pts, index - 1, chordP);
		calcBez(bez, 3 * (index - 1), bezMode, chordP, maxBez);

		refreshVals(pts, index, chordP);
		calcBez(bez, 3 * index, bezMode, chordP, maxBez);

		refreshVals(pts, index + 1, chordP);
		calcBez(bez, 3 * (index + 1), bezMode, chordP, maxBez);

		if (Math.abs(index - (sz - 1)) < 3) {
			refreshVals(pts, sz - 2, chordP);
			applyFinalEndConditionSlope(bez, maxBez);
		}

		if (Math.abs(index - 0) < 3) {
			refreshVals(pts, 1, chordP);
			applyInitialEndConditionSlope(bez, maxBez);
		}
	}


	/**
	 * Handles the addition of an interpolation point.
	 * @param pts The interpolation points.
	 * @param bez The Bezier points.
	 * @param p The new interpolation point.
	 * @param bezMode The Bezier mode.
	 * @param chordP Whether to use chord parametrization.
	 */
	public static void processAddPoint(
		Vector<SerPoint2D> pts,
		Vector<Point2D.Double> bez,
		SerPoint2D p,
		int bezMode,
		boolean chordP) {
		int index = pts.size();
		pts.add(p);
		processPointUpdate(pts, index, bez, bezMode, chordP);
	}

	/**
	 * Handles the alteration of an interpolation point.
	 * @param pts The interpolation points.
	 * @param bez The Bezier points.
	 * @param index The index in the interpolation vector
	 * 		at which to set the new point.
	 * @param p The new interpolation point.
	 * @param bezMode The Bezier mode.
	 * @param chordP Whether to use chord parametrization.
	 */
	public static void processChangePoint(
		Vector<SerPoint2D> pts,
		Vector<Point2D.Double> bez,
		int index,
		SerPoint2D p,
		int bezMode,
		boolean chordP) {
		pts.setElementAt(p, index);
		processPointUpdate(pts, index, bez, bezMode, chordP);
	}

	/**
	* Constructs a Java-2D GeneralPath object from a vector of Bezier points for
	* a continuous piecewise cubic curve.
	* @param prevBez The vector of Bezier points.
	*/
	public static GeneralPath createGeneralPathFromVector(Vector<Point2D.Double> prevBez) {
		int elem = prevBez.size();
		GeneralPath temp = new GeneralPath(GeneralPath.WIND_EVEN_ODD, elem);

		if (elem > 0) {
			Point2D.Double first = prevBez.elementAt(0);
			temp.moveTo((float) (first.getX()), (float) (first.getY()));
			int cnt = 1;
			for (cnt = 1;(cnt + 2) < elem; cnt = cnt + 3) {
				Point2D.Double b1 = prevBez.elementAt(cnt);
				Point2D.Double b2 =
					prevBez.elementAt(cnt + 1);
				Point2D.Double b3 =
					prevBez.elementAt(cnt + 2);
				temp.curveTo(
					(float) (b1.getX()),
					(float) (b1.getY()),
					(float) (b2.getX()),
					(float) (b2.getY()),
					(float) (b3.getX()),
					(float) (b3.getY()));
			}
		}

		return (temp);
	}

	
}

