package com.example.facilitoapp.models.user;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("telephone")
    private String telephone;

    @SerializedName("dui")
    private String dui;

    @SerializedName("email")
    private String email;

    @SerializedName("user_role_id")
    private String roleId;

    public User() {}

    public User(String id, String name, String lastName, String telephone,
                String dui, String email, String roleId) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.telephone = telephone;
        this.dui = dui;
        this.email = email;
        this.roleId = roleId;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getDui() { return dui; }
    public void setDui(String dui) { this.dui = dui; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }

}
