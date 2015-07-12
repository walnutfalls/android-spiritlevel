package com.footbits.oglwrapper;


import static android.opengl.GLES20.glUniformMatrix4fv;

public class MatrixUniform extends Uniform {
    private float[] matrix;

    public MatrixUniform(String name, float[] matrix) {
        super(name);

        this.matrix = matrix;
    }

    @Override
    public void useUniform() {
        glUniformMatrix4fv(uniformLocation, 1, false, matrix, 0);
    }

    public float[] getMatrix() {
        return matrix;
    }
}
