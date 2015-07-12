package com.footbits.oglwrapper;


import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glShaderSource;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by Sava on 7/8/2015.
 */
public class VertexShader extends Shader {
    private static final String TAG = "VertexShader";


    public VertexShader(String source) {
        this.source = source;
    }

    /**
     * This method compiles the shader
     */
    @Override
    public void init() {
        shaderId = glCreateShader(GLES20.GL_VERTEX_SHADER);

        if (shaderId == 0) {
            Log.e(TAG, "Couldn't create vertex shader.");
            return;
        }

        // Set shader source
        glShaderSource(shaderId, source);

        compile();
    }
}
