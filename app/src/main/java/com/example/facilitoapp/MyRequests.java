package com.example.facilitoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MyRequests extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setNavigationBarColor(android.graphics.Color.TRANSPARENT);
        setContentView(R.layout.activity_my_requests);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imgHeaderNotification = findViewById(R.id.imgHeaderNotification);
        if (imgHeaderNotification != null) {
            imgHeaderNotification.setOnClickListener(v -> {
                Intent intent = new Intent(MyRequests.this, Notificaciones.class);
                startActivity(intent);
            });
        }

        ImageView imgHeaderUser = findViewById(R.id.imgHeaderUser);
        if (imgHeaderUser != null) {
            imgHeaderUser.setOnClickListener(v -> {
                Intent intent = new Intent(MyRequests.this, VerPerfil.class);
                startActivity(intent);
            });
        }

        FooterNavigationHelper.setupHomeNavigation(this);
    }
}
