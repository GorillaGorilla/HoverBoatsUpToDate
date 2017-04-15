package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

/**
 * Created by New PC 2012 on 29/01/2015.
 */
public class EngageEnemy extends Routine {
    Routine atDest, turnAndFire, chooseGunDeck;
    Routine setMatchingBearing, turn, wait1, wait2, wait3;
    Routine selFightIfCloseElseApproach, selector2, selTurnAndFireOrSailParallel;
    Routine sequence1, seqFightTarget, seqSailParallel;
    Routine repeat;
    Routine closeOnTarget;
    Routine potShot;

    public EngageEnemy(){
        potShot = Routines.fireGuns();
        turnAndFire = Routines.turnBroadside();
        turnAndFire.setName("turnAndFire");
        setMatchingBearing = Routines.setMatchingBeating();
        setMatchingBearing.setName("setMatchingBearing");
        turn = Routines.turnToMatchBearing();
        turn.setName("turnToMatchBearing");
        wait1 = Routines.wait(3f);
        wait1.setName("wait1");
        seqSailParallel = Routines.sequence(setMatchingBearing, turn, wait1);
        seqSailParallel.setName("seqSailParallel");
        selTurnAndFireOrSailParallel = Routines.selector(turnAndFire, seqSailParallel);
        selTurnAndFireOrSailParallel.setName("selTurnAndFireOrSailParallel");
        chooseGunDeck = Routines.chooseGunDeck();
        chooseGunDeck.setName("chooseGunDeck");
        atDest = Routines.closeToTest(250f);
        atDest.setName("atDest");

        seqFightTarget = Routines.sequence(atDest,chooseGunDeck, selTurnAndFireOrSailParallel);
        seqFightTarget.setName("seqFightTarget");

        wait2 = Routines.wait(2f);
        closeOnTarget = Routines.closeOnTarget();
        closeOnTarget.setName("close n Trgt");
        selFightIfCloseElseApproach = Routines.selector(seqFightTarget, closeOnTarget);
        selFightIfCloseElseApproach.setName("close?fght:aprch");
//        selFightIfCloseElseApproach = Routines.selector((potShot) , seqFightTarget,closeOnTarget); // potShot not working so removed anyway
//      selFightIfCloseElseApproach = Routines.selector((potShot) , seqFightTarget,closeOnTarget,wait2);

        repeat = Routines.repeat(selFightIfCloseElseApproach);

    }

//    Is enemy in range?
// Yes: Attack --> Choose side ---> Turn to that side, if lined up, fire.
//    No: close on enemy (pick route to intersect path and get close enough to engage.


    @Override
    public void reset() {
        start();
        repeat.reset();
    }

    @Override
    public void act(Ship ship, World world, float delta){ super.act(ship, world, delta);

        if (isRunning()){
            ship.bb.resetRoutineLog();
            if (ship.bb.targets.isEmpty()){
                fail();
            }else if(ship.bb.targets.get(0).isSinking()){
                succeed();
            }

            repeat.act(ship,world,delta);

            if (repeat.isFailure()){
                fail();
            }
//            if (potShot.isFailure()){
//                potShot.reset();
//            }
        }

    }
}
