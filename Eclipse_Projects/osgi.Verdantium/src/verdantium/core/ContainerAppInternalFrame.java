package verdantium.core;

import java.awt.Graphics;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.plaf.InternalFrameUI;

import jundo.util.pdx_HashMapSh_pdx_ObjectRef;
import meta.WrapRuntimeException;
import verdantium.ProgramDirector;
import verdantium.VerdantiumUndoableInternalFrame;
import verdantium.undo.UndoManager;
import verdantium.utils.VTextProperties;

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
*    | 09/29/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for multiple PLAFs.                                          | Made code changes to support multiple PLAFs.
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
* Internal frame class for {@link ContainerApp}.
* Do not use this class directly unless you need to make serious modifications.
* In most cases, you should consider this class to be an internal feature of
* {@link ContainerApp}.
* 
* @author Thorn Green
*/
public class ContainerAppInternalFrame
	extends VerdantiumUndoableInternalFrame {
	
	/**
	* A listener that the class uses to determine whether only the designer is
	* editing the corresponding component.
	*/
	transient private OnlyDesignerEditListener myEdit = null;
	
	/**
	 * Map of Plaf names to Internal Frame classes.
	 */
	transient private static HashMap<String,Class<? extends InternalFrameUI>> plafMapCont = null;

	/**
	 * Constructs the internal frame.
	 * @param _mgr The multi-level undo manager.
	 * @param _map Undoable reference to the state information of the frame.
	 */
	public ContainerAppInternalFrame(
		UndoManager _mgr,
		pdx_HashMapSh_pdx_ObjectRef _map) {
		super(null, true, true, false, false, _mgr, _map, null);
		rootPane.setOpaque(false);
		((JPanel) (rootPane.getContentPane())).setOpaque(false);
		rootPane.getLayeredPane().setOpaque(false);
	}

	/**
	* Returns whether the frame is opaque.  The frame may be non-opaque depending
	* on the transparency of the embedded component.
	* @return Whether the frame is opaque.
	*/
	public boolean isOpaque() {
		return (getComponent().getGUI().isOpaque());
	}

	@Override
	public void update(Graphics g) {
	}

	/**
	* Returns the editor mode for this frame.
	* @return The editor mode for thr frame.
	*/
	public int getEditorMode() {
		if (myEdit == null)
			return (EditorControl.getEditorMode());
		else
			return (EditorControl.getEditorMode(myEdit.isOnlyDesignerEdits()));
	}

	/**
	* Sets the listener for an embedded component's designer edit property.
	* @param in The listener to be set.
	*/
	public void setOnlyDesignerEditListener(OnlyDesignerEditListener in) {
		myEdit = in;
		updateUI();
	}

	@Override
	public void updateUI() {
		super.updateUI();
	}

	@Override
	protected HashMap<String,Class<? extends InternalFrameUI>> getPlafMap() {
		return (getPlafMapCont());
	}

	/**
	 * Gets the map of Plaf names to Internal Frame classes.
	 * @return The map of Plaf names to Internal Frame classes.
	 */
	private static HashMap<String,Class<? extends InternalFrameUI>> getPlafMapCont() {
		try {
			if (plafMapCont == null) {
				plafMapCont = new HashMap<String,Class<? extends InternalFrameUI>>();
				String VersionKey = "PlafConfig09/01A";
				VTextProperties prop = ProgramDirector.getPlafProperties();
				int num_plafs = prop.getInt(VersionKey + ".NumPlaf");
				int count;

				for (count = 0; count < num_plafs; ++count) {
					String plaf_name =
						prop.getPropertyNonNull(
							VersionKey + ".Plaf_" + count + ".verd.PlafClass");
					String ui_name =
						prop.getPropertyNonNull(
							VersionKey
								+ ".Plaf_"
								+ count
								+ ".core.ContFrameClass");
					plafMapCont.put(plaf_name, (Class<? extends InternalFrameUI>)( Class.forName(ui_name) ) );
				}
			}
		} catch (Exception ex) {
			throw (new WrapRuntimeException("IntFrame Plaf Failed", ex));
		}

		return (plafMapCont);
	}

}

