package com.badlogic.androidgames.hoverboats;

import java.text.DecimalFormat;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import framework.classes.Camera2D;
import framework.classes.FPSCounter;
import framework.classes.OverlapTester;
import framework.classes.Rectangle;
import framework.classes.Vector2;
import framework.graphicsStuff.Animation;
import framework.graphicsStuff.GLScreen;
import framework.graphicsStuff.SpriteBatcher;
import framework.graphicsStuff.TextureRegion;
import framework.interfaces.Game;
import framework.interfaces.Input;

/**
 * Created by New PC 2012 on 04/01/2015.
 */
public class GameScreen extends GLScreen {

    static final int GAME_READY = 0;
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER = 4;

    int state;
    Camera2D guiCam;
    Vector2 touchPoint;
    SpriteBatcher batcher;
    World world;
    World.WorldListener worldListener;
    WorldRenderer renderer;
    Rectangle pauseBounds;
    Rectangle resumeBounds;
    Rectangle quitBounds;
    int lastScore;
    String scoreString;
    float lastDist2;
    float dist2;
    Vector2 touch1 = new Vector2();
    Vector2 touch2 = new Vector2();
    Vector2 touchDist = new Vector2();
    float zoomFactor;
    public FPSCounter fpsCounter = new FPSCounter();
    float stateTime = 0;
    boolean gesture = false;

