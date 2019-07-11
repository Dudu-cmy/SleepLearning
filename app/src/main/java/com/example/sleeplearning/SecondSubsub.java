package com.example.sleeplearning;

import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class SecondSubsub extends AppCompatActivity {
    Button submit;
    EditText message;
    TextView txt;
    HashMap<String, Object> responses = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondsubsub);
        submit = findViewById(R.id.submit);
        txt = findViewById(R.id.messageuserInput);
        message = findViewById(R.id.userResponse);
        responses = (HashMap<String, Object>)getIntent().getSerializableExtra("response data");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().toString().isEmpty()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(SecondSubsub.this);
                    alert.setTitle("Invalid response");
                    alert.setMessage("You must enter a response before continuing.");
                    alert.setPositiveButton("OK",null);
                    alert.setCancelable(false);
                    alert.show();
                }
                else {
                String userResponse = "null";
                userResponse = message.getText().toString();
                if (responses.get("notAwakenedButHeardALanguage").equals("yes")) {
                    responses.put("languageHeardWhileAsleep", userResponse);
                }
                else
                    responses.put("languageHeardAfterWaking", userResponse);
                Intent intent = new Intent(SecondSubsub.this, ThirdQuestion.class);
                intent.putExtra("response data", responses);
                startActivity(intent);
            }
            }
        });
    }
}
