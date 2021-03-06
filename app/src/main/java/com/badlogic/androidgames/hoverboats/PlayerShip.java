package com.badlogic.androidgames.hoverboats;

import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 04/01/2015.
 */
public class PlayerShip extends Ship {


    public static float VESSEL_WIDTH = 9f;
    public static float VESSEL_LENGTH = 19f;
    public static float TURN_SPEED = 3f;


    int hat = 5;
    float windFY;
    GunDeck starboardGuns;
    GunDeck portGuns;
    GunDeck forwardGuns;

//    boat is oriented sideways



    public PlayerShip(float x, float y, Vector2 initialHeading, Vector2 wind, World world) {
        super(x, y, initialHeading, wind, world, VESSEL_LENGTH, VESSEL_WIDTH);
        starboardGuns= new StarboardGunDeck(12,VESSEL_LENGTH, this);
        portGuns = new PortGunDeck(12,VESSEL_LENGTH, this);
        forwardGuns = new ForwardGunDeck(2,this);
        gunDecks.add(starboardGuns);
        gunDecks.add(portGuns);
        gunDecks.add(forwardGuns);
        this.VESSEL_MASS = 30000;
        Mast forwardMast = new Mast(3,0,1);
        Mast sternMast = new Mast(-5,0,1.25f);
        this.masts.add(forwardMast);
        this.masts.add(sternMast);
//        dS = new Vector2(0,0);
//        this.wind = wind;
        dAngle = 0f;
        this.initialHeading = initialHeading;
        angle = initialHeading.angle();
        this.sail = new Sail(1000);
        setName("H.M.S Victory");

    }


    public void update(float delta, float tillerPosition){

        checkDamage();

        if (state == VESSEL_STATE_SINKING){
            stateTime += delta;
            if (stateTime > 3f)
                state = VESSEL_STATE_SUNK;
            return;
        }

        sail.update(delta);

//        bounds2.center.set(position);
        bounds.lowerLeft.set(position).sub(VESSEL_LENGTH / 2, VESSEL_WIDTH / 2);
        bounds.rotate(angle, position);
        stateTime += delta;
        boundingShape.updatePos(position,angle);
        updateGunDecks(delta);
        updateMasts(delta);
        bb.update();
        nameLabel.setMessage(name + " x: " + (int)position.x + " " + (int)position.y );
        nameLabel.updatePosition(position.x, position.y -5);
    }

}
