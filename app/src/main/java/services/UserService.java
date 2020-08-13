package services;

import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import models.user;

public class UserService {

  private static final String ENDPOINT = "https://api.spotify.com/v1/me";
  private SharedPreferences msharedPreferences;
  private RequestQueue mqueue;
  private user spotifyUser;
  public UserService(RequestQueue queue, SharedPreferences sharedPreferences) {
    mqueue = queue;
    msharedPreferences = sharedPreferences;
  }

  public user getSpotifyUser() {
    return spotifyUser;
  }

  public void userSearch(final VolleyCallBack callBack) {
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(ENDPOINT, null, response -> {
      spotifyUser = new user();
      try {
        spotifyUser.setUsername(response.getString("display_name"));
        spotifyUser.setUserid(response.getString("id"));
      } catch (JSONException e) {
        e.printStackTrace();
      }
      callBack.onUserSearchFinish(true);
    }, error -> {
        callBack.onUserSearchFinish(false);
    }) {
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        String token = msharedPreferences.getString("token", "");
        String auth = "Bearer " + token;
        headers.put("Authorization", auth);
        return headers;
      }
    };
    mqueue.add(jsonObjectRequest);
  }

  public interface VolleyCallBack {
    void onUserSearchFinish(boolean foundUser);
  }

}
