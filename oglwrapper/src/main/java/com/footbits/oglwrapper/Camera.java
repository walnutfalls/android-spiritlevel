package com.footbits.oglwrapper;

import android.content.Context;
import android.opengl.Matrix;
import android.util.DisplayMetrics;
import android.view.Display;

import static android.opengl.Matrix.orthoM;

/**
 * Created by Sava on 7/9/2015.
 */
public class Camera {

    public enum Projection
    {
        Perspective,
        Orthographic
    }

    private Projection projection;

    private float[] projectionMatrix;
    private float[] viewMatrix;

    private int screenWidth;
    private int screenHeight;


    public Camera(Context context, Projection projection)
    {
        this.projection = projection;
        this.projectionMatrix = new float[16];
        this.viewMatrix = new float[16];

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }


    public void lookAt(float eyeX, float eyeY, float eyeZ,
                       float centerX, float centerY, float centerZ,
                       float upX, float upY, float upZ)
    {
        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    public void setScreenDimensions(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        computeProjection();
    }

    public void getProjectionViewnMatrix(float[] matrix)
    {
        Matrix.multiplyMM(matrix, 0, getProjectionMatrix(), 0, getViewMatrix(), 0);
    }


    private void computeProjection() {
        switch (projection)
        {
            case Orthographic:
                setOrthographic();
                break;
            case Perspective:
                setPerspective();
                break;
            default: break;
        }
    }

    private void setOrthographic() {
        float halfWidth = screenWidth / 2;
        float halfHeight = screenHeight / 2;

        orthoM(projectionMatrix, 0, -halfWidth, halfWidth, -halfHeight, halfHeight,
                Float.MIN_VALUE, Float.MAX_VALUE);
    }

    private void setPerspective() {
        final float aspectRatio = screenWidth > screenHeight ?
                (float) screenWidth / (float) screenHeight :
                (float) screenHeight / (float) screenWidth;


        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1, 1, 3, 7);
    }


    public float[] getProjectionMatrix() {
        return projectionMatrix;
    }

    public void setProjectionMatrix(float[] projection) {
        this.projectionMatrix = projection;
    }

    public void setProjection(Projection projection)
    {
        this.projection = projection;
        computeProjection();
    }

    public float[] getViewMatrix() {
        return viewMatrix;
    }

    public void setViewMatrix(float[] viewMatrix) {
        this.viewMatrix = viewMatrix;
    }
}
