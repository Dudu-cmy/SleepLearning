package com.example.sleeplearning;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class SecondQuestionSub extends AppCompatActivity {
Button submit;
EditText message;
TextView txt;
HashMap<String, String> responses = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_question_sub);
        submit = findViewById(R.id.submit);
        txt = findViewById(R.id.messageuserInput);
        message = findViewById(R.id.userResponse);
        responses = (HashMap<String, String>)getIntent().getSerializableExtra("response data");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userResponse = "null";
                userResponse = message.getText().toString();
                responses.put(txt.getText().toString(),userResponse);
                Intent intent = new Intent(SecondQuestionSub.this, SecondSubsub.class);
                intent.putExtra("response data", responses);
                startActivity(intent);
            }
        });

    }
}
