package framework.graphicsStuff;

import framework.classes.GLGame;
import framework.classes.GLGraphics;
import framework.interfaces.Game;
import framework.interfaces.Screen;

/**
 * Created by New PC 2012 on 02/01/2015.
 */
public abstract class GLScreen extends Screen {
    protected final GLGraphics glGraphics;
    protected final GLGame glGame;
    public GLScreen(Game game) {
        super(game);
        glGame = (GLGame)game;
        glGraphics = glGame.getGLGraphics();
    }

}
