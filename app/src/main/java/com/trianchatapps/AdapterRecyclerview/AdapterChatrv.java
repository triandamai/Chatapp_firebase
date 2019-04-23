package com.trianchatapps.AdapterRecyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.trianchatapps.Model.UserModel;
import com.trianchatapps.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterChatrv extends RecyclerView.Adapter<AdapterChatrv.MyViewHolder> {
    public     Context context;
    public final List<MessageModel> messageList;
    public final int ME = 1;
    public static final int VIEW_TYPE_SENT_WITH_DATE = 1;
    public final int FROM = 2;
    public static final int VIEW_TYPE_RECEIVED_WITH_DATE = 3;

    DatabaseReference databaseReference;

    public final String owner;
    public AdapterChatrv(Context context,  ArrayList<MessageModel> messageList){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        this.context = context;
        this.messageList = messageList;
        this.owner = user.getUid();

    }

    @Override
    public int getItemViewType(int position) {
        MessageModel message = messageList.get(position);
        if (message.getFrom().equals(owner)){
           return ME;

        }else {
           return FROM;
        }

    }

    @Override
    public AdapterChatrv.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewtype) {
        MyViewHolder viewHolder = null;

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (viewtype){
            case ME:
                View view1 = inflater.inflate(R.layout.item_message_sent,viewGroup,false);
                viewHolder = new MyViewHolder(view1);
                break;
            case FROM:
                View view2 = inflater.inflate(R.layout.item_message_received,viewGroup,false);
                viewHolder = new MyViewHolder(view2);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChatrv.MyViewHolder myViewHolder, int i) {
        switch (myViewHolder.getItemViewType()){
            case ME:
                MyViewHolder myViewHolder1 = (MyViewHolder) myViewHolder;
                myViewHolder.konfigurasi1(myViewHolder1,i);
                break;
            case FROM:
                MyViewHolder myViewHolder2 = (MyViewHolder) myViewHolder;
                myViewHolder.konfigurasi2(myViewHolder2,i);

                break;
        }
    }



    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_message_date)
        TextView itemMessageDateTextView;
        @BindView(R.id.item_message_body_text_view)
        TextView itemMessageBodyTextView;
        @BindView(R.id.item_message_parent)
        LinearLayout itemMessageParent;
        @BindView(R.id.iv_user_thread)
        CircleImageView iv_user_bubble;
        @BindView(R.id.iv_tick)
        ImageView iv_tick;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void konfigurasi1(AdapterChatrv.MyViewHolder vh, int position ) {
            FirebaseUser userdetail = FirebaseAuth.getInstance().getCurrentUser();


            MessageModel message = messageList.get(position);
            itemMessageBodyTextView.setText(message.getBody());
            itemMessageDateTextView.setText(Function.getDatePretty(message.getTimestamp(),true));
            Glide.with(context)
                    .load(userdetail.getPhotoUrl())
                    .into(iv_user_bubble);

            if (message.getTick() == 1){
                iv_tick.setImageResource(R.drawable.ic_check_1);
            }else if (message.getTick() == 2){
                iv_tick.setImageResource(R.drawable.ic_chech_2);
            }else if (message.getTick() == 3){

            }else {

            }

        }
        void konfigurasi2(AdapterChatrv.MyViewHolder vh, int position){
            MessageModel message = messageList.get(position);
            itemMessageBodyTextView.setText(message.getBody());
            itemMessageDateTextView.setText(Function.getDatePretty(message.getTimestamp(),true));
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.keepSynced(true);
            databaseReference.child(GlobalVariabel.CHILD_USER)
                    .child(message.getFrom())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                UserModel user;

                                user = dataSnapshot.getValue(UserModel.class);
                                Glide.with(context.getApplicationContext())
                                        .load(user.getPhotoUrl())
                                        .placeholder(R.drawable.undraw_working_remotely_jh40)
                                        .into(iv_user_bubble);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });





        }

//         void bluetick(int i) {
//            MessageModel message = messageList.get(i);
//            databaseReference.child(GlobalVariabel.CHILD_CHAT)
//                    .child(message.getFrom())
//                    .child(message.getTo())
//                    .child(key.get(i))
//                    .child("tick")
//                    .setValue(3);
//        }
    }

}
