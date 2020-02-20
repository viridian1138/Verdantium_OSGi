package verdantium.clmgr;

import java.util.HashSet;

import meta.Meta;
import verdantium.ProgramDirector;


/**
 * Manages the classpath for Verdantium.
 * 
 * @author tgreen
 *
 */
public class ClasspathManager {
	
	/**
	 * The set of classes contributing to the classpath.
	 */
	private final static HashSet<Class<?>> pathMap = new HashSet<Class<?>>();
	
	/**
	 * Returns a clone of the set of classes contributing to the classpath.
	 * @return A clone of the set of classes contributing to the classpath.
	 */
	public static synchronized HashSet<Class<?>> getSetClone()
	{
		return (HashSet<Class<?>>) ( pathMap.clone() );
	}
	
	/**
	 * Adds a class to the classpath.
	 * @param clss The class to be added.
	 */
	public static synchronized void addClass( Class<?> clss )
	{
		pathMap.add( clss );
		updateClasspath();
		System.out.println( "Add class " + clss );
	}
	
	/**
	 * Removes a class from the classpath.
	 * @param clss The class to be removed.
	 */
	public static synchronized void removeClass( Class<?> clss )
	{
		pathMap.remove( clss );
		updateClasspath();
		System.out.println( "Remove class " + clss );
	}

	/**
	 * Updates the composite classpath.
	 */
	private static synchronized void updateClasspath()
	{
		HashSet<Class<?>> s = getSetClone();
		s.add( ProgramDirector.class );
		ClassLoader cl = new CompositeClassLoader( s );
		Meta.setDefaultClassLoader( cl ); // !!!!!!!!!!!!!!!!!! Make this thread-safe in future !!!!!!!!!!!!!!!!!
	}

	
}

