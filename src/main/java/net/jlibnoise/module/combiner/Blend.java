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

package net.jlibnoise.module.combiner;

import net.jlibnoise.Utils;
import net.jlibnoise.exception.NoModuleException;
import net.jlibnoise.module.Module;

public class Blend extends Module {

	public Blend() {
		super(3);
	}

	public Module getControlModule() {
		if (sourceModule[2] == null)
			throw new NoModuleException();
		return sourceModule[2];
	}

	public void setControlModule(Module module) {
		if (module == null)
			throw new IllegalArgumentException("Control Module cannot be null");
		sourceModule[2] = module;
	}

	@Override
	public int getSourceModuleCount() {
		return 3;
	}

	@Override
	public double getValue(double x, double y, double z) {
		if (sourceModule[0] == null)
			throw new NoModuleException();
		if (sourceModule[1] == null)
			throw new NoModuleException();
		if (sourceModule[2] == null)
			throw new NoModuleException();

		double v0 = sourceModule[0].getValue(x, y, z);
		double v1 = sourceModule[1].getValue(x, y, z);
		double alpha = (sourceModule[2].getValue(x, y, z) + 1.0) / 2.0;
		return Utils.linearInterp(v0, v1, alpha);

	}

}
