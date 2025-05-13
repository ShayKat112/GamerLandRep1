package com.example.gamerland;

public class chatmodel {
    private String chatName;
    private String chatDescription;
    private String chatId;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

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
