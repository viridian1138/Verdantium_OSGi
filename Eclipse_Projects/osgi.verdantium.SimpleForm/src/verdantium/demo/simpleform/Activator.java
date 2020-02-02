package verdantium.demo.simpleform;

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
		
		ClasspathManager.addClass( SimpleForm.class );
		
		ComponentManager.addClass( SimpleForm.class , "Simple Form" );
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		
		ClasspathManager.removeClass( SimpleForm.class );
		
		ComponentManager.removeClass( SimpleForm.class , "Simple Form" );
		
	}

}

