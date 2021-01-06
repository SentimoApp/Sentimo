package ca.uwaterloo.sentimo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class AudioPlayerActivity extends AppCompatActivity {

    private TextView txtDescription, txtFilename, txtDate, txtSeekPos, txtSeekDur;
    private ImageButton btnPlay, btnFor, btnRev;
    private SeekBar seekBar;

    private MediaPlayer mediaPlayer;
    private Runnable updateSeekBar;
    private Handler seekBarHandler = new Handler();

    private static boolean isPlaying = false;
    private File fileToPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        fileToPlay = (File) getIntent().getSerializableExtra("FILE_TO_PLAY");

        txtDate = findViewById(R.id.player_moddate_txt);
        txtDescription = findViewById(R.id.player_description_txt);
        txtFilename = findViewById(R.id.player_filename_txt);
        txtSeekDur = findViewById(R.id.player_duration_txt);
        txtSeekPos = findViewById(R.id.player_position_txt);
        btnPlay = findViewById(R.id.player_play_btn);
        btnFor = findViewById(R.id.player_forward_btn);
        btnRev = findViewById(R.id.player_reverse_btn);
        seekBar = findViewById(R.id.player_seekbar);

        playAudio();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying)
                    pauseAudio();
                else
                    resumeAudio();
            }
        });

        btnFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying)
                    resumeAudio();
                int currentPos = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();
                if (currentPos + 5000 >= duration)
                {
                    // go to end of audio file and pause.
                    txtSeekPos.setText(Utils.formatMilliSeccond(currentPos));
                    seekBar.setProgress(seekBar.getMax());
                    mediaPlayer.seekTo(duration);
                    pauseAudio();
                } else {
                    // fast forward 5 secs.
                    currentPos = currentPos + 5000;
                    txtSeekPos.setText(Utils.formatMilliSeccond(currentPos));
                    mediaPlayer.seekTo(currentPos);
                }
            }
        });

        btnRev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying)
                    resumeAudio();
                int currentPos = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();
                if (currentPos - 5000 < 0)
                {
                    // go to beginning of audio file
                    txtSeekPos.setText("0:00");
                    mediaPlayer.seekTo(0);
                } else {
                    // rewind 5 secs.
                    currentPos = currentPos - 5000;
                    txtSeekPos.setText(Utils.formatMilliSeccond(currentPos));
                    mediaPlayer.seekTo(currentPos);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
                txtSeekPos.setText(Utils.formatMilliSeccond(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseAudio();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                mediaPlayer.seekTo(progress);
                resumeAudio();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                pauseAudio();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isPlaying)
            stopAudio();
    }

    private void pauseAudio() {
        mediaPlayer.pause();
        btnPlay.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_36dp));
        isPlaying = false;
        seekBarHandler.removeCallbacks(updateSeekBar);
    }

    private void resumeAudio() {
        mediaPlayer.start();
        btnPlay.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_36dp));
        isPlaying = true;
        updateRunnable();
        seekBarHandler.postDelayed(updateSeekBar, 0);
    }

    private void stopAudio() {
        //Stops The Audio Completely.
        isPlaying = false;
        mediaPlayer.pause();
        mediaPlayer.stop();
        mediaPlayer.release();
        seekBarHandler.removeCallbacks(updateSeekBar);
    }

    private void playAudio() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnPlay.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_36dp));
        txtFilename.setText(fileToPlay.getName());
        txtDate.setText(Utils.formatDateModified(fileToPlay.lastModified()));
        //Play the audio
        isPlaying = true;
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                pauseAudio();
            }
        });
        // set seekbar max value
        seekBar.setMax(mediaPlayer.getDuration());
        // set audio duration
        txtSeekDur.setText(Utils.formatMilliSeccond(mediaPlayer.getDuration()));

        seekBarHandler = new Handler();
        updateRunnable();
        seekBarHandler.postDelayed(updateSeekBar, 0);
    }

    private void updateRunnable() {
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekBarHandler.postDelayed(this, 500);
            }
        };
    }

}













