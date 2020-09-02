package com.afa.facebooklogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.afa.facebooklogin.databinding.ActivityMainBinding;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Facebooklogin";
    private CallbackManager callbackManager;



    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    ActivityMainBinding activityFacebookloginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_facebooklogin);

        activityFacebookloginBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityFacebookloginBinding.getRoot();
        setContentView(view);


        Log.e(TAG, "onCreate: ");
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();





        activityFacebookloginBinding.loginButton.setReadPermissions("user_friends");
        activityFacebookloginBinding.loginButton.registerCallback(callbackManager, callback);

        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                Log.e(TAG, "onCurrentAccessTokenChanged: " );
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                Log.e(TAG, "onCurrentProfileChanged: " );
                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult: " );
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void displayMessage(Profile profile){
        Log.e(TAG, "displayMessage: ");
        if(profile != null){
            activityFacebookloginBinding.textView.setText("Graph id: "+profile.getId());
            Log.d(TAG, "First name: "+profile.getFirstName());
            Log.d(TAG, "Last name: "+profile.getLastName());
            Log.d(TAG, " name: "+profile.getId());

            Uri url= Profile.getCurrentProfile().getProfilePictureUri(256,256);
            Glide.with(this)
                    .load(url)
                    .circleCrop()
                    .into(activityFacebookloginBinding.imageView);

        }
    }



    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            displayMessage(profile);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };
}