package com.trianchatapps.Model;

public class ContactModel {
   public boolean isFriend;
   public long timestamp;
   public String name;
   public String friendsUid;

    public ContactModel() {
    }

    public ContactModel(boolean isFriend,
                        long timestap,
                        String name,
                        String friedsUid) {
        this.isFriend = isFriend;
        this.timestamp = timestap;
        this.name = name;
        this.friendsUid = friedsUid;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
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
