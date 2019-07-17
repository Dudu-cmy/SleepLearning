package com.example.sleeplearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FifthQuestion extends AppCompatActivity {
    Button submit;
    EditText message;
    TextView txt;
    HashMap<String, Object> responses = new HashMap<>();
    ProgressDialog pd;
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth_question);

        submit = findViewById(R.id.submit);
        txt = findViewById(R.id.messageuserInput);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pd = new ProgressDialog(this);
        pd.setTitle("Thank you for your feedback ");
        pd.setMessage("\n Please wait while we are uploading your response to the server ;)");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        message = findViewById(R.id.userResponse);
        responses = (HashMap<String, Object>)getIntent().getSerializableExtra("response data");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().toString().isEmpty()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(FifthQuestion.this);
                    alert.setTitle("Invalid response");
                    alert.setMessage("You must enter a response before continuing.");
                    alert.setPositiveButton("OK",null);
                    alert.setCancelable(false);
                    alert.show();
                }
                else {
                    pd.show();
                    String userResponse = "null";
                    userResponse = message.getText().toString();

                    responses.put("issuesWithApp",userResponse);
                    HashMap<String, Object> responses_to_save = new HashMap<>();
                    for (String key : responses.keySet()) {
                        if (!responses.get(key).equals("NaN"))
                        {
                            responses_to_save.put(key,responses.get(key));
                        }
                    }
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Diary").document().set(responses_to_save).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                           pd.dismiss();
                           Intent intent = new Intent(FifthQuestion.this, MainActivity.class);
                           startActivity(intent);
                           finishAffinity();
                            System.out.print("yeeeeeeeep");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.print("NOOOOOOOOOOO");
                        }
                    });
                    //Intent intent = new Intent(FifthQuestion.this, FifthQuestion.class);
                    //intent.putExtra("response data", responses);
                    //startActivity(intent);
                }
            }
        });
    }
}
