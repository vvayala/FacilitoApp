package com.example.facilitoapp.models.service;

import com.google.gson.annotations.SerializedName;

public class CreateServiceRequestBody {

    @SerializedName("customer_id")
    private String customerId;

    @SerializedName("service_id")
    private String serviceId;

    @SerializedName("service_request_description")
    private String serviceRequestDescription;

    public CreateServiceRequestBody(String customerId, String serviceId, String description) {
        this.customerId = customerId;
        this.serviceId  = serviceId;
        this.serviceRequestDescription = description;
    }
}