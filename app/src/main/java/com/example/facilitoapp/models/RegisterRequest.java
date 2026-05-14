package com.example.facilitoapp.models;

public class RegisterRequest {
    private String name;
    private String last_name;
    private String telephone;
    private String dui;
    private String email;
    private String password;
    private String user_role_id;

    public RegisterRequest(String name, String last_name, String telephone, String dui,
                          String email, String password, String user_role_id) {
        this.name = name;
        this.last_name = last_name;
        this.telephone = telephone;
        this.dui = dui;
        this.email = email;
        this.password = password;
        this.user_role_id = user_role_id;
    }

    public String getName() { return name; }
    public String getLast_name() { return last_name; }
    public String getTelephone() { return telephone; }
    public String getDui() { return dui; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getUser_role_id() { return user_role_id; }
}