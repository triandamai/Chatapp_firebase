package com.tdn.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.tdn.data.model.ChatModel;
import com.tdn.data.model.ContactModel;
import com.tdn.ui.R;
import com.tdn.ui.databinding.ItemChat1Binding;
import com.tdn.ui.databinding.ItemChatListBinding;

import java.util.ArrayList;
import java.util.List;

public class AdapterChatScreen extends RecyclerView.Adapter<AdapterChatScreen.MyViewHolder> {
    private List<ChatModel> chatModelList = new ArrayList<>();
    private ContactModel Sender = null;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChat1Binding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_1,parent,false);
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return chatModelList == null ? 0 : chatModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ItemChat1Binding binding;
        public MyViewHolder(@NonNull ItemChat1Binding itemView) {

            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
