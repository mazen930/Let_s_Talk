package com.example.letstalk.Notifications;

public class Data {
    String user, body, title, sent;
    int iconId;

    public Data(String user, String body, String title, String sent, int iconId) {
        this.user = user;
        this.body = body;
        this.title = title;
        this.sent = sent;
        this.iconId = iconId;
    }

    public Data() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
