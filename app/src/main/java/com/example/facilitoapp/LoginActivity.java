package com.example.facilitoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
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
import com.example.facilitoapp.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String APP_PREFS = "facilito_user";
    private EditText etEmail, etPassword;
    private TextView txtRegistro;
    private Button btnIngresar;
    private FrameLayout loaderOverlay;
    private UserApiService userApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setNavigationBarColor(android.graphics.Color.TRANSPARENT);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        btnIngresar.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            showLoader();
            if (!email.isEmpty() && !password.isEmpty()) {
                LoginRequest loginRequest = new LoginRequest(email, password);

                userApiService.loginUser(loginRequest).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        hideLoader();

                        if (response.isSuccessful() && response.body() != null) {
                            LoginResponse loginResponse = response.body();
                            if (loginResponse.isOk()) {
                                User loggedUser = loginResponse.getUser();
                                String userId = loggedUser.getId();

                                SessionManager session = new SessionManager(LoginActivity.this);
                                session.saveUserId(userId);

                                Toast.makeText(LoginActivity.this,
                                        loginResponse.getMessage() + " Bienvenido " + loggedUser.getName(),
                                        Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(LoginActivity.this, MainScreen.class));
                            } else {
                                Toast.makeText(LoginActivity.this,
                                        "Error: " + loginResponse.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Error en la respuesta: " + response.code(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        hideLoader();
                        Toast.makeText(LoginActivity.this,
                                "Error de conexión: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(LoginActivity.this,
                        "Ingrese correo y contraseña", Toast.LENGTH_SHORT).show();
            }
        });

        txtRegistro.setOnClickListener( (v) -> {
            Intent registerView = new Intent(LoginActivity.this, RegistroOpciones.class);
            startActivity(registerView);
        });
    }

    private void initViews() {
        userApiService = ApiClient.getClient().create(UserApiService.class);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        txtRegistro = findViewById(R.id.txtRegistro);
        btnIngresar = findViewById(R.id.btnIngresar);
        loaderOverlay = findViewById(R.id.loaderOverlay);
    }

    private void showLoader() {
        hideLoader();
        loaderOverlay.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        loaderOverlay.setVisibility(View.GONE);
    }

    private void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}