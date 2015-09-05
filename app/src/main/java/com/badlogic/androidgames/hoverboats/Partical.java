package com.badlogic.androidgames.hoverboats;

import java.util.Random;

import framework.classes.DynamicGameObject;

/**
 * Created by New PC 2012 on 14/01/2015.
 */
public class Partical extends DynamicGameObject {
    public static final int STATE_ACTIVE = 0;
    public static final int STATE_INACTIVE = 1;

    int state;
    float stateTime;
    float angVel;
    float angle, width, height, activeTime;
    Random rand;

    public Partical(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.state = STATE_ACTIVE;
        this.rand = new Random();
        this.width = width;
        this.height = height;
        stateTime = 0;
        velocity.x = (rand.nextFloat()*5) - 2.5f;
        velocity.y = (rand.nextFloat()*5) - 2.5f;
//        System.out.println("R.... Velocity "+ velocity.x + " y: " + velocity.y);
        this.angVel = (rand.nextInt()*40) - 20;
        this.angle = (rand.nextInt()*720) - 360;
//        this.activeTime = 0.5f +
//        System.out.println("R.... AngVel "+ angVel );

    }
    public void update(float delta) {



        if (this.state != STATE_INACTIVE) {
            position.x += velocity.x*delta;
            position.y += velocity.y*delta;
//            System.out.println("R.... Partical position "+ position.x + " y: " + position.y);
            angle += angVel*delta;

            // extract alpha
//            int a = this.color >>> 24;
//            a -= 2; // fade by 2
//            if (a <= 0) { // if reached transparency kill the particle
//                this.state = STATE_DEAD;
//            } else {
//                this.color = (this.color & 0x00ffffff) + (a << 24);        // set the new alpha
//                this.paint.setAlpha(a);
//                this.age++; // increase the age of the particle
//            }
            if (stateTime >= 1) {    // reached the end if its life
                this.state = STATE_INACTIVE;
                stateTime = 0;
            }

        }
        stateTime += delta;
    }
}
