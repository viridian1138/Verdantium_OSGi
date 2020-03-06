package verdantium.core;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.VerdantiumUtils;
import verdantium.utils.VerticalLayout;
import verdantium.xapp.PropertyEditAdapter;

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
*    | 03/11/2001            | Thorn Green (viridian_1138@yahoo.com)           | Property editor needed retargeting to work with CAGD Kit.            | Added retargeting ability.
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
* Implements a client-independent editor for setting the background color and transparency
* of a component.
* 
* @author Thorn Green
*/
public class BackgroundPropertyEditor extends PropertyEditAdapter {
	
	/**
	 * Panel containing the GUI for the property editor.
	 */
	private JPanel myPanel = new JPanel();
	
	/**
	 * Checkbox for editing whether the background is transparent.
	 */
	private JCheckBox transCheck = new JCheckBox("Transparent");
	
	/**
	 * Color cell for editing the background color.
	 */
	private ColorCell backCell = new ColorCell();
	
	/**
	 * The target object being edited.
	 */
	private BackgroundListener target = null;
	
	/**
	 * Whether the property editor is currently updating.
	 */
	private boolean updating = false;

	/**
	* The client must fire a property change event with this property name every time
	* its background changes.
	*/
	public static final String AppBackground = "AppBackground";

	/**
	* Creates a property editor for a client <code>in</code>.  The <code>color</code> and
	* <code>trans</code> parameters determine whether the editor will edit color and
	* transparency respectively.
	* @param in The target object being edited.
	* @param color The initial background color.
	* @param trans Whether the background is initially transparent.
	*/
	public BackgroundPropertyEditor(
		BackgroundListener in,
		boolean color,
		boolean trans) {
		target = in;
		myPanel.setLayout(new VerticalLayout(true));
		if (color)
			myPanel.add("stretch", backCell.getGUI());
		if (trans)
			myPanel.add("any", transCheck);
		backCell.setColor(target.getBackgroundColor());
		transCheck.getModel().setSelected(!(target.getOpaqueFlag()));
		backCell.setOutlineBorder();

		ActionListener CellL =
			Adapters.createGActionListener(this, "handleCellColorChange");
		backCell.addColorActionListener(CellL);

		ItemListener item = Adapters.createGItemListener(this, "handleButton");
		transCheck.addItemListener(item);

		target.addPropertyChangeListener(this);
	}

	/**
	* Handles property change events.
	* @param e The input event.
	*/
	public void propertyChange(PropertyChangeEvent e) {
		if (!updating && (e.getPropertyName() == AppBackground)) {
			updating = true;
			backCell.setColor(target.getBackgroundColor());
			transCheck.getModel().setSelected(!(target.getOpaqueFlag()));
			myPanel.repaint();
			updating = false;
		}

	}

	/**
	* Sets the target object being edited.
	* @param in The target object being edited.
	*/
	public void setTarget(BackgroundListener in) {
		target.removePropertyChangeListener(this);
		target = in;
		PropertyChangeEvent pc =
			new PropertyChangeEvent(this, AppBackground, null, null);
		propertyChange(pc);
		target.addPropertyChangeListener(this);
	}

	/**
	* Handles color change events from the {@link ColorCell} component used to edit
	* the background color.
	* @param e The input event.
	*/
	public void handleCellColorChange(ActionEvent e) {
		try {
			handleBackgroundChange();
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles a change in the checkbox used to set the transparency.
	* @param evt The input event.
	*/
	public void handleButton(ItemEvent evt) {
		try {
			handleBackgroundChange();
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles a general change in the background state coming from this editor's
	* GUI.  Note: background changes coming from the client are not handled here.
	* To accept the background change, the client must implement the
	* {@link PropertyEditEtherEvent#setBackgroundState} Ether Event.
	*/
	protected void handleBackgroundChange() throws Throwable {
		if (!updating) {
			try {
				updating = true;
				EtherEvent send =
					new PropertyEditEtherEvent(
						this,
						PropertyEditEtherEvent.setBackgroundState,
						null,
						target);
				boolean sel = !(transCheck.getModel().isSelected());
				Color col = backCell.getColor();
				Object[] param = { col, new Boolean(sel)};
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);
			} catch (Throwable ex) {
				throw (ex);
			} finally {
				updating = false;
			}
		}
	}

	/**
	* Returns the GUI of the property editor.
	* @return The GUI of the property editor.
	*/
	public JComponent getGUI() {
		return (myPanel);
	}

	/**
	* Handles the destruction of the property editor
	* @return The destruction of the property editor.
	*/
	public void handleDestroy() {
		backCell.handleDestroy();
		target.removePropertyChangeListener(this);
	}

	/**
	* Gets the color cell used by the editor.
	* @return The color cell used by the editor.
	*/
	public ColorCell getBackCell() {
		return (backCell);
	}

	/**
	* Handles the throwing of an error or exception.
	* @param in The input error or exception.
	*/
	public void handleThrow(Throwable in) {
		VerdantiumUtils.handleThrow(in, this, target);
	}

	
}

