package com.example.facilitoapp.models.business;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BusinessResponse {
    @SerializedName("ok")
    private boolean ok;

    @SerializedName("businesses")
    private List<Business> businesses;

    public boolean isOk() { return ok; }
    public List<Business> getBusinesses() { return businesses; }
}
