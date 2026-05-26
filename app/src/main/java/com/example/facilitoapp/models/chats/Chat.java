package com.example.facilitoapp.models.chats;

import com.example.facilitoapp.models.business.Business;
import com.example.facilitoapp.models.service.Service;
import com.google.gson.annotations.SerializedName;

public class Chat {

    @SerializedName("_id")
    private String id;
    @SerializedName("service_request_id")
    private Service serviceRequestId;

    @SerializedName("businessId")
    private Business businessId;


    public Chat() {
    }

    public Chat(String id, Service serviceRequestId, Business businessId) {
        this.id = id;
        this.serviceRequestId = serviceRequestId;
        this.businessId = businessId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Service getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(Service serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }

    public Business getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Business businessId) {
        this.businessId = businessId;
    }
}
