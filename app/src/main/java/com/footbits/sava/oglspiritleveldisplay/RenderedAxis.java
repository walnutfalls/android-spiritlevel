package com.footbits.sava.oglspiritleveldisplay;

import android.opengl.Matrix;

import com.footbits.oglwrapper.GlslProgram;
import com.footbits.oglwrapper.IndexBuffer;
import com.footbits.oglwrapper.VertexBuffer;

import java.util.ArrayList;

/**
 * Created by Sava on 7/13/2015.
 */
public class RenderedAxis {
    private Mesh axisMarkMesh;
    private GlslProgram glslProgram;

    private ArrayList<RenderedObject> marks;

    public RenderedAxis(GlslProgram glslProgram){
        this.glslProgram = glslProgram;

        marks = new ArrayList<>();

        axisMarkMesh = new ShapeBuilder(true)
                .addQuad(
                        0.6f, 0.6f, -1.0f,
                        -0.6f, 0.6f, -1.0f,
                        -0.6f, -0.6f, -1.0f,
                        0.6f, -0.6f, -1.0f
                )
                .toMesh();
    }

    public void createMarks()
    {
        for(int i = -500; i < 10; i++)
        {
            RenderedObject ro = new RenderedObject(axisMarkMesh, glslProgram);

            Matrix.translateM(ro.getTransform().modelMatrix, 0, 0, i * 50, 0);
            Matrix.scaleM(ro.getTransform().modelMatrix, 0, 30, 3, 1);

            marks.add(ro);
        }
    }

    public ArrayList<RenderedObject> getMarks() {
        return marks;
    }
}
