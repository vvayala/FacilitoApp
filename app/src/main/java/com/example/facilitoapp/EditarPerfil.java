package com.example.facilitoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
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

public class EditarPerfil extends AppCompatActivity {

    private static final String PREFS_NAME = "facilito_profile";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_APELLIDOS = "apellidos";
    private static final String KEY_DUI = "dui";
    private static final String KEY_TELEFONO = "telefono";
    private static final String KEY_DIRECCION = "direccion";

    private SharedPreferences prefs;

    private EditText edtNombre;
    private EditText edtApellidos;
    private EditText edtDui;
    private EditText edtTelefono;
    private EditText edtDireccion;

    private Button btnEditarGuardar;

    private boolean isEditing = false;
    private boolean hasUnsavedChanges = false;

    private String baselineNombre;
    private String baselineApellidos;
    private String baselineDui;
    private String baselineTelefono;
    private String baselineDireccion;

    private boolean suppressDirtyCheck = false;

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

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        edtNombre = findViewById(R.id.edtNombre);
        edtApellidos = findViewById(R.id.edtApellidos);
        edtDui = findViewById(R.id.edtDui);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtDireccion = findViewById(R.id.edtDireccion);

        btnEditarGuardar = findViewById(R.id.btnEditarGuardar);

        loadProfileFromPrefs();
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

            saveToPrefs();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            exitEditMode();
        });

        ImageView imgHeaderUser = findViewById(R.id.imgHeaderUser);
        if (imgHeaderUser != null) {
            imgHeaderUser.setOnClickListener(v -> navigateWithConfirm(new Intent(this, VerPerfil.class)));
        }

        ImageView imgHeaderNotification = findViewById(R.id.imgHeaderNotification);
        if (imgHeaderNotification != null) {
            imgHeaderNotification.setOnClickListener(v -> navigateWithConfirm(new Intent(this, Notificaciones.class)));
        }

        FooterNavigationHelper.setupHomeNavigation(this);
        wireFooterNavigationWithConfirm();

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

    private void loadProfileFromPrefs() {
        suppressDirtyCheck = true;
        edtNombre.setText(prefs.getString(KEY_NOMBRE, "John"));
        edtApellidos.setText(prefs.getString(KEY_APELLIDOS, "Doe"));
        edtDui.setText(prefs.getString(KEY_DUI, "01234567-8"));
        edtTelefono.setText(prefs.getString(KEY_TELEFONO, "7123-4567"));
        edtDireccion.setText(prefs.getString(KEY_DIRECCION, "San Salvador, San Salvador"));
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

    private void saveToPrefs() {
        prefs.edit()
                .putString(KEY_NOMBRE, getText(edtNombre).trim())
                .putString(KEY_APELLIDOS, getText(edtApellidos).trim())
                .putString(KEY_DUI, getText(edtDui).trim())
                .putString(KEY_TELEFONO, getText(edtTelefono).trim())
                .putString(KEY_DIRECCION, getText(edtDireccion).trim())
                .apply();

        baselineNombre = getText(edtNombre);
        baselineApellidos = getText(edtApellidos);
        baselineDui = getText(edtDui);
        baselineTelefono = getText(edtTelefono);
        baselineDireccion = getText(edtDireccion);

        updateDirtyState();
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
