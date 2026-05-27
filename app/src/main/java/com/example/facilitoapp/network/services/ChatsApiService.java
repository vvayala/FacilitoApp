package com.example.facilitoapp.network.services;

import com.example.facilitoapp.models.chats.Chat;
import com.example.facilitoapp.models.chats.ChatMessages;
import com.example.facilitoapp.models.chats.ChatResponse;
import com.example.facilitoapp.models.user.UserProfileResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChatsApiService {

    @GET("chat")
    Call<Chat> getChatById();

    @GET("chat/messages")
    Call<ChatResponse> getChatMessagesById();

   //@GET("chat/messages/{id}")
   //Call<ChatMessages> getChatsById(@Path("id") String chatMessagesId);
}


