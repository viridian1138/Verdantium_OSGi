package verdantium.xapp;

import java.awt.Color;

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
 * @author thorngreen
 *
 * Standard interface for handling a change to the background state.
 */
public interface BackgroundStateHandler {

	/**
	 * Handles a change to the background state.
	 * @param InC The new background color.
	 * @param Opaque Whether the new background is opaque.
	 */
	public void handleBackgroundState(Color InC, boolean Opaque);

}
