package com.footbits.sava.oglspiritleveldisplay;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.texample2.GLText;


/**
 * A placeholder fragment containing a simple view.
 */
public class SpiritLevelActivityFragment extends Fragment {

    MyGLSurfaceView sampleView;

    public SpiritLevelActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sampleView = new MyGLSurfaceView(this.getActivity());
        return sampleView; // inflater.inflate(R.layout.fragment_spirit_level, container, false);


    }
}
