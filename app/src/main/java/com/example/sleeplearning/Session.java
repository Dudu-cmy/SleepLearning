package com.example.sleeplearning;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.os.SystemClock;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class Session extends AppCompatActivity {
    private TextView endSessionButton, restartSessionButton;
    private Chronometer timer;
    public boolean running;
    public long pauseOffset;
    private String timerLimit;
    private MediaPlayer silen;
    private int i = 0;
    private int counter = 0;
    private int x = 0;
    PowerManager.WakeLock pwakelock;
    private boolean paused = false;
    HashMap<String, Object> responses = new HashMap<>();
    String url = "https://storage.googleapis.com/sleep-learning-app/audio-files/"; // your URL here
    MediaPlayer mediaPlayer = new MediaPlayer();

    String ocean = "https://storage.googleapis.com/sleep-learning-app/audio-files/ocean.mp3";
    String silence = "https://storage.googleapis.com/sleep-learning-app/audio-files/20-minutes-of-silence.m4a";
    String fullsilence = "https://storage.googleapis.com/sleep-learning-app/audio-files/40-minutes-of-silence.m4a";
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        silen = new MediaPlayer();
        silen.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        silen.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            silen.setDataSource(fullsilence);
            silen.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        silen.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                silen.start();
            }
        });
        silen.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (silen.isPlaying())
                    silen.stop();
                silen.release();
                silen= null;
                playmusic();
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        //wakelock acquire
        final WifiManager.WifiLock wifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        final PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire();
        wifiLock.acquire();


        acquireLock();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date();
        responses = (HashMap<String, Object>)getIntent().getSerializableExtra("response data");
        responses.put("timeWhenAsleep",new Date());
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
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                chronometer.setText(hh+":"+mm+":"+ss);


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
                wifiLock.release();
                wakeLock.release();

                releaseLock();
                if (mediaPlayer!=null) {
                    if (mediaPlayer.isPlaying())
                        mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                if (silen!=null) {
                    if (silen.isPlaying())
                        silen.stop();
                    silen.release();
                    silen = null;
                }
                //alertD.setView(promptView);
                //alertD.setCancelable(false);
                //alertD.show();
                DateFormat dateFormats = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                dateFormats.setTimeZone(TimeZone.getTimeZone("UTC"));
                responses.put("timeWhenAwake",new Date());
                responses.put("numberOfRestarts",counter);
                Intent intent = new Intent(Session.this, FirstQuestion.class);
                intent.putExtra("response data", responses);
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


                if (mediaPlayer.isPlaying()) {
                    counter++;
                    startTimer(v);
                    x = mediaPlayer.getCurrentPosition();
                    mediaPlayer.pause();
                    paused = true;
                    try {
                        if (silen!=null){
                        if(silen.isPlaying())
                            silen.stop();
                        silen.reset();}
                        if (silen == null)
                            silen = new MediaPlayer();
                        silen.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

                        silen.setDataSource(silence);
                        silen.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    silen.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            silen.start();
                        }
                    });
                    silen.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            playmusic();
                        }
                    });
                    //mediaPlayer.release();
                    //mediaPlayer = null;
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
        if(paused)
        {
            paused =false;
            mediaPlayer.seekTo(x);
            mediaPlayer.start();

        }
        else {
            i = 0;

            if (mediaPlayer == null)
                mediaPlayer = new MediaPlayer();
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {

                mediaPlayer.setDataSource(ocean);

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
                    if (i < 15) {

                        try {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            if (mediaPlayer == null)
                                mediaPlayer = new MediaPlayer();
                            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
                            mediaPlayer.setDataSource(url + madarinsAudios[i]);
                            i++;
                            mediaPlayer.prepareAsync();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }
        }

    public void stopmusic ()
    {
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        if (silen.isPlaying())
            silen.stop();
        silen.stop();
        silen.release();
        silen = null;
    }
    public void acquireLock()
    {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        pwakelock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"myapp:mywakelocktag");
        pwakelock.acquire();
    }
    public  void releaseLock()
    {
        pwakelock.release();
    }
    /*int silencePosition=0,soundPosition=0;
    @Override
    protected void onPause() {
        super.onPause();
        if (silen!=null) {
            if (silen.isPlaying()) {
                silencePosition = silen.getCurrentPosition();
                silen.pause();
            }
        }
        if (mediaPlayer!=null) {
            if (mediaPlayer.isPlaying()) {
                soundPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (silen!=null)
        {
        if (!silen.isPlaying() && silen.getCurrentPosition() > 1)
        {
            silen.seekTo(silen.getCurrentPosition());
            silen.start();
        }
        }
        if (mediaPlayer!=null) {
            if (!mediaPlayer.isPlaying() && mediaPlayer.getCurrentPosition() > 1) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
                mediaPlayer.start();
            }
        }
    }*/
}

