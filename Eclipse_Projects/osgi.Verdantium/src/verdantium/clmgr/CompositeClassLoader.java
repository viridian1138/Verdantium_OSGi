package verdantium.clmgr;

import java.util.HashSet;

/**
 * A composite of several other ClassLoader instances.
 * 
 * @author tgreen
 *
 */
class CompositeClassLoader extends ClassLoader {
    
	/**
	 * The contributing ClassLoader instances.
	 */
	ClassLoader[] arr;
	
	/* public String toString()
	{
		String s1 = super.toString() + " [[ ";
		int aa = arr.length;
		int count;
		for( count = 0 ; count < aa ; count++ )
		{
			s1 = s1 + arr[ count ] + " , ";
		}
		s1 = s1 + " ]] ";
		return( s1 );
	} */
	
	/**
	 * Constructor.
	 * 
	 * @param in The classes for the contributing ClassLoader instances.
	 */
	public CompositeClassLoader( HashSet<Class<?>> in )
	{
		Class<?>[] aa = in.toArray( new Class[0] );
		int len = aa.length;
		int count;
		arr = new ClassLoader[ len ];
		for( count = 0 ; count < len ; count++ )
		{
			arr[ count ] = ( aa[ count ] ).getClassLoader();
		}
	}

	@Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        final int len = arr.length;
        int count;
        Class<?> clss = null;
        ClassNotFoundException exc = null;
        for( count = 0 ; count < len ; count++ )
        {
        	try
        	{
        		// System.out.println( "path " + arr[ count ] );
        		clss = arr[ count ].loadClass( name );
        		// System.out.println( "load " + clss );
        		if( clss != null )
        		{
        			// System.out.println( "ld " + clss.getClassLoader() );
        			return( clss );
        		}
        	}
        	catch( ClassNotFoundException ex )
        	{
        		exc = ex;
        	}
        }
        if( exc == null )
        {
        	throw( new ClassNotFoundException() );
        }
        throw( exc );
    }

}


