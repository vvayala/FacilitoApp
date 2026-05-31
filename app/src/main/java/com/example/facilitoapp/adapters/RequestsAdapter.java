package com.example.facilitoapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facilitoapp.R;
import com.example.facilitoapp.models.service.ServiceRequestDetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestViewHolder> {

    public interface OnRequestClickListener {
        void onViewDetails(ServiceRequestDetail request);
    }

    private final List<ServiceRequestDetail> requests = new ArrayList<>();
    private final OnRequestClickListener listener;

    public RequestsAdapter(OnRequestClickListener listener) {
        this.listener = listener;
    }

    public void setRequests(List<ServiceRequestDetail> newRequests) {
        requests.clear();
        requests.addAll(newRequests);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        holder.bind(requests.get(position), listener);
    }

    @Override
    public int getItemCount() { return requests.size(); }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtServiceName;
        private final TextView txtBusinessName;
        private final TextView txtDescription;
        private final TextView txtDate;
        private final Button btnViewDetails;

        RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            txtServiceName  = itemView.findViewById(R.id.txtRequestServiceName);
            txtBusinessName = itemView.findViewById(R.id.txtRequestBusinessName);
            txtDescription  = itemView.findViewById(R.id.txtRequestDescription);
            txtDate         = itemView.findViewById(R.id.txtRequestDate);
            btnViewDetails  = itemView.findViewById(R.id.btnViewDetails);
        }

        void bind(ServiceRequestDetail request, OnRequestClickListener listener) {
            if (request.getServiceId() != null) {
                txtServiceName.setText(request.getServiceId().getServiceName());

                if (request.getServiceId().getBusiness() != null) {
                    txtBusinessName.setText(request.getServiceId().getBusiness().getBusinessName());
                    txtBusinessName.setVisibility(View.VISIBLE);
                } else {
                    txtBusinessName.setVisibility(View.GONE);
                }
            }

            txtDescription.setText(request.getServiceRequestDescription());

            if (request.getServiceRequestDate() != null) {
                txtDate.setText(formatDate(request.getServiceRequestDate()));
            } else {
                txtDate.setVisibility(View.GONE);
            }

            btnViewDetails.setOnClickListener(v -> listener.onViewDetails(request));
        }

        private String formatDate(String rawDate) {
            try {
                SimpleDateFormat input  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                SimpleDateFormat output = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                Date date = input.parse(rawDate);
                return date != null ? output.format(date) : rawDate;
            } catch (ParseException e) {
                return rawDate;
            }
        }
    }
}