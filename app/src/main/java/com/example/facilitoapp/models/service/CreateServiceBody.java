package com.example.facilitoapp.models.service;

import com.google.gson.annotations.SerializedName;

public class CreateServiceBody {
    @SerializedName("service_name")
    private String serviceName;

    @SerializedName("service_description")
    private String serviceDescription;

    @SerializedName("business_id")
    private String businessId;

    public CreateServiceBody(String serviceName, String serviceDescription, String businessId) {
        this.serviceName        = serviceName;
        this.serviceDescription = serviceDescription;
        this.businessId         = businessId;
    }
}