package com.trianchatapps.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trianchatapps.AdapterRecyclerview.AdapterAllUser;
import com.trianchatapps.Auth.Register;
import com.trianchatapps.Function;
import com.trianchatapps.Model.UserModel;
import com.trianchatapps.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListUser extends AppCompatActivity {

    @BindView(R.id.linear_kosong)
    LinearLayout linearKosong;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.linear_isi)
    LinearLayout linearIsi;

    public DatabaseReference databaseReference;
    public FirebaseUser firebaseUser;
    private Context context;
    public List<UserModel> userList ;
    public AdapterAllUser adapter;
    public String Uidsaya;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);
        context = ListUser.this;
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Daftar User ");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        inisisi();
    }

    public void inisisi() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(layoutManager);

        loadData();
        Uidsaya = FirebaseAuth.getInstance().getCurrentUser().getUid();

    }

    public void loadData() {
        databaseReference.child("USER")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userList = new ArrayList<>();
                        UserModel user = new UserModel();
                        userList.clear();
                        if(dataSnapshot.exists()){
                            linearKosong.setVisibility(View.GONE);
                            linearIsi.setVisibility(View.VISIBLE);
                            for (DataSnapshot data : dataSnapshot.getChildren()){
                                user = data.getValue(UserModel.class);
                                if (user.getUid().equals(Uidsaya)){
                                    //gausah diinput
                                }else {
                                    userList.add(user);
                                }
                            }
                            adapter = new AdapterAllUser(context, Uidsaya,userList);
                            rv.setAdapter(adapter);

                        }else{
                            linearIsi.setVisibility(View.GONE);
                            linearKosong.setVisibility(View.VISIBLE);
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
