package verdantium.vhelp;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.RepaintManager;

import meta.WrapRuntimeException;

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
* This demonstrates how to make a printable for a scrolling version of a {@link VerdantiumHelp}.
* <P>
* @author Thorn Green
*/
public class ScrollingVerdantiumHelpPrintable
	extends Object
	implements Printable {
	
	/**
	 * The user interface pane to be printed.  The text pane is embedded in this pane.
	 */
	protected JComponent printUI = null;
	
	/**
	 * The pane of help text being printed.
	 */
	protected JEditorPane textPane = null;
	
	/**
	 * The pixel offset of the low page break for each page.
	 */
	protected Vector<Integer> offsetLow = new Vector<Integer>();
	
	/**
	 * The pixel offset of the high page break for each page.
	 */
	protected Vector<Integer> offsetHigh = new Vector<Integer>();
	
	/**
	 * The number of pages in the help text.
	 */
	protected int num_pages = 0;

	/**
	 * Constructs the printable.
	 * @param inPrintUI The user interface pane to be printed.  The text pane is embedded in this pane.
	 * @param inTextPane The pane of help text being printed.
	 * @param pf The format of the page in which to print.
	 */
	public ScrollingVerdantiumHelpPrintable(
		JComponent inPrintUI,
		JEditorPane inTextPane,
		PageFormat pf) {
		printUI = inPrintUI;
		textPane = inTextPane;
		double image_height = pf.getImageableHeight();
		num_pages = getNumPages(image_height, offsetLow, offsetHigh);
	}

	/**
	* Prints the contents of the panel.
	* @param gi The graphics context into which to print.
	* @param pf The format of the page onto which to print.
	* @param pageIndex The index of the current page.
	*/
	public int print(Graphics gi, PageFormat pf, int pageIndex)
		throws PrinterException {
		if (pageIndex >= num_pages)
			return (Printable.NO_SUCH_PAGE);

		AffineTransform CoordTrans = new AffineTransform();
		Graphics2D g = (Graphics2D) gi;
		JComponent MyC = printUI;
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
	* @param gi The graphics context into which to print.
	* @param pf The format of the page onto which to print.
	* @param pageIndex The index of the current page.
	* @param offsetLow The pixel offset of the low page break for each page.
	* @param offsetHigh The pixel offset of the high page break for each page.
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
			lrect = textPane.modelToView(loff);
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
			hrect = textPane.modelToView(hoff);
		} catch (Exception e) {
			throw (new WrapRuntimeException("Model To View Failed", e));
		}
		double hmark = hrect.y + hrect.height;
		Rectangle2D.Double r =
			new Rectangle2D.Double(
				0,
				mark,
				textPane.getBounds().width,
				hmark - mark);
		g.clip(r);

		printUI.print(g);

		g.setClip(clip);
	}

	/**
	* Returns the number of pages that can be printed.
	* @param imageHeight The overall image height of the text pane.
	* @param offsetLow The pixel offset of the low page break for each page.
	* @param offsetHigh The pixel offset of the high page break for each page.
	* @return The number of pages.
	*/
	protected int getNumPages(
		double imageHeight,
		Vector<Integer> offsetLow,
		Vector<Integer> offsetHigh) {
		int num = 1;
		int ioffset = textPane.getDocument().getStartPosition().getOffset();
		if (offsetLow == null)
			offsetLow = new Vector<Integer>();
		if (offsetHigh == null)
			offsetHigh = new Vector<Integer>();

		try {
			Rectangle rv = textPane.modelToView(ioffset);
		} catch (Exception e) {
			throw (new WrapRuntimeException("Model To View Failed", e));
		}

		while (ioffset < textPane.getDocument().getEndPosition().getOffset()) {
			int coffset = ioffset;
			while ((coffset
				< textPane.getDocument().getEndPosition().getOffset())
				&& withinPage(coffset, num, offsetLow, offsetHigh, imageHeight)) {
				coffset++;
			}

			offsetLow.setSize(num);
			offsetHigh.setSize(num);
			offsetLow.setElementAt(new Integer(ioffset), num - 1);
			offsetHigh.setElementAt(new Integer(coffset), num - 1);

			if (coffset
				< textPane.getDocument().getEndPosition().getOffset()) {
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
	* @param offset The vertical offset to check.
	* @param pgNum The number of the selected page.
	* @param offsetLow The pixel offset of the low page break for each page.
	* @param offsetHigh The pixel offset of the high page break for each page.
	* @param imageHeight The overall image height of the text pane.
	* @return Whether a particular offset is within the selected page.
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
				hrect = textPane.modelToView(hoff);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Model To View Failed", e));
			}
			imageStrt = hrect.y + hrect.height;
		}

		try {
			rect = textPane.modelToView(offset);
		} catch (Exception e) {
			throw (new WrapRuntimeException("Model To View Failed", e));
		}
		int max = rect.y + rect.height;
		return (max <= (imageStrt + imageHeight));
	}

	/**
	 * Gets the calculated number of pages.
	 * @return The calculated number of pages.
	 */
	public int getNumPages() {
		return (num_pages);
	}

	
}

