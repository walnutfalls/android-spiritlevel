package com.footbits.oglwrapper;

import static android.opengl.GLES20.glGetUniformLocation;

public abstract class Uniform
{
    protected int uniformLocation;
    protected String uniformName;

    public Uniform(String uniformName) {
        this.uniformName = uniformName;
    }


    public int getUniformLocation()
    {
        return uniformLocation;
    }

    public void setUniformLocation(int location)
    {
        uniformLocation = location;
    }

    public void determineUniformLocation(int programHandle)
    {
        uniformLocation = glGetUniformLocation(programHandle, uniformName);
    }

    public String getName() {
        return  uniformName;
    }


    //abstract methods
    public abstract void useUniform();
}
