package verdantium.standard;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;

import javax.swing.JPanel;

import meta.DataFormatException;
import meta.VersionBuffer;
import meta.WrapRuntimeException;
import verdantium.ProgramDirector;
import verdantium.TransVersionBuffer;
import verdantium.VerdantiumComponent;
import verdantium.utils.ComponentNotFoundException;
import verdantium.utils.IOComponentNotFoundException;
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
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Insert panel did not have desired UI characteristics.                | Among other things, made the UI transparent.
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
* the text of the {@link TextApp}.
* <P>
* @author Thorn Green
*/
public class TextAppInsertPanel extends JPanel {
	
	/**
	* Constructs the panel.
	*/
	public TextAppInsertPanel() {
		setOpaque(false);
	}

	/**
	* Reads the embedded component from persistent storage.
	* @param sta The version buffer from which to read the perssistent data.
	*/
	public void readData(TransVersionBuffer sta)
		throws IOException, ClassNotFoundException {
		try {
			DataFlavor[] myFlavors = { null };
			Class<? extends VerdantiumComponent> myClass = ProgramDirector.getComponentClass(sta, myFlavors);
			String myName = ProgramDirector.getComponentName();
			VerdantiumComponent myPart = myClass.newInstance();
			VersionBuffer.chkNul(myPart);
			comp = myPart;
			ProgramDirector.showComponent(myPart, this, null);
			DataFlavor flavor = myFlavors[0];
			myPart.loadPersistentData(flavor, sta);
		} catch (ClassNotFoundException ex) {
			throw (ex);
		} catch (ResourceNotFoundException ex) {
			throw (new IOResourceNotFoundException(ex));
		} catch (ComponentNotFoundException ex) {
			throw (new IOComponentNotFoundException(ex));
		} catch (IOException ex) {
			throw (ex);
		} catch (InstantiationException ex) {
			throw (new WrapRuntimeException("Creation Failed", ex));
		} catch (IllegalAccessException ex) {
			throw (new WrapRuntimeException("Creation Failed", ex));
		} catch (ClassCastException ex) {
			throw (new DataFormatException(ex));
		}
	}

	@Override
	public Component getComponent(int in) {
		if ((in == 0) && (comp != null)) {
			return (comp.getGUI());
		} else {
			return (super.getComponent(in));
		}
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

