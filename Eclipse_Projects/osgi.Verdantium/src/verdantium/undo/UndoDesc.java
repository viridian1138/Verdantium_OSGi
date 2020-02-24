package verdantium.undo;

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
 * A description of a particular level in multi-level undo.
 * 
 * @author thorngreen
 *
 */
class UndoDesc {

	/**
	 * The string describing the Undo operation.
	 */
	protected String undoString = null;

	/**
	 * The string describing the Redo operation.
	 */
	protected String redoString = null;

	/**
	 * Original string describing operation.
	 */
	protected String origString = null;

	/**
	 * Constructs the UndoDesc.
	 * @param _undoString The string describing the Undo operation.
	 * @param _redoString The string describing the Redo operation.
	 */
	public UndoDesc(
		String _undoString,
		String _redoString,
		String _origString) {
		undoString = _undoString;
		redoString = _redoString;
		origString = _origString;
	}

	/**
	 * Gets the string describing the Undo operation.
	 * @return The string describing the Undo operation.
	 */
	public String getUndoString() {
		return (undoString);
	}

	/**
	 * Gets the string describing the Redo operation.
	 * @return The string describing the Redo operation.
	 */
	public String getRedoString() {
		return (redoString);
	}

	/**
	* Gets the string describing the original operation.
	* @return The string describing the original operation.
	*/
	public String getOrigString() {
		return (origString);
	}

	
}

