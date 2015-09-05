package com.badlogic.androidgames.hoverboats;

import framework.classes.Vector2;

/**
 * Created by f mgregor on 13/04/2015.
 */
public class CargoBoat extends Ship {

    public static final float VESSEL_LENGTH = 20.625f;
    public static final float VESSEL_WIDTH = 5f;


    public CargoBoat(float x, float y, Vector2 initialHeading, Vector2 wind, World world) {
        super(x, y, initialHeading, wind, world, VESSEL_LENGTH, VESSEL_WIDTH);
        double[] polygon1y = new double[]{0,
                2.75*0.75,
                2.75*0.75,
                -2.75*0.75,
                -2.75*0.75};
        double[] polygon1x = new double[]{10*0.75,
                4.125*0.75,
                -9*0.75,
                -9*0.75,
                4.125*0.75};
        this.bowHull = 50;
        this.starboardHull = 75;
        this.portHull = 75;
        this.sternHull = 25;
        this.mass = 10000;
        boundingShape.setSize(polygon1x, polygon1y);
        this.sail = new Sail(400);
        Mast mast = new Mast(0,0,0.8f);
        masts.add(mast);
        this.spriteFactor = 0.75f;
    }
}
