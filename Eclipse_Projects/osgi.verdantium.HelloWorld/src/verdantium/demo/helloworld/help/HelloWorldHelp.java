package verdantium.demo.helloworld.help;

import java.net.URL;

import javax.swing.JComponent;

import verdantium.ProgramDirector;
import verdantium.VerdantiumComponent;
import verdantium.demo.helloworld.HelloWorld;
import verdantium.vhelp.WrappingScrollingVerdantiumHelp;

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
 * Online help class for HelloWorld.
 * 
 * @author tgreen
 *
 */
public class HelloWorldHelp extends WrappingScrollingVerdantiumHelp {

	/**
     * Constructs the help page.
     * @param in The URL of the help content page.
     */
	public HelloWorldHelp(URL in) {
		super(in);
	}

	 /**
     * Displays the help page.
     * @param in The component in which to display the help page.
     */
	public static void run(VerdantiumComponent in) {
		URL u = HelloWorldHelp.class.getResource("HelloWorldHelpPage.rtf");
		JComponent frm = null;
		if (in != null)
			frm = in.getGUI();
		HelloWorldHelp hlp = new HelloWorldHelp(u);
		ProgramDirector.showPropertyEditor(hlp, frm, "Hello World Help");
		hlp.parseElements();
	}

	@Override
	protected void dispatchAction(String txt) {
		if (txt.equals("comp")) {
			HelloWorld MyC = new HelloWorld();
			insertComponent(MyC);
		}
	}
	
    /**
     * Displays the help in a top-level window.
     * @param in Input parameters; not used.
     */
	public static void main(String[] in) {
		run(null);
	}

	
}

