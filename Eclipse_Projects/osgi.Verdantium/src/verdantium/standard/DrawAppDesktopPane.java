package verdantium.standard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JComponent;

import verdantium.core.ContainerAppDesktopPane;
import verdantium.undo.UndoManager;

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
* The desktop pane used to hold the embedded frames of the drawing application.
* 
* @author Thorn Green
*/
public class DrawAppDesktopPane extends ContainerAppDesktopPane {
	
	/**
	* The drawing pane.
	*/
	private JComponent backPane = null;

	/**
	* Constructs the desktop pane.
	* @param _mgr The manager for multi-level undo.
	* @param  ind The DrawApp associated with the desktop pane.
	* @param in The drawing pane.
	*/
	public DrawAppDesktopPane(UndoManager _mgr, DrawApp ind, JComponent in) {
		super(_mgr, ind);
		setOpaqueFlag(false);
		backPane = in;
		setBackground(Color.blue);
		add(backPane, 0);
	}

	/* public void update( Graphics g )
		{
		if( OpaqueFlag )
			{
			Rectangle r = getBounds();
			g.setColor( getBackground() );
			g.fillRect( r.x , r.y , r.width , r.height );
			}
		} */

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		backPane.setBounds(0, 0, width, height);
	}

	@Override
	public void setBounds(Rectangle r) {
		super.setBounds(r);
		Rectangle r2 = new Rectangle(0, 0, r.width, r.height);
		backPane.setBounds(r2);
	}

	@Override
	public void setSize(Dimension d) {
		super.setSize(d);
		backPane.setSize(d);
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		backPane.setSize(width, height);
	}

	@Override
	public Dimension getMinimumSize() {
		return (backPane.getMinimumSize());
	}

	@Override
	public Dimension getPreferredSize() {
		return (backPane.getPreferredSize());
	}

	
}

