package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

/**
 * Created by New PC 2012 on 17/01/2015.
 */
public class HoldCourse extends Routine {
    private float course;
    public HoldCourse(float course){
        this.course = course;
    }

    @Override
    public void reset() {
        start();
    }

    @Override
    public void act(Ship ship, World world, float delta) {


        if (isRunning()) {
            if (ship.angle > course) {
                ship.tillerPos = -10;
            } else if (ship.angle < course) {
                ship.tillerPos = 10;
            } else if (Math.abs(course - ship.angle) < 0.01) {
                ship.tillerPos = 0;
            }
        }

        if (ship.state == ship.VESSEL_STATE_SINKING){
            fail();
        }
    }
}
