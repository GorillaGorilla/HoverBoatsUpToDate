package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

/**
 * Created by New PC 2012 on 22/11/2014.
 */
public abstract class Routine {




    public enum RoutineState {
        Success,
        Failure,
        Running
    }

    protected RoutineState state;

    protected Ship ship;

    protected Routine() { }



    public abstract void reset();

    public abstract void act(Ship ship, World world, float delta);

    protected void succeed() {
//        System.out.println(">>> Routine: " + this.getClass().getSimpleName() + " SUCCEEDED");
        this.state = RoutineState.Success;
    }

    protected void fail() {
//        System.out.println(">>> Routine: " + this.getClass().getSimpleName() + " FAILED");
        this.state = RoutineState.Failure;
    }

    public boolean isSuccess() {
        return state.equals(RoutineState.Success);
    }

    public boolean isFailure() {
        return state.equals(RoutineState.Failure);
    }

    public boolean isRunning() {
        return state.equals(RoutineState.Running);
    }

    public RoutineState getState() {
        return state;
    }

    public void start() {
//        System.out.println(">>> Starting routine: " + this.getClass().getSimpleName());
        this.state = RoutineState.Running;
    }

    public void setXY(float x, float y){

    }


    public String getDescription() {
        return "Plain routine";
    }


}
