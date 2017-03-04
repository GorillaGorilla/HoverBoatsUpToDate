package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

/**
 * Created by New PC 2012 on 29/01/2015.
 */
public class EngageEnemy extends Routine {
    Routine atDest, turnAndFire, chooseGunDeck;
    Routine setMatchingBearing, turn, wait1, wait2, wait3;
    Routine selector1, selector2, selector3;
    Routine sequence1, sequence2, sequence3;
    Routine repeat;
    Routine closeOnTarget;
    Routine potShot;

    public EngageEnemy(){
        potShot = Routines.fireGuns();
        turnAndFire = Routines.turnBroadside();
        setMatchingBearing = Routines.setMatchingBeating();
        turn = Routines.turnToMatchBearing();
        wait1 = Routines.wait(3f);
        sequence3 = Routines.sequence(setMatchingBearing, turn, wait1);
        selector3 = Routines.selector(turnAndFire,sequence3);

        chooseGunDeck = Routines.chooseGunDeck();
        atDest = Routines.closeToTest(250f);
        sequence2 = Routines.sequence(atDest,chooseGunDeck,selector3);

        wait2 = Routines.wait(2f);
        closeOnTarget = Routines.closeOnTarget();
        selector1 = Routines.selector(sequence2, closeOnTarget);
//        selector1 = Routines.selector((potShot) , sequence2,closeOnTarget); // potShot not working so removed anyway
//      selector1 = Routines.selector((potShot) , sequence2,closeOnTarget,wait2);

        repeat = Routines.repeat(selector1);

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
    public void act(Ship ship, World world, float delta) {

        if (isRunning()){

            if (ship.bb.targets.isEmpty()){
                fail();
            }else if(ship.bb.targets.get(0).state == ship.bb.targets.get(0).VESSEL_STATE_SINKING){
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
