package com.example.raisein;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class signin_activity extends AppCompatActivity {


    private static final int RC_SIGN_IN = 1001;
    Button check;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener listener;

    List<AuthUI.IdpConfig> providers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.PhoneBuilder().build(),
                    new AuthUI.IdpConfig.FacebookBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            Toast.makeText(getApplicationContext(),"Signed in as "+ user.getEmail(),Toast.LENGTH_LONG).show();

        }
        else{
            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setTheme(R.style.LoginTheme)
                            .setLogo(R.drawable.sigin_logo)
                            .build(),RC_SIGN_IN
            );

        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {


            if (resultCode == RESULT_OK) {
                updateUI();
                // ...
            } else {

                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onStart() {

        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            updateUI();
        } else {
            Toast.makeText(getApplicationContext(),"Signin to continue",Toast.LENGTH_LONG).show();
        }

    }

    private void updateUI(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        startActivity(new Intent(signin_activity.this,welcome_screen.class));
        Toast.makeText(getApplicationContext(),"Signed in as "+ user.getEmail(),Toast.LENGTH_LONG).show();
    }

}

