/*
 * Created on Nov 26, 2005
 *
 * List of borders currently loaded into Verdantium.
 */
package verdantium;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Vector;

import meta.WrapRuntimeException;
import verdantium.utils.VTextProperties;

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
 * List of all borders currently loaded into the Verdantium system.
 * 
 * @author Thorn Green
 */
public class VerdantiumBorderList {

	/**
	 * Property change name for changes to the border list.
	 */
	public static final String BORDER_LIST_CHANGED = "BORDER_LIST_CHANGED";

	/**
	 * List of human-readable names for the borders.
	 */
	protected static Vector<String> borderNames;

	/**
	 * List of border construction parameter objects.
	 */
	protected static Vector<Object[]> borderConstructParams;

	/**
	 * Change support for firing property change events.
	 */
	protected static PropertyChangeSupport propSL =
		new PropertyChangeSupport("Border List");

	/**
	* Adds a property change listener to the class.
	* @param in PropertyChangeListener to add.
	*/
	public static void addClassPropertyChangeListener(PropertyChangeListener in) {
		propSL.addPropertyChangeListener(in);
	}

	/**
	* Removes a property change listener from the class.
	* @param in PropertyChangeListener to remove.
	*/
	public static void removeClassPropertyChangeListener(PropertyChangeListener in) {
		propSL.removePropertyChangeListener(in);
	}

	/**
	 * Gets the current list of human-readable border names.
	 * @return The border names.
	 */
	public static Vector<String> getBorderNames() {
		return (borderNames);
	}

	/**
	 * Gets the border construction parameters for a particular index.
	 * @param index The index for which to retrieve the construction parameters.
	 * @return The parameters for introspective construction of the border.
	 */
	public static Object[] getConstructParamsForIndex(int index) {
		Object[] ret = borderConstructParams.elementAt(index);
		return (ret);
	}

	/**
	 * Adds a border.
	 * @param borderName The human-readable name of the border to add.
	 * @param borderParam The construction parameters for the border.
	 */
	public static void addBorder( String borderName , Object[] borderParam )
	{
		borderNames.add( borderName );
		borderConstructParams.add( borderParam );
	}
	
	/**
	 * Removes a border.
	 * @param borderName The human-readable name of the border to add.
	 * @param borderParam The construction parameters for the border.
	 */
	public static void removeBorder( String borderName , Object[] borderParam )
	{
		borderNames.remove( borderName );
		borderConstructParams.remove( borderParam );
	}

	/**
	 * Loads default borders.
	 */
	static {
		borderNames = new Vector<String>();
		borderConstructParams = new Vector<Object[]>();

		borderNames.add("No Border");
		{
			Object[] myo = { null, null, null };
			borderConstructParams.add(myo);
		}

		borderNames.add("Line Border");
		{
			Class<?>[] types = { Color.class };
			Object[] params = { Color.black };
			Object[] myo = { "javax.swing.border.LineBorder", types, params };
			borderConstructParams.add(myo);
		}

		borderNames.add("Matte Border");
		{
			Class<?>[] types =
				{
					Integer.TYPE,
					Integer.TYPE,
					Integer.TYPE,
					Integer.TYPE,
					Color.class };
			Object[] params =
				{
					new Integer(5),
					new Integer(5),
					new Integer(5),
					new Integer(5),
					Color.black };
			Object[] myo = { "javax.swing.border.MatteBorder", types, params };
			borderConstructParams.add(myo);
		}

		borderNames.add("Bevel Border");
		{
			Class<?>[] types = { Integer.TYPE };
			Object[] params = { new Integer(0)};
			Object[] myo = { "javax.swing.border.BevelBorder", types, params };
			borderConstructParams.add(myo);
		}

		borderNames.add("Soft Bevel Border");
		{
			Class<?>[] types = { Integer.TYPE };
			Object[] params = { new Integer(0)};
			Object[] myo =
				{ "javax.swing.border.SoftBevelBorder", types, params };
			borderConstructParams.add(myo);
		}

		borderNames.add("Etched Border");
		{
			Class<?>[] types = {
			};
			Object[] params = {
			};
			Object[] myo = { "javax.swing.border.EtchedBorder", types, params };
			borderConstructParams.add(myo);
		}
	}

}
