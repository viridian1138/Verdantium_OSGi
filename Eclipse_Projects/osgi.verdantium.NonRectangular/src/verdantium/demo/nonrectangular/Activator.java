package verdantium.demo.nonrectangular;

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
		
		ClasspathManager.addClass( NonRectangular.class );
		
		ComponentManager.addClass( NonRectangular.class , "Non Rectangular" );
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		
		ClasspathManager.removeClass( NonRectangular.class );
		
		ComponentManager.removeClass( NonRectangular.class , "Non Rectangular" );
		
	}

}

