


package verdantium.standard;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;
import verdantium.kluges.SerPoint2D;
import umeta.*;
import java.io.*;



/**
 * A polygonal drawing object.
 * 
 * @author tgreen
 *
 */
public class pdx_DrawingPolygonalObject_pdx_ObjectRef extends verdantium.standard.pdx_DrawingPolygonalObjectBase_pdx_ObjectRef {
	
	/**
	 * Constructor.
	 * @param Key The key reference for the object.
	 */
    protected pdx_DrawingPolygonalObject_pdx_ObjectRef( jundo.runtime.KeyRef Key ) {
        super( Key );
    }
    
    /**
     * Sets all of the members of the object to zero.
     * @param pdx_thismilieu The input milieu.
     * @param in The input object reference.
     * @return The pair representing the resulting object.
     */
    public static verdantium.standard.pdx_DrawingPolygonalObjectBase_pdx_PairRef pdx_zero( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final verdantium.standard.pdx_DrawingPolygonalObjectBase_pdx_ObjectRef in ) {
        final verdantium.standard.pdx_DrawingPolygonalObjectBase_pdx_PairRef tx = new verdantium.standard.pdx_DrawingPolygonalObjectBase_pdx_PairRef( in , pdx_thismilieu );
        jundo.runtime.ExtMilieuRef mil = tx.getMilieu();
        mil = jundo.runtime.Runtime.asgBooleanObjectMember( tx.getObject() , "filledRender" , mil , false );
        mil = jundo.runtime.Runtime.asgBooleanObjectMember( tx.getObject() , "curveRender" , mil , false );
        mil = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "myPath" , mil , null );
        mil = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "lineColor" , mil , null );
        mil = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "fillColor" , mil , null );
        mil = jundo.runtime.Runtime.asgDoubleObjectMember( tx.getObject() , "basicStrokeWidth" , mil , 0 );
        mil = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "myStroke" , mil , null );
        mil = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "prevInt" , mil , null );
        mil = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "prevBez" , mil , null );
        mil = jundo.runtime.Runtime.asgIntObjectMember( tx.getObject() , "bezMode" , mil , 0 );
        mil = jundo.runtime.Runtime.asgBooleanObjectMember( tx.getObject() , "chordP" , mil , false );
        mil = jundo.runtime.Runtime.asgIntObjectMember( tx.getObject() , "renderingMode" , mil , 0 );
        final verdantium.standard.pdx_DrawingPolygonalObjectBase_pdx_PairRef z = new verdantium.standard.pdx_DrawingPolygonalObjectBase_pdx_PairRef( in , mil );
        return( z );
    }
    
    /**
     * Initializes the class members of the class.
     */
    public static void pdx_initClassMembers( ) {
    }
    
    static
    {
        pdx_initClassMembers();
    }
    
    @Override
    public java.lang.Object pdxm_downBuild( final jundo.runtime.ExtMilieuRef mil ) {
        DrawingPolygonalObject dobj = new DrawingPolygonalObject();
        
        dobj.setRenderingMode(
                pdxm_getRenderingMode(mil), pdxm_getBezMode(mil), pdxm_getChordP(mil) );
        dobj.setLineColor( (Color)( pdxm_getLineColor( mil ) ) );
        dobj.setFillColor( (Color)( pdxm_getFillColor( mil ) ) );
        double strokeWidth = pdxm_getBasicStrokeWidth( mil );
        if( strokeWidth >= 0.0 ) {
            dobj.setBasicStroke( strokeWidth );
        } else {
            dobj.setStroke( (Stroke)( pdxm_getMyStroke( mil ) ) );
        }
        Vector<SerPoint2D> prevInt = new Vector<SerPoint2D>( (Vector)( pdxm_getPrevInt(mil) ) );
        Vector<Point2D.Double> prevBez = new Vector( (Vector<Point2D.Double>)( pdxm_getPrevBez(mil) ) );
        dobj.setVectors( prevInt , prevBez );
        
        return( dobj );
    }
    
    @Override
    public java.lang.Object pdxm_clickedInRegion( final jundo.runtime.ExtMilieuRef mil , final java.lang.Object  _thePort , final int  toolMode , final java.lang.Object  _inPoint ) {
        
        APPRec NewRec = new APPRec();
        APPRec ret = null;
        double Priority = ClickRec.MIN_PRIORITY + 1;
        int LastClick = DrawingPolygonalObject.NO_MATCH;
        DrawApp thePort = (DrawApp)( _thePort );
        Point2D.Double InPt = (Point2D.Double)( _inPoint );
        
        final Vector<SerPoint2D> prevInt = (Vector<SerPoint2D>)( pdxm_getPrevInt(mil) );
        
        int sz = prevInt.size();
        int count;
        
        for (count = 0; count < sz; count++) {
            SerPoint2D ser = prevInt.elementAt(count);
            
            Priority =
                    thePort.defaultGravityField(InPt, ser.getX(), ser.getY());
            if ((Priority <= ClickRec.MIN_PRIORITY) && (LastClick == DrawingPolygonalObject.NO_MATCH)) {
                NewRec.setValue(count);
                NewRec.setXCoord(ser.getX());
                NewRec.setYCoord(ser.getY());
                NewRec.clickPriority = Priority;
                ret = NewRec;
                LastClick = DrawingPolygonalObject.MATCH;
            }
            
        }
        
        return ret;
    }
    
    @Override
    public jundo.runtime.ExtMilieuRef pdxm_draw( final jundo.runtime.ExtMilieuRef mil , final java.lang.Object  thePort , final java.lang.Object _g , final int  toolMode ) {
        final Graphics2D g = (Graphics2D) _g;
        
        if( pdxm_getFilledRender( mil ) ) {
            g.setStroke( (Stroke)( pdxm_getMyStroke(mil) ) );
            g.setColor( (Color)( pdxm_getFillColor(mil) ) );
            g.fill( (GeneralPath)( pdxm_getMyPath(mil) ) );
            g.setColor( (Color)( pdxm_getLineColor(mil) ) );
            g.draw( (GeneralPath)( pdxm_getMyPath(mil) ) );
        } else {
            g.setStroke( (Stroke)( pdxm_getMyStroke(mil) ) );
            g.setColor( (Color)( pdxm_getLineColor(mil) ) );
            g.draw( (GeneralPath)( pdxm_getMyPath(mil) ) );
        }
        
        return( mil );
    }
    
    @Override
    public jundo.runtime.ExtMilieuRef pdxm_drawTools( final jundo.runtime.ExtMilieuRef mil , final java.lang.Object  _thePort , final java.lang.Object  _g , final int  toolMode ) {
        final Graphics2D g = (Graphics2D) _g;
        final DrawApp thePort = (DrawApp) _thePort;
        final Vector<SerPoint2D> prevInt = (Vector<SerPoint2D>)( pdxm_getPrevInt(mil) );
        int sz = prevInt.size();
        int count;
        if( toolMode != DrawApp.ErASE_MODE )
                g.setColor(Color.cyan);
            else g.setColor(Color.orange);
        
        for (count = 0; count < sz; count++) {
            SerPoint2D ser = prevInt.elementAt(count);
            Rectangle2D.Double r1 =
                    thePort.instanceRect(ser.getX(), ser.getY());
            g.fill(r1);
        }
        
        return( mil );
    }
    
    
    @Override
    public jundo.runtime.ExtMilieuRef pdxm_wake( final jundo.runtime.ExtMilieuRef pdx_thismilieu ) {
        return( pdx_thismilieu );
    }
    
    /**
     * Creates the OID for the object.
     * @param inMil The input milieu.
     * @return The pair representing the created object.
     */
    public static verdantium.standard.pdx_DrawingPolygonalObject_pdx_PairRef createOID_DrawingPolygonalObject( jundo.runtime.ExtMilieuRef InMil ) {
        jundo.runtime.ExtMilieuRef NewMil = jundo.runtime.Runtime.createOIDmilieu( InMil );
        int NewID = NewMil.getMaxID();
        jundo.runtime.KeyRef MyKey = jundo.runtime.Runtime.getKeyRef( NewID );
        pdx_DrawingPolygonalObject_pdx_ObjectRef NewObj =
                new pdx_DrawingPolygonalObject_pdx_ObjectRef( MyKey );
        verdantium.standard.pdx_DrawingPolygonalObject_pdx_PairRef ret = new verdantium.standard.pdx_DrawingPolygonalObject_pdx_PairRef( NewObj , NewMil );
        return( ret );
    }
    
    /**
     * Constructs the object.
     * @param pdx_thismilieu The input milieu.
     * @return The pair representing the created object.
     */
    public static verdantium.standard.pdx_DrawingPolygonalObject_pdx_PairRef pdxm_new_DrawingPolygonalObject( final jundo.runtime.ExtMilieuRef pdx_thismilieu ) {
        final verdantium.standard.pdx_DrawingPolygonalObject_pdx_PairRef t1 = pdx_DrawingPolygonalObject_pdx_ObjectRef.createOID_DrawingPolygonalObject( pdx_thismilieu );
        final verdantium.standard.pdx_DrawingPolygonalObjectBase_pdx_PairRef t2 = pdx_zero( t1.getMilieu() , (verdantium.standard.pdx_DrawingPolygonalObject_pdx_ObjectRef)( t1.getObject() ) );
        final verdantium.standard.pdx_DrawingPolygonalObject_pdx_PairRef t3 = new verdantium.standard.pdx_DrawingPolygonalObject_pdx_PairRef(
                (verdantium.standard.pdx_DrawingPolygonalObject_pdx_ObjectRef)( t2.getObject() ) , t2.getMilieu() );
        
        return( t3 );
    }
    
    /**
     * Constructs a new object for persistence purposes.
     * @param pdx_thismilieu The input milieu.
     * @return The pair representing the created object.
     */
    public static verdantium.standard.pdx_DrawingPolygonalObject_pdx_PairRef pdxm_newPersist( final jundo.runtime.ExtMilieuRef pdx_thismilieu ) {
        final verdantium.standard.pdx_DrawingPolygonalObject_pdx_PairRef t1 = pdx_DrawingPolygonalObject_pdx_ObjectRef.createOID_DrawingPolygonalObject( pdx_thismilieu );
        final verdantium.standard.pdx_DrawingPolygonalObjectBase_pdx_PairRef t2 = pdx_zero( t1.getMilieu() , (verdantium.standard.pdx_DrawingPolygonalObject_pdx_ObjectRef)( t1.getObject() ) );
        final verdantium.standard.pdx_DrawingPolygonalObject_pdx_PairRef t3 = new verdantium.standard.pdx_DrawingPolygonalObject_pdx_PairRef(
                (verdantium.standard.pdx_DrawingPolygonalObject_pdx_ObjectRef)( t2.getObject() ) , t2.getMilieu() );
        
        return( t3 );
    }
    
    
    /**
    * Writes the object to persistent storage.  @serialData TBD.
    */
    public umeta.pdx_VersionBuffer_pdx_PairRef pdxm_writeObject( final jundo.runtime.ExtMilieuRef pdx_thismilieu ) {
        pdx_VersionBuffer_pdx_PairRef initial = pdx_VersionBuffer_pdx_ObjectRef.pdxm_new_VersionBuffer(pdx_thismilieu);
        jundo.runtime.ExtMilieuRef mil = initial.getMilieu();
        pdx_VersionBuffer_pdx_ObjectRef myv = (pdx_VersionBuffer_pdx_ObjectRef)( initial.getObject() );

        
        mil = myv.pdxm_setInt( mil , "BezMode" , pdxm_getBezMode( mil ) );
	mil = myv.pdxm_setBoolean(mil , "ChordP", pdxm_getChordP( mil ) );
        mil = myv.pdxm_setInt( mil , "RenderingMode" , pdxm_getRenderingMode( mil ) );
        mil = myv.pdxm_setBoolean(mil , "FilledRender", pdxm_getFilledRender( mil ) );
	mil = myv.pdxm_setBoolean(mil , "CurveRender", pdxm_getCurveRender( mil ) );
	mil = myv.pdxm_setJobjProperty(mil, "LineColor", pdxm_getLineColor( mil ) );
	mil = myv.pdxm_setJobjProperty(mil, "FillColor", pdxm_getFillColor( mil ) );
        mil = myv.pdxm_setJobjProperty(mil, "PrevInt", pdxm_getPrevInt( mil ) );
	mil = myv.pdxm_setJobjProperty(mil, "PrevBez", pdxm_getPrevBez( mil ) );

	if( pdxm_getBasicStrokeWidth( mil ) >= 0.0 )
		mil = myv.pdxm_setDouble(mil, "BasicStrokeWidth", pdxm_getBasicStrokeWidth( mil ) );

        Stroke myStroke = (Stroke)( pdxm_getMyStroke(mil) );
	if ((myStroke instanceof Serializable)
		|| (myStroke instanceof Externalizable))
		mil = myv.pdxm_setJobjProperty(mil, "MyStroke", myStroke);
        

        return( new pdx_VersionBuffer_pdx_PairRef( myv , mil ) );
    }
    
    /**
     * Sets the stroke used when rendering the object to a basic stroke with a certain width.
     * @param pdx_thismilieu The input milieu.
     * @param in The input basic stroke width.
     * @return The resulting milieu.
     */
	public jundo.runtime.ExtMilieuRef setBasicStroke( final jundo.runtime.ExtMilieuRef pdx_thismilieu , double in) {
		
            jundo.runtime.ExtMilieuRef mil = pdx_thismilieu;
            mil = pdxm_setBasicStrokeWidth( mil , in );
	    mil = pdxm_setMyStroke( mil , 
			new BasicStroke(
				(float) in,
				BasicStroke.CAP_SQUARE,
				BasicStroke.JOIN_MITER) );
            return( mil );
	}
        
    /**
    * Creates a GeneralPath object that represents the shape to be rendered, where PrevInt
    * contains the interpolation points, and PrevBez contains the Bezier points (if needed).
    * The GeneralPath is stored in MyPath.
    */
	protected jundo.runtime.ExtMilieuRef createGeneralPathFromVector(
                jundo.runtime.ExtMilieuRef _mil,
		Vector<SerPoint2D> prevInt,
		Vector<Point2D.Double> prevBez) {
                jundo.runtime.ExtMilieuRef mil = _mil;
		if( pdxm_getCurveRender( mil ) ) {
			mil = pdxm_setMyPath( mil , CurveEdit.createGeneralPathFromVector(prevBez) );
		} else {
			int elem = prevInt.size();
			GeneralPath temp = new GeneralPath(GeneralPath.WIND_EVEN_ODD, elem);

			if (elem > 0) {
				Point2D.Double first = prevInt.elementAt(0);
				temp.moveTo((float) (first.getX()), (float) (first.getY()));
				int cnt = 1;
				for (cnt = 1; cnt < elem; cnt = cnt + 1) {
					Point2D.Double b =
						prevInt.elementAt(cnt);
					temp.lineTo((float) (b.getX()), (float) (b.getY()));
				}
			}

			mil = pdxm_setMyPath( mil , temp );
		}
                
                return( mil );
	}
    
	  /**
	    * Reads the object from persistent storage.
	    * @param pdx_thismilieu The milieu.
	    * @param myv The version buffer from which to read the object.
	    */
    public jundo.runtime.ExtMilieuRef pdxm_readObject( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final umeta.pdx_VersionBuffer_pdx_ObjectRef  myv ) {
        jundo.runtime.ExtMilieuRef mil = pdx_thismilieu;
        
        mil = pdxm_setBezMode( mil ,  myv.pdxm_getInt(mil, "BezMode") );
	mil = pdxm_setChordP( mil ,  myv.pdxm_getBoolean(mil, "ChordP") );
        mil = pdxm_setRenderingMode( mil ,  myv.pdxm_getInt(mil, "RenderingMode") );
        mil = pdxm_setFilledRender( mil ,  myv.pdxm_getBoolean(mil, "FilledRender") );
	mil = pdxm_setCurveRender( mil ,  myv.pdxm_getBoolean(mil, "CurveRender") );
	mil = pdxm_setLineColor( mil ,  (Color)( myv.pdxm_getJobjProperty(mil, "LineColor") ) );
        pdx_VersionBuffer_pdx_ObjectRef.pdxm_chkJobjNul( mil , pdxm_getLineColor( mil ) );
        mil = pdxm_setFillColor( mil ,  (Color)( myv.pdxm_getJobjProperty(mil, "FillColor") ) );
        pdx_VersionBuffer_pdx_ObjectRef.pdxm_chkJobjNul( mil , pdxm_getFillColor( mil ) );
        mil = pdxm_setPrevInt( mil ,  (Vector<SerPoint2D>)( myv.pdxm_getJobjProperty(mil, "PrevInt") ) );
        pdx_VersionBuffer_pdx_ObjectRef.pdxm_chkJobjNul( mil , pdxm_getPrevInt( mil ) );
        mil = pdxm_setPrevBez( mil ,  (Vector<Point2D.Double>)( myv.pdxm_getJobjProperty(mil, "PrevBez") ) );
        pdx_VersionBuffer_pdx_ObjectRef.pdxm_chkJobjNul( mil , pdxm_getPrevBez( mil ) );
        
	if( myv.pdxm_propertyExists( mil , "BasicStrokeWidth" ) )
        {
            double wid = myv.pdxm_getDouble( mil , "BasicStrokeWidth" );
            mil = setBasicStroke( mil , wid );
        }

         if( myv.pdxm_propertyExists( mil , "MyStroke" ) )
        {
            mil = pdxm_setMyStroke( mil , (Stroke)( myv.pdxm_getJobjProperty( mil , "MyStroke" ) ) );
            mil = pdxm_setBasicStrokeWidth( mil , -1 );            
        }

	mil = createGeneralPathFromVector(mil , 
                (Vector<SerPoint2D>)( pdxm_getPrevInt( mil ) ) , 
                (Vector<Point2D.Double>)( pdxm_getPrevBez( mil ) ) );
                        
        return( mil );
    }
    
    
    
}


