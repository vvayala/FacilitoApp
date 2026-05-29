package com.example.facilitoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.facilitoapp.models.catalogs.Role;
import com.example.facilitoapp.models.catalogs.RoleReponse;
import com.example.facilitoapp.network.ApiClient;
import com.example.facilitoapp.network.services.CatalogApiService;
import com.example.facilitoapp.utils.FacilitoApp;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroOpciones extends AppCompatActivity {

    Button btnCliente, btnProveedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_opciones);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getRoles();
        initViews();
        setListeners();
    }

    public void getRoles() {
        CatalogApiService catalogApiService = ApiClient.getClient().create(CatalogApiService.class);

        catalogApiService.getRoles()
                .enqueue(new Callback<RoleReponse>() {
                    @Override
                    public void onResponse(Call<RoleReponse> call, Response<RoleReponse> response) {
                        RoleReponse data = response.body();
                        Role role = data.roles().get(0);
                        filterRoles(data.roles());
                    }

                    @Override
                    public void onFailure(Call<RoleReponse> call, Throwable t) {
                        Toast.makeText(RegistroOpciones.this, "Algo salió mal, intentalo de nuevo", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void filterRoles(List<Role> roles) {
        final String clientRole = "Rol de prueba";
        final String workerRole = "trabajador";

        Optional<Role> matchedRole = roles.stream()
                .filter(role -> role != null && role.getUserRoleName() != null)
                .filter(role -> role.getUserRoleName().equals(clientRole) || role.getUserRoleName().equals(workerRole))
                .findFirst();

        if (matchedRole.isPresent()) {
            String roleName = matchedRole.get().getUserRoleName();

            if (roleName.equals(clientRole)) {
                Toast.makeText(this, "Bienvenido cliente " + clientRole, Toast.LENGTH_SHORT).show();
            } else if (roleName.equals(workerRole)) {
                Toast.makeText(this, "Bienvenido trabajador", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Rol no reconocido", Toast.LENGTH_SHORT).show();
        }
    }

    public void initViews() {
        btnCliente = findViewById(R.id.btnCliente);
        btnProveedor = findViewById(R.id.btnProveedor);
    }

    public void setListeners() {
        btnCliente.setOnClickListener(v -> {
            FacilitoApp.playClick();
            Intent intent = new Intent(RegistroOpciones.this, RegistroCliente.class);
            startActivity(intent);
        });

        btnProveedor.setOnClickListener(v -> {
            FacilitoApp.playClick();
            Intent intent2 = new Intent(RegistroOpciones.this, RegistroProveedor.class);
            startActivity(intent2);
        });
    }
}