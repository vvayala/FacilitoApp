package com.example.facilitoapp.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.facilitoapp.R;
import com.example.facilitoapp.models.register.RegisterViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class BusinessDataFragment extends Fragment {
    private RegisterViewModel viewModel;
    private TextInputEditText txtBusinessName, txtBusinessDescription;
    private MaterialButton btnNext;
    private CardView cardImagePicker;
    private ImageView imgBusinessPicture;
    private LinearLayout layoutPlaceholder;

    private Uri selectedImageUri = null;

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    imgBusinessPicture.setImageURI(uri);
                    imgBusinessPicture.setVisibility(View.VISIBLE);
                    layoutPlaceholder.setVisibility(View.GONE);
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_data, container, false);

        viewModel              = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);
        txtBusinessName        = view.findViewById(R.id.txtBusinessName);
        txtBusinessDescription = view.findViewById(R.id.txtBusinessDescription);
        btnNext                = view.findViewById(R.id.btnNext);
        cardImagePicker        = view.findViewById(R.id.cardImagePicker);
        imgBusinessPicture     = view.findViewById(R.id.imgBusinessPicture);
        layoutPlaceholder      = view.findViewById(R.id.layoutPlaceholder);

        cardImagePicker.setOnClickListener(v ->
                imagePickerLauncher.launch("image/*")
        );

        btnNext.setOnClickListener(v -> {
            String businessName = txtBusinessName.getText().toString().trim();
            String businessDesc = txtBusinessDescription.getText().toString().trim();

            if (businessName.isEmpty()) {
                Toast.makeText(getContext(), "Please enter your business name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Guardar en ViewModel
            viewModel.businessName.setValue(businessName);
            viewModel.businessDescription.setValue(businessDesc);
            viewModel.businessImageUri.setValue(selectedImageUri);

            // Navegar al último paso
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AccountCredentialsFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}