package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.spotifyintervals.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapters.SearchAdapter;
import models.Artist;
import models.SearchObject;
import models.SongFull;
import models.constants;
import services.UserService;

public class SearchActivity extends AppCompatActivity {

  private static final String Tag = "SearchActivity";
  private SharedPreferences sharedPreferences;
  private RelativeLayout relativeLayout;
  private RequestQueue queue;
  private RecyclerView rvSearch;
  private EditText searchText;
  private SearchAdapter searchAdapter;
  private LinearLayoutManager linearLayoutManager;
  private ArrayList<SearchObject> searchObjects;
  private ImageButton backButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_search);
    getSupportActionBar().hide();
    sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
    queue = Volley.newRequestQueue(this);
    searchObjects = new ArrayList<>();
    rvSearch = findViewById(R.id.rvSearch);
    searchText = findViewById(R.id.searchBar);
    backButton = findViewById(R.id.backButton);
    SearchAdapter.OnClickListener onClickListener = new SearchAdapter.OnClickListener() {
      @Override
      public void onItemClicked(int position, String id, String name, Boolean isSong) {
        Intent intent = new Intent();
        intent.putExtra(constants.objectIDKey, id);
        intent.putExtra(constants.isSongKey, isSong);
        intent.putExtra(constants.objectNameKey, name);
        setResult(2, intent);
        finish();//finishing activity
      }
    };
    searchAdapter = new SearchAdapter(searchObjects, this, onClickListener);
    linearLayoutManager = new LinearLayoutManager(this);
    rvSearch.setAdapter(searchAdapter);
    rvSearch.setLayoutManager(linearLayoutManager);

    searchText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        getSearchObjects(new UserService.VolleyCallBack() {
          @Override
          public void onUserSearchFinish(boolean foundUser) {}
          @Override
          public void onSuccess() {
            Log.i(Tag, "search successful");
            }
          }, searchText.getText().toString());
          }
      @Override
      public void afterTextChanged(Editable editable) {
      }
    });
    backButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        goBack();
      }
    });
  }

  @Override
  public void onBackPressed() {
    goBack();
  }

  public void goBack() {
    Intent intent = new Intent();
    intent.putExtra("id", "");
    intent.putExtra("isSong", false);
    intent.putExtra("name", "");
    setResult(0, intent);
    finish();//finishing activity
  }

  public void getSearchObjects(UserService.VolleyCallBack text_searched, String q) {
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.GET, getUrl(q), null, response -> {
              Log.i(Tag, "searched " + q);
              Gson gson = new Gson();
              searchObjects.clear();
              JSONArray artists = new JSONArray();
              JSONArray songs = new JSONArray();
              try {
                artists = response.getJSONObject("artists").getJSONArray("items");
                for (int i = 0; i < artists.length(); i++) {
                  JSONObject jsonObject = (JSONObject) artists.get(i);
                  Artist artist = gson.fromJson(jsonObject.toString(), Artist.class);
                  SearchObject searchObject = new SearchObject(null, artist);
                  searchObjects.add(searchObject);
                }
                songs = response.getJSONObject("tracks").getJSONArray("items");
                for (int i = 0; i < songs.length(); i++) {
                  JSONObject jsonObject = (JSONObject) songs.get(i);
                  SongFull song = gson.fromJson(jsonObject.toString(), SongFull.class);
                  SearchObject searchObject = new SearchObject(song, null);
                  searchObjects.add(searchObject);
                }
              } catch (JSONException e) {
                e.printStackTrace();
              }
              searchAdapter.notifyDataSetChanged();
              text_searched.onSuccess();
            }, error -> {
              // TODO: Handle error
            }) {
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        String token = sharedPreferences.getString("token", "");
        String auth = "Bearer " + token;
        headers.put("Authorization", auth);
        return headers;
      }
    };
    queue.add(jsonObjectRequest);
  }

  private String getUrl(String q) {
    StringBuilder url = new StringBuilder();
    url.append("https://api.spotify.com/v1/search?q=");
    q.replace(" ", "%20");
    url.append(q);
    url.append("&type=artist,track");
    url.append("&limit=10");
    //TODO possible offset offset=(0,2000) for infinite scrolling
    return (url.toString());
  }
}
