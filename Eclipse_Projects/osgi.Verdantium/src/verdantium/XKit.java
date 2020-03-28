package verdantium;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import meta.Meta;
import verdantium.utils.VerticalLayout;

//$$strtCprt
/*
     Verdantium compound-document framework by Thorn Green
	Copyright (C) 2005 Thorn Green

	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
//$$endCprt

/**
*
* --- SOURCE MODIFICATION LIST ---
*
* Please document all changes to this source file here.
* Feel free to add rows if needed.
*
*
*    |-----------------------|-------------------------------------------------|----------------------------------------------------------------------|---------------------------------------------------------------...
*    | Date of Modification  |    Author of Modification                       |    Reason for Modification                                           |    Description of Modification (use multiple rows if needed)  ... 
*    |-----------------------|-------------------------------------------------|----------------------------------------------------------------------|---------------------------------------------------------------...
*    |                       |                                                 |                                                                      |
*    | 9/24/2000             | Thorn Green (viridian_1138@yahoo.com)           | Needed to provide a standard way to document source file changes.    | Added a souce modification list to the documentation so that changes to the souce could be recorded. 
*    | 10/22/2000            | Thorn Green (viridian_1138@yahoo.com)           | Methods did not have names that followed standard Java conventions.  | Performed a global modification to bring the names within spec.
*    | 08/12/2001            | Thorn Green (viridian_1138@yahoo.com)           | First-Cut at Error Handling.                                         | First-Cut at Error Handling.
*    | 09/29/2001            | Thorn Green (viridian_1138@yahoo.com)           | Support for multiple PLAFs.                                          | Made code changes to support multiple PLAFs.
*    | 03/16/2003            | Thorn Green (viridian_1138@yahoo.com)           | ObjectPC did not use defaultClassLoader.                             | Moved defaultClassLoader into Meta to fix this.
*    | 08/07/2004            | Thorn Green (viridian_1138@yahoo.com)           | Establish baseline for all changes in the last year.                 | Establish baseline for all changes in the last year.
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*
*
*/




/**
 * Annotated progress bar display.
 * 
 * @author Thorn Green
 *
 */
class LoadWaitFrame extends JFrame {
	
	/**
	 * The XKit for the load wait frame.
	 */
	XKit myX;
	
	/**
	 * The title of the frame.
	 */
	private final JLabel titleLab = new JLabel("  ");
	
	/**
	 * The general subject (e.g. copying a folder) for which the progress is being displayed.
	 */
	private final JLabel subjectLab = new JLabel("  ");
	
	/**
	 * The current step being loaded or completed as part of the progress (e.g. the current file being copied).
	 */
	private final JLabel loadLab = new JLabel("  ");
	
	/**
	 * Progress bar showing the percentage of progress.
	 */
	private final JProgressBar myCan = new JProgressBar();

	/**
	 * Constructor.
	 * @param in The XKit for the load wait frame.
	 */
	public LoadWaitFrame(XKit in) {
		super("Working...");
		myX = in;
		JPanel myPan = new JPanel();
		getContentPane().setLayout(new BorderLayout(5, 5));
		getContentPane().setFont(Font.decode("Dialog"));
		getContentPane().setBackground(SystemColor.control);
		getContentPane().add("Center", myPan);
		myPan.setLayout(new VerticalLayout(5));
		myPan.add("any", titleLab);
		myPan.add("any", subjectLab);
		myPan.add("any", loadLab);
		myPan.add("any", myCan);
		myCan.setMaximum(1000);
		myCan.setOrientation(JProgressBar.HORIZONTAL);
		setResizable(true);
		setSize(100, 100);
		pack();
		Dimension x = getMinimumSize();
		setSize(200, x.height);
		/* pack(); */
	}

	/**
	 * Sets the title of the frame.
	 * @param in The title of the frame.
	 */
	public void setTitleLab(String in) {
		titleLab.setText(in);
	}
	
	/**
	 * Sets the description of the general subject for which progress is being displayed.
	 * @param in The description of the general subject for which progress is being displayed.
	 */
	public void setSubjectLab(String in) {
		subjectLab.setText(in);
	}
	
	/**
	 * Sets the description of the current step being loaded or completed as part of the progress.
	 * @param in The description of the current step being loaded or completed as part of the progress.
	 */
	public void setLoadLab(String in) {
		loadLab.setText(in);
	}
	
	/**
	 * Sets the load fraction in the progress bar.
	 * @param in The load fraction in the progress bar.
	 */
	public void setLoadFract(double in) {
		myCan.setValue((int) (1000 * in));
		myCan.paintImmediately(myCan.getVisibleRect());
	}
	
	
}



/**
* Provides a set of methods for loading and using resources from
* an application or an applet.  This class will likely be revised in the next few
* months.
* 
* @author Thorn Green
*/
public class XKit extends Object {
	
	/**
	 * Toolkit instance used to load resources.
	 */
	private static Toolkit myKit = Toolkit.getDefaultToolkit();
	
	/**
	 * The currently running applet, or null if there is no running applet.  Applets are no longer in use.
	 */
	private static Applet myApp = null;
	
