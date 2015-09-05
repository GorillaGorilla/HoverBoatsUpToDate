package com.badlogic.androidgames.hoverboats;

import android.util.Log;

/**
 * Created by New PC 2012 on 14/01/2015.
 */
public class Hit {
    public static final int STATE_ACTIVE     = 0;    // at least 1 particle is alive
    public static final int STATE_INACTIVE      = 1;    // all particles are dead
    public Partical[] particles;           // particles in the explosion
    private int x, y;                       // the explosion's origin
    private int size;                       // number of particles
    public int state;                      // whether it's still active or not
    float stateTime = 0;


    public Hit(int particleNr, int x, int y) {
//        Log.d(R... "Explosion created at " + x + "," + y);
        this.state = STATE_ACTIVE;
        this.particles = new Partical[particleNr];
        for (int i = 0; i < this.particles.length; i++) {
            Partical p = new Partical(x, y, 2, 0.5f);
            this.particles[i] = p;
        }
        this.size = particleNr;
    }

    public void update(float delta){
       if (state == STATE_ACTIVE) {
           for (Partical partical : particles) {
               partical.update(delta);
           }
       }
        if (stateTime > 20){
            state = STATE_INACTIVE;
        }
        stateTime += delta;
    }

}
