package com.tdn.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tdn.data.mapper.realm.RealmLiveResult;
import com.tdn.data.model.ChatModel;
import com.tdn.data.model.ContactModel;
import com.tdn.data.model.UserModel;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

public class ContactListViewModel extends ViewModel {
    private final Realm realm;
    private final LiveData<List<UserModel>> chatlist ;

    public ContactListViewModel(){
        realm = Realm.getDefaultInstance();
        chatlist =  new RealmLiveResult(realm.where(UserModel.class).sort("created_at", Sort.ASCENDING) .distinct("sender").findAllAsync());

    }
    public LiveData<List<UserModel>> getContactlist(){
        return chatlist;
    }
}
