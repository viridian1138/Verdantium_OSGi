package verdantium.utils;

import java.io.PrintStream;
import java.io.PrintWriter;

import verdantium.PropertyChangeSource;
import verdantium.ThrowHandler;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.VerdantiumUtils;

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
*    | 09/10/2001            | Thorn Green (viridian_1138@yahoo.com)           | Needed an exception to indicate out-of-range user input.             | Initial creation, using other exception classes as a template.
*    | 11/17/2001            | Thorn Green (viridian_1138@yahoo.com)           | Verdantium Exceptions not modular or extensible.                     | Made the exception handling more extensible.
*    | 12/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Third-Cut at Error Handling.                                         | Third-Cut at Error Handling.
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
* An exception indicating that the user entered something illegal into a GUI
* (e.g. typing something alphabetic into an integer field).
* 
* @author Thorn Green
*/
public class IllegalInputException extends Exception implements ThrowHandler {
	
	/**
	 * The throwable being wrapped.
	 */
	Throwable wrap = null;

	/**
	 * Constructor.
	 * @param e The throwable being wrapped.
	 */
	public IllegalInputException(Throwable e) {
		super();
		wrap = e;
	}

	/**
	 * Constructor.
	 * @param str Descriptive string.
	 * @param e The throwable being wrapped.
	 */
	public IllegalInputException(String str, Throwable e) {
		super(str);
		wrap = e;
	}

	/**
	 * Constructor.
	 * @param str Descriptive string.
	 */
	public IllegalInputException(String str) {
		super(str);
	}

	/**
	 * Constructor.
	 */
	public IllegalInputException() {
		super();
	}

	/**
	 * Gets the throwable being wrapped.
	 * @return The throwable being wrapped.
	 */
	public Throwable getWrap() {
		return (wrap);
	}

	@Override
	public void printStackTrace(PrintWriter out) {
		super.printStackTrace(out);
		if (wrap != null) {
			out.print("\n\nWrapping Exception [[[ \n\n");
			wrap.printStackTrace(out);
			out.print("\n\n]]] End Wrap\n");
		}
	}

	@Override
	public void printStackTrace(PrintStream out) {
		super.printStackTrace(out);
		if (wrap != null) {
			out.print("\n\nWrapping Exception [[[ \n\n");
			wrap.printStackTrace(out);
			out.print("\n\n]]] End Wrap\n");
		}
	}

	@Override
	public void printStackTrace() {
		printStackTrace(System.out);
	}

	/**
	* Handles an exception for a Verdantium app. by displaying a message window.
	* @param in The exception to handle.
	* @param comp The component generating the exception.
	* @param src The property change source for the message window.
	* @return The generated property editor.
	*/
	public VerdantiumPropertiesEditor handleThrow(Throwable in, VerdantiumComponent comp, PropertyChangeSource src) {
		if (getMessage() != null)
			return (
				VerdantiumUtils.produceMessageWindow(in, "Illegal Input", "Illegal Input : ", getMessage(), comp, src));
		else
			return (
				VerdantiumUtils.produceMessageWindow(
					in,
					"Illegal Input",
					"Illegal Input : ",
					"Something in the input was illegal.  Please try again.",
					comp,
					src));
	}

	
}

