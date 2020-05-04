package com.tdn.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.tdn.data.model.ContactModel;
import com.tdn.ui.R;
import com.tdn.ui.databinding.ItemContactListBinding;

import java.util.ArrayList;
import java.util.List;

public class AdapterContactList extends RecyclerView.Adapter<AdapterContactList.MyViewHolder> {
    private List<ContactModel> contactModelList = new ArrayList<>();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContactListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_contact_list,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return contactModelList == null ? 0 : contactModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemContactListBinding binding;
        public MyViewHolder(@NonNull ItemContactListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
