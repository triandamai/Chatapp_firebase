package com.trianchatapps.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trianchatapps.Function;
import com.trianchatapps.GlobalVariabel;
import com.trianchatapps.Helper.Bantuan;
import com.trianchatapps.Model.ContactModel;
import com.trianchatapps.Model.UserModel;
import com.trianchatapps.R;
import com.trianchatapps.Thread.ThreadChat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.Date;

public class Profil extends AppCompatActivity {

    @BindView(R.id.iv_profil_foto)
    CircleImageView ivProfilFoto;
    @BindView(R.id.et_profil_nama)
    TextView etProfilNama;
    @BindView(R.id.et_profil_email)
    TextView etProfilEmail;
    @BindView(R.id.btn_profil_simpan)
    Button btnProfilSimpan;
    @BindView(R.id.btn_profil_chat)
    Button btnProfilChat;

    public DatabaseReference databaseReference;
    public FirebaseUser firebaseUser;
    private Context context;
    private String UserId;
    private Intent intent;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        ButterKnife.bind(this);
        context = Profil.this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        intent = getIntent();

        if (intent != null) {
            UserId = intent.getStringExtra(GlobalVariabel.EXTRA_UID);
            data_user();

            if (intent.getStringExtra(GlobalVariabel.EXTRA_REQUEST).equals(GlobalVariabel.EXTRA_REQUEST)) {
                btnProfilChat.setText("Terima");
            } else if (intent.getStringExtra(GlobalVariabel.EXTRA_REQUEST) == null) {

                btnProfilChat.setText("Kirim pesan");
            } else {
                btnProfilChat.setText("Kirim pesan");
            }
        } else {
            new Bantuan(context).swal_basic("Tidak dapat memuat detail ");
            finish();
        }


    }

    @OnClick(R.id.btn_profil_chat)
    public void chat() {
        if (intent.getStringExtra(GlobalVariabel.EXTRA_REQUEST).equals(GlobalVariabel.EXTRA_REQUEST)) {
            tambah_kontak(intent.getStringExtra(GlobalVariabel.EXTRA_UID));
        } else {
            startActivity(new Intent(context, ThreadChat.class).putExtra(GlobalVariabel.EXTRA_UID, UserId));
            finish();
        }

    }

    private void tambah_kontak(final String stringExtra) {
        long timestamp = new Date().getTime();

        final ContactModel contactModel1 = new ContactModel();
        final ContactModel contactModel2 = new ContactModel();

        contactModel1.setFriend(true);
        contactModel1.setFriendsUid(firebaseUser.getUid());
        contactModel1.setName(".");
        contactModel1.setTimestamp(timestamp);

        contactModel2.setFriend(true);
        contactModel2.setFriendsUid(stringExtra);
        contactModel2.setName(".");
        contactModel2.setTimestamp(timestamp);

        databaseReference.child(GlobalVariabel.CHILD_CONTACT)
                .child(stringExtra)
                .child(GlobalVariabel.CHILD_CONTACT_FRIEND)
                .child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            new Bantuan(context).swal_basic("Permintaan pertemanan sudah dikirim");
                        } else {
                            databaseReference.child(GlobalVariabel.CHILD_CONTACT)
                                    .child(firebaseUser.getUid())
                                    .child(GlobalVariabel.CHILD_CONTACT_FRIEND)
                                    .child(stringExtra)
                                    .setValue(contactModel2);
                            databaseReference.child(GlobalVariabel.CHILD_CONTACT)
                                    .child(stringExtra)
                                    .child(GlobalVariabel.CHILD_CONTACT_FRIEND)
                                    .child(firebaseUser.getUid())
                                    .setValue(contactModel1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    public void data_user() {
        databaseReference.child(GlobalVariabel.CHILD_USER)
                .child(UserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserModel user;
                        if (dataSnapshot.exists()) {
                            user = dataSnapshot.getValue(UserModel.class);

                            etProfilNama.setText(user.getDisplayName());
                            etProfilEmail.setText(user.getEmail());
                            Glide.with(context)
                                    .load(user.getPhotoUrl())
                                    .into(ivProfilFoto);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
