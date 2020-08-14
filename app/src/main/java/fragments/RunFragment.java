package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.spotifyintervals.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import adapters.PlaylistAdapter;
import models.SongFull;
import models.SongsViewModel;
import models.constants;

public class RunFragment extends Fragment {

    private static final String TAG = "PlaylistFragment";
    private Boolean attach = false;
    private SpotifyAppRemote mSpotifyAppRemote;
    private ArrayList<SongFull> allSongs = new ArrayList<>();
    private SongsViewModel viewModel;
    private RecyclerView rvPlaylist;
    private PlaylistAdapter playlistAdapter;
    private RelativeLayout relativeLayout;
    private TextView trackText;
    private TextView thankText;
    private LinearLayoutManager linearLayoutManager;
    private FloatingActionButton floatingActionButton;
    private ProgressBar progressBar;
    private TextView errorText;
    private Boolean playing = false;
    private AudioManager audioManager;
    private Track currentTrack;
    private String trackName;
    private String playlistURI;
    private Long songLength;
    private int songProgress;
    private View view;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_run, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        audioManager = (AudioManager) view.getContext().getSystemService(Context.AUDIO_SERVICE);
        sharedPreferences = view.getContext().getSharedPreferences("SPOTIFY", 0);
        int maxLevel = sharedPreferences.getInt("maxVolume", audioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC));
        int minLevel = sharedPreferences.getInt("minVolume", audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, minLevel + (maxLevel - minLevel) / 2, 0);
        relativeLayout = view.findViewById(R.id.playlistLayout);
        trackName = "";
        errorText = view.findViewById(R.id.errorText);
        trackText = view.findViewById(R.id.SongText);
        progressBar = view.findViewById(R.id.progressBarSong);
        rvPlaylist = view.findViewById(R.id.rvSongs);
        viewModel = ViewModelProviders.of(this.getActivity()).get(SongsViewModel.class);
        floatingActionButton = view.findViewById(R.id.playButton);
        try {
            allSongs = viewModel.getSongList();
            playlistURI = viewModel.getPlaylistService().getPlaylistURI();
        } catch (Exception e) {
            Log.i(TAG, "View model empty");
        }
        if (allSongs.size() > 0) {
            setUpSpotifyRemote(view);
            trackText.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            linearLayoutManager = new LinearLayoutManager(view.getContext());
            playlistAdapter = new PlaylistAdapter(allSongs, view.getContext());
            rvPlaylist.setAdapter(playlistAdapter);
            rvPlaylist.setLayoutManager(linearLayoutManager);
            playlistAdapter.notifyDataSetChanged();
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playOrPause();
                }
            });
        }
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (playing) {
                    updateProgressbar();
                }
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (floatingActionButton != null) {
            floatingActionButton.setVisibility(View.GONE);
        }
        if (mSpotifyAppRemote != null) {
            mSpotifyAppRemote.getPlayerApi().pause();
            SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        }
        playing = false;
    }

    private void updateProgressbar() {
        songProgress += 2000; // every 2 seconds this thread is triggered so 2000 ms are added to progress
        progressBar.setProgress(songProgress);
    }

    private void playOrPause() {
        if (playing) {
            mSpotifyAppRemote.getPlayerApi().pause();
            floatingActionButton.setImageResource(R.drawable.play_button);
            playing = false;
        } else {
            mSpotifyAppRemote.getPlayerApi().resume();
            floatingActionButton.setImageResource(R.drawable.pause_icon);
            playing = true;
        }
    }

    private void setUpSpotifyRemote(View view) {
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(constants.spotifyClientId)
                        .setRedirectUri(constants.spotifyRedirectLink)
                        .showAuthView(true)
                        .build();
        SpotifyAppRemote.connect(view.getContext(), connectionParams,
                new Connector.ConnectionListener() {
                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        mSpotifyAppRemote.getPlayerApi().setShuffle(false);
                        mSpotifyAppRemote.getPlayerApi().play(playlistURI);
                        floatingActionButton.setVisibility(View.VISIBLE);
                        floatingActionButton.setImageResource(R.drawable.pause_icon);
                        playing = true;
                        mSpotifyAppRemote.getPlayerApi()
                                .subscribeToPlayerState()
                                .setEventCallback(playerState -> {
                                    final Track track = playerState.track;
                                    if (track != null) {
                                        if (trackName.isEmpty() || !trackName.equals(track.name)) {
                                            trackName = track.name;
                                            songLength = track.duration;
                                            songProgress = 0;
                                            progressBar.setMax((int) track.duration);
                                            trackText.setText(track.name + " by " + track.artist.name);
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);
                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }
}
