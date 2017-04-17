package com.badlogic.androidgames.hoverboats;

import com.badlogic.androidgames.routines.EngageEnemy;
import com.badlogic.androidgames.routines.Flee;
import com.badlogic.androidgames.routines.HoldCourse;
import com.badlogic.androidgames.routines.HuntPatrol;
import com.badlogic.androidgames.routines.Idle;
import com.badlogic.androidgames.routines.IsSafeTest;
import com.badlogic.androidgames.routines.Patrol;
import com.badlogic.androidgames.routines.PatrolWarily;
import com.badlogic.androidgames.routines.Routine;
import com.badlogic.androidgames.routines.Routines;
import com.badlogic.androidgames.routines.SailAwaySimple;
import com.badlogic.androidgames.routines.Selector;
import com.badlogic.androidgames.routines.Sequence;
import com.badlogic.androidgames.routines.SetCourse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import framework.classes.GameObject;
import framework.classes.OverlapTester;
import framework.classes.Rectangle;
import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 04/01/2015.
 */
public class World {
    public interface WorldListener {
        public void hit();
        public void fire();
    }

    public static final float WORLD_WIDTH = 80*50;
    public static final float WORLD_HEIGHT = 120*50;
    public static final int WORLD_STATE_RUNNING = 0;
    public static final int WORLD_STATE_NEXT_LEVEL = 1;
    public static final int WORLD_STATE_GAME_OVER = 2;
    public static final Vector2 gravity = new Vector2(0f, -9.8f);
    public static final Vector2 wind = new Vector2(5,0.5f);
    public static final Vector2 stream = new Vector2(0,0);
    public static final Vector2 instantaneousWind = wind.cpy();
    public List<Vector2> windHist = new ArrayList<Vector2>();
    EnemyShip enemy;

    public final PlayerShip hmsVictory;
    public final List<Buoy> buoys;
    public final List<Rock> rocks;
    public final List<Ship> ships;
    public final List<CannonBall> balls;
    public final List<Smoke> smokes;
    public final List<Hit> hits = new ArrayList<Hit>();
    public final List<ToastMessage> toasts = new ArrayList<ToastMessage>();
    public final List<IngameMessage> messages = new ArrayList<IngameMessage>();
    public final WorldListener listener;
    public final Random rand;
    public boolean DEBUG_MODE = false;

    public int score;
    public int state;
    public float timer, t2;

    List<Vector2> patrolPoints = new ArrayList<Vector2>();



    public World(WorldListener listener) {
        this.hmsVictory = new PlayerShip(WORLD_WIDTH/2, WORLD_HEIGHT/2, new Vector2(1,0), wind, this);
        this.ships = new ArrayList<Ship>();
        this.rocks = new ArrayList<Rock>();
        this.balls = new ArrayList<CannonBall>();
        this.smokes = new ArrayList<Smoke>();
        this.buoys = new ArrayList<Buoy>();

//        patrol points to be marked with buoys
        patrolPoints.add(new Vector2(2000f,3750f));
        patrolPoints.add(new Vector2(1500f,3750f));
        patrolPoints.add(new Vector2(1400f,3000f));
        patrolPoints.add(new Vector2(1400f,2250f));
        patrolPoints.add(new Vector2(2000f,2250f));

        for (Vector2 point : patrolPoints){
            buoys.add(new Buoy(point.x,point.y));
        }

        this.listener = listener;
        rand = new Random();
        generateLevel();
        this.score = 0;
        this.state = WORLD_STATE_RUNNING;
        timer = 0;
        t2 = 0;
    }

