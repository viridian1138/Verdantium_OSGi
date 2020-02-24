package verdantium.undo;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Iterator;
import java.util.WeakHashMap;
import java.util.HashMap;
import java.io.Serializable;

import javax.swing.JOptionPane;

import jundo.runtime.*;
import jundo.util.pdx_MilieuRef_pdx_ObjectRef;
import jundo.util.pdx_MilieuRef_pdx_PairRef;
import verdantium.EtherEvent;
import verdantium.EtherEventHandler;

import meta.DataFormatException;
import meta.VersionBuffer;

import java.lang.reflect.*;

//$$strtCprt
/*
     Verdantium compound-document framework by Thorn Green
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
 * @author thorngreen
 *
 * Class for handling multi-level Undo in a general fashion.
 */
public class UndoManager implements EtherEventHandler {

	/**
	 * The current milieu for the Undo implementation.
	 */
	protected ExtMilieuRef mgrCurrentMil = null;

	/**
	 * Weak hash map containing previous states of the Undo Impl
	 * indexed by UTag.
	 */
	protected final WeakHashMap<UTag, ExtMilieuRef> prevCommitMils = new WeakHashMap<UTag, ExtMilieuRef>();

	/**
	 * JUndo implementation of multi-level Undo.
	 */
	protected pdx_UndoImpl_pdx_ObjectRef undoImpl = null;

	/**
	 * Flag indicating whether an undo or a redo is in the
	 * process of updating the data space.
	 */
	protected boolean undoUpdating = false;

	/**
	 * Property change support for broadcasting events.
	 */
	private PropertyChangeSupport PropL = null;

	/**
	 * Property change label indicating a change to the application model state.
	 */
	public static final String MODEL_STATE_CHANGED = "MODEL_STATE_CHANGED";

	/**
	 * Property change label indicating a change to the Undo GUI.
	 */
	public static final String UNDO_UI_CHANGED = "UNDO_UI_CHANGED";
        
	/**
	 * Persistence key for the class name of the JUndo data.
	 */
        public static final String JUNDO_PERSISTENCE_CLASS = "JUndoPersistenceClass";
        
    /**
     * Persistence key for the content of the JUndo data.
     */
        public static final String JUNDO_PERSISTENCE_DATA = "JUndoPersistenceData";

	/**
	 * Constructs the Undo Manager from the
	 * initial application milieu.
	 * @param initialRef The initial application milieu.
	 */
	private UndoManager(jundo.runtime.ExtMilieuRef initialRef) {
		super();

		PropL = new PropertyChangeSupport(this);
		pdx_MilieuRef_pdx_PairRef pref =
			pdx_MilieuRef_pdx_ObjectRef.pdxm_new_MilieuRef(
				jundo.runtime.Runtime.getInitialMilieu(),
				initialRef);
		pdx_UndoImpl_pdx_PairRef iref =
			pdx_UndoImpl_pdx_ObjectRef.pdxm_new_UndoImpl(
				pref.getMilieu(),
				(pdx_MilieuRef_pdx_ObjectRef) (pref.getObject()));
		mgrCurrentMil = iref.getMilieu();
		undoImpl = (pdx_UndoImpl_pdx_ObjectRef) (iref.getObject());
	}

	/**
	 * Creates an instance of the Undo Manager.
	 * @param initialRef The initial application milieu.
	 * @return The Undo Manager.
	 */
	public static UndoManager createInstanceUndoManager(ExtMilieuRef initialRef) {
		UndoManager undoMan = new UndoManager(initialRef);
		return (undoMan);
	}

	/**
	* Adds a property change listener to the manager.
	* @param e The listener to add.
	*/
	public void addPropertyChangeListener(PropertyChangeListener e) {
		PropL.addPropertyChangeListener(e);
	}

	/**
	* Removes a property change listener from the manager.
	* @param e The listener to remove.
	*/
	public void removePropertyChangeListener(PropertyChangeListener e) {
		PropL.removePropertyChangeListener(e);
	}

	/**
	 * Fires a property change event indicating a chnage to the
	 * application model state.
	 */
	protected void fireModelStateChanged() {
		undoUpdating = true;
		PropL.firePropertyChange(MODEL_STATE_CHANGED, null, null);
		undoUpdating = false;
	}

	/**
	 * Fires a property change event indicating a change to the Undo GUI.
	 */
	protected void fireUndoUiChanged() {
		PropL.firePropertyChange(UNDO_UI_CHANGED, null, null);
	}

