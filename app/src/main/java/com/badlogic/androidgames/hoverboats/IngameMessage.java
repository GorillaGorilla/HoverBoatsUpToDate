package com.badlogic.androidgames.hoverboats;

import framework.classes.DynamicGameObject;

/**
 * Created by frederickmacgregor on 05/03/2017.
 */

public class IngameMessage extends DynamicGameObject {
    public static final int DISPLAY = 0;
    public static final int FINISHED = 1;
    public int state = DISPLAY;

    public static float HEIGHT = 3;
    public static float WIDTH = 9;
    String message;
    public float stateTime = 0;
    public float maxTime = 2;

    IngameMessage(float x, float y, String string) {
        super(x, y, WIDTH, HEIGHT);
        this.message = string;
    }

    public void setMessage(String msg){
        message = msg;
    }

    public void updatePosition(float x, float y){
        this.position.x = x;
        this.position.y = y;
    }
}
