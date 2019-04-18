package com.trianchatapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trianchatapps.Helper.Bantuan;

import java.util.HashMap;

public class Function {


    public static class IsOffline extends AsyncTask<String, Void, String> {
        private DatabaseReference databaseReference;
        private FirebaseUser firebaseUser;



        @Override
        protected String doInBackground(String... strings) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.keepSynced(true);
            databaseReference.child("USER")
                    .child(firebaseUser.getUid())
                    .child("online")
                    .setValue(false);

            return "anda off";
        }

    }
    public static class IsOnline extends AsyncTask<String, Void, String> {
        private DatabaseReference databaseReference;
        private FirebaseUser firebaseUser;



        @Override
        protected String doInBackground(String... strings) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.keepSynced(true);
            databaseReference.child("USER")
                    .child(firebaseUser.getUid())
                    .child("online")
                    .setValue(true);

            return "anda off";
        }

    }

}
