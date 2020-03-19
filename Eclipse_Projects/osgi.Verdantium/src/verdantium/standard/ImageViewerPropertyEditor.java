package verdantium.standard;

import java.awt.datatransfer.DataFlavor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.EtherEventPropertySource;
import verdantium.ProgramDirector;
import verdantium.core.DefaultPropertyEditor;
import verdantium.core.PropertyEditEtherEvent;
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
*    | 03/10/2001            | Thorn Green (viridian_1138@yahoo.com)           | Add support for stretched and tiled images.                          | Created property editor to support the UI additions.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
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
* Property editor for the image viewer component.
* <P>
* @author Thorn Green
*/
public class ImageViewerPropertyEditor extends DefaultPropertyEditor {
	
	/**
	* Radio button to set the center mode.
	*/
	private JRadioButton centerButton;
	
	/**
	* Radio button to set the tile mode.
	*/
	private JRadioButton tileButton;
	
	/**
	* Radio button to set the stretch mode.
	*/
	private JRadioButton stretchButton;
	
	/**
	* Radio button to set the dock mode.
	*/
	private JRadioButton dockButton;

	/**
	* Set to true when updating a property to avoid recursion.
	*/
	protected boolean updating = false;

	/**
	* Handles the destruction of the component by calling handleDestroy() on all
	* embedded components.
	*/
	public void handleDestroy() {
		super.handleDestroy();
	}

	/**
	* Handles property change events by updating the display of the
	* appropriate properties.
	* @param e The input event.
	*/
	public void propertyChange(PropertyChangeEvent e) {
		super.propertyChange(e);

		if (e.getPropertyName() == ImageViewer.ImageViewerDisplayModeChanged)
			handleModeChange(e);
	}

	/**
	* Handles a change in the display mode of the image viewer being edited.
	* @param e The input event.
	*/
	protected void handleModeChange(PropertyChangeEvent e) {
		if (!updating) {
			updating = true;
			switch (target.getDisplayMode()) {
				case ImageViewer.CENTER_MODE :
					centerButton.getModel().setSelected(true);
					break;

				case ImageViewer.TILE_MODE :
					tileButton.getModel().setSelected(true);
					break;

				case ImageViewer.STRETCH_MODE :
					stretchButton.getModel().setSelected(true);
					break;

				case ImageViewer.DOCK_MODE :
					dockButton.getModel().setSelected(true);
					break;

			}
			updating = false;
			MyPanel.repaint();
		}

	}

	/**
	* Shunts the superclass method for adding the tabs.
	* @param in The property source from which to define the tabs.
	* @param inp Properties defining which tabs to display.
	*/
	protected void addTabs(EtherEventPropertySource in, Properties inp) {
	}

	/**
	* Adds the tabs for editing the button to the superclass tabs.
	* @param in The property source from which to define the tabs.
	* @param inp Properties defining which tabs to display.
	*/
	protected void addTabsSub(EtherEventPropertySource in, Properties inp) {
		target = (ImageViewer) in;

		if (!lacksProperty("Scrolling", inp))
			inp.put("NoTransparent", in);

		centerButton = new JRadioButton("Center");
		tileButton = new JRadioButton("Tile");
		stretchButton = new JRadioButton("Stretch");
		dockButton = new JRadioButton("Dock");

		JPanel p2 = new JPanel();
		TabPane.add("Display", p2);
		p2.setLayout(new VerticalLayout(1));
		p2.add("any", centerButton);
		p2.add("any", tileButton);
		p2.add("any", stretchButton);
		p2.add("any", dockButton);

		super.addTabs(in, inp);

		centerButton.getModel().setSelected(
			target.getDisplayMode() == ImageViewer.CENTER_MODE);
		tileButton.getModel().setSelected(
			target.getDisplayMode() == ImageViewer.TILE_MODE);
		stretchButton.getModel().setSelected(
			target.getDisplayMode() == ImageViewer.STRETCH_MODE);
		dockButton.getModel().setSelected(
			target.getDisplayMode() == ImageViewer.DOCK_MODE);

		ButtonGroup MyGrp = new ButtonGroup();
		MyGrp.add(centerButton);
		MyGrp.add(tileButton);
		MyGrp.add(stretchButton);
		MyGrp.add(dockButton);

		ItemListener item = Adapters.createGItemListener(this, "handleButton");
		centerButton.addItemListener(item);
		tileButton.addItemListener(item);
		stretchButton.addItemListener(item);
		dockButton.addItemListener(item);
	}

	/**
	* Handles one of the buttons for a mode change.
	* @param evt The input event.
	*/
	public void handleButton(ItemEvent evt) {
		if (!updating) {
			try {
				if (((JRadioButton) (evt.getSource()))
					.getModel()
					.isSelected()) {
					updating = true;
					if (centerButton.getModel().isSelected())
						setDisplayMode(ImageViewer.CENTER_MODE);
					if (tileButton.getModel().isSelected())
						setDisplayMode(ImageViewer.TILE_MODE);
					if (stretchButton.getModel().isSelected())
						setDisplayMode(ImageViewer.STRETCH_MODE);
					if (dockButton.getModel().isSelected())
						setDisplayMode(ImageViewer.DOCK_MODE);
				}
			} catch (Throwable ex) {
				handleThrow(ex);
			} finally {
				updating = false;
			}
		}
	}

	/**
	* Sets the display mode of the image viewer being edited.
	* @param in The display mode to set on the image viewer.
	*/
	protected void setDisplayMode(int in) throws Throwable {
		EtherEvent send =
			new PropertyEditEtherEvent(
				this,
				ImageViewer.setImageViewerDisplayMode,
				null,
				target);
		send.setParameter(new Integer(in));
		ProgramDirector.fireEtherEvent(send, null);
	}

	/**
	* Constructs the property editor.
	*/
	public ImageViewerPropertyEditor(ImageViewer in, Properties inp) {
		super(in, inp);
		addTabsSub(in, inp);
	}

	/**
	* Returns the persistent data flavors supported by the property editor, which are none.
	* @return The persistent data flavors supported by the property editor, which are none.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		return (null);
	}

	/**
	* The ImageViewer being edited by the property editor.
	*/
	protected ImageViewer target;
	
	
}

