
/**************
* This source file was generated by JUndo compiler version 051002A.
* JUndo is a declarative object-oriented programming language with 
* functional, and intensional programming characteristics.
* See http://sourceforge.net/projects/jundo
* Do not modify this file directly.  Instead, modify the .JUndo file
* and recompile.  See the associated .JUndo file for license and
* copyright information.
* This File Generated : Fri Oct 28 21:39:12 MDT 2005
* From Input File : ImageViewerModel.JUndo
**************/

package verdantium.standard.data;

public class pdx_ImageViewerModel_pdx_ObjectRef extends jundo.runtime.ExtObjectRef
	{
protected pdx_ImageViewerModel_pdx_ObjectRef( jundo.runtime.KeyRef Key )
{
super( Key );
}

public static verdantium.standard.data.pdx_ImageViewerModel_pdx_PairRef createOID_ImageViewerModel( jundo.runtime.ExtMilieuRef InMil )
{
jundo.runtime.ExtMilieuRef NewMil = jundo.runtime.Runtime.createOIDmilieu( InMil );
int NewID = NewMil.getMaxID();
jundo.runtime.KeyRef MyKey = jundo.runtime.Runtime.getKeyRef( NewID );
pdx_ImageViewerModel_pdx_ObjectRef NewObj = 
	new pdx_ImageViewerModel_pdx_ObjectRef( MyKey );
verdantium.standard.data.pdx_ImageViewerModel_pdx_PairRef ret = new verdantium.standard.data.pdx_ImageViewerModel_pdx_PairRef( NewObj , NewMil );
return( ret );
}
	public static verdantium.standard.data.pdx_ImageViewerModel_pdx_PairRef pdxm_new_ImageViewerModel( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final int  displayMode , final int  imageMode , final java.lang.Object  image , final java.lang.Object  imageBytes )
{
final verdantium.standard.data.pdx_ImageViewerModel_pdx_PairRef pdx_tmp_4062 = verdantium.standard.data.pdx_ImageViewerModel_pdx_ObjectRef.pdxm_allocate_ImageViewerModel( ( pdx_thismilieu ) );
final verdantium.standard.data.pdx_ImageViewerModel_pdx_ObjectRef pdx_tmp_4060 = (verdantium.standard.data.pdx_ImageViewerModel_pdx_ObjectRef)( pdx_tmp_4062.getObject() );
final jundo.runtime.ExtMilieuRef pdx_tmp_4061 = pdx_tmp_4062.getMilieu();
final jundo.runtime.ExtMilieuRef pdx_tmp_4380 = pdx_tmp_4060.pdxm_setDisplayMode( pdx_tmp_4061 , ( displayMode ) );
final jundo.runtime.ExtMilieuRef pdx_tmp_4620 = pdx_tmp_4060.pdxm_setImageData( pdx_tmp_4380 , ( imageMode ) , ( image ) , ( imageBytes ) );
return( new verdantium.standard.data.pdx_ImageViewerModel_pdx_PairRef( pdx_tmp_4060 , ( pdx_tmp_4620 ) ) );
}
	public static verdantium.standard.data.pdx_ImageViewerModel_pdx_PairRef pdxm_allocate_ImageViewerModel( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final verdantium.standard.data.pdx_ImageViewerModel_pdx_PairRef t1 = pdx_ImageViewerModel_pdx_ObjectRef.createOID_ImageViewerModel( pdx_thismilieu );
final verdantium.standard.data.pdx_ImageViewerModel_pdx_PairRef t2 = pdx_zero( t1.getMilieu() , (verdantium.standard.data.pdx_ImageViewerModel_pdx_ObjectRef)( t1.getObject() ) );

return( t2 );
}
	public static verdantium.standard.data.pdx_ImageViewerModel_pdx_PairRef pdx_zero( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final verdantium.standard.data.pdx_ImageViewerModel_pdx_ObjectRef in )
{
final verdantium.standard.data.pdx_ImageViewerModel_pdx_PairRef tx = new verdantium.standard.data.pdx_ImageViewerModel_pdx_PairRef( in , pdx_thismilieu );
final jundo.runtime.ExtMilieuRef t0 = tx.getMilieu();
final jundo.runtime.ExtMilieuRef t1 = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "image" , t0 , null );
final jundo.runtime.ExtMilieuRef t2 = jundo.runtime.Runtime.asgIntObjectMember( tx.getObject() , "displayMode" , t1 , 0 );
final jundo.runtime.ExtMilieuRef t3 = jundo.runtime.Runtime.asgIntObjectMember( tx.getObject() , "imageMode" , t2 , 0 );
final jundo.runtime.ExtMilieuRef t4 = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "imageBytes" , t3 , null );
final verdantium.standard.data.pdx_ImageViewerModel_pdx_PairRef z = new verdantium.standard.data.pdx_ImageViewerModel_pdx_PairRef( in , t4 );
return( z );
}
	public static void pdx_initClassMembers( )
{
}

	static
{
pdx_initClassMembers();
}

	public java.lang.Object pdx_ObjectMemberAccess_pdx_getimage( final jundo.runtime.ExtMilieuRef mil )
{
return( jundo.runtime.Runtime.getJobjObjectMember( this , "image" , mil ) );
}
	public jundo.runtime.ExtMilieuRef pdx_ObjectMemberAccess_pdx_asgimage( final jundo.runtime.ExtMilieuRef mil , final java.lang.Object val )
{
return( jundo.runtime.Runtime.asgJobjObjectMember( this , "image" , mil , val ) );
}
	public int pdx_ObjectMemberAccess_pdx_getdisplayMode( final jundo.runtime.ExtMilieuRef mil )
{
return( jundo.runtime.Runtime.getIntObjectMember( this , "displayMode" , mil ) );
}
	public jundo.runtime.ExtMilieuRef pdx_ObjectMemberAccess_pdx_asgdisplayMode( final jundo.runtime.ExtMilieuRef mil , final int val )
{
return( jundo.runtime.Runtime.asgIntObjectMember( this , "displayMode" , mil , val ) );
}
	public int pdx_ObjectMemberAccess_pdx_getimageMode( final jundo.runtime.ExtMilieuRef mil )
{
return( jundo.runtime.Runtime.getIntObjectMember( this , "imageMode" , mil ) );
}
	public jundo.runtime.ExtMilieuRef pdx_ObjectMemberAccess_pdx_asgimageMode( final jundo.runtime.ExtMilieuRef mil , final int val )
{
return( jundo.runtime.Runtime.asgIntObjectMember( this , "imageMode" , mil , val ) );
}
	public java.lang.Object pdx_ObjectMemberAccess_pdx_getimageBytes( final jundo.runtime.ExtMilieuRef mil )
{
return( jundo.runtime.Runtime.getJobjObjectMember( this , "imageBytes" , mil ) );
}
	public jundo.runtime.ExtMilieuRef pdx_ObjectMemberAccess_pdx_asgimageBytes( final jundo.runtime.ExtMilieuRef mil , final java.lang.Object val )
{
return( jundo.runtime.Runtime.asgJobjObjectMember( this , "imageBytes" , mil , val ) );
}
	public jundo.runtime.ExtMilieuRef pdxm_setImage( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final java.lang.Object  in )
{
final jundo.runtime.ExtMilieuRef pdx_tmp_1860 = ( this ).pdx_ObjectMemberAccess_pdx_asgimage( pdx_thismilieu , ( in ) );
return( pdx_tmp_1860 );
}
	public jundo.runtime.ExtMilieuRef pdxm_setImageBytes( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final java.lang.Object  in )
{
final jundo.runtime.ExtMilieuRef pdx_tmp_2340 = ( this ).pdx_ObjectMemberAccess_pdx_asgimageBytes( pdx_thismilieu , ( in ) );
return( pdx_tmp_2340 );
}
	public jundo.runtime.ExtMilieuRef pdxm_setImageMode( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final int  in )
{
final jundo.runtime.ExtMilieuRef pdx_tmp_1380 = ( this ).pdx_ObjectMemberAccess_pdx_asgimageMode( pdx_thismilieu , ( in ) );
return( pdx_tmp_1380 );
}
	public jundo.runtime.ExtMilieuRef pdxm_setImageData( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final int  imageMode , final java.lang.Object  image , final java.lang.Object  imageBytes )
{
final jundo.runtime.ExtMilieuRef pdx_tmp_2840 = ( this ).pdx_ObjectMemberAccess_pdx_asgimageMode( ( pdx_thismilieu ) , ( imageMode ) );
final jundo.runtime.ExtMilieuRef pdx_tmp_2960 = ( this ).pdx_ObjectMemberAccess_pdx_asgimage( pdx_tmp_2840 , ( image ) );
final jundo.runtime.ExtMilieuRef pdx_tmp_3080 = ( this ).pdx_ObjectMemberAccess_pdx_asgimageBytes( pdx_tmp_2960 , ( imageBytes ) );
return( ( pdx_tmp_3080 ) );
}
	public jundo.runtime.ExtMilieuRef pdxm_setDisplayMode( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final int  in )
{
final jundo.runtime.ExtMilieuRef pdx_tmp_900 = ( this ).pdx_ObjectMemberAccess_pdx_asgdisplayMode( pdx_thismilieu , ( in ) );
return( pdx_tmp_900 );
}
	public java.lang.Object pdxm_getImage( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final java.lang.Object pdx_tmp_1620 = ( this ).pdx_ObjectMemberAccess_pdx_getimage( pdx_thismilieu );
return( pdx_tmp_1620 );
}
	public int pdxm_getDisplayMode( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final int pdx_tmp_660 = ( this ).pdx_ObjectMemberAccess_pdx_getdisplayMode( pdx_thismilieu );
return( pdx_tmp_660 );
}
	public java.lang.Object pdxm_getImageBytes( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final java.lang.Object pdx_tmp_2100 = ( this ).pdx_ObjectMemberAccess_pdx_getimageBytes( pdx_thismilieu );
return( pdx_tmp_2100 );
}
	public int pdxm_getImageMode( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final int pdx_tmp_1140 = ( this ).pdx_ObjectMemberAccess_pdx_getimageMode( pdx_thismilieu );
return( pdx_tmp_1140 );
}
	}

