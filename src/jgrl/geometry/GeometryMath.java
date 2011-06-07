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

package jgrl.geometry;

import jgrl.struct.GeoTuple2D_F64;
import jgrl.struct.GeoTuple3D_F64;
import jgrl.struct.point.Point3D_F64;
import jgrl.struct.point.Vector3D_F64;
import org.ejml.alg.dense.mult.VectorVectorMult;
import org.ejml.data.DenseMatrix64F;


/**
 * Math operations that can be applied to geometric primatives.
 *
 * @author Peter Abeles
 */
// todo separate off fucntions that are in homogeneous coordinates into their own class?
//      alternatively indicate by the function name?
// todo make sure all functions have unit tests
@SuppressWarnings({"unchecked", "RedundantCast"})
public class GeometryMath {

    /**
     * Creates a skew symmetric cross product matrix from the provided tuple.
     *
     * @param x0 Element 0.
     * @param x1 Element 1.
     * @param x2 Element 2.
     * @param ret If not null the results are stored here, otherwise a new matrix is created.
     * @return Skew symmetric cross product matrix.
     */
    public static DenseMatrix64F crossMatrix( double x0 , double x1 , double x2 , DenseMatrix64F ret ) {
        if( ret == null ) {
            ret = new DenseMatrix64F(3,3);
        } else {
            ret.zero();
        }

        ret.set(0,1,-x2);
        ret.set(0,2,x1);
        ret.set(1,0,x2);
        ret.set(1,2,-x0);
        ret.set(2,0,-x1);
        ret.set(2,1,x0);

        return ret;
    }

    public static DenseMatrix64F crossMatrix( GeoTuple3D_F64 v , DenseMatrix64F ret ) {
        if( ret == null ) {
            ret = new DenseMatrix64F(3,3);
        } else {
            ret.zero();
        }

        double x = v.getX();
        double y = v.getY();
        double z = v.getZ();

        ret.set(0,1,-z);
        ret.set(0,2,y);
        ret.set(1,0,z);
        ret.set(1,2,-x);
        ret.set(2,0,-y);
        ret.set(2,1,x);

        return ret;
    }

    /**
     * <p>
     * Computes the cross product:<br>
     * <br>
     * c = a x b
     * </p>
     *
     * @param a Not modified.
     * @param b Not modified.
     * @param c Modified.
     */
    public static void cross( GeoTuple3D_F64 a , GeoTuple3D_F64 b , GeoTuple3D_F64 c ) {
        c.x = a.y*b.z - a.z*b.y;
        c.y = a.z*b.x - a.x*b.z;
        c.z = a.x*b.y - a.y*b.x;
    }

    /**
     * <p>
     * Adds two points together.<br>
     * <br>
     * c = a + b
     * </p>
     *
     * <p>
     * Point 'c' can be the same instance as 'a' or 'b'.
     * </p>
     * @param a A point. Not modified.
     * @param b A point. Not modified.
     * @param c Where the results are stored. Modified.
     */
    public static void add( GeoTuple3D_F64 a , GeoTuple3D_F64 b , GeoTuple3D_F64 c ) {
        c.x = a.x + b.x;
        c.y = a.y + b.y;
        c.z = a.z + b.z;
    }

    public static void add( double a0 , GeoTuple3D_F64 pt0 , double a1 , GeoTuple3D_F64 pt1 , GeoTuple3D_F64 pt3 ) {
        pt3.x = a0*pt0.x + a1*pt1.x;
        pt3.y = a0*pt0.y + a1*pt1.y;
        pt3.z = a0*pt0.z + a1*pt1.z;
    }

    /**
     * ret = p0 + M*p1
     *
     * @param p0
     * @param M
     * @param p1
     * @param ret
     */
    public static <T extends GeoTuple3D_F64> T addMult( T p0 , DenseMatrix64F M , T p1 , T ret ) {
        ret = mult(M,p1,ret);
        ret.x += p0.x;
        ret.y += p0.y;
        ret.z += p0.z;

        return ret;
    }

