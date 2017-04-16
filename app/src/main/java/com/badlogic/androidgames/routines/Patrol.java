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
                int startingPoint = findStartPoint(ship.position);
                first = false;

                for (Vector2 point : points){
                    Routine moveTo = Routines.moveTo(point.x,point.y);
                    routines.add(moveTo);

                    System.out.println("MoveTo: " + point.display());
                }
                sequence = Routines.sequence(routines);
                repeat = Routines.repeat(sequence);
                repeat.reset();
                int currentIndex = 0;
                for (Routine move : routines){
                    if (currentIndex < startingPoint){
                        move.succeed();
                    }
                    currentIndex++;
                }
            }

            repeat.act(ship, world, delta);

            if (sequence.isFailure()){
                fail();
                System.out.println("patrol failed");
            }


        }


    }

    private int findStartPoint(final Vector2 position){

//        this is broken, it sorts based on the closest, so the order of the points is changed permanently.
//        the order should be preserved, one should be able to start from te second point

//        instead find index of closest, and succeed the ones before. work through from there
        float minDist = 99999;
        int indexOfClosest = 0;
        for (int i = 0; i < points.size(); i++){
            float distance = points.get(i).dist(position);
            if (distance < minDist) {
                minDist = distance;
                indexOfClosest = i;
            }
        }
        return indexOfClosest;
//        Collections.sort(points,new Comparator<Vector2>(){
//            @Override public int compare(Vector2 p1, Vector2 p2){
//                return (int)(p1.dist(position) - (int)p2.dist(position));
//            }
//
//        });

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
