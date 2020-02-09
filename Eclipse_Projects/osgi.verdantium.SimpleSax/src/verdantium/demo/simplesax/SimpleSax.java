package verdantium.demo.simplesax;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import meta.DataFormatException;
import meta.VersionBuffer;
import meta.WrapRuntimeException;
import meta.FlexString;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.TransVersionBuffer;
import verdantium.TransVersionBufferFlavor;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumUtils;
import verdantium.demo.simplesax.help.SimpleSaxHelp;
import verdantium.utils.ResourceNotFoundException;
import verdantium.utils.VerdantiumDragUtils;
import verdantium.utils.VerdantiumDropUtils;
import verdantium.utils.VerticalLayout;

//$$strtCprt
/*
 Simple SAX demo program by Thorn Green
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
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/29/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for multiple PLAFs.                                          | Made code changes to support multiple PLAFs.
*    | 10/05/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for command-line arguments.                                  | Added support for command-line arguments.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
*    | 05/10/2002            | Thorn Green (viridian_1138@yahoo.com)           | Redundant information in persistent storage.                         | Made numerous persistence and packaging changes.
*    | 07/22/2002            | Thorn Green (viridian_1138@yahoo.com)           | Demonstrate support for XML.                                         | Created SimpleSax from modifications to SimpleForm.
*    | 07/27/2002            | Thorn Green (viridian_1138@yahoo.com)           | Left a detail out of the docs.                                       | Minor documentation change.
*    | 07/28/2002            | Thorn Green (viridian_1138@yahoo.com)           | SimpleSax layout didn't use stretch feature of VerticalLayout.       | Added stretch support.
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
* Component that loads and saves embedded XML using a SAX parser.
* <P>
* @author Thorn Green
*/
public class SimpleSax extends Object implements VerdantiumComponent {
	
	/**
	* Top-level panel.
	*/
	private JPanel FormPanel = new JPanel();

	/**
	* Field for name.
	*/
	private JTextField nameField = new JTextField("Bob");

	/**
	* Field for rank.
	*/
	private JTextField rankField = new JTextField("Sargent");

	/**
	* Field for serial number.
	*/
	private JTextField serialField = new JTextField("1234567");

	/**
	* Returns the GUI for the component.
    * @return The GUI.
	*/
	public JComponent getGUI() {
		return (FormPanel);
	}

	/**
	* Constructs the component.
	*/
	public SimpleSax() {
		JPanel p2 = FormPanel;
		p2.setLayout(new VerticalLayout(1, true));
		p2.add("any", new JLabel("Name : "));
		p2.add("stretch", nameField);
		p2.add("any", new JLabel("Rank : "));
		p2.add("stretch", rankField);
		p2.add("any", new JLabel("Serial Number : "));
		p2.add("stretch", serialField);
		VerdantiumDragUtils.setDragUtil(nameField, this);
		VerdantiumDropUtils.setDropUtil(nameField, this, null);
		VerdantiumDragUtils.setDragUtil(rankField, this);
		VerdantiumDropUtils.setDropUtil(rankField, this, null);
		VerdantiumDragUtils.setDragUtil(serialField, this);
		VerdantiumDropUtils.setDropUtil(serialField, this, null);
	}

	/**
	* Handles the destruction of the component.  Currently does nothing.
	*/
	public void handleDestroy() {}

	/**
	* Handles Ether Events on the component.  Does nothing in this case.
    * @param in The event to handle.
    * @param refcon A reference to context data for the event.
    * @return The result of handling the event, or null if there is no result.
	*/
	public Object processObjEtherEvent(EtherEvent in, Object refcon) {
		return (null);
	}

	/**
	 * Initializes the model of the component to a default state.
	 */
	public void newModel() {
		nameField.setText("Bob");
		rankField.setText("Sargent");
		serialField.setText("1234567");
	}

	/**
	* Returns the data flavors that the component can load from persistent storage.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF = { new TransVersionBufferFlavor("Simple SAX", "Simple SAX")};
		return (MyF);
	}

	/**
	* Returns the data flavors that the component can save to persistent storage.
    * @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF = { new TransVersionBufferFlavor("Simple SAX", "Simple SAX")};
		return (MyF);
	}

	/**
	* Loads data for the component from persistent storage.
    * @param flavor The flavor in which to load the data.
    * @param trans The transferable from which to load the data.
	*/
	public void loadPersistentData(DataFlavor flavor, Transferable trans)
		throws IOException, ClassNotFoundException, ResourceNotFoundException {

		if (trans == null) {
			newModel();
		}
		else {

			if (flavor instanceof TransVersionBufferFlavor) {
				try {
					TransVersionBuffer MyT = (TransVersionBuffer) trans;
					VersionBuffer.chkNul(MyT);
					String str = (String) (MyT.getPropertyEx("StreamData"));
                                        ByteArrayInputStream bais = new ByteArrayInputStream(str.getBytes());

					SimpleSaxHandler handler = new SimpleSaxHandler();

					SAXParserFactory factory = SAXParserFactory.newInstance();

					SAXParser saxParser = factory.newSAXParser();
					saxParser.parse(bais, handler);

					handler.setFields();
					FormPanel.repaint();
				}
				catch (IOException e) {
					throw (e);
				}
				catch (ParserConfigurationException e) {
					throw (new WrapRuntimeException(e));
				}
				catch (SAXException e) {
					throw (new DataFormatException(e));
				}
				catch (ClassCastException e) {
					throw (new DataFormatException(e));
				}
			}

		}

	}