	/**
	 * Gets the current milieu on which to access data.
	 * @return The current milieu on which to access data.
	 */
	public ExtMilieuRef getCurrentMil() {

		if (ExtMilieuRef.debug_on) {
			System.out.println("===== >>>>>> ");
			mgrCurrentMil.traceThrowable.printStackTrace(System.out);
		}

		ExtMilieuRef currentMil = undoImpl.pdxm_getCurrentMilieu(mgrCurrentMil);
		return (currentMil);
	}

	/**
	 * Notes that the current state marks a file save.
	 */
	public void noteFileSave() {
		mgrCurrentMil = undoImpl.pdxm_noteFileSave(mgrCurrentMil);
	}

	/**
	 * Prepares the undo manager for a commit on a particular UTag.
	 * @param utag The UTag for which to save the current state.
	 */
	public void prepareForTempCommit(UTag utag) {
		prevCommitMils.put(utag, mgrCurrentMil);
	}

	/**
	 * Handles the commit of a temporary change.
	 * @param in Milieu describing the temporary change.
	 */
	public void handleCommitTempChange(ExtMilieuRef in) {
		pdx_MilieuRef_pdx_PairRef pair =
			pdx_MilieuRef_pdx_ObjectRef.pdxm_new_MilieuRef(mgrCurrentMil, in);
		mgrCurrentMil = pair.getMilieu();
		mgrCurrentMil =
			undoImpl.pdxm_commitNonUndoableState(
				mgrCurrentMil,
				(pdx_MilieuRef_pdx_ObjectRef) (pair.getObject()));
		fireUndoUiChanged();
	}

	/**
	 * Commits an undoable operation.
	 * @param utag The undo tag on which to commit the operation.
	 * @param operationName The operation name to display on the GUI.
	 */
	public void commitUndoableOp(UTag utag, String operationName) {
		UndoDesc desc =
			new UndoDesc(
				"Undo " + operationName,
				"Redo " + operationName,
				operationName);
		ExtMilieuRef currentMil = getCurrentMil();
		ExtMilieuRef prevCommitMil = prevCommitMils.get(utag);

		pdx_MilieuRef_pdx_PairRef pair =
			pdx_MilieuRef_pdx_ObjectRef.pdxm_new_MilieuRef(
				prevCommitMil,
				currentMil);
		prevCommitMil = pair.getMilieu();
		prevCommitMil =
			undoImpl.pdxm_commitStateAction(
				prevCommitMil,
				(pdx_MilieuRef_pdx_ObjectRef) (pair.getObject()),
				desc);
		mgrCurrentMil = prevCommitMil;
		prevCommitMils.remove(utag);
		fireUndoUiChanged();
	}

	/**
	 * Commits the handling of a critical failure in an undo operation.
	 * @param utag The undo tag on which to commit the failure.
	 */
	public void commitHandleCriticalFailure(UTag utag) {
		ExtMilieuRef prevCommitMil = prevCommitMils.get(utag);
		mgrCurrentMil = prevCommitMil;
		prevCommitMils.remove(utag);
		fireModelStateChanged();
	}

	/**
	* Commits the overlay of a new milieu over a previously committed operation.
	* This method should only be used under unusual situations.
	*/
	public void commitOverlay(final ExtMilieuRef currentMil) {
		ExtMilieuRef prevCommitMil = mgrCurrentMil;
		pdx_MilieuRef_pdx_PairRef pair =
			pdx_MilieuRef_pdx_ObjectRef.pdxm_new_MilieuRef(
				prevCommitMil,
				currentMil);
		prevCommitMil = pair.getMilieu();
		prevCommitMil =
			undoImpl.pdxm_commitOverlay(
				prevCommitMil,
				(pdx_MilieuRef_pdx_ObjectRef) (pair.getObject()));
		mgrCurrentMil = prevCommitMil;
	}

	/**
	 * Returns true iff. this is a file save point.
	 * @return True iff. this is a file save point.
	 */
	protected boolean isFileSavePoint() {
		return (undoImpl.pdxm_isFileSavePoint(mgrCurrentMil));
	}

	/**
	 * Performs a Undo operation if possible.
	 */
	protected void performUndoComp() {
		mgrCurrentMil = undoImpl.pdxm_performUndo(mgrCurrentMil);
		prevCommitMils.clear();
		fireModelStateChanged();
	}

	/**
	 * Performs a Undo operation if possible.
	 */
	public void performUndo() {
		if (isFileSavePoint()) {
			int choice =
				JOptionPane.showConfirmDialog(
					null,
					"Undo past previous file save?");
			if (choice == JOptionPane.OK_OPTION) {
				performUndoComp();
			}
		} else {
			performUndoComp();
		}
	}

