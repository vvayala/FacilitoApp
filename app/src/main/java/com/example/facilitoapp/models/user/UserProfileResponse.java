package com.example.facilitoapp.models.user;

import com.google.gson.annotations.SerializedName;

public class UserProfileResponse {
    @SerializedName("ok")
    private boolean ok;
    @SerializedName("user")
    private User user;

    public boolean isOk() { return ok; }
    public User user() { return user; }
}
