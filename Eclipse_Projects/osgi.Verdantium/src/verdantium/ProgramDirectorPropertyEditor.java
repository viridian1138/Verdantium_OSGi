package verdantium;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.net.URL;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import verdantium.clmgr.ComponentManager;
import verdantium.utils.IllegalInputException;
import verdantium.utils.VerdantiumFileDndTextField;
import verdantium.utils.VerdantiumJListDragUtils;
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
*    | 10/07/2000            | Thorn Green (viridian_1138@yahoo.com)           | Problems with JDK 1.3 applets.                                       | Made code compatible with JDK 1.3 applets.
*    | 10/22/2000            | Thorn Green (viridian_1138@yahoo.com)           | Methods did not have names that followed standard Java conventions.  | Performed a global modification to bring the names within spec.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/10/2001            | Thorn Green (viridian_1138@yahoo.com)           | Second-Cut at Error Handling.                                        | Second-Cut at Error Handling.
*    | 10/05/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for command-line arguments.                                  | Added support for command-line arguments.
*    | 12/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Third-Cut at Error Handling.                                         | Third-Cut at Error Handling.
*    | 10/12/2002            | Thorn Green (viridian_1138@yahoo.com)           | Support for Discovery.                                               | Added support for Discovery.
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
*
*
*/

/**
* Provides an interface for creating an instance of a component, loading a file
* using a component, viewing a URL using a component, or doing any of the
* aforementioned in an embedded frame.
* 
* @author Thorn Green
*/
public class ProgramDirectorPropertyEditor extends PropertyEditAdapter {
	
	/**
	 * Tabbed pane for switching between different options for creating a component.
	 */
	private JTabbedPane myPane = new JTabbedPane();
	
	/**
	 * The top-level panel for the editor GUI.
	 */
	private JPanel myPanel = new JPanel();
	
	/**
	 * List of human-readable component names from which to select a component to create or display help.
	 */
	private final JList<String> myList = new JList<String>();
	
	/**
	 * The mouse location for component embedding, or null if no such location.
	 */
	private Point outPt = null;
	
	/**
	 * Text field for manually viewing / editing the filename to be loaded as a component.
	 */
	private JTextField fileField = null;
	
	/**
	 * Text field for manually viewing / editing the URL to be loaded as a component.
	 */
	private JTextField uRLField = new JTextField();
	
	/**
	 * Checkbox controlling whether to use FileWattcher to load a file.
	 */
	private JCheckBox fileWatch = new JCheckBox("Use File Watcher");
	
	/**
	 * Checkbox controlling whether to use FileWattcher to load a URL.
	 */
	private JCheckBox uRLWatch = new JCheckBox("Use File Watcher");
	
	/**
	 * Whether the property editor instance has a tab for creating a new component from scratch,
	 * or whether it is strictly a file / URL loading editor.
	 */
	private boolean usingNewTab = true;
	
	/**
	 * Map of human-readable names to component classes.
	 */
	private TreeMap<String,Class<? extends VerdantiumComponent>> componentMap = new TreeMap<String,Class<? extends VerdantiumComponent>>();
	
	/**
	 * Gets the GUI for the property editor.
	 * @return The GUI for the property editor.
	 */
	public JComponent getGUI() {
		return (myPanel);
	}

