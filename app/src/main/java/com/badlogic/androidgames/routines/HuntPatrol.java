package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import framework.classes.Vector2;

/**
 * Created by f mgregor on 12/04/2015.
 */
public class HuntPatrol extends Routine {

    Routine patrol;
    Routine engageTarget;
    Routine selector;
    List<Vector2> vecs = new ArrayList<Vector2>();

//    should engage enemy or fall back to patrol... seems to work for engaging enemy at least

    public HuntPatrol(Vector2... vector2s){
        engageTarget = Routines.engageEnemy();
        for (Vector2 vec : vector2s){
            vecs.add(vec);
        }
        patrol = Routines.patrol(vecs);
        selector = Routines.sequence(engageTarget, patrol);
    }
    public HuntPatrol(List<Vector2> vecs){
        engageTarget = Routines.engageEnemy();
        patrol = Routines.patrol(vecs);
        selector = Routines.selector(engageTarget, patrol);
    }
    @Override
    public void reset() {
        start();
        selector.reset();
    }

    @Override
    public void act(Ship ship, World world, float delta) {
        if (isRunning()){
            selector.act(ship,world,delta);
            if (engageTarget.isFailure() && !ship.bb.targets.isEmpty()) {
                for (Ship target : ship.bb.targets) {
                    if (target.position.dist(ship.position) < 300) {
                        engageTarget.reset();
                    } else {
                        engageTarget.fail();
                    }
                }
            }

            if (selector.isFailure()){
                fail();
            }
        }
    }
}
