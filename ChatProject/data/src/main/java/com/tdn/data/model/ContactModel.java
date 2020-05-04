package com.tdn.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ContactModel extends RealmObject {
    @PrimaryKey
    String idContact;

    String idUser;
    String idFriend;
    String detail;
    long created_at;
    long updated_at;

}
