package com.footbits.oglwrapper;

import android.opengl.GLES20;

import java.nio.ByteBuffer;

import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by Sava on 7/12/2015.
 */
public class GlBuffer {
    public int getBufferId() { return bufferId; }

    private int bufferId;
    private int glBufferType;

    private ByteBuffer data;


    public GlBuffer(int bufferType, ByteBuffer data) {
        this.glBufferType = bufferType;
        this.data = data;
    }

    public void init() {
        int[] buffer = new int[1];

        glGenBuffers(1, buffer, 0);
        bufferId = buffer[0];

        if (bufferId == 0) {
            throw new RuntimeException("Could not create a new vertex buffer object.");
        }

        bufferData();
    }

    public void bind() {
        glBindBuffer(glBufferType, bufferId);
    }

    public void unBind() {
        // IMPORTANT: Unbind from the buffer when we're done with it.
        glBindBuffer(glBufferType, 0);
    }

    public void bufferData() {
        bind();
        data.position(0);
        glBufferData(glBufferType, data.capacity(), data, GLES20.GL_STATIC_DRAW);
        unBind();
    }

    public void setVertexAttribPointer(int dataOffset, int attributeLocation,
                                       int componentCount, int stride) {
        glBindBuffer(glBufferType, bufferId);
        // This call is slightly different than the glVertexAttribPointer we've
        // used in the past: the last parameter is set to dataOffset, to tell OpenGL
        // to begin reading data at this position of the currently bound buffer.
        glVertexAttribPointer(attributeLocation, componentCount, GLES20.GL_FLOAT,
                false, stride, dataOffset);
        glEnableVertexAttribArray(attributeLocation);
        glBindBuffer(glBufferType, 0);
    }
}
