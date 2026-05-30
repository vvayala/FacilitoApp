package com.example.facilitoapp.models.chats;

import com.example.facilitoapp.models.business.Business;
import com.example.facilitoapp.models.service.ServiceRequestDetail;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class Chat {

    @SerializedName("_id")
    private String id;

    @SerializedName("service_request_id")
    private JsonElement serviceRequestId;

    @SerializedName("business_id")
    private JsonElement businessId;

    public Chat() {}

    public Chat(String id, JsonElement serviceRequestId, JsonElement businessId) {
        this.id = id;
        this.serviceRequestId = serviceRequestId;
        this.businessId = businessId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public ServiceRequestDetail getServiceRequest() {
        if (serviceRequestId == null) return null;
        Gson gson = new Gson();

        if (serviceRequestId.isJsonPrimitive()) {
            ServiceRequestDetail sr = new ServiceRequestDetail();
            sr.setId(serviceRequestId.getAsString());
            return sr;
        } else if (serviceRequestId.isJsonObject()) {
            return gson.fromJson(serviceRequestId, ServiceRequestDetail.class);
        }
        return null;
    }

    public void setServiceRequest(JsonElement serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }

    public Business getBusiness() {
        if (businessId == null) return null;
        Gson gson = new Gson();

        if (businessId.isJsonPrimitive()) {
            Business b = new Business();
            b.setId(businessId.getAsString());
            return b;
        } else if (businessId.isJsonObject()) {
            return gson.fromJson(businessId, Business.class);
        }
        return null;
    }

    public void setBusiness(JsonElement businessId) {
        this.businessId = businessId;
    }
}
