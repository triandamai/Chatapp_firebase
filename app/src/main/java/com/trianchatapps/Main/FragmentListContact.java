package com.trianchatapps.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.trianchatapps.AdapterRecyclerview.AdapterListContact;
import com.trianchatapps.GlobalVariabel;
import com.trianchatapps.Model.ContactModel;
import com.trianchatapps.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FragmentListContact extends Fragment {

    @BindView(R.id.iv_kosong)
    ImageView ivKosong;
    @BindView(R.id.linear_kosong)
    LinearLayout linearKosong;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.linear_isi)
    LinearLayout linearIsi;


    Context context;
    Unbinder unbinder;
    public DatabaseReference databaseReference;
    public ArrayList<ContactModel> listkontak = new ArrayList<>();
    public AdapterListContact adapter;
    public String saya;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank_contact, container, false);
        unbinder = ButterKnife.bind(this, view);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        inisisis();
        datacontact();
        return view;
    }


    public void inisisis() {
        context = getActivity();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        saya = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
    }

    public void datacontact() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);

        databaseReference.child(GlobalVariabel.CHILD_CONTACT)
                .child(saya)
                .child(GlobalVariabel.CHILD_CONTACT_FRIEND)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listkontak.clear();
                        if (dataSnapshot.exists()){
                            linearKosong.setVisibility(View.GONE);
                            ContactModel contact = new ContactModel();
                            for (DataSnapshot data : dataSnapshot.getChildren()){
                                       contact  = data.getValue(ContactModel.class);
                            }
                            listkontak.add(contact);
                            adapter = new AdapterListContact(context, saya,listkontak);
                            rv.setAdapter(adapter);

                        }else {
                            linearKosong.setVisibility(View.VISIBLE);
                            linearIsi.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



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
}
