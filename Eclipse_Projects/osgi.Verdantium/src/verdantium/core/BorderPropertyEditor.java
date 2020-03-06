package verdantium.core;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Constructor;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

import meta.Meta;
import meta.WrapRuntimeException;
import verdantium.Adapters;
import verdantium.EtherEvent;
import verdantium.EtherEventPropertySource;
import verdantium.ProgramDirector;
import verdantium.VerdantiumBorderList;
import verdantium.VerdantiumUtils;
import verdantium.utils.IllegalInputException;
import verdantium.utils.ResourceNotFoundException;
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
*    | 03/11/2001            | Thorn Green (viridian_1138@yahoo.com)           | Property editor needed retargeting to work with CAGD Kit.            | Added retargeting ability.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
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
* Implements a client-independent editor for setting the border
* of a component.
* 
* @author Thorn Green
*/
public class BorderPropertyEditor extends PropertyEditAdapter {
	
	/**
	 * The model of the component being edited.
	 */
	private EtherEventPropertySource target = null;
	
	/**
	 * List of borders from which to select.
	 */
	protected JList<String> borderList = new JList<String>();
	
	/**
	 * The list model for the list of borders.
	 */
	protected DefaultListModel<String> listModel = new DefaultListModel<String>();

	/**
	* Creates a property editor for a client <code>in</code>.
	* @param in The model of the component being edited.
	*/
	public BorderPropertyEditor(EtherEventPropertySource in) {
		target = in;

		myPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scp =
			new JScrollPane(
				borderList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		myPanel.add(
			BorderLayout.NORTH,
			new JLabel("Select A Border, Then Press Apply : "));

		myPanel.add(BorderLayout.CENTER, scp);

		JButton applyButton = new JButton("Apply");
		myPanel.add(BorderLayout.SOUTH, applyButton);

		ActionListener myL =
			Adapters.createGActionListener(this, "handleApplyButton");
		applyButton.addActionListener(myL);

		borderList.setModel(listModel);
		Iterator<String> it = VerdantiumBorderList.getBorderNames().iterator();
		while (it.hasNext()) {
			listModel.addElement(it.next());
		}
		VerdantiumBorderList.addClassPropertyChangeListener(this);
	}

	/**
	* Handles the apply button in the editor's GUI.  To handle the border change, the client
	* must implement the {@link PropertyEditEtherEvent#setBorderObject} Ether Event.
	* @param e The input event.
	*/
	public void handleApplyButton(ActionEvent e) {
		EtherEvent send =
			new PropertyEditEtherEvent(
				this,
				PropertyEditEtherEvent.setBorderObject,
				null,
				target);

		try {
			int index = borderList.getSelectedIndex();
			if (index < 0) {
				throw (
					new IllegalInputException("You Must Select A Border From The List."));
			}

			Object[] params =
				VerdantiumBorderList.getConstructParamsForIndex(index);

			send.setParameter(params);
			ProgramDirector.fireEtherEvent(send, null);
		} catch (Throwable ex) {
			handleThrow(ex);
		}

	}

	/**
	* Handles property change events.
	* @param evt The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName()
			== VerdantiumBorderList.BORDER_LIST_CHANGED) {
			try {
				listModel.clear();
				Iterator<String> it = VerdantiumBorderList.getBorderNames().iterator();
				while (it.hasNext()) {
					listModel.addElement(it.next());
				}
			} catch (Exception ex) {
				throw (
					new WrapRuntimeException("Border List Change Failed", ex));
			}
		}
	}

	/**
	* Creates a border given the input parameters of the border's constructor.
	* @param cName The border's class name.
	* @param types The parameter types of the border's constructor.
	* @param params The parameters of the border's constructor.
	*/
	public static Border createBorder(
		String cName,
		Class<?>[] types,
		Object[] params)
		throws ResourceNotFoundException {
		Border myBorder = null;

		if (cName != null) {
			try {
				Class<? extends Border> myClass =
					(Class<? extends Border>)( Class.forName(cName, true, Meta.getDefaultClassLoader()) );
				Constructor<? extends Border> myC = myClass.getConstructor(types);
				myBorder = myC.newInstance(params);
			} catch (Exception e) {
				throw (new ResourceNotFoundException("Border not found.", e));
			}
		}

		return (myBorder);
	}

	/**
	* Gets a string array from a type array.  Used to make the type array persistent.
	* @param types The input type array.
	* @return The output string array.
	*/
	public static String[] getTypeStrArray( Class<?>[] types ) {
		String[] str = new String[types.length];
		int count;
		for (count = 0; count < types.length; ++count)
			str[count] = types[count].getName();

		return (str);
	}

	/**
	* Gets a type array from a string array.  Used to get data from persistent storage.
	* @param in The input string array.
	* @return The output type array.
	*/
	public static Class<?>[] getTypeObjArray(Object in)
		throws ResourceNotFoundException {
		String[] types = (String[]) (in);
		Class<?>[] clss = new Class<?>[types.length];
		int count;
		for (count = 0; count < types.length; ++count) {
			if (types[count].equals("int")) {
				clss[count] = Integer.TYPE;
			} else {
				try {
					clss[count] = Class.forName(types[count]);
				} catch (Exception e) {
					throw (
						new ResourceNotFoundException("Border not found.", e));
				}
			}
		}

		return (clss);
	}

	/**
	 * Panel containing the GUI of the component.
	 */
	private JPanel myPanel = new JPanel();

	/**
	* Sets the model of the component being edited.
	* @param in The model of the component being edited.
	*/
	public void setTarget(EtherEventPropertySource in) {
		target = in;
	}

	/**
	* Returns the GUI of the property editor.
	* @return The GUI of the property editor.
	*/
	public JComponent getGUI() {
		return (myPanel);
	}

	/**
	* Handles the destruction of the component.
	*/
	public void handleDestroy() {
		VerdantiumBorderList.removeClassPropertyChangeListener(this);
	}

	/**
	* Handles the throwing of an error or exception.
	* @param in The input error or exception.
	*/
	public void handleThrow(Throwable in) {
		VerdantiumUtils.handleThrow(in, this, target);
	}

	
}

