package com.tdn.ui.view.ui.ChatScreen;

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
import com.tdn.ui.R;
import com.tdn.ui.databinding.FragmentChatScreenBinding;
import com.tdn.viewmodel.ChatScreenViewModel;

import java.util.List;


public class ChatScreen extends Fragment {
    private FragmentChatScreenBinding binding;
    private ChatScreenViewModel chatScreenViewModel;
    private String idSender = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_chat_screen, container, false);
        binding.setIsLoading(true);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getViewModel();
        observe(chatScreenViewModel);
    }

    @Override
    public void onResume() {
        super.onResume();
        observe(chatScreenViewModel);
    }

    public void getViewModel(){
        chatScreenViewModel = new ViewModelProvider(requireActivity(),new ChatScreenViewModel.ChatListViewModelFactory(idSender)).get(ChatScreenViewModel.class);


     }

    private void observe(ChatScreenViewModel chatScreenViewModel) {
        chatScreenViewModel.getChat().observe(getViewLifecycleOwner(), new Observer<List<ChatModel>>() {
            @Override
            public void onChanged(List<ChatModel> chatModels) {

            }
        });
    }
}
