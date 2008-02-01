/*
 * Copyright (c) 2008, Your Corporation. All Rights Reserved.
 */
package com.jme.math;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/*
 * $Log: TestMatrix3f.java,v $
 * Revision 1.1  2008/01/07 16:14:00  irrisor
 * topic 6810: moved test code
 *
 */

/**
 * @see Matrix4f
 */
public class TestMatrix3f {

    @Test
    public void testTranspose() {
        Matrix3f a = new Matrix3f();
        Quaternion q = new Quaternion();
        q.fromAngles(0.1f, 0.2f, 0.3f);
        a.set(q);
        Matrix3f b = a.transposeNew();
        assertTrue(equalIdentity(b.mult(a)));
        assertTrue(equalIdentity(a.mult(b)));
    }

    @Test
    public void testScale() {
        Matrix3f a = new Matrix3f(0, 1, 0, -1, 0, 0, 0, 0, 1);
        Matrix3f b = a.transposeNew();
        a.scale(new Vector3f(2, 2, 2));
        assertTrue(a.mult(new Vector3f(1, 0, 0)).lengthSquared() == 4);
        b.scale(new Vector3f(0.5f, 0.5f, 0.5f));
        assertTrue(equalIdentity(a.mult(b)));
    }

    private boolean equalIdentity(Matrix3f mat) {
        if (Math.abs(mat.m00 - 1) > 1e-4) return false;
        if (Math.abs(mat.m11 - 1) > 1e-4) return false;
        if (Math.abs(mat.m22 - 1) > 1e-4) return false;

        if (Math.abs(mat.m01) > 1e-4) return false;
        if (Math.abs(mat.m02) > 1e-4) return false;

        if (Math.abs(mat.m10) > 1e-4) return false;
        if (Math.abs(mat.m12) > 1e-4) return false;

        if (Math.abs(mat.m20) > 1e-4) return false;
        if (Math.abs(mat.m21) > 1e-4) return false;

        return true;
    }
}