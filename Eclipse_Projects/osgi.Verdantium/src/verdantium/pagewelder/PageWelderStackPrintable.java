package verdantium.pagewelder;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

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
* This class is a prototype for printing PageWelder stacks.  It is not fully
* documented at this time.
*
* @author Thorn Green
*/
class PageWelderStackPrintable implements Printable {
	
	/**
	 * The component being printed.
	 */
	private PageWelder pWelder;

	/**
	 * Constructor.
	 * @param in The component being printed.
	 */
	public PageWelderStackPrintable(PageWelder in) {
		pWelder = in;
	}

	/**
	* Prints the contents of the panel.
	* @param gi The context into which to print.
	* @param pf The input format of the page.
	* @param pageIndex The index of the page to be prointed.
	*/
	public int print(Graphics gi, PageFormat pf, int pageIndex)
		throws PrinterException {
		double height = pf.getImageableHeight();
		if (pageIndex >= getNumPages(height))
			return (Printable.NO_SUCH_PAGE);

		AffineTransform CoordTrans = new AffineTransform();
		Graphics2D g = (Graphics2D) gi;
		CoordTrans.setToTranslation(pf.getImageableX(), pf.getImageableY());
		AffineTransform trans = g.getTransform();
		trans.concatenate(CoordTrans);
		g.setTransform(trans);
		printContent(gi, pf, pageIndex);
		return (Printable.PAGE_EXISTS);
	}

	/**
	* Prints the content of a particular page.
	* @param gi The context into which to print.
	* @param pf The input format of the page.
	* @param pageIndex The index of the page to be prointed.
	*/
	protected void printContent(Graphics gi, PageFormat pf, int pageIndex)
		throws PrinterException {
		Graphics2D g = (Graphics2D) gi;

		PageWelderDesktopPane pane = pWelder.getObjectFromCardIndex(pageIndex);
		pane.print(g);
	}

	/**
	* Returns the number of pages that can be printed.
	* @param imageHeight The height of the image into which each page is printed.
	*/
	public int getNumPages(double imageHeight) {
		return (pWelder.getNumCards());
	}

	
}

