package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

import java.util.Collections;
import java.util.List;

/**
 * Created by New PC 2012 on 29/11/2014.
 */
public class RandomiseR extends Routine {
    List<Routine> routines;
    Routine routine;


    public RandomiseR(Selector routine){
        this.routines = routine.getRoutines();
        this.routine = routine;
        start();

    }

    public RandomiseR(Sequence routine){
        this.routines = routine.getRoutines();
        this.routine = routine;
        start();
    }



    @Override
    public void reset() {
        start();
        Randomise(routines);
        routine.reset();
    }

    @Override
    public void act(Ship ship, World world, float delta) {

        if (isRunning()) {
            if (routine.isRunning()) {
                routine.act(ship, world, delta);
            } else if (routine.isSuccess()){
                succeed();
            } else if (routine.isFailure()){
                fail();
            }
        }
    }

    protected void Randomise(List<Routine> routines){
        Collections.shuffle(routines);
        System.out.println("Randomising");
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

    public List<Routine> getRoutines(){
        return routines;
    }


    public void start() {
        System.out.println(">>> Starting routine: " + this.getClass().getSimpleName());
        this.state = RoutineState.Running;
    }


    public String getDescription() {
        return "Sequence";
    }


}
