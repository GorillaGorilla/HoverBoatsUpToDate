package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 30/01/2015.
 */
public class CalculateIntercept extends Routine {
    Ship target;
    float distance;
    Routine decideBearing;

    public CalculateIntercept(){
        name = "calcIntercept";
    }

    @Override
    public void reset() {
        start();
    }

    @Override
    public void act(Ship ship, World world, float delta){ super.act(ship, world, delta);
        if(isRunning()) {
            ship.bb.addRoutineToState(this.name);
            target = ship.bb.targets.get(0);
            distance = ship.bb.distHist[0];

            ship.bb.interceptionPoint = target.position.cpy().add(target.velocity.cpy().mul(distance / (target.velocity.len()*3)));

            System.out.println("intercept point! " + ship.bb.interceptionPoint.display());
            succeed();

        }


    }
}
