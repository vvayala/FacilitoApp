package com.example.facilitoapp.models.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceResponse {

    @SerializedName("ok")
    private boolean ok;

    @SerializedName("services")
    private List<Service> services;

    public boolean isOk() { return ok; }
    public List<Service> getServices() { return services; }
}
