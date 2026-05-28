package com.example.facilitoapp.models.chats;

import com.google.gson.annotations.SerializedName;

public class SentChat {
    @SerializedName("ok")
    private Boolean IsSent;
    @SerializedName("message")
    private String message;

    @SerializedName("chatMessage")
    private Chat chatMessages;

}
