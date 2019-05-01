package com.trianchatapps.Auth;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.trianchatapps.GlobalVariabel;
import com.trianchatapps.Helper.Bantuan;
import com.trianchatapps.Main.MainActivity;
import com.trianchatapps.Model.UserModel;
import com.trianchatapps.R;

public class Register extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public FirebaseAuth firebaseAuth;
    public DatabaseReference databaseReference;
    public GoogleApiClient googleApiClient;
    public static final int RC_SIGN_IN = 9001;
    public Context context;
    public FirebaseAnalytics mFirebaseAnalytics;


    @BindView(R.id.btn_sign_google)
    SignInButton signInButton;
    @BindView(R.id.progress_loding)
    ProgressBar progressLoding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(Register.this);
        inisiasi_var();
        build_auth();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(context, MainActivity.class));
            finish();
        }


    }


    public void inisiasi_var() {
        context = Register.this;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.setLanguageCode("id");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);

    }

    public void build_auth() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(Register.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @OnClick(R.id.btn_sign_google)
    void signin() {
        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                progressLoding.setVisibility(View.VISIBLE);
                GoogleSignInAccount account = result.getSignInAccount();
                authWithGogle(account);
            }
        }
    }

    public void authWithGogle(final GoogleSignInAccount account) {

        final AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    new Bantuan(context).swal_loading("").dismiss();
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    tambahkeuser(firebaseUser);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressLoding.setVisibility(View.GONE);
                            startActivity(new Intent(context, MainActivity.class));
                            finish();
                        }
                    }, 1000);

                }
            }
        });
    }

    public void tambahkeuser(final FirebaseUser user) {
        UserModel user1 = new UserModel(
                user.getDisplayName(),
                user.getEmail(),
                user.getUid(),
                user.getPhotoUrl() == null ? "" : user.getPhotoUrl().toString());

        UserModel userModel = new UserModel();
        userModel.setDisplayName(user.getDisplayName());
        userModel.setEmail(user.getEmail());
        userModel.setPhotoUrl(user.getPhotoUrl().toString());
        userModel.setUid(user.getUid());


        databaseReference.child(GlobalVariabel.CHILD_USER)
                .child(user.getUid())
                .setValue(user1);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String instanceId = instanceIdResult.getToken();
                databaseReference.child(GlobalVariabel.CHILD_USER)
                        .child(user.getUid())
                        .child("instanceId")
                        .setValue(instanceId);
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

