package com.tdn.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ChatModel extends RealmObject {
    @PrimaryKey
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
