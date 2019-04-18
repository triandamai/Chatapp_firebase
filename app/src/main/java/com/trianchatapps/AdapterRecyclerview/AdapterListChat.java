package com.trianchatapps.AdapterRecyclerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trianchatapps.Helper.Bantuan;
import com.trianchatapps.Model.Message;
import com.trianchatapps.Model.User;
import com.trianchatapps.R;
import com.trianchatapps.Thread.ThreadChat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterListChat extends RecyclerView.Adapter<AdapterListChat.MyViewHolder> {

    private List<String> userList;
    Context context;
    private String owner;
    private DatabaseReference databaseReference;
    private String position ;

    public AdapterListChat(Context context, String owner, ArrayList<String> userList) {
        this.context = context;
        this.owner = owner;
        this.userList = userList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_chat, viewGroup, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        position = userList.get(i);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("USER")
                .child(userList.get(i))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user;
                        if (dataSnapshot.exists()) {
                             user = dataSnapshot.getValue(User.class);
                                if (user.getUid().equals(owner)){
                                    myViewHolder.parentItem.setVisibility(View.GONE);
                                }else {
                                    myViewHolder.tvNama.setText(user.getDisplayName());
                                    Glide.with(context.getApplicationContext())
                                            .load(user.getPhotoUrl())
                                            .placeholder(R.drawable.undraw_online_friends_x73e)
                                            .into(myViewHolder.ivUser);
                                    if (user.isOnline()){
                                        myViewHolder.txtIsonline.setTextColor(Color.GREEN);
                                        myViewHolder.txtIsonline.setText("Online");
                                    }else {
                                        myViewHolder.txtIsonline.setTextColor(Color.RED);
                                       myViewHolder.txtIsonline.setText("Offline");
                                    }
                                }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        databaseReference.child("CHAT")
                .child(owner)
                .child(userList.get(i))
                .orderByKey().limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            Message message = new Message();
                            for (DataSnapshot data : dataSnapshot.getChildren()){
                                message = data.getValue(Message.class);
                            }
                            if (message.getFrom().equals(owner)) {
                                myViewHolder.tvIsi.setText("Anda : "+message.getBody());
                            }else {
                                myViewHolder.tvIsi.setText(message.getBody());
                            }
                        }else {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        myViewHolder.parentItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,ThreadChat.class)
                        .putExtra("uid",userList.get(i)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
