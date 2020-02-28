
package verdantium.xapp;


import java.lang.ref.WeakReference;

import javax.swing.JComponent;

import verdantium.VerdantiumComponent;


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
 * @author thorngreen
 *
 * Runnable for destroying a Verdantium component that is embedded in a JComponent
 */
public class DestructionRunnable implements Runnable {

	/**
	 * The component to be destroyed.
	 */
	protected VerdantiumComponent part;
	
	/**
	 * The JComponent embedding the component to be destroyed.
	 */
	protected WeakReference<JComponent> comp = null;

	/**
	 * Constructor.
	 * @param _part The component to be destroyed.
	 * @param _comp The JComponent embedding the component to be destroyed.
	 */
	public DestructionRunnable(
		VerdantiumComponent _part,
		WeakReference<JComponent> _comp) {
		part = _part;
		comp = _comp;
	}

	/**
	 * Performs the destruction of the component.
	 */
	public void run() {
		part.handleDestroy();

		final JComponent cmp = comp.get();
		if (cmp != null) {
			cmp.remove( part.getGUI() );
		}
	}

	
}

