package com.badlogic.androidgames.hoverboats;

import com.badlogic.androidgames.routines.Blackboard;
import com.badlogic.androidgames.routines.ReverseCourse;
import com.badlogic.androidgames.routines.Routine;
import com.badlogic.androidgames.routines.Routines;
import com.badlogic.androidgames.routines.SetCourse;

import java.util.ArrayList;
import java.util.List;

import framework.classes.Circle;
import framework.classes.DynamicGameObject;
import framework.classes.GameObject;
import framework.classes.Vector2;
import framework.classes.Vector3;

/**
 * Created by New PC 2012 on 04/01/2015.
 */
public class Ship extends DynamicGameObject {
    public static final int VESSEL_STATE_SAILING = 0;
    public static final int VESSEL_STATE_SINKING = 3;
    public static final int VESSEL_STATE_SUNK = 4;

    static World world;

    public float VESSEL_WIDTH;
    public float VESSEL_LENGTH;
    public static final float VESSEL_HEIGHT = 20f;
    public float VESSEL_MASS = 35000f;
    public static float TURN_SPEED = 3f;
    public static float VESSEL_MAXDAMAGE = 100;
//    boat is oriented sideways

    public float bowHull = 50;
    public float starboardHull = 150;
    public float portHull = 150;
    public float sternHull = 25;
    public List<GunDeck> gunDecks;
    public Sail sail;
    List<Mast> masts = new ArrayList<Mast>();
    List<CannonBall> firedBalls;


    //    should start pointing right
    public Blackboard bb ;
    public Vector2 initialHeading;
    public Vector2 acceleration;
    public Vector3 angVel = new Vector3();
    Vector2 bearing = new Vector2();
    public float angle;
    public float dAngle;
    public float damage = 0;
    public float targetBearing = 0;
    public float tb = 0;
    public float tillerPos = 0;
    public float sailAngle = 0;
    public Circle bounds2;
    public Routine routine;
    public BoundingShape boundingShape = new BoundingShape();
    public float spriteFactor = 1;  // this is a temporary scalar to allow me to alter the size of different boats


    public int state;
    float stateTime = 0;
    float thinkTime = 0;
    //    elemental forces




    //      cooeficients
    public float e = 0.5f; // coeficient of restitution
    public float Cdx = 0.0f;
    public float CdxNormal = 0.4f;
    public float Cdy = 0.4f;
    public float CdyNormal = 0.5f;
    public Vector2 impulse = new Vector2();
    public Vector2 destination;
    private boolean starting = true;


    public Ship(float x, float y, Vector2 initialHeading, Vector2 wind, World world, float length, float width) {
        super(x, y, length, width);
        VESSEL_LENGTH = length;
        VESSEL_WIDTH = width;
        bounds.lowerLeft.set(position).sub(VESSEL_LENGTH / 2, VESSEL_WIDTH / 2);
        velocity.set(0, 0);
        this.world = world;
        this.initialHeading = initialHeading;
        this.destination = new Vector2(0,0);
//        bounds2 = new Circle(position.x, position.y, VESSEL_LENGTH);

        gunDecks = new ArrayList<GunDeck>();
        firedBalls = new ArrayList<CannonBall>();
        System.out.println("angle: " + angle);
        dAngle = 0f;
        angle = initialHeading.angle();
        bounds.rotate(angle, position);
        this.bb = new Blackboard(this, world);

    }

    public void update(float delta) {

        checkDamage();
        if (state == VESSEL_STATE_SINKING) {
            stateTime += delta;
            if (stateTime > 1.6f)
                state = VESSEL_STATE_SUNK;
            return;
        } else if (state == VESSEL_STATE_SUNK) {
            return;
        }else if (state == VESSEL_STATE_SAILING){
            routine.act(this, world, delta);
            thinkTime += delta;
        }
        bounds.lowerLeft.set(position).sub(VESSEL_LENGTH / 2, VESSEL_WIDTH / 2);
        bounds.rotate(angle, position);
//        bounds2.center.set(position);
        boundingShape.updatePos(position,angle);
        updateGunDecks(delta);
        updateMasts(delta);
        bb.update();
    }

    public void updateGunDecks(float delta) {
        if (!gunDecks.isEmpty()) {
            for (GunDeck gunDeck : gunDecks) {
                gunDeck.update(delta);
            }
        }
    }

    public void updateMasts(float delta){
        if (!masts.isEmpty()) {
            for (Mast mast : masts){
                mast.updatePosition(position.x, position.y, angle);
                mast.updateRotation(sailAngle,delta);
            }
        }
    }

    public void checkDamage(){
        if (damage > VESSEL_MAXDAMAGE && state != VESSEL_STATE_SINKING && state != VESSEL_STATE_SUNK
                || sternHull < 0 || bowHull < 0 || portHull < 0 || starboardHull < 0){
            state = VESSEL_STATE_SINKING;
            System.out.println();
        }
    }



    public void hitRock(GameObject object) {
//        find bound point inside region

        if (portHull < 1 || starboardHull < 1 || bowHull < 1 || sternHull < 1) {
            state = VESSEL_STATE_SINKING;
            stateTime = 0;
        }
    }

    public void shipHit(Ship object) {
        if (object == this) {
            return;
        }

        if (portHull < 1 || starboardHull < 1 || bowHull < 1 || sternHull < 1) {
            state = VESSEL_STATE_SINKING;
            stateTime = 0;
        }
    }

