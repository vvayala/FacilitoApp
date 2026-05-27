package com.example.facilitoapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facilitoapp.models.chats.ChatAdapter;
import com.example.facilitoapp.models.chats.ChatMessages;
import com.example.facilitoapp.models.chats.ChatResponse;
import com.example.facilitoapp.network.ApiClient;
import com.example.facilitoapp.network.services.ChatsApiService;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatConversationActivity extends AppCompatActivity {

    public static final String EXTRA_CHAT_TITLE = "extra_chat_title";
    private TextView txtConversationTitle, btnBackConversation;
    private ImageButton imgAddPicture, imgSendMessage;
    private EditText etMessage2Send;
    private RecyclerView rvChats;
    private String currentUser = "";
    private List<ChatMessages> messages;
    ChatsApiService chatsApiService;

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

        txtConversationTitle = findViewById(R.id.txtConversationTitle);
        btnBackConversation = findViewById(R.id.btnBackConversation);
        imgAddPicture = findViewById(R.id.imgAddPicture);
        imgSendMessage = findViewById(R.id.imgSendMessage);
        etMessage2Send = findViewById(R.id.etMessage2Send);
        rvChats = findViewById(R.id.rvChats);
        chatsApiService = ApiClient.getClient().create(ChatsApiService.class);

        loadMessages();

        String chatTitle = getIntent().getStringExtra(EXTRA_CHAT_TITLE);
        if (chatTitle != null && !chatTitle.isEmpty()) {
            txtConversationTitle.setText(chatTitle);
        }

        btnBackConversation.setOnClickListener(v -> finish());
    }

    private void loadMessages() {
        chatsApiService.getChatMessagesById().enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChatResponse chatResponse = response.body();
                    Log.d("API_RESPONSE", new Gson().toJson(response.body()));

                    if (chatResponse.isSuccess()) {
                        List<ChatMessages> messages = chatResponse.getMessages();

                        ChatAdapter adapter = new ChatAdapter(messages, currentUser);
                        rvChats.setLayoutManager(new LinearLayoutManager(ChatConversationActivity.this));
                        rvChats.setAdapter(adapter);

                        rvChats.scrollToPosition(messages.size() - 1);
                    } else {
                        Toast.makeText(ChatConversationActivity.this,
                                "Error al cargar mensajes",
                                Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                Toast.makeText(ChatConversationActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
