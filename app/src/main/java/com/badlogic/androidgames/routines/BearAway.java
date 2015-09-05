package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

/**
 * Created by f mgregor on 19/04/2015.
 */
public class BearAway extends Routine {

    @Override
    public void reset() {
        start();
    }

    @Override
    public void act(Ship ship, World world, float delta) {
        if (isRunning()){
            if (!ship.bb.targets.isEmpty()) {
                ship.tb = ship.position.cpy().sub(ship.bb.targets.get(0).position).angle();
                System.out.println(ship.position.display());
                System.out.println(ship.bb.targets.get(0).position.display());
                System.out.println(ship.position.cpy().sub(ship.bb.targets.get(0).position).angle());
                succeed();
            }else{
                fail();
            }
        }
    }
}
