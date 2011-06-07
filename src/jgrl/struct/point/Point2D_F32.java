/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Java Geometric Regression Library (JGRL).
 *
 * JGRL is free software: you can redistribute it and/or modify
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
 * License along with JGRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.struct.point;

import jgrl.struct.GeoTuple2D_F32;

/**
 * A point in 2D composed of float
 */
public class Point2D_F32 extends GeoTuple2D_F32<Point2D_F32> {
    public Point2D_F32( float x , float y ) {
        set(x,y);
    }

    public Point2D_F32(){}

    public Point2D_F32( Point2D_F32 pt ) {
        set( pt.x , pt.y );
    }

    @Override
    public Point2D_F32 createNewInstance() {
        return new Point2D_F32();
    }

    public void set( Point2D_F32 orig ) {
        _set(orig);
    }

    @Override
    public Point2D_F32 copy() {
        return new Point2D_F32(this);
    }

    public String toString() {
        return "P( "+x+" "+y+" )";
    }
}