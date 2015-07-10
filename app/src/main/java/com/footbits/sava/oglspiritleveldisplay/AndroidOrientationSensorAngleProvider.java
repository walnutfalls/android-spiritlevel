package com.footbits.sava.oglspiritleveldisplay;

import android.content.Context;

import com.golshadi.orientationSensor.sensors.Orientation;
import com.golshadi.orientationSensor.utils.OrientationSensorInterface;

/**
 * An angle provider that uses the Android Orientation Sensor library for angles.
 */
public class AndroidOrientationSensorAngleProvider implements IAngleProvider, OrientationSensorInterface {

    private double pitch;
    private double roll;
    private Orientation orientationSensor;


    public AndroidOrientationSensorAngleProvider(Context context) {
        orientationSensor = new Orientation(context, this);

        //------Turn Orientation sensor ON-------
        // set tolerance for any directions
        orientationSensor.init(1.0, 1.0, 1.0);

        // set output speed and turn initialized sensor on
        // 0 Normal
        // 1 UI
        // 2 GAME
        // 3 FASTEST
        orientationSensor.on(3);
    }




    @Override
    public float getPitchDegrees() {
        return (float)pitch;
    }

    @Override
    public float getRollDegrees() {
        return (float)roll;
    }

    @Override
    public void orientation(Double AZIMUTH, Double PITCH, Double ROLL) {
        pitch = PITCH;
        roll = ROLL;
    }
}
