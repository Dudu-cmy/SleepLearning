package com.example.sleeplearning;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class SecondQuestion extends AppCompatActivity {
TextView yesBtn,noBtn,message;
HashMap<String, String> responses = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_question);
        yesBtn = findViewById(R.id.yesButton);
        noBtn = findViewById(R.id.noButton);
        message = findViewById(R.id.message);
        responses = (HashMap<String, String>)getIntent().getSerializableExtra("response data");
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responses.put(message.getText().toString(),"yes");
                responses.put("Do you still think you heard the sounds or a language?", "NaN");
                Intent intent = new Intent(SecondQuestion.this, SecondQuestionSub.class);
                intent.putExtra("response data", responses);
                startActivity(intent);
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responses.put("How many times did you wake up during the night?", "NaN");
                responses.put(message.getText().toString(),"no");
                Intent intent = new Intent(SecondQuestion.this, SecondSubtwo.class);
                intent.putExtra("response data", responses);
                startActivity(intent);
            }
        });
    }
}
