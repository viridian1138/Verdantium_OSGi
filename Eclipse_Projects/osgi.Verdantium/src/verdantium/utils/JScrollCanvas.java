
package verdantium.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;

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
* A helper class used by {@link JScrollCanvas}.  Do not use directly unless
* modifications are being made to {@link JScrollCanvas}.
* 
* @author Thorn Green
*/
class ScrollCanvasPane extends JScrollPaneAlt {
	
	transient private JScrollCanvas jsc = null;

	/**
	* Constructs a scroll canvas pane with an enclosed canvas, and scroll policies.
	* The scroll policies are the same as those for JScrollPane.
	*/
	public ScrollCanvasPane(int vsbPolicy, int hsbPolicy, JScrollCanvas in) {
		super(vsbPolicy, hsbPolicy);
		jsc = in;
		/* ( (JViewport)( getViewport() ) ).setBackingStoreEnabled( true ); */
	}

	@Override
	public void setPreferredSize(Dimension preferredSize) {
		jsc.setViewPreferredSize(preferredSize);
	}

	/**
	* Sets the preferred size of the scrolling pane.
	* @param preferredSize The preferred size of the scrolling pane.
	*/
	public void setViewPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(preferredSize);
	}

	@Override
	public void setMaximumSize(Dimension preferredSize) {
		jsc.setViewMaximumSize(preferredSize);
	}

	/**
	* Sets the maximum size of the scrolling pane.
	* @param maximumSize The maximum size of the scrolling pane.
	*/
	public void setViewMaximumSize(Dimension maximumSize) {
		super.setMaximumSize(maximumSize);
	}

	@Override
	public void setMinimumSize(Dimension preferredSize) {
		jsc.setViewMinimumSize(preferredSize);
	}

	/**
	* Sets the minimum size of the scrolling pane.
	* @param minimumSize The minimum size of the scrolling pane.
	*/
	public void setViewMinimumSize(Dimension minimumSize) {
		super.setMinimumSize(minimumSize);
	}

	@Override
	public Dimension getPreferredSize() {
		return (jsc.getViewPreferredSize());
	}

	/**
	* Gets the preferred size of the scrolling pane.
	* @return The preferred size of the scrolling pane.
	*/
	public Dimension getViewPreferredSize() {
		return (super.getPreferredSize());
	}

	/**
	* Gets the minimum size of the canvas.
	* @return The minimum size of the canvas.
	*/
	public Dimension getMinimumSize() {
		return (jsc.getViewMinimumSize());
	}

	/**
	* Gets the minimum size of the scrolling pane.
	* @return The minimum size of the scrolling pane.
	*/
	public Dimension getViewMinimumSize() {
		return (super.getMinimumSize());
	}

	/**
	* Gets the maximum size of the canvas.
	* @return The maximum size of the canvas.
	*/
	public Dimension getMaximumSize() {
		return (jsc.getViewMaximumSize());
	}

	/**
	* Gets the maximum size of the scrolling pane.
	* @return The maximum size of the scrolling pane.
	*/
	public Dimension getViewMaximumSize() {
		return (super.getMaximumSize());
	}

}

/**
* A convenience class that makes it easier to implement scrolling panes
* that do not contain embedded components.  This can be done by subclassing
* JScrollCanvas and then using the getView() method to get the top-level
* scrolling pane.
* 
* @author Thorn Green
*/
public class JScrollCanvas extends JComponent implements Scrollable {
	
	/**
	 * The associated top-level scroll pane enclosing the scroll canvas.
	 */
	transient private ScrollCanvasPane scp = null;
	
	/**
	 * The associated viewport enclosing the scroll canvas.
	 */
	transient private JViewport scv = null;

	/**
	* Constructs a scroll canvas with horizontal and vertical policies.  The
	* policies are the same as those for JScrollPane.
	* @param vsbPolicy The vertical scroll policy.
	* @param hsbPolicy The horizontal scroll policy.
	*/
	public JScrollCanvas(int vsbPolicy, int hsbPolicy) {
		jScrollCanvasInit(vsbPolicy, hsbPolicy);
	}

