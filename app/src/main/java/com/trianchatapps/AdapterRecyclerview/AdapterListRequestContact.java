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
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.google.firebase.database.*;
import com.trianchatapps.GlobalVariabel;
import com.trianchatapps.Main.Profil;
import com.trianchatapps.Model.ContactModel;
import com.trianchatapps.Model.UserModel;
import com.trianchatapps.R;

import java.util.List;

public class AdapterListRequestContact extends RecyclerView.Adapter<AdapterListRequestContact.MyViewHolder> {



    public List<ContactModel> contacts;
    public Context context;
    public DatabaseReference databaseReference;
    public String saya;


    public AdapterListRequestContact(Context context, String owner, List<ContactModel> listkontak) {
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
        if (user.isFriend != true){
            myViewHolder.tvNama.setText(user.getName());

        }else {
            myViewHolder.parentItem.setVisibility(View.GONE);
        }
        myViewHolder.parentItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Profil.class).putExtra(GlobalVariabel.EXTRA_UID, user.getFriendsUid()));
            }
        });



    }

    @Override
    public int getItemCount() {
        return contacts.size();
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
