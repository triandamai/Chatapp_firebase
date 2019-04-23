package com.trianchatapps.Model;

/*
 * Author Trian on 8/12/2018.
 */

public class MessageModel {

    private long timestamp;
    private long negatedTimestamp;
    private long dayTimestamp;
    private String body;
    private String from;
    private String to;
    private int tick;

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
