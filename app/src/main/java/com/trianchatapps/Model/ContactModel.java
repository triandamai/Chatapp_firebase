package com.trianchatapps.Model;

public class ContactModel {
    public boolean friend;
    public long timestamp;
    public String name;
    public String friendsUid;

    public ContactModel() {

    }

    public ContactModel(boolean friend, long timestamp, String name, String friendsUid) {
        this.friend = friend;
        this.timestamp = timestamp;
        this.name = name;
        this.friendsUid = friendsUid;
    }

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFriendsUid() {
        return friendsUid;
    }

    public void setFriendsUid(String friendsUid) {
        this.friendsUid = friendsUid;
    }
}
