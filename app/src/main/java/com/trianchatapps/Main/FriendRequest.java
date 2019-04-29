package com.trianchatapps.Main;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.*;
import com.trianchatapps.AdapterRecyclerview.AdapterListContact;
import com.trianchatapps.AdapterRecyclerview.AdapterListRequestContact;
import com.trianchatapps.Auth.Register;
import com.trianchatapps.Function;
import com.trianchatapps.GlobalVariabel;
import com.trianchatapps.Helper.Bantuan;
import com.trianchatapps.Model.ContactModel;
import com.trianchatapps.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.ArrayList;
import java.util.List;

public class FriendRequest extends AppCompatActivity {

    @BindView(R.id.iv_kosong)
    ImageView ivKosong;
    @BindView(R.id.linear_kosong)
    LinearLayout linearKosong;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.linear_isi)
    LinearLayout linearIsi;

   AdapterListRequestContact adapterListRequestContact;

    public Context context;
    public DatabaseReference databaseReference;
    public FirebaseUser firebaseUser;
    private FirebaseAnalytics mFirebaseAnalytics;
   public  List<ContactModel> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        context = FriendRequest.this;
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Permintaan Pertemanan");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        load_request_contact();

    }

    public void load_request_contact() {

        databaseReference.child(GlobalVariabel.CHILD_CONTACT)
                .child(firebaseUser.getUid())
                .child(GlobalVariabel.CHILD_CONTACT_FRIEND_REQUEST)
                .orderByChild("friend")
                .startAt(false)
                .endAt(false)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            try {
                                linearKosong.setVisibility(View.GONE);

                                ContactModel contact = new ContactModel();
                                for (DataSnapshot data : dataSnapshot.getChildren()) {

                                        contact = data.getValue(ContactModel.class);
                                    }

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                                rv.setLayoutManager(layoutManager);
                                contacts.add(contact);
                                adapterListRequestContact = new AdapterListRequestContact(context,firebaseUser.getUid(),contacts);
                                rv.setAdapter(adapterListRequestContact);
                            } catch (NullPointerException e){
                                Crashlytics.logException(e);
                                FirebaseCrash.report(e);
                            } catch (Exception e){
                                Crashlytics.logException(e);
                                FirebaseCrash.report(e);
                            } catch (Throwable e){
                                Crashlytics.logException(e);
                                FirebaseCrash.report(e);
                            }

                        }else {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    finish();
    }
}
