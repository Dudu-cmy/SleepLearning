package com.example.sleeplearning;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sleeplearning.model.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class SecondSubtwo extends AppCompatActivity {
    TextView yesBtn,noBtn,message;
    HashMap<String, Object> responses = new HashMap<>();

    TextView backButton;
    String userId,subjectId;
    FirebaseFirestore db ;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_subtwo);
        yesBtn = findViewById(R.id.yesButton);
        noBtn = findViewById(R.id.noButton);
        message = findViewById(R.id.message);
        backButton = findViewById(R.id.backButton);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        subjectId = "";

        db = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(this);
     //   pd.setTitle("Thank you for your feedback ");
       // pd.setMessage("\n Please wait while we are uploading your response to the server ;)");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        responses = (HashMap<String, Object>)getIntent().getSerializableExtra("response data");
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
                            //userResponse = message.getText().toString();

                            responses.put("notAwakenedButHeardALanguage","yes");
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
                                    Intent intent = new Intent(SecondSubtwo.this, SecondSubsub.class);
                                    intent.putExtra("response data", responses);
                                    startActivity(intent);

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


            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
                            //userResponse = message.getText().toString();

                            responses.put("notAwakenedButHeardALanguage","no");
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
                                    Intent intent = new Intent(SecondSubtwo.this, ThirdQuestion.class);
                                    intent.putExtra("response data", responses);
                                    startActivity(intent);

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


            }
        });
    }
}
