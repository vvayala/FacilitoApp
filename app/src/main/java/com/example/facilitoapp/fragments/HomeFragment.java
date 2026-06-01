package com.example.facilitoapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facilitoapp.MyServicesActivity;
import com.example.facilitoapp.R;
import com.example.facilitoapp.models.business.BusinessResponse;
import com.example.facilitoapp.models.catalogs.Role;
import com.example.facilitoapp.models.catalogs.RoleReponse;
import com.example.facilitoapp.network.ApiClient;
import com.example.facilitoapp.network.services.BusinessApiService;
import com.example.facilitoapp.network.services.CatalogApiService;
import com.example.facilitoapp.network.services.UserApiService;
import com.example.facilitoapp.services.NewServiceBottomSheet;
import com.example.facilitoapp.utils.FacilitoApp;
import com.example.facilitoapp.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private LinearLayout customerSection, providerSection;
    private MaterialButton btnMyRequests, btnCreateService, btnMyServices;
    private TextView txtGreeting, txtRoleBadge;
    private SessionManager sessionManager;
    private CatalogApiService catalogApiService;
    private String cachedBusinessId = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager  = new SessionManager(requireContext());
        catalogApiService  = ApiClient.getClient().create(CatalogApiService.class);

        txtGreeting      = view.findViewById(R.id.txtGreeting);
        txtRoleBadge     = view.findViewById(R.id.txtRoleBadge);
        customerSection  = view.findViewById(R.id.customerSection);
        providerSection  = view.findViewById(R.id.providerSection);
        btnMyRequests    = view.findViewById(R.id.btnMyRequests);
        btnCreateService = view.findViewById(R.id.btnCreateService);
        btnMyServices    = view.findViewById(R.id.btnMyServices);

        loadRoleAndSetupHome();
        setupListeners();
    }

    private void loadRoleAndSetupHome() {
        String savedRoleId = sessionManager.getUserRoleId();

        if (savedRoleId == null) {
            showCustomerSection();
            return;
        }

        catalogApiService.getRoles().enqueue(new Callback<RoleReponse>() {
            @Override
            public void onResponse(@NonNull Call<RoleReponse> call,
                                   @NonNull Response<RoleReponse> response) {
                if (!isAdded()) return;

                if (response.body() == null || !response.body().isOk()) {
                    showCustomerSection();
                    return;
                }

                String roleName = "Cliente";
                for (Role role : response.body().roles()) {
                    if (role.getId().equals(savedRoleId)) {
                        roleName = role.getUserRoleName();
                        break;
                    }
                }

                txtRoleBadge.setText(roleName);

                if ("Proveedor".equalsIgnoreCase(roleName)) {
                    showProviderSection();
                } else {
                    showCustomerSection();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RoleReponse> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                showCustomerSection();
            }
        });
    }

    private void showCustomerSection() {
        customerSection.setVisibility(View.VISIBLE);
        providerSection.setVisibility(View.GONE);
    }

    private void showProviderSection() {
        customerSection.setVisibility(View.GONE);
        providerSection.setVisibility(View.VISIBLE);
    }

    private void setupListeners() {
        btnMyRequests.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new MyRequestsFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnCreateService.setOnClickListener(v -> {
            FacilitoApp.playClick();
            openCreateServiceFromHome();
        });

        btnMyServices.setOnClickListener(v -> {
            FacilitoApp.playClick();
            startActivity(new Intent(requireContext(), MyServicesActivity.class));
        });
    }

    private void openCreateServiceFromHome() {
        if (cachedBusinessId != null) {
            launchServiceSheet(cachedBusinessId);
            return;
        }

        BusinessApiService businessApi = ApiClient.getClient().create(BusinessApiService.class);
        businessApi.getBusinessByUser(sessionManager.getUserId())
                .enqueue(new Callback<BusinessResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<BusinessResponse> call,
                                           @NonNull Response<BusinessResponse> response) {
                        if (!isAdded()) return;
                        if (response.isSuccessful() && response.body() != null
                                && response.body().isOk()
                                && !response.body().getBusinesses().isEmpty()) {
                            cachedBusinessId = response.body().getBusinesses().get(0).getId();
                            launchServiceSheet(cachedBusinessId);
                        } else {
                            Toast.makeText(requireContext(),
                                    "No se encontró tu negocio", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BusinessResponse> call, @NonNull Throwable t) {
                        if (!isAdded()) return;
                        Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void launchServiceSheet(String businessId) {
        NewServiceBottomSheet sheet = NewServiceBottomSheet.newInstance(businessId);
        sheet.setOnServiceCreatedListener(() ->
                Toast.makeText(requireContext(), "Servicio creado", Toast.LENGTH_SHORT).show());
        sheet.show(getParentFragmentManager(), "NewServiceSheet");
    }
}