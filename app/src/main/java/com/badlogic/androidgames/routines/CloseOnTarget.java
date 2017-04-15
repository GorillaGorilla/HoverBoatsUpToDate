package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

/**
 * Created by New PC 2012 on 30/01/2015.
 */

// should chase a moving target, succeeds when gets within a certain distance (200?)
//    fails if distance increases 3 intervals ina  row

public class CloseOnTarget extends Routine {

    Ship target;
    boolean first = true;
    Routine sequence;
    Routine losingTargetTester;
    Routine decideBearing;
    Routine calcIntercept;
    Routine atDest;
    Routine takePotShot;
    Routine turn = Routines.turnToMatchBearing();
    Routine wait = Routines.wait(10f);
    Routine selector;
    Routine select;
    Routine r;
    Routine r2;

    public CloseOnTarget() {
        losingTargetTester = new ChasingConditions();
        calcIntercept = new CalculateIntercept();
        atDest = Routines.closeToTest();
        takePotShot = Routines.fireGuns();
        select = Routines.selector(atDest,takePotShot);
        sequence = Routines.sequence();
        losingTargetTester = Routines.chasingConditions();

        decideBearing = Routines.decideBearing();
        sequence = Routines.sequence(losingTargetTester,calcIntercept, decideBearing, turn, wait);
        r = Routines.repeat(sequence);
//        r2 = Routines.repeat(select);
        selector = Routines.selector(select, r);
    }

    @Override
    public void reset() {
        selector.reset();
        atDest.reset();
        r.reset();
        start();
        first = true;
    }

    @Override
    public void act(Ship ship, World world, float delta){ super.act(ship, world, delta);
        if (isRunning()) {
            ship.bb.addRoutineToState(this.name);
            if (first) {
                target = ship.bb.targets.get(0);
                first = false;
                ship.bb.distHist[2] = 13000;
                ship.bb.distHist[1] = 12000;
            }
            if (!target.equals(ship.bb.targets.get(0))) {
                fail();
            }

            ship.bb.distHist[2] = ship.bb.distHist[1];
            ship.bb.distHist[1] = ship.bb.distHist[0];
            ship.bb.distHist[0] = ship.position.dist(ship.bb.targets.get(0).position);



            selector.act(ship, world, delta);

            if (atDest.isSuccess()) {
                succeed();
                return;
            }



            if (selector.isFailure()) {
                fail();
            }
            if (select.isFailure()) {
                select.reset();
            }



        }

    }
}
