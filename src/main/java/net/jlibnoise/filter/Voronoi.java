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
package net.jlibnoise.filter;

import net.jlibnoise.MathHelper;
import net.jlibnoise.Module;
import net.jlibnoise.NoiseGen;
import net.jlibnoise.Utils;

/**
 * Noise module that outputs Voronoi cells.
 *
 * In mathematics, a <i>Voronoi cell</i> is a region containing all the
 * points that are closer to a specific <i>seed point</i> than to any
 * other seed point.  These cells mesh with one another, producing
 * polygon-like formations.
 *
 * By default, this noise module randomly places a seed point within
 * each unit cube.  By modifying the <i>frequency</i> of the seed points,
 * an application can change the distance between seed points.  The
 * higher the frequency, the closer together this noise module places
 * the seed points, which reduces the size of the cells.  To specify the
 * frequency of the cells, call the SetFrequency() method.
 *
 * This noise module assigns each Voronoi cell with a random constant
 * value from a coherent-noise function.  The <i>displacement value</i>
 * controls the range of random values to assign to each cell.  The
 * range of random values is +/- the displacement value.  Call the
 * SetDisplacement() method to specify the displacement value.
 *
 * To modify the random positions of the seed points, call the SetSeed()
 * method.
 *
 * This noise module can optionally add the distance from the nearest
 * seed to the output value.  To enable this feature, call the
 * enableDistance() method.  This causes the points in the Voronoi cells
 * to increase in value the further away that point is from the nearest
 * seed point.
 *
 * Voronoi cells are often used to generate cracked-mud terrain
 * formations or crystal-like textures
 *
 * This noise module requires no source modules.
 */
public class Voronoi extends Module {

	// Default displacement to apply to each cell for the noise module.
	public static final double DEFAULT_VORONOI_DISPLACEMENT = 1.0;

	// Default frequency of the seed points for the noise module.
	public static final double DEFAULT_VORONOI_FREQUENCY = 1.0;

	// Default seed of the noise function for the noise module.
	public static final int DEFAULT_VORONOI_SEED = 0;

	// Scale of the random displacement to apply to each Voronoi cell.
	double displacement = DEFAULT_VORONOI_DISPLACEMENT;

	// Determines if the distance from the nearest seed point is applied to the output value.
	boolean enableDistance = false;

	// Frequency of the seed points.
	double frequency = DEFAULT_VORONOI_FREQUENCY;

	// Seed value used by the coherent-noise function to determine the positions of the seed points.
	int seed = DEFAULT_VORONOI_SEED;

	public Voronoi() {
		super(0);
	}

	public double getDisplacement() {
		return displacement;
	}

	public void setDisplacement(double displacement) {
		this.displacement = displacement;
	}

	public boolean isEnableDistance() {
		return enableDistance;
	}

    /**
     * Enables or disables applying the distance from the nearest seed
     * point to the output value.
     *
     * @param enable Specifies whether to apply the distance to the
     * output value or not.
     *
     * Applying the distance from the nearest seed point to the output
     * value causes the points in the Voronoi cells to increase in value
     * the further away that point is from the nearest seed point.
     * Setting this value to true (and setting the displacement to a
     * near-zero value) causes this noise module to generate cracked mud
     * formations.
     */
	public void setEnableDistance(boolean enableDistance) {
		this.enableDistance = enableDistance;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
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
        // This method could be more efficient by caching the seed values.  Fix later.

        x1 *= frequency;
        y1 *= frequency;
        z1 *= frequency;

        int xInt = (x1 > 0.0 ? (int) x1 : (int) x1 - 1);
        int yInt = (y1 > 0.0 ? (int) y1 : (int) y1 - 1);
        int zInt = (z1 > 0.0 ? (int) z1 : (int) z1 - 1);

        double minDist = 2147483647.0;
        double xCandidate = 0;
        double yCandidate = 0;
        double zCandidate = 0;

        // Inside each unit cube, there is a seed point at a random position.  Go
        // through each of the nearby cubes until we find a cube with a seed point
        // that is closest to the specified position.
        for (int zCur = zInt - 2; zCur <= zInt + 2; zCur++) {
            for (int yCur = yInt - 2; yCur <= yInt + 2; yCur++) {
                for (int xCur = xInt - 2; xCur <= xInt + 2; xCur++) {

                    // Calculate the position and distance to the seed point inside of
                    // this unit cube.
                    double xPos = xCur + NoiseGen.valueNoise3D(xCur, yCur, zCur, seed);
                    double yPos = yCur + NoiseGen.valueNoise3D(xCur, yCur, zCur, seed + 1);
                    double zPos = zCur + NoiseGen.valueNoise3D(xCur, yCur, zCur, seed + 2);
                    double xDist = xPos - x1;
                    double yDist = yPos - y1;
                    double zDist = zPos - z1;
                    double dist = xDist * xDist + yDist * yDist + zDist * zDist;

                    if (dist < minDist) {
                        // This seed point is closer to any others found so far, so record
                        // this seed point.
                        minDist = dist;
                        xCandidate = xPos;
                        yCandidate = yPos;
                        zCandidate = zPos;
                    }
                }
            }
        }

        double value;
        if (enableDistance) {
            // Determine the distance to the nearest seed point.
            double xDist = xCandidate - x1;
            double yDist = yCandidate - y1;
            double zDist = zCandidate - z1;
            value = (MathHelper.sqrt(xDist * xDist + yDist * yDist + zDist * zDist)) * Utils.SQRT_3 - 1.0;
        } else {
            value = 0.0;
        }

        // Return the calculated distance with the displacement value applied.
        return value + (displacement * NoiseGen.valueNoise3D((int) (MathHelper.floor(xCandidate)), (int) (MathHelper.floor(yCandidate)), (int) (MathHelper.floor(zCandidate)), seed));

    }

}
