package com.trianchatapps.Model;

/*
 * Author Trian on 8/12/2018.
 */

public class User {

    private String displayName;
    private String current;
    private String email;
    private String uid;
    private String photoUrl;
    private String instanceId;
    private boolean online;



    public User() {
    }

    public User(String displayName, String email, String uid, String photoUrl,boolean online) {
        this.displayName = displayName;
        this.email = email;
        this.uid = uid;
        this.photoUrl = photoUrl;
        this.online = online;

    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
