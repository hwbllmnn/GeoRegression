/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.fitting.se;

import georegression.fitting.MotionTransformPoint;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se3_F64;

/**
 * @author Peter Abeles
 */
public class TestMotionSe3PointCrossCovariance_F64 extends GeneralMotionSe3Tests_F64 {

	@Override
	MotionTransformPoint<Se3_F64, Point3D_F64> createAlg() {
		return new MotionSe3PointCrossCovariance_F64();
	}

}
