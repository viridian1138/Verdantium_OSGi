package verdantium.standard.democruiser;

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
		
		ClasspathManager.addClass( DemoCruiser.class );
		
		ComponentManager.addClass( DemoCruiser.class , "Demo Cruiser" );
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		
		ClasspathManager.removeClass( DemoCruiser.class );
		
		ComponentManager.removeClass( DemoCruiser.class , "Demo Cruiser" );
		
	}

}

