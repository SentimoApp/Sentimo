package ca.uwaterloo.sentimo.ui.voice;

public class ConvertMP3 {
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private boolean audioRecordingPermissionGranted;

    private Button startRecordingButton;

    private MediaRecorder mediaRecorder;

    private String recordedFileName;
    private String convertedFileName;

    private Handler mainHandler;

    private boolean isRecording;

    private void startAudioRecording() throws IOException {
        toggleRecording();
        String uuid = UUID.randomUUID().toString();
        recordedFileName = getFilesDir().getPath() + "/" + uuid + ".3gp";
        convertedFileName = getFilesDir().getPath() + "/" + uuid + ".mp3";

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(recordedFileName);

        mediaRecorder.prepare();
        mediaRecorder.start();
    }

    private void convertSpeech() throws FileNotFoundException {
        toggleRecording();

        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();

        int rc = FFmpeg.execute(String.format("-i %s -c:a libmp3lame %s", recordedFileName, convertedFileName));

        if (rc == RETURN_CODE_SUCCESS) {
            Log.i(Config.TAG, "Command execution completed successfully.");
            // invoke Speech to Text service
        } else if (rc == RETURN_CODE_CANCEL) {
            Log.i(Config.TAG, "Command execution cancelled by user.");
        } else {
            Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", rc));
            Config.printLastCommandOutput(Log.INFO);
        }
    }
}



}