    private void generateLevel() {

        hmsVictory.state = hmsVictory.VESSEL_STATE_SAILING;
        hmsVictory.velocity.set(2,0);
        for (int i = 0; i<3; i++) {
            rocks.add(new Rock(2050 + (i+1) * 80, (3000 + (i+1) * 80)));
            ships.add(new CargoBoat(2200 + (i+1) * 150, (3040 + (i+1) * 0),new Vector2(1,0f),wind,this));
        }

        for (Ship ship : ships){
            ship.velocity.set(3,0).rotate(ship.angle);
            ship.bb.targets.add(hmsVictory);
            ship.setRoutine(new SailAwaySimple(patrolPoints));
            ship.routine.reset();
        }
//        extra enemy for fun
        enemy = new EnemyShip(1850, (3000),new Vector2(5,0f),wind,this);
        enemy.setName("BlackBeard");
        enemy.setRoutine(new HuntPatrol(patrolPoints));
//        enemy.setRoutine(Routines.engageEnemy());
        enemy.bb.targets.add(hmsVictory);
        enemy.routine.reset();
        ships.add(enemy);
        for (Ship ship : ships){
            if (ship instanceof CargoBoat){
                ship.pointsForKill = 100f;
                ship.pointsForHit = 5f;
            }else if (ship instanceof EnemyShip){
                ship.pointsForKill = 15f;
                ship.pointsForHit = 5f;
            }
        }


    }

    public void update(float delta, float tillerPosition){
        if (DEBUG_MODE){
            delta = delta*6;
        }
        delta = delta*1.5f;

        checkCollisions();
        ForcesHandler.calculateLoads(hmsVictory, delta, tillerPosition);
        hmsVictory.update(delta, tillerPosition);
        updateHits(delta);
        updateBuoys(delta);
        updateBalls(delta);
        updateShips(delta);
        updateSmoke(delta);
        updateToasts(delta);
        checkGameOver();

        timer += delta;
        if (timer > 1){
            System.out.println("ship position: " + ships.get(0).position.display());
            System.out.println("player position: " + hmsVictory.position.display());
            System.out.println("angVel" + hmsVictory.angVel.z);
            timer = 0;
            updateWind();
        }

        t2 += delta;
        if (t2 > 300){
            spawnNewBoats();
            spawnNewBoats();
            t2 = 0;
        }


    }

    private void updateToasts(float delta) {
        for (ToastMessage toastMessage : toasts){
            toastMessage.update(delta);
        }
    }

    private void updateBuoys(float delta) {
        for (Buoy buoy : buoys){
            buoy.update(delta);
        }
    }

    private void updateHits(float delta) {
        Iterator<Hit> iter = hits.iterator();
        while (iter.hasNext()) {
            Hit entity = iter.next();

            if (entity.state == 1) {
                System.out.println("R... Remove hit ");
                entity = null;
                iter.remove();
            }else{
                entity.update(delta);
            }
        }
    }

    private void updateSmoke(float delta) {
        Iterator<Smoke> iter = smokes.iterator();
        while (iter.hasNext()) {
            Smoke entity = iter.next();

            if (!entity.isAlive()) {
                entity = null;
                iter.remove();
            }else{
                ForcesHandler.calculateLoads(entity, delta);
                entity.update(delta);
            }
        }
    }

    private void physics(float delta) {
        calcLoads();
    }

    private void calcLoads() {
        for (Ship ship : ships) {
//            ForcesHandler.calculateLoads(ship);
        }

    }

    private void updateShips(float delta) {
        if (!ships.isEmpty()){
            for (Ship ship : ships){
                if (ship.state == ship.VESSEL_STATE_SAILING) {
                    ForcesHandler.calculateLoads(ship, delta, ship.tillerPos);
                }
                ship.update(delta);
//                System.out.println("tillerpos: " + ship.tillerPos);
            }
        }
    }

    private void updateBalls(float delta) {
        for (CannonBall ball : balls){
            if (ball.isAlive()){
                ForcesHandler.calculateLoads(ball,delta);
                ball.update(delta);
            }
        }
        Iterator<CannonBall> iter = balls.iterator();
        while (iter.hasNext()) {
            CannonBall entity = iter.next();

            if (entity.z < 0) {
                addSmoke(entity.position);
                System.out.println("splashdown");
                entity = null;
                iter.remove();
            }
        }
    }

    private void checkCollisions() {
        checkRockCollisions(hmsVictory);
//        CollisionTester.checkVertexCollisions(hmsVictory, ships.get(0));
        checkBallCollisions();
        checkShipCollisions();
    }

