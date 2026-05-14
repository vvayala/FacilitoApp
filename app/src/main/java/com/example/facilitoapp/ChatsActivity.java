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

    private static final String DUMMY_CHAT_TITLE = "Mécanica General - Juan Mecánico";

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

        ImageView imgHeaderNotification = findViewById(R.id.imgHeaderNotification);
        if (imgHeaderNotification != null) {
            imgHeaderNotification.setOnClickListener(v -> {
                Intent intent = new Intent(ChatsActivity.this, Notificaciones.class);
                startActivity(intent);
            });
        }

        ImageView imgHeaderUser = findViewById(R.id.imgHeaderUser);
        if (imgHeaderUser != null) {
            imgHeaderUser.setOnClickListener(v -> {
                Intent intent = new Intent(ChatsActivity.this, VerPerfil.class);
                startActivity(intent);
            });
        }

        int[] chatCardIds = {
                R.id.cardChat1,
                R.id.cardChat2,
                R.id.cardChat3,
                R.id.cardChat4,
                R.id.cardChat5
        };

        for (int cardId : chatCardIds) {
            View chatCard = findViewById(cardId);
            chatCard.setOnClickListener(v -> openConversation(DUMMY_CHAT_TITLE));
        }

        FooterNavigationHelper.setupHomeNavigation(this);
    }

    private void openConversation(String chatTitle) {
        Intent intent = new Intent(ChatsActivity.this, ChatConversationActivity.class);
        intent.putExtra(ChatConversationActivity.EXTRA_CHAT_TITLE, chatTitle);
        startActivity(intent);
    }
}
