package com.badlogic.androidgames.hoverboats;

import framework.classes.DynamicGameObject;
import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 22/01/2015.
 */
public class Mast extends DynamicGameObject {
    public  static float WIDTH = 0.5f;
    public static float HEIGHT = 20f;
    public float bredth;
    public float depth;
    public final Vector2 boatRelPos = new Vector2();
    public float rotation = 0;
    public static final float dR = 25;
    public float sizeScalingFactor = 1.1f;

    public Mast(float x, float y) {
        super(x, y, WIDTH, WIDTH);
        boatRelPos.set(x,y);
        bredth = 7;
        depth = 5;
//        takes position relative to the boat centre as inputs.
    }
    public Mast(float x, float y, float sizeScalingFactor) {
        super(x, y, WIDTH, WIDTH);
        boatRelPos.set(x,y);
        bredth = 7;
        depth = 5;
        this.sizeScalingFactor = sizeScalingFactor;
//        takes position relative to the boat centre as inputs.
    }




    public void updatePosition(float boatPosx, float boatPosy, float angle){
        boatRelPos.rotate(angle);
        position.set((boatPosx + boatRelPos.x), (boatPosy + boatRelPos.y));
        boatRelPos.rotate(-angle);
    }


    public void updateRotation(float targetRotation, float delta){
        if (AngleCalc.directionClockwise(rotation,targetRotation)){
            rotation += -dR*delta;
        }else if (!AngleCalc.directionClockwise(rotation,targetRotation)){
            rotation += dR*delta;
        }else if ((rotation - targetRotation) < 0.1){
        }
        if (rotation<0){
            rotation += 360;
        }
        else if (rotation>360){
            rotation-=360;
        }
    }
}
