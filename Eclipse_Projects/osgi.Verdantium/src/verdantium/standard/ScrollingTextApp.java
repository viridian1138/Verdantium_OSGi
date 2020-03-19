package verdantium.standard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.RepaintManager;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.rtf.RTFEditorKit;

import meta.WrapRuntimeException;
import verdantium.BookPrintable;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.TransVersionBuffer;
import verdantium.TransVersionBufferFlavor;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumFlavorMap;
import verdantium.VerdantiumUtils;
import verdantium.core.BorderPropertyEditor;
import verdantium.core.ContainerApp;
import verdantium.core.PrintPreviewPropertyEditor;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.standard.help.ScrollingTextAppHelp;
import verdantium.utils.IllegalInputException;
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
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Needed to make multiple improvements to TextApp.                     | Improved printing and macro handling among other things.
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
* This demonstrates how to make a scrolling version of a {@link TextApp}.
* <P>
* @author Thorn Green
*/
public class ScrollingTextApp extends TextApp implements BookPrintable {
	
	/**
	* The scrolling pane containing the text app.
	*/
	protected JScrollPaneAlt scp;
	
	/**
	* The width of the document page.
	*/
	protected int pageWidth = 50;
	
	/**
	* The panel controlling the size of the document pane.
	*/
	protected JPanel sizingPanel;

	/**
	* The EtherEvent name to set the width of the document page.
	*/
	public static final String setPageWidth = "setPageWidth";

	@Override
	public void setColors(Color fg, Color bk) {
		super.setColors(fg, bk);
		scp.setBackground(bk);
	}

