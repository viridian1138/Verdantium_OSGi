package verdantium.xapp;

import java.lang.ref.WeakReference;

import javax.swing.JComponent;

import jundo.runtime.FinalizationRunner;
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
 * A reference to a Verdantium component that destroys the component upon garbage collection.
 */
public class ComponentRef {

	/**
	 * The Verdantium component being referenced.
	 */
	protected VerdantiumComponent part;
	
	/**
	 * WeakReference to the JComponent in which the component is embedded.
	 */
	protected WeakReference<JComponent> comp;
	
	/**
	 * The index of the reference.
	 */
	protected String idex;

	/**
	 * Constructor.
	 * @param _part The Verdantium component being referenced.
	 * @param _comp WeakReference to the JComponent in which the component is embedded.
	 * @param _idex The index of the reference.
	 */
	public ComponentRef(
		VerdantiumComponent _part,
		WeakReference<JComponent> _comp,
		String _idex) {
		part = _part;
		comp = _comp;
		idex = _idex;
	}

	/**
	 * Gets the Verdantium component being referenced.
	 * @return The Verdantium component being referenced.
	 */
	public VerdantiumComponent get() {
		return (part);
	}

	/**
	 * Gets the index of the reference.
	 * @return The index of the reference.
	 */
	public String getIdex() {
		return (idex);
	}

	/**
	 * Handles the finalization of the reference by destroying the referenced component.
	 */
	public void finalize() {
		if (part != null) {
			FinalizationRunner.invokeLater(new DestructionRunnable(part, comp));
		}
	}

	
}

