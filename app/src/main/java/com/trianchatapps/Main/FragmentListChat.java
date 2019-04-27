package com.trianchatapps.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trianchatapps.AdapterRecyclerview.AdapterListChat;
import com.trianchatapps.Auth.Register;
import com.trianchatapps.R;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FragmentListChat extends Fragment {
    @BindView(R.id.iv_kosong)
    ImageView ivKosong;
    @BindView(R.id.linear_kosong)
    LinearLayout linearKosong;
    @BindView(R.id.linear_isi)
    LinearLayout linearIsi;
    Unbinder unbinder;
    @BindView(R.id.rv)
    RecyclerView rv;

    public Context context;
    public DatabaseReference databaseReference;
    public ArrayList<String> listchat = new ArrayList<>();
    public AdapterListChat adapter;
    public String owner;
    public FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank_chat, container, false);
        unbinder = ButterKnife.bind(this, view);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        inisisi();
        datachat();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void inisisi() {
        context = getActivity();
        owner = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("CHAT")
                .child(owner);
        databaseReference.keepSynced(true);
    }

    public void datachat() {
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listchat.clear();
                if (dataSnapshot.exists()) {

                    linearKosong.setVisibility(View.GONE);
                    linearIsi.setVisibility(View.VISIBLE);
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        if (data.exists()) {
                            listchat.add(data.getKey());
                        }
                    }
                } else {
                    linearKosong.setVisibility(View.VISIBLE);
                    linearIsi.setVisibility(View.GONE);
                }
                adapter = new AdapterListChat(context, listchat);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
