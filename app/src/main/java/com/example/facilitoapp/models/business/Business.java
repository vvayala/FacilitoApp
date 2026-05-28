package com.example.facilitoapp.models.business;

import com.example.facilitoapp.models.user.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class Business {

    @SerializedName("_id")
    private String id;

    @SerializedName("business_name")
    private String businessName;

    @SerializedName("business_description")
    private String businessDescription;

    @SerializedName("business_pic_src")
    private String businessPicSrc;

    @SerializedName("user_id")
    private JsonElement userId;

    public Business() {}

    public Business(String id, String businessName, String businessDescription,
                    String businessPicSrc, JsonElement userId) {
        this.id = id;
        this.businessName = businessName;
        this.businessDescription = businessDescription;
        this.businessPicSrc = businessPicSrc;
        this.userId = userId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getBusinessDescription() { return businessDescription; }
    public void setBusinessDescription(String businessDescription) { this.businessDescription = businessDescription; }

    public String getBusinessPicSrc() { return businessPicSrc; }
    public void setBusinessPicSrc(String businessPicSrc) { this.businessPicSrc = businessPicSrc; }

    public User getUser() {
        if (userId == null) return null;

        Gson gson = new Gson();

        if (userId.isJsonPrimitive()) {
            String id = userId.getAsString();
            User u = new User();
            u.setId(id);
            u.setName(null);
            return u;
        } else if (userId.isJsonObject()) {
            return gson.fromJson(userId, User.class);
        }
        return null;
    }

    public void setUser(JsonElement userId) {
        this.userId = userId;
    }
}
