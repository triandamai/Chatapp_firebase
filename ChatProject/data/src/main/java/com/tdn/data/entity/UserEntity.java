package com.tdn.data.entity;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserEntity {
    String idUser;

    String nama;
    String username;
    String profilImage;
    String status;
    String lat;
    String lon;
    long created_at;
    long updated_at;
}
