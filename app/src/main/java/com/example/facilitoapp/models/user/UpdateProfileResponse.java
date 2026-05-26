package com.example.facilitoapp.models.user;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileResponse {
    @SerializedName("ok")
    private boolean ok;
    @SerializedName("message")
    private String message;
    @SerializedName("user")
    private User user;

    public boolean isOk() { return ok; }
    public String message() { return message; }
    public User user() { return user; }
}
