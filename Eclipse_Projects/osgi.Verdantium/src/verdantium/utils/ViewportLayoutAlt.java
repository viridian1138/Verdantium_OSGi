package verdantium.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JViewport;
import javax.swing.ViewportLayout;

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
* A layout manager class that helps {@link JScrollPaneAlt} to
* implement its functionality.  Classes other than {@link JScrollPaneAlt}
* and {@link JViewportAlt} usually should not access this class directly.
* This file uses part of Sun's Swing source code.  Because of the amount of
* code used, and the purpose for which it is used, the author considers
* this to be fair use.
* 
* @author Thorn Green
*/
class ViewportLayoutAlt extends ViewportLayout {

	@Override
	public void layoutContainer(Container parent) {
		JViewport vp = (JViewport) parent;
		Component view = vp.getView();
		ScrollableAlt scrollableView = null;

		if (view == null) {
			return;
		}
		else if (view instanceof ScrollableAlt) {
			scrollableView = (ScrollableAlt) view;
		}

		/* All of the dimensions below are in view coordinates, except
		 * vpSize which we're converting.
		 */

		Insets insets = vp.getInsets();
		Dimension viewPrefSize = view.getPreferredSize();
		Dimension vpSize = vp.getSize();
		Dimension extentSize = vp.toViewCoordinates(vpSize);
		Dimension viewSize = new Dimension(viewPrefSize);

		if (scrollableView != null) {
			if ((viewSize.width < vpSize.width) && (scrollableView.getAltTracksWidth())) {
				viewSize.width = vpSize.width;
			}
			if ((viewSize.height < vpSize.height) && (scrollableView.getAltTracksHeight())) {
				viewSize.height = vpSize.height;
			}
		}

		Point viewPosition = vp.getViewPosition();

		/* If the new viewport size would leave empty space to the
		 * right of the view, right justify the view or left justify
		 * the view when the width of the view is smaller than the
		 * container.
		 */
		if ((viewPosition.x + extentSize.width) > viewSize.width) {
			viewPosition.x = Math.max(0, viewSize.width - extentSize.width);
		}

		/* If the new viewport size would leave empty space below the
		 * view, bottom justify the view or top justify the view when
		 * the height of the view is smaller than the container.
		 */
		if ((viewPosition.y + extentSize.height) > viewSize.height) {
			viewPosition.y = Math.max(0, viewSize.height - extentSize.height);
		}

		/* If we haven't been advised about how the viewports size 
		 * should change wrt to the viewport, i.e. if the view isn't
		 * an instance of Scrollable, then adjust the views size as follows.
		 * 
		 * If the orgin of the view is showing and the viewport is
		 * bigger than the views preferred size, then make the view
		 * the same size as the viewport.
		 */
		if (scrollableView == null) {
			if ((viewPosition.x == 0) && (vpSize.width > viewPrefSize.width)) {
				viewSize.width = vpSize.width;
			}
			if ((viewPosition.y == 0) && (vpSize.height > viewPrefSize.height)) {
				viewSize.height = vpSize.height;
			}
		}
		vp.setViewPosition(viewPosition);
		vp.setViewSize(viewSize);
	}

	
}

