package com.example.facilitoapp.models.catalogs;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RoleReponse {
    @SerializedName("ok")
    private boolean ok;

    @SerializedName("roles")
    public List<Role> roles = new ArrayList<>();

    public boolean isOk() { return ok; }
    public List<Role> roles() { return roles; }
}
