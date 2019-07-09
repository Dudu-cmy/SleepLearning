package com.example.sleeplearning;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class FirstSubQuestion extends AppCompatActivity {
    TextView noBtn,yesBtn,message;
    HashMap<String, String> responses = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_sub_question);
        yesBtn = findViewById(R.id.yesButton);
        noBtn = findViewById(R.id.noButton);
        message = findViewById(R.id.message);
        responses = (HashMap<String, String>)getIntent().getSerializableExtra("response data");


        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responses.put(message.getText().toString(), "yes");
                Intent intent = new Intent(FirstSubQuestion.this, SecondQuestion.class);
                intent.putExtra("response data", responses);
                startActivity(intent);
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responses.put(message.getText().toString(), "no");
                Intent intent = new Intent(FirstSubQuestion.this, SecondQuestion.class);
                intent.putExtra("response data", responses);
                startActivity(intent);
            }
        });
    }
}
