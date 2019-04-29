package com.trianchatapps.AdapterRecyclerview;

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
import com.trianchatapps.GlobalVariabel;
import com.trianchatapps.Main.Profil;
import com.trianchatapps.Model.ContactModel;
import com.trianchatapps.Model.UserModel;
import com.trianchatapps.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.trianchatapps.Thread.ThreadChat;

public class AdapterListContact extends RecyclerView.Adapter<AdapterListContact.MyViewHolder> {



    public List<ContactModel> contacts;
    public Context context;
    public DatabaseReference databaseReference;
    public String saya;


    public AdapterListContact(Context context, String owner,List<ContactModel> listkontak) {
        this.context = context;
        this.contacts = listkontak;
        this.saya = owner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, viewGroup, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final ContactModel user = contacts.get(i);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(GlobalVariabel.CHILD_USER)
                .child(user.getFriendsUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                             UserModel user ;
                             user = dataSnapshot.getValue(UserModel.class);
                             myViewHolder.tvNama.setText(user.displayName);
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

        //TODO:: aksi onklik
        myViewHolder.parentItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        myViewHolder.btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ThreadChat.class)
                        .putExtra(GlobalVariabel.EXTRA_UID, user.getFriendsUid())
                        .putExtra(GlobalVariabel.EXTRA_REQUEST,"noo"));
            }
        });


    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_user)
        ImageView ivUser;
        @BindView(R.id.tv_nama)
        TextView tvNama;
        @BindView(R.id.parent_item)
        LinearLayout parentItem;
        @BindView(R.id.btn_call_voice)
        ImageView btnCall;
        @BindView(R.id.btn_contact_send_msg)
        ImageView btnMsg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
