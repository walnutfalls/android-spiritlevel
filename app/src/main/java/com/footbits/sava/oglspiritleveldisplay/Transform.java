package com.footbits.sava.oglspiritleveldisplay;

import android.opengl.Matrix;

/**
 * Created by Sava on 7/12/2015.
 */
public class Transform {

    public float[] modelMatrix;

    public Transform() {
        modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);
    }
}
