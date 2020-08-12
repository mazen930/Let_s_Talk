package com.example.letstalk.Notifications;

public class Sender {
    Data notification;// Thanks to comments below the video this name should be notification no thing else in order to work
    String to;

    public Sender(Data notification, String to) {
        this.notification = notification;
        this.to = to;
    }

    public Sender() {
    }
}
