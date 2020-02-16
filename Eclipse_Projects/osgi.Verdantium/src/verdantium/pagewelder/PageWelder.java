package verdantium.pagewelder;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import jundo.runtime.ExtMilieuRef;
import jundo.runtime.ExtPair;
import jundo.runtime.IExtObjectRef;
import jundo.runtime.IExtPair;
import jundo.util.pdx_HashMapSh_pdx_ObjectRef;
import jundo.util.pdx_JkeyRef_pdx_ObjectRef;
import jundo.util.pdx_JobjRef_pdx_ObjectRef;
import meta.DataFormatException;
import meta.VersionBuffer;
import meta.WrapRuntimeException;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.ProgramDirectorEvent;
import verdantium.StandardEtherEvent;
import verdantium.TransVersionBuffer;
import verdantium.TransVersionBufferFlavor;
import verdantium.UrlHolder;
import verdantium.VerdantiumComponent;
import verdantium.VerdantiumPropertiesEditor;
import verdantium.core.BackgroundListener;
import verdantium.core.BackgroundPropertyEditor;
import verdantium.core.ContainerApp;
import verdantium.core.ContainerAppDesktopPane;
import verdantium.core.ContainerAppInternalFrame;
import verdantium.core.DefaultPropertyEditor;
import verdantium.core.DesignerControl;
import verdantium.core.EditorControl;
import verdantium.core.PrintPreviewPropertyEditor;
import verdantium.core.PropertyEditEtherEvent;
import verdantium.pagewelder.help.PageWelderHelp;
import verdantium.undo.UTag;
import verdantium.undo.UndoManager;
import verdantium.utils.ComponentNotFoundException;
import verdantium.utils.IllegalInputException;
import verdantium.utils.ResourceNotFoundException;
import verdantium.utils.VerdantiumDragUtils;
import verdantium.utils.VerdantiumDropUtils;
import verdantium.xapp.ApplicationAdapter;
import verdantium.xapp.BackgroundState;

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
*    | 08/12/2004            | Thorn Green (viridian_1138@yahoo.com)           | First cut at template editing for PageWelder.                        | Added initial template functionality.
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
* PageWelder shows how to build a simple container application.  Copy this source code,
* rather than verdantium.core.ContainerApp, when building your own container.
* 
* @author Thorn Green
*/
public class PageWelder
	extends ApplicationAdapter
	implements BackgroundListener, MouseListener {
	
	/**
	* Returns the panel that encloses the GUI.
	*/
	transient protected JPanel MyPan = new JPanel();

	/**
	 * The inner panel of the GUI that holds the CardLayout.
	 */
	transient protected JPanel innerPanel = new JPanel();

	/**
	 * The layout manager used to switch between cards.
	 */
	protected CardLayout cardL = new CardLayout();
	
	/**
	 * The current number of card indices.
	 */
	protected int numIndices = 0;
	
	/**
	 * Map storing the desktop pane for each index in the displayed CardLayout.
	 */
	protected HashMap<Integer,PageWelderDesktopPane> cards = new HashMap<Integer,PageWelderDesktopPane>();

	/**
	 * Map from global card indices to indices in the displayed CardLayout.
	 */
	protected pdx_HashMapSh_pdx_ObjectRef cardIndices = null;

	/**
	 * The data model for multi-level undo.
	 */
	protected pdx_PageWelderModel_pdx_ObjectRef model = null;

	/**
	 * Gets the index of the current card.
	 * @return The index of the current card.
	 */
	public int getCurrentIndex() {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		return (model.pdxm_getCurrentIndex(mil));
	}

	/**
	 * Sets the index of the current card, and updates the multi-level undo state.
	 * @param in The index of the current card.
	 */
	protected void setCurrentIndex(int in) {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		mil = model.pdxm_setCurrentIndex(mil, in);
		undoMgr.handleCommitTempChange(mil);
	}

	/**
	 * Gets the number of cards.
	 * @return The number of cards.
	 */
	public int getNumCards() {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		return (model.pdxm_getNumCards(mil));
	}

	/**
	 * Sets the number of cards, and updates the multi-level undo state.
	 * @param in The number of cards.
	 */
	protected void setNumCards(int in) {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		mil = model.pdxm_setNumCards(mil, in);
		undoMgr.handleCommitTempChange(mil);
	}

	/**
	 * Gets the index of the previously-displayed card.
	 * @return The index of the previously-displayed card.
	 */
	protected int getPrevIndex() {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		return (model.pdxm_getPrevIndex(mil));
	}

	/**
	 * Sets the index of the previously-displayed card and updates the multi-level undo state.
	 * @param in The index of the previosuly-displayed card.
	 */
	protected void setPrevIndex(int in) {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		mil = model.pdxm_setPrevIndex(mil, in);
		undoMgr.handleCommitTempChange(mil);
	}

	/**
	 * Gets an index in the displayed CardLayout given a global card index.
	 * @param indx The global card index.
	 * @return The index in the displayed CardLayout.
	 */
	protected Integer getCardIndex(int indx) {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		IExtPair pair = createKey(mil, indx);
		mil = pair.getMilieu();
		IExtObjectRef ref = cardIndices.pdxm_get(mil, pair.getObject());
		pdx_JobjRef_pdx_ObjectRef jref = (pdx_JobjRef_pdx_ObjectRef) (ref);
		Object obj = jref.pdxm_getVal(mil);
		return ((Integer) (obj));
	}

	/**
	 * Sets an index for the displayed CardLayout given a global card index.
	 * @param indx The global card index.
	 * @param val The index in the displayed CardLayout.
	 */
	protected void putCardIndex(int indx, Integer val) {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		IExtPair pkey = createKey(mil, indx);
		mil = pkey.getMilieu();
		IExtPair pval = createValue(mil, val);
		mil = pval.getMilieu();
		IExtPair pair =
			cardIndices.pdxm_put(mil, pkey.getObject(), pval.getObject());
		undoMgr.handleCommitTempChange(pair.getMilieu());
	}

	/**
	 * Creates a key for a global card index in the multi-level undo state.
	 * @param _mil The mulieu from which to create the key.
	 * @param in The number to put in the key.
	 * @return The generated key.
	 */
	protected IExtPair createKey(ExtMilieuRef _mil, int in) {
		ExtMilieuRef mil = _mil;
		IExtPair p1 =
			pdx_JkeyRef_pdx_ObjectRef.pdxm_allocate_JkeyRef(
				mil,
				new Integer(in));
		return (p1);
	}

	/**
	 * Creates a value for the displayed CardLayout in the multi-level undo state.
	 * @param _mil The mulieu from which to create the value.
	 * @param in The number to put in the value.
	 * @return The generated value.
	 */
	protected IExtPair createValue(ExtMilieuRef _mil, Integer in) {
		ExtMilieuRef mil = _mil;
		IExtPair p1 = pdx_JobjRef_pdx_ObjectRef.pdxm_allocate_JobjRef(mil);
		IExtObjectRef oref = p1.getObject();
		mil = p1.getMilieu();
		pdx_JobjRef_pdx_ObjectRef jref = (pdx_JobjRef_pdx_ObjectRef) (oref);
		mil = jref.pdxm_setVal(mil, in);
		ExtPair pair = new ExtPair(jref, mil);
		return (pair);
	}

	/**
	 * Clears all card indices, and updates the multi-level undo state.
	 */
	protected void clearCardIndices() {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		mil = cardIndices.pdxm_clear(mil);
		undoMgr.handleCommitTempChange(mil);
	}

	/**
	 * Removes a card index, and updates the multi-level undo state.
	 * @param indx The index to be removed.
	 */
	protected void removeCardIndex(int indx) {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		IExtPair pair = createKey(mil, indx);
		IExtObjectRef key = pair.getObject();
		mil = pair.getMilieu();
		pair = cardIndices.pdxm_remove(mil, key);
		mil = pair.getMilieu();
		undoMgr.handleCommitTempChange(mil);
	}

	/**
	 * Returns the GUI of the component.
	 * @return The GUI of the component.
	 */
	public JComponent getGUI() {
		return (MyPan);
	}

	/**
	 * Gets the desktop pane at a particular card index.
	 * @param indx The desired index.
	 * @return The desktop pane at the desired index.
	 */
	public PageWelderDesktopPane getObjectFromCardIndex(int indx) {
		Integer intg = getCardIndex(indx);
		PageWelderDesktopPane desk = cards.get(intg);
		return (desk);
	}

	/**
	* Handles property change events.
	* @param evt The input event.
	*/
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == EditorControl.EditCntlChange) {
			for( final ContainerAppDesktopPane card : cards.values() ) {
				
				JInternalFrame[] AllFrames = card.getAllFrames();

				for (int count = 0; count < AllFrames.length; count++) {
					AllFrames[count].updateUI();
					AllFrames[count].repaint();
				}
			}
		}

		if (evt.getPropertyName() == UndoManager.MODEL_STATE_CHANGED) {
			handleUndoStateChange();
		}

	}

	/**
	 * Handles a change to the global multi-level undo state.
	 */
	protected void handleUndoStateChange() {
		switchToCurrentIndex();
	}

	/**
	* Handles the destruction of the component.
	*/
	public void handleDestroy() {
		super.handleDestroy();

		for( PageWelderDesktopPane card : cards.values() ) {
			card.handleDestroy();
		}
	}

	/**
	* Constructs the PageWelder component.
	*/
	public PageWelder() {
		super();
		undoMgr =
			UndoManager.createInstanceUndoManager(
				jundo.runtime.Runtime.getInitialMilieu());
		initialize(undoMgr);
		configureForEtherEvents();
		undoMgr.addPropertyChangeListener(this);
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		IExtPair pair =
			pdx_HashMapSh_pdx_ObjectRef.pdxm_allocate_HashMapSh(mil);
		mil = pair.getMilieu();
		cardIndices = (pdx_HashMapSh_pdx_ObjectRef) (pair.getObject());
		pair = pdx_PageWelderModel_pdx_ObjectRef.pdxm_new_PageWelderModel(mil);
		mil = pair.getMilieu();
		model = (pdx_PageWelderModel_pdx_ObjectRef) (pair.getObject());
		undoMgr.handleCommitTempChange(mil);
		arrangeLayout();
		try {
			addCardInit();
		} catch (Throwable ex) {
			throw (new WrapRuntimeException("Add Card Failed", ex));
		}
		setDefaultBkgnd();
	}

	/**
	* Sets up the layout managers for the component.
	*/
	protected void arrangeLayout() {
		MyPan.setLayout(new BorderLayout(0, 0));
		MyPan.add("Center", innerPanel);
		MyPan.setMinimumSize(new Dimension(2, 2));
		MyPan.setPreferredSize(new Dimension(50, 50));
		MyPan.setOpaque(false);
		innerPanel.setOpaque(false);
		innerPanel.setLayout(cardL);
	}

	/**
	 * Sets background of the card back to the default background.
	 */
	protected void setDefaultBkgnd() {
		PageWelderDesktopPane card = getCurrentCard();
		DefaultBkgnd = card.getBackground();
		getBkgnd().setBackgroundState(DefaultBkgnd, true);
		card.setToolTipText("Right-Click to edit properties");
		getBkgnd().configureForEtherEvents(getCurrentCard(), PropL);
	}

	/**
	 * Gets the background of the current card.
	 * @return The background of the current card.
	 */
	protected BackgroundState getBkgnd() {
		BackgroundState ret = null;
		if (getNumCards() > 0) {
			PageWelderDesktopPane dpane = getCurrentCard();
			ret = dpane.getBkgnd();
		}
		return (ret);
	}

	/**
	 * Gets the current card.
	 * @return The desktop pane of the current card.
	 */
	protected PageWelderDesktopPane getCurrentCard() {
		Integer curCardIndex = getCardIndex(getCurrentIndex());

		PageWelderDesktopPane currentCardObj =
			cards.get(curCardIndex);
		return (currentCardObj);
	}

	/**
	 * Gets the card at a particular index.
	 * @param index The index at which to get the card.
	 * @return The desktop pane of the card at the index.
	 */
	public PageWelderDesktopPane getCardForIndex(int index) {
		Integer cardIndex = getCardIndex(index);

		PageWelderDesktopPane currentCardObj =
			cards.get(cardIndex);
		return (currentCardObj);
	}

	protected void addCardInit() throws Throwable {
		addCard();
	}

	/**
	 * Loads a template for the background of a new card.
	 * @param undoMgr The multi-level undo manager.
	 * @param dpane The desktop pane into which to load the template.
	 * @throws ClassNotFoundException
	 * @throws ResourceNotFoundException
	 * @throws ComponentNotFoundException
	 * @throws IOException
	 */
	public void loadTemplate(UndoManager undoMgr, ContainerAppDesktopPane dpane)
		throws
			ClassNotFoundException,
			ResourceNotFoundException,
			ComponentNotFoundException,
			IOException {
		if (getBkgndTemplate() != null) {

			VersionBuffer buff = getBkgndTemplate();

			Transferable MyT = (Transferable) (buff.getProperty("Frames"));
			VersionBuffer.chkNul(MyT);

			boolean op = buff.getBoolean("Opaque");
			Color bk = (Color) (buff.getProperty("Background"));
			VersionBuffer.chkNul(bk);
			if (isScrolling())
				op = true;
			dpane.setOpaque(op);
			dpane.setBackground(bk);

			ContainerApp.loadInternalDesktopFrames(
				MyT,
				ContainerAppInternalFrame.class,
				undoMgr,
				dpane);

		} else
			dpane.setBackground(Color.white);
	}

	/**
	 * Adds a card to the PageWelder.
	 * @return The desktop pane for the card that was added.
	 * @throws ClassNotFoundException
	 * @throws ResourceNotFoundException
	 * @throws ComponentNotFoundException
	 * @throws IOException
	 */
	protected PageWelderDesktopPane addCard()
		throws
			ClassNotFoundException,
			ResourceNotFoundException,
			ComponentNotFoundException,
			IOException {
		int numCards = getNumCards();
		PageWelderDesktopPane dpane = new PageWelderDesktopPane(undoMgr, this);
		dpane.addMouseListener(this);
		dpane.setOpaqueFlag(true);
		dpane.getBkgnd().configureForEtherEvents(dpane, PropL);
		VerdantiumDragUtils.setDragUtil(dpane, this);
		VerdantiumDropUtils.setDropUtil(dpane, this, this);
		Integer numi = new Integer(numIndices);
		cards.put(numi, dpane);
		String newIndex = "" + numIndices;
		putCardIndex(numCards, numi);
		setCurrentIndex(numCards);
		numIndices++;
		numCards++;
		setNumCards(numCards);
		innerPanel.add(dpane, newIndex);
		cardL.show(innerPanel, newIndex);
		setPrevIndex(-1);

		loadTemplate(undoMgr,dpane);

		PropL.firePropertyChange(
			BackgroundPropertyEditor.AppBackground,
			null,
			null);

		return (dpane);
	}

	/**
	 * Deletes the current card.
	 * @throws Throwable
	 */
	protected void deleteCard() throws Throwable {
		int numCards = getNumCards();
		int currentIndex = getCurrentIndex();
		if (numCards < 2) {
			throw (
				new IllegalInputException("Can't Delete The Last Card In The Stack"));
		}

		int cnt;
		for (cnt = currentIndex; cnt < (numCards - 1); cnt++) {
			putCardIndex(cnt, getCardIndex(cnt + 1));
		}

		numCards--;
		setNumCards(numCards);
		int newIndex = currentIndex;
		if (newIndex >= numCards)
			newIndex = 0;
		currentIndex = newIndex;
		setCurrentIndex(currentIndex);
		Integer newCardIndex = getCardIndex(currentIndex);
		cardL.show(innerPanel, "" + newCardIndex);
		PropL.firePropertyChange(
			BackgroundPropertyEditor.AppBackground,
			null,
			null);
		setPrevIndex(-1);
		removeCardIndex(numCards);
	}

	/**
	 * Switches to the card at the current index.
	 */
	protected void switchToCurrentIndex() {
		Integer curCardIndex = getCardIndex(getCurrentIndex());
		cardL.show(innerPanel, "" + curCardIndex);
		getBkgnd().configureForEtherEvents(getCurrentCard(), PropL);
		PropL.firePropertyChange(
			BackgroundPropertyEditor.AppBackground,
			null,
			null);
	}

	/**
	 * Switches back to the previously-displayed card.
	 */
	protected void backCard() {
		if (getPrevIndex() >= 0) {
			setCurrentIndex(getPrevIndex());
			setPrevIndex(-1);
			switchToCurrentIndex();
		}
	}

	/**
	 * Switches to the first card.
	 */
	protected void firstCard() {
		setPrevIndex(getCurrentIndex());
		setCurrentIndex(0);
		switchToCurrentIndex();
	}

	/**
	 * Switches to the previous card.
	 * @throws Throwable
	 */
	protected void prevCard() throws Throwable {
		int currentIndex = getCurrentIndex();
		if (currentIndex < 1) {
			throw (
				new IllegalInputException("You Are Already At The First Card."));
		}

		setPrevIndex(currentIndex);
		currentIndex--;
		setCurrentIndex(currentIndex);
		if (currentIndex < 0)
			setCurrentIndex(0);
		switchToCurrentIndex();
	}

	/**
	 * Switches to the next card.
	 * @throws Throwable
	 */
	protected void nextCard() throws Throwable {
		int currentIndex = getCurrentIndex();
		int numCards = getNumCards();
		if (currentIndex >= (numCards - 1)) {
			throw (
				new IllegalInputException("You Are Already At The Last Card."));
		}

		setPrevIndex(currentIndex);
		currentIndex++;
		setCurrentIndex(currentIndex);
		if (currentIndex >= numCards)
			setCurrentIndex(numCards - 1);
		switchToCurrentIndex();
	}

	/**
	 * Switches to the last card.
	 */
	protected void lastCard() {
		setPrevIndex(getCurrentIndex());
		setCurrentIndex(getNumCards() - 1);
		switchToCurrentIndex();
	}

	/**
	 * Switches to a card at a particular index.
	 * @param index The index of the desired card.
	 */
	public void switchToIndex(int index) {
		setPrevIndex(getCurrentIndex());
		setCurrentIndex(index);
		switchToCurrentIndex();
	}

	/**
	 * Handles a request to edit the default background for new cards.
	 */
	protected void handleBkgnd() {
		PageWelderBkgndEditor rec = new PageWelderBkgndEditor(this);
		ProgramDirector.showPropertyEditor(
			rec,
			getGUI(),
			"Background Property Editor");
		try {
			if (getBkgndTemplate() != null)
				rec.loadBkgnd();
		} catch (Exception ex) {
			handleThrow(ex);
		}
	}

	/**
	 * Gets the template for the default background for a new card.
	 * @return The template for the default background for a new card.
	 */
	public VersionBuffer getBkgndTemplate() {
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		return ((VersionBuffer) (model.pdxm_getBkgndTemplate(mil)));
	}

	/**
	 * Sets the template for the default background for a new card.
	 * @param buff Version buffer that is loaded to create the default background for anew card.
	 */
	public void setBkgndTemplate(VersionBuffer buff) {
		UTag utag = new UTag();
		undoMgr.prepareForTempCommit(utag);
		ExtMilieuRef mil = undoMgr.getCurrentMil();
		mil = model.pdxm_setBkgndTemplate(mil, buff);
		undoMgr.handleCommitTempChange(mil);
		undoMgr.commitUndoableOp(utag, "Set Background Template");
	}

	/**
	 * Handles Ether Events to alter the properties of the component.
	 * @param in The event to handle.
	 * @param refcon A reference to context data for the event.
	 * @return The result of handling the event, or null if there is no result.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon)
		throws Throwable {
		boolean handled = false;

		if (in instanceof PageWelderEtherEvent) {
			if (in.getEtherID().equals(PageWelderEtherEvent.addCard)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				addCard();
				undoMgr.commitUndoableOp(utag, "Add Card");
			}

			if (in.getEtherID().equals(PageWelderEtherEvent.deleteCard)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				deleteCard();
				undoMgr.commitUndoableOp(utag, "Delete Card");
			}

			if (in.getEtherID().equals(PageWelderEtherEvent.backCard)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				backCard();
				undoMgr.commitUndoableOp(utag, "Go Back");
			}

			if (in.getEtherID().equals(PageWelderEtherEvent.firstCard)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				firstCard();
				undoMgr.commitUndoableOp(utag, "First Card");
			}

			if (in.getEtherID().equals(PageWelderEtherEvent.prevCard)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				prevCard();
				undoMgr.commitUndoableOp(utag, "Previous Card");
			}

			if (in.getEtherID().equals(PageWelderEtherEvent.nextCard)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				nextCard();
				undoMgr.commitUndoableOp(utag, "Next Card");
			}

			if (in.getEtherID().equals(PageWelderEtherEvent.lastCard)) {
				UTag utag = new UTag();
				undoMgr.prepareForTempCommit(utag);
				lastCard();
				undoMgr.commitUndoableOp(utag, "Last Card");
			}

			if (in.getEtherID().equals(PageWelderEtherEvent.editBkgnd)) {
				handleBkgnd();
			}

			if (in
				.getEtherID()
				.equals(PageWelderEtherEvent.printPreviewStack)) {
				handleStackPrintPreview();
			}

			if (in.getEtherID().equals(PageWelderEtherEvent.printStack)) {
				handleStackPrinting();
			}

		}

		if (in instanceof PropertyEditEtherEvent) {
			if (in
				.getEtherID()
				.equals(
					PropertyEditEtherEvent.isFindReplaceIteratorSupported)) {
				return (new Boolean(true));
			}

			if (in
				.getEtherID()
				.equals(PropertyEditEtherEvent.createFindReplaceIterator)) {
				Object[] param = (Object[]) (in.getParameter());
				return (new PageWelderFindIterator(param, this, this));
			}

		}

		if (in instanceof ProgramDirectorEvent) {
			if (in
				.getEtherID()
				.equals(ProgramDirectorEvent.isProgramDirectorEventSupported)) {
				return (new Boolean(true));
			} else {
				return (handleProgramDirectorEvent((ProgramDirectorEvent) in));
			}

		}

		if (!handled) {
			Object ret = super.processObjEtherEvent(in, refcon);
			if (ret == EtherEvent.EVENT_NOT_HANDLED)
				ret = null;
			return (ret);
		} else {
			return (null);
		}
	}

	/**
	 * Handles a change to the state defining whether only the designer edits.
	 */
	public void handleOnlyDesignerEditsChange() {
		
		for ( PageWelderDesktopPane card : cards.values() ) {

			JInternalFrame[] AllFrames = card.getAllFrames();

			for ( int count = 0; count < AllFrames.length; count++) {
				AllFrames[count].updateUI();
				AllFrames[count].repaint();
			}

			if (isOnlyDesignerEdits())
				card.setToolTipText(null);
			else
				card.setToolTipText("Right-Click to edit properties");
		}

	}

	/**
	 * Creates the properties editor for the component.
	 * @return The created property editor.
	 */
	public VerdantiumPropertiesEditor makePropertiesEditor() {
		Properties MyP = new Properties();
		if (isScrolling())
			MyP.put("Scrolling", this);
		PageWelderPropertyEditor MyEdit =
			new PageWelderPropertyEditor(this, null);
		MyEdit.setClickPoint(new Point(10, 10));
		return (MyEdit);
	}

	/**
	* Displays a property editor for the PageWelder component.
	*/
	public void showPropertiesEditor(EtherEvent e) {
		VerdantiumPropertiesEditor MyEdit = makePropertiesEditor();
		((DefaultPropertyEditor) MyEdit).setClickPoint(
			(Point) (e.getParameter()));
		ProgramDirector.showPropertyEditor(
			MyEdit,
			getGUI(),
			"Page Welder Property Editor");
	}

	/**
	* Handles mouse-clicked events.
	* @param e The input event.
	*/
	public void mouseClicked(MouseEvent e) {
	}
	
	/**
	* Handles mouse-entered events.
	* @param e The input event.
	*/
	public void mouseEntered(MouseEvent e) {
	}
	
	/**
	* Handles mouse-exit events.
	* @param e The input event.
	*/
	public void mouseExited(MouseEvent e) {	
	}
	
	/**
	* Handles mouse-released events.
	* @param e The input event.
	*/
	public void mousePressed(MouseEvent e) {
	}
	
	/**
	* Handles mouse-pressed events.
	* @param e The input event.
	*/
	public void mouseReleased(MouseEvent e) {
		try {
			if ((DesignerControl.isDesignTime()) || (!isOnlyDesignerEdits())) {
				EtherEvent send =
					new StandardEtherEvent(
						this,
						StandardEtherEvent.showPropertiesEditor,
						null,
						this);
				send.setParameter(e.getPoint());
				ProgramDirector.fireEtherEvent(send, null);
			}
		} catch (Throwable ex) {
			handleThrow(ex);
		}
	}

	/**
	* Handles a program Director event to embed a component frame.
	* @param e The input event.
	*/
	protected Object handleProgramDirectorEvent(ProgramDirectorEvent e)
		throws Throwable {
		return (ContainerApp.addComponentToPane(e, undoMgr, getCurrentCard()));
	}

	/**
	* Returns the data flavors that the component can load from persistent storage.
    * @return The supported flavors.
	*/
	public static DataFlavor[] getPersistentInputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{ new TransVersionBufferFlavor("Page Welder", "Page Welder")};
		return (MyF);
	}

	/**
	* Returns the data flavors that the component can save to persistent storage.
    * @return The supported flavors.
	*/
	public DataFlavor[] getPersistentOutputDataFlavorsSupported() {
		DataFlavor[] MyF =
			{ new TransVersionBufferFlavor("Page Welder", "Page Welder")};
		return (MyF);
	}

	/**
	* Loads data for the component from persistent storage.
    * @param flavor The flavor in which to load the data.
    * @param trans The transferable from which to load the data.
	*/
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
			borderObject.setBorderObject(null, null, null);
			onlyDesignerEdits.setOnlyDesignerEdits(false);

			int count;
			int maxCards = getNumCards();
			for (count = 0; count < maxCards; count++) {
				ContainerAppDesktopPane MyDesk = getCardForIndex(count);
				ContainerApp.closeAllFrames(MyDesk);
			}

			clearCardIndices();
			setNumCards(0);
			setPrevIndex(-1);
			setBkgndTemplate(null);

			macroMap.clear();
			docPageFormat.setDocPageFormat(null);
			addCard();
			setCurrentIndex(getNumCards() - 1);
		} else {
			try {
				loadPersistentData(null, null);
				TransVersionBuffer MyF = (TransVersionBuffer) trans;
				VersionBuffer.chkNul(MyF);

				int numc = MyF.getInt("NumCards");

				int count;
				for (count = 0; count < numc; count++) {
					Transferable MyT =
						(Transferable) (MyF.getProperty("Frames_" + count));
					VersionBuffer.chkNul(MyT);

					if (count != 0)
						addCard();
					ContainerAppDesktopPane MyDesk = getCardForIndex(count);

					ContainerApp.loadInternalDesktopFrames(
						MyT,
						ContainerAppInternalFrame.class,
						undoMgr,
						MyDesk);

					boolean op = MyF.getBoolean("Opaque_" + count);
					Color bk = (Color) (MyF.getProperty("Background_" + count));
					VersionBuffer.chkNul(bk);
					if (isScrolling())
						op = true;
					MyDesk.setBackground(bk);
					MyDesk.setOpaque(op);
				}

				borderObject.readData(MyF);

				Dimension dim = (Dimension) (MyF.getProperty("PageSize"));
				VersionBuffer.chkNul(dim);
				alterPageSize(dim);

				onlyDesignerEdits.setOnlyDesignerEdits(
					MyF.getBoolean("OnlyDesignerEdits"));
				macroMap.readData(MyF);

				setBkgndTemplate(
					(VersionBuffer) (MyF.getProperty("BkgndTemplate")));

				firstCard();
			} catch (ClassNotFoundException ex) {
				throw (ex);
			} catch (IOException ex) {
				throw (ex);
			} catch (ResourceNotFoundException ex) {
				throw (ex);
			} catch (ComponentNotFoundException ex) {
				throw (ex);
			} catch (ClassCastException ex) {
				throw (new DataFormatException(ex));
			} catch (IllegalInputException ex) {
				throw (new DataFormatException(ex));
			}
		}
	}

	/**
	* Saves the component to persistent storage.
    * @param The flavor in which to save the data.
    * @return Transferable to which the data is saved.
	*/
	public Transferable savePersistentData(DataFlavor flavor)
		throws IOException {
		TransVersionBuffer MyF =
			new TransVersionBuffer("Page Welder", "Page Welder");

		int numCards = getNumCards();
		try {
			int count;
			for (count = 0; count < numCards; count++) {
				ContainerAppDesktopPane MyDesk = getCardForIndex(count);
				Object buff = ContainerApp.saveInternalDesktopFrames(MyDesk);
				MyF.setProperty("Frames_" + count, buff);
				MyF.setBoolean("Opaque_" + count, MyDesk.isOpaque());
				MyF.setProperty("Background_" + count, MyDesk.getBackground());
			}
			MyF.setInt("NumCards", numCards);
			MyF.setBoolean("OnlyDesignerEdits", isOnlyDesignerEdits());
			macroMap.writeData(MyF);
			MyF.setProperty(
				"PageSize",
				new Dimension(
					MyPan.getBounds().width,
					MyPan.getBounds().height));

			borderObject.writeData(MyF);

			if (getBkgndTemplate() != null)
				MyF.setProperty("BkgndTemplate", getBkgndTemplate());
		} catch (IOException ex) {
			throw (ex);
		}

		return (MyF);
	}

	/**
	* Handles multi-page printing for the component.
	*/
	public void handleStackPrinting() throws PrinterException {
		PrinterJob job = PrinterJob.getPrinterJob();
		if (docPageFormat.getDocPageFormat() == null) {
			docPageFormat.setDocPageFormat(job.defaultPage());
		}
		docPageFormat.setDocPageFormat(
			job.validatePage(docPageFormat.getDocPageFormat()));
		PageFormat pf1 = docPageFormat.getDocPageFormat();

		Book bk = new Book();

		double height = pf1.getImageableHeight();

		PageWelderStackPrintable prnt = new PageWelderStackPrintable(this);
		int numPages = prnt.getNumPages(height);

		bk.append(prnt, pf1, numPages);
		job.setPageable(bk);
		if (job.printDialog()) {
			try {
				job.print();
			} catch (PrinterException ex) {
				throw (ex);
			}
		}
	}

	/**
	* Handles Print Preview generation for the component.
	*/
	public void handleStackPrintPreview() {
		PrinterJob job = PrinterJob.getPrinterJob();
		if (docPageFormat.getDocPageFormat() == null) {
			docPageFormat.setDocPageFormat(job.defaultPage());
		}
		docPageFormat.setDocPageFormat(
			job.validatePage(docPageFormat.getDocPageFormat()));
		PageFormat pf1 = docPageFormat.getDocPageFormat();

		Book bk = new Book();

		double height = pf1.getImageableHeight();

		PageWelderStackPrintable prnt = new PageWelderStackPrintable(this);
		int numPages = prnt.getNumPages(height);

		bk.append(prnt, pf1, numPages);
		PrintPreviewPropertyEditor myp =
			new PrintPreviewPropertyEditor(bk, this);
		ProgramDirector.showPropertyEditor(
			myp,
			getGUI(),
			"Print Preview Property Editor");
	}

	/**
	* Optional method to display help in a component.
    * @param in The component in which to display the help.
	*/
	public static void displayVerdantiumHelp(VerdantiumComponent in) {
		PageWelderHelp.run(in);
	}

	/**
	* Allows the component to be run as an application.
    * @param Input parameters for running application.
	*/
	public static void main(String argv[]) {
		ProgramDirector.initUI();
		PageWelder MyComp = new PageWelder();
		ProgramDirector.showComponent(MyComp, "Page Welder", argv, true);
	}

}
