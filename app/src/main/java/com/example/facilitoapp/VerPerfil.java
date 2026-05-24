package com.example.facilitoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

    private static final String APP_PREFS = "facilito_user";
    private SharedPreferences prefs;
    private TextView edtNombre;
    private TextView edtApellidos;
    private TextView edtDui;
    private TextView edtTelefono;
    private TextView edtDireccion;
    private ImageButton btnEditarPerfil;
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

        initView();
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserProfileData();
    }

    private void initView() {
        prefs = getSharedPreferences(APP_PREFS, MODE_PRIVATE);

        edtNombre = findViewById(R.id.edtNombre);
        edtApellidos = findViewById(R.id.edtApellidos);
        edtDui = findViewById(R.id.edtDui);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtDireccion = findViewById(R.id.edtDireccion);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
    }

    private void setListeners() {
        btnEditarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(VerPerfil.this, EditarPerfil.class);
            intent.putExtra("name", user.getName());
            intent.putExtra("lastname", user.getLastName());
            intent.putExtra("dui", user.getDui());
            intent.putExtra("telephone", user.getTelephone());
            intent.putExtra("address", "Change this address");
            startActivity(intent);
        });
    }

    private void getUserProfileData() {
        SessionManager session = new SessionManager(this);
        String userId = session.getUserId();

        if(userId == null) {
            Util.ShowDefaultErrorMessage(this);
            return;
        }

        UserApiService userService = ApiClient.getClient().create(UserApiService.class);

        userService.getUserById(userId).enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if(!response.isSuccessful() || response.body() == null) {
                    Util.ShowDefaultErrorMessage(VerPerfil.this);
                    return;
                }

                UserProfileResponse userProfileResponse = response.body();
                user = userProfileResponse.user();

                if(!userProfileResponse.isOk()) {
                    Util.ShowDefaultErrorMessage(VerPerfil.this);
                    return;
                }

                User user = userProfileResponse.user();
                edtNombre.setText(user.getName());
                edtApellidos.setText(user.getLastName());

                edtDui.setText(user.getDui());
                edtTelefono.setText(user.getTelephone());
                edtDireccion.setText("San Salvador");
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.e("API", "Error " + t.getMessage());
            }
        });
    }
}
