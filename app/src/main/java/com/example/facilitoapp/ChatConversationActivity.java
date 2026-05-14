package com.example.facilitoapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChatConversationActivity extends AppCompatActivity {

    public static final String EXTRA_CHAT_TITLE = "extra_chat_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_conversation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView txtConversationTitle = findViewById(R.id.txtConversationTitle);
        TextView btnBackConversation = findViewById(R.id.btnBackConversation);

        String chatTitle = getIntent().getStringExtra(EXTRA_CHAT_TITLE);
        if (chatTitle != null && !chatTitle.isEmpty()) {
            txtConversationTitle.setText(chatTitle);
        }

        btnBackConversation.setOnClickListener(v -> finish());
    }
}
