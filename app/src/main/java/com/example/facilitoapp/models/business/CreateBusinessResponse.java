package com.example.facilitoapp.models.business;

import com.google.gson.annotations.SerializedName;

public class CreateBusinessResponse {
    @SerializedName("ok")
    private boolean ok;
    @SerializedName("message")
    private String message;
    @SerializedName("business")
    private Business business;

    public boolean isOk() { return ok; }
    public Business getBusiness() { return business; }
}
