package com.example.facilitoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.example.facilitoapp.models.user.UserProfileResponse;
import com.example.facilitoapp.network.ApiClient;
import com.example.facilitoapp.network.services.UserApiService;
import com.example.facilitoapp.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfil extends AppCompatActivity {
    private static final String PREFS_NAME = "facilito_profile";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_APELLIDOS = "apellidos";
    private static final String KEY_DUI = "dui";
    private static final String KEY_TELEFONO = "telefono";
    private static final String KEY_DIRECCION = "direccion";

    private SharedPreferences prefs;
    private EditText edtNombre, edtApellidos, edtDui, edtTelefono, edtDireccion;
    private Button btnEditarGuardar;
    private boolean isEditing = false;
    private boolean hasUnsavedChanges = false;
    private String baselineNombre, baselineApellidos, baselineDui, baselineTelefono, baselineDireccion;
    private boolean suppressDirtyCheck = false;
    private FrameLayout loaderOverlay;

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
        btnEditarGuardar.setText("Editar");

        TextWatcher dirtyWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateDirtyState();
            }
        };

        edtNombre.addTextChangedListener(dirtyWatcher);
        edtApellidos.addTextChangedListener(dirtyWatcher);
        edtDui.addTextChangedListener(dirtyWatcher);
        edtTelefono.addTextChangedListener(dirtyWatcher);
        edtDireccion.addTextChangedListener(dirtyWatcher);

        btnEditarGuardar.setOnClickListener(v -> {
            if (!isEditing) {
                enterEditMode();
                return;
            }

            if (!hasChanges()) {
                exitEditMode();
                return;
            }

            if (!validateFields()) {
                return;
            }

            saveProfile();
        });


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!hasUnsavedChanges) {
                    finish();
                    return;
                }

                showDiscardChangesDialog(EditarPerfil.this::finish);
            }
        });
    }

    private void initViews() {
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        edtNombre = findViewById(R.id.edtNombre);
        edtApellidos = findViewById(R.id.edtApellidos);
        edtDui = findViewById(R.id.edtDui);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtDireccion = findViewById(R.id.edtDireccion);

        btnEditarGuardar = findViewById(R.id.btnEditarGuardar);
        loaderOverlay = findViewById(R.id.loaderOverlay);
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

    private void enterEditMode() {
        isEditing = true;
        baselineNombre = getText(edtNombre);
        baselineApellidos = getText(edtApellidos);
        baselineDui = getText(edtDui);
        baselineTelefono = getText(edtTelefono);
        baselineDireccion = getText(edtDireccion);

        setEditingEnabled(true);
        btnEditarGuardar.setText("Guardar");
        updateDirtyState();
    }

    private void exitEditMode() {
        isEditing = false;
        hasUnsavedChanges = false;
        setEditingEnabled(false);
        btnEditarGuardar.setText("Editar");
    }

    private void setEditingEnabled(boolean enabled) {
        setFieldEditable(edtNombre, enabled);
        setFieldEditable(edtApellidos, enabled);
        setFieldEditable(edtDui, enabled);
        setFieldEditable(edtTelefono, enabled);
        setFieldEditable(edtDireccion, enabled);
    }

    private void setFieldEditable(EditText field, boolean editable) {
        field.setEnabled(editable);
        field.setFocusable(editable);
        field.setFocusableInTouchMode(editable);
        field.setCursorVisible(editable);

        int color = editable
                ? ContextCompat.getColor(this, android.R.color.black)
                : ContextCompat.getColor(this, android.R.color.darker_gray);
        field.setTextColor(color);
    }

    private void updateDirtyState() {
        if (suppressDirtyCheck) {
            return;
        }
        hasUnsavedChanges = isEditing && hasChanges();
    }

    private boolean hasChanges() {
        if (!isEditing) {
            return false;
        }

        return !getText(edtNombre).equals(baselineNombre)
                || !getText(edtApellidos).equals(baselineApellidos)
                || !getText(edtDui).equals(baselineDui)
                || !getText(edtTelefono).equals(baselineTelefono)
                || !getText(edtDireccion).equals(baselineDireccion);
    }

    private boolean validateFields() {
        clearErrors();

        boolean ok = true;

        String nombre = getText(edtNombre).trim();
        String apellidos = getText(edtApellidos).trim();
        String dui = getText(edtDui).trim();
        String telefono = getText(edtTelefono).trim();
        String direccion = getText(edtDireccion).trim();

        if (nombre.isEmpty()) {
            edtNombre.setError("Required");
            ok = false;
        }

        if (apellidos.isEmpty()) {
            edtApellidos.setError("Required");
            ok = false;
        }

        if (!dui.matches("\\d{8}-\\d")) {
            edtDui.setError("Use format 12345678-9");
            ok = false;
        }

        if (!telefono.matches("\\d{4}-\\d{4}")) {
            edtTelefono.setError("Use format 1234-5678");
            ok = false;
        }

        if (direccion.isEmpty()) {
            edtDireccion.setError("Required");
            ok = false;
        }

        return ok;
    }

    private void clearErrors() {
        edtNombre.setError(null);
        edtApellidos.setError(null);
        edtDui.setError(null);
        edtTelefono.setError(null);
        edtDireccion.setError(null);
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

        showLoader();
        btnEditarGuardar.setEnabled(false);

        UserApiService userService = ApiClient.getClient().create(UserApiService.class);

        userService.updateUserProfile(userId, request)
                .enqueue(new Callback<UpdateProfileResponse>() {
                    @Override
                    public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                        hideLoader();
                        btnEditarGuardar.setEnabled(true);

                        if (!response.isSuccessful() || response.body() == null) {
                            Toast.makeText(EditarPerfil.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(EditarPerfil.this, response.body().message(), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                        hideLoader();
                        btnEditarGuardar.setEnabled(true);
                        Toast.makeText(EditarPerfil.this, "Sin conexión", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoader() {
        hideLoader();
        loaderOverlay.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        loaderOverlay.setVisibility(View.GONE);
    }

    private void navigateWithConfirm(Intent intent) {
        if (!hasUnsavedChanges) {
            startActivity(intent);
            return;
        }

        showDiscardChangesDialog(() -> startActivity(intent));
    }

    private void showDiscardChangesDialog(Runnable onDiscard) {
        new AlertDialog.Builder(this)
                .setMessage("You have unsaved changes. Discard them?")
                .setNegativeButton("Cancel", (d, w) -> d.dismiss())
                .setPositiveButton("Discard", (d, w) -> onDiscard.run())
                .show();
    }

    private void wireFooterNavigationWithConfirm() {
        ImageView navHome = findViewById(R.id.navHome);
        if (navHome != null) {
            navHome.setOnClickListener(v -> navigateWithConfirm(new Intent(this, MainScreen.class)));
        }

        ImageView navChat = findViewById(R.id.navChat);
        if (navChat != null) {
            navChat.setOnClickListener(v -> navigateWithConfirm(new Intent(this, ChatsActivity.class)));
        }
    }

    private static String getText(EditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString();
    }
}