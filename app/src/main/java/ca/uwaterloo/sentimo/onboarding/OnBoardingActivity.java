package ca.uwaterloo.sentimo.onboarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import ca.uwaterloo.sentimo.MainActivity;
import ca.uwaterloo.sentimo.R;

import static java.security.AccessController.getContext;

public class OnBoardingActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * The number of pages.
     */
    private static final int NUM_PAGES = 3;
    private final static String prevStarted = "prevStarted";

    ViewPager2 viewPager;
    Button start_btn;
    FragmentStateAdapter sliderAdapter;
    int currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding);

        //Hooks
        viewPager = findViewById(R.id.slider);
        start_btn = findViewById(R.id.onboarding_start_btn);
        start_btn.setOnClickListener(this);

        //Call adapter
        sliderAdapter = new SlidePagerAdapter(this);
        viewPager.setAdapter(sliderAdapter);
    }



    @Override
    public void onClick(View v) {
        skip();
    }

    private class SlidePagerAdapter extends FragmentStateAdapter {
        public SlidePagerAdapter(OnBoardingActivity onBoardingActivity) {
            super(onBoardingActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = new SlidePageFragment();
            Bundle args = new Bundle();
            args.putInt(SlidePageFragment.ARG_OBJECT, position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    public void skip() {
        // User has seen the Onboarding sequence, so mark our SharedPreferences
        // flag as completed so that we don't show our OnboardingSupportFragment
        // the next time the user launches the app.
        SharedPreferences.Editor sharedPreferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        sharedPreferencesEditor.putBoolean(
                prevStarted, true);
        sharedPreferencesEditor.apply();
        // go back to main activity
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void next(View view) {
        viewPager.setCurrentItem(currentPos + 1);
    }
}