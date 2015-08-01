package com.streetninja.corp.android.base;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

/**
 * Created by marctang on 3/3/15.
 */
public abstract class BaseAuthenticatedActivity extends BaseActivity implements
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

  public static final String BASE_TAG = BaseAuthenticatedActivity.class.getSimpleName();

  private static final String PREF_ACCOUNT_NAME = "preferred_account_name";

  /* Request code used to invoke sign in user interactions. */
  private static final int RC_SIGN_IN = 0;
  protected GoogleAccountCredential credential;
  protected GoogleApiClient mGoogleApiClient;
  protected SharedPreferences settings;

  /**
   * A flag indicating that a PendingIntent is in progress and prevents us from starting further
   * intents.
   */
  private boolean mIntentInProgress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    settings = getSharedPreferences(
        Constants.SETTINGSNAME, 0);
    credential = GoogleAccountCredential.usingAudience(this,
        "server:client_id:1028479695835-1qn3r5o3rebfvmmmlvmp564alg6nl40d"
            + ".apps.googleusercontent.com");

    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Plus.API)
        .addScope(Plus.SCOPE_PLUS_LOGIN)
        .build();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mGoogleApiClient.connect();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case RC_SIGN_IN:
        mIntentInProgress = false;

        if (!mGoogleApiClient.isConnecting()) {
          mGoogleApiClient.connect();
        }
        break;
    }
  }

  @Override
  protected void onStop() {
    super.onStop();

    if (mGoogleApiClient.isConnected()) {
      mGoogleApiClient.disconnect();
    }
  }

  @Override
  public void onConnected(Bundle bundle) {
    Log.d(BASE_TAG, "Google+ api connection successful");
  }

  @Override
  public void onConnectionSuspended(int i) {
    Log.d(BASE_TAG, "Google+ api connection suspended");
    mGoogleApiClient.connect();
  }

  /**
   * GoogleApiClient authentication failed callback.
   *
   * @param result the result of the connection attempt
   */
  @Override
  public void onConnectionFailed(ConnectionResult result) {
    Log.d(BASE_TAG, "Google+ api connection failed");
    if (!mIntentInProgress && result.hasResolution()) {
      try {
        mIntentInProgress = true;
        startIntentSenderForResult(result.getResolution().getIntentSender(),
            RC_SIGN_IN, null, 0, 0, 0);
      } catch (IntentSender.SendIntentException e) {
        // The intent was canceled before it was sent.  Return to the default
        // state and attempt to connect to get an updated ConnectionResult.
        mIntentInProgress = false;
        mGoogleApiClient.connect();
        Log.e(BASE_TAG, e.getMessage());
      }


    }

  }

  /**
   * Google+ api user login and sets account name.
   *
   * @param accountName username of the user
   */
  protected void setAccountName(String accountName) {
    if (credential != null && settings != null) {
      SharedPreferences.Editor editor = settings.edit();
      editor.putString(PREF_ACCOUNT_NAME, accountName);
      editor.commit();
      credential.setSelectedAccountName(accountName);
    }
  }


  public GoogleAccountCredential getCredential() {
    return credential;
  }

  public void setCredential(GoogleAccountCredential credential) {
    this.credential = credential;
  }
}
