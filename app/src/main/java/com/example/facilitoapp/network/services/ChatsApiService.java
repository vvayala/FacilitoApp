package com.example.facilitoapp.network.services;

import com.example.facilitoapp.models.chats.Chat;
import com.example.facilitoapp.models.chats.ChatMessages;
import com.example.facilitoapp.models.chats.ChatResponse;
import com.example.facilitoapp.models.chats.SendChat;
import com.example.facilitoapp.models.chats.SentChat;
import com.example.facilitoapp.models.user.LoginRequest;
import com.example.facilitoapp.models.user.LoginResponse;
import com.example.facilitoapp.models.user.UserProfileResponse;

import javax.net.ssl.SSLEngine;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChatsApiService {

   // @GET("chat/{user_id}")
    // Call<Chat> getChatById(@Path("user_id") String userId);

   @GET("chat/messages")
   Call<ChatResponse> getChatMessagesById(@Query("user_id") String userId);

    @POST("chat/messages")
    Call<SentChat> sendChat(@Body SendChat chatResponse);

}


