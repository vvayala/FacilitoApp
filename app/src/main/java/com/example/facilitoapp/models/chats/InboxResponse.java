package com.example.facilitoapp.models.chats;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InboxResponse {

    @SerializedName("ok")
    private boolean success;
    @SerializedName("chats")
    private List<Chat> inbox;


    public boolean isSuccess() { return success; }
    public List<Chat> getInbox() { return inbox; }
}
