package verdantium.pagewelder;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

import meta.DataFormatException;
import meta.HighLevelList;
import meta.VersionBuffer;
import meta.WrapRuntimeException;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.StandardEtherEvent;
import verdantium.TransVersionBuffer;
import verdantium.TransVersionBufferFlavor;
import verdantium.UrlHolder;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumFlavorMap;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.core.MacroParameter;
import verdantium.core.MacroRecorderNode;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.pagewelder.help.PageWelderHyperButtonHelp;
import verdantium.standard.HyperButton;
import verdantium.undo.UTag;
import verdantium.utils.ComponentNotFoundException;
import verdantium.utils.IllegalInputException;
import verdantium.utils.ResourceNotFoundException;

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
*    | 01/28/2001            | Thorn Green (viridian_1138@yahoo.com)           | Multiple bugs in calling of handleDestroy()                          | Implemented a set of bug-fixes.
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Macro support.                                                       | Added macro support.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/29/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for multiple PLAFs.                                          | Made code changes to support multiple PLAFs.
*    | 10/05/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for command-line arguments.                                  | Added support for command-line arguments.
*    | 12/01/2001            | Thorn Green (viridian_1138@yahoo.com)           | Third-Cut at Error Handling.                                         | Third-Cut at Error Handling.
*    | 03/01/2002            | Thorn Green (viridian_1138@yahoo.com)           | EtherEvent performance enhancements.                                 | EtherEvent performance enhancements.
*    | 04/21/2002            | Thorn Green (viridian_1138@yahoo.com)           | Find/Replace support.                                                | Added find/replace support.
*    | 05/10/2002            | Thorn Green (viridian_1138@yahoo.com)           | Redundant information in persistent storage.                         | Made numerous persistence and packaging changes.
*    | 01/12/2003            | Thorn Green (viridian_1138@yahoo.com)           | Application development too complex.                                 | Simplified application development.
*    | 03/09/2003            | Thorn Green (viridian_1138@yahoo.com)           | PageWelder.                                                          | Implemented PageWelder using code from other classes.
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
* HyperButton class for PageWelder.
* 
* @author Thorn Green
*/
public class PageWelderHyperButton extends HyperButton {
	
	/**
	* EtherEvent name for getting the enclosing PageWelder.
	*/
	public static final String pageWelderHypButtonGetEnc =
		"pageWelderHyperButtonGetEnc";

	/**
	* The skip count for enclosing Page Welders.
	*/
	protected int skipCount = 0;

	/**
	 * Constructor.
	 */
	public PageWelderHyperButton() {
		super();
		recordEventOntoEncloser(PageWelderEtherEvent.nextCard, null);
	}

	/**
	* Creates a properties editor for the component.
	* @return The propety editor for the component.
	*/
	public VerdantiumPropertiesEditor makePropertiesEditor() {
		Properties MyP = new Properties();
		MyP.put("ProgramDirectorEditorName", "Link");
		PageWelderHyperButtonPropertyEditor MyEdit =
			new PageWelderHyperButtonPropertyEditor(this, MyP);
		return (MyEdit);
	}

	/**
	* Displays the properties editor for the component.
	*/
	public void showPropertiesEditor() {
		VerdantiumPropertiesEditor MyEdit = makePropertiesEditor();
		ProgramDirector.showPropertyEditor(
			MyEdit,
			getGUI(),
			"Page Welder Hyper Button Property Editor");
	}

	/**
	 * Handles Ether Events to alter the properties of the component.
	 * @param in The event to handle.
	 * @param refcon A reference to context data for the event.
	 * @return The result of handling the event, or null if there is no result.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon)
		throws Throwable {
		if (in instanceof PropertyEditEtherEvent) {
			if (in.getEtherID().equals(pageWelderHypButtonGetEnc)) {
				Object ob = getEnclosingPageWelder(skipCount);
				return (ob);
			}
		}

		if (in instanceof PageWelderEtherEvent) {
			if (in.getEtherID().equals(PageWelderEtherEvent.pwButtonSetLink)) {
				Object[] param = (Object[]) (in.getParameter());
				String desc = (String) (param[0]);
				Object[] args = (Object[]) (param[1]);
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				recordEventOntoEncloser(desc, args);
				undoMgr.commitUndoableOp(utag, "Set Link");
				return (null);
			}
		}

		return (super.processObjEtherEvent(in, refcon));
	}

	/**
	 * Finds the PsgeWelder component in which the button is embedded.
	 * @param skipCount The number of nested PageWelder levels to slip before identifying the PageWelder.
	 * @return The enclosing PageWelder, or null if no PageWelder is found.
	 * @throws Throwable
	 */
	protected Object getEnclosingPageWelder(int skipCount) throws Throwable {
		Component comp = getGUI();
		boolean Done = false;
		PageWelder page = null;
		Object ret = null;

		while (!Done) {
			if (comp == null)
				Done = true;
			else {
				if (comp instanceof PageWelderDesktopPane) {
					if (skipCount == 0) {
						PageWelderDesktopPane pane =
							(PageWelderDesktopPane) comp;
						page = pane.getPageWelder();
						Done = true;
					} else
						skipCount--;
				}
			}

			if (!Done)
				comp = comp.getParent();
		}

		if (page != null) {
			ret = page;
		} else {
			Throwable ex =
				new IllegalInputException("Embedding Page Welder Not Found");
			throw (ex);
		}

		return (ret);
	}

