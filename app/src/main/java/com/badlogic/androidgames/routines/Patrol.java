package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 08/02/2015.
 */
public class Patrol extends Routine {

    boolean first = true;
    List<Vector2> points;
    List<Routine> routines;
    Routine sequence;
    Routine repeat;


    public Patrol(List<Vector2> points){

        this.points = points;
        this.routines = new ArrayList<Routine>();


    }

    @Override
    public void reset() {
        start();
        first = true;

    }

    @Override
    public void act(Ship ship, World world, float delta){ super.act(ship, world, delta);
        if (isRunning()){
            if (first){
                findStartPoint(ship.position);
                first = false;
                for (Vector2 point : points){
                    routines.add(Routines.moveTo(point.x,point.y));
                    System.out.println("MoveTo: " + point.display());
                }
                sequence = Routines.sequence(routines);
                repeat = Routines.repeat(sequence);
                repeat.reset();
            }

            repeat.act(ship, world, delta);

            if (sequence.isFailure()){
                fail();
                System.out.println("patrol failed");
            }


        }


    }

    private void findStartPoint(final Vector2 position){

        Collections.sort(points,new Comparator<Vector2>(){
            @Override public int compare(Vector2 p1, Vector2 p2){
                return (int)(p1.dist(position) - (int)p2.dist(position));
            }

        });

//
//        float closest = 1000000f;
//        List<Vector2> newPoints;
//        int i = 0;
//        int numberOfPoints = points.size();
//        int index = 0;
//        for (Vector2 point : points){
//
//            float dist = point.dist(position);
//            if (closest > dist){
//                closest = dist;
//                index = i;
//            }
//            i ++;
//        }
//        if (index != 0) {
//            System.out.println("Closest: " + index);
//            newPoints = new ArrayList<Vector2>(numberOfPoints);
////            for (i=0;i<numberOfPoints;i++){
////                newPoints.add(new Vector2(0,0));
////            }
//            Collections.copy(newPoints,points);
////            List<Vector2> newPoints = new List<Vector2>(points.subList(index, numberOfPoints));
////            Collections.copy(newPoints,points.subList(index, numberOfPoints));
////            Collections.copy(newPoints,points.subList(0, index-1));
//            newPoints.addAll(points.subList(0, index - 1));  //Bug found. doesnt seem to like shuffling the List<>. Tried to get around using Collections.copy. No luck yet. 05/07/15
//            points = newPoints;
//        }
    }
}
