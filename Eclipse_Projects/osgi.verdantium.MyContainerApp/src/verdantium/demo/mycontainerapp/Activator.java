package verdantium.demo.mycontainerapp;

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
		
		ClasspathManager.addClass( MyContainerApp.class );
		
		ComponentManager.addClass( MyContainerApp.class , "My Container App" );
		
		ComponentManager.addClass( ScrollingMyContainerApp.class , "Scrolling My Container App" );
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		
		ClasspathManager.removeClass( MyContainerApp.class );
		
		ComponentManager.removeClass( MyContainerApp.class , "My Container App" );
		
		ComponentManager.removeClass( ScrollingMyContainerApp.class , "Scrolling My Container App" );
		
	}

}

