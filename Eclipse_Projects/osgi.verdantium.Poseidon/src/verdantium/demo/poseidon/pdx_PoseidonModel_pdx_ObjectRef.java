
/**************
* This source file was generated by JUndo compiler version 051002A.
* JUndo is a declarative object-oriented programming language with 
* functional, and intensional programming characteristics.
* See http://sourceforge.net/projects/jundo
* Do not modify this file directly.  Instead, modify the .JUndo file
* and recompile.  See the associated .JUndo file for license and
* copyright information.
* This File Generated : Wed Dec 14 20:24:12 MST 2005
* From Input File : PoseidonModel.JUndo
**************/

package verdantium.demo.poseidon;

class pdx_PoseidonModel_pdx_ObjectRef extends jundo.runtime.ExtObjectRef
	{
protected pdx_PoseidonModel_pdx_ObjectRef( jundo.runtime.KeyRef Key )
{
super( Key );
}

public static verdantium.demo.poseidon.pdx_PoseidonModel_pdx_PairRef createOID_PoseidonModel( jundo.runtime.ExtMilieuRef InMil )
{
jundo.runtime.ExtMilieuRef NewMil = jundo.runtime.Runtime.createOIDmilieu( InMil );
int NewID = NewMil.getMaxID();
jundo.runtime.KeyRef MyKey = jundo.runtime.Runtime.getKeyRef( NewID );
pdx_PoseidonModel_pdx_ObjectRef NewObj = 
	new pdx_PoseidonModel_pdx_ObjectRef( MyKey );
verdantium.demo.poseidon.pdx_PoseidonModel_pdx_PairRef ret = new verdantium.demo.poseidon.pdx_PoseidonModel_pdx_PairRef( NewObj , NewMil );
return( ret );
}
	public static verdantium.demo.poseidon.pdx_PoseidonModel_pdx_PairRef pdxm_new_PoseidonModel( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final int  img_size , final jundo.util.array.pdx_JobjArray_pdx_ObjectRef  waves , final java.lang.Object  endColor , final java.lang.Object  midColor , final java.lang.Object  startColor )
{
final verdantium.demo.poseidon.pdx_PoseidonModel_pdx_PairRef pdx_tmp_3982 = verdantium.demo.poseidon.pdx_PoseidonModel_pdx_ObjectRef.pdxm_allocate_PoseidonModel( ( pdx_thismilieu ) );
final verdantium.demo.poseidon.pdx_PoseidonModel_pdx_ObjectRef pdx_tmp_3980 = (verdantium.demo.poseidon.pdx_PoseidonModel_pdx_ObjectRef)( pdx_tmp_3982.getObject() );
final jundo.runtime.ExtMilieuRef pdx_tmp_3981 = pdx_tmp_3982.getMilieu();
final jundo.runtime.ExtMilieuRef pdx_tmp_4300 = pdx_tmp_3980.pdxm_setResolution( pdx_tmp_3981 , ( img_size ) );
final jundo.runtime.ExtMilieuRef pdx_tmp_4460 = pdx_tmp_3980.pdxm_setWaves( pdx_tmp_4300 , ( waves ) );
final jundo.runtime.ExtMilieuRef pdx_tmp_4620 = pdx_tmp_3980.pdxm_setEndColor( pdx_tmp_4460 , ( endColor ) );
final jundo.runtime.ExtMilieuRef pdx_tmp_4780 = pdx_tmp_3980.pdxm_setMidColor( pdx_tmp_4620 , ( midColor ) );
final jundo.runtime.ExtMilieuRef pdx_tmp_4940 = pdx_tmp_3980.pdxm_setStartColor( pdx_tmp_4780 , ( startColor ) );
return( new verdantium.demo.poseidon.pdx_PoseidonModel_pdx_PairRef( pdx_tmp_3980 , ( pdx_tmp_4940 ) ) );
}
	public static verdantium.demo.poseidon.pdx_PoseidonModel_pdx_PairRef pdxm_allocate_PoseidonModel( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final verdantium.demo.poseidon.pdx_PoseidonModel_pdx_PairRef t1 = pdx_PoseidonModel_pdx_ObjectRef.createOID_PoseidonModel( pdx_thismilieu );
final verdantium.demo.poseidon.pdx_PoseidonModel_pdx_PairRef t2 = pdx_zero( t1.getMilieu() , (verdantium.demo.poseidon.pdx_PoseidonModel_pdx_ObjectRef)( t1.getObject() ) );

return( t2 );
}
	public static verdantium.demo.poseidon.pdx_PoseidonModel_pdx_PairRef pdx_zero( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final verdantium.demo.poseidon.pdx_PoseidonModel_pdx_ObjectRef in )
{
final verdantium.demo.poseidon.pdx_PoseidonModel_pdx_PairRef tx = new verdantium.demo.poseidon.pdx_PoseidonModel_pdx_PairRef( in , pdx_thismilieu );
final jundo.runtime.ExtMilieuRef t0 = tx.getMilieu();
final jundo.runtime.ExtMilieuRef t1 = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "endColor" , t0 , null );
final jundo.runtime.ExtMilieuRef t2 = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "startColor" , t1 , null );
final jundo.runtime.ExtMilieuRef t3 = jundo.runtime.Runtime.asgObjectObjectMember( tx.getObject() , "waves" , t2 , null );
final jundo.runtime.ExtMilieuRef t4 = jundo.runtime.Runtime.asgJobjObjectMember( tx.getObject() , "midColor" , t3 , null );
final jundo.runtime.ExtMilieuRef t5 = jundo.runtime.Runtime.asgIntObjectMember( tx.getObject() , "img_size" , t4 , 0 );
final verdantium.demo.poseidon.pdx_PoseidonModel_pdx_PairRef z = new verdantium.demo.poseidon.pdx_PoseidonModel_pdx_PairRef( in , t5 );
return( z );
}
	public static void pdx_initClassMembers( )
{
}

	static
{
pdx_initClassMembers();
}

	public java.lang.Object pdx_ObjectMemberAccess_pdx_getendColor( final jundo.runtime.ExtMilieuRef mil )
{
return( jundo.runtime.Runtime.getJobjObjectMember( this , "endColor" , mil ) );
}
	public jundo.runtime.ExtMilieuRef pdx_ObjectMemberAccess_pdx_asgendColor( final jundo.runtime.ExtMilieuRef mil , final java.lang.Object val )
{
return( jundo.runtime.Runtime.asgJobjObjectMember( this , "endColor" , mil , val ) );
}
	public java.lang.Object pdx_ObjectMemberAccess_pdx_getstartColor( final jundo.runtime.ExtMilieuRef mil )
{
return( jundo.runtime.Runtime.getJobjObjectMember( this , "startColor" , mil ) );
}
	public jundo.runtime.ExtMilieuRef pdx_ObjectMemberAccess_pdx_asgstartColor( final jundo.runtime.ExtMilieuRef mil , final java.lang.Object val )
{
return( jundo.runtime.Runtime.asgJobjObjectMember( this , "startColor" , mil , val ) );
}
	public jundo.util.array.pdx_JobjArray_pdx_ObjectRef pdx_ObjectMemberAccess_pdx_getwaves( final jundo.runtime.ExtMilieuRef mil )
{
return( (jundo.util.array.pdx_JobjArray_pdx_ObjectRef)( jundo.runtime.Runtime.getObjectObjectMember( this , "waves" , mil ) ) );
}
	public jundo.runtime.ExtMilieuRef pdx_ObjectMemberAccess_pdx_asgwaves( final jundo.runtime.ExtMilieuRef mil , final jundo.util.array.pdx_JobjArray_pdx_ObjectRef val )
{
return( jundo.runtime.Runtime.asgObjectObjectMember( this , "waves" , mil , val ) );
}
	public java.lang.Object pdx_ObjectMemberAccess_pdx_getmidColor( final jundo.runtime.ExtMilieuRef mil )
{
return( jundo.runtime.Runtime.getJobjObjectMember( this , "midColor" , mil ) );
}
	public jundo.runtime.ExtMilieuRef pdx_ObjectMemberAccess_pdx_asgmidColor( final jundo.runtime.ExtMilieuRef mil , final java.lang.Object val )
{
return( jundo.runtime.Runtime.asgJobjObjectMember( this , "midColor" , mil , val ) );
}
	public int pdx_ObjectMemberAccess_pdx_getimg_size( final jundo.runtime.ExtMilieuRef mil )
{
return( jundo.runtime.Runtime.getIntObjectMember( this , "img_size" , mil ) );
}
	public jundo.runtime.ExtMilieuRef pdx_ObjectMemberAccess_pdx_asgimg_size( final jundo.runtime.ExtMilieuRef mil , final int val )
{
return( jundo.runtime.Runtime.asgIntObjectMember( this , "img_size" , mil , val ) );
}
	public jundo.util.array.pdx_JobjArray_pdx_ObjectRef pdxm_getWaves( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final jundo.util.array.pdx_JobjArray_pdx_ObjectRef pdx_tmp_1380 = ( this ).pdx_ObjectMemberAccess_pdx_getwaves( pdx_thismilieu );
return( pdx_tmp_1380 );
}
	public jundo.runtime.ExtMilieuRef pdxm_setMidColor( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final java.lang.Object  in )
{
final jundo.runtime.ExtMilieuRef pdx_tmp_2580 = ( this ).pdx_ObjectMemberAccess_pdx_asgmidColor( pdx_thismilieu , ( in ) );
return( pdx_tmp_2580 );
}
	public java.lang.Object pdxm_getStartColor( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final java.lang.Object pdx_tmp_2820 = ( this ).pdx_ObjectMemberAccess_pdx_getstartColor( pdx_thismilieu );
return( pdx_tmp_2820 );
}
	public int pdxm_getResolution( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final int pdx_tmp_900 = ( this ).pdx_ObjectMemberAccess_pdx_getimg_size( pdx_thismilieu );
return( pdx_tmp_900 );
}
	public jundo.runtime.ExtMilieuRef pdxm_setStartColor( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final java.lang.Object  in )
{
final jundo.runtime.ExtMilieuRef pdx_tmp_3060 = ( this ).pdx_ObjectMemberAccess_pdx_asgstartColor( pdx_thismilieu , ( in ) );
return( pdx_tmp_3060 );
}
	public java.lang.Object pdxm_getEndColor( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final java.lang.Object pdx_tmp_1860 = ( this ).pdx_ObjectMemberAccess_pdx_getendColor( pdx_thismilieu );
return( pdx_tmp_1860 );
}
	public jundo.runtime.ExtMilieuRef pdxm_setEndColor( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final java.lang.Object  in )
{
final jundo.runtime.ExtMilieuRef pdx_tmp_2100 = ( this ).pdx_ObjectMemberAccess_pdx_asgendColor( pdx_thismilieu , ( in ) );
return( pdx_tmp_2100 );
}
	public java.lang.Object pdxm_getMidColor( final jundo.runtime.ExtMilieuRef pdx_thismilieu )
{
final java.lang.Object pdx_tmp_2340 = ( this ).pdx_ObjectMemberAccess_pdx_getmidColor( pdx_thismilieu );
return( pdx_tmp_2340 );
}
	public jundo.runtime.ExtMilieuRef pdxm_setResolution( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final int  in )
{
final jundo.runtime.ExtMilieuRef pdx_tmp_1140 = ( this ).pdx_ObjectMemberAccess_pdx_asgimg_size( pdx_thismilieu , ( in ) );
return( pdx_tmp_1140 );
}
	public jundo.runtime.ExtMilieuRef pdxm_setWaves( final jundo.runtime.ExtMilieuRef pdx_thismilieu , final jundo.util.array.pdx_JobjArray_pdx_ObjectRef  in )
{
final jundo.runtime.ExtMilieuRef pdx_tmp_1620 = ( this ).pdx_ObjectMemberAccess_pdx_asgwaves( pdx_thismilieu , ( in ) );
return( pdx_tmp_1620 );
}
	}

