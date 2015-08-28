package com.footbits.sava.oglspiritleveldisplay;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.audiofx.BassBoost;
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

    private float minExtent;
    private float maxExtent;

    public AngleViewSurface(Context context, final AngleGLRenderer renderer){
        super(context);

        angleProvider = new AndroidOrientationSensorAngleProvider(context);
        anglesArrivedResponder = new SpiritLevelAngleListener(context, renderer, this);
        scaleGestureDetector = new ScaleGestureDetector(context, mScaleGestureListener);
        angleProvider.getAngleListeners().add(anglesArrivedResponder);

        this.renderer = renderer;

        SharedPreferences preferences = context.getSharedPreferences(
                        SettingsActivity.PreferencesFileName,
                        Context.MODE_PRIVATE);

        // AngleExtent represents the full range. The settings assumes the user
        // will enter half of the range. AngleExtent of 200 represents -100 to 100,
        // while a user who enters 100 will actually mean -100-100. So, multiply by 2.
        minExtent = preferences.getFloat(SettingsActivity.MinRangePrefKey, 10)*2;
        maxExtent = preferences.getFloat(SettingsActivity.MaxRangePrefKey, 100)*2;


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

            float newExtent = extentLeft.get() * scaleGestureDetector.getScaleFactor();

            if(newExtent < minExtent) newExtent = minExtent;
            if(newExtent > maxExtent) newExtent = maxExtent;

            extentLeft.setExtent(newExtent);
            extentRight.setExtent(newExtent);

            //limit extents according to settings.
            Log.i(TAG, Float.toString(scaleGestureDetector.getScaleFactor()) + "\n");

            renderer.setUpdateAxes(true);

            return true;
        }
    };





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
