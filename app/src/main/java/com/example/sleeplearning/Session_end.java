package com.example.sleeplearning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class Session_end extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_end);
    }

    @Override
    public void onBackPressed() {

        finishAffinity();
        Toast.makeText(getApplicationContext(),"App closed", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}
