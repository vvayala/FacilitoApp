package com.example.facilitoapp.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.facilitoapp.R;
import com.example.facilitoapp.models.business.Business;
import com.example.facilitoapp.models.service.Service;
import com.example.facilitoapp.models.service.ServiceResponse;
import com.example.facilitoapp.models.user.User;
import com.example.facilitoapp.network.ApiClient;
import com.example.facilitoapp.network.services.ServicesApiService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProvidersMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_providers_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        geocoder = new Geocoder(requireContext(), Locale.getDefault());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        
        // Posición inicial (El Salvador por defecto)
        LatLng elSalvador = new LatLng(13.6929, -89.2182);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(elSalvador, 8));

        loadServiceMarkers();
    }

    private void loadServiceMarkers() {
        ServicesApiService api = ApiClient.getClient().create(ServicesApiService.class);
        api.getAllServices().enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServiceResponse> call, @NonNull Response<ServiceResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getServices() != null) {
                    for (Service service : response.body().getServices()) {
                        Business business = service.getBusiness();
                        if (business != null) {
                            User user = business.getUser();
                            if (user != null && user.getAddress() != null && !user.getAddress().isEmpty()) {
                                addMarkerForAddress(user.getAddress(), service.getServiceName(), business.getBusinessName());
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServiceResponse> call, @NonNull Throwable t) {
                Log.e("MapFragment", "Error loading services", t);
            }
        });
    }

    private void addMarkerForAddress(String addressStr, String serviceName, String businessName) {
        try {
            // El Geocoder es síncrono, para muchos puntos lo ideal sería un hilo separado, 
            // pero para esta implementación rápida funcionará si no son demasiados.
            List<Address> addresses = geocoder.getFromLocationName(addressStr + ", El Salvador", 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                
                String markerTitle = serviceName;
                String markerSnippet = businessName + " - " + addressStr;

                requireActivity().runOnUiThread(() -> {
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(markerTitle)
                            .snippet(markerSnippet));
                });
            }
        } catch (IOException e) {
            Log.e("MapFragment", "Geocoding error for: " + addressStr, e);
        }
    }
}
