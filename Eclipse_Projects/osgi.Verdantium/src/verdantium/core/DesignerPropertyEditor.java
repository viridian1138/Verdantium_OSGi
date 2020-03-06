package verdantium.core;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import meta.WrapRuntimeException;
import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
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
* Implements a client-independent editor for setting the "OnlyDesignerEdits" property of
* a component.
* 
* @author Thorn Green
*/
public class DesignerPropertyEditor extends PropertyEditAdapter {
	
	/**
	* Property type that the client must fire whenever its designer edit property changes.
	*/
	public static final String AppOnlyDesignerEdits = "AppOnlyDesignerEdits";

	/**
	 * The panel containing the GUI for the property editor.
	 */
	private JPanel myPanel = new JPanel();
	
	/**
	 * Checkbox used to set the "only designer edits" state.
	 */
	private JCheckBox DesignerCheck = new JCheckBox("Only Designer Edits");
	
	/**
	 * The target object being edited.
	 */
	private OnlyDesignerEditListener target = null;
	
	/**
	 * Whether the state of the property editor is updating.
	 */
	private boolean updating = false;

	
	/**
	* Constructor.
	* @param in The target object being edited.
	*/
	public DesignerPropertyEditor(OnlyDesignerEditListener in) {
		target = in;
		myPanel.setLayout(new BorderLayout(0, 0));
		myPanel.add("South", DesignerCheck);
		DesignerCheck.getModel().setSelected(target.isOnlyDesignerEdits());

		ItemListener item = Adapters.createGItemListener(this, "handleButton");
		DesignerCheck.addItemListener(item);

		target.addPropertyChangeListener(this);
	}

	/**
	* Handles a property change.
	* @param e The input event.
	*/
	public void propertyChange(PropertyChangeEvent e) {
		if (!updating && (e.getPropertyName() == AppOnlyDesignerEdits)) {
			updating = true;
			DesignerCheck.getModel().setSelected(target.isOnlyDesignerEdits());
			myPanel.repaint();
			updating = false;
		}

	}

	/**
	* Handles a change to the checkbox for the editor's GUI.
	* @param evt The input event.
	*/
	public void handleButton(ItemEvent evt) {
		handleDesignerChange();
	}

	/**
	* Handles a change in the designer property from the client.
	*/
	public void handleDesignerChange() {
		if (!updating) {
			try {
				EtherEvent send =
					new PropertyEditEtherEvent(
						this,
						PropertyEditEtherEvent.setOnlyDesignerEdit,
						null,
						target);
				boolean sel = DesignerCheck.getModel().isSelected();
				Object param = new Boolean(sel);
				send.setParameter(param);
				ProgramDirector.fireEtherEvent(send, null);
			} catch (Throwable ex) {
				throw (new WrapRuntimeException("Hndl Des Chg Failed", ex));
			} finally {
				updating = false;
			}
		}
	}

	/**
	* Sets the target object being edited.
	* @param in The target object being edited.
	*/
	public void setTarget(OnlyDesignerEditListener in) {
		target.removePropertyChangeListener(this);
		target = in;
		PropertyChangeEvent pc =
			new PropertyChangeEvent(this, AppOnlyDesignerEdits, null, null);
		propertyChange(pc);
		target.addPropertyChangeListener(this);
	}

	/**
	* Gets the GUI of the property editor.
	* @return The GUI of the property editor.
	*/
	public JComponent getGUI() {
		return (myPanel);
	}

	/**
	* Handles the destruction of the property editor.
	*/
	public void handleDestroy() {
		target.removePropertyChangeListener(this);
	}

	
}

