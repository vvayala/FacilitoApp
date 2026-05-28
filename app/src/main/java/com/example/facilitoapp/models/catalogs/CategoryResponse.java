package com.example.facilitoapp.models.catalogs;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse {
    @SerializedName("ok")
    private boolean ok;

    @SerializedName("categories")
    private List<Category> categories;

    public boolean isOk() { return ok; }
    public List<Category> getCategories() { return categories; }
}
