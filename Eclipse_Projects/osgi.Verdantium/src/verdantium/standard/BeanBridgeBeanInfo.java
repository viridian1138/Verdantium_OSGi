package verdantium.standard;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;

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
* This is the bean info class for {@link BeanBridge}.  It implements
* essentially the minimal functionality that a BeanInfo class can
* support.
* 
* @author Thorn Green
*/
public class BeanBridgeBeanInfo extends Object implements BeanInfo {
	
	/**
	* Constructs the BeanInfo.
	*/
	public BeanBridgeBeanInfo() {
		super();
	}

	/**
	 * Gets the additional bean info.  Returns null.
	 * @return The additional bean index.
	 */
	public BeanInfo[] getAdditionalBeanInfo() {
		return (null);
	}

	/**
	 * Gets the bean descriptor.  Returns null.
	 * @return The bean descriptor.
	 */
	public BeanDescriptor getBeanDescriptor() {
		return (null);
	}

	/**
	 * Gets the default property index.
	 * @return The default property index.
	 */
	public int getDefaultPropertyIndex() {
		return (-1);
	}

	/**
	 * Gets the default event index.
	 * @return The default event index.
	 */
	public int getDefaultEventIndex() {
		return (-1);
	}

	/**
	 * Gets the event set descriptors.  Returns null.
	 * @return The event set descriptors.
	 */
	public EventSetDescriptor[] getEventSetDescriptors() {
		return (null);
	}

	/**
	 * Gets the method descriptors. Returns null.
	 * @return The method descriptors.
	 */
	public MethodDescriptor[] getMethodDescriptors() {
		return (null);
	}

	/**
	 * Gets the property descriptors.  Returns null.
	 * @return The property descriptors.
	 */
	public PropertyDescriptor[] getPropertyDescriptors() {
		return (null);
	}

	/**
	* Gets the icon for the BeanBridge.
	* @return The icon for the BeanBridge.
	*/
	public Image getIcon(int iconKind) {
		int xm = -1;
		int ym = -1;
		Frame MyFr = new Frame();
		MyFr.addNotify();

		switch (iconKind) {
			case ICON_COLOR_16x16 :
				xm = 16;
				ym = 16;
				break;

			case ICON_COLOR_32x32 :
				xm = 32;
				ym = 32;
				break;

			case ICON_MONO_16x16 :
				xm = 16;
				ym = 16;
				break;

			case ICON_MONO_32x32 :
				xm = 32;
				ym = 32;
				break;
		}

		Image img = MyFr.createImage(xm, ym);

		Graphics g = img.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, xm, ym);
		g.setColor(Color.lightGray);
		g.fillOval(0, 0, xm, ym);
		return (img);
	}

	
}

