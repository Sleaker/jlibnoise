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

package net.jlibnoise.modifier;

import net.jlibnoise.Module;
import net.jlibnoise.exception.NoModuleException;

/**
 * Noise module that clamps the output value from a source module to a
 * range of values.
 *
 *
 * The range of values in which to clamp the output value is called the
 * <i>clamping range</i>.
 *
 * If the output value from the source module is less than the lower
 * bound of the clamping range, this noise module clamps that value to
 * the lower bound.  If the output value from the source module is
 * greater than the upper bound of the clamping range, this noise module
 * clamps that value to the upper bound.
 *
 * To specify the upper and lower bounds of the clamping range, call the
 * setBounds() method.
 *
 * This noise module requires one source module.
 */
public class Clamp extends Module {

    // Default lower bound of the clamping range for the Clamp noise module.
    public static double DEFAULT_CLAMP_LOWER_BOUND = -1.0;

    // Default upper bound of the clamping range for the Clamp noise module.
    public static double DEFAULT_CLAMP_UPPER_BOUND = 1.0;
    
	protected double lowerBound = DEFAULT_CLAMP_LOWER_BOUND;
	protected double upperBound = DEFAULT_CLAMP_UPPER_BOUND;

	public Clamp() {
		super(1);
	}

    /**
     * Returns the lower bound of the clamping range.
     *
     * @return The lower bound.
     *
     * If the output value from the source module is less than the lower
     * bound of the clamping range, this noise module clamps that value
     * to the lower bound.
     */
	public double getLowerBound() {
		return lowerBound;
	}

	/**
     * Returns the upper bound of the clamping range.
     *
     * @return The upper bound.
     *
     * If the output value from the source module is greater than the
     * upper bound of the clamping range, this noise module clamps that
     * value to the upper bound.
     */
	public double getUpperBound() {
		return upperBound;
	}

    /**
     * Sets the lower and upper bounds of the clamping range.
     *
     * @param lowerBound The lower bound.
     * @param upperBound The upper bound.
     *
     * @pre The lower bound must be less than or equal to the
     * upper bound.
     *
     * @throws IllegalArgumentException An invalid parameter was
     * specified; see the preconditions for more information.
     *
     * If the output value from the source module is less than the lower
     * bound of the clamping range, this noise module clamps that value
     * to the lower bound.  If the output value from the source module
     * is greater than the upper bound of the clamping range, this noise
     * module clamps that value to the upper bound.
     */
	public void setBounds(double lowerBound, double upperBound) {
	    if (lowerBound > upperBound) {
	        throw new IllegalArgumentException("lowerBound must be less than upperBound");
	    }
	    this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	@Override
	public int getSourceModuleCount() {
		return 1;
	}

	@Override
	public double getValue(double x, double y, double z) {
		if (sourceModule[0] == null)
			throw new NoModuleException();

		double value = sourceModule[0].getValue(x, y, z);
		if (value < lowerBound) {
			return lowerBound;
		} else if (value > upperBound) {
			return upperBound;
		} else {
			return value;
		}

	}

}
