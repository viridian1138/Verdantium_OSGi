package verdantium.standard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JViewport;
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
import verdantium.standard.help.WrappingScrollingTextAppHelp;
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
* This demonstrates how to make a scrolling version of a {@link TextApp}.  This one differs
* from {@link ScrollingTextApp} in that text can not wrap outside the page width set in the
* property editor.
* <P>
* @author Thorn Green
*/
public class WrappingScrollingTextApp
	extends TextApp
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
	public void setColors(Color fg, Color bk) {
		super.setColors(fg, bk);
		scp.setBackground(bk);
	}

	/**
	* Constructs the component.
	*/
	public WrappingScrollingTextApp() {
		super();
		docPageFormat.setPropertyName(
			PageWidthPropertyEditor.defaultPageWidthChanged);
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
		JPanel Panel2 = new WrappingScrollingTextAppPanel(myEdit, this);
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

	/**
	* Returns the data flavors that the component can load from persistent storage.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{
				new TransVersionBufferFlavor(
					"Wrapping Scrolling Text App",
					"Wrapping Scrolling Text App"),
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
						"Wrapping Scrolling Text App",
						"Wrapping Scrolling Text App"),
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
							"Wrapping Scrolling Text App",
							"Wrapping Scrolling Text App"),
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
							"Wrapping Scrolling Text App",
							"Wrapping Scrolling Text App"),
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
						"Wrapping Scrolling Text App",
						"Wrapping Scrolling Text App");
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
			try {
				trans = super.savePersistentData(flavor);
			} catch (IOException ex) {
				throw (ex);
			}
		}

		return (trans);
	}

	@Override
	public Object processObjEtherEvent(
		EtherEvent in,
		Object refcon)
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
	* @param in The page width to set.
	*/
	public void setPageWidth(int in) throws IllegalInputException {
		if (in <= 0)
			throw (
				new IllegalInputException("The page width must be at least 1/72 inch."));
		PageWidth = in;
	}

	/**
	* Provides an interface to alter the width of the document page.
	* @param in The width of the document page.
	*/
	public void alterPageWidth(int in) throws IllegalInputException {
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
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		WrappingScrollingTextAppHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		WrappingScrollingTextApp MyComp = new WrappingScrollingTextApp();
		ProgramDirector.showComponent(
			MyComp,
			"Wrapping Scrolling Text App",
			argv,
			true);
	}

	
}


/**
* A panel that acts as an intermediary between the scroll pane and the enclosed text
* app GUI.
* 
* @author Thorn Green
*/
class WrappingScrollingTextAppPanel
	extends JPanel
	implements Scrollable, ScrollableAlt {
	
	/**
	* The text pane used in the editing.
	*/
	JEditorPane MyEdit = null;
	
	/**
	* The scrolling text app. that uses the component.
	*/
	WrappingScrollingTextApp MyTextApp = null;

	/**
	* Constructs the panel for a certain scrolling text app.
	* @param inc The component for which to construct the panel.
	*/
	public WrappingScrollingTextAppPanel(
		JEditorPane in,
		WrappingScrollingTextApp inc) {
		MyEdit = in;
		MyTextApp = inc;
	}

	/**
	* Gets the preferred size of the viewport.
	* @return The preferred size of the viewport.
	*/
	public Dimension getPreferredScrollableViewportSize() {
		Dimension d = MyEdit.getPreferredScrollableViewportSize();
		Dimension d2 = new Dimension(MyTextApp.getPageWidth(), d.height);
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
			MyEdit.getScrollableUnitIncrement(
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
			MyEdit.getScrollableBlockIncrement(
				visibleRect,
				orientation,
				direction));
	}

	/**
	* Returns whether the scrollable tracks the viewport height.
	* @return Whether the scrollable tracks the viewport height.
	*/
	public boolean getScrollableTracksViewportWidth() {
		return (MyEdit.getScrollableTracksViewportWidth());
	}

	/**
	* Returns whether the scrollable tracks the viewport width.
	* @return Whether the scrollable tracks the viewport width.
	*/
	public boolean getScrollableTracksViewportHeight() {
		return (MyEdit.getScrollableTracksViewportHeight());
	}

	@Override
	public Dimension getMinimumSize() {
		Dimension d = MyEdit.getMinimumSize();
		Dimension d2 = new Dimension(MyTextApp.getPageWidth(), d.height);
		return (d2);
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d = MyEdit.getPreferredSize();
		Dimension d2 = new Dimension(MyTextApp.getPageWidth(), d.height);
		return (d2);
	}

	/**
	* Returns whether the width of the component can track beyond its
	* preferred size.
	* @return Whether the width of the component can track beyond its
	* preferred size.
	*/
	public boolean getAltTracksWidth() {
		return (false);
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

