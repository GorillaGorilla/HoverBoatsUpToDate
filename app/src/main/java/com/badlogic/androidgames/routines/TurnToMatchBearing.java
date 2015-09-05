package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.AngleCalc;
import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

/**
 * Created by New PC 2012 on 18/01/2015.
 */
public class TurnToMatchBearing extends Routine {

    boolean leeward = false;
    public TurnToMatchBearing(){

    }

    @Override
    public void reset() {
        start();
        leeward = false;
    }

    @Override
    public void act(Ship ship, World world, float delta) {
        if (isRunning()){
//            System.out.println("bearing ang: "+ ship.tb);
//            System.out.println("ship ang: "+ ship.angle);
//            System.out.println("Turning through wind? " +
//                    AngleCalc.turnThroughWind(ship.angle, ship.tb, world.wind.angle()));
            if (Math.abs(ship.angle - ship.tb)<0.1){
                succeed();
                ship.tillerPos = 0;
                return;
            }
            if (!AngleCalc.turnThroughWind(ship.angle, ship.tb, world.wind.angle())
                    ||
                    AngleCalc.turnThroughWind(ship.angle, ship.tb, world.wind.angle()) &&
                    Math.abs(AngleCalc.windAgle(ship, world.wind))< 85 &&
                    ship.velocity.len()>2 && !leeward){
//                turn directly: dont need to turn through wind OR have enough speed to get through
                if(AngleCalc.directionClockwise(ship.angle,ship.tb)){
                    ship.tillerPos = - 10;
//                    System.out.println("Turn right");
                }else if(!AngleCalc.directionClockwise(ship.angle,ship.tb)){
                    ship.tillerPos = 10;
//                    System.out.println("Turn left");
                }
                return;

            }else {
                leeward = true;
//                will get stuck, therefore go round the leeward side
                if (AngleCalc.directionClockwise(ship.angle, ship.tb)) {
                    ship.tillerPos = 10;
//                    System.out.println("Turn left");
                } else if (!AngleCalc.directionClockwise(ship.angle, ship.tb)) {
                    ship.tillerPos = -10;
//                    System.out.println("Turn right");
                }
                return;
            }
        }
    }
}