	/**
	* Saves the component to persistent storage.
    * @param The flavor in which to save the data.
    * @return Transferable to which the data is saved.
	*/
	public Transferable savePersistentData(DataFlavor flavor) {
		TransVersionBuffer MyF = new TransVersionBuffer("Simple SAX", "Simple SAX");

		FlexString fs = new FlexString();
		fs.insertJavaString("<?xml version='1.0' encoding='utf-8'?>\n");
		fs.insertJavaString("");
		fs.insertJavaString("<person>");
		fs.insertJavaString("<name>");
		fs.insertJavaString(nameField.getText());
		fs.insertJavaString("</name>");
		fs.insertJavaString("<rank>");
		fs.insertJavaString(rankField.getText());
		fs.insertJavaString("</rank>");
		fs.insertJavaString("<serialNumber>");
		fs.insertJavaString(serialField.getText());
		fs.insertJavaString("</serialNumber>");
		fs.insertJavaString("</person>");
		fs.insertJavaString("");

		MyF.setProperty("StreamData", fs.exportString() );

		return (MyF);
	}

	/**
	* Handles the throwing of an error or exception.
	* Note: this should only be called if there is a bug.
    * @param in The exception to handle.
	*/
	public void handleThrow(Throwable in) {
		VerdantiumUtils.handleThrow(in, this, null);
	}

	/**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		SimpleSaxHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		SimpleSax MyComp = new SimpleSax();
		ProgramDirector.showComponent(MyComp, "Simple SAX", argv, false);
	}

	/**
	* Inner class for handling Sax callbacks.
    * <P>
    * @author Thorn Green
	*/
	public class SimpleSaxHandler extends DefaultHandler {
		/**
		* Boolean indicating entered person production.
		*/
		boolean enteredPerson = false;
		/**
		* Boolean indicating entered name production.
		*/
		boolean enteredName = false;
		/**
		* Boolean indicating entered rank production.
		*/
		boolean enteredRank = false;
		/**
		* Boolean indicating entered serial production.
		*/
		boolean enteredSerial = false;

		/**
		* Currently read set of characters.
		*/
		String textBuffer = null;

		/**
		* String for parsed name.
		*/
		String name = null;
		/**
		* String for parsed rank.
		*/
		String rank = null;
		/**
		* String for parsed serial number.
		*/
		String serial = null;

		/**
		* Constructs the handler.
		*/
		public SimpleSaxHandler() {}

		/**
		* Sets fields based on parsed XML data.
		*/
		public void setFields() throws DataFormatException {
			VersionBuffer.chkNul(name);
			VersionBuffer.chkNul(rank);
			VersionBuffer.chkNul(serial);

			nameField.setText(name);
			rankField.setText(rank);
			serialField.setText(serial);
		}

		@Override
		public void startElement(String namespaceURI, String sName, // simple name
		String qName, // qualified name
		Attributes attrs) throws SAXException {
			if (qName.equals("person")) {
				if (enteredPerson)
					throw (new SAXException("Data Format"));

				enteredPerson = true;
			}

			if (enteredPerson) {
				if (qName.equals("name")) {
					if (enteredName || enteredRank || enteredSerial || (name != null))
						throw (new SAXException("Data Format"));

					enteredName = true;
				}

				if (qName.equals("rank")) {
					if (enteredName || enteredRank || enteredSerial || (rank != null))
						throw (new SAXException("Data Format"));

					enteredRank = true;
				}

				if (qName.equals("serialNumber")) {
					if (enteredName || enteredRank || enteredSerial || (serial != null))
						throw (new SAXException("Data Format"));

					enteredSerial = true;
				}

			}

			textBuffer = null;
		}

		@Override
		public void endElement(String namespaceURI, String sName, // simple name
		String qName // qualified name
		) throws SAXException {
			if (qName.equals("person")) {
				if (!enteredPerson)
					throw (new SAXException("Data Format"));

				enteredPerson = false;
			}

			if (enteredPerson) {
				if (qName.equals("name")) {
					if (!enteredName || enteredRank || enteredSerial)
						throw (new SAXException("Data Format"));

					name = "" + textBuffer;
					System.out.println(name);
					enteredName = false;
				}

				if (qName.equals("rank")) {
					if (enteredName || !enteredRank || enteredSerial)
						throw (new SAXException("Data Format"));

					rank = "" + textBuffer;
					System.out.println(rank);
					enteredRank = false;
				}

				if (qName.equals("serialNumber")) {
					if (enteredName || enteredRank || !enteredSerial)
						throw (new SAXException("Data Format"));

					serial = "" + textBuffer;
					System.out.println(serial);
					enteredSerial = false;
				}
			}
		}

		@Override
		public void characters(char buf[], int offset, int len) throws SAXException {
			if (textBuffer == null)
				textBuffer = "";

			int count;
			for (count = offset; count < (offset + len); ++count) {
				char ch = buf[count];
				int ich = (int) (ch);
				if ((ich != 10) && (ich != 13))
					textBuffer = textBuffer + ch;
			}
		}

		
	} /* End SimpleSaxHandler */

	
} /* End SimpleSax */

