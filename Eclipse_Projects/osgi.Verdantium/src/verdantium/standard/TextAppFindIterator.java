package verdantium.standard;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import meta.WrapRuntimeException;
import verdantium.ProgramDirector;
import verdantium.PropertyChangeSource;
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
*    | 04/21/2002            | Thorn Green (viridian_1138@yahoo.com)           | Needed an iterator for text find/replace.                            | Created TextAppFindIterator.
*    | 04/25/2002            | Thorn Green (viridian_1138@yahoo.com)           | Bug: iterator would sometimes generate bad location exceptions.      | Fixed the bug.
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
* Returns an iterator for find/replace operations in a component with a JEditorPane.
* 
* @author Thorn Green
*/
public class TextAppFindIterator
	extends Object
	implements FindReplaceIterator, PropertyChangeListener {

	/**
	* The editor pane to search.
	*/
	JEditorPane myEdit = null;

	/**
	* The property change source for the text app.
	*/
	PropertyChangeSource pcs = null;

	/**
	* The current frame index in the desktop pane.
	*/
	int currentIndex = -1;

	/**
	* The search parameter.
	*/
	Object[] parameter;

	/**
	* Indicates whether to match case.
	*/
	boolean matchCase;

	/**
	* The search string.
	*/
	String sparam;

	/**
	* The current jump delta.
	*/
	int currentJump = 1;

	/**
	* The top of the GUI for the component being searched.
	*/
	JComponent guiTop = null;

	/**
	* Indicates whether the text app. is scrolling.
	*/
	boolean isScrolling = false;

	/**
	* Constructs the iterator.  Takes in the ether parameter array that describes the details
	* of the find (e.g. the search string), the property change source of the component to
	* operate on, the editing pane of the component, the top-level GUI of the component,
	* and whether the component is scrolling.
	* @param param The search parameter.
	* @param pc The property change source for the text app.
	* @param edit The editor pane to search.
	* @param gui The top of the GUI for the component being searched.
	* @param scrolling Indicates whether the text app. is scrolling.
	*/
	public TextAppFindIterator(
		Object[] param,
		PropertyChangeSource pc,
		JEditorPane edit,
		JComponent gui,
		boolean scrolling) {
		parameter = param;
		sparam = (String) (param[0]);
		matchCase = ((Boolean) (param[1])).booleanValue();
		pcs = pc;
		myEdit = edit;
		guiTop = gui;
		isScrolling = scrolling;
		pcs.addPropertyChangeListener(this);
	}

	/**
	* Starts parsing from currentIndex in order to find the next match.  Returns
	* the index of the next match if one exists, or -2 if there are no further matches.
	* @param currentIndex The current index from which to start parsing.
	* @return The index of the next match if one exists, or -2 if there are no further matches.
	*/
	protected int parseElements(int currentIndex) {
		if (myEdit == null)
			return (-2);

		int idx = currentIndex + currentJump;
		int alen = myEdit.getDocument().getLength();
		int sidx = idx;
		int srchlen = sparam.length();
		boolean found = false;
		boolean done = false;

		if (idx == 0) {
			myEdit.setCaretPosition(0);
			myEdit.requestFocus();
		}

		if ((idx >= 0) && ((idx + srchlen) < (alen + 1))) {
			int cnt = idx;
			String Txt = "";
			while (!done) {
				try {
					Txt = myEdit.getText(cnt, srchlen);
				} catch (Exception e) {
					throw (new WrapRuntimeException("Search Failed", e));
				}

				boolean equals = false;

				if (matchCase) {
					if (sparam.equals(Txt))
						equals = true;
				} else {
					if (sparam.equalsIgnoreCase(Txt))
						equals = true;
				}

				if (equals) {
					sidx = cnt;
					found = true;
					done = true;
				}

				cnt++;
				done = done || ((cnt + srchlen) >= (alen + 1));
			}

		}

		if (!found)
			sidx = -2;

		return (sidx);
	}

	/**
	* Gets the next iterator from the frame list, and handles house-cleaning chores.
	* @return The next iterator from the frame list.
	*/
	protected int getNext() {
		int it = parseElements(currentIndex);

		if (it < 0) {
			currentIndex = -2;
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
		return ((getNext()) >= 0);
	}

	/**
	* Gets the next item the iterator traverses over.
	* @return The next item the iterator traverses over.
	*/
	public String next() {
		currentIndex = getNext();

		if (currentIndex >= 0) {
			myEdit.requestFocus();
			int srchlen = sparam.length();
			int eidx = currentIndex + srchlen;
			currentJump = srchlen;
			myEdit.setCaretPosition(currentIndex);
			myEdit.moveCaretPosition(eidx);
			scrollMultiple(true);
			myEdit.repaint();
			return (sparam);
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
	* @param in The string with which to replace the item.
	*/
	public void replace(String in) {
		if (myEdit != null) {
			String txt = myEdit.getSelectedText();
			if (txt != null) {
				if (txt.equalsIgnoreCase(sparam)) {
					myEdit.replaceSelection(in);
					currentJump = in.length();
				}
			}
		}
	}

	/**
	* Handles property change events.
	* @param evt The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == ProgramDirector.propertyDestruction) {
			currentIndex = -2;
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
		currentIndex = -2;
		if (myEdit != null) {
			myEdit = null;
			pcs.removePropertyChangeListener(this);
		}
	}

	/**
	* Scrolls to the proper region in multiple scroll panes.
	* @param scrolling Indicates whether the text app. is scrolling.
	*/
	protected void scrollMultiple(boolean scrolling) {
		if (isScrolling) {
			guiTop.scrollRectToVisible(guiTop.getBounds());
		}

		Component top = guiTop.getParent();
		while (top != null) {
			while (!(top instanceof JScrollPane) && (top != null))
				top = top.getParent();

			if (top != null) {
				((JScrollPane) top).scrollRectToVisible(top.getBounds());
				top = top.getParent();
			}
		}
	}

	
}


