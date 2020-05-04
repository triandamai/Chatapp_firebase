package com.tdn.data.entity;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ContactEntity {
    String idContact;
    String idUser;
    String idFriend;
    String detail;
    long created_at;
    long updated_at;
}
