package com.example.sleeplearning;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sleeplearning.adapter.ViewPagerAdapter;

import java.io.IOException;

public class InstructionsViewController extends AppCompatActivity {
    ViewPager viewPager;
    PagerAdapter adapter;
    LinearLayout linearLayout;
    TextView []mdots;
    Button start;
    String url = "https://storage.googleapis.com/sleep-learning-app/audio-files/ocean.mp3"; // your URL here
    MediaPlayer mediaPlayer = new MediaPlayer();
    int [] img;
    private  static  int currentpage = 0;
    private static int numpages = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_instructions_view_controller);
        img = new int[]{R.drawable.power, R.drawable.wifi, R.drawable.speaker};
        playmusic();
        linearLayout = findViewById(R.id.linearLayout);
        viewPager = findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(InstructionsViewController.this,img);
        viewPager.setAdapter((adapter));
        start = findViewById(R.id.start);
        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(viewListener);
        //Circle indicator
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopmusic();
                Intent intent = new Intent(InstructionsViewController.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

    }
    public void addDotsIndicator(int it)
    {
        mdots = new TextView[3];
        linearLayout.removeAllViews();
        for(int i = 0 ; i<3;i++)
        {
            mdots[i] = new TextView(this);
            mdots[i].setText(Html.fromHtml("&#8226;"));
            mdots[i].setTextSize(35);
            mdots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            linearLayout.addView(mdots[i]);

        }
        if(mdots.length>0)
        {
            mdots[it].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            currentpage = i;
            if (currentpage == mdots.length - 1){
                start.setEnabled(true);
                start.setVisibility(View.VISIBLE);
                start.setText("Continue");
            }
            else {
                start.setEnabled(false);
                start.setVisibility(View.INVISIBLE);
                start.setText("Continue");
            }


        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
    public void playmusic ()
    {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
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
