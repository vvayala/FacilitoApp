package com.example.facilitoapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import com.example.facilitoapp.models.chats.Chat;
import com.example.facilitoapp.models.chats.ChatAdapter;
import com.example.facilitoapp.models.chats.ChatMessages;
import com.example.facilitoapp.models.chats.ChatResponse;
import com.example.facilitoapp.models.chats.SendChat;
import com.example.facilitoapp.models.chats.SentChat;
import com.example.facilitoapp.network.ApiClient;
import com.example.facilitoapp.network.services.ChatsApiService;
import com.example.facilitoapp.utils.SessionManager;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatConversationActivity extends AppCompatActivity {

    public static final String EXTRA_CHAT_TITLE = "Chat";
    private TextView txtConversationTitle;
    private ImageButton btnBackConversation;
    private ImageButton imgSendMessage;
    private EditText etMessage2Send;
    private RecyclerView rvChats;
    private String currentUser, newMessage, currentChatId, businessName;
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

        currentUser = new SessionManager(this).getUserId();
        txtConversationTitle = findViewById(R.id.txtConversationTitle);
        btnBackConversation = findViewById(R.id.btnBackConversation);
        imgSendMessage = findViewById(R.id.imgSendMessage);
        etMessage2Send = findViewById(R.id.etMessage2Send);
        rvChats = findViewById(R.id.rvChats);
        chatsApiService = ApiClient.getClient().create(ChatsApiService.class);
        currentChatId = getIntent().getStringExtra("chatId");
        businessName = getIntent().getStringExtra("businessName");
        txtConversationTitle.setText(businessName);

        loadMessages();

        btnBackConversation.setOnClickListener(v -> finish());
        
        imgSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMessage = etMessage2Send.getText().toString().trim();
                if (newMessage.isEmpty()){
                    Toast.makeText(ChatConversationActivity.this,
                            "Escribe un mensaje",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    sendMessage(newMessage, currentUser,currentChatId);
                }
            }
        });
    }

    private void sendMessage(String newMessage, String currentUser, String currentChatId) {
        SendChat sendChat = new SendChat(newMessage,currentUser,currentChatId);
        chatsApiService.sendChat(sendChat).enqueue(new Callback<SentChat>() {
            @Override
            public void onResponse(Call<SentChat> call, Response<SentChat> response) {
                loadMessages();
                etMessage2Send.setText("");
            }

            @Override
            public void onFailure(Call<SentChat> call, Throwable t) {
                Toast.makeText(ChatConversationActivity.this,
                        "Error al enviar mensajes",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMessages() {
        chatsApiService.getChatMessagesById(currentChatId).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChatResponse chatResponse = response.body();

                    if (chatResponse.isSuccess()) {
                        List<ChatMessages> messages = chatResponse.getMessages();

                        ChatAdapter adapter = new ChatAdapter(messages, currentUser);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatConversationActivity.this);
                        layoutManager.setReverseLayout(true);
                        rvChats.setLayoutManager(layoutManager);
                        rvChats.setAdapter(adapter);
                        rvChats.scrollToPosition(0);
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