    /**
     * <p>
     * Substracts two points from each other.<br>
     * <br>
     * c = a - b
     * </p>
     *
     * <p>
     * Point 'c' can be the same instance as 'a' or 'b'.
     * </p>
     * @param a A point. Not modified.
     * @param b A point. Not modified.
     * @param c Where the results are stored. Modified.
     */
    public static void sub( GeoTuple3D_F64 a , GeoTuple3D_F64 b , GeoTuple3D_F64 c ) {
        c.x = a.x - b.x;
        c.y = a.y - b.y;
        c.z = a.z - b.z;
    }

    /**
     * <p>
     * Performs the following operation on the point:<br>
     * <br>
     * n = M*p
     * </p>
     * <p>
     * Both 'orig' and 'mod' can be the same point.
     * </p>
     *
     * @param M The transform being applied to the point.  Not modified.
     * @param orig The point being transformed.  Not Modified.
     * @param mod The result of 'orig' being transformed by M. Modified.
     * @param forward Should it perform the rotation in the forward or reverse direction
     */
    public static void rotate( DenseMatrix64F M , Point3D_F64 orig , Point3D_F64 mod , boolean forward )
    {
        if( forward ) {
            mult(M,orig,mod);
        } else {
            multTran(M,orig,mod);
        }
    }

    /**
     * Performs the following operation on the vector:
     *
     * p = M*p
     *
     * <p>
     * Both orig and mod can be the same vector.
     * </p>
     *
     * @param M The transform being applied to the vector.  Not modified.
     * @param v The vector being transformed.  Modified.
     */
    public static void rotate( DenseMatrix64F M , Vector3D_F64 v , Vector3D_F64 mod, boolean forward)
    {
        if( forward ) {
            mult(M,v,mod);
        } else {
            multTran(M,v,mod);
        }
    }

    /**
     * Rotates a 2D point by the specified angle.
     *
     * @param theta
     * @param pt
     * @param solution where the solution is written to.  Can be the same point as 'pt'.
     */
    public static void rotate( double theta , GeoTuple2D_F64 pt , GeoTuple2D_F64 solution ) {
        double c = Math.cos(theta);
        double s = Math.sin(theta);

        double x = pt.x;
        double y = pt.y;

        solution.x = c*x - s*y;
        solution.y = s*x + c*y;
    }

    /**
     *
     * mod = M*pt
     * <p>
     * pt and mod can be the same reference.
     * </p>
     * 
     * @param M
     * @param pt
     * @param mod
     */
    public static <T extends GeoTuple3D_F64> T mult( DenseMatrix64F M , T pt , T mod )
    {
        if( M.numRows != 3 || M.numCols != 3 )
            throw new IllegalArgumentException("Input matrix must be 3 by 3, not "+M.numRows+" "+M.numCols);

        if( mod == null ) {
            mod = (T)pt.createNewInstance();
        }

        double x = pt.x;
        double y = pt.y;
        double z = pt.z;

        mod.x = M.get(0,0)*x + M.get(0,1)*y + M.get(0,2)*z;
        mod.y = M.get(1,0)*x + M.get(1,1)*y + M.get(1,2)*z;
        mod.z = M.get(2,0)*x + M.get(2,1)*y + M.get(2,2)*z;

        return (T)mod;
    }

    public static GeoTuple2D_F64 mult( DenseMatrix64F M , GeoTuple3D_F64 pt , GeoTuple2D_F64 mod )
    {
        if( M.numRows != 3 || M.numCols != 3 )
            throw new IllegalArgumentException("Input matrix must be 3 by 3, not "+M.numRows+" "+M.numCols);

        double x = pt.x;
        double y = pt.y;
        double z = pt.z;

        mod.x = M.get(0,0)*x + M.get(0,1)*y + M.get(0,2)*z;
        mod.y = M.get(1,0)*x + M.get(1,1)*y + M.get(1,2)*z;
        z = M.get(2,0)*x + M.get(2,1)*y + M.get(2,2)*z;

        mod.x /= z;
        mod.y /= z;

        return mod;
    }

