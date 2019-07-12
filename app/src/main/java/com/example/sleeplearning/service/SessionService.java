package com.example.sleeplearning.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class SessionService extends Service {

    String url = "https://storage.googleapis.com/sleep-learning-app/audio-files/"; // your URL here
    MediaPlayer mediaPlayer = new MediaPlayer();
    String ocean = "https://storage.googleapis.com/sleep-learning-app/audio-files/ocean.mp3";

    String madarinsAudios []={
            "mandarin-1.m4a",
            "mandarin-2.m4a",
            "mandarin-3.m4a",

            "mandarin-1.m4a",
            "mandarin-2.m4a",
            "mandarin-3.m4a",
            "mandarin-1.m4a",
            "mandarin-2.m4a",
            "mandarin-3.m4a",
            "mandarin-1.m4a",
            "mandarin-2.m4a",
            "mandarin-3.m4a",
            "mandarin-1.m4a",
            "mandarin-2.m4a",
            "mandarin-3.m4a"
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
