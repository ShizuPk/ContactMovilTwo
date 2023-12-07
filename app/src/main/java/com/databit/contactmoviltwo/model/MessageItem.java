package com.databit.contactmoviltwo.model;

public class MessageItem {

    private String sender;
    private String message;


    public MessageItem(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
