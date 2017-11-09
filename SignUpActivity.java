
package com.example.danny.locally;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity
{

    private static final String TAG = "SignUpActivity";
    // Ok Button
    private Button okButton;
    // Name Edit Text
    private EditText nameInputText;
    // Email Edit Text
    private EditText emailInputText;
    // Password Edit Text
    private EditText passwordInputText;
    // Confirm Password Edit Text
    private EditText confirmPasswordInputText;

    //Prograss Dialog
    ProgressDialog progressDialog;
    // Firebase Authentication object
    private FirebaseAuth mAuth;
    // Firebase Authentication State Listener object
    private FirebaseAuth.AuthStateListener mAuthListener;


    // Called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Get the reference to the Ok Button
        okButton = (Button) findViewById(R.id.okButton);
        // Get the reference to the Name EditText
        nameInputText = (EditText) findViewById(R.id.nameInputText);
        // Get the reference to the Email EditText
        emailInputText = (EditText) findViewById(R.id.emailInputText);
        // Get the reference to the Password EditText
        passwordInputText = (EditText) findViewById(R.id.passwordInputText);
        // Get the reference to the Confirm Password EditText
        confirmPasswordInputText = (EditText) findViewById(R.id.confirmPasswordInputText);

        // Initialize the Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(SignUpActivity.this);
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

    @Override
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    // Method is when user clicks on the Ok Button
    public void okClicked(View view)
    {

        // Get the reference to the Name EditText
        String name = nameInputText.getText().toString().trim();
        String email = emailInputText.getText().toString().trim();
        String password = passwordInputText.getText().toString();
        String confirm_password = confirmPasswordInputText.getText().toString();

        // name text is empty
        if(name.equals(""))
        {
            // animate shake
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            nameInputText.startAnimation(shake);
        }
        // email text is empty
        if(email.equals(""))
        {
            // animate shake
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            emailInputText.startAnimation(shake);
        }
        // password text is empty
        if(password.equals(""))
        {
            // animate shake
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            passwordInputText.startAnimation(shake);
        }
        // confirm password text is empty
        if(confirm_password.equals(""))
        {
            // animate shake
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            confirmPasswordInputText.startAnimation(shake);
        }

        // password and confirm password does not match
        if( !password.equals(confirm_password))
        {
            // send a message
            Toast.makeText(this, "Password does not match. Please try again.", Toast.LENGTH_LONG).show();
        }

        // go to business layout if name, email, password and confirm password is not empty.
        // password and confirm password should be identical in order to go to business layout.
        else if(!name.isEmpty() && !email.isEmpty() &&
                !password.isEmpty() && !confirm_password.isEmpty() &&
                password.equals(confirm_password)
        //showing progress dialog if all fields are entered


                )
        {    progressDialog.setMessage("Registering User");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                //sign-in success
                                Log.d(TAG, "successfully created user w/ email", task.getException());
                                Toast.makeText(SignUpActivity.this,"Authentication Success", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateactivity
                            } else {
                                //if sign in fails
                                Log.w(TAG, "create userwithEmail:failed", task.getException());
                                Toast.makeText(SignUpActivity.this,"Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                            //hiding progress dialog after all tasts are complete
                            progressDialog.dismiss();
                        }
                    });

            // Open another Activity (Screen)
/*            Intent intent = new Intent(this, BusinessLayout.class);
            startActivity(intent);
            SignUpActivity.this.finish(); */
        }

    }


}
