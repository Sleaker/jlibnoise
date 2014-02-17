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
import net.jlibnoise.Utils;
import net.jlibnoise.exception.NoModuleException;

public class Terrace extends Module {
	/// Number of control points stored in this noise module.
	int controlPointCount = 0;

	/// Determines if the terrace-forming curve between all control points
	/// is inverted.
	boolean invertTerraces = false;

	/// Array that stores the control points.
	double[] controlPoints = new double[0];

	public Terrace() {
		super(1);
	}

	public boolean isInvertTerraces() {
		return invertTerraces;
	}

	public void setInvertTerraces(boolean invertTerraces) {
		this.invertTerraces = invertTerraces;
	}

	public int getControlPointCount() {
		return controlPointCount;
	}

	public double[] getControlPoints() {
		return controlPoints;
	}

	public void addControlPoint(double value) {
		int insertionPos = findInsertionPos(value);
		insertAtPos(insertionPos, value);
	}

	public void clearAllControlPoints() {
		controlPoints = null;
		controlPointCount = 0;

	}

	public void makeControlPoints(int controlPointCount) {
		if (controlPointCount < 2) {
			throw new IllegalArgumentException("Must have more than 2 control points");
		}

		clearAllControlPoints();

		double terraceStep = 2.0 / (controlPointCount - 1.0);
		double curValue = -1.0;
		for (int i = 0; i < controlPointCount; i++) {
			addControlPoint(curValue);
			curValue += terraceStep;
		}

	}

	protected int findInsertionPos(double value) {
		int insertionPos;
		for (insertionPos = 0; insertionPos < controlPointCount; insertionPos++) {
			if (value < controlPoints[insertionPos]) {
				// We found the array index in which to insert the new control point.
				// Exit now.
				break;
			} else if (value == controlPoints[insertionPos]) {
				// Each control point is required to contain a unique value, so throw
				// an exception.
				throw new IllegalArgumentException("Value must be unique");
			}
		}
		return insertionPos;

	}

	protected void insertAtPos(int insertionPos, double value) {
		// Make room for the new control point at the specified position within
		// the control point array.  The position is determined by the value of
		// the control point; the control points must be sorted by value within
		// that array.
		double[] newControlPoints = new double[controlPointCount + 1];
		for (int i = 0; i < controlPointCount; i++) {
			if (i < insertionPos) {
				newControlPoints[i] = controlPoints[i];
			} else {
				newControlPoints[i + 1] = controlPoints[i];
			}
		}

		controlPoints = newControlPoints;
		++controlPointCount;

		// Now that we've made room for the new control point within the array,
		// add the new control point.
		controlPoints[insertionPos] = value;

	}

	@Override
	public int getSourceModuleCount() {
		return 1;
	}

	@Override
	public double getValue(double x, double y, double z) {
		if (sourceModule[0] == null)
			throw new NoModuleException();

		// Get the output value from the source module.
		double sourceModuleValue = sourceModule[0].getValue(x, y, z);

		// Find the first element in the control point array that has a value
		// larger than the output value from the source module.
		int indexPos;
		for (indexPos = 0; indexPos < controlPointCount; indexPos++) {
			if (sourceModuleValue < controlPoints[indexPos]) {
				break;
			}
		}

		// Find the two nearest control points so that we can map their values
		// onto a quadratic curve.
		int index0 = Utils.clampValue(indexPos - 1, 0, controlPointCount - 1);
		int index1 = Utils.clampValue(indexPos, 0, controlPointCount - 1);

		// If some control points are missing (which occurs if the output value from
		// the source module is greater than the largest value or less than the
		// smallest value of the control point array), get the value of the nearest
		// control point and exit now.
		if (index0 == index1) {
			return controlPoints[index1];
		}

		// Compute the alpha value used for linear interpolation.
		double value0 = controlPoints[index0];
		double value1 = controlPoints[index1];
		double alpha = (sourceModuleValue - value0) / (value1 - value0);
		if (invertTerraces) {
			alpha = 1.0 - alpha;
			double temp = value0;
			value0 = value1;
			value1 = temp;
		}

		// Squaring the alpha produces the terrace effect.
		alpha *= alpha;

		// Now perform the linear interpolation given the alpha value.
		return Utils.linearInterp(value0, value1, alpha);

	}

}
