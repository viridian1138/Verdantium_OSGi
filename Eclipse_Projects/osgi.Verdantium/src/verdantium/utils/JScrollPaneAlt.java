
package verdantium.utils;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JViewport;

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
* A subclass of JScrollPane implemented so that extending the scroll pane
* beyond the preferred size of the scrolled component can cause the scrolled component
* to be expanded to fill the area of the scroll pane despite the fact that this will
* make the component larger than its preferred size.  This is individually controllable
* for horizontal and vertical directions through the {@link ScrollableAlt} interface.
* 
* @author Thorn Green
*/
public class JScrollPaneAlt extends JScrollPane {

	/**
	* Constructs a scrolling pane with a scrolled component, and a horizontal
	* and vertical policy.  The policies are the same as for JScrollPane.
	* @param view The scrolled component.
	* @param vsbPolicy The vertical scroll policy.
	* @param hsbPolicy The horizontal scroll policy.
	*/
	public JScrollPaneAlt(Component view, int vsbPolicy, int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);
	}

	/**
	* Constructs a scrolling pane with a scrolled component, and 
	* default policies.  The policies are the same as for JScrollPane.
	* @param view The scrolled component.
	*/
	public JScrollPaneAlt(Component view) {
		super(view);
	}

	/**
	* Constructs a scrolling pane with a horizontal
	* and vertical policy.  The policies are the same as for JScrollPane.
	* @param vsbPolicy The vertical scroll policy.
	* @param hsbPolicy The horizontal scroll policy.
	*/
	public JScrollPaneAlt(int vsbPolicy, int hsbPolicy) {
		super(vsbPolicy, hsbPolicy);
	}

	/**
	* Constructs a scrolling pane with
	* default policies.  The policies are the same as for JScrollPane.
	*/
	public JScrollPaneAlt() {
		super();
	}

	/**
	* Creates the viewport for the scrolling pane.
	* @return The viewport for the scrolling pane.
	*/
	protected JViewport createViewport() {
		return (new JViewportAlt());
	}

}

