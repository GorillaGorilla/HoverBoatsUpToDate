package com.badlogic.androidgames.hoverboats;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 07/01/2015.
 */
public class GunDeck {
    public float orientation;
    private float length;
    protected Random rand = new Random();
    protected int guns;
    protected Ship ship;
    public float[] timer;
    public boolean[] spent;
    public float offset = 2f;

    public static final int GUNS_STATE_FIRING = 0;
    public static final int GUNS_STATE_LOADED = 1;
    public static final int GUNS_STATE_RELOADING = 2;

    public List<CannonBall> cBalls;
    public List<Smoke> smokes;
    float[] positions;

    public int state;
    float stateTime = 0;


    public GunDeck(float orientation, int guns, float length, Ship ship) {
//        orientation assumes 0 is straight forward if the ship is along the x axis
        this.orientation = orientation;
        this.guns = guns;
        this.length = length;
        this.ship = ship;
        state = GUNS_STATE_LOADED;
        positions = new float[guns];
        for (int i = 0; i < guns; i++) {
            positions[i] = (float) i * (length*0.7f / (float) guns)  - length / 2f;
        }
        this.cBalls = new ArrayList<CannonBall>();
        this.smokes = new ArrayList<Smoke>();
        this.timer = new float[guns];
        this.spent = new boolean[guns];
    }
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
                Vector2 pos = new Vector2(0, positions[i]);
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

                    float yvel = (rand.nextFloat() * 10f) - 5f;
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

    public boolean fire() {
        if (state == GUNS_STATE_RELOADING) {
            System.out.println("Still reloading!");
            ToastMessage toast = new ToastMessage(ship.position.x,ship.position.y,"Reloading Cap'n!");
            ship.world.toasts.add(toast);
            return false;
        } else if (state == GUNS_STATE_FIRING) {

            System.out.println("Already firing");
            return false;


        } else {
            if (state == GUNS_STATE_LOADED) {
                System.out.println("Fire!");
                ship.world.toasts.add(new ToastMessage(ship.position.x,ship.position.y,"Fire!"));
                state = GUNS_STATE_FIRING;
                stateTime = 0;
                System.out.println("orientation: " + orientation);
                System.out.println("ship.angle: " + ship.angle);
                System.out.println("sum: " + (orientation + ship.angle));
                for (int i = 0; i < guns; i++) {
                    System.out.println("i: " + i);
                    timer[i] = rand.nextFloat()*-1f;
                    spent[i] = false;
                }
                return true;
            }
        }
        return false;
    }
}
