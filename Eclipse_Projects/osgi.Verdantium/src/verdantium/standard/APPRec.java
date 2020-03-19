package verdantium.standard;

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
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Needed a mechanism for dragging drawn shapes.                        | Took ClickRec/APPRec from GeoFrame into DrawApp.
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

/* This is strictly a record type.  Therefore object common law does not apply. */

/**
* A record representing a mouse click in the drawing frame that belongs to a
* particular drawn object.
*
* @author Thorn Green
*/
public class APPRec extends ClickRec {
	
	/**
	 * Value indicating what kind of operation is to be performed by the mouse event.
	 */
	protected int value;
	
	/**
	 * The X-Coordinate of the mouse click.
	 */
	protected double xcoord;
	
	/**
	 * The Y-Coordinate of the mouse click.
	 */
	protected double ycoord;

	/**
	 * Gets the value indicating what kind of operation is to be performed by the mouse event.
	 * @return Value indicating what kind of operation is to be performed by the mouse event.
	 */
	public int getValue() {
		return (value);
	}

	/**
	 * Sets the value indicating what kind of operation is to be performed by the mouse event.
	 * @param in Value indicating what kind of operation is to be performed by the mouse event.
	 */
	public void setValue(int in) {
		value = in;
	}

	/**
	 * Gets the X-Coordinate of the mouse click.
	 * @return The X-Coordinate of the mouse click.
	 */
	public double getXCoord() {
		return (xcoord);
	}

	/**
	 * Sets the X-Coordinate of the mouse click.
	 * @param in The X-Coordinate of the mouse click.
	 */
	public void setXCoord(double in) {
		xcoord = in;
	}

	/**
	 * Gets the Y-Coordinate of the mouse click.
	 * @return  The Y-Coordinate of the mouse click.
	 */
	public double getYCoord() {
		return (ycoord);
	}

	/**
	 * Sets the Y-Coordinate of the mouse click.
	 * @param in  The Y-Coordinate of the mouse click.
	 */
	public void setYCoord(double in) {
		ycoord = in;
	}

	
};

