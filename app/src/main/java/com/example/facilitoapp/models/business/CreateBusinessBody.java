package com.example.facilitoapp.models.business;


import com.google.gson.annotations.SerializedName;

public class CreateBusinessBody {

    @SerializedName("business_name")
    private String businessName;

    @SerializedName("business_description")
    private String businessDescription;

    @SerializedName("business_pic_src")
    private String businessPicSrc;

    @SerializedName("user_id")
    private String userId;

    public CreateBusinessBody(String businessName, String businessDescription, String businessPicSrc, String userId) {
        this.businessName = businessName;
        this.businessDescription = businessDescription;
        this.businessPicSrc = businessPicSrc;
        this.userId = userId;
    }

    // Getters
    public String getBusinessName() { return businessName; }
    public String getBusinessDescription() { return businessDescription; }
    public String getBusinessPicSrc() { return businessPicSrc; }
    public String getUserId() { return userId; }
}
