package com.trianchatapps;

import com.google.firebase.database.FirebaseDatabase;

public class OfflineCapabilities extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
