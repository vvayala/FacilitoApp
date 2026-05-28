package com.example.facilitoapp.network.services;

import com.example.facilitoapp.models.catalogs.CategoryResponse;
import com.example.facilitoapp.models.catalogs.RoleReponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CatalogApiService {
    @GET("users/roles/catalog")
    Call<RoleReponse> getRoles();

    @GET("services/categories")
    Call<CategoryResponse> getCategories();
}
