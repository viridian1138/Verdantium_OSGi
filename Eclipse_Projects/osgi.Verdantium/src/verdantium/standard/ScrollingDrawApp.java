package verdantium.standard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.HashMap;

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
import verdantium.core.PageSizeHandler;
import verdantium.core.PageSizePropertyEditor;
import verdantium.standard.help.ScrollingDrawAppHelp;
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
* This demonstrates how to make a scrolling version of a {@link DrawApp}.
* <P>
* @author Thorn Green
*/
public class ScrollingDrawApp
	extends DrawApp
	implements Printable, PageSizeHandler {

	/**
	* The panel controlling the size of the document pane.
	*/
	transient protected JPanel sizingPanel;

	/**
	* Constructs the component.
	*/
	public ScrollingDrawApp() {
		super();
		pageSizeState = new PageSizeState(undoMgr);
		pageSizeState.configureForEtherEvents(this);
		docPageFormat.setPropertyName(
			PageSizePropertyEditor.defaultPageSizeChanged);
		arrangeLayoutScr();
		PrinterJob job = PrinterJob.getPrinterJob();
		PageFormat pf1 = job.defaultPage();
		Dimension d =
			new Dimension(
				(int) (pf1.getImageableWidth()),
				(int) (pf1.getImageableHeight()));
		try {
			alterPageSize(d);
		} catch (Exception ex) {
			throw (new WrapRuntimeException(ex));
		}
	}

	/**
	* Shunts the superclass layout setup.
	*/
	protected void arrangeLayout() {
	}

	/**
	* Sets up the layout manager for the component.
	*/
	protected void arrangeLayoutScr() {
		scp =
			new UndoableScrollPane(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED,
				this,
				undoMgr);
		JPanel Panel2 = new ScrollingDrawAppPanel(this, this);
		sizingPanel = Panel2;
		myPan.setLayout(new BorderLayout(0, 0));
		Panel2.setLayout(new BorderLayout(0, 0));
		myPan.add("Center", scp);
		JViewport scv = (JViewport) (scp.getViewport());
		scv.add(Panel2);
		Panel2.add("Center", myDesk);
		myPan.setMinimumSize(new Dimension(2, 2));
		myPan.setPreferredSize(new Dimension(200, 200));
		scp.setBackground(Color.white);
	}

	@Override
	protected void paintDrawApp(Graphics2D g) {
		g.setColor(Color.orange);
		Dimension PageSz = getPageSize();
		g.drawRect(-2, -2, PageSz.width + 4, PageSz.height + 4);
		super.paintDrawApp(g);
	}

	/**
	* Returns the data flavors that the component can load from persistent storage.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{
				 new TransVersionBufferFlavor(
					"Scrolling Draw App",
					"Scrolling Draw App")};
		return (MyF);
	}

	@Override
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{
				 new TransVersionBufferFlavor(
					"Scrolling Draw App",
					"Scrolling Draw App")};
		return (MyF);
	}

	@Override
	public Transferable savePersistentData(DataFlavor flavor)
		throws IOException {
		TransVersionBuffer myF =
			new TransVersionBuffer("Scrolling Draw App", "Scrolling Draw App");

		try {
			myF.setProperty(
				"Frames",
				ContainerApp.saveInternalDesktopFrames(myDesk));
			myF.setProperty("LineColor", getLineColor() );
			myF.setProperty("FillColor", getFillColor() );
                        
			HashMap persistenceContext = new HashMap();
			myF.setProperty("DrawingList", 
                                    undoMgr.generatePersistenceFromJUndo( 
                                    model.pdxm_getDrawingList( undoMgr.getCurrentMil() ) , persistenceContext ) );
                        
			myF.setInt("currentMode", getEditMode() );
			bkgnd.writeData(myF);
			myF.setBoolean("OnlyDesignerEdits", isOnlyDesignerEdits());
			myF.setInt("BezMode", getBezMode() );
			myF.setBoolean("ChordP", getChordParam() );
			macroMap.writeData(myF);
			myF.setProperty("PageSize", getPageSize());

			borderObject.writeData(myF);
		} catch (IOException ex) {
			throw (ex);
		}

		return (myF);
	}

	/**
	* Prints the document.
	* @param gi The graphics context into which to print.
	* @param pf Input description of the page to be generated.
	* @param pageIndex Index indicating the page number.
	*/
	public int print(Graphics gi, PageFormat pf, int pageIndex)
		throws PrinterException {
		JComponent MyC = myDesk;
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

	/**
	* Alters the document's page size including the page layout.
	*/
	public void handlePageSizeChange() {
		sizingPanel.revalidate();
		scp.revalidate();
		scp.getViewport().revalidate();
		scp.repaint();
	}

	/* public void mouserDown( MouseEvent e , int x , int y )
		{
		alterPageSize( new Dimension( 100 , 100 ) );
		} (Used for testing only) */

	/**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		ScrollingDrawAppHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		ScrollingDrawApp MyComp = new ScrollingDrawApp();
		ProgramDirector.showComponent(MyComp, "Scrolling Draw App", argv, true);
	}

}

/**
* A panel that acts as an intermediary between the scroll pane and the enclosed draw
* app GUI.
* 
* @author Thorn Green
*/
class ScrollingDrawAppPanel
	extends JPanel
	implements Scrollable, ScrollableAlt {
	
	/**
	* The drawing pane used in the editing.
	*/
	JComponent myEdit = null;
	
	/**
	* The scrolling draw app. that uses the component.
	*/
	ScrollingDrawApp myDrawApp = null;

	/**
	 * Constructs the panel for a certain scrolling draw app.
	 * @param in The imaging pane used in the editing component.
	 * @param inc The scrolling image viewer that uses the component.
	 */
	public ScrollingDrawAppPanel(JComponent in, ScrollingDrawApp inc) {
		myEdit = in;
		myDrawApp = inc;
	}

	/**
	* Gets the preferred size of the viewport.
	* @return The preferred size of the viewport.
	*/
	public Dimension getPreferredScrollableViewportSize() {
		Dimension d2 = myDrawApp.getPageSize();
		return (d2);
	}

	/**
	* Returns the unit increment of the scrolling.
	* @return The unit increment of the scrolling.
	*/
	public int getScrollableUnitIncrement(
		Rectangle visibleRect,
		int orientation,
		int direction) {
		return (10);
	}

	/**
	* Gets the increment for clicking in the scrollbar track.
	* @return The increment for clicking in the scrollbar track.
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
	* Returns whether the scrollable tracks the viewport width.
	* @return Whether the scrollable tracks the viewport width.
	*/
	public boolean getScrollableTracksViewportWidth() {
		return (false);
	}

	/**
	* Returns whether the scrollable tracks the viewport height.
	* @return Whether the scrollable tracks the viewport height.
	*/
	public boolean getScrollableTracksViewportHeight() {
		return (false);
	}

	@Override
	public Dimension getMinimumSize() {
		Dimension d2 = myDrawApp.getPageSize();
		return (d2);
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d2 = myDrawApp.getPageSize();
		return (d2);
	}

	/**
	* Returns whether the width of the component can track beyond its
	* preferred size.
	* @return Whether the width of the component can track beyond its
	* preferred size.
	*/
	public boolean getAltTracksWidth() {
		return (true);
	}
	
	/**
	* Returns whether the height of the component can track beyond its
	* preferred size.
	* @return Whether the height of the component can track beyond its
	* preferred size.
	*/
	public boolean getAltTracksHeight() {
		return (true);
	}

	
}

