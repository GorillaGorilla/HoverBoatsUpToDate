package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

/**
 * Created by New PC 2012 on 17/01/2015.
 */
public class ReverseCourse extends Routine {
    private SetCourse setCourse;
    float course;
    private boolean courseSet = false;
    public ReverseCourse(){

    }

    @Override
    public void reset() {
        start();
        setCourse.reset();
        courseSet = false;
        setCourse.setBearing(course);
    }

    @Override
    public void act(Ship ship, World world, float delta) {
        if (isRunning()){
            if (!courseSet) {
                course = ship.angle -180;
                if (course < 0){
                    course += 360;
                }
                courseSet = true;

                setCourse.act(ship, world, delta);
            }
        }
        if (setCourse.isFailure()){
            fail();
        }else if (setCourse.isSuccess()){
            succeed();
        }

    }
}
