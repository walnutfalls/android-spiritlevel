package com.footbits.sava.oglspiritleveldisplay;

import android.opengl.Matrix;

import com.footbits.oglwrapper.GlslProgram;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * The view for an AngleRange model
 */
public class RenderedAxis {
    private static final int MARKING_HEIGHT = 3;
    public  static final float EXTRA_LEN_MULT = 2.0f;

    private Mesh axisMarkMesh;
    private GlslProgram glslProgram;
    private AngleExtent extent;

    private ArrayList<RenderedObject> marks;
    private ArrayList<RenderedString> renderedStrings;

    private Transform axisTransform;

    private float markLen;

    private float glPosToAngle;

    private DecimalFormat markFormat;

    public RenderedAxis(AngleExtent extent, GlslProgram glslProgram){
        this.glslProgram = glslProgram;
        this.extent = extent;
        this.renderedStrings = new ArrayList<>();
        this.markFormat = new DecimalFormat("0.00");


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
        this.markLen = markLen;
        axisTransform.setIdentity();


        double orderOfMagnitude = Math.floor(Math.log10(extent.get()));
        double incAngle = Math.pow(10, orderOfMagnitude - 1);

        int incPos = (int)((incAngle / extent.get()) * totalSpace);

        glPosToAngle = (float)(incAngle / (double)incPos);

        //draw upper half, starting with the mark at zero
        double halfExtent = extent.get()/2.0f;
        double currentAngle = 0;
        int currentPos = 0;
        int numMarks = 0;
        RenderedString str;

        //draw upper half
        while (currentAngle <= halfExtent) {
            RenderedObject ro = addRenderedObject();
            float[] mm = ro.getTransform().getLocalMatrix();

            Matrix.translateM(mm, 0, 0, currentPos, 0);
            Matrix.translateM(mm, 0, markLen/2, 0, 0);

            // draw bigger marks at increments of 10, and at angle 0
            if (numMarks == 10 || currentAngle == 0) {
                scaleExtra(mm, markLen);

                str = new RenderedString(
                        markFormat.format(currentAngle),
                        getWidth() + 10 + markLen/2, currentPos, 0, 0, 0, 0);

                str.getTransform().setParent(axisTransform);
                renderedStrings.add(str);
                numMarks = 0;
            }
            else
                Matrix.scaleM(mm, 0, markLen, MARKING_HEIGHT, 1); //scale normally

            numMarks++;
            currentPos += incPos;
            currentAngle += incAngle;
        }

        str = new RenderedString(
                markFormat.format(extent.get()/2),
                markLen/2, currentPos, 0, 0, 0, 0);

        str.getTransform().setParent(axisTransform);
        renderedStrings.add(str);

        currentAngle = -incAngle;
        currentPos = -incPos;
        numMarks = 1;


        //draw lower half
        while (currentAngle >= -halfExtent) {
            RenderedObject ro = addRenderedObject();
            float[] mm = ro.getTransform().getLocalMatrix();

            Matrix.translateM(mm, 0, 0, currentPos, 0);
            Matrix.translateM(mm, 0, markLen/2, 0, 0);

            // draw bigger marks at increments of 10, and at angle 0
            if (numMarks == 10 || currentAngle == 0) {
                scaleExtra(mm, markLen);

                str = new RenderedString(
                        markFormat.format(currentAngle),
                        getWidth() + 10 + markLen/2, currentPos, 0, 0, 0, 0);

                str.getTransform().setParent(axisTransform);
                renderedStrings.add(str);

                numMarks = 0;
            }
            else
                Matrix.scaleM(mm, 0, markLen, MARKING_HEIGHT, 1);

            numMarks++;
            currentPos -= incPos;
            currentAngle -= incAngle;
        }

        str.getTransform().setParent(axisTransform);
        renderedStrings.add(str);
    }

    //TODO: Optimize
    public void updateMarks(int totalSpace, int markLen) {
        marks.clear();
        renderedStrings.clear();
        createMarks(totalSpace, markLen);
    }


    private RenderedObject addRenderedObject() {
        RenderedObject ro = new RenderedObject(axisMarkMesh, glslProgram);
        ro.createBuffers();
        ro.getTransform().setParent(axisTransform);
        marks.add(ro);
        return ro;
    }

    private void scaleExtra(float[] m, float markLen) {
        Matrix.translateM(m, 0, markLen / EXTRA_LEN_MULT, 0, 0); // move right a bit
        Matrix.scaleM(m, 0, markLen * EXTRA_LEN_MULT, MARKING_HEIGHT, 1);
    }

    public ArrayList<RenderedObject> getMarks() {
        return marks;
    }
    public Transform getAxisTransform() { return axisTransform; }

    public ArrayList<RenderedString> getRenderedStrings() {
        return renderedStrings;
    }

    public float getWidth() {
        return markLen * EXTRA_LEN_MULT;
    }

    public float angleToGlUnits(float angle) {
        return (1/glPosToAngle) * angle;
    }


    public AngleExtent getExtent() {
        return extent;
    }
}