    public void cannonballHit(Vector2 imp, float energy) {
        float course = angle -180;
        if (course < 0){
            course += 360;
        }
        this.impulse.add(imp);
        float dirAngle = imp.angle();
        System.out.println("dirAngle: " + dirAngle);
        Vector2 bump = new Vector2(3, 0);
        float energyDivToughness = energy/20000f;
        damage += energyDivToughness;
//        SAIL_AREA -= energyDivToughness*10f;
        if (world.rand.nextInt(2)>1) {  // destroys bit of sail at random
            sail.area -= energyDivToughness * 10f;
            if (sail.area < 0) {
                sail.area = 0;
            }
        }

//        relVel = velocity.cpy().sub();
        float head = dirAngle - angle;
        System.out.println("angle relative to boat (heading): " + head);
        if (head > 165 && head < 195) {
//            collides on the front
            bowHull -= energyDivToughness;
            System.out.println("bowHull: " + bowHull);
        } else if (head < 5 || head > 355) {
//            collides on the front
            sternHull -= energyDivToughness;
            System.out.println("SternHull: " + sternHull);
        } else if (head < 355 && head > 195) {
//            collides right
            portHull -= energyDivToughness;
            System.out.println("portHull: " + portHull);
        } else if (head < 165 && head > 5) {
//            collides left
            starboardHull -= energyDivToughness;
            System.out.println("starboardHull: " + starboardHull);
        }
        bb.inDanger = true;
    }

    public boolean fire(float angle){
        boolean firing = false;
        if (angle < 0){
            angle +=180 + 360;
        }
        System.out.println("R... Shot detected angle: " + angle);

        if (angle > 20 && angle < 160) {
//            port side
            for (GunDeck deck : gunDecks) {
                if (deck instanceof PortGunDeck){
                    firing = deck.fire();
                }
            }
        }if (angle > 170 && angle < 190){
//            stern

        }if (angle > 200 && angle < 340){
//            starboard
            for (GunDeck deck : gunDecks) {
                if (deck instanceof StarboardGunDeck){
                    firing = deck.fire();
                }
            }

        }if (angle > 350 || angle < 10){
//            front
            System.out.println("R..... FOrwards");

            for (GunDeck deck : gunDecks) {
                if (deck instanceof ForwardGunDeck){
                    firing =deck.fire();
                }
            }

        }
        return firing;
    }

    private boolean isColliding(Vector2 p){
        float countCol = 0f;
        // BottomLeft - BottomRight
        float slope = ((bounds.lowerLeft.y - bounds.lowerRight.y) / (bounds.lowerLeft.x - bounds.lowerRight.x));
        float intercept = (bounds.lowerLeft.y - (bounds.lowerLeft.x * slope));

        // BottomLeft - TopLeft
        float slope2 = ((bounds.lowerLeft.y - bounds.topLeft.y) / (bounds.lowerLeft.x - bounds.topLeft.x));
        float intercept2 = (bounds.topLeft.y - (bounds.topLeft.x * slope2));

        // TopLeft - TopRight
        float slope3 = ((bounds.topLeft.y - bounds.topRight.y) / (bounds.topLeft.x - bounds.topRight.x));
        float intercept3 = (bounds.topRight.y - (bounds.topLeft.x * slope3));

        // TopRight - BottomRight
        float slope4 = ((bounds.topRight.y - bounds.lowerRight.y) / (bounds.topRight.x - bounds.lowerRight.x));
        float intercept4 = (bounds.lowerRight.y - (bounds.lowerRight.x * slope4));

        // Between top and bottom
        if(angle > -90 && angle < 90){
            // BottomLeft - BottomRight
            if(p.x * slope + intercept < p.x){
                countCol += 1;
            }

            // TopLeft - TopRight
            if(p.x * slope3 + intercept3 > p.y){
                countCol += 1;
            }
        }
        else{
            // BottomLeft - BottomRight
            if(p.x * slope + intercept > p.y){
                countCol += 1;
            }

            // TopLeft - TopRight
            if(p.x * slope3 + intercept3 < p.y){
                countCol += 1;
            }
        }

        // BottomLeft - TopLeft
        if(angle < 0){
            if(p.x * slope2 + intercept2 > p.y){
                countCol += 1;
            }
            if(p.x * slope4 + intercept4 < p.y){
                countCol += 1;
            }
        }
        else{
            if(p.x * slope2 + intercept2 < p.y){
                countCol += 1;
            }
            if(p.x * slope4 + intercept4 > p.y){
                countCol += 1;
            }
        }

        if(countCol >= 4){
            return true;
        }
        return false;
    }
    public void setRoutine(Routine routine){
        this.routine = routine;
    }

    public boolean hasGunDeck(int type){
//        only checks forward when type is one for now
        if (type == 1){
            for(GunDeck deck : gunDecks){
                if (deck instanceof ForwardGunDeck){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasLoadedGunDeck(int type){
        switch (type){
            case 0 :
                for (GunDeck deck : gunDecks) {
                    if (deck instanceof ForwardGunDeck && deck.state == deck.GUNS_STATE_LOADED){
                        return true;
                    }
                }
                return false;
            case 1 :
                for (GunDeck deck : gunDecks) {
                    if (deck instanceof PortGunDeck && deck.state == deck.GUNS_STATE_LOADED){
                        return true;
                    }
                }
                return false;
            case 2 :
                for (GunDeck deck : gunDecks) {
                    if (deck instanceof StarboardGunDeck && deck.state == deck.GUNS_STATE_LOADED){
                        return true;
                    }
                }
                return false;
        }
        return false;
    }

}
