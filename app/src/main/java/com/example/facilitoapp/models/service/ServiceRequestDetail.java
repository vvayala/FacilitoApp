package com.example.facilitoapp.models.service;

import com.example.facilitoapp.models.user.User;
import com.google.gson.annotations.SerializedName;

public class ServiceRequestDetail {

    @SerializedName("_id")
    private String id;

    @SerializedName("customer_id")
    private User customerId;

    @SerializedName("service_id")
    private Service serviceId;

    @SerializedName("service_request_date")
    private String serviceRequestDate;

    @SerializedName("service_request_description")
    private String serviceRequestDescription;

    public String getId() { return id; }
    public User getCustomerId() { return customerId; }
    public Service getServiceId() { return serviceId; }
    public String getServiceRequestDate() { return serviceRequestDate; }
    public String getServiceRequestDescription() { return serviceRequestDescription; }
    public void setId(String id) { this.id = id; }
}
