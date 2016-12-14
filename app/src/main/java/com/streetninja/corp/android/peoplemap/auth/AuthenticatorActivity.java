package com.streetninja.corp.android.peoplemap.auth;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.streetninja.corp.android.peoplemap.R;

/**
 * Created by marctang on 9/8/15.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    public final static String TAG = AuthenticatorActivity.class.getSimpleName();

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";

    public final static String PARAM_USER_PASS = "USER_PASS";

    AccountManager mAccountManager;
    private String mAuthTokenType;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_authentication_splash);
        mAccountManager = AccountManager.get(getBaseContext());

        final EditText txtUsername = ((EditText)findViewById(R.id.textUsername));
        final EditText txtPassword = ((EditText)findViewById(R.id.textPassword));

        Button btnSignin = ((Button) findViewById(R.id.btnSignin));
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = txtUsername.getText().toString();
                final String password = txtPassword.getText().toString();
                AuthenticationServerRequest.asyncUserSignIn(AuthenticatorActivity.this, username,
                        password, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS,
                        new AuthenticationServerRequest.ResponseListener() {
                            @Override
                            public void requestStarted() {
                                Log.d(TAG, "request started");
                            }

                            @Override
                            public void requestCompleted(String response) {
                                Log.d(TAG, "request completed");
                                Toast.makeText(AuthenticatorActivity.this, response, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void requestCompleted() {
                            }


                            @Override
                            public void requestEndedWithError(VolleyError error) {
                                Log.e(TAG, "request ended");
                                Toast.makeText(AuthenticatorActivity.this, error.getMessage()!=null?error.getMessage():"Network error", Toast.LENGTH_LONG).show();
                            }
                        }
                );
            }
        });

    }

    private void finishLogin(Intent intent) {
        Log.d(TAG, "finishLogin");

        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            Log.d(TAG, "finishLogin > addAccountExplicitly");
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = mAuthTokenType;

            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authtokenType, authtoken);
        } else {
            Log.d(TAG, "finishLogin > setPassword");
            mAccountManager.setPassword(account, accountPassword);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }
}
