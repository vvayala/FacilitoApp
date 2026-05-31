package com.example.facilitoapp.models.user;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileRequest {
    @SerializedName("name")
    private String name;

    @SerializedName("last_name")
    private String lastname;

    @SerializedName("dui")
    private String dui;

    // TODO: Implement edit text to update email
//    @SerializedName("email")
//    private String email;

    @SerializedName("telephone")
    private String telephone;

    public UpdateProfileRequest(String name, String lastname, String dui, String telephone) {
        this.name = name;
        this.lastname = lastname;
        this.dui = dui;
        this.telephone = telephone;
    }
}
