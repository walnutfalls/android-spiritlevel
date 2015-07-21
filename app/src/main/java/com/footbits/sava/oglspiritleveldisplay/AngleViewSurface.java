package com.footbits.sava.oglspiritleveldisplay;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;


public class AngleViewSurface extends GLSurfaceView {

    IAngleProvider angleProvider;
    IAngleListener anglesArrivedResponder;


    public AngleViewSurface(Context context, final AngleGLRenderer renderer){
        super(context);


        angleProvider = new AndroidOrientationSensorAngleProvider(context);
        anglesArrivedResponder = new SpiritLevelAngleListener(context, renderer, this);
        angleProvider.getAngleListeners().add(anglesArrivedResponder);


        // Using OpenGL ES2
        setEGLContextClientVersion(2);

        setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);

        // Render when requestRender() is called. Careful when setting
        // mode to continuous - multiply threads will read/write render strings.
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);


        angleProvider.calibrate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        angleProvider.calibrate();
        anglesArrivedResponder.anglesArrived(0, 0);
        return true;
    }
}
