package com.badlogic.androidgames.hoverboats;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import framework.classes.Camera2D;
import framework.classes.GLGraphics;
import framework.classes.Rectangle;
import framework.classes.Vector2;
import framework.graphicsStuff.Animation;
import framework.graphicsStuff.SpriteBatcher;
import framework.graphicsStuff.TextureRegion;

/**
 * Created by New PC 2012 on 04/01/2015.
 */
public class WorldRenderer {
    static final float FRUSTUM_WIDTH = 80;
    static final float FRUSTUM_HEIGHT = 142;  // where do these come from???
    GLGraphics glGraphics;
    World world;
    Camera2D cam;
    SpriteBatcher batcher;

    public WorldRenderer(GLGraphics glGraphics, SpriteBatcher batcher, World world) {
        this.glGraphics = glGraphics;
        this.world = world;
        this.cam = new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        this.batcher = batcher;

    }

    public void render() {

        cam.position.x = world.hmsVictory.position.x;
        cam.position.y = world.hmsVictory.position.y;
        cam.setViewportAndMatrices();
        renderBackground();
        renderObjects();
    }

    public void renderBackground() {
        batcher.beginBatch(Assets.background2);
//        goes the length and bredth of the world drawing the background.
        for (int i = 0; i < (world.WORLD_WIDTH / FRUSTUM_WIDTH); i++) {
            for (int j = 0; j < (world.WORLD_HEIGHT / 63); j++) {
                batcher.drawSprite(i * FRUSTUM_WIDTH + 40, j * 63 + 60,FRUSTUM_WIDTH
                        , 63,
                        Assets.backgroundRegion2);
            }
        }
        batcher.endBatch();
    }

    public void renderObjects() {
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        batcher.beginBatch(Assets.items);
        renderRocks();
        renderBuoys();
        renderCannonBalls();
        renderHits();
        renderShips();
        renderPlayer();
        renderSmoke();
        renderToasts();
//        Assets.font.drawText(batcher, "hello",
//                2000,
//                3000);
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }

    private void renderToasts() {

        for (ToastMessage toastMessage : world.toasts){

            if (toastMessage.state == toastMessage.DISPLAY) {
//                System.out.println("rendering toasts");

                Assets.font.drawToast(batcher, toastMessage.message,
                        toastMessage.position.x - toastMessage.width / 2,
                        toastMessage.position.y - toastMessage.height,(1f/7f));
            }

        }

    }

    private void renderHits() {
        for (Hit hit : world.hits){
            for (Partical partical : hit.particles) {
                TextureRegion keyFrame = Assets.hull;
                batcher.drawSprite(partical.position.x, partical.position.y,
                        partical.width, partical.height, partical.angle, keyFrame);
            }
        }
    }

    private void renderSmoke() {
        for (Smoke smoke : world.smokes){
            TextureRegion keyFrame = Assets.smokePuff;
            float fac = 2.5f-(1/(0.25f*(float)Math.exp(smoke.stateTime)));
            batcher.drawSprite(smoke.position.x, smoke.position.y, 13*fac, 13*fac, keyFrame);
        }
    }

    private void renderShips() {

        List<Ship> ships = world.ships;
        for (Ship ship : ships) {
            if (ship.state == ship.VESSEL_STATE_SUNK) {
            } else if (ship.state == ship.VESSEL_STATE_SINKING) {
                TextureRegion keyFrame = Assets.breakingShip.getKeyFrame(ship.stateTime,
                        Animation.ANIMATION_NONLOOPING);

                batcher.drawSprite(ship.position.x, ship.position.y, 10*ship.spriteFactor, 21*ship.spriteFactor,
                        (ship.angle - 90), keyFrame);
            }else {
                TextureRegion keyFrame = Assets.newBoatHull;
                batcher.drawSprite(ship.position.x, ship.position.y,
                        5.5f*ship.spriteFactor, 27.5f*ship.spriteFactor, (ship.angle - 90), keyFrame);
                keyFrame = Assets.newSailLarge;
                if (!ship.masts.isEmpty()) {
                    for (Mast mast : ship.masts) {
                        batcher.drawSprite(mast.position.x, mast.position.y,
                                mast.bredth*mast.sizeScalingFactor, mast.depth*mast.sizeScalingFactor,
                                (ship.angle - 90 + mast.rotation),keyFrame);
//                        if (ship.bb.inDanger){
//                            batcher.drawSprite(ship.position.x+10, ship.position.y+10,
//                                    3, 4,
//                                    (ship.angle),keyFrame);
//
//                    }

                    }
                }
            }
//            Rectangle b = ship.bounds;
//            TextureRegion keyFrame = Assets.cannonBall;
//            for (Vector2 point : ship.boundingShape.pointListWorld){
//                batcher.drawSprite(point.x, point.y, 1, 1,
//                        keyFrame);
//            }
        }
    }

