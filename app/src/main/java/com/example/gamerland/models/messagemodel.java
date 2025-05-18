package com.example.gamerland.models;

public class messagemodel {
    private String text;
    private String senderUsername;
    private String tvImvAvatar;
    private String messageId;
    private long timestamp;

    public messagemodel() {} // נדרש על ידי Firestore

    public messagemodel(String text, String senderUsername, String tvImvAvatar, long timestamp) {
        this.text = text;
        this.senderUsername = senderUsername;
        this.tvImvAvatar = tvImvAvatar;
        this.timestamp = timestamp;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getTvImvAvatar() {
        return tvImvAvatar;
    }

    public void setTvImvAvatar(String tvImvAvatar) {
        this.tvImvAvatar = tvImvAvatar;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
