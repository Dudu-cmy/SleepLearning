package com.example.sleeplearning;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import org.w3c.dom.Text;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private TextView startButton;
    HashMap<String, Object> responses = new HashMap<>();
    String language;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore database;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        responses.put("timeWhenAsleep","NaN");
        responses.put("timeWhenAwake","NaN");
        responses.put("numberOfRestarts","NaN");
        responses.put("heardInitialWarningSounds","NaN");
        responses.put("pressedRestart","NaN");
        responses.put("awakenedBySoundsOrLanguage","NaN");
        responses.put("numberOfTimesWokenUp","NaN");
        responses.put("languageHeardAfterWaking","NaN");
        responses.put("notAwakenedButHeardALanguage","NaN");
        responses.put("languageHeardWhileAsleep","NaN");
        responses.put("headphonesStayedIn","NaN");
        responses.put("issuesWithVolume","NaN");
        responses.put("issuesWithApp","NaN");

        //Get User language
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        language = "";
        database = FirebaseFirestore.getInstance();

        //progress dialog
        pd = new ProgressDialog(this);
        pd.setTitle("Loading.....");
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(true);

        startButton = findViewById(R.id.startTxt);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                if (user!=null)
                {
                    DocumentReference docRef =  database.collection("Subjects").document(user.getEmail());
                    Source source = Source.SERVER;
                    docRef.get(source).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            UserData userData = documentSnapshot.toObject(UserData.class);
                            Log.v("language",userData.getLanguage());
                            language = userData.getLanguage();
                            Intent intent = new Intent(MainActivity.this, Session.class);
                            intent.putExtra("response data", responses);
                            intent.putExtra("user language", language);
                            startActivity(intent);
                            finish();
                            pd.dismiss();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(),"Check your Internet Connection",Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });
    }
}
