package com.example.facilitoapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.facilitoapp.ChatConversationActivity;
import com.example.facilitoapp.ChatsActivity;
import com.example.facilitoapp.R;

public class MessagesFragment extends Fragment {
    private View chatCard;
    private static final String DUMMY_CHAT_TITLE = "Mécanica General - Juan Mecánico";
    private final int[] chatCardIds = {
            R.id.cardChat1,
            R.id.cardChat2,
            R.id.cardChat3,
            R.id.cardChat4,
            R.id.cardChat5
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        initViews(view);
        return view;
    }

    private void initViews(View view) {
        for (int cardId : chatCardIds) {
            chatCard = view.findViewById(cardId);
            chatCard.setOnClickListener(v -> openConversation());
        }
    }

    private void openConversation() {
        Intent intent = new Intent(requireActivity(), ChatConversationActivity.class);
        intent.putExtra(ChatConversationActivity.EXTRA_CHAT_TITLE, DUMMY_CHAT_TITLE);
        startActivity(intent);
    }
}