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

    public void createMarks(int totalSpacepx, int markGap, int markLen, int screenWidth)
    {
        int spaceHalf = totalSpacepx / 2;
        int numMarks = totalSpacepx / markGap;



        for(int i = -spaceHalf; i < numMarks; i++)
        {
            RenderedObject ro = new RenderedObject(axisMarkMesh, glslProgram);

            Matrix.translateM(ro.getTransform().modelMatrix, 0,
                    -screenWidth/2 + markLen/2,
                    i * markGap, 0);

            Matrix.scaleM(ro.getTransform().modelMatrix, 0, markLen, 3, 1);

            ro.createBuffers();

            marks.add(ro);
        }
    }

    public ArrayList<RenderedObject> getMarks() {
        return marks;
    }
}
