package com.texample2;

public class RenderedString
{
    private float x;
    private float y;
    private float z;

    private float xRot;
    private float zRot;
    private float yRot;

    private String text;

    public RenderedString(String text)
    {
        this.text = text;
    }

    public RenderedString(String text, float x, float y, float z, float xRot, float yRot, float zRot)
    {
        this.text = text;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xRot = xRot;
        this.yRot = yRot;
        this.zRot = zRot;

    }

    public String getText() { return text; }
    public void setText(String text){ this.text = text; }

    public float getyRot() { return yRot; }
    public void setyRot(float yRot) { this.yRot = yRot; }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }

    public float getZ() { return z; }
    public void setZ(float z) { this.z = z; }

    public float getxRot() { return xRot; }
    public void setxRot(float xRot) { this.xRot = xRot; }

    public float getzRot() { return zRot; }
    public void setzRot(float zRot) { this.zRot = zRot; }
}
