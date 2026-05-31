package com.example.facilitoapp.models.chats;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatsListResponse {
    @SerializedName("ok")
    private boolean ok;

    @SerializedName("chats")
    private List<Chat> chats;

    public boolean isOk() { return ok; }
    public List<Chat> getChats() { return chats; }
}
