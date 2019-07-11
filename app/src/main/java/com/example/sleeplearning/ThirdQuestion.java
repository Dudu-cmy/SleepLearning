package com.example.sleeplearning;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class ThirdQuestion extends AppCompatActivity {
    TextView yesBtn,noBtn,message;
    HashMap<String, Object> responses = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_question);
        yesBtn = findViewById(R.id.yesButton);
        noBtn = findViewById(R.id.noButton);
        message = findViewById(R.id.message);
        responses = (HashMap<String, Object>)getIntent().getSerializableExtra("response data");
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responses.put("headphonesStayedIn","yes");
                Intent intent = new Intent(ThirdQuestion.this, FourthQuestion.class);
                intent.putExtra("response data", responses);
                startActivity(intent);
                //finishAffinity();

            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responses.put("headphonesStayedIn","no");
                Intent intent = new Intent(ThirdQuestion.this, FourthQuestion.class);
                intent.putExtra("response data", responses);
                startActivity(intent);
                //finishAffinity();
            }
        });

    }
}
