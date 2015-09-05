package com.badlogic.androidgames.hoverboats;

import framework.classes.GameObject;

/**
 * Created by New PC 2012 on 06/01/2015.
 */
public class Buoy extends GameObject {
    float stateTime;
    public static final float BUOY_WIDTH = 5f;
    public static final float BUOY_HEIGHT = 10f;
    public static final int BUOY_SCORE = 10;


    public Buoy(float x, float y) {
        super(x, y, BUOY_WIDTH, BUOY_HEIGHT);
        stateTime = 0;

    }

    public void update(float delta){
        stateTime += delta;
    }
}
