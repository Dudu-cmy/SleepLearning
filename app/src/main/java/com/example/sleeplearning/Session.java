package com.example.sleeplearning;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sleeplearning.model.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class Session extends AppCompatActivity {
    private TextView endSessionButton, restartSessionButton;
    private Chronometer timer;
    public boolean running;
    public long pauseOffset;
    private String timerLimit;
    private int counter_offset = 1;
    private MediaPlayer silen;
    private  MediaPlayer oceanMediaPlayer = new MediaPlayer();
    private int i = 0;
    private int counter = 0;
    private int x = 0;
    private String offset;
    private boolean minimize = true;
    private ArrayList<Date> timestamps;
    PowerManager.WakeLock pwakelock;
    private boolean paused = false;
    HashMap<String, Object> responses = new HashMap<>();
    String url = "https://storage.googleapis.com/sleep-learning-app/audio-files/"; // your URL here
    MediaPlayer mediaPlayer = new MediaPlayer();
    String userId,subjectId;
    FirebaseFirestore db ;

    ProgressDialog pd;

    String ocean = "https://storage.googleapis.com/sleep-learning-app/audio-files/ocean.mp3";
    String silence = "https://storage.googleapis.com/sleep-learning-app/audio-files/20-minutes-of-silence.m4a";
    String fullsilence = "https://storage.googleapis.com/sleep-learning-app/audio-files/40-minutes-of-silence.m4a";
    String five_minutes_silence = "https://storage.googleapis.com/sleep-learning-app/audio-files/5-minutes-of-silence.m4a";
    String madarinsAudios []={
            "mandarin-1.m4a",
            "mandarin-2.m4a"
     };
    String arabicAudio [] ={
            "arabic-1.m4a",
            "arabic-2.m4a",
            "arabic-3.m4a"
    };
    String selectedAudioStream [];
    String language;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore database;
    WifiManager.WifiLock wifiLock;
    PowerManager powerManager;
     PowerManager.WakeLock wakeLock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        language = "";
        offset = "";
        timestamps =  new ArrayList<>();
        //wakelock acquire
         wifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
         powerManager = (PowerManager) getSystemService(POWER_SERVICE);
         wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire();
        wifiLock.acquire();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        subjectId = "";

        db = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(this);
       // pd.setTitle("Thank you for your feedback ");
        //pd.setMessage("\n Please wait while we are uploading your response to the server ;)");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        acquireLock();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date();
        Intent intent = getIntent();
        responses = (HashMap<String, Object>)intent.getSerializableExtra("response data");
        language = (String) intent.getSerializableExtra("user language");
        offset = (String) intent.getSerializableExtra("offset");
        Log.v("lan",language);
       // responses.put("timeWhenAsleep",new Date());
        // save the time when the user is going to sleep

        if(user!=null)
        {
            pd.show();
            userId= user.getEmail();
            DocumentReference docRef = db.collection("Subjects").document(userId);
            Source source = Source.SERVER;
            docRef.get(source).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    UserData data = documentSnapshot.toObject(UserData.class);
                    subjectId =data.getID();
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    formatter = new SimpleDateFormat("MMM dd, yyyy");
                    String strDate = formatter.format(date);
                    String userResponse = "null";
                    //userResponse = message.getText().toString();

                    responses.put("timeWhenAsleep",new Date());
                    HashMap<String, Object> responses_to_save = new HashMap<>();
                    for (String key : responses.keySet()) {
                        if (!responses.get(key).equals("NaN"))
                        {
                            responses_to_save.put(key,responses.get(key));
                        }
                    }

                    db.collection(subjectId).document(strDate).set(responses_to_save).addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void aVoid) {

                            pd.dismiss();


                        }


                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            pd.dismiss();
                            Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_LONG).show();
                }
            });
        }

        //
        endSessionButton = findViewById(R.id.endSessionTxtView);
        restartSessionButton = findViewById(R.id.restartSessionTxtView);
        /*timer = findViewById(R.id.chronometer);
        timerLimit = "40:00";
        timer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        timer.start();
        running = true;*/


        restartSessionButton.setEnabled(true);

        i=0;
        selectedAudioStream = new String[2];

        if (language.equals("Arabic") || language.equals("arabic"))
        {
            selectedAudioStream = arabicAudio;
        }
        else if (language.equals("Mandarin") || language.equals("mandarin"))
        {
            selectedAudioStream = madarinsAudios;
        }
        Log.v("songs",selectedAudioStream[0]);
      //  Log.v("songs",selectedAudioStream[1]);
        oceanMediaPlayer = new MediaPlayer();
        oceanMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        oceanMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        silen = new MediaPlayer();
        silen.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        silen.setAudioStreamType(AudioManager.STREAM_MUSIC);
        counter_offset = 1;
        try {
            silen.setDataSource(five_minutes_silence);
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
                counter_offset++;


                if(counter_offset <= Integer.parseInt(offset)/5) {
                    if (silen.isPlaying())
                        silen.stop();
                    silen.reset();

                    Log.v("offset_current",Integer.toString(counter_offset));
                    if (silen == null)
                        silen = new MediaPlayer();
                    try {
                        silen.setDataSource(five_minutes_silence);
                        silen.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(counter_offset > Integer.parseInt(offset)) {
                    if (silen.isPlaying())
                        silen.stop();
                    silen.release();
                    silen = null;
                    Log.v("silence", "silence audio finished");
                    playOceanAudio();
                }
            }
        });


        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View  promptView = layoutInflater.inflate(R.layout.requestfeedback_layout, null);

        final AlertDialog alertD = new AlertDialog.Builder(this).create();

        // Check the language of the user and assign corresponding audio stream


        // check if the 40 min mark was reached

       /* timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
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
        });*/
       /* timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if("00:40".equals(timer.getText()))
                {
                    stopmusic();
                }
            }
        });*/
       // timer.setBase(SystemClock.elapsedRealtime());
        //when the user clicks the end session button
        endSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder =  new AlertDialog.Builder(Session.this);
                builder.setMessage("Are you sure you want to end the session?");
                builder.setTitle("Confirm");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        wifiLock.release();
                        wakeLock.release();
                        minimize = false;
                        releaseLock();
                        if (mediaPlayer!=null) {
                            if (mediaPlayer.isPlaying())
                                mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                        if (oceanMediaPlayer!=null) {
                            if (oceanMediaPlayer.isPlaying())
                                oceanMediaPlayer.stop();
                            oceanMediaPlayer.release();
                            oceanMediaPlayer = null;
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
                        //responses.put("timeWhenAwake",new Date());
                        // save when the user wake up
                        if(user!=null)
                        {
                            pd.show();
                            userId= user.getEmail();
                            DocumentReference docRef = db.collection("Subjects").document(userId);
                            Source source = Source.SERVER;
                            docRef.get(source).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    UserData data = documentSnapshot.toObject(UserData.class);
                                    subjectId =data.getID();
                                    Date date = new Date();
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    formatter = new SimpleDateFormat("MMM dd, yyyy");
                                    String strDate = formatter.format(date);
                                    String userResponse = "null";
                                    //userResponse = message.getText().toString();

                                    responses.put("timeWhenAwake",new Date());
                                    HashMap<String, Object> responses_to_save = new HashMap<>();
                                    for (String key : responses.keySet()) {
                                        if (!responses.get(key).equals("NaN"))
                                        {
                                            responses_to_save.put(key,responses.get(key));
                                        }
                                    }

                                    db.collection(subjectId).document(strDate).set(responses_to_save).addOnSuccessListener(new OnSuccessListener<Void>() {

                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            pd.dismiss();
                                            Intent intent = new Intent(Session.this, FirstQuestion.class);
                                            intent.putExtra("response data", responses);
                                            startActivity(intent);
                                            finish();


                                        }


                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            pd.dismiss();
                                            Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_LONG).show();
                                }
                            });
                        }


                        //
                        /*responses.put("numberOfRestarts",counter);
                        if(timestamps.size()!=0) {
                            responses.put("timesPressedRestart", timestamps);
                        }*/
                        /*Intent intent = new Intent(Session.this, FirstQuestion.class);
                        intent.putExtra("response data", responses);
                        startActivity(intent);
                        finish();*/
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                //Intent intent = new Intent(Session.this, MainActivity.class);
                //startActivity(intent);
                //finish();
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        restartSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("songs", "restart pressed");
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying() || oceanMediaPlayer.isPlaying()) {
                        Log.v("songs", "restart accepted");
                        Toast.makeText(getApplicationContext(),"Session restarted",Toast.LENGTH_LONG).show();
                        counter++;
                        timestamps.add(new Date());
                        // save restart timestamps and the number of restarts

                        if(user!=null)
                        {
                            pd.show();
                            userId= user.getEmail();
                            DocumentReference docRef = db.collection("Subjects").document(userId);
                            Source source = Source.SERVER;
                            docRef.get(source).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    UserData data = documentSnapshot.toObject(UserData.class);
                                    subjectId =data.getID();
                                    Date date = new Date();
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    formatter = new SimpleDateFormat("MMM dd, yyyy");
                                    String strDate = formatter.format(date);
                                    String userResponse = "null";
                                    //userResponse = message.getText().toString();

                                    responses.put("numberOfRestarts",counter);
                                    if(timestamps.size()!=0) {
                                        responses.put("timesPressedRestart", timestamps);
                                    }
                                    HashMap<String, Object> responses_to_save = new HashMap<>();
                                    for (String key : responses.keySet()) {
                                        if (!responses.get(key).equals("NaN"))
                                        {
                                            responses_to_save.put(key,responses.get(key));
                                        }
                                    }

                                    db.collection(subjectId).document(strDate).set(responses_to_save).addOnSuccessListener(new OnSuccessListener<Void>() {

                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            pd.dismiss();


                                        }


                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            pd.dismiss();
                                            Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_LONG).show();
                                }
                            });
                        }




                        //
                        //startTimer(v);
                        if (mediaPlayer.isPlaying()) {
                            x = mediaPlayer.getCurrentPosition();
                            mediaPlayer.pause();
                            paused = true;
                            try {
                                if (silen != null) {
                                    if (silen.isPlaying())
                                        silen.stop();
                                    silen.reset();
                                }
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

                                    playOceanAudio();
                                }
                            });
                        } else if (oceanMediaPlayer.isPlaying()) {
                            i = 0;
                            x = 0;
                            paused = false;
                            oceanMediaPlayer.stop();
                            oceanMediaPlayer.release();
                            oceanMediaPlayer = null;
                            //playOceanAudio();
                            try {
                                if (silen != null) {
                                    if (silen.isPlaying())
                                        silen.stop();
                                    silen.reset();
                                }
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
                                    playOceanAudio();
                                }
                            });
                        }
                        //mediaPlayer.release();
                        //mediaPlayer = null;
                    } else {
                        Toast.makeText(getApplicationContext(),"The session can not be restarted because no audio sounds are playing currently",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
  /*  public void startTimer(View v)
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
*/
    // make the app run in the background so that the timer can continue to run and
    @Override
    public void onBackPressed() {
        //this.moveTaskToBack(true);
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

                mediaPlayer.setDataSource(url + selectedAudioStream[i]);
                Log.v("log","preparing");
                mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    Log.v("log","starting");
                    //  mediaPlayer.setLooping(true);
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (i < (selectedAudioStream.length-1)) {

                        try {
                            Log.v("audio","manadarin 2");
                            i++;
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            if (mediaPlayer == null)
                                mediaPlayer = new MediaPlayer();
                            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
                            mediaPlayer.setDataSource(url + selectedAudioStream[i]);

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
        if (oceanMediaPlayer.isPlaying())
            oceanMediaPlayer.stop();
        oceanMediaPlayer.stop();
        oceanMediaPlayer.release();
        oceanMediaPlayer = null;
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

    public void playOceanAudio()
    {
        restartSessionButton.setEnabled(true);
        Log.v("audio","ocean");
        if (oceanMediaPlayer == null)
            oceanMediaPlayer = new MediaPlayer();
        oceanMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        oceanMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            oceanMediaPlayer.setDataSource(ocean);
            oceanMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
            oceanMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.v("log","preparing ocean start");
                    oceanMediaPlayer.start();
                }
            });
        oceanMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (oceanMediaPlayer!=null)
                {
                if (oceanMediaPlayer.isPlaying())
                {
                    oceanMediaPlayer.stop();
                }
                oceanMediaPlayer.reset();
                }
                Log.v("log","next music");
                playmusic();
            }
        });

    }

    @Override
    protected void onUserLeaveHint() {
        if (minimize)
            Toast.makeText(getApplicationContext(),"App going in the background, for an uninterrupted session it is recommended to run the application and keep it in the foreground",Toast.LENGTH_LONG).show();
        super.onUserLeaveHint();
        wakeLock.acquire();
        wifiLock.acquire();


    }

    @Override
    protected void onPause() {
        /*wakeLock.acquire();
        wifiLock.acquire();
        oceanMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);*/
        Log.v("pause","pausing");
       /* final WifiManager.WifiLock wifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        final PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire();
        wifiLock.acquire();
        silen.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
       // onResume();*/
       super.onPause();
    }

    @Override
    protected void onResume() {
        //super.onResume();
        Log.v("paused","pausingd");
        /*oceanMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        wakeLock.acquire();
        wifiLock.acquire();*/
        super.onResume();
    }

}

