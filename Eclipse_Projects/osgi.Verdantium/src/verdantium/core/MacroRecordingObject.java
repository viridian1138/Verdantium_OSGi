package verdantium.core;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Externalizable;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Vector;

import meta.HighLevelList;
import meta.Meta;
import meta.WrapRuntimeException;
import verdantium.EtherEvent;
import verdantium.ProgramDirector;
import verdantium.PropertyChangeSource;
import verdantium.VerdantiumComponent;
import verdantium.xapp.MacroTreeMap;

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
*    | 10/29/2000            | Thorn Green (viridian_1138@yahoo.com)           | Classes did not have names that followed standard Java conventions.  | Performed a global modification to bring the names within spec.
*    | 06/24/2001            | Thorn Green (viridian_1138@yahoo.com)           | Needed to separate core macro functionality from MacroRecorder.      | Initial creation, taking code from MacroRecorder.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/10/2001            | Thorn Green (viridian_1138@yahoo.com)           | Second-Cut at Error Handling.                                        | Second-Cut at Error Handling.
*    | 11/17/2001            | Thorn Green (viridian_1138@yahoo.com)           | Documentation fixes.                                                 | Documentation fixes.
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
* This is a simple component that records and then plays
* macros.  This could be useful if one wanted to control a component
* by scripting.
* <P>
* @author Thorn Green
*/
public class MacroRecordingObject
	extends Object
	implements PropertyChangeListener, PropertyChangeSource {
	
	/**
	* The current recording mode of the component.  Can be either
	* {@link #RecordMode} or {@link #StopMode}.
	*/
	protected int currentMode = StopMode;
	
	/**
	* The new EtherEvent to be processed for recording.
	*/
	private EtherEvent newInEvent = null;
	
	/**
	* The persistent macro stored by the component.
	*/
	protected MacroObject myMacro = new MacroObject();
	
	/**
	* Contains parameters that can be matched during recording indexed by
	* parameter value.
	*/
	private Vector<Object> paramTable = new Vector<Object>();
	
	/**
	* Contains parameters to be matched during playback indexed by
	* parameter value.
	*/
	private Vector<Object> resultTable = new Vector<Object>();

	/**
	* Constant value that {@link #currentMode} takes on when recording.
	*/
	public static final int RecordMode = 1;
	
	/**
	* Constant value that {@link #currentMode} takes on when recording has stopped.
	*/
	public static final int StopMode = 2;

	/**
	* The number of macros playing at any given time.
	*/
	private static int playNum = 0;

	/**
	* Constructs the macro recorder.
	*/
	public MacroRecordingObject() {
		propL = new PropertyChangeSupport(this);
		ProgramDirector.addClassPropertyChangeListener(this);
	}

	/**
	* Plays a previously recorded client macro stored in a map.
	* @param map The map from which to retrieve the macro.
	* @param macroName The name of the macro in the map.
	* @param transmitter The transmitting source for the event.
	*/
	public static void playClientMacro(
		MacroTreeMap map,
		String macroName,
		Object transmitter)
		throws Throwable {
		MacroObject macroObj = map.getMacro(macroName);
		playClientMacro(macroObj, transmitter);
	}

	/**
	* Plays a previously recorded client macro.
	* @param macroObj The previously recorded macro.
	* @param transmitter The transmitting source for the event.
	*/
	public static void playClientMacro(
		MacroObject macroObj,
		Object transmitter)
		throws Throwable {
		Object[] trans = { transmitter };

		playClientMacro(macroObj, trans);
	}

	/**
	* Plays a previously recorded client macro.
	* @param macroObj The previously recorded macro.
	* @param transmitter The transmitting source for the event.
	*/
	public static void playClientMacro(
		MacroObject macroObj,
		Object[] transmitter)
		throws Throwable {
		HighLevelList macroList = macroObj.getMacroList();

		Vector<Object> resultTbl = new Vector<Object>();
		resultTbl.setSize(macroObj.getParamVal());

		int count;
		for (count = 0; count < transmitter.length; count++)
			resultTbl.setElementAt(transmitter[count], count);

		playMacro(macroList, transmitter, resultTbl);
	}

	/**
	* Plays a previously recorded macro.
	* @param macroList The list of macro instructions.
	* @param transmitter The transmitting source for the event.
	* @param resultTbl The table of macro parameter results.
	*/
	public static void playMacro(
		HighLevelList macroList,
		Object transmitter,
		Vector<Object> resultTbl)
		throws Throwable {
		boolean AddedCount = false;

		try {
			synchronized (MacroRecordingObject.class) {
				playNum++;
				AddedCount = true;
			}

			if (!(macroList.empty())) {
				macroList.searchHead();
				boolean Done = false;

				while (!Done) {
					MacroRecorderNode MyN =
						(MacroRecorderNode) (macroList.getNode());
					EtherEvent in = MyN.getCopyEvent();
					Object target = in.getTarget();
					if (target instanceof MacroParameter)
						target =
							getMapParameter((MacroParameter) target, resultTbl);
					EtherEvent send = copyEtherEvent(in, target, transmitter);
					gmapParameter(send, resultTbl);

					Object ob = ProgramDirector.fireEtherEvent(send, null);
					smapParameter(MyN.getCopyReply(), ob, resultTbl);

					macroList.right();
					Done = macroList.getHead();
				}
			}
		} catch (Throwable e) {
			throw (e);
		} finally {
			if (AddedCount) {
				synchronized (MacroRecordingObject.class) {
					playNum--;
					AddedCount = false;
				}
			}
		}
	}

	/**
	* Handles the pressing of the "Record" button by turning macro
	* recording on.
	* @param target The target component of the macro recording.
	*/
	public void handleClientRecord(Object target) {
		Object[] tar = { target };
		handleRecord();
		addReplyElement(tar);
	}

	/**
	* Handles the pressing of the "Record" button by turning macro
	* recording on.
	* @param target The target component of the macro recording.
	*/
	public void handleClientRecord(Object[] target) {
		handleRecord();
		int count;
		for (count = 0; count < target.length; count++)
			addReplyElement(target[count]);
	}

	/**
	* Handles the pressing of the "Record" button by turning macro
	* recording on.
	*/
	public void handleRecord() {
		handleStop();
		currentMode = RecordMode;
	}

	/**
	* Handles the pressing of the "Stop" button by stopping all
	* macro recording.
	*/
	public void handleStop() {
		currentMode = StopMode;
	}

	/**
	* Handles the pressing of the "Clear" button by
	* clearing the macro recorder to its initial state.
	*/
	public void handleClear() {
		handleStop();
		paramTable = new Vector<Object>();
		resultTable = new Vector<Object>();
		myMacro = new MacroObject();
	}

	/**
	* Adds a property change listener to the cell.
	* @param e The listener to add.
	*/
	public void addPropertyChangeListener(PropertyChangeListener e) {
		propL.addPropertyChangeListener(e);
	}

	/**
	* Removes a property change listener from the cell.
	* @param e The listener to remove.
	*/
	public void removePropertyChangeListener(PropertyChangeListener e) {
		propL.removePropertyChangeListener(e);
	}

	/**
	* Maps all parameter instances of type {@link MacroParameter} in a EtherEvent to their corresponding 
	* entries in resultTbl.  This is used for the playback of previously recorded events before
	* firing them to the ProgramManager.
	* @param in The EtherEvent in which to map the parameter instances.
	* @param resultTbl The result table from which to remap the parameter instances.
	*/
	protected static void gmapParameter(EtherEvent in, Vector<Object> resultTbl ) {
		Object param = in.getParameter();

		if (param instanceof MacroParameter)
			in.setParameter(getMapParameter((MacroParameter) param, resultTbl));

		if (param instanceof Object[]) {
			Object[] myo = (Object[]) param;
			int len = myo.length;
			int count;

			for (count = 0; count < len; ++count) {
				if (myo[count] instanceof MacroParameter)
					myo[count] =
						getMapParameter(
							(MacroParameter) (myo[count]),
							resultTbl);
			}
		}
	}

	/**
	* Takes the reply to an EtherEvent, compares it to its previously recorded copy, and maps
	* all objects in the reply with a corresponding {@link MacroParameter} in the copy to the parameter
	* table so that they can be matched to parameters in subsequent scripted EtherEvents.  This
	* is used in the playback of a script after a particular event has been fired.
	* @param replyCopy The previously recorded copy of the reply.
	* @param reply The reply to an EtherEvent.
	* @param resultTbl The parameter table holding the mapped parameters.
	*/
	protected static void smapParameter(
		Object replyCopy,
		Object reply,
		Vector<Object> resultTbl) {
		Object param = replyCopy;

		if (param instanceof MacroParameter)
			setMapParameter((MacroParameter) param, reply, resultTbl);

		if (param instanceof Object[]) {
			Object[] myo = (Object[]) param;
			Object[] myi = (Object[]) (reply);
			int len = myo.length;
			int count;

			for (count = 0; count < len; ++count) {
				if (myo[count] instanceof MacroParameter)
					setMapParameter(
						(MacroParameter) (myo[count]),
						myi[count],
						resultTbl);
			}
		}
	}

	/**
	* Gets the object that corresponds to a parameter during the playback of a macro.
	* @param The MacroParameter for which to retrieve the corresponding object.
	* @param resultTbl The result table from which to get the object corresponding to the macro parameter.
	* @return The corresponding object.
	*/
	protected static Object getMapParameter(
		MacroParameter in,
		Vector<Object> resultTbl) {
		int val = in.getParam();
		Object myo = resultTbl.elementAt(val);
		return (myo);
	}

	/**
	* Sets the object that corresponds to a particular parameter during playback so that 
	* the parameter can be reused by other scripted Ether Event objects.
	* @param in The macro parameter.
	* @param param The object corresponding to the macro parameter.
	* @param resultTbl The result table in which to store the object.
	*/
	protected static void setMapParameter(
		MacroParameter in,
		Object param,
		Vector<Object> resultTbl) {
		int val = in.getParam();
		resultTbl.setElementAt(param, val);
	}

	/**
	* Appends a new Ether Event to the macro list.
	* @param original The original event.
	* @param copy Copy of the original event that is either parameterized or serializable.
	* @param refcon A reference to context data that the generating code can associate with the event.  See various references to "refcon" in MacOS programming.
	*/
	protected void appendNewEtherEvent(
		EtherEvent original,
		EtherEvent copy,
		Object refcon) {
		MacroRecorderNode newNode = new MacroRecorderNode();
		newNode.setOrigEvent(original);
		newNode.setCopyEvent(copy);
		if (!(myMacro.getMacroList().empty())) {
			myMacro.getMacroList().searchHead();
			myMacro.getMacroList().left();
			myMacro.getMacroList().insertRight(newNode);
			myMacro.getMacroList().setCopyMode(Meta.COPY_ALL);
			myMacro.getMacroList().setEraseMode(Meta.ERASE_ALL);
		} else {
			myMacro.getMacroList().insertRight(newNode);
			myMacro.getMacroList().setCopyMode(Meta.COPY_ALL);
			myMacro.getMacroList().setEraseMode(Meta.ERASE_ALL);
		}

	}

	/**
	* Subclass this method to handle the adding of an EtherEvent to a list.
	* @param in The input event.
    * @param reply The reply generated by executing the event.
	* @param refcon A reference to context data that the generating code can associate with the event.  See various references to "refcon" in MacOS programming.
	*/
	protected void handleEtherEventFinish(
		EtherEvent in,
		Object reply,
		Object refcon) {
	}

	/**
	* Subclass this method to handle the abnormal termination of an EtherEvent with an error.
	* @param in The input event.
    * @param reply The reply generated by executing the event.
    * @param refcon A reference to context data that the generating code can associate with the event.  See various references to "refcon" in MacOS programming.
    * @param ex Error or exception generated by the attempt to execute the event.
	*/
	protected void handleEtherEventErr(
		EtherEvent in,
		Object reply,
		Object refcon,
		Throwable ex) {
	}

	/**
	* Handles an EtherEvent reply object by creating a new {@link MacroParameter} with value
	* in the paramVal of {@link #myMacro}, adds the parameter to the parameter list, 
	* increments paramVal of {@link #myMacro}, and returns the new MacroParameter.
	* @param in The EtherEvent reply to handle.
	* @return The corresponding MacroParameter.
	*/
	protected MacroParameter addReplyElement(Object in) {
		paramTable.setSize(myMacro.getParamVal() + 1);
		resultTable.setSize(myMacro.getParamVal() + 1);
		MacroParameter MyP = new MacroParameter(myMacro.getParamVal());
		paramTable.setElementAt(in, myMacro.getParamVal());
		myMacro.setParamVal(myMacro.getParamVal() + 1);
		return (MyP);
	}

	/**
	* Maps an EtherEvent reply to a set of {@link MacroParameter} instances.  Used when recording
	* to generate a set of parameters that can be mapped to subsequent events.
	* @param in The EtherEvent reply to check.
	* @return The corresponding MacroParameter map.
	*/
	protected Object checkReplyCopy(Object in) {
		Object parameter = in;
		if (parameter instanceof Object[]) {
			Object[] parm = (Object[]) (in);
			Object[] tmp = new Object[parm.length];
			int count;
			for (count = 0; count < parm.length; ++count) {
				if (parm[count] != null)
					tmp[count] = addReplyElement(parm[count]);
			}

			parameter = tmp;
		} else {
			if (parameter != null) {
				parameter = addReplyElement(parameter);
			}
		}

		return (parameter);
	}

	/**
	* Uses the Java reflection API to copy an EtherEvent.
	* @param in The event to copy.
	* @param target The target of the event.
	* @param transmitter The transmitting source for the event.
	*/
	public static EtherEvent copyEtherEvent(
		EtherEvent in,
		Object target,
		Object transmitter) {
		Object parameter = in.getParameter();

		if (parameter instanceof Object[]) {
			Object[] parm = (Object[]) (in.getParameter());
			Object[] tmp = new Object[parm.length];
			int count;
			for (count = 0; count < parm.length; ++count)
				tmp[count] = parm[count];

			parameter = tmp;
		}

		EtherEvent copy = null;
		if (target != null) {
			Class<?>[] classes =
				{ Object.class, String.class, Object.class, Object.class };
			Object[] param =
				{ transmitter, in.getEtherID(), parameter, target };
			try {
				Constructor<? extends EtherEvent> MyC = in.getClass().getConstructor(classes);
				EtherEvent res = MyC.newInstance(param);
				copy = res;
			} catch (Exception e) {
				throw (new WrapRuntimeException("Macro Copy Failed.", e));
			}
		}

		return (copy);
	}

	/**
	* Handles the closing of an Ether Event after the event has been executed.  If possible
	* and appropriate, the event will be added to the event list of the currently recorded macro.
	* @param in The input event.
    * @param reply The reply generated by executing the event.
    * @param refcon A reference to context data that the generating code can associate with the event.  See various references to "refcon" in MacOS programming.
	*/
	protected void closeNewEtherEvent(
		EtherEvent in,
		Object reply,
		Object refcon) {
		Object replyCopy = checkReplyCopy(reply);
		myMacro.getMacroList().searchHead();
		myMacro.getMacroList().left();
		MacroRecorderNode MyN =
			(MacroRecorderNode) (myMacro.getMacroList().getNode());
		EtherEvent InCopy = MyN.getCopyEvent();
		MyN.setOrigReply(reply);
		MyN.setCopyReply(replyCopy);
		handleEtherEventFinish(InCopy, replyCopy, refcon);
		newInEvent = null;
	}

	/**
	* Handles the closing of an Ether Event that returned an error.  Closes down the recording
	* of the event.
	* @param in The input event.
    * @param reply The reply generated by executing the event.
    * @param refcon A reference to context data that the generating code can associate with the event.  See various references to "refcon" in MacOS programming.
    * @param ex Error or exception generated by the attempt to execute the event.
	*/
	protected void closeNewEtherEventErr(
		EtherEvent in,
		Object reply,
		Object refcon,
		Throwable ex) {
		myMacro.getMacroList().searchHead();
		myMacro.getMacroList().left();
		myMacro.getMacroList().eraseNodeInfo();
		handleEtherEventErr(in, reply, refcon, ex);
		newInEvent = null;
	}

	/**
	* If the object "in" matches a scripting parameter already in the system, return
	* the scripting parameter.  Otherwise returns null.
	* @param in The object to check for a match.
	* @return Either the scripting parameter or null.
	*/
	protected Object parameterMatch(Object in) {
		int len = paramTable.size();
		int count = 0;
		boolean Found = false;
		Object FoundNode = null;

		while ((count < len) && (!Found)) {
			Object elem = paramTable.elementAt(count);
			if (elem == in) {
				FoundNode = new MacroParameter(count);
				Found = true;
			}

			count++;
		}

		return (FoundNode);
	}

	/**
	* Returns true if the object can be encoded in the macro listing without
	* the use of a scripting parameter.
	* @param obj Theobject to check.
	* @return True if the object can be encoded in the macro listing.
	*/
	protected boolean getObjectSerialCheck(Object obj) {
		boolean ret = false;

		if (((obj instanceof Serializable)
			|| (obj instanceof Externalizable)
			|| (obj == null))
			&& !((obj instanceof Component)
				|| (obj instanceof VerdantiumComponent)))
			ret = true;

		return (ret);
	}

	/**
	* Verifies that all of the parameters of the EtherEvent either match scripting parameters
	* or are serializable.  If they aren't, the method returns null.  Otherwise, it returns
	* the EtherEvent "in" with the scripting parameter matches replaced by {@link MacroParameter}
	* instances.
	* @param in The event to check.
	* @return Either a copy of the event or null.
	*/
	protected EtherEvent checkEtherEventParam(EtherEvent in) {
		EtherEvent copy = in;
		EtherEvent out = copy;
		Object param = in.getParameter();
		if (param instanceof Object[]) {
			Object[] parm = (Object[]) param;
			int count;
			for (count = 0; count < parm.length; count++) {
				Object obj = parm[count];
				if (getObjectSerialCheck(obj)) {
				} else {
					Object tmp = parameterMatch(obj);
					if (tmp != null)
						parm[count] = tmp;
					else
						out = null;
				}
			}
		} else {
			if (param != null) {
				if (getObjectSerialCheck(param)) {
				} else {
					Object tmp = parameterMatch(param);
					if (tmp != null)
						copy.setParameter(tmp);
					else
						out = null;
				}
			}
		}

		return (out);
	}

	/**
	* Determines if the target of an EtherEvent matches a scripting parameter or is serializable.
	* If it is, the method then determines if all parameters either match a scripting parameter
	* or are serializable.  If either of the conditions fail, the method returns null.  Otherwise,
	* it returns a copy of the EtherEvent with all scripting parameter matches replaced by
	* scripting parameters.
	* @param in The event to check.
	* @return Either a copy of the event or null.
	*/
	protected EtherEvent checkEtherEventCopy(EtherEvent in) {
		Object target = in.getTarget();

		if (getObjectSerialCheck(target)) {
		} else {
			Object tmp = parameterMatch(target);
			if (tmp != null)
				target = tmp;
			else
				target = null;
		}

		EtherEvent copy = null;
		if (target != null) {
			copy = copyEtherEvent(in, target, this);
			copy = checkEtherEventParam(copy);
		}

		return (copy);
	}

	/**
	* Attempts to add a new Ether Event to the listing.
	* @param in The input event.
    * @param reply The reply generated by executing the event.
    * @param refcon A reference to context data that the generating code can associate with the event.  See various references to "refcon" in MacOS programming.
	*/
	protected void attemptEtherEventAppend(
		EtherEvent in,
		Object reply,
		Object refcon) {
		if (newInEvent == null) {
			EtherEvent inCopy = checkEtherEventCopy(in);
			if (inCopy != null) {
				appendNewEtherEvent(in, inCopy, refcon);
				newInEvent = in;
			}
		}
	}

	/**
	* Handles property change events that describe Ether Events to be scripted.
	* @param e The input event.
	*/
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getPropertyName() == ProgramDirector.EtherEventStart) {
			if (currentMode == RecordMode) {
				Object[] parm = (Object[]) (e.getNewValue());
				EtherEvent in = (EtherEvent) (parm[0]);
				Object reply = parm[1];
				Object refcon = parm[2];
				attemptEtherEventAppend(in, reply, refcon);
			}
		}

		if (e.getPropertyName() == ProgramDirector.EtherEventEnd) {
			if (currentMode == RecordMode) {
				Object[] parm = (Object[]) (e.getNewValue());
				EtherEvent in = (EtherEvent) (parm[0]);
				Object reply = parm[1];
				Object refcon = parm[2];
				if (in == newInEvent) {
					closeNewEtherEvent(in, reply, refcon);
				}
			}
		}

		if (e.getPropertyName() == ProgramDirector.EtherEventErr) {
			if (currentMode == RecordMode) {
				Object[] parm = (Object[]) (e.getNewValue());
				EtherEvent in = (EtherEvent) (parm[0]);
				Object reply = parm[1];
				Object refcon = parm[2];
				Throwable ex = (Throwable) (parm[3]);
				if (in == newInEvent) {
					closeNewEtherEventErr(in, reply, refcon, ex);
				}
			}
		}

	}

	/**
	* Handles the destruction of the component by un-hooking its property change support.
	*/
	public void handleDestroy() {
		ProgramDirector.removeClassPropertyChangeListener(this);
		propL.firePropertyChange(
			ProgramDirector.propertyHide,
			null,
			null);
		propL.firePropertyChange(
			ProgramDirector.propertyDestruction,
			null,
			null);
	}

	/**
	* Gets whether a macro or macros are playing in any component.
	* @return Whether a macro or macros are playing in any component.
	*/
	public static boolean getMacroIsPlaying() {
		return (playNum > 0);
	}

	/**
	* Gets the current macro object.
	* @return The current macro object.
	*/
	public MacroObject getMacro() {
		return (myMacro);
	}

	/**
	* Sets the current macro object.
	* @param in The current macro object.
	*/
	public void setMacro(MacroObject in) {
		myMacro = in;
		paramTable.setSize(myMacro.getParamVal());
		resultTable.setSize(myMacro.getParamVal());
	}

	/**
	* Gets the parameter table.
	* @return The parameter table.
	*/
	public Vector<Object> getParamTable() {
		return (paramTable);
	}

	/**
	* PropertyChangeSupport object used by the component to fire property change events.
	*/
	private PropertyChangeSupport propL = null;
	
}

