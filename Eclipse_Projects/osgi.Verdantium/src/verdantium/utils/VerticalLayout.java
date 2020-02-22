package verdantium.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;

import meta.Meta;

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
*    | 07/28/2002            | Thorn Green (viridian_1138@yahoo.com)           | Found a bug in VerticalLayout.                                       | Fixed the bug.
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
* This is a helper class that provides a data definition for a node
* stored by {@link VerticalLayout} encapsulating one component.  Do
* not access directly unless making modifications to {@link VerticalLayout}.
* 
* @author Thorn Green
*/
class LayoutNode extends Meta {

	/**
	* Gets whether the component is allowed to vertically stretch.
	* @return Whether the component is allowed to vertically stretch.
	*/
	public final boolean getStretch() {
		return (stretch);
	}

	/**
	* Sets whether the component is allowed to vertically stretch.
	* @param in Whether the component is allowed to vertically stretch.
	*/
	public final void setStretch(boolean in) {
		stretch = in;
	}

	/**
	* Gets the vertical minimum size of the component.
	* @return The vertical minimum size of the component.
	*/
	public final int getMinSz() {
		return (min_sz);
	}

	/**
	* Sets the vertical minimum size of the component.
	* @param in The vertical minimum size of the component.
	*/
	public final void setMinSz(int in) {
		min_sz = in;
	}

	/**
	* Gets the vertical preferred size of the component.
	* @return The vertical preferred size of the component.
	*/
	public final int getMaxSz() {
		return (max_sz);
	}

	/**
	* Sets the vertical preferred size of the component.
	* @param in The vertical preferred size of the component.
	*/
	public final void setMaxSz(int in) {
		max_sz = in;
	}

	/**
	* Gets the component.
	* @return The component.
	*/
	public final Component getComp() {
		return (comp);
	}

	/**
	* Sets the component.
	* @param in The component.
	*/
	public final void setComp(Component in) {
		comp = in;
	}

	/**
	 * The component.
	 */
	private Component comp;
	
	/**
	 * The vertical minimum size of the component.
	 */
	private transient int min_sz;
	
	/**
	 * The vertical preferred size of the component.
	 */
	private transient int max_sz;
	
	/**
	 * Whether the component is allowed to vertically stretch.
	 */
	private boolean stretch = false;

	@Override
	public void wake() {};
}



/**
* A layout manager that vertically stacks its sub-components
* in a relatively visually pleasing manner.
* 
* @author Thorn Green
*/
public class VerticalLayout extends java.lang.Object implements java.awt.LayoutManager {
	
	/**
	 * The largest minimum size on the X-Axis.
	 */
	private transient int minX = 0;
	
	/**
	 * The summed gapped minimum height of all components in the layout.
	 */
	private transient int minY = 0;
	
	/**
	 * The largest maximum size on the X-Axis.
	 */
	private transient int maxX = 0;
	
	/**
	 * The summed gapped maximum height of all components in the layout.
	 */
	private transient int maxY = 0;
	
	/**
	 * The summed minimum height of all stretch components in the layout.
	 */
	private transient int summin = 0;
	
	/**
	 * The summed maximum height of all stretch components in the layout.
	 */
	private transient int summax = 0;

	/**
	 * The summed minimum height of all components in the layout.
	 */
	private transient int full_summin = 0;
	
	/**
	 * The summed maximum height of all components in the layout.
	 */
	private transient int full_summax = 0;
	
	/**
	 * The vertical gap between components.
	 */
	private int ygap;
	
	/**
	 * Whether the "stretch" keyword is required
	 * to have a component vertically stretch.
	 */
	private boolean useStretch = false;
	
	/**
	 * The list of participating components.
	 */
	private final ArrayList<LayoutNode> compList = new ArrayList<LayoutNode>();

	
	/**
	* Updates global statistics related to the components to be laid out.
	*/
	protected void updateLayoutStats() {
		minX = 0;
		minY = 0;
		maxX = 0;
		maxY = 0;
		summin = 0;
		summax = 0;
		full_summin = 0;
		full_summax = 0;

			for( final LayoutNode myNode : compList ) {
				Component myC = myNode.getComp();
				Dimension minD = myC.getMinimumSize();
				Dimension maxD = getRealPreferredSize(myC);
				minD = myC.getMinimumSize();
				maxD = getRealPreferredSize(myC);
				if (minD.width > minX)
					minX = minD.width;
				if (maxD.width > maxX)
					maxX = maxD.width;
				minY = minY + minD.height + ygap;
				maxY = maxY + maxD.height + ygap;
				if (!useStretch || myNode.getStretch())
					summin = summin + minD.height;
				if (!useStretch || myNode.getStretch())
					summax = summax + maxD.height;
				full_summin = full_summin + minD.height;
				full_summax = full_summax + maxD.height;
				myNode.setMinSz(minD.height);
				myNode.setMaxSz(maxD.height);
			}

			if( compList.size() >= 1  )
			{
				minY = minY - ygap;
				maxY = maxY - ygap;
			}
	}

	/**
	* Creates a vertical layout manager with a particular vertical gap,
	* where the stretch keyword is not required.
	* @param inYgap The vertical gap between components.
	*/
	public VerticalLayout(int inYgap) {
		ygap = inYgap;
	};

