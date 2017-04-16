package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.AngleCalc;
import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 18/01/2015.
 */
public class DecideBearing extends Routine {
    Vector2 destination = new Vector2();
    Routine wayPoint;
    boolean indirect = false;
    Vector2 bearing;
    float bearingAngleToWind = 0;
    boolean intercept = false;

    public  DecideBearing(float destx, float desty){
        this.destination.x = destx;
        this.destination.y = desty;
        name = "decideBrng";
    }

    public  DecideBearing(){
        intercept = true;
    }

    @Override
    public void reset() {
        start();
    }

    @Override
    public void act(Ship ship, World world, float delta){ super.act(ship, world, delta);
        if (isRunning()){

//            System.out.println("Deciding bearing");
            if (intercept){
                destination.x = ship.bb.interceptionPoint.x;
                destination.y = ship.bb.interceptionPoint.y;
            }

            if (indirect) {
//                wayPoint.act(ship,world,delta);
//                bearing = destination.cpy().sub(ship.position);
                bearingAngleToWind = AngleCalc.bearingWindAngle(ship, destination.cpy(), world.wind);
                if (!(bearingAngleToWind < 225 && bearingAngleToWind > 135)){
//                    System.out.println("Tak completed ");
                    indirect = false;
                    return;
                }else {
//                    System.out.println("Continue tak ");
                    succeed();
                    return;
                }
            }

            bearing = destination.cpy().sub(ship.position);
//            System.out.println("direction vectorx: " + bearing.x +"y: " + bearing.y);
//            System.out.println("direction angle: " + bearing.angle() );
            bearingAngleToWind = AngleCalc.bearingWindAngle(ship, destination.cpy(), world.wind);
//            System.out.println("wind angle : "+world.wind.angle());
//            System.out.println("bearing ang wind: "+bearingAngleToWind);
            if (bearingAngleToWind < 215 && bearingAngleToWind > 145){
//                angle is close to wind, must go upwind and then come at it from the side.
                ship.tb = AngleCalc.reverse(world.wind.angle()) - 30;
                System.out.println("Into wind ");
                ship.tb = calcTakBearing(world, ship);
//                wayPoint = Routines.moveTo(calculateWaypoint(world, ship).x,calculateWaypoint(world, ship).y);
//                wayPoint.reset();
                indirect = true;
                System.out.println("Indirect: " + indirect);
                succeed();
            }else{
//                in this case a direct route can be attempted
//                System.out.println("Direct route ");
                ship.tb = bearing.angle();
//                System.out.println("bearing ang: "+ ship.targetBearing);
                succeed();
            }


        }
    }

    private Vector2 calculateWaypoint(World world, Ship ship){
        float dx, dy;
        float bear = 0;
        float windAngRev = AngleCalc.reverse(world.wind.angle());
//        System.out.println("Ship positionx: " + ship.position.x + "y: " + ship.position.y);

        Vector2 newWaypoint;
//        returns true if the wind is clockwise to the bearing, in which case new waypoint should be
//        anti-clockwise to the wind to be closer to the actual destination
        if (AngleCalc.directionClockwise(bearing.angle(),windAngRev)) {
            bear = windAngRev + 45;

        }else if (!AngleCalc.directionClockwise(bearing.angle(),windAngRev)){
//            in this case the wind is anti-clockwise (left) of the destination
            bear = windAngRev - 45;
        }
        if (bear<0){
            bear += 360;
        }
//        System.out.println("bear: " + bear);
        newWaypoint = bearing.cpy();
//        System.out.println("angle of waypoint: " + newWaypoint.angle());
        newWaypoint.rotate(bear-newWaypoint.angle());
//        System.out.println("recalculated angle of waypoint: " + newWaypoint.angle());
        dx = ship.position.x + newWaypoint.x;
        dy = ship.position.y + newWaypoint.y;
//        System.out.println("waypoint.x: " + dx + "y: " +dy);

        return new Vector2(dx, dy);
    }

    private float calcTakBearing(World world, Ship ship){
        float dx, dy;
        float bear = 0;
        float windAngRev = AngleCalc.reverse(world.wind.angle());
//        System.out.println("Ship positionx: " + ship.position.x + "y: " + ship.position.y);
        if (AngleCalc.directionClockwise(bearing.angle(),windAngRev)) {
            bear = windAngRev + 45;

        }else if (!AngleCalc.directionClockwise(bearing.angle(),windAngRev)){
//            in this case the wind is anti-clockwise (left) of the destination
            bear = windAngRev - 45;
        }
        if (bear<0){
            bear += 360;
        }
        return bear;
    }
}
