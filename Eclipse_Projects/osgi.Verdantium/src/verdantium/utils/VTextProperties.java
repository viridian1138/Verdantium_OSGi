package verdantium.utils;

import java.util.Properties;

import meta.DataFormatException;

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
*    | 09/10/2001            | Thorn Green (viridian_1138@yahoo.com)           | Needed a way to access properties from an error handling context.    | Initial creation of the VTextProperties class.
*    | 05/10/2002            | Thorn Green (viridian_1138@yahoo.com)           | Redundant information in persistent storage.                         | Made numerous persistence and packaging changes.
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
* An enhanced version of the java.util.Properties class.
* 
* @author Thorn Green
*/
public class VTextProperties extends Properties {
	
	/**
	 * Constructor.
	 */
	public VTextProperties() {
		super();
	}

	/**
	 * Constructor.
	 * @param defaults Input properties.
	 */
	public VTextProperties(Properties defaults) {
		super(defaults);
	}

	/**
	 * Gets the property as an integer.
	 * @param key The property key.
	 * @return The property value.
	 * @throws DataFormatException
	 */
	public int getInt(String key) throws DataFormatException {
		try {
			return (Integer.parseInt(getProperty(key)));
		}
		catch (Exception ex) {
			throw (new DataFormatException(ex));
		}
	}

	/**
	 * Gets the property as an integer.
	 * @param key The property key.
	 * @param defaultValue The default value to use of the property is null.
	 * @return The property value.
	 */
	public int getInt(String key, int defaultValue) {
		try {
			return (Integer.parseInt(getProperty(key)));
		}
		catch (Exception ex) {
			return (defaultValue);
		}
	}

	/**
	 * Gets the property as a double.
	 * @param key The property key.
	 * @return The property value.
	 * @throws DataFormatException
	 */
	public double getDouble(String key) throws DataFormatException {
		try {
			return (Double.parseDouble(getProperty(key)));
		}
		catch (Exception ex) {
			throw (new DataFormatException(ex));
		}
	}

	/**
	 * Gets the property as a double.
	 * @param key The property key.
	 * @param defaultValue The default value to use of the property is null.
	 * @return The property value.
	 */
	public double getDouble(String key, double defaultValue) {
		try {
			return (Double.parseDouble(getProperty(key)));
		}
		catch (Exception ex) {
			return (defaultValue);
		}
	}

	/**
	 * Gets the property as a boolean.
	 * @param key The property key.
	 * @return The property value.
	 * @throws DataFormatException
	 */
	public boolean getBoolean(String key) throws DataFormatException {
		try {
			return ((Boolean.valueOf(getProperty(key))).booleanValue());
		}
		catch (Exception ex) {
			throw (new DataFormatException(ex));
		}
	}

	/**
	 * Gets the property as a boolean.
	 * @param key The property key.
	 * @param defaultValue The default value to use of the property is null.
	 * @return The property value.
	 */
	public boolean getBoolean(String key, boolean defaultValue) {
		try {
			return ((Boolean.valueOf(getProperty(key))).booleanValue());
		}
		catch (Exception ex) {
			return (defaultValue);
		}
	}

	/**
	 * Puts the property as an integer.
	 * @param key The property key.
	 * @param value The property value.
	 */
	public void putInt(String key, int value) {
		put(key, "" + value);
	}

	/**
	 * Puts the property as a double.
	 * @param key The property key.
	 * @param value The property value.
	 */
	public void putDouble(String key, double value) {
		put(key, "" + value);
	}

	/**
	 * Puts the property as a boolean.
	 * @param key The property key.
	 * @param value The property value.
	 */
	public void putBoolean(String key, boolean value) {
		put(key, "" + value);
	}

	/**
	 * Gets the property name as a non-null string.  Throws an exception if the string is null.
	 * @param key The property key.
	 * @return The property value.
	 * @throws DataFormatException
	 */
	public String getPropertyNonNull(String key) throws DataFormatException {
		String s = null;
		try {
			s = getProperty(key);
		}
		catch (Exception ex) {
			throw (new DataFormatException(ex));
		}
		if (s == null)
			throw (new DataFormatException());
		return (s);
	}

	
}

