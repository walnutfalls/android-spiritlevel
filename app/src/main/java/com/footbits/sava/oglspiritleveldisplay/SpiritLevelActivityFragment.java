package com.footbits.sava.oglspiritleveldisplay;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.footbits.oglwrapper.Program;
import com.golshadi.orientationSensor.sensors.Orientation;
import com.golshadi.orientationSensor.utils.OrientationSensorInterface;


public class SpiritLevelActivityFragment extends Fragment {

    MyGLSurfaceView sampleView;



    public SpiritLevelActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sampleView = new MyGLSurfaceView(this.getActivity());
        Program glProgram = new Program();

        return sampleView; // inflater.inflate(R.layout.fragment_spirit_level, container, false);


    }
}
