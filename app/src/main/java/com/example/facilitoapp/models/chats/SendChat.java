package com.example.facilitoapp.models.chats;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SendChat {
    @SerializedName("chat_id")
    private String chatId;
    @SerializedName("sender_id")
    private String senderId;

    @SerializedName("message_content")
    private String messageContent;

    public SendChat() {
    }

    public SendChat(String messageContent, String senderId, String chatId) {
        this.messageContent = messageContent;
        this.senderId = senderId;
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
