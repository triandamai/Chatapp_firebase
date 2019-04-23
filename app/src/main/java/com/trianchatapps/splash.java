package com.trianchatapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.trianchatapps.Auth.Register;
import com.trianchatapps.Helper.Internet;
import com.trianchatapps.Main.MainActivity;
import com.trianchatapps.Services.BackgroundService;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ibrahimsn.particle.ParticleView;

public class splash extends AppCompatActivity {
    @BindView(R.id.particleView)
    ParticleView particleView;
    @BindView(R.id.root)
    public FrameLayout root;
    public FirebaseUser firebaseUser;
    public Context context;
    public ProgressDialog progressDialog;
    public DatabaseReference databaseReference;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        context = splash.this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        final Internet internet = new Internet();
        if (internet.CekKoneksi(context)) {
            inisiasi();
              cek_auth();

            FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");
            startService(new Intent(getBaseContext(), BackgroundService.class));

        } else {
            ////gada

            Snackbar snackbar = Snackbar.make(root, "Tidak ada koneksi ", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("COBA LAGI", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    internet.CekKoneksi(context);
                }
            });
            snackbar.show();
        }
    }

    public void inisiasi() {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void cek_auth() {

        if (firebaseUser != null) {
            pindahActivity(1);

        } else {

            pindahActivity(3);
        }
    }

    public void pindahActivity(int act) {
        switch (act) {
            case 1:
                startActivity(new Intent(context, MainActivity.class));
                finish();
                break;
            case 2:
                startActivity(new Intent(context, Register.class));
                finish();
                break;
            case 3:
                startActivity(new Intent(context, Register.class));
                finish();
                break;
            default:
                progressDialog.dismiss();
                break;
        }

    }


}
