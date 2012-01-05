package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.Noise;
import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.Utils;

public class RigidMulti extends Module {

	/// Default frequency for the noise::module::RidgedMulti noise module.
	public static final double DEFAULT_RIDGED_FREQUENCY = 1.0;

	/// Default lacunarity for the noise::module::RidgedMulti noise module.
	public static final double DEFAULT_RIDGED_LACUNARITY = 2.0;

	/// Default number of octaves for the noise::module::RidgedMulti noise
	/// module.
	public static final int DEFAULT_RIDGED_OCTAVE_COUNT = 6;

	/// Default noise quality for the noise::module::RidgedMulti noise
	/// module.
	public static final NoiseQuality DEFAULT_RIDGED_QUALITY = NoiseQuality.STANDARD;

	/// Default noise seed for the noise::module::RidgedMulti noise module.
	public static final int DEFAULT_RIDGED_SEED = 0;

	/// Maximum number of octaves for the noise::module::RidgedMulti noise
	/// module.
	public static final int RIDGED_MAX_OCTAVE = 30;

	double frequency;

	/// Frequency multiplier between successive octaves.
	double lacunarity;

	/// Quality of the ridged-multifractal noise.
	NoiseQuality noiseQuality;

	/// Total number of octaves that generate the ridged-multifractal
	/// noise.
	int octaveCount;

	/// Contains the spectral weights for each octave.
	double[] SpectralWeights;

	/// Seed value used by the ridged-multfractal-noise function.
	int seed;


	public RigidMulti() {
		super(0);
		CalcSpectralWeights ();
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public double getLacunarity() {
		return lacunarity;
	}

	public void setLacunarity(double lacunarity) {
		this.lacunarity = lacunarity;
	}

	public NoiseQuality getNoiseQuality() {
		return noiseQuality;
	}

	public void setNoiseQuality(NoiseQuality noiseQuality) {
		this.noiseQuality = noiseQuality;
	}

	public int getOctaveCount() {
		return octaveCount;
	}

	public void setOctaveCount(int octaveCount) {
		this.octaveCount = Utils.GetMin(octaveCount, RIDGED_MAX_OCTAVE);
	}


	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	protected void CalcSpectralWeights(){
		// This exponent parameter should be user-defined; it may be exposed in a
		// future version of libnoise.
		double h = 1.0;

		double frequency = 1.0;
		for (int i = 0; i < RIDGED_MAX_OCTAVE; i++) {
			// Compute weight for each frequency.
			SpectralWeights[i] = Math.pow (frequency, -h);
			frequency *= lacunarity;
		}

	}

	@Override
	public int GetSourceModuleCount() {
		return 0;
	}

	@Override
	public double GetValue(double x, double y, double z) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		double signal = 0.0;
		double value  = 0.0;
		double weight = 1.0;

		// These parameters should be user-defined; they may be exposed in a
		// future version of libnoise.
		double offset = 1.0;
		double gain = 2.0;

		for (int curOctave = 0; curOctave < octaveCount; curOctave++) {

			// Make sure that these floating-point values have the same range as a 32-
			// bit integer so that we can pass them to the coherent-noise functions.
			double nx, ny, nz;
			nx = Utils.MakeInt32Range (x);
			ny = Utils.MakeInt32Range (y);
			nz = Utils.MakeInt32Range (z);

			// Get the coherent-noise value.
			int seed = (this.seed + curOctave) & 0x7fffffff;
			signal = Noise.GradientCoherentNoise3D (nx, ny, nz, seed, noiseQuality);

			// Make the ridges.
			signal = Math.abs(signal);
			signal = offset - signal;

			// Square the signal to increase the sharpness of the ridges.
			signal *= signal;

			// The weighting from the previous octave is applied to the signal.
			// Larger values have higher weights, producing sharp points along the
			// ridges.
			signal *= weight;

			// Weight successive contributions by the previous signal.
			weight = signal * gain;
			if (weight > 1.0) {
				weight = 1.0;
			}
			if (weight < 0.0) {
				weight = 0.0;
			}

			// Add the signal to the output value.
			value += (signal * SpectralWeights[curOctave]);

			// Go to the next octave.
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;
		}

		return (value * 1.25) - 1.0;

	}

}