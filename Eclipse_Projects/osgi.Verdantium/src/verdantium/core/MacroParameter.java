package verdantium.core;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import meta.DataFormatException;
import meta.Meta;
import meta.VersionBuffer;

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
*    | 9/24/2000             | Thorn Green (viridian_1138@yahoo.com)           | Needed to provide a standard way to document source file changes.    | Added a souce modification list to the documentation so that changes to the souce could be recorded. 
*    | 10/22/2000            | Thorn Green (viridian_1138@yahoo.com)           | Methods did not have names that followed standard Java conventions.  | Performed a global modification to bring the names within spec.
*    | 10/29/2000            | Thorn Green (viridian_1138@yahoo.com)           | Classes did not have names that followed standard Java conventions.  | Performed a global modification to bring the names within spec.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
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
* This is a data type used by {@link MacroRecorder} to provide a reference to
* scripting objects that have been parameterized by the MacroRecorder.  Each
* scripting parameter is given a parameter number by its client MacroRecorder.
* It is the MacroRecorder's responsibility to make sure that no two MacroParameter
* objects that it stores have the same parameter value.
* <P>
* @author Thorn Green
*/
public class MacroParameter extends Meta implements Externalizable {

	/**
	* Constructs the MacroParameter for a given parameter ID value.
	* @param in The parameter ID value.
	*/
	public MacroParameter(int in) {
		param = in;
	}

	/**
	* Constructs the MacroParameter without a parameter.  To be used only for
	* de-serializing the object.
	*/
	public MacroParameter() {
	}

	/**
	* Gets the parameter number of the object.
	* @return The parameter number of the object.
	*/
	public int getParam() {
		return (param);
	}

	/**
	* Sets the parameter number of the object.
	* @param in The parameter number of the object.
	*/
	public void setParam(int in) {
		param = in;
	}

	/**
	* Prints the parameter number when the object is printed.
	* @return The string containing the printed parameter number.
	*/
	public String toString() {
		return ("Param" + param);
	}

	/**
	* Allows the MacroParameter to work as a Meta node.
	*/
	public void wake() {
	}

	/**
	* Reads the object from persistent storage.
	*/
	public void readExternal(ObjectInput in)
		throws IOException, ClassNotFoundException {
		try {
			VersionBuffer myv = (VersionBuffer) (in.readObject());
			VersionBuffer.chkNul(myv);

			param = myv.getInt("Param");
		} catch (ClassCastException e) {
			throw (new DataFormatException(e));
		}
	}

	/**
	* Writes the object to persistent storage.  @serialData TBD.
	*/
	public void writeExternal(ObjectOutput out) throws IOException {
		VersionBuffer myv = new VersionBuffer(VersionBuffer.WRITE);

		myv.setInt("Param", param);

		out.writeObject(myv);
	}

	/**
	* The (hopefully) unique parameter ID value of the parameter object.
	*/
	private int param = 0;
	
}

