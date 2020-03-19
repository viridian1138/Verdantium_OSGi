package verdantium.standard;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import meta.WrapRuntimeException;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.PropertyChangeSource;
import verdantium.VerdantiumComponent;
import verdantium.core.FindReplaceIterator;
import verdantium.core.PropertyEditEtherEvent;

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
*    | 04/25/2002            | Thorn Green (viridian_1138@yahoo.com)           | Find/Replace for inserted components in text.                        | Created EmbedFindIterator from ContainerFindIterator.
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
* An iterator for find/replace operations on an embedded component in an editing pane.
* This is used by the {@link TextApp} class
* .
* @author Thorn Green
*/
public class EmbedFindIterator
	extends Object
	implements FindReplaceIterator, PropertyChangeListener {
	
	/**
	* The iterator from the current component.
	*/
	FindReplaceIterator currentIterator = null;

	/**
	* The editing pane to search.
	*/
	JEditorPane myEdit = null;

	/**
	* The property change source for the text app.
	*/
	PropertyChangeSource pcs = null;

	/**
	* The current character index in the editing pane.
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
	* searched, and the edting pane of the component.
	* @param param The search parameter.
	* @param pc The property change source for the text app.
	* @param ed The editing pane to search.
	*/
	public EmbedFindIterator(
		Object[] param,
		PropertyChangeSource pc,
		JEditorPane ed) {
		parameter = param;
		pcs = pc;
		myEdit = ed;
		pcs.addPropertyChangeListener(this);
	}

	/**
	* Gets the next iterator from the document.
	* @return The next iterator from the document.
	*/
	protected FindReplaceIterator getNextIteratorComp() {
		if (myEdit == null)
			return (null);

		Document d = myEdit.getDocument();
		if (!(d instanceof StyledDocument))
			return (null);

		StyledDocument doc = (StyledDocument) (d);
		FindReplaceIterator it = null;
		nxtIndex = currentIndex + 1;
		int len = doc.getLength();

		while ((it == null) && (nxtIndex < len)) {
			Element elem = doc.getCharacterElement(nxtIndex);
			AttributeSet at = elem.getAttributes();
			Component comp = StyleConstants.getComponent(at);

			if (comp != null) {
				TextAppInsertPanel panel = (TextAppInsertPanel) (comp);
				VerdantiumComponent vcomp = panel.getComponent();

				if (vcomp != null) {
					EtherEvent send =
						new PropertyEditEtherEvent(
							this,
							PropertyEditEtherEvent.createFindReplaceIterator,
							null,
							null);
					send.setParameter(parameter);
					Object ob = null;

					try {
						ob = vcomp.processObjEtherEvent(send, null);
					} catch (Throwable ex) {
						throw (new WrapRuntimeException("Iterator Failed", ex));
					}

					it = (FindReplaceIterator) (ob);
				}
			}

			if (it == null)
				nxtIndex++;
		}

		return (it);
	}

	/**
	* Gets the next iterator from the document, and handles house-cleaning chores.
	* @return The next iterator in the document.
	*/
	protected FindReplaceIterator getNextIterator() {
		FindReplaceIterator it = getNextIteratorComp();

		if (it == null) {
			setCurrentIterator(null);
			if (myEdit != null) {
				myEdit = null;
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

		Iterator<String> next_it = getNextIterator();
		if (next_it != null)
			return (next_it.hasNext());

		return (false);
	}

	/**
	* Gets the next item the iterator traverses over.
	* @return The next item the iterator traverses over.
	*/
	public String next() {
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
	* @param in The string with which to replace.
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
			if (myEdit != null) {
				myEdit = null;
				pcs.removePropertyChangeListener(this);
			}
		}

	}

	/**
	* Handles the destruction of the iterator.
	*/
	public void handleDestroy() {
		setCurrentIterator(null);
		if (myEdit != null) {
			myEdit = null;
			pcs.removePropertyChangeListener(this);
		}
	}

	/**
	* Sets the current FindReplaceIterator
	* @param in The iterator to set.
	*/
	protected void setCurrentIterator(FindReplaceIterator in) {
		if ((currentIterator != null) && (currentIterator != in))
			currentIterator.handleDestroy();
		currentIterator = in;
	}

	
}

