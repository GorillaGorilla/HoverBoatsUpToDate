package com.badlogic.androidgames.hoverboats;

import java.util.ArrayList;
import java.util.List;

import framework.classes.GameObject;
import framework.classes.OverlapTester;
import framework.classes.Vector2;
import framework.classes.Vector3;

/**
 * Created by New PC 2012 on 10/01/2015.
 */
public class CollisionTester {
    final static float ctol = 0.1f;
    final float tol = 0.1f;
    static float e = 0.5f; // restitution

    


    public static boolean checkCircleCollisions(Ship ship1, Ship ship2){
        Vector2 d;
        Vector2 fI = new Vector2(0,0);
        float r;
        int retval = 0;
        float s;
        r = ship1.VESSEL_LENGTH/2 + ship2.VESSEL_LENGTH/2;
        d = ship1.position.cpy().sub(ship2.position);
        s = d.len() - r;
        Vector2 vr = ship1.velocity.cpy().sub(ship2.velocity);
        float vrn = vr.rotate(-d.angle()).x;

//        ctol is a number which decides how sensitive we want detection to be: 0 would be touching
        if(s <= ctol && vrn < 0) {
//        calculate impulse and apply to both ships
            float j = -(1 + e) * vrn;
            j /= (1 / ship1.VESSEL_MASS) + (1 / ship2.VESSEL_MASS);
            fI.x = j;
            fI.rotate(d.angle());
            ship1.impulse.add(fI);
            ship2.impulse.add(fI.mul(-1f));
            fI.set(0,0);
            return true;
        }
        return false;

    }

    public static boolean checkClose(GameObject ob1, GameObject ob2, float distance){

        if(ob1.position.distSquared(ob2.position.x,ob2.position.y)< distance*distance){
            return true;
        }else{
            return false;
        }

    }

    public static boolean checkVertexCircleCollisions(Ship ship1, Ship ship2){
        boolean intersect = false;
        float v = 0;
//        double[] polygon1x = {ship1.bounds.lowerLeft.x,
//                ship1.bounds.lowerRight.x,
//                ship1.bounds.topRight.x,
//                ship1.bounds.topLeft.x,
//        };
//
//        double[] polygon1y = {ship1.bounds.lowerLeft.y,
//                ship1.bounds.lowerRight.y,
//                ship1.bounds.topRight.y,
//                ship1.bounds.topLeft.y,
//        };
//        double[] polygon2x = {ship2.bounds.lowerLeft.x,
//                ship2.bounds.lowerRight.x,
//                ship2.bounds.topRight.x,
//                ship2.bounds.topLeft.x,
//        };
//
//        double[] polygon2y = {ship2.bounds.lowerLeft.y,
//                ship2.bounds.lowerRight.y,
//                ship2.bounds.topRight.y,
//                ship2.bounds.topLeft.y,
//        };
        double[] polygon1x = ship1.boundingShape.polygon1x;
        double[] polygon1y = ship1.boundingShape.polygon1y;

        double[] polygon2x = ship2.boundingShape.polygon1x;
        double[] polygon2y = ship2.boundingShape.polygon1y;

        for (int i = 0; i<polygon1x.length && intersect == false; i++){
        if (isPointInsidePolygon(polygon1x,polygon1y,polygon2x[i],polygon2y[i])) {
            intersect = true;
//            find angular velocity of point in question
             v = (float)ship2.boundingShape.polygon1dist[i]*(float)Math.sin(ship2.angVel.z);
//            System.out.println("Velocity of point due to rotation"+v);
//            distanceToCentre*Math.sin(ship1.dAngle);
        }else if (isPointInsidePolygon(polygon2x,polygon2y,polygon1x[i],polygon1y[i])){
            intersect = true;
             v = (float)ship1.boundingShape.polygon1dist[i]*(float)Math.sin(ship1.angVel.z);
//            System.out.println("Velocity of point due to rotation"+v);
        }

        }

        if (intersect) {
            Vector2 d;
            Vector2 fI = new Vector2(0, 0);
            float r;
            int retval = 0;
            float s;
            r = ship1.VESSEL_LENGTH*2 + ship2.VESSEL_LENGTH*2;
            d = ship1.position.cpy().sub(ship2.position);
            s = d.len() - r;
            Vector2 vr = ship1.velocity.cpy().sub(ship2.velocity);

            float vrn = vr.rotate(-d.angle()).x;
            vrn -= Math.abs(v)/2;

//        ctol is a number which decides how sensitive we want detection to be: 0 would be touching
            if (s <= ctol && vrn < 0) {
//        calculate impulse and apply to both ships
                float j = -(1 + e) * vrn;
                j /= (1 / ship1.VESSEL_MASS) + (1 / ship2.VESSEL_MASS);
                fI.x = j;
                fI.rotate(d.angle());
                ship1.impulse.add(fI);
                ship2.impulse.add(fI.mul(-1f));
                fI.set(0, 0);
                return true;
            }
        }
        return false;

    }

