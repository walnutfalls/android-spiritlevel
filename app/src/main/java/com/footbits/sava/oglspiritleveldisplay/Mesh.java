package com.footbits.sava.oglspiritleveldisplay;

import com.footbits.oglwrapper.IndexBuffer;
import com.footbits.oglwrapper.VertexBuffer;



public class Mesh {
    private static final int FLOAT_SIZE = Float.SIZE/8;
    private static final int INT_SIZE = Integer.SIZE/8;


    private float[] vertices;
    private int[] indices;

    private VertexBuffer glVertexBuffer;
    private IndexBuffer glIndexBuffer;


    public void createBuffers() {
        glVertexBuffer = new VertexBuffer(vertices);
        glIndexBuffer = new IndexBuffer(indices);
    }

    public float[] getVertices() { return vertices; }
    public void setVertices(float[] vertices) { this.vertices = vertices; }
    public int[] getIndices() { return indices; }
    public void setIndices(int[] indices) { this.indices = indices; }

    public VertexBuffer getGlVertexBuffer() {return glVertexBuffer;}
    public IndexBuffer getGlIndexBuffer() {return glIndexBuffer;}

}
