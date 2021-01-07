package ca.uwaterloo.sentimo.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    android.widget.Button btnLogout;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ca.uwaterloo.sentimo.R.layout.activity_home);
        btnLogout = findViewById(ca.uwaterloo.sentimo.R.id.button2);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(HomeActivity.this, login_page.class);
                startActivity(intToMain);
            }
        });
    }
}