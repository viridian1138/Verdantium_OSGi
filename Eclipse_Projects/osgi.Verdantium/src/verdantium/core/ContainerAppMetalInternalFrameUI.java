package verdantium.core;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.plaf.ComponentUI;

import verdantium.VerdantiumMetalInternalFrameUI;

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
* This class defines the Metal user interface for a {@link ContainerApp} document frame.
* Do not use this class directly unless you need to make serious modifications.
* In most cases, you should consider this class to be an internal feature of
* {@link ContainerApp}.
* 
* @author Thorn Green
*/
public class ContainerAppMetalInternalFrameUI
	extends VerdantiumMetalInternalFrameUI {

	/**
	 * Constructor.
	 * @param b The internal frame onto which to apply the UI.
	 */
	public ContainerAppMetalInternalFrameUI(JInternalFrame b) {
		super(b);
	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);

		if (getEditorMode(c) == EditorControl.EditMode) {
			c.setBorder(new EmptyBorderResource(0, 0, 0, 0));
		}
	}

	/**
	* Creates a UI for ContainerAppInternalFrame.
	* @param c The component for which to create the UI.
	* @return The created UI.
	*/
	public static ComponentUI createUI(JComponent c) {
		return new ContainerAppMetalInternalFrameUI((JInternalFrame) c);
	}

	/**
	* Gets the current editor mode.
	* @return The current editor mode.
	*/
	protected int getEditorMode(JComponent in) {
		return (((ContainerAppInternalFrame) in).getEditorMode());
	}

	/**
	* Creates a dummy component of zero size.
	* @return A dummy component of zero size.
	*/
	protected JComponent createDummyComponent() {
		JComponent MyC = new JPanel();
		MyC.setMinimumSize(new Dimension(0, 0));
		MyC.setPreferredSize(new Dimension(0, 0));
		return (MyC);
	}

	@Override
	protected JComponent createNorthPane(JInternalFrame w) {
		if (getEditorMode(w) != EditorControl.MoveMode)
			return (createDummyComponent());
		else
			return (super.createNorthPane(w));
	}

	@Override
	protected JComponent createSouthPane(JInternalFrame w) {
		if (getEditorMode(w) != EditorControl.MoveMode)
			return (createDummyComponent());
		else
			return (super.createSouthPane(w));
	}

	@Override
	protected JComponent createWestPane(JInternalFrame w) {
		if (getEditorMode(w) != EditorControl.MoveMode)
			return (createDummyComponent());
		else
			return (super.createWestPane(w));
	}

	@Override
	protected JComponent createEastPane(JInternalFrame w) {
		if (getEditorMode(w) != EditorControl.MoveMode)
			return (createDummyComponent());
		else
			return (super.createEastPane(w));
	}

}