	/**
	 * Performs a Redo operation if possible.
	 */
	public void performRedo() {
		mgrCurrentMil = undoImpl.pdxm_performRedo(mgrCurrentMil);
		prevCommitMils.clear();
		fireModelStateChanged();
	}

	/**
	 * Returns true iff. Undo is possible.
	 * @return True iff. Undo is possible.
	 */
	public boolean isUndoPossible() {
		return (undoImpl.pdxm_isUndoPossible(mgrCurrentMil));
	}

	/**
	 * Returns true iff. Redo is possible.
	 * @return True iff. Redo is possible.
	 */
	public boolean isRedoPossible() {
		return (undoImpl.pdxm_isRedoPossible(mgrCurrentMil));
	}

	/**
	 * Gets the GUI string describing the Undo operation.
	 * @return The GUI string describing the Undo operation.
	 */
	public String getUndoString() {
		String ret = "Undo";

		UndoDesc desc = (UndoDesc) (undoImpl.pdxm_getUndoDesc(mgrCurrentMil));
		if (desc != null) {
			ret = desc.getUndoString();
		}

		return (ret);
	}

	/**
	 * Gets the GUI string describing the Redo operstion.
	 * @return The GUI string describing the Redo operation.
	 */
	public String getRedoString() {
		String ret = "Redo";

		UndoDesc desc = (UndoDesc) (undoImpl.pdxm_getRedoDesc(mgrCurrentMil));
		if (desc != null) {
			ret = desc.getRedoString();
		}

		return (ret);
	}

	/**
	 * Clears undo memory.
	 */
	public void clearUndoMemory() {
		mgrCurrentMil = undoImpl.pdxm_clearUndoMemory(mgrCurrentMil);
		fireUndoUiChanged();
	}

	/**
	 * Returns an iterator of multi-level undo level descriptions.
	 * @return An iterator of multi-level undo level descriptions.
	 */
	public Iterator<String> iterator() {
		pdx_UndoIterator_pdx_PairRef ref =
			undoImpl.pdxm_iterator(mgrCurrentMil);
		return (new UndoIterator(ref));
	}

	/**
	 * Returns whether the undo state is updating.
	 * @return Whether the undo state is updating.
	 */
	public boolean isUndoUpdating() {
		return (undoUpdating);
	}

	/**
	 * Handles Undo events on behalf of a client component.
	 * @param in The event to handle.
	 * @param refcon A reference to context data for the event.
	 * @return The result of handling the event, or null if there is no result.
	 */
	public Object processObjEtherEvent(EtherEvent in, Object refcon)
		throws Throwable {
		Object ret = EtherEvent.EVENT_NOT_HANDLED;

		if (in instanceof UndoEtherEvent) {
			if (in.getEtherID().equals(UndoEtherEvent.isUndoSupported)) {
				return (new Boolean(true));
			}

			if (in.getEtherID().equals(UndoEtherEvent.getUndoManager)) {
				return (this);
			}

			if (in.getEtherID().equals(UndoEtherEvent.clearUndoMemory)) {
				clearUndoMemory();
				return (null);
			}

		}

		return ret;
	}
        
