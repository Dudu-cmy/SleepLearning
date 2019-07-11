package com.example.sleeplearning;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private TextView startButton;
    HashMap<String, Object> responses = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        responses.put("timeWhenAsleep","NaN");
        responses.put("timeWhenAwake","NaN");
        responses.put("numberOfRestarts","NaN");
        responses.put("heardInitialWarningSounds","NaN");
        responses.put("pressedRestart","NaN");
        responses.put("awakenedBySoundsOrLanguage","NaN");
        responses.put("numberOfTimesWokenUp","NaN");
        responses.put("languageHeardAfterWaking","NaN");
        responses.put("notAwakenedButHeardALanguage","NaN");
        responses.put("languageHeardWhileAsleep","NaN");
        responses.put("headphonesStayedIn","NaN");
        responses.put("issuesWithVolume","NaN");
        responses.put("issuesWithApp","NaN");



        startButton = findViewById(R.id.startTxt);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Session.class);
                intent.putExtra("response data", responses);
                startActivity(intent);
                finish();

            }
        });
    }
}
