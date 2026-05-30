package com.example.facilitoapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facilitoapp.R;
import com.example.facilitoapp.models.business.Business;
import com.example.facilitoapp.models.service.Service;

import java.util.ArrayList;
import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder> {

    public interface OnServiceClickListener {
        void onServiceSelected(Service service);
    }

    private final List<Service> services = new ArrayList<>();
    private final OnServiceClickListener listener;

    public ServicesAdapter(OnServiceClickListener listener) {
        this.listener = listener;
    }

    public void setServices(List<Service> newServices) {
        services.clear();
        services.addAll(newServices);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        holder.bind(services.get(position), listener);
    }

    @Override
    public int getItemCount() { return services.size(); }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtName, txtBusiness, txtDescription;

        ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName        = itemView.findViewById(R.id.txtServiceName);
            txtBusiness    = itemView.findViewById(R.id.txtServiceBusiness);
            txtDescription = itemView.findViewById(R.id.txtServiceDescription);
        }

        void bind(Service service, OnServiceClickListener listener) {
            txtName.setText(service.getServiceName());
            txtDescription.setText(service.getServiceDescription());

            Business business = service.getBusiness();
            if (business != null) {
                txtBusiness.setText(business.getBusinessName());
                txtBusiness.setVisibility(View.VISIBLE);
            } else {
                txtBusiness.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> listener.onServiceSelected(service));
        }
    }
}
