package com.example.facilitoapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.facilitoapp.R;
import com.example.facilitoapp.models.service.CreateServiceBody;
import com.example.facilitoapp.models.service.CreateServiceResponse;
import com.example.facilitoapp.network.ApiClient;
import com.example.facilitoapp.network.services.ServicesApiService;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewServiceBottomSheet extends BottomSheetDialogFragment {

    public interface OnServiceCreatedListener {
        void onServiceCreated();
    }

    private OnServiceCreatedListener listener;
    private String businessId;

    private TextInputEditText etServiceName, etServiceDescription;
    private MaterialButton btnSubmit, btnCancel;

    private ServicesApiService serviceApiService;

    public static NewServiceBottomSheet newInstance(String businessId) {
        NewServiceBottomSheet sheet = new NewServiceBottomSheet();
        Bundle args = new Bundle();
        args.putString("business_id", businessId);
        sheet.setArguments(args);
        return sheet;
    }

    public void setOnServiceCreatedListener(OnServiceCreatedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_new_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            businessId = getArguments().getString("business_id");
        }

        serviceApiService = ApiClient.getClient().create(ServicesApiService.class);

        etServiceName        = view.findViewById(R.id.etServiceName);
        etServiceDescription = view.findViewById(R.id.etServiceDescription);
        btnSubmit            = view.findViewById(R.id.btnSubmitService);
        btnCancel            = view.findViewById(R.id.btnCancelService);

        btnCancel.setOnClickListener(v -> dismiss());

        btnSubmit.setOnClickListener(v -> {
            String name = etServiceName.getText() != null
                    ? etServiceName.getText().toString().trim() : "";
            String desc = etServiceDescription.getText() != null
                    ? etServiceDescription.getText().toString().trim() : "";

            if (name.isEmpty()) {
                etServiceName.setError("Ingresa el nombre del servicio");
                return;
            }
            if (desc.isEmpty()) {
                etServiceDescription.setError("Ingresa una descripción");
                return;
            }

            submitService(name, desc);
        });
    }

    private void submitService(String name, String desc) {
        btnSubmit.setEnabled(false);
        btnSubmit.setText("Creando...");

        CreateServiceBody body = new CreateServiceBody(name, desc, businessId);

        serviceApiService.createService(body).enqueue(new Callback<CreateServiceResponse>() {
            @Override
            public void onResponse(@NonNull Call<CreateServiceResponse> call,
                                   @NonNull Response<CreateServiceResponse> response) {
                if (!isAdded()) return;
                btnSubmit.setEnabled(true);
                btnSubmit.setText("Crear Servicio");

                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    Toast.makeText(requireContext(), "Servicio creado correctamente", Toast.LENGTH_SHORT).show();
                    if (listener != null) listener.onServiceCreated();
                    dismiss();
                } else {
                    Toast.makeText(requireContext(), "Error al crear el servicio", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreateServiceResponse> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                btnSubmit.setEnabled(true);
                btnSubmit.setText("Crear Servicio");
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                Log.e("NewServiceBottomSheet", "onFailure: " + t.getMessage());
            }
        });
    }
}