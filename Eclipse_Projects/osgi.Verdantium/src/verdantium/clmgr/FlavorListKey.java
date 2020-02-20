package verdantium.clmgr;

import verdantium.VerdantiumComponent;


/**
 * Key for accessing data flavors in the flavor list.
 * 
 * Flavors are based on a simplified version of MIME (Multipurpose Internet Mail Extensions) types.
 * 
 * See https://en.wikipedia.org/wiki/Media_type
 * 
 * @author tgreen
 *
 */
public class FlavorListKey {
	
	/**
	 * Gets the native file extension.
	 * @return The native file extension.
	 */
	public String getCnative() {
		return cnative;
	}
	
	/**
	 * Gets the associated component class.
	 * @return The associated component class.
	 */
	public Class<? extends VerdantiumComponent> getClss() {
		return clss;
	}
	
	/**
	 * Gets the MIME type primary name.
	 * @return The MIME type primary name.
	 */
	public String getPrimary() {
		return primary;
	}
	
	/**
	 * Gets the MIME type secondary name.
	 * @return The MIME type secondary name.
	 */
	public String getSecondary() {
		return secondary;
	}
	
	/**
	 * Constructor.
	 * @param _clss The associated component class.
	 * @param _cnative The native file extension.
	 * @param _primary The MIME type primary name.
	 * @param _secondary The MIME type secondary name.
	 */
	public FlavorListKey( Class<? extends VerdantiumComponent> _clss , String _cnative , String _primary , String _secondary )
	{
		clss = _clss;
		cnative = _cnative;
		primary = _primary;
		secondary = _secondary;
	}
	
	@Override
	public boolean equals( Object in )
	{
		if( in instanceof FlavorListKey )
		{
			FlavorListKey flav = (FlavorListKey) in;
			return( ( flav.getClss() == getClss() ) && ( flav.getCnative().equals( getCnative() ) ) );
		}
		return( false );
	}
	
	/**
	 * The associated component class.
	 */
	Class<? extends VerdantiumComponent> clss;
	
	/**
	 * The native file extension.
	 */
	String cnative;
	
	/**
	 * The MIME type primary name.
	 */
	String primary;

	/**
	 * The MIME type secondary name.
	 */
	String secondary;
	
	

}

