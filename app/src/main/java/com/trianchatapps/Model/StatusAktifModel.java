package com.trianchatapps.Model;

public class StatusAktifModel {
   public int online;
   public long timestamp;

    public StatusAktifModel(){

    }
    public StatusAktifModel(int online, long timestamp){
        this.online = online;
        this.timestamp = timestamp;
    }

    public int isOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
