package com.trianchatapps.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.trianchatapps.Auth.Register;
import com.trianchatapps.Function;
import com.trianchatapps.Model.UserModel;
import com.trianchatapps.R;

public class MainActivity extends AppCompatActivity {
    public Context context;
    public FirebaseAuth firebaseAuth;
    public DatabaseReference databaseReference;
    public FirebaseUser firebaseUser;
    public FirebaseAuth.AuthStateListener authStateListener;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.menu_new_message)
    FloatingActionButton menuNewMessage;
    @BindView(R.id.menu_new_contact)
    FloatingActionButton menuNewContact;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.fab)
    FloatingActionMenu fab;

    public String instanceId;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_main_subtitle)
    TextView txtMainSubtitle;
    @BindView(R.id.iv_main_notif)
    ImageView ivMainNotif;
    public TabAdapterMainActivity adapter;
    @BindView(R.id.ly_name_toolbar)
    LinearLayout lyNameToolbar;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = MainActivity.this;
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            tambahkeuser(firebaseUser);
            txtMainTitle.setText(firebaseUser.getDisplayName());
            txtMainSubtitle.setText(firebaseUser.getEmail());
        } else {
            pindahActivity(1);
        }


        adapter = new TabAdapterMainActivity(getSupportFragmentManager());
        adapter.addFragment(new FragmentListChat(), "Chat");
       // adapter.addFragment(new FragmentListContact(), "Contact");
        adapter.addFragment(new FragmentListStory(), "Status");


        viewpager.setAdapter(adapter);
        tab.setupWithViewPager(viewpager);
    }

    @OnClick(R.id.iv_main_notif)
    public void main_notification() {
        startActivity(new Intent(context, FriendRequest.class));
    }

    @OnClick(R.id.menu_new_message)
    public void new_message(){
        startActivity(new Intent(context, ContactActivity.class));
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

    @OnClick(R.id.ly_name_toolbar)
    public void ke_profil() {
        startActivity(new Intent(context, MyProfil.class));

    }

    @OnClick(R.id.menu_new_contact)
    public void tambah_user() {
        startActivity(new Intent(context, ListUser.class));
    }

    @OnClick(R.id.menu_new_message)
    public void tambah_chat() {
        startActivity(new Intent(context, ContactActivity.class));
    }

    public void pindahActivity(int i) {
        switch (i) {
            case 1:
                startActivity(new Intent(context, Register.class));
                finish();
                break;
        }
    }


    public void tambahkeuser(FirebaseUser user) {
        // new Bantuan(context).toastLong("hmmmm");
        UserModel user1 = new UserModel(
                user.getDisplayName(),
                user.getEmail(),
                user.getUid(),
                user.getPhotoUrl() == null ? "" : user.getPhotoUrl().toString());
        UserModel userModel = new UserModel();
        userModel.setDisplayName(firebaseUser.getDisplayName());
        userModel.setEmail(firebaseUser.getEmail());
        userModel.setPhotoUrl(firebaseUser.getPhotoUrl().toString());
        userModel.setUid(firebaseUser.getUid());


        databaseReference.child("USER")
                .child(user.getUid())
                .setValue(user1);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                instanceId = instanceIdResult.getToken();
                databaseReference.child("USER")
                        .child(firebaseUser.getUid())
                        .child("instanceId")
                        .setValue(instanceId);
            }
        });


    }


}
