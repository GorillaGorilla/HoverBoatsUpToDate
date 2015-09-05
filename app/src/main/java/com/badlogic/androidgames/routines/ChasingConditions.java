package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 30/01/2015.
 */
public class ChasingConditions extends Routine {
    public boolean first = true;


    public ChasingConditions(){

    }

    @Override
    public void reset() {
        start();
        first = true;

    }

    @Override
    public void act(Ship ship, World world, float delta) {
        if (isRunning()){
//            if (first){
//                ship.bb.distHist[2] = 13000;
//                ship.bb.distHist[1] = 12000;
//                first = false;
//            }
//            ship.bb.distHist[2] = ship.bb.distHist[1];
//            ship.bb.distHist[1] = ship.bb.distHist[0];
//            ship.bb.distHist[0] = ship.position.dist(ship.bb.targets.get(0).position);

            System.out.println("Current dist:  " + ship.bb.distHist[0]);
            System.out.println("previous dist:  " + ship.bb.distHist[1]);
            System.out.println("2nd previous dist:  " + ship.bb.distHist[2]);


            if (ship.bb.distHist[0]>ship.bb.distHist[1] &&
                    ship.bb.distHist[1]>ship.bb.distHist[2]
                    ){
                System.out.println("Lost target");
                fail();
            }else{
                succeed();
            }

        }

    }
}
