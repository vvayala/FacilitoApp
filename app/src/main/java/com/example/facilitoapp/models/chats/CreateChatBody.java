package com.example.facilitoapp.models.chats;

import com.google.gson.annotations.SerializedName;

public class CreateChatBody {

    @SerializedName("service_request_id")
    private String serviceRequestId;

    @SerializedName("business_id")
    private String businessId;

    public CreateChatBody(String serviceRequestId, String businessId) {
        this.serviceRequestId = serviceRequestId;
        this.businessId = businessId;
    }
}