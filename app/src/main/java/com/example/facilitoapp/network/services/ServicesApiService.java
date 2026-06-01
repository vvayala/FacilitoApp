package com.example.facilitoapp.network.services;

import com.example.facilitoapp.models.service.CreateServiceBody;
import com.example.facilitoapp.models.service.CreateServiceResponse;
import com.example.facilitoapp.models.service.ServiceResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServicesApiService {
    @GET("services")
    Call<ServiceResponse> getAllServices();

    @GET("services")
    Call<ServiceResponse> getServicesByUser(@Query("user_id") String userId);

    @POST("services")
    Call<CreateServiceResponse> createService(@Body CreateServiceBody body);
}
