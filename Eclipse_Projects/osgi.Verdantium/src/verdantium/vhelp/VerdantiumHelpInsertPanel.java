package verdantium.vhelp;

import java.awt.datatransfer.DataFlavor;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.swing.JPanel;

import meta.VersionBuffer;
import meta.WrapRuntimeException;
import verdantium.ProgramDirector;
import verdantium.TransVersionBuffer;
import verdantium.VerdantiumComponent;
import verdantium.utils.ComponentNotFoundException;
import verdantium.utils.IOResourceNotFoundException;
import verdantium.utils.ResourceNotFoundException;

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
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Slow performance when inserting large numbers of figures.            | Improved insertion performance by taking better advantage of JEditorPane.
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
* Panel used to enclose Verdantium components that are inserted directly into
* the text of the {@link VerdantiumHelp}.
* <P>
* @author Thorn Green
*/
public class VerdantiumHelpInsertPanel
	extends JPanel
	implements Externalizable {
	
	/**
	* Constructs the panel.
	*/
	public VerdantiumHelpInsertPanel() {
		setOpaque(false);
	}

	/**
	* Reads the embedded component from persistent storage.
	*/
	public void readExternal(ObjectInput in)
		throws IOException, ClassNotFoundException {
		VersionBuffer myv = (VersionBuffer) (in.readObject());

		try {
			TransVersionBuffer sta =
				(TransVersionBuffer) (myv.getProperty("Component"));
			DataFlavor[] myFlavors = { null };
			Class<? extends VerdantiumComponent> myClass = ProgramDirector.getComponentClass(sta, myFlavors);
			String MyName = ProgramDirector.getComponentName();
			VerdantiumComponent myPart = myClass.newInstance();
			comp = myPart;
			ProgramDirector.showComponent(myPart, this, null);
			DataFlavor flavor = myFlavors[0];
			myPart.loadPersistentData(flavor, sta);
		} catch (ClassNotFoundException e) {
			throw (e);
		} catch (IOException e) {
			throw (e);
		} catch (InstantiationException e) {
			throw (new WrapRuntimeException("Component Creation Failed", e));
		} catch (IllegalAccessException e) {
			throw (new WrapRuntimeException("Component Creation Failed", e));
		} catch (ComponentNotFoundException e) {
			throw (new WrapRuntimeException("Component Creation Failed", e));
		} catch (ResourceNotFoundException e) {
			throw (new IOResourceNotFoundException("Resource Not Found", e));
		}
	}

	/**
	* Writes the embedded component to serial storage.  @serialData TBD.
	*/
	public void writeExternal(ObjectOutput out) throws IOException {
		VersionBuffer myv = new VersionBuffer(VersionBuffer.WRITE);

		Object st = ProgramDirector.getSerializableState(comp);
		myv.setProperty("Component", st);

		out.writeObject(myv);
	}

	/**
	* Gets the embedded Verdantium component.
	* @return The embedded Verdantium component.
	*/
	public VerdantiumComponent getComponent() {
		return (comp);
	}

	/**
	* Sets the embedded Verdantium component.
	* @param in The embedded Verdantium component.
	*/
	public void setComponent(VerdantiumComponent in) {
		comp = in;
	}

	/**
	* The embedded Verdantium component.
	*/
	VerdantiumComponent comp = null;
}
