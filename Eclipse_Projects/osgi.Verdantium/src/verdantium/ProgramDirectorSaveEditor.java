package verdantium;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import verdantium.utils.VerdantiumFileDndTextField;

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
*    | 12/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Third-Cut at Error Handling.                                         | Third-Cut at Error Handling.
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
* Read-only (mostly!) property editor that produces a "save dialog" for a component.  Content can
* either be saved to a file or potentially (someday) to a URL (assuming a compatible URL protocol
* is implemented and there is a compatible web service at the URL location).  One typical
* generator of this property editor is clicking "File --> Save As..." from a pull-down menu.
* 
* This editor may also be displayed upon the user clicking the close button on the window in order
* to give the user one last chance to save the document before the component goes away.  This
* use-case puts some unique constraints on the property editor because the original component window 
* may already be disposed at the point the property editor is displayed, and hence several methods on 
* the original AWT Component class for the container may no longer work / may generate exceptions.
* 
* Events generated by the component are mostly read-only.  Many components do internally
* store the location of the last save operation so that a subsequent "Save" after a "Save As..."
* has a default storage path to which to save.  Other that this internal storing of save
* locations, operations should be read-only.
* 
* @author Thorn Green
*/
public class ProgramDirectorSaveEditor
	extends Object
	implements VerdantiumPropertiesEditor {
	
	/**
	 * Tabbed pane used to choose between saving to a URL and saving to a file.
	 */
	private JTabbedPane myPane = new JTabbedPane();
	
	/**
	 * Top-level GUI panel for the property editor.
	 */
	private JPanel myPanel = new JPanel();
	
	/**
	 * Text field containing the file path to which to perform the save.
	 */
	private JTextField fileField = null;
	
	/**
	 * Text field containing the URL to which to perform the save.
	 */
	private JTextField uRLField = new JTextField();
	
	/**
	 * Combo-box for choosing the data flavor under which to save to a file.
	 */
	private JComboBox<String> fileBox = new JComboBox<String>();
	
	/**
	 * Combo-box for choosing the data flavor under which to save to a URL.
	 */
	private JComboBox<String> uRLBox = new JComboBox<String>();
	
	/**
	 * The EtherEvent to be transmitted to initiate the save operation.
	 */
	private EtherEvent sendEvent = null;

	
	/**
	* Gets the GUI of the property editor.
	* @return The GUI of the property editor.
	*/
	public JComponent getGUI() {
		return (myPanel);
	}

	/**
	 * Handles Ether Events to alter the properties of the property editor.
	 * @param in The event to handle.
	 * @param refcon A reference to context data that the generating code can associate with the rectangle.  See various references to "refcon" in MacOS programming.
	 * @return The result of handling the event, or null if there is no result.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon) {
		return (null);
	}

	/**
	* Handles the destruction of the component.
	*/
	public void handleDestroy() {
	}

	/**
	 * Constructor.
	 * @param inPart The component being edited.
	 * @param inFrame AWT component for the top-level embedding frame that contains the component to be edited.  For future use.
	 */
	public ProgramDirectorSaveEditor(
		VerdantiumComponent inPart,
		Component inFrame) {
		this(inPart, inFrame, false);
	}

	/**
	 * Constructor.
	 * @param inPart The component being edited.
	 * @param inFrame AWT component for the top-level embedding frame that contains the component to be edited.  For future use.
	 * @param scripting Whether the generated save event is supposed to go through the macro recording / scripting system.
	 */
	public ProgramDirectorSaveEditor(
		VerdantiumComponent inPart,
		Component inFrame,
		boolean scripting) {
		this(inPart, inFrame, scripting, null);
		sendEvent =
			new StandardEtherEvent(
				this,
				StandardEtherEvent.objSaveAsEvent,
				null,
				myPart);
	}

	/**
	 * Constructor.
	 * @param inPart The component being edited.
	 * @param inFrame AWT component for the top-level embedding frame that contains the component to be edited.  For future use.
	 * @param scripting Whether the generated save event is supposed to go through the macro recording / scripting system.
	 * @param send The EtherEvent to be transmitted to initiate the save operation.
	 */
	public ProgramDirectorSaveEditor(
		VerdantiumComponent inPart,
		Component inFrame,
		boolean scripting,
		EtherEvent send) {
		myPart = inPart;
		myFrame = inFrame;
		scriptable = scripting;
		sendEvent = send;
		fileField = new VerdantiumFileDndTextField(inPart, null);
		myPanel.setLayout(new BorderLayout(0, 0));
		myPanel.add("Center", myPane);
		/* JButton ApplyButton = new JButton( "Apply" ); */
		JButton applyButton2 = new JButton("Apply");
		JButton applyButton3 = new JButton("Apply");
		JButton chooseButton = new JButton("Choose File");
		/* JPanel ClosePanel = new JPanel();
		ClosePanel.setLayout( new BorderLayout( 0 , 0 ) );
		ClosePanel.add( "South" , ApplyButton );
		myPane.addTab( "Toggle Visibility" , ClosePanel ); */
		JPanel saveFile = new JPanel();
		saveFile.setLayout(new BorderLayout(0, 0));
		saveFile.add("North", fileField);
		JPanel fileButtonPanel = new JPanel();
		saveFile.add("South", fileButtonPanel);
		JPanel sub1 = new JPanel();
		saveFile.add("Center", sub1);
		sub1.setLayout(new BorderLayout(0, 0));
		sub1.add("South", fileBox);
		fileButtonPanel.setLayout(new FlowLayout());
		fileButtonPanel.add(chooseButton);
		fileButtonPanel.add(applyButton2);
		myPane.addTab("Save File", saveFile);
		JPanel saveURL = new JPanel();
		saveURL.setLayout(new BorderLayout(0, 0));
		saveURL.add("North", uRLField);
		uRLField.setEditable(true);
		saveURL.add("South", applyButton3);
		sub1 = new JPanel();
		saveURL.add("Center", sub1);
		sub1.setLayout(new BorderLayout(0, 0));
		sub1.add("South", uRLBox);
		/* myPane.addTab( "Save to URL" , SaveURL );  !!!!!!!! Future Expansion !!!!!!!!!!!!! */

		myFlavors = myPart.getPersistentOutputDataFlavorsSupported();
		int count;
		for (count = 0; count < myFlavors.length; ++count) {
			fileBox.addItem(myFlavors[count].getHumanPresentableName());
			uRLBox.addItem(myFlavors[count].getHumanPresentableName());
		}

		ActionListener myL =
			Adapters.createGActionListener(this, "handleWindowEvent");
		/* applyButton.addActionListener( MyL ); */
		applyButton2.addActionListener(myL);
		applyButton3.addActionListener(myL);
		myL = Adapters.createGActionListener(this, "handleChooseFile");
		chooseButton.addActionListener(myL);
	}

	/**
	* Handles an "Apply" button action event.
	* @param e The input event.
	*/
	public void handleWindowEvent(ActionEvent e) {
		try {
			int tab = myPane.getSelectedIndex();

			/* if( tab == 0 )
				{
				if( visible )
					{
					if( myFrame instanceof Window )
						{
						( (VerdantiumFrame) myFrame ).setUpdating( true );
						( (VerdantiumFrame) myFrame ).hide();
						( (VerdantiumFrame) myFrame ).setUpdating( false );
						}	
			
					if( myFrame instanceof JInternalFrame )
						{
						( (VerdantiumInternalFrame) myFrame ).setUpdating( true );
						( (VerdantiumInternalFrame) myFrame ).setVisible( false );
						( (VerdantiumInternalFrame) myFrame ).setUpdating( false );
						}
					}
					else
					{
					if( myFrame instanceof Window )
						{
						( (VerdantiumFrame) myFrame ).setUpdating( true );
						( (VerdantiumFrame) myFrame ).show();
						( (VerdantiumFrame) myFrame ).setUpdating( false );
						}	
			
					if( myFrame instanceof JInternalFrame )
						{
						( (VerdantiumInternalFrame) myFrame ).setUpdating( true );
						( (VerdantiumInternalFrame) myFrame ).pack();
						( (VerdantiumInternalFrame) myFrame ).setVisible( true );
						( (VerdantiumInternalFrame) myFrame ).repaint();
						( (VerdantiumInternalFrame) myFrame ).setUpdating( false );
						}
					}
			
			
				visible = !visible;
				}  */

			if (tab == 0) {
				URL myU = null;
				try {
					File myFile = new File(fileField.getText());
					myU = myFile.toURL();
				} catch (Exception ex) {
					
					/*
					 * Note, a null PCE is used when calling produceMessageWindow() because the
				     * original component may have already destroyed itself at the time this the save editor, or
				     * this throwable, was created.  This may need to be refined in the future.
					 */
					
					VerdantiumUtils
						.produceMessageWindow(
							ex,
							"Bad File Name",
							"Bad File Name",
							"Bad file name.  Please try again.",
							this,
							null 
					);
					return;
				}
				saveFile(myU, myPart, myFlavors[fileBox.getSelectedIndex()]);
			}

			if (tab == 1) {
				URL myU = null;
				String myURL = uRLField.getText();
				try {
					myU = new URL(myURL);
				} catch (Exception ex) {
					
					/*
					 * Note, a null PCE is used when calling produceMessageWindow() because the
				     * original component may have already destroyed itself at the time this the save editor, or
				     * this throwable, was created.  This may need to be refined in the future.
					 */
					
					VerdantiumUtils
						.produceMessageWindow(
							ex,
							"Bad File Name",
							"Bad File Name",
							"Bad file name.  Please try again.",
							this,
							null 
					);
					return;
				}
				saveFile(myU, myPart, myFlavors[uRLBox.getSelectedIndex()]);
			}
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Gets the parent frame of the save dialog.
	* @return The parent frame of the save dialog.
	*/
	private Window getParentFrame() {
		Component tmp = getGUI();
		while ((tmp != null) && !(tmp instanceof Window))
			tmp = tmp.getParent();

		return ((Window) tmp);
	}

	/**
	* Handles the "Choose File" button in the dialog.
	* @param e The input event.
	*/
	public void handleChooseFile(ActionEvent e) {
		try {
			String fileName = null;

			JFileChooser fd = new JFileChooser();
			int returnVal = fd.showSaveDialog(getParentFrame());

			if (returnVal != JFileChooser.APPROVE_OPTION)
				return;

			File myFile = fd.getSelectedFile();
			fileName = myFile.getPath();
			fileField.setText(fileName);

			URL myU = null;
			try {
				myU = myFile.toURL();
			} catch (Exception ex) {
				
				/*
				 * Note, a null PCE is used when calling produceMessageWindow() because the
			     * original component may have already destroyed itself at the time this the save editor, or
			     * this throwable, was created.  This may need to be refined in the future.
				 */
				
				VerdantiumUtils
					.produceMessageWindow(
						ex,
						"Bad File Name",
						"Bad File Name",
						"Bad file name.  Please try again.",
						this,
						null 
				);
				return;
			}
			saveFile(myU, myPart, myFlavors[fileBox.getSelectedIndex()]);
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Saves the persistent state of <code>myPart</code> in format <code>MyFlavor</code>
	* to URL <code>MyU</code>.
	* @param myU The URL to which to save.
	* @param myPart The component for which to perform the save.
	* @param myFlavor The flavor to save as.
	*/
	protected void saveFile(
		URL myU,
		VerdantiumComponent myPart,
		DataFlavor myFlavor)
		throws Throwable {
		Object[] param = { myU, myFlavor };
		EtherEvent send = sendEvent;
		send.setParameter(param);

		if (scriptable)
			ProgramDirector.fireEtherEvent(send, null);
		else
			VerdantiumUtils.handleImplicitObjEtherEvent(send, null);
	}

	/**
	* Handles the throwing of an error or exception. 
	* @param in The error or exception to be handled.
	*/
	public void handleThrow(Throwable in) {
		
		/*
		 * Note, a null PCE is used when calling handleThrow() because the
	     * original component may have already destroyed itself at the time this the save editor, or
	     * this throwable, was created.  This may need to be refined in the future.
		 */
		
		VerdantiumUtils.handleThrow(in, myPart, null);
	}

	/**
	* Gets the input data flavors supported.  None are supported in this class.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		return (null);
	}
	
	/**
	* Gets the output data flavors supported.  None are supported in this class.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		return (null);
	}
	
	/**
	* Interface to load persistent data.  Does nothing.
	*/
	public void loadPersistentData(DataFlavor flavor, Transferable trans) {
	}
	
	/**
	* Interface to save persistent data.  Does nothing.
	*/
	public Transferable savePersistentData(DataFlavor flavor) {
		return (null);
	}

	
	/**
	 * Whether the generated save event is supposed to go through the 
	 * macro recording / scripting system.
	 */
	private boolean scriptable = false;
	
	
	// private boolean visible = false;
	
	/**
	 * The data flavors to which the component being edited can perform a save.
	 */
	private DataFlavor[] myFlavors = null;
	
	/**
	 * The component being edited.
	 */
	private VerdantiumComponent myPart = null;
	
	/**
	 * AWT component for the top-level embedding frame that contains the component to be edited.  For future use.
	 */
	private Component myFrame = null;
	
}

