package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.spotifyintervals.R;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import models.constants;
import models.user;
import services.UserService;

public class SplashActivity extends AppCompatActivity {

    private static final String SCOPES = "user-read-recently-played,user-library-modify,user-read-email,user-read-private,playlist-modify-public,playlist-modify-private";
    private static final String TAG = "Splash";
    private SharedPreferences.Editor editor;
    private SharedPreferences msharedPreferences;
    private RequestQueue queue;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        pb = findViewById(R.id.progressBarSplash);
        msharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);
        authenticate();
    }

    private void authenticate() {
        pb.setVisibility(View.VISIBLE);
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(constants.spotifyClientId,
                        AuthenticationResponse.Type.TOKEN,
                        constants.spotifyRedirectLink);
        builder.setScopes(new String[]{SCOPES});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, constants.REQUEST_CODE, request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == constants.REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    Log.d("STARTING", "GOT AUTH TOKEN");
                    editor.apply();
                    pb.setVisibility(View.INVISIBLE);
                    waitForUserInfo();
                    break;

                // Auth flow returned an error
                case ERROR:
                    Log.e(TAG, "error authenticating error: " + response.getError());
                    break;

                // Most likely auth flow was cancelled
                default:
                    break;
                    // Handle other cases
            }
        }
    }

    private void waitForUserInfo() {
        UserService userService = new UserService(queue, msharedPreferences);
        userService.userSearch(new UserService.VolleyCallBack() {
            @Override
            public void onUserSearchFinish(boolean foundUser) {
                if (foundUser) {
                    user spotifyUser = userService.getSpotifyUser();
                    editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("userid", spotifyUser.getUserid());
                    editor.putString("username", spotifyUser.getUsername());
                    Log.d("STARTING", "GOT USER INFORMATION");
                    // We use commit instead of apply because we need the information stored immediately
                    editor.commit();
                    startMain();
                } else {
                    Log.i(TAG, "Error finding user");
                }
            }

            @Override
            public void onSuccess() {

            }
        });
    }

    private void startMain() {
        Intent newintent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(newintent);
    }

}
