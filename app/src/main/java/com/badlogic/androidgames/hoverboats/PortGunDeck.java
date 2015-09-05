package com.badlogic.androidgames.hoverboats;

/**
 * Created by f mgregor on 23/01/2015.
 */
public class PortGunDeck extends GunDeck {
    public static final float orientation = 90;

    public PortGunDeck(int guns, float length, Ship ship) {
        super(orientation, guns, length, ship);
        for (int i = 0; i < guns; i++) {
            this.positions[i] = (float) i * (length*0.7f / (float) guns) - length / 4f;
        }
        this.offset = 2;
    }
}
