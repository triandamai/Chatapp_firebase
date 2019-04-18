package com.trianchatapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.trianchatapps.Auth.Register;
import com.trianchatapps.Helper.Bantuan;
import com.trianchatapps.Main.MainActivity;
import com.trianchatapps.Model.User;
import com.trianchatapps.Services.BackgroundService;

import java.util.Objects;

public class splash extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private Context context ;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        inisiasi();
        cek_auth();

        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");
        startService(new Intent(getBaseContext(), BackgroundService.class));
    }
    private void inisiasi() {
        context = splash.this;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }
    private void cek_auth() {

        if (firebaseUser != null)
        {
           pindahActivity(1);

        }else {

            pindahActivity(3);
        }
    }

    private void pindahActivity(int act)
    {
        switch (act)
        {
            case 1:
                startActivity(new Intent(context,MainActivity.class));
                finish();
                break;
            case 2:
                startActivity(new Intent(context,Register.class));
                finish();
                break;
            case 3:
                startActivity(new Intent(context,Register.class));
                finish();
                break;
            default:
                progressDialog.dismiss();
                break;
        }

    }


}
