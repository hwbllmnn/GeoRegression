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

package jgrl.test;

import jgrl.struct.GeoTuple2D_F32;
import jgrl.struct.GeoTuple2D_F64;
import jgrl.struct.GeoTuple3D_F64;
import jgrl.struct.GeoTuple_F64;
import jgrl.struct.point.UtilPoint2D;
import jgrl.struct.se.Se2;


/**
 * @author Peter Abeles
 */
public class GeometryUnitTest {

    public static void assertEquals( Se2 expected , Se2 found , double tolTran , double tolyaw )
    {
        assertEquals(expected.getTranslation(),found.getTranslation(),tolTran);
        assertEquals(expected.getYaw(),found.getYaw(),tolyaw,"yaw");
    }

    /**
     * Sees if every parameter in expected is not each to each other.  Note that this test
     * passes if only one is not equals.
     *
     * @param expected The expected transform.
     * @param found The found transform.
     * @param tolTran tolerance used for translational parameters.
     * @param tolYaw tolerance used for rotational parameters.
     */
    public static void assertNotEquals( Se2 expected , Se2 found , double tolTran , double tolYaw )
    {
        if( !UtilPoint2D.isEquals(expected.getTranslation(),found.getTranslation(),tolTran) )
            return;

        assertNotEquals(expected.getYaw(),found.getYaw(),tolYaw,"yaw");
    }

    public static void assertEquals( GeoTuple3D_F64 expected , GeoTuple3D_F64 found, double tol ) {
        assertEquals(expected.getX(),found.getX(),tol,"x-axis is not equals.");
        assertEquals(expected.getY(),found.getY(),tol,"y-axis is not equals.");
        assertEquals(expected.getZ(),found.getZ(),tol,"z-axis is not equals.");
    }

    public static void assertEquals( GeoTuple2D_F64 expected , GeoTuple2D_F64 found, double tol ) {
        assertEquals(expected.getX(),found.getX(),tol,"x-axis is not equals.");
        assertEquals(expected.getY(),found.getY(),tol,"y-axis is not equals.");
    }

	public static void assertEquals( GeoTuple2D_F32 expected , GeoTuple2D_F32 found, double tol ) {
        assertEquals(expected.getX(),found.getX(),tol,"x-axis is not equals.");
        assertEquals(expected.getY(),found.getY(),tol,"y-axis is not equals.");
    }

    public static void assertNotEquals( GeoTuple2D_F64 expected , GeoTuple2D_F64 found, double tol ) {
        assertNotEquals(expected.getX(),found.getX(),tol,"x-axis is equal.");
        assertNotEquals(expected.getY(),found.getY(),tol,"y-axis is equal.");
    }

    public static void assertEquals( GeoTuple3D_F64 a , double x , double y , double z , double tol ) {
        assertEquals(a.getX(),x,tol,"x-axis is not equals.");
        assertEquals(a.getY(),y,tol,"y-axis is not equals.");
        assertEquals(a.getZ(),z,tol,"z-axis is not equals.");
    }

    public static void assertEquals( GeoTuple_F64 a , GeoTuple_F64 b , double tol ) {
        assertTrue(a.getClass() == b.getClass(),"a and b are not the same type.");

        int N = a.getDimension();
        for( int i = 0; i < N; i++ ) {
            assertEquals(a.getIndex(i),b.getIndex(i),tol,"Index "+i+" is not the same.");
        }
    }

    public static void assertEquals( double valueA , double valueB , double tol , String message ) {
        if( Math.abs(valueA-valueB) > tol )
            throw new RuntimeException(message+" "+valueA+"  "+valueB);
    }

    public static void assertNotEquals( double valueA , double valueB , double tol , String message ) {
        if( Math.abs(valueA-valueB) <= tol )
            throw new RuntimeException(message+" "+valueA+"  "+valueB);
    }

    public static void assertTrue( boolean value , String message ) {
        if( !value )
            throw new RuntimeException(message);
    }
}