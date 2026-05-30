package com.example.facilitoapp.models.service;

import com.example.facilitoapp.models.business.Business;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class Service {

    @SerializedName("_id")
    private String id;

    @SerializedName("service_name")
    private String serviceName;

    @SerializedName("service_description")
    private String serviceDescription;

    @SerializedName("business_id")
    private JsonElement businessId;

    public String getId()                  { return id; }
    public void setId(String id)           { this.id = id; }

    public String getServiceName()                     { return serviceName; }
    public void setServiceName(String serviceName)     { this.serviceName = serviceName; }

    public String getServiceDescription()                          { return serviceDescription; }
    public void setServiceDescription(String serviceDescription)   { this.serviceDescription = serviceDescription; }

    public Business getBusiness() {
        if (businessId == null) return null;
        if (businessId.isJsonObject()) {
            return new Gson().fromJson(businessId, Business.class);
        }
        return null;
    }

    public String getBusinessId() {
        if (businessId == null) return null;
        if (businessId.isJsonPrimitive()) return businessId.getAsString();
        if (businessId.isJsonObject()) return businessId.getAsJsonObject().get("_id").getAsString();
        return null;
    }
}
