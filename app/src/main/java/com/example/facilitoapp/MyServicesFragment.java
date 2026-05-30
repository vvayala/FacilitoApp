package com.example.facilitoapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.facilitoapp.adapters.ServicesAdapter;
import com.example.facilitoapp.models.business.BusinessResponse;
import com.example.facilitoapp.models.service.Service;
import com.example.facilitoapp.models.service.ServiceResponse;
import com.example.facilitoapp.network.ApiClient;
import com.example.facilitoapp.network.services.BusinessApiService;
import com.example.facilitoapp.network.services.ServicesApiService;
import com.example.facilitoapp.services.NewServiceBottomSheet;
import com.example.facilitoapp.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyServicesFragment extends Fragment {

    private RecyclerView recyclerServices;
    private ProgressBar loadingServices;
    private LinearLayout emptyServicesState;
    private MaterialButton btnEmptyCreateService;
    private FloatingActionButton fabCreateService;

    private ServicesAdapter adapter;

    private ServicesApiService serviceApiService;
    private BusinessApiService businessApiService;
    private SessionManager sessionManager;

    private String cachedBusinessId = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_services, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager     = new SessionManager(requireContext());
        serviceApiService  = ApiClient.getClient().create(ServicesApiService.class);
        businessApiService = ApiClient.getClient().create(BusinessApiService.class);

        recyclerServices    = view.findViewById(R.id.recyclerServices);
        loadingServices     = view.findViewById(R.id.loadingServices);
        emptyServicesState  = view.findViewById(R.id.emptyServicesState);
        btnEmptyCreateService = view.findViewById(R.id.btnEmptyCreateService);
        fabCreateService    = view.findViewById(R.id.fabCreateService);

        adapter = new ServicesAdapter(service -> {
            Toast.makeText(requireContext(), service.getServiceName(), Toast.LENGTH_SHORT).show();
        });
        recyclerServices.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerServices.setAdapter(adapter);

        fabCreateService.setOnClickListener(v -> openCreateServiceSheet());
        btnEmptyCreateService.setOnClickListener(v -> openCreateServiceSheet());

        loadServices();
    }

    private void loadServices() {
        showLoading();
        String userId = sessionManager.getUserId();

        serviceApiService.getServicesByUser(userId).enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServiceResponse> call,
                                   @NonNull Response<ServiceResponse> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null
                        && response.body().isOk()) {
                    List<Service> data = response.body().getServices();
                    if (data != null) adapter.setServices(data);
                    else adapter.setServices(new ArrayList<>());

                    if (data == null || data.isEmpty()) showEmpty();
                    else showList();
                } else {
                    showEmpty();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServiceResponse> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                Log.e("MyServicesFragment", "onFailure: " + t.getMessage());
                showEmpty();
            }
        });
    }

    private void openCreateServiceSheet() {
        if (cachedBusinessId != null) {
            launchSheet(cachedBusinessId);
            return;
        }

        String userId = sessionManager.getUserId();
        businessApiService.getBusinessByUser(userId).enqueue(new Callback<BusinessResponse>() {
            @Override
            public void onResponse(@NonNull Call<BusinessResponse> call,
                                   @NonNull Response<BusinessResponse> response) {
                if (!isAdded()) return;

                Log.d("BusinessFetch", "code: " + response.code());
                Log.d("BusinessFetch", "body null: " + (response.body() == null));
                if (response.body() != null) {
                    Log.d("BusinessFetch", "ok: " + response.body().isOk());
                    Log.d("BusinessFetch", "businesses: " + response.body().getBusinesses());
                }

                if (response.isSuccessful() && response.body() != null
                        && response.body().isOk()
                        && !response.body().getBusinesses().isEmpty()) {

                    cachedBusinessId = response.body().getBusinesses().get(0).getId();
                    launchSheet(cachedBusinessId);
                } else {
                    Toast.makeText(requireContext(),
                            "No se encontró tu negocio", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BusinessResponse> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(),
                        "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void launchSheet(String businessId) {
        NewServiceBottomSheet sheet = NewServiceBottomSheet.newInstance(businessId);
        sheet.setOnServiceCreatedListener(() -> loadServices());
        sheet.show(getParentFragmentManager(), "NewServiceBottomSheet");
    }

    private void showLoading() {
        loadingServices.setVisibility(View.VISIBLE);
        emptyServicesState.setVisibility(View.GONE);
        recyclerServices.setVisibility(View.GONE);
    }

    private void showEmpty() {
        loadingServices.setVisibility(View.GONE);
        emptyServicesState.setVisibility(View.VISIBLE);
        recyclerServices.setVisibility(View.GONE);
    }

    private void showList() {
        loadingServices.setVisibility(View.GONE);
        emptyServicesState.setVisibility(View.GONE);
        recyclerServices.setVisibility(View.VISIBLE);
    }
}