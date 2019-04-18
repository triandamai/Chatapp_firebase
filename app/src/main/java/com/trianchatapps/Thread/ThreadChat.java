package com.trianchatapps.Thread;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trianchatapps.AdapterRecyclerview.AdapterChatrv;
import com.trianchatapps.Function;
import com.trianchatapps.Model.Message;
import com.trianchatapps.Model.User;
import com.trianchatapps.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ThreadChat extends AppCompatActivity {

    @BindView(R.id.activity_thread_send_fab)
    FloatingActionButton sendFab;
    @BindView(R.id.activity_thread_input_edit_text)
    TextInputEditText inputEditText;
    @BindView(R.id.activity_thread_editor_parent)
    RelativeLayout editorParent;
    @BindView(R.id.activity_thread_messages_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.iv_back_thread)
    ImageView ivBackThread;
    @BindView(R.id.gambar_user_thread)
    CircleImageView gambarUserThread;
    @BindView(R.id.txt_nama_thread_toolbar)
    TextView txtNamaThreadToolbar;
    @BindView(R.id.attach)
    ImageView attach;


    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private User user;
    private FirebaseUser firebaseUser;
    private String userUid;
    private String owner;
    private String photoUrl;
    private Context context;
    private AdapterChatrv adapter;
    final ArrayList<Message> messages = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        ButterKnife.bind(this);
        context = ThreadChat.this;

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        Intent intent = getIntent();

        sendFab.requestFocus();
        userUid = intent.getStringExtra("uid");
        owner = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataUser();
        inisisi_recycler();


    }

    private void dataUser() {
        databaseReference.child("USER")
                .child(userUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = new User();
                        if (dataSnapshot.exists()) {

                            user = dataSnapshot.getValue(User.class);
                            Glide.with(getApplicationContext())
                                    .load(user.getPhotoUrl())
                                    .placeholder(R.drawable.undraw_working_remotely_jh40)
                                    .into(gambarUserThread);
                            txtNamaThreadToolbar.setText(user.getDisplayName());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @OnClick(R.id.activity_thread_send_fab)
    public void onclck() {
        long timestam = new Date().getTime();
        long datTimestamp = getDayTimestamp(timestam);
        String body = inputEditText.getText().toString().trim();
        Message message =
                new Message(timestam, -timestam, datTimestamp, body, owner, userUid);
        databaseReference
                .child("CHAT")
                .child(userUid)
                .child(owner)
                .push()
                .setValue(message);
        if (!owner.equals(userUid)) {
            databaseReference
                    .child("CHAT")
                    .child(owner)
                    .child(userUid)
                    .push()
                    .setValue(message);
        }
        inputEditText.setText("");
        databaseReference.child("notifications")
                .push()
                .setValue(message);
        inisisi_recycler();

    }

    @OnClick(R.id.iv_back_thread)
    public void kembali() {
        onBackPressed();
    }

    private long getDayTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }

    public void inisisi_recycler() {

//        Query messageQuery = databaseReference
//                .child(owner)
//                .child(userUid)
//                .orderByChild("negatedTimestamp");
//        AdapterChat adapterChat = new AdapterChat(owner,context,messageQuery);
//        messagesRecycler.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true));
//
//        messagesRecycler.setAdapter(adapterChat);

        databaseReference.child("CHAT")
                .child(owner)
                .child(userUid)
                .orderByChild("negatedTimestamp")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Message chat;
                        if (dataSnapshot.exists()) {
                            messages.clear();

                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                chat = data.getValue(Message.class);
                                messages.add(chat);
                            }
                        } else {
                            //emptyView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        databaseReference.child("CHAT")
                .child(owner)
                .child(userUid)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



        adapter = new AdapterChatrv(context, owner, messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true));
        recyclerView.setAdapter(adapter);

    }
    @Override
    protected void onPause() {
        super.onPause();
        new Function.IsOffline().execute();
    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        new Function.IsOffline().execute();
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        adapter.notifyDataSetChanged();
//        new Function.IsOnline().execute();
//    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        new Function.IsOnline().execute();
    }
}
