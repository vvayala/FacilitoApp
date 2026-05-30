package com.example.facilitoapp.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.facilitoapp.R;
import com.example.facilitoapp.models.register.RegisterViewModel;

public class AccountTypeFragment extends Fragment {

    private RegisterViewModel viewModel;
    private CardView cardClient, cardProvider;
    private ImageView checkClient, checkProvider;
    private Button btnNext;

    private String selectedType = ""; // "CLIENT" or "PROVIDER"

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_type, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);

        cardClient   = view.findViewById(R.id.cardClient);
        cardProvider = view.findViewById(R.id.cardProvider);
        checkClient  = view.findViewById(R.id.checkClient);
        checkProvider= view.findViewById(R.id.checkProvider);
        btnNext      = view.findViewById(R.id.btnNext);

        cardClient.setOnClickListener(v -> selectType("CLIENT"));
        cardProvider.setOnClickListener(v -> selectType("PROVIDER"));

        btnNext.setOnClickListener(v -> {
            viewModel.accountType.setValue(selectedType);

            Fragment nextFragment = selectedType.equals("PROVIDER")
                    ? new BusinessDataFragment()
                    : new AccountCredentialsFragment();

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, nextFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void selectType(String type) {
        selectedType = type;

        // Reset ambas cards
        cardClient.setCardBackgroundColor(Color.parseColor("#F8F4FF"));
        cardProvider.setCardBackgroundColor(Color.parseColor("#FFF4EC"));
        checkClient.setVisibility(View.GONE);
        checkProvider.setVisibility(View.GONE);

        // Resaltar la seleccionada
        if (type.equals("CLIENT")) {
            cardClient.setCardBackgroundColor(Color.parseColor("#EDE7F6"));
            cardClient.setCardElevation(8f);
            checkClient.setVisibility(View.VISIBLE);
        } else {
            cardProvider.setCardBackgroundColor(Color.parseColor("#FFE0C4"));
            cardProvider.setCardElevation(8f);
            checkProvider.setVisibility(View.VISIBLE);
        }

        btnNext.setEnabled(true);
        btnNext.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F68031")));
    }
}