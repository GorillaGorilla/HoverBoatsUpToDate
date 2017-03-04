package com.badlogic.androidgames.hoverboats;

import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 08/02/2015.
 */
public class Sail extends ShipComponents {
    private float area = 500f;
    private float CHANGEOVER_TIME = 5f;
    private float changingTimer = 0f;
    private String STATE = "BALANCED";
    private float Cdx;
    private float CdxNormal;
    private float Cdy;
    private float ro = 1.2754f;

    public Sail(float area){
        this.area = area;
    }
    
    public Vector2 calcSailForces(Vector2 apparentWind, float CdyNormal){

        Vector2 windForce = new Vector2();



        if ((apparentWind.angle()) < 200 && (apparentWind.angle() > 160)) {
//            wind from behind
            Cdx = 0.0f; CdxNormal = 0.05f; Cdy = 0.1f;}
        else if((apparentWind.angle()) < 220 && (apparentWind.angle() > 200)
                || (apparentWind.angle()) < 160 && (apparentWind.angle() > 140) ){
//            wind from side-on but behind
            Cdy = 0.4f; Cdx = 0.3f; CdxNormal = 0.4f;
        }
        else {
            Cdy = 0.4f; Cdx = 0.6f; CdxNormal = 0.4f;

        }

//        aggregate forces from the sail
        windForce.x = Math.abs(0.5f * apparentWind.x * apparentWind.len() * ro * area * Cdx);
//      proportion of y sideways wind that is coverted into forward propulsion
        windForce.x += 0.5f * Math.abs(apparentWind.y * apparentWind.len() * ro * area * Cdy);
//        the proportion of the x-normal wind that is converted directly into propulsion,
//        slows into the wind, speeds downwind
        windForce.x += 0.5f * (apparentWind.x * apparentWind.len() * ro * area * CdxNormal);
//      proportion of y direction (sideways wind) which pushes sideways
        windForce.y = 0.5f * apparentWind.y * apparentWind.len() * ro * area * CdyNormal;

        return windForce;
    }
    
    public float getArea(){
        return area;
    }

    public void setArea(float newArea){
        this.area = newArea;
    }
}
