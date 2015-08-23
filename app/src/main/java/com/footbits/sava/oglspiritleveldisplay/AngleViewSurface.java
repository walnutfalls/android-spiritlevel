package com.footbits.sava.oglspiritleveldisplay;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.support.v4.view.ScaleGestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;


public class AngleViewSurface extends GLSurfaceView {
    private static final String  TAG = "AngleViewSurface";

    private IAngleProvider angleProvider;
    private IAngleListener anglesArrivedResponder;

    private ScaleGestureDetector scaleGestureDetector;

    private AngleGLRenderer renderer;


    /**
     * The scale listener, used for handling multi-finger scale gestures.
     */
    private final ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener
            = new ScaleGestureDetector.SimpleOnScaleGestureListener() {

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
           float d = scaleGestureDetector.getCurrentSpan() - scaleGestureDetector.getPreviousSpan();

            float sign = sign(d);
            float scale = sign > 0 ? 1.01f : 0.99f;

            AngleExtent extentLeft = renderer.getLeftAxis().getExtent();
            AngleExtent extentRight = renderer.getUpAxis().getExtent();


            extentLeft.setExtent(extentLeft.get() * scaleGestureDetector.getScaleFactor());
            extentRight.setExtent(extentLeft.get() * scaleGestureDetector.getScaleFactor());

            Log.i(TAG, Float.toString(scaleGestureDetector.getScaleFactor()) + "\n");

            renderer.setUpdateAxes(true);


            return true;
        }
    };


    public AngleViewSurface(Context context, final AngleGLRenderer renderer){
        super(context);

        angleProvider = new AndroidOrientationSensorAngleProvider(context);
        anglesArrivedResponder = new SpiritLevelAngleListener(context, renderer, this);
        scaleGestureDetector = new ScaleGestureDetector(context, mScaleGestureListener);
        angleProvider.getAngleListeners().add(anglesArrivedResponder);

        this.renderer = renderer;

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


    private int sign(float number) {
        if(number < 0) return -1;
        else if (number == 0) return 0;
        else return 1;
    }
}
