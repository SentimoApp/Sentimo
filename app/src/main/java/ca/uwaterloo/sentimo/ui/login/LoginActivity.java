package ca.uwaterloo.sentimo.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText emailID, password;
    Button btnSignIn;
    TextView tvSignUp;
    com.google.firebase.auth.FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ca.uwaterloo.sentimo.R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailID = findViewById(ca.uwaterloo.sentimo.R.id.editTextTextEmailAddress);
        password = findViewById(ca.uwaterloo.sentimo.R.id.editTextTextPassword);
        btnSignIn = findViewById(ca.uwaterloo.sentimo.R.id.button);
        tvSignUp = findViewById(ca.uwaterloo.sentimo.R.id.textView);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            com.google.firebase.auth.FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mFirebaseUser != null) {
                    Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    android.content.Intent i = new android.content.Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this, "Please Log In", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailID.getText().toString();
                String pwd = password.getText().toString();
                if (email.isEmpty()) {
                    emailID.setError("Please enter Email ID");
                    emailID.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields Are Empty", Toast.LENGTH_SHORT).show();

                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login Error, Please Login Again", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent intToHome = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intToHome);
                            }

                        }
                    });
                }
                else {
                    Toast.makeText(LoginActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(LoginActivity.this, login_page.class);
                startActivity(intSignUp);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}