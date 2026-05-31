package com.example.facilitoapp.network.services;

import com.example.facilitoapp.models.service.CreateServiceRequestBody;
import com.example.facilitoapp.models.service.ServiceRequestDetail;
import com.example.facilitoapp.models.service.ServiceRequestResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceRequestsApiService {

    @GET("requests")
    Call<ServiceRequestResponse> getRequestsByCustomer(@Query("customer_id") String customerId);

    @POST("requests")
    Call<ServiceRequestResponse> createRequest(@Body CreateServiceRequestBody request);
}
