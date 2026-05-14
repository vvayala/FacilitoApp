package com.example.facilitoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.facilitoapp.models.ApiClient;
import com.example.facilitoapp.models.ApiService;
import com.example.facilitoapp.models.LoginResponse;
import com.example.facilitoapp.models.RegisterRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroCliente extends AppCompatActivity {

    private EditText txtNombre, txtApellido, txtTelefono, txtDui, txtCorreo, txtContrasena;
    private Button btnRegistro;
    private ApiService apiService;

    private static final String CLIENTE_ROLE_ID = "6822f3a7b2f1c4a8b1d2e3f4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_cliente);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtDui = findViewById(R.id.txtDui);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtContrasena = findViewById(R.id.txtContrasena);
        btnRegistro = findViewById(R.id.btnRegistro);
        apiService = ApiClient.getClient().create(ApiService.class);

        btnRegistro.setOnClickListener(v -> registroUsuario());
    }

    private void registroUsuario() {
        String nombre = txtNombre.getText().toString().trim();
        String apellido = txtApellido.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();
        String dui = txtDui.getText().toString().trim();
        String correo = txtCorreo.getText().toString().trim();
        String contrasena = txtContrasena.getText().toString().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() ||
            dui.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request = new RegisterRequest(
            nombre, apellido, telefono, dui, correo, contrasena, CLIENTE_ROLE_ID
        );

        apiService.registerUser(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.isOk()) {
                        Toast.makeText(RegistroCliente.this,
                                "Registro exitoso. Bienvenido " + nombre,
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistroCliente.this, MainScreen.class));
                        finish();
                    } else {
                        Toast.makeText(RegistroCliente.this,
                                "Error: " + loginResponse.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegistroCliente.this,
                            "Error en la respuesta: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(RegistroCliente.this,
                        "Error de conexion: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
