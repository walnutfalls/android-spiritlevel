package com.footbits.sava.oglspiritleveldisplay;

import android.content.Context;
import android.opengl.Matrix;
import android.util.DisplayMetrics;

import java.text.DecimalFormat;

/**
 * Listens for angles and triggers redraw. This is where positions, strings, colors..etc are set.
 */
public class SpiritLevelAngleListener implements IAngleListener {

    private AngleGLRenderer renderer;
    private AngleViewSurface surface;
    private RenderedString pitch;
    private RenderedString roll;
    private RenderedAxis leftAxis;
    private RenderedAxis upAxis;

    public SpiritLevelAngleListener(Context context,
                                    AngleGLRenderer renderer,
                                    AngleViewSurface surface) {

        this.renderer = renderer;
        this.surface = surface;
        this.pitch = new RenderedString("0");
        this.roll = new RenderedString("0");
        this.leftAxis = renderer.getLeftAxis();
        this.upAxis = renderer.getUpAxis();


        //add pitch and roll strings to strings that get rendered.
        renderer.getStrings().add(pitch);
        renderer.getStrings().add(roll);


        //we are lacking an anchoring UI system. the coords that are set
        //for these objects are set for good.
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int halfHeight = dm.heightPixels / 2;
        int halfWidth = dm.widthPixels / 2;

        pitch.getTransform().setLocalX(halfWidth - 150);
        pitch.getTransform().setLocalY(-halfHeight + 50);
        roll.getTransform().setLocalX(halfWidth - 150);
        roll.getTransform().setLocalY(-halfHeight + 100);
    }


    @Override
    public void anglesArrived(float pitch, float roll) {

        DecimalFormat format = new DecimalFormat("0.00");
        String pitchStr = format.format(pitch);
        String rollStr = format.format(roll);

        float diff = pitchStr.length() - rollStr.length();
        float absDiff = Math.abs(diff);

        float[] chMat = renderer.getCrosshairs().getTransform().getLocalMatrix();
        Matrix.setIdentityM(chMat, 0);
        Matrix.translateM(chMat, 0,
                upAxis.angleToGlUnits(-roll),
                leftAxis.angleToGlUnits(-pitch),
                0);

        //ugh.. an ugly hack. could fix and optimize this,
        // but the real solution is an anchoring ui system or something
        if (diff > 0) {
            String spaces = "";
            for (int i = 0; i < absDiff; i++) spaces += "  ";

            rollStr = spaces + rollStr;
        } else if (diff > 0) {
            String spaces = "";
            for (int i = 0; i < absDiff; i++) spaces += "  ";

            pitchStr = spaces + pitchStr;
        }

        this.pitch.setText(pitchStr);
        this.roll.setText(rollStr);

        surface.requestRender();
    }



}