	/**
	* Handles property change events.
	* @param in The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == ProgramDirector.propertyHide) {
			VerdantiumUtils.disposeContainer(this);
		}
	}
	
	/**
	 * Updates the list of components.
	 */
	private void updateLst()
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			/**
			 * Updates the list of components.
			 */
			public void run()
			{
				componentMap = ComponentManager.getMap();
				String[] tp = { };
				String[] ob = componentMap.keySet().toArray( tp );
				myList.setListData( ob );
				getGUI().repaint();
			}
		} );
	}

	/**
	* Handles the destruction of the component.
	*/
	public void handleDestroy() {
		(director).removePropertyChangeListener(this);
		if (usingNewTab)
			ProgramDirector.removeClassPropertyChangeListener(this);
	}

	/**
	* Sets the location where the next frame should be embedded.
	* @param in The location where the next frame should be embedded.
	*/
	public void setClickPoint(Point InPt) {
		outPt = InPt;
	}

	/**
	* Creates a property editor that responds to an EtherEventPropertySource.  The EtherEventPropertySource
	* must respond to {@link ProgramDirectorEvent} events.
	* @param in The data model of the component being edited.
	* @param inPt The mouse location for component embedding, or null if no such location.
	*/
	public ProgramDirectorPropertyEditor(
		EtherEventPropertySource in,
		Point inPt) {
		fileField = new VerdantiumFileDndTextField(this, in);
		initialize(in, true, true, inPt);
	}

	/**
	* Creates a property editor that responds to an EtherEventPropertySource.  The EtherEventPropertySource
	* must respond to {@link ProgramDirectorEvent} events.  The parameter <code>UseNewTab</code>
	* determines whether the "New" tab will be used in the interface, or whether the interface
	* will only allow component creation through the loading of files.  The latter is useful in cases
	* where a distinct file reference is required.
	* @param in The data model of the component being edited.
	* @param useNewTab Whether the property editor instance has a tab for creating a new component from scratch.
	* @param inPt The mouse location for component embedding, or null if no such location.
	*/
	public ProgramDirectorPropertyEditor(
		EtherEventPropertySource in,
		boolean useNewTab,
		Point inPt) {
		fileField = new VerdantiumFileDndTextField(this, in);
		initialize(in, useNewTab, true, inPt);
	}

	/**
	* Creates a property editor that responds to an EtherEventPropertySource.  The EtherEventPropertySource
	* must respond to {@link ProgramDirectorEvent} events.  The parameter <code>UseNewTab</code>
	* determines whether the "New" tab will be used in the interface, or whether the interface
	* will only allow component creation through the loading of files.  The latter is useful in cases
	* where a distinct file reference is required.  The parameter <code>UseFileWatchCheck</code>
	* allows one to control whether to insert the FileWatcher check box.
	* @param in The data model of the component being edited.
	* @param useNewTab Whether the property editor instance has a tab for creating a new component from scratch.
	* @param useFileWatchCheck Whether to add checkboxes for using FileWatcher.
	* @param inPt The mouse location for component embedding, or null if no such location.
	*/
	public ProgramDirectorPropertyEditor(
		EtherEventPropertySource in,
		boolean useNewTab,
		boolean useFileWatchCheck,
		Point inPt) {
		fileField = new VerdantiumFileDndTextField(this, in);
		initialize(in, useNewTab, useFileWatchCheck, inPt);
	}

	/**
	 * Initializes the property editor.
	 * @param in The data model of the component being edited.
	 * @param useNewTab Whether the property editor instance has a tab for creating a new component from scratch.
	 * @param useFileWatchCheck Whether to add checkboxes for using FileWatcher.
	 * @param inPt The mouse location for component embedding, or null if no such location.
	 */
	private void initialize(
		EtherEventPropertySource in,
		boolean useNewTab,
		boolean useFileWatchCheck,
		Point inPt) {
		director = in;
		outPt = inPt;
		usingNewTab = useNewTab;
		VerdantiumJListDragUtils.setDragUtil(myList);
		myList.setToolTipText(
			"<HTML>To learn more, select an item <P>and click the help button.</HTML>");
		myPanel.setLayout(new BorderLayout(0, 0));
		myPanel.add("Center", myPane);
		JButton applyButton = new JButton("Apply");
		JButton applyButton2 = new JButton("Apply");
		JButton applyButton3 = new JButton("Apply");
		JButton chooseButton = new JButton("Choose File");
		JButton helpButton = new JButton("Help");
		JScrollPane scp =
			new JScrollPane(
				myList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		if (useNewTab) {
			JPanel newPanel = new JPanel();
			JPanel newSouthPanel = new JPanel();
			newPanel.setLayout(new BorderLayout(0, 0));
			newSouthPanel.setLayout(new FlowLayout());
			newPanel.add("Center", scp);
			newPanel.add("South", newSouthPanel);
			newSouthPanel.add(applyButton);
			newSouthPanel.add(helpButton);
			myPane.addTab("New", newPanel);
		}

		JPanel openFile = new JPanel();
		openFile.setLayout(new BorderLayout(0, 0));
		openFile.add("North", fileField);

		if (useFileWatchCheck)
			openFile.add("Center", fileWatch);

		JPanel fileButtonPanel = new JPanel();
		openFile.add("South", fileButtonPanel);
		fileButtonPanel.setLayout(new FlowLayout());
		fileButtonPanel.add(chooseButton);
		if (VerdantiumApplet.isAppletActivated())
			chooseButton.setEnabled(false);
		fileButtonPanel.add(applyButton2);
		if (VerdantiumApplet.isAppletActivated())
			applyButton2.setEnabled(false);
		myPane.addTab("Open File", openFile);
		JPanel openURL = new JPanel();
		openURL.setLayout(new BorderLayout(0, 0));
		openURL.add("North", uRLField);

		if (useFileWatchCheck)
			openURL.add("Center", uRLWatch);

		uRLField.setEditable(true);
		openURL.add("South", applyButton3);
		if (VerdantiumApplet.isAppletActivated())
			applyButton3.setEnabled(false);
		myPane.addTab("Open URL", openURL);

		ActionListener myL =
			Adapters.createGActionListener(this, "handleAddProgram");
		applyButton.addActionListener(myL);
		applyButton2.addActionListener(myL);
		applyButton3.addActionListener(myL);
		myL = Adapters.createGActionListener(this, "handleChooseFile");
		chooseButton.addActionListener(myL);
		myL = Adapters.createGActionListener(this, "handleDisplayHelp");
		helpButton.addActionListener(myL);

		(in).addPropertyChangeListener(this);
		if (usingNewTab)
			ProgramDirector.addClassPropertyChangeListener(this);
		
		ComponentManager.addListener( new Runnable()
		{
			/**
			 * Updates the list of components.
			 */
			public void run()
			{
				updateLst();
			}
		} );
		
		updateLst();
	}

	/**
	* Handles the pressing of the "Apply" button.
	* @param e The input event.
	*/
	public void handleAddProgram(ActionEvent e) {
		try {
			int tab = myPane.getSelectedIndex();
			if (!usingNewTab)
				tab++;

			if (tab == 0) {
				String sel = myList.getSelectedValue();
				if (sel != null) {
					String MyName = sel;
					Object[] param = { MyName, outPt };
					EtherEvent send =
						new ProgramDirectorEvent(
							this,
							ProgramDirectorEvent.createApp,
							param,
							director);
					fireProgramDirectorEtherEvent(send, null);
				} else
					throw (
						new IllegalInputException("You must select a component."));
			}

			if (tab == 1) {
				EtherEvent send =
					new ProgramDirectorEvent(
						this,
						ProgramDirectorEvent.loadURL,
						null,
						director);
				Object[] param =
					{
						null,
						outPt,
						new Boolean(fileWatch.getModel().isSelected())};
				try {
					File myFile = new File(fileField.getText());
					param[0] = myFile.toURL();
				} catch (Exception ex) {
					VerdantiumUtils.produceMessageWindow(
						ex,
						"Bad File Name",
						"Bad File Name",
						"Bad file name.  Please try again.",
						this,
						director);
					return;
				}
				send.setParameter(param);
				fireProgramDirectorEtherEvent(send, null);
			}

			if (tab == 2) {
				EtherEvent send =
					new ProgramDirectorEvent(
						this,
						ProgramDirectorEvent.loadURL,
						null,
						director);
				String myURL = uRLField.getText();
				Object[] param =
					{
						null,
						outPt,
						new Boolean(fileWatch.getModel().isSelected())};
				try {
					param[0] = new URL(myURL);
				} catch (Exception ex) {
					VerdantiumUtils.produceMessageWindow(
						ex,
						"Bad File Name",
						"Bad File Name",
						"Bad file name.  Please try again.",
						this,
						director);
					return;
				}
				send.setParameter(param);
				fireProgramDirectorEtherEvent(send, null);
			}
		} catch (Throwable in) {
			handleThrow(in);
		}
	}

	/**
	* Handles the displaying of help.
	* @param e The input event.
	*/
	public void handleDisplayHelp(ActionEvent e) {
		String sel = myList.getSelectedValue();
		if (sel != null) {
			String myName = sel;
			ProgramDirector.displayHelpOnComponent(myName, this, director);
		} else {
			String myName = "Program director";
			ProgramDirector.displayHelpOnComponent(myName, this, director);
		}
	}

	/**
	* Sets the selected index of the tabbed pane.
	* @param in The index to set.
	*/
	public void setSelectedIndex(int in) {
		myPane.setSelectedIndex(in);
	}

	/**
	* Fires the program director ether event.  Override this method to have the ether event go someplace else.
	* @param send The event to be sent.
	* @param refcon A reference to context data that the generating code can associate with the rectangle.  See various references to "refcon" in MacOS programming.
	* @return The result of executing the event.
	*/
	protected Object fireProgramDirectorEtherEvent(EtherEvent send, Object refcon)
		throws Throwable {
		return (ProgramDirector.fireEtherEvent(send, refcon));
	}

	/**
	* Gets the parent frame of the component.
	* @return The parent frame of the component.
	*/
	private Window getParentFrame() {
		Component tmp = getGUI();
		while ((tmp != null) && !(tmp instanceof Window))
			tmp = tmp.getParent();

		return ((Window) tmp);
	}

	/**
	* Handles the "Choose File" button of the component.
	* @param e The input event.
	*/
	public void handleChooseFile(ActionEvent e) {
		try {
			String fileName = null;

			JFileChooser fd = new JFileChooser();
			int returnVal = fd.showOpenDialog(getParentFrame());

			if (returnVal != JFileChooser.APPROVE_OPTION)
				return;

			File myFile = fd.getSelectedFile();
			fileName = myFile.getPath();
			fileField.setText(fileName);

			EtherEvent send =
				new ProgramDirectorEvent(
					this,
					ProgramDirectorEvent.loadURL,
					null,
					director);

			Object[] param =
				{ null, outPt, new Boolean(fileWatch.getModel().isSelected())};
			try {
				param[0] = myFile.toURL();
			} catch (Exception ex) {
				VerdantiumUtils.produceMessageWindow(
					ex,
					"Bad File Name",
					"Bad File Name",
					"Bad file name.  Please try again.",
					this,
					director);
				return;
			}
			send.setParameter(param);
			fireProgramDirectorEtherEvent(send, null);
		} catch (Throwable in) {
			handleThrow(in);
		}
	}

	/**
	* Handles the throwing of an error or exception.
	* @param in The error or exception to be handled.
	*/
	public void handleThrow(Throwable in) {
		VerdantiumUtils.handleThrow(in, this, director);
	}

	/**
	 * The data model of the component being edited.
	 */
	private EtherEventPropertySource director = null;
	
}

