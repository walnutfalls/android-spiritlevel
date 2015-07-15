package com.footbits.oglwrapper;

/**
 * Created by Sava on 7/14/2015.
 */
public class Color {
    public static final Color WHITE = new Color(1, 1, 1, 1);

    private float r; //0 to 1
    private float g; //0 to 1
    private float b; //0 to 1
    private float a; //0 to 1

    private float[] rgbaArray;


    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.b = b;
        this.g = g;
        this.a = a;

        computeRgbaArray();
    }


    private void computeRgbaArray() {
        if(rgbaArray == null)
            rgbaArray = new float[4];

        rgbaArray[0] = r;
        rgbaArray[1] = g;
        rgbaArray[2] = b;
        rgbaArray[3] = a;
    }


    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
        computeRgbaArray();
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        this.g = g;
        computeRgbaArray();
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
        computeRgbaArray();
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
        computeRgbaArray();
    }

    public float[] getRgbaArray() {
        return rgbaArray;
    }
}
