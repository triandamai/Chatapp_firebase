package com.trianchatapps.AdapterRecyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trianchatapps.Main.Profil;
import com.trianchatapps.Model.User;
import com.trianchatapps.R;
import com.trianchatapps.Thread.ThreadChat;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterListContact extends RecyclerView.Adapter<AdapterListContact.MyViewHolder> {



    private ArrayList<String> list;
    private Context context;
    private DatabaseReference databaseReference;


    public AdapterListContact(Context context, String owner, ArrayList<String> listkontak) {
        this.context = context;
        this.list = listkontak;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, viewGroup, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final String id = list.get(i);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("USER")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user ;
                    user = dataSnapshot.getValue(User.class);
                    myViewHolder.tvNama.setText(user.getDisplayName());
                    Glide.with(context)
                            .load(user.getPhotoUrl())
                            .placeholder(R.drawable.undraw_working_remotely_jh40)
                            .into(myViewHolder.ivUser);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myViewHolder.parentItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Profil.class)
                        .putExtra("uid", list.get(i)));

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Context context;
        @BindView(R.id.iv_user)
        ImageView ivUser;
        @BindView(R.id.tv_nama)
        TextView tvNama;
        @BindView(R.id.parent_item)
        LinearLayout parentItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
