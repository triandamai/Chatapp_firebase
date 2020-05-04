package com.tdn.data.entity;

import com.google.firebase.database.IgnoreExtraProperties;

import io.realm.annotations.PrimaryKey;

@IgnoreExtraProperties
public class ChatEntity {
    String idChat;
    String from;
    String to;
    int type;
    String body;
    String media;
    String status;
    long created_at;
    long updated_at;
}
