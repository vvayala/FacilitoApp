package com.example.facilitoapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.facilitoapp.MainScreen;
import com.example.facilitoapp.MyRequests;
import com.example.facilitoapp.R;

public class HomeFragment extends Fragment {
    private Button btnMisSolicitudes;
    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        setListeners();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initViews(View view) {
        btnMisSolicitudes = view.findViewById(R.id.btnMisSolicitudes);
    }

    private void setListeners() {
        btnMisSolicitudes.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new MyRequestsFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }
}