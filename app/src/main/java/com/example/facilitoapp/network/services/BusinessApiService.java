package com.example.facilitoapp.network.services;

import com.example.facilitoapp.models.business.BusinessResponse;
import com.example.facilitoapp.models.business.CreateBusinessBody;
import com.example.facilitoapp.models.business.CreateBusinessResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BusinessApiService {

    @GET("business")
    Call<BusinessResponse> getAllBusinesses();

    @GET("business")
    Call<BusinessResponse> getBusinessByUser(@Query("user_id") String userId);

    @POST("business")
    Call<CreateBusinessResponse> createBusiness(@Body CreateBusinessBody body);
}
