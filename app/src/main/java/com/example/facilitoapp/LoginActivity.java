package com.example.facilitoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.facilitoapp.network.ApiClient;
import com.example.facilitoapp.network.services.UserApiService;
import com.example.facilitoapp.models.user.LoginRequest;
import com.example.facilitoapp.models.user.LoginResponse;
import com.example.facilitoapp.models.user.User;
import com.example.facilitoapp.utils.FacilitoApp;
import com.example.facilitoapp.utils.LoadingDialog;
import com.example.facilitoapp.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private TextView txtRegistro;
    private MaterialButton btnIngresar;
    private LoadingDialog loadingDialog;
    private UserApiService userApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setNavigationBarColor(android.graphics.Color.TRANSPARENT);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupListeners();
    }

    private void initViews() {
        userApiService = ApiClient.getClient().create(UserApiService.class);
        loadingDialog  = new LoadingDialog(this);

        etEmail     = findViewById(R.id.etEmail);
        etPassword  = findViewById(R.id.etPassword);
        txtRegistro = findViewById(R.id.txtRegistro);
        btnIngresar = findViewById(R.id.btnIngresar);
    }

    private void setupListeners() {
        btnIngresar.setOnClickListener(v -> attemptLogin());

        txtRegistro.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterHostingActivity.class))
        );
    }

    private void attemptLogin() {
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        FacilitoApp.playClick();
        hideKeyboard();
        loadingDialog.show("Ingresando...");
        btnIngresar.setEnabled(false);

        userApiService.loginUser(new LoginRequest(email, password))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        loadingDialog.dismiss();
                        btnIngresar.setEnabled(true);

                        if (response.isSuccessful() && response.body() != null) {
                            LoginResponse loginResponse = response.body();
                            if (loginResponse.isOk()) {
                                User user = loginResponse.getUser();
                                SessionManager session = new SessionManager(LoginActivity.this);
                                session.saveUserId(user.getId());
                                session.saveTokens(loginResponse.getAccessToken(), loginResponse.getRefreshToken());
                                ApiClient.reset();

                                Toast.makeText(LoginActivity.this,
                                        "Bienvenido de nuevo, " + user.getName() + "! 👋",
                                        Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(LoginActivity.this, MainScreen.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this,
                                        loginResponse.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Correo o contraseña incorrectos",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        loadingDialog.dismiss();
                        btnIngresar.setEnabled(true);
                        Toast.makeText(LoginActivity.this,
                                "Connection error: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}