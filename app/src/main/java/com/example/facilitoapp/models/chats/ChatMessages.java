package com.example.facilitoapp.models.chats;

import com.example.facilitoapp.models.user.User;
import com.google.gson.annotations.SerializedName;

public class ChatMessages {

    @SerializedName("_id")
    private String id;
    @SerializedName("chat_id")
    private Chat chatId;
    @SerializedName("is_sent")
    private Boolean isSent;
    @SerializedName("is_read")
    private Boolean isRead;
    @SerializedName("sender_id")
    private User sender_id;
    @SerializedName("time_sent")
    private String timeSent;
    @SerializedName("message_content")
    private String messageContent;

    public ChatMessages() {
    }


    public ChatMessages(String id, Chat chatId, Boolean isSent, Boolean isRead, User sender_id, String timeSent, String messageContent) {
        this.id = id;
        this.chatId = chatId;
        this.isSent = isSent;
        this.isRead = isRead;
        this.sender_id = sender_id;
        this.timeSent = timeSent;
        this.messageContent = messageContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Chat getChatId() {
        return chatId;
    }

    public void setChatId(Chat chatId) {
        this.chatId = chatId;
    }

    public Boolean getSent() {
        return isSent;
    }

    public void setSent(Boolean sent) {
        isSent = sent;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public User getSender_id() {
        return sender_id;
    }

    public void setSender_id(User sender_id) {
        this.sender_id = sender_id;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
