package verdantium.pagewelder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import meta.WrapRuntimeException;
import verdantium.ProgramDirector;
import verdantium.TransVersionBuffer;
import verdantium.TransVersionBufferFlavor;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumUtils;
import verdantium.core.ContainerApp;
import verdantium.core.ContainerAppDesktopPane;
import verdantium.core.PageSizeHandler;
import verdantium.core.PageSizePropertyEditor;
import verdantium.pagewelder.help.ScrollingPageWelderHelp;
import verdantium.utils.ComponentNotFoundException;
import verdantium.utils.ResourceNotFoundException;
import verdantium.utils.ScrollableAlt;
import verdantium.utils.UndoableScrollPane;
import verdantium.xapp.PageSizeState;

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
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/10/2001            | Thorn Green (viridian_1138@yahoo.com)           | Second-Cut at Error Handling.                                        | Second-Cut at Error Handling.
*    | 09/29/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for multiple PLAFs.                                          | Made code changes to support multiple PLAFs.
*    | 10/05/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for command-line arguments.                                  | Added support for command-line arguments.
*    | 12/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Third-Cut at Error Handling.                                         | Third-Cut at Error Handling.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
*    | 05/10/2002            | Thorn Green (viridian_1138@yahoo.com)           | Redundant information in persistent storage.                         | Made numerous persistence and packaging changes.
*    | 03/09/2003            | Thorn Green (viridian_1138@yahoo.com)           | PageWelder.                                                          | Implemented PageWelder using code from other classes.
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
* This demonstrates how to make a scrolling version of a {@link PageWelder}.
* <P>
* @author Thorn Green
*/
public class ScrollingPageWelder
	extends PageWelder
	implements Printable, PageSizeHandler {

	/**
	* The panel used to contain thr non-scrolling parts of the PageWelder component.
	*/
	transient protected JPanel SizingPanel;

	/**
	* Constructs the scrolling PageWelder component.
	*/
	public ScrollingPageWelder() {
		super();
		pageSizeState = new PageSizeState(undoMgr);
		pageSizeState.configureForEtherEvents(this);
		docPageFormat.setPropertyName(
			PageSizePropertyEditor.defaultPageSizeChanged);
		arrangeLayoutScr();
		try {
			addCardInitSub();
		} catch (Throwable ex) {
			throw (new WrapRuntimeException(ex));
		}
		PrinterJob job = PrinterJob.getPrinterJob();
		PageFormat pf1 = job.defaultPage();
		Dimension d =
			new Dimension(
				(int) (pf1.getImageableWidth()),
				(int) (pf1.getImageableHeight()));
		try {
			alterPageSize(d);
		} catch (Throwable ex) {
			throw (new WrapRuntimeException(ex));
		}
		setDefaultBkgndSub();
	}

	/**
	* Shunts the superclass layout setup.
	*/
	protected void arrangeLayout() {
	}

	/**
	* Sets up the layout managers for the scrolling container app.
	*/
	protected void arrangeLayoutScr() {
		scp =
			new UndoableScrollPane(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED,
				this,
				undoMgr);
		JPanel Panel2 = new ScrollingPageWelderPanel(this);
		SizingPanel = Panel2;
		MyPan.setLayout(new BorderLayout(0, 0));
		Panel2.setLayout(new BorderLayout(0, 0));
		MyPan.add("Center", scp);
		JViewport scv = (JViewport) (scp.getViewport());
		scv.add(Panel2);
		Panel2.add("Center", innerPanel);
		MyPan.setMinimumSize(new Dimension(2, 2));
		MyPan.setPreferredSize(new Dimension(200, 200));
		scp.setBackground(Color.white);
		MyPan.setOpaque(false);
		innerPanel.setLayout(cardL);
	}

	/**
	* Returns the data flavors that the component can load from persistent storage.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{
				 new TransVersionBufferFlavor(
					"Scrolling Page Welder",
					"Scrolling Page Welder")};
		return (MyF);
	}

	@Override
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{
				 new TransVersionBufferFlavor(
					"Scrolling Page Welder",
					"Scrolling Page Welder")};
		return (MyF);
	}

	@Override
	public Transferable savePersistentData(DataFlavor flavor)
		throws IOException {
		TransVersionBuffer MyF =
			new TransVersionBuffer(
				"Scrolling Page Welder",
				"Scrolling Page Welder");

		try {
			int count;
			int numCards = getNumCards();
			for (count = 0; count < numCards; count++) {
				ContainerAppDesktopPane MyDesk = getCardForIndex(count);
				MyF.setProperty(
					"Frames_" + count,
					ContainerApp.saveInternalDesktopFrames(MyDesk));
				MyF.setBoolean("Opaque_" + count, MyDesk.isOpaque());
				MyF.setProperty("Background_" + count, MyDesk.getBackground());
			}
			MyF.setInt("NumCards", numCards);
			MyF.setBoolean("OnlyDesignerEdits", isOnlyDesignerEdits());
			macroMap.writeData(MyF);
			MyF.setProperty("PageSize", getPageSize());

			borderObject.writeData(MyF);

			if (getBkgndTemplate() != null)
				MyF.setProperty("BkgndTemplate", getBkgndTemplate());
		} catch (IOException ex) {
			throw (ex);
		}

		return (MyF);
	}

	/**
	* Prints a page of the document.
	* @param gi The context into which to print the document.
	* @param pf The page format into which to print.
	* @param pageIndex The index of the current page.
	*/
	public int print(Graphics gi, PageFormat pf, int pageIndex)
		throws PrinterException {
		JComponent MyC = getCurrentCard();
		int ret = 0;
		try {
			ret = VerdantiumUtils.printJComponent(gi, pf, pageIndex, MyC);
		} catch (PrinterException e) {
			throw (e);
		}
		return (ret);
	}

	@Override
	public boolean isScrolling() {
		return (true);
	}

	@Override
	public void handlePageSizeChange() {
		SizingPanel.revalidate();
		scp.revalidate();
		scp.getViewport().revalidate();
		scp.repaint();
	}

	/**
	 * Shunts the superclass method to add a card.
	 */
	protected void addCardInit() throws Throwable {
	}

	/**
	 * Adds a card to the PageWelder.
	 * @throws Throwable
	 */
	protected void addCardInitSub() throws Throwable {
		addCard();
	}

	@Override
	protected PageWelderDesktopPane addCard()
		throws
			ClassNotFoundException,
			ResourceNotFoundException,
			ComponentNotFoundException,
			IOException {
		PageWelderDesktopPane dpane = super.addCard();
		dpane.setPageSizeHandler(this);
		return (dpane);
	}

	/**
	 * Shunts the superclass method to set the background.
	 */
	protected void setDefaultBkgnd() {
	}

	/**
	 * Sets the background of the displayed GUI.
	 */
	protected void setDefaultBkgndSub() {
		super.setDefaultBkgnd();
		getBkgnd().setBackgroundState(Color.white, true);
	}

	/**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		ScrollingPageWelderHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		ScrollingPageWelder MyComp = new ScrollingPageWelder();
		ProgramDirector.showComponent(
			MyComp,
			"Scrolling Page Welder",
			argv,
			true);
	}

	
}


/**
* A panel that acts as an intermediary between the scroll pane and the enclosed container
* app GUI.
* 
* @author Thorn Green
*/
class ScrollingPageWelderPanel
	extends JPanel
	implements Scrollable, ScrollableAlt {
	
	/**
	* The scrolling PageWelder component for which this component is the client.
	*/
	ScrollingPageWelder MyCont = null;

	/**
	* Constructs the panel for a certain scrolling PageWelder component.
	* @param inc The component to be scrolled.
	*/
	public ScrollingPageWelderPanel(ScrollingPageWelder inc) {
		MyCont = inc;
	}

	/**
	* Gets the preferred size of the viewport.
	* @return The preferred size of the viewport.
	*/
	public Dimension getPreferredScrollableViewportSize() {
		Dimension d2 = MyCont.getPageSize();
		return (d2);
	}

	/**
	* Returns the unit increment of the scrolling.
	* @param visRect The visible rectangle in the scrolling pane.
	* @param orientation The scrolling orientation.
	* @param direction The scrolling direction.
	*/
	public int getScrollableUnitIncrement(
		Rectangle visibleRect,
		int orientation,
		int direction) {
		return (10);
	}

	/**
	* Gets the increment for clicking in the scrollbar track.
	* @param visRect The visible rectangle in the scrolling pane.
	* @param orientation The scrolling orientation.
	* @param direction The scrolling direction.
	*/
	public int getScrollableBlockIncrement(
		Rectangle visRect,
		int orientation,
		int direction) {
		if (orientation == SwingConstants.HORIZONTAL)
			return (visRect.width);
		else
			return (visRect.height);
	}

	/**
	* Returns whether the scrollable tracks the viewport height.
	* @Override Whether the scrollable tracks the viewport height.
	*/
	public boolean getScrollableTracksViewportWidth() {
		return (false);
	}

	/**
	* Returns whether the scrollable tracks the viewport width.
	* @return Whether the scrollable tracks the viewport width.
	*/
	public boolean getScrollableTracksViewportHeight() {
		return (false);
	}

	@Override
	public Dimension getMinimumSize() {
		Dimension d2 = MyCont.getPageSize();
		return (d2);
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d2 = MyCont.getPageSize();
		return (d2);
	}

	/**
	* Returns whether the width of the component can track beyond its
	* preferred size.
	* @return Whether the width of the component can track beyond its preferred size.
	*/
	public boolean getAltTracksWidth() {
		return (true);
	}
	/**
	* Returns whether the height of the component can track beyond its
	* preferred size.
	* @return Whether the height of the component can track beyond its preferred size.
	*/
	public boolean getAltTracksHeight() {
		return (true);
	}

	
}

