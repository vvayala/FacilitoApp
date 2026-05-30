package com.example.facilitoapp.models.user;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("ok")
    private boolean ok;

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private User user;

    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("refreshToken")
    private String refreshToken;

    @SerializedName("tokenType")
    private String tokenType;

    public boolean isOk()            { return ok; }
    public String getMessage()       { return message; }
    public User getUser()            { return user; }
    public String getAccessToken()   { return accessToken; }
    public String getRefreshToken()  { return refreshToken; }
    public String getTokenType()     { return tokenType; }
}
