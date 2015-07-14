package com.footbits.oglwrapper;


import android.util.Log;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;

public abstract class Shader {
    private static final String TAG = "Shader";

    protected int shaderId;

    protected String source;


    abstract void init();


    protected void compile() {
        // Compile the shader.
        glCompileShader(shaderId);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, compileStatus, 0);

        // Print the shader info log to the Android log output.
        String msg =glGetShaderInfoLog(shaderId);
        Log.v(TAG, "Results of compiling source:" + "\n" + source + "\n:"
                + msg);

        // Verify the compile status.
        if (compileStatus[0] == 0) {
            // If it failed, delete the shader object.
            delete();

            Log.e(TAG, "Compilation of shader failed: " + msg);
            throw new RuntimeException("Compilation of shader failed: " + msg);
        }
    }

    protected void delete() {
        glDeleteShader(shaderId);
        shaderId = 0;
    }

    public int getShaderId() { return shaderId; }

    public String getSource() {
        return source;
    }
}
