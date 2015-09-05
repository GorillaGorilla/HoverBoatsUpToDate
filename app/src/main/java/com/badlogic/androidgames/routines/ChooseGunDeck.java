package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.AngleCalc;
import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 04/02/2015.
 */
public class ChooseGunDeck extends Routine {

    Vector2 projectedTargetPos;
    Ship target;
    float distance;

    public ChooseGunDeck(){

    projectedTargetPos = new Vector2();
    }


    @Override
    public void reset() {
        start();
    }

    @Override
    public void act(Ship ship, World world, float delta) {

        if(isRunning()) {

            System.out.println("Choosing gunDeck");
            target = ship.bb.targets.get(0);
            distance = ship.bb.distHist[0];
//            projectedTargetPos = target.position.cpy().add(target.velocity.cpy().mul(distance / (target.velocity.len() * 3)));

            projectedTargetPos = target.position.cpy();
//        use bearing clockwise to determine the direction, if true turn opposite way ;)
            if (AngleCalc.directionClockwise(ship.angle, projectedTargetPos.angle())) {
//            turn anticlockwise starboard guns
                ship.bb.gunDeckChoice = 2;
                ship.tillerPos = 10;
                succeed();
                System.out.println("Starboard chosen");
            } else {
//            turn clockwise
                ship.bb.gunDeckChoice = 1;
                ship.tillerPos = -10;
                succeed();
                System.out.println("Port chosen");
            }
        }
    }
}
