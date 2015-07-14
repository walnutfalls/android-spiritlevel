package com.footbits.sava.oglspiritleveldisplay;

public class Mesh {


    private float[] vertices;
    private int[] indices;

    public Mesh(float[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
    }


    public float[] getVertices() { return vertices; }
    public int[] getIndices() { return indices; }
}
