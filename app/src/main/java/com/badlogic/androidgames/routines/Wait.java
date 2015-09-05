package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

/**
 * Created by New PC 2012 on 18/01/2015.
 */
public class Wait extends Routine {
    float timer = 1;
    float startingTime;

    public Wait(float seconds){
        this.startingTime = seconds;
        if (seconds<0.01){
            System.out.print("timer must be greater than 0");
            this.startingTime = 1;
        }


    }
    public Wait(){
    }

    @Override
    public void reset() {
        start();
        timer = startingTime;
    }

    @Override
    public void act(Ship ship, World world, float delta) {
        if (isRunning()) {
            timer -= delta;
            if (timer<0){
                succeed();
                System.out.println("wait finished: " + startingTime);
            }

        }
    }
}
