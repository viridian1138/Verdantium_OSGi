package verdantium.xapp;

import java.util.Iterator;

import jundo.runtime.ExtBooleanPair;
import jundo.runtime.ExtMilieuRef;
import jundo.runtime.IExtPair;
import jundo.util.pdx_Iterator_pdx_ObjectRef;
import jundo.util.pdx_JkeyRef_pdx_ObjectRef;

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
 * Iterator class used by MacroTreeMap.
 */
class MacroTreeMapIterator implements Iterator {

	/**
	 * Current milieu.
	 */
	protected ExtMilieuRef mil = null;

	/**
	 * Input iterator from MacroTreeMap.
	 */
	protected pdx_Iterator_pdx_ObjectRef ref = null;

	/**
	 * Constructs the Iterator.
	 * @param _mil Input milieu from MactoTreeMap.
	 * @param _ref Input Iterator from MacroTreeMap.
	 */
	public MacroTreeMapIterator(
		ExtMilieuRef _mil,
		pdx_Iterator_pdx_ObjectRef _ref) {
		mil = _mil;
		ref = _ref;
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		ExtBooleanPair pair = ref.pdxm_hasNext(mil);
		mil = pair.getMilieu();
		return (pair.getObject());
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
		IExtPair pair = ref.pdxm_next(mil);
		mil = pair.getMilieu();
		pdx_JkeyRef_pdx_ObjectRef ref =
			(pdx_JkeyRef_pdx_ObjectRef) (pair.getObject());
		Object oref = "null";
		if (ref != null)
			oref = ref.getKey();
		return (oref);
	}

	/**
	 * @see java.util.Iterator#remove()
	 * Currently not supported.
	 */
	public void remove() {
		// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		throw (new RuntimeException("Not Supported!!!!!!!!!!!!!!!!!!"));
	}

	
}