    public static boolean checkCircleCollisions(Ship ship1, Rock rock){
        Vector2 d;
        Vector2 fI = new Vector2(0,0);
        float r;
        int retval = 0;
        float s;
        r = ship1.VESSEL_LENGTH/2 + rock.bounds2.radius/2;
        d = ship1.position.cpy().sub(rock.position);
        s = d.len() - r;
        Vector2 vr = ship1.velocity.cpy();
        float vrn = vr.rotate(-d.angle()).x;

//        ctol is a number which decides how sensitive we want detection to be: 0 would be touching
        if(s <= ctol && vrn < 0) {
//        calculate impulse and apply to both ships
            float j = -(1 + e) * vrn;
            j /= (1 / ship1.VESSEL_MASS);
            fI.x = j;
            fI.rotate(d.angle());
            ship1.impulse.add(fI);

            return true;
        }
        return false;

    }

    public static boolean checkCircleCollisions(Ship ship1, CannonBall ball){
//       calculates whether a hit has occurred.
        ToastMessage toast;

        double[] polygon1x = ship1.boundingShape.polygon1x;
        double[] polygon1y = ship1.boundingShape.polygon1y;

        if (isPointInsidePolygon(polygon1x,polygon1y,ball.position.x,ball.position.y)) {

//            once the ball is determined to be inside the polygon, the forces are calculated by
// simple point momentum. This whole section should be moved into a new function (damage handler?)
//


            if (ball.z < ship1.VESSEL_HEIGHT * 2) {
                Vector2 d;
                Vector2 fI = new Vector2(0, 0);
                float r;
                int retval = 0;
                float s;
                r = ship1.VESSEL_LENGTH / 2 + ball.BALL_WIDTH / 2;
                d = ship1.position.cpy().sub(ball.position);
                s = d.len() - r;
                Vector2 vr = ship1.velocity.cpy().sub(ball.velocity);
                float vrn = vr.rotate(-d.angle()).x;


//        ctol is a number which decides how sensitive we want detection to be: 0 would be touching
                if (s <= ctol && vrn < 0) {
//        calculate impulse and apply to both ships
                    System.out.println("Ship hit by cannonball: ");
                    float e = Math.min(ship1.e, ball.e);
                    float j = -(1 + e) * vrn;
                    j /= (1 / ship1.VESSEL_MASS) + (1 / ball.BALL_MASS);
                    fI.x = j;
                    fI.rotate(d.angle());
                    ball.impulse.sub(fI);
                    float energy = 0.5f * ball.BALL_MASS * (vrn * vrn);
                    ship1.cannonballHit(fI, energy);
                    ship1.world.hits.add(new Hit(6, (int) ball.position.x, (int) ball.position.y));
                    fI.set(0, 0);
                    System.out.println("Ship damage: " + ship1.damage);
//                    StringBuilder sb = new StringBuilder("Hit! ");
//                    sb.append(Float.toString(energy/1000f));
                    String str = String.format("%.2f", energy/20000f);
                    toast = new ToastMessage(ball.position.x, ball.position.y,str);
                    ship1.world.toasts.add(toast);
                    return true;
                }
            }
        }
        return false;

    }


