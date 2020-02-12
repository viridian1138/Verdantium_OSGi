package verdantium.demo.poseidon;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import verdantium.clmgr.ClasspathManager;
import verdantium.clmgr.ComponentManager;



public class Activator implements BundleActivator {

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		
		ClasspathManager.addClass( Poseidon.class );
		
		ComponentManager.addClass( Poseidon.class , "Poseidon" );
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		
		ClasspathManager.removeClass( Poseidon.class );
		
		ComponentManager.removeClass( Poseidon.class , "Poseidon" );
		
	}

}

