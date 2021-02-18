package ca.uwaterloo.sentimo.ui.recordingList;

import java.io.File;

import ca.uwaterloo.sentimo.Utils;

public class Recording implements Comparable {

    private File recording;
    private boolean expanded;

    private String title;
    private String dateModified;
    private long chronologicalOrder;
    private String duration;

    public Recording(File recording) {
        setRecordingFile(recording);
        this.expanded = false;
    }

    public String getTitle() {
        return title;
    }

    public String getDateModified() {
        return dateModified;
    }

    public long getChronologicalOrder() {
        return chronologicalOrder;
    }

    public String getDuration() {
        return duration;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public File getFile() {
        return recording;
    }

    public void setRecordingFile(File recording) {
        this.recording = recording;
        title = recording.getName().replace(".mp3", "");
        dateModified = Utils.getTimeAgo(recording.lastModified());
        chronologicalOrder = recording.lastModified();
        duration = Utils.getDuration(recording);
    }

    @Override
    public int compareTo(Object o) {
        if (o.getClass() != Recording.class)
            return 0;
        return -Long.valueOf(this.getChronologicalOrder()).compareTo(((Recording) o).getChronologicalOrder());
    }
}
