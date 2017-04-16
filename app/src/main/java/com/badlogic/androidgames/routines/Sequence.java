package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by New PC 2012 on 24/11/2014.
 */
public class Sequence extends Routine {

    private List<Routine> routines = new ArrayList<Routine>();

    public Sequence(Routine... newRoutines){
        super();
        name = "seq";
        for (Routine routine : newRoutines) {
            routines.add(routine);

        }
        start();
    }

    public Sequence (List<Routine> routines){
        this.routines = routines;
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
//        check if this routine is running otherwise do nothing
        float successCount = 0;
        if (isRunning())
//        checks each routine for success, if one is found unsucceeded then loop breaks and success not called.

            for (Routine routine : routines) {
                routine.act(ship, world, delta);
                if (routine.isFailure()){
                    fail();
                }

                if (routine.isSuccess()){
                    successCount++;
                }else{
                    break;
                }
                if (successCount == routines.size()) {


                    succeed();
                }





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

    public Sequence addRoutine(Routine rout) {routines.add(rout); return this;}
}
