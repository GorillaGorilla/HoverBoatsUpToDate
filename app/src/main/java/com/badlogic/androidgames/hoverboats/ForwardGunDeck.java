package com.badlogic.androidgames.hoverboats;

import framework.classes.Vector2;

/**
 * Created by f mgregor on 23/01/2015.
 */
public class ForwardGunDeck extends GunDeck {
    public static final float orientation = 0;
    private static float length = 2f;



    public ForwardGunDeck(int guns, Ship ship) {
        super(orientation, guns, length, ship);
        float space = ship.VESSEL_WIDTH*0.5f;
        for (int i = 0; i < guns; i++) {
            this.positions[i] = (float) i * (space/ (float) guns) - space / 2f;
        }
        this.offset = 7;
    }

    @Override
    public void update(float delta){
        stateTime += delta;
        if (state == GUNS_STATE_FIRING){
//            add new smoke here
            if (stateTime > 2) {
                state = GUNS_STATE_RELOADING;
                stateTime = 0;
                return;
            }

            for (int i = 0; i < guns; i++) {
                timer[i] += delta;
                Vector2 pos = new Vector2(9f, positions[i]);

                pos.rotate((ship.angle + orientation));
                pos.add(ship.position);
//                add one new cannonball into the world, aiming boat.orientation + deck.orientation
//                starting position
                if (timer[i]>0 && spent[i] == false) {

//                balls.add(new CannonBall())
                    CannonBall c = new CannonBall(pos.x, pos.y);
                    Smoke s = new Smoke(pos.x, pos.y);
                    smokes.add(s);
                    s.velocity.set(7 + (rand.nextFloat() * 8 - 4), (rand.nextFloat() * 8 - 4)).rotate(orientation + ship.angle);

                    float yvel = (rand.nextFloat() * 6f) - 3f;
                    c.mass = 5f;
                    c.velocity.set(130f, yvel).rotate(orientation + ship.angle);
                    c.z += (rand.nextFloat() * 4 - 2);
//                    c.velocity.set(20f,0f).rotate(orientation);
                    ship.world.listener.fire();
                    cBalls.add(c);
                    spent[i] = true;
                }
                ship.world.balls.addAll(cBalls);
                ship.firedBalls.addAll(cBalls);
                ship.world.smokes.addAll(smokes);
                smokes.clear();
                cBalls.clear();
            }





        }else if (state == GUNS_STATE_RELOADING){
            if (stateTime > 10) {
                state = GUNS_STATE_LOADED;
                stateTime = 0;
                return;
            }
        }

    }

}
