package com.badlogic.androidgames.hoverboats;

import framework.classes.DynamicGameObject;
import framework.classes.GameObject;

/**
 * Created by New PC 2012 on 12/01/2015.
 */
public class Smoke extends DynamicGameObject {
    public static float SMOKE_WIDTH = 0.1f;
    public static float SMOKE_HEIGHT = 0.1f;
    float stateTime;

    public Smoke(float x, float y) {
        super(x, y, SMOKE_WIDTH, SMOKE_HEIGHT);
        stateTime = 0.01f;
        mass = 0.01f;
    }
    public void update(float delta){
        if (isAlive()) {
            stateTime += delta;
        }

    }

    public boolean isAlive(){
        if (stateTime < 25){
            return true;
        }
        else{
            return false;
        }
    }
}
