
package com.github.mikephil.charting.buffer;



public abstract class AbstractBuffer<T> {

    protected int index = 0;

    public final float[] buffer;

    protected float phaseX = 1f;

    protected float phaseY = 1f;



    public AbstractBuffer(int size) {
        index = 0;
        buffer = new float[size];
    }



    public void reset() {
        index = 0;
    }

    public int size() {
        return buffer.length;
    }


    public void setPhases(float phaseX, float phaseY) {
        this.phaseX = phaseX;
        this.phaseY = phaseY;
    }


    public abstract void feed(T data);
}
