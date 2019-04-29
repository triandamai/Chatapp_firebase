package com.trianchatapps.AdapterRecyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.trianchatapps.GlobalVariabel;
import com.trianchatapps.Helper.Bantuan;
import com.trianchatapps.Model.ContactModel;
import com.trianchatapps.Model.UserModel;
import com.trianchatapps.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterAllUser extends RecyclerView.Adapter<AdapterAllUser.myViewHolder> {
    public Context context;
    public List<UserModel> userList ;
    public DatabaseReference databaseReference;
    public FirebaseUser firebaseUser;
    public String Uidsaya;

    public AdapterAllUser(Context context, String owner, List<UserModel> users) {
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
        final UserModel user;
        user = userList.get(i);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        Glide.with(context)
                .load(user.getPhotoUrl())
                .into(myViewholder.ivUser);
        myViewholder.tvNama.setText(user.getDisplayName());


        myViewholder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timestamp = new Date().getTime();
                final Bantuan bantuan = new Bantuan(context);
                bantuan.swal_loading("Tunggu ya");

                final ContactModel contact =
                        new ContactModel(
                                false,
                                timestamp,
                                firebaseUser.getDisplayName(),
                                firebaseUser.getUid());



                databaseReference = FirebaseDatabase.getInstance().getReference();
                //cek sudah da belum
                final DatabaseReference refAdd =  databaseReference.child(GlobalVariabel.CHILD_CONTACT)
                        .child(user.getUid())
                        .child(GlobalVariabel.CHILD_CONTACT_FRIEND_REQUEST)
                        .child(firebaseUser.getUid());
                myViewholder.progressBar.setVisibility(View.VISIBLE);
                myViewholder.detail.setVisibility(View.VISIBLE);
                myViewholder.detail.setText("Mengirim Permintaan");
                refAdd.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            refAdd.setValue(contact)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            myViewholder.detail.setText("Meminta Pertemanan...");
                                            myViewholder.progressBar.setVisibility(View.GONE);
                                        }
                                    });
                        }else {
                            new Bantuan(context).swal_warning("Kamu sudah mengirim pertemanan");
                            myViewholder.detail.setVisibility(View.GONE);
                            myViewholder.progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        final PopupMenu popupMenu =  new PopupMenu(context, myViewholder.btnMenu);

        popupMenu.inflate(R.menu.menu_item_person_add);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        myViewholder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            popupMenu.show();

            }
        });



    }

    @Override
    public int getItemCount() {

        return userList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        public Context context;
        @BindView(R.id.iv_user)
        CircleImageView ivUser;
        @BindView(R.id.tv_nama)
        TextView tvNama;
        @BindView(R.id.parent_item)
        LinearLayout parent;
        @BindView(R.id.parent_item_parent)
        CardView item_parent;
        @BindView(R.id.btn_add)
        ImageView btnAdd;
        @BindView(R.id.txt_detail_progress)
        TextView detail;
        @BindView(R.id.progress_add)
        ProgressBar progressBar;
        @BindView(R.id.btn_menu)
        ImageView btnMenu;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
