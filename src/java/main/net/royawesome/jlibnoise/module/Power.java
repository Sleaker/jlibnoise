package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.exception.NoModuleException;

public class Power extends Module {

	public Power() {
		super(2);
	}

	@Override
	public int GetSourceModuleCount() {
		return 2;
	}

	@Override
	public double GetValue(double x, double y, double z) {
		if(SourceModule[0] == null) throw new NoModuleException();
		if(SourceModule[1] == null) throw new NoModuleException();

		return Math.pow (SourceModule[0].GetValue (x, y, z), SourceModule[1].GetValue (x, y, z));

	}

}