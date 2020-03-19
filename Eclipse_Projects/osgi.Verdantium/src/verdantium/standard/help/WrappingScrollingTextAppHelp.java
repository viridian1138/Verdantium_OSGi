package verdantium.standard.help;

import java.net.URL;

import javax.swing.JComponent;

import meta.WrapRuntimeException;
import verdantium.ProgramDirector;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.core.DefaultPropertyEditor;
import verdantium.standard.TextAppPropertyEditor;
import verdantium.standard.WrappingScrollingTextApp;
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
*    | 11/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for paragraph formatting.                                    | Added help to describe paragraph formatting.
*    | 04/21/2002            | Thorn Green (viridian_1138@yahoo.com)           | Find/Replace support.                                                | Added documentation for find/replace support.
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
 * Online help class for wrapping scrolling text app.
 * 
 * @author tgreen
 *
 */
public class WrappingScrollingTextAppHelp extends WrappingScrollingVerdantiumHelp {
	
	private WrappingScrollingTextApp MyCell = new WrappingScrollingTextApp();
	
	private DefaultPropertyEditor MyEdit0 = null;
	
	private DefaultPropertyEditor MyEdit1_0 = null;
	
	private DefaultPropertyEditor MyEdit1_1 = null;
	
	private DefaultPropertyEditor MyEdit1_2 = null;
	
	private DefaultPropertyEditor MyEdit2 = null;
	
	private DefaultPropertyEditor MyEdit3 = null;
	
	private DefaultPropertyEditor MyEdit4 = null;
	
	private DefaultPropertyEditor MyEdit5 = null;
	
	private DefaultPropertyEditor MyEdit6_0 = null;
	
	private DefaultPropertyEditor MyEdit6_1 = null;
	
	private DefaultPropertyEditor MyEdit6_2 = null;
	
	private DefaultPropertyEditor MyEdit7 = null;
	
	private VerdantiumPropertiesEditor BkEdit = null;
	
	private VerdantiumPropertiesEditor FgEdit = null;
	
	private VerdantiumPropertiesEditor Find0 = null;
	
	private VerdantiumPropertiesEditor Repl0 = null;
	
	private VerdantiumPropertiesEditor Macro0 = null;
	
	private VerdantiumPropertiesEditor Macro1 = null;
	
	private VerdantiumPropertiesEditor Format0 = null;
	
	private VerdantiumPropertiesEditor Format1 = null;

	
	/**
     * Constructs the help page.
     * @param in The URL of the help content page.
     */
	public WrappingScrollingTextAppHelp(URL in) {
		super(in);
		MyEdit0 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit1_0 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit1_1 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit1_2 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit2 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit3 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit4 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit5 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit6_0 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit6_1 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit6_2 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit7 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit0.setSelectedIndex(0);
		MyEdit1_0.setSelectedIndex(1);
		MyEdit1_1.setSelectedIndex(1);
		MyEdit1_2.setSelectedIndex(1);
		MyEdit2.setSelectedIndex(2);
		MyEdit3.setSelectedIndex(3);
		MyEdit4.setSelectedIndex(4);
		MyEdit5.setSelectedIndex(5);
		MyEdit6_0.setSelectedIndex(6);
		MyEdit6_1.setSelectedIndex(6);
		MyEdit6_2.setSelectedIndex(6);
		MyEdit7.setSelectedIndex(7);
		MyEdit1_0.getProgramDirectorEditor().setSelectedIndex(0);
		MyEdit1_1.getProgramDirectorEditor().setSelectedIndex(1);
		MyEdit1_2.getProgramDirectorEditor().setSelectedIndex(2);
		((TextAppPropertyEditor) MyEdit6_0).getInsertEditor().setSelectedIndex(0);
		((TextAppPropertyEditor) MyEdit6_1).getInsertEditor().setSelectedIndex(1);
		((TextAppPropertyEditor) MyEdit6_2).getInsertEditor().setSelectedIndex(2);
		BkEdit = MyEdit4.getBackgroundEditor().getBackCell().makePropertiesEditor();
		FgEdit = ((TextAppPropertyEditor) MyEdit0).getFgColorCell().makePropertiesEditor();
		Find0 = MyEdit0.createFindPropertyEditor();
		Repl0 = MyEdit0.createReplacePropertyEditor();
		Macro0 = MyEdit0.createClientMacroPropertyEditor();
		Macro1 = MyEdit0.createClientMacroRecordingEditor();
		try {
			Format0 = MyCell.createAlignmentPropertyEditor();
			Format1 = MyCell.createParagraphPropertyEditor();
		}
		catch (Exception ex) {
			throw (new WrapRuntimeException(ex));
		}
	}

	 /**
     * Displays the help page.
     * @param in The component in which to display the help page.
     */
	public static void run(VerdantiumComponent in) {
		URL u = WrappingScrollingTextAppHelp.class.getResource("WrappingScrollingTextAppHelpPage.rtf");
		JComponent frm = null;
		if (in != null)
			frm = in.getGUI();
		WrappingScrollingTextAppHelp hlp = new WrappingScrollingTextAppHelp(u);
		ProgramDirector.showPropertyEditor(hlp, frm, "Wrapping Scrolling Text App Help");
		hlp.parseElements();
	}

	@Override
	protected void dispatchAction(String txt) {
		if (txt.equals("comp")) {
			WrappingScrollingTextApp MyC = MyCell;
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

		if (txt.equals("edit6_0")) {
			VerdantiumComponent MyC = MyEdit6_0;
			insertComponent(MyC);
		}

		if (txt.equals("edit6_1")) {
			VerdantiumComponent MyC = MyEdit6_1;
			insertComponent(MyC);
		}

		if (txt.equals("edit6_2")) {
			VerdantiumComponent MyC = MyEdit6_2;
			insertComponent(MyC);
		}

		if (txt.equals("edit7")) {
			VerdantiumComponent MyC = MyEdit7;
			insertComponent(MyC);
		}

		if (txt.equals("bkcol")) {
			VerdantiumComponent MyC = BkEdit;
			insertComponent(MyC);
		}

		if (txt.equals("fgcol")) {
			VerdantiumComponent MyC = FgEdit;
			insertComponent(MyC);
		}

		if (txt.equals("find0")) {
			VerdantiumComponent MyC = Find0;
			insertComponent(MyC);
		}

		if (txt.equals("repl0")) {
			VerdantiumComponent MyC = Repl0;
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

		if (txt.equals("format0")) {
			VerdantiumComponent MyC = Format0;
			insertComponent(MyC);
		}

		if (txt.equals("format1")) {
			VerdantiumComponent MyC = Format1;
			insertComponent(MyC);
		}

	}

	
}

