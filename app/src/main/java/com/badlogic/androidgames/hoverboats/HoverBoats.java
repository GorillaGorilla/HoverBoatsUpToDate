package com.badlogic.androidgames.hoverboats;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import framework.classes.GLGame;
import framework.interfaces.Screen;


public class HoverBoats extends GLGame {
    boolean firstTimeCreate = true;
    public Screen getStartScreen() {
        return new MainMenuScreen(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        if(firstTimeCreate) {
            Settings.load(getFileIO());
            Assets.loadGame(this);
            firstTimeCreate = false;
        } else {
            Assets.reload();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if(Settings.soundEnabled)
            Assets.music.pause();
    }
}
