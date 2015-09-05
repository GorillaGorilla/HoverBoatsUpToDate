package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.AngleCalc;
import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 16/01/2015.
 */
public class BuildSpeed extends Routine {
    float targetSpeed = 3;
    float acc[] = new float[] {0,0,0};
    float timer = 0;
    boolean first;
    SetCourse course = new SetCourse();

    public BuildSpeed(float speed){
        this.targetSpeed = speed;
        this.first = true;
    }

    public  BuildSpeed(){
        this.first = true;
    }

    @Override
    public void reset() {
        start();
    }

    @Override
    public void act(Ship ship, World world, float delta) {
        if (isRunning()) {
            float angle = AngleCalc.windAgle(ship, world.wind);
            if (!course.isRunning()) {
                acc[0] = AngleCalc.findDirectionalComponent(ship.acceleration, ship.angle);
                if (acc[0]<0.1){
//                    ship is accelerating forward very slowly, turn away from wind
                    turnAway(world);
                }else{
//                    course is already running
            }
            }
            acc[2] = acc[1];
            acc[1] = acc[0];
            acc[0] = AngleCalc.findDirectionalComponent(ship.acceleration, ship.angle);






            course.act(ship, world, delta);
//            float acc[] =
            if (ship.acceleration.cpy().rotate(-ship.angle).x>0){
//                need a method to check whether the acceleration is positivein the direction boat is pointed...
//

            }
        }
    }

    private void turnAway(World world){
        if (AngleCalc.directionClockwise(ship.angle, world.wind.angle())){
            course.setBearing(ship.angle + 5);
        }else if (!AngleCalc.directionClockwise(ship.angle, world.wind.angle())){
            course.setBearing(ship.angle - 5);
        }
    }
}
