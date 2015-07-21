package com.footbits.sava.oglspiritleveldisplay;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SpiritLevelActivityFragment extends Fragment {

    AngleViewSurface angleViewSurface;
    AngleGLRenderer renderer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //create renderer
        renderer = new AngleGLRenderer(getActivity().getBaseContext());

        //then give renderer to surface
        angleViewSurface = new AngleViewSurface(getActivity().getBaseContext(), renderer);

        return angleViewSurface;
    }
}
