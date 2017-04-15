package com.badlogic.androidgames.hoverboats;

import framework.classes.DynamicGameObject;
import framework.classes.GameObject;

/**
 * Created by f mgregor on 28/01/2015.
 */
public class ToastMessage extends DynamicGameObject {

    public static final int DISPLAY = 0;
    public static final int FINISHED = 1;
    public int state = DISPLAY;

    public static float HEIGHT = 3;
    public static float WIDTH = 9;
    String message;
    public float stateTime = 0;
    public float maxTime = 2;



    public ToastMessage(float x, float y, String string) {
        super(x, y, WIDTH, HEIGHT);
        this.message = string;
    }
    public ToastMessage(float x, float y){
        super(x, y, WIDTH, HEIGHT);
    }


    public void update(float delta){
        if (state == DISPLAY) {
            if (stateTime < maxTime) {
                stateTime += delta;
            }else{
                state = FINISHED;
//                System.out.println("finished");
            }
        }
    }

    public void reset(){
        state = DISPLAY;
        stateTime = 0;
    }


}
