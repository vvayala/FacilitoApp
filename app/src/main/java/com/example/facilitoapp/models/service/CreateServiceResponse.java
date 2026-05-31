package com.example.facilitoapp.models.service;

import com.google.gson.annotations.SerializedName;

public class CreateServiceResponse {
    @SerializedName("ok")
    private boolean ok;

    @SerializedName("message")
    private String message;

    @SerializedName("service")
    private Service service;

    public boolean isOk()      { return ok; }
    public String getMessage() { return message; }
    public Service getService(){ return service; }
}