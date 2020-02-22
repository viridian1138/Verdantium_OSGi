package verdantium.kluges;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

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
* An object that draws an infinite line on the display in Java-2D.
* 
* @author Thorn Green
*/
public class InfiniteLine extends Object {
	
	/**
	* Draws an infinite line.
	* @param g The graphics context in which to draw the line.
	*/
	public void draw(Graphics2D g) {
		Shape Clip = g.getClip();
		Rectangle2D Bounds = Clip.getBounds2D();
		double maxx = Bounds.getMaxX();
		double maxy = Bounds.getMaxY();
		double minx = Bounds.getMinX();
		double miny = Bounds.getMinY();
		double DelX = maxx - minx;
		double DelY = maxy - miny;
		double tmp = 0.0;

		tmp = Math.abs(px1 - minx);
		if (tmp > DelX)
			DelX = tmp;

		tmp = Math.abs(px1 - maxx);
		if (tmp > DelX)
			DelX = tmp;

		tmp = Math.abs(px2 - minx);
		if (tmp > DelX)
			DelX = tmp;

		tmp = Math.abs(px2 - maxx);
		if (tmp > DelX)
			DelX = tmp;

		tmp = Math.abs(py1 - miny);
		if (tmp > DelY)
			DelY = tmp;

		tmp = Math.abs(py1 - maxy);
		if (tmp > DelY)
			DelY = tmp;

		tmp = Math.abs(py2 - miny);
		if (tmp > DelY)
			DelY = tmp;

		tmp = Math.abs(py2 - maxy);
		if (tmp > DelY)
			DelY = tmp;

		double Del = 2.0 * (DelX + DelY);
		double Manlen = Math.abs(px2 - px1) + Math.abs(py2 - py1);
		if (Manlen < 0.0001)
			Manlen = 5;

		double x1 = px1;
		double y1 = py1;
		double x2 = px2;
		double y2 = py2;

		if (extent1 == INFINITE_EXTENT) {
			x1 += (px1 - px2) * Del / Manlen;
			y1 += (py1 - py2) * Del / Manlen;
		}

		if (extent2 == INFINITE_EXTENT) {
			x2 += (px2 - px1) * Del / Manlen;
			y2 += (py2 - py1) * Del / Manlen;
		}

		myLine.setLine(x1, y1, x2, y2);
		g.draw(myLine);
	}

	/**
	* Constructs the infinite line.
	*/
	public InfiniteLine() {}

	/**
	* Constructs an infinite line with endpoint extents <code>e1</code>
	* and <code>e2</code> for the first and second endpoints respectively.
	* Each extent can either be a <code>FINITE_EXTENT</code>, which means
	* that rendering ends at that endpoint, or an <code>INFINITE_EXTENT</code>,
	* which means that rendering continues through the endpoint to infinity.
	* For instance, both extents finite creates a line segment, one extent
	* finite and one extent infinite creates a ray, and both extents infinite
	* creates an infinite line.
	* @param e1 The extent to which to draw through the specified first point on the line.
	* @param e2 The extent to which to draw through the specified second point on the line.
	*/
	public InfiniteLine(int e1, int e2) {
		extent1 = e1;
		extent2 = e2;
	}

	/**
	* Sets the endpoint positions of the object.
	* @param x1 The X-Axis position of the first point on the line.
	* @param y1 The Y-Axis position of the first point on the line.
	* @param x2 The X-Axis position of the second point on the line.
	* @param y2 The Y-Axis position of the second point on the line.
	*/
	public void setLine(double x1, double y1, double x2, double y2) {
		px1 = x1;
		py1 = y1;
		px2 = x2;
		py2 = y2;
	}

	/**
	* A finite extent.
	*/
	public final static int FINITE_EXTENT = 0;
	
	/**
	* An infinite extent.  Use this to make one side of the rendered
	* version of the object infinitely long.
	*/
	public final static int INFINITE_EXTENT = 1;

	/**
	 * The extent to which to draw through the specified first point on the line.
	 */
	private int extent1 = INFINITE_EXTENT;
	
	/**
	 * The extend to which to draw through the specified second point on the line.
	 */
	private int extent2 = INFINITE_EXTENT;
	
	/**
	 * 
	 */
	private double px1 = 0.0;
	
	/**
	 * The Y-Axis position of the first point on the line.
	 */
	private double py1 = 0.0;
	
	/**
	 * The X-Axis position of the second point on the line.
	 */
	private double px2 = 0.0;
	
	/**
	 * The Y-Axis position of the second point on the line.
	 */
	private double py2 = 0.0;
	
	/**
	 * The Java-2D line used for rendering.
	 */
	private final Line2D.Double myLine = new Line2D.Double();
	
	
}


