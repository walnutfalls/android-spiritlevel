/***
 * Some stuff excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 ***/

package com.footbits.oglwrapper;

import android.util.Log;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glValidateProgram;



public class GlslProgram {
    private static final String TAG = "GlslProgram";

    private int programId;
    private VertexShader vertexShader;
    private FragmentShader fragmentShader;


    public GlslProgram(VertexShader vertexShader, FragmentShader fragmentShader) {
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    public GlslProgram(String vertexShaderSource, String fragmentShaderSource) {
        vertexShader  = new VertexShader(vertexShaderSource);
        fragmentShader = new FragmentShader(fragmentShaderSource);
    }


    public void compile() {
        programId = glCreateProgram();

        if (programId == 0) {
            Log.e(TAG, "Could not create new GLSL program");
            return;
        }

        vertexShader.delete();
        fragmentShader.delete();

        vertexShader.init();
        fragmentShader.init();

        linkProgram();
    }

    public void delete()
    {
        vertexShader.delete();
        fragmentShader.delete();
        glDeleteProgram(programId);
    }


    private void linkProgram(){
        // Attach the vertex shader to the program.
        glAttachShader(programId, vertexShader.getShaderId());
        // Attach the fragment shader to the program.
        glAttachShader(programId, fragmentShader.getShaderId());

        // Link the two shaders together into a program.
        glLinkProgram(programId);

        // Get the link status.
        final int[] linkStatus = new int[1];
        glGetProgramiv(programId, GL_LINK_STATUS, linkStatus, 0);

        // Print the program info log to the Android log output.
        Log.v(TAG, "Results of linking program:\n"
                + glGetProgramInfoLog(programId));


        // Verify the link status.
        if (linkStatus[0] == 0) {
            // If it failed, delete the program object.
            Log.e(TAG, "Linking of program failed.");
            delete();
            return;
        }
    }


    /**
     * Validates an OpenGL program. Should only be called when developing the
     * application.
     */
    public static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(TAG, "Results of validating program: " + validateStatus[0]
                + "\nLog:" + glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }




    public VertexShader getVertexShader() { return vertexShader; }
    public FragmentShader getFragmentShader() { return fragmentShader; }
}
