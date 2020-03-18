package com.hackdroid.firebaseauth;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button _loginbtn, _signup;
    EditText _email, _password;
    FirebaseAuth _auth;
    FirebaseUser _currentUser;
    String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _loginbtn = findViewById(R.id.loginbtn);
        _email = findViewById(R.id.email);
        _password = findViewById(R.id.password);
        _signup = findViewById(R.id.signup);

        //INIT TODO::
        _auth = FirebaseAuth.getInstance();
        _loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = _email.getText().toString();
                password = _password.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    return;
                }

                _signInWithemailandpassword(email, password);
            }
        });
        _signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = _email.getText().toString();
                password = _password.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    return;
                }
                _createUser(email, password);
            }
        });
    }

    private void _createUser(String email, String password) {
        _auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            _currentUser = _auth.getCurrentUser();
                            Log.d(TAG, "onComplete: " + _currentUser.isEmailVerified());
                            sendVerification(_currentUser);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e);
            }
        });
    }

    private void sendVerification(final FirebaseUser currentUser) {

        currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: email sent to " + currentUser.getEmail());
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e);
            }
        });
    }


    private void _signInWithemailandpassword(String email, String password) {
        _auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    _currentUser = _auth.getCurrentUser();
                    if (_currentUser.isEmailVerified() == false) {
                        sendVerification(_currentUser);
                    } else {
                        Toast.makeText(getApplicationContext(), "Login Succe", Toast.LENGTH_SHORT).show();
                        //login success do other stuffe
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });


    }

}