	/**
	 * Returns a persistent representation of the multi-level undo state in JUndo.
	 * @param jundoObject The JUndo object to persist.
	 * @param persistenceContext Persistence context used to eliminate duplicates.
	 * @return A persistent representation of the multi-level undo state in JUndo.
	 * @throws DataFormatException
	 */
        public meta.VersionBuffer generatePersistenceFromJUndo( jundo.runtime.ExtObjectRef jundoObject , HashMap<KeyRef, VersionBuffer> persistenceContext ) throws DataFormatException {
            
            Object ob = persistenceContext.get( jundoObject.getKeyRef() );
            if( ob != null )
            {
                return( (meta.VersionBuffer) ob );
            }
            
            if( jundoObject instanceof umeta.pdx_Versionable_pdx_ObjectRef )
            {
                umeta.pdx_Versionable_pdx_ObjectRef vers = 
                        (umeta.pdx_Versionable_pdx_ObjectRef) jundoObject;
                umeta.pdx_VersionBuffer_pdx_PairRef versBuff = 
                        vers.pdxm_writeObject( getCurrentMil() );
                jundo.runtime.ExtMilieuRef mil = versBuff.getMilieu();
                umeta.pdx_VersionBuffer_pdx_ObjectRef versBuffRef =
                        (umeta.pdx_VersionBuffer_pdx_ObjectRef)(versBuff.getObject());
                commitOverlay( mil );
                meta.VersionBuffer mbuff = generatePersistenceFromJUndo( versBuffRef , persistenceContext );
                persistenceContext.put( jundoObject.getKeyRef() , mbuff );
                mbuff = overlayPersistenceClass( mbuff , vers.getClass().getName() );
                return( mbuff );
            }
            
            if( jundoObject instanceof umeta.pdx_VersionBuffer_pdx_ObjectRef )
            {
                meta.VersionBuffer mbuff = new meta.VersionBuffer(true);
                persistenceContext.put( jundoObject.getKeyRef() , mbuff );
                umeta.pdx_VersionBuffer_pdx_ObjectRef vbuff =
                        (umeta.pdx_VersionBuffer_pdx_ObjectRef)( jundoObject );
                Iterator<String> it = vbuff.getKeyIterator();
                while( it.hasNext() )
                {
                    String key = it.next();
                    Object obv = vbuff.getElem( getCurrentMil() , key );
                    if( obv instanceof jundo.runtime.IExtObjectRef )
                    {
                        mbuff.setProperty(key, generatePersistenceFromJUndo( 
                               (jundo.runtime.ExtObjectRef) obv , persistenceContext ) );
                    }
                    else if( ( obv instanceof Serializable ) || ( obv == null ) )
                    {
                        mbuff.setProperty(key, obv);
                    }
                    else
                    {
                        throw( new DataFormatException( "Unable To Recognize Format." ) );
                    }
                }
                mbuff = overlayPersistenceClass( mbuff , jundoObject.getClass().getName() );
                return(mbuff);
            }
            
            throw( new DataFormatException( "Unable To Recognize Format." ) );
        }
        
        /**
         * Overlays class-name information on a JUndo-related version buffer, if necessary.
         * @param inbuf The input version buffer.
         * @param className The input JUndo class name.
         * @return The overlayed buffer if overlaying is necessary, otherwise the original buffer.
         */
        protected meta.VersionBuffer overlayPersistenceClass( meta.VersionBuffer inbuf , String className )
        {
            meta.VersionBuffer buf = inbuf;
            if( inbuf.getProperty( JUNDO_PERSISTENCE_CLASS ) != null )
            {
                buf = new meta.VersionBuffer(true);
                buf.setProperty( JUNDO_PERSISTENCE_DATA , inbuf );
            }
            buf.setProperty(JUNDO_PERSISTENCE_CLASS, className );
            return( buf );
        }
        
        /**
         * Reconstructs a JUndo representation from a persistent representation of the multi-level undo levels.
         * @param pdx_thismilieu The milieu from which to start the reconstruction.
         * @param vbuff The persistence representation from which to reconstruct the JUndo.
         * @param persistenceContext Persistence context used to eliminate duplicates.
         * @return The pair of the reconstructed JUndo representation.
         * @throws DataFormatException
         */
        protected jundo.runtime.IExtPair generateJUndoFromPersistence( final jundo.runtime.ExtMilieuRef pdx_thismilieu , meta.VersionBuffer vbuff , HashMap<VersionBuffer, IExtObjectRef> persistenceContext ) throws DataFormatException {
            
            if( persistenceContext.get( vbuff ) != null )
            {
                jundo.runtime.IExtObjectRef ref = ( persistenceContext.get( vbuff ) );
                return( new jundo.runtime.ExtPair( ref , pdx_thismilieu ) );
            }
            
            String pclass = (String)( vbuff.getProperty(JUNDO_PERSISTENCE_CLASS) );
            
            if( pclass != null )
            {
                if( pclass.equals( umeta.pdx_VersionBuffer_pdx_ObjectRef.class.getName() ) )
                {
                    if( persistenceContext.get( vbuff ) != null )
                    {
                        jundo.runtime.IExtObjectRef ref = ( persistenceContext.get( vbuff ) );
                        return( new jundo.runtime.ExtPair( ref , pdx_thismilieu ) );
                    }
                    umeta.pdx_VersionBuffer_pdx_PairRef ref = generateJUndoVersionBufferFromPersistence( pdx_thismilieu , vbuff , persistenceContext );
                    persistenceContext.put( vbuff , ref.getObject() );
                    return( ref );
                }
                else
                {
                    try
                    {
                        
                        if( persistenceContext.get( vbuff ) != null )
                        {
                            jundo.runtime.IExtObjectRef ref = ( persistenceContext.get( vbuff ) );
                            return( new jundo.runtime.ExtPair( ref , pdx_thismilieu ) );
                        }
                        umeta.pdx_VersionBuffer_pdx_PairRef jundoVersBuff = generateJUndoVersionBufferFromPersistence( pdx_thismilieu , vbuff , persistenceContext );
                        Class clss = Class.forName( pclass );
                        Class[] cparam = { ExtMilieuRef.class };
                        Method method = clss.getMethod("pdxm_newPersist", cparam);
                        Object[] oparam = { jundoVersBuff.getMilieu() };
                        ExtPair newPair = (ExtPair)( method.invoke(null, oparam) );
                        umeta.pdx_Versionable_pdx_ObjectRef versionable = 
                                (umeta.pdx_Versionable_pdx_ObjectRef)( newPair.getObject() );
                        ExtMilieuRef rmil = versionable.pdxm_readObject( newPair.getMilieu() , 
                                (umeta.pdx_VersionBuffer_pdx_ObjectRef)( jundoVersBuff.getObject() ) );
                        persistenceContext.put( vbuff , versionable );
                        return( new jundo.runtime.ExtPair( versionable , rmil ) );
                    }
                    catch( Throwable ex )
                    {
                        throw( new DataFormatException( ex ) );
                    }
                }
            }

            throw( new DataFormatException( "Unable To Recognize Format." ) );
        }
        
        
        /**
         * Class for remapping persistent representations into reconstructions of the JUndo representation.
         * 
         * @author tgreen
         *
         */
        protected class UndoMgrRemap extends umeta.pdx_VersionBuffer_pdx_ObjectRef.Remap
        {
        	/**
        	 * Persistence context for eliminating duplicates.
        	 */
            HashMap<VersionBuffer, IExtObjectRef> persistenceContext;
            
