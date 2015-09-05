package com.badlogic.androidgames.hoverboats;

import framework.classes.GLGame;
import framework.graphicsStuff.Animation;
import framework.graphicsStuff.Font;
import framework.graphicsStuff.Texture;
import framework.graphicsStuff.TextureRegion;
import framework.interfaces.Music;
import framework.interfaces.Sound;

/**
 * Created by New PC 2012 on 04/01/2015.
 */
public class Assets {

    public static Texture background;
    public static Texture background2;
    public static Texture items;
    public static TextureRegion backgroundRegion;
    public static TextureRegion backgroundRegion2;
    public static TextureRegion mainMenu;
    public static TextureRegion boat;
    public static TextureRegion newBoatHull;
    public static TextureRegion newSailLarge;
    public static TextureRegion newSailSmall;
    public static TextureRegion hull;
    public static TextureRegion wake;
    public static TextureRegion smokePuff;
    public static TextureRegion cannonBall;
    public static TextureRegion rock;
    public static TextureRegion pause;
    public static TextureRegion ready;
    public static TextureRegion resume;
    public static TextureRegion soundOn;
    public static TextureRegion soundOff;
    public static TextureRegion gameOver;
    public static TextureRegion wheel;
    public static Music music;
    public static Animation breakingShip;
    public static Animation buoy;
    public static Animation cannonFire;
    public static Animation windSock;
    public static Animation smokeEffect;


    public static Sound hitSound;
    public static Sound cannonSound;
    public static Sound click;
    public static Font font;

    public static void loadGame(GLGame game) {
        background = new Texture(game, "Background.png");
        background2 = new Texture(game, "water2.png");
        backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);
        backgroundRegion2 = new TextureRegion(background2, 0, 0, 512, 401);
        items = new Texture(game, "AtlasGrid.png");
        mainMenu = new TextureRegion(items, 0, 224, 300, 110);
        boat = new TextureRegion(items, 64, 128, 45, 80);
        newBoatHull = new TextureRegion(items, 320, 320, 44, 201);
        newSailLarge = new TextureRegion(items, 367, 320, 57, 46);
        newBoatHull = new TextureRegion(items, 320, 320, 44, 201);
        cannonBall = new TextureRegion(items, 361, 28, 1, 1);
        rock = new TextureRegion(items, 128, 128, 80, 64);
        hull = new TextureRegion(items, 256, 128, 40, 80);
        pause = new TextureRegion(items, 288, 0, 64, 64);
        wheel = new TextureRegion(items, 5 * 32 , 352, 38, 38);
        soundOff = new TextureRegion(items, 11 * 32, 0, 64, 64);
        soundOn = new TextureRegion(items, 13 * 32, 0, 64, 64);
        ready = new TextureRegion(items, 288, 64, 32 * 6, 32);
        resume = new TextureRegion(items, 288, 96, 32 * 6, 32 * 3);
        gameOver = new TextureRegion(items, 320, 32 * 6, 32 * 5, 96);
        smokePuff = new TextureRegion(items, 192, 448, 14, 14);

        breakingShip = new Animation(0.2f,
                new TextureRegion(items, 0, 11 * 32, 32, 84),
                new TextureRegion(items, 32, 11 * 32, 32, 84),
                new TextureRegion(items, 64, 11 * 32, 32, 84),
                new TextureRegion(items, 96, 11 * 32, 32, 84));

        buoy = new Animation(0.15f,
                new TextureRegion(items, 0, 14 * 32, 18, 32),
                new TextureRegion(items, 18, 14 * 32, 16, 32),
                new TextureRegion(items, 34, 14 * 32, 14, 32),
                new TextureRegion(items, 48, 14 * 32, 18, 32),
                new TextureRegion(items, 66, 14 * 32, 19, 32),
                new TextureRegion(items, 48, 14 * 32, 18, 32),
                new TextureRegion(items, 34, 14 * 32, 14, 32),
                new TextureRegion(items, 18, 14 * 32, 16, 32));

        cannonFire = new Animation(0.02f,
                new TextureRegion(items, 0, 480, 6, 24),
                new TextureRegion(items, 6, 480, 8, 24),
                new TextureRegion(items, 14, 480, 8, 24),
                new TextureRegion(items, 6, 480, 8, 24),
                new TextureRegion(items, 14, 480, 8, 24),
                new TextureRegion(items, 0, 480, 6, 24));
        windSock = new Animation(0.2f,
                new TextureRegion(items, 128, 416, 13, 39*2),
                new TextureRegion(items, 141, 416, 13, 39*2),
                new TextureRegion(items, 154, 416, 16, 39*2));

        smokeEffect = new Animation(0.25f,
                new TextureRegion(items, 288, 320, 11, 12),
                new TextureRegion(items, 299, 320, 11, 12),
                new TextureRegion(items, 311, 320, 12, 12),
                new TextureRegion(items, 323, 320, 13, 12),
                new TextureRegion(items, 323, 320, 13, 12),
                new TextureRegion(items, 323, 320, 13, 12),
                new TextureRegion(items, 323, 320, 13, 12)
                );

        font = new Font(items, 0, 0, 16, 16, 20);
        music = game.getAudio().newMusic("music.mp3");
        click = game.getAudio().newSound("click.ogg");
        hitSound = game.getAudio().newSound("thud.ogg");
        cannonSound = game.getAudio().newSound("CannonFire.ogg");
        music.setLooping(true);
        music.setVolume(0.5f);
        if (Settings.soundEnabled) {
            music.play();
            click = game.getAudio().newSound("click.ogg");
            hitSound = game.getAudio().newSound("thud.ogg");
            cannonSound = game.getAudio().newSound("CannonFire.ogg");

        }
    }

    public static void reload() {
        background.reload();
        items.reload();
        if (Settings.soundEnabled)
            music.play();
    }

    public static void playSound(Sound sound) {
        if (Settings.soundEnabled)
            sound.play(1);
    }


}
