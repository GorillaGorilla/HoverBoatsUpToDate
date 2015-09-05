package com.badlogic.androidgames.hoverboats;

import framework.classes.Circle;
import framework.classes.GameObject;

/**
 * Created by New PC 2012 on 04/01/2015.
 */
public class Rock extends GameObject {

//    actual in game units w = 25, h = 20
    public static float ROCK_WIDTH = 23f;
    public static float ROCK_HEIGHT = 18;
    Circle bounds2;


    public Rock(float x, float y) {
        super(x, y, ROCK_WIDTH, ROCK_HEIGHT);

        this.bounds2 = new Circle(x, y, ROCK_WIDTH/2f);
    }
}
