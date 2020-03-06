package verdantium.core;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Constructor;

import meta.DataFormatException;
import meta.Meta;
import meta.VersionBuffer;
import verdantium.EtherEvent;
import verdantium.utils.IOResourceNotFoundException;

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
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/10/2001            | Thorn Green (viridian_1138@yahoo.com)           | Second-Cut at Error Handling.                                        | Second-Cut at Error Handling.
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
* MacroRecorderNode is a node type used by {@link MacroRecorder} to store
* the part of a macro that contains exactly one EtherEvent.  The EtherEvent
* is parameterized so that it can be played back later.
* <P>
* @author Thorn Green
*/
public class MacroRecorderNode extends Meta implements Externalizable {

	/**
	* Constructs the macro recorder node.
	*/
	public MacroRecorderNode() {
	}

	/**
	* Gets the original event that prompted the creation of the node.  This method
	* is only used during script recording.
	* @return The original event that prompted the creation of the node.
	*/
	public EtherEvent getOrigEvent() {
		return (OrigEvent);
	}
	
	/**
	* Sets the original event that prompted the creation of the node.  This method
	* is only used during script recording.
	* @param in The original event that prompted the creation of the node.
	*/
	public void setOrigEvent(EtherEvent in) {
		OrigEvent = in;
	}
	
	/**
	* Gets the copy of the original event with appropriate parameters
	* replaced by MacroParameterNode instances.  This copy could then be
	* used for playback.
	* @return The copy of the original event.
	*/
	public EtherEvent getCopyEvent() {
		return (CopyEvent);
	}
	
	/**
	* Sets the copy of the original event with appropriate parameters
	* replaced by MacroParameterNode instances.  This copy could then be
	* used for playback.
	* @param in The copy of the original event.
	*/
	public void setCopyEvent(EtherEvent in) {
		CopyEvent = in;
	}
	
	/**
	* Gets the reply sent to the original EtherEvent that prompted the creation
	* of the node.  This node is only accessed when a script is being recorded.
	* @return The reply sent to the original EtherEvent.
	*/
	public Object getOrigReply() {
		return (OrigReply);
	}
	
	/**
	* Sets the reply sent to the original EtherEvent that prompted the creation
	* of the node.  This node is only accessed when a script is being recorded.
	* @param in The reply sent to the original EtherEvent.
	*/
	public void setOrigReply(Object in) {
		OrigReply = in;
	}
	
	/**
	* Gets the copy of the original reply object with the parameters of the reply
	* replaced by MacroParameterNode objects.  This copy is the one used for playback.
	* @return The copy of the original reply object.
	*/
	public Object getCopyReply() {
		return (CopyReply);
	}
	
	/**
	* Sets the copy of the original reply object with the parameters of the reply
	* replaced by MacroParameterNode objects.  This copy is the one used for playback.
	* @param in The copy of the original reply object.
	*/
	public void setCopyReply(Object in) {
		CopyReply = in;
	}

	/**
	* Makes the node capable of being used with Meta.
	*/
	public void wake() {
	}

	/**
	* Loads the macro recorder node from persistent storage.
	*/
	public void readExternal(ObjectInput in)
		throws IOException, ClassNotFoundException {
		try {
			VersionBuffer myv = (VersionBuffer) (in.readObject());
			VersionBuffer.chkNul(myv);

			Object CopyEventTarget = myv.getProperty("CopyEventTarget");
			VersionBuffer.chkNul(CopyEventTarget);
			Object CopyEventParam = myv.getProperty("CopyEventParam");
			Class CopyEventClass = (Class) (myv.getProperty("CopyEventClass"));
			VersionBuffer.chkNul(CopyEventClass);
			String CopyEventID = (String) (myv.getProperty("CopyEventID"));
			VersionBuffer.chkNul(CopyEventID);
			Object CopyReplyParam = myv.getProperty("CopyReplyParam");

			CopyReply = CopyReplyParam;

			Class[] classes =
				{ Object.class, String.class, Object.class, Object.class };
			Object[] param =
				{
					"Macro Recorder",
					CopyEventID,
					CopyEventParam,
					CopyEventTarget };

			try {
				Constructor MyC = CopyEventClass.getConstructor(classes);
				Object res = MyC.newInstance(param);
				CopyEvent = (EtherEvent) res;
				VersionBuffer.chkNul(CopyEvent);
			} catch (IOException e) {
				throw (e);
			} catch (Exception e) {
				throw (
					new IOResourceNotFoundException(
						"Macro Resource Not Found",
						e));
			}
		} catch (ClassCastException e) {
			throw (new DataFormatException(e));
		}
	}

	/**
	* Saves the macro recorder node to persistent storage.  @serialData TBD.
	*/
	public void writeExternal(ObjectOutput out) throws IOException {
		VersionBuffer myv = new VersionBuffer(VersionBuffer.WRITE);

		myv.setProperty("CopyEventTarget", CopyEvent.getTarget());
		if (CopyEvent.getParameter() != null)
			myv.setProperty("CopyEventParam", CopyEvent.getParameter());
		myv.setProperty("CopyEventClass", CopyEvent.getClass());
		myv.setProperty("CopyEventID", CopyEvent.getEtherID());
		if (CopyReply != null)
			myv.setProperty("CopyReplyParam", CopyReply);

		out.writeObject(myv);
	}

	
	/**
	* The original event that prompted the creation of the node.  This node
	* is only used during script recording.
	*/
	private transient EtherEvent OrigEvent = null;
	
	/**
	* The copy of the original event object with the parameters of the reply
	* replaced by MacroParameterNode objects.  This copy is the one used for playback.
	*/
	private EtherEvent CopyEvent = null;
	
	/**
	* The reply sent to the original EtherEvent that prompted the creation
	* of the node.  This node is only accessed when a script is being recorded.
	*/
	private transient Object OrigReply = null;
	
	/**
	* The copy of the original reply object with the parameters of the reply
	* replaced by MacroParameterNode objects.  This copy is the one used for playback.
	*/
	private Object CopyReply = null;
	
	
}


