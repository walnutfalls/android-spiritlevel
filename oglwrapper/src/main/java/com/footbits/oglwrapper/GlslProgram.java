/***
 * Some stuff excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 ***/

package com.footbits.oglwrapper;

import android.opengl.GLES20;
import android.util.Log;

import java.util.HashMap;

import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glGetActiveAttrib;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glValidateProgram;
import static android.opengl.GLES20.glGetActiveUniform;
import static android.opengl.GLES20.glGetUniformLocation;



public class GlslProgram {
    private static final String TAG = "GlslProgram";

    private int programId;
    private VertexShader vertexShader;
    private FragmentShader fragmentShader;

    private HashMap<String, UniformInfo> uniformNameToInfo;
    private HashMap<String, AttributeInfo> attributeNameToInfo;


    public GlslProgram(VertexShader vertexShader, FragmentShader fragmentShader) {
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    public GlslProgram(String vertexShaderSource, String fragmentShaderSource) {
        vertexShader  = new VertexShader(vertexShaderSource);
        fragmentShader = new FragmentShader(fragmentShaderSource);
        uniformNameToInfo = new HashMap<>();
        attributeNameToInfo = new HashMap<>();
    }

    public int uniformLocation(String name){
        return uniformNameToInfo.get(name).location;
    }

    public int attributeLocation(String name) {
        return attributeNameToInfo.get(name).location;
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

    public void bind()
    {
        GLES20.glUseProgram(programId);
    }

    public void delete()
    {
        vertexShader.delete();
        fragmentShader.delete();
        glDeleteProgram(programId);
        programId = 0;
    }

    public boolean isBuilt() {
        return programId != 0 &&
                vertexShader.shaderId != 0 &&
                fragmentShader.shaderId != 0;
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

        setUniformInfoMap();
        setAttributeInfoMap();
    }

    /**
     * Iterates through shader's uniforms. Populates the uniformTypeToInfo map with uniform info.
     */
    private void setUniformInfoMap()
    {
        final int[] totalUniforms = new int[1]; //number of uniforms

        final int[] nameLen = new int[1]; //length of a uniform name
        final int namebufSize = 128; //number of characters ogl is allowed to write to uniform name
        final byte[] nameBuf = new byte[namebufSize]; //contains uniform name

        final int[] glslType = new int[1]; //type of uniform
        final int[] size = new int[1]; //size of uniform

        //get number of uniforms in program
        glGetProgramiv(programId, GLES20.GL_ACTIVE_UNIFORMS, totalUniforms, 0);

        for(int i = 0; i < totalUniforms[0]; ++i)
        {
            UniformInfo info = new UniformInfo();

            //get uniform name
            glGetActiveUniform(programId, i, namebufSize, nameLen, 0, size, 0, glslType, 0, nameBuf, 0);

            info.name = new String(nameBuf, 0, nameLen[0]);
            info.size = size[0];
            info.glslType = glslType[0];
            info.location = glGetUniformLocation(programId, info.name);

            uniformNameToInfo.put(info.name, info);
        }
    }

    /**
     * Iterates through shader's attributes. Populates the uniformTypeToInfo map with uniform info.
     */
    private void setAttributeInfoMap()
    {
        final int[] totalAttribs = new int[1]; //number of uniforms

        final int[] nameLen = new int[1]; //length of a uniform name
        final int namebufSize = 128; //number of characters ogl is allowed to write to uniform name
        final byte[] nameBuf = new byte[namebufSize]; //contains uniform name

        final int[] glslType = new int[1]; //type of uniform
        final int[] size = new int[1]; //size of uniform

        //get number of uniforms in program
        glGetProgramiv(programId, GLES20.GL_ACTIVE_ATTRIBUTES, totalAttribs, 0);

        for(int i = 0; i < totalAttribs[0]; ++i)
        {
            AttributeInfo info = new AttributeInfo();

            //get uniform name
            glGetActiveAttrib(programId, i, namebufSize, nameLen, 0, size, 0, glslType, 0, nameBuf, 0);

            info.name = new String(nameBuf, 0, nameLen[0]);
            info.size = size[0];
            info.glslType = glslType[0];
            info.location = glGetAttribLocation(programId, info.name);

            attributeNameToInfo.put(info.name, info);
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

    public int getProgramId() { return programId; }
}