	/**
	 * Records a macro event onto the enclosing PageWelder.
	 * @param evtDesc Name describing the event to be recorded.
	 * @param args  The parameters of the event to be recorded.
	 * @return The result of recording the macro.
	 */
	protected Object recordEventOntoEncloser(String evtDesc, Object args) {
		setMacroList(new HighLevelList());
		setResultTable(new Vector());
		setParamVal(2);
		EtherEvent eCopy = null;
		getResultTable().setSize(getParamVal());
		MacroParameter param0 = new MacroParameter(0);
		MacroParameter param1 = new MacroParameter(1);

		EtherEvent start =
			new StandardEtherEvent(
				this,
				StandardEtherEvent.getEventSource,
				null,
				"Page Welder Hyper Button");

		EtherEvent start2 =
			new PropertyEditEtherEvent(
				this,
				pageWelderHypButtonGetEnc,
				null,
				param0);

		Object startReply = param0;
		Object replyCopy = null;

		Object startReply2 = param1;

		eCopy = new PageWelderEtherEvent(this, evtDesc, args, param1);

		MacroRecorderNode MyA = new MacroRecorderNode();
		MyA.setOrigEvent(start);
		MyA.setCopyEvent(start);
		MyA.setOrigReply(startReply);
		MyA.setCopyReply(startReply);
		getMacroList().insertRight(MyA);
		MacroRecorderNode MyB = new MacroRecorderNode();
		MyB.setOrigEvent(start2);
		MyB.setCopyEvent(start2);
		MyB.setOrigReply(startReply2);
		MyB.setCopyReply(startReply2);
		getMacroList().insertRight(MyB);
		MacroRecorderNode MyC = new MacroRecorderNode();
		MyC.setOrigEvent(eCopy);
		MyC.setCopyEvent(eCopy);
		MyC.setOrigReply(replyCopy);
		MyC.setCopyReply(replyCopy);
		getMacroList().insertRight(MyC);
		return (null);
	}

