package com.example.facilitoapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.facilitoapp.fragments.PersonalDataFragment;
import com.example.facilitoapp.models.catalogs.Role;
import com.example.facilitoapp.models.catalogs.RoleReponse;
import com.example.facilitoapp.models.register.RegisterViewModel;
import com.example.facilitoapp.network.ApiClient;
import com.example.facilitoapp.network.services.CatalogApiService;
import com.example.facilitoapp.network.services.UserApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterHostingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_hosting);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PersonalDataFragment())
                    .commit();
        }

        fetchRoles();
    }

    private void fetchRoles() {
        RegisterViewModel viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        CatalogApiService catalogApiService = ApiClient.getClient().create(CatalogApiService.class);
        catalogApiService.getRoles().enqueue(new Callback<RoleReponse>() {
            @Override
            public void onResponse(Call<RoleReponse> call, Response<RoleReponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    for (Role role : response.body().roles) {
                        if (role.getUserRoleName().equalsIgnoreCase("Cliente")) {
                            viewModel.clientRoleId.setValue(role.getId());
                        } else if (role.getUserRoleName().equalsIgnoreCase("Proveedor")) {
                            viewModel.providerRoleId.setValue(role.getId());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RoleReponse> call, Throwable t) {
                Toast.makeText(RegisterHostingActivity.this,
                        "Could not load roles. Please restart the app.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}