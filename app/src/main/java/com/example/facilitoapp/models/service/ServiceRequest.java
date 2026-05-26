package com.example.facilitoapp.models.service;

import com.example.facilitoapp.models.business.Business;
import com.example.facilitoapp.models.user.User;
import com.google.gson.annotations.SerializedName;

public class ServiceRequest {

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

    public ServiceRequest() {
    }

    public ServiceRequest(String id, User customerId, Service serviceId, String serviceRequestDate, String serviceRequestDescription) {
        this.id = id;
        this.customerId = customerId;
        this.serviceId = serviceId;
        this.serviceRequestDate = serviceRequestDate;
        this.serviceRequestDescription = serviceRequestDescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getCustomerId() {
        return customerId;
    }

    public void setCustomerId(User customerId) {
        this.customerId = customerId;
    }

    public Service getServiceId() {
        return serviceId;
    }

    public void setServiceId(Service serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceRequestDate() {
        return serviceRequestDate;
    }

    public void setServiceRequestDate(String serviceRequestDate) {
        this.serviceRequestDate = serviceRequestDate;
    }

    public String getServiceRequestDescription() {
        return serviceRequestDescription;
    }

    public void setServiceRequestDescription(String serviceRequestDescription) {
        this.serviceRequestDescription = serviceRequestDescription;
    }
}
