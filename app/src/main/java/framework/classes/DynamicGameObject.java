package framework.classes;

/**
 * Created by New PC 2012 on 27/12/2014.
 */
public class DynamicGameObject extends GameObject {
    public final Vector2 velocity;
    public final Vector2 accel;
    public float mass, width, height;
    public DynamicGameObject(float x, float y, float width, float height) {
        super(x, y, width, height);
        velocity = new Vector2();
        accel = new Vector2();
        this.mass = 1f;
        this.width = width;
        this.height = height;
    }
    public DynamicGameObject(float x, float y, float width, float height, float mass) {
        super(x, y, width, height);
        velocity = new Vector2();
        accel = new Vector2();
        this.mass = mass;
    }
}
