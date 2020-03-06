package verdantium.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import verdantium.ProgramDirector;
import verdantium.PropertyChangeSource;

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
*    | 04/21/2002            | Thorn Green (viridian_1138@yahoo.com)           | Find/Replace support.                                                | Created CompoundFindIterator.
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
* Returns an iterator combining two find/replace iterators.
* 
* @author Thorn Green
*/
public class CompoundFindIterator
	extends Object
	implements FindReplaceIterator, PropertyChangeListener {
	
	/**
	* The iterator from the current component.
	*/
	FindReplaceIterator currentIterator = null;

	/**
	* The first iterator to traverse through.
	*/
	FindReplaceIterator i1;

	/**
	* The second iterator to traverse through.
	*/
	FindReplaceIterator i2;

	/**
	* The property change source for the container app.
	*/
	PropertyChangeSource pcs = null;

	/**
	* The search parameter.
	*/
	Object[] parameter;

	/**
	 * Constructs the iterator.
	 * @param param The search parameter.
	 * @param pc The property change source for the container app.
	 * @param it1 The first iterator to traverse through.
	 * @param it2 The second iterator to traverse through.
	 */
	public CompoundFindIterator(
		Object[] param,
		PropertyChangeSource pc,
		FindReplaceIterator it1,
		FindReplaceIterator it2) {
		parameter = param;
		pcs = pc;
		i1 = it1;
		i2 = it2;
		currentIterator = i1;
		pcs.addPropertyChangeListener(this);
	}

	/**
	* Gets the next iterator from the frame list, and handles house-cleaning chores.
	* @return The next iterator from the frame list.
	*/
	protected FindReplaceIterator getNextIterator() {
		FindReplaceIterator it = null;

		if (currentIterator == i1)
			it = i2;

		if (it == null) {
			currentIterator = null;
			if ((i1 != null) && (i2 != null)) {
				handleIteratorDestroy();
				pcs.removePropertyChangeListener(this);
			}
		}

		return (it);
	}

	/**
	* Returns whether there are any remaining elements.
	* @return Whether there are any remaining elements.
	*/
	public boolean hasNext() {
		if (currentIterator != null) {
			if (currentIterator.hasNext())
				return (true);
		}

		FindReplaceIterator next_it = getNextIterator();
		if (next_it != null)
			return (next_it.hasNext());

		return (false);
	}

	/**
	* Gets the next item the iterator traverses over.
	* @return The next item the iterator traverses over.
	*/
	public Object next() {
		if (currentIterator != null) {
			if (currentIterator.hasNext())
				return (currentIterator.next());
		}

		currentIterator = getNextIterator();
		if (currentIterator != null) {
			if (currentIterator.hasNext())
				return (currentIterator.next());
		}

		return (null);
	}

	/**
	* Removes the current item from the iterator.
	*/
	public void remove() {
		replace("");
	}

	/**
	* Replaces the current item in the iterator.
	* @param in The string with which to replace the current item.
	*/
	public void replace(String in) {
		if (currentIterator != null)
			currentIterator.replace(in);
		else {
			FindReplaceIterator it = getNextIterator();
			if (it != null)
				it.replace(in);
		}
	}

	/**
	* Handles property change events.
	* @param evt The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == ProgramDirector.propertyDestruction) {
			currentIterator = null;
			if ((i1 != null) && (i2 != null)) {
				handleIteratorDestroy();
				pcs.removePropertyChangeListener(this);
			}
		}

	}

	/**
	* Handles the destruction of the iterator.
	*/
	public void handleDestroy() {
		currentIterator = null;
		if ((i1 != null) && (i2 != null)) {
			handleIteratorDestroy();
			pcs.removePropertyChangeListener(this);
		}
	}

	/**
	* Destroys all currently linked iterators.
	*/
	protected void handleIteratorDestroy() {
		if (i1 != null)
			i1.handleDestroy();
		if (i2 != null)
			i2.handleDestroy();
		currentIterator = null;
		i1 = null;
		i2 = null;
	}

	
}

