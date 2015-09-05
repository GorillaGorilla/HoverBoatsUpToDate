package com.badlogic.androidgames.hoverboats;

import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 31/01/2015.
 */
public class ShipComponents {


    public static Ship ship(float x, float  y, Vector2 initialHeading, Vector2 wind, World world, float length, float width){
        return new Ship(x,y,initialHeading,wind,world, length, width);
    }

    public  static ForwardGunDeck forwardGunDeck(int guns, Ship ship){
        return new ForwardGunDeck(guns, ship);
    }

    public static PortGunDeck portGunDeck(int funs, Ship ship){
        return new PortGunDeck(funs, ship.VESSEL_LENGTH, ship);
    }

    public static StarboardGunDeck starboardGunDeck(int funs, Ship ship){
        return new StarboardGunDeck(funs, ship.VESSEL_LENGTH, ship);
    }
}
