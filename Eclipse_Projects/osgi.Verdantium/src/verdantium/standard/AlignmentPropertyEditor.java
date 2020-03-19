package verdantium.standard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import meta.WrapRuntimeException;
import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.VerdantiumUtils;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.utils.VerticalLayout;
import verdantium.xapp.PropertyEditAdapter;

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
*    | 09/10/2001            | Thorn Green (viridian_1138@yahoo.com)           | Second-Cut at Error Handling.                                        | Second-Cut at Error Handling.
*    | 11/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Needed a way to control paragraph alignments.                        | Created this class by taking code from another class as a template.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
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
* A property editor that allows the size of the document page width to be changed for
* a client scrolling version of {@link TextApp}.
* <P>
* @author Thorn Green
*/
public class AlignmentPropertyEditor extends PropertyEditAdapter {
	
	/**
	* The panel in which the property editor lies.
	*/
	private JPanel myPan = new JPanel();
	
	/**
	* The TextApp being edited.
	*/
	private TextApp myPage = null;
	
	/**
	* The document being edited.
	*/
	private JEditorPane myDoc = null;
	
	/**
	* Radio button for selecting left justify.
	*/
	private JRadioButton leftButton = new JRadioButton("Left Justify");
	
	/**
	* Radio button for selecting center justify.
	*/
	private JRadioButton centerButton = new JRadioButton("Center Justify");
	
	/**
	* Radio button for selecting right justify.
	*/
	private JRadioButton rightButton = new JRadioButton("Right Justify");
	
	/**
	* Radio button for selecting fill justify.
	*/
	private JRadioButton fillButton = new JRadioButton("Fill Justify");

	/**
	* Constructs the property editor for a given TextApp.
	* @param in The TextApp component being edited.
	* @param doc The editor pane of the TextApp being edited.
	*/
	public AlignmentPropertyEditor(TextApp in, JEditorPane doc) {
		myPage = in;
		myPan.setLayout(new BorderLayout(0, 0));
		JButton applyButton = new JButton("Apply");
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		JPanel pan3 = new JPanel();
		myPan.add(BorderLayout.SOUTH, pan3);

		JPanel pan2 = new JPanel();
		myPan.add(BorderLayout.CENTER, pan2);
		pan2.setLayout(new VerticalLayout(1));
		pan2.add("any", new JLabel("Alignment : "));
		pan2.add("any", leftButton);
		pan2.add("any", centerButton);
		pan2.add("any", rightButton);
		/* pan2.add( "any" , fillButton ); */

		ButtonGroup bg = new ButtonGroup();
		bg.add(leftButton);
		bg.add(centerButton);
		bg.add(rightButton);
		bg.add(fillButton);

		leftButton.setSelected(true);

		try {
			StyledDocument styl = (StyledDocument) (doc.getDocument());
			AttributeSet at =
				styl
					.getCharacterElement(doc.getCaretPosition())
					.getAttributes();
			SimpleAttributeSet simp = new SimpleAttributeSet(at);

			int alignment = StyleConstants.getAlignment(simp);
			if (alignment == StyleConstants.ALIGN_LEFT)
				leftButton.setSelected(true);
			if (alignment == StyleConstants.ALIGN_CENTER)
				centerButton.setSelected(true);
			if (alignment == StyleConstants.ALIGN_RIGHT)
				rightButton.setSelected(true);
			if (alignment == StyleConstants.ALIGN_JUSTIFIED)
				fillButton.setSelected(true);
		} catch (Exception ex) {
			throw (new WrapRuntimeException("Doc. Elem. Failed", ex));
		}

		pan3.setLayout(new FlowLayout());
		pan3.add(okButton);
		pan3.add(applyButton);
		pan3.add(cancelButton);

		myPage.addPropertyChangeListener(this);

		ActionListener actL =
			Adapters.createGActionListener(this, "handleApply");
		applyButton.addActionListener(actL);

		actL = Adapters.createGActionListener(this, "handleOk");
		okButton.addActionListener(actL);

		actL = Adapters.createGActionListener(this, "handleCancel");
		cancelButton.addActionListener(actL);
	}

	/**
	* Gets the GUI of the property editor.
	* @return The GUI of the property editor.
	*/
	public JComponent getGUI() {
		return (myPan);
	}

	/**
	* Handles the destruction of the component bu removing appropriate property change listeners.
	*/
	public void handleDestroy() {
		myPage.removePropertyChangeListener(this);
	}

	/**
	* Handles a button-press event from the Apply button by changing
	* the size of the client page size handler.
	* @param e The input event.
	*/
	public void handleApply(ActionEvent e) {
		try {
			int alignment = StyleConstants.ALIGN_LEFT;

			if (leftButton.isSelected())
				alignment = StyleConstants.ALIGN_LEFT;
			if (centerButton.isSelected())
				alignment = StyleConstants.ALIGN_CENTER;
			if (rightButton.isSelected())
				alignment = StyleConstants.ALIGN_RIGHT;
			if (fillButton.isSelected())
				alignment = StyleConstants.ALIGN_JUSTIFIED;

			EtherEvent send =
				new PropertyEditEtherEvent(
					this,
					TextApp.setParagraphAlignment,
					null,
					myPage);
			Integer param = new Integer(alignment);
			send.setParameter(param);
			ProgramDirector.fireEtherEvent(send, null);
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles the pressing of the "OK" button.
	* @param e The input event.
	*/
	public void handleOk(ActionEvent e) {
		handleApply(e);
		VerdantiumUtils.disposeContainer(this);
	}

	/**
	* Handles the pressing of the "Cancel" button.
	* @param e The input event.
	*/
	public void handleCancel(ActionEvent e) {
		VerdantiumUtils.disposeContainer(this);
	}

	/**
	* Handles the throwing of an error or exception.
	* @param in The input error or exception.
	*/
	public void handleThrow(Throwable in) {
		VerdantiumUtils.handleThrow(in, myPage, myPage);
	}

	
}


