package com.badlogic.androidgames.hoverboats;

import android.content.Loader;

import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 05/02/2015.
 */
public class EnemyShip extends Ship {
    public static final float VESSEL_LENGTH = 19f;
    public static final float VESSEL_WIDTH = 5.5f;

    GunDeck portGuns, starboardGuns, fGuns;

    public EnemyShip(float x, float y, Vector2 initialHeading, Vector2 wind, World world) {
        super(x, y, initialHeading, wind, world, VESSEL_LENGTH, VESSEL_WIDTH);
        fGuns = ShipComponents.forwardGunDeck(2,this);
        portGuns = ShipComponents.portGunDeck(6,this);
        starboardGuns = ShipComponents.starboardGunDeck(6,this);
        gunDecks.add(portGuns);
        gunDecks.add(starboardGuns);
        Mast forwardMast = new Mast(3,0);
        Mast sternMast = new Mast(-5,0,1.2f);
        this.masts.add(forwardMast);
        this.masts.add(sternMast);
        this.VESSEL_MASS = 25000;
        this.sail = new Sail(700);

    }
}