	/**
	* Returns the data flavors that the component can load from persistent storage.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{
				new TransVersionBufferFlavor(
					"Page Welder Hyper Button",
					"Page Welder Hyper Button"),
				new TransVersionBufferFlavor(
					"Macro Recorder",
					"Macro Recorder"),
				VerdantiumFlavorMap.createInputStreamFlavor("image", "gif"),
				VerdantiumFlavorMap.createInputStreamFlavor("image", "jpeg")};
		return (MyF);
	}

	@Override
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{
				 new TransVersionBufferFlavor(
					"Page Welder Hyper Button",
					"Page Welder Hyper Button")};
		return (MyF);
	}

	@Override
	protected void handleClear() {
		setImage(null);
		setImageBytes(null);
		setImageMode(NO_MODE);
		setMacroList(new HighLevelList());
		setResultTable(new Vector());
		setParamVal(0);
		setText("Button");
		bkgnd.setBackgroundState(DefaultBkgnd, true);
		Class[] types = {
		};
		Object[] params = {
		};
		try {
			borderObject.setBorderObject(
				"javax.swing.border.EtchedBorder",
				types,
				params);
		} catch (Exception ex) {
			throw (new WrapRuntimeException("Default Border Failed", ex));
		}
		onlyDesignerEdits.setOnlyDesignerEdits(false);
		macroMap.clear();
	}

	@Override
	public void loadPersistentData(DataFlavor flavor, Transferable trans)
		throws
			IOException,
			ClassNotFoundException,
			ResourceNotFoundException,
			ComponentNotFoundException {
		if (trans instanceof UrlHolder) {
			fileSaveURL = ((UrlHolder) trans).getUrl();
			fileSaveFlavor = flavor;
		}

		if (trans == null) {
			handleClear();
		} else {
			if (flavor instanceof TransVersionBufferFlavor) {
				try {
					TransVersionBuffer MyF = (TransVersionBuffer) trans;
					VersionBuffer.chkNul(MyF);
					TransVersionBufferFlavor T_flavor =
						(TransVersionBufferFlavor) flavor;
					VersionBuffer.chkNul(T_flavor);

					if (T_flavor
						.getComponentName()
						.equals("Page Welder Hyper Button")) {
						setMacroList(new HighLevelList());
						setResultTable(new Vector());
						setParamVal(0);
						setParamVal(MyF.getInt("ParamVal"));
						setMacroList(
							(HighLevelList) (MyF.getProperty("MacroList")));
						VersionBuffer.chkNul(getMacroList());
						macroMap.readData(MyF);

						String tmpstr = (String) (MyF.getProperty("Label"));
						VersionBuffer.chkNul(tmpstr);
						setText(tmpstr);

						Object myo = MyF.getProperty("ImageBytes");
						if (myo != null) {
							setImageBytes((byte[]) myo);
							VersionBuffer.chkNul(getImageBytes());
							setImage(
								(Toolkit.getDefaultToolkit()).createImage(
									getImageBytes()));
						}
						setImageMode(MyF.getInt("ImageMode"));

						bkgnd.readData(MyF);

						borderObject.readData(MyF);

						onlyDesignerEdits.setOnlyDesignerEdits(
							MyF.getBoolean("OnlyDesignerEdits"));
						getResultTable().setSize(getParamVal());
					}

					if (T_flavor.getComponentName().equals("Macro Recorder")) {
						setMacroList(new HighLevelList());
						setResultTable(new Vector());
						setParamVal(0);
						setParamVal(MyF.getInt("ParamVal"));
						setMacroList(
							(HighLevelList) (MyF.getProperty("MacroList")));
						VersionBuffer.chkNul(getMacroList());
						getResultTable().setSize(getParamVal());
					}
				} catch (ClassCastException ex) {
					throw (new DataFormatException(ex));
				}
			}

			if (flavor
				.getMimeType()
				.equals("image/gif; class=java.io.InputStream")) {
				try {
					loadImagePersistentData(flavor, trans);
				} catch (IOException ex) {
					throw (ex);
				}
				if (getImage() != null)
					setImageMode(GIF_MODE);
			}

			if (flavor
				.getMimeType()
				.equals("image/jpeg; class=java.io.InputStream")) {
				try {
					loadImagePersistentData(flavor, trans);
				} catch (IOException ex) {
					throw (ex);
				}
				if (getImage() != null)
					setImageMode(JPEG_MODE);
			}

		}
	}

	@Override
	protected void loadImagePersistentData(
		DataFlavor flavor,
		Transferable trans)
		throws IOException {
		byte buffer[] = new byte[1024];

		try {
			InputStream InStream =
				((InputStream) trans.getTransferData(flavor));

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			while (true) {
				int nRead = InStream.read(buffer, 0, buffer.length);
				if (nRead <= 0)
					break;
				baos.write(buffer, 0, nRead);
			}

			setImage(
				(Toolkit.getDefaultToolkit()).createImage(baos.toByteArray()));
			if (getImage() != null) {
				setImageBytes(baos.toByteArray());
			}

		} catch (IOException e) {
			throw (e);
		} catch (UnsupportedFlavorException e) {
			throw (
				new WrapRuntimeException(
					"Something Inconsistent in Flavors",
					e));
		}

	}

	@Override
	public Transferable savePersistentData(DataFlavor flavor) {
		TransVersionBuffer MyF =
			new TransVersionBuffer(
				"Page Welder Hyper Button",
				"Page Welder Hyper Button");

		MyF.setInt("ParamVal", getParamVal());
		MyF.setProperty("MacroList", getMacroList());

		MyF.setProperty("Label", getText());

		if (getImageBytes() != null)
			MyF.setProperty("ImageBytes", getImageBytes());
		MyF.setInt("ImageMode", getImageMode());

		bkgnd.writeData(MyF);
		MyF.setBoolean("OnlyDesignerEdits", isOnlyDesignerEdits());
		macroMap.writeData(MyF);

		borderObject.writeData(MyF);

		return (MyF);
	}

	/**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		PageWelderHyperButtonHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		PageWelderHyperButton MyComp = new PageWelderHyperButton();
		ProgramDirector.showComponent(
			MyComp,
			"Page Welder Hyper Button",
			argv,
			true);
	}

	
}

