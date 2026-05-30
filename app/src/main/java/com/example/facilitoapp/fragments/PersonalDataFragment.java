package com.example.facilitoapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.facilitoapp.R;
import com.example.facilitoapp.models.register.RegisterViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class PersonalDataFragment extends Fragment {

    private RegisterViewModel viewModel;

    private TextInputEditText txtNombre, txtApellido, txtTelefono, txtDui;
    private Button btnNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_data, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);

        txtNombre = view.findViewById(R.id.txtNombre);
        txtApellido = view.findViewById(R.id.txtApellido);
        txtTelefono = view.findViewById(R.id.txtTelefono);
        txtDui = view.findViewById(R.id.txtDui);
        btnNext = view.findViewById(R.id.btnNext);

        btnNext.setOnClickListener(v -> {
            String name = txtNombre.getText().toString().trim();
            String lastName = txtApellido.getText().toString().trim();

            if (name.isEmpty() || lastName.isEmpty()) {
                Toast.makeText(getContext(), "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.name.setValue(name);
            viewModel.lastName.setValue(lastName);
            viewModel.telephone.setValue(txtTelefono.getText().toString());
            viewModel.dui.setValue(txtDui.getText().toString());

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AccountTypeFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}