	/**
	* Creates a vertical layout manager with a particular vertical gap.  The
	* boolean parameter indicates whether the "stretch" keyword is required
	* to have a component vertically stretch.
	* @param inYgap The vertical gap between components.
	* @param usingStretch Whether the "stretch" keyword is required to have a component vertically stretch.
	*/
	public VerticalLayout(int inYgap, boolean usingStretch) {
		ygap = inYgap;
		useStretch = usingStretch;
	};

	/**
	* Creates a vertical layout manager with a vertical gap of 5 pixels,
	* where the stretch keyword is not required.
	*/
	public VerticalLayout() {
		ygap = 5;
	};

	/**
	* Creates a vertical layout manager with a vertical gap of five pixels,
	* where the boolean parameter indicates whether the stretch keyword is
	* required.
	* @param usingStretch Whether the "stretch" keyword is required to have a component vertically stretch.
	*/
	public VerticalLayout(boolean UsingStretch) {
		ygap = 5;
		useStretch = UsingStretch;
	};

	/**
	* Adds a component to the layout.
	* @param name The name of the mode under which to add the component.
	* @param comp The component to add.
	*/
	public void addLayoutComponent(String name, Component comp) {
		LayoutNode myNode = new LayoutNode();
		myNode.setComp(comp);
		if (name.equals("stretch"))
			myNode.setStretch(true);

		compList.add( myNode );
	};

	/**
	* Lays out components for a parent smaller than the minimum size 
	* of the set of components to be handled.
	* @param parent The size of the parent.
	*/
	protected void undefStdLayoutUnder(Dimension parent) {
		int delta = parent.height - minY;
		double ystrt = 0.0;

			for( final LayoutNode myNode : compList ) {
				Component myC = myNode.getComp();
				double ysz = myNode.getMinSz();

				double ratio = ysz / ((double) full_summin);
				myC.setBounds(0, (int) ystrt, parent.width, (int) (ysz + delta * ratio));
				ystrt = ystrt + ysz + delta * ratio;

				ystrt = ystrt + ygap;
			}

	}

	/**
	* Lays out components for a parent between the preferred size 
	* and the minimum size of the set of components to be handled.
	* @param parent The size of the parent.
	*/
	protected void undefStdLayoutBetween(Dimension parent) {
		int delta = parent.height - minY;
		double ystrt = 0.0;

			for( final LayoutNode myNode : compList ) {
				Component myC = myNode.getComp();
				double ysz = myNode.getMinSz();
				double yszm = myNode.getMaxSz();

				double ratio = (yszm - ysz) / ((double) (full_summax - full_summin));
				myC.setBounds(0, (int) ystrt, parent.width, (int) (ysz + delta * ratio));
				ystrt = ystrt + ysz + delta * ratio;

				ystrt = ystrt + ygap;
			}

	}

	/**
	* Lays out components for a parent larger than the preferred size 
	* of the set of components to be handled.
	* @param parent The size of the parent.
	*/
	protected void undefStdLayoutOver(Dimension parent) {
		int delta = parent.height - maxY;
		double ystrt = 0.0;

			for( final LayoutNode myNode : compList ) {
				Component myC = myNode.getComp();
				double ysz = myNode.getMaxSz();

				if (!useStretch || myNode.getStretch()) {
					double ratio = ysz / ((double) summax);
					myC.setBounds(0, (int) ystrt, parent.width, (int) (ysz + delta * ratio));
					ystrt = ystrt + ysz + delta * ratio;
				}
				else {
					myC.setBounds(0, (int) ystrt, parent.width, (int) (ysz));
					ystrt = ystrt + ysz;
				}

				ystrt = ystrt + ygap;
			}

	}

	/**
	* Lays out the components given the dimension of the parent.
	* @param parent The size of the parent.
	*/
	protected void undefStdLayout(Dimension parent) {
		int delta = parent.height - minY;
		int delta2 = parent.height - maxY;

		if (delta2 >= 0) {
			undefStdLayoutOver(parent);
		}

		if (delta <= 0) {
			undefStdLayoutUnder(parent);
		}

		if ((delta > 0) && (delta2 < 0)) {
			undefStdLayoutBetween(parent);
		}

	}

	/**
	* Gets the max of the minimum and preferred sizes.
	* @param comp The component from which to retrieve sizes.
	* @return The max of the minimum and preferred sizes.
	*/
	private Dimension getRealPreferredSize(Component in) {
		Dimension pref = in.getPreferredSize();
		Dimension min = in.getMinimumSize();
		Dimension ret = pref;

		if ((pref.width < min.width) || (pref.height < min.height)) {
			ret = new Dimension(Math.max(pref.width, min.width), Math.max(pref.height, min.height));
		}

		return (ret);
	}

	/**
	* Lays out the container.
	* @param parent The parent container.
	*/
	public void layoutContainer(Container parent) {
		updateLayoutStats();
		Dimension d = parent.getSize();
		undefStdLayout(d);
	};

	/**
	* Returns the minimum size of the components being laid out.
	* @param parent The parent container.
	*/
	public Dimension minimumLayoutSize(Container parent) {
		updateLayoutStats();
		return (new Dimension(minX, minY));
	};

	/**
	* Returns the preferred size of the components being laid out.
	* @param parent The parent container.
	*/
	public Dimension preferredLayoutSize(Container parent) {
		updateLayoutStats();
		return (new Dimension(maxX, maxY));
	};

	/**
	* Removes a component from the layout.  Not supported.
	* @param parent The parent container.
	*/
	public void removeLayoutComponent(Component comp) {
		throw (new RuntimeException("removeLayoutComponent( not supported in VerticalLayout."));
	};

	
	
}


