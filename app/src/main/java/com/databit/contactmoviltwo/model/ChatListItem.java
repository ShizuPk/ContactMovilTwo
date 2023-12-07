package com.databit.contactmoviltwo.model;

public class ChatListItem {

    private String sender;
    private String lastMessage;

    public ChatListItem(String sender, String lastMessage) {
        this.sender = sender;
        this.lastMessage = lastMessage;
    }

    public String getSender() {
        return sender;
    }

    public String getLastMessage() {
        return lastMessage;
    }
}
