package com.footbits.sava.oglspiritleveldisplay;

import android.opengl.Matrix;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.texample2.RenderedString;


public class SpiritLevelActivityFragment extends Fragment {

    AngleViewSurface angleViewSurface;
    AngleGLRenderer renderer;
    IAngleProvider angleProvider;

    RenderedString pitch;
    RenderedString roll;


    public SpiritLevelActivityFragment() {
        pitch = new RenderedString("0");
        roll = new RenderedString("0");

        pitch.setY(-60);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //create renderer
        renderer = new AngleGLRenderer(getActivity().getBaseContext());

        //then give renderer to surface
        angleViewSurface = new AngleViewSurface(getActivity().getBaseContext(), renderer);

        //instantiate provider
        angleProvider = new AndroidOrientationSensorAngleProvider(getActivity().getBaseContext());
        angleProvider.calibrate();

        //add pitch and roll strings to strings that get rendered.
        renderer.getStrings().add(pitch);
        renderer.getStrings().add(roll);


        //we are lacking an anchoring UI system. the coords that are set
        //for these objects are set for good.
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        int halhfHeight = dm.heightPixels / 2;
        int halfWidth = dm.widthPixels / 2;

        pitch.setX(halfWidth - 150);
        pitch.setY(-halhfHeight + 50);
        roll.setX(halfWidth - 150);
        roll.setY(-halhfHeight + 100);


        //listen for angle changes
        final RenderedString thisPitch = pitch;
        final RenderedString thisRoll = roll;
        angleProvider.getAngleListeners()
                .add(new IAngleListener() {
                    @Override
                    public void anglesArrived(float pitch, float roll) {
                        String pitchStr = String.format("%.2f", pitch);
                        String rollStr = String.format("%.2f", roll);

                        float diff = pitchStr.length() - rollStr.length();
                        float absDiff = Math.abs(diff);

                        float[] chMat = renderer.getCrosshairs().getTransform().modelMatrix;

                        Matrix.setIdentityM(chMat, 0);
                        Matrix.translateM(chMat, 0, roll * 3, pitch * 3, 0);

                        //ugh.. an ugly hack. could fix and optimize this,
                        // but the real solution is an anchoring ui system or something
                        if (diff > 0) {
                            String spaces = "";
                            for(int i = 0; i < absDiff; i++) spaces += "  ";

                            rollStr = spaces + rollStr;
                        }
                        else if(diff > 0) {
                            String spaces = "";
                            for(int i = 0; i < absDiff; i++) spaces += "  ";

                            pitchStr = spaces + pitchStr;
                        }

                        thisPitch.setText(pitchStr);
                        thisRoll.setText(rollStr);

                        angleViewSurface.requestRender();
                    }
                });

        return angleViewSurface;
    }
}
