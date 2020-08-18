package com.example.raisein;

import android.app.UiModeManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;


import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class signin_activity extends AppCompatActivity {
    private EditText mEmailText;
    private EditText mPasswordText;
    private Button login;
    private Button signup;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton google;

    CallbackManager mCallbackManager;

    private static final int RC_SIGN_IN = 1001;
    private static final int FB_SIGN_IN = 2001;
    Button check;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_in);

            mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setPermissions("public_profile","email", "user_birthday", "user_friends");
            mEmailText = findViewById(R.id.email);
            mPasswordText = findViewById(R.id.password);
            login = findViewById(R.id.login);
            signup = findViewById(R.id.signup);
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(signin_activity.this,signup_activitiy.class));
                }
            });
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String email = mEmailText.getText().toString().trim();
                    String password = mPasswordText.getText().toString().trim();
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {

                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("signin", "Signin:success");

                                    updateUI();

                            }
                            else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(signin_activity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }
            });
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
            google=findViewById(R.id.sign_in_button);
            google.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            });
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d("FB", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("FB", "facebook:onCancel");
                // ...
            }



            @Override
            public void onError(FacebookException error) {
                Log.d("FB", "facebook:onError", error);
                // ...
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            updateUI();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("gmail", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("gmail", "Google sign in failed", e);
                // ...
            }
        }
        else{
            mCallbackManager.onActivityResult(requestCode, resultCode, data);


        }


    }
    private void firebaseAuthWithGoogle(String idToken) {


        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Signin successful",Toast.LENGTH_LONG).show();
                    updateUI();

                } else {
                    Toast.makeText(getApplicationContext(), "Signin failed", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FB", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FB", "signInWithCredential:success");
                            updateUI();
                        }
                        else if (!task.isSuccessful()&& task.getException() instanceof FirebaseAuthUserCollisionException){
                            FirebaseAuthUserCollisionException e=(FirebaseAuthUserCollisionException)task.getException();

                            Task<SignInMethodQueryResult> auth = mAuth.fetchSignInMethodsForEmail(e.getEmail());
                            // If sign in fails, display a message to the user.
//                            Toast.makeText(getApplicationContext(), , Toast.LENGTH_LONG).show();
                            e.zza(credential);
                            Log.w("FB", "signInWithCredential:failure", task.getException());


                        }

                        // ...
                    }
                });
    }

    private void updateUI(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        startActivity(new Intent(signin_activity.this,welcome_screen.class));
        Toast.makeText(getApplicationContext(),"Signed in as "+ user.getEmail(),Toast.LENGTH_LONG).show();
    }

}

