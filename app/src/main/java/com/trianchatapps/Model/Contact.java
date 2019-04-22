package com.trianchatapps.Model;

public class Contact {
    boolean isFriend;
    long timestap;
    String name, friedsUid ;

    public Contact() {
    }

    public Contact(boolean isFriend,
                   long timestap,
                   String name,
                   String friedsUid) {
        this.isFriend = isFriend;
        this.timestap = timestap;
        this.name = name;
        this.friedsUid = friedsUid;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public long getTimestap() {
        return timestap;
    }

    public void setTimestap(long timestap) {
        this.timestap = timestap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFriedsUid() {
        return friedsUid;
    }

    public void setFriedsUid(String friedsUid) {
        this.friedsUid = friedsUid;
    }
}
