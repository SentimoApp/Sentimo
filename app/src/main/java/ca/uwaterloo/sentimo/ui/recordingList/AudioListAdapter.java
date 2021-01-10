package ca.uwaterloo.sentimo.ui.recordingList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import ca.uwaterloo.sentimo.R;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder> {

    private List<Recording> recordingList;
    private onItemListClick onItemListClick;

    public AudioListAdapter(List<Recording> recordingList, onItemListClick onItemListClick) {
        this.recordingList = recordingList;
        this.onItemListClick = onItemListClick;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
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

    public class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView list_title;
        private TextView list_date;
        private TextView list_duration;
        private ImageView img_play_audio;
        private ConstraintLayout expandableLayout;
        private ConstraintLayout nonExpandableLayout;
        private boolean expanded = false;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);

            list_title = itemView.findViewById(R.id.list_title);
            list_date = itemView.findViewById(R.id.list_date);
            list_duration = itemView.findViewById(R.id.list_duration);
            img_play_audio = itemView.findViewById(R.id.list_play_btn);
            img_play_audio.setOnClickListener(this);

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



        }

        @Override
        public void onClick(View v) {
            onItemListClick.onClickListener(recordingList.get(getAdapterPosition()).getFile(), getAdapterPosition());
        }
    }

    public interface onItemListClick {
        void onClickListener(File file, int position);
    }

}
