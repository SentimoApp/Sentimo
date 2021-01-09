package ca.uwaterloo.sentimo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.visualizer.amplitude.AudioRecordView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    private final static int bitDepth = 16;
    private final static int sampleRate = 44100;
    private final static int bitRate = sampleRate * bitDepth;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private static String recordPath = null;
    private static String oldFileName = null;
    private static long startTime = 0L, time_MS = 0L;


    private ImageButton btnRecord;
    private TextView txtRecord;
    private Chronometer txtTimer_hm;
    private TextView txtTimer_ms;
    private AudioRecordView arvVisualizer;
    private MediaRecorder mRecorder = null;
    private Handler customHandler = new Handler();
    public static final int DELAY_BTN_AMIN_MILLIS = 500;

    // animation drawables
    private static Drawable anim1;
    private static Drawable anim2;
    private static Drawable anim3;

    private boolean isRecording = false;

    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            time_MS = startTime + SystemClock.uptimeMillis();
            int milliseconds = (int)(time_MS % 1000);
            txtTimer_ms.setText(String.format("%03d", milliseconds));
            customHandler.postDelayed(updateTimerThread, 10);
        }
    };

    private Runnable updateVisualizerThread = new Runnable() {
        @Override
        public void run() {
            int currentMaxAmplitude = mRecorder.getMaxAmplitude();
            arvVisualizer.update(currentMaxAmplitude);
            customHandler.postDelayed(updateVisualizerThread, 25);
        }
    };

    private Runnable updateAnimationThread = new Runnable() {
        @Override
        public void run() {
            if (btnRecord.getDrawable() == anim1) {
                btnRecord.setImageDrawable(anim2);
            }
            else if (btnRecord.getDrawable() == anim2) {
                btnRecord.setImageDrawable(anim3);
            } else {
                btnRecord.setImageDrawable(anim1);
            }
            customHandler.postDelayed(updateAnimationThread, DELAY_BTN_AMIN_MILLIS);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        btnRecord = findViewById(R.id.btn_record);
        btnRecord.setImageDrawable(getDrawable(R.drawable.record_icon_off));
        btnRecord.setOnClickListener(this);

        txtRecord = findViewById(R.id.txt_record);

        txtTimer_hm = findViewById(R.id.txt_timer_hm);
        txtTimer_ms = findViewById(R.id.txt_timer_ms);

        arvVisualizer = findViewById(R.id.audioRecordView);

        anim1 = getDrawable(R.drawable.record_icon_on_1);
        anim2 = getDrawable(R.drawable.record_icon_on_2);
        anim3 = getDrawable(R.drawable.record_icon_on_3);

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

    @Override
    protected void onStop() {
        super.onStop();
        if (mRecorder != null) {
            stopRecording();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();
    }

    private boolean checkPermissions() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED))
            return true;
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        return (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
    }

    private void startRecording() {
        recordPath = getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        oldFileName = "Recording_" + formatter.format(new Date()) + ".mp3";
        recordPath += "/" + oldFileName;

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setAudioEncodingBitRate(bitRate);
        mRecorder.setAudioSamplingRate(sampleRate);
        mRecorder.setOutputFile(recordPath);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
        txtTimer_hm.setBase(SystemClock.elapsedRealtime());
        txtTimer_hm.start();
        startTime = System.currentTimeMillis();
        arvVisualizer.clearAnimation();
        customHandler.postDelayed(updateTimerThread, 0);
        customHandler.postDelayed(updateVisualizerThread, 0);
        customHandler.postDelayed(updateAnimationThread, DELAY_BTN_AMIN_MILLIS);
    }

    private void stopRecording() {
        customHandler.removeCallbacks(updateTimerThread);
        customHandler.removeCallbacks(updateVisualizerThread);
        customHandler.removeCallbacks(updateAnimationThread);
        mRecorder.stop();
        txtTimer_hm.stop();
        mRecorder.release();
        saveRecording();
        mRecorder = null;
    }

    private void saveRecording() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialogue_save, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        userInput.setText(oldFileName);
        userInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInput.selectAll();
            }
        });
        Button cancel = (Button) promptsView.findViewById(R.id.save_cancel);
        Button ok = (Button) promptsView.findViewById(R.id.save_ok);


        // set dialog message
        alertDialogBuilder.setCancelable(false);

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newFileName = userInput.getText().toString();
                if (!newFileName.contains(".mp3"))
                    newFileName += ".mp3";
                if (newFileName != null && newFileName.trim().length() > 0) {
                    File newFile = new File(getExternalFilesDir("/").getAbsolutePath(), newFileName);
                    File oldFile = new File(getExternalFilesDir("/").getAbsolutePath(), oldFileName);
                    oldFile.renameTo(newFile);
                    Toast.makeText(RecordActivity.this, "Recording Saved", Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                }
            }
        });
        // show it
        alertDialog.show();
    }
}