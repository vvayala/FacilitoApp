package com.example.facilitoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.facilitoapp.models.user.User;
import com.example.facilitoapp.models.user.UserProfileResponse;
import com.example.facilitoapp.network.ApiClient;
import com.example.facilitoapp.network.services.UserApiService;
import com.example.facilitoapp.utils.SessionManager;
import com.example.facilitoapp.utils.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerPerfil extends AppCompatActivity {

    private TextView edtNombre, edtApellidos, edtDui, edtEmail, edtTelefono, edtDireccion;
    private ImageButton btnEditarPerfil;
    private ImageView imgHeaderSettings;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setNavigationBarColor(android.graphics.Color.TRANSPARENT);
        setContentView(R.layout.activity_ver_perfil);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile();
    }

    private void initViews() {
        edtNombre = findViewById(R.id.edtNombre);
        edtApellidos = findViewById(R.id.edtApellidos);
        edtDui = findViewById(R.id.edtDui);
        edtEmail = findViewById(R.id.edtEmail);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtDireccion = findViewById(R.id.edtDireccion);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        imgHeaderSettings = findViewById(R.id.imgHeaderSettings);
        btnEditarPerfil.bringToFront();
    }

    private void setupListeners() {
        btnEditarPerfil.setOnClickListener(v -> {
            if (user == null) return;
            Intent intent = new Intent(this, EditarPerfil.class);
            intent.putExtra("name", user.getName());
            intent.putExtra("lastname", user.getLastName());
            intent.putExtra("dui", user.getDui());
            intent.putExtra("email", user.getEmail());
            intent.putExtra("telephone", user.getTelephone());
            intent.putExtra("address", user.getAddress());
            startActivity(intent);
        });

        imgHeaderSettings.setOnClickListener(v ->
                startActivity(new Intent(this, Ajustes.class))
        );
    }

    private void loadUserProfile() {
        String userId = new SessionManager(this).getUserId();

        if (userId == null) {
            Util.ShowDefaultErrorMessage(this);
            return;
        }

        ApiClient.getClient()
                .create(UserApiService.class)
                .getUserById(userId)
                .enqueue(new Callback<UserProfileResponse>() {
                    @Override
                    public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                        if (!response.isSuccessful() || response.body() == null || !response.body().isOk()) {
                            Util.ShowDefaultErrorMessage(VerPerfil.this);
                            return;
                        }

                        user = response.body().user();
                        bindUserData(user);
                    }

                    @Override
                    public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                        Log.e("VerPerfil", "Error: " + t.getMessage());
                        Toast.makeText(VerPerfil.this,
                                "Connection error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void bindUserData(User user) {
        edtNombre.setText(user.getName());
        edtApellidos.setText(user.getLastName());
        edtDui.setText(user.getDui().substring(0, 8) + "-" + user.getDui().substring(8));
        edtEmail.setText(user.getEmail());
        edtTelefono.setText(user.getTelephone().substring(0, 4) + "-" + user.getTelephone().substring(4));
        edtDireccion.setText(user.getAddress());
    }
}
