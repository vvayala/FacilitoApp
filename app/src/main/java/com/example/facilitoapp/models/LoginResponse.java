package com.example.facilitoapp.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("ok")
    private boolean ok;
    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private User user;

    public boolean isOk() { return ok; }
    public String getMessage() { return message; }
    public User getUser() { return user; }
}
