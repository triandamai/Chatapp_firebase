package com.trianchatapps.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.trianchatapps.AdapterRecyclerview.AdapterListContact;
import com.trianchatapps.Function;
import com.trianchatapps.GlobalVariabel;
import com.trianchatapps.Model.Contact;
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

    AdapterListContact adapterListContact;

    private Context context;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        context = FriendRequest.this;
        ButterKnife.bind(this);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();


      if (firebaseUser != null){
        load_contact();
      }
    }

    private void load_contact() {
        final List<Contact> contacts = new ArrayList<>();
        databaseReference.child(GlobalVariabel.CHILD_CONTACT)
                .child(firebaseUser.getUid())
                .child(GlobalVariabel.CHILD_CONTACT_FRIEND_REQUEST)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            Contact contact = new Contact();
                            for (DataSnapshot data : dataSnapshot.getChildren()){
                                if (data.child("isfriend").getValue(boolean.class) != true) {
                                    contact = data.getValue(Contact.class);
                                }
                            }
                            contacts.add(contact);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }


}