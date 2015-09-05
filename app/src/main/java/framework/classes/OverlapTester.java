package framework.classes;

/**
 * Created by New PC 2012 on 27/12/2014.
 */
public class OverlapTester {
    public static boolean overlapCircles(Circle c1, Circle c2) {
        float distance = c1.center.distSquared(c2.center);
        float radiusSum = c1.radius + c2.radius;
        return distance <= radiusSum * radiusSum;
    }

    public static boolean overlapRectangles(Rectangle r1, Rectangle r2) {

        Vector2 lowerRight = new Vector2(r1.lowerLeft.x + r1.width, r1.lowerLeft.y);
        Vector2 topRight = new Vector2(r1.lowerLeft.x + r1.width, r1.height);
        Vector2 topLeft = new Vector2();
        if (r1.angle != 0) {
//            if(r1.lowerLeft.x < r2.lowerLeft.x + r2.width &&
//                    r1.lowerRight.x > r2.lowerLeft.x &&
//                    r1.lowerLeft.y < r2.lowerLeft.y + r2.height &&
//                    r1.topLeft.y > r2.lowerLeft.y)
            if (pointInRectangle(r2, r1.lowerLeft) ||
                    pointInRectangle(r2, r1.lowerRight) ||
                    pointInRectangle(r2, r1.topLeft) ||
                    pointInRectangle(r2, r1.topRight))
                return true;
            else
                return false;
        }

        if (r1.lowerLeft.x < r2.lowerLeft.x + r2.width &&
                r1.lowerLeft.x + r1.width > r2.lowerLeft.x &&
                r1.lowerLeft.y < r2.lowerLeft.y + r2.height &&
                r1.lowerLeft.y + r1.height > r2.lowerLeft.y)
            return true;
        else
            return false;
    }

    public static boolean overlapCircleRectangle(Circle c, Rectangle r) {
        float closestX = c.center.x;
        float closestY = c.center.y;

        if (r.angle != 0){
//            System.out.println("Testing point in circle");
//            System.out.println("R.lowerleftx: " + r.lowerLeft.x + " R.lowerlefty: " + r.lowerLeft.y);
//            System.out.println("R.lowerrightx: " + r.lowerRight.x + " lry" + r.lowerRight.y);
//            System.out.println("R.topright: " + r.topRight.x + " try" + r.topRight.y);
//            System.out.println("R.topL: " + r.topLeft.x+ " tly" + r.topLeft.y);
//            System.out.println("c.center" + c.center.x + " centre.y" + c.center.y);
//            System.out.println("distance squared: " + c.center.distSquared(r.lowerLeft));
            if (pointInCircle(c, r.lowerLeft) ||
                pointInCircle(c, r.lowerRight) ||
                pointInCircle(c, r.topLeft) ||
                pointInCircle(c, r.topRight))
            return true;
        else
//                System.out.println("Not inside");
            return false;


        }else {

        if (c.center.x < r.lowerLeft.x) {
            closestX = r.lowerLeft.x;
        } else if (c.center.x > r.lowerLeft.x + r.width) {
            closestX = r.lowerLeft.x + r.width;
        }
        if (c.center.y < r.lowerLeft.y) {
            closestY = r.lowerLeft.y;
        } else if (c.center.y > r.lowerLeft.y + r.height) {
            closestY = r.lowerLeft.y + r.height;
        }
        }
        return c.center.distSquared(closestX, closestY) < c.radius * c.radius;
    }

    public static boolean pointInCircle(Circle c, Vector2 p) {
        return c.center.distSquared(p) < c.radius * c.radius;
    }

    public static boolean pointInCircle(Circle c, float x, float y) {
        return c.center.distSquared(x, y) < c.radius * c.radius;
    }

    public static boolean pointInRectangle(Rectangle r, Vector2 p) {
        return r.lowerLeft.x <= p.x && r.lowerLeft.x + r.width >= p.x &&
                r.lowerLeft.y <= p.y && r.lowerLeft.y + r.height >= p.y;
    }

    public static boolean pointInRectangle(Rectangle r, float x, float y) {
        return r.lowerLeft.x <= x && r.lowerLeft.x + r.width >= x &&
                r.lowerLeft.y <= y && r.lowerLeft.y + r.height >= y;
    }

    public static Vector2 overlapDistance(Circle c, Rectangle r){

        Vector2 dist = new Vector2();
        if (pointInCircle(c, r.lowerLeft)){
            dist = c.center.cpy().sub(r.lowerLeft);
            System.out.println("lower left");
            return dist;
        } else if(pointInCircle(c, r.lowerRight)){
            dist = c.center.cpy().sub(r.lowerRight);
            return dist;
        } else if(pointInCircle(c, r.topLeft)){
            dist = c.center.cpy().sub(r.topLeft);
            return dist;
        } else if(pointInCircle(c, r.topRight)){
            dist = c.center.cpy().sub(r.topRight);
            return dist;
        }
        return null;
    }

    private boolean isColliding(Vector2 p, Rectangle bounds){
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
//        if(angle > -90 && angle < 90){
//            // BottomLeft - BottomRight
//            if(p.x * slope + intercept < p.x){
//                countCol += 1;
//            }
//
//            // TopLeft - TopRight
//            if(p.x * slope3 + intercept3 > p.y){
//                countCol += 1;
//            }
//        }
//        else{
//            // BottomLeft - BottomRight
//            if(p.x * slope + intercept > p.y){
//                countCol += 1;
//            }
//
//            // TopLeft - TopRight
//            if(p.x * slope3 + intercept3 < p.y){
//                countCol += 1;
//            }
//        }
//
//        // BottomLeft - TopLeft
//        if(angle < 0){
//            if(p.x * slope2 + intercept2 > p.y){
//                countCol += 1;
//            }
//            if(p.x * slope4 + intercept4 < p.y){
//                countCol += 1;
//            }
//        }
//        else{
//            if(p.x * slope2 + intercept2 < p.y){
//                countCol += 1;
//            }
//            if(p.x * slope4 + intercept4 > p.y){
//                countCol += 1;
//            }
//        }

        if(countCol >= 4){
            return true;
        }
        return false;
    }
}
