package verdantium.standard;

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
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Needed to make multiple improvements to TextApp.                     | Improved printing and macro handling among other things.
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
* This demonstrates how to make a printable for a scrolling version of a {@link TextApp}.
* <P>
* @author Thorn Green
*/
public class ScrollingTextAppPrintable extends Object implements Printable {
	
	/**
	 * The user interface to be rendered to the print's graphics context.
	 */
	protected JComponent printUI = null;
	
	/**
	 * The text pane being printed.
	 */
	protected JEditorPane textPane = null;
	
	/**
	 * The low offset of each page index.
	 */
	protected Vector<Integer> offsetLow = new Vector<Integer>();
	
	/**
	 * The high offset of each page index.
	 */
	protected Vector<Integer> offsetHigh = new Vector<Integer>();
	
	/**
	 * The calculated number of pages to be printed.
	 */
	protected int num_pages = 0;

	/**
	 * Constructs the printable.
	 * @param inPrintUI The user interface to be rendered to the print's graphics context.
	 * @param inTextPane The text pane being printed.
	 * @param pf The format of the pages to print.
	 */
	public ScrollingTextAppPrintable(
		JComponent inPrintUI,
		JEditorPane inTextPane,
		PageFormat pf) {
		printUI = inPrintUI;
		textPane = inTextPane;
		double image_height = pf.getImageableHeight();
		num_pages = getNumPages(image_height, offsetLow, offsetHigh);
	}

	/**
	 * Prints the contents of the scrolling text app.
	 * @param gi The graphics context into which to render the page.
	 * @param pf The format of the page to print.
	 * @param pageIndex The page index of the page to print.
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
			lrect = textPane.modelToView(loff);
		} catch (Exception e) {
			throw (new WrapRuntimeException("Model To View Failed", e));
		}
		double mark = lrect.y;

		AffineTransform coordTrans = new AffineTransform();
		coordTrans.setToTranslation(0.0, -mark);
		trans.concatenate(coordTrans);
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
		int ioffset = textPane.getDocument().getStartPosition().getOffset();
		if (offsetLow == null)
			offsetLow = new Vector<Integer>();
		if (offsetHigh == null)
			offsetHigh = new Vector<Integer>();

		try {
			textPane.modelToView(ioffset);
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
				hrect = textPane.modelToView(hoff);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Model To View Failed.", e));
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
	 * Gets the calculated number of pages to be printed.
	 * @return The calculated number of pages to be printed.
	 */
	public int getNumPages() {
		return (num_pages);
	}

	
}

