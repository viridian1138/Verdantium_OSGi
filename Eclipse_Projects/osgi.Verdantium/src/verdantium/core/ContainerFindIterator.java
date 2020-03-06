package verdantium.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import meta.WrapRuntimeException;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.PropertyChangeSource;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumInternalFrame;

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
public class ContainerFindIterator
	extends Object
	implements FindReplaceIterator, PropertyChangeListener {
	
	/**
	* The iterator from the current component.
	*/
	FindReplaceIterator currentIterator = null;

	/**
	* The desktop pane for the container app.
	*/
	JDesktopPane jdp = null;

	/**
	* The property change source for the container app.
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
	* searched, and the desktop pane of the component.
	* @param param The search parameter.
	* @param pc The property change source for the container app.
	* @param jd The desktop pane for the container app.
	*/
	public ContainerFindIterator(
		Object[] param,
		PropertyChangeSource pc,
		JDesktopPane jd) {
		parameter = param;
		pcs = pc;
		jdp = jd;
		pcs.addPropertyChangeListener(this);
	}

	/**
	* Gets the next iterator from the frame list.
	* @return The next iterator from the frame list.
	*/
	protected FindReplaceIterator getNextIteratorComp() {
		if (jdp == null)
			return (null);

		nxtIndex = currentIndex;
		JInternalFrame[] Frames = jdp.getAllFrames();
		int len1 = Frames.length;
		FindReplaceIterator ret = null;

		nxtIndex++;
		while ((nxtIndex < len1) && (ret == null)) {
			VerdantiumInternalFrame fr =
				(VerdantiumInternalFrame) (Frames[nxtIndex]);
			if (fr != null) {
				VerdantiumComponent comp = fr.getComponent();
				if (comp != null) {
					EtherEvent send =
						new PropertyEditEtherEvent(
							this,
							PropertyEditEtherEvent.createFindReplaceIterator,
							null,
							null);
					send.setParameter(parameter);
					Object ob = null;

					try {
						ob = comp.processObjEtherEvent(send, null);
					} catch (Throwable ex) {
						throw (new WrapRuntimeException("Iterator Failed", ex));
					}

					ret = (FindReplaceIterator) (ob);
				}
			}

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
	* @return The next iterator from the frame list.
	*/
	protected FindReplaceIterator getNextIterator() {
		FindReplaceIterator it = getNextIteratorComp();

		if (it == null) {
			setCurrentIterator(null);
			if (jdp != null) {
				jdp = null;
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
			if (currentIterator.hasNext())
				return (currentIterator.next());
		}

		setCurrentIterator(getNextIterator());
		currentIndex = nxtIndex;
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
	* @param in The string with which to replace the current item in the iterator.
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
			if (jdp != null) {
				jdp = null;
				pcs.removePropertyChangeListener(this);
			}
		}

	}

	/**
	* Handles the destruction of the iterator.
	*/
	public void handleDestroy() {
		setCurrentIterator(null);
		if (jdp != null) {
			jdp = null;
			pcs.removePropertyChangeListener(this);
		}
	}

	
	/**
	* Sets the current iterator.
	* @param in The current iterator.
	*/
	protected void setCurrentIterator(FindReplaceIterator in) {
		if ((currentIterator != null) && (currentIterator != in))
			currentIterator.handleDestroy();
		currentIterator = in;
	}

	
}

