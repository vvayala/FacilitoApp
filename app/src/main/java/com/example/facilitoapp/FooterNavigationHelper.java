package com.example.facilitoapp;

import android.content.Intent;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public final class FooterNavigationHelper {

    private FooterNavigationHelper() {}

    public static void setupHomeNavigation(AppCompatActivity activity) {
        ImageView navHome = activity.findViewById(R.id.navHome);
        ImageView navChat = activity.findViewById(R.id.navChat);
        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                Intent intent = new Intent(activity, MainScreen.class);
                activity.startActivity(intent);
            });
        }

        if (navChat != null) {
            navChat.setOnClickListener(v -> {
                if (!(activity instanceof ChatsActivity)) {
                    Intent intent = new Intent(activity, ChatsActivity.class);
                    activity.startActivity(intent);
                }
            });
        }
    }
}
