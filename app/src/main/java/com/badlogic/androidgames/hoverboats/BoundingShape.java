package com.badlogic.androidgames.hoverboats;

import java.util.ArrayList;
import java.util.List;

import framework.classes.Vector2;

/**
 * Created by f mgregor on 27/01/2015.
 */
public class BoundingShape {

    double[] polygon1x;
    double[] polygon1y;
    double[] polygon1dist;
    List<Vector2> pointListObject = new ArrayList<Vector2>();
    List<Vector2> pointListWorld = new ArrayList<Vector2>();

    public BoundingShape(){
        polygon1y = new double[]{0,
        2.75,
        2.75,
        -2.75,
        -2.75};
        polygon1x = new double[]{10,
                4.125,
                -9,
                -9,
                4.125};
        polygon1dist = new double[polygon1x.length];
        for (int i = 0; i < polygon1x.length; i++){
            pointListObject.add(new Vector2((float)polygon1x[i],(float)polygon1y[i]));
            pointListWorld.add(new Vector2((float)polygon1x[i],(float)polygon1y[i]));
            polygon1dist[i] = pointListObject.get(i).len();
        }



    }

    public BoundingShape(double[] polyX, double[] polyY){
        polygon1dist = new double[polygon1x.length];
        for (int i = 0; i < polygon1x.length; i++){
            pointListObject.add(new Vector2((float)polygon1x[i],(float)polygon1y[i]));
            pointListWorld.add(new Vector2((float)polygon1x[i],(float)polygon1y[i]));
            polygon1dist[i] = pointListObject.get(i).len();
        }
    }

    public void updatePos(Vector2 centre, float angle){
        for (int i = 0; i < polygon1x.length; i++){
            Vector2 temp = (pointListObject.get(i).cpy().rotate(angle));
            pointListWorld.get(i).set(temp);

            pointListWorld.get(i).add(centre.x,
                    +centre.y);

            polygon1x[i] = pointListWorld.get(i).x;
            polygon1y[i] = pointListWorld.get(i).y;
        }
    };

    public void setSize(double[] polyX, double[] polyY){
        polygon1dist = new double[polygon1x.length];
        for (int i = 0; i < polygon1x.length; i++){
            pointListObject.add(new Vector2((float)polygon1x[i],(float)polygon1y[i]));
            pointListWorld.add(new Vector2((float)polygon1x[i],(float)polygon1y[i]));
            polygon1dist[i] = pointListObject.get(i).len();
        }

    }
}
