package com.footbits.sava.oglspiritleveldisplay;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Sava on 7/7/2015.
 */
public class MyGLSurfaceView  extends GLSurfaceView {

    public MyGLSurfaceView(Context context){
        super(context);
        setEGLContextClientVersion(2);
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(new MyGLRenderer());

    }
}
