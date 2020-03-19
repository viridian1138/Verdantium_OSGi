


package verdantium.standard;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import umeta.*;



/**
 * A rectangular drawing object.
 * 
 * @author tgreen
 *
 */
public class pdx_DrawingRectangularObject_pdx_ObjectRef extends verdantium.standard.pdx_DrawingRectangularObjectBase_pdx_ObjectRef {
	
	/**
	 * Constructor.
	 * @param Key The key reference for the object.
	 */
    protected pdx_DrawingRectangularObject_pdx_ObjectRef( jundo.runtime.KeyRef Key ) {
        super( Key );
    }
    
    /**
     * Sets all of the members of the object to zero.
     * @param pdx_thismilieu The input milieu.
     * @param in The input object reference.
     * @return The pair representing the resulting object.
     */
    public static verdantium.standard.pdx_DrawingRectangularObjectBase_pdx_PairRef pdx_zero( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final verdantium.standard.pdx_DrawingRectangularObjectBase_pdx_ObjectRef in ) {
        final verdantium.standard.pdx_DrawingRectangularObjectBase_pdx_PairRef tx = new verdantium.standard.pdx_DrawingRectangularObjectBase_pdx_PairRef( in , pdx_thismilieu );
        final jundo.runtime.ExtMilieuRef t0 = tx.getMilieu();
        final jundo.runtime.ExtMilieuRef t1 = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "myRect" , t0 , null );
        final jundo.runtime.ExtMilieuRef t2 = jundo.runtime.Runtime.asgDoubleObjectMember( tx.getObject() , "basicStrokeWidth" , t1 , 0 );
        final jundo.runtime.ExtMilieuRef t3 = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "myRct" , t2 , null );
        final jundo.runtime.ExtMilieuRef t4 = jundo.runtime.Runtime.asgIntObjectMember( tx.getObject() , "renderingMode" , t3 , 0 );
        final jundo.runtime.ExtMilieuRef t5 = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "myStroke" , t4 , null );
        final jundo.runtime.ExtMilieuRef t6 = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "myEll" , t5 , null );
        final jundo.runtime.ExtMilieuRef t7 = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "lineColor" , t6 , null );
        final jundo.runtime.ExtMilieuRef t8 = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "myRound" , t7 , null );
        final jundo.runtime.ExtMilieuRef t9 = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "myLine" , t8 , null );
        final jundo.runtime.ExtMilieuRef t10 = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "fillColor" , t9 , null );
        final verdantium.standard.pdx_DrawingRectangularObjectBase_pdx_PairRef z = new verdantium.standard.pdx_DrawingRectangularObjectBase_pdx_PairRef( in , t10 );
        return( z ); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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
        DrawingRectangularObject dobj = new DrawingRectangularObject();
        dobj.setLineColor( (Color)( pdxm_getLineColor( mil ) ) );
        dobj.setFillColor( (Color)( pdxm_getFillColor( mil ) ) );
        dobj.setRectangle( new Rectangle( (Rectangle)( pdxm_getMyRect( mil ) ) ) );
        double strokeWidth = pdxm_getBasicStrokeWidth( mil );
        if( strokeWidth >= 0.0 ) {
            dobj.setBasicStroke( strokeWidth );
        } else {
            dobj.setStroke( (Stroke)( pdxm_getMyStroke( mil ) ) );
        }
        dobj.setRenderingMode( pdxm_getRenderingMode( mil ) );
        return( dobj );
    }
    
    @Override
    public java.lang.Object pdxm_clickedInRegion( final jundo.runtime.ExtMilieuRef mil , final java.lang.Object  _thePort , final int  toolMode , final java.lang.Object  _inPoint ) {
        
        APPRec NewRec = new APPRec();
        APPRec ret = null;
        double Priority = ClickRec.MIN_PRIORITY + 1;
        int LastClick = DrawingRectangularObject.NO_MATCH;
        DrawApp thePort = (DrawApp)( _thePort );
        Point2D.Double InPt = (Point2D.Double)( _inPoint );
        Rectangle rect = (Rectangle)( pdxm_getMyRect( mil ) );
        
        if ( pdxm_getRenderingMode( mil ) == DrawApp.LINE_MODE) {
            Priority =
                    thePort.defaultGravityField(InPt, rect.getX(), rect.getY());
            if ((Priority <= ClickRec.MIN_PRIORITY) && (LastClick == DrawingRectangularObject.NO_MATCH)) {
                NewRec.setValue(DrawingRectangularObject.MANUAL_DRAG_P1);
                NewRec.setXCoord(rect.getX() + rect.getWidth());
                NewRec.setYCoord(rect.getY() + rect.getHeight());
                NewRec.clickPriority = Priority;
                ret = NewRec;
                LastClick = DrawingRectangularObject.MATCH;
            }
            
            Priority =
                    thePort.defaultGravityField(
                    InPt,
                    rect.getX() + rect.getWidth(),
                    rect.getY() + rect.getHeight());
            if ((Priority <= ClickRec.MIN_PRIORITY) && (LastClick == DrawingRectangularObject.NO_MATCH)) {
                NewRec.setValue(DrawingRectangularObject.MANUAL_DRAG_P2);
                NewRec.setXCoord(rect.getX());
                NewRec.setYCoord(rect.getY());
                NewRec.clickPriority = Priority;
                ret = NewRec;
                LastClick = DrawingRectangularObject.MATCH;
            }
            
        } else {
            Priority =
                    thePort.defaultGravityField(InPt, rect.getX(), rect.getY());
            if ((Priority <= ClickRec.MIN_PRIORITY) && (LastClick == DrawingRectangularObject.NO_MATCH)) {
                NewRec.setValue(DrawingRectangularObject.MANUAL_DRAG_P1);
                NewRec.setXCoord(rect.getX() + rect.getWidth());
                NewRec.setYCoord(rect.getY() + rect.getHeight());
                NewRec.clickPriority = Priority;
                ret = NewRec;
                LastClick = DrawingRectangularObject.MATCH;
            }
            
            Priority =
                    thePort.defaultGravityField(
                    InPt,
                    rect.getX() + rect.getWidth(),
                    rect.getY() + rect.getHeight());
            if ((Priority <= ClickRec.MIN_PRIORITY) && (LastClick == DrawingRectangularObject.NO_MATCH)) {
                NewRec.setValue(DrawingRectangularObject.MANUAL_DRAG_P2);
                NewRec.setXCoord(rect.getX());
                NewRec.setYCoord(rect.getY());
                NewRec.clickPriority = Priority;
                ret = NewRec;
                LastClick = DrawingRectangularObject.MATCH;
            }
            
            Priority =
                    thePort.defaultGravityField(
                    InPt,
                    rect.getX() + rect.getWidth(),
                    rect.getY());
            if ((Priority <= ClickRec.MIN_PRIORITY) && (LastClick == DrawingRectangularObject.NO_MATCH)) {
                NewRec.setValue(DrawingRectangularObject.MANUAL_DRAG_P3);
                NewRec.setXCoord(rect.getX());
                NewRec.setYCoord(rect.getY() + rect.getHeight());
                NewRec.clickPriority = Priority;
                ret = NewRec;
                LastClick = DrawingRectangularObject.MATCH;
            }
            
            Priority =
                    thePort.defaultGravityField(
                    InPt,
                    rect.getX(),
                    rect.getY() + rect.getHeight());
            if ((Priority <= ClickRec.MIN_PRIORITY) && (LastClick == DrawingRectangularObject.NO_MATCH)) {
                NewRec.setValue(DrawingRectangularObject.MANUAL_DRAG_P4);
                NewRec.setXCoord(rect.getX() + rect.getWidth());
                NewRec.setYCoord(rect.getY());
                NewRec.clickPriority = Priority;
                ret = NewRec;
                LastClick = DrawingRectangularObject.MATCH;
            }
            
        }
        
        return ret;
    }
    
    @Override
    public jundo.runtime.ExtMilieuRef pdxm_draw( final jundo.runtime.ExtMilieuRef mil , final java.lang.Object  thePort , final java.lang.Object _g , final int  toolMode ) {
        final Graphics2D g = (Graphics2D) _g;
        switch ( pdxm_getRenderingMode( mil ) ) {
            case DrawApp.LINE_MODE :
                g.setStroke((Stroke)(pdxm_getMyStroke(mil)));
                g.setColor((Color)(pdxm_getLineColor(mil)));
                g.draw((Line2D.Float)(pdxm_getMyLine(mil)));
                break;
                
            case DrawApp.RECT_MODE :
                g.setStroke((Stroke)(pdxm_getMyStroke(mil)));
                g.setColor((Color)(pdxm_getLineColor(mil)));
                g.draw((Rectangle2D.Float)(pdxm_getMyRct(mil)));
                break;
                
            case DrawApp.FILLED_RECT_MODE :
                g.setStroke((Stroke)(pdxm_getMyStroke(mil)));
                g.setColor((Color)(pdxm_getFillColor(mil)));
                g.fill((Rectangle2D.Float)(pdxm_getMyRct(mil)));
                g.setColor((Color)(pdxm_getLineColor(mil)));
                g.draw((Rectangle2D.Float)(pdxm_getMyRct(mil)));
                break;
                
            case DrawApp.ROUND_RECT_MODE :
                g.setStroke((Stroke)(pdxm_getMyStroke(mil)));
                g.setColor((Color)(pdxm_getLineColor(mil)));
                g.draw((RoundRectangle2D.Float)(pdxm_getMyRound(mil)));
                break;
                
            case DrawApp.FILLED_ROUND_RECT_MODE :
                g.setStroke((Stroke)(pdxm_getMyStroke(mil)));
                g.setColor((Color)(pdxm_getFillColor(mil)));
                g.fill((RoundRectangle2D.Float)(pdxm_getMyRound(mil)));
                g.setColor((Color)(pdxm_getLineColor(mil)));
                g.draw((RoundRectangle2D.Float)(pdxm_getMyRound(mil)));
                break;
                
            case DrawApp.OVAL_MODE :
                g.setStroke((Stroke)(pdxm_getMyStroke(mil)));
                g.setColor((Color)(pdxm_getLineColor(mil)));
                g.draw((Ellipse2D.Float)(pdxm_getMyEll(mil)));
                break;
                
            case DrawApp.FILLED_OVAL_MODE :
                g.setStroke((Stroke)(pdxm_getMyStroke(mil)));
                g.setColor((Color)(pdxm_getFillColor(mil)));
                g.fill((Ellipse2D.Float)(pdxm_getMyEll(mil)));
                g.setColor((Color)(pdxm_getLineColor(mil)));
                g.draw((Ellipse2D.Float)(pdxm_getMyEll(mil)));
                break;
                
        }
        
        return( mil );
    }
    
    @Override
    public jundo.runtime.ExtMilieuRef pdxm_drawTools( final jundo.runtime.ExtMilieuRef mil , final java.lang.Object  _thePort , final java.lang.Object  _g , final int  toolMode ) {
        final Graphics2D g = (Graphics2D) _g;
        final DrawApp thePort = (DrawApp) _thePort;
        if ( pdxm_getRenderingMode( mil ) == DrawApp.LINE_MODE) {
            Rectangle rect = (Rectangle)( pdxm_getMyRect( mil ) );
            Rectangle2D.Double r1 =
                    thePort.instanceRect(rect.getX(), rect.getY());
            Rectangle2D.Double r2 =
                    thePort.instanceRect(
                    rect.getX() + rect.getWidth(),
                    rect.getY() + rect.getHeight());
            if( toolMode != DrawApp.ErASE_MODE )
                g.setColor(Color.cyan);
            else g.setColor(Color.orange);
            g.fill(r1);
            g.fill(r2);
        } else {
            Rectangle rect = (Rectangle)( pdxm_getMyRect( mil ) );
            Rectangle2D.Double r1 =
                    thePort.instanceRect(rect.getX(), rect.getY());
            Rectangle2D.Double r2 =
                    thePort.instanceRect(
                    rect.getX() + rect.getWidth(),
                    rect.getY() + rect.getHeight());
            Rectangle2D.Double r3 =
                    thePort.instanceRect(
                    rect.getX() + rect.getWidth(),
                    rect.getY());
            Rectangle2D.Double r4 =
                    thePort.instanceRect(
                    rect.getX(),
                    rect.getY() + rect.getHeight());
            if( toolMode != DrawApp.ErASE_MODE )
                g.setColor(Color.cyan);
            else g.setColor(Color.orange);
            g.fill(r1);
            g.fill(r2);
            g.fill(r3);
            g.fill(r4);
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
    public static verdantium.standard.pdx_DrawingRectangularObject_pdx_PairRef createOID_DrawingRectangularObject( jundo.runtime.ExtMilieuRef inMil ) {
        jundo.runtime.ExtMilieuRef newMil = jundo.runtime.Runtime.createOIDmilieu( inMil );
        int NewID = newMil.getMaxID();
        jundo.runtime.KeyRef MyKey = jundo.runtime.Runtime.getKeyRef( NewID );
        pdx_DrawingRectangularObject_pdx_ObjectRef NewObj =
                new pdx_DrawingRectangularObject_pdx_ObjectRef( MyKey );
        verdantium.standard.pdx_DrawingRectangularObject_pdx_PairRef ret = new verdantium.standard.pdx_DrawingRectangularObject_pdx_PairRef( NewObj , newMil );
        return( ret );
    }
    
    /**
     * Constructs the object.
     * @param pdx_thismilieu The input milieu.
     * @return The pair representing the created object.
     */
    public static verdantium.standard.pdx_DrawingRectangularObject_pdx_PairRef pdxm_new_DrawingRectangularObject( final jundo.runtime.ExtMilieuRef pdx_thismilieu ) {
        final verdantium.standard.pdx_DrawingRectangularObject_pdx_PairRef t1 = pdx_DrawingRectangularObject_pdx_ObjectRef.createOID_DrawingRectangularObject( pdx_thismilieu );
        final verdantium.standard.pdx_DrawingRectangularObjectBase_pdx_PairRef t2 = pdx_zero( t1.getMilieu() , (verdantium.standard.pdx_DrawingRectangularObject_pdx_ObjectRef)( t1.getObject() ) );
        final verdantium.standard.pdx_DrawingRectangularObject_pdx_PairRef t3 = new verdantium.standard.pdx_DrawingRectangularObject_pdx_PairRef(
                (verdantium.standard.pdx_DrawingRectangularObject_pdx_ObjectRef)( t2.getObject() ) , t2.getMilieu() );
        
        return( t3 );
    }
    
    /**
     * Constructs a new object for persistence purposes.
     * @param pdx_thismilieu The input milieu.
     * @return The pair representing the created object.
     */
    public static verdantium.standard.pdx_DrawingRectangularObject_pdx_PairRef pdxm_newPersist( final jundo.runtime.ExtMilieuRef pdx_thismilieu ) {
        final verdantium.standard.pdx_DrawingRectangularObject_pdx_PairRef t1 = pdx_DrawingRectangularObject_pdx_ObjectRef.createOID_DrawingRectangularObject( pdx_thismilieu );
        final verdantium.standard.pdx_DrawingRectangularObjectBase_pdx_PairRef t2 = pdx_zero( t1.getMilieu() , (verdantium.standard.pdx_DrawingRectangularObject_pdx_ObjectRef)( t1.getObject() ) );
        final verdantium.standard.pdx_DrawingRectangularObject_pdx_PairRef t3 = new verdantium.standard.pdx_DrawingRectangularObject_pdx_PairRef(
                (verdantium.standard.pdx_DrawingRectangularObject_pdx_ObjectRef)( t2.getObject() ) , t2.getMilieu() );
        
        return( t3 );
    }
    
    
    /**
    * Writes the object to persistent storage.  @serialData TBD.
    */
    public umeta.pdx_VersionBuffer_pdx_PairRef pdxm_writeObject( final jundo.runtime.ExtMilieuRef pdx_thismilieu ) {
        pdx_VersionBuffer_pdx_PairRef initial = pdx_VersionBuffer_pdx_ObjectRef.pdxm_new_VersionBuffer(pdx_thismilieu);
        jundo.runtime.ExtMilieuRef mil = initial.getMilieu();
        pdx_VersionBuffer_pdx_ObjectRef myv = (pdx_VersionBuffer_pdx_ObjectRef)( initial.getObject() );

        
        mil = myv.pdxm_setInt( mil , "RenderingMode" , pdxm_getRenderingMode( mil ) );
        mil = myv.pdxm_setJobjProperty(mil, "LineColor", pdxm_getLineColor( mil ) );
	mil = myv.pdxm_setJobjProperty(mil, "FillColor", pdxm_getFillColor( mil ) );
        mil = myv.pdxm_setJobjProperty(mil, "MyRect", pdxm_getMyRect( mil ) );

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
	* Sets the rendering mode indicating what kind of object is to be rendered.
	* This mode can take on the values of either {@link DrawApp#LINE_MODE},
	* {@link DrawApp#RECT_MODE}, {@link DrawApp#FILLED_RECT_MODE}, {@link DrawApp#ROUND_RECT_MODE},
	* {@link DrawApp#FILLED_ROUND_RECT_MODE}, {@link DrawApp#OVAL_MODE}, or {@link DrawApp#FILLED_OVAL_MODE}.
	* @param _mil The input milieu.
	* @param in The input rendering mode.
	* @return The resulting milieu.
	*/
	public jundo.runtime.ExtMilieuRef installRenderingMode(jundo.runtime.ExtMilieuRef _mil , int in) {
		jundo.runtime.ExtMilieuRef mil = _mil;
                mil = pdxm_setRenderingMode( mil ,  in );
                Rectangle myRect = (Rectangle)( pdxm_getMyRect( mil ) );

		switch ( pdxm_getRenderingMode( mil ) ) {
			case DrawApp.LINE_MODE :
				if ( pdxm_getMyLine( mil ) == null)
					mil = pdxm_setMyLine( mil , new Line2D.Float() );
				( (Line2D.Float)( pdxm_getMyLine( mil ) ) ).setLine(
					myRect.x,
					myRect.y,
					myRect.x + myRect.width,
					myRect.y + myRect.height);
				break;

			case DrawApp.RECT_MODE :
			case DrawApp.FILLED_RECT_MODE :
				if ( pdxm_getMyRct( mil ) == null)
					mil = pdxm_setMyRct( mil , new Rectangle2D.Float() );
				int x = Math.min(myRect.x, myRect.x + myRect.width);
				int y = Math.min(myRect.y, myRect.y + myRect.height);
				( (Rectangle2D.Float)( pdxm_getMyRct( mil ) ) ).setRect(
					x,
					y,
					Math.abs(myRect.width),
					Math.abs(myRect.height));
				break;

			case DrawApp.ROUND_RECT_MODE :
			case DrawApp.FILLED_ROUND_RECT_MODE :
				if ( pdxm_getMyRound( mil ) == null)
					mil = pdxm_setMyRound( mil , new RoundRectangle2D.Float() );
				x = Math.min(myRect.x, myRect.x + myRect.width);
				y = Math.min(myRect.y, myRect.y + myRect.height);
				( (RoundRectangle2D.Float)( pdxm_getMyRound( mil ) ) ).setRoundRect(
					x,
					y,
					Math.abs(myRect.width),
					Math.abs(myRect.height),
					20,
					20);
				break;

			case DrawApp.OVAL_MODE :
			case DrawApp.FILLED_OVAL_MODE :
				if ( pdxm_getMyEll( mil ) == null)
					mil = pdxm_setMyEll( mil , new Ellipse2D.Float() );
				x = Math.min(myRect.x, myRect.x + myRect.width);
				y = Math.min(myRect.y, myRect.y + myRect.height);
				( (Ellipse2D.Float)( pdxm_getMyEll( mil ) ) ).setFrame(
					x,
					y,
					Math.abs(myRect.width),
					Math.abs(myRect.height));
				break;

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
        
        mil = pdxm_setLineColor( mil ,  (Color)( myv.pdxm_getJobjProperty(mil, "LineColor") ) );
        pdx_VersionBuffer_pdx_ObjectRef.pdxm_chkJobjNul( mil , pdxm_getLineColor( mil ) );
        mil = pdxm_setFillColor( mil ,  (Color)( myv.pdxm_getJobjProperty(mil, "FillColor") ) );
        pdx_VersionBuffer_pdx_ObjectRef.pdxm_chkJobjNul( mil , pdxm_getFillColor( mil ) );
        mil = pdxm_setMyRect( mil ,  (Rectangle)( myv.pdxm_getJobjProperty(mil, "MyRect") ) );
        pdx_VersionBuffer_pdx_ObjectRef.pdxm_chkJobjNul( mil , pdxm_getMyRect( mil ) );
                        
	mil = installRenderingMode( mil , myv.pdxm_getInt(mil, "RenderingMode") );

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
                        
        return( mil );
    }
    
    
    
}