            /**
             * Constructor
             * @param _persistenceContext Persistence context for eliminating duplicates.
             */
            public UndoMgrRemap( HashMap<VersionBuffer, IExtObjectRef> _persistenceContext )
            {
                persistenceContext = _persistenceContext;
            }
        
            @Override
            public Object remap( final jundo.runtime.ExtMilieuRef pdx_thismilieu , Object in )
            {
                try
                {
                    if( in instanceof meta.VersionBuffer )
                     {
                        return( generateJUndoFromPersistence( pdx_thismilieu , (meta.VersionBuffer) in , persistenceContext ) );
                     }
                    else
                    {
                        return( in );
                    }
                }
                catch( Throwable ex )
                {
                    ex.printStackTrace( System.out );
                    throw( new RuntimeException( "Data Format Not Recognized" ) );
                }
            }
            
        }
        
        
        /**
         * Generates a JUndo version buffer from a VersionBuffer persistence representation.
         * @param pdx_thismilieu The milieu from which to create the JUndo version buffer.
         * @param vbuff The version buffer persistence representation.
         * @param persistenceContext Persistence context for eliminating duplicates.
         * @return The pair of the generated JUndo version buffer.
         * @throws DataFormatException
         */
        protected umeta.pdx_VersionBuffer_pdx_PairRef generateJUndoVersionBufferFromPersistence( jundo.runtime.ExtMilieuRef pdx_thismilieu , meta.VersionBuffer vbuff , HashMap<VersionBuffer, IExtObjectRef> persistenceContext ) throws DataFormatException {
            
            Object versd = vbuff.getProperty( JUNDO_PERSISTENCE_DATA );
            if( ( versd != null ) && ( versd instanceof meta.VersionBuffer ) )
            {
                meta.VersionBuffer vbuffa = (meta.VersionBuffer)( versd );
                return( generateJUndoVersionBufferFromPersistence( pdx_thismilieu , vbuffa , persistenceContext ) );
            }
            
            if( versd != null )
            {
                throw( new RuntimeException( "Data Not Recognized " + versd.getClass().getName() ) );
            }
            
            UndoMgrRemap remap = new UndoMgrRemap( persistenceContext );
            umeta.pdx_VersionBuffer_pdx_PairRef pbuff = 
                    umeta.pdx_VersionBuffer_pdx_ObjectRef.new_VersionBuffer_Persist( pdx_thismilieu , vbuff , remap );
            return(pbuff);
        }
        
        
        /**
         * Outward-facing call to return a reconstructed JUndo representation given a persistence representation.
         * @param vbuff The persistence representation.
         * @param persistenceContext Persistence context for eliminating duplicates.
         */
        public jundo.runtime.IExtObjectRef generateJUndoFromPersistence( meta.VersionBuffer vbuff , HashMap<VersionBuffer, IExtObjectRef> persistenceContext ) throws DataFormatException
        {
            jundo.runtime.IExtPair pair = 
                    generateJUndoFromPersistence( getCurrentMil() , vbuff , persistenceContext );
            handleCommitTempChange( pair.getMilieu() );
            return( pair.getObject() );
        }
        

}


