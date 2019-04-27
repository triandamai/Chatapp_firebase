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
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trianchatapps.GlobalVariabel;
import com.trianchatapps.Helper.Bantuan;
import com.trianchatapps.Model.ContactModel;
import com.trianchatapps.Model.UserModel;
import com.trianchatapps.R;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterFriendRequest extends RecyclerView.Adapter<AdapterFriendRequest.myViewHolder> {
    public Context context;
    public List<UserModel> userList ;
    public DatabaseReference databaseReference;
    public FirebaseUser firebaseUser;
    public String Uidsaya;

    public AdapterFriendRequest(Context context, List<UserModel> users, String owner) {
        this.context = context;
        this.userList = users;
        this.Uidsaya = owner;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact_add, viewGroup, false);


        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewholder, int i) {
        final UserModel user ;
        user = userList.get(i);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Bantuan bantuan =  new Bantuan(context);

        Glide.with(context)
                .load(user.getPhotoUrl())
                .into(myViewholder.ivUser);
        myViewholder.tvNama.setText(user.getDisplayName());


        myViewholder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timestamp = new Date().getTime();
                ContactModel contact =
                        new ContactModel(
                                false,
                                timestamp,
                                firebaseUser.getDisplayName(),
                                firebaseUser.getUid());

                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child(GlobalVariabel.CHILD_CONTACT)
                        .child(user.getUid())
                        .child(GlobalVariabel.CHILD_CONTACT_FRIEND_REQUEST)
                        .push()
                        .setValue(contact).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        new Bantuan(context).swal_sukses("Berhasil Menambahkan");


                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        new Bantuan(context).swal_error("Gagal Mnambahkan");
                    }
                });



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
