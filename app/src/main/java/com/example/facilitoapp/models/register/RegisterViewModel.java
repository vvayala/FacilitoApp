package com.example.facilitoapp.models.register;


import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {
    public MutableLiveData<String> name = new MutableLiveData<>("");
    public MutableLiveData<String> lastName = new MutableLiveData<>("");
    public MutableLiveData<String> telephone = new MutableLiveData<>("");
    public MutableLiveData<String> dui = new MutableLiveData<>("");
    public MutableLiveData<String> accountType = new MutableLiveData<>("");
    public MutableLiveData<String> email = new MutableLiveData<>("");
    public MutableLiveData<String> password = new MutableLiveData<>("");
    public MutableLiveData<String> businessName = new MutableLiveData<>("");
    public MutableLiveData<String> businessDescription = new MutableLiveData<>("");
    public MutableLiveData<Uri> businessImageUri = new MutableLiveData<>(null);
    public MutableLiveData<String> clientRoleId = new MutableLiveData<>("");
    public MutableLiveData<String> providerRoleId = new MutableLiveData<>("");
}
