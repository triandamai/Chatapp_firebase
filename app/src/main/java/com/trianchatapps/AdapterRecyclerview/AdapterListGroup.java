package com.trianchatapps.AdapterRecyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.trianchatapps.Helper.Bantuan;
import com.trianchatapps.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterListGroup extends RecyclerView.Adapter<AdapterListGroup.MyViewHolder> {

    public ArrayList<String> list;
    public Context context;

    public AdapterListGroup(Context context, ArrayList<String> listkontak) {
        this.context = context;
        this.list = listkontak;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, viewGroup, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final String id = list.get(i);
        myViewHolder.tvNama.setText(id);
        myViewHolder.ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Bantuan(context).alertDialogPeringatan("klik "+id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Context context;
        @BindView(R.id.iv_user)
        ImageView ivUser;
        @BindView(R.id.tv_nama)
        TextView tvNama;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
