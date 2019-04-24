package com.trianchatapps.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trianchatapps.Auth.Register;
import com.trianchatapps.Function;
import com.trianchatapps.GlobalVariabel;
import com.trianchatapps.Model.UserModel;
import com.trianchatapps.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfil extends AppCompatActivity {

    @BindView(R.id.iv_profil_foto)
    CircleImageView ivProfilFoto;
    @BindView(R.id.et_profil_nama)
    EditText etProfilNama;
    @BindView(R.id.et_profil_email)
    EditText etProfilEmail;
    @BindView(R.id.btn_profil_simpan)
    Button btnProfilSimpan;
    @BindView(R.id.btn_myprofil_logout)
    Button btnMyprofilLogout;

    public DatabaseReference databaseReference;
    public FirebaseUser firebaseUser;
    public Context context;
    private FirebaseAnalytics mFirebaseAnalytics;
    private  FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profil);
        ButterKnife.bind(this);
        context = MyProfil.this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            data_user();
        }else {
            startActivity(new Intent(context, Register.class));
            finish();
        }
    }

    public void data_user() {
        try {


            databaseReference.child(GlobalVariabel.CHILD_USER)
                    .child(firebaseUser.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                UserModel user;

                                user = dataSnapshot.getValue(UserModel.class);

                                Glide.with(context)
                                        .load(user.getPhotoUrl())
                                        .placeholder(R.drawable.undraw_jason_mask_t07o)
                                        .into(ivProfilFoto);
                                etProfilEmail.setText(user.getEmail());
                                etProfilNama.setText(user.getDisplayName());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }catch (NullPointerException e){
            Crashlytics.logException(e);
            FirebaseCrash.report(e);
        }catch (Exception e){
            Crashlytics.logException(e);
        }catch (Throwable e){
            Crashlytics.logException(e);
        }
    }
    @OnClick(R.id.btn_myprofil_logout)
    public  void logout(){
        if (firebaseUser != null){
            //logout
            firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.signOut();
            firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                }
            });


            startActivity(new Intent(context, Register.class));
            finish();
        }else {
            // langsing ke resgister
            startActivity(new Intent(context, Register.class));
            finish();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (firebaseUser != null) {
            new Function.IsOffline().execute();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (firebaseUser != null) {
            new Function.IsOnline().execute();
        }
    }




}
