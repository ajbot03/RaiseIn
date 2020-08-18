package com.example.raisein;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup_activitiy extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mEmail,mPassword;
    Button mRegisterBtn;

    FirebaseAuth fAuth;
    ProgressBar progressBar;

    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activitiy);

        mEmail      = findViewById(R.id.email);
        mPassword   = findViewById(R.id.password);
        mRegisterBtn = findViewById(R.id.signup);

        fAuth = FirebaseAuth.getInstance();


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();




                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");

                                updateUI();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(signup_activitiy.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }


                        }
                    }
                });
            }
        });
    }

    private void updateUI(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        startActivity(new Intent(signup_activitiy.this,welcome_screen.class));
        Toast.makeText(getApplicationContext(),"Signed in as "+ user.getEmail(),Toast.LENGTH_LONG).show();
    }
}