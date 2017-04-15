package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

import java.util.ArrayList;
import java.util.List;

import framework.classes.Vector2;

/**
 * Created by f mgregor on 24/04/2015.
 */
public class PatrolWarily extends Routine {

    Routine seq;
    Routine sel;
    Routine flee;
    Routine patrol;
    Routine isSafe;
    Routine rep;

    public PatrolWarily(List<Vector2> points){
        patrol = Routines.patrol(points);
        flee = new Flee();
        isSafe = new IsSafeTest();
        seq = Routines.sequence(isSafe,patrol);
        sel = Routines.selector(seq,flee);
        rep = Routines.repeat(sel);
    }
    @Override
    public void reset() {
        rep.reset();
        start();
    }

    @Override
    public void act(Ship ship, World world, float delta){ super.act(ship, world, delta);
        if (isRunning()){
            rep.act(ship,world,delta);
            if (rep.isFailure()){
                fail();
            }
            if (isSafe.isSuccess()){
                isSafe.reset();
            }
        }

    }
}
