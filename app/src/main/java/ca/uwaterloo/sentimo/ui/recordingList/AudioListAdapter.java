package ca.uwaterloo.sentimo.ui.recordingList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import ca.uwaterloo.sentimo.AudioPlayerActivity;
import ca.uwaterloo.sentimo.R;
import ca.uwaterloo.sentimo.RecordActivity;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder> {

    private List<Recording> recordingList;
    private Context context;

    public AudioListAdapter(List<Recording> recordingList) {
        this.recordingList = recordingList;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        // get application context from parent ViewGroup
        context = parent.getContext();
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        Recording recording = recordingList.get(position);
        holder.list_title.setText(recording.getTitle());
        holder.list_date.setText(recording.getDateModified());
        holder.list_duration.setText(recording.getDuration());

        boolean isExpanded = recording.isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return recordingList.size();
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder {

        private TextView list_title;
        private TextView list_date;
        private TextView list_duration;
        private ImageView img_play_audio;
        private ImageView btn_save;
        private ImageView btn_rename;
        private ImageView btn_delete;
        private ImageView btn_share;
        private ConstraintLayout expandableLayout;
        private ConstraintLayout nonExpandableLayout;
        private boolean expanded = false;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);

            list_title = itemView.findViewById(R.id.list_title);
            list_date = itemView.findViewById(R.id.list_date);
            list_duration = itemView.findViewById(R.id.list_duration);
            img_play_audio = itemView.findViewById(R.id.list_play_btn);
            img_play_audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File fileToPlay = recordingList.get(getAdapterPosition()).getFile();
                    Intent intent = new Intent(context, AudioPlayerActivity.class);
                    intent.putExtra("FILE_TO_PLAY", fileToPlay);
                    context.startActivity(intent);
                }
            });

            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            nonExpandableLayout = itemView.findViewById(R.id.non_expandable_layout);
            nonExpandableLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     for (Recording recording : recordingList)
                     {
                         if (recordingList.indexOf(recording) == getAdapterPosition()) {
                             recording.setExpanded(!recording.isExpanded());
                             notifyItemChanged(getAdapterPosition());
                         }
                         else {
                             if (recording.isExpanded()) {
                                 recording.setExpanded(false);
                                 notifyItemChanged(recordingList.indexOf(recording));
                             }
                         }
                     }
                }
            });

            btn_delete = itemView.findViewById(R.id.btn_delete_audio);
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setMessage("Are you sure you want to delete this entry?")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    File oldFile = recordingList.get(getAdapterPosition()).getFile();
                                    oldFile.delete();
                                    Toast.makeText(context, "Recording discarded", Toast.LENGTH_LONG).show();
                                    notifyItemRemoved(getAdapterPosition());
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

            btn_rename = itemView.findViewById(R.id.btn_rename_audio);
            btn_rename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            btn_save = itemView.findViewById(R.id.btn_save_audio);
            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            btn_share = itemView.findViewById(R.id.btn_share_audio);
            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }

}
