
package verdantium.demo.helloworld;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JLabel;

import meta.DataFormatException;
import meta.VersionBuffer;
import verdantium.ProgramDirector;
import verdantium.TransVersionBuffer;
import verdantium.TransVersionBufferFlavor;
import verdantium.VerdantiumComponent;
import verdantium.demo.helloworld.help.HelloWorldHelp;
import verdantium.utils.VerdantiumDragUtils;
import verdantium.utils.VerdantiumDropUtils;
import verdantium.xapp.JpApplicationAdapter;


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
 * This is a demonstration of a "Hello World" component. It displays "Hello
 * World", and supports embedding, persistence, and scripting. Note: most of the
 * scripting support for a simple component like this is handled by Verdantium
 * itself through {@link verdantium.StandardEtherEvent}.
 * <P>
 * 
 * @author Thorn Green
 */
public class HelloWorld extends JpApplicationAdapter {

	/**
	* Returns the GUI for the component.
    * @return The GUI.
	*/
	public JComponent getGUI() {
		return (this);
	}

	/**
	 * Constructs the component.
	 */
	public HelloWorld() {
		JLabel lab = new JLabel("Hello World");
		lab.setFont(new Font("Serif", Font.PLAIN, 24));
		add(BorderLayout.CENTER, lab);
		setMinimumSize(new Dimension(2, 2));
		setPreferredSize(new Dimension(200, 100));
		VerdantiumDragUtils.setDragUtil(this, this);
		VerdantiumDropUtils.setDropUtil(this, this, null);
	}

	/**
	* Returns the data flavors that the component can load from persistent storage.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF = { new TransVersionBufferFlavor("Hello World",
				"Hello World") };
		return (MyF);
	}

	/**
	* Returns the data flavors that the component can save to persistent storage.
    * @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF = { new TransVersionBufferFlavor("Hello World",
				"Hello World") };
		return (MyF);
	}

	/**
	* Loads data for the component from persistent storage.
    * @param flavor The flavor in which to load the data.
    * @param trans The transferable from which to load the data.
	*/
	public void loadPersistentData(DataFlavor flavor, Transferable trans)
			throws IOException {
		if (trans == null) {
		} else {
			try {
				TransVersionBuffer MyF = (TransVersionBuffer) trans;
				VersionBuffer.chkNul(MyF);
			} catch (ClassCastException e) {
				throw (new DataFormatException(e));
			}
		}
	}

	/**
	* Saves the component to persistent storage.
    * @param The flavor in which to save the data.
    * @return Transferable to which the data is saved.
	*/
	public Transferable savePersistentData(DataFlavor flavor) {
		TransVersionBuffer MyF = new TransVersionBuffer("Hello World",
				"Hello World");
		return (MyF);
	}

	/**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		HelloWorldHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		HelloWorld MyComp = new HelloWorld();
		ProgramDirector.showComponent(MyComp, "Hello World", argv, false);
	}
	
	
}

