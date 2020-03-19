/*
 * Created on Dec 7, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package verdantium.standard;

import java.awt.Component;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JPanel;

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
 * @author thorngreen
 *
 * Subclass of JEditorPane with altered exception handling.
 */
public class TextAppEditorPane extends JEditorPane {

	/**
	 * Constructor.
	 */
	public TextAppEditorPane() {
		super();
	}

	/**
	 * Constructor.
	 * @param url The URL.
	 * @throws java.io.IOException
	 */
	public TextAppEditorPane(URL url) throws IOException {
		super(url);
	}

	/**
	 * Constructor.
	 * @param url The URL.
	 * @throws java.io.IOException
	 */
	public TextAppEditorPane(String url) throws IOException {
		super(url);
	}

	/**
	 * Constructor.
	 * @param type The MIME type to initialize with.
	 * @param text The text with which to initialize.
	 */
	public TextAppEditorPane(String type, String text) {
		super(type, text);
	}

	@Override
	public void validate() {
		try {
			super.validate();
		} catch (Exception ex) {
			// Does Notning.
		}
	}

	@Override
	public void validateTree() {
		try {
			super.validateTree();
		} catch (Exception ex) {
			// Does Notning.
		}
	}

	@Override
	public Component getComponent(int in) {
		Component ret = null;
		try {
			ret = super.getComponent(in);
		} catch (Exception ex) {
			ret = new JPanel();
		}
		return (ret);
	}

	
}

