package com.example.facilitoapp.models.catalogs;

import com.google.gson.annotations.SerializedName;

public class Role {
    @SerializedName("_id")
    private String id;

    @SerializedName("user_role_name")
    private  String userRoleName;

    public Role(String id, String userRoleName) {
        this.id = id;
        this.userRoleName = userRoleName;
    }

    public String getId() { return id; }
    public String getUserRoleName() { return userRoleName; }
}
