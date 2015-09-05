package com.badlogic.androidgames.hoverboats;

import framework.classes.DynamicGameObject;
import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 04/01/2015.
 */
public class CannonBall extends DynamicGameObject {

    public static float BALL_WIDTH = 0.1f;
    public static float BALL_HEIGHT = 0.1f;
    public float BALL_MASS = 10f;
    public float e = 0.2f;
    public Vector2 impulse = new Vector2(0,0);
    public Vector2 line = new Vector2(0,0);
    float z = 2f;
    float zv = 8f;

    public CannonBall(float x, float y) {
        super(x, y, BALL_WIDTH, BALL_HEIGHT);
        mass = BALL_MASS;
    }

    public void update(float delta){

//        position.x += velocity.x*delta;
//        position.y += velocity.y*delta;

        zv = zv + delta*-9.8f;
        z = z + zv*delta;
//        System.out.println("R... zv: " + zv);
//        System.out.println("R... z: " + z);
//        System.out.println("Vel x: " + velocity.x+ " Vel y: "+ velocity.y);
        if (z < 0){
            System.out.println("splashdown");
        }
        bounds.lowerLeft.set(position.x-(BALL_WIDTH/2f),position.y-(BALL_HEIGHT/2f));
    }

    public boolean isAlive(){
        if (z>0){
            return true;
        }else{
            return false;
        }
    }
}
