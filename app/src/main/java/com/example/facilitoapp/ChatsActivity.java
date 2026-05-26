package com.example.facilitoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChatsActivity extends AppCompatActivity {
    private ImageView imgHeaderNotification, imgHeaderUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setNavigationBarColor(android.graphics.Color.TRANSPARENT);
        setContentView(R.layout.activity_chats);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setListeners();
    }

    private void initViews() {
         imgHeaderNotification = findViewById(R.id.imgHeaderNotification);
         imgHeaderUser = findViewById(R.id.imgHeaderUser);
    }

    private void setListeners() {
        imgHeaderNotification.setOnClickListener(v -> {
            Intent intent = new Intent(ChatsActivity.this, Notificaciones.class);
            startActivity(intent);
        });

        imgHeaderUser.setOnClickListener(v -> {
            Intent intent = new Intent(ChatsActivity.this, VerPerfil.class);
            startActivity(intent);
        });
    }
}
