package com.trianchatapps.AdapterRecyclerview;

import android.content.Context;
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
import com.trianchatapps.Model.Message;
import com.trianchatapps.Model.User;
import com.trianchatapps.R;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterChatrv extends RecyclerView.Adapter<AdapterChatrv.MyViewHolder> {
            Context context;
    private final List<Message> messageList;
    private final int ME = 1;
    private static final int VIEW_TYPE_SENT_WITH_DATE = 1;
    private final int FROM = 2;
    private static final int VIEW_TYPE_RECEIVED_WITH_DATE = 3;

    DatabaseReference databaseReference;

    private final String owner;
    public AdapterChatrv(Context context, String owner,ArrayList<Message> messageList){
        this.context = context;
        this.messageList = messageList;
        this.owner = owner;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void konfigurasi1(AdapterChatrv.MyViewHolder vh, int position ) {
            FirebaseUser userdetail = FirebaseAuth.getInstance().getCurrentUser();


            Message message = messageList.get(position);
            itemMessageBodyTextView.setText(message.getBody());
            itemMessageDateTextView.setText(getDatePretty(message.getTimestamp(),true));
            Glide.with(context)
                    .load(userdetail.getPhotoUrl())
                    .into(iv_user_bubble);
        }
        void konfigurasi2(AdapterChatrv.MyViewHolder vh, int position){
            Message message = messageList.get(position);
            itemMessageBodyTextView.setText(message.getBody());
            itemMessageDateTextView.setText(getDatePretty(message.getTimestamp(),true));
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.keepSynced(true);
            databaseReference.child("USER")
                    .child(message.getFrom())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                User user;

                                user = dataSnapshot.getValue(User.class);
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

        private String getDatePretty(long timestamp, boolean showTimeOfDay) {
            DateTime yesterdayDT = new DateTime(
                    DateTime.now().getMillis() - 1000 * 60 * 60 * 24);
            yesterdayDT = yesterdayDT.withTime(0, 0, 0, 0);
            Interval today = new Interval(
                    DateTime.now().withTimeAtStartOfDay(), Days.ONE);
            Interval yesterday = new Interval(
                    yesterdayDT, Days.ONE);
            DateTimeFormatter timeFormatter = DateTimeFormat.shortTime();
            DateTimeFormatter dateFormatter = DateTimeFormat.shortDate();
            if (today.contains(timestamp)) {
                if (showTimeOfDay) {
                    return timeFormatter.print(timestamp);
                } else {
                    return context.getString(R.string.today)+" "+timeFormatter.print(timestamp);
                }
            } else if (yesterday.contains(timestamp)) {
                return context.getString(R.string.yesterday)+" "+timeFormatter.print(timestamp);
            } else {
                return dateFormatter.print(timestamp);
            }
        }
    }

}
