package verdantium;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLEditorKit;

import meta.WrapRuntimeException;
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
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | Needed a error dialog base class.                                    | Initial creation using code from geomdir.MbDialog.
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
* Read-only Verdantium property editor that displays a typical "alert" style dialog with a text message, and "OK" and "Cancel" buttons.
* Note: This class may be changed or replaced with a Swing-standard dialog
* at some point in the future.
* 
* @author Thorn Green
*/
public class MessageEditor extends PropertyEditAdapter {
	
	/**
	 * The data model of the source component.
	 */
	protected PropertyChangeSource src = null;
	
	/**
	 * The pane containing the message content.
	 */
	protected JPanel contentPane = new JPanel();
	
	/**
	 * The bottom pane containing the OK / Cancel buttons.
	 */
	protected JPanel buttonPane = new JPanel();

	/**
	* Shows the GUI of the property editor.
	* @return The GUI of the property editor.
	*/
	public JComponent getGUI() {
		return (contentPane);
	}

	/**
	 * Constructor.
	 * @param in_src The data model of the source component.
	 * @param lab The brief label text to be displayed along with the message.
	 * @param message The message to be displayed.
	 * @param horizontalScrollConstant The policy to be used with the horizontal scroll bar.
	 */
	public MessageEditor(
		PropertyChangeSource in_src,
		String lab,
		String message,
		int horizontalScrollConstant) {
		src = in_src;
		if (src != null)
			src.addPropertyChangeListener(this);
		contentPane.setLayout(new BorderLayout(5, 5));
		JEditorPane ta = new JEditorPane();
		ta.setEditorKit(new HTMLEditorKit());
		ta.setText("<B>" + message + "</B>");
		ta.setBackground(UIManager.getColor("Panel.background"));
		JScrollPane scp =
			new JScrollPane(
				ta,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				horizontalScrollConstant);
		ta.setEditable(false);
		contentPane.add("Center", scp);

		contentPane.add("North", new JLabel(lab));

		buttonPane.setLayout(new FlowLayout());
		JButton okButton = new JButton("OK");
		buttonPane.add(okButton);
		JButton cancelButton = new JButton("Cancel");
		buttonPane.add(cancelButton);

		ActionListener evt = Adapters.createGActionListener(this, "handleOK");
		okButton.addActionListener(evt);

		evt = Adapters.createGActionListener(this, "handleCancel");
		cancelButton.addActionListener(evt);

		contentPane.add("South", buttonPane);
		contentPane.setMinimumSize(new Dimension(250, 150));
		contentPane.setPreferredSize(new Dimension(250, 150));
	}

	/**
	 * Constructor.
	 * @param in_src The data model of the source component.
	 * @param lab The brief label text to be displayed along with the message.
	 * @param message The original error or exception that triggered the generation of the MessagaeEditor.
	 */
	public MessageEditor(
		PropertyChangeSource in_src,
		String lab,
		Throwable message) {
		this(
			in_src,
			lab,
			getString(message),
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	/**
	 * Gets an HTML-formatted string describing an error or exception.
	 * @param message The input error or exception from which to generate the string.
	 * @return The generated string.
	 */
	public static String getString(Throwable message) {
		String str1 = "";
		String str2 = "";

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream prt = new PrintStream(baos);
			message.printStackTrace(prt);
			baos.close();
			str1 = baos.toString();
		} catch (Exception ex) {
			throw (new WrapRuntimeException("Dialog Build Failed!", ex));
		}

		try {
			int count;
			int len = str1.length();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream prt = new PrintStream(baos);
			for (count = 0; count < len; ++count) {
				char ch = str1.charAt(count);
				if (ch != '\n')
					prt.print(ch);
				else
					prt.print("<BR>");
			}
			baos.close();
			str2 = baos.toString();
		} catch (Exception ex) {
			throw (new WrapRuntimeException("Dialog Build Failed!", ex));
		}

		return (str2);
	}

	/**
	* Handles the pressing of the "OK" button.
	* @param e The input event.
	*/
	public void handleOK(ActionEvent e) {
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
	* Handles the destruction of the property editor.
	*/
	public void handleDestroy() {
		if (src != null)
			src.removePropertyChangeListener(this);
	}

	
}

