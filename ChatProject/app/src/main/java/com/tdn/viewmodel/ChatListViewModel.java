package com.tdn.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.tdn.data.mapper.realm.RealmLiveResult;
import com.tdn.data.model.ChatModel;
import com.tdn.data.model.ContactModel;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

public class ChatListViewModel extends ViewModel {
    private final Realm realm;
    private final LiveData<List<ChatModel>> chatlist;

    public ChatListViewModel(){
        realm = Realm.getDefaultInstance();
        chatlist =  new RealmLiveResult(realm.where(ChatModel.class).sort("created_at", Sort.ASCENDING) .distinct("sender").findAllAsync());


    }
    public LiveData<List<ChatModel>> getChatlist(){
        return chatlist;
    }
}
