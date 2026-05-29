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
import androidx.fragment.app.Fragment;

import com.example.facilitoapp.fragments.HomeFragment;
import com.example.facilitoapp.fragments.MessagesFragment;
import com.example.facilitoapp.fragments.NotificationsFragment;
import com.example.facilitoapp.utils.FacilitoApp;

public class MainScreen extends AppCompatActivity {
    private View bottomBar;
    private ImageView navHome, navChat, navLocation, imgHeaderNotification, imgHeaderUser;
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
    }

    private void initViews() {
        bottomBar = findViewById(R.id.includeBottomBar);
        navHome = bottomBar.findViewById(R.id.navHome);
        navChat = bottomBar.findViewById(R.id.navChat);
        navLocation = bottomBar.findViewById(R.id.navLocation);

        imgHeaderNotification = findViewById(R.id.imgHeaderNotification);
        imgHeaderUser = findViewById(R.id.imgHeaderUser);
    }

    private void setListeners() {
        imgHeaderNotification.setOnClickListener(v -> {
            loadFragment(new NotificationsFragment());
        });

        imgHeaderUser.setOnClickListener(v -> {
            FacilitoApp.playClick();
            Intent intent = new Intent(MainScreen.this, VerPerfil.class);
            startActivity(intent);
        });

        navHome.setOnClickListener(v -> {
            FacilitoApp.playClick();
            loadFragment(new HomeFragment());
        });

        navChat.setOnClickListener(v -> {
            FacilitoApp.playClick();
            loadFragment(new MessagesFragment());
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
