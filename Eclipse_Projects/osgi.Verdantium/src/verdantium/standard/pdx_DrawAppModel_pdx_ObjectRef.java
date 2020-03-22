
/**************
* This source file was generated by JUndo compiler version 070726A.
* JUndo is a declarative object-oriented programming language with 
* functional, and intensional programming characteristics.
* See http://sourceforge.net/projects/jundo
* Do not modify this file directly.  Instead, modify the .JUndo file
* and recompile.  See the associated .JUndo file for license and
* copyright information.
* This File Generated : Wed Aug 01 16:39:09 MDT 2007
* From Input File : DrawAppModel.JUndo
**************/

package verdantium.standard;

class pdx_DrawAppModel_pdx_ObjectRef extends jundo.runtime.ExtObjectRef
	{
protected pdx_DrawAppModel_pdx_ObjectRef( jundo.runtime.KeyRef Key )
{
super( Key );
}

public static verdantium.standard.pdx_DrawAppModel_pdx_PairRef createOID_DrawAppModel( jundo.runtime.ExtMilieuRef InMil )
{
jundo.runtime.ExtMilieuRef NewMil = jundo.runtime.Runtime.createOIDmilieu( InMil );
int NewID = NewMil.getMaxID();
jundo.runtime.KeyRef MyKey = jundo.runtime.Runtime.getKeyRef( NewID );
pdx_DrawAppModel_pdx_ObjectRef NewObj = 
	new pdx_DrawAppModel_pdx_ObjectRef( MyKey );
verdantium.standard.pdx_DrawAppModel_pdx_PairRef ret = new verdantium.standard.pdx_DrawAppModel_pdx_PairRef( NewObj , NewMil );
return( ret );
}
	public static verdantium.standard.pdx_DrawAppModel_pdx_PairRef pdxm_new_DrawAppModel( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final int  currentMode , final java.lang.Object  lineColor , final java.lang.Object  fillColor , final double  basicStrokeWidth , final java.lang.Object  myStroke , final int  bezMode , final boolean  chordParam )
{
final verdantium.standard.pdx_DrawAppModel_pdx_PairRef pdx_tmp_5662 = verdantium.standard.pdx_DrawAppModel_pdx_ObjectRef.pdxm_allocate_DrawAppModel( ( pdx_thismilieu ) );
final verdantium.standard.pdx_DrawAppModel_pdx_ObjectRef pdx_tmp_5660 = (verdantium.standard.pdx_DrawAppModel_pdx_ObjectRef)( pdx_tmp_5662.getObject() );
final jundo.runtime.ExtMilieuRef pdx_tmp_5661 = pdx_tmp_5662.getMilieu();
final jundo.runtime.ExtMilieuRef pdx_tmp_5980 = pdx_tmp_5660.pdxm_setCurrentMode( pdx_tmp_5661 , ( currentMode ) );
final jundo.runtime.ExtMilieuRef pdx_tmp_6140 = pdx_tmp_5660.pdxm_setLineColor( pdx_tmp_5980 , ( lineColor ) );
final jundo.runtime.ExtMilieuRef pdx_tmp_6300 = pdx_tmp_5660.pdxm_setFillColor( pdx_tmp_6140 , ( fillColor ) );
final umeta.pdx_HighLevelList_pdx_PairRef pdx_tmp_6562 = umeta.pdx_HighLevelList_pdx_ObjectRef.pdxm_new_HighLevelList( pdx_tmp_6300 );
final umeta.pdx_HighLevelList_pdx_ObjectRef pdx_tmp_6560 = (umeta.pdx_HighLevelList_pdx_ObjectRef)( pdx_tmp_6562.getObject() );
final jundo.runtime.ExtMilieuRef pdx_tmp_6561 = pdx_tmp_6562.getMilieu();
final jundo.runtime.ExtMilieuRef pdx_tmp_6780 = pdx_tmp_5660.pdxm_setDrawingList( pdx_tmp_6561 , pdx_tmp_6560 );
final jundo.runtime.ExtMilieuRef pdx_tmp_6940 = pdx_tmp_5660.pdxm_setBasicStrokeWidth( pdx_tmp_6780 , ( basicStrokeWidth ) );
final jundo.runtime.ExtMilieuRef pdx_tmp_7100 = pdx_tmp_5660.pdxm_setMyStroke( pdx_tmp_6940 , ( myStroke ) );
final jundo.runtime.ExtMilieuRef pdx_tmp_7260 = pdx_tmp_5660.pdxm_setBezMode( pdx_tmp_7100 , ( bezMode ) );
final jundo.runtime.ExtMilieuRef pdx_tmp_7420 = pdx_tmp_5660.pdxm_setChordParam( pdx_tmp_7260 , ( chordParam ) );
return( new verdantium.standard.pdx_DrawAppModel_pdx_PairRef( pdx_tmp_5660 , ( pdx_tmp_7420 ) ) );
}
	public static verdantium.standard.pdx_DrawAppModel_pdx_PairRef pdxm_allocate_DrawAppModel( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final verdantium.standard.pdx_DrawAppModel_pdx_PairRef t1 = pdx_DrawAppModel_pdx_ObjectRef.createOID_DrawAppModel( pdx_thismilieu );
final verdantium.standard.pdx_DrawAppModel_pdx_PairRef t2 = pdx_zero( t1.getMilieu() , (verdantium.standard.pdx_DrawAppModel_pdx_ObjectRef)( t1.getObject() ) );

return( t2 );
}
	public static verdantium.standard.pdx_DrawAppModel_pdx_PairRef pdx_zero( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final verdantium.standard.pdx_DrawAppModel_pdx_ObjectRef in )
{
final verdantium.standard.pdx_DrawAppModel_pdx_PairRef tx = new verdantium.standard.pdx_DrawAppModel_pdx_PairRef( in , pdx_thismilieu );
final jundo.runtime.ExtMilieuRef t0 = tx.getMilieu();
final jundo.runtime.ExtMilieuRef t1 = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "fillColor" , t0 , null );
final jundo.runtime.ExtMilieuRef t2 = jundo.runtime.Runtime.asgObjectObjectMember( tx.getObject() , "drawingList" , t1 , null );
final jundo.runtime.ExtMilieuRef t3 = jundo.runtime.Runtime.asgIntObjectMember( tx.getObject() , "bezMode" , t2 , 0 );
final jundo.runtime.ExtMilieuRef t4 = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "myStroke" , t3 , null );
final jundo.runtime.ExtMilieuRef t5 = jundo.runtime.Runtime.asgBooleanObjectMember( tx.getObject() , "chordParam" , t4 , false );
final jundo.runtime.ExtMilieuRef t6 = jundo.runtime.Runtime.asgIntObjectMember( tx.getObject() , "currentMode" , t5 , 0 );
final jundo.runtime.ExtMilieuRef t7 = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "lineColor" , t6 , null );
final jundo.runtime.ExtMilieuRef t8 = jundo.runtime.Runtime.asgDoubleObjectMember( tx.getObject() , "basicStrokeWidth" , t7 , 0 );
final verdantium.standard.pdx_DrawAppModel_pdx_PairRef z = new verdantium.standard.pdx_DrawAppModel_pdx_PairRef( in , t8 );
return( z );
}
	public static void pdx_initClassMembers( )
{
}

	static
{
pdx_initClassMembers();
}

	public java.lang.Object pdx_ObjectMemberAccess_pdx_getfillColor( final jundo.runtime.ExtMilieuRef mil )
{
return( jundo.runtime.Runtime.getJobjObjectMember( this , "fillColor" , mil ) );
}
	public jundo.runtime.ExtMilieuRef pdx_ObjectMemberAccess_pdx_asgfillColor( final jundo.runtime.ExtMilieuRef mil , final java.lang.Object val )
{
return( jundo.runtime.Runtime.asgJobjObjectMember( this , "fillColor" , mil , val ) );
}
	public umeta.pdx_HighLevelList_pdx_ObjectRef pdx_ObjectMemberAccess_pdx_getdrawingList( final jundo.runtime.ExtMilieuRef mil )
{
return( (umeta.pdx_HighLevelList_pdx_ObjectRef)( jundo.runtime.Runtime.getObjectObjectMember( this , "drawingList" , mil ) ) );
}
	public jundo.runtime.ExtMilieuRef pdx_ObjectMemberAccess_pdx_asgdrawingList( final jundo.runtime.ExtMilieuRef mil , final umeta.pdx_HighLevelList_pdx_ObjectRef val )
{
return( jundo.runtime.Runtime.asgObjectObjectMember( this , "drawingList" , mil , val ) );
}
	public int pdx_ObjectMemberAccess_pdx_getbezMode( final jundo.runtime.ExtMilieuRef mil )
{
return( jundo.runtime.Runtime.getIntObjectMember( this , "bezMode" , mil ) );
}
	public jundo.runtime.ExtMilieuRef pdx_ObjectMemberAccess_pdx_asgbezMode( final jundo.runtime.ExtMilieuRef mil , final int val )
{
return( jundo.runtime.Runtime.asgIntObjectMember( this , "bezMode" , mil , val ) );
}
	public java.lang.Object pdx_ObjectMemberAccess_pdx_getmyStroke( final jundo.runtime.ExtMilieuRef mil )
{
return( jundo.runtime.Runtime.getJobjObjectMember( this , "myStroke" , mil ) );
}
	public jundo.runtime.ExtMilieuRef pdx_ObjectMemberAccess_pdx_asgmyStroke( final jundo.runtime.ExtMilieuRef mil , final java.lang.Object val )
{
return( jundo.runtime.Runtime.asgJobjObjectMember( this , "myStroke" , mil , val ) );
}
	public boolean pdx_ObjectMemberAccess_pdx_getchordParam( final jundo.runtime.ExtMilieuRef mil )
{
return( jundo.runtime.Runtime.getBooleanObjectMember( this , "chordParam" , mil ) );
}
	public jundo.runtime.ExtMilieuRef pdx_ObjectMemberAccess_pdx_asgchordParam( final jundo.runtime.ExtMilieuRef mil , final boolean val )
{
return( jundo.runtime.Runtime.asgBooleanObjectMember( this , "chordParam" , mil , val ) );
}
	public int pdx_ObjectMemberAccess_pdx_getcurrentMode( final jundo.runtime.ExtMilieuRef mil )
{
return( jundo.runtime.Runtime.getIntObjectMember( this , "currentMode" , mil ) );
}
	public jundo.runtime.ExtMilieuRef pdx_ObjectMemberAccess_pdx_asgcurrentMode( final jundo.runtime.ExtMilieuRef mil , final int val )
{
return( jundo.runtime.Runtime.asgIntObjectMember( this , "currentMode" , mil , val ) );
}
	public java.lang.Object pdx_ObjectMemberAccess_pdx_getlineColor( final jundo.runtime.ExtMilieuRef mil )
{
return( jundo.runtime.Runtime.getJobjObjectMember( this , "lineColor" , mil ) );
}
	public jundo.runtime.ExtMilieuRef pdx_ObjectMemberAccess_pdx_asglineColor( final jundo.runtime.ExtMilieuRef mil , final java.lang.Object val )
{
return( jundo.runtime.Runtime.asgJobjObjectMember( this , "lineColor" , mil , val ) );
}
	public double pdx_ObjectMemberAccess_pdx_getbasicStrokeWidth( final jundo.runtime.ExtMilieuRef mil )
{
return( jundo.runtime.Runtime.getDoubleObjectMember( this , "basicStrokeWidth" , mil ) );
}
	public jundo.runtime.ExtMilieuRef pdx_ObjectMemberAccess_pdx_asgbasicStrokeWidth( final jundo.runtime.ExtMilieuRef mil , final double val )
{
return( jundo.runtime.Runtime.asgDoubleObjectMember( this , "basicStrokeWidth" , mil , val ) );
}
	public boolean pdxm_getChordParam( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final boolean pdx_tmp_4380 = ( this ).pdx_ObjectMemberAccess_pdx_getchordParam( pdx_thismilieu );
return( pdx_tmp_4380 );
}
	public jundo.runtime.ExtMilieuRef pdxm_setBezMode( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final int  in )
{
final jundo.runtime.ExtMilieuRef pdx_tmp_4140 = ( this ).pdx_ObjectMemberAccess_pdx_asgbezMode( pdx_thismilieu , ( in ) );
return( pdx_tmp_4140 );
}
	public umeta.pdx_HighLevelList_pdx_ObjectRef pdxm_getDrawingList( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final umeta.pdx_HighLevelList_pdx_ObjectRef pdx_tmp_2460 = ( this ).pdx_ObjectMemberAccess_pdx_getdrawingList( pdx_thismilieu );
return( pdx_tmp_2460 );
}
	public jundo.runtime.ExtMilieuRef pdxm_setDrawingList( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final umeta.pdx_HighLevelList_pdx_ObjectRef  in )
{
final jundo.runtime.ExtMilieuRef pdx_tmp_2700 = ( this ).pdx_ObjectMemberAccess_pdx_asgdrawingList( pdx_thismilieu , ( in ) );
return( pdx_tmp_2700 );
}
	public jundo.runtime.ExtMilieuRef pdxm_setChordParam( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final boolean  in )
{
final jundo.runtime.ExtMilieuRef pdx_tmp_4620 = ( this ).pdx_ObjectMemberAccess_pdx_asgchordParam( pdx_thismilieu , ( in ) );
return( pdx_tmp_4620 );
}
	public java.lang.Object pdxm_getFillColor( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final java.lang.Object pdx_tmp_1980 = ( this ).pdx_ObjectMemberAccess_pdx_getfillColor( pdx_thismilieu );
return( pdx_tmp_1980 );
}
	public jundo.runtime.ExtMilieuRef pdxm_setFillColor( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final java.lang.Object  in )
{
final jundo.runtime.ExtMilieuRef pdx_tmp_2220 = ( this ).pdx_ObjectMemberAccess_pdx_asgfillColor( pdx_thismilieu , ( in ) );
return( pdx_tmp_2220 );
}
	public java.lang.Object pdxm_getLineColor( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final java.lang.Object pdx_tmp_1500 = ( this ).pdx_ObjectMemberAccess_pdx_getlineColor( pdx_thismilieu );
return( pdx_tmp_1500 );
}
	public jundo.runtime.ExtMilieuRef pdxm_setMyStroke( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final java.lang.Object  in )
{
final jundo.runtime.ExtMilieuRef pdx_tmp_3660 = ( this ).pdx_ObjectMemberAccess_pdx_asgmyStroke( pdx_thismilieu , ( in ) );
return( pdx_tmp_3660 );
}
	public java.lang.Object pdxm_getMyStroke( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final java.lang.Object pdx_tmp_3420 = ( this ).pdx_ObjectMemberAccess_pdx_getmyStroke( pdx_thismilieu );
return( pdx_tmp_3420 );
}
	public double pdxm_getBasicStrokeWidth( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final double pdx_tmp_2940 = ( this ).pdx_ObjectMemberAccess_pdx_getbasicStrokeWidth( pdx_thismilieu );
return( pdx_tmp_2940 );
}
	public jundo.runtime.ExtMilieuRef pdxm_setCurrentMode( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final int  in )
{
final jundo.runtime.ExtMilieuRef pdx_tmp_1260 = ( this ).pdx_ObjectMemberAccess_pdx_asgcurrentMode( pdx_thismilieu , ( in ) );
return( pdx_tmp_1260 );
}
	public jundo.runtime.ExtMilieuRef pdxm_setBasicStrokeWidth( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final double  in )
{
final jundo.runtime.ExtMilieuRef pdx_tmp_3180 = ( this ).pdx_ObjectMemberAccess_pdx_asgbasicStrokeWidth( pdx_thismilieu , ( in ) );
return( pdx_tmp_3180 );
}
	public jundo.runtime.ExtMilieuRef pdxm_setLineColor( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final java.lang.Object  in )
{
final jundo.runtime.ExtMilieuRef pdx_tmp_1740 = ( this ).pdx_ObjectMemberAccess_pdx_asglineColor( pdx_thismilieu , ( in ) );
return( pdx_tmp_1740 );
}
	public int pdxm_getBezMode( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final int pdx_tmp_3900 = ( this ).pdx_ObjectMemberAccess_pdx_getbezMode( pdx_thismilieu );
return( pdx_tmp_3900 );
}
	public int pdxm_getCurrentMode( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final int pdx_tmp_1020 = ( this ).pdx_ObjectMemberAccess_pdx_getcurrentMode( pdx_thismilieu );
return( pdx_tmp_1020 );
}
	}
