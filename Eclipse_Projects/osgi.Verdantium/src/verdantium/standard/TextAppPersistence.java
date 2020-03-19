/*
 * Created on Dec 4, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package verdantium.standard;

import java.io.IOException;
import java.io.NotSerializableException;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import meta.DataFormatException;
import meta.VersionBuffer;
import meta.WrapRuntimeException;
import verdantium.ProgramDirector;
import verdantium.TransVersionBuffer;
import verdantium.VerdantiumComponent;

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
 * @author thorngreen
 *
 * Persistence support class for TextApp.
 */
public class TextAppPersistence {

	/**
	 * Table of persistent VersionBuffers created from document objects that is used to eliminate duplicates.
	 */
	protected HashMap<Object,VersionBuffer> table = null;

	/**
	 * Version buffer for writing data to persistent storage.
	 */
	protected VersionBuffer writeBuffer = null;
	
	/**
	 * Incrementing count of the number of elements written to writeBuffer.
	 */
	protected int writeIndex = 0;

	/**
	 * Version buffer for reading data from persistent storage.
	 */
	protected TransVersionBuffer readBuffer = null;
	
	/**
	 * The GUI panel into which data from persistent storage is to be read.
	 */
	protected TextAppInsertPanel readPanel = null;

	
	/**
	 * Writes a document to a persistent VersionBuffer.
	 * @param doc The input document.
	 * @param out The output VersionBuffer.
	 * @throws IOException
	 * @throws NotSerializableException
	 */
	public void writeDocument(Document doc, VersionBuffer out)
		throws IOException, NotSerializableException {
		table = new HashMap<Object,VersionBuffer>();
		writeBuffer = new VersionBuffer(true);
		out.setProperty("Document", writeBuffer);
		Element elem = doc.getDefaultRootElement();
		createBufferFromElementOrAttribute(elem);
		writeBuffer.setInt("NumElements", writeIndex);
	}

	/**
	 * Creates a persistent VersionBuffer from an element or attribute in the document.
	 * @param obj The input element or attributes from the document.
	 * @return The created persistent VersionBuffer.
	 * @throws IOException
	 * @throws NotSerializableException
	 */
	protected VersionBuffer createBufferFromElementOrAttribute(Object obj)
		throws IOException, NotSerializableException {
		VersionBuffer vbuf = table.get(obj);

		if (vbuf == null) {
			vbuf = new VersionBuffer(true);
			table.put(obj, vbuf);

			if (obj instanceof Element) {
				writeBufferFromElement((Element) obj);
			}

			if (obj instanceof AttributeSet) {
				writeBufferFromAttributes((AttributeSet) obj, vbuf);
			}

		}

		return (vbuf);
	}

	/**
	 * Reads a TextApp document from a persistent VersionBuffer.
	 * @param _in The input VersionBuffer.
	 * @param out The output editor pane to receive the document.
	 * @throws DataFormatException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void readDocument(VersionBuffer _in, JEditorPane out)
		throws DataFormatException, IOException, ClassNotFoundException {
		Document doc = out.getDocument();
		VersionBuffer in = (VersionBuffer) (_in.getPropertyEx("Document"));
		int numEle = in.getInt("NumElements");
		int count;
		for (count = 0; count < numEle; count++) {
			VersionBuffer vbuf =
				(VersionBuffer) (in.getPropertyEx("Element_" + count));
			handleElementFromBuffer(vbuf, doc);
		}
		out.setDocument(doc);
		out.repaint();
	}

	/**
	 * Converts a persistent VersionBuffer to an Element, and then insers the element into a document.
	 * @param buf The input VersionBuffer.
	 * @param doc The output document into which to insert the element.
	 * @throws DataFormatException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	protected void handleElementFromBuffer(VersionBuffer buf, Document doc)
		throws DataFormatException, IOException, ClassNotFoundException {
		try {
			readBuffer = null;
			readPanel = null;
			SimpleAttributeSet simp =
				createAttributesFromBuffer(
					(VersionBuffer) (buf.getPropertyEx("Attributes")));
			int off0 = buf.getInt("StartOffset");
			int off1 = buf.getInt("EndOffset");
			int len = doc.getLength();
			if (off1 <= len) {
				String chars = doc.getText(off0, off1 - off0);
				doc.remove(off0, off1 - off0);
				doc.insertString(off0, chars, simp);
			}
			if (readBuffer != null) {
				readPanel.readData(readBuffer);
			}
		} catch (BadLocationException ex) {
			throw (new WrapRuntimeException(ex));
		}
	}

	/**
	 * Converts an element into a persistent VersionBuffer, and then stores the VersionBuffer in the writeBuffer member.
	 * @param elem The element to be converted.
	 * @throws IOException
	 * @throws NotSerializableException
	 */
	protected void writeBufferFromElement(Element elem)
		throws IOException, NotSerializableException {

		String ename = elem.getName();
		if (ename.equals("content") || ename.equals("component")) {
			VersionBuffer vbuf = new VersionBuffer(true);
			vbuf.setInt("StartOffset", elem.getStartOffset());
			vbuf.setInt("EndOffset", elem.getEndOffset());
			AttributeSet attrs = elem.getAttributes();
			VersionBuffer atts = createBufferFromElementOrAttribute(attrs);
			vbuf.setProperty("Attributes", atts);

			writeBuffer.setProperty("Element_" + writeIndex, vbuf);
			writeIndex++;
		}

		int elcnt = elem.getElementCount();
		int count;
		for (count = 0; count < elcnt; count++) {
			Element ele = elem.getElement(count);
			createBufferFromElementOrAttribute(ele);
		}
	}

