package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

/**
 * Created by New PC 2012 on 23/11/2014.
 */
public class Repeat extends Routine {

    private final Routine routine;
    private int times;
    private int originalTimes;

    public Repeat(Routine routine) {
        super();
        this.routine = routine;
        this.times = -1; // infinite
        this.originalTimes = times;
        start();
    }

    public Repeat(Routine routine, int times) {
        super();
        if (times < 1) {
            throw new RuntimeException("Can't repeat negative times.");
        }
        this.routine = routine;
        this.times = times;
        this.originalTimes = times;
        start();
    }


    @Override
    public void reset() {
        this.times = originalTimes;
        routine.reset();
    }

    @Override
    public void act(Ship ship, World world, float delta) {
        if (isRunning())
            if (routine.isFailure()) {
                fail();
            } else if (routine.isSuccess()) {
                if (times == 0) {
                    succeed();
                    return;
                }
                if (times > 0 || times <= -1) {
                    times--;
                    System.out.println("starting and reseting...");
                    routine.reset();
                }
            }
        if (routine.isRunning()) {
            routine.act(ship, world, delta);
        }


    }

    protected void succeed() {
        System.out.println(">>> Routine: " + this.getClass().getSimpleName() + " SUCCEEDED");
        this.state = RoutineState.Success;
    }

    protected void fail() {
        System.out.println(">>> Routine: " + this.getClass().getSimpleName() + " FAILED");
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
        System.out.println(">>> Starting routine: " + this.getClass().getSimpleName());
        this.state = RoutineState.Running;
        this.routine.start();
    }


    public String getDescription() {
        return "Plain routine";
    }
}
