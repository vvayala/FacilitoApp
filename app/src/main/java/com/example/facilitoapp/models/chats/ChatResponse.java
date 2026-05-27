package com.example.facilitoapp.models.chats;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatResponse {
    @SerializedName("ok")
    private boolean success;
    @SerializedName("messages")
    private List<ChatMessages> messages;

        public boolean isSuccess() { return success; }
        public List<ChatMessages> getMessages() { return messages; }

}
