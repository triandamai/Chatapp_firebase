package com.tdn.data.mapper.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tdn.data.repository.firebase.FirebaseRepository;

import java.util.List;

public class BaseValueEventListener<Model, Entity> implements ValueEventListener {
    private  FirebaseMapper<Entity,Model> mapper;
    private FirebaseRepository.FirebaseDatabaseRepositoryCallback callback;

    public BaseValueEventListener(FirebaseMapper<Entity,Model> mapper,
                                  FirebaseRepository.FirebaseDatabaseRepositoryCallback callback){
        this.mapper = mapper;
        this.callback = callback;
    }
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        List<Model> data = mapper.mapList(dataSnapshot);
        callback.onSuccess(data);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        callback.onError(databaseError.toException());
    }
}
