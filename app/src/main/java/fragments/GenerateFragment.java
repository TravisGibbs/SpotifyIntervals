package fragments;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.spotifyintervals.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;

import activities.SearchActivity;
import models.constants;
import models.objectID;
import services.PlaylistService;
import services.SongService;
import services.UserService;

public class GenerateFragment extends Fragment {

    private static final String TAG = "Generate";
    HashMap<String, objectID> nameIdMap = new HashMap<String, objectID>();
    private String radioButtonSelected = "";
    private int count = 0;
    private ChipGroup searchResults;
    private Chip chip0;
    private Chip chip1;
    private Chip chip2;
    private Chip chip3;
    private Chip chip4;
    private Button goToPlaylistButton;
    private Button makePlaylistButton;
    private Button getSongsButton;
    private Button searchButton;
    private RadioGroup radioGroup;
    private RadioButton radioTempo;
    private RadioButton radio352;
    private Slider sliderSets;
    private Slider sliderDanceIntense;
    private Slider sliderEnergyIntense;
    private Slider sliderValenceIntense;
    private Slider sliderDanceRelax;
    private Slider sliderEnergyRelax;
    private Slider sliderValenceRelax;
    private ArrayList<String> customIdArtists = new ArrayList<>();
    private ArrayList<String> customIdSongs = new ArrayList<>();
    private RelativeLayout relativeLayout;
    private SongService songService1;
    private SongService songService2;
    private PlaylistService playlistService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_generate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchResults = view.findViewById(R.id.searchObjects);
        chip0 = view.findViewById(R.id.chip0);
        chip1 = view.findViewById(R.id.chip1);
        chip2 = view.findViewById(R.id.chip2);
        chip3 = view.findViewById(R.id.chip3);
        chip4 = view.findViewById(R.id.chip4);
        makePlaylistButton = view.findViewById(R.id.button);
        searchButton = view.findViewById(R.id.searchButton);
        radio352 = view.findViewById(R.id.radioAlternate);
        radioTempo = view.findViewById(R.id.radioTempo);
        sliderSets = view.findViewById(R.id.setSlider);
        sliderDanceIntense = view.findViewById(R.id.danceSliderIntense);
        sliderDanceRelax = view.findViewById(R.id.danceSliderRelax);
        sliderEnergyIntense = view.findViewById(R.id.energySliderIntense);
        sliderEnergyRelax = view.findViewById(R.id.energySliderRelax);
        sliderValenceIntense = view.findViewById(R.id.valenceSliderIntense);
        sliderValenceRelax = view.findViewById(R.id.energySliderRelax);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        songService1 = new SongService(view.getContext(), relativeLayout);
        songService2 = new SongService(view.getContext(), relativeLayout);
        playlistService = new PlaylistService(view.getContext(), relativeLayout);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameIdMap.size() >= 5) {
                    Snackbar.make(relativeLayout, "Remove an artist and try again", Snackbar.LENGTH_SHORT).show();
                } else {
                    startSearchActivity();
                }
            }
        });

        makePlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTracks();
            }
        });

        chip0.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipListenerResponse(chip0);
            }
        });

        chip1.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipListenerResponse(chip1);
            }
        });

        chip2.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipListenerResponse(chip2);
            }
        });

        chip3.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipListenerResponse(chip3);
            }
        });

        chip4.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipListenerResponse(chip4);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == 0) {
                return;
            }
            setSearchResults(data);
        }
    }

    private void setSearchResults(Intent data) {
        nameIdMap.put(data.getStringExtra(constants.objectNameKey),
                new objectID(data.getBooleanExtra(constants.isSongKey, false), data.getStringExtra(constants.objectIDKey)));
        if (chip0.getText().toString().equals("")) {
            chip0.setText(data.getStringExtra(constants.objectNameKey));
            chip0.setVisibility(View.VISIBLE);
        } else if (chip1.getText().toString().equals("")) {
            chip1.setText(data.getStringExtra(constants.objectNameKey));
            chip1.setVisibility(View.VISIBLE);
        } else if (chip2.getText().toString().equals("")) {
            chip2.setText(data.getStringExtra(constants.objectNameKey));
            chip2.setVisibility(View.VISIBLE);
        } else if (chip3.getText().toString().equals("")) {
            chip3.setText(data.getStringExtra(constants.objectNameKey));
            chip3.setVisibility(View.VISIBLE);
        } else if (chip4.getText().toString().equals("")) {
            chip4.setText(data.getStringExtra(constants.objectNameKey));
            chip4.setVisibility(View.VISIBLE);
        }
    }

    private void startSearchActivity() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivityForResult(intent, 2);
    }

    private void chipListenerResponse(Chip chip) {
        nameIdMap.remove(chip.getText().toString());
        chip.setVisibility(View.GONE);
        chip.setText("");
    }

    private void getTracks() {
        int minutesIntense;
        int minutesRelaxed;
        if (radioTempo.isActivated()) {
            minutesIntense = Math.round(sliderSets.getValue() * 10);
            minutesRelaxed = Math.round(sliderSets.getValue() * 5);
        } else {
            minutesIntense = Math.round(sliderSets.getValue() * 7);
            minutesRelaxed = Math.round(sliderSets.getValue() * 3);
        }
        int songsIntense = (int) Math.round(minutesIntense / 3.5);
        int songsRelaxed = (int) Math.round(minutesRelaxed / 3.5);
        for (objectID objectID : nameIdMap.values()) {
            if (objectID.isSong()) {
                customIdSongs.add(objectID.getId());
            } else {
                customIdArtists.add(objectID.getId());
            }
        }
        if (nameIdMap.size() * 100 < songsIntense || nameIdMap.size() * 100 < songsRelaxed) {
            Float danceValueIntense = sliderDanceIntense.getValue();
            Float energyValueIntense = sliderEnergyIntense.getValue();
            Float valenceValueIntense = sliderValenceIntense.getValue();
            Float danceValueRelax = sliderDanceRelax.getValue();
            Float energyValueRelax = sliderEnergyRelax.getValue();
            Float valenceValueRelax = sliderValenceRelax.getValue();
            songService1.getSeedTracks(customIdSongs, customIdArtists, songsIntense, danceValueIntense, energyValueIntense, valenceValueIntense, new SongService.songServiceCallback() {
                @Override
                public void onSearchFinish(boolean found) {
                    if (found && count != 0) {
                        startButtonSwap(goToPlaylistButton, makePlaylistButton);
                    } else if (found) {
                        count++;
                    } else {
                        Log.i(TAG, "search failed");
                    }
                }
            });
            songService2.getSeedTracks(customIdSongs, customIdArtists, songsIntense, danceValueRelax, energyValueRelax, valenceValueRelax,
            new SongService.songServiceCallback() {
                @Override
                public void onSearchFinish(boolean found) {
                    if (found && count != 0) {
                        startButtonSwap(goToPlaylistButton, makePlaylistButton);
                    } else if (found) {
                        count++;
                    } else {
                        Log.i(TAG, "Search Failed");
                    }
                }
            });
        }
    }

    private void startButtonSwap(Button goUpButton, Button goRightButton) {
        ObjectAnimator animationButtonUp = ObjectAnimator.ofFloat(goUpButton, "translationY", -170f);
        animationButtonUp.setDuration(2000);
        moveButtonOffScreenRight(goRightButton);
        animationButtonUp.start();
    }

    private void moveButtonOffScreenRight(Button button) {
        ObjectAnimator animationButtonRight = ObjectAnimator.ofFloat(button, "translationX", 1800f);
        animationButtonRight.setDuration(1500);
        animationButtonRight.start();
    }
}
