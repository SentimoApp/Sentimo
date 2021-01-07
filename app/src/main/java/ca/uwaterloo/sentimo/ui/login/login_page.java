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

import ca.uwaterloo.sentimo.MainActivity;

public class login_page extends AppCompatActivity {
    EditText emailID, password;
    Button btnSignUp;
    TextView tvSignIn;
    com.google.firebase.auth.FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ca.uwaterloo.sentimo.R.layout.activity_login_page);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailID = findViewById(ca.uwaterloo.sentimo.R.id.editTextTextEmailAddress);
        password = findViewById(ca.uwaterloo.sentimo.R.id.editTextTextPassword);
        btnSignUp = findViewById(ca.uwaterloo.sentimo.R.id.button);
        tvSignIn = findViewById(ca.uwaterloo.sentimo.R.id.textView);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                String email = emailID.getText().toString();
                String pwd = password.getText().toString();
                if (email.isEmpty()) {
                    emailID.setError("Please enter Email ID");
                    emailID.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(login_page.this, "Fields Are Empty", Toast.LENGTH_SHORT).show();

                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(login_page.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(login_page.this, "SignUp Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(login_page.this, HomeActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(login_page.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login_page.this,LoginActivity.class);
                startActivity(i);

            }
        });


    }
}