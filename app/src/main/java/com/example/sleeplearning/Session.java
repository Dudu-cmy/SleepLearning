package com.example.sleeplearning;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.SystemClock;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import java.io.IOException;

public class Session extends AppCompatActivity {
    private TextView endSessionButton, restartSessionButton;
    private Chronometer timer;
    public boolean running;
    public long pauseOffset;
    private String timerLimit;
    private int i = 0;

    String url = "https://storage.googleapis.com/sleep-learning-app/audio-files/"; // your URL here
    MediaPlayer mediaPlayer = new MediaPlayer();


    String madarinsAudios []={
      "mandarin-1.m4a",
            "mandarin-2.m4a",
            "mandarin-3.m4a"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        endSessionButton = findViewById(R.id.endSessionTxtView);
        restartSessionButton = findViewById(R.id.restartSessionTxtView);
        timer = findViewById(R.id.chronometer);
        timerLimit = "40:00";
        timer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        timer.start();
        running = true;
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View  promptView = layoutInflater.inflate(R.layout.requestfeedback_layout, null);

        final AlertDialog alertD = new AlertDialog.Builder(this).create();



        // check if the 40 min mark was reached

        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

                if(timerLimit.equals(timer.getText()))
                {
                    playmusic();
                }
                if ("05:00:00".equals(timer.getText()))
                {
                    stopmusic();
                }
            }
        });
       /* timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if("00:40".equals(timer.getText()))
                {
                    stopmusic();
                }
            }
        });*/
        timer.setBase(SystemClock.elapsedRealtime());
        //when the user clicks the end session button
        endSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer(v);
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                //alertD.setView(promptView);
                //alertD.setCancelable(false);
                //alertD.show();
                Intent intent = new Intent(Session.this, FirstQuestion.class);
                startActivity(intent);
                finish();
                //Intent intent = new Intent(Session.this, MainActivity.class);
                //startActivity(intent);
                //finish();

            }
        });

        restartSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer(v);
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    timerLimit = "20:00";
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }
        });
    }
    public void startTimer(View v)
    {

            timer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            timer.start();
            running = true;

    }
    public void stopTimer(View v)
    {
        timer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        timer.stop();
        running= false;

    }

    // make the app run in the background so that the timer can continue to run and
    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
    public void playmusic ()
    {
            i = 0;

            if (mediaPlayer == null)
                mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {

                mediaPlayer.setDataSource(url+madarinsAudios[i]);

                mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                  //  mediaPlayer.setLooping(true);
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(i<2)
                    {
                        i++;
                        try {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            if (mediaPlayer == null)
                                mediaPlayer = new MediaPlayer();

                            mediaPlayer.setDataSource(url+madarinsAudios[i]);
                            mediaPlayer.prepareAsync();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(i==2)
                    {
                        stopmusic();
                        playmusic();
                    }
                }
            });

        }

    public void stopmusic ()
    {
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

}

