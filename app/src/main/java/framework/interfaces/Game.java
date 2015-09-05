package framework.interfaces;

/**
 * Created by New PC 2012 on 20/12/2014.
 */
public interface Game {

    public Input getInput();

    public FileIO getFileIO();

    public Graphics getGraphics();

    public Audio getAudio();

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();

    public Screen getStartScreen();

}
