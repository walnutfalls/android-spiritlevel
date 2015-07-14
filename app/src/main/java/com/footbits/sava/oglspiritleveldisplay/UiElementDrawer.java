package com.footbits.sava.oglspiritleveldisplay;


import android.opengl.Matrix;

/**
 * Created by Sava on 8/26/2014.
 */
public class UiElementDrawer
{
    private static ShapeBuilder shapeBuilder = new ShapeBuilder(true);

    public static Mesh drawCrosshair(float gapSize, float thickness, float span,
                                          float r, float g, float b)
    {
        float halfGapSize = gapSize / 2.0f;
        float halfThickness = thickness / 2.0f;

        //draw shape
        shapeBuilder.reset();

        //uppder quad
        shapeBuilder.addQuad(
                halfThickness, halfGapSize, 0.0f, //lower right
                halfThickness, span, 0.0f, //upper right
                -halfThickness, span, 0.0f, //upper left
                -halfThickness, halfGapSize, 0.0f); //lower left

        //right
        shapeBuilder.addQuad(
                span, -halfThickness, 0.0f, //lower right
                span, halfThickness, 0.0f, //upper right
                halfGapSize, halfThickness, 0.0f, //upper left
                halfGapSize, -halfThickness, 0.0f); //lower left

        //bottom
        shapeBuilder.addQuad(
                halfThickness, -span, 0.0f,
                halfThickness, -halfGapSize, 0.0f,
                -halfThickness, -halfGapSize, 0.0f,
                -halfThickness, -span, 0.0f);

        //left
        shapeBuilder.addQuad(
                -halfGapSize, -halfThickness, 0.0f,
                -halfGapSize, halfThickness, 0.0f,
                -span, halfThickness, 0.0f,
                -span, -halfThickness, 0.0f);

        return shapeBuilder.toMesh();
    }
}

