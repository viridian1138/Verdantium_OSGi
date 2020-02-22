
package verdantium.utils;

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
 * Common interface for classes that fire property change events.
 * 
 * @author thorngreen
 */
public interface PropertyChangeFire {
	
	/**
	* Fires a property change event.
	* @param evt The event name.
	* @param oldVal The old value of the property.
	* @param newVal The new value of the property.
	*/
	public void firePropertyChg(String evt, Object oldVal, Object newVal);

}

