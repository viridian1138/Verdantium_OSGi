package verdantium.standard.flowbox.help;

import java.net.URL;

import javax.swing.JComponent;

import verdantium.ProgramDirector;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.core.DefaultPropertyEditor;
import verdantium.standard.flowbox.FlowBox;
import verdantium.standard.flowbox.FlowBoxPropertyEditor;
import verdantium.vhelp.WrappingScrollingVerdantiumHelp;

//$$strtCprt
/*
 FlowBox diagram animation by Thorn Green
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
 * Online help class for FlowBox.
 * 
 * @author tgreen
 *
 */
public class FlowBoxHelp extends WrappingScrollingVerdantiumHelp {
	private FlowBox MyCell = new FlowBox();

	private DefaultPropertyEditor MyEdit0 = null;

	private DefaultPropertyEditor MyEdit1 = null;

	private DefaultPropertyEditor MyEdit2 = null;

	private DefaultPropertyEditor MyEdit3 = null;

	private VerdantiumPropertiesEditor TopEdit = null;

	private VerdantiumPropertiesEditor Undo0 = null;

	private VerdantiumPropertiesEditor Macro0 = null;

	private VerdantiumPropertiesEditor Macro1 = null;

	/**
     * Constructs the help page.
     * @param in The URL of the help content page.
     */
	public FlowBoxHelp(URL in) {
		super(in);
		try {
			MyEdit0 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
			MyEdit1 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
			MyEdit2 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
			MyEdit3 = (DefaultPropertyEditor) (MyCell.makePropertiesEditor());
			MyEdit0.setSelectedIndex(0);
			MyEdit1.setSelectedIndex(1);
			MyEdit2.setSelectedIndex(2);
			MyEdit3.setSelectedIndex(3);
			TopEdit = ((FlowBoxPropertyEditor) MyEdit0).getTopColorCell()
					.makePropertiesEditor();
			Undo0 = MyEdit0.createUndoPropertyEditor();
			Macro0 = MyEdit0.createClientMacroPropertyEditor();
			Macro1 = MyEdit0.createClientMacroRecordingEditor();
		} catch (RuntimeException ex) {
			ex.printStackTrace(System.out);
			throw (ex);
		}
	}

	 /**
     * Displays the help page.
     * @param in The component in which to display the help page.
     */
	public static void run(VerdantiumComponent in) {
		URL u = FlowBoxHelp.class.getResource("FlowBoxHelpPage.rtf");
		JComponent frm = null;
		if (in != null)
			frm = in.getGUI();
		FlowBoxHelp hlp = new FlowBoxHelp(u);
		ProgramDirector.showPropertyEditor(hlp, frm, "FlowBox Help");
		hlp.parseElements();
	}

	@Override
	protected void dispatchAction(String txt) {
		if (txt.equals("comp")) {
			FlowBox MyC = MyCell;
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

		if (txt.equals("topcol")) {
			VerdantiumComponent MyC = TopEdit;
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
