package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.AngleCalc;
import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

/**
 * Created by New PC 2012 on 04/02/2015.
 */
public class TurnBroadside extends Routine {

    float timer = 0;

    @Override
    public void reset() {
        timer = 0;
        start();
    }

    @Override
    public void act(Ship ship, World world, float delta){ super.act(ship, world, delta);

        if (isRunning()) {
//            System.out.println("turning to fire!");
            ship.bb.addRoutineToState(this.name);
            timer += delta;
            if (!ship.hasLoadedGunDeck(ship.bb.gunDeckChoice)){
                fail();
                System.out.println("failed turing to fire!");
                return;
            }
            System.out.println("---&&& Turning to fire" + timer);
            if (ship.bb.gunDeckChoice == 2) {
//            starboard

                ship.tillerPos = 10;
                if (AngleCalc.relAngle(ship, ship.bb.targets.get(0)) < 271 &&
                        AngleCalc.relAngle(ship, ship.bb.targets.get(0)) > 269) {
                    if (ship.fire(270)) {
                        succeed();
                    }
                }

            } else if (ship.bb.gunDeckChoice == 1) {
//            port
                ship.tillerPos = -10;
                if (AngleCalc.relAngle(ship, ship.bb.targets.get(0)) > 89 &&
                        AngleCalc.relAngle(ship, ship.bb.targets.get(0)) < 91) {
                    if (ship.fire(90)) {
                        succeed();
                    }
                }

            }

            if (timer > 20) {
                System.out.println("failed turing to fire! Timer ran out");
                fail();
            }
        }

    }
}
