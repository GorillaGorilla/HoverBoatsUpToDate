package framework.classes;

import android.media.SoundPool;

import framework.interfaces.Sound;

/**
 * Created by New PC 2012 on 21/12/2014.
 */
public class AndroidSound implements Sound {

    int soundId;
    SoundPool soundPool;
    public AndroidSound(SoundPool soundPool, int soundId) {
        this.soundId = soundId;
        this.soundPool = soundPool;
    }
    public void play(float volume) {
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }
    public void dispose() {
        soundPool.unload(soundId);
    }

}
