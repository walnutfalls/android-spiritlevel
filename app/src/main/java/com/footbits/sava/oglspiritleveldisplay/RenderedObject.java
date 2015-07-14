package com.footbits.sava.oglspiritleveldisplay;


import com.footbits.oglwrapper.GlslProgram;
import com.footbits.oglwrapper.IndexBuffer;
import com.footbits.oglwrapper.VertexBuffer;



public class RenderedObject {
    private Mesh mesh;
    private Transform transform;
    private GlslProgram program;

    private VertexBuffer glVertexBuffer;
    private IndexBuffer glIndexBuffer;

    public RenderedObject(Mesh mesh, GlslProgram program) {
        transform = new Transform();

        this.mesh = mesh;
        this.program = program;
    }


    public GlslProgram getProgram() {
        return program;
    }

    public void createBuffers() {
        glVertexBuffer = new VertexBuffer(mesh.getVertices());
        glIndexBuffer = new IndexBuffer(mesh.getIndices());
    }

    public void setProgram(GlslProgram program) {
        this.program = program;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Transform getTransform() { return transform; }
    public void setTransform(Transform transform) {this.transform = transform;}


    public VertexBuffer getGlVertexBuffer() {return glVertexBuffer;}
    public IndexBuffer getGlIndexBuffer() {return glIndexBuffer;}
}
