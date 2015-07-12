package com.footbits.sava.oglspiritleveldisplay;

import android.support.v4.app.Fragment;
import android.os.Bundle;
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


        //listen for angle changes
        final RenderedString thisPitch = pitch;
        final RenderedString thisRoll = roll;
        angleProvider.getAngleListeners()
                .add(new IAngleListener() {
                    @Override
                    public void anglesArrived(float pitch, float roll) {
                        thisPitch.setText(Float.toString(pitch));
                        thisRoll.setText(Float.toString(roll));

                        angleViewSurface.requestRender();
                    }
                });

        return angleViewSurface;
    }
}
