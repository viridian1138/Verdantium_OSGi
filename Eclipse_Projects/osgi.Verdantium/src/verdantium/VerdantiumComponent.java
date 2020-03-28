package verdantium;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.IOException;

import javax.swing.JComponent;

import verdantium.utils.ComponentNotFoundException;
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
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 10/05/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for command-line arguments.                                  | Added support for command-line arguments (docs only).
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
* An embeddable component supporting multiple format persistence, and
* scripting, where the scripting support is provided by the inherited interface
* {@link EtherEventHandler}.  Note that the {@link #getGUI()} method allows
* one to change the user interface of a component via a subclass that overrides
* getGUI().
* <P>
* In addition to the interface shown here, all VerdantiumComponent classes
* are required to have a static method:
* <P>
* public static DataFlavor[] getPersistentInputDataFlavorsSupported( ) { return( null ); }
* <P>
* which returns the data flavors that the component is capable of loading.
* Also, all VerdantiumComponent classes except for property editors are
* required to have a zero argument constructor.  Many components have as an
* optional feature a main() method that allows them to be run as stand-alone applications.
* An example of such a main() method for an example class called MyComponent is as follows:
* <P>
* <pre>public static void main( String argv[] )
* 	{
*	ProgramDirector.initUI();
*	MyComponent myComp = new MyComponent();
*	ProgramDirector.showComponent( myComp , "My Component" , argv , false );
* 	}
* </pre>
* @author Thorn Green
*/
public interface VerdantiumComponent extends EtherEventHandler {
	
	/**
	* Returns the JComponent that provides the visual interface for this
	* component.
	* 
	* @return The JComponent that provides the visual interface.
	*/
	public JComponent getGUI();
	
	/**
	* Handles the destruction of the component.  Usually, a component will call
	* handleDestroy() on any sub-components it has, and break and event feeds it's
	* getting from other components.
	*/
	public void handleDestroy();

	/**
	* Returns the set of that flavors that this component can now save to.
	* <P>
	* If more than zero flavors are returned, then for the current version
	* of Verdantium the flavor list must fit one of two criteria:
	* <P>
	* 1. At least one of the flavors is a TransVersionBufferFlavor (e.g. a flavor
	* associated with a TransVersionBuffer).
	* <P>
	* 2. The flavor at index zero in the returned flavor list must be associated with
	* a Transferable that is either Serializable or Externalizable.
	* 
	* @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported();
	
	/**
	* Loads persistent data in a data format defined by <code>flavor</code>
	* when <code>trans</code> is NOT equal to null; the persistent data is
	* packaged in the Transferable <code>trans</code>.  When <code>trans</code> is
	* equal to null, reset the component to its initial state and then repaint
	* its GUI.
	* <P>
	* The component may assume that the flavor is one of the ones supported.
	* 
	* @param flavor The flavor in which to load the data.
    * @param trans The transferable from which to load the data.
	*/
	public void loadPersistentData(DataFlavor flavor, Transferable trans)
		throws IOException, ClassNotFoundException, ResourceNotFoundException, ComponentNotFoundException;
	
	/**
	* Saves persistent data in a data format defined by <code>flavor</code> by
	* placing the data in an appropriate Transferable, and then returning the
	* Transferable.
	* <P>
	* The component may assume that the flavor is one of the ones supported.
	* 
	* @param The flavor in which to save the data.
    * @return Transferable to which the data is saved.
	*/
	public Transferable savePersistentData(DataFlavor flavor) throws IOException;
	
}

