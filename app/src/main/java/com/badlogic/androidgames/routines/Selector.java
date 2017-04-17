package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by New PC 2012 on 28/11/2014.
 */
public class Selector extends Routine {

    private List<Routine> routines = new ArrayList<Routine>();

    public Selector(Routine... newRoutines){
        super();
        name = "sel";
        for (Routine routine : newRoutines) {
            routines.add(routine);

        }
        start();
    }

    @Override
    public void reset() {
        for (Routine routine : routines) {
            routine.reset();

        }
        start();

    }

    @Override
    public void act(Ship ship, World world, float delta){ super.act(ship, world, delta);
        int failCount = 0;
        if (isRunning())

            for (Routine routine : routines) {
                routine.act(ship, world, delta);
                if (routine.isFailure()) {
//                    add to failCount and try next routine
                    failCount ++;
                }

                if (routine.isSuccess()) {
//                    succeed! Selector has completed it task
                    succeed();
                    break;
                }else if (routine.isRunning()){
                    break;
                }

                if (failCount == routines.size()) {


                    fail();
                }


            }
//                failCount must reach the threashold in one update, not accumulate over many loops

        failCount = 0;
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


    public String getDescription() {
        return "Selector";
    }

    public Selector addRoutine(Routine rout) {routines.add(rout); return this;}
}
