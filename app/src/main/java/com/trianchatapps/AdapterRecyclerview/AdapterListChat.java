package com.trianchatapps.AdapterRecyclerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trianchatapps.Function;
import com.trianchatapps.GlobalVariabel;
import com.trianchatapps.Model.MessageModel;
import com.trianchatapps.Model.StatusAktifModel;
import com.trianchatapps.Model.UserModel;
import com.trianchatapps.R;
import com.trianchatapps.Thread.ThreadChat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterListChat extends RecyclerView.Adapter<AdapterListChat.MyViewHolder> {

    public List<String> UIDPengirim;
    public Context context;
    public String owner;
    public DatabaseReference databaseReference;
    public FirebaseUser firebaseUser;


    public AdapterListChat(Context context, String owner, ArrayList<String> UIDPengirim) {
        this.context = context;

        this.UIDPengirim = UIDPengirim;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_chat, viewGroup, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {

            myViewHolder.detail_user(UIDPengirim.get(i));
            myViewHolder.pesan_terakhir(UIDPengirim.get(i));
            myViewHolder.statusonline(UIDPengirim.get(i));

            myViewHolder.parentItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, ThreadChat.class)
                            .putExtra(GlobalVariabel.EXTRA_UID, UIDPengirim.get(i)));
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return UIDPengirim.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_user)
        CircleImageView ivUser;
        @BindView(R.id.tv_nama)
        TextView tvNama;
        @BindView(R.id.tv_isi)
        TextView tvIsi;
        @BindView(R.id.parent_item)
        LinearLayout parentItem;
        @BindView(R.id.txt_status_online)
        TextView txtIsonline;
        @BindView(R.id.txt_jumlah_unread)
        TextView txtbadge;

        public FirebaseUser firebaseUser;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        }
        public void pesan_terakhir(final String s) {
            databaseReference.child(GlobalVariabel.CHILD_CHAT)
                    .child(firebaseUser.getUid())
                    .child(s)
                    .orderByKey().limitToLast(1)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                MessageModel message = new MessageModel();
                                for (DataSnapshot data : dataSnapshot.getChildren()){
                                    message = data.getValue(MessageModel.class);
                                }
                                if (message.getFrom().equals(firebaseUser.getUid())) {
                                    tvIsi.setText("Anda : "+message.getBody());
                                }else {
                                    final MessageModel finalMessage = message;
                                    databaseReference.child(GlobalVariabel.CHILD_CHAT_BELUMDILIHAT)
                                            .child(firebaseUser.getUid())
                                            .child(s)
                                            .child(GlobalVariabel.CHILD_CHAT_UNREAD)
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()){
                                                    int jumlah = dataSnapshot.getValue(Integer.class);
                                                        if (jumlah > 0) {
                                                            tvIsi.setTypeface(null, Typeface.BOLD);
                                                            tvIsi.setText(finalMessage.getBody());
                                                            txtbadge.setVisibility(View.VISIBLE);
                                                            txtbadge.setText(String.valueOf(jumlah));
                                                        }else {
                                                            tvIsi.setTypeface(null,Typeface.NORMAL);
                                                            tvIsi.setText(finalMessage.getBody());
                                                            txtbadge.setVisibility(View.INVISIBLE);
                                                        }
                                                        }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                }
                            }else {

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        public void detail_user(String s) {
            databaseReference.child(GlobalVariabel.CHILD_USER)
                    .child(s)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserModel user;
                            if (dataSnapshot.exists()) {
                                user = dataSnapshot.getValue(UserModel.class);
                                if (user.getUid().equals(firebaseUser.getUid())){
                                    parentItem.setVisibility(View.GONE);
                                }else {
                                    tvNama.setText(user.getDisplayName());
                                    Glide.with(context.getApplicationContext())
                                            .load(user.getPhotoUrl())
                                            .placeholder(R.drawable.undraw_online_friends_x73e)
                                            .into(ivUser);
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        public void statusonline(String s) {
            databaseReference.child(GlobalVariabel.CHILD_USER_ONLINE)
                    .child(s)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                StatusAktifModel statusAktif;
                                statusAktif = dataSnapshot.getValue(StatusAktifModel.class);

                                if (statusAktif.isOnline() == 1){
                                    txtIsonline.setText("online");
                                }else if(statusAktif.isOnline() == 2){
                                    txtIsonline.setTextColor(Color.GRAY);
                                    txtIsonline.setText("Terakhir dilihat "+Function.getDatePretty(statusAktif.getTimestamp(),true));
                                }else if (statusAktif.isOnline() == 3){
                                    txtIsonline.setTextColor(Color.GRAY);
                                    txtIsonline.setText("Mengetik... ");

                                }else {
                                    txtIsonline.setTextColor(Color.GRAY);
                                    txtIsonline.setText("Terakhir dilihat "+Function.getDatePretty(statusAktif.getTimestamp(),true));

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }
}
