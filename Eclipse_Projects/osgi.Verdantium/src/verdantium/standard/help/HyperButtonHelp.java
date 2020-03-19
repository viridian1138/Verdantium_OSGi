package verdantium.standard.help;

import java.net.URL;

import javax.swing.JComponent;

import verdantium.ProgramDirector;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.core.DefaultPropertyEditor;
import verdantium.standard.HyperButton;
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
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Macro support.                                                       | Added documentation for macro support.
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
 * Online help class for hyper button.
 * 
 * @author tgreen
 *
 */
public class HyperButtonHelp extends WrappingScrollingVerdantiumHelp {
	
	private HyperButton MyCell = new HyperButton();
	
	private DefaultPropertyEditor MyEdit0 = null;
	
	private DefaultPropertyEditor MyEdit1_0 = null;
	
	private DefaultPropertyEditor MyEdit1_1 = null;
	
	private DefaultPropertyEditor MyEdit1_2 = null;
	
	private DefaultPropertyEditor MyEdit2 = null;
	
	private DefaultPropertyEditor MyEdit3 = null;
	
	private DefaultPropertyEditor MyEdit4 = null;
	
	private DefaultPropertyEditor MyEdit5 = null;
	
	private VerdantiumPropertiesEditor BkEdit = null;
	
	private VerdantiumPropertiesEditor Macro0 = null;
	
	private VerdantiumPropertiesEditor Macro1 = null;

	
	/**
     * Constructs the help page.
     * @param in The URL of the help content page.
     */
	public HyperButtonHelp(URL in) {
		super(in);
		MyEdit0 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit1_0 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit1_1 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit1_2 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit2 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit3 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit4 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit5 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit0.setSelectedIndex(0);
		MyEdit1_0.setSelectedIndex(1);
		MyEdit1_1.setSelectedIndex(1);
		MyEdit1_2.setSelectedIndex(1);
		MyEdit2.setSelectedIndex(2);
		MyEdit3.setSelectedIndex(3);
		MyEdit4.setSelectedIndex(4);
		MyEdit5.setSelectedIndex(5);
		MyEdit1_0.getProgramDirectorEditor().setSelectedIndex(0);
		MyEdit1_1.getProgramDirectorEditor().setSelectedIndex(1);
		MyEdit1_2.getProgramDirectorEditor().setSelectedIndex(2);
		BkEdit = MyEdit4.getBackgroundEditor().getBackCell().makePropertiesEditor();
		Macro0 = MyEdit0.createClientMacroPropertyEditor();
		Macro1 = MyEdit0.createClientMacroRecordingEditor();
	}

	 /**
     * Displays the help page.
     * @param in The component in which to display the help page.
     */
	public static void run(VerdantiumComponent in) {
		URL u = HyperButtonHelp.class.getResource("HyperButtonHelpPage.rtf");
		JComponent frm = null;
		if (in != null)
			frm = in.getGUI();
		HyperButtonHelp hlp = new HyperButtonHelp(u);
		ProgramDirector.showPropertyEditor(hlp, frm, "Hyper Button Help");
		hlp.parseElements();
	}

	@Override
	protected void dispatchAction(String txt) {
		if (txt.equals("comp")) {
			HyperButton MyC = MyCell;
			insertComponent(MyC);
		}

		if (txt.equals("edit0")) {
			VerdantiumComponent MyC = MyEdit0;
			insertComponent(MyC);
		}

		if (txt.equals("edit1_0")) {
			VerdantiumComponent MyC = MyEdit1_0;
			insertComponent(MyC);
		}

		if (txt.equals("edit1_1")) {
			VerdantiumComponent MyC = MyEdit1_1;
			insertComponent(MyC);
		}

		if (txt.equals("edit1_2")) {
			VerdantiumComponent MyC = MyEdit1_2;
			insertComponent(MyC);
		}

		if (txt.equals("edit2")) {
			VerdantiumComponent MyC = MyEdit2;
			insertComponent(MyC);
		}

		if (txt.equals("edit3")) {
			VerdantiumComponent MyC = MyEdit3;
			insertComponent(MyC);
		}

		if (txt.equals("edit4")) {
			VerdantiumComponent MyC = MyEdit4;
			insertComponent(MyC);
		}

		if (txt.equals("edit5")) {
			VerdantiumComponent MyC = MyEdit5;
			insertComponent(MyC);
		}

		if (txt.equals("bkcol")) {
			VerdantiumComponent MyC = BkEdit;
			insertComponent(MyC);
		}

		if (txt.equals("macro0")) {
			VerdantiumComponent MyC = Macro0;
			insertComponent(MyC);
		}

		if (txt.equals("macro1")) {
			VerdantiumComponent MyC = Macro1;
			insertComponent(MyC);
		}

	}

}
