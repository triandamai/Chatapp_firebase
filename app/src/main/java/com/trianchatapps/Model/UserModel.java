package com.trianchatapps.Model;

/*
 * Author Trian on 8/12/2018.
 */

import android.net.Uri;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserModel {

    public String displayName;
    public String email;
    public String uid;
    public String photoUrl;
    public String instanceId;




    public UserModel() {
    }

    public UserModel(String displayName, String email, String uid, String photoUrl) {
        this.displayName = displayName;
        this.email = email;
        this.uid = uid;
        this.photoUrl = photoUrl;


    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
