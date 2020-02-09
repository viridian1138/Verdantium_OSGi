package verdantium.demo.gradientpane.help;

import java.net.URL;

import javax.swing.JComponent;

import verdantium.ProgramDirector;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.demo.gradientpane.GradientPane;
import verdantium.demo.gradientpane.GradientPanePropertyEditor;
import verdantium.vhelp.WrappingScrollingVerdantiumHelp;

//$$strtCprt
/*
     Gradient Pane rendering component by Thorn Green
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
 * Online help class for GradientPane.
 * 
 * @author tgreen
 *
 */
public class GradientPaneHelp extends WrappingScrollingVerdantiumHelp {
	private GradientPane MyCell = new GradientPane();
	private GradientPanePropertyEditor MyEdit0 = null;
	private GradientPanePropertyEditor MyEdit1 = null;
	private VerdantiumPropertiesEditor BkEdit = null;
	private VerdantiumPropertiesEditor Undo0 = null;
	private VerdantiumPropertiesEditor Macro0 = null;
	private VerdantiumPropertiesEditor Macro1 = null;

	/**
     * Constructs the help page.
     * @param in The URL of the help content page.
     */
	public GradientPaneHelp(URL in) {
		super(in);
		MyEdit0 = (GradientPanePropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit1 = (GradientPanePropertyEditor) (MyCell.makePropertiesEditor());
		MyEdit0.setSelectedIndex(0);
		MyEdit1.setSelectedIndex(1);
		BkEdit = MyEdit0.getCoACell().makePropertiesEditor();
		Undo0 = MyEdit0.createUndoPropertyEditor();
		Macro0 = MyEdit0.createClientMacroPropertyEditor();
		Macro1 = MyEdit0.createClientMacroRecordingEditor();
	}

	 /**
     * Displays the help page.
     * @param in The component in which to display the help page.
     */
	public static void run(VerdantiumComponent in) {
		URL u = GradientPaneHelp.class.getResource("GradientPaneHelpPage.rtf");
		JComponent frm = null;
		if (in != null)
			frm = in.getGUI();
		GradientPaneHelp hlp = new GradientPaneHelp(u);
		ProgramDirector.showPropertyEditor(hlp, frm, "Gradient Pane Help");
		hlp.parseElements();
	}

	@Override
	protected void dispatchAction(String txt) {
		if (txt.equals("comp")) {
			GradientPane MyC = MyCell;
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

		if (txt.equals("bkcol")) {
			VerdantiumComponent MyC = BkEdit;
			insertComponent(MyC);
		}
		
		if (txt.equals("undo0")) {
			VerdantiumComponent MyC = Undo0;
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
