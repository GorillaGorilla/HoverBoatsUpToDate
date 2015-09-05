package framework.classes;

import android.view.View.OnTouchListener;

import java.util.List;

import framework.interfaces.Input.TouchEvent;

/**
 * Created by New PC 2012 on 21/12/2014.
 */
public interface TouchHandler extends OnTouchListener {


    public boolean isTouchDown(int pointer);
    public int getTouchX(int pointer);
    public int getTouchY(int pointer);
    public List<TouchEvent> getTouchEvents();

}
