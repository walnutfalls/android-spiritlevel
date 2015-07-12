package com.footbits.oglwrapper;

import android.opengl.GLES20;

/**
 * Created by Sava on 7/7/2015.
 */
public class DefaultTextureOptionsStrategy implements ISetTextureOptionsStrategy {
    @Override
    public void setTextureOptions()
    {
        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);  // Set U Wrapping
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);  // Set V Wrapping
    }
}