    public static int checkVertexCollisions(Ship ship1, Ship ship2){
        Vector3 d;
        float r;
        int retval = 0;
        float s;
        List<Vector2> vList1 = new ArrayList<Vector2>();
        List<Vector2> vList2 = new ArrayList<Vector2>();
        float wd, lg;
        int i,j;
        boolean haveNodeNode = false;
        boolean interpenetrating = false;
        boolean haveNodeEdge = false;

        Vector2 v1, v2, u, dd;
        Vector2 edge, p;
        Vector2 ddd = new Vector2();
        Vector3 proj;
        d = new Vector3();
        Vector2 fI = new Vector2();
        Vector2 vCollisionPoint;
        Vector3 vCollisionPoint1 = new Vector3();
        Vector3 vCollisionPoint2 = new Vector3();
        float dist, dot;
        float vrn = 0;


        // First check to see if the bounding circles are colliding
        r = ship1.VESSEL_LENGTH/2 + ship2.VESSEL_LENGTH/2;
        dd = ship1.position.cpy().sub(ship2.position);
        s = dd.len() - r;
        Vector2 vr = ship1.velocity.cpy().sub(ship2.velocity);
        vrn = vr.rotate(-dd.angle()).x;

//        ctol is a number which decides how sensitive we want detection to be: 0 would be touching
        if(s <= ctol) {

            Vector2 ll1 = ship1.bounds.lowerLeft.cpy();
            Vector2 lr1 = ship1.bounds.lowerRight.cpy();
            Vector2 tr1 = ship1.bounds.topRight.cpy();
            Vector2 tl1 = ship1.bounds.topLeft.cpy();
            vList1.add(0,tr1);
            vList1.add(1,lr1);
            vList1.add(2,ll1);
            vList1.add(3,tl1);
            double[] polygon1 = {ll1.x, ll1.y,
                    lr1.x,lr1.y,
                    tr1.x,tr1.y,
                    tl1.x, tl1.y
            };
            double[] polygon1x = {ll1.x,
                    lr1.x,
                    tr1.x,
                    tl1.x,
            };

            double[] polygon1y = {ll1.y,
                    lr1.y,
                    tr1.y,
                    tl1.y
            };


            Vector2 ll2 = ship2.bounds.lowerLeft.cpy();
            Vector2 lr2 = ship2.bounds.lowerRight.cpy();
            Vector2 tr2 = ship2.bounds.topRight.cpy();
            Vector2 tl2 = ship2.bounds.topLeft.cpy();

            vList2.add(0,tr2);
            vList2.add(1,lr2);
            vList2.add(2,ll2);
            vList2.add(3,tl2);

            double[] polygon2 = {ll2.x, ll2.y,
                    lr2.x,lr2.y,
                    tr2.x,tr2.y,
                    tl2.x, tl2.y
            };
            double[] polygon2x = {ll2.x,
                    lr2.x,
                    tr2.x,
                    tl2.x,
            };

            double[] polygon2y = {ll2.y,
                    lr2.y,
                    tr2.y,
                    tl2.y
            };

//            Check each vertex for vertexvertex collisions
//            for (i = 0; i < 4 && !haveNodeNode; i++) {
//                for (j = 0; j < 4 && !haveNodeNode; j++) {
//                    vCollisionPoint = vList1.get(i);
//                    vCollisionPoint1.x = vCollisionPoint.x - ship1.position.x;
//                    vCollisionPoint1.y = vCollisionPoint.y - ship1.position.y;
//                    vCollisionPoint2.x = vCollisionPoint.x - ship2.position.x;
//                    vCollisionPoint2.y = vCollisionPoint.y - ship2.position.y;
////                    determining collisional normal vector, (assumed to be along direct path
////                    between centres of gravity
//                    Vector2 vCollisionNormal = ship1.position.cpy().sub(ship2.position);
//                    Vector3 vColNorm3 = new Vector3(vCollisionNormal.x,vCollisionNormal.y, 0);
//                    vCollisionNormal.nor();
//                    v1 = ship1.velocity.cpy();
//                    v2 = ship2.velocity.cpy();
////                    v1.rotate(ship1.angle);
////                    v2.rotate(ship2.angle);
////                    Vector2 ddd = ship1.position.cpy().sub(ship2.position);
////                    Vector2 vr = ship1.velocity.cpy().sub(ship2.velocity);
////                    vrn = vr.rotate(-ddd.angle()).x;
//
//                    Vector2 vRelativeVelocity = v1.sub(v2);
//                    Vector3 vRelVel = new Vector3(vRelativeVelocity.x, vRelativeVelocity.y, 0);
//                    vrn = vRelVel.dotProduct(vColNorm3);
//
//
//                    if (ArePointsEqual(vList1.get(i),vList2.get(j))&&
//                            (vrn<0)) {
//                        haveNodeNode = true;
//                        System.out.println("vertexvertex collision vrn: " + vrn);
//                    }
//                }
//            }

//            Check for vertex-edge collision
//            if (!haveNodeNode) {
//                for (i = 0; i < 4 && !haveNodeEdge; i++) {
//                    for (j = 0; j < 3 && !haveNodeEdge; j++) {
//                        if (j == 2){
//                            edge = vList2.get(0).cpy().sub(vList2.get(j));}
//                        else {
//                            edge = vList2.get(j + 1).cpy().sub(vList2.get(j));
//                        }
//                            u = edge.cpy();
//                            Vector3 u3 = new Vector3(u.x, u.y, 0);
//                            u3.nor();
//
//                            p = vList1.get(i).cpy().sub(vList2.get(j));
//                            Vector3 p3 = new Vector3(p.x, p.y, 0);
////                        System.out.println("p3x: " + p3.x + " y: "+ p3.y + " z: "+ p3.z);
//                            float mult = p3.dotProduct(u3);
//                            proj = u3.cpy().mul(mult);
//                            d = p3.cpy().crossProduct(u3);
//
//                            dist = d.len();
//                        vCollisionPoint = vList1.get(i).cpy();
//
//                        vCollisionPoint1.x = vCollisionPoint.x - ship1.position.x;
//                        vCollisionPoint1.y = vCollisionPoint.y - ship1.position.y;
//                        vCollisionPoint2.x = vCollisionPoint.x - ship2.position.x;
//                        vCollisionPoint2.y = vCollisionPoint.y - ship2.position.y;
//
//                            Vector3 vCollisionNormal = u3.cpy().crossProduct(p3);
//                            vCollisionNormal.crossProduct(u3);
////                        System.out.println("vCollisionNormal2x: " + vCollisionNormal.x + " y: "+ vCollisionNormal.y + " z: "+ vCollisionNormal.z);
//                        vCollisionNormal.nor();
//
//                            v1 = ship1.velocity.cpy().rotate(-ship1.angle);
//                        Vector3 v13D = v1.to3D().add(ship1.angVel.cpy().crossProduct(vCollisionPoint1));
//                            v2 = ship2.velocity.cpy().rotate(-ship2.angle);
//                        Vector3 v23D = v2.to3D().add(ship2.angVel.cpy().crossProduct(vCollisionPoint2));
//
//                        v13D.rotate(ship1.angle,0,0,1);
//                        v23D.rotate(ship2.angle,0,0,1);
//
//
//
//
//                            Vector2 vRelativeVelocity = v1.sub(v2);
////                            Vector3 vRelVel = new Vector3(vRelativeVelocity.x, vRelativeVelocity.y, 0);
//                        Vector3 vRelVel = v13D.sub(v23D);
//                            vrn = vRelVel.dotProduct(vCollisionNormal);
////                        System.out.println("vrn1: " + vrn);
////                        Vector2 ddd = ship1.position.cpy().sub(ship2.position);
////                        Vector2 vr = ship1.velocity.cpy().sub(ship2.velocity);
////                        vrn = vr.rotate(-ddd.angle()).x;
////                        System.out.println("vrn2: " + vrn);
//                            if ((proj.len() > 0.0f) &&
//                                    (proj.len() <= edge.len()) && (dist <= ctol) && (vrn < 0.0)) {
//                            haveNodeEdge = true;
//                            System.out.println("Vertex edge collision, vrn: " + vrn);
//                                System.out.println("Unit u3x: " + u3.x + " y: "+ u3.y + " z: "+ u3.z);
//                                System.out.println("p3x: " + p3.x + " y: "+ p3.y + " z: "+ p3.z);
//                                System.out.println("vCollisionNormal1x: " + vCollisionNormal.x + " y: "+ vCollisionNormal.y + " z: "+ vCollisionNormal.z);
//                                System.out.println("vRelVel: " + vRelVel.x + " y: "+ vRelVel.y +  " z: "+ vRelVel.z);
//                        }
//                        }
//                    }
//                }


//            if (!haveNodeNode && !haveNodeEdge) {
//                for (i = 0; (i < 4) && !interpenetrating; i++) {
//                    for (j = 0; (j < 4) && !interpenetrating; j++) {
//
//                        Vector2 vertex = vList1.get(i).cpy();
//                        System.out.println("vertex x: " + vertex.x + ", y: " + vertex.y);
//                        if (j == 3) {
//                            edge = vList2.get(0).cpy().sub(vList2.get(j));
//                            System.out.println("tr1" + tr1);
//                        } else {
//                            edge = vList2.get(j + 1).cpy().sub(vList2.get(j));
//                        }
//
//                        Vector2 evjplus1 = vList2.get(j+1).cpy();
//                        Vector2 evj = vList2.get(j).cpy();
//                        System.out.println("evj+1 x: " + evjplus1.x + ", y: " + evjplus1.y);
//                        System.out.println("evj x: " + evj.x + ", y: " + evj.y);
//
//                        System.out.println("j = " + j + ", i = " + i);
//                        p = (vList1.get(i).cpy().sub(vList2.get(j))).mul(-1);
//                        Vector3 edge3 = new Vector3(edge.x, edge.y, 0);
//                        System.out.println("edgex: " + edge3.x + " y: "+ edge3.y + " z: "+ edge3.z);
//                        Vector3 p3 = new Vector3(p.x, p.y, 0);
//                        System.out.println("p3x: " + p3.x + " y: "+ p3.y + " z: "+ p3.z);
//                        dot = p3.dotProduct(edge3);
//                        System.out.println("dot " + dot);
//                        ddd = ship1.position.cpy().sub(ship2.position);
//                        Vector2 vr = ship1.velocity.cpy().sub(ship2.velocity);
//                        vrn = vr.rotate(-dd.angle()).x;
//                        if (dot < 0) {
//                            interpenetrating = true;
//                            System.out.println("Interpenetrating");
//                        }
//                    }
//                }
//            }

            for (i = 0; (i < 4) && !interpenetrating; i++) {
                vCollisionPoint = new Vector2((float)polygon2x[i],(float)polygon2y[i]);
                vCollisionPoint1.x = vCollisionPoint.x - ship1.position.x;
                vCollisionPoint1.y = vCollisionPoint.y - ship1.position.y;
                vCollisionPoint2.x = vCollisionPoint.x - ship2.position.x;
                vCollisionPoint2.y = vCollisionPoint.y - ship2.position.y;

                Vector3 vCollisionNormal = dd.to3D();
//                        System.out.println("vCollisionNormal2x: " + vCollisionNormal.x + " y: "+ vCollisionNormal.y + " z: "+ vCollisionNormal.z);
                vCollisionNormal.nor();

                v1 = ship1.velocity.cpy().rotate(-ship1.angle);
                Vector3 v13D = v1.to3D().add(ship1.angVel.cpy().crossProduct(vCollisionPoint1));
                v2 = ship2.velocity.cpy().rotate(-ship2.angle);
                Vector3 v23D = v2.to3D().add(ship2.angVel.cpy().crossProduct(vCollisionPoint2));

                v13D.rotate(ship1.angle,0,0,1);
                v23D.rotate(ship2.angle,0,0,1);




                Vector2 vRelativeVelocity = v1.sub(v2);
//                            Vector3 vRelVel = new Vector3(vRelativeVelocity.x, vRelativeVelocity.y, 0);
                Vector3 vRelVel = v13D.sub(v23D);
                vrn = vRelVel.dotProduct(vCollisionNormal);


                if (isPointInsidePolygon(polygon1x,polygon1y,polygon2x[i],polygon2y[i])&& vrn < 0){
                    interpenetrating = true;
                    System.out.println("Interpenetrating");
                    float jj = -(1 + e) * vrn;
                    jj /= (1 / ship1.VESSEL_MASS) + (1 / ship2.VESSEL_MASS);
                    fI.x = jj;
                    fI.rotate(dd.angle());
                    ship1.impulse.add(fI);
                    ship2.impulse.add(fI.mul(-1f));
                    fI.set(0,0);
                }

            }
            for (i = 0; (i < 4) && !interpenetrating; i++) {

                vCollisionPoint = new Vector2((float)polygon1x[i],(float)polygon1y[i]);
                vCollisionPoint1.x = vCollisionPoint.x - ship1.position.x;
                vCollisionPoint1.y = vCollisionPoint.y - ship1.position.y;
                vCollisionPoint2.x = vCollisionPoint.x - ship2.position.x;
                vCollisionPoint2.y = vCollisionPoint.y - ship2.position.y;

                Vector3 vCollisionNormal = dd.to3D();
//                        System.out.println("vCollisionNormal2x: " + vCollisionNormal.x + " y: "+ vCollisionNormal.y + " z: "+ vCollisionNormal.z);
                vCollisionNormal.nor();

                v1 = ship1.velocity.cpy().rotate(-ship1.angle);
                Vector3 v13D = v1.to3D().add(ship1.angVel.cpy().crossProduct(vCollisionPoint1));
                v2 = ship2.velocity.cpy().rotate(-ship2.angle);
                Vector3 v23D = v2.to3D().add(ship2.angVel.cpy().crossProduct(vCollisionPoint2));

                v13D.rotate(ship1.angle,0,0,1);
                v23D.rotate(ship2.angle,0,0,1);




                Vector2 vRelativeVelocity = v1.sub(v2);
//                            Vector3 vRelVel = new Vector3(vRelativeVelocity.x, vRelativeVelocity.y, 0);
                Vector3 vRelVel = v13D.sub(v23D);
                vrn = vRelVel.dotProduct(vCollisionNormal);


                if (isPointInsidePolygon(polygon2x,polygon2y,polygon1x[i],polygon1x[i])&& vrn < 0){
                    interpenetrating = true;
                    System.out.println("Interpenetrating");
                    float jj = -(1 + e) * vrn;
                    jj /= (1 / ship1.VESSEL_MASS) + (1 / ship2.VESSEL_MASS);
                    fI.x = jj;
                    fI.rotate(dd.angle());
                    ship2.impulse.add(fI);
                    ship1.impulse.add(fI.mul(-1f));
                    fI.set(0,0);

                }
            }





        }

        if(interpenetrating){
             retval = -1;
        }
        else if(haveNodeNode || haveNodeEdge){
            retval = 1;
        } else {
            retval = 0;
        }

        if (retval == 1){
            float jj = -(1 + e) * vrn;
            jj /= (1 / ship1.VESSEL_MASS) + (1 / ship2.VESSEL_MASS);
            fI.x = jj;
            fI.rotate(dd.angle());
            ship1.impulse.add(fI);
            ship2.impulse.add(fI.mul(-1f));
            fI.set(0,0);
        }


    return retval;
}


