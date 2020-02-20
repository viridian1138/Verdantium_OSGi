package verdantium.clmgr;

import verdantium.VerdantiumComponent;

/**
 * Key for accessing classes in the component list.
 * 
 * @author tgreen
 *
 */
public class ComponentListKey {
	
	/**
	 * Gets the human-readable name of the component.
	 * @return The human-readable name of the component.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * The component class.
	 * @return The component class.
	 */
	public Class<? extends VerdantiumComponent> getClss() {
		return clss;
	}
	
	/**
	 * Constructor.
	 * @param _clss The component class.
	 * @param _name The human-readable name of the component.
	 */
	public ComponentListKey( Class<? extends VerdantiumComponent> _clss , String _name )
	{
		clss = _clss;
		name = _name;
	}
	
	@Override
	public boolean equals( Object in )
	{
		if( in instanceof ComponentListKey )
		{
			ComponentListKey flav = (ComponentListKey) in;
			return( ( flav.getClss() == getClss() ) && ( flav.getName().equals( getName() ) ) );
		}
		return( false );
	}
	
	/**
	 * The component class.
	 */
	Class<? extends VerdantiumComponent> clss;
	
	/**
	 * The human-readable name of the component.
	 */
	String name;
	
	

}


