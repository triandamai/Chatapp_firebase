package com.trianchatapps.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trianchatapps.Model.User;
import com.trianchatapps.R;
import com.trianchatapps.Thread.ThreadChat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class Profil extends AppCompatActivity {

    @BindView(R.id.iv_profil_foto)
    CircleImageView ivProfilFoto;
    @BindView(R.id.et_profil_nama)
    EditText etProfilNama;
    @BindView(R.id.et_profil_email)
    EditText etProfilEmail;
    @BindView(R.id.btn_profil_simpan)
    Button btnProfilSimpan;
    @BindView(R.id.btn_profil_chat)
    Button btnProfilChat;

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    Context context;
    String UserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        ButterKnife.bind(this);
        context = Profil.this;

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();

        UserId = intent.getStringExtra("uid");
        data_user();


    }
    @OnClick(R.id.btn_profil_chat)
    public void chat(){
        startActivity(new Intent(context, ThreadChat.class).putExtra("uid", UserId));
        finish();
    }
    private void data_user() {
        databaseReference.child("USER")
                .child(UserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user;
                        if (dataSnapshot.exists()) {
                            user = dataSnapshot.getValue(User.class);

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
}
