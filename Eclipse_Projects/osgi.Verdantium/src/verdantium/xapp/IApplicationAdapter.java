package verdantium.xapp;

import java.beans.PropertyChangeListener;

import verdantium.EtherEventPropertySource;
import verdantium.VerdantiumComponent;
import verdantium.core.OnlyDesignerEditListener;

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
 * Standard interface implemented by components with most of the capabilities of an application.
 */
public interface IApplicationAdapter
	extends
		VerdantiumComponent,
		PropertyChangeListener,
		OnlyDesignerEditListener,
		EtherEventPropertySource,
		OnlyDesignerEditsChangeHandler,
		IScrollingComponent,
		BackgroundStateHandler,
		PageHandler {

	
}

