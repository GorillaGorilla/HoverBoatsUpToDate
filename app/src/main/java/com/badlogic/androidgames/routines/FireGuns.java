package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.AngleCalc;
import com.badlogic.androidgames.hoverboats.ForwardGunDeck;
import com.badlogic.androidgames.hoverboats.GunDeck;
import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

import java.util.List;

/**
 * Created by New PC 2012 on 31/01/2015.
 */
// fire if guns are in the correct orientation

public class FireGuns extends Routine {
    public static final int FORWARD = 0;
    public static final int PORT = 1;
    public static final int STARBOARD = 2;

    GunDeck fGunDeck;

    int type = FORWARD;


public FireGuns(int type){
    this.type = type;

}

    @Override
    public void reset() {
        start();
    }

    @Override
    public void act(Ship ship, World world, float delta) {
        if (isRunning()){

            boolean hasFGunDeck = false;
            for (GunDeck deck : ship.gunDecks){
                if (deck instanceof ForwardGunDeck){
                    fGunDeck = deck;
                    hasFGunDeck = true;
                    break;
                }
            }
            if (type == FORWARD) {
                if (!hasFGunDeck){
                    fail();
                    return;
                }

                if (fGunDeck.state == GunDeck.GUNS_STATE_RELOADING) {
                    fail();
                } else {


                    for (Ship target : ship.bb.targets) {
                        float angle = AngleCalc.relAngle(ship, target);
                        if (angle < 2.5 ||
                                angle > 357.5) {
                            fGunDeck.fire();
                            fail();
                        } else {
                            fail();
                        }

                    }
                }
            }
        }
    }
}
