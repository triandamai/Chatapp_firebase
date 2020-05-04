package com.tdn.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.tdn.data.mapper.realm.RealmLiveResult;
import com.tdn.data.model.ChatModel;

import java.util.List;

import io.realm.Realm;

public class ChatScreenViewModel extends ViewModel {
    private final Realm realm;
    private final LiveData<List<ChatModel>> chatlist ;
    public ChatScreenViewModel(String idUser){
        realm = Realm.getDefaultInstance();
        chatlist =  new RealmLiveResult(realm.where(ChatModel.class).equalTo("sender",idUser).findAllAsync());

    }
    public LiveData<List<ChatModel>> getChat(){
        return chatlist;
    }
    public static class ChatListViewModelFactory implements ViewModelProvider.Factory{

        private String param ;
        public ChatListViewModelFactory(String id){

            this.param = id;
        }


        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ChatScreenViewModel.class)) {
                return (T) new ChatScreenViewModel(param);
            } else {
                 throw new IllegalArgumentException("Unknown ViewModel class");
            }

        }
    }
}
