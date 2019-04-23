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
import com.trianchatapps.Auth.Register;
import com.trianchatapps.GlobalVariabel;
import com.trianchatapps.Model.UserModel;
import com.trianchatapps.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfil extends AppCompatActivity {

    @BindView(R.id.iv_profil_foto)
    CircleImageView ivProfilFoto;
    @BindView(R.id.et_profil_nama)
    EditText etProfilNama;
    @BindView(R.id.et_profil_email)
    EditText etProfilEmail;
    @BindView(R.id.btn_profil_simpan)
    Button btnProfilSimpan;
    @BindView(R.id.btn_myprofil_logout)
    Button btnMyprofilLogout;

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profil);
        ButterKnife.bind(this);
        context = MyProfil.this;

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            data_user();
        }else {
            startActivity(new Intent(context, Register.class));
            finish();
        }
    }

    private void data_user() {
        databaseReference.child(GlobalVariabel.CHILD_USER)
                .child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            UserModel user;

                            user = dataSnapshot.getValue(UserModel.class);

                            Glide.with(context)
                                    .load(user.getPhotoUrl())
                                    .placeholder(R.drawable.undraw_jason_mask_t07o)
                                    .into(ivProfilFoto);
                            etProfilEmail.setText(user.getEmail());
                            etProfilNama.setText(user.getDisplayName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    @OnClick(R.id.btn_myprofil_logout)
    public  void logout(){
        if (firebaseUser != null){
            //logout

            FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(context, Register.class));
            finish();
        }else {
            // langsing ke resgister
            startActivity(new Intent(context, Register.class));
            finish();
        }
    }



}
