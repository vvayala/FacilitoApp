package com.example.facilitoapp.models.service;

import com.example.facilitoapp.models.business.Business;
import com.google.gson.annotations.SerializedName;

public class Service {

    @SerializedName("_id")
    private String id;

    @SerializedName("service_name")
    private String serviceName;

    @SerializedName("service_description")
    private String serviceDescription;

    @SerializedName("business_id")
    private Business businessId;

    public Service() {
    }

    public Service(String id, String serviceName, String serviceDescription, Business businessId) {
        this.id = id;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.businessId = businessId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public Business getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Business businessId) {
        this.businessId = businessId;
    }
}
