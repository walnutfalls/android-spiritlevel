package com.footbits.sava.oglspiritleveldisplay;




import com.footbits.oglwrapper.Color;
import com.footbits.oglwrapper.GlslProgram;
import com.footbits.oglwrapper.IndexBuffer;
import com.footbits.oglwrapper.VertexBuffer;

import java.util.HashMap;


public class RenderedObject {
    private class Buffers
    {
        public Buffers(VertexBuffer vb, IndexBuffer ib) { vertexBuffer = vb; indexBuffer = ib; }

        public VertexBuffer vertexBuffer;
        public IndexBuffer indexBuffer;
    }

    // If the rendered object get initialized with the same mesh, we can use the same
    // vertex buffer objects. Keep track of meshes that have been processed into VBO/IBO
    // in this map.
    private static final HashMap<Mesh, Buffers> meshBuffersHashMap = new HashMap<>();


    private Mesh mesh;
    private Transform transform;
    private GlslProgram program;

    private VertexBuffer glVertexBuffer;
    private IndexBuffer glIndexBuffer;

    private Color color;

    public RenderedObject(Mesh mesh, GlslProgram program) {
        transform = new Transform();
        color = Color.WHITE;

        this.mesh = mesh;
        this.program = program;
    }


    public GlslProgram getProgram() {
        return program;
    }

    public void createBuffers() {
        Buffers buffs = meshBuffersHashMap.get(mesh);

        if(buffs == null) {
            glVertexBuffer = new VertexBuffer(mesh.getVertices());
            glIndexBuffer = new IndexBuffer(mesh.getIndices());

            buffs = new Buffers(glVertexBuffer, glIndexBuffer);
            meshBuffersHashMap.put(mesh, buffs);
        }
        else {
            glVertexBuffer = buffs.vertexBuffer;
            glIndexBuffer = buffs.indexBuffer;
        }
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
