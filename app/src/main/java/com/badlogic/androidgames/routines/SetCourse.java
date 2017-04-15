package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.AngleCalc;
import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

/**
 * Created by New PC 2012 on 16/01/2015.
 */
public class SetCourse extends Routine {
    float bearing;

    public SetCourse(float bearing){
        this.bearing = bearing;
        name = "set course";
    }
    public SetCourse(){
        name = "set course";
    }


    @Override
    public void reset() {
        start();
    }

    @Override
    public void act(Ship ship, World world, float delta){ super.act(ship, world, delta);
        if (isRunning()){
            ship.bb.addRoutineToState(this.name);
            System.out.println("Turning through wind? " + AngleCalc.turnThroughWind(ship.angle,bearing,world.wind.angle()));
            if (Math.abs(ship.angle - bearing)<0.1){
                succeed();
                ship.tillerPos = 0;
            }else if(AngleCalc.directionClockwise(ship.angle,bearing)){
                ship.tillerPos = - 10;
            }else if(!AngleCalc.directionClockwise(ship.angle,bearing)){
                ship.tillerPos = 10;
            }
        }
        if (ship.state == ship.VESSEL_STATE_SINKING || ship.state == ship.VESSEL_STATE_SUNK){
            fail();
        }

    }

    public void setBearing(float bearing){
        this.bearing = bearing;
    }
}
