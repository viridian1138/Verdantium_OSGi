package verdantium.utils;

import java.io.IOException;
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
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | Needed a set of standard exceptions for verdantium error handling.   | Initial creation.
*    | 11/17/2001            | Thorn Green (viridian_1138@yahoo.com)           | Verdantium Exceptions not modular or extensible.                     | Made the exception handling more extensible.
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
* An exception indicating that a particular component was not found.
* not supported by other (usually older) versions.
* 
* @author Thorn Green
*/
public class IOComponentNotFoundException extends IOException implements ThrowHandler {
	
	/**
	 * The throwable being wrapped.
	 */
	Throwable wrap = null;

	/**
	 * Constructor.
	 * @param e The throwable being wrapped.
	 */
	public IOComponentNotFoundException(Throwable e) {
		super();
		wrap = e;
	}

	/**
	 * Constructor.
	 * @param str Descriptive string.
	 * @param e The throwable being wrapped.
	 */
	public IOComponentNotFoundException(String str, Throwable e) {
		super(str);
		wrap = e;
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
		return (
			VerdantiumUtils.produceMessageWindow(
				in,
				"Component Not Found",
				"A Required Component Was Not Found : ",
				getMessage(),
				comp,
				src));
	}

	
}

