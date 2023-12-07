package com.databit.contactmoviltwo.model;

public class MessageItem {

    private String sender;
    private String message;

    public MessageItem() {

    }

    public MessageItem(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
