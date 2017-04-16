package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

/**
 * Created by New PC 2012 on 04/02/2015.
 */
public class SetMatchingBearing extends Routine {


    public SetMatchingBearing(){

    }

    @Override
    public void reset() {
        start();
    }

    @Override
    public void act(Ship ship, World world, float delta){
        super.act(ship, world, delta);
        if (isRunning()){

            ship.tb = ship.bb.targets.get(0).velocity.angle();
            succeed();
        }
    }
}
