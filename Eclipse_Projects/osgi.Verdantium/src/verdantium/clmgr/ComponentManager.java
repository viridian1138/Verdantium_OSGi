package verdantium.clmgr;


import java.awt.datatransfer.DataFlavor;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.SwingUtilities;

import meta.WrapRuntimeException;

import verdantium.ProgramDirector;
import verdantium.TransVersionBufferFlavor;
import verdantium.VerdantiumDataFlavor;
import verdantium.VerdantiumComponent;


/**
 * Manages the set of Verdantium components, and flavor maps.
 * 
 * Flavors are based on a simplified version of MIME (Multipurpose Internet Mail Extensions) types.
 * 
 * See https://en.wikipedia.org/wiki/Media_type
 * 
 * @author tgreen
 *
 */
public class ComponentManager {
	
	/**
	 * Map of human-readable component names to component classes.
	 */
	private static TreeMap<String,Class<? extends VerdantiumComponent>> componentMap;
	
	/**
	 * Set of listeners that are run upon a change to the component set.
	 */
	private /*final*/ static HashSet<Runnable> listenerSet /*= new HashSet<Runnable>()*/;
	
	/**
	 * Gets the map of human-readable component names to component classes.
	 * @return The map of human-readable component names to component classes.
	 */
	public static TreeMap<String,Class<? extends VerdantiumComponent>> getMap()
	{
		if (inputFlavorTable == null)
			updateFlavorTables();
		return( componentMap );
	}
	
	/**
	 * Called to indicate that the set of components has changed.
	 */
	public static void touch()
	{
		componentMap = null;
	}
	