    static boolean ArePointsEqual(Vector2 p1, Vector2 p2)
    {
// Points are equal if each component is within ctol of each other
        if( (Math.abs(p1.x - p2.x) <= ctol) &&
                (Math.abs(p1.y - p2.y) <= ctol) )
            return true;
        else
            return false;
    }

    public static boolean isPointInsidePolygon (double[] x, double[] y,
                                                double pointX, double pointY)
    {
        boolean  isInside = false;
        int nPoints = x.length;

        int j = 0;
        for (int i = 0; i < nPoints; i++) {
            j++;
            if (j == nPoints) j = 0;

            if (y[i] < pointY && y[j] >= pointY || y[j] < pointY && y[i] >= pointY) {
                if (x[i] + (pointY - y[i]) / (y[j] - y[i]) * (x[j] - x[i]) < pointX) {
                    isInside = !isInside;
                }
            }
        }

        return isInside;
    }
    public static boolean isLineIntersectingLine (int x0, int y0, int x1, int y1,
                                                  int x2, int y2, int x3, int y3)
    {
        int s1 = sameSide(x0, y0, x1, y1, x2, y2, x3, y3);
        int s2 = sameSide(x2, y2, x3, y3, x0, y0, x1, y1);

        return s1 <= 0 && s2 <= 0;
    }

    private static int sameSide (double x0, double y0, double x1, double y1,
                                 double px0, double py0, double px1, double py1){
        return sameSide ((double)x0, (double)y0, (double)x1, (double)y1,
                (double)px0, (double)py0, (double)px1, (double)py1);
    }


    /**
     * Find the intersections between a polygon and a straight line.
     *
     * NOTE: This method is only guaranteed to work if the polygon
     * is first preprocessed so that "unneccesary" vertices are removed
     * (i.e vertices on the straight line between its neighbours).
     *
     * @param  x   X coordinates of polygon.
     * @param  y   Y coordinates of polygon.
     * @param  x0  X first end point of line.
     * @param  x0  Y first end point of line.
     * @param  x0  X second end point of line.
     * @param  x0  Y second end point of line.
     * @return     Intersections [x,y,x,y...].
     */

}
