package com.trianchatapps.Thread;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.FontRequestEmojiCompatConfig;
import android.support.v4.provider.FontRequest;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
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
import com.trianchatapps.GlobalVariabel;
import com.trianchatapps.Helper.Bantuan;
import com.trianchatapps.Main.Profil;
import com.trianchatapps.Model.MessageModel;
import com.trianchatapps.Model.StatusAktifModel;
import com.trianchatapps.Model.UserModel;
import com.trianchatapps.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @BindView(R.id.attach_emoji)
    ImageView attach;
    @BindView(R.id.txt_status_online_toolbar)
    TextView txtStatusOnlineToolbar;
    @BindView(R.id.rl_toolbar)
    RelativeLayout rlToolbar;
    @BindView(R.id.attach_file)
    CircleImageView attachFile;
    @BindView(R.id.ly_input)
    LinearLayout lyInput;
    @BindView(R.id.root)
    LinearLayout root;


    public DatabaseReference databaseReference;
    public FirebaseAuth firebaseAuth;
    public UserModel user;
    public FirebaseUser firebaseUser;
    public String id_pengirim;
    public  String id_saya;
    public String photoUrl;
    public Context context;
    public AdapterChatrv adapter;
    public int tambah_jumlah_unseen_msg;
    public int jumlah;
    public final ArrayList<MessageModel> messages = new ArrayList<>();
    public MediaPlayer mediaPlayer;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        ButterKnife.bind(this);
        context = ThreadChat.this;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        Intent intent = getIntent();

        if (intent != null){

        sendFab.requestFocus();
        id_pengirim = intent.getStringExtra("uid");
        id_saya = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataUser();
        inisisi_recycler();
    }else {
            new Bantuan(context).swal_basic("tidak dapat memuat..");
            finish();
        }

        new Function.IsOnline().execute();

        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                new Function.IsTyping().execute();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new Function.IsTyping().execute();
            }

            @Override
            public void afterTextChanged(Editable s) {
                new Function.IsTyping().execute();
            }
        });

        FontRequest fontRequest = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs);
        EmojiCompat.Config config = new FontRequestEmojiCompatConfig(this, fontRequest)
                .setReplaceAll(true)
                .setEmojiSpanIndicatorEnabled(true)
                .setEmojiSpanIndicatorColor(Color.GREEN)
                .registerInitCallback(new EmojiCompat.InitCallback() {
                    @Override
                    public void onInitialized() {
                        super.onInitialized();
                    }

                    @Override
                    public void onFailed(@Nullable Throwable throwable) {
                        super.onFailed(throwable);
                    }
                });

        EmojiCompat.init(config);
    }

    public void dataUser() {
        databaseReference.child(GlobalVariabel.CHILD_USER)
                .child(id_pengirim)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserModel user;
                        if (dataSnapshot.exists()) {

                            user = dataSnapshot.getValue(UserModel.class);
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
        databaseReference.child(GlobalVariabel.CHILD_USER_ONLINE)
                .child(id_pengirim)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        StatusAktifModel statusAktif;
                        if (dataSnapshot.exists()) {
                            statusAktif = dataSnapshot.getValue(StatusAktifModel.class);

                            if (statusAktif.isOnline() == 1) {
                                txtStatusOnlineToolbar.setText("Online");
                            } else if (statusAktif.isOnline() == 2) {
                                txtStatusOnlineToolbar.setText("Terakhir dilihat " + Function.getDatePretty(statusAktif.getTimestamp(), true));
                            } else if (statusAktif.isOnline() == 3) {
                                txtStatusOnlineToolbar.setText("Mengetik...");
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        databaseReference
                .child(GlobalVariabel.CHILD_CHAT_BELUMDILIHAT)
                .child(id_saya)
                .child(id_pengirim)
                .child(GlobalVariabel.CHILD_CHAT_UNREAD)
                .setValue(0);
    }

    @OnClick(R.id.activity_thread_send_fab)
    public void onclck() {
        long timestam = new Date().getTime();
        long datTimestamp = Function.getDayTimestamp(timestam);
        String body = inputEditText.getText().toString().trim();
        MessageModel message =
                new MessageModel(timestam, -timestam, datTimestamp, body, id_saya, id_pengirim, 1);
        final String id_saya = databaseReference.push().getKey();
        databaseReference
                .child(GlobalVariabel.CHILD_CHAT)
                .child(id_pengirim)
                .child(this.id_saya)
                .child(id_saya)
                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(GlobalVariabel.CHILD_CHAT)
                        .child(id_pengirim)
                        .child(ThreadChat.this.id_saya)
                        .child(id_saya)
                        .child("tick")
                        .setValue(2);
            }
        });
        if (!this.id_saya.equals(id_pengirim)) {
            final String id_to = databaseReference.push().getKey();
            databaseReference
                    .child(GlobalVariabel.CHILD_CHAT)
                    .child(this.id_saya)
                    .child(id_pengirim)
                    .child(id_to)
                    .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    databaseReference.child(GlobalVariabel.CHILD_CHAT)
                            .child(ThreadChat.this.id_saya)
                            .child(id_pengirim)
                            .child(id_to)
                            .child("tick")
                            .setValue(2);
                }
            });
        }

        databaseReference.child(GlobalVariabel.CHILD_NOTIF)
                .push()
                .setValue(message);
        databaseReference.child(GlobalVariabel.CHILD_CHAT_BELUMDILIHAT)
                .child(id_pengirim)
                .child(this.id_saya)
                .child(GlobalVariabel.CHILD_CHAT_UNREAD)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            jumlah = dataSnapshot.getValue(Integer.class);
                            databaseReference
                                    .child(GlobalVariabel.CHILD_CHAT_BELUMDILIHAT)
                                    .child(id_pengirim)
                                    .child(ThreadChat.this.id_saya)
                                    .child(GlobalVariabel.CHILD_CHAT_UNREAD)
                                    .setValue(jumlah + 1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        inputEditText.setText("");
        inisisi_recycler();

    }


    @OnClick(R.id.iv_back_thread)
    public void kembali() {
        onBackPressed();
    }

    @OnClick(R.id.rl_toolbar)
    public void clickrl() {
        startActivity(new Intent(ThreadChat.this, Profil.class)
                .putExtra(GlobalVariabel.EXTRA_UID, id_pengirim));
    }

    @OnClick(R.id.attach_emoji)
    public void attach_emoji() {
        EmojiCompat.get();
    }


    public void inisisi_recycler() {
        final List<String> key = new ArrayList<>();

//        Query messageQuery = databaseReference
//                .child(id_saya)
//                .child(id_pengirim)
//                .orderByChild("negatedTimestamp");
//        AdapterChat adapterChat = new AdapterChat(id_saya,context,messageQuery);
//        messagesRecycler.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true));
//
//        messagesRecycler.setAdapter(adapterChat);

        databaseReference.child("CHAT")
                .child(id_saya)
                .child(id_pengirim)
                .orderByChild("negatedTimestamp")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        MessageModel chat;
                        if (dataSnapshot.exists()) {
                            messages.clear();
                            try {


                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    chat = data.getValue(MessageModel.class);
                                    String dtaa = data.getKey();
                                    messages.add(chat);
                                    key.add(dtaa);
                                }
                            }catch (Exception e){
                                Crashlytics.logException(e);
                            }catch (Error e){
                                Crashlytics.logException(e);
                            }
                        } else {
                            //emptyView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        databaseReference.child(GlobalVariabel.CHILD_CHAT)
                .child(id_saya)
                .child(id_pengirim)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        adapter.notifyDataSetChanged();
                    }
                });

        try {


            adapter = new AdapterChatrv(context, messages);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true));
            recyclerView.setAdapter(adapter);
        } catch (NullPointerException e){
            Crashlytics.logException(e);
        } catch (Exception e){
            Crashlytics.logException(e);
        } catch (Throwable e){
            Crashlytics.logException(e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (firebaseUser != null) {
            new Function.IsOffline().execute();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (firebaseUser != null) {
            new Function.IsOnline().execute();
        }
    }
}
