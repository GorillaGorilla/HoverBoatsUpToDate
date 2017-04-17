package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 18/01/2015.
 */
public class CloseToTest extends Routine {
    Vector2 point = new Vector2();
    boolean intercept;
    boolean targetTest;
    float closeness = 100;
    boolean opposite = false;

    public CloseToTest(float x, float y){
        this.point.x = x;
        this.point.y = y;
    }

    public CloseToTest(float distance){
        name = "closetotest";

        targetTest = true;
        closeness = distance;
    }
    public CloseToTest(float distance, boolean opposite){

        opposite = true;
        targetTest = true;
        closeness = distance;
    }



    public CloseToTest(){
     intercept = true;
    }

    @Override
    public void reset() {
        start();
    }

    @Override
    public void act(Ship ship, World world, float delta){
        super.act(ship, world, delta);
        if (isRunning()){
            if (ship.bb.targets.isEmpty() && targetTest){
                fail();
                return;
            }
            if (intercept){
                point = ship.bb.interceptionPoint.cpy();
                closeness = 100;
            }
            if (targetTest){
                point = ship.bb.targets.get(0).position.cpy();
            }
//            System.out.println("Checking Whether close to");
//            System.out.println("distance to point " + point.dist(ship.position));
            if (point.dist(ship.position)<closeness){

                if (!opposite) {
                    succeed();
                    return;
                }else{
                    fail();
                }
            }else{
                if(!opposite) {
                    fail();
                }else{
                    succeed();
                    return;
                }
            }
        }
    }
    @Override
    protected void fail() {
        System.out.println(">>> Routine: " + this.getClass().getSimpleName() + " " + closeness + " FAILED");
        this.state = RoutineState.Failure;
    }

}
