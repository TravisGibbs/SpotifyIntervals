package models;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import javax.annotation.Nullable;

import services.PlaylistService;

public class SongsViewModel extends ViewModel {

  private ArrayList<SongFull> songList;
  private PlaylistService playlistService;

  public SongsViewModel() {
    songList = new ArrayList<>();
  }

  public ArrayList<SongFull> getSongList() {
    return songList;
  }

  public void setSongList(ArrayList<SongFull> songList) {
    this.songList = songList;
  }

  @Nullable
  public PlaylistService getPlaylistService() {
    return playlistService;
  }

  public void setPlaylistService(PlaylistService playlistService) {
    this.playlistService = playlistService;
  }
}
