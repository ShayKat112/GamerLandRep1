package com.example.gamerland;

public class Chat {
    private String chatId;
    private String chatName;
    private long lastMessageTimestamp;

    public Chat() {}  // דרוש ל-Firebase

    public Chat(String chatId, String chatName, long lastMessageTimestamp) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getChatId() { return chatId; }
    public String getChatName() { return chatName; }
    public long getLastMessageTimestamp() { return lastMessageTimestamp; }

    public void setChatId(String chatId) { this.chatId = chatId; }
    public void setChatName(String chatName) { this.chatName = chatName; }
    public void setLastMessageTimestamp(long lastMessageTimestamp) { this.lastMessageTimestamp = lastMessageTimestamp; }
}
