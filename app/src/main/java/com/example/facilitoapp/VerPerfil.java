package com.example.facilitoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VerPerfil extends AppCompatActivity {

    private static final String PREFS_NAME = "facilito_profile";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_APELLIDOS = "apellidos";
    private static final String KEY_DUI = "dui";
    private static final String KEY_TELEFONO = "telefono";
    private static final String KEY_DIRECCION = "direccion";

    private SharedPreferences prefs;

    private TextView edtNombre;
    private TextView edtApellidos;
    private TextView edtDui;
    private TextView edtTelefono;
    private TextView edtDireccion;

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

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        edtNombre = findViewById(R.id.edtNombre);
        edtApellidos = findViewById(R.id.edtApellidos);
        edtDui = findViewById(R.id.edtDui);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtDireccion = findViewById(R.id.edtDireccion);

        Button btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnEditarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(VerPerfil.this, EditarPerfil.class);
            startActivity(intent);
        });

        ImageView imgHeaderNotification = findViewById(R.id.imgHeaderNotification);
        if (imgHeaderNotification != null) {
            imgHeaderNotification.setOnClickListener(v -> {
                Intent intent = new Intent(VerPerfil.this, Notificaciones.class);
                startActivity(intent);
            });
        }

        ImageView imgHeaderSettings = findViewById(R.id.imgHeaderSettings);
        if (imgHeaderSettings != null) {
            imgHeaderSettings.setOnClickListener(v -> {
                Intent intent = new Intent(VerPerfil.this, Ajustes.class);
                startActivity(intent);
            });
        }

        FooterNavigationHelper.setupHomeNavigation(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfileIntoFields();
    }

    private void loadProfileIntoFields() {
        String nombre = prefs.getString(KEY_NOMBRE, "John");
        String apellidos = prefs.getString(KEY_APELLIDOS, "Doe");
        String dui = prefs.getString(KEY_DUI, "01234567-8");
        String telefono = prefs.getString(KEY_TELEFONO, "7123-4567");
        String direccion = prefs.getString(KEY_DIRECCION, "San Salvador, San Salvador");

        edtNombre.setText(nombre);
        edtApellidos.setText(apellidos);
        edtDui.setText(dui);
        edtTelefono.setText(telefono);
        edtDireccion.setText(direccion);
    }
}
