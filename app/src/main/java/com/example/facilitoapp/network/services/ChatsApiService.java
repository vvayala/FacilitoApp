package com.example.facilitoapp.network.services;

import com.example.facilitoapp.models.chats.ChatResponse;
import com.example.facilitoapp.models.chats.ChatsListResponse;
import com.example.facilitoapp.models.chats.CreateChatBody;
import com.example.facilitoapp.models.chats.CreateChatResponse;
import com.example.facilitoapp.models.chats.SendChat;
import com.example.facilitoapp.models.chats.SentChat;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChatsApiService {

   @GET("chat")
   Call<ChatsListResponse> getChatsByUser(@Query("user_id") String userId);

   @GET("chat/messages")
   Call<ChatResponse> getChatMessagesById(@Query("chat_id") String userId);

    @POST("chat/messages")
    Call<SentChat> sendChat(@Body SendChat chatResponse);

    @POST("chat")
    Call<CreateChatResponse> createChat(@Body CreateChatBody body);
}


