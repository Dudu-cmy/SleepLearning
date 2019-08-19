package com.example.sleeplearning;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class SecondQuestionSub extends AppCompatActivity {
Button submit;
EditText message;
TextView txt;
HashMap<String, Object> responses = new HashMap<>();
ImageView backButton,done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_question_sub);
        done = findViewById(R.id.done);
        submit = findViewById(R.id.submit);
        txt = findViewById(R.id.messageuserInput);
        message = findViewById(R.id.userResponse);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        responses = (HashMap<String, Object>)getIntent().getSerializableExtra("response data");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().toString().isEmpty()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(SecondQuestionSub.this);
                    alert.setTitle("Invalid response");
                    alert.setMessage("You must enter a response before continuing.");
                    alert.setPositiveButton("OK",null);
                    alert.setCancelable(false);
                    alert.show();
                } else {
                    String userResponse = "null";
                    userResponse = message.getText().toString();
                    responses.put("numberOfTimesWokenUp", Integer.parseInt(userResponse));
                    Intent intent = new Intent(SecondQuestionSub.this, SecondSubsub.class);
                    intent.putExtra("response data", responses);
                    startActivity(intent);
                }
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(message.getWindowToken(), 0);
            }
        });

    }
}
