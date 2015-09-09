package com.streetninja.corp.android.peoplemap.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        PeoplemapAuthenticator pp = new PeoplemapAuthenticator(this);
        return pp.getIBinder();
    }
}
