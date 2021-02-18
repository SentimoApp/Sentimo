package ca.uwaterloo.sentimo.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ca.uwaterloo.sentimo.R;

public class SlidePageFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    TextView slide_txt;
    ImageView slide_img;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.onboarding_slides_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        slide_img = view.findViewById(R.id.slide_img);
        slide_txt = view.findViewById(R.id.slide_txt);
        Bundle args = getArguments();
        switch (args.getInt(ARG_OBJECT)) {
            case 0:
                slide_txt.setText(getString(R.string.first_screen_description));
                slide_img.setImageResource(R.drawable.onboarding1);
                break;
            case 1:
                slide_txt.setText(getString(R.string.second_screen_description));
                slide_img.setImageResource(R.drawable.onboarding2);
                break;
            case 2:
                slide_txt.setText(getString(R.string.third_screen_description));
                slide_img.setImageResource(R.drawable.onboarding3);
                break;
        }
    }
}