	/**
	 * Annotated progress bar display.
	 */
	private static LoadWaitFrame myFrame = null;
	
	/**
	 * The number of annotated progress bar displays that are currently in use.
	 */
	private static int waitFrameUp = 0;
	
	/**
	 * The top reference object associated with the allocating Verdantium component.
	 */
	private Object topObj = null;

	/**
	 * Buffer used to expedite loading through the use of buffered streams.
	 */
	private final byte buffer[] = new byte[1024];
	

	/**
	 * Constructor.
	 * @param in The currently running applet.
	 */
	public XKit(Applet in) {
		myApp = in;
		if (myFrame == null) {
			myFrame = new LoadWaitFrame(this);
			myFrame.addNotify();
		}
	}

	/**
	 * Constructor.
	 */
	public XKit() {
		if (myFrame == null) {
			myFrame = new LoadWaitFrame(this);
			myFrame.addNotify();
		}
	}

	/**
	 * Dynamically loads a class through the Verdantium class loaders.
	 * @param packageName The name of the class's package.
	 * @param name The name of the class to be loaded.
	 * @return The loaded class.
	 * @throws ClassNotFoundException
	 */
	public Class<?> getClass(String packageName, String name)
		throws ClassNotFoundException {
		Class<?> retClass = null;
		try {
			retClass =
				Class.forName(
					packageName + name,
					true,
					Meta.getDefaultClassLoader());
		} catch (ClassNotFoundException e) {
			throw (e);
		}
		return (retClass);
	}

	/**
	 * Loads an image from a base/offset path.
	 * @param baseName The relative path to the folder creating the image.
	 * @param offsetName The name of the image in the folder.
	 * @param res Whether to use the resource mechanism of the class to determine the location to stream.
	 * @param inClass The class relative to which to determine the stream location, assuming the resource mechanism of the class is used to determine the location of the image.
	 * @return The created image.
	 */
	public Image getBaseImage(
		String baseName,
		String offsetName,
		boolean res,
		Class<?> inClass) {
		Image im = null;
		String fname = baseName + offsetName;
		IOException tmp = null;

		InputStream inStream = null;
		if (res) {
			try {
				inStream = getBaseStream(baseName + offsetName, inClass);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				while (true) {
					int nRead = inStream.read(buffer, 0, buffer.length);
					if (nRead <= 0)
						break;
					baos.write(buffer, 0, nRead);
				}

				im =
					(Toolkit.getDefaultToolkit()).createImage(
						baos.toByteArray());
			} catch (IOException e) {
				tmp = e; /* Handled Im == null */
			}
		}

		if (inStream != null) {
			try {
				inStream.close();
			} catch (IOException e) { /* Handled Im == null */
			}
		}

		if (im == null) {
			if (myApp != null)
				im = myApp.getImage(myApp.getCodeBase(), fname);
		}

		return (im);
	};

	/**
	 * Loads an image from a base/offset path.
	 * @param baseName The relative path to the folder creating the image.
	 * @param offsetName The name of the image in the folder.
	 * @param inClass The class relative to which to determine the stream location, assuming the resource mechanism of the class is used to determine the location of the image.
	 * @return The created image.
	 */
	public Image getBaseImage(
		String baseName,
		String offsetName,
		Class<?> inClass) {
		return (getBaseImage(baseName, offsetName, true, inClass));
	}

	/**
	 * Loads an image as an applet offset, assuming that an applet is in use.
	 * @param baseName The offset folder path of the resource relative to the code base of the applet.
	 * @param fileName The filename of the image to load.
	 * @param state Not used.
	 * @return The loaded image.
	 * @throws IOException
	 */
	public Image getFileBaseImage(
		String baseName,
		String fileName,
		boolean state)
		throws IOException {
		String fname = baseName + fileName;
		Image im = null;
		if (myApp != null)
			im = myApp.getImage(myApp.getCodeBase(), fname);
		return (im);
	};

	/**
	 * Loads an image given a URL.
	 * @param myU The input URL.
	 * @return The image loaded from the URL.
	 * @throws IOException
	 */
	public Image getImage(URL myU) throws IOException {
		Image im = null;
		if (myApp != null)
			im = myApp.getImage(myU);
		else {
			im = myKit.getImage(myU);
		}
		return (im);
	};

	/**
	 * Gets the screen resolution of the display in DPI.
	 * @return The screen resolution of the display in DPI.
	 */
	public int getDPI() {
		return (myKit.getScreenResolution());
	};

	/**
	 * If there is an active applet, show a string as the status string of the applet.  Otherwise does nothing.
	 * @param msg The string to be displayed.
	 */
	public void showStatus(String msg) {
		if (myApp != null)
			myApp.showStatus(msg);
	}

	/**
	 * Creates a new MediaTracker.
	 * @return The newly created MediaTracker.
	 */
	public MediaTracker newMediaTracker() {
		return (new MediaTracker(myFrame));
	}

