package com.badlogic.androidgames.hoverboats;

/**
 * Created by f mgregor on 23/01/2015.
 */
public class StarboardGunDeck extends GunDeck {
    public static final float orientation = 270;

    public StarboardGunDeck(int guns, float length, Ship ship) {
        super(orientation, guns, length, ship);
        for (int i = 0; i < guns; i++) {
            this.positions[i] = (float) i * (length*0.7f / (float) guns) - length / 2f;
        }
        this.offset = 2;
    }
}
