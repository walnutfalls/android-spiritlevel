package com.footbits.sava.oglspiritleveldisplay;

import android.opengl.Matrix;

import com.footbits.oglwrapper.GlslProgram;
import com.footbits.oglwrapper.IndexBuffer;
import com.footbits.oglwrapper.MatrixUniform;
import com.footbits.oglwrapper.VertexBuffer;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * The view for an AngleRange model
 */
public class RenderedAxis {
    private static final int MARKING_HEIGHT = 5;

    private Mesh axisMarkMesh;
    private GlslProgram glslProgram;
    private AngleExtent extent;

    private ArrayList<RenderedObject> marks;

    private Transform axisTransform;

    public RenderedAxis(AngleExtent extent, GlslProgram glslProgram){
        this.glslProgram = glslProgram;
        this.extent = extent;

        marks = new ArrayList<>();
        axisTransform = new Transform();

        axisMarkMesh = new ShapeBuilder(true)
                .addQuad(
                        0.5f, 0.5f, -1.0f,
                        -0.5f, 0.5f, -1.0f,
                        -0.5f, -0.5f, -1.0f,
                        0.5f, -0.5f, -1.0f
                )
                .toMesh();
    }

    /**
     * Creates axis marking RenderedObjects.
     * @param totalSpace in open gl units, the space taken up by the axis
     * @param markLen length of the markings.
     */
    public void createMarks(int totalSpace, int markLen)
    {
        Matrix.translateM(axisTransform.modelMatrix, 0, markLen/2, 0, 0);

        double orderOfMagnitude = Math.floor(Math.log10(extent.get()));
        double incAngle = Math.pow(10, orderOfMagnitude - 1);

        int incPos = (int)((incAngle / extent.get()) * totalSpace);

        //draw upper half, starting with the mark at zero
        double halfExtent = extent.get()/2.0f;
        double currentAngle = 0;
        int currentPos = 0;
        int numMarks = 0;

        //draw upper half
        while (currentAngle <= halfExtent) {
            RenderedObject ro = addRenderedObject();
            float[] mm = ro.getTransform().modelMatrix;

            Matrix.translateM(mm, 0, 0, currentPos, 0);

            // draw bigger marks at increments of 10, and at angle 0
            if (numMarks == 10 || currentAngle == 0) {
                scaleExtra(mm, markLen);
                numMarks = 0;
            }
            else
                Matrix.scaleM(mm, 0, markLen, MARKING_HEIGHT, 1); //scale normally

            //move so that marking aligns with y axis
            float[] temp = new float[16];
            Matrix.multiplyMM(temp,0, axisTransform.modelMatrix, 0, mm, 0);
            System.arraycopy(temp, 0, ro.getTransform().modelMatrix, 0, 16);

            numMarks++;
            currentPos += incPos;
            currentAngle += incAngle;
        }

        currentAngle = -incAngle;
        currentPos = -incPos;
        numMarks = 1;

        //draw lower half
        while (currentAngle >= -halfExtent) {
            RenderedObject ro = addRenderedObject();
            float[] mm = ro.getTransform().modelMatrix;

            Matrix.translateM(mm, 0, 0, currentPos, 0);

            // draw bigger marks at increments of 10, and at angle 0
            if (numMarks == 10 || currentAngle == 0) {
                scaleExtra(mm, markLen);
                numMarks = 0;
            }
            else
                Matrix.scaleM(mm, 0, markLen, MARKING_HEIGHT, 1);

            //move so that marking aligns with y axis
            float[] temp = new float[16];
            Matrix.multiplyMM(temp,0, axisTransform.modelMatrix, 0, mm, 0);
            System.arraycopy(temp, 0, ro.getTransform().modelMatrix, 0, 16);

            numMarks++;
            currentPos -= incPos;
            currentAngle -= incAngle;
        }
    }

    //TODO: Optimize
    public void updateMarks(int totalSpace, int markLen) {
        marks.clear();
        createMarks(totalSpace, markLen);
    }


    private RenderedObject addRenderedObject() {
        RenderedObject ro = new RenderedObject(axisMarkMesh, glslProgram);
        ro.createBuffers();
        marks.add(ro);
        return ro;
    }

    private void scaleExtra(float[] m, float markLen) {
        Matrix.translateM(m, 0, markLen/2, 0, 0); // move right a bit
        Matrix.scaleM(m, 0, markLen * 2, MARKING_HEIGHT, 1);
    }

    public ArrayList<RenderedObject> getMarks() {
        return marks;
    }
    public Transform getAxisTransform() { return axisTransform; }
}
