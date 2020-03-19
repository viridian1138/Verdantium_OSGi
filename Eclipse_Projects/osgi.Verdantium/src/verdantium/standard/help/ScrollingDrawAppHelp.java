package verdantium.standard.help;

import java.net.URL;

import javax.swing.JComponent;

import verdantium.ProgramDirector;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.core.DefaultPropertyEditor;
import verdantium.standard.DrawAppPropertyEditor;
import verdantium.standard.ScrollingDrawApp;
import verdantium.undo.UndoPropertyEditor;
import verdantium.vhelp.WrappingScrollingVerdantiumHelp;

//$$strtCprt
/*
     Verdantium compound-document framework by Thorn Green
	Copyright (C) 2007 Thorn Green

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
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Multiple changes to DrawApp.                                         | Added documentation for the changes.
*    | 04/21/2002            | Thorn Green (viridian_1138@yahoo.com)           | Find/Replace support.                                                | Added documentation for find/replace support.
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
 * Online help class for scrolling draw app.
 * 
 * @author tgreen
 *
 */
public class ScrollingDrawAppHelp extends WrappingScrollingVerdantiumHelp {
	
	private ScrollingDrawApp MyCell = new ScrollingDrawApp();
	
	private DefaultPropertyEditor MyEdit0 = null;
	
	private DefaultPropertyEditor MyEdit1 = null;
	
	private DefaultPropertyEditor MyEdit2 = null;
	
	private DefaultPropertyEditor MyEdit3 = null;
	
	private DefaultPropertyEditor MyEdit4_0 = null;
	
	private DefaultPropertyEditor MyEdit4_1 = null;
	
	private DefaultPropertyEditor MyEdit4_2 = null;
	
	private DefaultPropertyEditor MyEdit5 = null;
	
	private DefaultPropertyEditor MyEdit6 = null;
	
	private DefaultPropertyEditor MyEdit7 = null;
	
	private DefaultPropertyEditor MyEdit8 = null;
	
	private DefaultPropertyEditor MyEdit9 = null;
	
	private VerdantiumPropertiesEditor BkEdit = null;
	
	private VerdantiumPropertiesEditor LnEdit = null;
	
        private VerdantiumPropertiesEditor Undo0 = null;
        
	private VerdantiumPropertiesEditor Undo1 = null;
	
	private VerdantiumPropertiesEditor Find0 = null;
	
	private VerdantiumPropertiesEditor Repl0 = null;
	
	private VerdantiumPropertiesEditor Macro0 = null;
	
	private VerdantiumPropertiesEditor Macro1 = null;

	
	/**
     * Constructs the help page.
     * @param in The URL of the help content page.
     */
	public ScrollingDrawAppHelp(URL in) {
		super(in);
		MyEdit0 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit1 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit2 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit3 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit4_0 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit4_1 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit4_2 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit5 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit6 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit7 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit8 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit9 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit0.setSelectedIndex(0);
		MyEdit1.setSelectedIndex(1);
		MyEdit2.setSelectedIndex(2);
		MyEdit3.setSelectedIndex(3);
		MyEdit4_0.setSelectedIndex(4);
		MyEdit4_1.setSelectedIndex(4);
		MyEdit4_2.setSelectedIndex(4);
		MyEdit5.setSelectedIndex(5);
		MyEdit6.setSelectedIndex(6);
		MyEdit7.setSelectedIndex(7);
		MyEdit8.setSelectedIndex(8);
		MyEdit9.setSelectedIndex(9);
		MyEdit4_0.getProgramDirectorEditor().setSelectedIndex(0);
		MyEdit4_1.getProgramDirectorEditor().setSelectedIndex(1);
		MyEdit4_2.getProgramDirectorEditor().setSelectedIndex(2);
		BkEdit = MyEdit7.getBackgroundEditor().getBackCell().makePropertiesEditor();
		LnEdit = ((DrawAppPropertyEditor) MyEdit1).getLineColorCell().makePropertiesEditor();
                Undo0 = MyEdit0.createUndoPropertyEditor();
		Undo1 = ((UndoPropertyEditor) Undo0).createUndoHistoryEditor();
		Find0 = MyEdit0.createFindPropertyEditor();
		Repl0 = MyEdit0.createReplacePropertyEditor();
		Macro0 = MyEdit0.createClientMacroPropertyEditor();
		Macro1 = MyEdit0.createClientMacroRecordingEditor();
	}

	 /**
     * Displays the help page.
     * @param in The component in which to display the help page.
     */
	public static void run(VerdantiumComponent in) {
		URL u = ScrollingDrawAppHelp.class.getResource("ScrollingDrawAppHelpPage.rtf");
		JComponent frm = null;
		if (in != null)
			frm = in.getGUI();
		ScrollingDrawAppHelp hlp = new ScrollingDrawAppHelp(u);
		ProgramDirector.showPropertyEditor(hlp, frm, "Scrolling Draw App Help");
		hlp.parseElements();
	}

	@Override
	protected void dispatchAction(String txt) {
		if (txt.equals("comp")) {
			ScrollingDrawApp MyC = MyCell;
			insertComponent(MyC);
		}

		if (txt.equals("edit0")) {
			VerdantiumComponent MyC = MyEdit0;
			insertComponent(MyC);
		}

		if (txt.equals("edit1")) {
			VerdantiumComponent MyC = MyEdit1;
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

		if (txt.equals("edit4_0")) {
			VerdantiumComponent MyC = MyEdit4_0;
			insertComponent(MyC);
		}

		if (txt.equals("edit4_1")) {
			VerdantiumComponent MyC = MyEdit4_1;
			insertComponent(MyC);
		}

		if (txt.equals("edit4_2")) {
			VerdantiumComponent MyC = MyEdit4_2;
			insertComponent(MyC);
		}

		if (txt.equals("edit5")) {
			VerdantiumComponent MyC = MyEdit5;
			insertComponent(MyC);
		}

		if (txt.equals("edit6")) {
			VerdantiumComponent MyC = MyEdit6;
			insertComponent(MyC);
		}

		if (txt.equals("edit7")) {
			VerdantiumComponent MyC = MyEdit7;
			insertComponent(MyC);
		}

		if (txt.equals("edit8")) {
			VerdantiumComponent MyC = MyEdit8;
			insertComponent(MyC);
		}

		if (txt.equals("edit9")) {
			VerdantiumComponent MyC = MyEdit9;
			insertComponent(MyC);
		}

		if (txt.equals("bkcol")) {
			VerdantiumComponent MyC = BkEdit;
			insertComponent(MyC);
		}

		if (txt.equals("lncol")) {
			VerdantiumComponent MyC = LnEdit;
			insertComponent(MyC);
		}
                
                if (txt.equals("undo0")) {
			VerdantiumComponent MyC = Undo0;
			insertComponent(MyC);
		}

		if (txt.equals("undo1")) {
			VerdantiumComponent MyC = Undo1;
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

	}

}
