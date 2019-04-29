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
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.trianchatapps.GlobalVariabel;
import com.trianchatapps.Helper.Bantuan;
import com.trianchatapps.Main.Profil;
import com.trianchatapps.Model.ContactModel;
import com.trianchatapps.Model.UserModel;
import com.trianchatapps.R;

import java.util.Date;
import java.util.List;

public class AdapterListRequestContact extends RecyclerView.Adapter<AdapterListRequestContact.MyViewHolder> {



    public List<ContactModel> contacts;
    public Context context;
    public DatabaseReference databaseReference;
    public String saya;
    public FirebaseUser firebaseUser;


    public AdapterListRequestContact(Context context, String owner, List<ContactModel> listkontak) {
        this.context = context;
        this.contacts = listkontak;
        this.saya = owner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact_accept, viewGroup, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final ContactModel user = contacts.get(i);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            myViewHolder.tvNama.setText(user.getName());
            myViewHolder.parentItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, Profil.class)
                            .putExtra(GlobalVariabel.EXTRA_UID, user.getFriendsUid())
                            .putExtra(GlobalVariabel.EXTRA_REQUEST, GlobalVariabel.EXTRA_REQUEST));
                }
            });

            myViewHolder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long timestamp = new Date().getTime();

                    final ContactModel contactModel1 = new ContactModel();
                    final ContactModel contactModel2 = new ContactModel();

                    contactModel1.setFriend(true);
                    contactModel1.setFriendsUid(firebaseUser.getUid());
                    contactModel1.setName(".");
                    contactModel1.setTimestamp(timestamp);

                    contactModel2.setFriend(true);
                    contactModel2.setFriendsUid(user.friendsUid);
                    contactModel2.setName(".");
                    contactModel2.setTimestamp(timestamp);
                    myViewHolder.progressAccept.setVisibility(View.VISIBLE);
                    myViewHolder.txtAcceptprog.setVisibility(View.VISIBLE);
                    myViewHolder.txtAcceptprog.setText("Menambahkan..");

                    databaseReference.child(GlobalVariabel.CHILD_CONTACT)
                            .child(user.friendsUid)
                            .child(GlobalVariabel.CHILD_CONTACT_FRIEND)
                            .child(firebaseUser.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        new Bantuan(context).swal_basic("Permintaan pertemanan sudah dikirim");
                                        myViewHolder.progressAccept.setVisibility(View.GONE);
                                        myViewHolder.txtAcceptprog.setVisibility(View.GONE);

                                    } else {

                                        DatabaseReference contactsayaref =  databaseReference.child(GlobalVariabel.CHILD_CONTACT)
                                                .child(firebaseUser.getUid())
                                                .child(GlobalVariabel.CHILD_CONTACT_FRIEND);

                                                //punya dia
                                                contactsayaref.child(user.friendsUid)
                                                .setValue(contactModel2);

                                                //punya saya
                                                contactsayaref.child(firebaseUser.getUid())
                                                .setValue(contactModel1);

                                                //punya saya
                                        databaseReference.child(GlobalVariabel.CHILD_CONTACT)
                                                .child(firebaseUser.getUid())
                                                .child(GlobalVariabel.CHILD_CONTACT_FRIEND_REQUEST)
                                                .child(user.friendsUid)
                                                .child("friend")
                                                .setValue(true);

                                        myViewHolder.progressAccept.setVisibility(View.GONE);
                                        myViewHolder.txtAcceptprog.setVisibility(View.GONE);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
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
        @BindView(R.id.btn_request_accept)
        ImageView btnAccept;
        @BindView(R.id.txt_progress_accept)
        TextView txtAcceptprog;
        @BindView(R.id.progress_accept)
        ProgressBar progressAccept;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
