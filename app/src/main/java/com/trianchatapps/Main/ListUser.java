package com.trianchatapps.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trianchatapps.AdapterRecyclerview.AdapterAllUser;
import com.trianchatapps.Model.User;
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

    private DatabaseReference databaseReference;
            Context context;
    private List<User> userList ;
    private AdapterAllUser adapter;
    private String owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);
        ButterKnife.bind(this);
        inisisi();
    }

    private void inisisi() {
        context = ListUser.this;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(layoutManager);

        loadData();
        owner = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

    }

    private void loadData() {
        databaseReference.child("USER")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userList = new ArrayList<>();
                        User user = new User();
                        userList.clear();
                        if(dataSnapshot.exists()){
                            linearKosong.setVisibility(View.GONE);
                            linearIsi.setVisibility(View.VISIBLE);
                            for (DataSnapshot data : dataSnapshot.getChildren()){
                                user = data.getValue(User.class);
                                if (user.getUid().equals(owner)){
                                    //gausah diinput
                                }else {
                                    userList.add(user);
                                }
                            }
                            adapter = new AdapterAllUser(context,owner,userList);
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

}
