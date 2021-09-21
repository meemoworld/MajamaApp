package com.memoworld.majama.AllModals;

public class MessageModel {
    String type , message , sender;
    Long timeStamp;

    public MessageModel(String type, String message, String sender, Long timeStamp) {
        this.type = type;
        this.message = message;
        this.sender = sender;
        this.timeStamp = timeStamp;
    }

    public MessageModel(){}

    public String getSender() {
        return sender;
    }

    public void getSender(String sender) {
        this.sender = sender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
