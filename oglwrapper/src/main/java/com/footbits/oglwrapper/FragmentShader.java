package com.footbits.oglwrapper;

import android.util.Log;

import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glShaderSource;


public class FragmentShader extends Shader {
    private static final String TAG = "FragmentShader";


    public FragmentShader(String source) {
        this.source = source;
    }

    /**
     * This method compiles the shader
     */
    @Override
    public void init() {
        shaderId = glCreateShader(GL_FRAGMENT_SHADER);

        if (shaderId == 0) {
            Log.e(TAG, "Couldn't create vertex shader.");
            return;
        }

        // Set shader source
        glShaderSource(shaderId, source);

        compile();
    }
}
