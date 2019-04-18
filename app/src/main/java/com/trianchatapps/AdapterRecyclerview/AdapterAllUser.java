package com.trianchatapps.AdapterRecyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trianchatapps.Model.User;
import com.trianchatapps.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterAllUser extends RecyclerView.Adapter<AdapterAllUser.myViewHolder> {
    Context context;
    List<User> userList = new ArrayList<>();
    DatabaseReference databaseReference;
    private String owner;

    public AdapterAllUser(Context context, String owner, List<User> users) {
        this.context = context;
        this.userList = users;
        this.owner = owner;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact_add, viewGroup, false);


        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewholder, int i) {
        User user = new User();
        user = userList.get(i);


        Glide.with(context)
                .load(user.getPhotoUrl())
                .into(myViewholder.ivUser);
        myViewholder.tvNama.setText(user.getDisplayName());

        final User finalUser1 = user;
        myViewholder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("CONTACT")
                        .child(owner)
                        .child(finalUser1.getUid().toString())
                        .setValue("true");
                myViewholder.btnAdd.setEnabled(false);

            }
        });


    }

    @Override
    public int getItemCount() {

        return userList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        Context context;
        @BindView(R.id.iv_user)
        CircleImageView ivUser;
        @BindView(R.id.tv_nama)
        TextView tvNama;
        @BindView(R.id.parent_item)
        LinearLayout parent;
        @BindView(R.id.parent_item_parent)
        CardView item_parent;
        @BindView(R.id.btn_add)
        Button btnAdd;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
