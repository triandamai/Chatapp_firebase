package com.trianchatapps.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BackgroundService extends Service {
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service", "onstart");
//
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("service", "ondestroy");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        databaseReference.child("USER")
                .child(firebaseUser.getUid())
                .child("online")
                .setValue(false);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("service", "ontaskremove");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        databaseReference.child("USER")
                .child(firebaseUser.getUid())
                .child("online")
                .setValue(false);
    }

}
