package com.footbits.sava.oglspiritleveldisplay;


import com.footbits.oglwrapper.GlslProgram;
import com.footbits.oglwrapper.Uniform;
import com.footbits.oglwrapper.GlBuffer;

import java.util.ArrayList;

public class RenderedObject {
    private Mesh mesh;
    private Transform transform;
    private GlslProgram program;

    private ArrayList<Uniform> uniforms;

    public RenderedObject(Mesh mesh, GlslProgram program) {
        transform = new Transform();

        this.mesh = mesh;
        this.program = program;
        this.uniforms = new ArrayList<>();
    }

    //buffer objects
    private GlBuffer indexBO;
    private GlBuffer vertexBO;

    public GlslProgram getProgram() {
        return program;
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

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }
}
