package com.example.facilitoapp.models;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface ApiService {

    // Registrar un nuevo usuario
    @POST("auth/register")
    Call<LoginResponse> registerUser(@Body User user);

    // Login de usuario
    @POST("auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);
}
