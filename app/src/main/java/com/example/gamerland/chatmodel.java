package com.example.gamerland;

public class chatmodel {
    private String chatName;
    private String chatDescription;

    public chatmodel() {
    }

    public chatmodel(String chatName, String chatDescription) {
        this.chatName = chatName;
        this.chatDescription = chatDescription;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatDescription() {
        return chatDescription;
    }

    public void setChatDescription(String chatDescription) {
        this.chatDescription = chatDescription;
    }

}
