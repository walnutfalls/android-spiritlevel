package com.footbits.sava.oglspiritleveldisplay;

import java.util.ArrayList;

/**
 * Model for angle axis
 */
public class AngleExtent {
    public interface IExtentListener { void onChange(float newExtent); }
    private ArrayList<IExtentListener> listeners;

    private float extent;


    public AngleExtent(float extent) {
        listeners = new ArrayList<>();
        this.extent = extent;
    }

    public  AngleExtent()
    {
        listeners = new ArrayList<>();
    }



    public float get() {
        return extent;
    }

    public void setExtent(float extent) {
        this.extent = extent;
        for(IExtentListener l : listeners) l.onChange(extent);
    }

    public ArrayList<IExtentListener> getListeners() {
        return listeners;
    }
}