	/**
	 * Creates a blank image that can be rendered to from a graphics context.
	 * @param width The desired width of the image.  Zero width will be automatically increased.
	 * @param height The desired height of the image.  Zero height will be automatically increased.
	 * @return The created image.
	 */
	public Image createImage(int width, int height) {
		if ((width > 0) && (height > 0))
			return (myFrame.createImage(width, height));
		else
			return (myFrame.createImage(2, 2));
	}

	/**
	 * Gets the AWT component for the annotated progress bar display.
	 * @return The AWT component for the annotated progress bar display.
	 */
	public Component getComponent() {
		return (myFrame);
	}

	/**
	 * Opens an input stream for a resource offset relative to a class.
	 * @param in The resource offset string.
	 * @param res Whether to use the resource mechanism of the class to determine the location to stream.  Ignored.
	 * @param inClass The class relative to which to determine the stream location.
	 * @return The input stream.
	 * @throws IOException
	 */
	public InputStream getBaseStream(String in, boolean res, Class<?> inClass)
		throws IOException {
		InputStream u = null;

		try {
			URL y = getBaseURL(in, res, inClass);
			URLConnection myCon = y.openConnection();
			u = new BufferedInputStream(myCon.getInputStream());
		} catch (IOException e) {
			throw (e);
		}

		return (u);
	}

	/**
	 * Opens an input stream for a resource offset relative to a class.
	 * @param in The resource offset string.
	 * @param inClass The class relative to which to determine the stream location.
	 * @return The input stream.
	 * @throws IOException
	 */
	public InputStream getBaseStream(String in, Class<?> inClass)
		throws IOException {
		return (getBaseStream(in, true, inClass));
	}

	/**
	 * Gets the base URL of a resource offset relative to a class.
	 * @param in The resource offset string.
	 * @param res Whether to use the resource mechanism of the class to determine the URL.
	 * @param inClass The class relative to which to determine the URL.
	 * @return The base URL.
	 * @throws IOException
	 */
	public URL getBaseURL(String in, boolean res, Class<?> inClass)
		throws IOException {
		URL u = null;

		if (res)
			u = inClass.getResource(in);

		if (u == null) {
			try {
				if (myApp != null) {
					u = new URL(myApp.getCodeBase(), in);
				} 
			} catch (IOException e) {
				throw (e);
			}
		}

		return (u);
	}

	/**
	 * Gets the base URL of a resource offset relative to a class.
	 * @param in The resource offset string.
	 * @param inClass The class relative to which to determine the URL.
	 * @return The base URL.
	 * @throws IOException
	 */
	public URL getBaseURL(String in, Class<?> inClass) throws IOException {
		return (getBaseURL(in, true, inClass));
	}

	/**
	 * Gets the currently running applet, or null if there is no running applet.
	 * @return The currently running applet, or null if there is no running applet.
	 */
	public Applet getApplet() {
		return (myApp);
	}
	
	/**
	 * Sets the top reference object associated with the allocating Verdantium component.
	 * @param in The top reference object associated with the allocating Verdantium component.
	 */
	public void setTopObj(Object in) {
		topObj = in;
	}
	
	/**
	 * Gets the top reference object associated with the allocating Verdantium component.
	 * @return The top reference object associated with the allocating Verdantium component.
	 */
	public Object getTopObj() {
		return (topObj);
	}

	/**
	 * Sets the frame title of the annotated progress bar display.
	 * @param in The frame title of the annotated progress bar display.
	 */
	public void setTitleLab(String in) {
		myFrame.setTitleLab(in);
	}
	
	/**
	 * Sets the description of the general subject for which progress is being displayed in the annotated progress bar display.
	 * @param in The description of the general subject for which progress is being displayed in the annotated progress bar display.
	 */
	public void setSubjectLab(String in) {
		myFrame.setSubjectLab(in);
	}
	
	/**
	 * Sets the description of the current step being loaded or completed as part of the progress in the annotated progress bar display.
	 * @param in The description of the current step being loaded or completed as part of the progress in the annotated progress bar display.
	 */
	public void setLoadLab(String in) {
		myFrame.setLoadLab(in);
	}
	
	/**
	 * Sets the load fraction in the progress bar of the annotated progress bar display.
	 * @param in The current amount loaded.
	 * @param max The current max to be loaded.
	 */
	public void setLoadFract(int in, int max) {
		myFrame.setLoadFract(((double) in) / ((double) max));
	}
	
	/**
	 * Shows the annotated progress bar frame.
	 */
	public void showLoadFrame() {
		waitFrameUp++;
		if (waitFrameUp == 1) {
			myFrame.setVisible(true);
		}
	}
	
	/**
	 * Hides the annotated progress bar frame.
	 */
	public void hideLoadFrame() {
		waitFrameUp--;
		if (waitFrameUp == 0)
			myFrame.setVisible(false);
	}
	
	/**
	 * Brings the annotated progress bar frame, if visible, to the front.
	 */
	public void setToFront() {
		myFrame.toFront();
	}
	
	/**
	 * Updates the pluggable look and feel (PLAF) for the annotated progress bar display.
	 */
	public void initLoadFrameUI() {
		SwingUtilities.updateComponentTreeUI(myFrame);
	}

	
	
};


