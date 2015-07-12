package com.footbits.sava.oglspiritleveldisplay;

import java.util.ArrayList;

/**
 * Created by Sava on 8/25/2014.
 */
public interface IAngleProvider
{
    float getPitchDegrees();
    float getRollDegrees();

    void calibrate();

    ArrayList<IAngleListener> getAngleListeners();
}
