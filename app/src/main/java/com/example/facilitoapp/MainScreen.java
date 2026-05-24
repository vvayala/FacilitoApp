package com.example.facilitoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.facilitoapp.fragments.HomeFragment;

public class MainScreen extends AppCompatActivity {
    private ImageView imgHeaderNotification, imgHeaderUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setNavigationBarColor(android.graphics.Color.TRANSPARENT);
        setContentView(R.layout.activity_main_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        initViews();
        setListeners();

        FooterNavigationHelper.setupHomeNavigation(this);
    }

    private void initViews() {
        imgHeaderNotification = findViewById(R.id.imgHeaderNotification);
        imgHeaderUser = findViewById(R.id.imgHeaderUser);
    }

    private void setListeners() {
        imgHeaderNotification.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this, Notificaciones.class);
            startActivity(intent);
        });

        imgHeaderUser.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this, VerPerfil.class);
            startActivity(intent);
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
