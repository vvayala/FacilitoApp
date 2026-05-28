package com.example.facilitoapp.fragments;

import android.content.ContentUris;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.facilitoapp.ChatConversationActivity;
import com.example.facilitoapp.ChatsActivity;
import com.example.facilitoapp.R;
import com.example.facilitoapp.models.chats.Chat;
import com.example.facilitoapp.models.chats.ChatAdapter;
import com.example.facilitoapp.models.chats.ChatMessages;
import com.example.facilitoapp.models.chats.ChatResponse;
import com.example.facilitoapp.models.chats.InboxAdapter;
import com.example.facilitoapp.models.chats.InboxResponse;
import com.example.facilitoapp.network.ApiClient;
import com.example.facilitoapp.network.services.ChatsApiService;
import com.example.facilitoapp.utils.SessionManager;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesFragment extends Fragment {

    private RecyclerView rvMensajes;
    private ChatsApiService chatsApiService;
    private String currentUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        rvMensajes = view.findViewById(R.id.rvMensajes);
        chatsApiService = ApiClient.getClient().create(ChatsApiService.class);
        currentUser = new SessionManager(getContext()).getUserId();
        loadMessages();
        return view;
    }

    private void loadMessages() {
        chatsApiService.getChatById(currentUser).enqueue(new Callback<InboxResponse>() {
            @Override
            public void onResponse(Call<InboxResponse> call, Response<InboxResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    InboxResponse inboxResponse = response.body();

                    if (inboxResponse.isSuccess()) {
                        List<Chat> inbox = inboxResponse.getInbox();

                        InboxAdapter adapter = new InboxAdapter(inbox);
                        rvMensajes.setLayoutManager(new LinearLayoutManager(getContext()));
                        rvMensajes.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(),
                                "Error al cargar inbox",
                                Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<InboxResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }


}