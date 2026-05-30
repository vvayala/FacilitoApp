package com.example.facilitoapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.facilitoapp.ChatConversationActivity;
import com.example.facilitoapp.R;
import com.example.facilitoapp.models.business.Business;
import com.example.facilitoapp.models.chats.Chat;
import com.example.facilitoapp.models.chats.ChatsListAdapter;
import com.example.facilitoapp.models.chats.ChatsListResponse;
import com.example.facilitoapp.network.ApiClient;
import com.example.facilitoapp.network.services.ChatsApiService;
import com.example.facilitoapp.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesFragment extends Fragment {

    private RecyclerView recyclerChats;
    private ProgressBar loadingChats;
    private LinearLayout emptyChatsState;
    private ChatsApiService chatsApiService;
    private ChatsListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerChats  = view.findViewById(R.id.recyclerChats);
        loadingChats   = view.findViewById(R.id.loadingChats);
        emptyChatsState = view.findViewById(R.id.emptyChatsState);

        chatsApiService = ApiClient.getClient().create(ChatsApiService.class);

        adapter = new ChatsListAdapter(this::openChat);
        recyclerChats.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerChats.setAdapter(adapter);

        loadChats();
    }

    private void loadChats() {
        loadingChats.setVisibility(View.VISIBLE);
        recyclerChats.setVisibility(View.GONE);
        emptyChatsState.setVisibility(View.GONE);

        String userId = new SessionManager(requireContext()).getUserId();

        chatsApiService.getChatsByUser(userId).enqueue(new Callback<ChatsListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChatsListResponse> call,
                                   @NonNull Response<ChatsListResponse> response) {
                if (!isAdded()) return;

                loadingChats.setVisibility(View.GONE);

                if (!response.isSuccessful() || response.body() == null || !response.body().isOk()) {
                    adapter.setChats(new java.util.ArrayList<>());
                    recyclerChats.setVisibility(View.GONE);
                    emptyChatsState.setVisibility(View.VISIBLE);
                    return;
                }

                List<Chat> chats = response.body().getChats();

                if (chats == null || chats.isEmpty()) {
                    adapter.setChats(new java.util.ArrayList<>());
                    recyclerChats.setVisibility(View.GONE);
                    emptyChatsState.setVisibility(View.VISIBLE);
                } else {
                    adapter.setChats(chats);
                    emptyChatsState.setVisibility(View.GONE);
                    recyclerChats.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChatsListResponse> call, @NonNull Throwable t) {
                if (!isAdded()) return;

                loadingChats.setVisibility(View.GONE);
                adapter.setChats(new java.util.ArrayList<>());
                recyclerChats.setVisibility(View.GONE);
                emptyChatsState.setVisibility(View.VISIBLE);

                Toast.makeText(requireContext(), "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openChat(Chat chat) {
        Business business = chat.getBusiness();
        String businessName = business != null ? business.getBusinessName() : "Chat";

        Intent intent = new Intent(requireContext(), ChatConversationActivity.class);
        intent.putExtra("chatId", chat.getId());
        intent.putExtra("businessName", businessName);
        startActivity(intent);
    }
}