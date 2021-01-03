package ca.uwaterloo.sentimo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private ImageButton recordBtn;
    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        recordBtn = findViewById(R.id.btn_record);
        recordBtn.setImageDrawable(getDrawable(R.drawable.record_icon_off));
        recordBtn.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void onClick(View v) {
        if(isRecording)
        {
            isRecording = false;
            recordBtn.setImageDrawable(getDrawable(R.drawable.record_icon_off));
        }
        else
        {
            if (checkPermissions())
            {
                isRecording = true;
                recordBtn.setImageDrawable(getDrawable(R.drawable.record_icon_on_1));
            }
        }
    }

    private boolean checkPermissions() {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(RecordActivity.this, MainActivity.class));
        return true;
    }
}