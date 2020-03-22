package verdantium;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Method;

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
* Uses reflection to make Java event handling work in a manner more
* similar to X-Window callbacks.  Each inner class is a listener
* for a particular event type.
* 
* @author Thorn Green
*/
public final class Adapters {

	/**
	 * Returns an ActionListener for invoking a public method with a specified name.
	 * @param in The object on which to execute the method.
	 * @param mName The name of the method to be executed.
	 * @return The created ActionListener.
	 */
	public static ActionListener createGActionListener(
		Object in,
		String mName) {
		return (new GActionListener(in, mName));
	}

	/**
	 * ActionListener for invoking a public method with a specified name.
	 * 
	 * @author tgreen
	 *
	 */
	private static final class GActionListener implements ActionListener {
		
		/**
		* Constructor.  Fires action events at method <code>mName</code> of object <code>in</code>.
		* @param in The object on which to execute the method.
		* @param mName The name of the method to be executed.
		*/
		public GActionListener(Object in, String mName) {
			Class<ActionEvent> evtClass = null;

			evtClass = ActionEvent.class;

			Class<?> paramTypes[] = { evtClass };

			exObject = in;
			try {
				exMethod = (exObject.getClass()).getMethod(mName, paramTypes);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * Handles the event by invoking the public method.
		 * @param evt The input event.
		 */
		public void actionPerformed(ActionEvent evt) {
			Object[] myo = { evt };
			try {
				exMethod.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * The object on which to execute the method.
		 */
		Object exObject = null;
		
		/**
		 * The method to be executed.
		 */
		Method exMethod = null;
	}

	/**
	 * Returns a FocusListener for invoking public methods with specified names upon focus events.
	 * @param in The object on which to execute the methods.
	 * @param gainedName The name of the method to be executed upon focus gained.
	 * @param lostName The name of the method to be executed upon focus lost.
	 * @return The created focus listener.
	 */
	public static FocusListener createGFocusListener(
		Object in,
		String gainedName,
		String lostName) {
		return (new GFocusListener(in, gainedName, lostName));
	}

	/**
	 * FocusListener for invoking public methods with specified names upon focus events.
	 * 
	 * @author tgreen
	 *
	 */
	private static final class GFocusListener implements FocusListener {
		
		/**
		* Constructor.  Fires focus gained events at method <code>gainedName</code> of object <code>in</code>
		* and focus lost events at method <code>lostName</code> of object <code>in</code>.
		* @param in The object on which to execute the methods.
	    * @param gainedName The name of the method to be executed upon focus gained.
	    * @param lostName The name of the method to be executed upon focus lost.
		*/
		public GFocusListener(Object in, String gainedName, String lostName) {
			Class<FocusEvent> evtClass = null;

			evtClass = FocusEvent.class;

			Class<?> paramTypes[] = { evtClass };

			exObject = in;
			try {
				gainedMethod =
					(exObject.getClass()).getMethod(gainedName, paramTypes);

				lostMethod =
					(exObject.getClass()).getMethod(lostName, paramTypes);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * Handles a focus gained event by invoking the focus gained method on the object.
		 * @param evt The input event.
		 */
		public void focusGained(FocusEvent evt) {
			Object[] myo = { evt };
			try {
				gainedMethod.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * Handles a focus lost event by invoking the focus lost method on the object.
		 * @param evt The input event.
		 */
		public void focusLost(FocusEvent evt) {
			Object[] myo = { evt };
			try {
				lostMethod.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * The object on which to execute the methods.
		 */
		Object exObject = null;
		
		/**
		 * The method to invoke on the object at the point focus is gained.
		 */
		Method gainedMethod = null;
		
		/**
		 * The method to invoke on the object at the point focus is lost.
		 */
		Method lostMethod = null;
		
	}

	/**
	 * Returns a KeyListener for invoking public methods with specified names upon key events.
	 * @param in The object on which to execute the methods.
	 * @param pressName The name of the method to be executed upon key-press.
	 * @param releaseName The name of the method to be executed upon key-release.
	 * @param typeName The name of the method to be executed upon key-typed.
	 * @return The created key listener.
	 */
	public static KeyListener createGKeyListener(
		Object in,
		String pressName,
		String releaseName,
		String typeName) {
		return (new GKeyListener(in, pressName, releaseName, typeName));
	}

	/**
	 * KeyListener for invoking public methods with specified names upon key events.
	 * 
	 * @author tgreen
	 *
	 */
	private static final class GKeyListener implements KeyListener {
		
		/**
		* Constructor.  Fires key pressed events at method <code>pressName</code> of object <code>in</code>
		* and key released events at method <code>RrleaseName</code> of object <code>in</code>
		* and key typed events at method <code>typeName</code> of object <code>in</code>.
		* @param in The object on which to execute the methods.
	    * @param pressName The name of the method to be executed upon key-press.
	    * @param releaseName The name of the method to be executed upon key-release.
	    * @param typeName The name of the method to be executed upon key-typed.
		*/
		public GKeyListener(
			Object in,
			String pressName,
			String releaseName,
			String typeName)
		{
			Class<KeyEvent> evtClass = null;

			evtClass = KeyEvent.class;

			Class<?> paramTypes[] = { evtClass };

			exObject = in;
			try {
				pressMethod =
					(exObject.getClass()).getMethod(pressName, paramTypes);

				releaseMethod =
					(exObject.getClass()).getMethod(releaseName, paramTypes);

				typeMethod =
					(exObject.getClass()).getMethod(typeName, paramTypes);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * Handles a key-pressed event by invoking the key-pressed method on the object.
		 * @param evt The input event.
		 */
		public void keyPressed(KeyEvent evt) {
			Object[] myo = { evt };
			try {
				pressMethod.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * Handles a key-released event by invoking the key-released method on the object.
		 * @param evt The input event.
		 */
		public void keyReleased(KeyEvent evt) {
			Object[] myo = { evt };
			try {
				releaseMethod.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * Handles a key-typed event by invoking the key-typed method on the object.
		 * @param evt The input event.
		 */
		public void keyTyped(KeyEvent evt) {
			Object[] myo = { evt };
			try {
				typeMethod.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * The object on which to execute the methods.
		 */
		Object exObject = null;
		
		/**
		 * Method to invoke upon a key-press.
		 */
		Method pressMethod = null;
		
		/**
		 * Method to invoke upon a key-release.
		 */
		Method releaseMethod = null;
		
		/**
		 * Method to invoke upon a key-type.
		 */
		Method typeMethod = null;
		
	}

	/**
	 * Returns an ItemListener for invoking a public method with a specified name.
	 * @param in The object on which to execute the method.
	 * @param mName The name of the method to be executed.
	 * @return The created ItemListener.
	 */
	public static ItemListener createGItemListener(Object in, String mName) {
		return (new GItemListener(in, mName));
	}

	/**
	 * ItemListener for invoking a public method with a specified name.
	 * 
	 * @author tgreen
	 *
	 */
	private static final class GItemListener implements ItemListener {
		
		/**
		* Constructor.  Fires item events at method <code>mName</code> of object <code>in</code>.
		* @param in The object on which to execute the method.
		* @param mName The name of the method to be executed.
		*/
		public GItemListener(Object in, String mName) {
			Class<ItemEvent> evtClass = null;

			evtClass = ItemEvent.class;

			Class<?> paramTypes[] = { evtClass };

			exObject = in;
			try {
				exMethod = (exObject.getClass()).getMethod(mName, paramTypes);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * Handles the event by invoking the public method.
		 * @param evt The input event.
		 */
		public void itemStateChanged(ItemEvent evt) {
			Object[] myo = { evt };
			try {
				exMethod.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * The object on which to execute the method.
		 */
		Object exObject = null;
		
		/**
		 * The method to be executed.
		 */
		Method exMethod = null;
	}

	/**
	 * Returns an AdjustmentListener for invoking a public method with a specified name.
	 * @param in The object on which to execute the method.
	 * @param mName The name of the method to be executed.
	 * @return The created AdjustmentListener.
	 */
	public static AdjustmentListener createGAdjustmentListener(
		Object in,
		String mName) {
		return (new GAdjustmentListener(in, mName));
	}

	/**
	 * AdjustmentListener for invoking a public method with a specified name.
	 * 
	 * @author tgreen
	 *
	 */
	private static final class GAdjustmentListener
		implements AdjustmentListener {
		
		/**
		* Constructor.  Fires adjustment events at method <code>mName</code> of object <code>in</code>.
		* @param in The object on which to execute the method.
		* @param mName The name of the method to be executed.
		*/
		public GAdjustmentListener(Object in, String mName) {
			Class<AdjustmentEvent> evtClass = null;

			evtClass = AdjustmentEvent.class;

			Class<?> paramTypes[] = { evtClass };

			exObject = in;
			try {
				exMethod = (exObject.getClass()).getMethod(mName, paramTypes);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * Handles the event by invoking the public method.
		 * @param evt The input event.
		 */
		public void adjustmentValueChanged(AdjustmentEvent evt) {
			Object[] myo = { evt };
			try {
				exMethod.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * The object on which to execute the method.
		 */
		Object exObject = null;
		
		/**
		 * The method to be executed.
		 */
		Method exMethod = null;
	}

	/**
	 * Returns a WindowListener for invoking a public method with a specified name upon window events.
	 * @param in The object on which to execute the method.
	 * @param mName The name of the method to execute upon the mouse events.
	 * @return The created window listener.
	 */
	public static WindowListener createGWindowListener(
		Object in,
		String mName) {
		return (new GWindowListener(in, mName));
	}

	/**
	 * Returns a WindowListener for invoking public methods with specified names upon window events.
	 * @param in The object on which to execute the methods.
	 * @param actName The name of the method to be executed upon window-activate.
	 * @param closedName The name of the method to be executed upon window-closed.
	 * @param closingName The name of the method to be executed upon window-closing.
	 * @param deactName The name of the method to be executed upon window-deactivate.
	 * @param deiconName The name of the method to be executed upon window-deiconify.
	 * @param openedName The name of the method to be executed upon window-open.
	 * @param iconName The name of the method to be executed upon window-iconify.
	 * @return The created window listener.
	 */
	public static WindowListener createGWindowListener(
		Object in,
		String actName,
		String closedName,
		String closingName,
		String deactName,
		String deiconName,
		String openedName,
		String iconName) {
		return (
			new GWindowListener(
				in,
				actName,
				closedName,
				closingName,
				deactName,
				deiconName,
				openedName,
				iconName));
	}

	
	/**
	 * WindowListener for invoking public methods with specified names upon window events.
	 * 
	 * @author tgreen
	 *
	 */
	private static final class GWindowListener implements WindowListener {
		
		/**
		* Constructor.  Fires window events at method <code>mName</code> of object <code>in</code>.
		* @param in The object on which to execute the method.
		* @param mName The name of the method to execute upon the mouse events.
		*/
		public GWindowListener(Object in, String mName) {
			Class<WindowEvent> evtClass = null;

			evtClass = WindowEvent.class;

			Class<?> paramTypes[] = { evtClass };

			exObject = in;
			try {
				actMeth = (exObject.getClass()).getMethod(mName, paramTypes);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}

			closedMeth = actMeth;
			closingMeth = actMeth;
			deactMeth = actMeth;
			deiconMeth = actMeth;
			openedMeth = actMeth;
			iconMeth = actMeth;
		}

		/**
		 * Constructor.  Routes a series of window events to different method names.
		 * @param in The object on which to execute the methods.
		 * @param actName The name of the method to be executed upon window-activate.
		 * @param closedName The name of the method to be executed upon window-closed.
		 * @param closingName The name of the method to be executed upon window-closing.
		 * @param deactName The name of the method to be executed upon window-deactivate.
		 * @param deiconName The name of the method to be executed upon window-deiconify.
		 * @param openedName The name of the method to be executed upon window-open.
		 * @param iconName The name of the method to be executed upon window-iconify.
		 */
		public GWindowListener(
			Object in,
			String actName,
			String closedName,
			String closingName,
			String deactName,
			String deiconName,
			String openedName,
			String iconName) {
			Class<WindowEvent> evtClass = null;

			evtClass = WindowEvent.class;

			Class<?> paramTypes[] = { evtClass };

			exObject = in;
			try {
				actMeth = (exObject.getClass()).getMethod(actName, paramTypes);

				closedMeth =
					(exObject.getClass()).getMethod(closedName, paramTypes);

				closingMeth =
					(exObject.getClass()).getMethod(closingName, paramTypes);

				deactMeth =
					(exObject.getClass()).getMethod(deactName, paramTypes);

				deiconMeth =
					(exObject.getClass()).getMethod(deiconName, paramTypes);

				openedMeth =
					(exObject.getClass()).getMethod(openedName, paramTypes);

				iconMeth =
					(exObject.getClass()).getMethod(iconName, paramTypes);

			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}

		}

		/**
		 * Handles a window-activated event by invoking the window-activated method on the object.
		 * @param evt The input event.
		 */
		public void windowActivated(WindowEvent evt) {
			Object[] myo = { evt };
			try {
				actMeth.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * Handles a window-closed event by invoking the window-closed method on the object.
		 * @param evt The input event.
		 */
		public void windowClosed(WindowEvent evt) {
			Object[] myo = { evt };
			try {
				closedMeth.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * Handles a window-closing event by invoking the window-closing method on the object.
		 * @param evt The input event.
		 */
		public void windowClosing(WindowEvent evt) {
			Object[] myo = { evt };
			try {
				closingMeth.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * Handles a window-deactivated event by invoking the window-deactivated method on the object.
		 * @param evt The input event.
		 */
		public void windowDeactivated(WindowEvent evt) {
			Object[] myo = { evt };
			try {
				deactMeth.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * Handles a window-deiconified event by invoking the window-deiconified method on the object.
		 * @param evt The input event.
		 */
		public void windowDeiconified(WindowEvent evt) {
			Object[] myo = { evt };
			try {
				deiconMeth.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * Handles a window-opened event by invoking the window-opened method on the object.
		 * @param evt The input event.
		 */
		public void windowOpened(WindowEvent evt) {
			Object[] myo = { evt };
			try {
				openedMeth.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * Handles a window-iconified event by invoking the window-iconified method on the object.
		 * @param evt The input event.
		 */
		public void windowIconified(WindowEvent evt) {
			Object[] myo = { evt };
			try {
				iconMeth.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * The object on which to execute the methods.
		 */
		Object exObject = null;
		
		/**
		 * Method to invoke upon a window-activation.
		 */
		Method actMeth = null;
		
		/**
		 * Method to invoke upon a window-closed.
		 */
		Method closedMeth = null;
		
		/**
		 * Method to invoke upon a window-closing.
		 */
		Method closingMeth = null;
		
		/**
		 * Method to invoke upon a window-deactivation.
		 */
		Method deactMeth = null;
		
		/**
		 * Method to invoke upon a window-deiconify.
		 */
		Method deiconMeth = null;
		
		/**
		 * Method to invoke upon a window-open.
		 */
		Method openedMeth = null;
		
		/**
		 * Method to invoke upon a window-iconify.
		 */
		Method iconMeth = null;
		
	}

	/**
	 * Returns a MouseListener for invoking a public method with a specified name upon mouse events.
	 * @param in The object on which to execute the method.
	 * @param mName The name of the method to execute upon the window events.
	 * @return The created mouse listener.
	 */
	public static MouseListener createGMouseListener(Object in, String mName) {
		return (new GMouseListener(in, mName));
	}

	/**
	 * Returns a MouseListener for invoking public methods with specified names upon mouse events.
	 * @param in The object on which to execute the methods.
	 * @param clickName The name of the method to be executed upon mouse-click.
	 * @param enterName The name of the method to be executed upon mouse-enter.
	 * @param exitName The name of the method to be executed upon mouse-exit.
	 * @param pressName The name of the method to be executed upon mouse-press.
	 * @param releaseName The name of the method to be executed upon mouse-released.
	 * @return The created mouse listener.
	 */
	public static MouseListener createGMouseListener(
		Object in,
		String clickName,
		String enterName,
		String exitName,
		String pressName,
		String releaseName) {
		return (
			new GMouseListener(
				in,
				clickName,
				enterName,
				exitName,
				pressName,
				releaseName));
	}

	
	/**
	 * MouseListener for invoking public methods with specified names upon mouse events.
	 * 
	 * @author tgreen
	 *
	 */
	private static final class GMouseListener implements MouseListener {
		
		/**
		* Constructor.  Fires mouse events at method <code>mName</code> of object <code>in</code>.
		* @param in The object on which to execute the method.
		* @param mName The name of the method to execute upon the window events.
		*/
		public GMouseListener(Object in, String mName) {
			Class<MouseEvent> evtClass = null;

			evtClass = MouseEvent.class;

			Class<?> paramTypes[] = { evtClass };

			exObject = in;
			try {
				clickMeth = (exObject.getClass()).getMethod(mName, paramTypes);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}

			enterMeth = clickMeth;
			exitMeth = clickMeth;
			pressMeth = clickMeth;
			releaseMeth = clickMeth;
		}

		/**
		 * Constructor.  Routes a set of mouse events to different method names.
		 * @param in The object on which to execute the methods.
		 * @param clickName The name of the method to be executed upon mouse-click.
		 * @param enterName The name of the method to be executed upon mouse-enter.
		 * @param exitName The name of the method to be executed upon mouse-exit.
		 * @param pressName The name of the method to be executed upon mouse-press.
		 * @param releaseName The name of the method to be executed upon mouse-released.
		 */
		public GMouseListener(
			Object in,
			String clickName,
			String enterName,
			String exitName,
			String pressName,
			String releaseName) {
			Class<MouseEvent> evtClass = null;

			evtClass = MouseEvent.class;

			Class<?> paramTypes[] = { evtClass };

			exObject = in;
			try {
				clickMeth =
					(exObject.getClass()).getMethod(clickName, paramTypes);

				enterMeth =
					(exObject.getClass()).getMethod(enterName, paramTypes);

				exitMeth =
					(exObject.getClass()).getMethod(exitName, paramTypes);

				pressMeth =
					(exObject.getClass()).getMethod(pressName, paramTypes);

				releaseMeth =
					(exObject.getClass()).getMethod(releaseName, paramTypes);

			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}

		}

		/**
		 * Handles a mouse-clicked event by invoking the mouse-clicked method on the object.
		 * @param evt The input event.
		 */
		public void mouseClicked(MouseEvent evt) {
			Object[] myo = { evt };
			try {
				clickMeth.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * Handles a mouse-entered event by invoking the mouse-entered method on the object.
		 * @param evt The input event.
		 */
		public void mouseEntered(MouseEvent evt) {
			Object[] myo = { evt };
			try {
				enterMeth.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * Handles a mouse-exited event by invoking the mouse-exited method on the object.
		 * @param evt The input event.
		 */
		public void mouseExited(MouseEvent evt) {
			Object[] myo = { evt };
			try {
				exitMeth.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * Handles a mouse-pressed event by invoking the mouse-pressed method on the object.
		 * @param evt The input event.
		 */
		public void mousePressed(MouseEvent evt) {
			Object[] myo = { evt };
			try {
				pressMeth.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * Handles a mouse-released event by invoking the mouse-released method on the object.
		 * @param evt The input event.
		 */
		public void mouseReleased(MouseEvent evt) {
			Object[] myo = { evt };
			try {
				releaseMeth.invoke(exObject, myo);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * The object on which to execute the methods.
		 */
		Object exObject = null;
		
		/**
		 * Method to invoke upon a mouse-click.
		 */
		Method clickMeth = null;
		
		/**
		 * Method to invoke upon a mouse-enter.
		 */
		Method enterMeth = null;
		
		/**
		 * Method to invoke upon a mouse-exit.
		 */
		Method exitMeth = null;
		
		/**
		 * Method to invoke upon a mouse-press.
		 */
		Method pressMeth = null;
		
		/**
		 * Method to invoke upon a mouse-release.
		 */
		Method releaseMeth = null;
	}

	/**
	 * Returns a thread for invoking a public method with a supplied name.
	 * @param in The object on which to execute the method.
	 * @param mName The name of the method to be executed.
	 * @param paramTypes The parameter types of the method to be executed.
	 * @param args The parameter arguments to be passed to the method upon execution.
	 * @return The created thread.
	 */
	public static Thread createGThread(
		Object in,
		String mName,
		Class<?> paramTypes[],
		Object[] args) {
		return (new GThread(in, mName, paramTypes, args));
	}

	/**
	 * Invokes a public method with a supplied name in its own thread.
	 * 
	 * @author tgreen
	 *
	 */
	private static final class GThread extends Thread {
		
		/**
		 * Constructor.  Executes method <code>mName</code> of object <code>in</code>, with the supplied
		 * parameter types and arguments, in its own thread.
		 * @param in The object on which to execute the method.
		 * @param mName The name of the method to be executed.
		 * @param paramTypes The parameter types of the method to be executed.
		 * @param args The parameter arguments to be passed to the method upon execution.
		 */
		public GThread(
			Object in,
			String mName,
			Class<?> paramTypes[],
			Object args[]) {
			exObject = in;
			myArgs = args;
			try {
				threadMeth = (exObject.getClass()).getMethod(mName, paramTypes);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}

		}

		@Override
		public void run() {
			try {
				threadMeth.invoke(exObject, myArgs);
			} catch (Exception e) {
				throw (new WrapRuntimeException("Event Broken", e));
			}
		}

		/**
		 * The parameter arguments to be passed to the method upon execution.
		 */
		Object myArgs[] = null;
		
		/**
		 * The object on which to execute the method.
		 */
		Object exObject = null;
		
		/**
		 * The method to be executed.
		 */
		Method threadMeth = null;
		
	}

	
}

