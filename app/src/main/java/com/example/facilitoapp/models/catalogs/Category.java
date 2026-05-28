package com.example.facilitoapp.models.catalogs;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("_id")
    private String id;

    @SerializedName("service_category_name")
    private String categoryName;

    public Category(String id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public String getId() { return id; }
    public String getCategoryName() { return categoryName; }
}
