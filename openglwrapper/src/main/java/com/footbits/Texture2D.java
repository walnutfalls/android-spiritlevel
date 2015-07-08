package com.footbits;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class Texture2D
{
    private static final String TAG = "Texture2D";

    private int handle;
    private int textureUnit;

    private ISetTextureOptionsStrategy setTextureOptionsStrategy;

    public Texture2D(ISetTextureOptionsStrategy setTextureOptionsStrategy)
    {
        this.setTextureOptionsStrategy = setTextureOptionsStrategy;
    }

    public static Texture2D fromBitmap(Bitmap bitmap, ISetTextureOptionsStrategy setTextureOptionsStrategy)
    {
        Texture2D tex = new Texture2D(setTextureOptionsStrategy);
        tex.load(bitmap);
        return tex;
    }

    public static Texture2D fromBitmap(Bitmap bitmap)
    {
        return fromBitmap(bitmap, SetTextureOptionsStrat.Default);
    }

    public void load(Bitmap bitmap)
    {
        //generate a handle for the texture
        final int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] == 0)
        {
            Log.e(TAG, "Failed to create OpenGL texture.");
            return;
        }

        handle = textureHandle[0];

        //bind and define texture options
        bind();

        setTextureOptionsStrategy.setTextureOptions();

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        unbind();
    }

    public void bind()
    {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handle);
    }

    public void unbind()
    {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }


    //getters and setters
    public int getHandle()
    {
        return handle;
    }

    public void setSetTextureOptionsStrategy(ISetTextureOptionsStrategy setTextureOptionsStrategy)
    {
        this.setTextureOptionsStrategy = setTextureOptionsStrategy;
    }

    public int getTextureUnit()
    {
        return textureUnit;
    }

    public void setTextureUnit(int textureUnit)
    {
        this.textureUnit = textureUnit;
    }
}