    private void renderCannonBalls() {
        TextureRegion keyFrame = Assets.cannonBall;
        List<CannonBall> balls = world.balls;
        for (CannonBall ball : balls) {
            if (ball.z>0) {
                batcher.drawSprite(ball.position.x, ball.position.y,
                        0.25f, 0.25f, keyFrame);
            }
        }
    }

    private void renderBuoys() {
        for (Buoy buoy : world.buoys){
            TextureRegion keyFrame = Assets.buoy.getKeyFrame(buoy.stateTime,
                    Animation.ANIMATION_LOOPING);
            batcher.drawSprite(buoy.position.x, buoy.position.y, 4, 10, keyFrame);
        }
    }

    private void renderPlayer() {
//        sprite is verticle so -90 to reflect this
        for (GunDeck deck : world.hmsVictory.gunDecks) {
            TextureRegion keyFrame = Assets.cannonFire.getKeyFrame(deck.stateTime,
                    Animation.ANIMATION_NONLOOPING);

            if (deck.state == deck.GUNS_STATE_FIRING) {
                float[] gpos = deck.positions;
                for (int i = 0; i < gpos.length; i++) {

                    if (deck.timer[i] > 0 && deck.timer[i] < 0.4f) {
                        Vector2 pos = new Vector2(deck.offset, gpos[i]);
                        pos.rotate((world.hmsVictory.angle + deck.orientation));
                        pos.add(world.hmsVictory.position);
                        batcher.drawSprite(pos.x, pos.y, 2, 6,
                                ((world.hmsVictory.angle - 90) + deck.orientation), keyFrame);
                    }
                }
            }
        }
        if (world.hmsVictory.state == world.hmsVictory.VESSEL_STATE_SINKING) {
            TextureRegion keyFrame = Assets.breakingShip.getKeyFrame(world.hmsVictory.stateTime,
                    Animation.ANIMATION_NONLOOPING);

            batcher.drawSprite(world.hmsVictory.position.x, world.hmsVictory.position.y, 12, 25,
                    (world.hmsVictory.angle - 90), keyFrame);
        } else if (world.hmsVictory.state == world.hmsVictory.VESSEL_STATE_SUNK) {

        } else {
            TextureRegion keyFrame = Assets.newBoatHull;
            batcher.drawSprite(world.hmsVictory.position.x, world.hmsVictory.position.y,
                    5.5f, 27.5f, (world.hmsVictory.angle - 90), keyFrame);
//            batcher.drawSprite(world.hmsVictory.bb.fordwardPos.x, world.hmsVictory.bb.fordwardPos.y,
//                    5.5f*0.5f, 27.5f*0.5f, (world.hmsVictory.angle - 90), keyFrame);

            keyFrame = Assets.newSailLarge;

            if (!world.hmsVictory.masts.isEmpty()) {
                for (Mast mast : world.hmsVictory.masts) {
                    if(world.hmsVictory.getSailState() == "BALANCED"){
                        batcher.drawSprite(mast.position.x, mast.position.y,
                                mast.bredth*mast.sizeScalingFactor*0.8f, mast.depth*mast.sizeScalingFactor*0.8f,
                                (world.hmsVictory.angle - 90 + mast.rotation),
                                keyFrame);
                    } else if (world.hmsVictory.getSailState() == "FULL CANVAS"){
                        batcher.drawSprite(mast.position.x, mast.position.y,
                                mast.bredth*mast.sizeScalingFactor, mast.depth*mast.sizeScalingFactor,
                                (world.hmsVictory.angle - 90 + mast.rotation),
                                keyFrame);
                    }else {
                        batcher.drawSprite(mast.position.x, mast.position.y,
                        mast.bredth*mast.sizeScalingFactor, mast.depth*mast.sizeScalingFactor*0.4f,
                                (world.hmsVictory.angle - 90 + mast.rotation),
                                keyFrame);
                    }

                }
            }
        }
//        Rectangle b = world.hmsVictory.bounds;
//        TextureRegion keyFrame = Assets.cannonBall;
//        for (Vector2 point : world.hmsVictory.boundingShape.pointListWorld){
//            batcher.drawSprite(point.x, point.y, 1, 1,
//                    keyFrame);
//        }
    }

    private void renderRocks() {
        TextureRegion keyFrame = Assets.rock;
        for (Rock rock : world.rocks) {
            batcher.drawSprite(rock.position.x, rock.position.y,
                    25, 20, keyFrame);
        }

    }

}
