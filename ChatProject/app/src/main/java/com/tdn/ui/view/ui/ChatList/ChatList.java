package com.tdn.ui.view.ui.ChatList;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tdn.data.model.ChatModel;
import com.tdn.data.model.ContactModel;
import com.tdn.data.model.UserModel;
import com.tdn.ui.R;
import com.tdn.ui.databinding.FragmentChatListBinding;
import com.tdn.viewmodel.ChatListViewModel;
import com.tdn.viewmodel.ContactListViewModel;

import java.util.List;


public class ChatList extends Fragment {
   private FragmentChatListBinding binding;
   private ContactListViewModel contactListViewModel;
   private ChatListViewModel chatListViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       binding = DataBindingUtil.inflate(inflater,R.layout.fragment_chat_list, container, false);
       binding.setIsLoading(true);
       return binding.getRoot();


    }

    private void getViewModel() {
        chatListViewModel = new ViewModelProvider(requireActivity()).get(ChatListViewModel.class);
        contactListViewModel = new ViewModelProvider(requireActivity()).get(ContactListViewModel.class);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getViewModel();
        observe(chatListViewModel,contactListViewModel);
    }

    @Override
    public void onResume() {
        super.onResume();
        observe(chatListViewModel,contactListViewModel);
    }

    private void observe(ChatListViewModel chatListViewModel, ContactListViewModel contactListViewModel) {
        chatListViewModel.getChatlist().observe(getViewLifecycleOwner(), new Observer<List<ChatModel>>() {
            @Override
            public void onChanged(List<ChatModel> chatModels) {

            }
        });
        contactListViewModel.getContactlist().observe(getViewLifecycleOwner(), new Observer<List<UserModel>>() {
            @Override
            public void onChanged(List<UserModel> contactModels) {

            }
        });
    }

}
