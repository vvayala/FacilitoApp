package com.example.facilitoapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.facilitoapp.models.user.UpdateProfileRequest;
import com.example.facilitoapp.models.user.UpdateProfileResponse;
import com.example.facilitoapp.network.ApiClient;
import com.example.facilitoapp.network.services.UserApiService;
import com.example.facilitoapp.utils.LoadingDialog;
import com.example.facilitoapp.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfil extends AppCompatActivity {

    private TextInputEditText edtNombre, edtApellidos, edtDui, edtTelefono, edtDireccion;
    private Button btnEditarGuardar;
    private LoadingDialog loadingDialog;
    private boolean isEditing = false;
    private boolean hasUnsavedChanges = false;
    private boolean suppressDirtyCheck = false;
    private String baselineNombre, baselineApellidos, baselineDui, baselineTelefono, baselineDireccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setNavigationBarColor(android.graphics.Color.TRANSPARENT);
        setContentView(R.layout.activity_editar_perfil);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        loadProfileFromIntent();
        setEditingEnabled(false);

        setupDirtyWatcher();
        setupListeners();
    }

    private void initViews() {
        edtNombre      = findViewById(R.id.edtNombre);
        edtApellidos   = findViewById(R.id.edtApellidos);
        edtDui         = findViewById(R.id.edtDui);
        edtTelefono    = findViewById(R.id.edtTelefono);
        edtDireccion   = findViewById(R.id.edtDireccion);
        btnEditarGuardar = findViewById(R.id.btnEditarGuardar);
        loadingDialog  = new LoadingDialog(this);
    }

    private void loadProfileFromIntent() {
        suppressDirtyCheck = true;
        Intent intent = getIntent();
        edtNombre.setText(intent.getStringExtra("name"));
        edtApellidos.setText(intent.getStringExtra("lastname"));
        edtDui.setText(intent.getStringExtra("dui"));
        edtTelefono.setText(intent.getStringExtra("telephone"));
        edtDireccion.setText(intent.getStringExtra("address"));
        suppressDirtyCheck = false;
    }

    private void setupDirtyWatcher() {
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { updateDirtyState(); }
        };
        edtNombre.addTextChangedListener(watcher);
        edtApellidos.addTextChangedListener(watcher);
        edtDui.addTextChangedListener(watcher);
        edtTelefono.addTextChangedListener(watcher);
        edtDireccion.addTextChangedListener(watcher);
    }

    private void setupListeners() {
        // Botón back en el header
        findViewById(R.id.btnBack).setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        btnEditarGuardar.setOnClickListener(v -> {
            if (!isEditing) {
                enterEditMode();
                return;
            }
            if (!hasChanges()) {
                exitEditMode();
                return;
            }
            if (!validateFields()) return;
            saveProfile();
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!hasUnsavedChanges) { finish(); return; }
                showDiscardChangesDialog(EditarPerfil.this::finish);
            }
        });
    }

    private void enterEditMode() {
        isEditing = true;
        baselineNombre    = getText(edtNombre);
        baselineApellidos = getText(edtApellidos);
        baselineDui       = getText(edtDui);
        baselineTelefono  = getText(edtTelefono);
        baselineDireccion = getText(edtDireccion);
        setEditingEnabled(true);
        btnEditarGuardar.setText("Guardar");
        btnEditarGuardar.setBackgroundTintList(
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.deep_purple))
        );
    }

    private void exitEditMode() {
        isEditing = false;
        hasUnsavedChanges = false;
        setEditingEnabled(false);
        btnEditarGuardar.setText("Editar");
        btnEditarGuardar.setBackgroundTintList(
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary_orange))
        );
    }

    private void setEditingEnabled(boolean enabled) {
        int bgColor = enabled ? R.color.white : R.color.surface_gray;
        setFieldEditable(edtNombre, enabled, bgColor);
        setFieldEditable(edtApellidos, enabled, bgColor);
        setFieldEditable(edtDui, enabled, bgColor);
        setFieldEditable(edtTelefono, enabled, bgColor);
        setFieldEditable(edtDireccion, enabled, bgColor);
    }

    private void setFieldEditable(TextInputEditText field, boolean editable, int bgColorRes) {
        field.setEnabled(editable);
        field.setFocusable(editable);
        field.setFocusableInTouchMode(editable);
        field.setCursorVisible(editable);
        // Cambiar el fondo del TextInputLayout padre para feedback visual
        if (field.getParent() != null && field.getParent().getParent() instanceof TextInputLayout) {
            TextInputLayout layout = (TextInputLayout) field.getParent().getParent();
            layout.setBoxBackgroundColor(ContextCompat.getColor(this, bgColorRes));
        }
    }

    private void updateDirtyState() {
        if (!suppressDirtyCheck) hasUnsavedChanges = isEditing && hasChanges();
    }

    private boolean hasChanges() {
        return !getText(edtNombre).equals(baselineNombre)
                || !getText(edtApellidos).equals(baselineApellidos)
                || !getText(edtDui).equals(baselineDui)
                || !getText(edtTelefono).equals(baselineTelefono)
                || !getText(edtDireccion).equals(baselineDireccion);
    }

    private boolean validateFields() {
        boolean ok = true;
        if (getText(edtNombre).trim().isEmpty()) {
            edtNombre.setError("Required"); ok = false;
        }
        if (getText(edtApellidos).trim().isEmpty()) {
            edtApellidos.setError("Required"); ok = false;
        }
        if (!getText(edtDui).trim().matches("\\d{8}-\\d")) {
            edtDui.setError("Format: 12345678-9"); ok = false;
        }
        if (!getText(edtTelefono).trim().matches("\\d{4}-\\d{4}")) {
            edtTelefono.setError("Format: 1234-5678"); ok = false;
        }
        if (getText(edtDireccion).trim().isEmpty()) {
            edtDireccion.setError("Required"); ok = false;
        }
        return ok;
    }

    private void saveProfile() {
        String userId = new SessionManager(this).getUserId();

        UpdateProfileRequest request = new UpdateProfileRequest(
                getText(edtNombre).trim(),
                getText(edtApellidos).trim(),
                getText(edtDui).trim(),
                getText(edtTelefono).trim(),
                getText(edtDireccion).trim()
        );

        loadingDialog.show("Saving changes...");
        btnEditarGuardar.setEnabled(false);

        ApiClient.getClient()
                .create(UserApiService.class)
                .updateUserProfile(userId, request)
                .enqueue(new Callback<UpdateProfileResponse>() {
                    @Override
                    public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                        loadingDialog.dismiss();
                        btnEditarGuardar.setEnabled(true);

                        if (!response.isSuccessful() || response.body() == null) {
                            Toast.makeText(EditarPerfil.this, "Error al guardar el perfil", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(EditarPerfil.this, response.body().message(), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                        loadingDialog.dismiss();
                        btnEditarGuardar.setEnabled(true);
                        Toast.makeText(EditarPerfil.this, "Connection error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showDiscardChangesDialog(Runnable onDiscard) {
        new AlertDialog.Builder(this)
                .setMessage("Tienes cambios sin guardar. ¿Deseas salir sin guardar?")
                .setNegativeButton("Cancelar", (d, w) -> d.dismiss())
                .setPositiveButton("Descartar", (d, w) -> onDiscard.run())
                .show();
    }

    private static String getText(TextInputEditText field) {
        return field.getText() == null ? "" : field.getText().toString();
    }
}