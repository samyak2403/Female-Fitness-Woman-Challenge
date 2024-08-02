package com.utillity.objects;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.utillity.R;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.AUDIO_SERVICE;

public class MySoundUtil {

    public static int SOUND_DING = 1;
    public static int SOUND_WHISTLE = 0;
    @SuppressLint("StaticFieldLeak")
    private static MySoundUtil utils;
    private AudioManager audioManager;
    private Map<Integer, Integer> soundMap;
    private SoundPool soundPool;
    private SoundPool ttsSoundPool;
    private Context context;

    public MySoundUtil(Context context) {
        init(context);
        this.context = context;
    }

    public static synchronized MySoundUtil getInstance(Context context) {
        MySoundUtil mySoundUtil;
        synchronized (MySoundUtil.class) {
            if (utils == null) {
                utils = new MySoundUtil(context);
            }
            mySoundUtil = utils;
        }
        return mySoundUtil;
    }

    public void init(Context context) {
        try {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
            soundMap = new HashMap();
            soundMap.put(SOUND_WHISTLE, soundPool.load(context, R.raw.whistle, 1));
            soundMap.put(SOUND_DING, soundPool.load(context, R.raw.ding, 1));
            audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
            ttsSoundPool = new SoundPool(1, 3, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playSound(int sound_index) {
        try {
            if (!LocalDB.INSTANCE.getSoundMute(context)) {
                if (soundPool != null && soundMap != null && audioManager != null) {
                    soundPool.play((soundMap.get(sound_index))
                            , 1.0F
                            , 1.0F, 1, 0, 1.0f);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
