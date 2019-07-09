package com.example.sleeplearning;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Feedback extends AppCompatActivity {
    private TextView message,yesbtn,nobtn;
    private LinearLayout userInput, normalInterface;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        message = findViewById(R.id.message);
        yesbtn = findViewById(R.id.yesButton);
        nobtn = findViewById(R.id.noButton);
        userInput = findViewById(R.id.linearLayout3);
        normalInterface = findViewById(R.id.linearLayout2);
        submit = findViewById(R.id.submit);

        message.setText("Did you hear the initial warning sounds that language was coming while you were asleep?");


        //   message.setText("What Language do you think you heard?");



    }
}
