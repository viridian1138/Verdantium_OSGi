package verdantium.vhelp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;

import verdantium.BookPrintable;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.core.PrintPreviewPropertyEditor;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.utils.JScrollPaneAlt;
import verdantium.utils.ScrollableAlt;

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
*    | 03/04/2001            | Thorn Green (viridian_1138@yahoo.com)           | Printed items did not properly wrap around page breaks.              | Improved handling of page breaks.
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Slow performance when inserting large numbers of figures.            | Improved insertion performance by taking better advantage of JEditorPane.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/10/2001            | Thorn Green (viridian_1138@yahoo.com)           | Second-Cut at Error Handling.                                        | Second-Cut at Error Handling.
*    | 11/17/2001            | Thorn Green (viridian_1138@yahoo.com)           | Documentation fixes.                                                 | Documentation fixes.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
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
* This is a scrolling version of {@link VerdantiumHelp} with built-in word wrap.
* <P>
* @author Thorn Green
*/
public class WrappingScrollingVerdantiumHelp
	extends VerdantiumHelp
	implements BookPrintable {
	
	/**
	* The scrolling pane containing the text app.
	*/
	protected JScrollPaneAlt scp;
	
	/**
	* The width of the document page.
	*/
	protected int PageWidth = 50;
	
	/**
	* The panel controlling the size of the document pane.
	*/
	protected JPanel SizingPanel;

	/**
	* The EtherEvent name to set the width of the document page.
	*/
	public static final String setPageWidth = "setPageWidth";

	@Override
	public void setColors(Color Fg, Color Bk) {
		super.setColors(Fg, Bk);
		scp.setBackground(Bk);
	}

	/**
	* Constructs the component.
	* @param in The location of the help page text to be displayed.
	*/
	public WrappingScrollingVerdantiumHelp(URL in) {
		super(in);
		arrangeLayoutScr();
		PrinterJob job = PrinterJob.getPrinterJob();
		PageFormat pf1 = job.defaultPage();
		int d = (int) (pf1.getImageableWidth());
		alterPageWidth(d);
	}

	/**
	* Shunts the superclass layout setup.
	*/
	protected void arrangeLayout() {
	}

	/**
	* Sets up the layout managers for the component.
	*/
	protected void arrangeLayoutScr() {
		scp =
			new JScrollPaneAlt(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel Panel2 = new WrappingScrollingVerdantiumHelpPanel(myEdit, this);
		SizingPanel = Panel2;
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
	public Object processObjEtherEvent(EtherEvent in, Object refcon)
		throws Throwable {
		boolean handled = false;

		if (in instanceof PropertyEditEtherEvent) {
			if (in.getEtherID().equals(setPageWidth)) {
				Integer d = (Integer) (in.getParameter());
				alterPageWidth(d.intValue());
				handled = true;
			}
		}

		if (!handled)
			return (super.processObjEtherEvent(in, refcon));
		return (null);
	}

	@Override
	protected boolean isScrolling() {
		return (true);
	}

	/**
	* Gets the component's document page width.
	* @return The component's document page width.
	*/
	public int getPageWidth() {
		return (PageWidth);
	}

	/**
	* Sets the component's document page width.
	* @param in The component's document page width.
	*/
	public void setPageWidth(int in) {
		PageWidth = in;
	}

	/**
	* Alters the component's document page width including screen representation.
	* @param in The component's document page width.
	*/
	public void alterPageWidth(int in) {
		setPageWidth(in);
		SizingPanel.revalidate();
		scp.revalidate();
		scp.getViewport().revalidate();
		scp.repaint();
	}

	/**
	* Handles multi-page printing for the component.
	*/
	public void handleBookPrinting() throws PrinterException {
		PrinterJob job = PrinterJob.getPrinterJob();
		PageFormat pf1 = job.defaultPage();
		job.validatePage(pf1);
		Book bk = new Book();

		ScrollingVerdantiumHelpPrintable st =
			new ScrollingVerdantiumHelpPrintable(myDesk, myEdit, pf1);
		int numPages = st.getNumPages();

		bk.append(st, pf1, numPages);
		job.setPageable(bk);
		if (job.printDialog()) {
			try {
				job.print();
			} catch (PrinterException ex) {
				throw (ex);
			}
		}
	}

	/**
	* Handles Print Preview on the component.
	*/
	public void handlePrintPreview() {
		PrinterJob job = PrinterJob.getPrinterJob();
		PageFormat pf1 = job.defaultPage();
		job.validatePage(pf1);
		Book bk = new Book();

		ScrollingVerdantiumHelpPrintable st =
			new ScrollingVerdantiumHelpPrintable(myDesk, myEdit, pf1);
		int numPages = st.getNumPages();

		bk.append(st, pf1, numPages);
		job.setPageable(bk);
		PrintPreviewPropertyEditor myp =
			new PrintPreviewPropertyEditor(bk, this);
		ProgramDirector.showPropertyEditor(
			myp,
			getGUI(),
			"Print Preview Property Editor");
	}

}

/**
* Panel that helps to support the scrolling in {@link WrappingScrollingVerdantiumHelp}
* 
* @author Thorn Green
*/
class WrappingScrollingVerdantiumHelpPanel
	extends JPanel
	implements Scrollable, ScrollableAlt {
	
	/**
	* The text pane used in the editing.
	*/
	JEditorPane MyEdit = null;
	
	/**
	* The scrolling text app. that uses the component.
	*/
	WrappingScrollingVerdantiumHelp MyVerdantiumHelp = null;

	/**
	* Constructs the panel.
	*/
	public WrappingScrollingVerdantiumHelpPanel(
		JEditorPane in,
		WrappingScrollingVerdantiumHelp inc) {
		MyEdit = in;
		MyVerdantiumHelp = inc;
	}

	/**
	* Gets the preferred scrolling viewport size for the component.
	* @return The preferred scrolling viewport size for the component.
	*/
	public Dimension getPreferredScrollableViewportSize() {
		Dimension d = MyEdit.getPreferredScrollableViewportSize();
		Dimension d2 = new Dimension(MyVerdantiumHelp.getPageWidth(), d.height);
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
		return (
			MyEdit.getScrollableUnitIncrement(
				visibleRect,
				orientation,
				direction));
	}

	/**
	* Gets the increment for clicking in the scrollbar track.
	* @param visRect The visible rectangle in the scrolling pane.
	* @param orientation The scrolling orientation.
	* @param direction The scrolling direction.
	*/
	public int getScrollableBlockIncrement(
		Rectangle visibleRect,
		int orientation,
		int direction) {
		return (
			MyEdit.getScrollableBlockIncrement(
				visibleRect,
				orientation,
				direction));
	}

	/**
	* Gets whether the scrolling tracks the viewport width.
	* @return Whether the scrolling tracks the viewport width.
	*/
	public boolean getScrollableTracksViewportWidth() {
		return (MyEdit.getScrollableTracksViewportWidth());
	}

	/**
	* Gets whether the scrolling tracks the viewport height.
	* @return Whether the scrolling tracks the viewport height.
	*/
	public boolean getScrollableTracksViewportHeight() {
		return (MyEdit.getScrollableTracksViewportHeight());
	}

	@Override
	public Dimension getMinimumSize() {
		Dimension d = MyEdit.getMinimumSize();
		Dimension d2 = new Dimension(MyVerdantiumHelp.getPageWidth(), d.height);
		return (d2);
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d = MyEdit.getPreferredSize();
		Dimension d2 = new Dimension(MyVerdantiumHelp.getPageWidth(), d.height);
		return (d2);
	}

	/**
	* Returns whether the width of the component can track beyond its
	* preferred size.
	* @return Whether the width of the component can track beyond its preferred size.
	*/
	public boolean getAltTracksWidth() {
		return (false);
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

