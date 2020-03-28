package verdantium;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;

import com.sun.java.swing.plaf.motif.MotifInternalFrameUI;

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
* Implements a Motif internal frame UI for verdantium.  This UI is mainly intended
* to skirt around some bugs in Swing's InternalFrame implementation.  To accomplish
* this, the class disables the Swing glass pane entirely.  The UI works much better
* with the glass pane disabled, especially for the purpose of creating compound
* documents.  With the glass pane, a frame's contents are "dead" to mouse events if
* the frame is not selected.  It would defeat the purpose of {@link verdantium.core.ContainerApp}
* if one had to select a particular embedded frame before doing anything with its contents.
*
* The InternalFrame in the current version of Swing does not have a focus manager in
* the same sense that the JFrame does, and this causes problems for getting the proper
* keyboard focus in an internal frame (Sun lists this as bug number 4109910).
* Removing the glass pane makes it easier to deal with these problems.  Finally, the
* author has noticed several problems with frames in nested JDesktopPane instances
* (this is listed by Sun as bug number 4188846).
* Glass pane does not seem to know what to do when JDesktopPanes frames are nested.
* Getting rid of the glass pane fixes that, too.
*
* There are few reasons to make direct use of this class.
* {@link verdantium.core.ContainerAppMotifInternalFrameUI} already makes
* seemingly all of the modifications that would really be useful.
*
* This class makes some use of Sun's Swing source code.  Given the amount of code used,
* and the purpose for which it is used, the author considers this to be fair use.
*
* @author Thorn Green
*/
public class VerdantiumMotifInternalFrameUI extends MotifInternalFrameUI {

	/**
	 * Constructor.
	 * @param b The internal frame in which to embed.
	 */
	public VerdantiumMotifInternalFrameUI(JInternalFrame b) {
		super(b);
	}

	/**
	 * Creates the user interface to be embedded in the JComponent.
	 * @param c The internal frame in which to embed.
	 * @return The created user interface.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new VerdantiumMotifInternalFrameUI((JInternalFrame) c);
	}

	/**
	 * Gets the glass pane dispatcher for the frame.
	 * @return The glass pane dispatcher for the frame.
	 */
	protected MouseInputListener getGlassPaneDispatcher() {
		return (glassPaneDispatcher);
	}

	@Override
	protected void activateFrame(JInternalFrame f) {
		super.activateFrame(f);
	}

	@Override
	protected void deactivateFrame(JInternalFrame f) {
		super.deactivateFrame(f);
	}

	@Override
	protected PropertyChangeListener createPropertyChangeListener() {
		return new VerdantiumInternalFramePropertyChangeListener();
	}

	/**
	 * Property change listener for internal frames.
	 * 
	 * @author tgreen
	 *
	 */
	public class VerdantiumInternalFramePropertyChangeListener
		extends InternalFramePropertyChangeListener {
	
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			String prop = (String) evt.getPropertyName();
			JInternalFrame f = (JInternalFrame) evt.getSource();
			Object newValue = evt.getNewValue();
			Object oldValue = evt.getOldValue();
			// aSSERT(frame == f) - This should always be true

			if (JInternalFrame.IS_SELECTED_PROPERTY.equals(prop)) {
				Component glassPane = f.getGlassPane();
				if (newValue == Boolean.TRUE && oldValue == Boolean.FALSE) {
					activateFrame(f);
					glassPane.removeMouseListener(getGlassPaneDispatcher());
					glassPane.removeMouseMotionListener(
						getGlassPaneDispatcher());
					glassPane.setVisible(false);
				} else if (
					newValue == Boolean.FALSE && oldValue == Boolean.TRUE) {
					deactivateFrame(f);
					/* glassPane.addMouseListener( getGlassPaneDispatcher() );
							glassPane.addMouseMotionListener( getGlassPaneDispatcher() );
					glassPane.setVisible(true); */
				}
			} else {
				super.propertyChange(evt);
			}

		}
	}

	
}

