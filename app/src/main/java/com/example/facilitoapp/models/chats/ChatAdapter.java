package com.example.facilitoapp.models.chats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facilitoapp.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_SENT = 1;
    private static final int TYPE_RECEIVED = 2;

    private List<ChatMessages> messages;
    private String currentUser;

    public ChatAdapter(List<ChatMessages> messages, String currentUser) {
        this.messages = messages;
        this.currentUser = currentUser;
    }

    public ChatAdapter() {
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessages message = messages.get(position);
        if (message.getSender_id().getId().equals(currentUser)) {
            return TYPE_SENT;
        } else {
            return TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sent_message, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.received_message, parent, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessages mensaje = messages.get(position);
        if (holder instanceof SentViewHolder) {
            ((SentViewHolder) holder).bind(mensaje);
        } else if (holder instanceof ReceivedViewHolder) {
            ((ReceivedViewHolder) holder).bind(mensaje);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    // ViewHolder for sent messages
    static class SentViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserSent, txtMessageSent, txtTimeSent;

        SentViewHolder(View itemView) {
            super(itemView);
            txtUserSent = itemView.findViewById(R.id.txtUserSent);
            txtMessageSent = itemView.findViewById(R.id.txtMessageSent);
            txtTimeSent = itemView.findViewById(R.id.txtTimeSent);
        }

        void bind(ChatMessages messages) {
            txtMessageSent.setText(messages.getMessageContent().trim());
            txtTimeSent.setText(messages.getTimeSent().trim());
            txtUserSent.setText(messages.getSender_id().getName().trim());
        }
    }

    // ViewHolder for received messages
    static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserReceived, txtMessageReceived, imgUserReceived, txtTimeReceived;

        ReceivedViewHolder(View itemView) {
            super(itemView);
            txtUserReceived = itemView.findViewById(R.id.txtUserReceived);
            txtMessageReceived = itemView.findViewById(R.id.txtMessageReceived);
            txtTimeReceived = itemView.findViewById(R.id.txtTimeReceived);
        }

        void bind(ChatMessages messages) {
            txtUserReceived.setText(messages.getSender_id().getName().trim() );
            txtMessageReceived.setText(messages.getMessageContent().trim());
            txtTimeReceived.setText(messages.getTimeSent().trim());
        }
    }
    public void setCurrentUserId(String usuarioActualId) {
        this.currentUser = usuarioActualId;
    }
}

