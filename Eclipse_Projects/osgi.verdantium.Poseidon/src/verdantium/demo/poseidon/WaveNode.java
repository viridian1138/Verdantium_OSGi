package verdantium.demo.poseidon;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import meta.DataFormatException;
import meta.VersionBuffer;

//$$strtCprt
/*
     Poseidon ripple-tank simulator by Thorn Green
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
* This is class used in a very simple ripple tank application that can be embedded
* in other components.
* <P>
* @author Thorn Green
*/
public class WaveNode implements Externalizable {
	
	/**
	* The magnitude of a particular wave.
	*/
	private double magnitude;
	
	/**
	* The X coordinate of a particular wave in normalized device coordinates.
	*/
	private double x;
	
	/**
	* The Y coordinate of a particular wave in normalized device coordinates.
	*/
	private double y;
	
	/**
	* The phase of the wave in radians.
	*/
	private double phase;
	
	/**
	* The period of the wave (represented as the wavelength in normalized device coordinates).
	*/
	private double period;


	/**
	 * Constructor
	 * @param _magnitude The magnitude of a particular wave.
	 * @param _x The X coordinate of a particular wave in normalized device coordinates.
	 * @param _y The Y coordinate of a particular wave in normalized device coordinates.
	 * @param _phase The phase of the wave in radians.
	 * @param _period The period of the wave (represented as the wavelength in normalized device coordinates).
	 */
	public WaveNode( double _magnitude , double _x , double _y ,
		double _phase , double _period )
	{
		magnitude = _magnitude;
		x = _x;
		y = _y;
		phase = _phase;
		period = _period;
	}
	
	/**
	 * Constructor to be used for serial storage purposes only.
	 */
	public WaveNode()
	{
	}

	/**
	* Reads the node from serial storage.
	*/
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			VersionBuffer myv = (VersionBuffer) (in.readObject());
			VersionBuffer.chkNul(myv);

			magnitude = myv.getDouble("Magnitude");
			x = myv.getDouble("x");
			y = myv.getDouble("y");
			phase = myv.getDouble("Phase");
			period = myv.getDouble("Period");
		}
		catch (ClassCastException ex) {
			throw (new DataFormatException(ex));
		}
	}

	/**
	* Writes the node to serial storage.
	* @serialData TBD.
	*/
	public void writeExternal(ObjectOutput out) throws IOException {
		VersionBuffer myv = new VersionBuffer(VersionBuffer.WRITE);

		myv.setDouble("Magnitude", magnitude);
		myv.setDouble("x", x);
		myv.setDouble("y", y);
		myv.setDouble("Phase", phase);
		myv.setDouble("Period", period);

		out.writeObject(myv);
	}

	/**
	 * Gets the magnitude of a particular wave.
	 * @return The magnitude of a particular wave.
	 */
	public double getMagnitude() {
		return magnitude;
	}

	/**
	 * Gets the period of the wave (represented as the wavelength in normalized device coordinates).
	 * @return The period of the wave (represented as the wavelength in normalized device coordinates).
	 */
	public double getPeriod() {
		return period;
	}

	/**
	 * Gets the phase of the wave in radians.
	 * @return The phase of the wave in radians.
	 */
	public double getPhase() {
		return phase;
	}

	/**
	 * Gets the X coordinate of a particular wave in normalized device coordinates.
	 * @return The X coordinate of a particular wave in normalized device coordinates.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Gets the Y coordinate of a particular wave in normalized device coordinates.
	 * @return The Y coordinate of a particular wave in normalized device coordinates.
	 */
	public double getY() {
		return y;
	}

}
