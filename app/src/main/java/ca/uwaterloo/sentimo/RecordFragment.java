package ca.uwaterloo.sentimo;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class RecordFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private ImageButton recordBtn;
    private boolean isRecording = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_record, container, false);

        recordBtn = getView().findViewById(R.id.btn_record);
        recordBtn.setOnClickListener(this);

        return root;
    }
    
    @Override
    public void onClick(View v) {
        if(isRecording)
        {
            isRecording = false;
            recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_icon_off));
        }
        else
        {
            if (checkPermissions())
            {
                isRecording = true;
                recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_icon_on));
            }
        }
    }

    private boolean checkPermissions() {
        ActivityCompat.requestPermissions(this.getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        return true;
    }
}