	/**
	 * Generates an attribute set from a VersionBuffer.
	 * @param buff The input VersionBuffer from which to read the attributes.
	 * @return The generated attribute set.
	 * @throws DataFormatException
	 */
	protected SimpleAttributeSet createAttributesFromBuffer(VersionBuffer buff)
		throws DataFormatException {
		SimpleAttributeSet simp = new SimpleAttributeSet();
		try {
			int atcnt = buff.getInt("AttrCnt");
			int count;
			for (count = 0; count < atcnt; count++) {
				Object keyClass = buff.getPropertyEx("KeyClass_" + count);
				String keyString =
					(String) (buff.getPropertyEx("KeyString_" + count));
				String ks1 = keyString.substring(1, keyString.length());
				String ks0 = ("" + keyString.charAt(0)).toUpperCase();
				keyString = ks0 + ks1;
				if (keyString.equals("Component")) {
					readPanel = new TextAppInsertPanel();
					readBuffer =
						(TransVersionBuffer) (buff
							.getPropertyEx("Comp_" + count));
					StyleConstants.setComponent(simp, readPanel);
				} else {
					Class clss = Class.forName((String) keyClass);
					Field field = clss.getDeclaredField((String) keyString);
					Object key = field.get(null);
					Object ob = buff.getProperty("Obj_" + count);
					if (ob == null) {
						VersionBuffer vb =
							(VersionBuffer) (buff
								.getPropertyEx("Att_" + count));
						AttributeSet sc = createAttributesFromBuffer(vb);
						ob = sc;
					}
					simp.addAttribute(key, ob);
				}
			}
		} catch (ClassNotFoundException ex) {
			throw (new DataFormatException(ex));
		} catch (IllegalAccessException ex) {
			throw (new DataFormatException(ex));
		} catch (NoSuchFieldException ex) {
			throw (new DataFormatException(ex));
		}

		return (simp);
	}

	/**
	 * Writes the attributes from an attribute set into a VersionBuffer.
	 * @param attrs The input set of attributes.
	 * @param buff The output VersionBuffer to which to write the attributes.
	 * @throws IOException
	 * @throws NotSerializableException
	 */
	protected void writeBufferFromAttributes(
		AttributeSet attrs,
		VersionBuffer buff)
		throws IOException, NotSerializableException {
		Enumeration<?> enuma = attrs.getAttributeNames();
		int count = 0;
		while (enuma.hasMoreElements()) {
			Object key = enuma.nextElement();
			Object ob = attrs.getAttribute(key);
			String str = key.toString();
			if (!(str.equals("resolver")) && !(str.equals("$ename"))) {
				buff.setProperty("KeyClass_" + count, key.getClass().getName());
				buff.setProperty("KeyString_" + count, str);

				if ((ob instanceof AttributeSet) || (ob instanceof Element)) {
					VersionBuffer vb = createBufferFromElementOrAttribute(ob);
					buff.setProperty("Att_" + count, vb);
				} else if (ob instanceof TextAppInsertPanel) {
					VerdantiumComponent comp =
						((TextAppInsertPanel) (ob)).getComponent();
					Object st = ProgramDirector.getSerializableState(comp);
					buff.setProperty("Comp_" + count, st);
				} else {
					buff.setProperty("Obj_" + count, ob);
				}

				count++;
			}
		}

		buff.setInt("AttrCnt", count);

		if (attrs instanceof StyleContext.NamedStyle) {
			StyleContext.NamedStyle sc = (StyleContext.NamedStyle) (attrs);
			if (sc.getName() != null)
				buff.setProperty("Name", sc.getName());
		}

	}

	
}

