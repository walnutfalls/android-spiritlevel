package com.footbits.sava.oglspiritleveldisplay;

import android.opengl.Matrix;

import com.footbits.oglwrapper.MatrixUniform;

import java.util.ArrayList;


public class Transform {

    // Transformation from model space to the space of the parent's transform.
    private float[] modelMatrix;

    // Transformation from model space to world space.
    private float[] globalModelMatrix;


    private ArrayList<Transform> children;
    private Transform parent;


    public Transform() {
        modelMatrix = new float[16];
        globalModelMatrix = new float[16];

        Matrix.setIdentityM(globalModelMatrix, 0);
        Matrix.setIdentityM(modelMatrix, 0);

        children = new ArrayList<>();
    }

    public Transform(Transform parent) {
        this();

        this.parent = parent;
    }

    public void setNoRotation() {
       Transform.setNoRotation(modelMatrix);
    }

    public static void setNoRotation(float[] matrix) {
        matrix[1] = matrix[2] = matrix[4] = matrix[6] = matrix[8]= matrix[9] = 0;

        matrix[0] = matrix[5] = matrix[10] = 1;
    }


    public float getLocalX() { return modelMatrix[12]; }
    public void setLocalX(float x) { modelMatrix[12] = x; }

    public float getLocalY() { return modelMatrix[13]; }
    public void setLocalY(float y) { modelMatrix[13] = y; }

    public float getLocalZ() { return modelMatrix[14]; }
    public void setLocalZ(float z) { modelMatrix[14] = z; }

    public Transform getParent() { return parent; }
    public void setParent(Transform parent) { this.parent = parent; }

    public float[] getLocalMatrix() { return modelMatrix; }
    public float[] getGlobalMatrix() {
        if(parent == null) return modelMatrix;

        Matrix.multiplyMM(globalModelMatrix, 0, parent.getGlobalMatrix(), 0, modelMatrix, 0);

        return globalModelMatrix;
    }

}
