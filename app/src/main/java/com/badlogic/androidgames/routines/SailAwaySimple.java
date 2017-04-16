package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

import java.util.ArrayList;
import java.util.List;

import framework.classes.Vector2;

/**
 * Created by f mgregor on 25/04/2015.
 */
public class SailAwaySimple extends Routine {
    Ship self;
    Ship enemy;
    Routine sailAway;
    List<Vector2> patrolPoints = new ArrayList<Vector2>();
    boolean fleeing = false;

    Routine patrol;

    public SailAwaySimple(){
        sailAway = new SailAwayFrom();
        patrolPoints.add(new Vector2(2000f,3500f));
        patrolPoints.add(new Vector2(2000f,3000f));
        patrolPoints.add(new Vector2(1800f,3800f));
        patrol = Routines.patrol(patrolPoints);


    }

    public SailAwaySimple(List<Vector2> points){
        System.out.print("---&&& SailAwaySimple right declaration called");
        sailAway = new SailAwayFrom();
        patrol = Routines.patrol(points);
    }

    @Override
    public void reset() {
        start();
        sailAway.reset();
        patrol.reset();
    }

    @Override
    public void act(Ship ship, World world, float delta){ super.act(ship, world, delta);
        self = ship;
        if (!self.bb.targets.isEmpty()) {
            enemy = ship.bb.targets.get(0);
            if (self.position.dist(enemy.position)<100 && !fleeing){
                fleeing = true;
                patrol.reset();
                sailAway.act(ship,world,delta);
            }
            else if (fleeing){
                sailAway.act(ship,world,delta);
                if (self.position.dist(enemy.position)>500){
                    fleeing = false;
                    sailAway.reset();
                }
            }else{
                patrol.act(self,world,delta);
            }


        }
        else {
//            only happens if boat has no targets/enemies
            patrol.act(self,world,delta);
            fleeing = false;
        }

        if (patrol.isFailure()){
            fail();
        }
    }
}
