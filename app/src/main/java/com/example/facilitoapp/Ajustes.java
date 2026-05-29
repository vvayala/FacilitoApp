package com.example.facilitoapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.materialswitch.MaterialSwitch;

public class Ajustes extends AppCompatActivity {

    private MaterialSwitch switchNotificaciones, switchSonido;
    private Button btnGuardarAjustes;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ajustes);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        switchNotificaciones = findViewById(R.id.switchNotificaciones);
        switchSonido = findViewById(R.id.switchSonido);
        btnGuardarAjustes = findViewById(R.id.btnGuardarAjustes);

        pref = getSharedPreferences("settings", MODE_PRIVATE);

        boolean notificacionesOn = pref.getBoolean("notificaciones", true);
        boolean sonidoOn = pref.getBoolean("sonido", true);

        switchNotificaciones.setChecked(notificacionesOn);
        switchSonido.setChecked(sonidoOn);

        // Ya no modificamos el volumen global, solo guardamos la preferencia
        btnGuardarAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = pref.edit();
                editor.putBoolean("notificaciones", switchNotificaciones.isChecked());
                editor.putBoolean("sonido", switchSonido.isChecked());
                editor.apply();
                Toast.makeText(Ajustes.this, "Ajustes guardados", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
