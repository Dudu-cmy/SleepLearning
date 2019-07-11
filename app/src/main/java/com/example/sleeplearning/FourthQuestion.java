package com.example.sleeplearning;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class FourthQuestion extends AppCompatActivity {
    Button submit;
    EditText message;
    TextView txt;
    HashMap<String, Object> responses = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_question);

        submit = findViewById(R.id.submit);
        txt = findViewById(R.id.messageuserInput);
        message = findViewById(R.id.userResponse);
        responses = (HashMap<String, Object>)getIntent().getSerializableExtra("response data");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().toString().isEmpty()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(FourthQuestion.this);
                    alert.setTitle("Invalid response");
                    alert.setMessage("You must enter a response before continuing.");
                    alert.setPositiveButton("OK",null);
                    alert.setCancelable(false);
                    alert.show();
                }
                else {
                    String userResponse = "null";
                    userResponse = message.getText().toString();

                    responses.put("issuesWithVolume",userResponse);
                    Intent intent = new Intent(FourthQuestion.this, FifthQuestion.class);
                    intent.putExtra("response data", responses);
                    startActivity(intent);
                }
            }
        });
    }
}
