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

import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {

    private List<Chat> chatList;

    public InboxAdapter(List<Chat> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_message, parent, false);
        return new InboxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        if (chat.getBusiness() != null) {
        holder.txtSenderName.setText(chat.getBusiness().getBusinessName().trim());
        }
        if (chat.getServiceRequest() != null) {
            holder.txtServiceName.setText(chat.getServiceRequest().getServiceId().getServiceName());
            holder.txtMessageTime.setText(chat.getServiceRequest().getServiceRequestDate());
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ChatConversationActivity.class);
            intent.putExtra("chatId", chat.getId());
            intent.putExtra("businessName", chat.getBusiness().getBusinessName());
            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    static class InboxViewHolder extends RecyclerView.ViewHolder {
        TextView txtSenderName, txtServiceName, txtMessageTime;

        InboxViewHolder(View itemView) {
            super(itemView);
            txtSenderName = itemView.findViewById(R.id.txtSenderName);
            txtServiceName = itemView.findViewById(R.id.txtServiceName);
            txtMessageTime = itemView.findViewById(R.id.txtMessageTime);
        }
    }
}
