package fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.example.spotifyintervals.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;

import activities.SearchActivity;
import models.SongFull;
import models.constants;
import models.objectID;
import services.PlaylistService;
import services.SongService;

public class GenerateFragment extends Fragment {

    private static final String TAG = "Generate";
    HashMap<String, objectID> nameIdMapIntense = new HashMap<String, objectID>();
    HashMap<String, objectID> nameIdMapRelax = new HashMap<String, objectID>();
    private String radioButtonSelected = "";
    private int count = 0;
    private ChipGroup searchResults;
    private ChipGroup searchResults2;
    private Chip chip0;
    private Chip chip1;
    private Chip chip2;
    private Chip chip3;
    private Chip chip4;
    private Chip chip5;
    private Chip chip6;
    private Chip chip7;
    private Chip chip8;
    private Chip chip9;
    private Button makePlaylistButton;
    private Button generateButton;
    private Button getSongsButton;
    private Button searchButtonIntense;
    private Button searchButtonRelaxed;
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
    private ScrollView scrollView;
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
        searchResults2 = view.findViewById(R.id.searchObjects);
        chip0 = view.findViewById(R.id.chip0);
        chip1 = view.findViewById(R.id.chip1);
        chip2 = view.findViewById(R.id.chip2);
        chip3 = view.findViewById(R.id.chip3);
        chip4 = view.findViewById(R.id.chip4);
        chip5 = view.findViewById(R.id.chip5);
        chip6 = view.findViewById(R.id.chip6);
        chip7 = view.findViewById(R.id.chip7);
        chip8 = view.findViewById(R.id.chip8);
        chip9 = view.findViewById(R.id.chip9);
        scrollView = view.findViewById(R.id.scrollView);
        generateButton = view.findViewById(R.id.generateButton);
        makePlaylistButton = view.findViewById(R.id.makePlaylistButton);
        searchButtonIntense = view.findViewById(R.id.searchButton);
        searchButtonRelaxed = view.findViewById(R.id.searchButton2);
        radioGroup = view.findViewById(R.id.radioGroup);
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

