package com.example.sleeplearning;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class SecondQuestion extends AppCompatActivity {
TextView yesBtn,noBtn,message;
HashMap<String, Object> responses = new HashMap<>();
TextView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_question);
        yesBtn = findViewById(R.id.yesButton);
        noBtn = findViewById(R.id.noButton);
        message = findViewById(R.id.message);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        responses = (HashMap<String, Object>)getIntent().getSerializableExtra("response data");
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responses.put("awakenedBySoundsOrLanguage","yes");
                responses.put("notAwakenedButHeardALanguage", "NaN");
                Intent intent = new Intent(SecondQuestion.this, SecondQuestionSub.class);
                intent.putExtra("response data", responses);
                startActivity(intent);
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responses.put("numberOfTimesWokenUp", "NaN");
                responses.put("awakenedBySoundsOrLanguage","no");
                Intent intent = new Intent(SecondQuestion.this, SecondSubtwo.class);
                intent.putExtra("response data", responses);
                startActivity(intent);
            }
        });
    }
}