    private void checkShipCollisions() {
        Iterator<Ship> iter = ships.iterator();
        while (iter.hasNext()) {
            Ship entity = iter.next();
                checkRockCollisions(entity);
                for(Ship ship : ships){
                    if (entity == ship) {
                        continue;
                    }
                    CollisionTester.checkVertexCircleCollisions(entity, ship);
//                        must be returned to their actual orientation
            }

            CollisionTester.checkVertexCircleCollisions(entity, hmsVictory);
//            CollisionTester.checkVertexCollisions(entity, hmsVictory);
        }
    }


    private void checkRockCollisions(Ship ship){
        int len = rocks.size();
        for (int i = 0; i < len; i++) {
            Rock rock = rocks.get(i);
            if (CollisionTester.checkCircleCollisions(ship, rock)){
            break;}

        }
    }
    private void checkBallCollisions(){
        int len = balls.size();
        for (int i = 0; i < len; i++) {
            CannonBall ball = balls.get(i);
            for (Ship ship : ships){
                if (!ship.firedBalls.contains(ball)) {
//                    tests for collisions, momentum/energy calc included inside
//   collision tester method as these are used for damage calc
                    if (CollisionTester.checkCircleCollisions(ship, ball)) {
//                        if statement used to calculate points. Should be moved to an external
//                        method
                        if (ship instanceof CargoBoat || ship instanceof EnemyShip){
                            score += ship.pointsForHit;
                        }
                    };
                }
            }
            CollisionTester.checkCircleCollisions(hmsVictory, ball);

        }

    }

    public void updateWind(){
//        call every second, each windHist is the average over a second
        float variance = rand.nextFloat()*1f - 0.5f;
        float angle = rand.nextFloat()*360f;
        instantaneousWind.add(new Vector2(variance,0).rotate(angle));
        if (instantaneousWind.len() > 6f ){
            float ang = instantaneousWind.angle();
            instantaneousWind.set(6f, 0);
            instantaneousWind.rotate(ang);
        }

        windHist.add(instantaneousWind.cpy());

        if (windHist.size()>60){
            windHist.remove(0);
        }
//        System.out.println("instant wind: " + instantaneousWind.display());
        Vector2 windAv = new Vector2();
        for (Vector2 reading : windHist){ // add up the readings
            windAv.add(reading);
        }
        windAv.mul(1f/windHist.size());
        wind.set(windAv);
//        System.out.println("windSize: " + windHist.size());
//        System.out.println("wind: " + wind.display());
//        instantaneousWind
//        wind;
    }

    public void checkGameOver(){
        if (hmsVictory.state == hmsVictory.VESSEL_STATE_SUNK){
            state = WORLD_STATE_GAME_OVER;
        }
    }

    public void spawnNewBoats(){
        int xPos, yPos;
        Vector2 h = new Vector2(3,0);
        Ship boat = new EnemyShip(0,0, h, wind, this);
        boat.pointsForKill = 15f;
        boat.pointsForHit = 5f;
            boolean retry = true;
            while (retry) {
                retry = false;
                xPos = rand.nextInt((int) WORLD_WIDTH) ;  // may not produce what I want, check later
                yPos = rand.nextInt((int) WORLD_HEIGHT);
                boat.position.set(xPos, yPos);
                System.out.println("checking");
                for (GameObject ship : ships) {
                    if (CollisionTester.checkClose(boat, ship, 100)){
                        retry = true;
                    }
                }

                for (GameObject rock : rocks) {
                    if(CollisionTester.checkClose(boat, rock, 100)){
                        retry = true;
                    }
                }

                for (GameObject ball : balls) {
                    if(retry = CollisionTester.checkClose(boat, ball, 100)){
                        retry = true;
                    }
                }
            }
        System.out.println("space found");
        boat.setRoutine(Routines.huntPatrol(patrolPoints));
        boat.bb.targets.add(hmsVictory);
        boat.routine.reset();
            ships.add(boat);

    }

    public void addSmoke(Vector2 pos){
        smokes.add(new Smoke(pos.x,pos.y));
    }


}
