package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

/**
 * Created by f mgregor on 23/04/2015.
 */
public class Flee extends Routine{
    Routine sel;
    Routine sailAway;
    Routine safeDistanceCheck;

    public Flee(){
        safeDistanceCheck = Routines.closeToTest(1000);
        sailAway = new SailAwayFrom();
        sel = Routines.selector(safeDistanceCheck,sailAway);
    }

    @Override
    public void reset() {
        start();
        sel.reset();
    }

    @Override
    public void act(Ship ship, World world, float delta) {
        if (isRunning()){
            sel.act(ship,world,delta);
            if (safeDistanceCheck.isSuccess()){
                ship.bb.inDanger = false;
            }
            if (sel.isSuccess()){
                succeed();
            }else if(sel.isFailure()){
                fail();
            }
        }
    }
}
