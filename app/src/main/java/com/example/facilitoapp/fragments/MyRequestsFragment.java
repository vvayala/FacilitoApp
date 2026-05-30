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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.facilitoapp.ChatConversationActivity;
import com.example.facilitoapp.R;
import com.example.facilitoapp.RequestDetailActivity;
import com.example.facilitoapp.adapters.RequestsAdapter;
import com.example.facilitoapp.models.business.Business;
import com.example.facilitoapp.models.chats.Chat;
import com.example.facilitoapp.models.chats.CreateChatBody;
import com.example.facilitoapp.models.chats.CreateChatResponse;
import com.example.facilitoapp.models.chats.SendChat;
import com.example.facilitoapp.models.chats.SentChat;
import com.example.facilitoapp.models.service.ServiceRequestDetail;
import com.example.facilitoapp.models.service.ServiceRequestResponse;
import com.example.facilitoapp.network.ApiClient;
import com.example.facilitoapp.network.services.ChatsApiService;
import com.example.facilitoapp.network.services.ServiceRequestsApiService;
import com.example.facilitoapp.utils.NewRequestBottomSheet;
import com.example.facilitoapp.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//public interface OnRequestClickListener {
//    void onViewDetails(ServiceRequestDetail request);
//}

public class MyRequestsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RequestsAdapter adapter;
    private LinearLayout emptyState;
    private ProgressBar loadingIndicator;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        loadRequests();
    }

    private void initViews(View view) {
        recyclerView     = view.findViewById(R.id.recyclerRequests);
        emptyState       = view.findViewById(R.id.emptyState);
        loadingIndicator = view.findViewById(R.id.loadingIndicator);

        adapter = new RequestsAdapter(this::onViewDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabNewRequest = view.findViewById(R.id.fabNewRequest);
        Button btnNewRequestEmpty          = view.findViewById(R.id.btnNewRequestEmpty);

        fabNewRequest.setOnClickListener(v -> openNewRequestSheet());
        btnNewRequestEmpty.setOnClickListener(v -> openNewRequestSheet());
    }

    private void loadRequests() {
        String customerId = new SessionManager(requireContext()).getUserId();
        if (customerId == null) return;

        showLoading(true);

        ApiClient.getClient()
                .create(ServiceRequestsApiService.class)
                .getRequestsByCustomer(customerId)
                .enqueue(new Callback<ServiceRequestResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ServiceRequestResponse> call,
                                           @NonNull Response<ServiceRequestResponse> response) {
                        if (!isAdded()) return;
                        showLoading(false);

                        if (!response.isSuccessful() || response.body() == null || !response.body().isOk()) {
                            showEmpty(true);
                            return;
                        }

                        List<ServiceRequestDetail> list = response.body().getRequests();
                        if (list == null || list.isEmpty()) {
                            showEmpty(true);
                        } else {
                            showEmpty(false);
                            adapter.setRequests(list);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ServiceRequestResponse> call,
                                          @NonNull Throwable t) {
                        if (!isAdded()) return;
                        showLoading(false);
                        showEmpty(true);
                    }
                });
    }

    private void openNewRequestSheet() {
        String customerId = new SessionManager(requireContext()).getUserId();

        NewRequestBottomSheet sheet = NewRequestBottomSheet.newInstance(customerId);
        sheet.setOnRequestCreatedListener(request -> {
            loadRequests();
            createChatForRequest(request);
        });
        sheet.show(getChildFragmentManager(), "new_request");
    }

    private void createChatForRequest(ServiceRequestDetail request) {
        // Obtén el business_id del service_id del request
        String serviceRequestId = request.getId();
        String businessId = null;

        if (request.getServiceId() != null) {
            businessId = request.getServiceId().getBusinessId(); // String del POST
        }

        if (businessId == null) {
            Toast.makeText(requireContext(), "Could not determine business", Toast.LENGTH_SHORT).show();
            return;
        }

        final String finalBusinessId = businessId;

        CreateChatBody body = new CreateChatBody(serviceRequestId, finalBusinessId);

        ApiClient.getClient()
                .create(ChatsApiService.class)
                .createChat(body)
                .enqueue(new Callback<CreateChatResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CreateChatResponse> call,
                                           @NonNull Response<CreateChatResponse> response) {
                        if (!isAdded()) return;
                        if (!response.isSuccessful() || response.body() == null || !response.body().isOk()) {
                            Toast.makeText(requireContext(), "Error creating chat", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Chat chat = response.body().getChat();
                        sendFirstMessage(chat.getId(),
                                request.getServiceRequestDescription(),
                                chat.getBusiness());
                    }

                    @Override
                    public void onFailure(@NonNull Call<CreateChatResponse> call, @NonNull Throwable t) {
                        if (!isAdded()) return;
                        Toast.makeText(requireContext(), "Connection error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendFirstMessage(String chatId, String description, Business business) {
        String senderId = new SessionManager(requireContext()).getUserId();

        SendChat firstMessage = new SendChat(description, senderId, chatId);

        ApiClient.getClient()
                .create(ChatsApiService.class)
                .sendChat(firstMessage)
                .enqueue(new Callback<SentChat>() {
                    @Override
                    public void onResponse(@NonNull Call<SentChat> call,
                                           @NonNull Response<SentChat> response) {
                        if (!isAdded()) return;

                        // Abre el chat
                        String businessName = business != null ? business.getBusinessName() : "Chat";
                        Intent intent = new Intent(requireContext(), ChatConversationActivity.class);
                        intent.putExtra("chatId", chatId);
                        intent.putExtra("businessName", businessName);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(@NonNull Call<SentChat> call, @NonNull Throwable t) {
                        if (!isAdded()) return;
                        // El chat se creó igual, abre sin el primer mensaje
                        String businessName = business != null ? business.getBusinessName() : "Chat";
                        Intent intent = new Intent(requireContext(), ChatConversationActivity.class);
                        intent.putExtra("chatId", chatId);
                        intent.putExtra("businessName", businessName);
                        startActivity(intent);
                    }
                });
    }

    private void onViewDetails(ServiceRequestDetail request) {
        String requestJson = new Gson().toJson(request);
        Intent intent = new Intent(requireContext(), RequestDetailActivity.class);
        intent.putExtra("request_json", requestJson);
        startActivity(intent);
    }

    private void showLoading(boolean show) {
        loadingIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        emptyState.setVisibility(View.GONE);
    }

    private void showEmpty(boolean show) {
        emptyState.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}