package com.badlogic.androidgames.hoverboats;

import java.util.ArrayList;
import java.util.List;

import framework.classes.DynamicGameObject;
import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 11/01/2015.
 */
public class ForcesHandler {

    public static void calculateLoads(Ship ship, float delta, float tillerPos) {
        ship.bb.lastPosition.set(ship.position);
        List<Vector2> forces = new ArrayList<Vector2>();
        Vector2 impulse = ship.impulse.cpy().mul(1/delta).rotate(-ship.angle);
        Vector2 v1 = ship.velocity.cpy();
        forces.add(impulse);
        forces.addAll(calcWindWater(ship, v1, ship.angle));
        //       V(t+1) = V(t)+(k1+k2)*0.5*dt
//        k1 = acceleration(t), k2 = acceleration(t+1)

        Vector2 dV = new Vector2(0,0);
        for (Vector2 force : forces){
            dV.add(force);
        }
        Vector2 k1 = dV.mul(1/ship.VESSEL_MASS).cpy();

        v1.rotate(-ship.angle);
        Vector2 vt2 = v1.cpy().add(dV.mul(delta));
        forces.clear();
        forces.addAll(calcWindWater(ship, vt2.rotate(ship.angle), ship.angle));
        forces.add(impulse);
        dV.x=0; dV.y=0;
        for (Vector2 force : forces){
            dV.add(force);
        }
        Vector2 k2 = dV.mul(1/ship.VESSEL_MASS);

        k1.add(k2);
        ship.acceleration = k1.cpy().mul(0.5f);
        k1.mul(delta*0.5f);

        ship.velocity.rotate(-ship.angle);
        ship.velocity.add(k1);

        Vector2 dS = new Vector2();

//        displacement in local coordinates
        dS.x = (v1.x + ship.velocity.x) * delta/2f;
        dS.y = (v1.y + ship.velocity.y) * delta/2f;
        float dAngle = dS.x * ship.TURN_SPEED * (tillerPos / 10f);
        ship.angVel.z = dAngle/delta;

        ship.position.add(dS.rotate(ship.angle));

        //       S(t+1) = S(t)+(k1+k2)*0.5*dt
//        k1 = velocity(t), k2 = velocity(t+1)
        ship.velocity.rotate(ship.angle);
        ship.angle += dAngle;
        ship.dAngle = dAngle;
        if (ship.angle < 0) {
            ship.angle += 360;
        } else if (ship.angle > 360) {
            ship.angle -= 360;
        }
        ship.impulse.set(0,0);
    }

    public static List<Vector2> calcWindWater(Ship ship, Vector2 velocity, float angle){

//        takes a velocity in world coordinates, outputs two force vectors in local coordinates
        float Cdx = ship.Cdx;
        float CdxNormal = ship.CdxNormal;
        float Cdy = ship.Cdy;
        float CdyNormal = ship.CdyNormal;
        float sailArea = ship.sail.area;
        float ro = 1.2754f;
        float CdWaterX = 150f;
        float CdWaterY = 5000f;
        Vector2 windForce = new Vector2(0,0);
        Vector2 apparentWind = new Vector2();
        Vector2 relativeWater = new Vector2();
        Vector2 waterForces = new Vector2();
        List<Vector2> forces = new ArrayList<Vector2>();
        apparentWind.x = World.instantaneousWind.x - velocity.x;
        apparentWind.y = World.instantaneousWind.y - velocity.y;

        relativeWater.x = World.stream.x - velocity.x;
        relativeWater.y = World.stream.y - velocity.y;

        //        calculate forces on boat normalised in terms of the boat aiming east along x axis
//        object coordinate system.
        apparentWind.rotate(-angle);
        relativeWater.rotate(-angle);
        calcSailAngle(AngleCalc.reverse(apparentWind.angle()),ship);
        if ((apparentWind.angle()) < 200 && (apparentWind.angle() > 160)) {
            Cdx = 0.0f; CdxNormal = 0.05f; Cdy = 0.1f;}
        else if((apparentWind.angle()) < 220 && (apparentWind.angle() > 200)
                || (apparentWind.angle()) < 160 && (apparentWind.angle() > 140) ){
            Cdy = 0.4f; Cdx = 0.3f; CdxNormal = 0.4f;
        }
        else {
            Cdy = 0.4f; Cdx = 0.6f; CdxNormal = 0.4f;

        }

//        aggregate forces from the sail
        windForce.x = Math.abs(0.5f * apparentWind.x * apparentWind.len() * ro * sailArea * Cdx);
//      proportion of y sideways wind that is coverted into forward propulsion
        windForce.x += 0.5f * Math.abs(apparentWind.y * apparentWind.len() * ro * sailArea * Cdy);
//        the proportion of the x-normal wind that is converted directly into propulsion,
//        slows into the wind, speeds downwind
        windForce.x += 0.5f * (apparentWind.x * apparentWind.len() * ro * sailArea * CdxNormal);
//      proportion of y direction (sideways wind) which pushes sideways
        windForce.y = 0.5f * apparentWind.y * apparentWind.len() * ro * sailArea * CdyNormal;



        waterForces.x = relativeWater.x * relativeWater.len() * CdWaterX;
        waterForces.y = relativeWater.y * relativeWater.len() * CdWaterY;
        if (ship instanceof PlayerShip) {
//            System.out.println("Sail Area: " + sailArea);
//            System.out.println("WindForce: " + windForce.display());
//            System.out.println("WaterForce: " + waterForces.display());
        }

        forces.add(windForce);
        forces.add(waterForces);
        return forces;
    }

