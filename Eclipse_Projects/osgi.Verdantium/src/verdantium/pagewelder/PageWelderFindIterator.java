package verdantium.pagewelder;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import verdantium.ProgramDirector;
import verdantium.PropertyChangeSource;
import verdantium.core.ContainerFindIterator;
import verdantium.core.FindReplaceIterator;

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
*    | 04/21/2002            | Thorn Green (viridian_1138@yahoo.com)           | Find/Replace support.                                                | Created ContainerFindIterator.
*    | 05/10/2002            | Thorn Green (viridian_1138@yahoo.com)           | Redundant information in persistent storage.                         | Made numerous persistence and packaging changes.
*    | 03/09/2003            | Thorn Green (viridian_1138@yahoo.com)           | PageWelder.                                                          | Implemented PageWelder using code from other classes.
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
* An iterator for container find/replace operations.
* 
* @author Thorn Green
*/
public class PageWelderFindIterator
	extends Object
	implements FindReplaceIterator, PropertyChangeListener {
	
	/**
	* The iterator from the current component.
	*/
	FindReplaceIterator currentIterator = null;

	/**
	* The component being edited.
	*/
	PageWelder pw = null;

	/**
	* The property change source of the component being searched.
	*/
	PropertyChangeSource pcs = null;

	/**
	* The current frame index in the desktop pane.
	*/
	int currentIndex = -1;

	/**
	* Temporary storage for the index of the next iterator.
	*/
	int nxtIndex = 0;

	/**
	* The search parameter.
	*/
	Object[] parameter;

	/**
	* Constructs the iterator.  Takes in the ether parameter description (e.g. the
	* string to search with), the property change source of the component to be
	* searched, and the component being edited.
	* @param param The ether parameter description (e.g. the string to search with).
	* @param pc The property change source of the component being searched.
	* @param p The component being edited.
	*/
	public PageWelderFindIterator(
		Object[] param,
		PropertyChangeSource pc,
		PageWelder p) {
		parameter = param;
		pcs = pc;
		pw = p;
		pcs.addPropertyChangeListener(this);
	}

	/**
	* Gets the next iterator from the frame list.
	* @return The next iterator.
	*/
	protected FindReplaceIterator getNextIteratorComp() {
		if (pw == null)
			return (null);

		nxtIndex = currentIndex;
		int len1 = pw.getNumCards();
		FindReplaceIterator ret = null;

		nxtIndex++;
		while ((nxtIndex < len1) && (ret == null)) {
			ret =
				new ContainerFindIterator(
					parameter,
					pw,
					pw.getCardForIndex(nxtIndex));

			if (ret != null) {
				if (!(ret.hasNext()))
					ret = null;
			}

			if (ret == null)
				nxtIndex++;
		}

		return (ret);
	}

	/**
	* Gets the next iterator from the frame list, and handles house-cleaning chores.
	* @return The next iterator.
	*/
	protected FindReplaceIterator getNextIterator() {
		FindReplaceIterator it = getNextIteratorComp();

		if (it == null) {
			setCurrentIterator(null);
			if (pw != null) {
				pw = null;
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

		Iterator next_it = getNextIterator();
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
			if (currentIterator.hasNext()) {
				pw.switchToIndex(currentIndex);
				return (currentIterator.next());
			}
		}

		setCurrentIterator(getNextIterator());
		currentIndex = nxtIndex;
		if (currentIterator != null) {
			if (currentIterator.hasNext()) {
				pw.switchToIndex(currentIndex);
				return (currentIterator.next());
			}
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
	* @param in The input string with which to rep[lace the current item.
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
			setCurrentIterator(null);
			if (pw != null) {
				pw = null;
				pcs.removePropertyChangeListener(this);
			}
		}

	}

	/**
	* Handles the destruction of the iterator.
	*/
	public void handleDestroy() {
		setCurrentIterator(null);
		if (pw != null) {
			pw = null;
			pcs.removePropertyChangeListener(this);
		}
	}

	/**
	* Sets the current iterator.
	* @param in The iterator to set.
	*/
	protected void setCurrentIterator(FindReplaceIterator in) {
		if ((currentIterator != null) && (currentIterator != in))
			currentIterator.handleDestroy();
		currentIterator = in;
	}

	
}

