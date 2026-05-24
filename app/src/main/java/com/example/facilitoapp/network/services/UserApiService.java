package com.example.facilitoapp.network.services;

import com.example.facilitoapp.models.user.LoginRequest;
import com.example.facilitoapp.models.user.LoginResponse;
import com.example.facilitoapp.models.user.RegisterRequest;
import com.example.facilitoapp.models.user.User;
import com.example.facilitoapp.models.user.UserProfileResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Path;

public interface UserApiService {

    // Registrar un nuevo usuario
    @POST("auth/register")
    Call<LoginResponse> registerUser(@Body RegisterRequest request);

    // Login de usuario
    @POST("auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);

    @GET("users/{id}")
    Call<UserProfileResponse> getUserById(@Path("id") String userId);
}
