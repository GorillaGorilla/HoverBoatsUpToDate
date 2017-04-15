package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;
import com.badlogic.androidgames.routines.Routine;
import com.badlogic.androidgames.routines.Routines;

/**
 * Created by f mgregor on 19/04/2015.
 */
public class SailAwayFrom extends Routine {
    Routine ba = Routines.bearAway();
    Routine turn = Routines.turnToMatchBearing();
    Routine wait = Routines.wait(5f);
    Routine closeTest = Routines.closeToTest(800);
    Routine seq = Routines.sequence(ba,turn, wait);
    Routine seq2 = Routines.sequence(closeTest,seq);
    @Override
    public void reset() {
        start();
        seq2.reset();
    }

    @Override
    public void act(Ship ship, World world, float delta){ super.act(ship, world, delta);
        if (isRunning()){

            seq2.act(ship,world,delta);

            if (closeTest.isFailure()){
                succeed();
            }else if(closeTest.isSuccess()){
                ship.bb.inDanger = true;
            }

            if (seq2.isSuccess()){
                seq2.reset();
            }else if (seq.isFailure()){
                fail();

            }

            closeTest.reset();

        }
    }
}
