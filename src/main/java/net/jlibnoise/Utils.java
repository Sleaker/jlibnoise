/* Copyright (C) 2011 Garrett Fleenor

 This library is free software; you can redistribute it and/or modify it
 under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation; either version 3.0 of the License, or (at
 your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 License (COPYING.txt) for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation,
 Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 This is a port of libnoise ( http://libnoise.sourceforge.net/index.html ).  Original implementation by Jason Bevins

*/

package net.jlibnoise;

public class Utils {
	public static final double PI = 3.1415926535897932385;
	public static final double SQRT_2 = 1.4142135623730950488;
	public static final double SQRT_3 = 1.7320508075688772935;
	public static final double DEG_TO_RAD = PI / 180.0;
	public static final double RAD_TO_DEG = 1.0 / DEG_TO_RAD;

	/**
	 * Performs cubic interpolation between two values bound between two other
	 * values
	 * 
	 * @param n0 the value before the first value
	 * @param n1 the first value
	 * @param n2 the second value
	 * @param n3 the value after the second value
	 * @param a the alpha value
	 * @return the interpolated value
	 */
	public static double cubicInterp(double n0, double n1, double n2, double n3, double a) {
		double p = (n3 - n2) - (n0 - n1);
		double q = (n0 - n1) - p;
		double r = n2 - n0;

		return p * a * a * a + q * a * a + r * a + n1;
	}

	/**
	 * Performs linear interpolation between two values
	 * 
	 * @param n0 first value
	 * @param n1 second value
	 * @param a the alpha value. Should be between 0 and 1.
	 * 
	 * @return the interpolated value
	 */
	public static double linearInterp(double n0, double n1, double a) {
		return (1.0 - a) * n0 + (a * n1);
	}

	/**
	 * Maps a value onto a cubic S-Curve
	 * 
	 * @param a the value to map onto a S-Curve
	 * @return the mapped value
	 */
	public static double sCurve3(double a) {
		return (a * a * (3.0 - 2.0 * a));
	}

	/**
	 * maps a value onto a quitnic S-Curve
	 * 
	 * @param a the value to map onto a quitic S-curve
	 * @return the mapped value
	 */
	public static double sCurve5(double a) {
		double a3 = a * a * a;
		double a4 = a3 * a;
		double a5 = a4 * a;
		return (6.0 * a5) - (15.0 * a4) + (10.0 * a3);
	}

	/**
	 *   Converts latitude/longitude coordinates on a unit sphere into 3D Cartesian coordinates.
     * 
	 * @param lat The latitude, in degrees.
	 * @param lon The longitude, in degrees.
	 * @return array of x,y,z
	 */
	public static double[] latLonToXYZ(double lat, double lon) {
		double r = MathHelper.cos(Math.toRadians(lat));
		double x = r * MathHelper.cos(Math.toRadians(lon));
		double y = MathHelper.sin(Math.toRadians(lat));
		double z = r * MathHelper.sin(Math.toRadians(lon));
		return new double[] {x, y, z};
	}

	public static int clampValue(int value, int lowerBound, int upperBound) {
		if (value < lowerBound) {
			return lowerBound;
		} else if (value > upperBound) {
			return upperBound;
		} else {
			return value;
		}
	}

	public static int getMax(int a, int b) {
		return (a > b) ? a : b;
	}

	public static double getMax(double a, double b) {
		return (a > b) ? a : b;
	}

	public static double getMin(double a, double b) {
		return (a < b) ? a : b;
	}

	public static int getMin(int a, int b) {
		return (a < b) ? a : b;
	}

	/**
	 * Modifies a floating-point value so that it can be stored in a
	 * integer variable.
	 * 
	 * @param n A floating-point number.
	 * @return The modified floating-point number.
	 * 
	 *         This function does not modify @a n.
	 * 
	 *         In libnoise, the noise-generating algorithms are all
	 *         integer-based; they use variables of type int. Before
	 *         calling a noise function, pass the @a x, @a y, and @a z
	 *         coordinates to this function to ensure that these coordinates can
	 *         be cast to a noise::int32 value.
	 * 
	 *         Although you could do a straight cast from double to
	 *         int, the resulting value may differ between platforms.
	 *         By using this function, you ensure that the resulting value is
	 *         identical between platforms.
	 */
	public static double makeInt32Range(double n) {
		if (n >= 1073741824.0) {
			return (2.0 * n % 1073741824.0) - 1073741824.0;
		} else if (n <= -1073741824.0) {
			return (2.0 * n % 1073741824.0) + 1073741824.0;
		} else {
			return n;
		}
	}
}
