package ca.uwaterloo.sentimo.ui.recordingList;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import ca.uwaterloo.sentimo.AudioPlayerActivity;
import ca.uwaterloo.sentimo.R;

public class AudioListFragment extends Fragment {

    private RecyclerView audioList;
    private SwipeRefreshLayout refreshLayout;
    private File[] allFiles;

    private AudioListAdapter audioListAdapter;

    private MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;

    private File fileToPlay = null;

    public AudioListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audio_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshAdapter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        audioList = view.findViewById(R.id.lst_audio_list_view);
        refreshLayout = view.findViewById(R.id.swipe_container);

        audioList.setHasFixedSize(true);
        audioList.setLayoutManager(new LinearLayoutManager(getContext()));
        audioList.addItemDecoration(new DividerItemDecoration(audioList.getContext(), DividerItemDecoration.VERTICAL));
        audioList.setAdapter(audioListAdapter);
        audioListAdapter.notifyDataSetChanged();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // update file list
                refreshAdapter();
                // notify recycler view and update it
                audioList.setAdapter(audioListAdapter);
                audioListAdapter.notifyDataSetChanged();
                // Call setRefreshing(false) to signal refresh has finished
                refreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // update file list
        refreshAdapter();
        // notify recycler view and update it
        audioList.setAdapter(audioListAdapter);
        audioListAdapter.notifyDataSetChanged();
    }

    private void refreshAdapter() {
        String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        File directory = new File(path);
        allFiles = directory.listFiles();

        ArrayList<Recording> recordingList = new ArrayList<>();
        for (File recording : allFiles) {
            recordingList.add(new Recording(recording));
        }
        Collections.sort(recordingList);

        audioListAdapter = new AudioListAdapter(recordingList);
    }
}