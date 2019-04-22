package com.trianchatapps.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.trianchatapps.AdapterRecyclerview.AdapterListContact;
import com.trianchatapps.Function;
import com.trianchatapps.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendRequest extends AppCompatActivity {

    @BindView(R.id.iv_kosong)
    ImageView ivKosong;
    @BindView(R.id.linear_kosong)
    LinearLayout linearKosong;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.linear_isi)
    LinearLayout linearIsi;

    AdapterListContact adapterListContact;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        context = FriendRequest.this;
        ButterKnife.bind(this);
        Function function = new Function();

      //  adapterListContact = new AdapterListContact(context,"",function.contact(""));
    }


}
