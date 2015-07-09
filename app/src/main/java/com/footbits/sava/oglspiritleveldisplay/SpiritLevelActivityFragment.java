package com.footbits.sava.oglspiritleveldisplay;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.golshadi.orientationSensor.sensors.Orientation;
import com.golshadi.orientationSensor.utils.OrientationSensorInterface;


public class SpiritLevelActivityFragment extends Fragment implements OrientationSensorInterface {

    MyGLSurfaceView sampleView;
    Orientation orientationSensor;


    public SpiritLevelActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sampleView = new MyGLSurfaceView(this.getActivity());
        orientationSensor = new Orientation(getActivity().getBaseContext(), this);

        //------Turn Orientation sensor ON-------
        // set tolerance for any directions
        orientationSensor.init(1.0, 1.0, 1.0);

        // set output speed and turn initialized sensor on
        // 0 Normal
        // 1 UI
        // 2 GAME
        // 3 FASTEST
        orientationSensor.on(3);

        return sampleView; // inflater.inflate(R.layout.fragment_spirit_level, container, false);


    }

    @Override
    public void orientation(Double AZIMUTH, Double PITCH, Double ROLL) {

    }
}
