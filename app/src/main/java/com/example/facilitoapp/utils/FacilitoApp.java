package com.example.facilitoapp.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.media.SoundPool;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.facilitoapp.R;

public class FacilitoApp extends Application {

    private static SoundPool soundPool;
    private static int clickSound;
    private static SharedPreferences prefs;
    private static boolean loaded = false;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        prefs = getSharedPreferences("settings", MODE_PRIVATE);

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .build();

        // Listener: se activa cuando el sonido ya está cargado
        soundPool.setOnLoadCompleteListener((sp, sampleId, status) -> {
            if (status == 0) {
                loaded = true;
            }
        });

        // Cargar el archivo click.mp3 en res/raw
        clickSound = soundPool.load(this, R.raw.click, 1);
    }

    public static void playClick() {
        boolean sonidoOn = prefs.getBoolean("sonido", true);
        if (sonidoOn && soundPool != null && loaded) {
            soundPool.play(clickSound, 1f, 1f, 0, 0, 1f);
        }
    }

    public static void releaseSounds() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}
