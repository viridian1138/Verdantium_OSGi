package verdantium.undo;

import java.util.Iterator;

import jundo.runtime.ExtBooleanPair;
import jundo.runtime.ExtMilieuRef;
import jundo.runtime.IExtPair;

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
 * Iterator that returns the descriptions of the current list of multi-level undo levels.
 * 
 * @author thorngreen
 *
 */
class UndoIterator implements Iterator<String> {

	/**
	 * The milieu over which to retrieve the undo state.
	 */
	ExtMilieuRef mil = null;
	
	/**
	 * The object reference for the undo iterator.
	 */
	pdx_UndoIterator_pdx_ObjectRef iter = null;

	/**
	 * Constructor.
	 * @param ref The pair reference for the undo iterator.
	 */
	public UndoIterator(pdx_UndoIterator_pdx_PairRef ref) {
		mil = ref.getMilieu();
		iter = (pdx_UndoIterator_pdx_ObjectRef) (ref.getObject());
	}

	/**
	 * Gets whether there is a subsequent item
	 * @return Whether there is a subsequent item.
	 */
	public boolean hasNext() {
		ExtBooleanPair nxt = iter.pdxm_hasNext(mil);
		mil = nxt.getMilieu();
		return (nxt.getObject());
	}

	/**
	 * Returns the next item.
	 * @return The next item.
	 */
	public String next() {
		IExtPair nxt = iter.pdxm_next(mil);
		mil = nxt.getMilieu();
		pdx_UndoNode_pdx_ObjectRef ref =
			(pdx_UndoNode_pdx_ObjectRef) (nxt.getObject());
		UndoDesc desc = (UndoDesc) (ref.pdxm_getUndoDesc(mil));
		String ret = null;
		if (desc != null) {
			ret = desc.getOrigString();
		}
		return (ret);
	}

	/**
	 * Removes the current item.  Not supported.
	 */
	public void remove() {
		throw (new RuntimeException("Operation Not Supported"));
	}

	
}

