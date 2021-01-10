package ca.uwaterloo.sentimo;

import android.media.MediaMetadataRetriever;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utils {

    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     */
    public static String formatMilliSeccond(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        //      return  String.format("%02d Min, %02d Sec",
        //                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
        //                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
        //                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));

        // return timer string
        return finalTimerString;
    }

    public static String formatDateModified(long lastModified) {
        Date lastModDate = new Date(lastModified);
        String[] date = lastModDate.toString().split(" ");
        return date[2] + " " + date[1] + " " + date[5];
    }

    public static String getTimeAgo(long lastModified) {
        Date now = new Date();

        long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - lastModified);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - lastModified);
        long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - lastModified);
        long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - lastModified);

        if(seconds < 60){
            return "just now";
        } else if (minutes == 1) {
            return "a minute ago";
        } else if (minutes > 1 && minutes < 60) {
            return minutes + " minutes ago";
        } else if (hours == 1) {
            return "an hour ago";
        } else if (hours > 1 && hours < 24) {
            return hours + " hours ago";
        } else if (days == 1) {
            return "a day ago";
        } else if (days <= 5) {
            return days + " days ago";
        } else {
            return formatDateModified(lastModified);
        }
    }

    public static String getDuration(File file) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(file.getAbsolutePath());
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return Utils.formatMilliSeccond(Long.parseLong(durationStr));
    }
}
