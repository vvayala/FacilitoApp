package com.example.facilitoapp.models.chats;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facilitoapp.ChatConversationActivity;
import com.example.facilitoapp.R;
import com.example.facilitoapp.models.business.Business;
import com.example.facilitoapp.models.service.ServiceRequestDetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatsListAdapter extends RecyclerView.Adapter<ChatsListAdapter.ChatViewHolder> {

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    private final List<Chat> chats = new ArrayList<>();
    private final OnChatClickListener listener;

    public ChatsListAdapter(OnChatClickListener listener) {
        this.listener = listener;
    }

    public void setChats(List<Chat> newChats) {
        chats.clear();
        chats.addAll(newChats);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bind(chats.get(position), listener);
    }

    @Override
    public int getItemCount() { return chats.size(); }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtBusinessName, txtServiceName, txtDate;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBusinessName = itemView.findViewById(R.id.txtChatBusinessName);
            txtServiceName  = itemView.findViewById(R.id.txtChatServiceName);
            txtDate         = itemView.findViewById(R.id.txtChatDate);
        }

        void bind(Chat chat, OnChatClickListener listener) {
            // Business name
            Business business = chat.getBusiness();
            txtBusinessName.setText(business != null ? business.getBusinessName() : "Unknown");

            // Service name from service_request_id → service_id
            ServiceRequestDetail sr = chat.getServiceRequest();
            if (sr != null && sr.getServiceId() != null) {
                txtServiceName.setText(sr.getServiceId().getServiceName());
                txtServiceName.setVisibility(View.VISIBLE);
            } else {
                txtServiceName.setVisibility(View.GONE);
            }

            // Date
            if (sr != null && sr.getServiceRequestDate() != null) {
                txtDate.setText(formatDate(sr.getServiceRequestDate()));
            } else {
                txtDate.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> listener.onChatClick(chat));
        }

        private String formatDate(String rawDate) {
            try {
                SimpleDateFormat input  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                SimpleDateFormat output = new SimpleDateFormat("MMM dd", Locale.getDefault());
                Date date = input.parse(rawDate);
                return date != null ? output.format(date) : rawDate;
            } catch (ParseException e) {
                return rawDate;
            }
        }
    }
}