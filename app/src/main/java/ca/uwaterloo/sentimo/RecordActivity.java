package ca.uwaterloo.sentimo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static String recordPath = null;

    private ImageButton btnRecord;
    private TextView txtRecord;
    private Chronometer tmrRecord;

    private MediaRecorder mRecorder;

    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        btnRecord = findViewById(R.id.btn_record);
        btnRecord.setImageDrawable(getDrawable(R.drawable.record_icon_off));
        btnRecord.setOnClickListener(this);

        txtRecord = findViewById(R.id.txt_record);

        tmrRecord = findViewById(R.id.tmr_record);

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
            stopRecording();
            btnRecord.setImageDrawable(getDrawable(R.drawable.record_icon_off));
            txtRecord.setText(R.string.stop_record);
        }
        else
        {
            if (checkPermissions())
            {
                isRecording = true;
                startRecording();
                btnRecord.setImageDrawable(getDrawable(R.drawable.record_icon_on_1));
                txtRecord.setText(R.string.start_record);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(RecordActivity.this, MainActivity.class));
        return true;
    }

    private boolean checkPermissions() {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        return true;
    }

    private void onRecord(boolean start) {
        if (start)
            startRecording();
        else
            stopRecording();
    }

    private void startRecording() {
        recordPath = getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat  formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        recordPath += "/Recording_" + formatter.format(new Date()) + ".3gp";

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(recordPath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
        tmrRecord.setBase(SystemClock.elapsedRealtime());
        tmrRecord.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        tmrRecord.stop();
        mRecorder.release();
        mRecorder = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }
}