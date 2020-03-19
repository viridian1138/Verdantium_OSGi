package verdantium.standard;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import meta.DataFormatException;
import meta.Meta;
import meta.VersionBuffer;

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
* A basic class for a renderable object used by {@link DrawApp}.  The renderable
* object supports drawing and serialization.  More methods will be added later.
* <P>
* @author Thorn Green
*/
public abstract class DrawingObject {

	/**
	* Renders the object.
	* @param thePort DrawApp providing the context supporting the rendering.
	* @param g The graphics context in which to render.
	* @param toolMode The application mode in which to perform the rendering.
	*/
	public abstract void draw(DrawApp thePort, Graphics2D g, int toolMode);

	/**
	* Renders the object's tools.
	* @param thePort DrawApp providing the context supporting the rendering.
	* @param g The graphics context in which to render.
	* @param toolMode The application mode in which to perform the rendering.
	*/
	public abstract void drawTools(DrawApp thePort, Graphics2D g, int toolMode);

	/**
	 * Handles a mouse-drag operation on the object.
	 * @param thePort DrawApp providing the context supporting the mouse-drag.
	 * @param in Description of the display control being dragged.
	 * @param toolMode The application mode in which to perform the mouse-drag.
	 * @param inPt The current mouse-drag location.
	 */
	public abstract void dragDisplayControl(
		DrawApp thePort,
		ClickRec in,
		int toolMode,
		Point2D.Double inPt);
        
	/**
	 * Populates the current state of the DrawingObject into an undoable representation.
	 * @param mil The milieu in which to populate the undoable representation.
	 * @param out The undoable representation of the DrawingObject's state to be populated.
	 * @return The milieu resulting from the population of the undoable representation.
	 */
        public abstract jundo.runtime.ExtMilieuRef upBuild( jundo.runtime.ExtMilieuRef mil ,
                pdx_DrawingObject_pdx_ObjectRef out );
        
        /**
         * Creates an undoable representation of the DrawingObject's state.
         * @param mil The milieu in which to build the undoable representation of the DrawingObject's state.
         * @return The undoable representation of the DrawingObject's state.
         */
        public abstract pdx_DrawingObject_pdx_PairRef upCreate( jundo.runtime.ExtMilieuRef mil );

}


