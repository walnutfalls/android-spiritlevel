package com.footbits.sava.oglspiritleveldisplay;

import android.opengl.Matrix;

public class RenderedString
{
    private String text;
    private Transform transform;


    public RenderedString(String text)
    {
        this.text = text;
        transform = new Transform();
    }


    public RenderedString(String text, Transform transform)
    {
        this.text = text;
        this.transform = transform;
    }

    public RenderedString(String text, float x, float y, float z, float xRot, float yRot, float zRot)
    {
        this.text = text;
        transform = new Transform();
        float[] m = transform.getLocalMatrix();

        Matrix.translateM(m, 0,x, y, z);

        Matrix.rotateM(m, 0, xRot, 1, 0, 0);
        Matrix.rotateM(m,0,yRot, 0, 1, 0);
        Matrix.rotateM(m,0,zRot, 0, 0, 1);
    }

    public String getText() { return text; }
    public void setText(String text){ this.text = text; }

    public Transform getTransform() {
        return transform;
    }

    /**
     * TODO: This is a hack, and is not precise. We need better integration with fractous es 2
     * @return The width in GL units of the string.
     */
    public float getWidth() {
        return 23 * text.length();
    }

}
