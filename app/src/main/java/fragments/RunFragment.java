package fragments;

import android.content.SharedPreferences;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
    private Button saveDataButton;
    private Button finishDriveButton;
    private Button goToGraphButton;
    private EditText editText;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_run, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
