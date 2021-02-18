package ca.uwaterloo.sentimo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.uwaterloo.sentimo.onboarding.OnBoardingActivity;

public class MainActivity extends AppCompatActivity {

    private final static String prevStarted = "prevStarted";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Check if we need to display our Onboarding Fragment
        if (!sharedpreferences.getBoolean(prevStarted, false)) {
            startActivity(new Intent(this, OnBoardingActivity.class));
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_recordingList, R.id.navigation_dashboard, R.id.navigation_calendar)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_activity_main);
        setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(navView, navController);
        // Not using toolbar to display titles/headings anymore
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        FloatingActionButton fab = findViewById(R.id.fab_record);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RecordActivity.class));
            }
        });
    }

}