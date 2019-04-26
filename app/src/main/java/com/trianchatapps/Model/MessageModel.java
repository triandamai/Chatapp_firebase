package com.trianchatapps.Model;

import com.google.firebase.database.IgnoreExtraProperties;

/*
 * Author Trian on 8/12/2018.
 */
@IgnoreExtraProperties
public class MessageModel {

    public long timestamp;
    public long negatedTimestamp;
    public long dayTimestamp;
    public String body;
    public String from;
    public String to;
    public int tick;

    public MessageModel(long timestamp, long negatedTimestamp, long dayTimestamp, String body, String from, String to, int tick) {
        this.timestamp = timestamp;
        this.negatedTimestamp = negatedTimestamp;
        this.dayTimestamp = dayTimestamp;
        this.body = body;
        this.from = from;
        this.to = to;
        this.tick = tick;
    }

    public MessageModel() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getNegatedTimestamp() {
        return negatedTimestamp;
    }

    public String getTo() {
        return to;
    }

    public long getDayTimestamp() {
        return dayTimestamp;
    }

    public String getFrom() {
        return from;
    }

    public String getBody() {
        return body;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }
}
