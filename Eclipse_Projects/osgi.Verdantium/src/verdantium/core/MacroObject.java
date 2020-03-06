package verdantium.core;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import meta.DataFormatException;
import meta.HighLevelList;
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
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Client macros.                                                       | Made changes to support client macro usage.
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
* An object that encapsulates a particular macro.
* 
* @author Thorn Green
*/
public class MacroObject extends Object implements Externalizable {
	
	/**
	* Parameter counting value.  Incremented by one each time a new scripting
	* parameter is created.
	*/
	private int paramVal = 0;
	
	/**
	* The current list of macro commands.
	*/
	private HighLevelList macroList = new HighLevelList();

	/**
	* Constructs the MacroObject.
	*/
	public MacroObject() {
	}

	/**
	* Reads objects from persistent storage.
	*/
	public void readExternal(ObjectInput in)
		throws IOException, ClassNotFoundException {
		try {
			VersionBuffer myv = (VersionBuffer) (in.readObject());
			VersionBuffer.chkNul(myv);

			paramVal = myv.getInt("paramVal");
			macroList = (HighLevelList) (myv.getProperty("macroList"));
			VersionBuffer.chkNul(macroList);
		} catch (ClassCastException e) {
			throw (new DataFormatException(e));
		}
	}

	/**
	* Writes objects to persistent storage.  @serialData TBD.
	*/
	public void writeExternal(ObjectOutput out) throws IOException {
		VersionBuffer myv = new VersionBuffer(VersionBuffer.WRITE);

		/* This if-then helps to prevent the spread of potential macro viruses
			by refusing to save any macro to persistent storage while another
			macro is playing.  This blocks one mechanism by which a macro
			virus could reproduce itself. */

		if (MacroRecordingObject.getMacroIsPlaying()) {
			myv.setInt("paramVal", 0);
			myv.setProperty("macroList", new HighLevelList());
		} else {
			myv.setInt("paramVal", paramVal);
			myv.setProperty("macroList", macroList);
		}

		out.writeObject(myv);
	}

	/**
	* Gets the parameter value of the macro.
	* @return The parameter value of the macro.
	*/
	public int getParamVal() {
		return (paramVal);
	}

	/**
	* Sets the parameter value of the macro.
	* @param in The parameter value of the macro.
	*/
	public void setParamVal(int in) {
		paramVal = in;
	}

	/**
	* Gets the macro's instruction list.
	* @return The macro's instruction list.
	*/
	public HighLevelList getMacroList() {
		return (macroList);
	}

	/**
	* Sets the macro's instruction list.
	* @param in The macro's instruction list.
	*/
	public void setMacroList(HighLevelList in) {
		macroList = in;
	}

	
}

