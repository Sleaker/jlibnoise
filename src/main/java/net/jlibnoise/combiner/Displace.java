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

package net.jlibnoise.combiner;

import net.jlibnoise.Module;
import net.jlibnoise.exception.NoModuleException;

public class Displace extends Module {

	public Displace() {
		super(4);
	}

	@Override
	public int getSourceModuleCount() {
		return 4;
	}

	public Module GetXDisplaceModule() {
		if (sourceModule == null || sourceModule[1] == null) {
			throw new NoModuleException();
		}
		return sourceModule[1];
	}

	public Module GetYDisplaceModule() {
		if (sourceModule == null || sourceModule[2] == null) {
			throw new NoModuleException();
		}
		return sourceModule[2];
	}

	public Module GetZDisplaceModule() {
		if (sourceModule == null || sourceModule[3] == null) {
			throw new NoModuleException();
		}
		return sourceModule[3];
	}

	public void SetXDisplaceModule(Module x) {
		if (x == null)
			throw new IllegalArgumentException("x cannot be null");
		sourceModule[1] = x;
	}

	public void SetYDisplaceModule(Module y) {
		if (y == null)
			throw new IllegalArgumentException("y cannot be null");
		sourceModule[2] = y;
	}

	public void SetZDisplaceModule(Module z) {
		if (z == null)
			throw new IllegalArgumentException("z cannot be null");
		sourceModule[3] = z;
	}

	public void SetDisplaceModules(Module x, Module y, Module z) {
		SetXDisplaceModule(x);
		SetYDisplaceModule(y);
		SetZDisplaceModule(z);
	}

	@Override
	public double getValue(double x, double y, double z) {
		if (sourceModule[0] == null)
			throw new NoModuleException();
		if (sourceModule[1] == null)
			throw new NoModuleException();
		if (sourceModule[2] == null)
			throw new NoModuleException();
		if (sourceModule[3] == null)
			throw new NoModuleException();

		// Get the output values from the three displacement modules.  Add each
		// value to the corresponding coordinate in the input value.
		double xDisplace = x + (sourceModule[1].getValue(x, y, z));
		double yDisplace = y + (sourceModule[2].getValue(x, y, z));
		double zDisplace = z + (sourceModule[3].getValue(x, y, z));

		// Retrieve the output value using the offsetted input value instead of
		// the original input value.
		return sourceModule[0].getValue(xDisplace, yDisplace, zDisplace);

	}

}
