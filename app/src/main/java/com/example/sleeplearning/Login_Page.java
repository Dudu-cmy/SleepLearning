package com.example.sleeplearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Page extends AppCompatActivity {

    private EditText email;
    private EditText id;
    private Button login;
    private String useremail, idnumber;
    private FirebaseAuth mAuth;
    private  ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__page);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        useremail = "";
        idnumber = "";
        id = findViewById(R.id.idnumber);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(Login_Page.this, InstructionsViewController.class));
            finish();
        }
        email = findViewById(R.id.email);
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                if (!id.getText().toString().isEmpty() && !email.getText().toString().isEmpty())
                {
                    useremail = email.getText().toString();
                    idnumber = id.getText().toString();
                    mAuth.signInWithEmailAndPassword(useremail, idnumber)
                            .addOnCompleteListener(Login_Page.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        pd.dismiss();
                                        // Sign in success, update UI with the signed-in user's information
                                      //  Log.d(TAG, "signInWithEmail:success");
                                        Intent intent= new Intent(Login_Page.this,InstructionsViewController.class);
                                        startActivity(intent);
                                        finish();
                                        //updateUI(user);
                                    } else {
                                        pd.dismiss();
                                        notifyUser("Please Enter correct credentials");
                                        // If sign in fails, display a message to the user.
                                       // Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                          //      Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }

                                    // ...
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                           // notifyUser("Check, your connection or if you entered your correct credentials");
                        }
                    });

                }
                else
                {
                    pd.dismiss();
                    notifyUser("Please Enter correct credentials");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        if (mAuth.getCurrentUser() != null ) {
            startActivity(new Intent(Login_Page.this, InstructionsViewController.class));
            finish();
        }
        super.onResume();
    }

    private void notifyUser(String e)
    {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage(e);
        dialog.setTitle("Error");
        dialog.setPositiveButton("Back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        pd.dismiss();
                    }
                });


        AlertDialog alertDialog=dialog.create();
        alertDialog.show();

    }
}
