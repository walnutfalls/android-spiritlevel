package com.footbits.sava.oglspiritleveldisplay;

import android.content.Context;
import android.opengl.GLSurfaceView;


public class AngleViewSurface extends GLSurfaceView {

    public AngleViewSurface(Context context, AngleGLRenderer renderer){
        super(context);

        // Using OpenGL ES2
        setEGLContextClientVersion(2);

        // todo
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);

        // Render when requestRender() is called. Careful when setting
        // mode to continuous - multiply threads will read/write render strings.
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
