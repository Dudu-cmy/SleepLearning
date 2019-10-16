package com.example.sleeplearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sleeplearning.model.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class FifthQuestion extends AppCompatActivity {
    Button submit;
    EditText message;
    TextView txt;
    String subjectId;
    HashMap<String, Object> responses = new HashMap<>();
    ProgressDialog pd;
    ImageView done;
    TextView backButton;
    String userId;
    FirebaseFirestore db ;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth_question);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        subjectId = "";

        db = FirebaseFirestore.getInstance();

        done = findViewById(R.id.done);
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
        pd.setCancelable(false);
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

                    if(user!=null)
                    {
                        pd.show();
                        userId= user.getEmail();
                        DocumentReference docRef = db.collection("Subjects").document(userId);
                        Source source = Source.SERVER;
                        docRef.get(source).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                UserData data = documentSnapshot.toObject(UserData.class);
                                subjectId =data.getID();
                                Date date = new Date();
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                formatter = new SimpleDateFormat("MMM dd, yyyy");
                                String strDate = formatter.format(date);
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

                                db.collection(subjectId).document(strDate).set(responses_to_save).addOnSuccessListener(new OnSuccessListener<Void>() {

                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        pd.dismiss();
                                        Intent intent = new Intent(FifthQuestion.this, Session_end.class);
                                        startActivity(intent);
                                        finishAffinity();

                                    }


                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    //Intent intent = new Intent(FifthQuestion.this, FifthQuestion.class);
                    //intent.putExtra("response data", responses);
                    //startActivity(intent);
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
