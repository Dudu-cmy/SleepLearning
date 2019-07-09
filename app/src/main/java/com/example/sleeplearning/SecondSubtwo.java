package com.example.sleeplearning;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class SecondSubtwo extends AppCompatActivity {
    TextView yesBtn,noBtn,message;
    HashMap<String, String> responses = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_subtwo);
        yesBtn = findViewById(R.id.yesButton);
        noBtn = findViewById(R.id.noButton);
        message = findViewById(R.id.message);
        responses = (HashMap<String, String>)getIntent().getSerializableExtra("response data");
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responses.put(message.getText().toString(),"yes");
                Intent intent = new Intent(SecondSubtwo.this, SecondSubsub.class);
                intent.putExtra("response data", responses);
                startActivity(intent);
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responses.put(message.getText().toString(),"no");
                Intent intent = new Intent(SecondSubtwo.this, ThirdQuestion.class);
                intent.putExtra("response data", responses);
                startActivity(intent);
            }
        });
    }
}