    public static void calculateLoads(CannonBall ball, float delta) {

        List<Vector2> forces = new ArrayList<Vector2>();
        Vector2 impulse = ball.impulse.cpy().mul(1 / delta);
        Vector2 v1 = ball.velocity.cpy();
        forces.add(impulse);
        forces.add(windForceParticleCalc(ball));

        //       V(t+1) = V(t)+(k1+k2)*0.5*dt
//        k1 = acceleration(t), k2 = acceleration(t+1)

        Vector2 dV = new Vector2(0,0);
        for (Vector2 force : forces){
            dV.add(force);
        }
        Vector2 k1 = dV.mul(1/ball.BALL_MASS).cpy();


        Vector2 vt2 = v1.cpy().add(dV.mul(delta));
        forces.clear();
//        forces.addAll(calcWindWater(ball, vt2.rotate(ball.angle), ball.angle));
        forces.add(impulse);
        dV.x=0; dV.y=0;
        for (Vector2 force : forces){
            dV.add(force);
        }
        Vector2 k2 = dV.mul(1/ball.BALL_MASS);

        k1.add(k2);
        k1.mul(delta*0.5f);


        ball.velocity.add(k1);

        Vector2 dS = new Vector2();

//        displacement in local coordinates
        dS.x = (v1.x + ball.velocity.x) * delta/2f;
        dS.y = (v1.y + ball.velocity.y) * delta/2f;

        ball.line = ball.position.cpy();
        ball.position.add(dS);
        //       S(t+1) = S(t)+(k1+k2)*0.5*dt
//        k1 = velocity(t), k2 = velocity(t+1)


        ball.impulse.set(0,0);
    }

    private static void calcSailAngle(float relWindAngle, Ship ship){
        if (relWindAngle<0){
            relWindAngle += 360;
        }
        float maxAngle = 15f;  // Maximum tilting of the sail beams from perpendicular
        if (relWindAngle > 90 && relWindAngle < 270){
            ship.sailAngle = (maxAngle/90f) * (relWindAngle - 180f);
        } else if (relWindAngle < 90 && relWindAngle > 30){
            ship.sailAngle = 360 - maxAngle;
        } else if (relWindAngle > 270 && relWindAngle < 330){
            ship.sailAngle = maxAngle;
        } else if (relWindAngle<20 || relWindAngle > 340){
            ship.sailAngle = 0;
        }
        if (ship.sailAngle < 0){
            ship.sailAngle += 360;
        }
    }

    public static void calculateLoads(Smoke smoke, float delta) {

        List<Vector2> forces = new ArrayList<Vector2>();
        Vector2 v1 = smoke.velocity.cpy();
        forces.add(windForceParticleCalc(smoke));

        //       V(t+1) = V(t)+(k1+k2)*0.5*dt
//        k1 = acceleration(t), k2 = acceleration(t+1)

        Vector2 dV = new Vector2(0,0);
        for (Vector2 force : forces){
            dV.add(force);
        }
        Vector2 k1 = dV.mul(1/smoke.mass).cpy();


        Vector2 vt2 = v1.cpy().add(dV.mul(delta));
        forces.clear();
//        forces.addAll(calcWindWater(ball, vt2.rotate(ball.angle), ball.angle));

        dV.x=0; dV.y=0;
        for (Vector2 force : forces){
            dV.add(force);
        }
        Vector2 k2 = dV.mul(1/smoke.mass);

        k1.add(k2);
        k1.mul(delta*0.5f);


        smoke.velocity.add(k1);

        Vector2 dS = new Vector2();

//        displacement in local coordinates
        dS.x = (v1.x + smoke.velocity.x) * delta/2f;
        dS.y = (v1.y + smoke.velocity.y) * delta/2f;


        smoke.position.add(dS);

        //       S(t+1) = S(t)+(k1+k2)*0.5*dt
//        k1 = velocity(t), k2 = velocity(t+1)




    }

    public static Vector2 windForceParticleCalc(DynamicGameObject ball){
        Vector2 windForce = new Vector2(0,0);
        Vector2 apparentWind = new Vector2();

        float A = (float)Math.PI*ball.width*ball.height*0.25f;
        float ro = 1.2754f;
        float Cd = 0.47f;
        apparentWind.x = World.instantaneousWind.x - ball.velocity.x;
        apparentWind.y = World.instantaneousWind.y - ball.velocity.y;
        windForce.x = 0.5f * apparentWind.x * apparentWind.len() * ro * A * Cd;
        windForce.y = 0.5f * apparentWind.y * apparentWind.len() * ro * A * Cd;
        return windForce;
    }

}
