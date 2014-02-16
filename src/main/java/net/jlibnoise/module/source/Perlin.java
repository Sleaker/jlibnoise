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

package net.jlibnoise.module.source;

import net.jlibnoise.NoiseGen;
import net.jlibnoise.NoiseQuality;
import net.jlibnoise.Utils;
import net.jlibnoise.module.Module;

/**
 * Noise module that outputs 3-dimensional Perlin noise.
 *
 *
 * Perlin noise is the sum of several coherent-noise functions of
 * ever-increasing frequencies and ever-decreasing amplitudes.
 *
 * An important property of Perlin noise is that a small change in the
 * input value will produce a small change in the output value, while a
 * large change in the input value will produce a random change in the
 * output value.
 *
 * This noise module outputs Perlin-noise values that usually range from
 * -1.0 to +1.0, but there are no guarantees that all output values will
 * exist within that range.
 *
 * For a better description of Perlin noise, see the links in the
 * <i>References and Acknowledgments</i> section.
 *
 * This noise module does not require any source modules.
 *
 * <b>Octaves</b>
 *
 * The number of octaves control the <i>amount of detail</i> of the
 * Perlin noise.  Adding more octaves increases the detail of the Perlin
 * noise, but with the drawback of increasing the calculation time.
 *
 * An octave is one of the coherent-noise functions in a series of
 * coherent-noise functions that are added together to form Perlin
 * noise.
 *
 * An application may specify the frequency of the first octave by
 * calling the SetFrequency() method.
 *
 * An application may specify the number of octaves that generate Perlin
 * noise by calling the SetOctaveCount() method.
 *
 * These coherent-noise functions are called octaves because each octave
 * has, by default, double the frequency of the previous octave.  Musical
 * tones have this property as well; a musical C tone that is one octave
 * higher than the previous C tone has double its frequency.
 *
 * <b>Frequency</b>
 *
 * An application may specify the frequency of the first octave by
 * calling the SetFrequency() method.
 *
 * <b>Persistence</b>
 *
 * The persistence value controls the <i>roughness</i> of the Perlin
 * noise.  Larger values produce rougher noise.
 *
 * The persistence value determines how quickly the amplitudes diminish
 * for successive octaves.  The amplitude of the first octave is 1.0.
 * The amplitude of each subsequent octave is equal to the product of the
 * previous octave's amplitude and the persistence value.  So a
 * persistence value of 0.5 sets the amplitude of the first octave to
 * 1.0; the second, 0.5; the third, 0.25; etc.
 *
 * An application may specify the persistence value by calling the
 * SetPersistence() method.
 *
 * <b>Lacunarity</b>
 *
 * The lacunarity specifies the frequency multipler between successive
 * octaves.
 *
 * The effect of modifying the lacunarity is subtle; you may need to play
 * with the lacunarity value to determine the effects.  For best results,
 * set the lacunarity to a number between 1.5 and 3.5.
 *
 * <b>References &amp; acknowledgments</b>
 *
 * <a href=http://www.noisemachine.com/talk1/>The Noise Machine</a> -
 * From the master, Ken Perlin himself.  This page contains a
 * presentation that describes Perlin noise and some of its variants.
 * He won an Oscar for creating the Perlin noise algorithm!
 *
 * <a href=http://freespace.virgin.net/hugo.elias/models/m_perlin.htm>
 * Perlin Noise</a> -  Hugo Elias's webpage contains a very good
 * description of Perlin noise and describes its many applications.  This
 * page gave me the inspiration to create libnoise in the first place.
 * Now that I know how to generate Perlin noise, I will never again use
 * cheesy subdivision algorithms to create terrain (unless I absolutely
 * need the speed.)
 *
 * <a href=http://www.robo-murito.net/code/perlin-noise-math-faq.html>The
 * Perlin noise math FAQ</a> - A good page that describes Perlin noise in
 * plain English with only a minor amount of math.  During development of
 * libnoise, I noticed that my coherent-noise function generated terrain
 * with some "regularity" to the terrain features.  This page describes a
 * better coherent-noise function called <i>gradient noise</i>.  This
 * version of noise::module::Perlin uses gradient coherent noise to
 * generate Perlin noise.
 */
public class Perlin extends Module {

	// Default frequency for the noise::module::Perlin noise module.
	public static final double DEFAULT_PERLIN_FREQUENCY = 1.0;

	// Default lacunarity for the noise::module::Perlin noise module.
	public static final double DEFAULT_PERLIN_LACUNARITY = 2.0;

	// Default number of octaves for the noise::module::Perlin noise module.
	public static final int DEFAULT_PERLIN_OCTAVE_COUNT = 6;

	// Default persistence value for the noise::module::Perlin noise module.
	public static final double DEFAULT_PERLIN_PERSISTENCE = 0.5;

	// Default noise quality for the noise::module::Perlin noise module.
	public static final NoiseQuality DEFAULT_PERLIN_QUALITY = NoiseQuality.STANDARD;

	// Default noise seed for the noise::module::Perlin noise module.
	public static final int DEFAULT_PERLIN_SEED = 0;

	// Maximum number of octaves for the noise::module::Perlin noise module.
	public static final int PERLIN_MAX_OCTAVE = 30;

	// Frequency of the first octave.
	double frequency = DEFAULT_PERLIN_FREQUENCY;

	// Frequency multiplier between successive octaves.
	double lacunarity = DEFAULT_PERLIN_LACUNARITY;

	// Quality of the Perlin noise.
	NoiseQuality noiseQuality = DEFAULT_PERLIN_QUALITY;

	// Total number of octaves that generate the Perlin noise.
	int octaveCount = DEFAULT_PERLIN_OCTAVE_COUNT;

	// Persistence of the Perlin noise.
	double persistence = DEFAULT_PERLIN_PERSISTENCE;

	// Seed value used by the Perlin-noise function.
	int seed = DEFAULT_PERLIN_SEED;

	public Perlin() {
		super(0);
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
		if (octaveCount < 1 || octaveCount > PERLIN_MAX_OCTAVE) {
			throw new IllegalArgumentException("octaveCount must be between 1 and MAX OCTAVE: " + PERLIN_MAX_OCTAVE);
		}

		this.octaveCount = octaveCount;
	}

	public double getPersistence() {
		return persistence;
	}

	public void setPersistence(double persistence) {
		this.persistence = persistence;
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	@Override
	public int getSourceModuleCount() {
		return 0;
	}

    @Override
    public double getValue(double x, double y, double z) {
        double x1 = x;
        double y1 = y;
        double z1 = z;
        double value = 0.0;
        double signal;
        double curPersistence = 1.0;
        double nx, ny, nz;
        int seed;

        x1 *= frequency;
        y1 *= frequency;
        z1 *= frequency;

        for (int curOctave = 0; curOctave < octaveCount; curOctave++) {

            // Make sure that these floating-point values have the same range as a 32-bit integer so that we can pass them to the coherent-noise functions.
            nx = Utils.makeInt32Range(x1);
            ny = Utils.makeInt32Range(y1);
            nz = Utils.makeInt32Range(z1);

            // Get the coherent-noise value from the input value and add it to the
            // final result.
            seed = (this.seed + curOctave);
            signal = NoiseGen.gradientCoherentNoise3D(nx, ny, nz, seed, noiseQuality);
            value += signal * curPersistence;

            // Prepare the next octave.
            x1 *= lacunarity;
            y1 *= lacunarity;
            z1 *= lacunarity;
            curPersistence *= persistence;
        }

        return value;

    }

}
