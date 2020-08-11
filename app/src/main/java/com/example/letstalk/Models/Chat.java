package com.example.letstalk.Models;

public class Chat {
    String message, sender, receiver;
    boolean isSeen;

    public Chat() {

    }

    public Chat(String message, String sender, String receiver, boolean isSeen) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.isSeen = isSeen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