	/**
	 * Adds a component to the set.
	 * @param clss The component class.
	 * @param name The human-readable name of the component.
	 */
	public static void addClass( final Class<? extends VerdantiumComponent> clss , final String name )
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			/**
			 * Adds a component to the set.
			 */
			public void run()
			{
		componentList.add( new ComponentListKey( clss , name ) );
		inputFlavorTable = null;
		inputNativeTable = null;
		outputFlavorTable = null;
		outputNativeTable = null;
		componentMap = null;
		fireToListeners();
		System.out.println( "Added: " + clss );
			}
		} );
	}
	
	/**
	 * Removes a component from the set.
	 * @param clss The component class.
	 * @param name The human-readable name of the component.
	 */
	public static void removeClass( final Class<? extends VerdantiumComponent> clss , final String name )
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			/**
			 * Removes a component from the set.
			 */
			public void run()
			{
		componentList.remove( new ComponentListKey( clss , name ) );
		inputFlavorTable = null;
		inputNativeTable = null;
		outputFlavorTable = null;
		outputNativeTable = null;
		componentMap = null;
		fireToListeners();
		System.out.println( "Removed: " + clss );
			}
		} );
	}
	
	/**
	 * Adds an input (read) data flavor.
	 * @param ckey The associated component class.
	 * @param extName The native file extension.
	 * @param mimePrimary The MIME type primary name.
	 * @param mimeSecondary The MIME type secondary name.
	 */
	public static void addInputFlavor( final Class<? extends VerdantiumComponent> ckey , final String extName , final String mimePrimary , final String mimeSecondary )
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			/**
			 * Adds an input (read) data flavor.
			 */
			public void run()
			{
		inputFlavorList.add( new FlavorListKey( ckey , extName , mimePrimary , mimeSecondary ) );
		inputFlavorTable = null;
		inputNativeTable = null;
		outputFlavorTable = null;
		outputNativeTable = null;
		componentMap = null;
		System.out.println( "added inp: " + extName );
			}
		} );
	}
	
	/**
	 * Removes an input (read) data flavor.
	 * @param ckey The associated component class.
	 * @param extName The native file extension.
	 * @param mimePrimary The MIME type primary name.
	 * @param mimeSecondary The MIME type secondary name.
	 */
	public static void removeInputFlavor( final Class<? extends VerdantiumComponent> ckey , final String extName , final String mimePrimary , final String mimeSecondary )
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			/**
			 * Removes an input (read) data flavor.
			 */
			public void run()
			{
		inputFlavorList.remove( new FlavorListKey( ckey , extName , mimePrimary , mimeSecondary ) );
		inputFlavorTable = null;
		inputNativeTable = null;
		outputFlavorTable = null;
		outputNativeTable = null;
		componentMap = null;
		System.out.println( "removed inp: " + extName );
			}
		} );
	}
	
	/**
	 * Adds an output (write) data flavor.
	 * @param ckey The associated component class.
	 * @param extName The native file extension.
	 * @param mimePrimary The MIME type primary name.
	 * @param mimeSecondary The MIME type secondary name.
	 */
	public static void addOutputFlavor( final Class<? extends VerdantiumComponent> ckey , final String extName , final String mimePrimary , final String mimeSecondary )
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			/**
			 * Adds an output (write) data flavor.
			 */
			public void run()
			{
		outputFlavorList.add( new FlavorListKey( ckey , extName , mimePrimary , mimeSecondary ) );
		inputFlavorTable = null;
		inputNativeTable = null;
		outputFlavorTable = null;
		outputNativeTable = null;
		componentMap = null;
		System.out.println( "added outp: " + extName );
			}
		} );
	}
	
	/**
	 * Removes an output (write) data flavor.
	 * @param ckey The associated component class.
	 * @param extName The native file extension.
	 * @param mimePrimary The MIME type primary name.
	 * @param mimeSecondary The MIME type secondary name.
	 */
	public static void removeOutputFlavor( final Class<? extends VerdantiumComponent> ckey , final String extName , final String mimePrimary , final String mimeSecondary )
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			/**
			 * Removes an output (write) data flavor.
			 */
			public void run()
			{
		outputFlavorList.remove( new FlavorListKey( ckey , extName , mimePrimary , mimeSecondary ) );
		inputFlavorTable = null;
		inputNativeTable = null;
		outputFlavorTable = null;
		outputNativeTable = null;
		componentMap = null;
		System.out.println( "removed outp: " + extName );
			}
		} );
	}
	
	/**
	 * Notifies listeners of a change to the component manager state.
	 */
	private static synchronized void fireToListeners()
	{
		for( final Runnable r : listenerSet )
		{
			r.run();
		}
	}
	
	/**
	* Associates an native type (file extension) with an input (read) data flavor.
	* @param nativ The native type.
	* @param The input data flavor.
	*/
	private static void addInputFlavorLink(String nativ, DataFlavor flavor) {
		int count;

		DataFlavor[] fl = inputNativeTable.get(nativ);
		if (fl == null) {
			DataFlavor[] fa = { flavor };
			inputNativeTable.put(nativ, fa);
		} else {
			DataFlavor[] fa = new DataFlavor[fl.length + 1];
			for (count = 0; count < fl.length; count++)
				fa[count] = fl[count];
			fa[fl.length] = flavor;
			inputNativeTable.put(nativ, fa);
		}

		String[] sl = inputFlavorTable.get(flavor.getMimeType());
		if (sl == null) {
			String[] sa = { nativ };
			inputFlavorTable.put(flavor.getMimeType(), sa);
		} else {
			String[] sa = new String[sl.length + 1];
			for (count = 0; count < sl.length; count++)
				sa[count] = sl[count];
			sa[sl.length] = nativ;
			inputFlavorTable.put(flavor.getMimeType(), sa);
		}
	}

	/**
	* Associates an native type (file extension) with an output (write) data flavor.
	* @param nativ The native type.
	* @param The output data flavor.
	*/
	private static void addOutputFlavorLink(String nativ, DataFlavor flavor) {
		int count;

		DataFlavor[] fl = outputNativeTable.get(nativ);
		if (fl == null) {
			DataFlavor[] fa = { flavor };
			outputNativeTable.put(nativ, fa);
		} else {
			DataFlavor[] fa = new DataFlavor[fl.length + 1];
			for (count = 0; count < fl.length; count++)
				fa[count] = fl[count];
			fa[fl.length] = flavor;
			outputNativeTable.put(nativ, fa);
		}

		String[] sl = outputFlavorTable.get(flavor.getMimeType());
		if (sl == null) {
			String[] sa = { nativ };
			outputFlavorTable.put(flavor.getMimeType(), sa);
		} else {
			String[] sa = new String[sl.length + 1];
			for (count = 0; count < sl.length; count++)
				sa[count] = sl[count];
			sa[sl.length] = nativ;
			outputFlavorTable.put(flavor.getMimeType(), sa);
		}
	}
	
	/**
	 * Static initializer.
	 */
	static
	{
		listenerSet = new HashSet<Runnable>();
		
		inputFlavorList = new ArrayList<FlavorListKey>();
		
		outputFlavorList = new ArrayList<FlavorListKey>();
		
		componentList = new ArrayList<ComponentListKey>();
		
		
		try
		{	

			addClass( verdantium.ProgramDirector.class , "Program Director" );


			addClass( verdantium.core.EditorControl.class , "Editor Control" );


			addClass( verdantium.core.DesignerControl.class , "Designer Control" );


			addClass( verdantium.core.ContainerApp.class , "Container App" );


			addClass( verdantium.core.ColorCell.class , "Color Cell" );


			addClass( verdantium.standard.WrappingScrollingTextApp.class , "Wrapping Scrolling Text App" );


			addClass( verdantium.standard.ScrollingTextApp.class , "Scrolling Text App" );


			addClass( verdantium.standard.ScrollingDrawApp.class , "Scrolling Draw App" );


			addClass( verdantium.standard.ScrollingInternalDesktop.class , "Scrolling Internal Desktop" );


			addClass( verdantium.standard.ScrollingImageViewer.class , "Scrolling Image Viewer" );


			addClass( verdantium.standard.TextApp.class , "Text App" );


			addClass( verdantium.standard.DrawApp.class , "Draw App" );


			addClass( verdantium.standard.InternalDesktop.class , "Internal Desktop" );


			addClass( verdantium.standard.ImageViewer.class , "Image Viewer" );


			addClass( verdantium.core.FileWatcher.class , "File Watcher" );


			addClass( verdantium.core.MacroRecorder.class , "Macro Recorder" );


			addClass( verdantium.standard.BeanBridge.class , "Bean Bridge" );


			addClass( verdantium.standard.HyperButton.class , "Hyper Button" );

			
			addClass( verdantium.pagewelder.PageWelder.class , "Page Welder" );


			addClass( verdantium.pagewelder.ScrollingPageWelder.class , "Scrolling Page Welder" );


			addClass( verdantium.pagewelder.PageWelderHyperButton.class , "Page Welder Hyper Button" );
			
			
			

			addInputFlavor( verdantium.ProgramDirector.class , ".txt" , "text" , "plain" );


			addInputFlavor( verdantium.ProgramDirector.class , ".text" , "text" , "plain" );


			addInputFlavor( verdantium.ProgramDirector.class , ".aif" , "audio" , "x-aiff" );


			addInputFlavor( verdantium.ProgramDirector.class , ".aiff" , "audio" , "x-aiff" );


			addInputFlavor( verdantium.ProgramDirector.class , ".aif" , "audio" , "aiff" );


			addInputFlavor( verdantium.ProgramDirector.class , ".aiff" , "audio" , "aiff" );


			addInputFlavor( verdantium.ProgramDirector.class , ".au" , "audio" , "basic" );


			addInputFlavor( verdantium.ProgramDirector.class , ".gif" , "image" , "gif" );


			addInputFlavor( verdantium.ProgramDirector.class , ".htm" , "text" , "html" );


			addInputFlavor( verdantium.ProgramDirector.class , ".html" , "text" , "html" );


			addInputFlavor( verdantium.ProgramDirector.class , ".jar" , "application" , "java-archive" );


			addInputFlavor( verdantium.ProgramDirector.class , ".jpeg" , "image" , "jpeg" );


			addInputFlavor( verdantium.ProgramDirector.class , ".jpg" , "image" , "jpeg" );


			addInputFlavor( verdantium.ProgramDirector.class , ".jpe" , "image" , "jpeg" );


			addInputFlavor( verdantium.ProgramDirector.class , ".jfif" , "image" , "jpeg" );


			addInputFlavor( verdantium.ProgramDirector.class , ".pjpeg" , "image" , "jpeg" );


			addInputFlavor( verdantium.ProgramDirector.class , ".pjp" , "image" , "jpeg" );


			addInputFlavor( verdantium.ProgramDirector.class , ".hqx" , "application" , "mac-binhex40" );


			addInputFlavor( verdantium.ProgramDirector.class , ".doc" , "application" , "msword" );


			addInputFlavor( verdantium.ProgramDirector.class , ".dot" , "application" , "msword" );


			addInputFlavor( verdantium.ProgramDirector.class , ".rtf" , "application" , "msword" );


			addInputFlavor( verdantium.ProgramDirector.class , ".wiz" , "application" , "msword" );


			addInputFlavor( verdantium.ProgramDirector.class , ".nsc" , "application" , "x-conference" );


			addInputFlavor( verdantium.ProgramDirector.class , ".pcd" , "image" , "x-photo-cd" );


			addInputFlavor( verdantium.ProgramDirector.class , ".png" , "image" , "x-png" );


			addInputFlavor( verdantium.ProgramDirector.class , ".pdf" , "application" , "pdf" );


			addInputFlavor( verdantium.ProgramDirector.class , ".mov" , "video" , "quicktime" );


			addInputFlavor( verdantium.ProgramDirector.class , ".rtf" , "application" , "rtf" );


			addInputFlavor( verdantium.ProgramDirector.class , ".tiff" , "image" , "tiff" );


			addInputFlavor( verdantium.ProgramDirector.class , ".tif" , "image" , "tiff" );


			addInputFlavor( verdantium.ProgramDirector.class , ".avi" , "video" , "avi" );


			addInputFlavor( verdantium.ProgramDirector.class , ".avi" , "video" , "x-msvideo" );


			addInputFlavor( verdantium.ProgramDirector.class , ".wav" , "audio" , "x-wav" );


			addInputFlavor( verdantium.ProgramDirector.class , ".wav" , "audio" , "wav" );


			addInputFlavor( verdantium.ProgramDirector.class , ".bmp" , "image" , "x-MS-bmp" );


			addInputFlavor( verdantium.ProgramDirector.class , ".xbm" , "image" , "x-xbitmap" );


			addInputFlavor( verdantium.ProgramDirector.class , ".zip" , "application" , "x-zip-compressed" );


			addInputFlavor( verdantium.ProgramDirector.class , ".js" , "application" , "x-javascript" );


			addInputFlavor( verdantium.ProgramDirector.class , ".mocha" , "application" , "x-javascript" );


			addInputFlavor( verdantium.ProgramDirector.class , ".geo" , "application" , "x-geometric-algebra" );


			addInputFlavor( verdantium.ProgramDirector.class , ".java" , "application" , "x-java-source" );


			addInputFlavor( verdantium.ProgramDirector.class , ".vip" , "application" , "x-viper-java" );










			addOutputFlavor( verdantium.ProgramDirector.class , ".txt" , "text" , "plain" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".text" , "text" , "plain" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".aif" , "audio" , "x-aiff" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".aiff" , "audio" , "x-aiff" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".aif" , "audio" , "aiff" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".aiff" , "audio" , "aiff" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".au" , "audio" , "basic" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".gif" , "image" , "gif" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".htm" , "text" , "html" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".html" , "text" , "html" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".jar" , "application" , "java-archive" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".jpeg" , "image" , "jpeg" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".jpg" , "image" , "jpeg" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".jpe" , "image" , "jpeg" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".jfif" , "image" , "jpeg" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".pjpeg" , "image" , "jpeg" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".pjp" , "image" , "jpeg" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".hqx" , "application" , "mac-binhex40" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".doc" , "application" , "msword" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".dot" , "application" , "msword" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".rtf" , "application" , "msword" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".wiz" , "application" , "msword" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".nsc" , "application" , "x-conference" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".pcd" , "image" , "x-photo-cd" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".png" , "image" , "x-png" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".pdf" , "application" , "pdf" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".mov" , "video" , "quicktime" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".rtf" , "application" , "rtf" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".tiff" , "image" , "tiff" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".tif" , "image" , "tiff" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".avi" , "video" , "avi" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".avi" , "video" , "x-msvideo" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".wav" , "audio" , "x-wav" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".wav" , "audio" , "wav" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".bmp" , "image" , "x-MS-bmp" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".xbm" , "image" , "x-xbitmap" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".zip" , "application" , "x-zip-compressed" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".js" , "application" , "x-javascript" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".mocha" , "application" , "x-javascript" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".geo" , "application" , "x-geometric-algebra" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".java" , "application" , "x-java-source" );


			addOutputFlavor( verdantium.ProgramDirector.class , ".vip" , "application" , "x-viper-java" );



			
		}
		catch( Throwable ex )
		{
			ex.printStackTrace( System.out );
		}
	}
	
	/**
	 * Adds a listener to the component manager.
	 * @param run The listener to add.
	 */
	public static synchronized void addListener( Runnable run )
	{
		listenerSet.add( run );
	}
	
	/**
	 * Removes a listener from the component manager.
	 * @param run The listener to remove.
	 */
	public static synchronized void removeListener( Runnable run )
	{
		listenerSet.remove( run );
	}
	
	
	
	/**
	* Gets the native extensions for a particular input flavor.
	* @param flavor The data flavor.
	* @return An array of matching native extensions.
	*/
	public static String[] getInputNativeForFlavor(DataFlavor flavor) {
		if (inputFlavorTable == null)
			updateFlavorTables();
		return inputFlavorTable.get(flavor.getMimeType());
	}

	/**
	* Gets the native extensions for a particular output flavor.
	* @param flavor The data flavor.
	* @return An array of matching native extensions.
	*/
	public static String[] getOutputNativeForFlavor(DataFlavor flavor) {
		if (inputFlavorTable == null)
			updateFlavorTables();
		return inputFlavorTable.get(flavor.getMimeType());
	}

	/**
	* Gets the input flavors for a particular native extension.
	* @param nativ The native file extension.
	* @return The array of matching input flavors.
	*/
	public static DataFlavor[] getInputFlavorForNative(String nativ) {
		if (inputFlavorTable == null)
			updateFlavorTables();
		return inputNativeTable.get(nativ);
	}

	/**
	* Gets the output flavor for a particular native extension.
	* @param nativ The native file extension.
	* @return The array of matching output flavors.
	*/
	public static DataFlavor[] getOutputFlavorForNative(String nativ) {
		if (inputFlavorTable == null)
			updateFlavorTables();
		return outputNativeTable.get(nativ);
	}
	
	
	/**
	 * Gets the Verdantium component class for a human-readable component name.
	 * @param cname The human-readable component name.
	 * @return The Verdantium component class.
	 */
	public static Class<? extends VerdantiumComponent> getComponentClassForComponentName( String cname )
	{
		return( getMap().get( cname ) );
	}
	
	
	/**
	* Gets the component names for a particular input data flavor.
	* @param in The input data flavor.
	* @return The matching component names.
	*/
	public static String[] getComponentNameForInputFlavor(DataFlavor in) {
		String tmp = null;
		if (in instanceof TransVersionBufferFlavor)
			tmp =
				"Buf("
					+ ((TransVersionBufferFlavor) in).getComponentName()
					+ ")";
		else
			tmp = "Mime(" + in.getMimeType() + ")";
		return partTable.get(tmp);
	}
	
	
	/**
	* Gets the component name for a particular input component name.
	* @param in The input component name.
	* @return The original component name if the component is found in the manager, otherwise null.
	*/
	public static String[] getReadingComponentNameForInputName(String in) {
		if( getMap().get( in ) == null )
		{
			return( null );
		}
		String[] ret = { in }; // For now it's a system error to have more than one.
		return( ret );
	}
	

	/**
	 * Updates the data flavor tables.
	 */
	private static void updateFlavorTables()
	{
		inputFlavorTable = new Hashtable<String, String[]>();
		inputNativeTable = new Hashtable<String, DataFlavor[]>();
		outputFlavorTable = new Hashtable<String, String[]>();
		outputNativeTable = new Hashtable<String, DataFlavor[]>();
		partTable = new Hashtable<String, String[]>();
		componentMap = new TreeMap<String,Class<? extends VerdantiumComponent>>();
		
		{
			for( final FlavorListKey key : inputFlavorList )
			{
				addInputFlavorLink(
						key.getCnative(),
						createInputStreamFlavor(key.getPrimary(), key.getSecondary()));
			}
		}
		
		
		{
			for( final FlavorListKey key : outputFlavorList )
			{
				addOutputFlavorLink(
						key.getCnative(),
						createOutputStreamFlavor(key.getPrimary(), key.getSecondary()));
			}
		}
		
		
		{
			Class<?>[] ParamTypes = {
			};
			for( final ComponentListKey key : componentList )
			{
				final String name = key.getName();
				final Class<? extends VerdantiumComponent> cl = key.getClss();
				componentMap.put(name, cl);
				try
				{
					Method ObjMeth =
						(cl).getMethod(
							"getPersistentInputDataFlavorsSupported",
							ParamTypes);

					Object[] params = {
					};
					Object myo = ObjMeth.invoke(null, params);

					DataFlavor[] test = (DataFlavor[]) myo;

					int count2;
					for (count2 = 0; count2 < test.length; count2++) {
						addPartLink(name, test[count2]);
					}
				}
				catch (Exception e) {
					throw (new WrapRuntimeException("Dynamic Init. Failed", e));
				}
			}
		}
		
		
		
		
	}
	
	
	
	/**
	* Associates a VerdantiumComponent name with an input (read) TransVersionBuffer flavor.
	* @param The component name.
	* @param flavor The TransVersionBuffer flavor.
	*/
	public static void addPartLink(String pname, DataFlavor flavor) {
		String data = pname;
		String tmp = null;
		if (flavor instanceof TransVersionBufferFlavor)
			tmp =
				"Buf("
					+ ((TransVersionBufferFlavor) flavor).getComponentName()
					+ ")";
		else
			tmp = "Mime(" + flavor.getMimeType() + ")";

		String[] sl = partTable.get(tmp);
		if (sl == null) {
			String[] sa = { data };
			partTable.put(tmp, sa);
		} else {
			int count;
			String[] sa = new String[sl.length + 1];
			for (count = 0; count < sl.length; count++)
				sa[count] = sl[count];
			sa[sl.length] = data;
			partTable.put(tmp, sa);
		}
	}
	
	
	/**
	* Creates an input stream flavor with a particular MIME type.
	* @param primary The MIME primary name.
	* @param secondary The MIME secondary name.
	* @return The input stream flavor.
	*/
	public static DataFlavor createInputStreamFlavor(
		String primary,
		String secondary) {
		return (
			new VerdantiumDataFlavor(
				primary,
				secondary,
				null,
				InputStream.class,
				primary + "/" + secondary));
	}

	/**
	* Creates an output stream flavor with a particular MIME type.
	* @param primary The MIME primary name.
	* @param secondary The MIME secondary name.
	* @return The output stream flavor.
	*/
	public static DataFlavor createOutputStreamFlavor(
		String primary,
		String secondary) {
		return (
			new VerdantiumDataFlavor(
				primary,
				secondary,
				null,
				ByteArrayOutputStream.class,
				primary + "/" + secondary));
	}
	
	
	/**
	 * The list of input (read) data flavors.
	 */
	private static ArrayList<FlavorListKey> inputFlavorList;
	
	/**
	 * The list of output (write) data flavors.
	 */
	private static ArrayList<FlavorListKey> outputFlavorList;
	
	/**
	 * The list of component classes.
	 */
	private static ArrayList<ComponentListKey> componentList;
	
	/**
	 * Map of input (read) MIME type strings to arrays of matching component names.
	 */
	private static Hashtable<String,String[]> partTable;
	
	/**
	 * Map of input (read) MIME type strings to arrays of matching file extensions.
	 */
	private static Hashtable<String,String[]> inputFlavorTable;
	
	/**
	 * Map of native file extensions to arrays of matching input data flavors.
	 */
	private static Hashtable<String,DataFlavor[]> inputNativeTable;
	
	/**
	 * Map of output (write) MIME type strings to arrays of matching file extensions.
	 */
	private static Hashtable<String,String[]> outputFlavorTable;
	
	/**
	 * Map of native file extensions to arrays of matching output data flavors.
	 */
	private static Hashtable<String,DataFlavor[]> outputNativeTable;
	
}

