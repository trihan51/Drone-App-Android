package com.example.trihan.drone_app_android;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mReenterPasswordEditText;
    private Button mSignupButton;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, Signup.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Signup", "onAuthStateChanged:signed_in:" + user.getUid());

                    // Direct User to FindDevicesActivity
                    Intent intent = FindDevicesActivity.newIntent(Signup.this);
                    startActivity(intent);
                } else {
                    // User is signed out
                    Log.d("Signup", "onAuthStateChanged:signed_out");
                }
            }
        };

        mEmailEditText = (EditText) findViewById(R.id.editText_email);
        mPasswordEditText = (EditText) findViewById(R.id.editText_password);
        mReenterPasswordEditText = (EditText) findViewById(R.id.editText_reenter_password);
        mSignupButton = (Button) findViewById(R.id.button_signup);

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                String reenteredPassword = mReenterPasswordEditText.getText().toString();

                if (!password.equals(reenteredPassword)) {
                    Toast.makeText(Signup.this, "Error: Passwords do not match", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    // Firebase expects passwords of length 6 or more, or else a
                    // FirebaseAuthWeakPasswordException will be thrown.
                    Toast.makeText(Signup.this, "Error: Password must contain at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("Signup", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(Signup.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
