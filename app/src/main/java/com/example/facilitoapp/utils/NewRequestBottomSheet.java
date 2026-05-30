package com.example.facilitoapp.utils;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facilitoapp.R;
import com.example.facilitoapp.adapters.ServicesAdapter;
import com.example.facilitoapp.interfaces.OnRequestCreatedListener;
import com.example.facilitoapp.models.business.Business;
import com.example.facilitoapp.models.service.CreateServiceRequestBody;
import com.example.facilitoapp.models.service.Service;
import com.example.facilitoapp.models.service.ServiceRequestDetail;
import com.example.facilitoapp.models.service.ServiceRequestResponse;
import com.example.facilitoapp.models.service.ServiceResponse;
import com.example.facilitoapp.network.ApiClient;
import com.example.facilitoapp.network.services.ServiceRequestsApiService;
import com.example.facilitoapp.network.services.ServicesApiService;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewRequestBottomSheet extends BottomSheetDialogFragment {

//    public interface OnRequestCreatedListener {
//        void onRequestCreated();
//    }

    private static final String ARG_CUSTOMER_ID = "customer_id";
    private OnRequestCreatedListener listener;
    private Service selectedService;

    // Views
    private LinearLayout stepSelectService, stepDescription;
    private ProgressBar loadingServices;
    private RecyclerView recyclerServices;
    private TextView txtSelectedService, txtSelectedBusiness;
    private TextInputLayout tilDescription;
    private TextInputEditText edtDescription;
    private Button btnSubmit;

    public static NewRequestBottomSheet newInstance(String customerId) {
        NewRequestBottomSheet sheet = new NewRequestBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_CUSTOMER_ID, customerId);
        sheet.setArguments(args);
        return sheet;
    }

    public void setOnRequestCreatedListener(OnRequestCreatedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_new_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        stepSelectService  = view.findViewById(R.id.stepSelectService);
        stepDescription    = view.findViewById(R.id.stepDescription);
        loadingServices    = view.findViewById(R.id.loadingServices);
        recyclerServices   = view.findViewById(R.id.recyclerServices);
        txtSelectedService = view.findViewById(R.id.txtSelectedService);
        txtSelectedBusiness= view.findViewById(R.id.txtSelectedBusiness);
        tilDescription     = view.findViewById(R.id.tilDescription);
        edtDescription     = view.findViewById(R.id.edtDescription);
        btnSubmit          = view.findViewById(R.id.btnSubmitRequest);

        ServicesAdapter servicesAdapter = new ServicesAdapter(this::onServiceSelected);
        recyclerServices.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerServices.setAdapter(servicesAdapter);

        view.findViewById(R.id.btnBack).setOnClickListener(v -> showStep(1));
        btnSubmit.setOnClickListener(v -> submitRequest());

        loadServices(servicesAdapter);
    }

    private void loadServices(ServicesAdapter adapter) {
        ApiClient.getClient()
                .create(ServicesApiService.class)
                .getServicesByUser("")
                .enqueue(new Callback<ServiceResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ServiceResponse> call,
                                           @NonNull Response<ServiceResponse> response) {
                        if (!isAdded()) return;
                        loadingServices.setVisibility(View.GONE);

                        if (!response.isSuccessful() || response.body() == null || !response.body().isOk()) return;

                        List<Service> list = response.body().getServices();
                        if (list != null && !list.isEmpty()) {
                            adapter.setServices(list);
                            recyclerServices.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ServiceResponse> call, @NonNull Throwable t) {
                        if (!isAdded()) return;
                        loadingServices.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), "Could not load services", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onServiceSelected(Service service) {
        selectedService = service;
        txtSelectedService.setText(service.getServiceName());

        Business business = service.getBusiness();
        if (business != null) {
            txtSelectedBusiness.setText(business.getBusinessName());
            txtSelectedBusiness.setVisibility(View.VISIBLE);
        } else {
            txtSelectedBusiness.setVisibility(View.GONE);
        }

        showStep(2);
    }

    private void showStep(int step) {
        stepSelectService.setVisibility(step == 1 ? View.VISIBLE : View.GONE);
        stepDescription.setVisibility(step == 2 ? View.VISIBLE : View.GONE);
    }

    private void submitRequest() {
        String description = edtDescription.getText() != null
                ? edtDescription.getText().toString().trim() : "";

        if (description.isEmpty()) {
            tilDescription.setError("Please describe what you need");
            return;
        }
        tilDescription.setError(null);

        if (selectedService == null) return;

        String customerId = requireArguments().getString(ARG_CUSTOMER_ID);
        btnSubmit.setEnabled(false);
        btnSubmit.setText("Creating...");

        CreateServiceRequestBody body = new CreateServiceRequestBody(
                customerId, selectedService.getId(), description);

        ApiClient.getClient()
                .create(ServiceRequestsApiService.class)
                .createRequest(body)
                .enqueue(new Callback<ServiceRequestResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ServiceRequestResponse> call,
                                           @NonNull Response<ServiceRequestResponse> response) {
                        if (!isAdded()) return;
                        btnSubmit.setEnabled(true);
                        btnSubmit.setText("Create Request");

                        if (!response.isSuccessful() || response.body() == null || !response.body().isOk()) {
                            Toast.makeText(requireContext(), "Error creating request", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (listener != null) listener.onRequestCreated(response.body().getRequest());
                        dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ServiceRequestResponse> call, @NonNull Throwable t) {
                        if (!isAdded()) return;
                        btnSubmit.setEnabled(true);
                        btnSubmit.setText("Create Request");
                        Toast.makeText(requireContext(), "Connection error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}