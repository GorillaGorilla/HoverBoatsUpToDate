package framework.classes;

/**
 * Created by New PC 2012 on 27/12/2014.
 */
public class Rectangle {
    public final Vector2 lowerLeft;
    public final Vector2 lowerRight;
    public final Vector2 topRight;
    public final Vector2 topLeft;
    public final Vector2 diff;
    public float width, height, angle, first;
    public Rectangle(float x, float y, float width, float height) {
        this.lowerLeft = new Vector2(x,y);
        this.width = width;
        this.height = height;
        angle  = 0f;
        lowerRight = new Vector2(0,0);
        topRight = new Vector2(0,0);
        topLeft = new Vector2(0,0);
        lowerRight.set(lowerLeft.x + width, lowerLeft.y);
        topRight.set(lowerLeft.x + width, lowerLeft.y + height);
        topLeft.set(lowerLeft.x, lowerLeft.y + height);
        diff = new Vector2(0,0);
        this.first = 0;
    }




    public void rotate(float angle, Vector2 point){
//        rotates the rectangle
//         set the other vectors
//        System.out.println("angle: " + angle);
        this.angle =angle;

            lowerRight.set(lowerLeft.x + width, lowerLeft.y);
            topRight.set(lowerLeft.x + width, lowerLeft.y + height);
            topLeft.set(lowerLeft.x, lowerLeft.y + height);

//            System.out.println("lower left.x: " + lowerLeft.x + "y: " + lowerLeft.y);
//            System.out.println("lower right.x: " + lowerRight.x + "y: " + lowerRight.y);
//            System.out.println("top right.x: " + topRight.x + "y: " + topRight.y);
//            System.out.println("top left.x: " + topLeft.x + "y: " + topLeft.y);

        diff.x = lowerLeft.x - point.x;
        diff.y = lowerLeft.y - point.y;
        diff.rotate(angle);
        diff.add(point);

        lowerLeft.set(diff.cpy());
//        System.out.println("lower left.x: " + lowerLeft.x + "y: " + lowerLeft.y);

        diff.x = lowerRight.x - point.x;
        diff.y = lowerRight.y - point.y;
        diff.rotate(angle);
        diff.add(point);
        lowerRight.set(diff.cpy());
//        System.out.println("lower right.x: " + lowerRight.x + "y: " + lowerRight.y);

        diff.x = topRight.x - point.x;
        diff.y = topRight.y - point.y;
        diff.rotate(angle);
        diff.add(point);
        topRight.set(diff.cpy());
//        System.out.println("top right.x: " + topRight.x + "y: " + topRight.y);

        diff.x = topLeft.x - point.x;
        diff.y = topLeft.y - point.y;
        diff.rotate(angle);
        diff.add(point);
        topLeft.set(diff.cpy());
//        System.out.println("top left.x: " + topLeft.x + "y: " + topLeft.y);
    }
    public Rectangle cpy(){
        Rectangle r = new Rectangle(0, 0, 0, 0);
        r.width = width;
        r.height = height;
        r.angle = angle;
        r.lowerLeft.set(lowerLeft);
        r.lowerRight.set(lowerRight);
        r.topLeft.set(topLeft);
        r.topRight.set(topRight);

        return r;
    }

}
