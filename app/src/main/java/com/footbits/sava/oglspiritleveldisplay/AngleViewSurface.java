package com.footbits.sava.oglspiritleveldisplay;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.support.v4.view.ScaleGestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;


public class AngleViewSurface extends GLSurfaceView {

    IAngleProvider angleProvider;
    IAngleListener anglesArrivedResponder;
    private ScaleGestureDetector scaleGestureDetector;

    /**
     * The scale listener, used for handling multi-finger scale gestures.
     */
    private final ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener
            = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        /**
         * This is the active focal point in terms of the viewport. Could be a local
         * variable but kept here to minimize per-frame allocations.
         */

        private float lastSpanX;
        private float lastSpanY;

        // Detects that new pointers are going down.
        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float focusX = scaleGestureDetector.getFocusX();
            float focusY = scaleGestureDetector.getFocusY();
            return true;
        }
    };


    public AngleViewSurface(Context context, final AngleGLRenderer renderer){
        super(context);

        angleProvider = new AndroidOrientationSensorAngleProvider(context);
        anglesArrivedResponder = new SpiritLevelAngleListener(context, renderer, this);
        scaleGestureDetector = new ScaleGestureDetector(context, mScaleGestureListener);
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
        boolean retVal = scaleGestureDetector.onTouchEvent(e);
        retVal = scaleGestureDetector.onTouchEvent(e) || retVal;

        angleProvider.calibrate();
        anglesArrivedResponder.anglesArrived(0, 0);

        return retVal || super.onTouchEvent(e);
    }
}
