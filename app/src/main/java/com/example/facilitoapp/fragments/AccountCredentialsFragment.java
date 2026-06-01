package com.example.facilitoapp.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facilitoapp.MainScreen;
import com.example.facilitoapp.R;
import com.example.facilitoapp.models.business.CreateBusinessBody;
import com.example.facilitoapp.models.business.CreateBusinessResponse;
import com.example.facilitoapp.models.register.RegisterViewModel;
import com.example.facilitoapp.models.user.LoginResponse;
import com.example.facilitoapp.models.user.RegisterRequest;
import com.example.facilitoapp.network.ApiClient;
import com.example.facilitoapp.network.services.BusinessApiService;
import com.example.facilitoapp.network.services.UserApiService;
import com.example.facilitoapp.utils.LoadingDialog;
import com.example.facilitoapp.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountCredentialsFragment extends Fragment {

    private RegisterViewModel viewModel;
    private TextInputEditText txtEmail, txtPassword, txtConfirmPassword;
    private TextInputLayout layoutEmail, layoutConfirmPassword;
    private Button btnRegister;
    private TextView txtPasswordError;
    private View strengthBar1, strengthBar2, strengthBar3;

    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_credentials, container, false);
        loadingDialog = new LoadingDialog((AppCompatActivity) requireActivity());

        viewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPassword = view.findViewById(R.id.txtPassword);
        txtConfirmPassword = view.findViewById(R.id.txtConfirmPassword);
        layoutEmail = view.findViewById(R.id.layoutEmail);
        layoutConfirmPassword = view.findViewById(R.id.layoutConfirmPassword);
        btnRegister = view.findViewById(R.id.btnRegister);
        txtPasswordError = view.findViewById(R.id.txtPasswordError);
        strengthBar1 = view.findViewById(R.id.strengthBar1);
        strengthBar2 = view.findViewById(R.id.strengthBar2);
        strengthBar3 = view.findViewById(R.id.strengthBar3);

        // Password strength watcher
        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updatePasswordStrength(s.toString());
            }
        });

        // Confirm password watcher
        txtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pass = txtPassword.getText().toString();
                if (!s.toString().isEmpty() && !s.toString().equals(pass)) {
                    txtPasswordError.setVisibility(View.VISIBLE);
                    layoutConfirmPassword.setBoxStrokeColor(Color.parseColor("#E53935"));
                } else {
                    txtPasswordError.setVisibility(View.GONE);
                    layoutConfirmPassword.setBoxStrokeColor(Color.parseColor("#7640BD"));
                }
            }
        });

        btnRegister.setOnClickListener(v -> attemptRegister());

        return view;
    }

    private void updatePasswordStrength(String password) {
        int strength = 0;
        if (password.length() >= 8) strength++;
        if (password.matches(".*[A-Z].*") || password.matches(".*[0-9].*")) strength++;
        if (password.matches(".*[!@#$%^&*()_+].*")) strength++;

        int[] colors = {
                Color.parseColor("#E53935"), // Weak  - red
                Color.parseColor("#FB8C00"), // Fair  - orange
                Color.parseColor("#43A047")  // Strong - green
        };

        View[] bars = {strengthBar1, strengthBar2, strengthBar3};
        for (int i = 0; i < bars.length; i++) {
            bars[i].setBackgroundTintList(ColorStateList.valueOf(
                    i < strength ? colors[strength - 1] : Color.parseColor("#BDBDBD")
            ));
        }
    }

    private void attemptRegister() {
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String confirm = txtConfirmPassword.getText().toString().trim();

        // Validaciones
        if (email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            layoutEmail.setBoxStrokeColor(Color.parseColor("#E53935"));
            Toast.makeText(getContext(), "Ingresa un correo válido", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 8) {
            Toast.makeText(getContext(), "Contraseña debe contener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirm)) {
            txtPasswordError.setVisibility(View.VISIBLE);
            return;
        }

        viewModel.email.setValue(email);
        viewModel.password.setValue(password);

        registerUser();
    }

    private void registerUser() {
        loadingDialog.show("Creando tu cuenta");
        btnRegister.setEnabled(false);

        String name = viewModel.name.getValue();
        String lastName = viewModel.lastName.getValue();
        String phone = viewModel.telephone.getValue();
        String dui = viewModel.dui.getValue();
        String address = viewModel.address.getValue();
        String email = viewModel.email.getValue();
        String password = viewModel.password.getValue();

        String accountType = viewModel.accountType.getValue();
        String roleId = "PROVIDER".equals(accountType)
                ? viewModel.providerRoleId.getValue()
                : viewModel.clientRoleId.getValue();

        RegisterRequest request = new RegisterRequest(
                name, lastName, phone, dui, address, email, password, roleId
        );

        UserApiService userApiService = ApiClient.getClient().create(UserApiService.class);
        userApiService.registerUser(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loadingDialog.dismiss();
                btnRegister.setEnabled(true);

                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    LoginResponse loginResponse = response.body();
                    String userId = loginResponse.getUser().getId();
                    SessionManager sessionManager = new SessionManager(requireContext());
                    sessionManager.clearSession();
                    sessionManager.saveUserId(userId);
                    sessionManager.saveTokens(loginResponse.getAccessToken(), loginResponse.getRefreshToken());

                    ApiClient.reset();

                    boolean isProvider = "PROVIDER".equals(viewModel.accountType.getValue());

                    if (isProvider) {
                        createBusinessForProvider(userId);
                    } else {
                        Toast.makeText(getContext(),
                                "Bienvenido, " + name + "! Cuenta creada correctamente.",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(requireActivity(), MainScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        requireActivity().finish();
                    }
                } else {
                    Toast.makeText(getContext(),
                            "Registro falló, favor intentar nuevamente.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Connection error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createBusinessForProvider(String userId) {
        loadingDialog.show("Configurando tu negocio");

        CreateBusinessBody body = new CreateBusinessBody(
                viewModel.name.getValue(),
                viewModel.businessDescription.getValue(),
                viewModel.businessImageUri.getValue().toString(),
                userId
        );

        BusinessApiService businessApiService = ApiClient.getClient().create(BusinessApiService.class);
        businessApiService.createBusiness(body).enqueue(new Callback<CreateBusinessResponse>() {
            @Override
            public void onResponse(@NonNull Call<CreateBusinessResponse> call,
                                   @NonNull Response<CreateBusinessResponse> response) {
                loadingDialog.dismiss();

                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    Toast.makeText(getContext(),
                            "Bienvenido, " + viewModel.name.getValue() + "! Cuenta creada correctamente.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),
                            "Cuenta creada, pero hubo un error al configurar tu negocio.",
                            Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(requireActivity(), MainScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
            }

            @Override
            public void onFailure(@NonNull Call<CreateBusinessResponse> call, @NonNull Throwable t) {
                loadingDialog.dismiss();
                Toast.makeText(getContext(),
                        "Cuenta creada, pero error de conexión al configurar negocio.",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(requireActivity(), MainScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
            }
        });
    }
}