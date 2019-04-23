package com.trianchatapps.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trianchatapps.Function;
import com.trianchatapps.GlobalVariabel;
import com.trianchatapps.Model.StatusAktifModel;

import java.util.Date;

public class BackgroundService extends Service {
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    long timestamp = new Date().getTime();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service", "onstart");

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("service", "ondestroy");
        StatusAktifModel statusAktif = new StatusAktifModel(2, timestamp);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        try {


            if (firebaseUser != null) {
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.keepSynced(true);
                databaseReference.child(GlobalVariabel.CHILD_USER_ONLINE)
                        .child(firebaseUser.getUid())
                        .setValue(statusAktif);
            } else {
                stopSelf();
            }
        } catch (NullPointerException e){
            new Function.send_report(e.getMessage());
        } catch (Exception e){
            new Function.send_report(e.getMessage());
        } catch (Throwable e){
            new Function.send_report(e.getMessage());
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("service", "ontaskremove");
        StatusAktifModel statusAktif = new StatusAktifModel(2, timestamp);
        try {


            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            if (firebaseUser != null) {
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.keepSynced(true);
                databaseReference.child(GlobalVariabel.CHILD_USER_ONLINE)
                        .child(firebaseUser.getUid())
                        .setValue(statusAktif);
            } else {
                stopSelf();
            }
        } catch (NullPointerException e){
            new Function.send_report(e.getMessage());
        } catch (Exception e){
            new Function.send_report(e.getMessage());
        } catch (Throwable e){
            new Function.send_report(e.getMessage());
        }
    }

}
