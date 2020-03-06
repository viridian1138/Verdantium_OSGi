package verdantium.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.PropertyChangeSource;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.VerdantiumUtils;
import verdantium.utils.IllegalInputException;
import verdantium.utils.VerticalLayout;

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
*    | 12/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Third-Cut at Error Handling.                                         | Third-Cut at Error Handling.
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
* This is a "first cut" at a standard Print Preview display for Verdantium components.
* There are some known problems with this implementation that are currently being worked.
* Among them, the print preview does not get updated if the user changes the number of
* pages in the document, or changes the desired page dimensions in the Page Setup dialog.
* To prevent these problems, this component will likely be displayed as a modal dialog
* in the future.  It also needs some more bells and whistles.
*
* @author Thorn Green
*/
public class PrintPreviewPropertyEditor
	extends JComponent
	implements VerdantiumPropertiesEditor, ItemListener, PropertyChangeListener {
	
	/**
	* The current page index to be displayed.
	*/
	transient protected int curPage = 0;

	/**
	* The Pageable object to be previewed.
	*/
	transient protected Pageable pages = null;

	/**
	* A pane for scrolling the preview page, if necessary.
	*/
	transient protected JScrollPane scp = null;

	/**
	* The background color for the preview region.
	*/
	transient protected Color bkgnd = UIManager.getColor("Panel.background");

	/**
	* The antialias_hint rendering hint.
	*/
	transient protected Object antialias_hint = RenderingHints.VALUE_ANTIALIAS_ON;

	/**
	* The panel used to display the GUI.
	*/
	transient protected JPanel master = new JPanel();

	/**
	* Button that takes the user to the previous page.
	*/
	transient protected JButton prevButton = new JButton("Previous Page");

	/**
	* Button that takes the user to the next page.
	*/
	transient protected JButton nextButton = new JButton("Next Page");

	/**
	* Button to refresh the display.
	*/
	transient protected JButton refButton = new JButton("Refresh");

	/**
	* Check box to control whether anti-aliasing is used.
	*/
	transient protected JCheckBox antialias =
		new JCheckBox("Use antialias_hint Rendering");

	/**
	* Combo box to scale the display.
	*/
	transient protected JComboBox<String> scaleBox = null;

	/**
	* Factor at which the page is scaled.
	*/
	transient protected double scaleFactor = 1.0 / 2.7;

	/**
	* The data model of the component being edited.
	*/
	private PropertyChangeSource myPage = null;

	/**
	* Gets the GUI for the property editor.
	* @return The GUI for the property editor.
	*/
	public JComponent getGUI() {
		return (master);
	}

	/**
	* Constructs a Print Preview of a Pageable object.
	* @param in The Pageable object to be print-previewed.
	* @param pg The data model of the component being edited.
	*/
	public PrintPreviewPropertyEditor(Pageable in, PropertyChangeSource pg) {
		pages = in;
		myPage = pg;
		myPage.addPropertyChangeListener(this);
		scp =
			new JScrollPane(
				this,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		String[] scales =
			{ "12.5%", "25%", "50%", "100%", "200%", "400%", "800%", "1600%" };
		scaleBox = new JComboBox<String>(scales);
		scaleBox.setEditable(true);
		scaleBox.setSelectedItem((scaleFactor * 100.0) + "%");
		scaleBox.addItemListener(this);
		master.setLayout(new BorderLayout(0, 0));
		master.add("Center", scp);
		JPanel sub = new JPanel();
		master.add("South", sub);
		sub.setLayout(new VerticalLayout());
		JPanel sub3 = new JPanel();
		sub.add("any", new JLabel("Scale Factor : "));
		sub.add("any", scaleBox);
		sub.add("any", antialias);
		sub.add("any", sub3);
		sub3.setLayout(new FlowLayout());
		sub3.add(prevButton);
		sub3.add(nextButton);
		sub3.add(refButton);
		handleButtonState();

		ActionListener MyL = Adapters.createGActionListener(this, "handlePrev");
		prevButton.addActionListener(MyL);
		MyL = Adapters.createGActionListener(this, "handleNext");
		nextButton.addActionListener(MyL);
		MyL = Adapters.createGActionListener(this, "handleRefresh");
		refButton.addActionListener(MyL);
		MyL = Adapters.createGActionListener(this, "handleAntialias");
		antialias.addActionListener(MyL);
	}

	/**
	* Handles the pressing of the "Previous" button by going to the previous page.
	* @param e The input event.
	*/
	public void handlePrev(ActionEvent e) {
		int tmp = curPage - 1;
		if (tmp >= 0)
			curPage = tmp;
		handleButtonState();
		invalidate();
		scp.invalidate();
		scp.getViewport().invalidate();
		revalidate();
		scp.revalidate();
		scp.getViewport().revalidate();
		repaint();
	}

	/**
	* Handles the pressing of the "Next" button by going to the next page.
	* @param e The input event.
	*/
	public void handleNext(ActionEvent e) {
		int tmp = curPage + 1;
		if (tmp < pages.getNumberOfPages())
			curPage = tmp;
		handleButtonState();
		invalidate();
		scp.invalidate();
		scp.getViewport().invalidate();
		revalidate();
		scp.revalidate();
		scp.getViewport().revalidate();
		repaint();
	}

	/**
	* Handles the pressing of the "Refresh" button.
	* @param e The input event.
	*/
	public void handleRefresh(ActionEvent e) {
		repaint();
	}

	/**
	* Handles the pressing of the antialias_hint check box.
	* @param e The input event.
	*/
	public void handleAntialias(ActionEvent e) {
		repaint();
	}

	/**
	* Handles the enabling and disabling of the "Next" and "Previous" buttons.
	*/
	public void handleButtonState() {
		boolean pState = curPage > 0;
		prevButton.setEnabled(pState);
		boolean nState = curPage < (pages.getNumberOfPages() - 1);
		nextButton.setEnabled(nState);
	}

	/**
	* Handles the selection of the scaling item.
	* @param e The input event.
	*/
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			try {
				String sel = (String) (scaleBox.getSelectedItem());
				if (sel.length() < 1)
					throw (
						new IllegalInputException("The scale factor may not be empty."));
				if (sel.charAt(sel.length() - 1) != '%')
					throw (
						new IllegalInputException("The scale factor must end with a percent sign."));
				String nums = sel.substring(0, sel.length() - 1);
				double tmp = (new Double(nums)).doubleValue() / 100.0;
				if (tmp <= 0.0)
					throw (
						new IllegalInputException("The scale factor must be a positive number."));
				scaleFactor = tmp;
				invalidate();
				scp.invalidate();
				scp.getViewport().invalidate();
				revalidate();
				scp.revalidate();
				scp.getViewport().revalidate();
				repaint();
			} catch (NumberFormatException ex) {
				handleThrow(
					new IllegalInputException(
						"The scale factor must be a number.",
						ex));
			} catch (IllegalInputException ex) {
				handleThrow(ex);
			}
		}
	}

	/**
	* Handles property change events by updating the display of the
	* appropriate properties.
	* @param e The input event.
	*/
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getPropertyName() == ProgramDirector.propertyHide) {
			VerdantiumUtils.disposeContainer(this);
		}

	}

	@Override
	public void paint(Graphics gg) {
		double fac = scaleFactor;
		Graphics2D g = (Graphics2D) (gg);

		g.setColor(bkgnd);
		g.fillRect(0, 0, 10000, 10000);

		AffineTransform at = g.getTransform();
		at.translate(25.0, 25.0);
		at.scale(fac, fac);
		g.setTransform(at);

		g.setColor(Color.white);
		if (antialias.isSelected())
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialias_hint);

		PageFormat pg = pages.getPageFormat(curPage);
		Rectangle2D.Double rect =
			new Rectangle2D.Double(0, 0, pg.getWidth(), pg.getHeight());
		g.fill(rect);
		rect =
			new Rectangle2D.Double(
				pg.getImageableX(),
				pg.getImageableY(),
				pg.getImageableWidth(),
				pg.getImageableHeight());
		g.clip(rect);
		Printable prn = getPrintableForPage(curPage);
		try {
			prn.print(g, pg, curPage);
		} catch (Exception e) {
			g.setColor(Color.red);
			g.fill(rect);
		}
	}

	/**
	* Gets the Printable object to use for a particular page index.
	* @return The Printable object to use for a particular page index.
	*/
	protected Printable getPrintableForPage(int pgnum) {
		return (pages.getPrintable(pgnum));
	}

	@Override
	public Dimension getMinimumSize() {
		double fac = scaleFactor;
		PageFormat pg = pages.getPageFormat(curPage);
		return (
			new Dimension(
				(int) (fac * pg.getWidth() + 50),
				(int) (fac * pg.getHeight() + 50)));
	}

	@Override
	public Dimension getPreferredSize() {
		return (getMinimumSize());
	}

	/**
	* Handles Ether Events.  Does nothing.
	* @param in The event to handle.
	* @param refcon A reference to context data for the event.
	* @return The result of handling the event, or null if there is no result.
	*/
	public Object processObjEtherEvent(EtherEvent in, Object refcon) {
		return (null);
	}

	/**
	* Handles the destruction of the component.
	*/
	public void handleDestroy() {
		myPage.removePropertyChangeListener(this);
	}

	/**
	* Gets the input data flavors supported.  None are supported in this class.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		return (null);
	}
	
	/**
	* Gets the output data flavors supported.  None are supported in this class.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		return (null);
	}
	
	/**
	* Interface to load persistent data.  Does nothing.
	*/
	public void loadPersistentData(DataFlavor flavor, Transferable trans) {
	}
	
	/**
	* Interface to save persistent data.  Does nothing.
	*/
	public Transferable savePersistentData(DataFlavor flavor) {
		return (null);
	}

	/**
	* Handles the throwing of an error or exception.
	* @param e The input error or exception.
	*/
	public void handleThrow(Throwable in) {
		VerdantiumUtils.handleThrow(in, this, myPage);
	}

}

