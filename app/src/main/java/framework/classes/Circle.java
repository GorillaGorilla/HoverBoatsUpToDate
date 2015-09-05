package framework.classes;

/**
 * Created by New PC 2012 on 27/12/2014.
 */
public class Circle {
    public final Vector2 center = new Vector2();
    public float radius;
    public Circle(float x, float y, float radius) {
        this.center.set(x,y);
        this.radius = radius;
    }
}
