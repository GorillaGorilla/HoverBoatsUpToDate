package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 18/01/2015.
 */
public class MoveTo extends Routine {
    Routine decideBearing;
    Routine turn = Routines.turnToMatchBearing();
    Routine wait = Routines.wait(5f);
    Routine sequence;
    Routine selector;
    Routine atDest;
    Routine r;

    public MoveTo(float x, float y){
        atDest = Routines.closeToTest(x, y);
        decideBearing = Routines.decideBearing(x, y);
        sequence = Routines.sequence(decideBearing, turn, wait);
        r = Routines.repeat(sequence);
        selector = Routines.selector(atDest,r);
        name = "moveTo";
    }

    @Override
    public void reset() {
        selector.reset();
        atDest.reset();
        r.reset();
        start();
    }

    @Override
    public void act(Ship ship, World world, float delta){ super.act(ship, world, delta);
        if (isRunning()) {
            ship.bb.addRoutineToState(this.name);
            selector.act(ship, world, delta);
            if (atDest.isFailure()){
                atDest.reset();
            }else if(atDest.isSuccess()){
//                System.out.println("destination reached");
                succeed();
            }


        }
        if (selector.isFailure()){
            fail();
        }
    }
}
