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
package net.jlibnoise.transformer;

import net.jlibnoise.Module;
import net.jlibnoise.exception.NoModuleException;
import net.jlibnoise.generator.Perlin;

/**
 * Noise module that randomly displaces the input value before
 * returning the output value from a source module.
 *
 *
 * Turbulence is the pseudo-random displacement of the input value.
 * The GetValue() method randomly displaces the ( x, y, z )
 * coordinates of the input value before retrieving the output value from
 * the source module.  To control the turbulence, an application can
 * modify its frequency, its power, and its roughness.
 *
 * The frequency of the turbulence determines how rapidly the
 * displacement amount changes.  To specify the frequency, call the
 * setFrequency() method.
 *
 * The power of the turbulence determines the scaling factor that is
 * applied to the displacement amount.  To specify the power, call the
 * setPower() method.
 *
 * The roughness of the turbulence determines the roughness of the
 * changes to the displacement amount.  Low values smoothly change the
 * displacement amount.  High values roughly change the displacement
 * amount, which produces more "kinky" changes.  To specify the
 * roughness, call the setRoughness() method.
 *
 * Use of this noise module may require some trial and error.  Assuming
 * that you are using a generator module as the source module, you
 * should first:
 * - Set the frequency to the same frequency as the source module.
 * - Set the power to the reciprocal of the frequency.
 *
 * From these initial frequency and power values, modify these values
 * until this noise module produce the desired changes in your terrain or
 * texture.  For example:
 * - Low frequency (1/8 initial frequency) and low power (1/8 initial
 *   power) produces very minor, almost unnoticeable changes.
 * - Low frequency (1/8 initial frequency) and high power (8 times
 *   initial power) produces "ropey" lava-like terrain or marble-like
 *   textures.
 * - High frequency (8 times initial frequency) and low power (1/8
 *   initial power) produces a noisy version of the initial terrain or
 *   texture.
 * - High frequency (8 times initial frequency) and high power (8 times
 *   initial power) produces nearly pure noise, which isn't entirely
 *   useful.
 *
 * Displacing the input values result in more realistic terrain and
 * textures.  If you are generating elevations for terrain height maps,
 * you can use this noise module to produce more realistic mountain
 * ranges or terrain features that look like flowing lava rock.  If you
 * are generating values for textures, you can use this noise module to
 * produce realistic marble-like or "oily" textures.
 *
 * Internally, there are three {@link Perlin} noise modules
 * that displace the input value; one for the x, one for the y,
 * and one for the z coordinate.
 *
 * This noise module requires one source module.
 */
public class Turbulence extends Module {
	// Default frequency for the noise::module::Turbulence noise module.
	public static final double DEFAULT_TURBULENCE_FREQUENCY = Perlin.DEFAULT_PERLIN_FREQUENCY;

	// Default power for the noise::module::Turbulence noise module.
	public static final double DEFAULT_TURBULENCE_POWER = 1.0;

	// Default roughness for the noise::module::Turbulence noise module.
	public static final int DEFAULT_TURBULENCE_ROUGHNESS = 3;

	// Default noise seed for the noise::module::Turbulence noise module.
	public static final int DEFAULT_TURBULENCE_SEED = Perlin.DEFAULT_PERLIN_SEED;

	// The power (scale) of the displacement.
	double power = DEFAULT_TURBULENCE_POWER;

	// Noise module that displaces the x coordinate.
    final Perlin xDistortModule;

	// Noise module that displaces the y coordinate.
    final Perlin yDistortModule;

	// Noise module that displaces the z coordinate.
    final Perlin zDistortModule;

	public Turbulence() {
		super(1);
		xDistortModule = new Perlin();
		yDistortModule = new Perlin();
		zDistortModule = new Perlin();
	}

	/**
     * Returns the power of the turbulence.
     *
     * @return The power of the turbulence.
     *
     * The power of the turbulence determines the scaling factor that is
     * applied to the displacement amount.
     */
	public double getPower() {
		return power;
	}

	/**
     * Sets the power of the turbulence.
     *
     * @param power The power of the turbulence.
     *
     * The power of the turbulence determines the scaling factor that is
     * applied to the displacement amount.
     */
	public void setPower(double power) {
		this.power = power;
	}

