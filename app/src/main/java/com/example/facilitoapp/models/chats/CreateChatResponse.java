package com.example.facilitoapp.models.chats;

import com.google.gson.annotations.SerializedName;

public class CreateChatResponse {

    @SerializedName("ok")
    private boolean ok;

    @SerializedName("chat")
    private Chat chat;

    public boolean isOk()    { return ok; }
    public Chat getChat()    { return chat; }
}
