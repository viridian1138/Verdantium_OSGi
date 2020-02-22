package verdantium.kluges;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

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
* This class is a kluge that makes part of a rendered image transparent.
* 
* Basically the notion is to put a transparency switch in the 
* least-significant bits of the color components.  This was 
* primarily used back when GeoFrame was generating one semi-transparent
* sprite image for each Greek symbol (plus superscripts plus subscripts) 
* that needed to be displayed next to a Vector, Bivector, etc.
* 
* @author David Halliday and Thorn Green
*/
public class GeoColorFilter extends RGBImageFilter {
	
	/**
	 * Mask leaving the RGB portions of the color.
	 */
	protected static final int RGB_MASK = 0x00ffffff;
	
	/**
	 * Mask leaving the alpha-channel (transparency) portions of the color.
	 */
	protected static final int ALPHA_MASK = 0xff000000;
	
	/**
	 * Mask leaving the two most significant bits of each RGB component.
	 */
	protected static final int CMASK = 0x00c0c0c0;

	/**
	 * The color to map from.  Currently not used.
	 */
	protected int from;
	
	/**
	 * The color to map to.  Usually transparent or semi-transparent.
	 */
	protected int to;

	/**
	 * Constructor.
	 * @param frm The color to map from.  Currently not used.
	 * @param t The color to map to.  Usually transparent or semi-transparent.
	 */
	public GeoColorFilter(Color frm, Color t) {
		super();
		from = frm.getRGB() & RGB_MASK;
		to = t.getRGB() & RGB_MASK;
		//  The  filter's  operation  does  not  depend  on  the
		//  pixel's  location,  so  IndexColorModels  can  be
		//  filtered  directly.
		canFilterIndexColorModel = true;
	}
	
	@Override
	public int filterRGB(int x, int y, int rgb) {
		return (/* ((rgb & CMASK) == ( from & CMASK ) ) */
		 ((rgb & CMASK) != CMASK) ? (to | (rgb & ALPHA_MASK)) : RGB_MASK);
	}
	
	
}