    public static GeoTuple3D_F64 mult( DenseMatrix64F M , GeoTuple2D_F64 pt , GeoTuple3D_F64 mod )
    {
        if( M.numRows != 3 || M.numCols != 3 )
            throw new IllegalArgumentException("Input matrix must be 3 by 3, not "+M.numRows+" "+M.numCols);

        if( mod == null ) {
            throw new IllegalArgumentException("Must provide an instance in mod");
        }

        double x = pt.x;
        double y = pt.y;

        mod.x = M.get(0,0)*x + M.get(0,1)*y + M.get(0,2);
        mod.y = M.get(1,0)*x + M.get(1,1)*y + M.get(1,2);
        mod.z = M.get(2,0)*x + M.get(2,1)*y + M.get(2,2);

        return mod;
    }

    /**
     * pt and mod are assumed to be homogeneous coordinates (z=1).  M is multiplied by
     * [pt.x , pt.y , 1] and mod is computed by computing (x,y,z) then [x/z , y/z , 1].
     *
     * @param M
     * @param pt
     * @param mod
     * @return
     */
    public static GeoTuple2D_F64 mult( DenseMatrix64F M , GeoTuple2D_F64 pt , GeoTuple2D_F64 mod )
    {
        if( M.numRows != 3 || M.numCols != 3 )
            throw new IllegalArgumentException("Input matrix must be 3 by 3, not "+M.numRows+" "+M.numCols);

        if( mod == null ) {
            throw new IllegalArgumentException("Must provide an instance in mod");
        }

        double x = pt.x;
        double y = pt.y;

        double modz = M.get(2,0)*x + M.get(2,1)*y + M.get(2,2);

        mod.x = (M.get(0,0)*x + M.get(0,1)*y + M.get(0,2))/modz;
        mod.y = (M.get(1,0)*x + M.get(1,1)*y + M.get(1,2))/modz;

        return mod;
    }

    public static <T extends GeoTuple3D_F64> T multTran( DenseMatrix64F M , T pt , T mod )
    {
        if( M.numRows != 3 || M.numCols != 3 )
            throw new IllegalArgumentException("Rotation matrices are 3 by 3.");

        if( mod == null ) {
            mod = (T)pt.createNewInstance();
        }

        double x = pt.x;
        double y = pt.y;
        double z = pt.z;

        mod.x = M.get(0,0)*x + M.get(1,0)*y + M.get(2,0)*z;
        mod.y = M.get(0,1)*x + M.get(1,1)*y + M.get(2,1)*z;
        mod.z = M.get(0,2)*x + M.get(1,2)*y + M.get(2,2)*z;

        return (T)mod;
    }

    public static GeoTuple3D_F64 multTran( DenseMatrix64F M , GeoTuple2D_F64 pt , GeoTuple3D_F64 mod )
    {
        if( M.numRows != 3 || M.numCols != 3 )
            throw new IllegalArgumentException("Rotation matrices are 3 by 3.");

        if( mod == null ) {
            throw new IllegalArgumentException("Must provide an instance in mod");
        }

        double x = pt.x;
        double y = pt.y;

        mod.x = M.get(0,0)*x + M.get(1,0)*y + M.get(2,0);
        mod.y = M.get(0,1)*x + M.get(1,1)*y + M.get(2,1);
        mod.z = M.get(0,2)*x + M.get(1,2)*y + M.get(2,2);

        return mod;
    }

    public static <T extends GeoTuple3D_F64> T mult( GeoTuple3D_F64 pt , DenseMatrix64F M , GeoTuple3D_F64 mod )
    {
        if( M.numRows != 3 || M.numCols != 3 )
            throw new IllegalArgumentException("Rotation matrices are 3 by 3.");

        return (T)multTran(M,pt,mod);
    }

