package ca.uwaterloo.sentimo;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends AppCompatActivity {
    //private ViewPager mSlideViewPager;
    //private LinearLayout mDotLayout;

    private static String API_KEY = "r26SCJXZRdrIAdG6XZ5T2Iv6cP-jBqavfvJF-PaZqzOe";
    private static String URL =  "https://api.us-south.speech-to-text.watson.cloud.ibm.com/instances/574d9655-fd55-4be3-9a8d-88147d1ca2ff";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //mSlideViewPager = (ViewPager2) findViewById(R.id.slideViewPager);
        //mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);
    }

}