package com.example.facilitoapp.network.services;

import com.example.facilitoapp.models.service.ServiceResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServicesApiService {
    @GET("services")
    Call<ServiceResponse> getAllServices();
}