    /**
     *
     * @param pt Homogeneous coordinate with the last element assumed to be 1 (x,y,1)
     * @param M
     * @param mod
     * @param <T>
     * @return
     */
    public static <T extends GeoTuple3D_F64> T mult( GeoTuple2D_F64 pt , DenseMatrix64F M , GeoTuple3D_F64 mod )
    {
        if( M.numRows != 3 || M.numCols != 3 )
            throw new IllegalArgumentException("Rotation matrices are 3 by 3.");

        return (T)multTran(M,pt,mod);
    }

    /**
     * <p>
     * ret = a<sup>T</sup>*M*b
     * </p>
     *
     * @param a
     * @param M
     * @param b
     * @return
     */
    public static double innerProd( GeoTuple3D_F64 a , DenseMatrix64F M , GeoTuple3D_F64 b ) {
        if( M.numRows != 3 || M.numCols != 3 )
            throw new IllegalArgumentException("M must be 3 by 3.");

        DenseMatrix64F m1 = new DenseMatrix64F(3,1,true,a.x,a.y,a.z);
        DenseMatrix64F m2 = new DenseMatrix64F(3,1,true,b.x,b.y,b.z);

        return VectorVectorMult.innerProdA(m1,M,m2);
    }

    /**
     * <p>
     * Computes the inner matrix product:<br>
     * ret = x<sup>T</sup>A<sup>T</sup>y
     * </p>
     *
     * @param a 3D point.
     * @param M 3 by 3 matrix.
     * @param b 3D point.
     * @return scalar number
     */
    public static double innerProdTranM( GeoTuple3D_F64 a , DenseMatrix64F M , GeoTuple3D_F64 b ) {
        if( M.numRows != 3 || M.numCols != 3 )
            throw new IllegalArgumentException("M must be 3 by 3.");

        DenseMatrix64F m1 = new DenseMatrix64F(3,1,true,a.x,a.y,a.z);
        DenseMatrix64F m2 = new DenseMatrix64F(3,1,true,b.x,b.y,b.z);

        return VectorVectorMult.innerProdTranA(m1,M,m2);
    }

    /**
     * <p>
     * Computes the inner matrix product: ret = a'*M*b'<br>
     * where ret is a scalar number. 'a' and 'b' are automatically converted into homogeneous
     * coordinates.
     * </p>
     *
     * @param a 2D point.
     * @param M 3 by 3 matrix.
     * @param b 2D point.
     * @return scalar number,
     */
    public static double innerProd( GeoTuple2D_F64 a , DenseMatrix64F M , GeoTuple2D_F64 b ) {
        if( M.numRows != 3 || M.numCols != 3 )
            throw new IllegalArgumentException("M must be 3 by 3.");

        DenseMatrix64F m1 = new DenseMatrix64F(3,1,true,a.x,a.y,1);
        DenseMatrix64F m2 = new DenseMatrix64F(3,1,true,b.x,b.y,1);

        return VectorVectorMult.innerProdA(m1,M,m2);
    }

    /**
     * <p>
     * Dot product: ret = a<sup>T</sup>b
     * </p>
     *
     * @param a A tuple.
     * @param b A tuple.
     * @return scalar
     */
    public static double dot( GeoTuple3D_F64 a , GeoTuple3D_F64 b ) {
        return a.x*b.x + a.y*b.y + a.z*b.z;
    }

    /**
     * <p>
     * Multiplies each element in the tuple by 'v'.<br>
     * p<sub>i</sub>=p<sub>i</sub>*v
     * </p>
     *
     * @param p tuple.
     * @param v scaling factor.
     */
    public static void scale(GeoTuple3D_F64 p, double v) {
        p.x *= v;
        p.y *= v;
        p.z *= v;
    }

    /**
     * <p>
     * Changes the sign of the vector:<br>
     * <br>
     * T = -T
     * </p>
     * @param t Vector whose sign is being changed.  Modified.
     */
    public static void changeSign(Vector3D_F64 t) {
        t.x = -t.x;
        t.y = -t.y;
        t.z = -t.z;
    }
}