    public GameScreen(Game game) {
        super(game);
        state = GAME_READY;
        guiCam = new Camera2D(glGraphics, 320, 480);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 10000);
        worldListener = new World.WorldListener() {
            public void hit() {
                Assets.playSound(Assets.hitSound);
            }
            public void fire(){
                Assets.playSound(Assets.cannonSound);
            }
        };
        world = new World(worldListener);
        renderer = new WorldRenderer(glGraphics, batcher, world);
        pauseBounds = new Rectangle(320 - 64, 480 - 64, 64, 64);
        resumeBounds = new Rectangle(160 - 96, 240, 192, 36);
        quitBounds = new Rectangle(160 - 96, 240 - 36, 192, 36);
        lastScore = 0;
        scoreString = "Velocity: ";
    }

    @Override
    public void update(float deltaTime) {
//        fpsCounter.logFrame();
        if (deltaTime > 0.1f)
            deltaTime = 0.1f;
        stateTime += deltaTime;
        switch (state) {
            case GAME_READY:
                updateReady();
                break;
            case GAME_RUNNING:
                updateRunning(deltaTime);
                break;
            case GAME_PAUSED:
                updatePaused();
                break;
//            case GAME_LEVEL_END:
//                updateLevelEnd();
//                break;
            case GAME_OVER:
                updateGameOver();
                break;
        }
    }

    private void updateReady() {
        if (game.getInput().getTouchEvents().size() > 0) {
            state = GAME_RUNNING;
        }
    }

    private void updateRunning(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();

        if (game.getInput().isTouchDown(0) && game.getInput().isTouchDown(1)) {
            gesture = true;
            pinchZoom();

        }

            int len = touchEvents.size();
            for (int i = 0; i < len; i++) {
                Input.TouchEvent event = touchEvents.get(i);

                if (event.type != Input.TouchEvent.TOUCH_UP)
                    continue;
                if (gesture) {

                    gesture = false;
                    return;
                }

                    touchPoint.set(event.x, event.y);
                    guiCam.touchToWorld(touchPoint);

                    if (OverlapTester.pointInRectangle(pauseBounds, touchPoint)) {
                        Assets.playSound(Assets.click);
                        state = GAME_PAUSED;
                        return;
                    } else {
                        Vector2 tp = new Vector2(event.x, event.y);
                        renderer.cam.touchToWorld(tp);
//                calculates angle to the boat assuming the centre of screen is always the boat
                        touchPoint.sub(160f, 240f);
                        float degrees = touchPoint.angle() - world.hmsVictory.angle;
                        if (degrees < 0) {
                            degrees += 360;
                        }
                        System.out.println("R... World Coordiantes " + tp.x + " y: " + tp.y);
                        System.out.println("R... degrees " + (touchPoint.angle()));
                        System.out.println("R... angle relative to boat " + (degrees));
                        world.hmsVictory.fire((degrees));
//                    world.hits.add(new Hit(6,(int)tp.x,(int)tp.y));
                        return;
                    }
                }






        world.update(deltaTime, game.getInput().getAccelX());
        if (world.score != lastScore) {
            lastScore = world.score;
            scoreString = "" + lastScore;
        }
        String speed = new DecimalFormat("#.##").format(world.hmsVictory.velocity.len());
        scoreString = "SPEED: " + speed;
        if (world.state == World.WORLD_STATE_NEXT_LEVEL) {
            state = GAME_LEVEL_END;
        }
        if (world.state == World.WORLD_STATE_GAME_OVER) {
            state = GAME_OVER;
            if (lastScore >= Settings.highscores[4])
                scoreString = "new highscore: " + lastScore;
            else
                scoreString = "score: " + lastScore;
            Settings.addScore(lastScore);
            Settings.save(game.getFileIO());
        }
    }

    private void updatePaused() {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type != Input.TouchEvent.TOUCH_UP)
                continue;
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            if (OverlapTester.pointInRectangle(resumeBounds, touchPoint)) {
                Assets.playSound(Assets.click);
                state = GAME_RUNNING;
                return;
            }
            if (OverlapTester.pointInRectangle(quitBounds, touchPoint)) {
                Assets.playSound(Assets.click);
                game.setScreen(new MainMenuScreen(game));
                return;
            }
        }
    }

    private void updateGameOver() {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type != Input.TouchEvent.TOUCH_UP)
                continue;
            game.setScreen(new MainMenuScreen(game));
        }
    }

    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        renderer.render();
        guiCam.setViewportAndMatrices();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        batcher.beginBatch(Assets.items);
        switch (state) {
            case GAME_READY:
                presentReady();
                break;
            case GAME_RUNNING:
                presentRunning();
                break;
            case GAME_PAUSED:
                presentPaused();
                break;
//            case GAME_LEVEL_END:
//                presentLevelEnd();
//                break;
            case GAME_OVER:
                presentGameOver();
                break;
        }
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }

    private void presentReady() {
        batcher.drawSprite(160, 240, 192, 32, Assets.ready);
    }

    private void presentRunning() {
        batcher.drawSprite(320 - 32, 480 - 32, 64, 64, Assets.pause);
        Assets.font.drawText(batcher, scoreString, 16, 480 - 20);
//        TextureRegion keyFrame = Assets.breakingShip.getKeyFrame(world.hmsVictory.stateTime,
//        Animation.ANIMATION_NONLOOPING);
//
//        batcher.drawSprite(world.hmsVictory.position.x, world.hmsVictory.position.y, 10, 21,
//                (world.hmsVictory.angle - 90), keyFrame);
//        batcher.drawSprite(16, 16, 64, 64, Assets.windSoc)
        batcher.drawSprite(320/2, 25, 38, 38, (game.getInput().getAccelX())*9, Assets.wheel);
        TextureRegion keyframe = Assets.windSock.getKeyFrame(stateTime, Animation.ANIMATION_LOOPING);
        batcher.drawSprite(32, 32, 11, 64, (world.instantaneousWind.angle() - 90), keyframe);

    }

    private void presentPaused() {
        batcher.drawSprite(160, 240, 192, 96, Assets.resume);
        Assets.font.drawText(batcher, scoreString, 16, 480 - 20);
        batcher.drawSprite(320/2, 25, 38, 38, (game.getInput().getAccelX())*9, Assets.wheel);
    }

    private void presentGameOver() {
        batcher.drawSprite(160, 240, 160, 96, Assets.gameOver);
        float scoreWidth = Assets.font.glyphWidth * scoreString.length();
        Assets.font.drawText(batcher, scoreString, 160 - scoreWidth / 2, 480 - 20);
    }

    private void pinchZoom() {
        touch1.set(game.getInput().getTouchX(0), game.getInput().getTouchY(0));
        touch2.set(game.getInput().getTouchX(1), game.getInput().getTouchY(1));
        guiCam.touchToWorld(touch1);
        guiCam.touchToWorld(touch2);


//
//            float x2 = game.getInput().getTouchX(1);
//            float y2 = game.getInput().getTouchY(1);
        if (game.getInput().isTouchDown(0) && game.getInput().isTouchDown(1)) {
            if (lastDist2 == -1) {
                lastDist2 = touch1.dist(touch2);
            } else {
                zoomFactor = ((lastDist2 - touch1.dist(touch2)));
                if (zoomFactor > 100) {
                    zoomFactor = 100;
                }
//                System.out.println("zoomFactor: " + zoomFactor);
                renderer.cam.zoom += zoomFactor / 100;
                if (renderer.cam.zoom < 1){
                    renderer.cam.zoom = 1;
                }if (renderer.cam.zoom > 10){
                    renderer.cam.zoom = 10;
                }
                lastDist2 = touch1.dist(touch2);


            }
        }

    }

    @Override
    public void pause() {
        if (state == GAME_RUNNING)
            state = GAME_PAUSED;
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

}
