package verdantium;

import java.awt.AWTEventMulticaster;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;

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
*    | 10/19/2001            | Thorn Green (viridian_1138@yahoo.com)           | Expanded window menus for GeoFrame/GeoPad.                           | Added functionality relating to window menus.
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
* Defines a frame that holds a Verdantium component.  One usually won't need to make
* direct use of this class.
* 
* @author Thorn Green
*/
public class VerdantiumFrame extends JFrame implements PropertyChangeListener {

	/**
	 * Constructor.
	 * @param comp The component embedded in the frame.
	 */
	public VerdantiumFrame(VerdantiumComponent comp) {
		super();
		myComp = comp;
		VerdantiumApplet.addClassPropertyChangeListener(this);
	}

	/**
	* Gets the component embedded in the frame.
	* @return The component embedded in the frame.
	*/
	public VerdantiumComponent getComponent() {
		return (myComp);
	}

	/**
	* Sets the component embedded in the frame.
	* @param in The component embedded in the frame.
	*/
	public void setComponent(VerdantiumComponent in) {
		myComp = in;
	}

	/**
	* Informs the {@link ProgramDirector} upon activation and closing of this frame.
	* This allows the program Director to know when to shut down.  Also produces
	* a "save" dialog whenever a component that isn't a property editor is closed.
	*/
	public void handleWindowEvent(WindowEvent e) {
		if ((e.getID() == WindowEvent.WINDOW_CLOSING)
			|| (e.getID() == WindowEvent.WINDOW_CLOSED)) {
			if (!(myComp instanceof VerdantiumPropertiesEditor)
				&& (ProgramDirector.getXKit().getApplet() == null)) {
				handleSaveOnClose(null);
				ProgramDirector.decrementComponentFrameCount();
			}

			if (myComp instanceof ProgramDirectorSaveEditor)
				ProgramDirector.decrementSaveEditorCount();

			ActionEvent ae =
				new ActionEvent(myComp, ActionEvent.ACTION_PERFORMED, "");
			fireClosureActionPerformed(ae);
		}

		if (e.getID() == WindowEvent.WINDOW_ACTIVATED) {
			fireActivationActionPerformed(null);
		}
	}

	/**
	* Tells the frame to act when its close button is pressed.
	*/
	public void addCloseButtonEvent() {
		WindowListener myL =
			Adapters.createGWindowListener(this, "handleWindowEvent");
		addWindowListener(myL);
	}

	/**
	* Produces a "save" dialog for the component.  Called upon window closure.
	* @param The input window closure event.
	*/
	public void handleSaveOnClose(WindowEvent e) {
		if (!updating && !(VerdantiumApplet.isAppletActivated())) {
			ProgramDirectorSaveEditor mySave =
				new ProgramDirectorSaveEditor(myComp, this);
			ProgramDirector.showComponent(mySave, null, "Save Dialog");
		}
	}

	/**
	* Sets whether the frame is updating.
	* @param in Whether the frame is updating.
	*/
	public void setUpdating(boolean in) {
		updating = in;
	}

	@Override
	public Dimension getMinimumSize() {
		if (minSize != null)
			return (minSize);
		else
			return (super.getMinimumSize());
	}

	@Override
	public void setMinimumSize(Dimension in) {
		minSize = in;
	}

	/**
	* Disposes the frame.
	*/
	public void dispose() {
		if (!disposed) {
			VerdantiumApplet.removeClassPropertyChangeListener(this);
			setVisible(false);

			/* The code in the block below is a workaround for Sun bug number 4193022 */ {
				long now = System.currentTimeMillis();
				MouseEvent event =
					new MouseEvent(
						this,
						MouseEvent.MOUSE_EXITED,
						now,
						0,
						0,
						0,
						0,
						false);
				dispatchEvent(event);
			}

			super.dispose();
		}

		disposed = true;
	}

	/**
	* Packs the frame to its preferred size.
	*/
	public void packToPreferred() {
		pack();
		Dimension pref = getPreferredSize();
		setMinimumSize(pref);
		pack();
		setMinimumSize(null);
	}

	/**
	* Adds an action listener that fires on frame activation.
	* @param l The listener to be added.
	*/
	public synchronized void addActivationActionListener(ActionListener l) {
		activate = AWTEventMulticaster.add(activate, l);
	}
	
	/**
	* Fires a frame activation action.
	* @param evt The event to be fired.
	*/
	public void fireActivationActionPerformed(ActionEvent evt) {
		if (activate != null) {
			activate.actionPerformed(evt);
		}
	}
	
	/**
	* Removes an action listener that fires on frame activation.
	* @param l The listener to be removed.
	*/
	public synchronized void removeActivationActionListener(ActionListener l) {
		activate = AWTEventMulticaster.remove(activate, l);
	}

	/**
	* Adds an action listener that fires on frame closure.
	* @param l The listener to be added.
	*/
	public synchronized void addClosureActionListener(ActionListener l) {
		closure = AWTEventMulticaster.add(closure, l);
	}
	
	/**
	* Fires a frame closure action.
	* @param evt The event to be fired.
	*/
	public void fireClosureActionPerformed(ActionEvent evt) {
		if (closure != null) {
			closure.actionPerformed(evt);
		}
	}
	
	/**
	* Removes an action listener that fires on frame closure.
	* @param l The listener to be removed.
	*/
	public synchronized void removeClosureActionListener(ActionListener l) {
		closure = AWTEventMulticaster.remove(closure, l);
	}

	/**
	* Handles property change events, as in applet start and stop events.
	* @param evt The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == VerdantiumApplet.appletStart) {
			if (hiddenByAppletStop) {
				setVisible(true);
			}

			hiddenByAppletStop = false;
		}

		if (evt.getPropertyName() == VerdantiumApplet.appletStop) {
			if (isVisible()) {
				setVisible(false);
				hiddenByAppletStop = true;
			}
		}
	}

	/**
	 * Whether the frame is hidden by a host applet going into the "stop" state.
	 * Applets are no longer in use.
	 */
	transient private boolean hiddenByAppletStop = false;
	
	/**
	 * AWT multicaster called upon frame activation.
	 */
	transient private ActionListener activate = null;
	
	/**
	 * AWT multicaster called upon frame closure.
	 */
	transient private ActionListener closure = null;
	
	/**
	 * Whether the frame has been disposed.
	 */
	transient private boolean disposed = false;
	
	/**
	 * The minimum size at which to display the frame.
	 */
	transient private Dimension minSize = null;
	
	/**
	 * Whether the state of the frame is updating.
	 */
	transient private boolean updating = false;
	
	/**
	 * The component embedded in the frame.
	 */
	transient private VerdantiumComponent myComp = null;
	
}

