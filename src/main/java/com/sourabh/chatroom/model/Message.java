package com.sourabh.chatroom.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Message {

    @JSONField(name = "userName")
    private String userName;
    @JSONField(name = "message")
    private String message;
    @JSONField(name = "onlineCount")
    private int onlineCount = 0;
    @JSONField(name = "type")
    private Type type;

    public enum Type {
        ENTER,
        CHAT,
        LEAVE
    }

    public Message() {

    }

    public Message(String userName, String message, int onlineCount, Type type) {
        this.userName = userName;
        this.message = message;
        this.onlineCount = onlineCount;
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
