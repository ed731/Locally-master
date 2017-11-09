package com.example.danny.locally;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity
{
    private static final String TAG = "LogInActivity";

    // Firebase Authentication object
    private FirebaseAuth mAuth;
    // Firebase Authentication State Listener object
    private FirebaseAuth.AuthStateListener mAuthListener;
    // Login Button
    private Button logInButton;
    // Email Edit Text
    private EditText emailEditText;
    // Password Edit Text
    private EditText passwordEditText;

    /***** Function is called when app creates. AKA Constructor *****/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Set the view to the layout
        setContentView(R.layout.activity_log_in);

        // Get the reference of the Log In button
        logInButton = (Button) findViewById(R.id.logInButton);
        // Get the reference of the Email Edit Text
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        // Get the reference of the Password Edit Text
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);


        // Initialize the Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        // Intialize the Firebase Authentication listener to track whenever user is signs in or out
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    /***** Function is called when app starts *****/
    @Override
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    /***** Function is called when app stop *****/
    @Override
    public void onStop()
    {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



    /***** Function is used when log in button is clicked *****/
    public void logInClicked(View view)
    {
        // Get the reference of the Email Edit Text
        String email = emailEditText.getText().toString().trim();
        // Get the reference of the Password Edit Text
        String password = passwordEditText.getText().toString().trim();

        // email text is empty
        if(email.equals(""))
        {
            // animate a shake
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            emailEditText.startAnimation(shake);
        }
        // password text is empty
        if(password.equals(""))
        {
            // animate a shake
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            passwordEditText.startAnimation(shake);
        }

        if( (!emailEditText.equals("")) && (!password.equals("")) ) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signedInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(LogInActivity.this, "Logged In",
                                        Toast.LENGTH_SHORT).show();
                                //update UI

                            } else {
                                //if sign in fails, display this message
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LogInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI
                                    }
                        }

                    });

            //Intent intent = new Intent(this, LogInActivity.class);
            //startActivity(intent);
        }
    }

    /***** Function is used if New Account Text is clicked *****/
    public void newAccountClicked(View view)
    {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        LogInActivity.this.finish();
    }
}