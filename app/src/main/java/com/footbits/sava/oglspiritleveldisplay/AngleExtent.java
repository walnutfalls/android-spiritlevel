package com.footbits.sava.oglspiritleveldisplay;

import java.util.ArrayList;

/**
 * Model for angle axis
 */
public class AngleExtent {



    private float extent;


    public AngleExtent(float extent) {
        this.extent = extent;
    }

    public  AngleExtent()
    {
    }

    public float get() {
        return extent;
    }

    public void setExtent(float extent) {
        this.extent = extent;
    }
}