	/**
	* Constructs the component.
	*/
	public ScrollingTextApp() {
		super();
		arrangeLayoutScr();
		PrinterJob job = PrinterJob.getPrinterJob();
		PageFormat pf1 = job.defaultPage();
		int d = (int) (pf1.getImageableWidth());
		try {
			alterPageWidth(d);
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
	* Sets up the layout managers for the component.
	*/
	protected void arrangeLayoutScr() {
		scp =
			new JScrollPaneAlt(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel Panel2 = new ScrollingTextAppPanel(myEdit, this);
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

	/**
	* Returns the data flavors that the component can load from persistent storage.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{
				new TransVersionBufferFlavor(
					"Scrolling Text App",
					"Scrolling Text App"),
				DataFlavor.stringFlavor,
				VerdantiumFlavorMap.createInputStreamFlavor(
					"application",
					"rtf"),
				VerdantiumFlavorMap.createInputStreamFlavor("text", "plain"),
				VerdantiumFlavorMap.createInputStreamFlavor("text", "html")};
		return (MyF);
	}

	@Override
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF = null;

		if (myEdit.getEditorKit() instanceof HTMLEditorKit) {
			DataFlavor[] fl =
				{
					new TransVersionBufferFlavor(
						"Scrolling Text App",
						"Scrolling Text App"),
					VerdantiumFlavorMap.createOutputStreamFlavor(
						"text",
						"html"),
					VerdantiumFlavorMap.createOutputStreamFlavor(
						"text",
						"plain")};
			MyF = fl;
		} else {
			if (myEdit.getEditorKit() instanceof RTFEditorKit) {
				DataFlavor[] fl =
					{
						new TransVersionBufferFlavor(
							"Scrolling Text App",
							"Scrolling Text App"),
						VerdantiumFlavorMap.createOutputStreamFlavor(
							"application",
							"rtf"),
						VerdantiumFlavorMap.createOutputStreamFlavor(
							"text",
							"plain")};
				MyF = fl;
			} else {
				DataFlavor[] fl =
					{
						new TransVersionBufferFlavor(
							"Scrolling Text App",
							"Scrolling Text App"),
						VerdantiumFlavorMap.createOutputStreamFlavor(
							"text",
							"plain")};
				MyF = fl;
			}
		}

		return (MyF);
	}

	@Override
	public Transferable savePersistentData(DataFlavor flavor)
		throws IOException {
		Transferable trans = null;

		if (flavor instanceof TransVersionBufferFlavor) {
			try {
				TransVersionBuffer MyF =
					new TransVersionBuffer(
						"Scrolling Text App",
						"Scrolling Text App");
				MyF.setProperty(
					"Frames",
					ContainerApp.saveInternalDesktopFrames(myDesk));
				MyF.setProperty("Document", myEdit.getDocument());
				MyF.setBoolean("Opaque", getOpaqueFlag());
				MyF.setProperty(
					"Background",
					VerdantiumUtils.cloneColorRGB(getBackgroundColor()));
				MyF.setBoolean("OnlyDesignerEdits", isOnlyDesignerEdits());
				MyF.setInt("pageWidth", getPageWidth());
				macroMap.writeData(MyF);

				if (borderClass != null) {
					MyF.setProperty("borderClass", borderClass);
					MyF.setProperty(
						"borderTypes",
						BorderPropertyEditor.getTypeStrArray(borderTypes));
					MyF.setProperty("borderParam", borderParam);
				}

				trans = MyF;
			} catch (IOException ex) {
				throw (ex);
			}
		} else {
			trans = super.savePersistentData(flavor);
		}

		return (trans);
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
		return (pageWidth);
	}

	/**
	* Sets the component's document page width.
	* @param in The page width to set.
	*/
	public void setPageWidth(int in) throws IllegalInputException {
		if (in <= 0)
			throw (
				new IllegalInputException("The page width must be at least 1/72 inch."));
		pageWidth = in;
	}

	/**
	* Provides an interface to alter the width of the document page.
	* @param in The width of the document page.
	*/
	public void alterPageWidth(int in) throws IllegalInputException {
		setPageWidth(in);
		sizingPanel.revalidate();
		scp.revalidate();
		scp.getViewport().revalidate();
		scp.repaint();
	}

	/**
	* Handles multi-page printing for the component.
	*/
	public void handleBookPrinting() throws PrinterException {
		PrinterJob job = PrinterJob.getPrinterJob();
		if (docPageFormat.getDocPageFormat() == null) {
			docPageFormat.setDocPageFormat(job.defaultPage());
		}
		docPageFormat.setDocPageFormat(
			job.validatePage(docPageFormat.getDocPageFormat()));
		PageFormat pf1 = docPageFormat.getDocPageFormat();

		Book bk = new Book();

		ScrollingTextAppPrintable st =
			new ScrollingTextAppPrintable(myDesk, myEdit, pf1);
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
	* Handles print-preview for the component.
	*/
	public void handlePrintPreview() {
		PrinterJob job = PrinterJob.getPrinterJob();
		if (docPageFormat.getDocPageFormat() == null) {
			docPageFormat.setDocPageFormat(job.defaultPage());
		}
		docPageFormat.setDocPageFormat(
			job.validatePage(docPageFormat.getDocPageFormat()));
		PageFormat pf1 = docPageFormat.getDocPageFormat();

		Book bk = new Book();

		ScrollingTextAppPrintable st =
			new ScrollingTextAppPrintable(myDesk, myEdit, pf1);
		int numPages = st.getNumPages();

		bk.append(st, pf1, numPages);
		PrintPreviewPropertyEditor myp =
			new PrintPreviewPropertyEditor(bk, this);
		ProgramDirector.showPropertyEditor(
			myp,
			getGUI(),
			"Print Preview Property Editor");
	}

	/**
	 * Prints the contents of the scrolling text app.
	 * @param gi The graphics context into which to render the page.
	 * @param pf The format of the page to print.
	 * @param pageIndex The page index of the page to print.
	 */
	public int print(Graphics gi, PageFormat pf, int pageIndex)
		throws PrinterException {
		Vector<Integer> offsetLow = new Vector<Integer>();
		Vector<Integer> offsetHigh = new Vector<Integer>();

		double height = pf.getImageableHeight();
		if (pageIndex >= getNumPages(height, offsetLow, offsetHigh))
			return (Printable.NO_SUCH_PAGE);

		AffineTransform CoordTrans = new AffineTransform();
		Graphics2D g = (Graphics2D) gi;
		JComponent MyC = myDesk;
		CoordTrans.setToTranslation(pf.getImageableX(), pf.getImageableY());
		AffineTransform trans = g.getTransform();
		trans.concatenate(CoordTrans);
		g.setTransform(trans);
		RepaintManager.currentManager(MyC).setDoubleBufferingEnabled(false);
		printContent(gi, pf, pageIndex, offsetLow, offsetHigh);
		RepaintManager.currentManager(MyC).setDoubleBufferingEnabled(true);
		return (Printable.PAGE_EXISTS);
	}

	/**
	 * Prints the content of a particular page.
	 * @param gi The graphics context into which to render the page.
	 * @param pf The format of the page to print.
	 * @param pageIndex The page index of the page to print.
	 * @param offsetLow The low offset of each page index.
	 * @param offsetHigh The high offset of each page index.
	 * @throws PrinterException
	 */
	protected void printContent(
		Graphics gi,
		PageFormat pf,
		int pageIndex,
		Vector<Integer> offsetLow,
		Vector<Integer> offsetHigh)
		throws PrinterException {
		Graphics2D g = (Graphics2D) gi;

		AffineTransform trans = g.getTransform();

		Rectangle lrect = null;
		int loff = offsetLow.elementAt(pageIndex);
		try {
			lrect = myEdit.modelToView(loff);
		} catch (Exception e) {
			throw (new WrapRuntimeException("Model To View Failed", e));
		}
		double mark = lrect.y;

		AffineTransform CoordTrans = new AffineTransform();
		CoordTrans.setToTranslation(0.0, -mark);
		trans.concatenate(CoordTrans);
		g.setTransform(trans);

		Shape clip = g.getClip();

		Rectangle hrect = null;
		int hoff = offsetHigh.elementAt(pageIndex);
		hoff--;
		if (hoff < loff)
			hoff = loff;
		try {
			hrect = myEdit.modelToView(hoff);
		} catch (Exception e) {
			throw (new WrapRuntimeException("Model To View Failed", e));
		}
		double hmark = hrect.y + hrect.height;
		Rectangle2D.Double r =
			new Rectangle2D.Double(
				0,
				mark,
				myEdit.getBounds().width,
				hmark - mark);
		g.clip(r);

		myDesk.print(g);

		g.setClip(clip);
	}

	/**
	 * Returns the number of pages that can be printed.
	 * @param imageHeight The pixel image height of the page.
	 * @param offsetLow The low offset of each page index.
	 * @param offsetHigh The high offset of each page index.
	 * @return The calculated number of pages.
	 */
	protected int getNumPages(
		double imageHeight,
		Vector<Integer> offsetLow,
		Vector<Integer> offsetHigh) {
		int num = 1;
		int ioffset = myEdit.getDocument().getStartPosition().getOffset();
		if (offsetLow == null)
			offsetLow = new Vector<Integer>();
		if (offsetHigh == null)
			offsetHigh = new Vector<Integer>();

		try {
			Rectangle rv = myEdit.modelToView(ioffset);
		} catch (Exception e) {
			throw (new WrapRuntimeException("Model To View Failed", e));
		}

		while (ioffset < myEdit.getDocument().getEndPosition().getOffset()) {
			int coffset = ioffset;
			while ((coffset
				< myEdit.getDocument().getEndPosition().getOffset())
				&& withinPage(coffset, num, offsetLow, offsetHigh, imageHeight)) {
				coffset++;
			}

			offsetLow.setSize(num);
			offsetHigh.setSize(num);
			offsetLow.setElementAt(new Integer(ioffset), num - 1);
			offsetHigh.setElementAt(new Integer(coffset), num - 1);

			if (coffset < myEdit.getDocument().getEndPosition().getOffset()) {
				if (!withinPage(coffset,
					num,
					offsetLow,
					offsetHigh,
					imageHeight)) {
					num++;
				}
			}

			ioffset = coffset;
		}

		return (num);
	}

	/**
	* Returns whether a particular offset is within the selected page.
	* @param offset The offset to check.
	* @param pgNum The index of the selected page.
	* @param offsetLow The low offset of each page index.
	* @param offsetHigh The high offset of each page index.
	* @param imageHeight The pixel image height of the page.
	*/
	protected boolean withinPage(
		int offset,
		int pgNum,
		Vector<Integer> offsetLow,
		Vector<Integer> offsetHigh,
		double imageHeight) {
		Rectangle rect = null;
		double imageStrt = 0.0;

		if (pgNum > 1) {
			int loff = offsetLow.elementAt(pgNum - 2);

			Rectangle hrect = null;
			int hoff = offsetHigh.elementAt(pgNum - 2);
			hoff--;
			if (hoff < loff)
				hoff = loff;
			try {
				hrect = myEdit.modelToView(hoff);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Model To View Failed", e));
			}
			imageStrt = hrect.y + hrect.height;
		}

		try {
			rect = myEdit.modelToView(offset);
		} catch (Exception e) {
			throw (new WrapRuntimeException("Model To View Failed", e));
		}
		int max = rect.y + rect.height;
		return (max <= (imageStrt + imageHeight));
	}

	/**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		ScrollingTextAppHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		ScrollingTextApp MyComp = new ScrollingTextApp();
		ProgramDirector.showComponent(MyComp, "Scrolling Text App", argv, true);
	}

}

/**
* A panel that acts as an intermediary between the scroll pane and the enclosed text
* app GUI.
* 
* @author Thorn Green
*/
class ScrollingTextAppPanel
	extends JPanel
	implements Scrollable, ScrollableAlt {
	
	/**
	* The text pane used in the editing.
	*/
	JEditorPane myEdit = null;
	
	/**
	* The scrolling text app. that uses the component.
	*/
	ScrollingTextApp myTextApp = null;

	/**
	* Constructs the panel for a certain scrolling text app.
	* @param inc The component for which to construct the panel.
	*/
	public ScrollingTextAppPanel(JEditorPane in, ScrollingTextApp inc) {
		myEdit = in;
		myTextApp = inc;
	}

	@Override
	public void paintChildren(Graphics g) {
		super.paintChildren(g);
		g.setColor(Color.orange);
		int pw = myTextApp.getPageWidth();
		g.drawLine(pw + 2, -10000, pw + 2, 10000);
	}

	/**
	* Gets the preferred size of the viewport.
	* @return The preferred size of the viewport.
	*/
	public Dimension getPreferredScrollableViewportSize() {
		Dimension d = myEdit.getPreferredScrollableViewportSize();
		Dimension d2 = new Dimension(myTextApp.getPageWidth(), d.height);
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
		return (
			myEdit.getScrollableUnitIncrement(
				visibleRect,
				orientation,
				direction));
	}

	/**
	* Gets the increment for clicking in the scrollbar track.
	* @return The increment for clicking in the scrollbar track.
	*/
	public int getScrollableBlockIncrement(
		Rectangle visibleRect,
		int orientation,
		int direction) {
		return (
			myEdit.getScrollableBlockIncrement(
				visibleRect,
				orientation,
				direction));
	}

	/**
	* Returns whether the scrollable tracks the viewport height.
	* @return Whether the scrollable tracks the viewport height.
	*/
	public boolean getScrollableTracksViewportWidth() {
		return (myEdit.getScrollableTracksViewportWidth());
	}

	/**
	* Returns whether the scrollable tracks the viewport width.
	* @return Whether the scrollable tracks the viewport width.
	*/
	public boolean getScrollableTracksViewportHeight() {
		return (myEdit.getScrollableTracksViewportHeight());
	}

	@Override
	public Dimension getMinimumSize() {
		Dimension d = myEdit.getMinimumSize();
		Dimension d2 = new Dimension(myTextApp.getPageWidth(), d.height);
		return (d2);
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d = myEdit.getPreferredSize();
		Dimension d2 = new Dimension(myTextApp.getPageWidth(), d.height);
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


