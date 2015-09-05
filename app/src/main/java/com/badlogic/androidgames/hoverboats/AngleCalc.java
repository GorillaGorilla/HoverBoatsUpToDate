package com.badlogic.androidgames.hoverboats;

import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 16/01/2015.
 */
public class AngleCalc {


    public static float relAngle(Ship ship1, Ship ship2){
//        returns angle of boat 2 relative to boat 1, port 90, starboard 270, forward 0
        float angle = ship2.position.cpy().sub(ship1.position).angle();
        angle -= ship1.angle;
        if (angle < 0){
            angle += 360;
        }
        return angle;
    }

    public static float windAgle(Ship ship, Vector2 wind){
//        calculates relative angle between wind and a boat (into wind: 0, away: 180)
        float angle = wind.angle() - 180;
        if (angle < 0){
            angle += 360;
        }
        angle -= ship.angle;
        if (angle < 0){
            angle += 360;
        }
        return angle;
    }

    public static float bearingWindAngle(Ship ship, Vector2 dest, Vector2 wind){
//        returns wind direction relative to the bearing, 90 if wind is to port
        float windA = wind.angle();

        float angle = dest.cpy().sub(ship.position).angle();
        if (angle < 0){
            angle += 360;
        }
        float bearing =  angle-windA;
        if (bearing < 0){
            bearing += 360;
        }
        return bearing;
    }

    public  static boolean directionClockwise(float shipAngle, float bearing){
//        find shortest route, clockwise or anticlockwise to turn to match the bearing

        float angleDiff = bearing- shipAngle;
        if (angleDiff < 0){
            angleDiff += 360;
        }
        if (angleDiff > 180){
            return true;
        }else{
            return false;
        }
    }

    public static float findDirectionalComponent(Vector2 v1, float angle){
        return v1.cpy().rotate(-angle).x;
    }
    public static float findDirectionalComponent(Vector2 v1, Vector2 v2){
        return v1.cpy().rotate(-v2.angle()).x;
    }

    public static  boolean turnThroughWind(float shipAngle, float directionAngle, float windAngle){
        windAngle = windAngle - 180;
        if (windAngle < 0){
            windAngle += 360;
        }
        if (directionClockwise(shipAngle,directionAngle) == directionClockwise(shipAngle,windAngle)){
//            first checks if wind is on the same side  of the ship as the new bearing

            if (directionClockwise(shipAngle,directionAngle) == false){
//                anticlockwise
                directionAngle = changeFrameOfReference(directionAngle,shipAngle);
                windAngle = changeFrameOfReference(windAngle,shipAngle);
                if (directionAngle - windAngle > 0){
                    return true;
                }
            } else if (directionClockwise(shipAngle,directionAngle) == true){
//                clockwise
                directionAngle = changeFrameOfReference(directionAngle,shipAngle);
                windAngle = changeFrameOfReference(windAngle,shipAngle);
                if (directionAngle - windAngle < 0){
                    return true;
                }
            }
        }
        return  false;

    }

    public static float reverse(float angle){
        angle -= 180;
        if (angle < 0){
            angle += 360;
        }
        return angle;
    }

    public static float changeFrameOfReference(float angle, float referenceAngle){
        angle -= referenceAngle;
        if(angle < 0){
            angle += 360;
        }
        return angle;
    }

}