	/**
     * Returns the roughness of the turbulence.
     *
     * @return The roughness of the turbulence.
     *
     * The roughness of the turbulence determines the roughness of the
     * changes to the displacement amount.  Low values smoothly change
     * the displacement amount.  High values roughly change the
     * displacement amount, which produces more "kinky" changes.
     */
	public int getRoughnessCount() {
		return xDistortModule.getOctaveCount();
	}

	/**
     * Returns the frequency of the turbulence.
     *
     * @return The frequency of the turbulence.
     *
     * The frequency of the turbulence determines how rapidly the
     * displacement amount changes.
     */
	public double getFrequency() {
		return xDistortModule.getFrequency();
	}

	/**
     * Returns the seed value of the internal Perlin-noise modules that
     * are used to displace the input values.
     *
     * @return The seed value.
     *
     * Internally, there are three {@link Perlin} noise modules
     * that displace the input value; one for the x, one for the y,
     * and one for the z coordinate.
     */
	public int getSeed() {
		return xDistortModule.getSeed();
	}

	/**
     * Sets the seed value of the internal noise modules that are used to
     * displace the input values.
     *
     * @param seed The seed value.
     *
     * Internally, there are three noise::module::Perlin noise modules
     * that displace the input value; one for the @a x, one for the @a y,
     * and one for the @a z coordinate.  This noise module assigns the
     * following seed values to the noise::module::Perlin noise modules:
     * - It assigns the seed value (@a seed + 0) to the @a x noise module.
     * - It assigns the seed value (@a seed + 1) to the @a y noise module.
     * - It assigns the seed value (@a seed + 2) to the @a z noise module.
     */
	public void setSeed(int seed) {
		xDistortModule.setSeed(seed);
		yDistortModule.setSeed(seed + 1);
		zDistortModule.setSeed(seed + 2);
	}

	/**
     * Sets the frequency of the turbulence.
     *
     * @param frequency The frequency of the turbulence.
     *
     * The frequency of the turbulence determines how rapidly the
     * displacement amount changes.
     */
	public void setFrequency(double frequency) {
		xDistortModule.setFrequency(frequency);
		yDistortModule.setFrequency(frequency);
		zDistortModule.setFrequency(frequency);
	}

	/**
     * Sets the roughness of the turbulence.
     *
     * @param roughness The roughness of the turbulence.
     *
     * The roughness of the turbulence determines the roughness of the
     * changes to the displacement amount.  Low values smoothly change
     * the displacement amount.  High values roughly change the
     * displacement amount, which produces more "kinky" changes.
     *
     * Internally, there are three {@link Perlin} noise modules
     * that displace the input value; one for the x, one for the y,
     * and one for the z coordinate.  The roughness value is equal to
     * the number of octaves used by the {@link Perlin} noise modules.
     */
	public void setRoughness(int roughness) {
		xDistortModule.setOctaveCount(roughness);
		yDistortModule.setOctaveCount(roughness);
		zDistortModule.setOctaveCount(roughness);
	}

	@Override
	public int getSourceModuleCount() {
		return 1;
	}

	@Override
	public double getValue(double x, double y, double z) {
		if (sourceModule[0] == null)
			throw new NoModuleException();

		// Get the values from the three noise::module::Perlin noise modules and
		// add each value to each coordinate of the input value.  There are also
		// some offsets added to the coordinates of the input values.  This prevents
		// the distortion modules from returning zero if the (x, y, z) coordinates,
		// when multiplied by the frequency, are near an integer boundary.  This is
		// due to a property of gradient coherent noise, which returns zero at
		// integer boundaries.
		double x0, y0, z0;
		double x1, y1, z1;
		double x2, y2, z2;
		x0 = x + (12414.0 / 65536.0);
		y0 = y + (65124.0 / 65536.0);
		z0 = z + (31337.0 / 65536.0);
		x1 = x + (26519.0 / 65536.0);
		y1 = y + (18128.0 / 65536.0);
		z1 = z + (60493.0 / 65536.0);
		x2 = x + (53820.0 / 65536.0);
		y2 = y + (11213.0 / 65536.0);
		z2 = z + (44845.0 / 65536.0);
		double xDistort = x + (xDistortModule.getValue(x0, y0, z0) * power);
		double yDistort = y + (yDistortModule.getValue(x1, y1, z1) * power);
		double zDistort = z + (zDistortModule.getValue(x2, y2, z2) * power);

		// Retrieve the output value at the offsetted input value instead of the
		// original input value.
		return sourceModule[0].getValue(xDistort, yDistort, zDistort);

	}

}
