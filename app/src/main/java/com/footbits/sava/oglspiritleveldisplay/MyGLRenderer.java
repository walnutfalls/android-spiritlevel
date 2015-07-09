package com.footbits.sava.oglspiritleveldisplay;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.texample2.GLText;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Sava on 7/7/2015.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Context context;

    public MyGLRenderer(Context context) {
        this.context = context;
    }



    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.2f, 0.0f, 0.3f, 1.0f);

        GLText glText = new GLText(context.getAssets());
    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }
}