        searchButtonIntense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameIdMapIntense.size() >= 5) {
                    Snackbar.make(relativeLayout, "Remove an artist and try again", Snackbar.LENGTH_SHORT).show();
                } else {
                    startSearchActivity(false);
                }
            }
        });

        searchButtonRelaxed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameIdMapRelax.size() >= 5) {
                    Snackbar.make(relativeLayout, "Remove an artist and try again", Snackbar.LENGTH_SHORT).show();
                } else {
                    startSearchActivity(true);
                }
            }
        });

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTracks();
            }
        });

        makePlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postPlaylist();
            }
        });

        chip0.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipListenerResponse(chip0, 0);
            }
        });

        chip1.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipListenerResponse(chip1, 1);
            }
        });

        chip2.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipListenerResponse(chip2, 2);
            }
        });

        chip3.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipListenerResponse(chip3, 3);
            }
        });

        chip4.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipListenerResponse(chip4, 4);
            }
        });

        chip5.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipListenerResponse(chip5, 5);
            }
        });

        chip6.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipListenerResponse(chip6, 6);
            }
        });

        chip7.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipListenerResponse(chip7, 7);
            }
        });

        chip8.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipListenerResponse(chip8, 8);
            }
        });

        chip9.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipListenerResponse(chip9, 9);
            }
        });
    }

    private void postPlaylist() {
        ArrayList<SongFull> songsToPost = new ArrayList<>();
        ArrayList<SongFull> songsIntense = new ArrayList<>();
        ArrayList<SongFull> songsRelaxed = new ArrayList<>();
        songsIntense = songService1.getSongFulls();
        songsRelaxed = songService2.getSongFulls();
        // tempo: 3 5 5 2
        // basic 3 5 2
        if (radioTempo.isActivated()) {
            for (int i = 0; i < Math.round(sliderSets.getValue()); i++) {
                int j = 180000;
                while (j > 0) {
                    songsToPost.add(songsRelaxed.get(0));
                    j = j - songsRelaxed.get(0).getDuration_ms();
                    songsRelaxed.remove(0);
                }
                j = 600000;
                while (j > 0) {
                    songsToPost.add(songsIntense.get(0));
                    j = j - songsIntense.get(0).getDuration_ms();
                    songsIntense.remove(0);
                }
                j = 120000;
                while (j > 0) {
                    songsToPost.add(songsRelaxed.get(0));
                    j = j - songsRelaxed.get(0).getDuration_ms();
                    songsRelaxed.remove(0);
                }
            }
        } else {
            for (int i = 0; i < Math.round(sliderSets.getValue()); i++) {
                int j = 180000;
                while (j > 0) {
                    songsToPost.add(songsRelaxed.get(0));
                    j = j - songsRelaxed.get(0).getDuration_ms();
                    songsRelaxed.remove(0);
                }
                j = 420000;
                while (j > 0) {
                    songsToPost.add(songsIntense.get(0));
                    j = j - songsIntense.get(0).getDuration_ms();
                    songsIntense.remove(0);
                }
            }
        }
        playlistService.addPlaylist("title placeholder", songsToPost);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == 0) {
                return;
            }
            setSearchResults(data, false);
        }
        if (requestCode == 3) {
            if (requestCode == 0) {
                return;
            }
            setSearchResults(data, true);
        }
    }

    private void setSearchResults(Intent data, boolean relaxed) {
        if (!relaxed) {
            nameIdMapIntense.put(data.getStringExtra(constants.objectNameKey),
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
        } else {
            nameIdMapRelax.put(data.getStringExtra(constants.objectNameKey),
                    new objectID(data.getBooleanExtra(constants.isSongKey, false), data.getStringExtra(constants.objectIDKey)));
            if (chip5.getText().toString().equals("")) {
                chip5.setText(data.getStringExtra(constants.objectNameKey));
                chip5.setVisibility(View.VISIBLE);
            } else if (chip6.getText().toString().equals("")) {
                chip6.setText(data.getStringExtra(constants.objectNameKey));
                chip6.setVisibility(View.VISIBLE);
            } else if (chip7.getText().toString().equals("")) {
                chip7.setText(data.getStringExtra(constants.objectNameKey));
                chip7.setVisibility(View.VISIBLE);
            } else if (chip8.getText().toString().equals("")) {
                chip8.setText(data.getStringExtra(constants.objectNameKey));
                chip8.setVisibility(View.VISIBLE);
            } else if (chip9.getText().toString().equals("")) {
                chip9.setText(data.getStringExtra(constants.objectNameKey));
                chip9.setVisibility(View.VISIBLE);
            }
        }
    }

    private void startSearchActivity(boolean relaxed) {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        if (relaxed) {
            startActivityForResult(intent, 3);
        } else {
          startActivityForResult(intent, 2);
        }
    }

    private void chipListenerResponse(Chip chip, int chipNum) {
        if (chipNum < 5) {
            nameIdMapIntense.remove(chip.getText().toString());
        } else {
            nameIdMapRelax.remove(chip.getText().toString());
        }
        chip.setVisibility(View.GONE);
        chip.setText("");
    }

    private void getTracks() {
        int minutesIntense;
        int minutesRelaxed;
        if (radioTempo.isActivated()) {
            minutesIntense = Math.round(sliderSets.getValue() * 15); // pattern with a 1.5 times error allowance
            minutesRelaxed = Math.round(sliderSets.getValue() * 8);
        } else {
            minutesIntense = Math.round(sliderSets.getValue() * 12);
            minutesRelaxed = Math.round(sliderSets.getValue() * 6);
        }
        int songsIntense = minutesIntense / 3;
        int songsRelaxed = minutesRelaxed / 3;
        for (objectID objectID : nameIdMapIntense.values()) {
            if (objectID.isSong()) {
                customIdSongs.add(objectID.getId());
            } else {
                customIdArtists.add(objectID.getId());
            }
        }
        if (nameIdMapIntense.size() * 100 > songsIntense || nameIdMapIntense.size() * 100 > songsRelaxed) {
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
                        clearScreen();
                    } else if (found) {
                        count++;
                    } else {
                        Log.i(TAG, "search failed");
                    }
                }
            });
            customIdArtists.clear();
            customIdSongs.clear();
            for (objectID objectID : nameIdMapRelax.values()) {
                if (objectID.isSong()) {
                    customIdSongs.add(objectID.getId());
                } else {
                    customIdArtists.add(objectID.getId());
                }
            }
            songService2.getSeedTracks(customIdSongs, customIdArtists, songsIntense, danceValueRelax, energyValueRelax, valenceValueRelax,
            new SongService.songServiceCallback() {
                @Override
                public void onSearchFinish(boolean found) {
                    if (found && count != 0) {
                        clearScreen();
                    } else if (found) {
                        count++;
                    } else {
                        Log.i(TAG, "Search Failed");
                    }
                }
            });
        }
    }

    private void clearScreen() {
        scrollView.setVisibility(View.GONE);
        makePlaylistButton.setVisibility(View.VISIBLE);
    }
}
