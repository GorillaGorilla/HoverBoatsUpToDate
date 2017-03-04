package com.badlogic.androidgames.routines;

import com.badlogic.androidgames.hoverboats.Ship;
import com.badlogic.androidgames.hoverboats.World;

import java.util.ArrayList;
import java.util.Vector;

import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 18/01/2015.
 */
public class Blackboard {
    /**
     * Reference to the vector of players in the game
     */
    public static Vector<Ship> players;

    public ArrayList<Ship> targets = new ArrayList<Ship>();

    public Vector2 lastPosition = new Vector2();

    float[] distHist = new float[] {9999f,10000f,10001f};

    Vector2 interceptionPoint = new Vector2();

    public Vector2 fordwardPos = new Vector2();   // approximate position ahead

    public boolean inDanger = false;

    /**
     * Reference to the game map
     */
    public static World world;

    /**
     * Closest enemy cursor
     */

//    the gun deck the first target will be aimed at with 0 = forward, 1 port, 2 starboard.
    int gunDeckChoice = 0;

    public Vector2 closestEnemyPosition;

    /**
     * Direction vector to move in
     */
    public Vector2 targetBearing;

    /**
     * Destination point to arrive at
     */
    public Vector2 destination;

    /**
     * Path of positions to move to
     */
//    public Vector<Tile> path;

    /**
     * Reference to the owner player
     */
    public Ship ship;

    /**
     * Creates a new instance of the Blackboard class
     */
    public Blackboard(Ship ship, World world)
    {
        this.ship = ship;
        this.world = world;
        this.targetBearing = new Vector2();
        this.destination = new Vector2();
        this.fordwardPos = ship.position.cpy().add(ship.velocity.mul(5f));
//        this.path = new Vector<Tile>();
    }

    public void update(){
        Vector2 dP;
        Vector2 fM;
        fordwardPos.set(ship.position.x,ship.position.y);
        dP = ship.velocity.cpy();
        fM = new Vector2(ship.velocity.len(),0).rotate(ship.angle);
        if (Math.abs(ship.tillerPos)>1){
            float speedFac = ship.velocity.len();
            System.out.println(fM.display());
            fM.rotate(-speedFac * 10f* ship.TURN_SPEED*ship.TURN_SPEED * (ship.tillerPos / 10f));
//            System.out.println("fM: "+ fM.display());
//            System.out.println("rotate: "+-speedFac * 10f* ship.TURN_SPEED*ship.TURN_SPEED * (ship.tillerPos / 10f));
        }
        fordwardPos.add(fM.mul(10f));
    }

}