	/**
	* Constructs a default canvas with "scroll never" policies.
	*/
	public JScrollCanvas() {
		jScrollCanvasInit(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	}

	/**
	* Initializes the scrolling canvas.
	* @param vsbPolicy The vertical scroll policy.
	* @param hsbPolicy The horizontal scroll policy.
	*/
	private void jScrollCanvasInit(int vsbPolicy, int hsbPolicy) {

		scp = new ScrollCanvasPane(vsbPolicy, hsbPolicy, this);
		scv = (JViewport) (scp.getViewport());

		if (getBackground() == null || getBackground() instanceof UIResource) {
			setBackground(UIManager.getColor("Panel.background"));
		}

		scv.add(this);
	}

	/**
	* Gets the enclosing top-level component.
	* @return The enclosing top-level component.
	*/
	public JScrollPane getView() {
		return (scp);
	}

	/**
	* Gets whether the enclosing scroll pane is valid.
	* @return Whether the enclosing scroll pane is valid.
	*/
	public boolean isViewValid() {
		return (scp.isValid());
	}

	/**
	* Gets whether the enclosing scroll pane is visible.
	* @return Whether the enclosing scroll pane is visible.
	*/
	public boolean isViewVisible() {
		return (scp.isVisible());
	}

	/**
	* Gets whether the enclosing scroll pane is showing.
	* @return Whether the enclosing scroll pane is showing.
	*/
	public boolean isViewShowing() {
		return (scp.isShowing());
	}

	/**
	* Gets whether the enclosing scroll pane is enabled.
	* @return Whether the enclosing scroll pane is enabled.
	*/
	public boolean isViewEnabled() {
		return (scp.isEnabled());
	}

	/**
	* Sets whether the enclosing scroll pane is enabled.
	* @param b Whether the enclosing scroll pane is enabled.
	*/
	public void setViewEnabled(boolean b) {
		scp.setEnabled(b);
	}

	/**
	* Sets whether the enclosing scroll pane is visible.
	* @param b Whether the enclosing scroll pane is visible.
	*/
	public void setViewVisible(boolean b) {
		scp.setVisible(b);
	}

	/**
	* Gets the locale of the enclosing scroll pane.
	* @return The locale of the enclosing scroll pane.
	*/
	public Locale getViewLocale() {
		return (scp.getLocale());
	}

	/**
	* Sets the locale of the enclosing scroll pane.
	* @param l The locale of the enclosing scroll pane.
	*/
	public void setViewLocale(Locale l) {
		scp.setLocale(l);
	}

	/**
	* Gets the location of the enclosing scroll pane.
	* @return The location of the enclosing scroll pane.
	*/
	public Point getViewLocation() {
		return (scp.getLocation());
	}

	/**
	* Gets the location of the enclosing scroll pane in screen coordinates.
	* @return The location of the enclosing scroll pane in screen coordinates.
	*/
	public Point getViewLocationOnScreen() {
		return (scp.getLocationOnScreen());
	}

	/**
	* Sets the location of the enclosing scroll pane.
	* @param x The X-Axis location of the enclosing scroll pane.
	* @param y The Y-Axis location of the enclosing scroll pane.
	*/
	public void setViewLocation(int x, int y) {
		scp.setLocation(x, y);
	}

	/**
	* Sets the location of the enclosing scroll pane.
	* @param p The location of the enclosing scroll pane.
	*/
	public void setViewLocation(Point p) {
		scp.setLocation(p);
	}

	/**
	* Gets the size of the enclosing scroll pane.
	* @return The size of the enclosing scroll pane.
	*/
	public Dimension getViewSize() {
		return (scp.getSize());
	}

	/**
	* Sets the size of the enclosing scroll pane.
	* @param width The width of the enclosing scroll pane.
	* @param height The height of the enclosing scroll pane.
	*/
	public void setViewSize(int width, int height) {
		scp.setSize(width, height);
	}

	/**
	* Sets the size of the enclosing scroll pane.
	* @param p The size of the enclosing scroll pane.
	*/
	public void setViewSize(Dimension d) {
		scp.setSize(d);
	}

	/**
	* Gets the bounds of the enclosing scroll pane.
	* @return The bounds of the enclosing scroll pane.
	*/
	public Rectangle getViewBounds() {
		return (scp.getBounds());
	}

	/**
	* Sets the bounds of the enclosing scroll pane.
	* @param x The X-position of the enclosing scroll pane.
	* @param y The Y-position of the enclosing scroll pane.
	* @param width The width of the enclosing scroll pane.
	* @param height The height of the enclosing scroll pane.
	*/
	public void setViewBounds(int x, int y, int width, int height) {
		scp.setBounds(x, y, width, height);
	}

	/**
	* Sets the bounds of the enclosing scroll pane.
	* @param r The bounds of the enclosing scroll pane.
	*/
	public void setViewBounds(Rectangle r) {
		scp.setBounds(r);
	}

	/**
	* Gets the enclosing viewport.
	* @return The enclosing viewport.
	*/
	public JViewport getViewport() {
		return (scv);
	}

	/**
	* Gets the background color of the scroll pane.
	* @return The background color of the scroll pane.
	*/
	public Color getScrollBackground() {
		return (scp.getBackground());
	}

	/**
	* Sets the background color of the scroll pane.
	* @param c The background color of the scroll pane.
	*/
	public void setScrollBackground(Color c) {
		scp.setBackground(c);
	}

	@Override
	public Color getBackground() {
		return (scv.getBackground());
	}

	@Override
	public void setBackground(Color c) {
		scv.setBackground(c);
	}

	/**
	* Gets the name of the scroll pane view.
	* @return The name of the scroll pane view.
	*/
	public String getViewName() {
		return (scp.getName());
	}

	/**
	* Sets the name of the scroll pane view.
	* @param name The name of the scroll pane view.
	*/
	public void setViewName(String name) {
		scp.setName(name);
	}

	/**
	* Gets the parent of the enclosing scroll pane.
	* @return The parent of the enclosing scroll pane.
	*/
	public Container getViewParent() {
		return (scp.getParent());
	}

	/**
	* Gets the horizontal scroll bar of the scroll pane.
	* @return The horizontal scroll bar of the scroll pane.
	*/
	public JScrollBar getHorizontalScrollBar() {
		return (scp.getHorizontalScrollBar());
	}

	/**
	* Gets the vertical scroll bar of the scroll pane.
	* @return The vertical scroll bar of the scroll pane.
	*/
	public JScrollBar getVerticalScrollBar() {
		return (scp.getVerticalScrollBar());
	}

	/**
	* Gets the border for the enclosing viewport.
	* @return The border for the enclosing viewport.
	*/
	public Border getViewportBorder() {
		return (scp.getViewportBorder());
	}

	/**
	* Sets the border for the enclosing viewport.
	* @param viewportBorder The border for the enclosing viewport.
	*/
	public void setViewportBorder(Border viewportBorder) {
		scp.setViewportBorder(viewportBorder);
	}

	/**
	* Gets the row header for the scroll pane.
	* @return The row header for the scroll pane.
	*/
	public JViewport getRowHeader() {
		return (scp.getRowHeader());
	}

	/**
	* Sets the row header for the scroll pane.
	* @param x The row header for the scroll pane.
	*/
	public void setRowHeader(JViewport x) {
		scp.setRowHeader(x);
	}

	/**
	* Sets the row header for the scroll pane.
	* @param view The row header for the scroll pane.
	*/
	public void setRowHeaderView(Component view) {
		scp.setRowHeaderView(view);
	}

	/**
	* Gets the column header for the scroll pane.
	* @return The column header for the scroll pane.
	*/
	public JViewport getColumnHeader() {
		return (scp.getColumnHeader());
	}

	/**
	* Sets the column header for the scroll pane.
	* @param x The column header for the scroll pane.
	*/
	public void setColumnHeader(JViewport x) {
		scp.setColumnHeader(x);
	}

	/**
	* Sets the column header for the scroll pane.
	* @param view The column header for the scroll pane.
	*/
	public void setColumnHeaderView(Component view) {
		scp.setColumnHeaderView(view);
	}

	/**
	* Gets the extent size of the viewport into which the canvas is embedded.
	* @return The extent size of the viewport into which the canvas is embedded.
	*/
	public Dimension getExtentSize() {
		return (scv.getExtentSize());
	}

	/**
	* Gets the insets of the enclosing scroll pane.
	* @return The insets of the enclosing scroll pane.
	*/
	public Insets getViewInsets() {
		return (scp.getInsets());
	}

	/**
	* Gets the vertical scroll policy.  The policy is the same as that for JScrollPane.
	* @return The vertical scroll policy.  The policy is the same as that for JScrollPane.
	*/
	public int getVerticalScrollBarPolicy() {
		return (scp.getVerticalScrollBarPolicy());
	}

	/**
	* Sets the vertical scroll policy.  The policy is the same as that for JScrollPane.
	* @param x The vertical scroll policy.  The policy is the same as that for JScrollPane.
	*/
	public void setVerticalScrollBarPolicy(int x) {
		scp.setVerticalScrollBarPolicy(x);
	}

	/**
	* Gets the horizontal scroll policy.  The policy is the same as that for JScrollPane.
	* @return The horizontal scroll policy.  The policy is the same as that for JScrollPane.
	*/
	public int getHorizontalScrollBarPolicy() {
		return (scp.getHorizontalScrollBarPolicy());
	}

	/**
	* Sets the horizontal scroll policy.  The policy is the same as that for JScrollPane.
	* @param x The horizontal scroll policy.  The policy is the same as that for JScrollPane.
	*/
	public void setHorizontalScrollBarPolicy(int x) {
		scp.setHorizontalScrollBarPolicy(x);
	}

	/**
	* Gets whether the backing store is enabled.
	* @return Whether the backing store is enabled.
	*/
	public boolean isBackingStoreEnabled() {
		return (scv.isBackingStoreEnabled());
	}

	/**
	* Sets whether the backing store is enabled.
	* @param x Whether the backing store is enabled.
	*/
	public void setBackingStoreEnabled(boolean x) {
		scv.setBackingStoreEnabled(x);
	}

	/**
	* Gets the preferred size for the scroll pane.
	* @return The preferred size for the scroll pane.
	*/
	public Dimension getViewPreferredSize() {
		return (scp.getViewPreferredSize());
	}

	/**
	* Gets the minimum size for the scroll pane.
	* @return The minimum size for the scroll pane.
	*/
	public Dimension getViewMinimumSize() {
		return (scp.getViewMinimumSize());
	}

	/**
	* Gets the maximum size for the scroll pane.
	* @return The maximum size for the scroll pane.
	*/
	public Dimension getViewMaximumSize() {
		return (scp.getViewMaximumSize());
	}

	/**
	* Sets the preferred size for the scroll pane.
	* @param preferredSize The preferred size for the scroll pane.
	*/
	public void setViewPreferredSize(Dimension preferredSize) {
		scp.setViewPreferredSize(preferredSize);
	}

	/**
	* Sets the maximum size for the scroll pane.
	* @param maximumSize The maximum size for the scroll pane.
	*/
	public void setViewMaximumSize(Dimension maximumSize) {
		scp.setViewMaximumSize(maximumSize);
	}

	/**
	* Sets the minimum size for the scroll pane.
	* @param minimumSize The minimum size for the scroll pane.
	*/
	public void setViewMinimumSize(Dimension minimumSize) {
		scp.setViewMinimumSize(minimumSize);
	}

	/**
	* Sets the tool tip text for the scroll pane.
	* @param text The tool tip text for the scroll pane.
	*/
	public void setViewToolTipText(String text) {
		scp.setToolTipText(text);
	}

	/**
	* Gets the tool tip text from the scroll pane.
	* @return The tool tip text from the scroll pane.
	*/
	public String getViewToolTipText() {
		return (scp.getToolTipText());
	}

	/**
	* Gets the X position of the enclosing scroll pane.
	* @return The X position of the enclosing scroll pane.
	*/
	public int getViewX() {
		return (scp.getX());
	}

	/**
	* Gets the Y position of the enclosing scroll pane.
	* @return The Y position of the enclosing scroll pane.
	*/
	public int getViewY() {
		return (scp.getY());
	}

	/**
	* Gets the width of the enclosing scroll pane.
	* @return The width of the enclosing scroll pane.
	*/
	public int getViewWidth() {
		return (scp.getWidth());
	}

	/**
	* Gets the height of the enclosing scroll pane.
	* @return The height of the enclosing scroll pane.
	*/
	public int getViewHeight() {
		return (scp.getHeight());
	}

	/**
	* Sets whether the scroll pane is double-buffered.
	* @param aFlag Whether the scroll pane is double-buffered.
	*/
	public void setViewDoubleBuffered(boolean aFlag) {
		scp.setDoubleBuffered(aFlag);
	}

	/**
	* Returns whether the scroll pane is double-buffered.
	* @return Whether the scroll pane is double-buffered.
	*/
	public boolean isViewDoubleBuffered() {
		return (scp.isDoubleBuffered());
	}

	/*
		public Component add(Component comp)
			{ System.out.println( "Don't use add with a Canvas component." ); return( null ); }
	
	 public Component add(String name,
	                      Component comp)
		{ System.out.println( "Don't use add with a Canvas component." ); return( null ); }
	
	 public Component add(Component comp,
	                      int index)
	
		{ System.out.println( "Don't use add with a Canvas component." ); return( null ); }
	
	 public void add(Component comp,
	                 Object constraints)
	
		{ System.out.println( "Don't use add with a Canvas component." ); }
	
	 public void add(Component comp,
	                 Object constraints,
	                 int index)
	
		{ System.out.println( "Don't use add with a Canvas component." ); }
	
	 protected void addImpl(Component comp,
	                        Object constraints,
	                        int index)
	
	     { System.out.println( "Don't use add with a Canvas component." ); }
	
	 public void remove(int index)
	
		{ System.out.println( "Don't use add with a Canvas component." ); }
	
	 public void remove(Component comp)
	
		{ System.out.println( "Don't use add with a Canvas component." ); }
	
	 public void removeAll()
	
		{ System.out.println( "Don't use add with a Canvas component." ); }
	
	*/

	/**
	* Returns a string representation of the scroll canvas.  Not implemented.
	* @return A string representation of the scroll canvas.  Not implemented.
	*/
	protected String paramString() {
		System.out.println("Not implemented");
		return (null);
	}

	/**
	* Returns a string representation of the scroll canvas.  Not implemented.
	* @return A string representation of the scroll canvas.  Not implemented.
	*/
	public String toString() {
		System.out.println("Not implemented.");
		return (null);
	}

	/**
	* Returns the preferred size of the scrolling viewport.  This default is
	* 200 x 100 pixels.
	* @return The preferred size of the scrolling viewport.
	*/
	public Dimension getPreferredScrollableViewportSize() {
		return (new Dimension(200, 100));
	}

	/**
	* Returns the unit scroll increment of the canvas.  This is currently
	* one pixel.
	* @return The unit scroll increment of the canvas.
	*/
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return (1);
	}

	/**
	* Returns the default block increment of the canvas.  The default is
	* the height and width of the visible rect.
	* @return The default block increment of the canvas.
	*/
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		if (orientation == SwingConstants.VERTICAL) {
			return (visibleRect.height);
		}
		else {
			return (visibleRect.width);
		}
	}

	/**
	* Returns whether the scrolled component tracks the width of the viewport.
	* @return Whether the scrolled component tracks the width of the viewport.
	*/
	public boolean getScrollableTracksViewportWidth() {
		return (false);
	}

	/**
	* Returns whether the scrolled component tracks the height of the viewport.
	* @return Whether the scrolled component tracks the height of the viewport.
	*/
	public boolean getScrollableTracksViewportHeight() {
		return (false);
	}

	@Override
	public void updateUI() {
		super.updateUI();
		if (getBackground() == null || getBackground() instanceof UIResource) {
			setBackground(UIManager.getColor("Panel.background"));
		}
	}

	
}

