package com.example.facilitoapp.models.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceRequestResponse {

    @SerializedName("ok")
    private boolean ok;

    @SerializedName("message")
    private String message;

    @SerializedName("request")
    private ServiceRequestDetail request; // POST — service_id.business_id es String

    @SerializedName("requests")
    private List<ServiceRequestDetail> requests; // GET — service_id.business_id es objeto

    public boolean isOk() { return ok; }
    public String getMessage() { return message; }
    public ServiceRequestDetail getRequest() { return request; }
    public List<ServiceRequestDetail> getRequests() { return requests; }
}
