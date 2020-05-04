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
import com.tdn.ui.databinding.ItemChatListBinding;

import java.util.ArrayList;
import java.util.List;

public class AdapterChatList extends RecyclerView.Adapter<AdapterChatList.MyViewHolder> {
    private List<ChatModel> chatModelList = new ArrayList<>();
    private List<ContactModel> contactModelList = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_list,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return chatModelList == null ? 0 : chatModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemChatListBinding binding;
        public MyViewHolder(@NonNull ItemChatListBinding bind) {
            super(bind.getRoot());

            binding = bind;
        }
    }
}
