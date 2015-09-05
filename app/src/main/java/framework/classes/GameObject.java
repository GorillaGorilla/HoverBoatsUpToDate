package framework.classes;

/**
 * Created by New PC 2012 on 27/12/2014.
 */
public class GameObject {
    public final Vector2 position;
    public final Rectangle bounds;
    public GameObject(float x, float y, float width, float height) {
        this.position = new Vector2(x,y);
        this.bounds = new Rectangle(x-width/2, y-height/2, width, height);
    }
}
