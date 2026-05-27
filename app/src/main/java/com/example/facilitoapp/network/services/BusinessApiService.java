package com.example.facilitoapp.network.services;

import com.example.facilitoapp.models.business.Business;
import com.example.facilitoapp.models.user.UserProfileResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BusinessApiService {

    @GET("business/{id}")
    Call<Business> getBussinessbyId(@Path("id") String businessId);

}
